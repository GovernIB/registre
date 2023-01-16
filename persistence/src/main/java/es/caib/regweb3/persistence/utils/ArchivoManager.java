package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.utils.MimeTypeUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Fundació BIT.
 * User: earrivi
 * Date:20/06/2016
 */
public class ArchivoManager {

    protected final Logger log = LoggerFactory.getLogger(getClass());

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

        if(mime == null){
            mime = MimeTypeUtils.getMimeTypeFileName(nombreFichero);
            if(mime == null) {
                mime = "application/octet-stream";
            }
        }

        archivoActual.setMime(mime);
        archivoActual.setNombre(nombreFichero);
        archivoActual.setTamano((long) fichero.length);

        archivoActual = archivoEjb.persist(archivoActual);

        FileSystemManager.crearArchivo(fichero, archivoActual.getId());

        return archivoActual;
    }

    /**
     * Si ha habido un error, elimina el Archivo de la bbdd y del sistema de archivos
     * @throws I18NException
     */
    public void processError() throws I18NException {

        archivoEjb.remove(this.archivoActual);
        FileSystemManager.eliminarArchivo(this.archivoActual.getId());
    }

    public void processErrorArchivosWithoutThrowException() {

        try {
            processError();
        } catch (I18NException e) {
            log.info("Error eliminado archivos en disco..");
            e.printStackTrace();
        }
    }

}
