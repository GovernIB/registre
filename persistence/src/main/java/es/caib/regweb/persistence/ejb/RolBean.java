package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Rol;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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
public class RolBean extends BaseEjbJPA<Rol, Long> implements RolLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


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
    public List<Rol> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select rol from Rol as rol order by rol.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<Rol> getByRol(List<String> roles) throws Exception {

        Query q = em.createQuery("Select rol from Rol as rol where rol.nombre IN (:roles) order by rol.orden");

        q.setParameter("roles",roles);

        return q.getResultList();

    }
}
