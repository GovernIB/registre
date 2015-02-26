package es.caib.regweb.webapp.utils;


import es.caib.regweb.model.Archivo;
import es.caib.regweb.persistence.ejb.ArchivoLocal;
import es.caib.regweb.persistence.utils.FileSystemManager;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Fundació BIT.
 * User: earrivi
 * Date: 7/08/13
 */
public class ArchivoFormManager {

    protected final Logger log = Logger.getLogger(getClass());

    private final ArchivoLocal archivoEjb;
    private final CommonsMultipartFile archivoSubido;
    private Archivo archivoActual;
    private String ruta;

    public ArchivoFormManager(ArchivoLocal archivoEjb, CommonsMultipartFile archivoSubido, String ruta) {
        this.archivoEjb = archivoEjb;
        this.archivoSubido = archivoSubido;
        this.ruta = ruta;
    }

    public Archivo prePersist() throws Exception {

        archivoActual = new Archivo();
        archivoActual.setMime(archivoSubido.getContentType());
        archivoActual.setNombre(archivoSubido.getOriginalFilename());
        archivoActual.setTamano(Long.valueOf(archivoSubido.getBytes().length));

        archivoActual = archivoEjb.persist(archivoActual);

        FileSystemManager.crearArchivo(archivoSubido.getBytes(), archivoActual.getId());

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
