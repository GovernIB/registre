package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import org.apache.log4j.Logger;

/**
 * Created by Fundació BIT.
 * User: earrivi
 * Date:20/06/2016
 */
public class ArchivoManager {

    protected final Logger log = Logger.getLogger(getClass());

    private ArchivoLocal archivoEjb;
    private Archivo archivoActual;
    private String nombreFichero;
    private String mime;
    private byte[] fichero;

    public ArchivoManager(Archivo archivoActual, ArchivoLocal archivoEjb) {
        this.archivoActual = archivoActual;
        this.archivoEjb = archivoEjb;
    }

    public ArchivoManager(ArchivoLocal archivoEjb, String nombreFichero, String mime, byte[] fichero) {
        this.archivoEjb = archivoEjb;
        this.nombreFichero = nombreFichero;
        this.mime = mime;
        this.fichero = fichero;
    }

    public Archivo prePersist() throws Exception {

        archivoActual = new Archivo();
        archivoActual.setMime(mime);
        archivoActual.setNombre(nombreFichero);
        archivoActual.setTamano((long) fichero.length);

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
