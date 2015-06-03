package es.caib.regweb.webapp.controller.registro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.axis.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.RegistroDetalle;
import es.caib.regweb.persistence.ejb.AnexoFull;
import es.caib.regweb.persistence.ejb.AnexoLocal;
import es.caib.regweb.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb.persistence.ejb.HistoricoRegistroSalidaLocal;
import es.caib.regweb.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb.persistence.utils.AnnexFileSystemManager;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.scan.ScannerManager;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.AnexoWebValidator;

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
  
  
    public static final int FILE_TAB_HEIGHT = 107;
  
    public static final int BASE_IFRAME_HEIGHT = 480 - FILE_TAB_HEIGHT;
  

    @Autowired
    private AnexoWebValidator anexoValidator;
    
    @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb/HistoricoRegistroSalidaEJB/local")
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    
    @EJB(mappedName = "regweb/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;
    
    @EJB(mappedName = "regweb/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;

    @RequestMapping(value = "/nou/{registroDetalleID}/{tipoRegistro}/{registroID}", method = RequestMethod.GET)
    public String crearAnexoGet(HttpServletRequest request,
        HttpServletResponse response, @PathVariable Long registroDetalleID,
        @PathVariable Long registroID, @PathVariable String tipoRegistro,
        Model model) throws I18NException, Exception {
      
      log.info(" Passa per AnexoController::crearAnexoGet(" + registroDetalleID 
          + "," + tipoRegistro + ", " + registroID + ")");

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



    protected void loadCommonAttributes(HttpServletRequest request, Model model, Long registroID) throws Exception {
      model.addAttribute("tiposDocumental", tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId()));
      model.addAttribute("tiposDocumentoAnexo", RegwebConstantes.TIPOS_DOCUMENTO);
      model.addAttribute("tiposFirma", RegwebConstantes.TIPOS_FIRMA);
      model.addAttribute("tiposValidezDocumento", RegwebConstantes.TIPOS_VALIDEZDOCUMENTO);
      
      
      // Scan XYZ
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
      
      if (result.hasErrors()){//si hay errores
        
        processErrors(result, request, anexoForm.getRegistroID());
        
        loadCommonAttributes(request, model, anexoForm.getRegistroID());

        log.info("XYZ  crearAnexoPost: return X");
        return "registro/formularioAnexo";
        
      } else { // Si no hay errores

        
        try {
           manageDocumentCustodySignatureCustody(request, anexoForm);

           anexoEjb.crearAnexo(anexoForm, getUsuarioEntidadActivo(request),
               anexoForm.getRegistroID(), anexoForm.getTipoRegistro());
           

           
           model.addAttribute("closeAndReload", "true");


           return "registro/formularioAnexo";
           
        } catch(I18NException i18n) {
          Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio()));   
          log.info("XYZ  crearAnexoPost: return B");
          return "registro/formularioAnexo";
        } catch(Exception e) {
          log.error(e.getMessage(), e);
          Mensaje.saveMessageError(request, e.getMessage());
          log.info("XYZ  crearAnexoPost: return C");
          return "registro/formularioAnexo";
        }
      
      }

     
      
    }



    public void processErrors(BindingResult result, HttpServletRequest request, Long registroID) {
/*
      log.error(" HI HA ERRORS ");

      result.getFieldErrors();
      
      List<FieldError> errors = result.getFieldErrors();
      StringBuffer str = new StringBuffer();
      for (FieldError error : errors ) {
        
        log.error(" XYZ  =========================================== ");
        log.error(" XYZ  getCode: " + error.getCode());
        log.error(" XYZ  getDefaultMessage: " + error.getDefaultMessage());
        log.error(" XYZ  getField: " + error.getField());
        log.error(" XYZ  getObjectName: " + error.getObjectName());
        log.error(" XYZ  getArguments: " + Arrays.toString(error.getArguments()));
        log.error(" XYZ  getCodes: " + Arrays.toString(error.getCodes()));
        log.error(" XYZ  getRejectedValue: " + error.getRejectedValue());
        
        if (error.getDefaultMessage() == null) {
          String[] stringArray = null;
          Object[] objectArray = error.getArguments();
          if (objectArray != null) {
             stringArray = Arrays.copyOf(objectArray, objectArray.length, String[].class);
          }
          str.append(I18NUtils.tradueix(error.getField()) + ": " +
              I18NUtils.tradueix( error.getCode(), stringArray) + "<br/>");
        } else {
          str.append(error.getDefaultMessage() + "<br/>");
        }

      }
      
      Mensaje.saveMessageError(request, str.toString());
*/
    }
    


    // edit
    
    @RequestMapping(value = "/editar/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}",
        method = RequestMethod.GET)
    public String editarAnexoGet(HttpServletRequest request,
        HttpServletResponse response,  @PathVariable Long registroDetalleID,
        @PathVariable String tipoRegistro, @PathVariable Long registroID,
        @PathVariable Long anexoID,  Model model) throws I18NException, Exception {
      
      log.info("XYZ Passa per AnexoController::editarAnexoGet("
          + "registroDetalleID = " +  registroDetalleID 
          + " | tipoRegistro = " + tipoRegistro
          + " | registroID = " + registroID
          + " | anexoID = " + anexoID + ")");

      AnexoFull anexoFull2 = anexoEjb.getAnexoFull(anexoID);
      
      
     
      AnexoForm anexoForm = new AnexoForm(anexoFull2);
      anexoForm.setRegistroID(registroID);
      anexoForm.setTipoRegistro(tipoRegistro);
      //anexoForm.setReturnURL(getRedirectURL(request, tipoRegistro, registroID));
      
      
      
      log.info("XYZ Carregant ANEXO: ");
      log.info("XYZ     - anexoFull.getDocumentoCustody(): " + anexoForm.getDocumentoCustody());
      if (anexoForm.getDocumentoCustody() != null) {
        log.info("XYZ     - anexoFull.getDocumentoCustody().getName(): " + anexoForm.getDocumentoCustody().getName());
      }
      log.info("XYZ     - anexoFull.getSignatureCustody(): " + anexoForm.getSignatureCustody());
      if (anexoForm.getSignatureCustody() != null) {
        log.info("XYZ     - anexoFull.getSignatureCustody().getName(): " + anexoForm.getSignatureCustody().getName());
      }
      
      
      
      initScan(request, registroID);

      model.addAttribute("anexoForm", anexoForm);

      loadCommonAttributes(request, model, registroID);

      return "registro/formularioAnexo";
    
    }
    
    
    
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String editarAnexoPost(@ModelAttribute AnexoForm anexoForm,
        BindingResult result, HttpServletRequest request,
        HttpServletResponse response, Model model) throws Exception {

      anexoValidator.validate(anexoForm.getAnexo(),result);
      
      if (result.hasErrors()) { //si hay errores

        processErrors(result, request, anexoForm.getRegistroID());
        
        loadCommonAttributes(request, model, anexoForm.getRegistroID());
        
        return "registro/formularioAnexo";
        
      } else { // Si no hay errores

        try {
        
           manageDocumentCustodySignatureCustody(request, anexoForm);

           anexoEjb.actualizarAnexo(anexoForm, getUsuarioEntidadActivo(request),
               anexoForm.getRegistroID(), anexoForm.getTipoRegistro());
           
           model.addAttribute("closeAndReload", "true");
           
           
           return "registro/formularioAnexo";
           
           
        } catch(I18NException i18n) {
          Mensaje.saveMessageError(request, I18NUtils.tradueix(i18n.getTraduccio())); 
          return "registro/formularioAnexo";
        } catch(Exception e) {
          log.error(e.getMessage(), e);
          Mensaje.saveMessageError(request, e.getMessage());
          return "registro/formularioAnexo";
        }
      
      }


      
    }
    
    
    /**
     * Elimina un Anexo de la variable de sesion que almacena los interesados
     * @param idAnexo
     * @param idRegistroDetalle
     * @return
     */
      @RequestMapping(value = "/delete/{registroDetalleID}/{tipoRegistro}/{registroID}/{anexoID}", method = RequestMethod.GET)
      public String eliminarAnexo( @PathVariable Long registroDetalleID,
          @PathVariable String tipoRegistro, @PathVariable Long registroID,
          @PathVariable Long anexoID, HttpServletRequest request) {

          try {
              anexoEjb.eliminarAnexoRegistroDetalle(anexoID, registroDetalleID);

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
      HttpSession session = request.getSession(true);
      Object scan = session.getAttribute("scan_" + registroID);
      
      if (scan != null) {
        File scanFile = (File)scan;
        
        //InputStream inputStream = new FileInputStream(scanFile);
       
        
        //DiskFileItem fileItem = new DiskFileItem("file", "application/pdf", false, scanFile.getName(), (int) scanFile.length() , scanFile.getParentFile());
         
        
        
        
        dc = new DocumentCustody();
        dc.setData(FileUtils.readFileToByteArray(scanFile));
        int modoFirma= anexoForm.getAnexo().getModoFirma();
        if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {
          dc.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
        } else {
          // TODO es podria especificar el tipus PDF, DOCX, ODT, ... a partir del tipus MIME
          dc.setDocumentType(DocumentCustody.OTHER_DOCUMENT_WITH_SIGNATURE);
        }
        
        
        String mime = (String)session.getAttribute("scan_" + registroID + ".mime");
        if (es.caib.regweb.utils.StringUtils.isEmpty(mime)) {
          // TODO Quin tipus li pos aqui // JPG, PNG, TIFF, PDF, ...
          // Mirar Mime manager de PortaFIB o de GenApp!!!!
          mime = "application/pdf";
          
        }
        dc.setMime(mime);
        
        String name = (String)session.getAttribute("scan_" + registroID + ".name");
        if (name == null) {
          name = "FitxerEscanejat.bin";
        }
        dc.setName(name);

        if (!scanFile.delete()) {
          scanFile.deleteOnExit();
        };
        
        sc = null;
        
        initScan(request, registroID);
        
      } else {

        // Formulari Fitxer de Sistema
        dc = getDocumentCustody(anexoForm);
        sc = getSignatureCustody(anexoForm, dc);
        
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

    
    /*
    protected String getRedirectURLStr(HttpServletRequest request, String returnURL,  String tipoRegistro,
        Long registroID) {
      if (StringUtils.isEmpty(returnURL)) {
        String url = getRedirectURL(request, tipoRegistro, registroID); 
        return "redirect:" + url;
      } else {
        return returnURL;
      }
      
    }
    */
    

    /*
    protected String getRedirectURL(HttpServletRequest request, String returnURL,  String tipoRegistro,
        Long registroID) {
      //ModelAndView mav;
      if (StringUtils.isEmpty(returnURL)) {
        String url = getRedirectURL2(request, tipoRegistro, registroID); 
        
        return url;
       // mav =  new ModelAndView(new RedirectView(url, true));
      } else {
        return returnURL; 
        //mav = new ModelAndView(new RedirectView(returnURL, false));
      }
      //return mav;
    }
*/
    
    protected String getRedirectURL2(HttpServletRequest request, String tipoRegistro,
        Long registroID) {
      if (StringUtils.isEmpty(tipoRegistro)) {
        
        return request.getContextPath();
        
      } else {
        String nombreCompleto =  getNombreCompletoTipoRegistro(tipoRegistro);
        if (registroID == null || registroID == 0 ) {
          String url = "redirect:/" + nombreCompleto + "/list";
          log.info("DELETE XYZ URL 1 = " + url);
          return url;
        } else {
          String url = "redirect:/" + nombreCompleto + "/" + registroID + "/detalle";
          log.info("DELETE XYZ URL 2 = " + url);
          return url;
        }
      }
    }

/*
    protected String getRedirectURL(HttpServletRequest request, String tipoRegistro,
        Long registroID) {
      if (StringUtils.isEmpty(tipoRegistro)) {
        
        return request.getContextPath();
        
      } else {
        String nombreCompleto =  getNombreCompletoTipoRegistro(tipoRegistro);
        if (registroID == null || registroID == 0 ) {
          String url = request.getContextPath()  +"/"+  nombreCompleto + "/list";
          log.info("XYZ URL 1 = " + url);
          return url;
        } else {
          String url = request.getContextPath() +"/"+ nombreCompleto + "/" + registroID + "/detalle";
          log.info("XYZ URL 2 = " + url);
          return url;
        }
      }
    }
*/


    protected String getNombreCompletoTipoRegistro(String tipoRegistro) {
      String nombreCompletoTipoRegistro = "registro" + Character.toUpperCase(tipoRegistro.charAt(0)) 
          + tipoRegistro.substring(1);
      return nombreCompletoTipoRegistro;
    }



    protected SignatureCustody getSignatureCustody(AnexoForm anexoForm, DocumentCustody dc) {
      System.out.println("XYZ  ------------------------------");
      System.out.println("XYZ anexoForm.getFirmaFile() = " + anexoForm.getFirmaFile());
      System.out.println("XYZ anexoForm.getFirmaFile().isEmpty() = " + anexoForm.getFirmaFile().isEmpty());
      System.out.println("XYZ anexoForm.isFirmaFileDelete() = " + anexoForm.isSignatureFileDelete());
      SignatureCustody sc = null;
      if (!anexoForm.getFirmaFile().isEmpty()) {
        CommonsMultipartFile multipart = anexoForm.getFirmaFile();
        sc = new SignatureCustody();
        sc.setAttachedDocument(dc == null? true : false);
        sc.setData(multipart.getBytes());
        sc.setMime(multipart.getContentType());
        sc.setName(multipart.getOriginalFilename());
        sc.setSignatureType(SignatureCustody.OTHER_SIGNATURE);
      }
      return sc;
    }



    protected DocumentCustody getDocumentCustody(AnexoForm anexoForm) {
      System.out.println("XYZ  ------------------------------");

      System.out.println("XYZ anexoForm.getDocumentoFile() = " + anexoForm.getDocumentoFile());
      System.out.println("XYZ anexoForm.getDocumentoFile().isEmpty() = " + anexoForm.getDocumentoFile().isEmpty());
      System.out.println("XYZ anexoForm.isDocumentoFileDelete() = " + anexoForm.isDocumentoFileDelete());
      DocumentCustody dc = null;
      if (!anexoForm.getDocumentoFile().isEmpty()) {
        dc = new DocumentCustody();
        CommonsMultipartFile multipart = anexoForm.getDocumentoFile();
        dc.setData(multipart.getBytes());
        int modoFirma= anexoForm.getAnexo().getModoFirma();
        if (modoFirma == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {
          dc.setDocumentType(DocumentCustody.DOCUMENT_ONLY);
        } else {
          // TODO es podria especificar el tipus PDF, DOCX, ODT, ... a partir del tipus MIME
          dc.setDocumentType(DocumentCustody.OTHER_DOCUMENT_WITH_SIGNATURE);
        }
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
         
         // NOM i MIME no són correctes !!!!!!!
         
         fullDownload(anexo.getAnexo().getCustodiaID(), anexo.getSignatureCustody().getName(),
             anexo.getSignatureCustody().getMime(), true,response);
    }

    /**
    *  Función que obtiene los datos de un archivo para mostrarlo
    * @param archivoId  identificador del archivo
    * @param filename   nombre del archivo
    * @param contentType
    * @param response
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
                   DocumentCustody dc = AnnexFileSystemManager.getArchivo(custodiaID);
                   filename = dc.getName();
                   data = dc.getData();
                 }else{
                   SignatureCustody sc = AnnexFileSystemManager.getFirma(custodiaID);
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
           // TODO XYZ QUE FER !!!!!
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
    
    
    
    
    // ---------------------------------------------------------------------------------------
    
    
    

   /**
   * Método que guarda el archivo fisico asociado a un anexo a posteriori, en este metodo
   *  solo se gestiona el archivo asociado
   * @param idAnexo identificador del anexo
   * @param accion  indica si es nuevo o edición
   * @param borrar  indica si se ha de borrar el archivo asociado
   */
    /*
   @RequestMapping(value = "/guardarArchivo/{idAnexo}/{accion}/{borrar}", method = RequestMethod.POST)
   public void archivo(@PathVariable Long idAnexo,@PathVariable String accion, @PathVariable boolean borrar,
       MultipartHttpServletRequest request, HttpServletResponse response)  {

	  Object scan = null;
      MultipartFile doc = null;
      MultipartFile firma = null;
      AnexoFormManager afm;

      try {

          Anexo anexo = anexoEjb.findById(idAnexo);
          
          if (anexo == null) {
            log.info(" archivo :: anexo = null");
          } else {
             log.info(" archivo :: anexo.getCustodiaID()  = " + anexo.getCustodiaID() );
          }

          //Cogemos el archivo
          HttpSession session = request.getSession(true);
          scan = session.getAttribute("scan_" + idAnexo);
          
          if (scan != null) {
        	  File scanFile = (File)scan;
        	  
        	  InputStream inputStream = new FileInputStream(scanFile);
//              int availableBytes = inputStream.available();
//
//              // Write the inputStream to a FileItem
//              File outFile = new File("c:\\tmp\\scan.pdf"); // This is your tmp file, the code stores the file here in order to avoid storing it in memory
//              FileItem fileItem = new DiskFileItem("fileUpload", "plain/text", false, "sometext.txt", availableBytes, outFile); // You link FileItem to the tmp outFile 
//              OutputStream outputStream = fileItem.getOutputStream(); // Last step is to get FileItem's output stream, and write your inputStream in it. This is the way to write to your FileItem. 
//
//              int read = 0;
//              byte[] bytes = new byte[1024];
//              while ((read = inputStream.read(bytes)) != -1) {
//                  outputStream.write(bytes, 0, read);
//              }
//
//              // Don't forget to release all the resources when you're done with them, or you may encounter memory/resource leaks.
//              inputStream.close();
//              outputStream.flush(); // This actually causes the bytes to be written.
//              outputStream.close();
        	  
        	  
        	  DiskFileItem fileItem = new DiskFileItem("file", "application/pdf", false, scanFile.getName(), (int) scanFile.length() , scanFile.getParentFile());
        	  OutputStream outputStream = fileItem.getOutputStream();
        	  int read = 0;
              byte[] bytes = new byte[1024];
              while ((read = inputStream.read(bytes)) != -1) {
                  outputStream.write(bytes, 0, read);
              }

              // Don't forget to release all the resources when you're done with them, or you may encounter memory/resource leaks.
              inputStream.close();
              outputStream.flush(); // This actually causes the bytes to be written.
              outputStream.close();
              
        	  doc = new CommonsMultipartFile(fileItem);
        	  fileItem.delete();
          } else {
        	  doc = request.getFile("archivo");
          }

          if(anexo.getModoFirma() == 1 || anexo.getModoFirma() == 0){// no hay firma detached  o no hay firma
              if(doc != null){ // Modificamos archivo
                  //Si editamos, eliminamos el anexo anterior
                
               
                  
//                  if(!accion.equals("nuevo") ){
//                    // Eliminar el archivo fisico del sistema de archivos
//                    AnnexFileSystemManager.eliminarArchivo(anexo.getCustodiaID());
//                  }
                  
                  //Inicializamos el archivo form manager con el nuevo archivo doc
                  afm = new AnexoFormManager(anexoEjb, doc, null); //, RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                  // Método que actualiza el anexo asociandole el nuevo archivo.
                  afm.prePersist(anexo);

              } else { // no modificamos archivo

                  if(borrar){ // No hay archivo nuevo y borramos el archivo antiguo
                     log.info("borrar");
                     AnnexFileSystemManager.eliminarDocumento(anexo.getCustodiaID());
                     anexo.setNombreFicheroAnexado("");
                     anexoEjb.actualizarAnexo(anexo);
                  }
              }
          } else {// Viene firma y gestion del doc
              //Cogemos la firma
              firma = request.getFile("firma");
              // Comprobar que doc y firma no sean null
              if(firma != null && doc != null){

                  
//                  if(!accion.equals("nuevo") ){ // Si editamos
//                   
//                    // Eliminar los archivos fisicos del sistema de archivos
//                    AnnexFileSystemManager.eliminarArchivo(anexo.getCustodiaID());
//                  } 
                  
                  // Guardar el anexo con los dos archivos asociados
                  afm = new AnexoFormManager(anexoEjb, doc, firma); //, RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                  afm.prePersist(anexo);
              }else{
                   if(doc != null ){ //firma es null, gestionamos solo el doc

                     
//                      if(!accion.equals("nuevo") ){ //Editamos
//                        // Eliminar el archivo fisico del sistema de archivos
//                        AnnexFileSystemManager.eliminarArchivo(anexo.getCustodiaID());
//                      }
                      
                      //Inicializamos el archivo form manager con el nuevo archivo doc
                      afm = new AnexoFormManager(anexoEjb, doc, null); //, RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                      // Método que actualiza el anexo asociandole el nuevo archivo.
                      afm.prePersist(anexo);

                  } else { // no modificamos archivo

                      if(borrar){ // No hay archivo nuevo y borramos el archivo antiguo
                         log.info("borrar");
                         AnnexFileSystemManager.eliminarDocumento(anexo.getCustodiaID());
                         anexo.setNombreFicheroAnexado("");
                         anexo.setNombreFirmaAnexada("");
                         anexoEjb.actualizarAnexo(anexo);
                      }
                  }

              }

          }
          
          if (scan != null)
        	  session.removeAttribute("scan_" + idAnexo);

      }catch (Exception e) {
         e.printStackTrace();
      }
   }
*/


    /**
     * Obtiene el {@link es.caib.regweb.model.Anexo} según su identificador.
     * XYZ
     */
    /*
    @RequestMapping(value = "/obtenerAnexo", method = RequestMethod.GET)
    public @ResponseBody
    Anexo obtenerAnexo(@RequestParam Long id, HttpServletRequest request) throws Exception {

        Anexo anexo = anexoEjb.findById(id);

        return anexo;
    }
*/
    
    
    /**
     * Obtiene el {@link es.caib.regweb.model.Anexo} según su identificador.
     *
     */
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
    public Object obtenerRecursoPath1(
    		@PathVariable String path,
    		@PathVariable String resourcename, 
    		HttpServletRequest request,
    		HttpServletResponse response) throws Exception {

        return obtenerRecursoPath2(path, null, resourcename, request, response);
    }
    
    @RequestMapping(value = "/scanwebresource/{resourcename:.+}", method = RequestMethod.GET)
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
           HttpSession session = request.getSession(true);
           session.setAttribute("scan_" + idRegistro, temp);
           session.setAttribute("scan_" + idRegistro +".name", scan.getOriginalFilename());
           session.setAttribute("scan_" + idRegistro +".mime", scan.getContentType());

       }catch (Exception e) {
         // TODO PROCESSSAR ERROR !!!!
          e.printStackTrace();
       }
    }
    
    
    protected void initScan(HttpServletRequest request, Long registroID) {
      HttpSession session = request.getSession(true);

      session.removeAttribute("scan_" + registroID);
      session.removeAttribute("scan_" + registroID +".name");
      session.removeAttribute("scan_" + registroID +".mime");
    }
    
    
    
   /**
    * Crea o modifica un Anexo. Los datos vienen desde un formulario plano y en Json.
    * @param anexo
    * @param request
    * @param result
    * @return
    */
    /*
   @RequestMapping(value="/{accion}/{idRegistro}/{idRegistroDetalle}/{tipoRegistro}", method= RequestMethod.POST)
   @ResponseBody
   public JsonResponse nuevoAnexo(@PathVariable String accion, @PathVariable Long idRegistro,
       @PathVariable Long idRegistroDetalle, @PathVariable String tipoRegistro,
       @RequestBody Anexo anexo, BindingResult result, HttpServletRequest request) {

       log.info("\n\n);");
       log.info("Accion: " + accion);
       //Indica si es el primer anexo que se crea. Lo necesitamos al mostrar los datos en el registro detalle.
       Boolean isPrimerAnexo = false;
       
       if (anexo.getCustodiaID() != null && anexo.getCustodiaID().trim().length() == 0) {
         anexo.setCustodiaID(null);
       }
       

       JsonResponse jsonResponse = new JsonResponse();
       anexoValidator.validate(anexo,result);

       if (result.hasErrors()){//si hay errores
           // Montamos la respuesta de los errores en json
           jsonResponse.setStatus("FAIL");

           List<FieldError> errores = setDefaultMessageToErrors(result.getFieldErrors(), "Anexo");

           jsonResponse.setErrores(errores);
           
           HttpSession session = request.getSession(true);
           session.removeAttribute("scan_" + idRegistro);
           
           
       } else { // Si no hay errores

           try {
               Entidad entidadActiva = getEntidadActiva(request);
               UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);



               //Miramos si es el primer anexo

               List<Anexo> anexos = anexoEjb.getByRegistroDetalle(idRegistroDetalle);


               if(anexos.isEmpty()){
                  isPrimerAnexo= true;
               }

               // Si no se ha escogido ningún TipoDocumental, lo ponemos a null
               if(anexo.getValidezDocumento() != null && anexo.getValidezDocumento() == -1) {
                  anexo.setValidezDocumento(null);
               }
               anexo.setFechaCaptura(new Date());


               if("entrada".equals(tipoRegistro)){
                   RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
                   // Dias que han pasado desde que se creó el registroEntrada
                   Long dias = RegistroUtils.obtenerDiasRegistro(registroEntrada.getFecha());

                   if(accion.equals("nuevo")){//NUEVO ANEXO
                       // Si han pasado más de los dias de visado de la entidad se crearan historicos de todos los
                       // cambios y se cambia el estado del registroEntrada a pendiente visar
                       if(dias >= entidadActiva.getDiasVisado()){
                          registroEntradaEjb.cambiarEstado(idRegistro, RegwebConstantes.ESTADO_PENDIENTE_VISAR);

                          // Creamos el historico de registro de entrada
                          historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);

                       }

                       anexo.setRegistroDetalle(registroEntrada.getRegistroDetalle());
                       anexo = anexoEjb.persist(anexo);

                   }else if(accion.equals("editar")){// MODIFICACION DE ANEXO

                       if(dias >= entidadActiva.getDiasVisado()){ // Si han pasado más de los dias de visado cambiamos estado registro
                           registroEntradaEjb.cambiarEstado(idRegistro, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                       }
                       anexo.setRegistroDetalle(registroEntrada.getRegistroDetalle());
                       anexoEjb.actualizarAnexo(anexo);

                       // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
                       historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);
                   }
               }else{
                   RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
                   // Dias que han pasado desde que se creó el registroEntrada
                   Long dias = RegistroUtils.obtenerDiasRegistro(registroSalida.getFecha());

                   if(accion.equals("nuevo")){//NUEVO ANEXO
                       // Si han pasado más de los dias de visado de la entidad se crearan historicos de todos los
                       // cambios y se cambia el estado del registroEntrada a pendiente visar
                       if(dias >= entidadActiva.getDiasVisado()){
                          registroSalidaEjb.cambiarEstado(idRegistro, RegwebConstantes.ESTADO_PENDIENTE_VISAR);

                          // Creamos el historico de registro de entrada
                          historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);

                       }

                       anexo.setRegistroDetalle(registroSalida.getRegistroDetalle());
                       anexo = anexoEjb.persist(anexo);

                   }else if(accion.equals("editar")){// MODIFICACION DE ANEXO

                       if(dias >= entidadActiva.getDiasVisado()){ // Si han pasado más de los dias de visado cambiamos estado registro
                           registroSalidaEjb.cambiarEstado(idRegistro, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                       }
                       anexo.setRegistroDetalle(registroSalida.getRegistroDetalle());
                       anexoEjb.actualizarAnexo(anexo);

                       // Creamos el historico de registro de entrada, siempre creamos histórico independiente de los dias.
                       historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ANEXOS,true);
                   }
               }
               //Indicamos que el proceso ha ido bien
               jsonResponse.setStatus("SUCCESS");
               //Montamos la respuesta en json
               AnexoJson anexoJson = new AnexoJson();
               anexoJson.setId(anexo.getId().toString());
               anexoJson.setNombre(anexo.getTitulo());
               anexoJson.setPrimerAnexo(isPrimerAnexo);

               jsonResponse.setResult(anexoJson);
               
               // Guardam el document escanejat a una variable de sessió amb l'identificador de l'annex
               HttpSession session = request.getSession(true);
               Object scan = session.getAttribute("scan_" + idRegistro);
               session.setAttribute("scan_" + anexo.getId(), scan);
               session.removeAttribute("scan_" + idRegistro);
               
           } catch (Exception e) {
               e.printStackTrace();
               HttpSession session = request.getSession(true);
               session.removeAttribute("scan_" + idRegistro);
           }
       }

       return jsonResponse;
   }
   */






}
