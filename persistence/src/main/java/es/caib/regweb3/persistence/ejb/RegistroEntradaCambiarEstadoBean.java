package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
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
 * @author anadal
 * Date: 16/01/14
 */

@Stateless(name = "RegistroEntradaCambiarEstadoEJB")
@SecurityDomain("seycon")
public class RegistroEntradaCambiarEstadoBean extends BaseEjbJPA<RegistroEntrada, Long>
   implements RegistroEntradaCambiarEstadoLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;



    @Override
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception{

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", idRegistro);
        q.executeUpdate();
    }

    @Override
    public RegistroEntrada getReference(Long id) throws Exception {

        return em.getReference(RegistroEntrada.class, id);
    }

    @Override
    public RegistroEntrada findById(Long id) throws Exception {

        return em.find(RegistroEntrada.class, id);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getAll() throws Exception {

        return  em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada order by registroEntrada.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroEntrada.id) from RegistroEntrada as registroEntrada");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroEntrada from RegistroEntrada as registroEntrada order by registroEntrada.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


}
