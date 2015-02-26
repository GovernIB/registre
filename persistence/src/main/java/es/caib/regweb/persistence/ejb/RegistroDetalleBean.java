package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.RegistroDetalle;
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

@Stateless(name = "RegistroDetalleEJB")
@SecurityDomain("seycon")
public class RegistroDetalleBean extends BaseEjbJPA<RegistroDetalle, Long> implements RegistroDetalleLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public RegistroDetalle findById(Long id) throws Exception {

        return em.find(RegistroDetalle.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroDetalle> getAll() throws Exception {

        return  em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.orden").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroDetalle.id) from RegistroDetalle as registroDetalle");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<RegistroDetalle> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroDetalle from RegistroDetalle as registroDetalle order by registroDetalle.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }
}
