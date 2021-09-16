package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA","RWE_WS_CIUDADANO"})
public class RolBean extends BaseEjbJPA<Rol, Long> implements RolLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB private PluginLocal pluginEjb;


    @Override
    public Rol getReference(Long id) throws Exception {

        return em.getReference(Rol.class, id);
    }

    @Override
    public Rol findById(Long id) throws Exception {

        return em.find(Rol.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getAll() throws Exception {

        return  em.createQuery("Select rol from Rol as rol order by rol.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(rol.id) from Rol as rol");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select rol from Rol as rol order by rol.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Rol> getByRol(List<String> roles) throws Exception {

        Query q = em.createQuery("Select rol from Rol as rol where rol.nombre IN (:roles) order by rol.orden");

        q.setParameter("roles",roles);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();

    }
}
