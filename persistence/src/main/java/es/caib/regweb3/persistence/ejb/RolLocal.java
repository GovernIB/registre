package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface RolLocal extends BaseEjb<Rol, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/RolEJB";


    /**
     * Retorna los {@link es.caib.regweb3.model.Rol} a partir de una lista de roles.
     *
     * @param roles
     * @return
     * @throws Exception
     */
    List<Rol> getByRol(List<String> roles) throws Exception;
}
