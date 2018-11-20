package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.Oficio;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Session;
import org.jboss.ejb3.annotation.SecurityDomain;

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
 * @author anadal (Convertir en EJB)
 * Date: 16/01/14
 */
@Stateless(name = "OficioRemisionSalidaUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionsSalidaUtilsBean implements OficioRemisionSalidaUtilsLocal {

    public final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    private RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(name = "OficinaEJB")
    private OficinaLocal oficinaEjb;

    @EJB(name = "CatEstadoEntidadEJB")
    private CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;

    @Override
    @SuppressWarnings(value = "unchecked")
    public Long oficiosSalidaPendientesRemisionCount(Long idOficina, List<Libro> libros, Set<String> organismos, Long entidadActiva) throws Exception {

        Long total = 0L;

        // Registros de salida que son Oficios de Remision
        List<Long> registros = registrosSalidaPendientesRemision(idOficina, libros, organismos, null);

        if(registros.size() > 0){

            // Obtenemos los destinatarios de tipo de Adminitración de un conjunto de Registros
            List<Object[]> destinos = destinatariosAdministracion(registros, false);

            for (Object[] object : destinos) {
                Organismo organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero((String) object[0], entidadActiva);

                if(organismo != null){ // Interno

                    // Solo los internos vigentes
                    if(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){
                        total = total + 1;
                    }
                }else{ // Externo

                    total = total + 1;
                }
            }
        }

        return total;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public LinkedHashSet<Organismo> organismosSalidaPendientesRemision(Long idOficina, List<Libro> libros, Set<String> organismos, Long entidadActiva, Integer total) throws Exception {

        LinkedHashSet<Organismo> organismosDestino =  new LinkedHashSet<Organismo>(); // LinkedHashSet No permite duplicados

        // Registros de salida que son Oficios de Remision
        List<Long> registros = registrosSalidaPendientesRemision(idOficina, libros, organismos, total);

        if(registros.size() > 0){

            // Obtenemos los destinatarios de tipo de Adminitración de un conjunto de Registros
            List<Object[]> destinos = destinatariosAdministracion(registros, true);

            // Solo incluimos los organismos vigentes
            for (Object[] object : destinos) {
                Organismo organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero((String) object[0], entidadActiva);

                if(organismo != null){ // Interno

                    // Solo los internos vigentes
                    if(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){
                        organismosDestino.add(new Organismo(null, (String) object[0], (String) object[1]));
                    }
                }else{ // Externo

                    organismosDestino.add(new Organismo(null, (String) object[0], (String) object[1]));
                }
            }
        }

        return organismosDestino;
    }

    /**
     * Obtenemos los destinatarios de tipo de Adminitración de un conjunto de Registros
     * @param registros
     * @param distinct
     * @return
     * @throws Exception
     */
    @SuppressWarnings(value = "unchecked")
    private List<Object[]> destinatariosAdministracion(List<Long> registros, Boolean distinct) throws Exception{

        String query = "";
        if(distinct){
            query = "Select distinct(i.codigoDir3), i.razonSocial from Interesado as i where i.tipo = :administracion and " +
                    "  i.registroDetalle.id in (:registros)";
        }else{
            query = "Select i.codigoDir3, i.razonSocial from Interesado as i where i.tipo = :administracion and " +
                    "  i.registroDetalle.id in (:registros)";
        }

        List<Object[]> destinos = new ArrayList<Object[]>();

        // Si hay más de 1000 registros, dividimos las queries (ORA-01795).
        while (registros.size() > RegwebConstantes.NUMBER_EXPRESSIONS_IN) {
            List<?> subList = registros.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN);

            Query q2 = em.createQuery(query);

            // Parámetros
            q2.setParameter("registros", subList);
            q2.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);

            destinos.addAll(q2.getResultList());
            registros.subList(0, RegwebConstantes.NUMBER_EXPRESSIONS_IN).clear();
        }

        // Obtenemos los destinos de los Interesados de los Registros Salida anteriores
        Query q2 = em.createQuery(query);

        // Parámetros
        q2.setParameter("registros", registros);
        q2.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);

        destinos.addAll(q2.getResultList());

        return destinos;
    }

    /**
     * Obtiene los Registros de Salida que son Oficio de remisión
     * @param idOficina
     * @param libros
     * @param organismos
     * @return
     * @throws Exception
     */
    @SuppressWarnings(value = "unchecked")
    private List<Long> registrosSalidaPendientesRemision(Long idOficina, List<Libro> libros,Set<String> organismos, Integer total) throws Exception{

        String queryFecha="";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if(StringUtils.isNotEmpty(fecha)){
            queryFecha = " rs.fecha >= :fecha and ";
        }

        // Obtenemos los Registros de Salida que son Oficio de remisión
        Query q = em.createQuery("Select distinct(rs.registroDetalle.id) from RegistroSalida as rs where " +
                "rs.estado = :valido and rs.oficina.id = :idOficina and rs.libro in (:libros) and " + queryFecha +
                " rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and codigoDir3 not in (:organismos)) ");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("libros", libros);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("organismos", organismos);
        if(StringUtils.isNotEmpty(fecha)){
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        if(total != null){
            q.setMaxResults(total);
        }

        return q.getResultList(); // Registros de salida que son Oficios de Remision

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionOrganismo oficiosSalidaPendientesRemision(Integer pageNumber, Integer any, Oficina oficinaActiva, Long idLibro, String codigoOrganismo, Entidad entidadActiva) throws Exception {

        OficiosRemisionOrganismo oficios = new OficiosRemisionOrganismo();

        Oficio oficio = oficioRemisionEjb.obtenerTipoOficio(codigoOrganismo, entidadActiva.getId());

        if(oficio.getInterno()){ // Destinatario organismo interno

            Organismo organismo = organismoEjb.findByCodigoEntidadSinEstadoLigero(codigoOrganismo, entidadActiva.getId());
            oficios.setOrganismo(organismo);
            oficios.setVigente(organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setOficinas(oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

        }else if (oficio.getExterno() || oficio.getEdpExterno()){ // Destinatario organismo externo

            oficios.setExterno(true);

            // Obtenemos el Organismo externo de Dir3Caib
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            UnidadTF unidadTF = unidadesService.obtenerUnidad(codigoOrganismo,null,null);

            if(unidadTF != null){
                Organismo organismoExterno = new Organismo(null,codigoOrganismo,unidadTF.getDenominacion());
                organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                oficios.setVigente(true);
                oficios.setOrganismo(organismoExterno);

                // Comprueba si la Entidad Actual está en SIR
                if (entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {
                    // Averiguamos si el Organismo Externo está en Sir o no
                    Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
                    List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(organismoExterno.getCodigo());
                    if (oficinasSIR.size() > 0) {
                        oficios.setSir(true);
                        oficios.setOficinasSIR(oficinasSIR);
                        log.info("El organismo externo " + organismoExterno + " TIENE oficinas Sir: " + oficinasSIR.size());
                    } else {
                        log.info("El organismo externo " + organismoExterno + " no tiene oficinas Sir");
                    }

                }else{
                    oficios.setSir(false);
                    oficios.setOficinasSIR(null);
                    log.info("Nuestra entidad no está en SIR, se creará un oficio de remision tradicional");
                }


            }else{
                log.info("Organismo externo extinguido");
                oficios.setVigente(false);
                oficios.setOrganismo(new Organismo(null,codigoOrganismo,null));
            }
        }

        //Buscamos los Registros de Salida, pendientes de tramitar mediante un Oficio de Remision
        oficios.setPaginacion(oficiosSalidaByOrganismo(pageNumber, codigoOrganismo, any, oficinaActiva.getId(), idLibro));

        return oficios;

    }

    @SuppressWarnings(value = "unchecked")
    private Paginacion oficiosSalidaByOrganismo(Integer pageNumber, String codigoOrganismo, Integer any, Long idOficina, Long idLibro) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(rs.fecha) = :any and ";
        }

        String queryFecha="";
        String fecha = PropiedadGlobalUtil.getFechaOficiosSalida();

        if(StringUtils.isNotEmpty(fecha)){
            queryFecha = " rs.fecha >= :fecha and ";
        }

        Query q;
        Query q2;

        StringBuilder query = new StringBuilder("Select rs.id, rs.numeroRegistroFormateado, rs.fecha, rs.oficina, rs.origen, rs.registroDetalle.extracto from RegistroSalida as rs where " + anyWhere +
                "rs.libro.id = :idLibro and rs.oficina.id = :idOficina and rs.estado = :valido and " + queryFecha +
                " rs.registroDetalle.id in (select i.registroDetalle.id from Interesado as i where i.registroDetalle.id = rs.registroDetalle.id and i.tipo = :administracion and i.codigoDir3 = :codigoOrganismo) ");

        q2 = em.createQuery(query.toString().replaceAll("Select rs.id, rs.numeroRegistroFormateado, rs.fecha, rs.oficina, rs.origen, rs.registroDetalle.extracto", "Select count(rs.id)"));
        query.append(" order by rs.fecha desc ");
        q = em.createQuery(query.toString());


        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("idLibro", idLibro);
        q.setParameter("idOficina", idOficina);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q.setParameter("codigoOrganismo", codigoOrganismo);
        if(StringUtils.isNotEmpty(fecha)){
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q.setParameter("fecha", sdf.parse(fecha));
        }

        q2.setParameter("idLibro", idLibro);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q2.setParameter("administracion", RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION);
        q2.setParameter("codigoOrganismo", codigoOrganismo);
        if(StringUtils.isNotEmpty(fecha)){
            SimpleDateFormat sdf = new SimpleDateFormat(RegwebConstantes.FORMATO_FECHA);
            q2.setParameter("fecha", sdf.parse(fecha));
        }

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber);
            int inicio = (pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION;
            q.setFirstResult(inicio);
            q.setMaxResults(10);
        } else {
            paginacion = new Paginacion(0, 0);
        }

        List<Object[]> result = q.getResultList();
        List<RegistroSalida> registros = new ArrayList<RegistroSalida>();

        for (Object[] object : result) {
            RegistroSalida rs = new RegistroSalida();
            rs.setId((Long)  object[0]);
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
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws Exception
     */
    @Override
    public OficioRemision crearOficioRemisionInterno(List<RegistroSalida> registrosSalida,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro)
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

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_INTERNO);
        }

        return oficioRemision;

    }

    /**
     * Crea un OficioRemision con todos los ResgistroSalida seleccionados
     *
     * @param registrosSalida Listado de RegistrosSalida que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param organismoExterno
     * @param idLibro
     * @return
     * @throws Exception
     */

    public OficioRemision crearOficioRemisionExterno(List<RegistroSalida> registrosSalida,
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

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_EXTERNO);
        }

        return oficioRemision;

    }

    @Override
    public List<RegistroSalida> crearJustificantesRegistros(List<RegistroSalida> registros, UsuarioEntidad usuario) throws Exception, I18NException, I18NValidationException {

        List<RegistroSalida> correctos = new ArrayList<RegistroSalida>();

        for (RegistroSalida registro : registros) {

            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(registro.getId());

            //Justificante, Si no tiene generado el Justificante, lo hacemos
            if (!registroSalida.getRegistroDetalle().getTieneJustificante()) {

                try{
                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuario, registroSalida, RegwebConstantes.REGISTRO_SALIDA_ESCRITO.toLowerCase(), Configuracio.getDefaultLanguage());
                    registroSalida.getRegistroDetalle().getAnexosFull().add(anexoFull);
                    // Añadimos el Correcto
                    correctos.add(registro);
                }catch (I18NException e){
                    log.info("Error generando justificante: " + e.getMessage());
                    e.printStackTrace();
                }

            }else{
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
    public List<RegistroEntrada> aceptarOficioRemision(OficioRemision oficioRemision,
                                                        UsuarioEntidad usuario, Oficina oficinaActiva,
                                                        List<OficioPendienteLlegada> oficios) throws Exception, I18NException, I18NValidationException {

        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

        // Recorremos los RegistroSalida del Oficio
        for (OficioPendienteLlegada oficio : oficios) {

            // Creamos un Nuevo RegistroEntrada
            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficio.getIdRegistro());
            List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroSalida.getRegistroDetalle().getAnexosFull();
            Libro libro = libroEjb.findById(oficio.getIdLibro());

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
            registroDetalle.setAnexos(null);
            registroDetalle.setInteresados(null);

            for (AnexoFull anexo : anexos) {
                anexo.getAnexo().setId(null);
                anexo.getAnexo().setJustificante(false);
            }

            nuevoRE.setRegistroDetalle(registroDetalle);

            // Registramos el nuevo RegistroEntrada
            synchronized (this) {
                nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE, usuario, interesados, anexos);
            }

            registros.add(nuevoRE);

            // ACTUALIZAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroSalida(oficioRemision.getId(), registroSalida.getId());
            trazabilidad.setRegistroEntradaDestino(nuevoRE);
            trazabilidadEjb.merge(trazabilidad);

            // Marcamos el RegistroSalida original como ACEPTADO
            registroSalidaEjb.cambiarEstadoHistorico(registroSalida,RegwebConstantes.REGISTRO_OFICIO_ACEPTADO, usuario);

        }

        oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
        oficioRemision.setFechaEstado(new Date());

        // Actualizamos el oficio de remisión
        oficioRemisionEjb.merge(oficioRemision);

        return registros;

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

}
