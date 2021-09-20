package es.caib.regweb3.webapp.utils;


import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Fundació BIT.
 * User: earrivi
 * Date: 7/08/13
 */
public class ArchivoFormManager {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final ArchivoLocal archivoEjb;
    private final CommonsMultipartFile archivoSubido;
    private Archivo archivoActual;
    private String ruta;

    public ArchivoFormManager(ArchivoLocal archivoEjb, CommonsMultipartFile archivoSubido, String ruta) {
        this.archivoEjb = archivoEjb;
        this.archivoSubido = archivoSubido;
        this.ruta = ruta;
    }

    public Archivo prePersist(byte[] fichero) throws Exception {

        if (fichero == null) {
            fichero =  archivoSubido.getBytes();
        }

        archivoActual = new Archivo();
        archivoActual.setMime(archivoSubido.getContentType());
        archivoActual.setNombre(archivoSubido.getOriginalFilename());
        archivoActual.setTamano(Long.valueOf(fichero.length));

        archivoActual = archivoEjb.persist(archivoActual);

        FileSystemManager.crearArchivo(fichero, archivoActual.getId());

        return archivoActual;
    }

    /**
     * Si ha habido un error, elimina el Archivo de la bbdd y del sistema de archivos
     * @throws Exception
     */
    public void processError() throws Exception {

        archivoEjb.remove(this.archivoActual);
        FileSystemManager.eliminarArchivo(this.archivoActual.getId());
    }

    public void processErrorArchivosWithoutThrowException() {

        try {
            processError();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
