package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Pendiente;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created 14/10/14 9:55
 *
 * @author mgonzalez
 */
@Stateless(name = "PendienteEJB")
@SecurityDomain("seycon")
public class PendienteBean extends BaseEjbJPA<Pendiente, Long> implements PendienteLocal {
  
  
    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public Pendiente findById(Long id) throws Exception {

        return em.find(Pendiente.class, id);
    }


    public Pendiente findByIdOrganismo(Long idOrganismo) throws Exception {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente where pendiente.idOrganismo=:idOrganismo");
        q.setParameter("idOrganismo", idOrganismo);
        return (Pendiente)q.getSingleResult();

    }

    public List<Pendiente> findByEstadoProcesado(String estado, Boolean procesado) throws Exception {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente where pendiente.estado=:estado and pendiente.procesado=:procesado");
        q.setParameter("estado", estado);
        q.setParameter("procesado", procesado);
        return q.getResultList();

    }

    public List<Pendiente> findPendientesProcesar() throws Exception {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente where pendiente.procesado = false");
        return q.getResultList();

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Pendiente> getAll() throws Exception {

        return  em.createQuery("Select pendiente from Pendiente as pendiente order by pendiente.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(pendiente.id) from Pendiente as pendiente");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Pendiente> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select pendiente from Pendiente as pendiente order by pendiente.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }
}
