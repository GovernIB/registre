package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.api.ScanWebMode;
import org.fundaciobit.plugins.scanweb.api.ScannedDocument;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.UserInfo;
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
import java.util.Set;

/**
 * Created by mgonzalez on 04/05/2017.
 * Controller que se encarga de gestionar los anexos introducidos via scan
 */
@Controller
@RequestMapping(value = "/anexoScan")
public class AnexoScanController extends AnexoController {


    @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
    private ScanWebModuleLocal scanWebModuleEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    private RegistroDetalleLocal registroDetalleEjb;




    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;


    @RequestMapping(value = "/new/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String crearAnexoGet(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable Long registroDetalleID,
                                @PathVariable Long tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                                Model model) throws I18NException, Exception {

        log.info(" Passa per AnexoScanController::ficherosGet(" + registroDetalleID
                + "," + tipoRegistro + ", " + registroID + ")");

       //Actualiza las variables con la ultima acción y prepara el anexoForm
        AnexoForm anexoForm = prepararAnexoForm(request, registroDetalleID, tipoRegistro, registroID, isOficioRemisionSir);
        request.getSession().setAttribute("anexoForm", anexoForm);
        loadCommonAttributesScan(request, model, anexoForm.getRegistroID());

        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_ENVIO);
        model.addAttribute("anexoForm", anexoForm);
        return "registro/formularioAnexoScan";
    }


    @RequestMapping(value = "/new")
    public String crearAnexoPost(HttpServletRequest request) throws Exception, I18NException {


        AnexoForm anexoForm = (AnexoForm) request.getSession().getAttribute("anexoForm");
        boolean isSIR = anexoForm.getOficioRemisionSir();
        try {
            //Preparamos el DocumentCustody y SignatureCustody de lo que nos envia el anexoForm
            manageDocumentCustodySignatureCustody(request, anexoForm);

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


        return "redirect:/anexoScan/new/" + anexoForm.getAnexo().getRegistroDetalle().getId() + "/" + anexoForm.getTipoRegistro() + "/" + anexoForm.getRegistroID() + "/" + isSIR;

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
                                            Long registroID) throws Exception, I18NException {

        loadCommonAttributes(request, model);

        // Scan
        Entidad entidad = getEntidadActiva(request);
        final long entitatID = entidad.getId();

        boolean teScan = scanWebModuleEjb.entitatTeScan(entitatID);
        model.addAttribute("teScan", teScan);


        if (teScan) {//Si tiene scan

            String languageUI = request.getParameter("lang");

            if (languageUI == null) {
                languageUI = I18NUtils.getLocale().getLanguage();

            }
            request.setAttribute("lang", languageUI);

            // Utilitzam l'ID del registre per escanejar
            final String scanWebID = String.valueOf(registroID);

            String urlToPluginWebPage = initializeScan(request, entitatID, scanWebID, languageUI);

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
                                  String languageUI) throws Exception, I18NException {


        final String scanType = IScanWebPlugin.SCANTYPE_PDF;

        // 'null' significa obtenir la configuració per defecte del plugin
        final Set<String> flags = null;

        final List<Metadata> metadades = new ArrayList<Metadata>();

        final ScanWebMode mode = ScanWebMode.SYNCHRONOUS;

        final String urlFinal = request.getContextPath() + "/anexoScan/new";

        //final ScanWebMode mode = ScanWebMode.ASYNCHRONOUS;
        // Si és asincron no hi ha necessitat de urlFinal
        // final String urlFinal = null;

        // Vull suposar que abans de 10 minuts haurà escanejat
        Calendar caducitat = Calendar.getInstance();
        caducitat.add(Calendar.MINUTE, 10);

        long expiryTransaction = caducitat.getTimeInMillis();

        ScanWebConfigRegWeb ss = new ScanWebConfigRegWeb(scanWebID, scanType, flags,
                metadades, mode, languageUI, urlFinal, expiryTransaction);
        ss.setEntitatID(entitatID);

        if (ss.getFlags() == null || ss.getFlags().size() == 0) {
            // Seleccionam el primer suportat
            Set<String> defaultFlags = scanWebModuleEjb.getDefaultFlags(ss);
            ss.setFlags(defaultFlags);
        }

        // Le pasamos en la configuración del escaner en los metadatos los datos del funcionario
        // que realiza el escaneo
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        ss.getMetadades().add(new Metadata("functionary.username" , usuarioEntidad.getUsuario().getIdentificador()));
        ss.getMetadades().add(new Metadata("functionary.fullname" , usuarioEntidad.getNombreCompleto()));
        ss.getMetadades().add(new Metadata("document.language" , RegwebConstantes.IDIOMA_CATALAN_CODIGO));
        String funcionariNif = "";
        if(usuarioEntidad.getUsuario().getDocumento() != null){
            ss.getMetadades().add(new Metadata("functionary.administrationid", usuarioEntidad.getUsuario().getDocumento()));
        }else{
            //Si no tiene documento lo recuperamos del sistema de información de usuario(seycon)
            IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION);
            UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(usuarioEntidad.getUsuario().getIdentificador());
            funcionariNif = regwebUserInfo.getAdministrationID();
            if(funcionariNif != null && !funcionariNif.isEmpty()) {
                ss.getMetadades().add(new Metadata("functionary.administrationid", funcionariNif));
            }
        }

        //Obtenemos las url relativas y absolutas del sistema de escaner configurado
        String relativeRequestPluginBasePath = ScanRequestServlet.getRelativeRequestPluginBasePath(request,
                ScanRequestServlet.CONTEXTWEB, scanWebID);

        String absoluteRequestPluginBasePath = ScanRequestServlet.getAbsoluteRequestPluginBasePath(request,
                ScanRequestServlet.CONTEXTWEB, scanWebID);

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
     * Método que prepara el DocumentCustody y el Signature Custody de un anexo que viene del escaner
     *
     * @param request
     * @param anexoForm
     * @throws Exception
     * @throws I18NException
     */
    protected void manageDocumentCustodySignatureCustody(
            HttpServletRequest request, AnexoForm anexoForm) throws Exception, I18NException {

        final Long registroID = anexoForm.getRegistroID();

        DocumentCustody dc = null;
        SignatureCustody sc = null;

        //Recuperamos el identificador de escaneo
        final String scanWebID = String.valueOf(registroID);

        //Obtenemos el resultado del escaneo
        ScanWebConfigRegWeb config = scanWebModuleEjb.getScanWebConfig(request, scanWebID);


       // log.info("Error detectat REGWEB3: " + config.getStatus().getErrorMsg());
        if (config.getStatus().getErrorMsg() != null) {
            throw new I18NException("anexo.perfilscan.error");
        }


        //Tratamiento de los documentos obtenidos del scanner
        if (config != null && config.getScannedFiles().size() != 0) {

            if (config.getScannedFiles().size() != 1) {
                throw new I18NException("anexo.error.scanmultiplefiles",
                        String.valueOf(config.getScannedFiles().size()));
            }

            List<ScannedDocument> listDocs = config.getScannedFiles();

            // Només processam el primer fitxer enviat
            ScannedDocument sd = listDocs.get(0);

            // Hem de modificar el Modo de Firma segons el que ens hagin enviat des de SCAN
            dc = sd.getScannedPlainFile();
            sc = sd.getScannedSignedFile();

            if(sc != null) {
                log.info(sc.getName());
            }

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

            //Asignamos las metadatas obtenidas del plugin de scan
            anexoForm.setMetadatas(sd.getMetadatas());

            //Fijamos el título del anexo si nos lo proporciona el plugin de scan
            for(Metadata metadata : sd.getMetadatas()){
                if(metadata.getKey().equals("title")){
                    anexoForm.getAnexo().setTitulo(metadata.getValue());
                }
            }

        }

        if (config != null) {
            // tancam tant si hem emprat com si no SCAN
            scanWebModuleEjb.closeScanWebProcess(request, scanWebID);
        }

        //Asignamos los valores de documentCustody y SignatureCustody en función de lo obtenido anteriormente.
        anexoForm.setDocumentoCustody(dc);
        anexoForm.setSignatureCustody(sc);


    }
}
