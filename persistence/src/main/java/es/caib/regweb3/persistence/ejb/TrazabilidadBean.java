package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public RegistroEntrada getRegistroAceptado(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("Select tra.registroEntradaDestino from Trazabilidad as tra " +
                "where tra.registroSir.id = :registroSir and tra.registroSir.estado = :aceptado");

        q.setParameter("registroSir", idRegistroSir);
        q.setParameter("aceptado", EstadoRegistroSir.ACEPTADO);

        return (RegistroEntrada) q.getSingleResult();
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
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getPendientesDistribuirSir(Long idOficina, Long idEntidad, Set<Long> organismos, Integer total) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = " and t.registroEntradaDestino.destino.id in (:organismos) ";
        }

        Query q = em.createQuery("Select t.registroEntradaDestino from Trazabilidad as t " +
                "where t.tipo = :recibido_sir and t.registroSir.entidad.id = :idEntidad and " +
                "t.registroEntradaDestino.destino != null and " +
                "t.registroEntradaDestino.oficina.id = :idOficina and " +
                "t.registroEntradaDestino.estado = :registro_valido " + organismosWhere +
                " order by t.fecha");

        q.setParameter("recibido_sir", RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idOficina", idOficina);
        q.setParameter("registro_valido", RegwebConstantes.REGISTRO_VALIDO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        if(total != null){
            q.setMaxResults(total);
        }

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer pageNumber) throws Exception {

        Query q;
        Query q2;

        q = em.createQuery("Select t.registroEntradaDestino from Trazabilidad as t " +
                "where t.tipo = :recibido_sir and t.registroSir.entidad.id = :idEntidad and " +
                "t.registroEntradaDestino.destino != null and " +
                "t.registroEntradaDestino.oficina.id = :idOficina and " +
                "t.registroEntradaDestino.estado = :registro_valido " +
                " order by t.fecha desc");

        q.setParameter("recibido_sir", RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idOficina", idOficina);
        q.setParameter("registro_valido", RegwebConstantes.REGISTRO_VALIDO);

        q2 = em.createQuery("Select count(t.registroEntradaDestino.id) from Trazabilidad as t " +
                "where t.tipo = :recibido_sir and t.registroSir.entidad.id = :idEntidad and " +
                "t.registroEntradaDestino.destino != null and " +
                "t.registroEntradaDestino.oficina.id = :idOficina and " +
                "t.registroEntradaDestino.estado = :registro_valido " +
                " order by t.fecha desc");

        q2.setParameter("recibido_sir", RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        q2.setParameter("idEntidad", idEntidad);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("registro_valido", RegwebConstantes.REGISTRO_VALIDO);


        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
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
