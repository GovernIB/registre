package es.caib.regweb3.persistence.ejb;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 07/07/2021
 */

@Stateless(name = "MultiEntidadEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public class MultiEntidadBean implements MultiEntidadLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;


    @Override
    public boolean isMultiEntidadSir() throws I18NException {
        return em.createQuery("Select entidad.id from Entidad as entidad where entidad.sir = true order by entidad.id").getResultList().size() > 1;
    }

}


