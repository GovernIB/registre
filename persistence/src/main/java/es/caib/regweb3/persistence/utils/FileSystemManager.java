package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by Fundació BIT.
 * User: anadal
 * Date: 31/07/13
 */
public class FileSystemManager {



    protected static final Logger log = Logger.getLogger(FileSystemManager.class);

    /**
     * Obtiene el fichero existente en el sistema de archivos
     * @param id
     * @return
     */
    public static File getArchivo(Long id) {
        return new File(getArchivosPath(), String.valueOf(id));
    }

    /**
     * Obtiene el fichero existente en el sistema de archivos
     * @param id
     * @return
     */
    public static byte[] getBytesArchivo(Long id) throws Exception{
        File file = new File(getArchivosPath(), String.valueOf(id));
        FileInputStream input = null;
        byte[] content = null;

        try {
            input = new FileInputStream(file);
            content = IOUtils.toByteArray(input);
        } catch (FileNotFoundException e) {
            log.info("El archivo no se encuentro en el path indicado");
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return content;
    }



    /**
     * Obtiene la ruta donde se almacenarán los archivos
     * @return
     */
    public static File getArchivosPath() {
        if(System.getProperty(RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY) != null) {
            return new File(System.getProperty(RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY));
        }else{
            return null;
        }
    }

    /**
     * Elimina un Archivo del sistema de archivos
     * @param id
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     *         casos.
     */
    public static boolean eliminarArchivo(Long id) {

        File file = new File(getArchivosPath(), String.valueOf(id));

        if (file.exists()) {
            if (!file.delete()) {
                file.deleteOnExit();
                log.warn("Per algun motiu desconegut no s'ha pogut borrar l'arxiu "
                        + file.getAbsolutePath());
                return false;
            }
        } else {
            log.warn("El fichero " + file.getAbsolutePath()
                    + " no existe y no se ha podido eliminar.");
        }
        return true;
    }

    /**
     * Crea un archivo en el sistema de archivos obtenido de un formulario
     * @param src
     * @param dstId
     * @return
     * @throws Exception
     */
    /*
    public static File crearArchivo(MultipartFile src, Long dstId) throws Exception {
        File newFile = new File(getArchivosPath(), String.valueOf(dstId));
        src.transferTo(newFile);
        return newFile;
    }
    */

    /**
     * Crea un archivo en el sistema de archivos
     * @param archivo
     * @param dstId
     * @return
     * @throws Exception
     */
    public static File crearArchivo(byte[] archivo, Long dstId) throws Exception {

        //log.info("Creamos el archivo: " + getArchivosPath()+dstId.toString());
        File file = new File(getArchivosPath(), String.valueOf(dstId));
        FileOutputStream out = new FileOutputStream(file);

        if (!file.exists()) {
            file.createNewFile();
        }

        try{
            out.write(archivo);
            out.flush();
        }finally {
            out.close();
        }

        return getArchivo(dstId);

    }


    /**
     * Copia un input en un ouput
     * @param input
     * @param output
     * @throws java.io.IOException
     */
    public static void copy(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
    }
}
