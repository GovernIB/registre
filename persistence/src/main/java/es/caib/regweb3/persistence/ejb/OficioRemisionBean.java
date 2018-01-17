package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.DataBaseUtils;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
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

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB public LibroLocal libroEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private ContadorLocal contadorEjb;


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
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber,Date fechaInicio, Date fechaFin, String usuario, OficioRemision oficioRemision, List<Libro> libros, Long destinoOficioRemision, Integer estadoOficioRemision, Long tipoOficioRemision, Boolean sir) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select oficioRemision from OficioRemision as oficioRemision ");

        // Oficios Remisión no SIR
        where.add(" oficioRemision.sir = :sir "); parametros.put("sir",sir);

        // Tipo Oficio: Entrada o Salida
        if(tipoOficioRemision != 0){
            where.add(" oficioRemision.tipoOficioRemision = :tipoOficioRemision "); parametros.put("tipoOficioRemision",tipoOficioRemision);
        }

        if(oficioRemision.getNumeroOficio()!= null && oficioRemision.getNumeroOficio() > 0){where.add(" oficioRemision.numeroOficio = :numeroOficio"); parametros.put("numeroOficio",oficioRemision.getNumeroOficio());}

        // Usuario
        if (StringUtils.isNotEmpty(usuario)) {
            where.add(DataBaseUtils.like("oficioRemision.usuarioResponsable.usuario.identificador", "usuario", parametros, usuario));
        }

        // Comprobamos si la búsqueda es sobre un libro en concreto o sobre todos a los que tiene acceso el usuario.
        if(oficioRemision.getLibro().getId() != null && oficioRemision.getLibro().getId() > 0){
            where.add(" oficioRemision.libro.id = :idLibro"); parametros.put("idLibro",oficioRemision.getLibro().getId());
        }else{
            where.add(" oficioRemision.libro in (:libros)"); parametros.put("libros",libros);
        }

        // Oficio Remisión Interno o Externo
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

        // Identificador Intercambio
        if(StringUtils.isNotEmpty(oficioRemision.getIdentificadorIntercambio())){
            where.add(DataBaseUtils.like("oficioRemision.identificadorIntercambio", "identificadorIntercambio", parametros, oficioRemision.getIdentificadorIntercambio()));
        }

        // Intervalo fechas
        where.add(" (oficioRemision.fecha >= :fechaInicio  "); parametros.put("fechaInicio", fechaInicio);
        where.add(" oficioRemision.fecha <= :fechaFin) "); parametros.put("fechaFin", fechaFin);

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


        Paginacion paginacion;

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
        /*if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){
            for(OficioRemision oficio:oficios){
                Hibernate.initialize(oficio.getRegistrosEntrada());
            }
        }else if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
            for(OficioRemision oficio:oficios){
                Hibernate.initialize(oficio.getRegistrosSalida());
            }
        }*/

        paginacion.setListado(oficios);

        return paginacion;
    }


    @Override    
    public synchronized OficioRemision registrarOficioRemision(OficioRemision oficioRemision,
        Long estado) throws Exception, I18NException, I18NValidationException {

        try{

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

                    registroEntrada = registroEntradaEjb.findById(registroEntrada.getId());

                    RegistroSalida registroSalida = new RegistroSalida();

                    // Creamos un nuevo Registro Detalle para la SALIDA
                    registroSalida.setRegistroDetalle(new RegistroDetalle(registroEntrada.getRegistroDetalle()));
                    registroSalida.getRegistroDetalle().setId(null);
                    registroSalida.getRegistroDetalle().setInteresados(null);

                    Interesado destinatario =  new Interesado();
                    destinatario.setTipo(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
                    destinatario.setTipoDocumentoIdentificacion(RegwebConstantes.TIPODOCUMENTOID_CODIGO_ORIGEN_ID);

                    if(registroEntrada.getDestino() != null){
                        destinatario.setCodigoDir3(registroEntrada.getDestino().getCodigo());
                        destinatario.setDocumento(registroEntrada.getDestino().getCodigo());
                        destinatario.setRazonSocial(registroEntrada.getDestino().getDenominacion());
                    }else{
                        destinatario.setCodigoDir3(registroEntrada.getDestinoExternoCodigo());
                        destinatario.setDocumento(registroEntrada.getDestinoExternoCodigo());
                        destinatario.setRazonSocial(registroEntrada.getDestinoExternoDenominacion());
                    }

                    List<Interesado> destinatarios = new ArrayList<Interesado>();
                    destinatarios.add(destinatario);

                    registroSalida.setUsuario(oficioRemision.getUsuarioResponsable());
                    registroSalida.setOficina(oficioRemision.getOficina());
                    registroSalida.setOrigen(libro.getOrganismo());
                    registroSalida.setLibro(oficioRemision.getLibro());
                    registroSalida.setEstado(RegwebConstantes.REGISTRO_TRAMITADO);

                    // Registramos la Salida
                    registroSalida = registroSalidaEjb.registrarSalida(registroSalida, oficioRemision.getUsuarioResponsable(), destinatarios, null);

                    // CREAMOS LA TRAZABILIDAD
                    Trazabilidad trazabilidad = new Trazabilidad();
                    trazabilidad.setOficioRemision(oficioRemision);
                    trazabilidad.setFecha(new Date());
                    if(oficioRemision.getSir()){
                        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_OFICIO_SIR);
                    }else{
                        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_OFICIO);
                    }
                    trazabilidad.setRegistroEntradaOrigen(registroEntrada);
                    trazabilidad.setRegistroSalida(registroSalida);
                    trazabilidad.setRegistroEntradaDestino(null);
                    trazabilidadEjb.persist(trazabilidad);

                    // Modificamos el estado del Registro de Entrada
                    registroEntradaEjb.cambiarEstadoTrazabilidad(registroEntrada,estado, oficioRemision.getUsuarioResponsable());
                }
            }

            // Oficio de Remisión Salida
            if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){

                // CREAMOS LA TRAZABILIDAD
                for (RegistroSalida registroSalida : oficioRemision.getRegistrosSalida()) {

                    registroSalida = registroSalidaEjb.findById(registroSalida.getId());

                    // CREAMOS LA TRAZABILIDAD
                    Trazabilidad trazabilidad = new Trazabilidad();
                    trazabilidad.setOficioRemision(oficioRemision);
                    trazabilidad.setFecha(new Date());
                    if(oficioRemision.getSir()){
                        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_OFICIO_SIR);
                    }else{
                        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_OFICIO);
                    }
                    trazabilidad.setRegistroEntradaOrigen(null);
                    trazabilidad.setRegistroSalida(registroSalida);
                    trazabilidad.setRegistroEntradaOrigen(null);
                    trazabilidad.setRegistroEntradaDestino(null);
                    trazabilidadEjb.persist(trazabilidad);

                    // Modificamos el estado del Registro de Salida
                    registroSalidaEjb.cambiarEstadoTrazabilidad(registroSalida,estado, oficioRemision.getUsuarioResponsable());
                }

            }

        } catch (I18NException e) {
            log.info("Error creando Oficio Remision: " + e.getLocalizedMessage());
            ejbContext.setRollbackOnly();
            throw e;
        } catch (I18NValidationException ve) {
            log.info("Error creando Oficio Remision: " + ve.getLocalizedMessage());
            ejbContext.setRollbackOnly();
            throw ve;
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
                registroEntradaEjb.cambiarEstadoTrazabilidad(registroEntrada,RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
            }

            // Anulamos los Registros de Salida generado por el Oficio
            for(RegistroSalida registroSalida:trazabilidadEjb.obtenerRegistrosSalida(idOficioRemision)){
                registroSalidaEjb.cambiarEstadoTrazabilidad(registroSalida,RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad);
            }

        }else if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){

            List<RegistroSalida> registrosSalida = getSalidasByOficioRemision(oficioRemision.getId());
            // Modificamos el estado de cada RS a Válido
            for(RegistroSalida registroSalida:registrosSalida){
                registroSalidaEjb.cambiarEstadoTrazabilidad(registroSalida,RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
            }

        }

        // Anulamos el Oficio de Remisión
        oficioRemision.setEstado(RegwebConstantes.OFICIO_ANULADO);
        oficioRemision.setFechaEstado(new Date());
        merge(oficioRemision);

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws Exception {

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision "
                + "where oficioRemision.organismoDestinatario in (:organismos) "
                + " and oficioRemision.estado = " + RegwebConstantes.OFICIO_INTERNO_ENVIADO
                + " order by oficioRemision.id desc");

        q.setParameter("organismos",organismos);
        if(total != null){
            q.setMaxResults(total);
        }

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosBusqueda(Set<Organismo> organismos, Integer pageNumber,OficioRemision oficioRemision, Long tipoOficioRemision, int estado) throws Exception {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder query = new StringBuilder("Select oficioRemision from OficioRemision as oficioRemision ");

        // Tipo Oficio
        where.add(" oficioRemision.tipoOficioRemision = :tipoOficioRemision "); parametros.put("tipoOficioRemision",tipoOficioRemision);
        // Destinatario
        where.add(" oficioRemision.organismoDestinatario in (:organismos)"); parametros.put("organismos",organismos);
        // Estado
        where.add(" oficioRemision.estado = :estado");parametros.put("estado",estado);
        // Número
        if(oficioRemision.getNumeroOficio()!= null && oficioRemision.getNumeroOficio() > 0){where.add(" oficioRemision.numeroOficio = :numeroOficio"); parametros.put("numeroOficio",oficioRemision.getNumeroOficio());}

        // Parámetros
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


        Paginacion paginacion;

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
                + " and oficioRemision.estado = " + RegwebConstantes.OFICIO_INTERNO_ENVIADO);

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
    public List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws Exception{

        Query q = em.createQuery("Select oficioRemision.registrosSalida from OficioRemision as oficioRemision where oficioRemision.id = :idOficioRemision ");

        q.setParameter("idOficioRemision", idOficioRemision);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getByOficinaEstado(Long idOficina, int idEstado, int total) throws Exception{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.estado = :idEstado " +
                "and oficioRemision.oficina.id = :idOficina order by oficioRemision.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idEstado", idEstado);
        q.setParameter("idOficina", idOficina);

        List<OficioRemision> oficios = new ArrayList<OficioRemision>();

        // Inicializamos los Registros según su tipo de registro
        for(OficioRemision oficio: (List<OficioRemision>) q.getResultList()){
            oficio.getRegistrosEntrada().size(); //Inicializamos collection
            oficio.getRegistrosSalida().size();//Inicializamos collection
        }

        return oficios;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long getByOficinaEstadoCount(Long idOficina, int idEstado) throws Exception{

        Query q = em.createQuery("Select count(oficioRemision.id) from OficioRemision as oficioRemision where oficioRemision.estado = :idEstado " +
                "and oficioRemision.oficina.id = :idOficina");

        q.setParameter("idEstado", idEstado);
        q.setParameter("idOficina", idOficina);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Integer idEstado) throws Exception {

        Query q;
        Query q2;


        q = em.createQuery("Select oficio from OficioRemision as oficio where oficio.oficina.id = :idOficinaActiva " +
                "and oficio.estado = :idEstado order by oficio.fecha desc");

        q.setParameter("idOficinaActiva", idOficinaActiva);
        q.setParameter("idEstado", idEstado);

        q2 = em.createQuery("Select count(oficio.id) from OficioRemision as oficio where oficio.oficina.id = :idOficinaActiva " +
                "and oficio.estado = :idEstado");

        q2.setParameter("idOficinaActiva", idOficinaActiva);
        q2.setParameter("idEstado", idEstado);


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

        List<OficioRemision> oficios = q.getResultList();

        // Inicializamos los Registros según su tipo de registro
        for(OficioRemision oficio:oficios){
            oficio.getRegistrosEntrada().size();
            oficio.getRegistrosSalida().size();
        }

        paginacion.setListado(oficios);

        return paginacion;

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getEnviadosSinAck(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where (oficioRemision.estado = :enviado or oficioRemision.estado = :reenviado) " +
                "and oficioRemision.usuarioResponsable.entidad.id = :idEntidad and oficioRemision.numeroReintentos < :maxReintentos");

        q.setParameter("enviado", RegwebConstantes.OFICIO_SIR_ENVIADO);
        q.setParameter("reenviado", RegwebConstantes.OFICIO_SIR_REENVIADO);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getEnviadosConError(Long idEntidad) throws Exception {

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.usuarioResponsable.entidad.id = :idEntidad " +
                "and (oficioRemision.estado = :enviadoError or oficioRemision.estado = :reenviadoError) " +
                "and (oficioRemision.codigoError = '0039' or oficioRemision.codigoError = '0046' or oficioRemision.codigoError = '0057') " +
                "and oficioRemision.numeroReintentos < :maxReintentos");

        q.setParameter("enviadoError", RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR);
        q.setParameter("reenviadoError", RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.identificadorIntercambio = :identificadorIntercambio and" +
                " oficioRemision.oficina.codigo = :codigoEntidadRegistralDestino and oficioRemision.estado != :devuelto and oficioRemision.estado != :rechazado");

        q.setParameter("identificadorIntercambio", identificadorIntercambio);
        q.setParameter("codigoEntidadRegistralDestino", codigoEntidadRegistralDestino);
        q.setParameter("devuelto", RegwebConstantes.OFICIO_SIR_DEVUELTO);
        q.setParameter("rechazado", RegwebConstantes.OFICIO_SIR_RECHAZADO);

        List<OficioRemision> oficioRemision = q.getResultList();
        if(oficioRemision.size() == 1){
            return oficioRemision.get(0);
        }else{
            return null;
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

        Query q= em.createQuery("select registroEntrada.registroDetalle.id, registroEntrada.numeroRegistroFormateado from RegistroEntrada registroEntrada, OficioRemision ofiRem " +
                " where registroEntrada in elements(ofiRem.registrosEntrada) and ofiRem.id = :idOficioRemision");

        q.setParameter("idOficioRemision", idOficioRemision);

        List<Object[]> result = q.getResultList();

        List<String> numeros = new ArrayList<String>();
        Query q2;

        for (Object[] object : result) {

            q2 = em.createQuery("Select interesado from Interesado as interesado where interesado.registroDetalle.id = :registroDetalle " +
                    "order by interesado.id");

            q2.setParameter("registroDetalle",object[0]);

            List<Interesado> interesados = q2.getResultList();
            String nombreInteresado = "";
            if(interesados.size() > 0){
                nombreInteresado = interesados.get(0).getNombreCompletoInforme();
            }

            String numero = object[1] + " (" + nombreInteresado + ")";
            numeros.add(numero);
        }

        return numeros;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws Exception{

        Query q= em.createQuery("select registroSalida.registroDetalle.id, registroSalida.numeroRegistroFormateado from RegistroSalida registroSalida, OficioRemision ofiRem " +
                " where registroSalida in elements(ofiRem.registrosSalida) and ofiRem.id = :idOficioRemision");

        q.setParameter("idOficioRemision", idOficioRemision);

        List<Object[]> result = q.getResultList();

        List<String> numeros = new ArrayList<String>();
        Query q2;

        for (Object[] object : result) {

            q2 = em.createQuery("Select interesado from Interesado as interesado where interesado.registroDetalle.id = :registroDetalle " +
                    "order by interesado.id");

            q2.setParameter("registroDetalle",object[0]);

            List<Interesado> interesados = q2.getResultList();
            String nombreInteresado = "";
            if(interesados.size() > 0){
                nombreInteresado = interesados.get(0).getNombreCompletoInforme();
            }

            String numero = object[1] + " (" + nombreInteresado + ")";
            numeros.add(numero);
        }

        return numeros;
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
