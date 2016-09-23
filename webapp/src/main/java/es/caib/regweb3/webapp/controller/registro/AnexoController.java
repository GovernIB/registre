package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.ScanWebConfigRegWeb;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.AnexoWebValidator;

import org.apache.axis.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.scanweb.api.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.api.ScanWebMode;
import org.fundaciobit.plugins.scanweb.api.ScannedDocument;
import org.fundaciobit.plugins.utils.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created 3/06/14 14:22
 *
 * @author mgonzalez
 * @author anadal (plugin de custodia, errors i refactoring no ajax)
 * @author anadal migracio a ScanWebApi 2.0.0 (06/07/2016)
 */
@Controller
@RequestMapping(value = "/anexo")
@SessionAttributes(types = {AnexoForm.class })
public class AnexoController extends BaseController {

    public static final int FILE_TAB_HEIGHT = 107;
  
    public static final int BASE_IFRAME_HEIGHT = 495 - FILE_TAB_HEIGHT;
   

    @Autowired
    private AnexoWebValidator anexoValidator;
    
    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    
    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;
    
    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;
    
    @EJB(mappedName = "regweb3/ScanWebModuleEJB/local")
    public ScanWebModuleLocal scanWebModuleEjb;
    
    /**
     *  Si arriba aqui és que hi ha un error de  Tamany de Fitxer Superat   
     */
    @RequestMapping(value = "/nou", method = RequestMethod.GET)
    public ModelAndView crearAnexoGet(HttpServletRequest request,
        HttpServletResponse response,  Model model) throws I18NException, Exception {
      
      HttpSession session = request.getSession();
      Long registroDetalleID = (Long)session.getAttribute("LAST_registroDetalleID");
      String tipoRegistro = (String)session.getAttribute("LAST_tipoRegistro");
      Long registroID = (Long)session.getAttribute("LAST_registroID");

      Long anexoID = (Long)session.getAttribute("LAST_anexoID");
      
      
      return new ModelAndView(new RedirectView("/anexo/" + (anexoID == null? "nou/" : "editar/") 
          + registroDetalleID + "/" + tipoRegistro + "/" + registroID + (anexoID == null? "" : ("/" + anexoID)), true));
    }
    
    /**
     *  Si arriba aqui és que hi ha un error de  Tamany de Fitxer Superat   
     */
    @RequestMapping(value = "/editar", method = RequestMethod.GET)
    public ModelAndView editarAnexoGet(HttpServletRequest request,
        HttpServletResponse response,  Model model) throws I18NException, Exception {
      return crearAnexoGet(request, response,   model);
    }
    

    @RequestMapping(value = "/nou/{registroDetalleID}/{tipoRegistro}/{registroID}", method = RequestMethod.GET)
    public String crearAnexoGet(HttpServletRequest request,
        HttpServletResponse response, @PathVariable Long registroDetalleID,
        @PathVariable String tipoRegistro, @PathVariable Long registroID, 
        Model model) throws I18NException, Exception {
      
      log.info(" Passa per AnexoController::crearAnexoGet(" + registroDetalleID 
          + "," + tipoRegistro + ", " + registroID + ")");
      
      
      saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, null);
     

      RegistroDetalle registroDetalle = registroDetalleEjb.findById(registroDetalleID);
     
      AnexoForm anexoForm = new AnexoForm();
      anexoForm.setRegistroID(registroID);
      anexoForm.setTipoRegistro(tipoRegistro);
      anexoForm.getAnexo().setRegistroDetalle(registroDetalle);
      model.addAttribute("anexoForm" ,anexoForm);
      // Anexos
      
      loadCommonAttributes(request, model, registroID);

      return "registro/formularioAnexo";
    }



    protected void saveLastAnnexoAction(HttpServletRequest request, Long registroDetalleID,
        Long registroID, String tipoRegistro, Long anexoID) {
      HttpSession session = request.getSession();
      session.setAttribute("LAST_registroDetalleID", registroDetalleID);
      session.setAttribute("LAST_tipoRegistro", tipoRegistro);
      session.setAttribute("LAST_registroID", registroID);
      session.setAttribute("LAST_anexoID", anexoID); // nou = null o editar != null
    }



    protected void loadCommonAttributes(HttpServletRequest request, Model model,
        Long registroID) throws Exception {
      model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
      model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
      model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);
      model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
      
      
      // Scan
      Entidad entidad = getEntidadActiva(request);
      Long pluginID = null;
      if (entidad.getTipoScan() != null && !"".equals(entidad.getTipoScan())) {
        pluginID = Long.parseLong(entidad.getTipoScan());
      }
      //      Integer tipusScan = 2;
      boolean teScan = scanWebModuleEjb.teScan(pluginID);
      model.addAttribute("teScan", teScan);
      
      if (teScan) {
        
        String languageUI = request.getParameter("lang");
        
        if (languageUI == null) {
          languageUI = I18NUtils.getLocale().getLanguage();
          
        }
        request.setAttribute("lang", languageUI);

        // Utilitzam l'ID del registre per escanejar  
        final long scanWebID = registroID;

        String urlToPluginWebPage = initializeScan(request, pluginID, scanWebID, languageUI);

        model.addAttribute("urlToPluginWebPage", urlToPluginWebPage);
      }
      
    }

    
    

    private String initializeScan(HttpServletRequest request, long pluginID, final long scanWebID,
        String languageUI) throws Exception {


      final String scanType = IScanWebPlugin.SCANTYPE_PDF;

      // 'null' significa obtenir la configuració per defecte del plugin
      final Set<String> flags = null;

      // TODO per ara l'entrada de metadades està buida
      final List<Metadata> metadades = new ArrayList<Metadata>();

      final ScanWebMode mode = ScanWebMode.ASYNCHRONOUS;

      // Si és asincron no hi ha necessitat de urlFinal
      final String urlFinal = null;

      // Vull suposar que abans de 3 minuts haurà escanejat
      Calendar caducitat = Calendar.getInstance();
      caducitat.add(Calendar.MINUTE, 3);

      long expiryTransaction = caducitat.getTimeInMillis();

      ScanWebConfigRegWeb ss = new ScanWebConfigRegWeb(scanWebID, scanType, flags,
          metadades, mode, languageUI, urlFinal, expiryTransaction);
      ss.setPluginID(pluginID);

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
    


    @RequestMapping(value = "/nou", method = RequestMethod.POST)
    public String crearAnexoPost(@ModelAttribute AnexoForm anexoForm,
        BindingResult result, HttpServletRequest request,
        HttpServletResponse response, Model model) throws Exception {
      
      log.info(" Passa per crearAnexoPost");

      anexoValidator.validate(anexoForm.getAnexo(),result);

      if (!result.hasErrors()) { // Si no hay errores

        try {
           manageDocumentCustodySignatureCustody(request, anexoForm);

           anexoEjb.crearAnexo(anexoForm, getUsuarioEntidadActivo(request),
               anexoForm.getRegistroID(), anexoForm.getTipoRegistro());

           model.addAttribute("closeAndReload", "true");

           return "registro/formularioAnexo";
           
        } catch(I18NValidationException i18n) {
          log.error(i18n.getMessage(), i18n);
          // TODO
          Mensaje.saveMessageError(request, i18n.getMessage());   
           
        } catch(I18NException i18n) {
          log.debug(i18n.getMessage(), i18n);
          Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio()));   
          
        } catch(Exception e) {
          log.error(e.getMessage(), e);
          Mensaje.saveMessageError(request, e.getMessage());
        }
      
      }

      // Errors 
      
      loadCommonAttributes(request, model, anexoForm.getRegistroID());

      return "registro/formularioAnexo";
     
      
    }



    // edit
    @RequestMapping(value = "/editar/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}",
        method = RequestMethod.GET)
    public String editarAnexoGet(HttpServletRequest request,
        HttpServletResponse response,  @PathVariable Long registroDetalleID,
        @PathVariable String tipoRegistro, @PathVariable Long registroID,
        @PathVariable Long anexoID,  Model model) throws I18NException, Exception {
      
      final boolean debug = log.isDebugEnabled();
      
      if (debug ) {
        log.debug(" Passa per AnexoController::editarAnexoGet("
          + "registroDetalleID = " +  registroDetalleID 
          + " | tipoRegistro = " + tipoRegistro
          + " | registroID = " + registroID
          + " | anexoID = " + anexoID + ")");
      }

      AnexoFull anexoFull2 = anexoEjb.getAnexoFull(anexoID);
      
      
      saveLastAnnexoAction(request, registroDetalleID, registroID, tipoRegistro, anexoID);
      
     
      AnexoForm anexoForm = new AnexoForm(anexoFull2);
      anexoForm.setRegistroID(registroID);
      anexoForm.setTipoRegistro(tipoRegistro);
      //anexoForm.setReturnURL(getRedirectURL(request, tipoRegistro, registroID));
      
      if (debug ) {
      
        log.info(" Carregant ANEXO: ");
        log.info("     - anexoFull.getDocumentoCustody(): " + anexoForm.getDocumentoCustody());
        if (anexoForm.getDocumentoCustody() != null) {
          log.info("     - anexoFull.getDocumentoCustody().getName(): " + anexoForm.getDocumentoCustody().getName());
        }
        log.info("     - anexoFull.getSignatureCustody(): " + anexoForm.getSignatureCustody());
        if (anexoForm.getSignatureCustody() != null) {
          log.info("     - anexoFull.getSignatureCustody().getName(): " + anexoForm.getSignatureCustody().getName());
        }
      }

      final long scanWebID = registroID;

      scanWebModuleEjb.closeScanWebProcess(request, scanWebID);

      model.addAttribute("anexoForm", anexoForm);

      loadCommonAttributes(request, model, scanWebID);

      return "registro/formularioAnexo";
    
    }
    
    
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String editarAnexoPost(@ModelAttribute AnexoForm anexoForm,
        BindingResult result, HttpServletRequest request,
        HttpServletResponse response, Model model) throws Exception, I18NValidationException {

      anexoValidator.validate(anexoForm.getAnexo(),result);
      
      if (!result.hasErrors()) { // Si no hay errores

        try {
        
           manageDocumentCustodySignatureCustody(request, anexoForm);

           anexoEjb.actualizarAnexo(anexoForm, getUsuarioEntidadActivo(request),
               anexoForm.getRegistroID(), anexoForm.getTipoRegistro());
           
           model.addAttribute("closeAndReload", "true");
           
           
           return "registro/formularioAnexo";
           
           
        } catch(I18NException i18n) {
          log.error(i18n.getMessage(), i18n);
          Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio())); 
        } catch(Exception e) {
          log.error(e.getMessage(), e);
          Mensaje.saveMessageError(request, e.getMessage());
        }
      
      }

      
      loadCommonAttributes(request, model, anexoForm.getRegistroID());
      
      return "registro/formularioAnexo";

      
    }
    
    
    /**
     * Elimina un Anexo del registroDetalle
     * @param idAnexo
     * @param idRegistroDetalle
     * @return
     */
      @RequestMapping(value = "/delete/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}", method = RequestMethod.GET)
      public String eliminarAnexo( @PathVariable Long registroDetalleID,
          @PathVariable String tipoRegistro, @PathVariable Long registroID,
          @PathVariable Long anexoID, HttpServletRequest request) {

          try {
              registroDetalleEjb.eliminarAnexoRegistroDetalle(anexoID, registroDetalleID);

          } catch (Exception e) {
            log.error(e.getMessage(), e);
            Mensaje.saveMessageError(request, e.getMessage());
          }

          return  getRedirectURL2(request, tipoRegistro, registroID);
      }
      

    
    protected void manageDocumentCustodySignatureCustody( 
        HttpServletRequest request,  AnexoForm anexoForm) throws Exception, I18NException {
      
      final Long registroID = anexoForm.getRegistroID();
      
      DocumentCustody dc;
      
      SignatureCustody sc;
      
      
      final Long scanWebID = registroID;

      ScanWebConfigRegWeb config = scanWebModuleEjb.getScanWebConfig(request, scanWebID);

      
      if (config != null &&  config.getScannedFiles().size() != 0) {
        
        List<ScannedDocument> listDocs = config.getScannedFiles();
        
        // Només processam el primer fitxer enviat
        ScannedDocument sd = listDocs.get(0);
        
        // Hem de modificar el Modo de Firma segons el que ens hagin enviat des de SCAN
        dc = sd.getScannedPlainFile();
        sc = sd.getScannedSignedFile();

        final boolean validezCopia;
        validezCopia = (anexoForm.getAnexo().getValidezDocumento() == RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA);
        
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
        
        if ((modoFirma != RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) && validezCopia) {
          // El mode de firma i la validesa del document no es corresponen
          
          // Si hi ha definida aquesta propietat, li posam el valor 
          // Si el valor de la propietat és -1, lavors llançarem una excepció
          // per indicar a l'usuari que ha de posar a ma el tipo validez correcte
          
          String defValDoc = System.getProperty(
              RegwebConstantes.REGWEB3_PROPERTY_BASE + "scan_default_validez_documento");
          if (defValDoc == null || defValDoc.trim().length() == 0) {
            // Forçam a Còpia Compulsada 
            anexoForm.getAnexo().setValidezDocumento(
                RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA);
          } else {
            long defVal; 
            try {
              defVal = Long.parseLong(defValDoc);
            } catch (Exception e) {
              log.error("La propietat '" + RegwebConstantes.REGWEB3_PROPERTY_BASE 
                  + "scan_default_validez_documento' no conté un valor numeric ");
              defVal = -1;              
            }
            
            
              
            if ((defVal == RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA)
                || (defVal == RegwebConstantes.TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL) 
                || (defVal == RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL) ) {
              // OK
            } else {
              if (defVal != -1) {
                log.error("La propietat '" + RegwebConstantes.REGWEB3_PROPERTY_BASE 
                  + "scan_default_validez_documento' no conté un valor no permés("
                  + defVal + "). Els valors vàlids són 2,3 i 4");
                defVal = -1;
              }
            }

            if (defVal == -1) {
              throw new I18NException("anexo.error.tipovalidezmodofirma");
            }

            anexoForm.getAnexo().setValidezDocumento(defVal);
          }

          
        }

        anexoForm.setMetadatas(sd.getMetadatas());

      } else {

        // Formulari Fitxer de Sistema
        int modoFirma= anexoForm.getAnexo().getModoFirma();
        // TODO 1 Fer switch del valor modoFirma
        // TODO 2 Comprovar que els fitxers enviat s'ajusten al modoFirma
        
        dc = getDocumentCustody(anexoForm);
        sc = getSignatureCustody(anexoForm, dc, modoFirma);
        
      }
      
      if (config != null) {
        // tancam tant si hem emprat com si no SCAN
        scanWebModuleEjb.closeScanWebProcess(request, scanWebID);
      }
      
      anexoForm.setDocumentoCustody(dc);
      anexoForm.setSignatureCustody(sc);

      
    }
    
    
    

    
    protected Long getRegistroDetalleID(AnexoForm anexoForm) {
      try {
        return anexoForm.getAnexo().getRegistroDetalle().getId();
      } catch (Throwable e) {
        return null;
      }
    }

   

 
    protected String getRedirectURL2(HttpServletRequest request, String tipoRegistro,
        Long registroID) {
      if (StringUtils.isEmpty(tipoRegistro)) {
        
        return request.getContextPath();
        
      } else {
        String nombreCompleto =  getNombreCompletoTipoRegistro(tipoRegistro);
        if (registroID == null || registroID == 0 ) {
          String url = "redirect:/" + nombreCompleto + "/list";
          //log.info("DELETE URL 1 = " + url);
          return url;
        } else {
          String url = "redirect:/" + nombreCompleto + "/" + registroID + "/detalle";
          //log.info("DELETE URL 2 = " + url);
          return url;
        }
      }
    }




    protected String getNombreCompletoTipoRegistro(String tipoRegistro) {
      String nombreCompletoTipoRegistro = "registro" + Character.toUpperCase(tipoRegistro.charAt(0)) 
          + tipoRegistro.substring(1);
      return nombreCompletoTipoRegistro;
    }



    protected SignatureCustody getSignatureCustody(AnexoForm anexoForm, DocumentCustody dc,
        int modoFirma) throws Exception {
      if (log.isDebugEnabled()) {
        log.debug("  ------------------------------");
        log.debug(" anexoForm.getFirmaFile() = " + anexoForm.getFirmaFile());
        log.debug(" anexoForm.getFirmaFile().isEmpty() = " + anexoForm.getFirmaFile().isEmpty());
        log.debug(" anexoForm.isFirmaFileDelete() = " + anexoForm.isSignatureFileDelete());
      }
      SignatureCustody sc = null;
      if (anexoForm.getFirmaFile().isEmpty()) {
        
        if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
          || modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
          String msg = "L'usuari ens indica que hi ha una firma hi no ve (modoFirma = " + modoFirma + ")";
          log.error(msg, new Exception());
          throw new Exception(msg);
        }
        
      } else {
        
        if (modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED
            && modoFirma !=  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
            String msg = "L'usuari ens indica que NO hi ha una firma pero n'envia una"
                + " (modoFirma = " + modoFirma + ")";
            log.error(msg, new Exception());
            throw new Exception(msg);
        }
        
        
        CommonsMultipartFile multipart = anexoForm.getFirmaFile();
        sc = new SignatureCustody();
        
        sc.setData(multipart.getBytes());
        sc.setMime(multipart.getContentType());
        sc.setName(multipart.getOriginalFilename());
        
        
        if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {
          // Document amb firma adjunta
          sc.setAttachedDocument(null);
          
          // TODO Emprar mètode per descobrir tipus de signatura
          sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);
         
        } else if (modoFirma ==  RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
          // Firma en document separat CAS 4
          if (dc == null) {
            throw new Exception("Aquesta firma requereix el document original"
                + " i no s'ha enviat");
          }

          sc.setAttachedDocument(false);
          // TODO Emprar mètode per descobrir tipus de signatura
          sc.setSignatureType(SignatureCustody.OTHER_SIGNATURE_WITH_DETACHED_DOCUMENT);
        }
      }
      return sc;
    }



    protected DocumentCustody getDocumentCustody(AnexoForm anexoForm) {
      if (log.isDebugEnabled()) {
        log.debug("  ------------------------------");

        log.debug(" anexoForm.getDocumentoFile() = " + anexoForm.getDocumentoFile());
        log.debug(" anexoForm.getDocumentoFile().isEmpty() = " + anexoForm.getDocumentoFile().isEmpty());
        log.debug(" anexoForm.isDocumentoFileDelete() = " + anexoForm.isDocumentoFileDelete());
      }
      DocumentCustody dc = null;
      if (!anexoForm.getDocumentoFile().isEmpty()) {
        dc = new DocumentCustody();
        CommonsMultipartFile multipart = anexoForm.getDocumentoFile();
        dc.setData(multipart.getBytes());
        dc.setMime(multipart.getContentType());
        dc.setName(multipart.getOriginalFilename());
      }
      return dc;
    }


    /**
    * Función que nos permite mostrar el contenido de un anexo
    * @param anexoId identificador del anexo
    */
    @RequestMapping(value = "/descargarDocumento/{anexoId}", method = RequestMethod.GET)
    public void  anexo(@PathVariable("anexoId") Long anexoId, HttpServletRequest request,
        HttpServletResponse response)  throws Exception, I18NException {
         AnexoFull anexoFull = anexoEjb.getAnexoFull(anexoId);
         fullDownload(anexoFull.getAnexo().getCustodiaID(), anexoFull.getDocumentoCustody().getName(),
             anexoFull.getDocumentoCustody().getMime(), false,response);
    }

     /**
    * Función que nos permite mostrar el contenido de un firma de un anexo
    * @param anexoId identificador del anexo
    */
    @RequestMapping(value = "/descargarFirma/{anexoId}", method = RequestMethod.GET)
    public void  firma(@PathVariable("anexoId") Long anexoId, HttpServletRequest request, 
        HttpServletResponse response)  throws Exception, I18NException {
         AnexoFull anexo = anexoEjb.getAnexoFull(anexoId);
        //Parche para la api de custodia antigua que se guardan los documentos firmados en DocumentCustody.
        if (anexo.getSignatureCustody() == null) {//Api antigua, hay que descargar el document custody
            fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getDocumentoCustody().getName(),
                    anexo.getDocumentoCustody().getMime(), false, response);
        } else {
            fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getSignatureCustody().getName(),
                    anexo.getSignatureCustody().getMime(), true, response);
        }

    }

    /**
    *  Función que obtiene los datos de un archivo para mostrarlo
     *
    * @param archivoId  identificador del archivo
    * @param filename   nombre del archivo
    * @param contentType
    * @param response
     * @return Descarga el archivo y además devuelve true o false en funcion de si ha encontrado el archivo indicado.
     * En la api de custodia antigua, los documentos firmados se guardaban en DocumentCustody y en la nueva en SignatureCustody.
     * Por tanto cuando vaya a recuperar un documento con firma antiguo, mirarà en SignatureCustody y no lo encontrará, por tanto controlamos ese caso y devolvemos false.
     * para poder ir a buscarlo a DocumentCustody, que es donde estará. (todo esto se hace en el método firma)
    */
    public void fullDownload(String custodiaID, String filename, String contentType, boolean firma,
        HttpServletResponse response)  {

         //FileInputStream input = null;
      
      
         OutputStream output = null;
         MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
         byte[] data;
         try {
             if (custodiaID != null) {
                 if(!firma){
                   DocumentCustody dc = anexoEjb.getArchivo(custodiaID);
                   filename = dc.getName();
                   data = dc.getData();
                 }else{
                   SignatureCustody sc = anexoEjb.getFirma(custodiaID);
                   filename = sc.getName();
                   data = sc.getData();
                 }


                 if (contentType == null) {
                   try {
                    File tmp = File.createTempFile("regweb_annex_", filename);
                    FileOutputStream fos = new FileOutputStream(tmp);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    contentType = mimeTypesMap.getContentType(tmp);
                    if (!tmp.delete()) {
                      tmp.deleteOnExit();
                    }
                   } catch(Throwable th) {
                     log.error("Error intentant obtenir el tipus MIME: " + th.getMessage() , th);
                     contentType = "application/octet-stream";
                   }
                 }
                 response.setContentType(contentType);
                 response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                 response.setContentLength((int) data.length);

                 output = response.getOutputStream();
                 output.write(data);

                 output.flush();
             }

         } catch (NumberFormatException e) {
             // TODO QUE FER
             log.info(e);
         }  catch (Exception e) {
             e.printStackTrace();
         }
    }

    
    
    
    @InitBinder     
    public void initBinder(WebDataBinder binder){
         binder.registerCustomEditor(       Date.class,     
                             new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true, 10));   
    }
    

}
