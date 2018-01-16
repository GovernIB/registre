package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatTipoVia;
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

@Stateless(name = "CatTipoViaEJB")
@SecurityDomain("seycon")
public class CatTipoViaBean extends BaseEjbJPA<CatTipoVia, Long> implements CatTipoViaLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public CatTipoVia getReference(Long id) throws Exception {

        return em.getReference(CatTipoVia.class, id);
    }

    @Override
    public CatTipoVia findById(Long id) throws Exception {

        return em.find(CatTipoVia.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatTipoVia> getAll() throws Exception {

        return  em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia order by catTipoVia.codigoTipoVia").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(catTipoVia.id) from CatTipoVia as catTipoVia");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<CatTipoVia> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia order by catTipoVia.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatTipoVia findByCodigo(Long codigo) throws Exception {

        Query q = em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia where catTipoVia.codigoTipoVia = :codigo");

         q.setParameter("codigo", codigo);

         List<CatTipoVia> catTipoVia = q.getResultList();
         if(catTipoVia.size() == 1){
             return catTipoVia.get(0);
         }else{
             return  null;
         }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public CatTipoVia findByDescripcion(String descripcion) throws Exception{

        Query q = em.createQuery("Select catTipoVia from CatTipoVia as catTipoVia where catTipoVia.descripcionTipoVia = :descripcion");

        q.setParameter("descripcion", descripcion);

        List<CatTipoVia> catTipoVia = q.getResultList();
        if(catTipoVia.size() == 1){
            return catTipoVia.get(0);
        }else{
            return  null;
        }
    }

}
