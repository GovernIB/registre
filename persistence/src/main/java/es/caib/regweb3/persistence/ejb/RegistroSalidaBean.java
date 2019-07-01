package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Session;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */

@Stateless(name = "RegistroSalidaEJB")
@SecurityDomain("seycon")
/*@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)*/
public class RegistroSalidaBean extends RegistroSalidaCambiarEstadoBean
        implements RegistroSalidaLocal {

    protected final Logger log = Logger.getLogger(getClass());

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
    @EJB private IntegracionLocal integracionEjb;


    @Override
    public synchronized RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                                       UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException {

        try{

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroSalida.getLibro().getId());
            Oficina oficina = oficinaEjb.findById(registroSalida.getOficina().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorSalida().getId());
            registroSalida.setNumeroRegistro(numeroRegistro.getNumero());
            registroSalida.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroSalida.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroSalida, libro, oficina));

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
            historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalida, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

            // Procesamos los Interesados
            if(interesados != null && interesados.size() > 0){
                interesadoEjb.guardarInteresados(interesados, registroSalida.getRegistroDetalle());
                registroSalida.getRegistroDetalle().setInteresados(interesados);
            }

            // Procesamos los anexos
            if (anexos != null && anexos.size() != 0) {
                final Long registroID = registroSalida.getId();
                for (AnexoFull anexoFull : anexos) {
                    anexoFull.getAnexo().setRegistroDetalle(registroSalida.getRegistroDetalle());
                    anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "salida", true);
                }
            }

            // Obtenemos el próximo evento del Registro
            Long evento = proximoEventoSalida(registroSalida,usuarioEntidad.getEntidad());
            log.info("Evento: " + evento);
            registroSalida.setEvento(evento);

            //Llamamos al plugin de postproceso
            postProcesoNuevoRegistro(registroSalida,usuarioEntidad.getEntidad().getId());

            return registroSalida;

        }catch (I18NException i18n){
            log.info("Error registrando la salida");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        }catch (I18NValidationException i18nv){
            log.info("Error de validación registrando la salida");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        } catch (Exception e){
            log.info("Error registrando la salida");
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

    }

    @Override
    public RegistroSalida actualizar(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        // Obtenemos el RS antes de guardarlos, para crear el histórico
        RegistroSalida registroSalidaAntiguo = findById(registroSalida.getId());

        registroSalida = merge(registroSalida);

        // Obtenemos el próximo evento del Registro
        Long evento = proximoEventoSalida(registroSalida,usuarioEntidad.getEntidad());
        log.info("Evento actualizado: " + evento);
        registroSalida.setEvento(evento);

        // Creamos el Historico RegistroEntrada
        historicoRegistroSalidaEjb.crearHistoricoRegistroSalida(registroSalidaAntiguo, usuarioEntidad, I18NLogicUtils.tradueix(LocaleContextHolder.getLocale(),"registro.modificacion.datos" ), true);
        postProcesoActualizarRegistro(registroSalida,usuarioEntidad.getEntidad().getId());

        return registroSalida;
    }


    @Override
    public Oficio isOficio(RegistroSalida registroSalida, Set<String> organismos, Entidad entidadActiva) throws Exception{

        Oficio oficio = new Oficio();

        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida(); // Fecha a partir de la cual se generarán oficios de salida

        if((StringUtils.isEmpty(fecha) || registroSalida.getFecha().after(new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA).parse(fecha))) && registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)){

            if(isOficioRemisionExterno(registroSalida, organismos)){ // Externo
                oficio.setOficioRemision(true);

                List<OficinaTF> oficinasSIR = isOficioRemisionSir(registroSalida, organismos);

                if(!oficinasSIR.isEmpty() && entidadActiva.getSir()){
                    oficio.setSir(true);
                }else{
                    oficio.setExterno(true);
                }

            }else{
                Boolean interno = isOficioRemisionInterno(registroSalida, organismos);

                oficio.setOficioRemision(interno);
                oficio.setInterno(interno);
            }
        }

        return oficio;
    }


    @Override
    public Boolean isOficioRemisionInterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception {

        String codigoDir3 = organismoOficioRemision(registroSalida, organismos);

        if(StringUtils.isNotEmpty(codigoDir3)){
            Long idEntidad = registroSalida.getOficina().getOrganismoResponsable().getEntidad().getId();
            return organismoEjb.findByCodigoEntidad(codigoDir3, idEntidad) != null;
        }

        return false;
    }

    @Override
    public Boolean isOficioRemisionExterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception {

        String codigoDir3 = organismoOficioRemision(registroSalida, organismos);

        if(StringUtils.isNotEmpty(codigoDir3)){
            Long idEntidad = registroSalida.getOficina().getOrganismoResponsable().getEntidad().getId();
            return organismoEjb.findByCodigoEntidadSinEstadoLigero(codigoDir3, idEntidad) == null;
        }

        return false;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficinaTF> isOficioRemisionSir(RegistroSalida registroSalida, Set<String> organismos) throws Exception {

        // Obtenemos el organismo destinatario del Registro en el caso de que sea un OficioRemision externo
        String codigoDir3 = organismoOficioRemision(registroSalida, organismos);

        // Si se trata de un OficioRemisionExterno, comprobamos si el destino tiene Oficinas Sir
        if(StringUtils.isNotEmpty(codigoDir3) && isOficioRemisionExterno(registroSalida, organismos)){

            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());

            return oficinasService.obtenerOficinasSIRUnidad(codigoDir3);
        }

        return null;
    }

    /**
     * Comprueba si el RegistroSalida es un Oficio de Remisión y obtiene el códigoDir3 del
     * Interesado tipo administración asociado al registro.
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    private String organismoOficioRemision(RegistroSalida registroSalida, Set<String> organismos) throws Exception{

        List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                if(!organismos.contains(interesado.getCodigoDir3())){

                    return interesado.getCodigoDir3();
                }
            }
        }

        return null;
    }

    @Override
    public Long proximoEventoSalida(RegistroSalida registroSalida, Entidad entidadActiva) throws Exception{

        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida(); // Fecha a partir de la cual se generarán oficios de salida


        // Obtiene los Organismos de la OficinaActiva en los que puede registrar sin generar OficioRemisión
        LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(registroSalida.getOficina(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        // Creamos un Set solo con los codigos
        Set<String> organismosCodigo = new HashSet<String>();

        for (Organismo organismo : organismos) {
            organismosCodigo.add(organismo.getCodigo());

        }

        if((StringUtils.isEmpty(fecha) || registroSalida.getFecha().after(new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA).parse(fecha))) && registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)){

            if(isOficioRemisionExterno(registroSalida, organismosCodigo)){ // Externo

                List<OficinaTF> oficinasSIR = isOficioRemisionSir(registroSalida, organismosCodigo);

                if(!oficinasSIR.isEmpty() && entidadActiva.getSir()){
                    return RegwebConstantes.EVENTO_OFICIO_SIR;
                }else{
                    return RegwebConstantes.EVENTO_OFICIO_EXTERNO;
                }

            }else if (isOficioRemisionInterno(registroSalida, organismosCodigo)){
                return RegwebConstantes.EVENTO_OFICIO_INTERNO;
            }
        }

        return RegwebConstantes.EVENTO_DISTRIBUIR;
    }

    @Override
    public void actualizarEvento(Long idRegistro, Entidad entidadActiva) throws Exception{

        RegistroSalida registroSalida = findById(idRegistro);

        Long evento = proximoEventoSalida(registroSalida,entidadActiva);

        registroSalida.setEvento(evento);

        merge(registroSalida);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    @TransactionTimeout(value = 1200)  // 20 minutos
    public void actualizarRegistrosSinEvento(Entidad entidad) throws Exception {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        peticion.append("setMaxResults: ").append(PropiedadGlobalUtil.getTotalActualizarProximoEvento(entidad.getId())).append(System.getProperty("line.separator"));

        try{

            Query q;
            q = em.createQuery("Select rs from RegistroSalida as rs where " +
                    "rs.oficina.organismoResponsable.entidad.id = :idEntidad and rs.evento is null " +
                    "and rs.estado = :valido order by fecha desc");

            // Parámetros
            q.setParameter("idEntidad", entidad.getId());
            q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
            q.setMaxResults(PropiedadGlobalUtil.getTotalActualizarProximoEvento(entidad.getId()));

            List<RegistroSalida> registros = q.getResultList();

            peticion.append("Total registros: ").append(registros.size()).append(System.getProperty("line.separator"));

            for (RegistroSalida registroSalida:registros) {
                Long evento = proximoEventoSalida(registroSalida, entidad);

                Query q1 = em.createQuery("update RegistroSalida set evento=:evento where id = :idRegistro");
                q1.setParameter("evento", evento);
                q1.setParameter("idRegistro", registroSalida.getId());
                q1.executeUpdate();

            }

            // Integración
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_ACTUALIZAR_EVENTO, "Actualizar eventos de salidas", peticion.toString(),System.currentTimeMillis() - tiempo, entidad.getId(), "");

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_ACTUALIZAR_EVENTO, "Actualizar eventos de entradas", peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidad.getId(), "");
        }

    }


    @Override
    public void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoAnuladoHistorico(registroSalida, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);
    }

    @Override
    public void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroSalida
        cambiarEstadoHistorico(registroSalida, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

        // Asignamos su evento
        if(registroSalida.getEvento() != null){
            Long evento = proximoEventoSalida(findById(registroSalida.getId()), usuarioEntidad.getEntidad());
            registroSalida.setEvento(evento);
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

        List<Object> registros = em.createQuery("Select distinct(rs.id) from RegistroSalida as rs where rs.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

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
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.estado" ), false);
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
    public RegistroSalida rectificar(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception {

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
            registroSalida.getRegistroDetalle().setAnexos(null);

            registroSalida.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroSalida.getNumeroRegistroFormateado());

            // Registramos el nuevo registro
            rectificado = registrarSalida(registroSalida, usuarioEntidad,interesados, anexos);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro,RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_SALIDA);
            trazabilidad.setRegistroSalida(registroSalida);
            trazabilidad.setRegistroSalidaRectificado(getReference(idRegistro));
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

        } catch (I18NException e) {
            e.printStackTrace();
        } catch (I18NValidationException e) {
            e.printStackTrace();
        }

        return rectificado;
    }

    @Override
    public RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroSalida registroSalida = findById(id);
        return cargarAnexosFull(registroSalida);
    }

    /**
     * Carga los Anexos Completos al RegistroSalida pasado por parámetro
     * @param registroSalida
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroSalida cargarAnexosFull(RegistroSalida registroSalida) throws Exception, I18NException {
        Long idEntidad = registroSalida.getOficina().getOrganismoResponsable().getEntidad().getId();

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
        if(postProcesoPlugin != null){
            postProcesoPlugin.actualizarRegistroSalida(rs);
        }

    }

    @Override
    public void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.nuevoRegistroSalida(rs);
        }
    }
}
