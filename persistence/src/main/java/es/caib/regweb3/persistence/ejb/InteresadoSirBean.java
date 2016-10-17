package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.sir.core.model.InteresadoSir;
import es.caib.regweb3.utils.RegwebConstantes;
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

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> result = em.createQuery("Select distinct(i.id) from InteresadoSir as i where i.idAsientoRegistralSir.entidad.id =:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        Integer total = result.size();

        if(result.size() > 0){

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (result.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from InteresadoSir where id in (:result) ").setParameter("result", subList).executeUpdate();
                result.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from InteresadoSir where id in (:result) ").setParameter("result", result).executeUpdate();
        }

        return total;

    }
}
