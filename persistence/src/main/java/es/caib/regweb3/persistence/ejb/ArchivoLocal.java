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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface ArchivoLocal extends BaseEjb<Archivo, Long> {

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
