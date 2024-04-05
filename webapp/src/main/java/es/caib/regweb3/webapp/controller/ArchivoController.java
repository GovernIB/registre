package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Created by Fundacio Bit
 *
 * @author jpernia
 * Date: 28/05/14
 */
@Controller
public class ArchivoController extends BaseController{

    //protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @EJB(mappedName =ArchivoLocal.JNDI_NAME)
    private ArchivoLocal archivoEjb;

    @RequestMapping(value = "/archivo/{archivoId}", method = RequestMethod.GET)
    public void  descargarArchivo(@PathVariable("archivoId") Long archivoId, HttpServletResponse response)  {

        descargarArchivo(archivoId, true, response);
    }

    @RequestMapping(value = "/archivo/{archivoId}/{attachment}", method = RequestMethod.GET)
    public void  descargarArchivo(@PathVariable("archivoId") Long archivoId, @PathVariable("attachment") Boolean attachment,HttpServletResponse response)  {
        Archivo archivo = null;

        try {
            archivo = archivoEjb.findById(archivoId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fullDownload(archivoId, archivo.getNombre(), archivo.getMime(), response, attachment);

    }

    public void fullDownload(Long archivoId, String filename, String contentType, HttpServletResponse response, boolean attachment) {

        FileInputStream input;
        OutputStream output;
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();

        try {
            if (archivoId != null) {

                File file = FileSystemManager.getArchivo(archivoId);
                if (filename == null) {
                    filename = "imatge.jpg"; // archivo.getNombre()
                }
                if (contentType == null) {
                    contentType = mimeTypesMap.getContentType(file);
                }
                response.setContentType(contentType);
                if(attachment){
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                }else{
                    response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
                }

                response.setContentLength((int) file.length());

                output = response.getOutputStream();
                input = new FileInputStream(file);

                IOUtils.copy(input, output);

                input.close();
                output.close();
            }

        } catch (NumberFormatException e) {
            log.info(e.getMessage());
        }  catch (Exception e) {
            e.printStackTrace();
        }


    }
}
