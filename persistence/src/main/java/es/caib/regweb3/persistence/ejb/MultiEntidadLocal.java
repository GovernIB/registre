package es.caib.regweb3.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 07/07/2021
 */

@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface MultiEntidadLocal {

   /**
    * Determina si una implementación es multientidad (más de una entidad con sir activado)
    * @throws Exception
    */
   boolean isMultiEntidad() throws Exception;
}
