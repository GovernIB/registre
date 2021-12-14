package es.caib.regweb3.plugins.distribucion.goib;


import es.caib.distribucio.ws.v1.bustia.*;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NCommonUtils;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.util.*;

/**
 * Plugin para distribuir registros a DISTRIBUCIÓ
 *
 * @author mgonzalez
 */
public class DistribucionGoibPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());

    private static final String basePluginDistribucionGoib = DISTRIBUCION_BASE_PROPERTY + "goib.";
    private static final String PROPERTY_USUARIO = basePluginDistribucionGoib + "usuario";
    private static final String PROPERTY_PASSWORD = basePluginDistribucionGoib + "password";
    private static final String PROPERTY_ENDPOINT = basePluginDistribucionGoib + "endpoint";


    /**
     *
     */
    public DistribucionGoibPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionGoibPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionGoibPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    @Override
    public Boolean distribuir(RegistroEntrada registro, Locale locale) throws Exception {

        try {

            //Transformamos a registreAnotació.
            RegistreAnotacio registreAnotacio = getRegistreAnotacio(registro, locale);

            // Interesados
            for (Interesado interesado : registro.getRegistroDetalle().getInteresados()) {
                if (!interesado.getIsRepresentante()) {
                    RegistreInteressat registreInteressat = getRegistreInteressat(interesado);
                    if (interesado.getRepresentante() != null) {
                        registreInteressat.setRepresentant(getRegistreInteressat(interesado.getRepresentante()));
                    }
                    registreAnotacio.getInteressats().add(registreInteressat);
                }
            }

            // Anexos
            for (AnexoFull anexoFull : registro.getRegistroDetalle().getAnexosFull()) {
                // No distribuimos los Anexos confidenciales, ni el justificnte
                if (!anexoFull.getAnexo().getConfidencial() && !anexoFull.getAnexo().isJustificante()) {
                    registreAnotacio.getAnnexos().add(getRegistreAnnex(anexoFull));
                }
            }

            // Justificante (Solo se envía el custodyID)
            registreAnotacio.setJustificant(getRegistreAnnexJustificante(registro.getRegistroDetalle().getJustificanteAnexoFull().getAnexo()));

            //Obtenemos la entidad y la unidad Administrativa a donde distribuir el registro
            String entidadCodigo = registro.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3();
            String unidadAdministrativaCodigo = registro.getDestino().getCodigo();

            //Método donde se invoca al ws de DISTRIBUCIÓ para enviar el registro a la bustica correspondiente
            BustiaV1 client = es.caib.distribucio.ws.client.BustiaV1WsClientFactory.getWsClient(getPropertyEndPoint(), getPropertyUsuario(), getPropertyPassword());

            // Le especificamos un timeout mayor que el habitual (1 minuto)
            setTimeoutWSCall(client, 300000);

            // Enviamos el registro a DISTRIBUCIÓ
            client.enviarAnotacioRegistreEntrada(entidadCodigo, unidadAdministrativaCodigo, registreAnotacio);

            return true;

        } catch (Exception e) {
            if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().contains("ja ha estat donada")) {
                log.info("Consideramos que la anotacion : " + registro.getNumeroRegistroFormateado() + "  ya existe y la marcamos como Distribuida");
                return true;
            } else {
                e.printStackTrace();
            }
            throw new Exception(e);
        }

    }


    /**
     * Método que transforma de RegistroEntrada a RegistreAnotacio de DISTRIBUCIÓ.
     *
     * @param re RegistroEntrada
     * @return
     * @throws Exception
     */
    private RegistreAnotacio getRegistreAnotacio(RegistroEntrada re, Locale language) throws Exception {

        RegistreAnotacio registreAnotacio = new RegistreAnotacio();

        //Tipo Anotacio
        registreAnotacio.setTipusES("E");

        //Libro
        registreAnotacio.setLlibreCodi(re.getLibro().getCodigo());
        registreAnotacio.setLlibreDescripcio(re.getLibro().getNombre());

        //Extracto
        registreAnotacio.setExtracte(re.getRegistroDetalle().getExtracto());

        //Tipo Asunto
        //TODO eliminar cuando hayan adaptado modelo de distribució
        registreAnotacio.setAssumpteTipusCodi("01");
        registreAnotacio.setAssumpteTipusDescripcio("");

        //Idioma
        registreAnotacio.setIdiomaCodi(re.getRegistroDetalle().getIdioma().toString());
        registreAnotacio.setIdiomaDescripcio(I18NCommonUtils.tradueix(language, "idioma." + re.getRegistroDetalle().getIdioma()));

        // Documentación Física
        // Es crea dependencia al genapp.
        String tipoDocumentacionFisica = re.getRegistroDetalle().getTipoDocumentacionFisica().toString();
        registreAnotacio.setDocumentacioFisicaCodi(tipoDocumentacionFisica);
        registreAnotacio.setDocumentacioFisicaDescripcio(I18NCommonUtils.tradueix(language, "tipoDocumentacionFisica." + tipoDocumentacionFisica));

        //Codigo Asunto
        if (re.getRegistroDetalle().getCodigoAsunto() != null) {
            registreAnotacio.setAssumpteCodi(re.getRegistroDetalle().getCodigoAsunto().getCodigo());
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) re.getRegistroDetalle().getCodigoAsunto().getTraduccion();
            registreAnotacio.setAssumpteDescripcio(traduccionCodigoAsunto.getNombre());
        }

        //Referencia y expediente
        registreAnotacio.setReferencia(re.getRegistroDetalle().getReferenciaExterna());
        registreAnotacio.setExpedientNumero(re.getRegistroDetalle().getExpediente());

        //Transporte y número
        registreAnotacio.setTransportNumero(re.getRegistroDetalle().getNumeroTransporte());
        if (re.getRegistroDetalle().getTransporte() != null) {
            String codigoTransporte = "0" + re.getRegistroDetalle().getTransporte().toString();
            registreAnotacio.setTransportTipusCodi(codigoTransporte);
            registreAnotacio.setTransportTipusDescripcio(I18NCommonUtils.tradueix(language, "transporte." + codigoTransporte));
        }

        //Observaciones
        registreAnotacio.setObservacions(re.getRegistroDetalle().getObservaciones());

        //Oficina Origen
        if (re.getRegistroDetalle().getOficinaOrigen() != null) {
            registreAnotacio.setOficinaOrigenCodi(re.getRegistroDetalle().getOficinaOrigen().getCodigo());
            registreAnotacio.setOficinaOrigenDescripcio(re.getRegistroDetalle().getOficinaOrigen().getDenominacion());
        } else {
            registreAnotacio.setOficinaOrigenCodi(re.getRegistroDetalle().getOficinaOrigenExternoCodigo());
            registreAnotacio.setOficinaOrigenDescripcio(re.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
        }

        //Numero Registro Origen y Fecha Registro Origen
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(re.getRegistroDetalle().getFechaOrigen());
        XMLGregorianCalendar dateOri = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        registreAnotacio.setDataOrigen(dateOri);
        registreAnotacio.setNumeroOrigen(re.getRegistroDetalle().getNumeroRegistroOrigen());

        //Identificador
        registreAnotacio.setIdentificador(re.getId().toString());

        //Aplicación y versión
        registreAnotacio.setAplicacioCodi(re.getRegistroDetalle().getAplicacion());
        registreAnotacio.setAplicacioVersio(re.getRegistroDetalle().getVersion());

        //Oficina
        registreAnotacio.setOficinaCodi(re.getOficina().getCodigo());
        registreAnotacio.setOficinaDescripcio(re.getOficina().getDenominacion());

        //Entidad
        registreAnotacio.setEntitatCodi(re.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3());
        registreAnotacio.setEntitatDescripcio(re.getOficina().getOrganismoResponsable().getEntidad().getNombre());

        //Fecha
        c.setTime(re.getFecha());
        registreAnotacio.setData(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));

        //Expone y Solicita
        registreAnotacio.setExposa(re.getRegistroDetalle().getExpone());
        registreAnotacio.setSolicita(re.getRegistroDetalle().getSolicita());

        //Numero de registro Formateado
        registreAnotacio.setNumero(re.getNumeroRegistroFormateado());

        //Usuario
        registreAnotacio.setUsuariCodi(re.getUsuario().getId().toString());
        registreAnotacio.setUsuariContacte(re.getUsuario().getUsuario().getEmail());
        registreAnotacio.setUsuariNom(re.getUsuario().getNombreCompleto());

        //Codi SIA
        if (re.getRegistroDetalle().getCodigoSia() != null) {
            registreAnotacio.setProcedimentCodi(re.getRegistroDetalle().getCodigoSia().toString());
        }

        //Presencial
        registreAnotacio.setPresencial(re.getRegistroDetalle().getPresencial());

        return registreAnotacio;

    }

    /**
     * Método que transforma de un {@link es.caib.regweb3.model.Interesado} a {@link es.caib.distribucio.ws.v1.bustia.RegistreInteressat}
     *
     * @param interesado
     * @return
     * @throws Exception
     */
    private RegistreInteressat getRegistreInteressat(Interesado interesado) throws Exception {

        RegistreInteressat registreInteressat = new RegistreInteressat();

        //Nombre y Apellidos
        registreInteressat.setNom(interesado.getNombre());
        registreInteressat.setLlinatge1(interesado.getApellido1());
        registreInteressat.setLlinatge2(interesado.getApellido2());

        //Tipo Documento y Documento
        if (interesado.getTipoDocumentoIdentificacion() != null) {
            registreInteressat.setDocumentTipus(RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion()).toString());
            registreInteressat.setDocumentNum(interesado.getDocumento());
        }

        //Email
        registreInteressat.setEmail(interesado.getEmail());
        registreInteressat.setEmailHabilitat(interesado.getDireccionElectronica());

        //Teléfono
        registreInteressat.setTelefon(interesado.getTelefono());
        //Canal Notificación
        if (interesado.getCanal() != null && interesado.getCanal() != -1) {
            registreInteressat.setCanalPreferent(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
        }

        //Pais
        CatPais pais = interesado.getPais();
        if (pais != null) {
            registreInteressat.setPais(pais.getDescripcionPais());
            registreInteressat.setPaisCodi(pais.getCodigoPais().toString());
        }

        //Provincia
        CatProvincia provincia = interesado.getProvincia();
        if (provincia != null) {
            registreInteressat.setProvincia(provincia.getDescripcionProvincia());
            registreInteressat.setProvinciaCodi(provincia.getCodigoProvincia().toString());
        }

        //Localitat
        CatLocalidad localidad = interesado.getLocalidad();
        if (localidad != null) {
            registreInteressat.setMunicipi(localidad.getNombre());
            registreInteressat.setMunicipiCodi(localidad.getCodigoLocalidad().toString());
        }


        registreInteressat.setAdresa(interesado.getDireccion()); //Dirección
        registreInteressat.setCodiPostal(interesado.getCp()); //Codigo Postal
        registreInteressat.setRaoSocial(interesado.getRazonSocial()); //Razón Social
        registreInteressat.setObservacions(interesado.getObservaciones()); //Observaciones

        //Tipo
        if (interesado.getTipo() != null) {
            registreInteressat.setTipus(interesado.getTipo().toString());
        }

        //CodigoDIRe
        if (StringUtils.isNotEmpty(interesado.getCodigoDire())) {
            registreInteressat.setCodiDire(interesado.getCodigoDire());
        }

        return registreInteressat;
    }

    /**
     * Método que transforma un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.distribucio.ws.v1.bustia.RegistreAnnex}
     *
     * @param anexo
     * @return
     * @throws Exception
     */
    public RegistreAnnex getRegistreAnnexJustificante(Anexo anexo) throws Exception {
        RegistreAnnex registreAnnex = new RegistreAnnex();

        if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)) {
            //PARCHE uuid Anexo, veure si finalment passam numExpediente#numDoc o no
            String id = anexo.getCustodiaID();
            registreAnnex.setFitxerArxiuUuid(id.substring(id.lastIndexOf('#') + 1));

        } else if (anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {
            registreAnnex.setFitxerArxiuUuid(anexo.getCustodiaID());
        }

        return registreAnnex;
    }

    /**
     * Método que transforma un {@link es.caib.regweb3.model.utils.AnexoFull} en un {@link es.caib.distribucio.ws.v1.bustia.RegistreInteressat}
     *
     * @param anexoFull
     * @return
     * @throws Exception
     */
    public RegistreAnnex getRegistreAnnex(AnexoFull anexoFull) throws Exception {

        RegistreAnnex annex = new RegistreAnnex();

        annex.setTitol(anexoFull.getAnexo().getTitulo());
        annex.setEniOrigen(anexoFull.getAnexo().getOrigenCiudadanoAdmin().toString());
        annex.setSicresTipusDocument(RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexoFull.getAnexo().getTipoDocumento()));
        annex.setEniTipusDocumental(anexoFull.getAnexo().getTipoDocumental().getCodigoNTI());
        annex.setEniEstatElaboracio((RegwebConstantes.CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO.get(anexoFull.getAnexo().getValidezDocumento()))); // Validez del Documento(SICRES) = Estado Elaboración (ENI)
        annex.setObservacions(anexoFull.getAnexo().getObservaciones());
        annex.setValidacioOCSP(Base64.encodeBase64String(anexoFull.getAnexo().getValidacionOCSPCertificado()));

        //Metadatos
        List<Metadata> metadadesAnexo = anexoFull.getMetadatas();
        RegistreAnnex.MetaDades metaDades = new RegistreAnnex.MetaDades();

        if (metadadesAnexo != null) {

            for (Metadata metadata : metadadesAnexo) {

                if (anexoFull.getAnexo().getScan()) {

                    if (metadata.getKey().equals(MetadataConstants.EEMGDE_RESOLUCION)) {
                        RegistreAnnex.MetaDades.Entry resolucion = new RegistreAnnex.MetaDades.Entry();
                        resolucion.setKey("eni:resolucion");
                        resolucion.setValue(metadata.getValue());
                        metaDades.getEntry().add(resolucion);
                    }
                    if (metadata.getKey().equals(MetadataConstants.EEMGDE_PROFUNDIDAD_COLOR)) {
                        RegistreAnnex.MetaDades.Entry profundidadColor = new RegistreAnnex.MetaDades.Entry();
                        profundidadColor.setKey("eni:profundidad_color");
                        profundidadColor.setValue(metadata.getValue());
                        metaDades.getEntry().add(profundidadColor);
                    }
                    if (metadata.getKey().equals(MetadataConstants.EEMGDE_IDIOMA)) {
                        RegistreAnnex.MetaDades.Entry idioma = new RegistreAnnex.MetaDades.Entry();
                        idioma.setKey("eni:idioma");
                        idioma.setValue(metadata.getValue());
                        metaDades.getEntry().add(idioma);
                    }
                }

                if (metadata.getKey().equals(MetadataConstants.ENI_DESCRIPCION)) {
                    RegistreAnnex.MetaDades.Entry descripcion = new RegistreAnnex.MetaDades.Entry();
                    descripcion.setKey("eni:descripcion");
                    descripcion.setValue(metadata.getValue());
                    metaDades.getEntry().add(descripcion);
                }

            }

        }
        //Metadata CM:TITLE
        RegistreAnnex.MetaDades.Entry cmtitle = new RegistreAnnex.MetaDades.Entry();
        cmtitle.setKey("cm:title");
        cmtitle.setValue(anexoFull.getAnexo().getTitulo());
        metaDades.getEntry().add(cmtitle);

        //Fecha de Captura
        GregorianCalendar fecha = new GregorianCalendar();
        fecha.setTime(anexoFull.getAnexo().getFechaCaptura());
        annex.setEniDataCaptura(DatatypeFactory.newInstance().newXMLGregorianCalendar(fecha));

        annex.setMetaDades(metaDades);


        //Caso Anexo SIN FIRMA se obtiene de DocumentCustody
        if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

            annex.setFitxerNom(anexoFull.getDocumentoCustody().getName());
            annex.setFitxerTamany((int) anexoFull.getDocumentoCustody().getLength());
            annex.setFitxerTipusMime(anexoFull.getDocumentoCustody().getMime());
            annex.setFitxerContingut(anexoFull.getDocumentoCustody().getData());
        }

        //Caso Anexo FIRMA ATTACHED El anexo se obtiene de  signature custody
        if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
            annex.setFitxerNom(anexoFull.getSignatureCustody().getName());
            annex.setFitxerTamany((int) anexoFull.getSignatureCustody().getLength());
            annex.setFitxerTipusMime(anexoFull.getSignatureCustody().getMime());

            // Si tiene información de la firma creamos el objeto Firma (Los XMLResumenSolicitudENI.xml están firmados, pero no disponemos de información de Firma)
            if(anexoFull.getAnexo().getSignType() != null && anexoFull.getAnexo().getSignProfile() != null){

                Firma firma = new Firma();
                firma.setFitxerNom(anexoFull.getSignatureCustody().getName());
                firma.setTipusMime(anexoFull.getSignatureCustody().getMime());
                firma.setTipus(transformarTipoFirma(anexoFull.getAnexo()));
                firma.setPerfil(transformarPerfilFirma(anexoFull.getAnexo()));
                firma.setContingut(anexoFull.getSignatureCustody().getData());

                annex.getFirmes().add(firma);
            }else{
                annex.setFitxerContingut(anexoFull.getSignatureCustody().getData());
            }
        }

        //Caso Anexo FIRMA DETACHED
        if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

            //En registreAnnex metemos toda la información del documento
            annex.setFitxerNom(anexoFull.getDocumentoCustody().getName());
            annex.setFitxerTamany((int) anexoFull.getDocumentoCustody().getLength());
            annex.setFitxerTipusMime(anexoFull.getDocumentoCustody().getMime());
            annex.setFitxerContingut(anexoFull.getDocumentoCustody().getData());

            //en este caso el objeto Firma también incorpora el contenido de la firma
            if(anexoFull.getAnexo().getSignType()!= null && anexoFull.getAnexo().getSignProfile() != null) {

                Firma firma = new Firma();
                firma.setFitxerNom(anexoFull.getSignatureCustody().getName());
                firma.setTipusMime(anexoFull.getSignatureCustody().getMime());
                //TODO prueba con csv comentado
                firma.setCsv(anexoFull.getAnexo().getCsv());
                firma.setCsvRegulacio("Regulació CSV");
                firma.setContingut(anexoFull.getSignatureCustody().getData());
                firma.setTipus(transformarTipoFirma(anexoFull.getAnexo()));
                firma.setPerfil(transformarPerfilFirma(anexoFull.getAnexo()));

                annex.getFirmes().add(firma);
            }

        }

        return annex;
    }

    /**
     * Transforma el Tipo Firma obtenido tras realizar la Validación de la Firma al código NTI correspondiente
     * La transformación al tipo de Firma que espera DISTRIBUCIÓ se obtiene de la combinación de los siguientes campos de anexo
     * getAnexo().getSignType() y getAnexo().getSignFormat();
     *
     * @param anexo
     * @return
     */
    private String transformarTipoFirma(Anexo anexo) {

        if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) && (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat())
                || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF02
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_XADES_DETACHED_SIGNATURE);

        } else if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) && (ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())
                || ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED.equals(anexo.getSignFormat()))) { //TF03
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE);

        } else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) && (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat())
                || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF04
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE);

        } else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) && ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF05
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE);

        } else if (ValidateSignatureConstants.SIGNTYPE_PAdES.equals(anexo.getSignType())) {//TF06
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_PADES);

        } else if (ValidateSignatureConstants.SIGNTYPE_ODF.equals(anexo.getSignType())) { //TF08
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_ODF);

        } else if (ValidateSignatureConstants.SIGNTYPE_OOXML.equals(anexo.getSignType())) { //TF09
            return RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_OOXML);
        }

        log.info("No hay ninguna coincidencia de TipoFirma para el Anexo: " + anexo.getId());

        return null;
    }

    /**
     * Transforma el Perfil Firma obtenido tras realizar la Validación de la Firma al código NTI correspondiente
     * La transformación al tipo de Firma de DISTRIBICIÓ es un mapeo directo del campo anexoFull.getAnexo().getSignProfile()
     *
     * @param anexo
     * @return
     */
    private String transformarPerfilFirma(Anexo anexo) {

        if (ValidateSignatureConstants.SIGNPROFILE_BES.equals(anexo.getSignProfile()) || ValidateSignatureConstants.SIGNPROFILE_X1.equals(anexo.getSignProfile()) ||
                ValidateSignatureConstants.SIGNPROFILE_X2.equals(anexo.getSignProfile()) || ValidateSignatureConstants.SIGNPROFILE_XL1.equals(anexo.getSignProfile()) ||
                ValidateSignatureConstants.SIGNPROFILE_XL2.equals(anexo.getSignProfile()) || ValidateSignatureConstants.SIGNPROFILE_PADES_BASIC.equals(anexo.getSignProfile())) {

            return RegwebConstantes.PERFIL_FIRMA_BES;

        } else if (ValidateSignatureConstants.SIGNPROFILE_EPES.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_EPES;
        } else if (ValidateSignatureConstants.SIGNPROFILE_PADES_LTV.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_LTV;
        } else if (ValidateSignatureConstants.SIGNPROFILE_T.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_T;
        } else if (ValidateSignatureConstants.SIGNPROFILE_C.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_C;
        } else if (ValidateSignatureConstants.SIGNPROFILE_X.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_X;
        } else if (ValidateSignatureConstants.SIGNPROFILE_XL.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_XL;
        } else if (ValidateSignatureConstants.SIGNPROFILE_A.equals(anexo.getSignProfile())) {
            return RegwebConstantes.PERFIL_FIRMA_A;
        }

        log.info("No hay ninguna coincidencia de PerfilFirma para el Anexo: " + anexo.getId());


        return null;
    }

    /**
     * Añade un timeout espeficio a la llamada WS
     *
     * @param client
     * @param timeout
     */
    private void setTimeoutWSCall(BustiaV1 client, Integer timeout) {

        Map<String, Object> reqContext = ((BindingProvider) client).getRequestContext();
        reqContext.put("javax.xml.ws.client.connectionTimeout", timeout);
        reqContext.put("javax.xml.ws.client.receiveTimeout", timeout);
    }

    /**
     * @return
     * @throws Exception
     */
    private String getPropertyUsuario() throws Exception {
        return getPropertyRequired(PROPERTY_USUARIO);
    }

    /**
     * @return
     * @throws Exception
     */
    private String getPropertyPassword() throws Exception {
        return getPropertyRequired(PROPERTY_PASSWORD);
    }

    /**
     * @return
     * @throws Exception
     */
    private String getPropertyEndPoint() throws Exception {
        return getPropertyRequired(PROPERTY_ENDPOINT);
    }
}