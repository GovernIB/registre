package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RolEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class RolBean extends BaseEjbJPA<Rol, Long> implements RolLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private PluginLocal pluginEjb;


    @Override
    public Rol getReference(Long id) throws I18NException {

        return em.getReference(Rol.class, id);
    }

    @Override
    public Rol findById(Long id) throws I18NException {

        return em.find(Rol.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getAll() throws I18NException {

        return em.createQuery("Select rol from Rol as rol order by rol.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(rol.id) from Rol as rol");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select rol from Rol as rol order by rol.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getByRol(List<String> roles) throws I18NException {

        Query q = em.createQuery("Select rol from Rol as rol where rol.nombre IN (:roles) order by rol.orden");

        q.setParameter("roles", roles);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }
}
