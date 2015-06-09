package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Trazabilidad;
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

@Stateless(name = "TrazabilidadEJB")
@SecurityDomain("seycon")
public class TrazabilidadBean extends BaseEjbJPA<Trazabilidad, Long> implements TrazabilidadLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb")
    private EntityManager em;


    @Override
    public Trazabilidad findById(Long id) throws Exception {

        return em.find(Trazabilidad.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getAll() throws Exception {

        return  em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad order by trazabilidad.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(trazabilidad.id) from Trazabilidad as trazabilidad");

        return (Long) q.getSingleResult();
    }


    @Override
    public List<Trazabilidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad order by trazabilidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    public List<Trazabilidad> getByRegistroSalida(Long idRegistroSalida) throws Exception {

        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroSalida.id = :idRegistroSalida order by trazabilidad.fecha desc");

        q.setParameter("idRegistroSalida",idRegistroSalida);

        return q.getResultList();
    }

    @Override
    public List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws Exception {

        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroEntradaOrigen.id = :idRegistroEntrada or trazabilidad.registroEntradaDestino.id = :idRegistroEntrada " +
                "order by trazabilidad.fecha desc");

        q.setParameter("idRegistroEntrada",idRegistroEntrada);

        return q.getResultList();
    }

    @Override
    public Trazabilidad getByOficioRegistroEntrada(Long idOficioRemision, Long idRegistroEntrada) throws Exception{

        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.oficioRemision.id = :idOficioRemision and trazabilidad.registroEntradaOrigen.id = :idRegistroEntrada");

        q.setParameter("idOficioRemision",idOficioRemision);
        q.setParameter("idRegistroEntrada",idRegistroEntrada);

        return (Trazabilidad) q.getSingleResult();

    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> trazabilidades =  em.createQuery("Select id from Trazabilidad where oficioRemision.usuarioResponsable.entidad.id=:idEntidad").setParameter("idEntidad",idEntidad).getResultList();

        if(trazabilidades.size() >0){
            return em.createQuery("delete from Trazabilidad where id in (:id)").setParameter("id", trazabilidades).executeUpdate();
        }

        return  0;

    }
}
