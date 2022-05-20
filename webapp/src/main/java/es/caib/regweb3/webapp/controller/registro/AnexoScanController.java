package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;
import org.fundaciobit.pluginsib.scanweb.api.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static es.caib.regweb3.utils.StringUtils.eliminarCaracteresProhibidosArxiu;

/**
 * Created by mgonzalez on 04/05/2017.
 * Controller que se encarga de gestionar los anexos introducidos via scan
 */
@Controller
@RequestMapping(value = "/anexoScan")
public class AnexoScanController extends AnexoController {


    @EJB(mappedName = ScanWebModuleLocal.JNDI_NAME)
    private ScanWebModuleLocal scanWebModuleEjb;

    @EJB(mappedName = TipoDocumentalLocal.JNDI_NAME)
    private TipoDocumentalLocal tipoDocumentalEjb;



    // Prepara el anexo Form para escaneo simple
    @RequestMapping(value = "/new/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String crearAnexoGet(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable Long registroDetalleID,
                                @PathVariable Long tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                                Model model) throws I18NException, Exception {

        //Obtenemos la url que nos pasa el jsp por parámetro
        String scanwebAbsoluteurl= request.getParameter("scanweb_absoluteurl");
        //Cogemos solo hasta el contexto web.
        String scanwebAbsoluteurlBase = getUrlBaseFromFullUrl(request,scanwebAbsoluteurl);
        //La guardamos en sessión para que la use ScanRequestServlet
        request.getSession().setAttribute("scanwebAbsoluteurlBase", scanwebAbsoluteurlBase);

       //Actualiza las variables con la ultima acción y prepara el anexoForm
        AnexoForm anexoForm = prepararAnexoForm(request, registroDetalleID, tipoRegistro, registroID, isOficioRemisionSir, true);
        request.getSession().setAttribute("anexoForm", anexoForm);
        loadCommonAttributesScan(request, model, anexoForm.getRegistroID(),scanwebAbsoluteurlBase);

        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
        model.addAttribute("anexoForm", anexoForm);

        return "registro/formularioAnexoScan";

    }

    //Prepara el anexo Form para escaneo masivo
    @RequestMapping(value = "/new/masivo/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String crearAnexoMasivoGet(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable Long registroDetalleID,
                                @PathVariable Long tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                                Model model) throws I18NException, Exception {

        //Obtenemos la url que nos pasa el jsp por parámetro.
        // Esto sustituye a la propiedad global es.caib.regweb3.scanweb.absoluteurl
        String scanwebAbsoluteurl= request.getParameter("scanweb_absoluteurl");
        //Cogemos solo hasta el contexto web.
        String scanwebAbsoluteurlBase = getUrlBaseFromFullUrl(request,scanwebAbsoluteurl);
        //La guardamos en sessión para que la use ScanRequestServlet
        request.getSession().setAttribute("scanwebAbsoluteurlBase", scanwebAbsoluteurlBase);

        //Actualiza las variables con la ultima acción y prepara el anexoForm
        AnexoForm anexoForm = prepararAnexoForm(request, registroDetalleID, tipoRegistro, registroID, isOficioRemisionSir, true);
        request.getSession().setAttribute("anexoForm", anexoForm);
        loadCommonAttributesScan(request, model, anexoForm.getRegistroID(),scanwebAbsoluteurlBase);

        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
        model.addAttribute("anexoForm", anexoForm);

        return "registro/formularioAnexoScanMasivo";

    }


    /**
     * Método que transforma los datos de los documentos recibidos a través del request para que se pueda guardar como anexo.
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @RequestMapping(value = "/transforma")
    public String crearAnexoPost(HttpServletRequest request) throws Exception, I18NException {

        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");
        boolean isSIR = anexoForm.getOficioRemisionSir();
        List<ScanWebDocument> documentosEscaneados = (List<ScanWebDocument>) request.getSession().getAttribute("documentosEscaneados");


        try {
            // Si quedan documentos por transformar
            if(anexoForm.getNumAnexosRecibidos()>0 && anexoForm.getNumDocumento() <= documentosEscaneados.size()-1) {
                ScanWebDocument documentoEscaneado = documentosEscaneados.get(anexoForm.getNumDocumento());
                //Transformamos el DocumentCustody y SignatureCustody de lo que nos envia el anexoForm
                manageDocumentCustodySignatureCustody(request, anexoForm, documentoEscaneado);
                //Actualizamos el número de documentos procesados
                anexoForm.setNumDocumento(anexoForm.getNumDocumento()+1);
            }


            //Obtenemos tamanyo máximo permitido
            Long maxUploadSizeInBytes;
            Long entidadID = getEntidadActiva(request).getId();
            if(entidadID != null) {
                maxUploadSizeInBytes = PropiedadGlobalUtil.getMaxUploadSizeInBytes(entidadID);
            }else {
                maxUploadSizeInBytes = PropiedadGlobalUtil.getMaxUploadSizeInBytes();
            }

            //Si hay tamaño máximo permitido o isSIR hay que comprobar los tamanyos y extensiones
            if(maxUploadSizeInBytes != null || isSIR){
                String docExtension = "";
                String firmaExtension = "";
                long docSize = -1;
                long firmaSize = -1;
                //obtenemos tamaño y extensión del documento
                if (anexoForm.getDocumentoCustody() != null) {
                    docExtension = AnexoUtils.obtenerExtensionAnexo(anexoForm.getDocumentoCustody().getName());
                    docSize = anexoForm.getDocumentoCustody().getLength();
                }
                //obtenemos tamaño y extensión de la firma
                if (anexoForm.getSignatureCustody() != null) {
                    firmaExtension = AnexoUtils.obtenerExtensionAnexo(anexoForm.getSignatureCustody().getName());
                    firmaSize = anexoForm.getSignatureCustody().getLength();
                }

                //Validadmos Tamanyo máximo permitido
                if(maxUploadSizeInBytes !=null) {
                    validarMaxUploadSize( docSize, firmaSize, maxUploadSizeInBytes);
                }

                if(isSIR) {
                    //validamos las limitaciones SIR
                    validarLimitacionesSIRAnexos(anexoForm.getRegistroID(), anexoForm.getTipoRegistro(), docSize, firmaSize, docExtension, firmaExtension, null, true);
                }

            }

            //Validamos la firma del anexoForm que nos indican, previo a crear el anexo
            validarAnexoForm(request, anexoForm);


            request.getSession().setAttribute("anexoForm", anexoForm);
            return "redirect:/anexo/nou2";

        } catch (I18NException i18n) {
            String msg = I18NUtils.tradueix(i18n.getTraduccio());
            log.error(msg, i18n);
            Mensaje.saveMessageError(request, msg);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Mensaje.saveMessageError(request, e.getMessage());
        }


        return "redirect:/anexoScan/new/" + anexoForm.getIdRegistroDetalle() + "/" + anexoForm.getTipoRegistro() + "/" + anexoForm.getRegistroID() + "/" + isSIR;

    }


    /**
     * Método que obtiene los documentos escaneados y define el número de documentos escaneados y fija el número de documento a procesar
     * @param request
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @RequestMapping(value = "/new")
    public String crearAnexo2Post(HttpServletRequest request) throws Exception, I18NException {

        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");
        try {
            //Documentos obtenidos del scan
            List<ScanWebDocument> documentosEscaneados = obtenerDocumentosEscaneados(request, anexoForm.getRegistroID());

            log.info("Documentos escaneados: " + documentosEscaneados.size());

            //Fijamos el número total de documentos escaneados
            anexoForm.setNumAnexosRecibidos(documentosEscaneados.size());

            //Fijamos el número actual de documento a procesar
            anexoForm.setNumDocumento(0);

            request.getSession().setAttribute("documentosEscaneados", documentosEscaneados);


        } catch (I18NException i18n) {
            String msg = I18NUtils.tradueix(i18n.getTraduccio());
            log.error(msg, i18n);
            Mensaje.saveMessageError(request, msg);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Mensaje.saveMessageError(request, e.getMessage());
        }

        return "redirect:/anexoScan/transforma";
}

    /**
     * Método que carga una serie de atributos comunes del scan
     *
     * @param request
     * @param model
     * @param registroID
     * @throws Exception
     * @throws I18NException
     */
    protected void loadCommonAttributesScan(HttpServletRequest request, Model model,
                                            Long registroID, String scanwebAbsoluteurlBase) throws Exception, I18NException {

        loadCommonAttributes(request, model);

        // Scan
        Entidad entidad = getEntidadActiva(request);
        final long entitatID = entidad.getId();

        boolean teScan = scanWebModuleEjb.entitatTeScan(entitatID);
        model.addAttribute("teScan", teScan);

        if (teScan) {//Si tiene scan
            String languageUI = I18NUtils.getLocale().getLanguage();

            // Utilitzam l'ID del registre per escanejar
            final String scanWebID = String.valueOf(registroID);

            String urlToPluginWebPage = initializeScan(request, entitatID, scanWebID, languageUI, scanwebAbsoluteurlBase);
            model.addAttribute("urlToPluginWebPage", urlToPluginWebPage);
        }

    }

    /**
     * Método que inicializa el plugin de escaneo
     *
     * @param request
     * @param entitatID
     * @param scanWebID
     * @param languageUI
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private String initializeScan(HttpServletRequest request, long entitatID, final String scanWebID,
                                  String languageUI, String scanwebAbsoluteurlBase) throws Exception, I18NException {

        //  Pasamos los datos del funcionario que realiza el escaneo en el list de metadades
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);


        final  String scanType = ScanWebDocument.SCANTYPE_MIME_PDF;

        // 'null' significa obtenir la configuració per defecte del plugin
        final String flag = ScanWebDocument.FLAG_SIGNED;


        final List<Metadata> metadades = new ArrayList<Metadata>();

        final ScanWebMode mode = ScanWebMode.SYNCHRONOUS;


        final String urlFinal =  scanwebAbsoluteurlBase + PublicScanWebController.CONTEXT_WEB + scanWebID;


        //final ScanWebMode mode = ScanWebMode.ASYNCHRONOUS;
        // Si és asincron no hi ha necessitat de urlFinal
        // final String urlFinal = null;

        // Vull suposar que abans de 10 minuts haurà escanejat
        Calendar caducitat = Calendar.getInstance();
        caducitat.add(Calendar.MINUTE, 10);

        long expiryTransaction = caducitat.getTimeInMillis();
        String transactionName = scanWebID;
        ScanWebRequestSignatureInfo signatureInfo = new ScanWebRequestSignatureInfo(usuarioEntidad.getNombreCompleto(),  usuarioEntidad.getUsuario().getDocumento(), oficinaActiva.getOrganismoResponsable().getCodigo());
        ScanWebRequest scanWebRequest = new ScanWebRequest( scanWebID, transactionName,  scanType,  flag,  mode,  languageUI,  usuarioEntidad.getUsuario().getIdentificador(),  urlFinal,  metadades, signatureInfo);

        ScanWebConfigRegWeb ss = new ScanWebConfigRegWeb(scanWebRequest,expiryTransaction);
        ss.setEntitatID(entitatID);


        //Obtenemos las url relativas y absolutas del sistema de escaner configurado
        String relativeRequestPluginBasePath = ScanRequestServlet.getRelativeRequestPluginBasePath(request,
                ScanRequestServlet.CONTEXTWEB, scanWebID);

        String absoluteRequestPluginBasePath = ScanRequestServlet.getAbsoluteRequestPluginBasePath(request,
                ScanRequestServlet.CONTEXTWEB, scanWebID, scanwebAbsoluteurlBase);

        scanWebModuleEjb.registerScanWebProcess(request, ss);

        if (log.isDebugEnabled()) {
            log.info("absoluteRequestPluginBasePath = " + absoluteRequestPluginBasePath);
            log.info("relativeRequestPluginBasePath = " + relativeRequestPluginBasePath);
        }

        String urlToPluginWebPage;

        //Invocamos el proceso de escaneo
        urlToPluginWebPage = scanWebModuleEjb.scanDocument(request,
                absoluteRequestPluginBasePath, relativeRequestPluginBasePath, scanWebID);
        return urlToPluginWebPage;
    }


    /**
     * Método que transforma un documento escaneado al formato DocumentCustody y SignatureCustody y metadatos
     * @param request
     * @param anexoForm
     * @param documento
     * @throws Exception
     * @throws I18NException
     */
    protected void manageDocumentCustodySignatureCustody(
       HttpServletRequest request, AnexoForm anexoForm, ScanWebDocument documento) throws Exception, I18NException {

        DocumentCustody dc = null;
        SignatureCustody sc = null;

        // Hem de modificar el Modo de Firma segons el que ens hagin enviat des de SCAN
        dc = documento.getScannedPlainFile();
        sc = documento.getScannedSignedFile();

        if (sc != null) {
            log.info(sc.getName());
        }

        anexoForm.getAnexo().setScan(true);

        //Fijamos el MODO DE FIRMA
        final int modoFirma;
        if (dc == null) {
            // Firma: PAdES, CAdES, XAdES
            // Document amb firma adjunta
            modoFirma = RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED;
        } else if (sc == null) {
            // Simple document
            modoFirma = RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA;
        } else {
            // Firma i document separat
            modoFirma = RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED;
        }

        //TODO ver si es así para todos o solo digitalIB (pendent d'en Felip)
        //modoFirma = RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED;

        if (log.isDebugEnabled()) {
            log.debug("NOU MODE DE FIRMA: " + modoFirma);
        }
        anexoForm.getAnexo().setModoFirma(modoFirma);


        //Fijamos el tipo de documento como "Documento Adjunto".
        anexoForm.getAnexo().setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
        //Fijamos la validez del documento como "COPIA"
        anexoForm.getAnexo().setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA);
        //Fecha de escaneo
        anexoForm.getAnexo().setFechaCaptura(documento.getScanDate());

        //Tipo Documental si viene informado
        if(documento.getDocumentType()!= null) {
            anexoForm.getAnexo().setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad(documento.getDocumentType(), getEntidadActiva(request).getId()));
        }


        //Asignamos las metadatas obtenidas del plugin de scan
        List<Metadata> metadatasScan = new ArrayList<>();
        if(documento.getDocumentLanguage() != null) {
            metadatasScan.add(new Metadata(MetadataConstants.EEMGDE_IDIOMA, documento.getDocumentLanguage()));
        }
        if(documento.getOcr() != null) {
            metadatasScan.add(new Metadata(MetadataConstants.OCR, documento.getOcr()));
        }

        if(documento.getPixelType() != null) {
            metadatasScan.add(new Metadata(MetadataConstants.EEMGDE_PROFUNDIDAD_COLOR, documento.getPixelType()));

        }
        if(documento.getPppResolution() != null) {
            metadatasScan.add(new Metadata(MetadataConstants.EEMGDE_RESOLUCION, documento.getPppResolution()));
        }

        if(documento.getScanDate() != null){
            metadatasScan.add(new Metadata(MetadataConstants.ENI_FECHA_INICIO, documento.getScanDate()));
        }

        if(documento.getPaperSize()!= null){
            metadatasScan.add(new Metadata(MetadataConstants.PAPER_SIZE, documento.getPaperSize()));
        }

        //Metadatas adicionales
        if(documento.getAdditionalMetadatas() != null){
            metadatasScan.addAll(documento.getAdditionalMetadatas());
        }

        anexoForm.setMetadatas(metadatasScan);

        //TODO Metadades del funcionari
        anexoForm.getAnexo().setTitulo(eliminarCaracteresProhibidosArxiu(documento.getTransactionName()));


        //Asignamos los valores de documentCustody y SignatureCustody en función de lo obtenido anteriormente.
        anexoForm.setDocumentoCustody(dc);
        anexoForm.setSignatureCustody(sc);


    }


    /**
     * Método que obtiene los documentos escaneados del plugin de scanweb
     * @param request
     * @param registroID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    protected List<ScanWebDocument> obtenerDocumentosEscaneados(HttpServletRequest request, Long registroID) throws Exception, I18NException{

        List<ScanWebDocument> docsEscaneados = null;

        //Recuperamos el identificador de escaneo
        final String scanWebID = String.valueOf(registroID);

        //Obtenemos el resultado del escaneo
        ScanWebConfigRegWeb config = scanWebModuleEjb.getScanWebConfig(request, scanWebID);
        ScanWebResult scanWebResult = config.getScanWebResult();


       // log.info("Error detectat REGWEB3: " + scanWebResult.getStatus().getErrorMsg());
        if (scanWebResult.getStatus().getErrorMsg() != null) {
            log.error(scanWebResult.getStatus().getErrorMsg());
            throw new I18NException("anexo.perfilscan.error", new I18NArgumentString(scanWebResult.getStatus().getErrorMsg()));
        }


        //Tratamiento de los documentos obtenidos del escaner
        if (scanWebResult != null && scanWebResult.getScannedDocuments().size() != 0) {

            docsEscaneados = scanWebResult.getScannedDocuments();

        }else{
            if (scanWebResult.getScannedDocuments().size() == 0) {
                throw new I18NException("anexo.error.noscanedfiles");
            }
        }

        if (config != null) {
            // tancam tant si hem emprat com si no SCAN
            scanWebModuleEjb.closeScanWebProcess(request, scanWebID);
        }

        return docsEscaneados;

    }

    public void validarMaxUploadSize( long docSize, long firmaSize, Long maxUploadSizeInBytes) throws I18NException {

        String sMaxUploadSizeInBytes= RegwebUtils.bytesToHuman(maxUploadSizeInBytes);
        if(maxUploadSizeInBytes!= null){ // Si no está especificada, se permite cualquier tamaño
            if(docSize>0) {
                if (docSize > maxUploadSizeInBytes) {
                    String tamanoDoc= RegwebUtils.bytesToHuman(docSize);
                    throw new I18NException("tamanyfitxerpujatsuperat", tamanoDoc, sMaxUploadSizeInBytes);

                }
            }else {
                if (firmaSize > maxUploadSizeInBytes) {
                    String tamanoDoc= RegwebUtils.bytesToHuman(firmaSize);
                    throw new I18NException("tamanyfitxerpujatsuperat", tamanoDoc, sMaxUploadSizeInBytes);

                }
            }
        }

    }
}
