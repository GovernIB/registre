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
public interface RolLocal extends BaseEjb<Rol, Long> {

    /**
     * Retorna los {@link es.caib.regweb3.model.Rol} a partir de una lista de roles.
     * @param roles
     * @return
     * @throws Exception
     */
    List<Rol> getByRol(List<String> roles) throws Exception;
}
