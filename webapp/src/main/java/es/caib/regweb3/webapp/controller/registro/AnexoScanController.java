package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.persistence.ejb.ScanWebModuleLocal;
import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.api.ScanWebMode;
import org.fundaciobit.plugins.scanweb.api.ScannedDocument;
import org.fundaciobit.plugins.utils.Metadata;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 */
@Controller
@RequestMapping(value = "/anexoScan")
public class AnexoScanController extends AnexoController {


    @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
    private ScanWebModuleLocal scanWebModuleEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    private RegistroDetalleLocal registroDetalleEjb;


    @RequestMapping(value = "/new/{registroDetalleID}/{tipoRegistro}/{registroID}/{isOficioRemisionSir}", method = RequestMethod.GET)
    public String crearAnexoGet(HttpServletRequest request,
                              HttpServletResponse response, @PathVariable Long registroDetalleID,
                              @PathVariable String tipoRegistro, @PathVariable Long registroID, @PathVariable Boolean isOficioRemisionSir,
                              Model model) throws I18NException, Exception {

        log.info(" Passa per AnexoScanController::ficherosGet(" + registroDetalleID
                + "," + tipoRegistro + ", " + registroID + ")");


        saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, null, isOficioRemisionSir);

        RegistroDetalle registroDetalle = registroDetalleEjb.findById(registroDetalleID);

        AnexoForm anexoForm = new AnexoForm();
        anexoForm.setRegistroID(registroID);
        anexoForm.setTipoRegistro(tipoRegistro);
        anexoForm.getAnexo().setRegistroDetalle(registroDetalle);
        anexoForm.setOficioRemisionSir(isOficioRemisionSir);
        model.addAttribute("anexoForm" ,anexoForm);
        loadCommonAttributesScan(request, model,anexoForm.getRegistroID());
        model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO_ENVIO);

        return "registro/formularioAnexoScan";
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String crearAnexoPost(@ModelAttribute AnexoForm anexoForm,
                               BindingResult result, HttpServletRequest request,
                               HttpServletResponse response, Model model) throws Exception,I18NException {
        //XYZ
        log.info("llego al post de scan");

        boolean isSIR = anexoForm.getOficioRemisionSir();
        try {
            //loadCommonAttributes(request, model);
            manageDocumentCustodySignatureCustody(request, anexoForm);
            
            if (isSIR) {
                String docExtension="";
                String firmaExtension="";
                long docSize=-1;
                long firmaSize=-1;
                if(anexoForm.getDocumentoCustody()!=null) {
                    docExtension = AnexoUtils.obtenerExtensionAnexo(anexoForm.getDocumentoCustody().getName());
                    docSize = anexoForm.getDocumentoCustody().getLength();
                }
                if(anexoForm.getSignatureCustody()!=null){
                    firmaExtension = AnexoUtils.obtenerExtensionAnexo(anexoForm.getSignatureCustody().getName());
                    firmaSize = anexoForm.getSignatureCustody().getLength();
                }

                validarLimitacionesSIRAnexos(anexoForm.getRegistroID(), anexoForm.tipoRegistro, docSize, firmaSize, docExtension, firmaExtension, result, true);
            }


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


        return "redirect:/anexoScan/new/" + anexoForm.getAnexo().getRegistroDetalle().getId() + "/" + anexoForm.getTipoRegistro() + "/" + anexoForm.getRegistroID()+ "/" + isSIR;

    }

    protected void loadCommonAttributesScan(HttpServletRequest request, Model model,
                                        Long registroID) throws Exception, I18NException {

        loadCommonAttributes(request,model);

        // Scan
        Entidad entidad = getEntidadActiva(request);
        final long entitatID = entidad.getId();
        
        boolean teScan = scanWebModuleEjb.entitatTeScan(entitatID);
        model.addAttribute("teScan", teScan);


        if (teScan) {

            String languageUI = request.getParameter("lang");

            if (languageUI == null) {
                languageUI = I18NUtils.getLocale().getLanguage();

            }
            request.setAttribute("lang", languageUI);

            // Utilitzam l'ID del registre per escanejar
            final long scanWebID = registroID;

            String urlToPluginWebPage = initializeScan(request, entitatID, scanWebID, languageUI);

            model.addAttribute("urlToPluginWebPage", urlToPluginWebPage);
        }

    }

    private String initializeScan(HttpServletRequest request, long entitatID, final long scanWebID,
                                  String languageUI) throws Exception, I18NException {


        final String scanType = IScanWebPlugin.SCANTYPE_PDF;

        // 'null' significa obtenir la configuració per defecte del plugin
        final Set<String> flags = null;

        // TODO per ara l'entrada de metadades està buida
        final List<Metadata> metadades = new ArrayList<Metadata>();

        final ScanWebMode mode = ScanWebMode.ASYNCHRONOUS;

        // Si és asincron no hi ha necessitat de urlFinal
        final String urlFinal = null;

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
        urlToPluginWebPage = scanWebModuleEjb.scanDocument(request,
                absoluteRequestPluginBasePath, relativeRequestPluginBasePath, scanWebID);
        return urlToPluginWebPage;
    }

    /**
     * Método que prepara el DocumentCustody y el Signature Custody de un anexo
     *
     * @param request
     * @param anexoForm
     * @throws Exception
     * @throws I18NException
     */
    protected void manageDocumentCustodySignatureCustody(
            HttpServletRequest request,  AnexoForm anexoForm) throws Exception, I18NException {


        final Long registroID = anexoForm.getRegistroID();

        DocumentCustody dc = null;

        SignatureCustody sc = null;


        final Long scanWebID = registroID;

        ScanWebConfigRegWeb config = scanWebModuleEjb.getScanWebConfig(request, scanWebID);


        //Tratamiento de los documentos obtenidos del scanner
        if (config != null &&  config.getScannedFiles().size() != 0) {


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

            if (log.isDebugEnabled()) {
                log.debug("NOU MODE DE FIRMA: " + modoFirma);
            }
            anexoForm.getAnexo().setModoFirma(modoFirma);

            anexoForm.setMetadatas(sd.getMetadatas());

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
