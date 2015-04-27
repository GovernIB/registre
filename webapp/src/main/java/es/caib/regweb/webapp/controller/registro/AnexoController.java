package es.caib.regweb.webapp.controller.registro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import es.caib.regweb.model.Anexo;
import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.ejb.AnexoLocal;
import es.caib.regweb.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb.persistence.ejb.HistoricoRegistroSalidaLocal;
import es.caib.regweb.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.utils.AnnexFileSystemManager;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.ScannerManager;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.utils.AnexoFormManager;
import es.caib.regweb.webapp.utils.AnexoJson;
import es.caib.regweb.webapp.utils.JsonResponse;
import es.caib.regweb.webapp.validator.AnexoWebValidator;

/**
 * Created 3/06/14 14:22
 *
 * @author mgonzalez
 * @author anadal (plugin de custodia i errores)
 */
@Controller
@RequestMapping(value = "/anexo")
public class AnexoController extends BaseController {


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


   /**
   * Método que guarda el archivo fisico asociado a un anexo a posteriori, en este metodo solo se gestiona el archivo asociado
   * @param idAnexo identificador del anexo
   * @param accion  indica si es nuevo o edición
   * @param borrar  indica si se ha de borrar el archivo asociado
   */
   @RequestMapping(value = "/guardarArchivo/{idAnexo}/{accion}/{borrar}", method = RequestMethod.POST)
   public void archivo(@PathVariable Long idAnexo,@PathVariable String accion, @PathVariable boolean borrar,  MultipartHttpServletRequest request, HttpServletResponse response)  {

	  Object scan = null;
      MultipartFile doc = null;
      MultipartFile firma = null;
      AnexoFormManager afm;

      try {

          Anexo anexo = anexoEjb.findById(idAnexo);

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
                  if(!accion.equals("nuevo") ){
                    // Eliminar el archivo fisico del sistema de archivos
                    AnnexFileSystemManager.eliminarArchivo(anexo.getId());
                  }
                  //Inicializamos el archivo form manager con el nuevo archivo doc
                  afm = new AnexoFormManager(anexoEjb, doc, null, RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                  // Método que actualiza el anexo asociandole el nuevo archivo.
                  afm.prePersist(anexo);

              } else { // no modificamos archivo

                  if(borrar){ // No hay archivo nuevo y borramos el archivo antiguo
                     log.info("borrar");
                     AnnexFileSystemManager.eliminarArchivo(anexo.getId());
                     anexo.setNombreFicheroAnexado("");
                     anexoEjb.actualizarAnexo(anexo);
                  }
              }
          }else{// Viene firma y gestion del doc
              //Cogemos la firma
              firma = request.getFile("firma");
              // Comprobar que doc y firma no sean null
              if(firma != null && doc != null){
                  if(!accion.equals("nuevo") ){ // Si editamos
                    // Eliminar los archivos fisicos del sistema de archivos
                    AnnexFileSystemManager.eliminarArchivo(anexo.getId());
                  }
                  // Guardar el anexo con los dos archivos asociados
                  afm = new AnexoFormManager(anexoEjb, doc, firma, RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                  afm.prePersist(anexo);
              }else{
                   if(doc != null ){ //firma es null, gestionamos solo el doc

                      if(!accion.equals("nuevo") ){ //Editamos
                        // Eliminar el archivo fisico del sistema de archivos
                        AnnexFileSystemManager.eliminarArchivo(anexo.getId());
                      }
                      //Inicializamos el archivo form manager con el nuevo archivo doc
                      afm = new AnexoFormManager(anexoEjb, doc, null, RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                      // Método que actualiza el anexo asociandole el nuevo archivo.
                      afm.prePersist(anexo);

                  } else { // no modificamos archivo

                      if(borrar){ // No hay archivo nuevo y borramos el archivo antiguo
                         log.info("borrar");
                         AnnexFileSystemManager.eliminarArchivo(anexo.getId());
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



    /**
     * Obtiene el {@link es.caib.regweb.model.Anexo} según su identificador.
     *
     */
    @RequestMapping(value = "/obtenerAnexo", method = RequestMethod.GET)
    public @ResponseBody
    Anexo obtenerAnexo(@RequestParam Long id, HttpServletRequest request) throws Exception {

        Anexo anexo = anexoEjb.findById(id);

        return anexo;
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
    	String resource = (path1 != null ? path1 + "/" : "") + (path2 != null ? path2 + "/" : "") + resourcename;
        ScanWebResource recurs = ScannerManager.getResource(request, tipusScan, resource);

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
       }catch (Exception e) {
          e.printStackTrace();
       }
    }
    
   /**
    * Crea o modifica un Anexo. Los datos vienen desde un formulario plano y en Json.
    * @param anexo
    * @param request
    * @param result
    * @return
    */
   @RequestMapping(value="/{accion}/{idRegistro}/{idRegistroDetalle}/{tipoRegistro}", method= RequestMethod.POST)
   @ResponseBody
   public JsonResponse nuevoAnexo(@PathVariable String accion, @PathVariable Long idRegistro,
       @PathVariable Long idRegistroDetalle, @PathVariable String tipoRegistro,
       @RequestBody Anexo anexo, BindingResult result, HttpServletRequest request) {

       log.info("Accion: " + accion);
       //Indica si es el primer anexo que se crea. Lo necesitamos al mostrar los datos en el registro detalle.
       Boolean isPrimerAnexo = false;


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


   /**
   * Elimina un Anexo de la variable de sesion que almacena los interesados
   * @param idAnexo
   * @param idRegistroDetalle
   * @return
   */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarAnexo(@RequestParam String idAnexo,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        try {
            anexoEjb.eliminarAnexoRegistroDetalle(new Long(idAnexo), new Long(idRegistroDetalle));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


     /**
     * Función que nos permite mostrar el contenido de un anexo
     * @param anexoId identificador del anexo
     */
     @RequestMapping(value = "/{anexoId}", method = RequestMethod.GET)
     public void  anexo(@PathVariable("anexoId") Long anexoId, HttpServletRequest request, HttpServletResponse response)  {
          Anexo anexo = null;

          try {
              anexo = anexoEjb.findById(anexoId);
          } catch (Exception e) {
              e.printStackTrace();
          }

          fullDownload(anexoId, anexo.getNombreFicheroAnexado(), anexo.getTipoMIME(), false,response);
     }

      /**
     * Función que nos permite mostrar el contenido de un firma de un anexo
     * @param anexoId identificador del anexo
     */
     @RequestMapping(value = "/firma/{anexoId}", method = RequestMethod.GET)
     public void  firma(@PathVariable("anexoId") Long anexoId, HttpServletRequest request, HttpServletResponse response)  {
          Anexo anexo = null;

          try {
              anexo = anexoEjb.findById(anexoId);
          } catch (Exception e) {
              e.printStackTrace();
          }

          fullDownload(anexoId, anexo.getNombreFicheroAnexado(), anexo.getTipoMIME(), true,response);

     }

     /**
     *  Función que obtiene los datos de un archivo para mostrarlo
     * @param archivoId  identificador del archivo
     * @param filename   nombre del archivo
     * @param contentType
     * @param response
     */
     public void fullDownload(Long archivoId, String filename, String contentType, boolean firma, HttpServletResponse response) {

          //FileInputStream input = null;
          OutputStream output = null;
          MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
          byte[] data;
          try {
              if (archivoId != null) {
                  if(!firma){
                    DocumentCustody dc = AnnexFileSystemManager.getArchivo(archivoId);
                    filename = dc.getName();
                    data = dc.getData();
                  }else{
                    SignatureCustody sc = AnnexFileSystemManager.getFirma(archivoId);
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
              log.info(e);
          }  catch (Exception e) {
              e.printStackTrace();
          }

     }


}
