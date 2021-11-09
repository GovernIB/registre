package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.Trazabilidad;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
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
    @SuppressWarnings(value = "unchecked")
    @TransactionTimeout(value = 1200)  // 20 minutos
    public List<Trazabilidad> oficiosSinREDestino(Long tipoOficio) throws Exception{
        Query q = em.createQuery("Select t from Trazabilidad as t " +
                "where t.tipo = :tipoTrazabilidad and t.oficioRemision.tipoOficioRemision = :tipoOficio and t.oficioRemision.estado = :estadoOficio " +
                "and t.registroEntradaDestino is null");

        q.setParameter("tipoTrazabilidad",RegwebConstantes.TRAZABILIDAD_OFICIO);
        q.setParameter("estadoOficio",RegwebConstantes.OFICIO_ACEPTADO);
        q.setParameter("tipoOficio", tipoOficio);
        q.setMaxResults(20);

        return q.getResultList();
    }

    @Override
    @TransactionTimeout(value = 1200)  // 20 minutos
    public void actualizarTrazabilidad(Long idTrazabilidad, Long idRegistro) throws Exception{

        Query q = em.createQuery("update from Trazabilidad set registroEntradaDestino.id = :idRegistro where id = :idTrazabilidad");

        q.setParameter("idTrazabilidad", idTrazabilidad);
        q.setParameter("idRegistro", idRegistro);
        q.executeUpdate();
    }


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
    public List<Trazabilidad> getByIdIntercambio(String idIntercambio, Long idEntidad) throws Exception {

        Query q = em.createQuery("Select DISTINCT t from Trazabilidad as t " +
                "where t.oficioRemision.identificadorIntercambio = :idIntercambio and t.oficioRemision.usuarioResponsable.entidad.id = :idEntidad order by t.fecha ");

        q.setParameter("idIntercambio", idIntercambio);
        q.setParameter("idEntidad", idEntidad);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByRegistroSalida(Long idRegistroSalida) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroSalida.id = :idRegistroSalida or trazabilidad.registroSalidaRectificado.id = :idRegistroSalida order by trazabilidad.fecha ");

        q.setParameter("idRegistroSalida", idRegistroSalida);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByRegistroEntrada(Long idRegistroEntrada) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroEntradaOrigen.id = :idRegistroEntrada or trazabilidad.registroEntradaDestino.id = :idRegistroEntrada " +
                "order by trazabilidad.fecha");

        q.setParameter("idRegistroEntrada", idRegistroEntrada);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByOficioRemision(Long idOficioRemision) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.oficioRemision.id = :idOficioRemision " +
                "order by trazabilidad.fecha desc");

        q.setParameter("idOficioRemision", idOficioRemision);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    public Trazabilidad getByOficioRegistroEntrada(Long idOficioRemision, Long idRegistroEntrada) throws Exception{

        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.oficioRemision.id = :idOficioRemision and trazabilidad.registroEntradaOrigen.id = :idRegistroEntrada");

        q.setParameter("idOficioRemision",idOficioRemision);
        q.setParameter("idRegistroEntrada",idRegistroEntrada);
        //q.setHint("org.hibernate.readOnly", true);

        return (Trazabilidad) q.getSingleResult();

    }

    @Override
    public Trazabilidad getByOficioRegistroSalida(Long idOficioRemision, Long idRegistroSalida) throws Exception{
        Query q = em.createQuery("Select trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.oficioRemision.id = :idOficioRemision and trazabilidad.registroSalida.id = :idRegistroSalida");

        q.setParameter("idOficioRemision",idOficioRemision);
        q.setParameter("idRegistroSalida",idRegistroSalida);
        //q.setHint("org.hibernate.readOnly", true);

        return (Trazabilidad) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Trazabilidad> getByRegistroSir(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("Select DISTINCT trazabilidad from Trazabilidad as trazabilidad " +
                "where trazabilidad.registroSir.id = :registroSir order by trazabilidad.fecha");

        q.setParameter("registroSir", idRegistroSir);
        //q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada getRegistroAceptado(Long idRegistroSir) throws Exception {

        Query q = em.createQuery("Select tra.registroEntradaDestino.id, tra.registroEntradaDestino.numeroRegistroFormateado, tra.registroEntradaDestino.fecha from Trazabilidad as tra " +
                "where tra.registroSir.id = :registroSir and tra.registroSir.estado = :aceptado");

        q.setParameter("registroSir", idRegistroSir);
        q.setParameter("aceptado", EstadoRegistroSir.ENVIADO_CONFIRMADO);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();

        if(result.size() == 1){
            Object[] object = result.get(0);

            RegistroEntrada registroEntrada = new RegistroEntrada();
            registroEntrada.setId((Long)  object[0]);
            registroEntrada.setNumeroRegistroFormateado((String) object[1]);
            registroEntrada.setFecha((Date) object[2]);

            return registroEntrada;
        }

        return null;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> obtenerRegistrosSalida(Long idOficioRemision) throws Exception{

        Query q = em.createQuery("Select t.registroSalida.id, t.registroSalida.estado from Trazabilidad as t " +
                "where t.oficioRemision.id = :idOficioRemision");

        q.setParameter("idOficioRemision",idOficioRemision);
        q.setHint("org.hibernate.readOnly", true);

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
    public List<RegistroEntrada> getPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer total) throws Exception {

        Query q = em.createQuery("Select t.registroEntradaDestino.id, t.registroEntradaDestino.numeroRegistroFormateado, " +
                "t.registroEntradaDestino.fecha, t.registroEntradaDestino.registroDetalle.extracto from Trazabilidad as t " +
                "where t.tipo = :recibido_sir and t.registroSir.entidad.id = :idEntidad and " +
                "t.registroEntradaDestino.evento = :distribuir and " +
                "t.registroEntradaDestino.oficina.id = :idOficina and " +
                "t.registroEntradaDestino.estado = :registro_valido order by t.fecha");

        q.setParameter("recibido_sir", RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("idOficina", idOficina);
        q.setParameter("registro_valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("distribuir", RegwebConstantes.EVENTO_DISTRIBUIR);
        q.setHint("org.hibernate.readOnly", true);

        if(total != null){
            q.setMaxResults(total);
        }

        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result) {
            RegistroEntrada registroEntrada = new RegistroEntrada();
            registroEntrada.setId((Long) object[0]);
            registroEntrada.setNumeroRegistroFormateado((String) object[1]);
            registroEntrada.setFecha((Date) object[2]);
            registroEntrada.getRegistroDetalle().setExtracto((String) object[3]);

            registros.add(registroEntrada);

        }

        return registros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion buscarPendientesDistribuirSir(Long idOficina, Long idEntidad, Integer pageNumber) throws Exception {

        Query q;
        Query q2;

        String oficinaWhere = "";
        if (idOficina != null) {
            oficinaWhere = " t.registroEntradaDestino.oficina.id = :idOficina and ";
        }

        q = em.createQuery("Select t.registroEntradaDestino from Trazabilidad as t " +
                "where t.tipo = :recibido_sir and t.registroSir.entidad.id = :idEntidad and " +
                "t.registroEntradaDestino.destino != null and " + oficinaWhere +
                "t.registroEntradaDestino.estado = :registro_valido and t.registroEntradaDestino.evento = :distribuir " +
                " order by t.fecha desc");

        q.setParameter("recibido_sir", RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("registro_valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("distribuir", RegwebConstantes.EVENTO_DISTRIBUIR);
        if (idOficina != null) q.setParameter("idOficina", idOficina);

        q2 = em.createQuery("Select count(t.registroEntradaDestino.id) from Trazabilidad as t " +
                "where t.tipo = :recibido_sir and t.registroSir.entidad.id = :idEntidad and " +
                "t.registroEntradaDestino.destino != null and " + oficinaWhere +
                "t.registroEntradaDestino.estado = :registro_valido and t.registroEntradaDestino.evento = :distribuir ");

        q2.setParameter("recibido_sir", RegwebConstantes.TRAZABILIDAD_RECIBIDO_SIR);
        q2.setParameter("idEntidad", idEntidad);
        q2.setParameter("registro_valido", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("distribuir", RegwebConstantes.EVENTO_DISTRIBUIR);
        if (idOficina != null) q2.setParameter("idOficina", idOficina);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
            q.setHint("org.hibernate.readOnly", true);
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
        List<?> trazabilidadesRectificacionEntrada =  em.createQuery("Select id from Trazabilidad where registroEntradaOrigen.usuario.entidad.id=:idEntidad and tipo = :rectificacion").setParameter("idEntidad",idEntidad).setParameter("rectificacion",RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA).getResultList();
        List<?> trazabilidadesRectificacionSalida =  em.createQuery("Select id from Trazabilidad where registroSalida.usuario.entidad.id=:idEntidad and tipo = :rectificacion").setParameter("idEntidad",idEntidad).setParameter("rectificacion",RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA).getResultList();
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

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer actualizarEstadoSirEntrada(Long idEntidad) throws Exception{

        Query q =  em.createQuery("Select registroEntradaOrigen.id from Trazabilidad where " +
                "oficioRemision.sir = true " +
                "and oficioRemision.tipoOficioRemision = :entrada " +
                "and registroEntradaOrigen.estado = :oficioExterno " +
                "and oficioRemision.usuarioResponsable.entidad.id = :idEntidad");

        q.setParameter("entrada",RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
        q.setParameter("idEntidad",idEntidad);
        q.setParameter("oficioExterno",RegwebConstantes.REGISTRO_OFICIO_EXTERNO);

        List<Long> registrosEntrada = q.getResultList();

        Integer total = registrosEntrada.size();
        log.info("Total registros entrada: " + total);

        if(total > 0){
            while (registrosEntrada.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = registrosEntrada.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Update entrada: " + em.createQuery("update from RegistroEntrada set estado = 13 where id in (:id)").setParameter("id", subList).executeUpdate());
                log.info("Update historico entrada: " + em.createQuery("update from HistoricoRegistroEntrada set estado = 13 where estado=4 and registroEntrada.id in (:id)").setParameter("id", subList).executeUpdate());
                registrosEntrada.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }
            log.info("Update entrada: " + em.createQuery("update from RegistroEntrada set estado = 13 where id in (:id)").setParameter("id", registrosEntrada).executeUpdate());
            log.info("Update historico entrada: " + em.createQuery("update from HistoricoRegistroEntrada set estado = 13 where estado=4 and registroEntrada.id in (:id)").setParameter("id", registrosEntrada).executeUpdate());
        }


        return total;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer actualizarEstadoSirSalida(Long idEntidad) throws Exception{

        Query q1 =  em.createQuery("Select registroSalida.id from Trazabilidad where " +
                "oficioRemision.sir = true " +
                "and oficioRemision.tipoOficioRemision = :salida " +
                "and registroSalida.estado = :oficioExterno " +
                "and oficioRemision.usuarioResponsable.entidad.id = :idEntidad");

        q1.setParameter("salida",RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        q1.setParameter("idEntidad",idEntidad);
        q1.setParameter("oficioExterno",RegwebConstantes.REGISTRO_OFICIO_EXTERNO);

        List<Long> registrosSalida = q1.getResultList();

        Integer total = registrosSalida.size();
        log.info("Total registros salida: " + total);

        if(total > 0){
            while (registrosSalida.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = registrosSalida.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                log.info("Update salida: " + em.createQuery("update from RegistroSalida set estado = 13 where id in (:id)").setParameter("id", subList).executeUpdate());
                log.info("Update historico salida: " + em.createQuery("update from HistoricoRegistroSalida set estado = 13 where estado=4 and registroSalida.id in (:id)").setParameter("id", subList).executeUpdate());
                registrosSalida.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }
            log.info("Update salida: " + em.createQuery("update from RegistroSalida set estado = 13 where id in (:id)").setParameter("id", registrosSalida).executeUpdate());
            log.info("Update historico salida: " + em.createQuery("update from HistoricoRegistroSalida set estado = 13 where estado=4 and registroSalida.id in (:id)").setParameter("id", registrosSalida).executeUpdate());
        }

        return total;
    }
}
