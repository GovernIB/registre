package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.oficina.RelacionOrganizativaOfiTF;
import es.caib.dir3caib.ws.api.oficina.RelacionSirOfiTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.TimeUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.hibernate.Hibernate;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.*;

//import java.text.SimpleDateFormat;

/**
 * Created 23/10/14 9:33
 *
 * @author mgonzalez
 */
@Stateless(name = "SincronizadorDir3EJB")
@RolesAllowed({"RWE_ADMIN"})
public class SincronizadorDir3Bean implements SincronizadorDir3Local {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private OrganismoLocal organismoEjb;
    @EJB private OficinaLocal oficinaEjb;
    @EJB private CatEstadoEntidadLocal catEstadoEntidadEjb;
    @EJB private CatNivelAdministracionLocal catNivelAdministracionEjb;
    @EJB private CatProvinciaLocal catProvinciaEjb;
    @EJB private CatComunidadAutonomaLocal catComunidadAutonomaEjb;
    @EJB private RelacionSirOfiLocal relacionSirOfiEjb;
    @EJB private RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;
    @EJB private EntidadLocal entidadEjb;
    @EJB private PendienteLocal pendienteEjb;
    @EJB private DescargaLocal descargaEjb;
    @EJB private CatLocalidadLocal catLocalidadEjb;
    @EJB private CatPaisLocal catPaisEjb;
    @EJB private CatTipoViaLocal catTipoViaEjb;
    @EJB private CatServicioLocal catServicioEjb;
    @EJB private PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;

    //Caches
    private Map<Long, CatProvincia> cacheProvincia = new TreeMap<Long, CatProvincia>();
    private Map<Long, CatComunidadAutonoma> cacheComunidadAutonoma = new TreeMap<Long, CatComunidadAutonoma>();
    private Map<Long, CatNivelAdministracion> cacheNivelAdministracion = new TreeMap<Long, CatNivelAdministracion>();
    private Map<Long, CatPais> cachePais = new TreeMap<Long, CatPais>();
    private Map<String, CatEstadoEntidad> cacheEstadoEntidad = new TreeMap<String, CatEstadoEntidad>();
    private Map<Long, CatTipoVia> cacheTipoVia = new TreeMap<Long, CatTipoVia>();
    private Map<Long, CatServicio> cacheServicio = new TreeMap<Long, CatServicio>();

    /**
     * Método que sincroniza o actualiza una entidad de regweb3 desde dir3caib. Lo hace en función de si se indica la
     * fecha de actualización o no. Si no se indica se sincroniza y si se indica se actualiza
     *
     * @param entidadId           entidad a tratar
     * @param fechaActualizacion  fecha de la ultima actualización con dir3caib
     * @param fechaSincronizacion fecha de la primera sincronización con dir3caib.
     * @return
     * @throws I18NException
     */
    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public int sincronizarActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws I18NException {

        Entidad entidad = entidadEjb.findByIdLigero(entidadId);

        // Obtenemos el Service de los WS de Unidades
        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(entidadId), PropiedadGlobalUtil.getDir3CaibUsername(entidadId), PropiedadGlobalUtil.getDir3CaibPassword(entidadId));

        // Obtenemos el arbol de Unidades
        List<UnidadTF> arbol = unidadesService.obtenerArbolUnidades(entidad.getCodigoDir3(), fechaActualizacion, fechaSincronizacion);

        log.info("Organimos obtenidos de " + entidad.getNombre() + ": " + arbol.size());

        /*  CACHES */
        inicializarCaches();

        // Procesamos el arbol de organismos obtenido
        if (!arbol.isEmpty()) {

            for (UnidadTF unidadTF : arbol) {

                sincronizarOrganismo(unidadTF, entidadId);
            }

            // Sincronizamos los históricos del arbol de organismos obtenido
            for (UnidadTF unidadTF : arbol) {
                if (unidadTF != null) {

                    Organismo organismo = organismoEjb.findByCodigoEntidadSinEstado(unidadTF.getCodigo(), entidadId);
                    sincronizarHistoricosOrganismo(organismo, unidadTF, entidadId);

                    // Comprobamos si se ha extinguido y hay que realizar acciones en consecuencia
                    procesarExtinguido(organismo, entidad);
                }
            }

        }

        // Creamos la descarga de Unidades
        nuevaDescarga(RegwebConstantes.UNIDAD, entidad);

        log.info("");
        log.info("Finalizada la importacion de Organismos");
        log.info("");

        Set<OficinaTF> todasOficinasEntidad = new HashSet<OficinaTF>();
        int oficinasActualizadas = 0;

        if (arbol.size() > 0 || fechaActualizacion != null) {// obtenemos las oficinas en caso de actualizacion o en caso de sincro sin han venido organismos.

            // Obtenemos el Service de los WS de Oficinas
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(entidadId), PropiedadGlobalUtil.getDir3CaibUsername(entidadId), PropiedadGlobalUtil.getDir3CaibPassword(entidadId));

            // Obtenemos todas las oficinas de la entidad.
            List<OficinaTF> oficinasTF = oficinasService.obtenerArbolOficinas(entidad.getCodigoDir3(), fechaActualizacion, fechaSincronizacion);
            todasOficinasEntidad.addAll(oficinasTF);

            // Procesamos todas las oficinas de la entidad
            crearActualizarOficinas(todasOficinasEntidad, entidadId);
            // asignamos su oficina responsable a todas las oficinas de la entidad,
            // ya que al haberlas creado en el paso previo nos aseguramos de que la encuentra.
            asignarOficinasResponsables(todasOficinasEntidad, entidadId);
            // creamos las relaciones organizativas de todas las oficinas de la entidad
            crearRelacionesOrganizativas(todasOficinasEntidad, entidadId);
            // creamos las relaciones sir de todas las oficinas de la entidad
            crearRelacionesSir(todasOficinasEntidad, entidadId);


            oficinasActualizadas += todasOficinasEntidad.size();


            nuevaDescarga(RegwebConstantes.OFICINA, entidad);

            // En el siguiente for se revisa que organismos con libros que son vigentes han podido quedar sin oficinas y guardarlos como pendientes.
            //Pueden quedar sin oficinas al borrarselas o quitarles las únicas relaciones organizativas que tengan.


            // Obtenemos los organismos vigentes de la entidad que tienen libros
            List<Organismo> vigentes = organismoEjb.getPermitirUsuarios(entidadId);

            for (Organismo organismo : vigentes) {

                //Miramos si el organismo tiene oficinas,
                Boolean tieneOficinas = oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_SI);
                if (!tieneOficinas) {//si no tiene se debe guardar en la tabla de pendientes para que los procese el usuario manualmente
                    //guardar pendiente
                    pendienteEjb.persist(new Pendiente(entidad,organismo.getId(), false, RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
                    log.info("Pendiente procesar: Organismo sin oficinas que le den servicio: " + organismo.getDenominacion());
                }

            }
        }
        //Si no hay pendientes de procesar desactivamos el mantenimiento de la entidad
        // porque ya ha acabado el proceso de sincronización
        //if (pendienteEjb.findPendientesProcesar(entidadId).isEmpty()) {
            entidadEjb.marcarEntidadMantenimiento(entidadId, false);
        //}

        log.info(" REGWEB3 ORGANISMOS ACTUALIZADOS:  " + arbol.size());
        log.info(" REGWEB3 OFICINAS ACTUALIZADAS:  " + oficinasActualizadas);


        /* borramos cache */
        cacheEstadoEntidad.clear();
        cacheProvincia.clear();
        cacheComunidadAutonoma.clear();
        cacheNivelAdministracion.clear();
        cachePais.clear();
        cacheTipoVia.clear();
        cacheServicio.clear();

        return arbol.size();


    }

    /**
     * Crea un {@link es.caib.regweb3.model.Organismo} a partir de una UnidadTF y lo relaciona con su {@link es.caib.regweb3.model.Entidad}
     * Este método se emplea tanto en el proceso de sincronización como en el de actualización
     *
     * @param unidadTF
     * @param idEntidad
     * @throws I18NException
     */
    private Organismo sincronizarOrganismo(UnidadTF unidadTF, Long idEntidad) throws I18NException {


        Entidad entidad = entidadEjb.findByIdLigero(idEntidad);

        Organismo organismo = null;

        // Comprobamos que la unidad que nos envian no sea null
        // (ocurre en el caso de que actualicemos y no se haya actualizado en el origen)
        if (unidadTF != null) {
            log.info("ORGANISMO ACTUALIZADO/SINCRONIZADO: " + unidadTF.getCodigo() + " - " + unidadTF.getDenominacion());
            // Comprobamos primero si ya existe el organismo

            organismo = organismoEjb.findByCodigoEntidadSinEstado(unidadTF.getCodigo(), idEntidad);

            if (organismo == null) {
                log.info("Nuevo organismo: " + unidadTF.getDenominacion());
                organismo = new Organismo();
                procesarOrganismo(organismo, unidadTF, entidad);

                //Guardamos el Organismo
                organismo = organismoEjb.persist(organismo);
            } else { // Si existe hay que actualizarlo
                Hibernate.initialize(organismo.getLibros());
                procesarOrganismo(organismo, unidadTF, entidad);
            }

            // Es necesario que el organismo esté creado previamente.
            // Asignamos su Organismo Raíz
            Organismo organismoRaiz = organismoEjb.findByCodigoEntidadSinEstado(unidadTF.getCodUnidadRaiz(), idEntidad);
            organismo.setOrganismoRaiz(organismoRaiz);


            // Asignamos su Organismo Superior
            Organismo organismoSuperior = organismoEjb.findByCodigoEntidadSinEstado(unidadTF.getCodUnidadSuperior(), idEntidad);
            organismo.setOrganismoSuperior(organismoSuperior);

            // Asignamos su EDP Principal
            if (StringUtils.isNotEmpty(unidadTF.getCodEdpPrincipal())) {
                Organismo edpPrincipal = organismoEjb.findByCodigoEntidadSinEstado(unidadTF.getCodEdpPrincipal(), idEntidad);
                organismo.setEdpPrincipal(edpPrincipal);
            }


            // Actualizamos el Organismo
            organismo = organismoEjb.merge(organismo);
            log.info("Fin sincronizar organismo: " + organismo.getDenominacion());
            log.info("  ");
        }

        return organismo;
    }

    /**
     * Este método crea todas las oficinas recibidas. Se guardan denominación, estado y organismo responsable
     *
     * @param oficinas conjunto de oficinas recibidas de una entidad
     * @throws I18NException
     */
    private void crearActualizarOficinas(Set<OficinaTF> oficinas, Long idEntidad) throws I18NException {

        for (OficinaTF oficinaTF : oficinas) {

            if (oficinaTF != null) {

                Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaTF.getCodigo(), idEntidad);

                if (oficina == null) { // Nueva oficina

                    oficina = new Oficina(null, oficinaTF.getCodigo(), null);

                    procesarOficina(oficina, oficinaTF, idEntidad); // Se procesa la oficina para asignar sus valores

                    // Guardamos la Oficina
                    oficinaEjb.persist(oficina);

                } else { // Actualización oficina

                    procesarOficina(oficina, oficinaTF, idEntidad); // Se procesa la oficina para asignar sus valores
                    oficinaEjb.merge(oficina);
                }

            }

        }

        log.info("");
        log.info("Oficinas creadas: " + oficinas.size());
        log.info("");
    }

    /**
     * En este método se asigna la oficina responsable a la lista de oficinas recibidas.
     *
     * @param oficinas
     * @throws I18NException
     */
    private void asignarOficinasResponsables(Set<OficinaTF> oficinas, Long idEntidad) throws I18NException {

        for (OficinaTF oficinaTF : oficinas) {

            if (oficinaTF != null) {
                Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaTF.getCodigo(), idEntidad);

                // OficinaResponsable
                if (oficinaTF.getCodOfiResponsable() != null) {
                    Oficina oficinaResponsable = oficinaEjb.findByCodigoEntidadSinEstado(oficinaTF.getCodOfiResponsable(), idEntidad);
                    if (oficinaResponsable != null) {
                        oficina.setOficinaResponsable(oficinaResponsable);
                        oficinaEjb.merge(oficina);
                    } else {
                        log.info("TIENE OFICINA RESPONSABLE, PERO NO LA ENCUENTRA: " + oficinaTF.getCodOfiResponsable());
                    }
                } else {
                    oficina.setOficinaResponsable(null);
                    oficinaEjb.merge(oficina);
                }
            }
        }
    }


    /**
     * Método que crea todas las relaciones organizativas de las oficinas recibidas
     *
     * @param oficinas oficinas de la entidad
     * @throws I18NException
     */
    private void crearRelacionesOrganizativas(Set<OficinaTF> oficinas, Long idEntidad) throws I18NException {

        log.info("RELACIONES ORGANIZATIVAS");
        log.info("");

        for (OficinaTF oficinaTF : oficinas) {

            if (oficinaTF != null) {

                if (oficinaTF.getOrganizativasOfi() != null) {

                    List<RelacionOrganizativaOfiTF> relacionOrganizativaOfiTFList = oficinaTF.getOrganizativasOfi();
                    Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaTF.getCodigo(), idEntidad);

                    log.info("Relaciones organizativas " + oficinaTF.getDenominacion() + " - " + oficinaTF.getCodigo() + ": " + relacionOrganizativaOfiTFList.size());

                    //Borramos las relaciones existentes para el caso de la actualizacion
                    log.info("Relaciones ORG eliminadas: " + relacionOrganizativaOfiEjb.deleteByOficinaEntidad(oficina.getId()));

                    for (RelacionOrganizativaOfiTF relacionOrganizativaOfiTF : relacionOrganizativaOfiTFList) {
                        log.info("");

                        RelacionOrganizativaOfi relacionOrganizativaOfi = new RelacionOrganizativaOfi();

                        CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionOrganizativaOfiTF.getEstado());
                        relacionOrganizativaOfi.setEstado(catEstadoEntidad);

                        relacionOrganizativaOfi.setOficina(oficina);

                        Organismo organismoOrg = organismoEjb.findByCodigoEntidadSinEstado(relacionOrganizativaOfiTF.getUnidad(), idEntidad);

                        relacionOrganizativaOfi.setOrganismo(organismoOrg);

                        log.info("Relacion ORG entre " + oficina.getDenominacion() + " - " + organismoOrg.getDenominacion());
                        relacionOrganizativaOfiEjb.persist(relacionOrganizativaOfi);

                    }

                }

            }
            log.info("");
        }
    }

    /**
     * Método que crea todas las relaciones SIR de las oficinas recibidas
     *
     * @param oficinas oficinas de la entidad
     * @throws I18NException
     */
    private void crearRelacionesSir(Set<OficinaTF> oficinas, Long idEntidad) throws I18NException {

        log.info("RELACIONES SIR");
        log.info("");

        for (OficinaTF oficinaTF : oficinas) {

            if (oficinaTF != null) {

                if (oficinaTF.getSirOfi() != null) {

                    List<RelacionSirOfiTF> relacionSirOfiTFList = oficinaTF.getSirOfi();
                    Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaTF.getCodigo(), idEntidad);

                    log.info("Relaciones SIR " + oficinaTF.getDenominacion() + " - " + oficinaTF.getCodigo() + ": " + relacionSirOfiTFList.size());

                    //Borramos las relaciones existentes para el caso de la actualizacion
                    log.info("Relaciones SIR eliminadas: " + relacionSirOfiEjb.deleteByOficinaEntidad(oficina.getId()));

                    for (RelacionSirOfiTF relacionSirOfiTF : relacionSirOfiTFList) {
                        log.info("");

                        RelacionSirOfi relacionSirOfi = new RelacionSirOfi();

                        CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionSirOfiTF.getEstado());
                        relacionSirOfi.setEstado(catEstadoEntidad);

                        relacionSirOfi.setOficina(oficina);

                        Organismo organismoOrg = organismoEjb.findByCodigoEntidadSinEstado(relacionSirOfiTF.getUnidad(), idEntidad);

                        relacionSirOfi.setOrganismo(organismoOrg);

                        log.info("Relacion SIR entre " + oficina.getDenominacion() + " - " + organismoOrg.getDenominacion());
                        relacionSirOfiEjb.persist(relacionSirOfi);

                    }

                }

            }
            log.info("");
        }

    }


    /**
     * Método que sincroniza los históricos de un organismo. Se debe ejecutar después de sincronizarlos todos.
     *
     * @param organismo organismo al que guardar los históricos
     * @param unidadTF  unidad transferida equivalente al organismo que nos proporciona los historicos.
     * @throws I18NException
     */
    private void sincronizarHistoricosOrganismo(Organismo organismo, UnidadTF unidadTF, Long idEntidad) throws I18NException {
        // Inicializamos sus Historicos, ya la relación está a FetchType.LAZY
        List<String> historicos = unidadTF.getHistoricosUO();
        Set<Organismo> historicosOrg = organismo.getHistoricoUO();
        if (!historicos.isEmpty()) {
            log.info("UnidadTF : " + unidadTF.getCodigo() + " Historicos: " + historicos.size());

        }
        // Si l'organisme no te històrics, inicialitzam la variable.
        if (historicosOrg == null) {
            historicosOrg = new HashSet<Organismo>();
        }

        for (String historico : historicos) {
            log.info("HISTORICO  :" + historico);

            Organismo orgUltima = organismoEjb.findByCodigoEntidadSinEstado(historico, idEntidad);
            log.info("orgUltima  :" + orgUltima);
            historicosOrg.add(orgUltima);
        }
        organismo.setHistoricoUO(historicosOrg);
        organismoEjb.merge(organismo);

    }


    /**
     * Función que actualiza un conjunto de datos del organismo
     *
     * @param organismo organismo a actualizar
     * @param unidadTF  datos de la unidad transferida desde dir3caib
     * @param entidad   entidad que se está actualizando
     * @throws I18NException
     */
    private void procesarOrganismo(Organismo organismo, UnidadTF unidadTF, Entidad entidad) throws I18NException {


        CatEstadoEntidad estado = cacheEstadoEntidad.get(unidadTF.getCodigoEstadoEntidad());

        organismo.setCodigo(unidadTF.getCodigo());
        organismo.setEstado(estado);
        organismo.setEntidad(entidad);
        organismo.setDenominacion(unidadTF.getDenominacion());
        organismo.setNivelJerarquico(unidadTF.getNivelJerarquico());
        organismo.setEdp(unidadTF.isEsEdp());


        //Nivel Administracion
        CatNivelAdministracion nivelAdministracion = cacheNivelAdministracion.get(unidadTF.getNivelAdministracion());
        organismo.setNivelAdministracion(nivelAdministracion);

        if (unidadTF.getCodAmbProvincia() != null) {
            CatProvincia provincia = cacheProvincia.get(unidadTF.getCodAmbProvincia());
            organismo.setCodAmbProvincia(provincia);

        } else {
            organismo.setCodAmbProvincia(null);
        }

        if (unidadTF.getCodAmbComunidad() != null) {
            CatComunidadAutonoma comunidadAutonoma = cacheComunidadAutonoma.get(unidadTF.getCodAmbComunidad());
            organismo.setCodAmbComunidad(comunidadAutonoma);

        } else {
            organismo.setCodAmbComunidad(null);
        }

        if (unidadTF.getCodigoAmbPais() != null) {
            organismo.setCodPais(cachePais.get(unidadTF.getCodigoAmbPais()));
        }
        if (StringUtils.isNotEmpty(unidadTF.getDescripcionLocalidad())) {
            organismo.setLocalidad(catLocalidadEjb.findByNombre(unidadTF.getDescripcionLocalidad()));
        }
        if (unidadTF.getCodigoTipoVia() != null) {
            organismo.setTipoVia(cacheTipoVia.get(unidadTF.getCodigoTipoVia()));
        }
        if (StringUtils.isNotEmpty(unidadTF.getNombreVia())) {
            organismo.setNombreVia(unidadTF.getNombreVia());
        }
        if (StringUtils.isNotEmpty(unidadTF.getNumVia())) {
            organismo.setNumVia(unidadTF.getNumVia());
        }
        if (StringUtils.isNotEmpty(unidadTF.getCodPostal())) {
            organismo.setCodPostal(unidadTF.getCodPostal());
        }

    }

    /**
     * Función que actualiza el estado y el organismo responsable de una oficina
     *
     * @param oficina   oficina a tratar
     * @param oficinaTF oficina transferida desde dir3caib
     * @throws I18NException
     */
    private void procesarOficina(Oficina oficina, OficinaTF oficinaTF, Long idEntidad) throws I18NException {

        oficina.setDenominacion(oficinaTF.getDenominacion());
        oficina.setEstado(cacheEstadoEntidad.get(oficinaTF.getEstado()));


        Organismo organismoResponsable = organismoEjb.findByCodigoEntidadSinEstado(oficinaTF.getCodUoResponsable(), idEntidad);
        oficina.setOrganismoResponsable(organismoResponsable);

        if (oficinaTF.getCodigoPais() != null) {
            oficina.setCodPais(cachePais.get(oficinaTF.getCodigoPais()));
        }
        if (oficinaTF.getCodigoComunidad() != null) {
            oficina.setCodComunidad(cacheComunidadAutonoma.get(oficinaTF.getCodigoComunidad()));
        }

        if (StringUtils.isNotEmpty(oficinaTF.getDescripcionLocalidad())) {
            oficina.setLocalidad(catLocalidadEjb.findByNombre(oficinaTF.getDescripcionLocalidad()));
        }

        if (oficinaTF.getCodigoTipoVia() != null) {
            oficina.setTipoVia(cacheTipoVia.get(oficinaTF.getCodigoTipoVia()));
        }
        if (StringUtils.isNotEmpty(oficinaTF.getNombreVia())) {
            oficina.setNombreVia(oficinaTF.getNombreVia());
        }
        if (StringUtils.isNotEmpty(oficinaTF.getNumVia())) {
            oficina.setNumVia(oficinaTF.getNumVia());
        }
        if (StringUtils.isNotEmpty(oficinaTF.getCodPostal())) {
            oficina.setCodPostal(oficinaTF.getCodPostal());
        }

        if (oficinaTF.getServicios() != null && oficinaTF.getServicios().size() > 0) {

            Set<CatServicio> servicios = new HashSet<CatServicio>();

            for (Long servicio : oficinaTF.getServicios()) {
                servicios.add(cacheServicio.get(servicio));
            }

            oficina.setServicios(servicios);
        } else {
            oficina.setServicios(null);
        }
    }


    /**
     * Función que crea una nueva entrada en la tabla RWE_DESCARGA que indica que se ha producido una nueva descarga
     * (sincronización o actualización) de la entidad indicada.
     *
     * @param tipo    indica organismo o oficina.
     * @param entidad entidad descargada
     * @throws I18NException
     */
    private void nuevaDescarga(String tipo, Entidad entidad) throws I18NException {

        Descarga descarga = new Descarga(new Date(), tipo, entidad);

        descargaEjb.persist(descarga);
    }

    /**
     * Comprueba si el Organismo se ha extinguido y realiza las acciones en consecuencia:
     * <p>
     * 1- Crea una entrada en la tabla de RWE_PENDIENTE que indica que es un organismo que está pendiente
     * de procesar(reasignar sus libros a los organismos que lo sustituyen). Se crea según el estado del organismo
     * recibido y si tiene libros.
     *
     * @param organismo organismo a tratar
     * @throws I18NException
     */
    private void procesarExtinguido(Organismo organismo, Entidad entidad) throws I18NException {

        if (organismo != null) {

            String estado = organismo.getEstado().getCodigoEstadoEntidad();

            if (RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO.equals(estado)
                    || RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO.equals(estado)
                    || RegwebConstantes.ESTADO_ENTIDAD_ANULADO.equals(estado)) {

                // Si el Organismos tiene permisos asignados, creamos un registro en Pendiente
                if (permisoOrganismoUsuarioEjb.tienePermisos(organismo.getId())) {

                    pendienteEjb.persist(new Pendiente(entidad, organismo.getId(), false, organismo.getEstado().getCodigoEstadoEntidad()));
                    log.info("Pendiente procesar - Organismo extinguido: " + organismo.getDenominacion());
                }
            }
        }
    }

    /**
     * Inicializa los caches que se utilizarán en el método
     *
     * @throws I18NException
     */
    private void inicializarCaches() throws I18NException {

        long start = System.currentTimeMillis();
        for (CatEstadoEntidad ca : catEstadoEntidadEjb.getAll()) {
            cacheEstadoEntidad.put(ca.getCodigoEstadoEntidad(), ca);
        }

        for (CatProvincia ca : catProvinciaEjb.getAll()) {
            cacheProvincia.put(ca.getCodigoProvincia(), ca);
        }

        for (CatComunidadAutonoma ca : catComunidadAutonomaEjb.getAll()) {
            cacheComunidadAutonoma.put(ca.getCodigoComunidad(), ca);
        }

        for (CatNivelAdministracion na : catNivelAdministracionEjb.getAll()) {
            cacheNivelAdministracion.put(na.getCodigoNivelAdministracion(), na);
        }

        for (CatPais pa : catPaisEjb.getAll()) {
            cachePais.put(pa.getCodigoPais(), pa);
        }

        for (CatTipoVia tv : catTipoViaEjb.getAll()) {
            cacheTipoVia.put(tv.getCodigoTipoVia(), tv);
        }

        for (CatServicio se : catServicioEjb.getAll()) {
            cacheServicio.put(se.getCodServicio(), se);
        }
        long end = System.currentTimeMillis();
        log.info("Inicializadas Caches sincronizador Dir3 en " + TimeUtils.formatElapsedTime(end - start));
        log.info("");
    }
}
