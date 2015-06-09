package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.RegistroDetalle;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.AnexoFull;
import es.caib.regweb.persistence.utils.AnnexFileSystemManager;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.scan.ScannerManager;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.AnexoWebValidator;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
  
    public static final int BASE_IFRAME_HEIGHT = 495 - FILE_TAB_HEIGHT;
  

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
           
        } catch(I18NException i18n) {
          log.error(i18n.getMessage(), i18n);
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
        HttpServletResponse response, Model model) throws Exception {

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



    protected SignatureCustody getSignatureCustody(AnexoForm anexoForm, DocumentCustody dc) {
      if (log.isDebugEnabled()) {
        log.debug("  ------------------------------");
        log.debug(" anexoForm.getFirmaFile() = " + anexoForm.getFirmaFile());
        log.debug(" anexoForm.getFirmaFile().isEmpty() = " + anexoForm.getFirmaFile().isEmpty());
        log.debug(" anexoForm.isFirmaFileDelete() = " + anexoForm.isSignatureFileDelete());
      }
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

}
