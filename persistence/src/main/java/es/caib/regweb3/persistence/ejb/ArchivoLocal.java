package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Archivo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
public interface ArchivoLocal extends BaseEjb<Archivo, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ArchivoEJB";

    /**
     * Obtiene los id's de todos los Archivos de la aplicación
     * @return
     * @throws Exception
     */
    List<Long> getAllLigero() throws Exception;

    /**
     *
     * @param archivo
     * @return
     * @throws Exception
     */
    @RolesAllowed({"RWE_ADMIN"})
    boolean borrarArchivo(Archivo archivo) throws Exception;
}
