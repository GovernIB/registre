package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import org.fundaciobit.genapp.common.i18n.I18NException;

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

    String JNDI_NAME = "java:app/regweb3-persistence/RolEJB";


    /**
     * Retorna los {@link es.caib.regweb3.model.Rol} a partir de una lista de roles.
     *
     * @param roles
     * @return
     * @throws I18NException
     */
    List<Rol> getByRol(List<String> roles) throws I18NException;
}
