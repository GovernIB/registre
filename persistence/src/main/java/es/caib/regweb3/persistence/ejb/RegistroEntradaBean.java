package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "RegistroEntradaEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class RegistroEntradaBean extends RegistroEntradaCambiarEstadoBean implements RegistroEntradaLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

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
    @EJB private MultiEntidadLocal multiEntidadEjb;


    @Override
    public RegistroEntrada findByIdCompleto(Long id) throws I18NException {

        RegistroEntrada registroEntrada = findById(id);

        Hibernate.initialize(registroEntrada.getRegistroDetalle().getAnexos());
        Hibernate.initialize(registroEntrada.getRegistroDetalle().getInteresados());

        return registroEntrada;
    }

    @Override
    public RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull, Boolean validarAnexos)
            throws I18NException, I18NValidationException {

        try {
            //Asociamos su entidad
            registroEntrada.setEntidad(entidad);

            // Obtenemos el Número de registro
            Libro libro = libroEjb.getReference(registroEntrada.getLibro().getId());
            NumeroRegistro numeroRegistro = contadorEjb.incrementarContador(libro.getContadorEntrada().getId());
            registroEntrada.setNumeroRegistro(numeroRegistro.getNumero());
            registroEntrada.setFecha(numeroRegistro.getFecha());

            // Generamos el Número de registro formateado
            registroEntrada.setNumeroRegistroFormateado(RegistroUtils.numeroRegistroFormateado(registroEntrada, libro, entidad));

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
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "registro.modificacion.creacion"), false);

            // Procesamos los Interesados
            if (interesados != null && interesados.size() > 0) {
                interesados = interesadoEjb.guardarInteresados(interesados, registroEntrada.getRegistroDetalle());
                registroEntrada.getRegistroDetalle().setInteresados(interesados);
            }

            // Procesamos los Anexos
            if (anexosFull != null && anexosFull.size() != 0) {
                final Long registroID = registroEntrada.getId();

                for (AnexoFull anexoFull : anexosFull) {
                    anexoFull.getAnexo().setRegistroDetalle(registroEntrada.getRegistroDetalle());
                    AnexoFull anexoFullCreado;
                    if(!anexoFull.getAnexo().getConfidencial()){
                        anexoFullCreado = anexoEjb.crearAnexo(anexoFull, usuarioEntidad,entidad, registroID, REGISTRO_ENTRADA, null, validarAnexos);
                    }else{
                        anexoFullCreado = anexoEjb.crearAnexoConfidencial(anexoFull, usuarioEntidad, entidad, registroID, REGISTRO_ENTRADA);
                    }
                    registroEntrada.getRegistroDetalle().getAnexos().add(anexoFullCreado.getAnexo());
                }

                registroEntrada.getRegistroDetalle().getAnexosFull().addAll(anexosFull);
            }

            // Obtenemos el próximo evento del Registro
            if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){

                if(multiEntidadEjb.isMultiEntidad()) {
                    Long evento = proximoEventoEntradaMultiEntidad(registroEntrada, entidad, registroEntrada.getOficina().getId());
                    registroEntrada.setEvento(evento);
                }else{
                    Long evento = proximoEventoEntrada(registroEntrada, entidad, registroEntrada.getOficina().getId());
                    registroEntrada.setEvento(evento);
                }
            }

            //Llamamos al plugin de postproceso
            postProcesoNuevoRegistro(registroEntrada, entidad.getId());

            return registroEntrada;

        } catch (I18NException i18n) {
            log.info("Error registrando la entrada");
            i18n.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18n;

        } catch (I18NValidationException i18nv) {
            log.info("Error de validación registrando la entrada");
            i18nv.printStackTrace();
            ejbContext.setRollbackOnly();
            throw i18nv;

        }

    }

    @Override
    public RegistroEntrada actualizar(RegistroEntrada antiguo, RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException {

        registroEntrada = merge(registroEntrada);

        // Obtenemos el próximo evento del Registro
        if(multiEntidadEjb.isMultiEntidad()) {
            Long evento = proximoEventoEntradaMultiEntidad(registroEntrada, entidad, registroEntrada.getOficina().getId());
            registroEntrada.setEvento(evento);
        }else{
            Long evento = proximoEventoEntrada(registroEntrada, entidad, registroEntrada.getOficina().getId());
            registroEntrada.setEvento(evento);
        }

        // Creamos el Historico RegistroEntrada
        historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(antiguo, usuarioEntidad, I18NLogicUtils.tradueix(LocaleContextHolder.getLocale(), "registro.modificacion.datos"), true);
        postProcesoActualizarRegistro(registroEntrada, usuarioEntidad.getEntidad().getId());

        return registroEntrada;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws I18NException {

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
        q.setHint("org.hibernate.readOnly", true);

        if (organismos.size() > 0) {
            q.setParameter("organismos", organismos);
        }

        return q.getResultList().size() > 0;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isOficioRemisionExterno(Long idRegistro) throws I18NException {

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
    public List<OficinaTF> isOficioRemisionSir(Long idRegistro, Long idEntidad) throws I18NException {

        Query q;
        q = em.createQuery("Select re.destinoExternoCodigo from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null and re.estado = :valido");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setHint("org.hibernate.readOnly", true);

        List<String> result = q.getResultList();

        if (result.size() > 0) {

            String codigoDir3 = result.get(0);
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

            return oficinasService.obtenerOficinasSIRUnidad(codigoDir3);
        }

        return null;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public List<OficinaTF> isOficioRemisionSirMultiEntidad(Long idRegistro, Long idEntidad) throws I18NException {

        //Miramos los externos
        Query q;
        q = em.createQuery("Select re.destinoExternoCodigo from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null and re.estado = :valido");

        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setHint("org.hibernate.readOnly", true);

        List<String> result = q.getResultList();

        if(result.size() == 0){ //Si no hay externo miramos destino
            q = em.createQuery("select re.destino.codigo from RegistroEntrada as re where re.id =:idRegistro and re.destino.codigo in(select entidad.codigoDir3 from Entidad as entidad)");

            // Parámetros
            q.setParameter("idRegistro", idRegistro);
            q.setHint("org.hibernate.readOnly", true);

            result = q.getResultList();
        }

        if(result.size()>0){ // Si hay buscamos las oficinas SIR
            String codigoDir3 = result.get(0);
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(idEntidad), PropiedadGlobalUtil.getDir3CaibUsername(idEntidad), PropiedadGlobalUtil.getDir3CaibPassword(idEntidad));

            return oficinasService.obtenerOficinasSIRUnidad(codigoDir3);
        }else{
            return null;
        }
    }


    @Override
    public String obtenerDestinoExternoRE(Long idRegistro) throws I18NException {

        Query q;
        q = em.createQuery("Select re.destinoExternoCodigo from RegistroEntrada as re where " +
                "re.id = :idRegistro and re.destino is null ");


        // Parámetros
        q.setParameter("idRegistro", idRegistro);
        q.setHint("org.hibernate.readOnly", true);

        if (q.getResultList().size() > 0) {
            return (String) q.getResultList().get(0);
        } else {
            return null;
        }

    }


    @Override
    public Long proximoEventoEntrada(RegistroEntrada registroEntrada, Entidad entidadActiva, Long idOficina) throws I18NException {

        if (registroEntrada.getDestino() == null) { // Externo

            // Si la entidad está en SIR y la Oficina está activada para Envío Sir
            if (entidadActiva.getSir() && oficinaEjb.isSIREnvio(idOficina)) {
                List<OficinaTF> oficinasSIR = isOficioRemisionSir(registroEntrada.getId(), entidadActiva.getId());

                if (oficinasSIR != null && !oficinasSIR.isEmpty()) {
                    return RegwebConstantes.EVENTO_OFICIO_SIR;
                }
            }

            return RegwebConstantes.EVENTO_OFICIO_EXTERNO;

        }

        return RegwebConstantes.EVENTO_DISTRIBUIR;
    }


    @Override
    public Long proximoEventoEntradaMultiEntidad(RegistroEntrada registroEntrada, Entidad entidadActiva, Long idOficina) throws I18NException {

        //Si el destino no es null debemos obtener el organismo correcto en un entorno multientidad para poder comprobar
        // en el siguiente if si hay una entidad que le da soporte
        Organismo organismo = null;
        if(registroEntrada.getDestino()!=null) {
            organismo = organismoEjb.findByCodigoMultiEntidad(registroEntrada.getDestino().getCodigo());

        }

        if( registroEntrada.getDestino() == null || (organismo!=null &&!organismo.getEntidad().getId().equals(entidadActiva.getId()))){ //Externo o multientidad

            // Si la entidad está en SIR y la Oficina está activada para Envío Sir
            if (entidadActiva.getSir() && oficinaEjb.isSIREnvio(idOficina)) {
                List<OficinaTF> oficinasSIR = isOficioRemisionSirMultiEntidad(registroEntrada.getId(), entidadActiva.getId());

                if (oficinasSIR != null && !oficinasSIR.isEmpty()) {
                    return RegwebConstantes.EVENTO_OFICIO_SIR;
                }
            }

            return RegwebConstantes.EVENTO_OFICIO_EXTERNO;

        }else{
            return RegwebConstantes.EVENTO_DISTRIBUIR;
        }
    }

    @Override
    public void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws I18NException {

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
    public void cambiarEstadoAnuladoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws I18NException {

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
                                      String observacionesAnulacion) throws I18NException {

        // Estado anulado
        cambiarEstadoAnuladoHistorico(registroEntrada, RegwebConstantes.REGISTRO_ANULADO, usuarioEntidad, observacionesAnulacion);

    }

    @Override
    public void activarRegistroEntrada(RegistroEntrada registroEntrada,
                                       UsuarioEntidad usuarioEntidad) throws I18NException {

        // Actualizamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_PENDIENTE_VISAR, usuarioEntidad);

    }

    @Override
    public void visarRegistroEntrada(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException {

        // Modificamos el estado del RegistroEntrada
        cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_VALIDO, usuarioEntidad);

        // Asignamos su evento
        if (registroEntrada.getEvento() != null) {
            if(multiEntidadEjb.isMultiEntidad()) {
                Long evento = proximoEventoEntradaMultiEntidad(registroEntrada, entidad, registroEntrada.getOficina().getId());
                registroEntrada.setEvento(evento);
            }else{
                Long evento = proximoEventoEntrada(registroEntrada, entidad, registroEntrada.getOficina().getId());
                registroEntrada.setEvento(evento);
            }
            merge(registroEntrada);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void marcarDistribuido(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String descripcion) throws I18NException {

        // CREAMOS LA TRAZABILIDAD
        Trazabilidad trazabilidad = new Trazabilidad();
        trazabilidad.setOficioRemision(null);
        trazabilidad.setFecha(new Date());
        trazabilidad.setTipo(RegwebConstantes.TRAZABILIDAD_DISTRIBUCION);
        trazabilidad.setRegistroEntradaOrigen(registroEntrada);
        trazabilidad.setRegistroSalida(null);
        trazabilidad.setRegistroEntradaDestino(null);

        em.persist(trazabilidad);

        // Creamos el HistoricoRegistroEntrada para la distribución
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_DISTRIBUIDO);
        HistoricoRegistroEntrada historico = new HistoricoRegistroEntrada();
        historico.setEstado(registroEntrada.getEstado());
        historico.setRegistroEntrada(registroEntrada);
        historico.setFecha(new Date());
        historico.setModificacion(descripcion);
        historico.setUsuario(usuarioEntidad);
        em.persist(historico);

        Query q = em.createQuery("update RegistroEntrada set estado=:idEstado where id = :idRegistro");
        q.setParameter("idEstado", RegwebConstantes.REGISTRO_DISTRIBUIDO);
        q.setParameter("idRegistro", registroEntrada.getId());
        q.executeUpdate();
    }


    @Override
    public Integer eliminarByEntidad(Long idEntidad) throws I18NException {

        List<?> registros = em.createQuery("Select distinct(re.id) from RegistroEntrada as re where re.entidad.id = :idEntidad").setParameter("idEntidad", idEntidad).getResultList();

        for (Object id : registros) {
            remove(findById((Long) id));
        }
        em.flush();

        return registros.size();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public RegistroEntrada rectificar(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException {

        RegistroEntrada rectificado = null;
        Long idRegistro = registroEntrada.getId();

        try {
            List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroEntrada.getRegistroDetalle().getAnexosFull();

            try{
                // Detach de la sesion para poder duplicar el registro
                Session session = (Session) em.getDelegate();
                session.evict(registroEntrada);
                session.evict(registroEntrada.getRegistroDetalle());
                session.evict(registroEntrada.getRegistroDetalle().getInteresados());
            }catch (IllegalArgumentException iae){
                // No hacemos nada: https://stackoverflow.com/questions/58016010/updating-to-hibernate-5-1-from-3-6-produce-non-entity-object-instance-passed-to
            }

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
            registroEntrada.getRegistroDetalle().setAnexos(new ArrayList<Anexo>());

            registroEntrada.getRegistroDetalle().setObservaciones("Rectificación del registro " + registroEntrada.getNumeroRegistroFormateado());

            //Gestión Organo destino extinguido
            if (registroEntrada.getDestino() != null) { // Destino interno
                //Si está extinguido, obtenemos sus sustitutos y asignamos el primero.
                if (!registroEntrada.getDestino().getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                    Set<Organismo> historicosFinales = new HashSet<Organismo>();
                    organismoEjb.obtenerHistoricosFinales(registroEntrada.getDestino().getId(), historicosFinales);
                    if (historicosFinales.size() > 0) {
                        registroEntrada.setDestino(historicosFinales.iterator().next());
                    } else {
                        log.info("No hay sustitutos, se calculará mal el próximo evento");
                    }
                }
            } else { //destino externo
                //UnidadTF destinoExterno = obtenerDestinoExternoRE(idRegistro);
                UnidadTF destinoExterno = organismoEjb.obtenerDestinoExterno(registroEntrada.getDestinoExternoCodigo(), entidad.getId());
                //Si está extinguido
                if (!destinoExterno.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                    //Si es SIR, obtenemos sus sustitutos y asignamos el primero.
                    if (registroEntrada.getEvento().equals(RegwebConstantes.EVENTO_OFICIO_SIR)) {
                        List<UnidadTF> destinosExternosSIR = organismoEjb.obtenerSustitutosExternosSIR(destinoExterno.getCodigo(), entidad.getId());
                        if (destinosExternosSIR.size() > 0) {
                            registroEntrada.setDestinoExternoCodigo(destinosExternosSIR.get(0).getCodigo());
                            registroEntrada.setDestinoExternoDenominacion(destinosExternosSIR.get(0).getDenominacion());
                        } else {
                            log.info("No hay sustitutos SIR, se calculará mal el próximo evento");
                        }

                    } else { //Si no es SIR, obtenemos sus sustitutos y asignamos el primero.
                        List<UnidadTF> destinosExternos = organismoEjb.obtenerSustitutosExternos(destinoExterno.getCodigo(), entidad.getId());
                        if (destinosExternos.size() > 0) {
                            registroEntrada.setDestinoExternoCodigo(destinosExternos.get(0).getCodigo());
                            registroEntrada.setDestinoExternoDenominacion(destinosExternos.get(0).getDenominacion());
                        } else {
                            log.info("No hay sustitutos externos, se calculará mal el próximo evento");
                        }
                    }
                }
            }

            // Registramos el nuevo registro
            rectificado = registrarEntrada(registroEntrada, entidad, usuarioEntidad, interesados, anexos, false);

            // Moficiamos el estado al registro original
            cambiarEstado(idRegistro, RegwebConstantes.REGISTRO_RECTIFICADO);

            // Creamos la Trazabilidad de la rectificación
            Trazabilidad trazabilidad = new Trazabilidad(RegwebConstantes.TRAZABILIDAD_RECTIFICACION_ENTRADA);
            trazabilidad.setRegistroEntradaOrigen(getReference(idRegistro));
            trazabilidad.setRegistroEntradaDestino(registroEntrada);
            trazabilidad.setRegistroSir(null);
            trazabilidad.setOficioRemision(null);
            trazabilidad.setRegistroSalida(null);
            trazabilidad.setFecha(new Date());

            trazabilidadEjb.persist(trazabilidad);

        } catch (I18NException | I18NValidationException e) {
            log.info("Ha ocurrido un error rectificando el registro");
            e.printStackTrace();
        }

        return rectificado;
    }


    @Override
    public void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws I18NException {

        Query q = em.createQuery("update RegistroEntrada set destino = :idOrganismoSustituto where destino = :idOrganismoExtinguido and (estado = :valido or estado = :pendienteVisar)");
        q.setParameter("idOrganismoSustituto", idOrganismoSustituto);
        q.setParameter("idOrganismoExtinguido", idOrganismoExtinguido);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("pendienteVisar", RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

        q.executeUpdate();

    }

    @Override
    public void actualizarDestinoExternoExtinguido(Long idRegistro, String destinoExternoCodigo, String destinoExternoDenominacion) throws I18NException {

        Query q = em.createQuery("update RegistroEntrada set destinoExternoCodigo = :destinoExternoCodigo, destinoExternoDenominacion = :destinoExternoDenominacion where id = :idRegistro");
        q.setParameter("idRegistro", idRegistro);
        q.setParameter("destinoExternoCodigo", destinoExternoCodigo);
        q.setParameter("destinoExternoDenominacion", destinoExternoDenominacion);

        q.executeUpdate();

    }

    @Override
    public RegistroEntrada getConAnexosFull(Long id) throws I18NException {

        RegistroEntrada re = findByIdCompleto(id);

        return cargarAnexosFull(re, true);
    }

    @Override
    public RegistroEntrada getConAnexosFullLigero(Long id) throws I18NException {

        RegistroEntrada re = em.find(RegistroEntrada.class, id);
        Long idEntidad = re.getEntidad().getId();

        List<Anexo> anexos = re.getRegistroDetalle().getAnexos();
        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            anexosFull.add(anexoEjb.getAnexoFullLigero(anexo.getId(),idEntidad));
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        re.getRegistroDetalle().setAnexosFull(anexosFull);
        return re;
    }

    @Override
    public RegistroEntrada getConAnexosFullDistribuir(Long id) throws I18NException {

        RegistroEntrada re = findByIdCompleto(id);

        return cargarAnexosFull(re, false);
    }

    /**
     * Carga los Anexos Completos al RegistroEntrada pasado por parámetro
     *
     * @param registroEntrada
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    private RegistroEntrada cargarAnexosFull(RegistroEntrada registroEntrada, Boolean justificante) throws I18NException {

        Long idEntidad = registroEntrada.getEntidad().getId();
        List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();

        List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();
        for (Anexo anexo : anexos) {
            if(!anexo.isJustificante()){ // si no es Justificante, cargamos el AnexoFull
                anexosFull.add(anexoEjb.getAnexoFull(anexo.getId(), idEntidad));
            }else if(justificante){
                anexosFull.add(anexoEjb.getAnexoFull(anexo.getId(), idEntidad));
            }else {
                anexosFull.add(new AnexoFull(anexo));
            }
        }
        //Asignamos los documentos recuperados de custodia al registro de entrada.
        registroEntrada.getRegistroDetalle().setAnexosFull(anexosFull);
        return registroEntrada;
    }

    @Override
    public void postProcesoActualizarRegistro(RegistroEntrada re, Long entidadId) throws I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if (postProcesoPlugin != null) {
            postProcesoPlugin.actualizarRegistroEntrada(re);
        }

    }

    @Override
    public void postProcesoNuevoRegistro(RegistroEntrada re, Long entidadId) throws I18NException {
        IPostProcesoPlugin postProcesoPlugin = (IPostProcesoPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_POSTPROCESO);
        if (postProcesoPlugin != null) {
            postProcesoPlugin.nuevoRegistroEntrada(re);
        }
    }
}
