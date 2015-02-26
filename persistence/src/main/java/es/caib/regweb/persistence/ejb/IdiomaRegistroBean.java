package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.IdiomaRegistro;
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

@Stateless(name = "IdiomaRegistroEJB")
@SecurityDomain("seycon")
public class IdiomaRegistroBean extends BaseEjbJPA<IdiomaRegistro, Long> implements IdiomaRegistroLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public IdiomaRegistro findById(Long id) throws Exception {

        return em.find(IdiomaRegistro.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<IdiomaRegistro> getAll() throws Exception {

        return  em.createQuery("Select idiomaRegistro from IdiomaRegistro as idiomaRegistro order by idiomaRegistro.orden").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(idiomaRegistro.id) from IdiomaRegistro as idiomaRegistro");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<IdiomaRegistro> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select idiomaRegistro from IdiomaRegistro as idiomaRegistro order by idiomaRegistro.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public IdiomaRegistro findByCodigo(String codigo) throws Exception {

        Query q = em.createQuery("Select idiomaRegistro from IdiomaRegistro as idiomaRegistro where idiomaRegistro.codigo = :codigo");

        q.setParameter("codigo",codigo);

        List<IdiomaRegistro> idiomaRegistro = q.getResultList();

        if(idiomaRegistro.size() == 1){
            return idiomaRegistro.get(0);
        }else{
            return  null;
        }
    }


}
