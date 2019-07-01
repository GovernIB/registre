package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
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
import org.springframework.context.i18n.LocaleContextHolder;

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
 *         Date: 16/01/14
 */

@Stateless(name = "RegistroEntradaEJB")
@SecurityDomain("seycon")
/*@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)*/
public class RegistroEntradaBean extends RegistroEntradaCambiarEstadoBean
        implements RegistroEntradaLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @Resource
    private javax.ejb.SessionContext ejbContext;

    @EJB private LibroLocal libroEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private InteresadoLocal interesadoEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private IntegracionLocal integracionEjb;


    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                            UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException {

        try{

            // Obtenemos el Número de registro
            Libro libro = libroEjb.findById(registroEntrada.getLibro().getId());
            Oficina oficina = oficinaEjb.findById(registroEntrada.getOficina().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorEntrada().getId());
            registroEntrada.setNumeroRegistro(numeroRegistro.getNumero());
            registroEntrada.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, libro, oficina));

            // Si no ha introducido ninguna fecha de Origen
            if (registroEntrada.getRegistroDetalle().getFechaOrigen() == null) {
                registroEntrada.getRegistroDetalle().setFechaOrigen(registroEntrada.getFecha());
            }

            //Si no se ha espeficicado un NumeroRegistroOrigen, le asignamos el propio
            if (StringUtils.isEmpty(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen())) {

                registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            }

            // Guardar RegistroEntrada
            registroEntrada = persist(registroEntrada);

            // Guardar el HistorioRegistroEntrada
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()),"registro.modificacion.creacion" ),false);

            // Procesamos los Interesados
            if(interesados != null && interesados.size() > 0){
                interesados = interesadoEjb.guardarInteresados(interesados, registroEntrada.getRegistroDetalle());
                registroEntrada.getRegistroDetalle().setInteresados(interesados);
            }

            // Procesamos los Anexos
            if (anexosFull != null && anexosFull.size() != 0) {
                final Long registroID = registroEntrada.getId();

                for (AnexoFull anexoFull : anexosFull) {
                    anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
                    anexoEjb.crearAnexo(anexoFull, usuarioEntidad, registroID, "entrada", true);
                }
            }

            // Obtenemos el próximo evento del Registro
            Long evento = proximoEventoEntrada(registroEntrada,usuarioEntidad.getEntidad());
            log.info("Evento: " + evento);
            registroEntrada.setEvento(evento);

            //Llamamos al plugin de postproceso
            postProcesoNuevoRegistro(registroEntrada,usuarioEntidad.getEntidad().getId());

            return registroEntrada;

        }catch (I18NException i18n){
            log.info("Error registrando la entrada");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        }catch (I18NValidationException i18nv){
            log.info("Error de validación registrando la entrada");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        } catch (Exception e){
            log.info("Error registrando la entrada");
            e.printStackTrace();
            ejbContext.setRollbackOnly();
            throw e;
        }

    }

    @Override
    public RegistroEntrada actualizar(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        // Obtenemos el RE antes de guardarlos, para crear el histórico
        RegistroEntrada registroEntradaAntiguo = findById(registroEntrada.getId());

        registroEntrada = merge(registroEntrada);

        // Obtenemos el próximo evento del Registro
        Long evento = proximoEventoEntrada(registroEntrada,usuarioEntidad.getEntidad());
        log.info("Evento actualizado: " + evento);
        registroEntrada.setEvento(evento);

        // Creamos el Historico RegistroEntrada
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntradaAntiguo, usuarioEntidad, I18NLogicUtils.tradueix(LocaleContextHolder.getLocale(),"registro.modificacion.datos" ),true);
        postProcesoActualizarRegistro(registroEntrada,usuarioEntidad.getEntidad().getId());

        return registroEntrada;
    }

    @Override
    public Oficio isOficio(Long idRegistro, Set<Long> organismos, Entidad entidadActiva) throws Exception{

        Oficio oficio = new Oficio();

        if(isOficioRemisionExterno(idRegistro)){ // Externo

            oficio.setOficioRemision(true);

            List<OficinaTF> oficinasSIR = isOficioRemisionSir(idRegistro);

            if(!oficinasSIR.isEmpty() && entidadActiva.getSir()){
                oficio.setSir(true);

            }else{
                oficio.setExterno(true);
            }

        }else{

            Boolean interno = isOficioRemisionInterno(idRegistro, organismos);

            oficio.setOficioRemision(interno);
            oficio.setInterno(interno);
        }

        return oficio;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws Exception {

        // Si el array de organismos está vacío, no incluimos la condición.
        String organismosWhere = "";
        if (organismos.size() > 0) {
            organismosWhere = " and re.destino.id not in (:organismos)";
        }

        Query q;

        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.estado = :valido and " +
                "re.destino != null " + organismosWhere);

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionExterno(Long idRegistro) throws Exception {

        Query q;
        q = em.createQuery("Select re.id from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null and re.estado = :valido");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);


        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficinaTF> isOficioRemisionSir(Long idRegistro) throws Exception {

        Query q;
        q = em.createQuery("Select re.destinoExternoCodigo from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null and re.estado = :valido");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);

        List<String> result = q.getResultList();

        if(result.size() > 0){

            String codigoDir3 = result.get(0);
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());

            return oficinasService.obtenerOficinasSIRUnidad(codigoDir3);
        }

        return null;
    }

    @Override
    public Long proximoEventoEntrada(RegistroEntrada registroEntrada, Entidad entidadActiva) throws Exception{


        if(isOficioRemisionExterno(registroEntrada.getId())){ // Externo

            List<OficinaTF> oficinasSIR = isOficioRemisionSir(registroEntrada.getId());

            if(!oficinasSIR.isEmpty() && entidadActiva.getSir()){
                return RegwebConstantes.EVENTO_OFICIO_SIR;

            }else{

                return RegwebConstantes.EVENTO_OFICIO_EXTERNO;
            }

        }else {

            // Obtiene los Organismos de la OficinaActiva en los que puede registrar sin generar OficioRemisión
            LinkedHashSet<Organismo> organismos = organismoEjb.getByOficinaActiva(registroEntrada.getOficina(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
            Set<Long> organismosId = new HashSet<Long>();

            for (Organismo organismo : organismos) {
                organismosId.add(organismo.getId());

            }

            if(isOficioRemisionInterno(registroEntrada.getId(), organismosId)){
                return RegwebConstantes.EVENTO_OFICIO_INTERNO;
            }

        }

        return RegwebConstantes.EVENTO_DISTRIBUIR;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void actualizarRegistrosSinEvento(Entidad entidad) throws Exception {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        peticion.append("setMaxResults: ").append(PropiedadGlobalUtil.getTotalActualizarProximoEvento(entidad.getId())).append(System.getProperty("line.separator"));

        try{

            Query q;
            q = em.createQuery("Select re.id, re.oficina from RegistroEntrada as re where " +
                    "re.oficina.organismoResponsable.entidad.id = :idEntidad and re.evento is null " +
                    "and re.estado = :valido order by fecha desc");

            // Parámetros
            q.setParameter("idEntidad", entidad.getId());
            q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
            q.setMaxResults(PropiedadGlobalUtil.getTotalActualizarProximoEvento(entidad.getId()));

            List<Object[]> result = q.getResultList();

            List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

            peticion.append("Total registros: ").append(registros.size()).append(System.getProperty("line.separator"));

            for (Object[] object : result) {
                RegistroEntrada registro = new RegistroEntrada();
                registro.setId((Long) object[0]);
                registro.setOficina((Oficina) object[1]);
                registros.add(registro);
            }

            for (RegistroEntrada registroEntrada:registros) {
                Long evento = proximoEventoEntrada(registroEntrada, entidad);

                Query q1 = em.createQuery("update RegistroEntrada set evento=:evento where id = :idRegistro");
                q1.setParameter("evento", evento);
                q1.setParameter("idRegistro", registroEntrada.getId());
                q1.executeUpdate();

            }

        }catch (Exception e){
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_ACTUALIZAR_EVENTO, "Actualizar eventos de entrada", peticion.toString(), e, null,System.currentTimeMillis() - tiempo, entidad.getId(), "");
        }

        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_ACTUALIZAR_EVENTO, "Actualizar eventos de entrada", peticion.toString(),System.currentTimeMillis() - tiempo, entidad.getId(), "");

    }

    @Override
    public void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        registroEntrada.setEstado(idEstado);

        // Creamos el HistoricoRegistroEntrada para la modificación d estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada,
                usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.estado"), false);
    }

    @Override
    public void cambiarEstadoAnuladoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", idEstado);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();

        registroEntrada.setEstado(idEstado);

        // Creamos el HistoricoRegistroEntrada para la modificación de estado
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, observacionesAnulacion, false);
    }


    @Override
    public void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad,
                                      String observacionesAnulacion) throws Exception {

        // Estado anulado
        cambiarEstadoAnuladoHistorico(registroEntrada, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);

    }

    @Override
    public void activarRegistroEntrada(RegistroEntrada registroEntrada,
                                       UsuarioEntidad usuarioEntidad) throws Exception {

        // Actualizamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }

    @Override
    public void visarRegistroEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad) throws Exception {

        // Modificamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

        // Asignamos su evento
        if(registroEntrada.getEvento() != null){
            Long evento = proximoEventoEntrada(findById(registroEntrada.getId()), usuarioEntidad.getEntidad());
            registroEntrada.setEvento(evento);
            merge(registroEntrada);
        }

    }

    @Override
    public void tramitarRegistroEntrada(RegistroEntrada registroEntrada,
                                        UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException {

        // CREAMOS LA TRAZABILIDAD
        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOficioRemision(null);
        trazabilidad.setFecha(new Date());
        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_DISTRIBUCION);
        trazabilidad.setRegistroEntradaOrigen(registroEntrada);
        trazabilidad.setRegistroSalida(null);
        trazabilidad.setRegistroEntradaDestino(null);
        trazabilidadEjb.persist(trazabilidad);

        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_DISTRIBUIDO, usuarioEntidad);

    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws Exception {

        List<?> registros = em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.usuario.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada rectificar(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        RegistroEntrada rectificado = null;
        Long idRegistro = registroEntrada.getId();

        try {
            List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroEntrada.getRegistroDetalle().getAnexosFull();

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroEntrada);
            session.evict(registroEntrada.getRegistroDetalle());
            session.evict(registroEntrada.getRegistroDetalle().getInteresados());

            // Nuevas propiedades
            registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            registroEntrada.setFecha(new Date());

            // Set Id's a null
            registroEntrada.setId(null);
            registroEntrada.getRegistroDetalle().setId(null);
            registroEntrada.getRegistroDetalle().setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }
            registroEntrada.getRegistroDetalle().setAnexos(null);

            registroEntrada.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroEntrada.getNumeroRegistroFormateado());

            // Registramos el nuevo registro
            rectificado = registrarEntrada(registroEntrada, usuarioEntidad,interesados, anexos);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro,RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA);
            trazabilidad.setRegistroEntradaOrigen(getReference(idRegistro));
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setRegistroSir(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
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
    public void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws Exception {

        Query q = em.createQuery("update RegistroEntrada set destino = :idOrganismoSustituto where destino = :idOrganismoExtinguido and (estado = :valido or estado = :pendienteVisar)");
        q.setParameter("idOrganismoSustituto", idOrganismoSustituto);
        q.setParameter("idOrganismoExtinguido", idOrganismoExtinguido);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("pendienteVisar", RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

        q.executeUpdate();

    }

    @Override
    public RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException {

        RegistroEntrada re = findById(id);

        return cargarAnexosFull(re);
    }

    /**
     * Carga los Anexos Completos al RegistroEntrada pasado por parámetro
     * @param registroEntrada
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private RegistroEntrada cargarAnexosFull(RegistroEntrada registroEntrada) throws Exception, I18NException {
        Long idEntidad = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getId();

        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            AnexoFull anexoFull = anexoEjb.getAnexoFull(anexo.getId(), idEntidad);
            anexosFull.add(anexoFull);
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        registroEntrada.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroEntrada;
    }

    @Override
    public void postProcesoActualizarRegistro(RegistroEntrada re,Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.actualizarRegistroEntrada(re);
        }

    }

    @Override
    public void postProcesoNuevoRegistro(RegistroEntrada re,Long entidadId) throws Exception, I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if(postProcesoPlugin != null){
            postProcesoPlugin.nuevoRegistroEntrada(re);
        }
    }



}
