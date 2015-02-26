package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Idioma;
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

@Stateless(name = "IdiomaEJB")
@SecurityDomain("seycon")
public class IdiomaBean extends BaseEjbJPA<Idioma, Long> implements IdiomaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public Idioma findById(Long id) throws Exception {

        return em.find(Idioma.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Idioma> getAll() throws Exception {

        return  em.createQuery("Select idioma from Idioma as idioma order by idioma.orden").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(idioma.id) from Idioma as idioma");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Idioma> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select idioma from Idioma as idioma order by idioma.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }
}
