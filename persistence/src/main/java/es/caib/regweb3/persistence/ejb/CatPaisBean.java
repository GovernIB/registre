package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatPais;
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

@Stateless(name = "CatPaisEJB")
@SecurityDomain("seycon")
public class CatPaisBean extends BaseEjbJPA<CatPais, Long> implements CatPaisLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatPais getReference(Long id) throws Exception {

        return em.getReference(CatPais.class, id);
    }

    @Override
    public CatPais findById(Long id) throws Exception {

        return em.find(CatPais.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatPais> getAll() throws Exception {

        return  em.createQuery("Select catPais from CatPais as catPais order by catPais.descripcionPais").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catPais.id) from CatPais as catPais");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatPais> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catPais from CatPais as catPais order by catPais.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatPais findByCodigo(Long codigo) throws Exception {
        Query q = em.createQuery("Select catPais from CatPais as catPais where catPais.codigoPais = :codigo");

        q.setParameter("codigo",codigo);
        q.setHint("org.hibernate.readOnly", true);

        List<CatPais> catPais = q.getResultList();
        if(catPais.size() == 1){
            return catPais.get(0);
        }else{
            return  null;
        }
    }
}
