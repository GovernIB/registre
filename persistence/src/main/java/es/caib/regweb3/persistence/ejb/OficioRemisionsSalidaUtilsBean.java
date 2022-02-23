package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (Convertir en EJB)
 * Date: 16/01/14
 */
@Stateless(name = "OficioRemisionSalidaUtilsEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class OficioRemisionsSalidaUtilsBean implements OficioRemisionSalidaUtilsLocal {

    public final Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private JustificanteLocal justificanteEjb;


    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Organismo> organismosSalidaPendientesRemisionTipo(Long idOficina, Long tipoEvento, Integer total) throws Exception {

        String queryFecha = "";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if (StringUtils.isNotEmpty(fecha)) {
            queryFecha = " and rs.fecha >= :fecha";
        }

        // Obtenemos los Registros de Salida que son Oficio de remisión
        Query q = em.createQuery("Select distinct(rs.registroDetalle.id) from RegistroSalida as rs where " +
                "rs.estado = :valido and rs.oficina.id = :idOficina and rs.evento = :tipoEvento" + queryFecha + " order by rs.registroDetalle.id desc");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("tipoEvento", tipoEvento);
        if (StringUtils.isNotEmpty(fecha)) {
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        if (total != null) {
            q.setMaxResults(total);
        } else {
            q.setMaxResults(999);
        }
        q.setHint("org.hibernate.readOnly", true);

        List<Long> registros = q.getResultList(); // Registros de salida que son Oficios de Remision

        LinkedHashSet<Organismo> organismosDestino = new LinkedHashSet<Organismo>();

        if (registros.size() > 0) {
            // Obtenemos los destinatarios de tipo administración de los registros anteriores
            Query q1 = em.createQuery("Select distinct(i.codigoDir3), i.razonSocial from Interesado as i where i.tipo = :administracion and " +
                    "  i.registroDetalle.id in (:registros)");

            // Parámetros
            q1.setParameter("registros", registros);
            q1.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
            q.setHint("org.hibernate.readOnly", true);

            List<Object[]> destinos = q1.getResultList();

            for (Object[] object : destinos) {
                organismosDestino.add(new Organismo(null, (String) object[0], (String) object[1]));
            }
        }

        return organismosDestino;
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public Long oficiosSalidaPendientesRemisionCount(Long idOficina, Long tipoEvento) throws Exception {

        String queryFecha = "";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if (StringUtils.isNotEmpty(fecha)) {
            queryFecha = " and rs.fecha >= :fecha";
        }

        // Obtenemos los Registros de Salida que son Oficio de remisión
        Query q = em.createQuery("Select count(rs.registroDetalle.id) from RegistroSalida as rs where " +
                "rs.estado = :valido and rs.oficina.id = :idOficina and rs.evento = :tipoEvento" + queryFecha + " order by rs.registroDetalle.id desc");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("tipoEvento", tipoEvento);

        if (StringUtils.isNotEmpty(fecha)) {
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionOrganismo oficiosSalidaPendientesRemision(Integer pageNumber, Integer any, Oficina oficinaActiva, Long idOrganismo, String codigoOrganismo, Entidad entidadActiva, Long tipoEvento) throws Exception {

        OficiosRemisionOrganismo oficios = new OficiosRemisionOrganismo();

        if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_INTERNO)) {

            Organismo organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero(codigoOrganismo, entidadActiva.getId());
            oficios.setOrganismo(organismo);
            oficios.setVigente(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setOficinas(oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

            if (!oficios.getVigente()) { // Organismo extinguido, obtenemos los organismos sustitutos
                log.info("Organismo interno extinguido, buscamos sustitutos");
                Set<Organismo> historicosFinales = new HashSet<Organismo>();
                Set<Organismo> sustitutos = new HashSet<Organismo>();
                //Obtenemos los organismos vigentes que lo sustituyen que se devolverán en la variable historicosFinales;
                organismoEjb.obtenerHistoricosFinales(organismo.getId(), historicosFinales);
                for (Organismo historico : historicosFinales) {
                    //Solo devolvemos aquellos sustitutos que tienen oficinas que le dan servicio
                    if (oficinaEjb.tieneOficinasServicio(historico.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO)) {
                        sustitutos.add(historico);
                        oficios.setOficinas(true);
                    }
                }

                oficios.setSustitutos(new ArrayList<Organismo>(sustitutos));
            }

        } else if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_EXTERNO) || tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_SIR)) {

            oficios.setExterno(true);

            // Obtenemos el Organismo externo de Dir3Caib
            UnidadTF unidadTF = organismoEjb.obtenerDestinoExterno(codigoOrganismo, entidadActiva.getId());


            if (unidadTF != null) {
                Organismo organismoExterno = new Organismo(null, codigoOrganismo, unidadTF.getDenominacion());
                oficios.setOrganismo(organismoExterno);

                if (unidadTF.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                    log.info("Organismo externo vigente");
                    organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                    oficios.setVigente(true);

                } else {// Organismo externo extinguido, obtenemos los organismos sustitutos

                    log.info("Organismo externo extinguido, buscamos sustitutos");
                    organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO));
                    oficios.setVigente(false);

                    //Obtenemos los sustitutos externos
                    List<UnidadTF> sustitutosExternos = organismoEjb.obtenerSustitutosExternos(organismoExterno.getCodigo(), entidadActiva.getId());

                    //Transformamos a organismo de regweb3
                    List<Organismo> sustitutos = new ArrayList<Organismo>();
                    for (UnidadTF sustituto : sustitutosExternos) {
                        sustitutos.add(new Organismo(null, sustituto.getCodigo(), sustituto.getDenominacion()));
                    }

                    oficios.setSustitutos(sustitutos);
                }

                // Averiguamos si el Organismo Externo está en SIR y tiene Oficinas SIR
                if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_SIR) && entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {

                    //Oficio SIR - Organismo Externo Vigente
                    if (unidadTF.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                        log.info("Organismo externo vigente");
                        organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                        oficios.setVigente(true);

                        //Obtenemos de dir3caib las oficinas SIR del organismo externo
                        Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(entidadActiva.getId()), PropiedadGlobalUtil.getDir3CaibUsername(entidadActiva.getId()), PropiedadGlobalUtil.getDir3CaibPassword(entidadActiva.getId()));
                        List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(organismoExterno.getCodigo());
                        if (oficinasSIR.size() > 0) {
                            oficios.setSir(true);
                            oficios.setOficinasSIR(oficinasSIR);
                            log.info("El organismo externo " + organismoExterno + " TIENE oficinas Sir: " + oficinasSIR.size());
                        } else {
                            oficios.setOficinasSIR(null);
                            log.info("El organismo externo " + organismoExterno + " no tiene oficinas Sir");
                        }

                    } else { // Organismo externo extinguido, obtenemos los organismos sustitutos
                        log.info("Organismo externo extinguido, buscamos sustitutos");
                        organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO));
                        oficios.setVigente(false);

                        // Obtenemos los sustitutos de dir3caib
                        List<UnidadTF> sustitutosExternos = organismoEjb.obtenerSustitutosExternosSIR(organismoExterno.getCodigo(), entidadActiva.getId());

                        //Si solo hay un sustituto, se obtienen sus oficinas SIR y se mandan.
                        if (sustitutosExternos.size() == 1) {
                            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(entidadActiva.getId()), PropiedadGlobalUtil.getDir3CaibUsername(entidadActiva.getId()), PropiedadGlobalUtil.getDir3CaibPassword(entidadActiva.getId()));
                            List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(sustitutosExternos.get(0).getCodigo());
                            oficios.setOficinasSIR(oficinasSIR);
                        }

                        //Transformamos a organismo de regweb3
                        List<Organismo> sustitutos = new ArrayList<Organismo>();
                        for (UnidadTF sustituto : sustitutosExternos) {
                            sustitutos.add(new Organismo(null, sustituto.getCodigo(), sustituto.getDenominacion()));
                        }
                        oficios.setSustitutos(sustitutos);
                    }
                    oficios.setSir(true);

                } else { //Oficio de Remisión Tradicional
                    oficios.setSir(false);
                    oficios.setOficinasSIR(null);
                    log.info("Nuestra entidad no está en SIR, se creará un oficio de remision tradicional");
                }
            }
        }

        //Buscamos los Registros de Salida, pendientes de tramitar mediante un Oficio de Remision
        oficios.setPaginacion(oficiosSalidaByOrganismo(pageNumber, codigoOrganismo, any, oficinaActiva.getId(), tipoEvento));

        return oficios;

    }

    @SuppressWarnings(value = "unchecked")
    private Paginacion oficiosSalidaByOrganismo(Integer pageNumber, String codigoOrganismo, Integer any, Long idOficina, Long tipoEvento) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(rs.fecha) = :any and ";
        }

        String queryFecha = "";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if (StringUtils.isNotEmpty(fecha)) {
            queryFecha = " rs.fecha >= :fecha and ";
        }

        Query q;
        Query q2;

        StringBuilder query = new StringBuilder("Select rs.id, rs.numeroRegistroFormateado, rs.fecha, rs.oficina, rs.origen, rs.registroDetalle.extracto from RegistroSalida as rs where " + anyWhere +
                " rs.oficina.id = :idOficina and rs.estado = :valido and rs.evento = :tipoEvento and " + queryFecha +
                " rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and i.codigoDir3 = :codigoOrganismo) ");

        q2 = em.createQuery(query.toString().replaceAll("Select rs.id, rs.numeroRegistroFormateado, rs.fecha, rs.oficina, rs.origen, rs.registroDetalle.extracto", "Select count(rs.id)"));
        query.append(" order by rs.fecha desc ");
        q = em.createQuery(query.toString());


        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("idOficina", idOficina);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("tipoEvento", tipoEvento);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("codigoOrganismo", codigoOrganismo);
        if (StringUtils.isNotEmpty(fecha)) {
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        q2.setParameter("idOficina", idOficina);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("tipoEvento", tipoEvento);
        q2.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q2.setParameter("codigoOrganismo", codigoOrganismo);
        if (StringUtils.isNotEmpty(fecha)) {
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q2.setParameter("fecha", sdf.parse(fecha));
        }

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(10);
            q.setHint("org.hibernate.readOnly", true);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> result = q.getResultList();
        List<RegistroSalida> registros = new ArrayList<RegistroSalida>();

        for (Object[] object : result) {
            RegistroSalida rs = new RegistroSalida();
            rs.setId((Long) object[0]);
            rs.setNumeroRegistroFormateado((String) object[1]);
            rs.setFecha((Date) object[2]);
            rs.setOficina((Oficina) object[3]);
            rs.setOrigen((Organismo) object[4]);
            rs.setRegistroDetalle(new RegistroDetalle());
            rs.getRegistroDetalle().setExtracto((String) object[5]);

            registros.add(rs);
        }

        paginacion.setListado(registros);

        return paginacion;
    }

    /**
     * Crea un OficioRemision con todos los ResgistroSalida seleccionados
     * Crea la trazabilidad para los RegistroSalida
     *
     * @param registrosSalida Listado de ResgistroSalida que forman parte del Oficio de remisión
     * @param oficinaActiva   Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad  Usuario que realiza el OficioRemision
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws Exception
     */
    @Override
    public OficioRemision crearOficioRemisionInterno(List<RegistroSalida> registrosSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_INTERNO_ENVIADO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setRegistrosSalida(registrosSalida);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setOrganismoDestinatario(new Organismo(idOrganismo));

        oficioRemision = oficioRemisionEjb.registrarOficioRemision(entidad, oficioRemision, RegwebConstantes.REGISTRO_OFICIO_INTERNO);

        return oficioRemision;

    }

    /**
     * Crea un OficioRemision con todos los ResgistroSalida seleccionados
     *
     * @param registrosSalida  Listado de RegistrosSalida que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param organismoExterno
     * @param idLibro
     * @return
     * @throws Exception
     */

    public OficioRemision crearOficioRemisionExterno(List<RegistroSalida> registrosSalida, Entidad entidad,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExterno,
                                                     String organismoExternoDenominacion, Long idLibro)
            throws Exception, I18NException, I18NValidationException {


        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_EXTERNO_ENVIADO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setRegistrosSalida(registrosSalida);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setDestinoExternoCodigo(organismoExterno);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
        oficioRemision.setOrganismoDestinatario(null);

        oficioRemision = oficioRemisionEjb.registrarOficioRemision(entidad, oficioRemision, RegwebConstantes.REGISTRO_OFICIO_EXTERNO);

        return oficioRemision;

    }

    @Override
    public OficioRemision crearOficioRemisionSIR(RegistroSalida registroSalida, Entidad entidad, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, OficinaTF oficinaSirDestino)
            throws Exception, I18NException, I18NValidationException {

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);

        oficioRemision.setLibro(new Libro(registroSalida.getLibro().getId()));
        oficioRemision.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
        oficioRemision.setDestinoExternoCodigo(registroSalida.interesadoDestinoCodigo());
        oficioRemision.setDestinoExternoDenominacion(registroSalida.getInteresadoDestinoDenominacion());
        oficioRemision.setRegistrosSalida(Collections.singletonList(registroSalida));
        oficioRemision.setOrganismoDestinatario(null);
        oficioRemision.setRegistrosEntrada(null);
        oficioRemision.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
        oficioRemision.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
        oficioRemision.setContactosEntidadRegistralDestino(RegistroUtils.getContactosOficinaSir(oficinaSirDestino));
        oficioRemision.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
        oficioRemision.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

        // Registramos el Oficio de Remisión SIR
        oficioRemision = oficioRemisionEjb.registrarOficioRemision(entidad, oficioRemision, RegwebConstantes.REGISTRO_OFICIO_SIR);

        return oficioRemision;
    }

    @Override
    public List<RegistroSalida> crearJustificantesRegistros(Entidad entidad, List<RegistroSalida> registros, UsuarioEntidad usuario) throws Exception, I18NException, I18NValidationException {

        List<RegistroSalida> correctos = new ArrayList<RegistroSalida>();

        for (RegistroSalida registro : registros) {

            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(registro.getId());

            //Justificante, Si no tiene generado el Justificante, lo hacemos
            if (!registroSalida.getRegistroDetalle().getTieneJustificante()) {

                try {
                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(entidad, usuario, registroSalida, RegwebConstantes.REGISTRO_SALIDA, Configuracio.getDefaultLanguage());
                    registroSalida.getRegistroDetalle().getAnexosFull().add(anexoFull);
                    // Añadimos el Correcto
                    correctos.add(registro);
                } catch (I18NException e) {
                    log.info("Error generando justificante: " + e.getMessage());
                    e.printStackTrace();
                }

            } else {
                // Añadimos el Correcto
                correctos.add(registro);
            }

        }
        return correctos;
    }

    /**
     * Procesa un OficioRemision pendiente de llegada, creando tantos Registros de Entrada,
     * como contenga el Oficio.
     *
     * @param oficioRemision
     * @throws Exception
     */
    @Override
    public List<RegistroEntrada> aceptarOficioRemision(OficioRemision oficioRemision, Entidad entidad, UsuarioEntidad usuario, Oficina oficinaActiva,
                                                       List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException {

        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

        // Recorremos los RegistroSalida del Oficio
        for (OficioPendienteLlegada oficio : oficios) {

            // Creamos un Nuevo RegistroEntrada
            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficio.getIdRegistro());
            List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroSalida.getRegistroDetalle().getAnexosFull();
            Libro libro = libroEjb.findById(usuario.getEntidad().getLibro().getId());

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroSalida);
            session.evict(registroSalida.getRegistroDetalle());
            session.evict(registroSalida.getRegistroDetalle().getAnexos());
            session.evict(registroSalida.getRegistroDetalle().getInteresados());

            RegistroEntrada nuevoRE = new RegistroEntrada();
            nuevoRE.setUsuario(usuario);
            nuevoRE.setDestino(organismoEjb.findByIdLigero(oficio.getIdOrganismoDestinatario()));
            nuevoRE.setOficina(oficinaActiva);
            nuevoRE.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            nuevoRE.setLibro(libro);

            // Creamos un nuevo RegistroDetalle, modificando las propiedades Origen
            RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

            // Set Id's a null
            registroDetalle.setId(null);
            registroDetalle.setAnexos(new ArrayList<Anexo>());
            registroDetalle.setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }

            nuevoRE.setRegistroDetalle(registroDetalle);

            // Registramos el nuevo RegistroEntrada
            nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE, entidad, usuario, interesados, anexos, false);

            registros.add(nuevoRE);

            // ACTUALIZAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroSalida(oficioRemision.getId(), registroSalida.getId());
            trazabilidad.setRegistroEntradaDestino(nuevoRE);
            trazabilidadEjb.merge(trazabilidad);

            // Marcamos el RegistroSalida original como ACEPTADO
            registroSalidaEjb.cambiarEstadoHistorico(registroSalida, RegwebConstantes.REGISTRO_OFICIO_ACEPTADO, usuario);

        }

        oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
        oficioRemision.setFechaEstado(new Date());

        // Actualizamos el oficio de remisión
        oficioRemisionEjb.merge(oficioRemision);

        return registros;

    }
}
