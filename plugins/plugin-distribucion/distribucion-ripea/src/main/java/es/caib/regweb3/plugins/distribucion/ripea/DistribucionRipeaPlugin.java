package es.caib.regweb3.plugins.distribucion.ripea;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.distribucion.ConfiguracionDistribucion;
import es.caib.regweb3.plugins.distribucion.Destinatario;
import es.caib.regweb3.plugins.distribucion.Destinatarios;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.ripea.ws.v1.bustia.BustiaV1;
import es.caib.ripea.ws.v1.bustia.RegistreAnnex;
import es.caib.ripea.ws.v1.bustia.RegistreAnotacio;
import es.caib.ripea.ws.v1.bustia.RegistreInteressat;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NCommonUtils;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;

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
public class DistribucionRipeaPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = Logger.getLogger(getClass());


    public static final String basePluginRipea = DISTRIBUCION_BASE_PROPERTY + "distribucionripea.";
    public static final String PROPERTY_USUARIO = basePluginRipea + "usuario";
    public static final String PROPERTY_PASSWORD = basePluginRipea + "password";
    public static final String PROPERTY_ENDPOINT = basePluginRipea + "endpoint";


    public String getPropertyUsuario() throws Exception {
        //return getProperty(PROPERTY_EXEMPLE);
        log.info("PROPERTY_USUARIO : " + PROPERTY_USUARIO);

        return getPropertyRequired(PROPERTY_USUARIO);
    }

    public String getPropertyPassword() throws Exception {
        //return getProperty(PROPERTY_EXEMPLE);

        return getPropertyRequired(PROPERTY_PASSWORD);
    }

    public String getPropertyEndPoint() throws Exception {
        //return getProperty(PROPERTY_EXEMPLE);

        return getPropertyRequired(PROPERTY_ENDPOINT);
    }

    /**
     *
     */
    public DistribucionRipeaPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionRipeaPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionRipeaPlugin(String propertyKeyBase) {
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
        //Invocar ws de ripea

        String endpoint = getPropertyEndPoint();
        String usuario = getPropertyUsuario();
        String password = getPropertyPassword();


        BustiaV1 client = es.caib.ripea.ws.client.BustiaV1WsClientFactory.getWsClient(
                endpoint,
                usuario,
                password);

        //Transformamos a registreAnotació. TODO VER QUE CAMPOS DE REGISTRO ENVIAMOS A RIPEA
        RegistreAnotacio registreAnotacio = transformarARegistreAnotacio(registro, locale);


        //Parte de interesados
        for (Interesado interesado : registro.getRegistroDetalle().getInteresados()) {
            RegistreInteressat registreInteressat = transformarARegistreInteressat(interesado, locale);
            if(interesado.getRepresentante()!=null) {
                RegistreInteressat representant = transformarARegistreInteressat(interesado.getRepresentante(), locale);
                registreInteressat.setRepresentant(representant);
            }

          /*  log.info(registreInteressat.getNom());
            log.info(registreInteressat.getLlinatge1());
            log.info(registreInteressat.getRepresentant().getNom());
            log.info(registreInteressat.getRepresentant().getLlinatge1());*/
            registreAnotacio.getInteressats().add(registreInteressat);
        }



        // Anexos : preparamos los anexos en función de la configuración establecida
        switch (configurarDistribucion().getConfiguracionAnexos()){
            // 1 =  metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
            case 1:{
                log.info("NUM ANEXOS FULL " + registro.getRegistroDetalle().getAnexosFull().size());
                for (AnexoFull anexoFull : registro.getRegistroDetalle().getAnexosFull()) {
                    //Transformar a RegistreAnnex
                    RegistreAnnex registreAnnex = transformarARegistreAnnex(anexoFull, configurarDistribucion().getConfiguracionAnexos());
                    registreAnotacio.getAnnexos().add(registreAnnex);
                }
                break;
            }
            // 2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
            case 2:{
                log.info("NUM ANEXOS " + registro.getRegistroDetalle().getAnexos().size());
                for (Anexo anexo : registro.getRegistroDetalle().getAnexos()) {
                    //Transformar a RegistreAnnex
                    RegistreAnnex registreAnnex = transformarARegistreAnnex(anexo);
                    registreAnotacio.getAnnexos().add(registreAnnex);
                }
                break;
            }
            // 3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
            case 3:{
                log.info("NUM ANEXOS FULL " + registro.getRegistroDetalle().getAnexosFull().size());
                for (AnexoFull anexoFull : registro.getRegistroDetalle().getAnexosFull()) {
                    //Transformar a RegistreAnnex
                    RegistreAnnex registreAnnex = transformarARegistreAnnex(anexoFull, configurarDistribucion().getConfiguracionAnexos());
                    registreAnotacio.getAnnexos().add(registreAnnex);
                }
                break;
            }
        }

        //Obtenemos la entidad y la unidad Administrativa a donde distribuir el registro
        String entidadCodigo = registro.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3();
        log.info("entidadCodigo " + entidadCodigo);
        String unidadAdministrativaCodigo= registro.getDestino().getCodigo();
        log.info("unidadAdminitrativaCodigo "  + unidadAdministrativaCodigo);

        //Invocamos ws de RIPEA
        client.enviarAnotacioRegistreEntrada(entidadCodigo, unidadAdministrativaCodigo, registreAnotacio);


        return true;

    }

    @Override
    public ConfiguracionDistribucion configurarDistribucion() throws Exception {
        //especifica que información se enviará en el segmento de anexo del registro de entrada.
        /* 1 =  metadades + fitxer + firma. És a dir a dins el segment annexes de l'assentament s'enviaria tot el contingut de l'annexe.
        *  2 =  custodiaId. A dins el segment annexes de l'assentament només s'enviaria l'Id del sistema que custodia l'arxiu.
        *  3 = custodiaId + metadades. A dins el segment annexes de l'assentament s'enviaria l'Id del sistema que custodia l'arxiu i les metadades del document.
        * */
        ConfiguracionDistribucion cd = new ConfiguracionDistribucion(false, 1);
        return cd;

    }


    /**
     * Método que transforma de RegistroEntrada a RegistreAnotacio de Ripea.
     *
     * @param re
     * @return
     * @throws Exception
     */
    private RegistreAnotacio transformarARegistreAnotacio(RegistroEntrada re, Locale language) throws Exception {

        RegistreAnotacio registreAnotacio = new RegistreAnotacio();


        registreAnotacio.setIdentificador(re.getId().toString());
        registreAnotacio.setAplicacioCodi(re.getRegistroDetalle().getAplicacion());
        registreAnotacio.setAplicacioVersio(re.getRegistroDetalle().getVersion());
        registreAnotacio.setOficinaCodi(re.getOficina().getCodigo());
        registreAnotacio.setOficinaDescripcio(re.getOficina().getDenominacion());

        if(re.getRegistroDetalle().getCodigoAsunto()!=null) {
            registreAnotacio.setAssumpteCodi(re.getRegistroDetalle().getCodigoAsunto().getCodigo());
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) re.getRegistroDetalle().getCodigoAsunto().getTraduccion();
            registreAnotacio.setAssumpteDescripcio(traduccionCodigoAsunto.getNombre());
        }

        registreAnotacio.setAssumpteTipusCodi(re.getRegistroDetalle().getTipoAsunto().getCodigo());
        TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) re.getRegistroDetalle().getTipoAsunto().getTraduccion();
        registreAnotacio.setAssumpteTipusDescripcio(traduccionTipoAsunto.getNombre());


        GregorianCalendar c = new GregorianCalendar();
        c.setTime(re.getFecha());
        XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);


        registreAnotacio.setData(date2);
        String codiDF = re.getRegistroDetalle().getTipoDocumentacionFisica().toString();
        registreAnotacio.setDocumentacioFisicaCodi(codiDF);
        //TODO Es crea dependencia al genapp.
        String descripcionTipoDocFisica = I18NCommonUtils.tradueix(language,"tipoDocumentacionFisica." + codiDF);
        log.info(descripcionTipoDocFisica);
        registreAnotacio.setDocumentacioFisicaDescripcio(descripcionTipoDocFisica);
        registreAnotacio.setEntitatCodi(re.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3());
        registreAnotacio.setEntitatDescripcio(re.getOficina().getOrganismoResponsable().getEntidad().getNombre());
        registreAnotacio.setExpedientNumero(re.getRegistroDetalle().getExpediente());
        registreAnotacio.setExposa(re.getRegistroDetalle().getExpone());
        registreAnotacio.setExtracte(re.getRegistroDetalle().getExtracto());
        registreAnotacio.setIdiomaCodi(re.getRegistroDetalle().getIdioma().toString());
        registreAnotacio.setIdiomaDescripcio(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(re.getRegistroDetalle().getIdioma()));
        registreAnotacio.setLlibreCodi(re.getLibro().getCodigo());
        registreAnotacio.setLlibreDescripcio(re.getLibro().getNombre());
        registreAnotacio.setNumero(re.getNumeroRegistro());
        registreAnotacio.setReferencia(re.getRegistroDetalle().getReferenciaExterna());
        registreAnotacio.setSolicita(re.getRegistroDetalle().getSolicita());
        registreAnotacio.setTransportNumero(re.getRegistroDetalle().getNumeroTransporte());
        if(re.getRegistroDetalle().getTransporte()!=null) {
            String codigoTransporte = re.getRegistroDetalle().getTransporte().toString();
            registreAnotacio.setTransportTipusCodi(codigoTransporte);
            String transporte = I18NCommonUtils.tradueix(language, "transporte." + codigoTransporte);
            registreAnotacio.setTransportTipusDescripcio(transporte);
        }
        registreAnotacio.setUsuariCodi(re.getUsuario().getId().toString());
        registreAnotacio.setUsuariContacte(re.getUsuario().getUsuario().getEmail());
        registreAnotacio.setUsuariNom(re.getUsuario().getNombreCompleto());

        return registreAnotacio;

    }

    /**
     * Método que transforma de un {@link es.caib.regweb3.model.Interesado} a {@link es.caib.ripea.ws.v1.bustia.RegistreInteressat}
     * @param interesado
     * @param language
     * @return
     * @throws Exception
     */
    private RegistreInteressat transformarARegistreInteressat(Interesado interesado, Locale language) throws Exception {
        RegistreInteressat registreInteressat = new RegistreInteressat();

        registreInteressat.setAdresa(interesado.getDireccion());
        if(interesado.getCanal()!=null && interesado.getCanal() != -1) {
            registreInteressat.setCanalPreferent(RegwebConstantes.CODIGO_BY_CANALNOTIFICACION.get(interesado.getCanal()));
        }
        registreInteressat.setCodiPostal(interesado.getCp());


        if(interesado.getTipoDocumentoIdentificacion()!=null) {
            Character tipoDocumentoIdentificacion = RegwebConstantes.CODIGO_NTI_BY_TIPODOCUMENTOID.get(interesado.getTipoDocumentoIdentificacion());
            registreInteressat.setDocumentTipus(tipoDocumentoIdentificacion.toString());
            registreInteressat.setDocumentNum(interesado.getDocumento());
        }
        registreInteressat.setEmail(interesado.getEmail());
        registreInteressat.setEmailHabilitat(interesado.getDireccionElectronica());
        registreInteressat.setNom(interesado.getNombre());
        registreInteressat.setLlinatge1(interesado.getApellido1());
        registreInteressat.setLlinatge2(interesado.getApellido2());
        CatLocalidad localidad = interesado.getLocalidad();
        if (localidad != null) {
            registreInteressat.setMunicipi(localidad.getCodigoLocalidad().toString());
        }
        registreInteressat.setObservacions(interesado.getObservaciones());
        CatPais pais = interesado.getPais();
        if (pais != null) {
            registreInteressat.setPais(pais.getCodigoPais().toString());
        }
        CatProvincia provincia = interesado.getProvincia();
        if (provincia != null) {
            registreInteressat.setProvincia(provincia.getCodigoProvincia().toString());
        }
        registreInteressat.setRaoSocial(interesado.getRazonSocial());
        registreInteressat.setTelefon(interesado.getTelefono());
        if(interesado.getTipo()!=null) {
            registreInteressat.setTipus(interesado.getTipo().toString());
        }

        return registreInteressat;

    }

    /**
     * Método que transforma un {@link es.caib.regweb3.model.Anexo} en un {@link es.caib.ripea.ws.v1.bustia.RegistreInteressat}
     * @param anexo
     * @return
     * @throws Exception
     */
    public RegistreAnnex transformarARegistreAnnex(Anexo anexo) throws Exception{
        RegistreAnnex registreAnnex = new RegistreAnnex();
        registreAnnex.setFitxerArxiuUuid(anexo.getCustodiaID());
        return registreAnnex;
    }

    /**
     * Método que transforma un {@link es.caib.regweb3.model.utils.AnexoFull} en un {@link es.caib.ripea.ws.v1.bustia.RegistreInteressat}
     * @param anexoFull
     * @param configuracionDistribucion
     * @return
     * @throws Exception
     */
    public RegistreAnnex transformarARegistreAnnex(AnexoFull anexoFull,int  configuracionDistribucion) throws Exception{
        RegistreAnnex registreAnnex = new RegistreAnnex();
        registreAnnex.setTitol(anexoFull.getAnexo().getTitulo());

       // registreAnnex.setNtiElaboracioEstat(RegwebConstantes.CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO.get(anexoFull.getAnexo().getValidezDocumento())); //TODO PENDIENTE ARREGLEN RIPEA
        registreAnnex.setNtiElaboracioEstat("01");
        registreAnnex.setNtiTipusDocument(anexoFull.getAnexo().getTipoDocumental().getCodigoNTI());
        registreAnnex.setFirmaMode(anexoFull.getAnexo().getModoFirma());
        registreAnnex.setObservacions(anexoFull.getAnexo().getObservaciones());
        registreAnnex.setOrigenCiutadaAdmin(anexoFull.getAnexo().getOrigenCiudadanoAdmin().toString());
        registreAnnex.setValidacioOCSP(Base64.encodeBase64String(anexoFull.getAnexo().getValidacionOCSPCertificado()));
        registreAnnex.setSicresTipusDocument(RegwebConstantes.CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(anexoFull.getAnexo().getTipoDocumento()));

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(anexoFull.getAnexo().getFechaCaptura());
        XMLGregorianCalendar fechaCaptura = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        registreAnnex.setDataCaptura(fechaCaptura);


        //TODO METADADES
       // registreAnnex.setMetadades(anexoFull.getMetadatas());

        // Se monta el contenido de los anexos en función de la configuración del plugin
        switch (configuracionDistribucion) {
            case 1: {
                //DOCUMENTO
                if( anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

                    registreAnnex.setFitxerNom(anexoFull.getDocumentoCustody().getName());
                    registreAnnex.setFitxerTamany((int)anexoFull.getDocSize());
                    registreAnnex.setFitxerTipusMime(anexoFull.getDocumentoCustody().getMime());
                    registreAnnex.setFitxerContingutBase64(Base64.encodeBase64String(anexoFull.getDocumentoCustody().getData()));

                }
                //El anexo está en signature custody
                if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){
                    registreAnnex.setFitxerNom(anexoFull.getSignatureCustody().getName());
                    registreAnnex.setFitxerTamany((int)anexoFull.getSignSize());
                    registreAnnex.setFitxerTipusMime(anexoFull.getSignatureCustody().getMime());
                    registreAnnex.setFitxerContingutBase64(Base64.encodeBase64String(anexoFull.getSignatureCustody().getData()));

                }
                if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED){

                    //DOCUMENTO
                    registreAnnex.setFitxerNom(anexoFull.getDocumentoCustody().getName());
                    registreAnnex.setFitxerTamany((int)anexoFull.getDocSize());
                    registreAnnex.setFitxerTipusMime(anexoFull.getDocumentoCustody().getMime());
                    registreAnnex.setFitxerContingutBase64(Base64.encodeBase64String(anexoFull.getDocumentoCustody().getData()));
                    //FIRMA
                    registreAnnex.setFirmaFitxerNom(anexoFull.getSignatureCustody().getName());
                    registreAnnex.setFirmaFitxerTamany((int)anexoFull.getSignSize());
                    registreAnnex.setFirmaFitxerTipusMime(anexoFull.getSignatureCustody().getMime());
                    registreAnnex.setFirmaCsv(anexoFull.getAnexo().getCsv());
                    registreAnnex.setFirmaFitxerContingutBase64(Base64.encodeBase64String(anexoFull.getSignatureCustody().getData()));

                }
                break;
            }
            case 3: {
                if( anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA
                        ||anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {

                  registreAnnex.setFitxerArxiuUuid(anexoFull.getAnexo().getCustodiaID());

                }
                if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                    registreAnnex.setFitxerArxiuUuid(anexoFull.getAnexo().getCustodiaID());
                    registreAnnex.setFirmaFitxerArxiuUuid(anexoFull.getAnexo().getCustodiaID());
                }
                break;
            }

        }

        return registreAnnex;
    }


}