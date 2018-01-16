package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroSalida;
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
 *  @author anadal
 * Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaCambiarEstadoEJB")
@SecurityDomain("seycon")
public class RegistroSalidaCambiarEstadoBean extends BaseEjbJPA<RegistroSalida, Long>
    implements RegistroSalidaCambiarEstadoLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;



    @Override
    public RegistroSalida getReference(Long id) throws Exception {

        return em.getReference(RegistroSalida.class, id);
    }

    @Override
    public RegistroSalida findById(Long id) throws Exception {

        return em.find(RegistroSalida.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getAll() throws Exception {

        return  em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(registroSalida.id) from RegistroSalida as registroSalida");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select registroSalida from RegistroSalida as registroSalida order by registroSalida.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public void cambiarEstado(Long idRegistro, Long idEstado) throws Exception{

        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", idRegistro);
        q.executeUpdate();
    }

   

}
