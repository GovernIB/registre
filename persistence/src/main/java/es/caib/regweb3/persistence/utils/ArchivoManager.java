package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import org.apache.log4j.Logger;

/**
 * Created by Fundació BIT.
 * User: earrivi
 * Date:20/06/2016
 */
public class ArchivoManager {

    protected final Logger log = Logger.getLogger(getClass());

    private final WebServicesMethodsLocal webServicesMethodsLocal;
    private Archivo archivoActual;
    private String nombreFichero;
    private String mime;
    private byte[] fichero;

    public ArchivoManager(WebServicesMethodsLocal webServicesMethodsLocal, String nombreFichero, String mime, byte[] fichero) {
        this.webServicesMethodsLocal = webServicesMethodsLocal;
        this.nombreFichero = nombreFichero;
        this.mime = mime;
        this.fichero = fichero;
    }

    public Archivo prePersist() throws Exception {

        archivoActual = new Archivo();
        archivoActual.setMime(mime);
        archivoActual.setNombre(nombreFichero);
        archivoActual.setTamano((long) fichero.length);

        archivoActual = webServicesMethodsLocal.persistArchivo(archivoActual);

        FileSystemManager.crearArchivo(fichero, archivoActual.getId());

        return archivoActual;
    }

    /**
     * Si ha habido un error, elimina el Archivo de la bbdd y del sistema de archivos
     * @throws Exception
     */
    public void processError() throws Exception {

        webServicesMethodsLocal.removeArchivo(this.archivoActual);
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
