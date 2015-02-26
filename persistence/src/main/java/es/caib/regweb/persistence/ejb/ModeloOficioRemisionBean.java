package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.ModeloOficioRemision;
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
 * @author jpernia
 * Date: 2/09/14
 */

@Stateless(name = "ModeloOficioRemisionEJB")
@SecurityDomain("seycon")
public class ModeloOficioRemisionBean extends BaseEjbJPA<ModeloOficioRemision, Long> implements ModeloOficioRemisionLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public ModeloOficioRemision findById(Long id) throws Exception {

        return em.find(ModeloOficioRemision.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<ModeloOficioRemision> getAll() throws Exception {

        return  em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision order by modeloOficioRemision.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(modeloOficioRemision.id) from ModeloOficioRemision as modeloOficioRemision");

        return (Long) q.getSingleResult();
    }

    @Override
    public Long getTotal(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select count(modeloOficioRemision.id) from ModeloOficioRemision as modeloOficioRemision where modeloOficioRemision.entidad.id = :idEntidad");
        q.setParameter("idEntidad",idEntidad);

        return (Long) q.getSingleResult();
    }

    @Override
    public List<ModeloOficioRemision> getByEntidad(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision where modeloOficioRemision.entidad.id = :idEntidad");
        q.setParameter("idEntidad",idEntidad);

        return q.getResultList();
    }



    @Override
    public List<ModeloOficioRemision> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision order by modeloOficioRemision.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public List<ModeloOficioRemision> getPagination(int inicio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select modeloOficioRemision from ModeloOficioRemision as modeloOficioRemision where modeloOficioRemision.entidad.id = :idEntidad order by modeloOficioRemision.id");
        q.setParameter("idEntidad",idEntidad);
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

}
