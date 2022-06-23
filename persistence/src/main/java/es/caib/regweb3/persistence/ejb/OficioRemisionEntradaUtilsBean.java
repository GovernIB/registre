package es.caib.regweb3.persistence.ejb;


import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.OficioPendienteLlegada;
import es.caib.regweb3.persistence.utils.*;
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
import java.util.*;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (Convertir en EJB)
 * Date: 16/01/14
 */
@Stateless(name = "OficioRemisionEntradaUtilsEJB")
@SecurityDomain("seycon")
public class OficioRemisionEntradaUtilsBean implements OficioRemisionEntradaUtilsLocal {

    public final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private OrganismoLocal organismoEjb;
    @EJB private LibroLocal libroEjb;
    @EJB private TrazabilidadLocal trazabilidadEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private JustificanteLocal justificanteEjb;

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> organismosEntradaPendientesRemisionInternos(Long idOficina, List<Libro> libros, Integer total) throws Exception {

        // Obtenemos los Organismos destinatarios PROPIOS que tiene Oficios de Remision pendientes de tramitar
        Query q;

        q = em.createQuery("Select distinct re.destino.codigo, re.destino.denominacion from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and re.libro in (:libros) and " +
                "re.destino != null and re.evento = :oficio_interno ");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
//        q.setParameter("oficioSir", RegwebConstantes.REGISTRO_OFICIO_SIR);
        q.setParameter("idOficina", idOficina);
        q.setParameter("libros", libros);
        q.setParameter("oficio_interno", RegwebConstantes.EVENTO_OFICIO_INTERNO);
        q.setHint("org.hibernate.readOnly", true);

        if (total != null) {
            q.setMaxResults(total);
        }

        List<Object[]> organismosInternos = q.getResultList();

        List<Organismo> organismosDestino = new ArrayList<Organismo>();
        for (Object[] organismoInterno : organismosInternos) {
            Organismo organismo = new Organismo(null, (String) organismoInterno[0], (String) organismoInterno[1]);

            organismosDestino.add(organismo);
        }

        return organismosDestino;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Organismo> organismosEntradaPendientesRemisionExternosTipo(Long idOficina, Long tipoEvento, Integer total) throws Exception {

        // Obtenemos los Organismos destinatarios EXTERNOS que tiene Oficios de Remision pendientes de tramitar
        Query q;
        q = em.createQuery("Select distinct re.destinoExternoCodigo, re.destinoExternoDenominacion from RegistroEntrada as re where " +
                "re.estado = :valido and re.numeroRegistro is null and re.oficina.id = :idOficina  and re.destino is null and re.evento = :tipoEvento ");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
//        q.setParameter("oficioSir", RegwebConstantes.REGISTRO_OFICIO_SIR);
        q.setParameter("idOficina", idOficina);
        q.setParameter("tipoEvento", tipoEvento);
        q.setHint("org.hibernate.readOnly", true);

        if (total != null) {
            q.setMaxResults(total);
        }

        List<Object[]> organismosExternos = q.getResultList();

        List<Organismo> organismosDestino = new ArrayList<Organismo>();
        for (Object[] organismoExterno : organismosExternos) {
            Organismo organismo = new Organismo(null, (String) organismoExterno[0], StringUtils.isNotEmpty(((String) organismoExterno[1])) ? (String) organismoExterno[1]:(String) organismoExterno[0]);
            organismosDestino.add(organismo);
        }

        return organismosDestino;
    }


    @Override
    public Long oficiosEntradaInternosPendientesRemisionCount(Long idOficina, List<Libro> libros) throws Exception {

        // Total oficios internos
        Query q;
        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and re.libro in (:libros) and " +
                "re.destino != null and re.destino.estado.codigoEstadoEntidad = :vigente and re.evento = :oficio_interno");

        // Parámetros
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("vigente", RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        q.setParameter("idOficina", idOficina);
        q.setParameter("libros", libros);
        q.setParameter("oficio_interno", RegwebConstantes.EVENTO_OFICIO_INTERNO);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }

    @Override
    public Long oficiosEntradaExternosPendientesRemisionCount(Long idOficina) throws Exception {

        // Total oficios externos
        Query q;
        q = em.createQuery("Select count(re.id) from RegistroEntrada as re where " +
                "re.estado = :valido and re.oficina.id = :idOficina and " +
                "re.destino is null and re.evento = :oficio_externo ");

        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
        q.setParameter("idOficina", idOficina);
        q.setParameter("oficio_externo", RegwebConstantes.EVENTO_OFICIO_EXTERNO);
        q.setHint("org.hibernate.readOnly", true);

        return (Long) q.getSingleResult();
    }


    @Override
    @SuppressWarnings(value = "unchecked")
    public OficiosRemisionOrganismo oficiosEntradaPendientesRemision(Long tipoEvento, Integer pageNumber, final Integer resultsPerPage, Integer any, Oficina oficinaActiva, String codigoOrganismo, Entidad entidadActiva) throws Exception {

        OficiosRemisionOrganismo oficios = new OficiosRemisionOrganismo();
        Oficio oficio = oficioRemisionEjb.obtenerTipoOficio(codigoOrganismo, entidadActiva.getId());

        if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_INTERNO)) {

            Organismo destino = organismoEjb.findByCodigoEntidadSinEstadoLigero(codigoOrganismo, entidadActiva.getId());
            oficios.setOrganismo(destino);
            oficios.setVigente(destino.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            oficios.setOficinas(oficinaEjb.tieneOficinasServicio(destino.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO));

            // Organismo extinguido, obtenemos los organismos sustitutos
            if (!oficios.getVigente()) {
                Set<Organismo> historicosFinales = new HashSet<Organismo>();
                Set<Organismo> sustitutos = new HashSet<Organismo>();
                //Obtenemos los organismos vigentes que lo sustituyen que se devolverán en la variable historicosFinales;
                organismoEjb.obtenerHistoricosFinales(destino.getId(), historicosFinales);
                for (Organismo historico : historicosFinales) {
                    //Solo devolvemos aquellos sustitutos que tienen oficinas que le dan servicio
                    if (oficinaEjb.tieneOficinasServicio(historico.getId(), RegwebConstantes.OFICINA_VIRTUAL_NO)) {
                        sustitutos.add(historico);
                        oficios.setOficinas(true);
                    }
                }
                //Asignamos los sustitutos a los oficios
                oficios.setSustitutos(new ArrayList<Organismo>(sustitutos));
            }

            // Buscamos los Registros de Entrada internos, pendientes de tramitar mediante un Oficio de Remision
            oficios.setPaginacion(oficiosRemisionByOrganismoInterno(pageNumber, resultsPerPage, destino.getId(), any, oficinaActiva.getId()));


        } else if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_EXTERNO) || tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_SIR)) {

            oficios.setExterno(true);

            // Obtenemos el Organismo externo de Dir3Caib
            UnidadTF unidadTF = organismoEjb.obtenerDestinoExterno(codigoOrganismo);

            if (unidadTF != null) {

                Organismo organismoExterno = new Organismo(null, codigoOrganismo, unidadTF.getDenominacion());
                oficios.setOrganismo(organismoExterno);

                //Oficio Externo
                if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_EXTERNO)) {
                    //Organismo externo vigente
                    if (unidadTF.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                        log.info("Organismo externo vigente");
                        organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                        oficios.setVigente(true);

                    } else { // Organismo externo extinguido, obtenemos los organismos sustitutos
                        log.info("Organismo externo extinguido, buscamos sustitutos");
                        organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO));
                        oficios.setVigente(false);

                        //Obtenemos los sustitutos de dir3caib del organismo externo indicado
                        List<UnidadTF> sustitutosExternos = organismoEjb.obtenerSustitutosExternos(organismoExterno.getCodigo());

                        //Convertimos los sustitutos a organismos de regweb3
                        List<Organismo> sustitutos = new ArrayList<Organismo>();
                        for (UnidadTF sustituto : sustitutosExternos) {
                            sustitutos.add(new Organismo(null, sustituto.getCodigo(), sustituto.getDenominacion()));
                        }

                        oficios.setSustitutos(sustitutos);

                    }
                }


                // Averiguamos si el Organismo Externo está en SIR y tiene Oficinas SIR
                if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_SIR) && entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {

                    //Oficio SIR - Organismo Externo Vigente
                    if (unidadTF.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                        log.info("Organismo externo SIR vigente");
                        organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                        oficios.setVigente(true);

                        //Obtenemos de dir3caib las oficinas SIR del organismo externo
                        Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
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

                        log.info("Organismo externo SIR extinguido, buscamos sustitutos");
                        organismoExterno.setEstado(catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO));
                        oficios.setVigente(false);

                        // Obtenemos los sustitutos de dir3caib
                        List<UnidadTF> sustitutosExternos = organismoEjb.obtenerSustitutosExternosSIR(organismoExterno.getCodigo());
                        //Si solo hay un sustituto, se obtienen sus oficinas SIR y se mandan.
                        if (sustitutosExternos.size() == 1) {
                            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
                            List<OficinaTF> oficinasSIR = oficinasService.obtenerOficinasSIRUnidad(sustitutosExternos.get(0).getCodigo());
                            oficios.setOficinasSIR(oficinasSIR);
                        }

                        //Transformamos los sustitutos
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
                    log.info("Nuestra entidad no esta en SIR, se creara un oficio de remision tradicional");
                }

                //Buscamos los Registros de Entrada externos, pendientes de tramitar mediante un Oficio de Remision
                if (oficio.getExterno()) {
                    oficios.setPaginacion(oficiosRemisionByOrganismoExterno(pageNumber, resultsPerPage, codigoOrganismo, any, oficinaActiva.getId(), tipoEvento));
                } else if (oficio.getEdpExterno()) {
                    oficios.setPaginacion(oficiosRemisionByOrganismoInterno(pageNumber, resultsPerPage, organismoEjb.findByCodigoLigero(codigoOrganismo).getId(), any, oficinaActiva.getId()));
                }

            }

        }

        return oficios;
    }


    @SuppressWarnings(value = "unchecked")
    private Paginacion oficiosRemisionByOrganismoInterno(Integer pageNumber, final Integer resultsPerPage, Long idOrganismo, Integer any, Long idOficina) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        Query q;
        Query q2;

        StringBuilder query = new StringBuilder("Select re.id, re.numeroRegistroFormateado, re.fecha, re.oficina, re.destino, re.registroDetalle.extracto from RegistroEntrada as re where " + anyWhere +
                " re.oficina.id = :idOficina and re.destino.id = :idOrganismo and re.estado = :valido and re.evento = :eventoInterno ");

        q2 = em.createQuery(query.toString().replaceAll("Select re.id, re.numeroRegistroFormateado, re.fecha, re.oficina, re.destino, re.registroDetalle.extracto", "Select count(re.id)"));
        query.append(" order by re.fecha desc ");
        q = em.createQuery(query.toString());


        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("idOrganismo", idOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
//        q.setParameter("oficioSir", RegwebConstantes.REGISTRO_OFICIO_SIR);
        q.setParameter("eventoInterno", RegwebConstantes.EVENTO_OFICIO_INTERNO);
        q.setHint("org.hibernate.readOnly", true);

        q2.setParameter("idOrganismo", idOrganismo);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
//        q2.setParameter("oficioSir", RegwebConstantes.REGISTRO_OFICIO_SIR);
        q2.setParameter("eventoInterno", RegwebConstantes.EVENTO_OFICIO_INTERNO);
        q2.setHint("org.hibernate.readOnly", true);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();
        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

        for (Object[] object : result) {
            RegistroEntrada re = new RegistroEntrada();
            re.setId((Long) object[0]);
            re.setNumeroRegistroFormateado((String) object[1]);
            re.setFecha((Date) object[2]);
            re.setOficina((Oficina) object[3]);
            re.setDestino((Organismo) object[4]);
            re.setRegistroDetalle(new RegistroDetalle());
            re.getRegistroDetalle().setExtracto((String) object[5]);

            registros.add(re);
        }

        paginacion.setListado(registros);

        return paginacion;
    }


    @SuppressWarnings(value = "unchecked")
    private Paginacion oficiosRemisionByOrganismoExterno(Integer pageNumber, final Integer resultsPerPage, String codigoOrganismo, Integer any, Long idOficina, Long tipoEvento) throws Exception {

        String anyWhere = "";
        if (any != null) {
            anyWhere = "year(re.fecha) = :any and ";
        }

        StringBuilder query = new StringBuilder("Select re.id, re.numeroRegistroFormateado, re.fecha, re.oficina, re.destinoExternoCodigo, re.destinoExternoDenominacion, re.registroDetalle.extracto from RegistroEntrada as re where " + anyWhere +
                " re.oficina.id = :idOficina and re.destino is null and re.destinoExternoCodigo = :codigoOrganismo and re.estado = :valido and re.numeroRegistro is null and re.evento = :tipoEvento");

        Query q;
        Query q2;

        q2 = em.createQuery(query.toString().replaceAll("Select re.id, re.numeroRegistroFormateado, re.fecha, re.oficina, re.destinoExternoCodigo, re.destinoExternoDenominacion, re.registroDetalle.extracto", "Select count(re.id)"));
        query.append(" order by re.fecha desc ");
        q = em.createQuery(query.toString());

        // Parámetros
        if (any != null) {
            q.setParameter("any", any);
            q2.setParameter("any", any);
        }
        q.setParameter("codigoOrganismo", codigoOrganismo);
        q.setParameter("idOficina", idOficina);
        q.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
//        q.setParameter("oficioSir", RegwebConstantes.REGISTRO_OFICIO_SIR);
        q.setHint("org.hibernate.readOnly", true);

        q.setParameter("tipoEvento", tipoEvento);
        q2.setParameter("codigoOrganismo", codigoOrganismo);
        q2.setParameter("idOficina", idOficina);
        q2.setParameter("valido", RegwebConstantes.REGISTRO_VALIDO);
//        q2.setParameter("oficioSir", RegwebConstantes.REGISTRO_OFICIO_SIR);
        q2.setParameter("tipoEvento", tipoEvento);
        q2.setHint("org.hibernate.readOnly", true);

        Paginacion paginacion;

        if (pageNumber != null) { // Comprobamos si es una busqueda paginada o no
            q2.setHint("org.hibernate.readOnly", true);
            Long total = (Long) q2.getSingleResult();
            paginacion = new Paginacion(total.intValue(), pageNumber, resultsPerPage);
            int inicio = (pageNumber - 1) * resultsPerPage;
            q.setHint("org.hibernate.readOnly", true);
            q.setFirstResult(inicio);
            q.setMaxResults(resultsPerPage);
        } else {
            paginacion = new Paginacion(0, 0, resultsPerPage);
        }

        List<Object[]> result = q.getResultList();
        List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

        for (Object[] object : result) {
            RegistroEntrada re = new RegistroEntrada();
            re.setId((Long) object[0]);
            re.setNumeroRegistroFormateado((String) object[1]);
            re.setFecha((Date) object[2]);
            re.setOficina((Oficina) object[3]);
            re.setDestinoExternoCodigo((String) object[4]);
            re.setDestinoExternoDenominacion((String) object[5]);
            re.setRegistroDetalle(new RegistroDetalle());
            re.getRegistroDetalle().setExtracto((String) object[6]);
            registros.add(re);
        }

        paginacion.setListado(registros);

        return paginacion;
    }


    /**
     * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
     * Crea un RegistroSalida por cada uno de los RegistroEntrada que contenga el OficioRemision
     * Crea la trazabilidad para los RegistroEntrada y RegistroSalida
     *
     * @param registrosEntrada Listado de RegistrosEntrada que forman parte del Oficio de remisión
     * @param oficinaActiva    Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad   Usuario que realiza el OficioRemision
     * @param idOrganismo
     * @param idLibro
     * @return
     * @throws Exception
     */
    @Override
    public OficioRemision crearOficioRemisionInterno(List<RegistroEntrada> registrosEntrada,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, Long idOrganismo, Long idLibro)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_INTERNO_ENVIADO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setRegistrosEntrada(registrosEntrada);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setOrganismoDestinatario(new Organismo(idOrganismo));

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_INTERNO);
        }

        return oficioRemision;

    }

    /**
     * Crea un OficioRemision con todos los ResgistroEntrada seleccionados
     *
     * @param registrosEntrada       Listado de RegistrosEntrada que forman parte del Oficio de remisión
     * @param oficinaActiva          Oficia en la cual se realiza el OficioRemision
     * @param usuarioEntidad         Usuario que realiza el OficioRemision
     * @param organismoExternoCodigo
     * @param idLibro
     * @return
     * @throws Exception
     */

    public OficioRemision crearOficioRemisionExterno(List<RegistroEntrada> registrosEntrada,
                                                     Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, String organismoExternoCodigo,
                                                     String organismoExternoDenominacion, Long idLibro)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_EXTERNO_ENVIADO);
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setFecha(new Date());
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setRegistrosEntrada(registrosEntrada);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);
        oficioRemision.setLibro(new Libro(idLibro));
        oficioRemision.setDestinoExternoCodigo(organismoExternoCodigo);
        oficioRemision.setDestinoExternoDenominacion(organismoExternoDenominacion);
        oficioRemision.setOrganismoDestinatario(null);

        synchronized (this) {
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_EXTERNO);
        }

        return oficioRemision;

    }

    @Override
    public OficioRemision crearOficioRemisionSIR(RegistroEntrada registroEntrada, Oficina oficinaActiva, UsuarioEntidad usuarioEntidad, OficinaTF oficinaSirDestino )
            throws Exception, I18NException, I18NValidationException {

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIO_PROCESO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuarioEntidad);

        oficioRemision.setLibro(new Libro(registroEntrada.getLibro().getId()));
        oficioRemision.setIdentificadorIntercambio(registroEntrada.getRegistroDetalle().getIdentificadorIntercambio());
        oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
        oficioRemision.setDestinoExternoCodigo(registroEntrada.getDestinoExternoCodigo());
        oficioRemision.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
        oficioRemision.setRegistrosEntrada(Collections.singletonList(registroEntrada));
        oficioRemision.setOrganismoDestinatario(null);
        oficioRemision.setRegistrosSalida(null);
        oficioRemision.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
        oficioRemision.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
        oficioRemision.setContactosEntidadRegistralDestino(RegistroUtils.getContactosOficinaSir(oficinaSirDestino));

        // Registramos el Oficio de Remisión SIR
        oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_SIR);

        return oficioRemision;

    }

    @Override
    public List<RegistroEntrada> crearJustificantesRegistros(List<RegistroEntrada> registros, UsuarioEntidad usuario) throws Exception, I18NException, I18NValidationException {

        List<RegistroEntrada> correctos = new ArrayList<RegistroEntrada>();

        for (RegistroEntrada registro : registros) {

            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(registro.getId());

            //Justificante, Si no tiene generado el Justificante, lo hacemos
            if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {

                try {
                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuario, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, Configuracio.getDefaultLanguage());
                    registroEntrada.getRegistroDetalle().getAnexosFull().add(anexoFull);
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
     * Aceptar un OficioRemision pendiente de llegada, creando tantos Registros de Entrada,
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

        // Recorremos los RegistroEntrada del Oficio
        for (OficioPendienteLlegada oficio : oficios) {

            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(oficio.getIdRegistro());
            List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
            List<AnexoFull> anexos = registroEntrada.getRegistroDetalle().getAnexosFull();
            Libro libro = libroEjb.findById(usuario.getEntidad().getLibro().getId());

            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(registroEntrada);
            session.evict(registroEntrada.getRegistroDetalle());
            session.evict(registroEntrada.getRegistroDetalle().getAnexos());
            session.evict(registroEntrada.getRegistroDetalle().getInteresados());

            // Creamos un Nuevo RegistroEntrada
            RegistroEntrada nuevoRE = new RegistroEntrada();
            nuevoRE.setUsuario(usuario);
            nuevoRE.setDestino(organismoEjb.findByIdLigero(oficio.getIdOrganismoDestinatario()));
            nuevoRE.setOficina(oficinaActiva);
            nuevoRE.setEstado(RegwebConstantes.REGISTRO_VALIDO);
            nuevoRE.setLibro(libro);

            // Creamos un nuevo RegistroDetalle, modificando las propiedades Origen
            RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

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
            synchronized (this) {
                nuevoRE = registroEntradaEjb.registrarEntrada(nuevoRE, usuario, interesados, anexos, false, true);
            }

            registros.add(nuevoRE);

            // ACTUALIZAMOS LA TRAZABILIDAD
            Trazabilidad trazabilidad = trazabilidadEjb.getByOficioRegistroEntrada(oficioRemision.getId(), registroEntrada.getId());
            trazabilidad.setRegistroEntradaDestino(nuevoRE);
            trazabilidadEjb.merge(trazabilidad);

            // Marcamos el RegistroEntrada original como ACEPTADO
            registroEntradaEjb.cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_OFICIO_ACEPTADO, usuario);

        }

        oficioRemision.setEstado(RegwebConstantes.OFICIO_ACEPTADO);
        oficioRemision.setFechaEstado(new Date());

        // Actualizamos el oficio de remisión
        oficioRemisionEjb.merge(oficioRemision);

        return registros;
    }
}
