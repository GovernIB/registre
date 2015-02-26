package es.caib.regweb.persistence.ejb;


import es.caib.regweb.model.Archivo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
public interface ArchivoLocal extends BaseEjb<Archivo, Long> {

    /**
     *
     * @param archivo
     * @return
     * @throws Exception
     */
    @RolesAllowed({"RWE_ADMIN"})
    public boolean borrarArchivo(Archivo archivo) throws Exception;
}
