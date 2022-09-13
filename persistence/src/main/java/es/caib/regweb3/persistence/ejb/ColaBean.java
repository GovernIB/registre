package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by mgonzalez on 21/03/2018.
 */
@Stateless(name = "ColaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ColaBean extends BaseEjbJPA<Cola, Long> implements ColaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroEntradaLocal registroEntradaEjb;


    @Override
    public Cola getReference(Long id) throws Exception {

        return em.getReference(Cola.class, id);
    }

    @Override
    public Cola findById(Long id) throws Exception {

        return em.find(Cola.class, id);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> findByTipoEntidad(Long tipo, Long idEntidad,Integer inicio, Integer total) throws Exception {

        Query q = em.createQuery("select cola from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad  and cola.numeroReintentos < :maxReintentos and cola.estado != :procesado order by cola.fecha asc ");
        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosCola(idEntidad));
        q.setParameter("procesado", RegwebConstantes.COLA_ESTADO_PROCESADO);

        if(inicio != null) {
            q.setFirstResult(inicio);
        }

        if (total != null) {
            q.setMaxResults(total);
        }

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long findPendientesByTipo(Long tipo, Long idEntidad) throws Exception {

        Query q = em.createQuery( "select count(cola.id) from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad and cola.estado =:pendiente");
        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("pendiente", RegwebConstantes.COLA_ESTADO_PENDIENTE);

        return (Long) q.getSingleResult();
    }

    @Override
    public Cola findByIdObjeto(Long idObjeto, Long idEntidad) throws Exception {

        Query q = em.createQuery("select cola from Cola as cola where cola.idObjeto=:idObjeto and cola.usuarioEntidad.entidad.id=:idEntidad");
        q.setParameter("idObjeto", idObjeto);
        q.setParameter("idEntidad", idEntidad);

        if (q.getResultList().size() > 0) {
            return (Cola) q.getResultList().get(0);
        } else {
            return null;
        }
    }

    @Override
    public Cola findByIdObjetoEstado(Long idObjeto, Long idEntidad, Long idEstado) throws Exception {

        Query q = em.createQuery("select cola from Cola as cola where cola.idObjeto=:idObjeto and cola.usuarioEntidad.entidad.id=:idEntidad and " +
                "cola.estado = :estado");
        q.setParameter("idObjeto", idObjeto);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("estado", idEstado);

        q.setHint("org.hibernate.readOnly", true);

        if (q.getResultList().size() > 0) {
            return (Cola) q.getResultList().get(0);
        } else {
            return null;
        }
    }


    @Override
    public List<Cola> findByTipoMaxReintentos(Long tipo, Long idEntidad, int maxReintentos) throws Exception {

        Query q = em.createQuery("select cola from Cola as cola where cola.tipo=:tipo and cola.usuarioEntidad.entidad.id=:idEntidad " +
                "and cola.numeroReintentos = :maxReintentos and cola.estado = :error order by cola.usuarioEntidad.entidad.id asc");
        q.setParameter("tipo", tipo);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", maxReintentos);
        q.setParameter("error", RegwebConstantes.COLA_ESTADO_ERROR);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> getAll() throws Exception {

        return em.createQuery("Select cola from Cola as cola order by cola.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(cola.id) from Cola as cola");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select cola from Cola as cola order by cola.id");
        q.setFirstResult(inicio);
        q.setMaxResults(Cola.RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    public Paginacion busqueda(Cola cola, Long idEntidad) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select cola from Cola as cola ");

        //MaxReintentos
        // where.add("cola.numeroMaximoReintentos = cola.numeroReintentos ");

        // Tipo integración
        if (cola.getTipo() != -1) {
            where.add("cola.tipo = :tipo ");
            parametros.put("tipo", cola.getTipo());
        }

        // Estado
        if (cola.getEstado() != null) {
            where.add("cola.estado = :estado ");
            parametros.put("estado", cola.getEstado());
        }

        // Descripcion objeto
        if (StringUtils.isNotEmpty(cola.getDescripcionObjeto())) {
            where.add(" (UPPER(cola.descripcionObjeto) LIKE UPPER(:descripcionObjeto)) ");
            parametros.put("descripcionObjeto", "%" + cola.getDescripcionObjeto() + "%");
        }

        // Entidad
        where.add("cola.usuarioEntidad.entidad.id = :idEntidad ");
        parametros.put("idEntidad", idEntidad);

        // Añadimos los parámetros a la query
        if (parametros.size() != 0) {
            query.append("where ");
            int count = 0;
            for (String w : where) {
                if (count != 0) {
                    query.append(" and ");
                }
                query.append(w);
                count++;
            }
            q2 = em.createQuery(query.toString().replaceAll("Select cola from Cola as cola ", "Select count(cola.id) from Cola as cola "));
            query.append("order by cola.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        } else {
            q2 = em.createQuery(query.toString().replaceAll("Select cola from Cola as cola ", "Select count(cola.id) from Cola as cola "));
            query.append("order by cola.id desc");
            q = em.createQuery(query.toString());
        }

        Paginacion paginacion;

        if (cola.getPageNumber() != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), cola.getPageNumber(), Cola.RESULTADOS_PAGINACION);
            int inicio = (cola.getPageNumber() - 1) * Cola.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(Cola.RESULTADOS_PAGINACION);
            q.setHint("org.hibernate.readOnly", true);

        } else {
            paginacion = new Paginacion(0, 0);
        }

        paginacion.setListado(q.getResultList());

        return paginacion;
    }

    @Override
    public void procesarElemento(Cola elemento) throws Exception {

        Query q = em.createQuery("update from Cola set estado = :procesado, fechaProcesado = :fechaProcesado where id = :idCola");

        q.setParameter("procesado", RegwebConstantes.COLA_ESTADO_PROCESADO);
        q.setParameter("fechaProcesado", new Date());
        q.setParameter("idCola", elemento.getId());
        q.executeUpdate();
    }

    @Override
    public void procesarElementoDistribucion(Cola elemento) throws Exception {

        procesarElemento(elemento);

        // Cambiar el estado al RegistroEntrada a DISTRIBIDO
        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", RegwebConstantes.REGISTRO_DISTRIBUIDO);
        q.setParameter("idRegistro", elemento.getIdObjeto());
        q.executeUpdate();
    }


    @Override
    public boolean enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception {

        try {

            //Creamos un elemento nuevo de la cola de distribución
            Cola cola = new Cola();
            cola.setIdObjeto(re.getId());
            cola.setTipoRegistro(RegwebConstantes.REGISTRO_ENTRADA);
            cola.setDescripcionObjeto(re.getNumeroRegistroFormateado());
            cola.setTipo(RegwebConstantes.COLA_DISTRIBUCION);
            cola.setUsuarioEntidad(usuarioEntidad);
            cola.setDenominacionOficina(re.getOficina().getDenominacion());
            cola.setEstado(RegwebConstantes.COLA_ESTADO_PENDIENTE);

            persist(cola);

            registroEntradaEjb.cambiarEstado(re.getId(), RegwebConstantes.REGISTRO_DISTRIBUYENDO);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean enviarAColaCustodia(IRegistro registro, Long tipoRegistro, UsuarioEntidad usuarioEntidad) {

        try {

            //Creamos un elemento nuevo de la cola de distribución
            Cola cola = new Cola();
            cola.setIdObjeto(registro.getId());
            cola.setTipoRegistro(tipoRegistro);
            cola.setDescripcionObjeto(registro.getNumeroRegistroFormateado());
            cola.setTipo(RegwebConstantes.COLA_CUSTODIA);
            cola.setUsuarioEntidad(usuarioEntidad);
            cola.setDenominacionOficina(registro.getOficina().getDenominacion());
            cola.setEstado(RegwebConstantes.COLA_ESTADO_PENDIENTE);

            persist(cola);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void reiniciarColabyEntidadTipo(Long idEntidad, Long tipo) throws Exception {

        //Obtenemos el numero máximo de reintentos de una propiedad global
        Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(idEntidad);

        //Obtenemos todos los que han alcanzado el máximo de reintentos.
        List<Cola> pendientesDistribuir = findByTipoMaxReintentos(tipo, idEntidad, maxReintentos);

        //Reseteamos los valores de los pendientes para que vuelvan a entrar en la cola.
        for (Cola pendiente : pendientesDistribuir) {
            reiniciarElementoCola(pendiente.getId());
        }
    }


    @Override
    public void actualizarElementoCola(Cola elemento, Long entidadId, String error) throws Exception {

        //Montamos el string de la causa del error
        if (StringUtils.isNotEmpty(error)) {
            error += "<br>";
        }

        try {
            //Obtenemos el numero máximo de reintentos de una propiedad global
            Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(entidadId);

            //Incrementar numeroreintentos si no hemos llegado al máximo
            if (elemento.getNumeroReintentos() < maxReintentos) {
                elemento.setNumeroReintentos(elemento.getNumeroReintentos() + 1);
                //Si hemos alcanzado el máximo de reintentos marcamos estado a error
                if (elemento.getNumeroReintentos() == maxReintentos) {
                    elemento.setEstado(RegwebConstantes.COLA_ESTADO_ERROR);
                }
            }
            //Guardamos que ha ocurrido un error en integraciones
            elemento.setError(elemento.getError() + "&nbsp;" + error);
            merge(elemento);


        } catch (Exception e) {
            elemento.setError(elemento.getError() + "&nbsp;" + error);
            merge(elemento);
            e.printStackTrace();
        }
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> colas = em.createQuery("Select distinct(id) from Cola as cola where cola.usuarioEntidad.entidad.id =:idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : colas) {
            remove(findById((Long) id));
        }

        return colas.size();
    }


    @Override
    public void reiniciarElementoCola(Long idCola) throws Exception {

        Query q = em.createQuery("update Cola set estado=:pendiente, numeroReintentos = :numeroReintentos, error = '' where id = :idCola");
        q.setParameter("pendiente", RegwebConstantes.COLA_ESTADO_PENDIENTE);
        q.setParameter("numeroReintentos", 0);
        q.setParameter("idCola", idCola);
        q.executeUpdate();
    }

    @Override
    public Integer purgarElementosProcesados(Long idEntidad) throws Exception {

        Integer mesesPurgo = PropiedadGlobalUtil.getMesesPurgoProcesadosCola(idEntidad);

        Date fechaPurgo = DateUtils.addMonths(new Date(), -mesesPurgo);

        List<?> elementos = em.createQuery("select distinct(cola.id) from Cola as cola where cola.estado = :procesado and cola.fecha <= :fechaPurgo and cola.usuarioEntidad.entidad.id =: idEntidad")
                .setParameter("procesado", RegwebConstantes.COLA_ESTADO_PROCESADO)
                .setParameter("idEntidad", idEntidad)
                .setParameter("fechaPurgo", fechaPurgo).getResultList();

        Integer total = elementos.size();

        if (elementos.size() > 0) {

            // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
            while (elementos.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {

                List<?> subList = elementos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);
                em.createQuery("delete from Cola where id in (:elementos)").setParameter("elementos", subList).executeUpdate();
                elementos.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
            }

            em.createQuery("delete from Cola where id in (:elementos)").setParameter("elementos", elementos).executeUpdate();
        }

        return total;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Cola> getElementosError(Long idEntidad) throws Exception {

        Query q = em.createQuery("select cola from Cola as cola where cola.usuarioEntidad.entidad.id=:idEntidad " +
                "and cola.estado = :error order by cola.id desc");

        q.setParameter("idEntidad", idEntidad);
        q.setParameter("error", RegwebConstantes.COLA_ESTADO_ERROR);
        q.setMaxResults(5);

        return q.getResultList();
    }

    /**
     * Obtiene los elementos procesados de la Cola de una antiguedad determinada
     *
     * @param idEntidad
     * @param meses
     * @return
     * @throws Exception
     */
    private List<Cola> obtenerProcesados(Long idEntidad, Integer meses) throws Exception {

        Date fechaPurgo = DateUtils.addMonths(new Date(), -meses);

        Query q = em.createQuery("select cola from Cola as cola where cola.estado=:procesado and cola.fecha<=:fechaPurgo and cola.usuarioEntidad.entidad.id =:idEntidad");

        q.setParameter("fechaPurgo", fechaPurgo);
        q.setParameter("procesado", RegwebConstantes.COLA_ESTADO_PROCESADO);
        q.setParameter("idEntidad", idEntidad);

        return q.getResultList();
    }

}
