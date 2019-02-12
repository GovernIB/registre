package es.caib.regweb3.plugins.distribucion.goib;


import es.caib.distribucio.ws.v1.bustia.*;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.distribucion.ConfiguracionDistribucion;
import es.caib.regweb3.plugins.distribucion.Destinatario;
import es.caib.regweb3.plugins.distribucion.Destinatarios;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NCommonUtils;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Plugin para distribuir registros a Ripea
 * @author mgonzalez
 */
public class DistribucionGoibPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());


    private static final String basePluginRipea = DISTRIBUCION_BASE_PROPERTY + "goib.";
    private static final String PROPERTY_USUARIO = basePluginRipea + "usuario";
    private static final String PROPERTY_PASSWORD = basePluginRipea + "password";
    private static final String PROPERTY_ENDPOINT = basePluginRipea + "endpoint";
    private static final String PROPERTY_ENVIOCOLA = basePluginRipea + "enviocola";
    private static final String PROPERTY_LISTADODESTINATARIOSMODIFICABLE = basePluginRipea + "listadodestinatariosmodificable";
    private static final String PROPERTY_CONFIGURACIONANEXOS = basePluginRipea + "configuracionanexos";


    public String getPropertyUsuario() throws Exception {

        return getPropertyRequired(PROPERTY_USUARIO);
    }

    public String getPropertyPassword() throws Exception {

        return getPropertyRequired(PROPERTY_PASSWORD);
    }

    public String getPropertyEndPoint() throws Exception {

        return getPropertyRequired(PROPERTY_ENDPOINT);
    }

    public  String getPropertyEnvioCola()  throws Exception{
        return getPropertyRequired(PROPERTY_ENVIOCOLA);
    }

    public  String getPropertyDestinatariosModificable()  throws Exception{
        return getPropertyRequired(PROPERTY_LISTADODESTINATARIOSMODIFICABLE);
    }

    public  String getPropertyConfiguracionAnexos()  throws Exception{
        return getPropertyRequired(PROPERTY_CONFIGURACIONANEXOS);
    }


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
    public Destinatarios distribuir(RegistroEntrada registro) throws Exception {

        //nothing to do
        //En este plugin los destinatarios(busties) se obtienen del organismo destino del registro.
        return null;
    }

    @Override
    public Boolean enviarDestinatarios(RegistroEntrada registro,
        List<Destinatario> destinatariosDefinitivos, String observaciones,
        Locale locale) throws Exception {

        try {
            //Transformamos a registreAnotació. TODO VER QUE CAMPOS DE REGISTRO ENVIAMOS A RIPEA
            RegistreAnotacio registreAnotacio = transformarARegistreAnotacio(registro, locale);

            //Parte de interesados
            for (Interesado interesado : registro.getRegistroDetalle().getInteresados()) {
                if(!interesado.getIsRepresentante()) {
                    RegistreInteressat registreInteressat = transformarARegistreInteressat(interesado, locale);
                    if (interesado.getRepresentante() != null) {
                        RegistreInteressat representant = transformarARegistreInteressat(interesado.getRepresentante(), locale);
                        registreInteressat.setRepresentant(representant);
                    }
                    registreAnotacio.getInteressats().add(registreInteressat);
                }
            }

            //Parte de anexos
            /*En el caso de Ripea no se tiene en cuenta la configuración de los Anexos.
            Siempre se envia el identificador de custodia del Justificante +  el contenido completo de los anexos.*/
            for (AnexoFull anexoFull : registro.getRegistroDetalle().getAnexosFull()) {
                //Transformar a RegistreAnnex
                RegistreAnnex registreAnnex;
                Anexo anexo = anexoFull.getAnexo();
                if (anexo.isJustificante()) { //Si es justificante, solo enviamos referencia Arxiu
                    registreAnnex = transformarARegistreAnnexJustificante(anexo);
                    registreAnotacio.setJustificant(registreAnnex);
                } else {
                    registreAnnex = transformarARegistreAnnex(anexoFull); //Montamos contenido completo
                    registreAnotacio.getAnnexos().add(registreAnnex);
                }
            }

            //Obtenemos la entidad y la unidad Administrativa a donde distribuir el registro
            //TODO Definir bien los criterios de distribución (organismo destinatario, codigoasunto, tipoasunto
            String entidadCodigo = registro.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3();
            log.info("entidadCodigo " + entidadCodigo);
            String unidadAdministrativaCodigo= registro.getDestino().getCodigo();
            log.info("unidadAdminitrativaCodigo "  + unidadAdministrativaCodigo);

            //Método donde se invoca al ws de RIPEA para enviar el registro a los destinatarios(busties en este caso)
            //Propietats per atacar els WS de RIPEA
            String endpoint = getPropertyEndPoint();
            String usuario = getPropertyUsuario();
            String password = getPropertyPassword();

            BustiaV1 client = es.caib.distribucio.ws.client.BustiaV1WsClientFactory.getWsClient(
                    endpoint,
                    usuario,
                    password);

            long start = System.currentTimeMillis();
            client.enviarAnotacioRegistreEntrada(entidadCodigo, unidadAdministrativaCodigo, registreAnotacio);
            log.info("Total enviarAnotacioRegistreEntrada: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));

            return true;
        } catch (Exception e) {
            throw new Exception(e);
        }

    }

    @Override
    public ConfiguracionDistribucion configurarDistribucion() throws Exception {
        //especifica que información se enviará en el segmento de anexo del registro de entrada.
        /* 1 =  metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
        *  2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
        *  3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
        * */
        /* EN ESTA IMPLEMENTACION NO SE EMPLEA */
        ConfiguracionDistribucion cd = new ConfiguracionDistribucion(Boolean.valueOf(getPropertyDestinatariosModificable()), Integer.valueOf(getPropertyConfiguracionAnexos()),Boolean.valueOf(getPropertyEnvioCola()));
        return cd;

    }


    /**
     * Método que transforma de RegistroEntrada a RegistreAnotacio de Ripea.
     *
     * @param re RegistroEntrada
     * @return
     * @throws Exception
     */
    private RegistreAnotacio transformarARegistreAnotacio(RegistroEntrada re, Locale language) throws Exception {

        RegistreAnotacio registreAnotacio = new RegistreAnotacio();
        GregorianCalendar c = new GregorianCalendar();

        //Libro
        registreAnotacio.setLlibreCodi(re.getLibro().getCodigo());
        registreAnotacio.setLlibreDescripcio(re.getLibro().getNombre());

        //Extracto
        registreAnotacio.setExtracte(re.getRegistroDetalle().getExtracto());

        //Tipo Asunto
        registreAnotacio.setAssumpteTipusCodi(re.getRegistroDetalle().getTipoAsunto().getCodigo());
        TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) re.getRegistroDetalle().getTipoAsunto().getTraduccion();
        registreAnotacio.setAssumpteTipusDescripcio(traduccionTipoAsunto.getNombre());

        //Idioma
        registreAnotacio.setIdiomaCodi(re.getRegistroDetalle().getIdioma().toString());
       // registreAnotacio.setIdiomaDescripcio(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(re.getRegistroDetalle().getIdioma()));
        registreAnotacio.setIdiomaDescripcio(I18NCommonUtils.tradueix(language, "idioma." + re.getRegistroDetalle().getIdioma()));

        // Documentación Física
        // Es crea dependencia al genapp.
        String codiDF = re.getRegistroDetalle().getTipoDocumentacionFisica().toString();
        registreAnotacio.setDocumentacioFisicaCodi(codiDF);
        String descripcionTipoDocFisica = I18NCommonUtils.tradueix(language,"tipoDocumentacionFisica." + codiDF);
        registreAnotacio.setDocumentacioFisicaDescripcio(descripcionTipoDocFisica);

        //Codigo Asunto
        if(re.getRegistroDetalle().getCodigoAsunto()!=null) {
            registreAnotacio.setAssumpteCodi(re.getRegistroDetalle().getCodigoAsunto().getCodigo());
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) re.getRegistroDetalle().getCodigoAsunto().getTraduccion();
            registreAnotacio.setAssumpteDescripcio(traduccionCodigoAsunto.getNombre());
        }

        //Referencia y expediente
        registreAnotacio.setReferencia(re.getRegistroDetalle().getReferenciaExterna());
        registreAnotacio.setExpedientNumero(re.getRegistroDetalle().getExpediente());

        //Transporte y número
        registreAnotacio.setTransportNumero(re.getRegistroDetalle().getNumeroTransporte());
        if(re.getRegistroDetalle().getTransporte()!=null) {
            String codigoTransporte = "0" + re.getRegistroDetalle().getTransporte().toString();
            registreAnotacio.setTransportTipusCodi(codigoTransporte);
            String transporte = I18NCommonUtils.tradueix(language, "transporte." + codigoTransporte);
            registreAnotacio.setTransportTipusDescripcio(transporte);
        }

        //Observaciones
        registreAnotacio.setObservacions(re.getRegistroDetalle().getObservaciones());

        //Oficina Origen
        if(re.getRegistroDetalle().getOficinaOrigen() != null) {
            registreAnotacio.setOficinaOrigenCodi(re.getRegistroDetalle().getOficinaOrigen().getCodigo());
            registreAnotacio.setOficinaDescripcio(re.getRegistroDetalle().getOficinaOrigen().getDenominacion());
        }else{
            registreAnotacio.setOficinaOrigenCodi(re.getRegistroDetalle().getOficinaOrigenExternoCodigo());
            registreAnotacio.setOficinaDescripcio(re.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
        }

        //Numero Registro Origen y Fecha Registro Origen
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
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        registreAnotacio.setData(date2);

        //Expone y Solicita
        registreAnotacio.setExposa(re.getRegistroDetalle().getExpone());
        registreAnotacio.setSolicita(re.getRegistroDetalle().getSolicita());

        //Numero de registro Formateado
        registreAnotacio.setNumero(re.getNumeroRegistroFormateado());

        //Usuario
        registreAnotacio.setUsuariCodi(re.getUsuario().getId().toString());
        registreAnotacio.setUsuariContacte(re.getUsuario().getUsuario().getEmail());
        registreAnotacio.setUsuariNom(re.getUsuario().getNombreCompleto());

        return registreAnotacio;

    }

    /**
     * Método que transforma de un {@link es.caib.regweb3.model.Interesado} a {@link es.caib.distribucio.ws.v1.bustia.RegistreInteressat}
     * @param interesado
     * @param language
     * @return
     * @throws Exception
     */
    private RegistreInteressat transformarARegistreInteressat(Interesado interesado, Locale language) throws Exception {
        RegistreInteressat registreInteressat = new RegistreInteressat();

        //Nombre y Apellidos
        registreInteressat.setNom(interesado.getNombre());
        registreInteressat.setLlinatge1(interesado.getApellido1());
        registreInteressat.setLlinatge2(interesado.getApellido2());

        //Tipo Documento y Documento
        if(interesado.getTipoDocumentoIdentificacion()!=null) {
            Character tipoDocumentoIdentificacion = RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion());
            registreInteressat.setDocumentTipus(tipoDocumentoIdentificacion.toString());
            registreInteressat.setDocumentNum(interesado.getDocumento());
        }

        //Email
        registreInteressat.setEmail(interesado.getEmail());
        registreInteressat.setEmailHabilitat(interesado.getDireccionElectronica());

        //Teléfono
        registreInteressat.setTelefon(interesado.getTelefono());
        //Canal Notificación
        if(interesado.getCanal()!=null && interesado.getCanal() != -1) {
            registreInteressat.setCanalPreferent(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
        }

        //Pais
        CatPais pais = interesado.getPais();
        if (pais != null) {
            registreInteressat.setPais(pais.getDescripcionPais());
        }
        //Provincia
        CatProvincia provincia = interesado.getProvincia();
        if (provincia != null) {
            registreInteressat.setProvincia(provincia.getDescripcionProvincia());
        }
        //Localitat
        CatLocalidad localidad = interesado.getLocalidad();
        if (localidad != null) {
            registreInteressat.setMunicipi(localidad.getNombre());
        }

        //Dirección
        registreInteressat.setAdresa(interesado.getDireccion());

        //Codigo Postal
        registreInteressat.setCodiPostal(interesado.getCp());

        //Razón Social
        log.info("Razon social " + interesado.getRazonSocial());
        registreInteressat.setRaoSocial(interesado.getRazonSocial());

        //Observaciones
        registreInteressat.setObservacions(interesado.getObservaciones());

        //Tipo
        if(interesado.getTipo()!=null) {
            registreInteressat.setTipus(interesado.getTipo().toString());
        }

        return registreInteressat;

    }

    /**
     * Método que transforma un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.distribucio.ws.v1.bustia.RegistreAnnex}
     * @param anexo
     * @return
     * @throws Exception
     */
    public RegistreAnnex transformarARegistreAnnexJustificante(Anexo anexo) throws Exception{
        RegistreAnnex registreAnnex = new RegistreAnnex();
        //PARCHE uuid Anexo, veure si finalment passam numExpediente#numDoc o no
        String id = anexo.getCustodiaID();
        registreAnnex.setFitxerArxiuUuid(id.substring(id.lastIndexOf('#')+1));
        return registreAnnex;
    }

    /**
     * Método que transforma un {@link es.caib.regweb3.model.utils.AnexoFull} en un {@link es.caib.distribucio.ws.v1.bustia.RegistreInteressat}
     * @param anexoFull
     * @return
     * @throws Exception
     */
    public RegistreAnnex transformarARegistreAnnex(AnexoFull anexoFull) throws Exception{
        RegistreAnnex registreAnnex = new RegistreAnnex();

        //Título
        registreAnnex.setTitol(anexoFull.getAnexo().getTitulo());
        //Origen
        registreAnnex.setEniOrigen(anexoFull.getAnexo().getOrigenCiudadanoAdmin().toString());
        //Tipo Documento
        registreAnnex.setSicresTipusDocument(RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexoFull.getAnexo().getTipoDocumento()));
        //Tipo Documental
        registreAnnex.setEniTipusDocumental(anexoFull.getAnexo().getTipoDocumental().getCodigoNTI());
        // Validez del Documento(SICRES) = Estado Elaboración (ENI)
        registreAnnex.setEniEstatElaboracio((RegwebConstantes.CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO.get(anexoFull.getAnexo().getValidezDocumento())));
        //Observaciones
        registreAnnex.setObservacions(anexoFull.getAnexo().getObservaciones());
        //Validación OCSP
        registreAnnex.setValidacioOCSP(Base64.encodeBase64String(anexoFull.getAnexo().getValidacionOCSPCertificado()));

        //Fecha de Captura
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(anexoFull.getAnexo().getFechaCaptura());
        XMLGregorianCalendar fechaCaptura = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        registreAnnex.setEniDataCaptura(fechaCaptura);


        //TODO METADADES(han eliminat el mètode)
        //registreAnnex.setMetadades(anexoFull.getMetadatas());



        //Caso Anexo SIN FIRMA se obtiene de DocumentCustody
        if( anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

            registreAnnex.setFitxerNom(anexoFull.getDocumentoCustody().getName());
            registreAnnex.setFitxerTamany((int)anexoFull.getDocumentoCustody().getLength());
            registreAnnex.setFitxerTipusMime(anexoFull.getDocumentoCustody().getMime());
            registreAnnex.setFitxerContingut(anexoFull.getDocumentoCustody().getData());


        }
        //Caso Anexo FIRMA ATTACHED El anexo se obtiene de  signature custody
        if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){
            //RIPEA pide que se envie un registreAnnex con los campos "Fitxer" rellenados y además añadirle un objeto Firma con ciertos valores
            registreAnnex.setFitxerNom(anexoFull.getSignatureCustody().getName());
            registreAnnex.setFitxerTamany((int)anexoFull.getSignatureCustody().getLength());
            registreAnnex.setFitxerTipusMime(anexoFull.getSignatureCustody().getMime());
            registreAnnex.setFitxerContingut(anexoFull.getSignatureCustody().getData());


            //RIPEA obliga a indicar un objeto Firma indicando el tipusMime, el perfil y el tipo de firma
            log.info("Tipus Mime  " +anexoFull.getSignatureCustody().getMime());
            Firma firma = new Firma();
            firma.setTipusMime(anexoFull.getSignatureCustody().getMime());
            //transformamos el perfil y la firma que obtenemos de la validación del plugin a los valores que espera RIPEA
            transformarPerfilTipoFirma(anexoFull.getAnexo(),firma);

            //Añadimos el objeto firma al registreAnnex.
            registreAnnex.getFirmes().add(firma);

        }
        //Caso Anexo FIRMA DETACHED
        if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED){

            //En registreAnnex metemos toda la información del documento
            registreAnnex.setFitxerNom(anexoFull.getDocumentoCustody().getName());
            registreAnnex.setFitxerTamany((int)anexoFull.getDocumentoCustody().getLength());
            registreAnnex.setFitxerTipusMime(anexoFull.getDocumentoCustody().getMime());
            registreAnnex.setFitxerContingut(anexoFull.getDocumentoCustody().getData());
            //FIRMA ANTIGUA
            // TODO FALTA ESTO EN LA NUEVA VERSION registreAnnex.setFirmaFitxerTamany((int)anexoFull.getSignSize());

            //en este caso el objeto Firma también incorpora el contenido de la firma
            Firma firma = new Firma();
            log.info("Nombre de la firma " +anexoFull.getSignatureCustody().getName());
            firma.setFitxerNom(anexoFull.getSignatureCustody().getName());
            firma.setTipusMime(anexoFull.getSignatureCustody().getMime());
            //TODO prueba con csv comentado
            firma.setCsv(anexoFull.getAnexo().getCsv());
            firma.setCsvRegulacio("Regulació CSV");
            firma.setContingut(anexoFull.getSignatureCustody().getData());

            //transformamos el perfil y la firma que obtenemos de la validación del plugin a los valores que espera RIPEA
            transformarPerfilTipoFirma(anexoFull.getAnexo(),firma);

            //Añadimos el objeto firma al registreAnnex.
            registreAnnex.getFirmes().add(firma);
        }

        return registreAnnex;
    }

    /**
     * Método que transforma el Perfil y el tipo de firma que se obtiene de la validación
     * a través del plugin a los valores que espera RIPEA.
     * @param anexo anexo del que se obtiene la información de validación
     * @param firma firma a transformar
     */
    protected void transformarPerfilTipoFirma(Anexo anexo, Firma firma) throws Exception {

        // La transformación al tipo de Firma que espera RIPEA se obtiene de la combinación de los siguientes campos de anexo
        //anexoFull.getAnexo().getSignType() y anexoFull.getAnexo().getSignFormat();

        if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) &&
                (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat()) || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF02
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_XADES_DETACHED_SIGNATURE));
        }else if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) &&
                ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF03
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE));
        }else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) &&
                (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat()) || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF04
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE));
        } else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) &&
                ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF05
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE));
        } else if (ValidateSignatureConstants.SIGNTYPE_PAdES.equals(anexo.getSignType())) {//TF06
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_PADES));
        } else  if (ValidateSignatureConstants.SIGNTYPE_ODF.equals(anexo.getSignType())) { //TF08
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_ODF));
        }else  if (ValidateSignatureConstants.SIGNTYPE_OOXML.equals(anexo.getSignType())) { //TF09
            firma.setTipus(RegwebConstantes.CODIGO_NTI_BY_TIPOFIRMA.get(RegwebConstantes.TIPO_FIRMA_OOXML));
        }



        //Perfil de firma
        // La transformación al tipo de Firma de RIPEA es un mapeo directo del campo anexoFull.getAnexo().getSignProfile()

        if(ValidateSignatureConstants.SIGNPROFILE_BES.equals(anexo.getSignProfile()) ||
           ValidateSignatureConstants.SIGNPROFILE_X1.equals(anexo.getSignProfile()) ||
           ValidateSignatureConstants.SIGNPROFILE_X2.equals(anexo.getSignProfile()) ||
           ValidateSignatureConstants.SIGNPROFILE_XL1.equals(anexo.getSignProfile()) ||
           ValidateSignatureConstants.SIGNPROFILE_XL2.equals(anexo.getSignProfile()) ||
           ValidateSignatureConstants.SIGNPROFILE_PADES_BASIC.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_BES);
        }else if(ValidateSignatureConstants.SIGNPROFILE_EPES.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_EPES);
        }else  if(ValidateSignatureConstants.SIGNPROFILE_PADES_LTV.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_LTV);
        }else if(ValidateSignatureConstants.SIGNPROFILE_T.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_T);
        }else  if(ValidateSignatureConstants.SIGNPROFILE_C.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_C);
        }else if(ValidateSignatureConstants.SIGNPROFILE_X.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_X);
        }else if(ValidateSignatureConstants.SIGNPROFILE_XL.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_XL);
        }else  if(ValidateSignatureConstants.SIGNPROFILE_A.equals(anexo.getSignProfile())){
            firma.setPerfil(RegwebConstantes.PERFIL_FIRMA_A);
        }

        log.info("Firma Perfil " + firma.getPerfil());
        log.info("Firma Tipo " + firma.getTipus());

        if(firma.getTipus() == null || firma.getPerfil() == null){
            log.info("Anexo: " + anexo.getTitulo());
            throw new Exception("Error: Tipus Firma o Perfil Firma no poden ser buits");
        }
    }
}