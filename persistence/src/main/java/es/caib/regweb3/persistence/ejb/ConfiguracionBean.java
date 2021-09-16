package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Configuracion;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;


/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 07/07/15
 */

@Stateless(name = "ConfiguracionEJB")
@SecurityDomain("seycon")
@RolesAllowed({"RWE_SUPERADMIN"})
public class ConfiguracionBean extends BaseEjbJPA<Configuracion, Long> implements ConfiguracionLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Configuracion getReference(Long id) throws Exception {

        return em.getReference(Configuracion.class, id);
    }

    @Override
    public Configuracion findById(Long id) throws Exception {

        return em.find(Configuracion.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Configuracion> getAll() throws Exception {

        return  em.createQuery("Select configuracion from Configuracion as configuracion order by configuracion.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(configuracion.id) from Configuracion as configuracion");

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Configuracion> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select configuracion from Configuracion as configuracion order by configuracion.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

}
