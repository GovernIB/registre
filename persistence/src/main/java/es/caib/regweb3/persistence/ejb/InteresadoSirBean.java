package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.sir.core.model.InteresadoSir;
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

@Stateless(name = "InteresadoSirEJB")
@SecurityDomain("seycon")
public class InteresadoSirBean extends BaseEjbJPA<InteresadoSir, Long> implements InteresadoSirLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public InteresadoSir findById(Long id) throws Exception {

        return em.find(InteresadoSir.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<InteresadoSir> getAll() throws Exception {

        return  em.createQuery("Select interesadoSir from InteresadoSir as interesadoSir order by interesadoSir.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(interesadoSir.id) from InteresadoSir as interesadoSir");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<InteresadoSir> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select interesadoSir from InteresadoSir as interesadoSir order by interesadoSir.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


}
