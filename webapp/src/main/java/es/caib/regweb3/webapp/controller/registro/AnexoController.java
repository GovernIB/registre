package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.scan.ScannerManager;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.AnexoWebValidator;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.HandlerMapping;
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
 */
@Controller
@RequestMapping(value = "/anexo")
@SessionAttributes(types = {AnexoForm.class })
public class AnexoController extends BaseController {
  
  
    public static class UploadedScanFile {
      protected final File file;
      protected final String name;
      protected final String mime;
      protected final long expiryDate;
      /**
       * @param file
       * @param name
       * @param mime
       * @param uploadDate
       */
      public UploadedScanFile(File file, String name, String mime, long expiryDate) {
        super();
        this.file = file;
        this.name = name;
        this.mime = mime;
        this.expiryDate = expiryDate;
      }
      public File getFile() {
        return file;
      }
      public String getName() {
        return name;
      }
      public String getMime() {
        return mime;
      }
      public long getExpiryDate() {
        return expiryDate;
      }
      
    }
  
    //  Uploaded Scan Files
    private static final Map<Long, UploadedScanFile> scanDocumentsByID = new HashMap<Long, AnexoController.UploadedScanFile>();
    
        
    protected static final long FIVE_MINUTES = 1000 * 60 * 5;
    
    protected UploadedScanFile getScanFileByID(long docID) {

      synchronized (scanDocumentsByID) {
        clearExpiredScanDocuments();
  
        return scanDocumentsByID.get(docID);
      }
    }


        
  
    protected void saveScanFile(long docID, UploadedScanFile usf) {
    
      synchronized (scanDocumentsByID) {
        clearExpiredScanDocuments();
        scanDocumentsByID.put(docID, usf);
      }
      
    }
    
    protected void clearScanFile(long docID) {
      
      synchronized (scanDocumentsByID) {
        clearExpiredScanDocuments();
        scanDocumentsByID.remove(docID);
      }
      
    }
    

    private void clearExpiredScanDocuments() {
      // Clear Uploaded Scan Files
      List<Long> deleteItems = new ArrayList<Long>();
 
      final long now = System.currentTimeMillis();
      
      for (Long id : scanDocumentsByID.keySet()) {
        UploadedScanFile usf = scanDocumentsByID.get(id);
        
        if (usf.getExpiryDate() < now) {
          deleteItems.add(id);
        }
        
      }
 
      for (Long id : deleteItems) {
        scanDocumentsByID.remove(id);
      }
    }
    
  
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



    protected void loadCommonAttributes(HttpServletRequest request, Model model, Long registroID) throws Exception {
      model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
      model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
      model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);
      model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
      
      
      // Scan
      Entidad entidad = getEntidadActiva(request);
      Integer tipusScan = 0;
      if (entidad.getTipoScan() != null && !"".equals(entidad.getTipoScan())) {
        tipusScan = Integer.parseInt(entidad.getTipoScan());
      }
      //      Integer tipusScan = 2;
      boolean teScan = ScannerManager.teScan(tipusScan);
      model.addAttribute("teScan", teScan);
      if (teScan) {
        if (request.getParameter("lang") == null) {
          request.setAttribute("lang", I18NUtils.getLocale().getLanguage());
        }
        model.addAttribute("headerScan", ScannerManager.getHeaderJSP(request, tipusScan, registroID));
        model.addAttribute("coreScan", ScannerManager.getCoreJSP(request, tipusScan, registroID));
        
        initScan(request, registroID);
        
      }
      
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
      
      
      
      initScan(request, registroID);

      model.addAttribute("anexoForm", anexoForm);

      loadCommonAttributes(request, model, registroID);

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
        HttpServletRequest request,  AnexoForm anexoForm) throws Exception {
      
      final Long registroID = anexoForm.getRegistroID();
      
      DocumentCustody dc;
      
      SignatureCustody sc;
      
      
      //Cogemos el archivo
      UploadedScanFile usf = getScanFileByID(registroID);
      
      if (usf != null) {
        File scanFile = usf.getFile();

        AnnexCustody file;

        // TODO Això s'ha de solucionar amb el nou sistema de digitalització
        final int modoFirma= anexoForm.getAnexo().getModoFirma();
        switch(modoFirma) {
          case RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA:
            dc = new DocumentCustody();
            file = dc;
            sc = null;
          break;
          // Document amb firma adjunta
          case RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED:
             dc = null;
             sc = new SignatureCustody();
             // TODO Vull suposar que és un PDF firmat. Amb scan web 2.0.0 s'hauria de solucionar
             sc.setSignatureType(SignatureCustody.OTHER_DOCUMENT_WITH_ATTACHED_SIGNATURE);
             file =sc;
          break;
          // Firma en document separat
          case RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED:
            // TODO Aquest Cas només tindrà sentit amb el plugin scan web 2.0.0
            throw new Exception("El modo de firma MODO_FIRMA_ANEXO_DETACHED en "
                + "fitxers escanejats ara no te sentit. Estirà disponible en API SCAN WEB 2.0.0");

          
          default:
            String msg = "El modo de firma " + modoFirma + " es desconegut.";
            log.error(msg, new Exception());
            throw new Exception(msg);

        }
       
        
        file.setData(FileUtils.readFileToByteArray(scanFile));
        
        
        //String mime = (String)session.getAttribute("scan_" + registroID + ".mime");
        String mime = usf.getMime();
        if (es.caib.regweb3.utils.StringUtils.isEmpty(mime)) {
          // TODO Quin tipus li pos aqui // JPG, PNG, TIFF, PDF, ...
          // Mirar Mime manager de PortaFIB o de GenApp!!!!
          if (sc == null) { 
            // Single document
            mime = "application/octet-stream";
          } else {
         // TODO Presuposam que es un PDF firmat. Ho hauria de solucionar scan web 2.0.0
            mime = "application/pdf"; 
          }
        }
        file.setMime(mime);
        
        //String name = (String)session.getAttribute("scan_" + registroID + ".name");
        String name = usf.getName();
        if (name == null) {
          name = "FitxerEscanejat.bin";
        }
        file.setName(name);

        if (!scanFile.delete()) {
          scanFile.deleteOnExit();
        };

        initScan(request, registroID);
        
      } else {

        // Formulari Fitxer de Sistema
        int modoFirma= anexoForm.getAnexo().getModoFirma();
        // TODO 1 Fer switch del valor modoFirma
        // TODO 2 Comprovar que els fitxers enviat s'ajusten al modoFirma
        
        dc = getDocumentCustody(anexoForm);
        sc = getSignatureCustody(anexoForm, dc, modoFirma);
        
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
    
    
    
    @RequestMapping(value = "/scanwebresource2/{tipusScan}/{registroID}/**", method = RequestMethod.GET)
    public Object obtenerRecursoPath(
        @PathVariable Integer tipusScan,
        @PathVariable Long registroID,        
        HttpServletRequest request,
        HttpServletResponse response) throws Exception {
      
      
      
   // Don't repeat a pattern
      String pattern = (String)
          request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);  

      String resourcePath  = new AntPathMatcher().extractPathWithinPattern(pattern, 
          request.getServletPath());

      log.info("Downloading Scan Resource:");
      log.info("   + tipusScan = ]" + tipusScan+ "[");
      log.info("   + registroID = ]" + registroID+ "[");
      log.info("   + resourcePath = ]" + resourcePath + "[");
      

        ScanWebResource recurs = ScannerManager.getResource(request, tipusScan, resourcePath, registroID);

        response.setHeader("Pragma", "");
        response.setHeader("Expires", "");
        response.setHeader("Cache-Control", "");
        response.setHeader("Content-Disposition", "inline; filename=\"" + recurs.getName() + "\"");
        response.setContentType(recurs.getMime());
        response.getOutputStream().write(recurs.getContent());
        
        return null;
    }

    

    /**
     * Obtiene el {@link es.caib.regweb3.model.Anexo} según su identificador.
     *
     */
    // TODO Borrar
    @RequestMapping(value = "/scanwebresource/{path1}/{path2}/{resourcename:.+}", method = RequestMethod.GET)
    public Object obtenerRecursoPath2(
    		@PathVariable String path1,
    		@PathVariable String path2,
    		@PathVariable String resourcename, 
    		HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

    	Integer tipusScan = 2;
    	long registroID = 0; // TODO ???
    	String resource = (path1 != null ? path1 + "/" : "") + (path2 != null ? path2 + "/" : "") + resourcename;
        ScanWebResource recurs = ScannerManager.getResource(request, tipusScan, resource, registroID);

        response.setHeader("Pragma", "");
		response.setHeader("Expires", "");
		response.setHeader("Cache-Control", "");
		response.setHeader("Content-Disposition", "inline; filename=\"" + recurs.getName() + "\"");
		response.setContentType(recurs.getMime());
		response.getOutputStream().write(recurs.getContent());
        
        return null;
    }
    
    @RequestMapping(value = "/scanwebresource/{path}/{resourcename:.+}", method = RequestMethod.GET)
    // TODO Borrar
    public Object obtenerRecursoPath1(
    		@PathVariable String path,
    		@PathVariable String resourcename, 
    		HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

        return obtenerRecursoPath2(path, null, resourcename, request, response);
    }
    
    @RequestMapping(value = "/scanwebresource/{resourcename:.+}", method = RequestMethod.GET)
    // TODO Borrar
    public Object obtenerRecurso(
    		@PathVariable String resourcename, 
    		HttpServletRequest request,
    		HttpServletResponse response) throws Exception {
    	return obtenerRecursoPath2(null, null, resourcename, request, response);
    }

    @RequestMapping(value = "/guardarScan/{idRegistro}", method = RequestMethod.POST)
    public void scan(
    		@PathVariable Long idRegistro, 
    		MultipartHttpServletRequest request, 
    		HttpServletResponse response)  {

       MultipartFile scan = null;

       try {
           //Cogemos el archivo
    	   scan = request.getFile("RemoteFile");
    	   
    	   File temp = File.createTempFile("scan", ".pdf");
    	   FileOutputStream fos = new FileOutputStream(temp);
    	   fos.write(scan.getBytes());
    	   fos.close();
    	   temp.deleteOnExit();
    	   
           //Obtain the session object, create a new session if doesn't exist
    	     UploadedScanFile usf = new UploadedScanFile(temp, scan.getOriginalFilename(),
    	         scan.getContentType(), System.currentTimeMillis() + FIVE_MINUTES);
    	     saveScanFile(idRegistro, usf);
    	     /*
           HttpSession session = request.getSession(true);
           session.setAttribute("scan_" + idRegistro, temp);
           session.setAttribute("scan_" + idRegistro +".name", scan.getOriginalFilename());
           session.setAttribute("scan_" + idRegistro +".mime", scan.getContentType());
           */

       }catch (Exception e) {
         // TODO PROCESSSAR ERROR !!!!
          e.printStackTrace();
       }
    }
    
    
    protected void initScan(HttpServletRequest request, Long registroID) {
      
      
      clearScanFile(registroID);
/*    HttpSession session = request.getSession(true);
      session.removeAttribute("scan_" + registroID);
      session.removeAttribute("scan_" + registroID +".name");
      session.removeAttribute("scan_" + registroID +".mime");
      */
    }

}
