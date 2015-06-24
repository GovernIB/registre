package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatProvincia;
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

@Stateless(name = "CatProvinciaEJB")
@SecurityDomain("seycon")
public class CatProvinciaBean extends BaseEjbJPA<CatProvincia, Long> implements CatProvinciaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatProvincia findById(Long id) throws Exception {

        return em.find(CatProvincia.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatProvincia> getAll() throws Exception {

        return  em.createQuery("Select catProvincia from CatProvincia as catProvincia order by catProvincia.descripcionProvincia").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catProvincia.id) from CatProvincia as catProvincia");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<CatProvincia> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catProvincia from CatProvincia as catProvincia order by catProvincia.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public CatProvincia findByCodigo(Long codigo) throws Exception {
         Query q = em.createQuery("Select catProvincia from CatProvincia as catProvincia where catProvincia.codigoProvincia = :codigo");

         q.setParameter("codigo",codigo);

         List<CatProvincia> catProvincia = q.getResultList();
         if(catProvincia.size() == 1){
             return catProvincia.get(0);
         }else{
             return  null;
         }

    }

}
