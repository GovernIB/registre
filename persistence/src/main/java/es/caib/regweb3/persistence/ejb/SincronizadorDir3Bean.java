package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaWs;
import es.caib.dir3caib.ws.api.oficina.RelacionOrganizativaOfiTF;
import es.caib.dir3caib.ws.api.oficina.RelacionSirOfiTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadWs;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.TimeUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.*;


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
    @EJB private CatIslaLocal catIslaEjb;
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
    private Map<Long, CatIsla> cacheIsla = new TreeMap<Long, CatIsla>();
    private Map<Long, CatNivelAdministracion> cacheNivelAdministracion = new TreeMap<Long, CatNivelAdministracion>();
    private Map<Long, CatPais> cachePais = new TreeMap<Long, CatPais>();
    private Map<String, CatEstadoEntidad> cacheEstadoEntidad = new TreeMap<String, CatEstadoEntidad>();
    private Map<Long, CatTipoVia> cacheTipoVia = new TreeMap<Long, CatTipoVia>();
    private Map<Long, CatServicio> cacheServicio = new TreeMap<Long, CatServicio>();

    /**
     * Función que sincroniza o actualiza una entidad de regweb3 desde dir3caib. Lo hace en función de si se indica la
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
    public void sincronizarActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws I18NException {

        Entidad entidad = entidadEjb.findByIdLigero(entidadId);
        List<String> sincronizados = new ArrayList<String>();

        // Obtenemos el Service de los WS de Unidades
        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(entidadId), PropiedadGlobalUtil.getDir3CaibUsername(entidadId), PropiedadGlobalUtil.getDir3CaibPassword(entidadId));

        // Obtenemos el arbol de Unidades
        List<UnidadWs> unidadesWs = unidadesService.obtenerArbolUnidadesV2(entidad.getCodigoDir3(), fechaActualizacion, fechaSincronizacion);

        log.info("Organimos obtenidos de " + entidad.getNombre() + ": " + unidadesWs.size());

        /*  CACHES */
        inicializarCaches();

        // Procesamos el arbol de organismos obtenido
        if (!unidadesWs.isEmpty()) {

            for (UnidadWs unidadWs : unidadesWs) {
                sincronizarOrganismo(unidadWs, entidadId);
                sincronizados.add((StringUtils.isNotEmpty(unidadWs.getDenomLenguaCooficial())) ? unidadWs.getDenomLenguaCooficial() : unidadWs.getDenominacion());
            }

            //Actualizamos organismo raiz y superiores y EDP principal
            for(UnidadWs unidadWs : unidadesWs) {
                Organismo organismo = organismoEjb.findByCodigoEntidadSinEstado(unidadWs.getCodigo(), entidadId);
                // Es necesario que el organismo esté creado previamente.
                // Asignamos su Organismo Raíz
                Organismo organismoRaiz = organismoEjb.findByCodigoEntidadSinEstado(unidadWs.getCodUnidadRaiz(), entidadId);
                organismo.setOrganismoRaiz(organismoRaiz);


                // Asignamos su Organismo Superior
                Organismo organismoSuperior = organismoEjb.findByCodigoEntidadSinEstado(unidadWs.getCodUnidadSuperior(), entidadId);
                organismo.setOrganismoSuperior(organismoSuperior);

                // Asignamos su EDP Principal
                if (StringUtils.isNotEmpty(unidadWs.getCodEdpPrincipal())) {
                    Organismo edpPrincipal = organismoEjb.findByCodigoEntidadSinEstado(unidadWs.getCodEdpPrincipal(), entidadId);
                    organismo.setEdpPrincipal(edpPrincipal);
                }
                organismo = organismoEjb.merge(organismo);
                log.info("Asignado Organismo Raiz, Superior y EDP Principal a: " + organismo.getDenominacion());
            }

            // Sincronizamos los históricos del arbol de organismos obtenido
            for (UnidadWs unidadWs : unidadesWs) {
                if (unidadWs != null) {

                    Organismo organismo = organismoEjb.findByCodigoEntidadSinEstado(unidadWs.getCodigo(), entidadId);
                    sincronizarHistoricosOrganismo(organismo, unidadWs, entidadId);

                    // Comprobamos si se ha extinguido y hay que realizar acciones en consecuencia
                    procesarExtinguido(organismo, entidad);
                }
            }
        }

        // Creamos la descarga de Unidades
        nuevaDescarga(RegwebConstantes.DESCARGA_UNIDAD, entidad, Arrays.toString(sincronizados.toArray()));

        log.info("");
        log.info("Finalizada la importacion de Organismos");
        log.info("");

        if (!unidadesWs.isEmpty() || fechaActualizacion != null) {// obtenemos las oficinas en caso de actualizacion o en caso de sincro sin han venido organismos.

            // Obtenemos el Service de los WS de Oficinas
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(entidadId), PropiedadGlobalUtil.getDir3CaibUsername(entidadId), PropiedadGlobalUtil.getDir3CaibPassword(entidadId));

            // Obtenemos todas las oficinas de la entidad.
            List<OficinaWs> oficinasWS = oficinasService.obtenerArbolOficinasV2(entidad.getCodigoDir3(), fechaActualizacion, fechaSincronizacion);
            log.info("Oficinas obtenidas de " + entidad.getNombre() + ": " + oficinasWS.size());

            sincronizados.clear();

            // Procesamos las oficinas obtenidas
            if (!oficinasWS.isEmpty()) {

                // Procesamos todas las oficinas de la entidad
                for (OficinaWs oficinaWs : oficinasWS) {
                    sincronizarOficinas(oficinaWs, entidadId);
                    sincronizados.add((StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial())) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion());
                }

                // asignamos su oficina responsable a todas las oficinas de la entidad,
                // ya que al haberlas creado en el paso previo nos aseguramos de que la encuentra.
                asignarOficinasResponsables(oficinasWS, entidadId);
                // creamos las relaciones organizativas de todas las oficinas de la entidad
                crearRelacionesOrganizativas(oficinasWS, entidadId);
                // creamos las relaciones sir de todas las oficinas de la entidad
                crearRelacionesSir(oficinasWS, entidadId);
            }

            // Creamos la descarga de Oficinas
            nuevaDescarga(RegwebConstantes.DESCARGA_OFICINA, entidad, Arrays.toString(sincronizados.toArray()));

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

            log.info(" REGWEB3 ORGANISMOS SINCRONIZADOS:  " + unidadesWs.size());
            log.info(" REGWEB3 OFICINAS SINCRONIZADAS:  " + oficinasWS.size());
        }

        entidadEjb.marcarEntidadMantenimiento(entidadId, false);

        /* borramos cache */
        cacheEstadoEntidad.clear();
        cacheProvincia.clear();
        cacheIsla.clear();
        cacheComunidadAutonoma.clear();
        cacheNivelAdministracion.clear();
        cachePais.clear();
        cacheTipoVia.clear();
        cacheServicio.clear();

    }

    /**
     * Crea un {@link es.caib.regweb3.model.Organismo} a partir de una UnidadWs y lo relaciona con su {@link es.caib.regweb3.model.Entidad}
     * Esta función se emplea tanto en el proceso de sincronización como en el de actualización
     *
     * @param unidadWs
     * @param idEntidad
     * @throws I18NException
     */
    private Organismo sincronizarOrganismo(UnidadWs unidadWs, Long idEntidad) throws I18NException {

        Entidad entidad = entidadEjb.findByIdLigero(idEntidad);

        Organismo organismo = null;

        // Comprobamos que la unidad que nos envian no sea null
        // (ocurre en el caso de que actualicemos y no se haya actualizado en el origen)
        if (unidadWs != null) {

            // Comprobamos primero si ya existe el organismo
            organismo = organismoEjb.findByCodigoEntidadSinEstado(unidadWs.getCodigo(), idEntidad);

            if (organismo == null) {
                log.info("Nuevo organismo: " + unidadWs.getCodigo()+ " - " + (StringUtils.isNotEmpty(unidadWs.getDenomLenguaCooficial()) ? unidadWs.getDenomLenguaCooficial() : unidadWs.getDenominacion()));
                organismo = new Organismo();
                procesarOrganismo(organismo, unidadWs, entidad);

                //Guardamos el Organismo
                organismo = organismoEjb.persist(organismo);
            } else { // Si existe hay que actualizarlo
                log.info("Actualizar organismo: " + (StringUtils.isNotEmpty(unidadWs.getDenomLenguaCooficial()) ? unidadWs.getDenomLenguaCooficial() : unidadWs.getDenominacion()));
                procesarOrganismo(organismo, unidadWs, entidad);
            }

        }

        return organismo;
    }

    /**
     * Esta función crea todas las oficinas recibidas. Se guardan denominación, estado y organismo responsable
     *
     * @param oficinaWs a sincronizar
     * @throws I18NException
     */
    private void sincronizarOficinas(OficinaWs oficinaWs, Long idEntidad) throws I18NException {

        if (oficinaWs != null) {

            Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaWs.getCodigo(), idEntidad);

            if (oficina == null) { // Nueva oficina
                log.info("Nueva oficina: " + oficinaWs.getCodigo()+ " - " + (StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial()) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion()));

                oficina = new Oficina(idEntidad, oficinaWs.getCodigo());

                procesarOficina(oficina, oficinaWs, idEntidad); // Se procesa la oficina para asignar sus valores

                // Guardamos la Oficina
                oficinaEjb.persist(oficina);

            } else { // Actualización oficina
                log.info("Actualizar oficina: " + oficinaWs.getCodigo()+ " - " + (StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial()) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion()));
                procesarOficina(oficina, oficinaWs, idEntidad); // Se procesa la oficina para asignar sus valores

                // Actualizamos la Oficina
                oficinaEjb.merge(oficina);
            }
        }
    }

    /**
     * Esta función se asigna la oficina responsable a la lista de oficinas recibidas.
     *
     * @param oficinas
     * @throws I18NException
     */
    private void asignarOficinasResponsables(List<OficinaWs> oficinas, Long idEntidad) throws I18NException {

        for (OficinaWs oficinaWs : oficinas) {

            if (oficinaWs != null) {
                Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaWs.getCodigo(), idEntidad);

                // OficinaResponsable
                if (oficinaWs.getCodOfiResponsable() != null) {
                    Oficina oficinaResponsable = oficinaEjb.findByCodigoEntidadSinEstado(oficinaWs.getCodOfiResponsable(), idEntidad);
                    if (oficinaResponsable != null) {
                        oficina.setOficinaResponsable(oficinaResponsable);
                        oficinaEjb.merge(oficina);
                    } else {
                        log.info("TIENE OFICINA RESPONSABLE, PERO NO LA ENCUENTRA: " + oficinaWs.getCodOfiResponsable());
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
    private void crearRelacionesOrganizativas(List<OficinaWs> oficinas, Long idEntidad) throws I18NException {

        log.info("RELACIONES ORGANIZATIVAS");
        log.info("");

        for (OficinaWs oficinaWs : oficinas) {

            if (oficinaWs != null && oficinaWs.getOrganizativasOfi() != null) {

                    List<RelacionOrganizativaOfiTF> relacionOrganizativaOfiTFList = oficinaWs.getOrganizativasOfi();
                    Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaWs.getCodigo(), idEntidad);

                    log.info("Relaciones organizativas " + (StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial()) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion()) + " - " + oficinaWs.getCodigo() + ": " + relacionOrganizativaOfiTFList.size());

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

                        log.info("Relacion ORG creada entre " + (StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial()) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion()) + " y " + organismoOrg.getDenominacion());
                        relacionOrganizativaOfiEjb.persist(relacionOrganizativaOfi);

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
    private void crearRelacionesSir(List<OficinaWs> oficinas, Long idEntidad) throws I18NException {

        log.info("RELACIONES SIR");
        log.info("");

        for (OficinaWs oficinaWs : oficinas) {

            if (oficinaWs != null) {

                if (oficinaWs.getSirOfi() != null) {

                    List<RelacionSirOfiTF> relacionSirOfiTFList = oficinaWs.getSirOfi();
                    Oficina oficina = oficinaEjb.findByCodigoEntidadSinEstado(oficinaWs.getCodigo(), idEntidad);

                    log.info("Relaciones SIR " + (StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial()) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion()) + " - " + oficinaWs.getCodigo() + ": " + relacionSirOfiTFList.size());

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

                        log.info("Relacion SIR creada entre " + oficina.getDenominacion() + " y " + organismoOrg.getDenominacion());
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
     * @param unidadWs  unidad transferida equivalente al organismo que nos proporciona los historicos.
     * @throws I18NException
     */
    private void sincronizarHistoricosOrganismo(Organismo organismo, UnidadWs unidadWs, Long idEntidad) throws I18NException {
        // Inicializamos sus Historicos, ya la relación está a FetchType.LAZY
        List<String> historicos = unidadWs.getHistoricosUO();
        Set<Organismo> historicosOrg = organismo.getHistoricoUO();
        if (!historicos.isEmpty()) {
            log.info("UnidadWs : " + unidadWs.getCodigo() + " Historicos: " + historicos.size());

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
     * @param unidadWs  datos de la unidad transferida desde dir3caib
     * @param entidad   entidad que se está actualizando
     * @throws I18NException
     */
    private void procesarOrganismo(Organismo organismo, UnidadWs unidadWs, Entidad entidad) throws I18NException {

        CatEstadoEntidad estado = cacheEstadoEntidad.get(unidadWs.getCodigoEstadoEntidad());

        organismo.setCodigo(unidadWs.getCodigo());
        organismo.setEstado(estado);
        organismo.setEntidad(entidad);
        organismo.setDenominacion((StringUtils.isNotEmpty(unidadWs.getDenomLenguaCooficial())) ? unidadWs.getDenomLenguaCooficial() : unidadWs.getDenominacion());
        organismo.setNivelJerarquico(unidadWs.getNivelJerarquico());
        organismo.setEdp(unidadWs.isEsEdp());

        //Nivel Administracion
        CatNivelAdministracion nivelAdministracion = cacheNivelAdministracion.get(unidadWs.getNivelAdministracion());
        organismo.setNivelAdministracion(nivelAdministracion);

        if (unidadWs.getCodAmbProvincia() != null) {
            CatProvincia provincia = cacheProvincia.get(unidadWs.getCodAmbProvincia());
            organismo.setCodAmbProvincia(provincia);

        } else {
            organismo.setCodAmbProvincia(null);
        }

        if (unidadWs.getCodAmbComunidad() != null) {
            CatComunidadAutonoma comunidadAutonoma = cacheComunidadAutonoma.get(unidadWs.getCodAmbComunidad());
            organismo.setCodAmbComunidad(comunidadAutonoma);

        } else {
            organismo.setCodAmbComunidad(null);
        }

        if(unidadWs.getCodAmbIsla() != null){
            organismo.setIsla(cacheIsla.get(unidadWs.getCodAmbIsla()));
        }

        if (unidadWs.getCodigoAmbPais() != null) {
            organismo.setCodPais(cachePais.get(unidadWs.getCodigoAmbPais()));
        }
        if (StringUtils.isNotEmpty(unidadWs.getDescripcionLocalidad())) {
            organismo.setLocalidad(catLocalidadEjb.findByNombre(unidadWs.getDescripcionLocalidad()));
        }
        if (unidadWs.getCodigoTipoVia() != null) {
            organismo.setTipoVia(cacheTipoVia.get(unidadWs.getCodigoTipoVia()));
        }
        if (StringUtils.isNotEmpty(unidadWs.getNombreVia())) {
            organismo.setNombreVia(unidadWs.getNombreVia());
        }
        if (StringUtils.isNotEmpty(unidadWs.getNumVia())) {
            organismo.setNumVia(unidadWs.getNumVia());
        }
        if (StringUtils.isNotEmpty(unidadWs.getCodPostal())) {
            organismo.setCodPostal(unidadWs.getCodPostal());
        }

    }

    /**
     * Función que actualiza el estado y el organismo responsable de una oficina
     *
     * @param oficina   oficina a tratar
     * @param oficinaWs oficina transferida desde dir3caib
     * @throws I18NException
     */
    private void procesarOficina(Oficina oficina, OficinaWs oficinaWs, Long idEntidad) throws I18NException {

        oficina.setDenominacion((StringUtils.isNotEmpty(oficinaWs.getDenomLenguaCooficial()) ? oficinaWs.getDenomLenguaCooficial() : oficinaWs.getDenominacion()));
        oficina.setEstado(cacheEstadoEntidad.get(oficinaWs.getEstado()));


        Organismo organismoResponsable = organismoEjb.findByCodigoEntidadSinEstado(oficinaWs.getCodUoResponsable(), idEntidad);
        oficina.setOrganismoResponsable(organismoResponsable);

        if (oficinaWs.getCodigoPais() != null) {
            oficina.setCodPais(cachePais.get(oficinaWs.getCodigoPais()));
        }
        if (oficinaWs.getCodigoComunidad() != null) {
            oficina.setCodComunidad(cacheComunidadAutonoma.get(oficinaWs.getCodigoComunidad()));
        }
        if (StringUtils.isNotEmpty(oficinaWs.getDescripcionLocalidad())) {
            oficina.setLocalidad(catLocalidadEjb.findByNombre(oficinaWs.getDescripcionLocalidad()));
        }
        if (oficinaWs.getCodigoTipoVia() != null) {
            oficina.setTipoVia(cacheTipoVia.get(oficinaWs.getCodigoTipoVia()));
        }
        if (StringUtils.isNotEmpty(oficinaWs.getNombreVia())) {
            oficina.setNombreVia(oficinaWs.getNombreVia());
        }
        if (StringUtils.isNotEmpty(oficinaWs.getNumVia())) {
            oficina.setNumVia(oficinaWs.getNumVia());
        }
        if (StringUtils.isNotEmpty(oficinaWs.getCodPostal())) {
            oficina.setCodPostal(oficinaWs.getCodPostal());
        }

        if (oficinaWs.getServicios() != null && !oficinaWs.getServicios().isEmpty()) {

            if(oficina.getId() != null){
                oficinaEjb.eliminarServicios(oficina.getId());
            }
            Set<CatServicio> servicios = new HashSet<CatServicio>();

            for (Long servicio : oficinaWs.getServicios()) {
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
     * @param elementos Unidades u Oficinas sincronizadas
     * @throws I18NException
     */
    private void nuevaDescarga(Integer tipo, Entidad entidad, String elementos) throws I18NException {

        Descarga descarga = new Descarga(new Date(), tipo, entidad, elementos);

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

        for (CatIsla isla : catIslaEjb.getAll()) {
            cacheIsla.put(isla.getCodigoIsla(), isla);
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
