package es.caib.regweb3.persistence.integracion;

import es.caib.plugins.arxiu.api.*;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.IntegracionLocal;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.utils.ClientUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ejb.EJB;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ArxiuCaibUtils {

    public static final Logger log = LoggerFactory.getLogger(ArxiuCaibUtils.class);

    @EJB(mappedName = PluginLocal.JNDI_NAME)
    private PluginLocal pluginEjb;

    @EJB(mappedName = RegistroDetalleLocal.JNDI_NAME)
    private RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = IntegracionLocal.JNDI_NAME)
    private IntegracionLocal integracionEjb;

    private static final String basePluginArxiuCaib = RegwebConstantes.REGWEB3_PROPERTY_BASE + "plugin.arxiu.caib.";
    private static final String PROPERTY_APLICACION = basePluginArxiuCaib + "aplicacio.codi";
    private static final String PROPERTY_SERIE_DOCUMENTAL = basePluginArxiuCaib + "serieDocumental";
    private static final String PROPERTY_NOMBRE_PROCEDIMIENTO = basePluginArxiuCaib + "nombreProcedimiento";
    private static final String PROPERTY_CODIGO_PROCEDIMIENTO = basePluginArxiuCaib + "codigoProcedimiento";
    private static final String PROPERTY_CSV_VALIDATION_URL = basePluginArxiuCaib + "csv.validation.url";
    private static final String PROPERTY_CONCSV_URL = basePluginArxiuCaib + "concsv.url";
    private static final String PROPERTY_CONCSV_USERNAME = basePluginArxiuCaib + "concsv.username";
    private static final String PROPERTY_CONCSV_PASSWORD = basePluginArxiuCaib + "concsv.password";
    private static final String PROPERTY_CERRAR_EXPEDIENTE = basePluginArxiuCaib + "cerrarExpediente";

    private IArxiuPlugin arxiuPlugin;
    private Properties properties;


    /**
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    public IArxiuPlugin cargarPlugin(Long idEntidad) throws I18NException {

        IArxiuPlugin iArxiuPlugin = (IArxiuPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_ARXIU_JUSTIFICANTE, true);

        setArxiuPlugin(iArxiuPlugin);
        setProperties(pluginEjb.getPropertiesPlugin(idEntidad, RegwebConstantes.PLUGIN_ARXIU_JUSTIFICANTE));

        return iArxiuPlugin;
    }

    /**
     * Crea el Justificante en un entorno GOIB (expediente + documento)
     * @param registro
     * @param firma
     * @return
     * @throws I18NException
     */
    public JustificanteArxiu crearJustificanteArxiuCaib(IRegistro registro, Long tipoRegistro, Firma firma) throws I18NException{

        ContingutArxiu expediente = null;
        ContingutArxiu documento = null;

        String serieDocumental = getPropertySerieDocumental();
        String codigoProcedimiento = getPropertyCodigoProcedimiento();

        try{

            // Comprobamos que no se haya creado previamente el expediente
            if(StringUtils.isEmpty(registro.getRegistroDetalle().getExpedienteJustificante())){

                // Creamos el Expediente del Justificante
                expediente = crearExpediente(registro, tipoRegistro, serieDocumental, codigoProcedimiento);


                // Guardamos la referencia del expediente creado
                registro.getRegistroDetalle().setExpedienteJustificante(expediente.getIdentificador());
                registroDetalleEjb.merge(registro.getRegistroDetalle());

            }else{
                expediente = new Expedient();
                expediente.setIdentificador(registro.getRegistroDetalle().getExpedienteJustificante());

            }

            // Creamos el Documento del Justificante
            documento = crearDocumentoJustificante(registro, getTipoRegistroEni(tipoRegistro), serieDocumental, firma, expediente.getIdentificador(), DocumentEstat.DEFINITIU);


            //Cerramos el expediente
            if(getPropertyCerrarExpediente()){
                getArxiuPlugin().expedientTancar(expediente.getIdentificador());
            }

        }catch (ArxiuException e){
            log.info("Error creando el justificante en Arxiu: " + e.getMessage());

            // Si el error es porqué el Justificante ya existe, intentamos recuperarlo
            if(e.getMessage().contains("Duplicate child name not allowed") || e.getMessage().contains("Null")){

                try{
                    if(expediente != null){
                        log.info("Comprobamos si el Justificante ya existe, e intentamos asociarlo al expediente: " + expediente.getIdentificador());
                        
                        Date inicio = new Date();
                        StringBuilder peticion = new StringBuilder();

                        Expedient expedient = arxiuPlugin.expedientDetalls(expediente.getIdentificador(), null);

                        if(expedient != null){
                            for(ContingutArxiu contingutArxiu:expedient.getContinguts()){
                                if(contingutArxiu.getNom().equals(firma.getFitxerNom())){
                                    Document doc = arxiuPlugin.documentDetalls(contingutArxiu.getIdentificador(), null, false);

                                    // Integración
                                    peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
                                    peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
                                    peticion.append("expedienteID: ").append(expediente.getIdentificador()).append(System.getProperty("line.separator"));
                                    peticion.append("documentoID: ").append(doc.getIdentificador()).append(System.getProperty("line.separator"));
                                    peticion.append("csv: ").append(doc.getDocumentMetadades().getCsv()).append(System.getProperty("line.separator"));
                                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_CUSTODIA, "Recuperar Justificante Api Arxiu", peticion.toString(), System.currentTimeMillis() - inicio.getTime(), registro.getUsuario().getEntidad().getId(), registro.getNumeroRegistroFormateado());

                                    return new JustificanteArxiu(expediente, doc);
                                }
                            }
                        }
                    }

                }catch (Exception e1){
                    e.printStackTrace();
                    log.info("Error intentando asociar el justificante ya existente: " + expediente.getIdentificador());
                }
            }

            e.printStackTrace();
            throw new I18NException(e, "justificante.custodiando.error", new I18NArgumentString(e.getMessage()));
        }

        return new JustificanteArxiu(expediente, documento);
    }

    /**
     * Crea el Justificante en un entorno GOIB FileSystem (expediente + documento)
     * @param registro
     * @param firma
     * @return
     * @throws I18NException
     */
    public JustificanteArxiu crearJustificanteFileSystem(IRegistro registro, Long tipoRegistro, Firma firma) throws I18NException{

        ContingutArxiu expediente = null;
        ContingutArxiu documento = null;

        String serieDocumental = getPropertySerieDocumental();
        String codigoProcedimiento = getPropertyCodigoProcedimiento();

        try{

            // Creamos el Expediente del Justificante
            expediente = crearExpediente(registro, tipoRegistro, serieDocumental, codigoProcedimiento);

            // Creamos el Documento del Justificante
            documento = crearDocumentoJustificante(registro, getTipoRegistroEni(tipoRegistro), serieDocumental, firma, expediente.getIdentificador(), DocumentEstat.ESBORRANY);

        }catch (ArxiuException e){
            log.info("Error creando el justificante en Arxiu");
            e.printStackTrace();
            throw e;
        }

        return new JustificanteArxiu(expediente, documento);
    }

    /**
     * Custodia un Justificante en Arxiu-Caib, que ya existía en FileSyetem
     * @param expedientFS
     * @param documentFS
     * @return
     * @throws I18NException
     */
    public JustificanteArxiu custodiarJustificante(Anexo anexo, Expedient expedientFS, Document documentFS) throws I18NException{

        ContingutArxiu expediente = null;
        ContingutArxiu documento = null;

        try{

            // Comprobamos que no se haya creado previamente el expediente
            if(StringUtils.isEmpty(anexo.getRegistroDetalle().getExpedienteJustificante())){

                // Creamos el Expediente nuevo de Arxiu-Caib
                Expedient expedient = new Expedient();
                expedient.setIdentificador(null);
                expedient.setNom(expedientFS.getNom());

                // Metadates
                ExpedientMetadades metadadesExp = new ExpedientMetadades();
                metadadesExp.setIdentificador(null);
                metadadesExp.setDataObertura(expedientFS.getExpedientMetadades().getDataObertura());
                metadadesExp.setClassificacio(getPropertyCodigoProcedimiento());
                metadadesExp.setEstat(ExpedientEstat.OBERT);
                metadadesExp.setOrgans(expedientFS.getExpedientMetadades().getOrgans());
                metadadesExp.setInteressats(expedientFS.getExpedientMetadades().getInteressats());
                metadadesExp.setSerieDocumental(getPropertySerieDocumental());

                // Metadates adicionals
                Map<String, Object> metaDadesAddicionals = new HashMap<String, Object>();
                metaDadesAddicionals.put("eni:app_tramite_exp", RegwebConstantes.APLICACION_NOMBRE);
                metadadesExp.setMetadadesAddicionals(metaDadesAddicionals);

                expedient.setMetadades(metadadesExp);

                // Creamos el Expediente en Arxiu-Caib
                expediente = getArxiuPlugin().expedientCrear(expedient);

                // Guardamos la referencia del expediente creado
                anexo.getRegistroDetalle().setExpedienteJustificante(expediente.getIdentificador());
                registroDetalleEjb.merge(anexo.getRegistroDetalle());

            }else{

                expediente = new Expedient();
                expediente.setIdentificador(anexo.getRegistroDetalle().getExpedienteJustificante());

            }

            // Creamos el Documento nuevo de Arxiu-Caib
            Document document = new Document();
            document.setIdentificador(null);
            document.setEstat(DocumentEstat.DEFINITIU);

            // Contenido y Firma
            document.setContingut(null);
            document.setNom("justificante.pdf"); //TODO cambiar por documentFS.getNom()
            document.setFirmes(new ArrayList<Firma>());
            document.getFirmes().add(documentFS.getFirmes().get(0));

            // Metadatos
            DocumentMetadades metadadesDoc = new DocumentMetadades();
            metadadesDoc.setIdentificador(null);
            metadadesDoc.setSerieDocumental(getPropertySerieDocumental());
            metadadesDoc.setOrgans(documentFS.getDocumentMetadades().getOrgans());
            metadadesDoc.setDataCaptura(documentFS.getDocumentMetadades().getDataCaptura());
            metadadesDoc.setOrigen(ContingutOrigen.ADMINISTRACIO);
            metadadesDoc.setEstatElaboracio(DocumentEstatElaboracio.ORIGINAL);
            metadadesDoc.setTipusDocumental(DocumentTipus.ALTRES);
            metadadesDoc.setExtensio(DocumentExtensio.PDF);
            metadadesDoc.setFormat(DocumentFormat.PDF);

            // Metadatos adicionales
            metadadesDoc.setMetadadesAddicionals(new HashMap<String, Object>()); // Todo añadir document.getMetadades().getMetadadesAddicionals() cuando funcione

            document.setMetadades(metadadesDoc);

            // Creamos el Documento en Arxiu-Caib
            documento = getArxiuPlugin().documentCrear(document, expediente.getIdentificador());

            //Cerramos el expediente
            if(getPropertyCerrarExpediente()){
                getArxiuPlugin().expedientTancar(expediente.getIdentificador());
            }

        }catch (ArxiuException e){
            log.info("Error creando el justificante en Arxiu");
            e.printStackTrace();
            throw e;
        }

        return new JustificanteArxiu(expediente, documento);
    }

    /**
     *
     * @param registro
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    private ContingutArxiu crearExpediente(IRegistro registro, Long tipoRegistro, String serieDocumental, String codigoProcedimiento) throws I18NException {

        String nombreExpediente = getNombreExpediente(registro, tipoRegistro);

        Interesado interesado = registro.getRegistroDetalle().getInteresados().get(0);

        // TODO COMPROBAR EXISTENCIA DEL EXPEDIENTE ANTES DE CREAR
        // Generamos el Expedient
        Expedient expediente = generarExpediente(null,
                nombreExpediente,
                null,
                Arrays.asList(registro.getOficina().getCodigo()),
                new Date(),
                codigoProcedimiento,
                ExpedientEstat.OBERT,
                Arrays.asList(interesado.getDocumentoNTI()),
                serieDocumental, RegwebConstantes.APLICACION_NOMBRE);

        // Creamos el Expediente en Arxiu
        ContingutArxiu expedienteCreado = getArxiuPlugin().expedientCrear(expediente);

        return expedienteCreado;
    }


    /**
     *
     * @param registro
     * @param tipoRegistro
     * @param uuidExpedient
     * @param serieDocumental
     * @return
     * @throws I18NException
     */
    private ContingutArxiu crearDocumentoJustificante(IRegistro registro, Integer tipoRegistro, String serieDocumental, Firma firma, String uuidExpedient, DocumentEstat estadoDocumento) throws I18NException{

        //Generamos el Documento
        Document documento = generarDocumentoJustificante(registro, tipoRegistro, serieDocumental, firma, estadoDocumento);

        // Crear el Documento en Arxiu
        ContingutArxiu documentoCreado = getArxiuPlugin().documentCrear(documento, uuidExpedient);

        return documentoCreado;
    }

    /**
     *
     * @param anexoFull
     * @param registro
     * @param tipoRegistro
     * @param uuidExpedient
     * @param serieDocumental
     * @return
     * @throws I18NException
     */
    private ContingutArxiu crearDocumento(AnexoFull anexoFull, IRegistro registro, Integer tipoRegistro, String uuidExpedient, String serieDocumental) throws I18NException{

        //Generamos el Documento
        Document documento = generarDocumento(registro, tipoRegistro, anexoFull, serieDocumental);

        // Crear el Documento en Arxiu
        ContingutArxiu documentoCreado = getArxiuPlugin().documentCrear(documento, uuidExpedient);

        return documentoCreado;
    }

    /**
     * Obtiene el CSV de un {@link es.caib.plugins.arxiu.api.Document}
     * @param identificadorDocumento
     * @return
     * @throws I18NException
     */
    public String getCsv(String identificadorDocumento) throws I18NException{
        return getArxiuPlugin().getCsv(identificadorDocumento);
    }

    /**
     * Obtiene la Url de Printable (CSV incrustado) {@link es.caib.plugins.arxiu.api.Document}
     * @param identificadorDocumento
     * @return
     * @throws I18NException
     */
    public String getUrlPrintable(String identificadorDocumento) throws I18NException{
        return getPropertyConCsvUrl(identificadorDocumento);
    }

    /**
     * Obtiene la Url de la Web Validacion CSV {@link es.caib.plugins.arxiu.api.Document}
     * @param identificadorDocumento
     * @return
     * @throws I18NException
     */
    public String getCsvValidationWeb(String identificadorDocumento) throws I18NException{
        return getPropertyCsvValidationWeb(identificadorDocumento);
    }


    /**
     * 
     * @param uuidDocument
     * @param version
     * @param contenido
     * @param original
     * @return
     * @throws I18NException
     */
    public Document getDocumento(String uuidDocument, String version, Boolean contenido, Boolean original) throws I18NException{

        Document documento = null;

        try{

            if(!contenido){ // Solo información del Documento, sin contenido
                return getArxiuPlugin().documentDetalls(uuidDocument, version, false);

            }else if(original){ // Información + Contenido  del Documento original
                return getArxiuPlugin().documentDetalls(uuidDocument, version, true);

            }else { // Información + Contenido desde la PrintableUrl (CSV incrustado)

                documento = getArxiuPlugin().documentDetalls(uuidDocument, version, true); // todo Hay que obtener el contenido, porque si no no viene el tipo Mime del fichero

                log.info("Obteniendo el documento desde url validacion: " + uuidDocument);

                byte[] data = null;

                try{

                    String url = getUrlPrintable(documento.getIdentificador());
                    String username = getPropertyConCsvUsername();
                    String password = getPropertyConCsvPassword();

                    if(StringUtils.isNotEmpty(url)){
                        // Descargamos el Justificante desde la url de validación
                        data = ClientUtils.descargarArchivoUrl(url, username, password);
                        documento.getContingut().setContingut(data);
                    }

                }catch (Exception e){
                    log.info("Error obteniendo el documento desde la url de validacion");
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            log.info("Error obteniendo el Documento: " + uuidDocument);
            e.printStackTrace();
        }

        return documento;
    }

    /**
     * Obtiene un Expediente a partir de su uuid
     * @param uuidExpedient
     * @param version
     * @return
     * @throws I18NException
     */
    public Expedient getExpediente(String uuidExpedient, String version) throws I18NException{

        Expedient expedient = null;

        try{
            expedient =  arxiuPlugin.expedientDetalls(uuidExpedient, version);

        }catch (Exception e){
            log.info("Error obteniendo el Expedient: " + uuidExpedient);
            e.printStackTrace();
        }

        return expedient;
    }

    /**
     * Elimina el Expediente y sus documentos
     * @param idExpediente
     */
    public void eliminarExpediente(String idExpediente){


        try {

            getArxiuPlugin().expedientEsborrar(idExpediente);

        } catch (Exception e) {
            log.info("Error eliminando el expediente " + idExpediente);
            e.printStackTrace();
        }

    }

    /**
     * Elimina el Justificante (Expediente + Documento)
     * @param uuidExpedient
     * @param uuidDocument
     */
    public void eliminarJustificanteFS(String uuidExpedient, String uuidDocument) throws I18NException{


            log.info("Eliminamos el JustificanteArxiu (Expediente + Documento)");

            //Eliminamos el documento
            //log.info("Eliminamos el documento: " + uuidDocument);
            //getArxiuPlugin().documentEsborrar(uuidDocument);

            //Eliminamos el expediente creado
            log.info("Eliminamos el expediente: " + uuidExpedient);
            getArxiuPlugin().expedientEsborrar(uuidExpedient);

    }


    /**
     * Genera un {@link es.caib.plugins.arxiu.api.Expedient} a partir de los parámetros
     * @param identificador
     * @param nombre
     * @param ntiIdentificador
     * @param ntiOrgans
     * @param ntiDataObertura
     * @param ntiClassificacio
     * @param expedientEstat
     * @param ntiInteressats
     * @param serieDocumental
     * @return
     */
    private Expedient generarExpediente(String identificador,
                                        String nombre,
                                        String ntiIdentificador,
                                        List<String> ntiOrgans,
                                        Date ntiDataObertura,
                                        String ntiClassificacio,
                                        ExpedientEstat expedientEstat,
                                        List<String> ntiInteressats,
                                        String serieDocumental, String aplicacion) {

        Expedient expedient = new Expedient();
        expedient.setNom(nombre);
        expedient.setIdentificador(identificador);

        // Metadates
        ExpedientMetadades metadades = new ExpedientMetadades();
        metadades.setIdentificador(ntiIdentificador);
        metadades.setDataObertura(ntiDataObertura);
        metadades.setClassificacio(ntiClassificacio);
        metadades.setEstat(expedientEstat);
        metadades.setOrgans(ntiOrgans);
        metadades.setInteressats(ntiInteressats);
        metadades.setSerieDocumental(serieDocumental);

        // Metadates adicionals
        Map<String, Object> metaDadesAddicionals = new HashMap<String, Object>();
        metaDadesAddicionals.put("eni:app_tramite_exp", aplicacion);
        metadades.setMetadadesAddicionals(metaDadesAddicionals);


        expedient.setMetadades(metadades);

        return expedient;
    }

    /**
     * Genera un {@link es.caib.plugins.arxiu.api.Document} que representa un Justificante
     * @param registro
     * @param tipoRegistro
     * @param firma
     * @return
     * @throws I18NException
     */
    private Document generarDocumentoJustificante(IRegistro registro, Integer tipoRegistro, String serieDocumental, Firma firma, DocumentEstat estadoDocumento) throws I18NException {

        // Documento
        Document document = new Document();
        document.setIdentificador(null);
        document.setEstat(estadoDocumento);

        // Metadatos
        DocumentMetadades metadades = new DocumentMetadades();
        metadades.setIdentificador(null);
        metadades.setSerieDocumental(serieDocumental);
        metadades.setOrgans(Arrays.asList(registro.getOficina().getCodigo()));
        metadades.setDataCaptura(new Date());

        metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
        metadades.setEstatElaboracio(DocumentEstatElaboracio.ORIGINAL);
        metadades.setTipusDocumental(DocumentTipus.ALTRES); // TODO Revisar si sería más conveniente poner DocumentTipus.JUSTIFICANT_RECEPCIO

        metadades.setExtensio(DocumentExtensio.PDF);
        metadades.setFormat(DocumentFormat.PDF);

        // Contenido y Firma
        document.setContingut(null);
        document.setNom(firma.getFitxerNom());

        document.setFirmes(new ArrayList<Firma>());
        document.getFirmes().add(firma);

        // Metadatos adicionales
        metadades.setMetadadesAddicionals(getMedadadesAdicionals(registro, tipoRegistro));

        document.setMetadades(metadades);

        return document;
    }

    /**
     * Genera un {@link es.caib.plugins.arxiu.api.Document} a partir de los parámetros
     * @param registro
     * @param tipoRegistro
     * @param anexoFull
     * @return
     * @throws I18NException
     */
    private Document generarDocumento(IRegistro registro, Integer tipoRegistro, AnexoFull anexoFull, String serieDocumental) throws I18NException {

        // Documento
        Document document = new Document();
        document.setIdentificador(null);
        document.setEstat(DocumentEstat.DEFINITIU);

        // Metadatos
        DocumentMetadades metadades = new DocumentMetadades();
        metadades.setIdentificador(null);
        metadades.setSerieDocumental(serieDocumental);
        metadades.setOrgans(Arrays.asList(registro.getOficina().getCodigo()));
        metadades.setDataCaptura(anexoFull.getAnexo().getFechaCaptura());

        // Metadata Origen
        switch (anexoFull.getAnexo().getOrigenCiudadanoAdmin()) {
            case 0:
                metadades.setOrigen(ContingutOrigen.CIUTADA);
                break;
            case 1:
                metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
                break;
        }

        // Metadata Estado elaboración
        //SICRES4 Deprecated
       /* switch (anexoFull.getAnexo().getValidezDocumento().intValue()) {

            case 1: // Tipo Validez Doc: Copia
                metadades.setEstatElaboracio(DocumentEstatElaboracio.ALTRES);
                break;
            case 2: // Tipo Validez Doc: Copia compulsada -> se convierte en Copia
                metadades.setEstatElaboracio(DocumentEstatElaboracio.ALTRES);
                break;
            case 3: // Tipo Validez Doc: Copia original
                metadades.setEstatElaboracio(DocumentEstatElaboracio.COPIA_DP);
                break;
            case 4: // Tipo Validez Doc: Original
                metadades.setEstatElaboracio(DocumentEstatElaboracio.ORIGINAL);
                break;
        }*/

        metadades.setTipusDocumental(DocumentTipus.toEnum(anexoFull.getAnexo().getTipoDocumental().getCodigoNTI()));

        // Metadata Extension
        DocumentExtensio extension = DocumentExtensio.toEnum("." + anexoFull.getExtension());
        if (extension != null) {
            metadades.setExtensio(extension);
            metadades.setFormat(getDocumentFormat(extension));
        }

        // Contingut
        DocumentContingut contingut = new DocumentContingut();

        if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

            document.setNom(anexoFull.getDocumentoCustody().getName());

            // Documento sin firma
            contingut.setArxiuNom(anexoFull.getDocumentoCustody().getName());
            contingut.setContingut(anexoFull.getDocumentoCustody().getData());
            contingut.setTipusMime(anexoFull.getDocMime());
            contingut.setTamany(anexoFull.getDocSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior


        } else if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {

            document.setNom(anexoFull.getSignatureCustody().getName());

            // Documento y Firma attached
            Firma firma = getFirma(anexoFull);
            document.setFirmes(new ArrayList<Firma>());
            document.getFirmes().add(firma);

            contingut = null;


        } else if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

            document.setNom(anexoFull.getDocumentoCustody().getName());

            // Documento
            contingut.setArxiuNom(anexoFull.getDocumentoCustody().getName());
            contingut.setContingut(anexoFull.getDocumentoCustody().getData());
            contingut.setTipusMime(anexoFull.getDocMime());
            contingut.setTamany(anexoFull.getDocSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior

            // Firma detached
            Firma firma = getFirma(anexoFull);
            document.setFirmes(new ArrayList<Firma>());
            document.getFirmes().add(firma);

        }

        document.setContingut(contingut);

        // Metadatos adicionales
        metadades.setMetadadesAddicionals(getMedadadesAdicionals(registro, tipoRegistro));

        document.setMetadades(metadades);

        return document;
    }


    /**
     * Obtiene una {@link es.caib.plugins.arxiu.api.Firma} a partir de los parámetros
     * @param anexoFull
     * @return
     * @throws I18NException
     */
    private Firma getFirma(AnexoFull anexoFull) throws I18NException {

        Firma firma = new Firma();
        firma.setFitxerNom(anexoFull.getSignatureCustody().getName());
        firma.setContingut(anexoFull.getSignatureCustody().getData());
        firma.setCsvRegulacio("");
        firma.setTamany(anexoFull.getSignatureCustody().getData().length);

        firma.setPerfil(getFirmaPerfil(anexoFull.getAnexo().getSignProfile()));
        firma.setTipus(getFirmaTipus(anexoFull.getAnexo()));

        firma.setTipusMime(anexoFull.getSignatureCustody().getMime());

        return firma;
    }

    /**
     *  ${registro.libro.codigo}-<#if registro.origen??>S<#else>E</#if>-${registro.numeroRegistro?string[\"0\"]}_${(registro.fecha)?string[\"yyyy\"]}
     * @param registro
     * @return
     * @throws I18NException
     */
    public String getNombreExpediente(IRegistro registro, Long tipoRegistro) throws I18NException{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        return registro.getLibro().getCodigo() +"-"+ getTipoRegistro(tipoRegistro) +"-"+ registro.getNumeroRegistro() +"_"+ sdf.format(registro.getFecha());

    }

    /**
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    private String getTipoRegistro(Long tipoRegistro) throws I18NException {

        if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
            return "E";
        } else {
            return "S";
        }

    }

    public Integer getTipoRegistroEni(Long tipoRegistro) throws I18NException{

        if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     *
     * @param registro
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    private Map<String, Object> getMedadadesAdicionals(IRegistro registro, Integer tipoRegistro) throws I18NException{

        Map<String, Object> metaDadesAddicionals = new HashMap<String, Object>();

        metaDadesAddicionals.put("eni:numero_asiento_registral", registro.getNumeroRegistroFormateado());
        if (registro.getFecha() != null) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(tz);
            metaDadesAddicionals.put("eni:fecha_asiento_registral", df.format(registro.getFecha()));
        }
        metaDadesAddicionals.put("eni:codigo_oficina_registro", registro.getOficina().getCodigo());
        metaDadesAddicionals.put("eni:tipo_asiento_registral", tipoRegistro);
        metaDadesAddicionals.put("eni:app_tramite_exp", RegwebConstantes.APLICACION_NOMBRE);

        return metaDadesAddicionals;
    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.FirmaPerfil} a partir de los parámetros
     * @param singProfile
     * @return
     * @throws I18NException
     */
    private FirmaPerfil getFirmaPerfil(String singProfile) throws I18NException{

        if(ValidateSignatureConstants.SIGNPROFILE_BES.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_X1.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_X2.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_XL1.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_XL2.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_PADES_BASIC.equals(singProfile)){
            return FirmaPerfil.BES;
        }else if(ValidateSignatureConstants.SIGNPROFILE_EPES.equals(singProfile)){
            return FirmaPerfil.EPES;
        }else  if(ValidateSignatureConstants.SIGNPROFILE_PADES_LTV.equals(singProfile)){
            return FirmaPerfil.LTV;
        }else if(ValidateSignatureConstants.SIGNPROFILE_T.equals(singProfile)){
            return FirmaPerfil.T;
        }else  if(ValidateSignatureConstants.SIGNPROFILE_C.equals(singProfile)){
            return FirmaPerfil.C;
        }else if(ValidateSignatureConstants.SIGNPROFILE_X.equals(singProfile)){
            return FirmaPerfil.X;
        }else if(ValidateSignatureConstants.SIGNPROFILE_XL.equals(singProfile)){
            return FirmaPerfil.XL;
        }else  if(ValidateSignatureConstants.SIGNPROFILE_A.equals(singProfile)){
            return FirmaPerfil.A;
        }

        return null;
    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.FirmaTipus} a partir de los parámetros
     * @param anexo
     * @return
     * @throws I18NException
     */
    private FirmaTipus getFirmaTipus(Anexo anexo) throws I18NException{

        if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) &&
                (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat()) || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF02

            return FirmaTipus.XADES_DET;
        }else if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) &&
                ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF03

            return FirmaTipus.XADES_ENV;
        }else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) &&
                (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat()) || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF04

            return FirmaTipus.CADES_DET;
        } else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) &&
                ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF05
            return FirmaTipus.CADES_ATT;
        } else if (ValidateSignatureConstants.SIGNTYPE_PAdES.equals(anexo.getSignType())) {//TF06

            return FirmaTipus.PADES;
        } else  if (ValidateSignatureConstants.SIGNTYPE_ODF.equals(anexo.getSignType())) { //TF08

            return FirmaTipus.ODT;
        }else  if (ValidateSignatureConstants.SIGNTYPE_OOXML.equals(anexo.getSignType())) { //TF09

            return FirmaTipus.OOXML;
        }

        return null;

    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.DocumentFormat} a partir de los parámetros
     * @param extension
     * @return
     */
    private DocumentFormat getDocumentFormat(DocumentExtensio extension){

        switch (extension) {
            case AVI: return DocumentFormat.AVI;
            case CSS: return DocumentFormat.CSS;
            case CSV: return DocumentFormat.CSV;
            case DOCX: return DocumentFormat.SOXML;
            case GML: return DocumentFormat.GML;
            case GZ: return DocumentFormat.GZIP;
            case HTM: return DocumentFormat.XHTML; // HTML o XHTML!!!
            case HTML: return DocumentFormat.XHTML; // HTML o XHTML!!!
            case JPEG: return DocumentFormat.JPEG;
            case JPG: return DocumentFormat.JPEG;
            case MHT: return DocumentFormat.MHTML;
            case MHTML: return DocumentFormat.MHTML;
            case MP3: return DocumentFormat.MP3;
            case MP4: return DocumentFormat.MP4V; // MP4A o MP4V!!!
            case MPEG: return DocumentFormat.MP4V; // MP4A o MP4V!!!
            case ODG: return DocumentFormat.OASIS12;
            case ODP: return DocumentFormat.OASIS12;
            case ODS: return DocumentFormat.OASIS12;
            case ODT: return DocumentFormat.OASIS12;
            case OGA: return DocumentFormat.OGG;
            case OGG: return DocumentFormat.OGG;
            case PDF: return DocumentFormat.PDF; // PDF o PDFA!!!
            case PNG: return DocumentFormat.PNG;
            case PPTX: return DocumentFormat.SOXML;
            case RTF: return DocumentFormat.RTF;
            case SVG: return DocumentFormat.SVG;
            case TIFF: return DocumentFormat.TIFF;
            case TXT: return DocumentFormat.TXT;
            case WEBM: return DocumentFormat.WEBM;
            case XLSX: return DocumentFormat.SOXML;
            case ZIP: return DocumentFormat.ZIP;
            case CSIG: return DocumentFormat.CSIG;
            case XSIG: return DocumentFormat.XSIG;
            case XML: return DocumentFormat.XML;
        }
        return null;
    }

    /**
     *
     * @return
     * @throws I18NException
     */
    private IArxiuPlugin getArxiuPlugin() throws I18NException {

        return arxiuPlugin;

    }

    /**
     * Asociamos el plugin de Arxiu
     * @param iArxiuPlugin
     */
    public void setArxiuPlugin(IArxiuPlugin iArxiuPlugin) {

        this.arxiuPlugin = iArxiuPlugin;
    }

    /**
     *
     * @param properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    private String getPropertyAplicacio() throws I18NException {
        return getPropertyRequired(PROPERTY_APLICACION);
    }

    private String getPropertySerieDocumental() throws I18NException {
        return getPropertyRequired(PROPERTY_SERIE_DOCUMENTAL);
    }

    private String getPropertyCodigoProcedimiento() throws I18NException {
        return getPropertyRequired(PROPERTY_CODIGO_PROCEDIMIENTO);
    }

    private String getPropertyNombreProcedimiento() throws I18NException {
        return getPropertyRequired(PROPERTY_NOMBRE_PROCEDIMIENTO);
    }

    private String getPropertyConCsvUrl(String custodyId) throws I18NException {
        String url = getProperty(PROPERTY_CONCSV_URL);

        if(StringUtils.isNotEmpty(url)){
            return url.concat(custodyId);
        }

        return null;
    }

    private String getPropertyCsvValidationWeb(String custodyId) throws I18NException {
        String url = getProperty(PROPERTY_CSV_VALIDATION_URL);

        if(StringUtils.isNotEmpty(url)){
            return url.concat(custodyId);
        }

        return null;
    }

    private String getPropertyConCsvUsername() throws I18NException {
        return getPropertyRequired(PROPERTY_CONCSV_USERNAME);
    }

    private String getPropertyConCsvPassword() throws I18NException {
        return getPropertyRequired(PROPERTY_CONCSV_PASSWORD);
    }

    private Boolean getPropertyCerrarExpediente() throws I18NException {
        String propiedad = getProperty(PROPERTY_CERRAR_EXPEDIENTE);

        return "true".equals(propiedad);
    }

    public final String getProperty(String name) throws I18NException{

        return properties.getProperty(name);
    }

    public final String getPropertyRequired(String name) throws I18NException{

        String value = properties.getProperty(name);

        if (value == null) {
            throw new I18NException("Property " + name + " is required but it has not defined in the Properties");
        } else {
            return value;
        }
    }

}
