package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatServicio;
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

@Stateless(name = "CatServicioEJB")
@SecurityDomain("seycon")
public class CatServicioBean extends BaseEjbJPA<CatServicio, Long> implements CatServicioLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatServicio getReference(Long id) throws Exception {

        return em.getReference(CatServicio.class, id);
    }

    @Override
    public CatServicio findById(Long id) throws Exception {

        return em.find(CatServicio.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatServicio> getAll() throws Exception {

        return  em.createQuery("Select catServicio from CatServicio as catServicio order by catServicio.descServicio").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catServicio.id) from CatServicio as catServicio");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatServicio> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catServicio from CatServicio as catServicio order by catServicio.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatServicio findByCodigo(Long codigo) throws Exception {
        Query q = em.createQuery("Select catServicio from CatServicio as catServicio where catServicio.codServicio = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatServicio> catServicio = q.getResultList();

        if(catServicio.size() == 1){
            return catServicio.get(0);
        }else{
            return  null;
        }
    }
}
