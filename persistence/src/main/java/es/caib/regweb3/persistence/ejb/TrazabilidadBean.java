package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
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

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;


    @Override
    public Trazabilidad getReference(Long id) throws Exception {

        return em.getReference(Trazabilidad.class, id);
    }

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
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad order by trazabilidad.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByRegistroSalida(Long idRegistroSalida) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroSalida.id = :idRegistroSalida or trazabilidad.registroSalidaRectificado.id = :idRegistroSalida order by trazabilidad.fecha ");

        q.setParameter("idRegistroSalida", idRegistroSalida);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroEntradaOrigen.id = :idRegistroEntrada or trazabilidad.registroEntradaDestino.id = :idRegistroEntrada " +
                "order by trazabilidad.fecha");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByOficioRemision(Long idOficioRemision) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.oficioRemision.id = :idOficioRemision " +
                "order by trazabilidad.fecha desc");

        q.setParameter("idOficioRemision", idOficioRemision);

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
    public Trazabilidad getByOficioRegistroSalida(Long idOficioRemision, Long idRegistroSalida) throws Exception{
        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.oficioRemision.id = :idOficioRemision and trazabilidad.registroSalida.id = :idRegistroSalida");

        q.setParameter("idOficioRemision",idOficioRemision);
        q.setParameter("idRegistroSalida",idRegistroSalida);

        return (Trazabilidad) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByRegistroSir(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroSir.id = :registroSir order by trazabilidad.fecha");

        q.setParameter("registroSir", idRegistroSir);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> obtenerRegistrosSalida(Long idOficioRemision) throws Exception{

        Query q = em.createQuery("Select t.registroSalida.id, t.registroSalida.estado from Trazabilidad as t " +
                "where t.oficioRemision.id = :idOficioRemision");

        q.setParameter("idOficioRemision",idOficioRemision);

        List<RegistroSalida> registros =  new ArrayList<RegistroSalida>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            RegistroSalida registroSalida = new RegistroSalida();
            registroSalida.setId((Long) object[0]);
            registroSalida.setEstado((Long) object[1]);

            registros.add(registroSalida);
        }

        return registros;
    }

    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        List<?> trazabilidades =  em.createQuery("Select id from Trazabilidad where oficioRemision.usuarioResponsable.entidad.id=:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        List<?> trazabilidadesSir =  em.createQuery("Select id from Trazabilidad where registroSir.entidad.id=:idEntidad").setParameter("idEntidad",idEntidad).getResultList();
        List<?> trazabilidadesRectificacionEntrada =  em.createQuery("Select id from Trazabilidad where registroEntradaOrigen.libro.organismo.entidad.id=:idEntidad and tipo = :rectificacion").setParameter("idEntidad",idEntidad).setParameter("rectificacion",RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA).getResultList();
        List<?> trazabilidadesRectificacionSalida =  em.createQuery("Select id from Trazabilidad where registroSalida.libro.organismo.entidad.id=:idEntidad and tipo = :rectificacion").setParameter("idEntidad",idEntidad).setParameter("rectificacion",RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA).getResultList();
        Integer total = trazabilidades.size() + trazabilidadesSir.size();

        eliminarTrazabilidades(trazabilidades);
        eliminarTrazabilidades(trazabilidadesSir);
        eliminarTrazabilidades(trazabilidadesRectificacionEntrada);
        eliminarTrazabilidades(trazabilidadesRectificacionSalida);

        return total;

    }

    private void eliminarTrazabilidades(List<?> trazabilidades){

        if (trazabilidades.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (trazabilidades.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = trazabilidades.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Trazabilidad where id in (:id)").setParameter("id", subList).executeUpdate();
                trazabilidades.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from Trazabilidad where id in (:id)").setParameter("id", trazabilidades).executeUpdate();
        }

    }
}
