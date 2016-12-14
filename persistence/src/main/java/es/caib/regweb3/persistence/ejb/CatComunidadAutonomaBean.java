package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatComunidadAutonoma;
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

@Stateless(name = "CatComunidadAutonomaEJB")
@SecurityDomain("seycon")
public class CatComunidadAutonomaBean extends BaseEjbJPA<CatComunidadAutonoma, Long> implements CatComunidadAutonomaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatComunidadAutonoma getReference(Long id) throws Exception {

        return em.getReference(CatComunidadAutonoma.class, id);
    }

    @Override
    public CatComunidadAutonoma findById(Long id) throws Exception {

        return em.find(CatComunidadAutonoma.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatComunidadAutonoma> getAll() throws Exception {

        return  em.createQuery("Select catComunidadAutonoma from CatComunidadAutonoma as catComunidadAutonoma order by catComunidadAutonoma.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catComunidadAutonoma.id) from CatComunidadAutonoma as catComunidadAutonoma");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<CatComunidadAutonoma> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catComunidadAutonoma from CatComunidadAutonoma as catComunidadAutonoma order by catComunidadAutonoma.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public CatComunidadAutonoma findByCodigo(Long codigo) throws Exception {
         Query q = em.createQuery("Select catComunidadAutonoma from CatComunidadAutonoma as catComunidadAutonoma where catComunidadAutonoma.codigoComunidad = :codigo");

         q.setParameter("codigo",codigo);

         List<CatComunidadAutonoma> catComunidadAutonoma = q.getResultList();
         if(catComunidadAutonoma.size() == 1){
             return catComunidadAutonoma.get(0);
         }else{
             return  null;
         }

    }
}
