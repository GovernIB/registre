package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.utils.CombineStream;
import es.caib.regweb3.utils.ConvertirTexto;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "OficioRemisionEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public class OficioRemisionBean extends BaseEjbJPA<OficioRemision, Long> implements OficioRemisionLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB private LibroLocal libroEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroSalidaConsultaLocal registroSalidaConsultaEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private InteresadoLocal interesadoEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;


    @Override
    public OficioRemision getReference(Long id) throws I18NException {

        return em.getReference(OficioRemision.class, id);
    }

    @Override
    public OficioRemision findById(Long id) throws I18NException {

        OficioRemision oficioRemision = em.find(OficioRemision.class, id);
        Hibernate.initialize(oficioRemision.getRegistrosEntrada());
        Hibernate.initialize(oficioRemision.getRegistrosSalida());

        return oficioRemision;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getAll() throws I18NException {

        return  em.createQuery("Select oficioRemision from OficioRemision as oficioRemision order by oficioRemision.id").getResultList();
    }

    @Override
    public Long getTotal() throws I18NException {

        Query q = em.createQuery("Select count(oficioRemision.id) from OficioRemision as oficioRemision");
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getPagination(int inicio) throws I18NException {

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision order by oficioRemision.id");
        q.setFirstResult(inicio);
        q.setMaxResults(RESULTADOS_PAGINACION);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion busqueda(Integer pageNumber,Long idOrganismo, Date fechaInicio, Date fechaFin, String usuario, OficioRemision oficioRemision, Long destinoOficioRemision, Integer estadoOficioRemision, Long tipoOficioRemision, Boolean sir, Long idEntidad) throws I18NException {

        Query q;
        Query q2;
        Map<String, Object> parametros = new HashMap<String, Object>();
        List<String> where = new ArrayList<String>();

        StringBuilder queryBase = new StringBuilder("Select o.id, o.numeroOficio, o.fecha, o.identificadorIntercambio, o.oficina, o.organismoDestinatario, o.destinoExternoCodigo, o.destinoExternoDenominacion, " +
                "o.estado, o.tipoOficioRemision, o.numeroReintentos, o.codigoError, o.descripcionError from OficioRemision as o LEFT JOIN o.organismoDestinatario destino ");

        StringBuilder query = new StringBuilder(queryBase);

        // Entidad
        where.add(" o.entidad.id =:idEntidad  ");
        parametros.put("idEntidad", idEntidad);

        // Organismo
        if(idOrganismo != null){
            where.add(" o.oficina.organismoResponsable.id = :idOrganismo ");
            parametros.put("idOrganismo", idOrganismo);
        }

        // Oficios Remisión no SIR
        where.add(" o.sir = :sir "); parametros.put("sir",sir);

        // Tipo Oficio: Entrada o Salida
        if(tipoOficioRemision != 0){
            where.add(" o.tipoOficioRemision = :tipoOficioRemision "); parametros.put("tipoOficioRemision",tipoOficioRemision);
        }

        if(oficioRemision.getNumeroOficio()!= null && oficioRemision.getNumeroOficio() > 0){where.add(" o.numeroOficio = :numeroOficio"); parametros.put("numeroOficio",oficioRemision.getNumeroOficio());}

        // Usuario
        if (StringUtils.isNotEmpty(usuario)) {
            where.add(DataBaseUtils.like("o.usuarioResponsable.usuario.identificador", "usuario", parametros, usuario));
        }

        // Oficio Remisión Interno o Externo
        if (destinoOficioRemision != null) {
            if (destinoOficioRemision.equals(RegwebConstantes.DESTINO_OFICIO_REMISION_INTERNO)) {
                where.add(" o.organismoDestinatario != null");
            } else if (destinoOficioRemision.equals(RegwebConstantes.DESTINO_OFICIO_REMISION_EXTERNO)) {
                where.add(" o.organismoDestinatario is null");
            }
        }

        // Estado Oficio Remisión
        if(estadoOficioRemision != null){
            where.add(" o.estado = :estadoOficioRemision");
            parametros.put("estadoOficioRemision",estadoOficioRemision);
        }

        // Identificador Intercambio
        if(StringUtils.isNotEmpty(oficioRemision.getIdentificadorIntercambio())){
            where.add(DataBaseUtils.like("o.identificadorIntercambio", "identificadorIntercambio", parametros, oficioRemision.getIdentificadorIntercambio()));
        }

        // Intervalo fechas
        where.add(" (o.fecha >= :fechaInicio  "); parametros.put("fechaInicio", fechaInicio);
        where.add(" o.fecha <= :fechaFin) "); parametros.put("fechaFin", fechaFin);


        // Añadimos los parámetros a la query
        query.append("where ");
        int count = 0;
        for (String w : where) {
            if (count != 0) {
                query.append(" and ");
            }
            query.append(w);
            count++;
        }

        // Duplicamos la query solo para obtener los resultados totales
        StringBuilder queryCount = new StringBuilder("Select count(o.id) from OficioRemision as o ");
        q2 = em.createQuery(query.toString().replaceAll(queryBase.toString(), queryCount.toString()));

        // añadimos el order by
        query.append(" order by o.id desc");
        q = em.createQuery(query.toString());

        // Mapeamos los parámetros
        for (Map.Entry<String, Object> param : parametros.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
            q2.setParameter(param.getKey(), param.getValue());
        }

        // Ejecutamos las queries
        Paginacion paginacion;

        if(pageNumber != null){ // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> results = q.getResultList();
        List<OficioRemision> oficios = new ArrayList<>();

        for (Object[] result : results) {
            OficioRemision oficio =  new OficioRemision();
            oficio.setId((Long) result[0]);
            oficio.setNumeroOficio((Integer) result[1]);
            oficio.setFecha((Date) result[2]);
            oficio.setIdentificadorIntercambio((String) result[3]);
            oficio.setOficina((Oficina) result[4]);
            oficio.setOrganismoDestinatario((Organismo) result[5]);
            oficio.setDestinoExternoCodigo((String) result[6]);
            oficio.setDestinoExternoDenominacion((String) result[7]);
            oficio.setEstado((int) result[8]);
            oficio.setTipoOficioRemision((Long) result[9]);
            oficio.setNumeroReintentos((Integer) result[10]);
            oficio.setCodigoError((String) result[11]);
            oficio.setDescripcionError((String) result[12]);


            oficios.add(oficio);
        }

        paginacion.setListado(oficios);

        return paginacion;
    }


    @Override
    public OficioRemision registrarOficioRemision(Entidad entidad, OficioRemision oficioRemision, Long estado) throws I18NException, I18NValidationException {

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

                    registroEntrada = registroEntradaEjb.findByIdCompleto(registroEntrada.getId());

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
                    registroSalida.setOrigen(registroEntrada.getOficina().getOrganismoResponsable());
                    registroSalida.setLibro(oficioRemision.getLibro());
                    registroSalida.setEstado(RegwebConstantes.REGISTRO_DISTRIBUIDO);

                    // Registramos la Salida
                    registroSalida = registroSalidaEjb.registrarSalida(registroSalida, entidad, oficioRemision.getUsuarioResponsable(), destinatarios, null, false);

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
                    registroEntradaEjb.cambiarEstadoHistorico(registroEntrada,estado, oficioRemision.getUsuarioResponsable());
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
                    trazabilidad.setRegistroEntradaDestino(null);
                    trazabilidadEjb.persist(trazabilidad);

                    // Modificamos el estado del Registro de Salida
                    registroSalidaEjb.cambiarEstadoHistorico(registroSalida,estado, oficioRemision.getUsuarioResponsable());
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
    public void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws I18NException{

        OficioRemision oficioRemision = findById(idOficioRemision);

        if(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())){
            List<RegistroEntrada> registrosEntrada = getEntradasByOficioRemision(oficioRemision.getId());

            // Modificamos el estado de cada RE a Válido
            for(RegistroEntrada registroEntrada:registrosEntrada){
                registroEntradaEjb.cambiarEstadoHistorico(registroEntrada,RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
            }

            // Anulamos los Registros de Salida generado por el Oficio
            for(RegistroSalida registroSalida:trazabilidadEjb.obtenerRegistrosSalida(idOficioRemision)){
                registroSalidaEjb.cambiarEstadoHistorico(registroSalida,RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad);
            }

        }else if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){

            List<RegistroSalida> registrosSalida = getSalidasByOficioRemision(oficioRemision.getId());
            // Modificamos el estado de cada RS a Válido
            for(RegistroSalida registroSalida:registrosSalida){
                registroSalidaEjb.cambiarEstadoHistorico(registroSalida,RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
            }

        }

        // Anulamos el Oficio de Remisión
        oficioRemision.setEstado(RegwebConstantes.OFICIO_ANULADO);
        oficioRemision.setFechaEstado(new Date());
        merge(oficioRemision);

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws I18NException {

        Query q = em.createQuery("Select o.id, o.fecha, o.oficina, o.organismoDestinatario, o.destinoExternoCodigo, o.destinoExternoDenominacion " +
                "from OficioRemision as o LEFT JOIN o.organismoDestinatario destino where o.organismoDestinatario in (:organismos) "
                + " and o.estado = " + RegwebConstantes.OFICIO_INTERNO_ENVIADO
                + " order by o.id desc");

        q.setParameter("organismos",organismos);
        q.setHint("org.hibernate.readOnly", true);

        if(total != null){
            q.setMaxResults(total);
        }

        List<Object[]> results = q.getResultList();
        List<OficioRemision> oficios = new ArrayList<>();

        for (Object[] result : results) {
            OficioRemision oficio =  new OficioRemision();
            oficio.setId((Long) result[0]);
            oficio.setFecha((Date) result[1]);
            oficio.setOficina((Oficina) result[2]);
            oficio.setOrganismoDestinatario((Organismo) result[3]);
            oficio.setDestinoExternoCodigo((String) result[4]);
            oficio.setDestinoExternoDenominacion((String) result[5]);

            oficios.add(oficio);
        }

        return oficios;
    }

    @Override
    public void actualizarDestinoPendientesLlegada(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws I18NException {

        Query q = em.createQuery("update OficioRemision set organismoDestinatario.id = :idOrganismoSustituto where organismoDestinatario.id = :idOrganismoExtinguido and estado = :pendienteLlegada");
        q.setParameter("idOrganismoSustituto", idOrganismoSustituto);
        q.setParameter("idOrganismoExtinguido", idOrganismoExtinguido);
        q.setParameter("pendienteLlegada", RegwebConstantes.OFICIO_INTERNO_ENVIADO);

        q.executeUpdate();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion oficiosBusqueda(Set<Organismo> organismos, Integer pageNumber,OficioRemision oficioRemision, Long tipoOficioRemision, int estado) throws I18NException {

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
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long)q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(RESULTADOS_PAGINACION);
        }else{
            paginacion = new Paginacion(0, 0);
        }


        List<OficioRemision> oficios = q.getResultList();

        /*// Inicializamos los Registros según su tipo de registro
        if(tipoOficioRemision.equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){
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
    public Long oficiosPendientesLlegadaCount(Set<Organismo> organismos) throws I18NException {

        Query q = em.createQuery("Select count(oficioRemision.id) from OficioRemision as oficioRemision "
                + "where oficioRemision.organismoDestinatario in (:organismos) "
                + " and oficioRemision.estado = " + RegwebConstantes.OFICIO_INTERNO_ENVIADO);

        q.setParameter("organismos",organismos);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroEntrada> getEntradasByOficioRemision(Long idOficioRemision) throws I18NException{

        Query q = em.createQuery("Select oficioRemision.registrosEntrada from OficioRemision as oficioRemision where oficioRemision.id = :idOficioRemision ");

        q.setParameter("idOficioRemision", idOficioRemision);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws I18NException{

        Query q = em.createQuery("Select oficioRemision.registrosSalida from OficioRemision as oficioRemision where oficioRemision.id = :idOficioRemision ");

        q.setParameter("idOficioRemision", idOficioRemision);
        q.setHint("org.hibernate.readOnly", true);

        return q.getResultList();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getByOficinaEstado(Long idOficina, int idEstado, int total) throws I18NException{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.estado = :idEstado " +
                "and oficioRemision.oficina.id = :idOficina order by oficioRemision.fecha desc");

        q.setMaxResults(total);
        q.setParameter("idEstado", idEstado);
        q.setParameter("idOficina", idOficina);
        q.setHint("org.hibernate.readOnly", true);

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
    public Long getByOficinaEstadoCount(Long idOficina, int idEstado) throws I18NException{

        Query q = em.createQuery("Select count(oficioRemision.id) from OficioRemision as oficioRemision where oficioRemision.estado = :idEstado " +
                "and oficioRemision.oficina.id = :idOficina");

        q.setParameter("idEstado", idEstado);
        q.setParameter("idOficina", idOficina);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Integer idEstado) throws I18NException {

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
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setHint("org.hibernate.readOnly", true);
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
    public List<Long> getEnviadosSinAck(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select oficioRemision.id from OficioRemision as oficioRemision where (oficioRemision.estado = :enviado or oficioRemision.estado = :reenviado) " +
                "and oficioRemision.entidad.id = :idEntidad and oficioRemision.sir=true and oficioRemision.numeroReintentos < :maxReintentos order by id");

        q.setParameter("enviado", RegwebConstantes.OFICIO_SIR_ENVIADO);
        q.setParameter("reenviado", RegwebConstantes.OFICIO_SIR_REENVIADO);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setMaxResults(25);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Long> getEnviadosConError(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select oficioRemision.id from OficioRemision as oficioRemision where oficioRemision.entidad.id = :idEntidad and oficioRemision.sir=true " +
                "and (oficioRemision.estado = :enviadoError or oficioRemision.estado = :reenviadoError) " +
                "and (oficioRemision.codigoError = '0039' or oficioRemision.codigoError = '0046' or oficioRemision.codigoError = '0057' or oficioRemision.codigoError = '0065' " +
                "or oficioRemision.codigoError = '0063' or oficioRemision.codigoError = '0058' or oficioRemision.codigoError = '0068' or oficioRemision.codigoError = '0043' " +
                "or oficioRemision.codigoError = '0037' ) and oficioRemision.numeroReintentos < :maxReintentos order by id");

        q.setParameter("enviadoError", RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR);
        q.setParameter("reenviadoError", RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setMaxResults(20);

        return q.getResultList();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getEnviadosSinAckMaxReintentos(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select oficioRemision.fecha, oficioRemision.identificadorIntercambio, oficioRemision.tipoOficioRemision from OficioRemision as oficioRemision where (oficioRemision.estado = :enviado or oficioRemision.estado = :reenviado) " +
                "and oficioRemision.entidad.id = :idEntidad and oficioRemision.sir=true and oficioRemision.numeroReintentos = :maxReintentos");

        q.setParameter("enviado", RegwebConstantes.OFICIO_SIR_ENVIADO);
        q.setParameter("reenviado", RegwebConstantes.OFICIO_SIR_REENVIADO);
        q.setParameter("idEntidad", idEntidad);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setMaxResults(5);

        List<OficioRemision> oficios =  new ArrayList<OficioRemision>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            OficioRemision oficio = new OficioRemision((Date)object[0],(String)object[1],(Long)object[2]);
            oficios.add(oficio);
        }

        return oficios;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficioRemision> getEnviadosErrorMaxReintentos(Long idEntidad) throws I18NException {

        Query q = em.createQuery("Select oficioRemision.fecha, oficioRemision.identificadorIntercambio, oficioRemision.tipoOficioRemision from OficioRemision as oficioRemision where (oficioRemision.estado = :enviadoError or oficioRemision.estado = :reenviadoError) " +
                "and oficioRemision.entidad.id = :idEntidad and oficioRemision.sir=true and oficioRemision.numeroReintentos = :maxReintentos");

        q.setParameter("enviadoError", RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR);
        q.setParameter("reenviadoError", RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR);
        q.setParameter("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(idEntidad));
        q.setParameter("idEntidad", idEntidad);
        q.setMaxResults(5);

        List<OficioRemision> oficios =  new ArrayList<OficioRemision>();
        List<Object[]> result = q.getResultList();

        for (Object[] object : result){
            OficioRemision oficio = new OficioRemision((Date)object[0],(String)object[1],(Long)object[2]);
            oficios.add(oficio);
        }

        return oficios;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws I18NException{

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
    @SuppressWarnings(value = "unchecked")
    public OficioRemision getBySirRechazado(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws I18NException{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.identificadorIntercambio = :identificadorIntercambio and" +
                " oficioRemision.oficina.codigo = :codigoEntidadRegistralDestino and oficioRemision.estado = :rechazado");

        q.setParameter("identificadorIntercambio", identificadorIntercambio);
        q.setParameter("codigoEntidadRegistralDestino", codigoEntidadRegistralDestino);
        q.setParameter("rechazado", RegwebConstantes.OFICIO_SIR_RECHAZADO);

        List<OficioRemision> oficioRemision = q.getResultList();
        if(oficioRemision.size() > 0){
            return oficioRemision.get(0);
        }else{
            return null;
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio) throws I18NException{

        Query q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where oficioRemision.identificadorIntercambio = :identificadorIntercambio ");

        q.setParameter("identificadorIntercambio", identificadorIntercambio);

        List<OficioRemision> oficioRemision = q.getResultList();
        if(oficioRemision.size() == 1){
            return oficioRemision.get(0);
        }else{
            return null;
        }
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void modificarEstado(Long idOficioRemision, int estado) throws I18NException {

        Query q = em.createQuery("update OficioRemision set estado=:estado, fechaEstado=:fechaEstado where id = :idOficioRemision");
        q.setParameter("estado", estado);
        q.setParameter("fechaEstado", new Date());
        q.setParameter("idOficioRemision", idOficioRemision);
        q.executeUpdate();
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void modificarEstadoError(Long idOficioRemision, int estado, String codigoError, String descripcionError) throws I18NException {

        Query q = em.createQuery("update OficioRemision set estado=:estado, codigoError=:codigoError, descripcionError=:descripcionError, fechaEstado=:fechaEstado where id = :idOficioRemision");
        q.setParameter("estado", estado);
        q.setParameter("codigoError", codigoError);
        q.setParameter("descripcionError", descripcionError);
        q.setParameter("fechaEstado", new Date());
        q.setParameter("idOficioRemision", idOficioRemision);
        q.executeUpdate();
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void incrementarReintentos(Long idOficioRemision, Integer reintentos) throws I18NException {

        Query q = em.createQuery("update OficioRemision set numeroReintentos=:reintentos where id = :idOficioRemision");
        q.setParameter("reintentos", reintentos);
        q.setParameter("idOficioRemision", idOficioRemision);
        q.executeUpdate();
    }

    @Override
    public void reiniciarIntentos(Long idOficioRemision) throws I18NException {

        Query q = em.createQuery("update OficioRemision set numeroReintentos=0 where id = :idOficioRemision");
        q.setParameter("idOficioRemision", idOficioRemision);
        q.executeUpdate();

    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public void aceptarOficioSir(OficioRemision oficio, String codigoEntidadRegistralOrigen, String decodificacionEntidadRegistralOrigen, String numeroRegistroDestino, Date fechaRegistroDestino) throws I18NException {

        // Actualizamos el Oficio con los datos de la confirmación
        Query q = em.createQuery("update OficioRemision set estado=:estado, codigoEntidadRegistralProcesado=:codigoEntidadRegistralOrigen, " +
                "decodificacionEntidadRegistralProcesado=:decodificacionEntidadRegistralOrigen, numeroRegistroEntradaDestino=:numeroRegistroDestino ,fechaEntradaDestino=:fechaRegistroDestino, fechaEstado=:fechaEstado where id = :idOficioRemision");

        q.setParameter("estado", RegwebConstantes.OFICIO_ACEPTADO);
        q.setParameter("codigoEntidadRegistralOrigen", codigoEntidadRegistralOrigen);
        q.setParameter("decodificacionEntidadRegistralOrigen", decodificacionEntidadRegistralOrigen);
        q.setParameter("numeroRegistroDestino", numeroRegistroDestino);
        q.setParameter("fechaRegistroDestino", fechaRegistroDestino);
        q.setParameter("fechaEstado", new Date());
        q.setParameter("idOficioRemision", oficio.getId());
        q.executeUpdate();

        // Marcamos el Registro original como ACEPTADO y purgamos sus anexos
        if (oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)) {
            registroEntradaEjb.cambiarEstado(oficio.getRegistrosEntrada().get(0).getId(), RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
            anexoEjb.purgarAnexosRegistroAceptado(oficio.getRegistrosEntrada().get(0).getId(), RegwebConstantes.REGISTRO_ENTRADA, oficio.getEntidad().getId());

        }else if(oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){
            registroSalidaEjb.cambiarEstado(oficio.getRegistrosSalida().get(0).getId(),RegwebConstantes.REGISTRO_OFICIO_ACEPTADO);
            anexoEjb.purgarAnexosRegistroAceptado(oficio.getRegistrosSalida().get(0).getId(), RegwebConstantes.REGISTRO_SALIDA, oficio.getEntidad().getId());

        }

    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<String> getNumerosRegistroEntradaFormateadoByOficioRemision(Long idOficioRemision) throws I18NException{

        Query q= em.createQuery("select registroEntrada.registroDetalle.id, registroEntrada.numeroRegistroFormateado from RegistroEntrada registroEntrada, OficioRemision ofiRem " +
                " where registroEntrada in elements(ofiRem.registrosEntrada) and ofiRem.id = :idOficioRemision");

        q.setParameter("idOficioRemision", idOficioRemision);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();

        List<String> numeros = new ArrayList<String>();

        for (Object[] object : result) {

            List<Interesado> interesados = interesadoEjb.findByRegistroDetalle((Long) object[0]);

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
    public List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws I18NException{

        Query q= em.createQuery("select registroSalida.registroDetalle.id, registroSalida.numeroRegistroFormateado from RegistroSalida registroSalida, OficioRemision ofiRem " +
                " where registroSalida in elements(ofiRem.registrosSalida) and ofiRem.id = :idOficioRemision");

        q.setParameter("idOficioRemision", idOficioRemision);
        q.setHint("org.hibernate.readOnly", true);

        List<Object[]> result = q.getResultList();

        List<String> numeros = new ArrayList<String>();


        for (Object[] object : result) {

            List<Interesado> interesados = interesadoEjb.findByRegistroDetalle((Long) object[0]);

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
    public Oficio obtenerTipoOficio(String codigoOrganismo, Long idEntidad) throws I18NException{

        Oficio oficio = new Oficio();
        oficio.setOficioRemision(true);

        Organismo organismo;
        if(multiEntidadEjb.isMultiEntidad()){
            organismo = organismoEjb.findByCodigoMultiEntidad(codigoOrganismo);
        }else{
            organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero(codigoOrganismo, idEntidad);
        }

        if(organismo != null){
            if(!organismo.getEntidad().getId().equals(idEntidad)){
                oficio.setExterno(true);
            }else{
                if(!organismo.getEdp()){
                    //log.info("Es un oficio interno");
                    oficio.setInterno(true);
                }else{
                    // Comprobamos si el organismo edp tiene algún libro que le registre
                    Boolean oficioEdpInterno = organismoEjb.isEdpConLibro(organismo.getId());

                    if(oficioEdpInterno){
                        oficio.setInterno(true);
                    }else{
                        oficio.setEdpExterno(true);
                    }
                    // log.info("Es un oficio edp interno?: " + oficioEdpInterno);
                }
            }

        }else{
            oficio.setExterno(true);
            //log.info("Es un oficio externo");
        }

        return oficio;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException{

        Query or = em.createQuery("select distinct(id) from OficioRemision where usuarioResponsable.entidad.id = :idEntidad");
        or.setParameter("idEntidad", idEntidad);
        List<Object> oficiosRemision =  or.getResultList();

        for (Object id : oficiosRemision) {
            remove(findById((Long) id));
        }

        return oficiosRemision.size();

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficioRemision getByNumeroRegistroFormateado(String numeroRegistroFormateado, Long idEntidad) throws I18NException {

        Query q;
        RegistroEntrada registroEntrada = registroEntradaConsultaEjb.findByNumeroRegistroFormateado(idEntidad, numeroRegistroFormateado);
        if (registroEntrada == null) {
            RegistroSalida registroSalida = registroSalidaConsultaEjb.findByNumeroRegistroFormateado(idEntidad, numeroRegistroFormateado);
            if (registroSalida == null) {
                return null;
            } else {
                q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where " +
                        " :registroSalida in elements(oficioRemision.registrosSalida) ");
                q.setParameter("registroSalida", registroSalida);

            }
        } else {
            q = em.createQuery("Select oficioRemision from OficioRemision as oficioRemision where " +
                    " :registroEntrada in elements(oficioRemision.registrosEntrada) ");
            q.setParameter("registroEntrada", registroEntrada);
        }

        q.setHint("org.hibernate.readOnly", true);

        List<OficioRemision> oficioRemision = q.getResultList();
        if (oficioRemision.size() == 1) {
            return oficioRemision.get(0);
        } else {
            return null;
        }


    }

    @Override
    public CombineStream generarOficioRemisionRtf(OficioRemision oficioRemision, ModeloOficioRemision modeloOficioRemision, List<String> registrosEntrada, List<String> registrosSalida) throws I18NException{

        File archivo = es.caib.regweb3.persistence.utils.FileSystemManager.getArchivo(modeloOficioRemision.getModelo().getId());
        Oficina oficina = oficinaEjb.findById(oficioRemision.getOficina().getId());

        // Convertimos el archivo a array de bytes
        byte[] datos = new byte[0];
        try {
            datos = FileUtils.readFileToByteArray(archivo);
        } catch (IOException e) {
            throw new I18NException(e.getMessage());
        }
        InputStream is = new ByteArrayInputStream(datos);
        java.util.Hashtable<String,Object> ht = new java.util.Hashtable<String,Object>();

        // Extraemos año de la fecha del registro
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String anoOficio = formatYear.format(oficioRemision.getFecha());

        // Fecha según el idioma y mes
        Date dataActual = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MMMMM", new Locale("es"));
        SimpleDateFormat sdf4 = new SimpleDateFormat("MMMMM", new Locale("ca"));
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
        String diaRecibo = sdf1.format(dataActual);
        String mesRecibo;
        String anoRecibo = sdf3.format(dataActual);
        String fechaActualCa;
        String fechaActualEs;

        // Fecha en castellano
        mesRecibo = sdf2.format(dataActual);
        fechaActualEs = diaRecibo + " de " + mesRecibo + " de " + anoRecibo;

        // Fecha en catalán
        mesRecibo = sdf4.format(dataActual);
        if(mesRecibo.startsWith("a") || mesRecibo.startsWith("o")){
            mesRecibo= " d'" + mesRecibo;
        }else{
            mesRecibo= " de " + mesRecibo;
        }
        fechaActualCa = diaRecibo + mesRecibo + " de " + anoRecibo;

        // Registros
        String registros = "";

        if(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())){
            for (String registroEntrada : registrosEntrada) {
                registros = registros.concat("- " + registroEntrada + "\\\r\n");
            }

        }else if(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())){
            for (String registroSalida : registrosSalida) {
                registros = registros.concat("- " + registroSalida + "\\\r\n");
            }
        }

        // Mapeamos los campos del rtf con los del registro
        if(oficioRemision.getOrganismoDestinatario() != null) {
            ht.put("(organismoDestinatario)", ConvertirTexto.toCp1252(oficioRemision.getOrganismoDestinatario().getDenominacion()));
            String direccion = "";
            if(oficioRemision.getOrganismoDestinatario().getNombreVia() != null){
                direccion = direccion + oficioRemision.getOrganismoDestinatario().getNombreVia() + " ";
            }
            if(oficioRemision.getOrganismoDestinatario().getNumVia() != null){
                direccion = direccion + oficioRemision.getOrganismoDestinatario().getNumVia() + " ";
            }
            if(oficioRemision.getOrganismoDestinatario().getCodPostal() != null){
                direccion = direccion + "- " + oficioRemision.getOrganismoDestinatario().getCodPostal() + " ";
            }
            if(oficioRemision.getOrganismoDestinatario().getLocalidad() != null){
                direccion = direccion + oficioRemision.getOrganismoDestinatario().getLocalidad().getNombre();
            }
            ht.put("(direccionOrgDest)", ConvertirTexto.toCp1252(direccion));
        } else{
            ht.put("(organismoDestinatario)", ConvertirTexto.toCp1252(oficioRemision.getDestinoExternoDenominacion()));
            ht.put("(direccionOrgDest)", ConvertirTexto.toCp1252(""));
        }
        ht.put("(numeroOficio)", ConvertirTexto.toCp1252(oficioRemision.getNumeroOficio().toString()));
        ht.put("(anoOficio)", ConvertirTexto.toCp1252(anoOficio));
        ht.put("(oficina)", ConvertirTexto.toCp1252(oficioRemision.getOficina().getDenominacion()));
        if(oficina.getLocalidad() != null){
            ht.put("(localidadOficina)", ConvertirTexto.toCp1252(oficina.getLocalidad().getNombre()));
        }else{
            ht.put("(localidadOficina)", "");
        }
        ht.put("(registrosEntrada)", ConvertirTexto.toCp1252(registros));
        ht.put("(data)", ConvertirTexto.toCp1252(fechaActualCa));
        ht.put("(fecha)", ConvertirTexto.toCp1252(fechaActualEs));

        // Reemplaza el texto completo
        ht.put("(read_only)", ConvertirTexto.getISOBytes("\\annotprot\\readprot\\enforceprot1\\protlevel3\\readonlyrecommended "));

        return new CombineStream(is, ht);

    }
}
