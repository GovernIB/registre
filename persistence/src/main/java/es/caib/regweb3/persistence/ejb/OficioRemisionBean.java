package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "OficioRemisionEJB")
@SecurityDomain("seycon")
public class OficioRemisionBean extends BaseEjbJPA<OficioRemision, Long> implements OficioRemisionLocal{

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName="regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    private RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    private RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/ContadorEJB/local")
    private ContadorLocal contadorEjb;


    @Override
    public OficioRemision getReference(Long id) throws Exception {

        return em.getReference(OficioRemision.class, id);
    }

    @Override
    public OficioRemision findById(Long id) throws Exception {

        OficioRemision oficioRemision = em.find(OficioRemision.class, id);
        Hibernate.initialize(oficioRemision.getRegistrosEntrada());
        Hibernate.initialize(oficioRemision.getRegistrosSalida());

        return oficioRemision;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getAll() throws Exception {

        return  em.createQuery("Select oficioRemision from OficioRemision as oficioRemision order by oficioRemision.id").getResultList();
    }

    @Override
    public Long getTotal() throws Exception {

        Query q = em.createQuery("Select count(oficioRemision.id) from OficioRemision as oficioRemision");

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getPagination(int inicio) throws Exception {

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision order by oficioRemision.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);

        return q.getResultList();
    }

    @Override
    public Paginacion busqueda(Integer pageNumber, Integer any, OficioRemision oficioRemision, List<Libro> libros, Long destinoOficioRemision, Integer estadoOficioRemision, Long tipoOficioRemision) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select oficioRemision from OficioRemision as oficioRemision ");

        // Tipo Oficio
        where.add(" oficioRemision.tipoOficioRemision = :tipoOficioRemision "); parametros.put("tipoOficioRemision",tipoOficioRemision);

        if(oficioRemision.getNumeroOficio()!= null && oficioRemision.getNumeroOficio() > 0){where.add(" oficioRemision.numeroOficio = :numeroOficio"); parametros.put("numeroOficio",oficioRemision.getNumeroOficio());}

        if(any!= null){where.add(" year(oficioRemision.fecha) = :any "); parametros.put("any",any);}

        // Comprobamos si la búsqueda es sobre un libro en concreto o sobre todos a los que tiene acceso el usuario.
        if(oficioRemision.getLibro().getId() != null && oficioRemision.getLibro().getId() > 0){
            where.add(" oficioRemision.libro.id = :idLibro"); parametros.put("idLibro",oficioRemision.getLibro().getId());
        }else{
            where.add(" oficioRemision.libro in (:libros)"); parametros.put("libros",libros);
        }

        // Tipo Oficio Remisión Interno o Externo
        if (destinoOficioRemision != null) {
            if (destinoOficioRemision.equals(RegwebConstantes.DESTINO_OFICIO_REMISION_INTERNO)) {
                where.add(" oficioRemision.organismoDestinatario != null");
            } else if (destinoOficioRemision.equals(RegwebConstantes.DESTINO_OFICIO_REMISION_EXTERNO)) {
                where.add(" oficioRemision.organismoDestinatario is null");
            }
        }

        // Estado Oficio Remisión
        if(estadoOficioRemision != null){
            where.add(" oficioRemision.estado = :estadoOficioRemision");
            parametros.put("estadoOficioRemision",estadoOficioRemision);
        }

        // Parametros
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
            q2 = em.createQuery(query.toString().replaceAll("Select oficioRemision from OficioRemision as oficioRemision ", "Select count(oficioRemision.id) from OficioRemision as oficioRemision "));
            query.append(" order by oficioRemision.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select oficioRemision from OficioRemision as oficioRemision ", "Select count(oficioRemision.id) from OficioRemision as oficioRemision "));
            query.append("order by oficioRemision.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion = null;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }

        List<OficioRemision> oficios = q.getResultList();

        // Inicializamos los Registros según su tipo de registro
        if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){
            for(OficioRemision oficio:oficios){
                Hibernate.initialize(oficio.getRegistrosEntrada());
            }
        }else if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
            for(OficioRemision oficio:oficios){
                Hibernate.initialize(oficio.getRegistrosSalida());
            }
        }

        paginacion.setListado(oficios);

        return paginacion;
    }


    @Override    
    public synchronized OficioRemision registrarOficioRemision(OficioRemision oficioRemision,
        Long estado) throws Exception, I18NException, I18NValidationException {

        // Obtenemos el Número de registro del OficioRemision
        Libro libro = libroEjb.findById(oficioRemision.getLibro().getId());
        NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorOficioRemision().getId());
        oficioRemision.setNumeroOficio(numeroRegistro.getNumero());
        oficioRemision.setFecha(numeroRegistro.getFecha());

        // Guardamos el Oficio de Remisión
        oficioRemision = persist(oficioRemision);

        // Oficio de Remisión Entrada
        if(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())){

            // Creamos un Registro de Salida y Trazabilidad por cada Registro de Entrada que contenga el OficioRemision
            for (RegistroEntrada registroEntrada : oficioRemision.getRegistrosEntrada()) {
                RegistroSalida registroSalida = new RegistroSalida();

                registroSalida.setRegistroDetalle(registroDetalleEjb.findByRegistroEntrada(registroEntrada.getId()));
                registroSalida.setUsuario(oficioRemision.getUsuarioResponsable());
                registroSalida.setOficina(oficioRemision.getOficina());
                registroSalida.setOrigen(libro.getOrganismo());//todo: Esta asignación es correcta?
                registroSalida.setLibro(oficioRemision.getLibro());

                registroSalida.setEstado(RegwebConstantes.REGISTRO_TRAMITADO);

                // Registramos la Salida
                registroSalida = registroSalidaEjb.registrarSalida(registroSalida, oficioRemision.getUsuarioResponsable(), null, null);

                // CREAMOS LA TRAZABILIDAD
                Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_OFICIO);
                trazabilidad.setRegistroEntradaOrigen(registroEntrada);
                trazabilidad.setOficioRemision(oficioRemision);
                trazabilidad.setRegistroSalida(registroSalida);
                trazabilidad.setFecha(new Date());

                trazabilidadEjb.persist(trazabilidad);

                // Modificamos el estado del Registro de Entrada
                registroEntradaEjb.cambiarEstado(registroEntrada,estado, oficioRemision.getUsuarioResponsable());
            }
        }

        // Oficio de Remisión Salida
        if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){

            // CREAMOS LA TRAZABILIDAD
            for (RegistroSalida registroSalida : oficioRemision.getRegistrosSalida()) {

                Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_OFICIO);
                trazabilidad.setRegistroEntradaOrigen(null);
                trazabilidad.setOficioRemision(oficioRemision);
                trazabilidad.setRegistroSalida(registroSalida);
                trazabilidad.setFecha(new Date());

                trazabilidadEjb.persist(trazabilidad);

                // Modificamos el estado del Registro de Salida
                registroSalidaEjb.cambiarEstado(registroSalida,estado, oficioRemision.getUsuarioResponsable());
            }

        }

        return oficioRemision;
    }

    @Override
    public void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws Exception{

        OficioRemision oficioRemision = findById(idOficioRemision);

        if(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())){
            List<RegistroEntrada> registrosEntrada = getEntradasByOficioRemision(oficioRemision.getId());

            // Modificamos el estado de cada RE a Válido
            for(RegistroEntrada registroEntrada:registrosEntrada){
                registroEntradaEjb.cambiarEstado(registroEntrada,RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
            }

            // Anulamos los Registros de Salida generado por el Oficio
            for(RegistroSalida registroSalida:trazabilidadEjb.obtenerRegistrosSalida(idOficioRemision)){
                registroSalidaEjb.cambiarEstado(registroSalida,RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad);
            }

        }else if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){

            List<RegistroSalida> registrosSalida = getSalidasByOficioRemision(oficioRemision.getId());
            // Modificamos el estado de cada RS a Válido
            for(RegistroSalida registroSalida:registrosSalida){
                registroSalidaEjb.cambiarEstado(registroSalida,RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
            }

        }

        // Anulamos el Oficio de Remisión
        oficioRemision.setEstado(RegwebConstantes.OFICIO_ANULADO);
        oficioRemision.setFechaEstado(new Date());
        merge(oficioRemision);

    }

    @Override
    public synchronized OficioRemision registrarOficioRemisionSIR(OficioRemision oficioRemision) throws Exception, I18NException, I18NValidationException {

        // Obtenemos el Número de registro del OficioRemision
        Libro libro = libroEjb.findById(oficioRemision.getLibro().getId());
        NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorOficioRemision().getId());
        oficioRemision.setNumeroOficio(numeroRegistro.getNumero());
        oficioRemision.setFecha(numeroRegistro.getFecha());

        // Guardamos el Oficio de Remisión
        oficioRemision = persist(oficioRemision);

        // Oficio de Remisión Entrada
        if(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())){

            // Creamos un Registro de Salida y Trazabilidad a partir del Registro de Entrada del OficioRemision
            RegistroEntrada registroEntrada = oficioRemision.getRegistrosEntrada().get(0);
            RegistroSalida registroSalida = new RegistroSalida();

            registroSalida.setRegistroDetalle(registroDetalleEjb.findByRegistroEntrada(registroEntrada.getId()));
            registroSalida.setUsuario(oficioRemision.getUsuarioResponsable());
            registroSalida.setOficina(oficioRemision.getOficina());
            registroSalida.setOrigen(libro.getOrganismo());//todo: Esta asignación es correcta?
            registroSalida.setLibro(oficioRemision.getLibro());

            registroSalida.setEstado(RegwebConstantes.REGISTRO_TRAMITADO);

            // Registramos la Salida
            registroSalida = registroSalidaEjb.registrarSalida(registroSalida, oficioRemision.getUsuarioResponsable(), null, null);

            // CREAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_OFICIO_SIR);
            trazabilidad.setRegistroEntradaOrigen(registroEntrada);
            trazabilidad.setOficioRemision(oficioRemision);
            trazabilidad.setRegistroSalida(registroSalida);
            trazabilidad.setFecha(new Date());
            trazabilidad.setRegistroEntradaDestino(null);

            trazabilidadEjb.persist(trazabilidad);

            // Modificamos el estado del Registro de Entrada
            registroEntradaEjb.cambiarEstado(registroEntrada, RegwebConstantes.REGISTRO_OFICIO_EXTERNO, oficioRemision.getUsuarioResponsable());

        }

        // Oficio de Remisión Salida
        if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){

            RegistroSalida registroSalida = oficioRemision.getRegistrosSalida().get(0);

            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_OFICIO_SIR);
            trazabilidad.setOficioRemision(oficioRemision);
            trazabilidad.setRegistroSalida(registroSalida);
            trazabilidad.setFecha(new Date());
            trazabilidad.setRegistroEntradaOrigen(null);
            trazabilidad.setRegistroEntradaDestino(null);

            trazabilidadEjb.persist(trazabilidad);

            // Modificamos el estado del Registro de Salida
            registroSalidaEjb.cambiarEstado(registroSalida,RegwebConstantes.REGISTRO_OFICIO_EXTERNO, oficioRemision.getUsuarioResponsable());

        }


        return oficioRemision;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws Exception {

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision "
                + "where oficioRemision.organismoDestinatario in (:organismos) "
                + " and oficioRemision.estado = " + RegwebConstantes.OFICIO_INTERNO
                + " order by oficioRemision.id desc");

        q.setParameter("organismos",organismos);
        if(total != null){
            q.setMaxResults(total);
        }

        return q.getResultList();
    }

    @Override
    public Paginacion oficiosPendientesLlegadaBusqueda(Set<Organismo> organismos, Integer pageNumber,OficioRemision oficioRemision, Long tipoOficioRemision) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuffer query = new StringBuffer("Select oficioRemision from OficioRemision as oficioRemision ");

        // Tipo Oficio
        where.add(" oficioRemision.tipoOficioRemision = :tipoOficioRemision "); parametros.put("tipoOficioRemision",tipoOficioRemision);

        where.add(" oficioRemision.organismoDestinatario in (:organismos)"); parametros.put("organismos",organismos);
        where.add(" oficioRemision.estado = :estado");parametros.put("estado",RegwebConstantes.OFICIO_INTERNO);

        if(oficioRemision.getNumeroOficio()!= null && oficioRemision.getNumeroOficio() > 0){where.add(" oficioRemision.numeroOficio = :numeroOficio"); parametros.put("numeroOficio",oficioRemision.getNumeroOficio());}

        /*// Comprobamos si la búsqueda es sobre un libro en concreto o sobre todos a los que tiene acceso el usuario.
        if(oficioRemision.getLibro().getId() != null && oficioRemision.getLibro().getId() > 0){
            where.add(" oficioRemision.libro.id = :idLibro"); parametros.put("idLibro",oficioRemision.getLibro().getId());
        }else{
            where.add(" oficioRemision.libro in (:libros)"); parametros.put("libros",libros);
        }*/


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
            q2 = em.createQuery(query.toString().replaceAll("Select oficioRemision from OficioRemision as oficioRemision ", "Select count(oficioRemision.id) from OficioRemision as oficioRemision "));
            query.append(" order by oficioRemision.id desc");
            q = em.createQuery(query.toString());

            for (Map.Entry<String, Object> param : parametros.entrySet()) {

                q.setParameter(param.getKey(), param.getValue());
                q2.setParameter(param.getKey(), param.getValue());
            }

        }else{
            q2 = em.createQuery(query.toString().replaceAll("Select oficioRemision from OficioRemision as oficioRemision ", "Select count(oficioRemision.id) from OficioRemision as oficioRemision "));
            query.append("order by oficioRemision.id desc");
            q = em.createQuery(query.toString());
        }


        Paginacion paginacion = null;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }


        List<OficioRemision> oficios = q.getResultList();

        // Inicializamos los Registros según su tipo de registro
        if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){
            for(OficioRemision oficio:oficios){
                Hibernate.initialize(oficio.getRegistrosEntrada());
            }
        }else if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
            for(OficioRemision oficio:oficios){
                Hibernate.initialize(oficio.getRegistrosSalida());
            }
        }

        paginacion.setListado(oficios);

        return paginacion;
    }

    @Override
    public Long oficiosPendientesLlegadaCount(Set<Organismo> organismos) throws Exception {

        Query q = em.createQuery("Select count(oficioRemision.id) from OficioRemision as oficioRemision "
                + "where oficioRemision.organismoDestinatario in (:organismos) "
                + " and oficioRemision.estado = " + RegwebConstantes.OFICIO_INTERNO);

        q.setParameter("organismos",organismos);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getEntradasByOficioRemision(Long idOficioRemision) throws Exception{

        Query q = em.createQuery("Select oficioRemision.registrosEntrada from OficioRemision as oficioRemision where oficioRemision.id = :idOficioRemision ");

        q.setParameter("idOficioRemision", idOficioRemision);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getByEstado(int idEstado, Long idOficina) throws Exception{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.estado = :idEstado " +
                "and oficioRemision.oficina.id = :idOficina");

        q.setParameter("idEstado", idEstado);
        q.setParameter("idOficina", idOficina);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws Exception{

        Query q = em.createQuery("Select oficioRemision.registrosSalida from OficioRemision as oficioRemision where oficioRemision.id = :idOficioRemision ");

        q.setParameter("idOficioRemision", idOficioRemision);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.identificadorIntercambio = :identificadorIntercambio ");

        q.setParameter("identificadorIntercambio", identificadorIntercambio);

        List<OficioRemision> oficioRemision = q.getResultList();
        if(oficioRemision.size() == 1){
            return oficioRemision.get(0);
        }else{
            return  null;
        }
    }

    @Override
    public void modificarEstado(Long idOficioRemision, int estado) throws Exception {

        Query q = em.createQuery("update OficioRemision set estado=:estado where id = :idOficioRemision");
        q.setParameter("estado", estado);
        q.setParameter("idOficioRemision", idOficioRemision);
        q.executeUpdate();

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> getNumerosRegistroEntradaFormateadoByOficioRemision(Long idOficioRemision) throws Exception{

        Query q= em.createQuery("select registroEntrada.numeroRegistroFormateado from RegistroEntrada registroEntrada, OficioRemision ofiRem " +
                " where registroEntrada in elements(ofiRem.registrosEntrada) and ofiRem.id = :idOficioRemision");

        q.setParameter("idOficioRemision", idOficioRemision);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws Exception{

        Query q= em.createQuery("select registroSalida.numeroRegistroFormateado from RegistroSalida registroSalida, OficioRemision ofiRem " +
                " where registroSalida in elements(ofiRem.registrosSalida) and ofiRem.id = :idOficioRemision");

        q.setParameter("idOficioRemision", idOficioRemision);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception{

        Query or = em.createQuery("select distinct(id) from OficioRemision where usuarioResponsable.entidad.id = :idEntidad");
        or.setParameter("idEntidad", idEntidad);
        List<Object> oficiosRemision =  or.getResultList();

        for (Object id : oficiosRemision) {
            remove(findById((Long) id));
        }

        return oficiosRemision.size();

    }
}
