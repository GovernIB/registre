package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.NumeroRegistro;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_SALIDA;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RegistroSalidaBean extends RegistroSalidaCambiarEstadoBean implements RegistroSalidaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB private LibroLocal libroEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private InteresadoLocal interesadoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private MultiEntidadLocal multiEntidadEjb;


    @Override
    public RegistroSalida findByIdCompleto(Long id) throws Exception {

        RegistroSalida registroSalida = findById(id);

        Hibernate.initialize(registroSalida.getRegistroDetalle().getAnexos());
        Hibernate.initialize(registroSalida.getRegistroDetalle().getInteresados());

        return registroSalida;
    }

    @Override
    public RegistroSalida registrarSalida(RegistroSalida registroSalida, Entidad entidad, UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException {

        try {
            //Asociamos su entidad
            registroSalida.setEntidad(entidad);

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroSalida.getLibro().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorSalida().getId());
            registroSalida.setNumeroRegistro(numeroRegistro.getNumero());
            registroSalida.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, libro, entidad));

            // Si no ha introducido ninguna fecha de Origen
            if (registroSalida.getRegistroDetalle().getFechaOrigen() == null) {
                registroSalida.getRegistroDetalle().setFechaOrigen(registroSalida.getFecha());
            }

            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen())) {

                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            }

            // Guardamos el RegistroSalida
            registroSalida = persist(registroSalida);

            //Guardamos el HistorioRegistroSalida
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.creacion"), false);

            // Procesamos los Interesados
            if (interesados != null && interesados.size() > 0) {
                interesadoEjb.guardarInteresados(interesados, registroSalida.getRegistroDetalle());
                registroSalida.getRegistroDetalle().setInteresados(interesados);
            }

            // Procesamos los anexos
            if (anexosFull != null && anexosFull.size() != 0) {
                final Long registroID = registroSalida.getId();
                for (AnexoFull anexoFull : anexosFull) {
                    anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
                    AnexoFull anexoFullCreado = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, entidad, registroID, REGISTRO_SALIDA, null, validarAnexos);

                    registroSalida.getRegistroDetalle().getAnexos().add(anexoFullCreado.getAnexo());
                }
                registroSalida.getRegistroDetalle().getAnexosFull().addAll(anexosFull);
            }

            // Obtenemos el próximo evento del Registro
            if(multiEntidadEjb.isMultiEntidad()) {
                Long evento = proximoEventoSalidaMultiEntidad(registroSalida, entidad);
                registroSalida.setEvento(evento);
            }else{
                Long evento = proximoEventoSalida(registroSalida, entidad);
                registroSalida.setEvento(evento);
            }


            //Llamamos al plugin de postproceso
            postProcesoNuevoRegistro(registroSalida, entidad.getId());

            return registroSalida;

        } catch (I18NException | Exception i18n) {
            log.info("Error registrando la salida");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        } catch (I18NValidationException i18nv) {
            log.info("Error de validación registrando la salida");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        }

    }

    @Override
    public RegistroSalida actualizar(RegistroSalida antiguo, RegistroSalida registroSalida, Entidad entidad, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        registroSalida = merge(registroSalida);

        // Obtenemos el próximo evento del Registro
        if(multiEntidadEjb.isMultiEntidad()) {
            Long evento = proximoEventoSalidaMultiEntidad(registroSalida, entidad);
            registroSalida.setEvento(evento);
        }else{
            Long evento = proximoEventoSalida(registroSalida, entidad);
            registroSalida.setEvento(evento);
        }


        // Creamos el Historico RegistroEntrada
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(antiguo, usuarioEntidad, I18NLogicUtils.tradueix(LocaleContextHolder.getLocale(), "registro.modificacion.datos"), true);
        postProcesoActualizarRegistro(registroSalida, entidad.getId());

        return registroSalida;
    }


    @Override
    public Boolean isOficioRemisionInterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception {

        String codigoDir3 = RegistroUtils.obtenerCodigoDir3Interesado(registroSalida);

        if (StringUtils.isNotEmpty(codigoDir3)) {
            Long idEntidad = registroSalida.getEntidad().getId();
            return organismoEjb.findByCodigoEntidadLigero(codigoDir3, idEntidad) != null;
        }

        return false;
    }

    @Override
    public Boolean isOficioRemisionExterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception {

        String codigoDir3 = RegistroUtils.obtenerCodigoDir3Interesado(registroSalida);

        if (StringUtils.isNotEmpty(codigoDir3)) {
            Long idEntidad = registroSalida.getEntidad().getId();
            return organismoEjb.findByCodigoEntidadSinEstadoLigero(codigoDir3, idEntidad) == null;
        }

        return false;
    }


    @Override
    public Boolean isOficioRemisionExternoMultiEntidad(RegistroSalida registroSalida, Set<String> organismos) throws Exception {

        String codigoDir3 = RegistroUtils.obtenerCodigoDir3Interesado(registroSalida);

        if (StringUtils.isNotEmpty(codigoDir3)) {

            Organismo organismo = organismoEjb.findByCodigoMultiEntidad(codigoDir3);
            return organismo == null || !organismo.getEntidad().equals(registroSalida.getOficina().getOrganismoResponsable().getEntidad());

        }

        return false;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficinaTF> isOficioRemisionSir(RegistroSalida registroSalida, Set<String> organismos, Long idEntidad) throws Exception {

        // Obtenemos el organismo destinatario del Registro en el caso de que sea un OficioRemision externo
        String codigoDir3 = RegistroUtils.obtenerCodigoDir3Interesado(registroSalida);

        // Si se trata de un OficioRemisionExterno, comprobamos si el destino tiene Oficinas Sir
        if (StringUtils.isNotEmpty(codigoDir3) && isOficioRemisionExterno(registroSalida, organismos)) {

            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

            return oficinasService.obtenerOficinasSIRUnidad(codigoDir3);
        }

        return null;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficinaTF> isOficioRemisionSirMultiEntidad(RegistroSalida registroSalida, Set<String> organismos, Long idEntidad) throws Exception {

        // Obtenemos el organismo destinatario del Registro en el caso de que sea un OficioRemision externo
        String codigoDir3 = RegistroUtils.obtenerCodigoDir3Interesado(registroSalida);

        // Si se trata de un OficioRemisionExterno, comprobamos si el destino tiene Oficinas Sir
        if (StringUtils.isNotEmpty(codigoDir3) && isOficioRemisionExternoMultiEntidad(registroSalida, organismos)) {

            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

            return oficinasService.obtenerOficinasSIRUnidad(codigoDir3);
        }

        return null;
    }

    @Override
    public Long proximoEventoSalida(RegistroSalida registroSalida, Entidad entidadActiva) throws Exception {

        Oficina oficina = oficinaEjb.findById(registroSalida.getOficina().getId());

        // Obtiene los Organismos de la OficinaActiva en los que puede registrar sin generar OficioRemisión
        LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(oficina, RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());

        }

        if (registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

            if (isOficioRemisionExterno(registroSalida, organismosCodigo)) { // Externo

                // Si la entidad está en SIR y la Oficina está activada para Envío Sir
                if (entidadActiva.getSir() && oficinaEjb.isSIREnvio(registroSalida.getOficina().getId())) {
                    List<OficinaTF> oficinasSIR = isOficioRemisionSir(registroSalida, organismosCodigo, entidadActiva.getId());

                    if (oficinasSIR != null && !oficinasSIR.isEmpty()) {
                        return RegwebConstantes.EVENTO_OFICIO_SIR;
                    }
                }
                return RegwebConstantes.EVENTO_OFICIO_EXTERNO;
            }
        }

        return RegwebConstantes.EVENTO_DISTRIBUIR;
    }



    @Override
    public Long proximoEventoSalidaMultiEntidad(RegistroSalida registroSalida, Entidad entidadActiva) throws Exception {

        Oficina oficina = oficinaEjb.findById(registroSalida.getOficina().getId());

        // Obtiene los Organismos de la OficinaActiva en los que puede registrar sin generar OficioRemisión
        LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(oficina, RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());

        }

        if (registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

            if (isOficioRemisionExternoMultiEntidad(registroSalida, organismosCodigo)) { // Externo

                // Si la entidad está en SIR y la Oficina está activada para Envío Sir
                if (entidadActiva.getSir() && oficinaEjb.isSIREnvio(registroSalida.getOficina().getId())) {
                    List<OficinaTF> oficinasSIR = isOficioRemisionSirMultiEntidad(registroSalida, organismosCodigo, entidadActiva.getId());

                    if (oficinasSIR != null && !oficinasSIR.isEmpty()) {
                        return RegwebConstantes.EVENTO_OFICIO_SIR;
                    }
                }
                return RegwebConstantes.EVENTO_OFICIO_EXTERNO;
            }
        }
        return RegwebConstantes.EVENTO_DISTRIBUIR;
    }

    @Override
    public void actualizarEvento(Long idRegistro, Entidad entidadActiva) throws Exception {

        RegistroSalida registroSalida = findById(idRegistro);
        Hibernate.initialize(registroSalida.getRegistroDetalle().getInteresados());

        if(multiEntidadEjb.isMultiEntidad()) {
            Long evento = proximoEventoSalidaMultiEntidad(registroSalida, entidadActiva);
            registroSalida.setEvento(evento);
        }else{
            Long evento = proximoEventoSalida(registroSalida, entidadActiva);
            registroSalida.setEvento(evento);
        }

        merge(registroSalida);
    }

    /**
     * Obtiene los Registros de Salida que son DISTRIBUCIÓN
     *
     * @param oficina
     * @param entidad
     * @return
     * @throws Exception
     */
    @SuppressWarnings(value = "unchecked")
    public Integer actualizarEventoDistribuirSalidas(Oficina oficina, Entidad entidad) throws Exception {

        // Obtiene los Organismos de la OficinaActiva en los que puede registrar sin generar OficioRemisión
        LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(oficina, RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();
        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());
        }

        String queryFecha = "";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if (StringUtils.isNotEmpty(fecha)) {
            queryFecha = " rs.fecha >= :fecha and ";
        }

        // Obtenemos los Registros de Salida que son Oficio de remisión
        Query q = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where " +
                "rs.estado = :valido and evento is null and rs.oficina.id = :idOficina and " + queryFecha +
                " rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and codigoDir3 in (:organismos)) ");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", oficina.getId());
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("organismos", organismosCodigo);
        if (StringUtils.isNotEmpty(fecha)) {
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        //q.setMaxResults(PropiedadGlobalUtil.getTotalActualizarProximoEvento(entidad.getId()));
        q.setHint("org.hibernate.readOnly", true);
        List<Long> registros = q.getResultList();

        for (Long idRegistro : registros) {

            Query q1 = em.createQuery("update RegistroSalida set evento=:evento where id = :idRegistro and evento is null");
            q1.setParameter("evento", RegwebConstantes.EVENTO_DISTRIBUIR);
            q1.setParameter("idRegistro", idRegistro);
            q1.executeUpdate();

        }

        return registros.size();
    }

    /**
     * Obtiene los Registros de Salida que son DISTRIBUCIÓN
     *
     * @param oficina
     * @param entidad
     * @return
     * @throws Exception
     */
    @SuppressWarnings(value = "unchecked")
    public Integer actualizarEventoDistribuirSalidasPersona(Oficina oficina, Entidad entidad) throws Exception {

        String queryFecha = "";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if (StringUtils.isNotEmpty(fecha)) {
            queryFecha = " rs.fecha >= :fecha and ";
        }

        // Obtenemos los Registros de Salida que son Oficio de remisión
        Query q = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where " +
                "rs.estado = :valido and evento is null and rs.oficina.id = :idOficina and " + queryFecha +
                " rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo != :administracion ) ");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", oficina.getId());
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        if (StringUtils.isNotEmpty(fecha)) {
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        //q.setMaxResults(PropiedadGlobalUtil.getTotalActualizarProximoEvento(entidad.getId()));
        q.setHint("org.hibernate.readOnly", true);
        List<Long> registros = q.getResultList();

        for (Long idRegistro : registros) {

            Query q1 = em.createQuery("update RegistroSalida set evento=:evento where id = :idRegistro and evento is null");
            q1.setParameter("evento", RegwebConstantes.EVENTO_DISTRIBUIR);
            q1.setParameter("idRegistro", idRegistro);
            q1.executeUpdate();

        }

        return registros.size();
    }


    @Override
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoAnuladoHistorico(registroSalida, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);
    }

    @Override
    public void activarRegistroSalida(RegistroSalida registroSalida, Entidad entidad, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoHistorico(registroSalida, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

        // Asignamos su evento
        if (registroSalida.getEvento() != null) {
            if(multiEntidadEjb.isMultiEntidad()) {
                Long evento = proximoEventoSalidaMultiEntidad(registroSalida, entidad);
                registroSalida.setEvento(evento);
            }else{
                Long evento = proximoEventoSalida(registroSalida, entidad);
                registroSalida.setEvento(evento);
            }
            merge(registroSalida);
        }

    }


    @Override
    public void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoHistorico(registroSalida, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);
    }

    /**
     * Convierte los resultados de una query en una lista de {@link es.caib.regweb3.model.utils.RegistroBasico}
     *
     * @param result
     * @return
     * @throws Exception
     */
    private List<RegistroBasico> getRegistroBasicoList(List<Object[]> result) throws Exception {

        List<RegistroBasico> registros = new ArrayList<RegistroBasico>();

        for (Object[] object : result) {
            RegistroBasico registroBasico = new RegistroBasico((Long) object[0], (String) object[1], (Date) object[2], (String) object[3], (String) object[4], (String) object[5]);

            registros.add(registroBasico);
        }

        return registros;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<Object> registros = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where rs.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }


    @Override
    public void cambiarEstadoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {
        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroSalida.getId());
        q.executeUpdate();

        registroSalida.setEstado(idEstado);

        // Creamos el HistoricoRegistroSalida para la modificación de estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), false);
    }

    @Override
    public void cambiarEstadoAnuladoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        Query q = em.createQuery("update RegistroSalida set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroSalida.getId());
        q.executeUpdate();

        registroSalida.setEstado(idEstado);

        // Creamos el HistoricoRegistroSalida para la modificación de estado
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, observacionesAnulacion, false);
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroSalida rectificar(Entidad entidad, RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        RegistroSalida rectificado = null;
        Long idRegistro = registroSalida.getId();

        try {
            List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroSalida.getRegistroDetalle().getAnexosFull();

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroSalida);
            session.evict(registroSalida.getRegistroDetalle());
            session.evict(registroSalida.getRegistroDetalle().getInteresados());

            // Nuevas propiedades
            registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            registroSalida.setFecha(new Date());

            // Set Id's a null
            registroSalida.setId(null);
            registroSalida.getRegistroDetalle().setId(null);
            registroSalida.getRegistroDetalle().setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }
            registroSalida.getRegistroDetalle().setAnexos(new ArrayList<Anexo>());

            registroSalida.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroSalida.getNumeroRegistroFormateado());


            //Para evitar lazy.
            Oficina oficina = oficinaEjb.findById(registroSalida.getOficina().getId());

            LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(oficina, RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
            // Creamos un Set solo con los codigos
            Set<String> organismosCodigo = new HashSet<String>();

            for (Organismo organismo : organismos) {
                organismosCodigo.add(organismo.getCodigo());

            }

            //Obtenemos el codigo dir3 del interesado que es tipo administración
            String codigoDir3 = "";
            Interesado interesadoAdministracion = null;
            for (Interesado interesado : interesados) {
                if (interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

                    interesadoAdministracion = interesado;

                }
            }

            /* En el caso de rectificar se escoge el primer sustituto de la lista que nos devuelven y se cambia de manera transparente para el usuario */
            if (interesadoAdministracion != null && !interesadoAdministracion.getCodigoDir3().isEmpty()) {
                Organismo organismo = organismoEjb.findByCodigoByEntidadMultiEntidad(codigoDir3, registroSalida.getUsuario().getEntidad().getId());
                if (organismo != null) { //Destino interno
                    if (!organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                        Set<Organismo> historicosFinales = new HashSet<Organismo>();
                        organismoEjb.obtenerHistoricosFinales(organismo.getId(), historicosFinales);

                        // si tiene sustitutos, modificamos el interesado de tipo administración con los datos del sustituto.
                        if (historicosFinales.size() > 0) {
                            interesadoAdministracion.setCodigoDir3(historicosFinales.iterator().next().getCodigo());
                            interesadoAdministracion.setDocumento(historicosFinales.iterator().next().getCodigo());
                            interesadoAdministracion.setRazonSocial(historicosFinales.iterator().next().getDenominacion());

                        } else {
                            log.info("No hay sustitutos, se calculará mal el próximo evento");
                        }
                    }

                } else {

                    UnidadTF destinoExterno = organismoEjb.obtenerDestinoExterno(interesadoAdministracion.getCodigoDir3(), entidad.getId());
                    //Si está extinguido
                    if (destinoExterno != null && !destinoExterno.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                        //Si es SIR, obtenemos sus sustitutos y asignamos el primero.
                        if (registroSalida.getEvento().equals(RegwebConstantes.EVENTO_OFICIO_SIR)) {
                            List<UnidadTF> destinosExternosSIR = organismoEjb.obtenerSustitutosExternosSIR(destinoExterno.getCodigo(), entidad.getId());
                            if (destinosExternosSIR.size() > 0) {
                                interesadoAdministracion.setCodigoDir3(destinosExternosSIR.get(0).getCodigo());
                                interesadoAdministracion.setDocumento(destinosExternosSIR.get(0).getCodigo());
                                interesadoAdministracion.setRazonSocial(destinosExternosSIR.get(0).getDenominacion());
                            } else {
                                log.info("No hay sustitutos SIR, se calculará mal el próximo evento");
                            }

                        } else { //Si no es SIR, obtenemos sus sustitutos y asignamos el primero.
                            List<UnidadTF> destinosExternos = organismoEjb.obtenerSustitutosExternos(destinoExterno.getCodigo(), entidad.getId());
                            if (destinosExternos.size() > 0) {
                                interesadoAdministracion.setCodigoDir3(destinosExternos.get(0).getCodigo());
                                interesadoAdministracion.setDocumento(destinosExternos.get(0).getCodigo());
                                interesadoAdministracion.setRazonSocial(destinosExternos.get(0).getDenominacion());
                            } else {
                                log.info("No hay sustitutos externos, se calculará mal el próximo evento");
                            }
                        }
                    }
                }
            }

            // Registramos el nuevo registro
            rectificado = registrarSalida(registroSalida, entidad, usuarioEntidad, interesados, anexos, false);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro, RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA);
            trazabilidad.setRegistroSalida(registroSalida);
            trazabilidad.setRegistroSalidaRectificado(getReference(idRegistro));
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

        } catch (I18NException | I18NValidationException e) {
            e.printStackTrace();
        }

        return rectificado;
    }

    @Override
    public RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroSalida registroSalida = findByIdCompleto(id);
        return cargarAnexosFull(registroSalida);
    }

    @Override
    public RegistroSalida getConAnexosFullLigero(Long id) throws Exception, I18NException {

        RegistroSalida rs = em.find(RegistroSalida.class, id);
        Long idEntidad = rs.getEntidad().getId();
        List<Anexo> anexos = rs.getRegistroDetalle().getAnexos();

        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFullLigero(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de salida.
        rs.getRegistroDetalle().setAnexosFull(anexosFull);
        return rs;
    }

    /**
     * Carga los Anexos Completos al RegistroSalida pasado por parámetro
     *
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroSalida cargarAnexosFull(RegistroSalida registroSalida) throws Exception, I18NException {

        Long idEntidad = registroSalida.getEntidad().getId();
        List<Anexo> anexos = registroSalida.getRegistroDetalle().getAnexos();

        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de salida.
        registroSalida.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroSalida;
    }


    @Override
    public void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if (postProcesoPlugin != null) {
            postProcesoPlugin.actualizarRegistroSalida(rs);
        }

    }

    @Override
    public void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if (postProcesoPlugin != null) {
            postProcesoPlugin.nuevoRegistroSalida(rs);
        }
    }
}
