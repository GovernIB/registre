package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.oficina.RelacionOrganizativaOfiTF;
import es.caib.dir3caib.ws.api.oficina.RelacionSirOfiTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;

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
@SecurityDomain("seycon")
public class SincronizadorDir3Bean implements SincronizadorDir3Local {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb3/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb3/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb3/RelacionSirOfiEJB/local")
    public RelacionSirOfiLocal relacionSirOfiEjb;

    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/PendienteEJB/local")
    public PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    public DescargaLocal descargaEjb;

    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/CatTipoViaEJB/local")
    public CatTipoViaLocal catTipoViaEjb;

    @EJB(mappedName = "regweb3/CatServicioEJB/local")
    public CatServicioLocal catServicioEjb;


    /**
     * Método que sincroniza o actualiza una entidad de regweb3 desde dir3caib. Lo hace en función de si se indica la
     * fecha de actualización o no. Si no se indica se sincroniza y si se indica se actualiza
     * @param entidadId entidad a tratar
     * @param fechaActualizacion fecha de la ultima actualización con dir3caib
     * @param fechaSincronizacion fecha de la primera sincronización con dir3caib.
     * @return
     * @throws Exception
     */
    @Override
    public int sincronizarActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws Exception {


        Entidad entidad = entidadEjb.findById(entidadId);


        // Obtenemos el Service de los WS de Unidades y Oficinas
        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
        Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();


        // Obtenemos la Unidad Padre y las dependientes.
        es.caib.dir3caib.ws.api.unidad.UnidadTF unidadPadre = unidadesService.obtenerUnidad(entidad.getCodigoDir3(),fechaActualizacion, fechaSincronizacion);

        List<UnidadTF> arbol = unidadesService.obtenerArbolUnidades(entidad.getCodigoDir3(), fechaActualizacion, fechaSincronizacion);

        log.info("Organimos obtenidos de " + entidad.getNombre() +": " + arbol.size());


        // Guardamos el Organismo-Entidad sincronizado
        Organismo padre = sincronizarOrganismo(unidadPadre, entidadId);
        // Clasificamos el padre en función de su estado
        if(padre != null){
            guardarPendiente(padre);
        }

        // Sincronizamos el arbol de organismos
        for (UnidadTF unidadTF : arbol) {
            Organismo hijo = sincronizarOrganismo(unidadTF, entidadId);
            // Clasificamos el organismo hijo en función del estado
            if(hijo != null){
                guardarPendiente(hijo);
            }
        }

        // Sincronizamos históricos
        if(unidadPadre != null){
          sincronizarHistoricosOrganismo(padre, unidadPadre);// históricos del padre
        }

        for(UnidadTF unidadTF : arbol){ // Sincronizamos los históricos del arbol.
           if(unidadTF != null){
            Organismo hijo = organismoEjb.findByCodigo(unidadTF.getCodigo());
            sincronizarHistoricosOrganismo(hijo, unidadTF);
           }
        }



        // Guardamos los datos de la ultima descarga siempre, independiente de si no hay datos actualizados.
        nuevaDescarga(RegwebConstantes.UNIDAD, entidad);


        log.info("");
        log.info("Finalizada la importacion de Organismos");
        log.info("");
        int oficinasActualizadas = 0;

        Set<OficinaTF> todasOficinasEntidad= new HashSet<OficinaTF>(); // Guardará todas las oficinas de la entidad
        // Obtenemos todas las oficinas de la entidad. Para ello obtenemos para cada Organismo las Oficinas dependientes de él
        for(Organismo organismo: organismoEjb.findByEntidad(entidadId)){
            List<OficinaTF> oficinas = oficinasService.obtenerArbolOficinas(organismo.getCodigo(), fechaActualizacion,fechaSincronizacion);
            todasOficinasEntidad.addAll(oficinas);
        }
        // Procesamos todas las oficinas de la entidad
        crearActualizarOficinas(todasOficinasEntidad);
        // asignamos su oficina responsable a todas las oficinas de la entidad,
        // ya que al haberlas creado en el paso previo nos aseguramos de que la encuentra.
        asignarOficinasResponsables(todasOficinasEntidad);
        // creamos las relaciones organizativas de todas las oficinas de la entidad
        crearRelacionesOrganizativas(todasOficinasEntidad);
        // creamos las relaciones sir de todas las oficinas de la entidad
        crearRelacionesSir(todasOficinasEntidad);


        oficinasActualizadas += todasOficinasEntidad.size();


        nuevaDescarga(RegwebConstantes.OFICINA, entidad);
        log.info(" REGWEB3 ORGANISMOS ACTUALIZADOS:  " + arbol.size() );
        log.info(" REGWEB3 OFICINAS ACTUALIZADAS:  " + oficinasActualizadas );

        return arbol.size();


    }

    /**
     * Crea un {@link es.caib.regweb3.model.Organismo} a partir de una UnidadTF y lo relaciona con su {@link es.caib.regweb3.model.Entidad}
     * Este método se emplea tanto en el proceso de sincronización como en el de actualización
     * @param unidadTF
     * @param idEntidad
     * @throws Exception
     */
    public Organismo sincronizarOrganismo(UnidadTF unidadTF, Long idEntidad) throws Exception {

      /*  CACHES */
      
      Map<String, CatEstadoEntidad> cacheEstadoEntidad = montarCacheEstadoEntidad();


      Map<Long,CatProvincia> cacheProvincia = new TreeMap<Long,CatProvincia>();
      for (CatProvincia ca : catProvinciaEjb.getAll()) {
        cacheProvincia.put(ca.getCodigoProvincia(), ca);
      }

      Map<Long,CatComunidadAutonoma> cacheComunidadAutonoma = new TreeMap<Long,CatComunidadAutonoma>();
      for (CatComunidadAutonoma ca : catComunidadAutonomaEjb.getAll()) {
        cacheComunidadAutonoma.put(ca.getCodigoComunidad(), ca);
      }

      Map<Long, CatNivelAdministracion> cacheNivelAdministracion  = new TreeMap<Long, CatNivelAdministracion>();
      for (CatNivelAdministracion na : catNivelAdministracionEjb.getAll()) {
        cacheNivelAdministracion.put(na.getCodigoNivelAdministracion(), na);
      }

      Entidad entidad = entidadEjb.findById(idEntidad);

      Organismo organismo = null;
      if(unidadTF != null){
        log.info("UnidadTF " + unidadTF.getCodigo());
      }
      // Comprobamos que la unidad que nos envian no sea null
      // (ocurre en el caso de que actualicemos y no se haya actualizado en el origen)
      if(unidadTF != null){
          log.info("ORGANISMO ACTUALIZADO/SINCRONIZADO: " + unidadTF.getDenominacion() );
          // Comprobamos primero si ya existe el organismo
          organismo = organismoEjb.findByCodigo(unidadTF.getCodigo());

          if(organismo == null){
              log.info("Nuevo organismo: " + unidadTF.getDenominacion());
              organismo = new Organismo();
              procesarOrganismo(organismo, unidadTF, entidad, cacheEstadoEntidad, cacheProvincia, cacheComunidadAutonoma, cacheNivelAdministracion);

              //Guardamos el Organismo
              organismo = organismoEjb.persist(organismo);
          }else{ // Si existe hay que actualizarlo
              procesarOrganismo(organismo, unidadTF, entidad, cacheEstadoEntidad, cacheProvincia, cacheComunidadAutonoma, cacheNivelAdministracion);
          }

          // Es necesario que el organismo esté creado previamente.
          // Asignamos su Organismo Raíz
          Organismo organismoRaiz = organismoEjb.findByCodigo(unidadTF.getCodUnidadRaiz());
          organismo.setOrganismoRaiz(organismoRaiz);


          // Asignamos su Organismo Superior
          Organismo organismoSuperior = organismoEjb.findByCodigo(unidadTF.getCodUnidadSuperior());
          organismo.setOrganismoSuperior(organismoSuperior);


          // Actualizamos el Organismo
          organismo = organismoEjb.merge(organismo);
          log.info("Fin sincronizar organismo: " + organismo.getDenominacion());
          log.info("  ");

           /* borramos cache */
          cacheEstadoEntidad.clear();
          cacheProvincia.clear();
          cacheComunidadAutonoma.clear();
          cacheNivelAdministracion.clear();

      }

       return organismo;
    }

    /**
     * Este método crea todas las oficinas recibidas. Se guardan denominación, estado y organismo responsable
     * @param oficinas conjunto de oficinas recibidas de una entidad
     * @throws Exception
     */
    public void crearActualizarOficinas(Set<OficinaTF> oficinas) throws Exception{

        Map<String, CatEstadoEntidad> cacheEstadoEntidad = montarCacheEstadoEntidad();

        for (OficinaTF oficinaTF : oficinas) {

            if(oficinaTF != null){

                Oficina oficina = oficinaEjb.findByCodigo(oficinaTF.getCodigo());

                if(oficina == null){
                    oficina = new Oficina();
                    oficina.setCodigo(oficinaTF.getCodigo());
                    // Se procesa la oficina para asignar sus valores
                    procesarOficina(oficina, oficinaTF,cacheEstadoEntidad);

                    // Guardamos la Oficina
                    oficina = oficinaEjb.persist(oficina);
                } else {
                    // Se procesa la oficina para asignar sus valores
                    procesarOficina(oficina, oficinaTF, cacheEstadoEntidad);
                    oficina = oficinaEjb.merge(oficina);
                }

            }

        }

        log.info("");
        log.info("Oficinas creadas: " + oficinas.size());
        log.info("");
    }

    /**
     * En este método se asigna la oficina responsable a la lista de oficinas recibidas.
     * @param oficinas
     * @throws Exception
     */
    public void asignarOficinasResponsables(Set<OficinaTF> oficinas) throws Exception{

        for (OficinaTF oficinaTF : oficinas) {

            if(oficinaTF != null){

                // OficinaResponsable
                if(oficinaTF.getCodOfiResponsable() != null){

                    Oficina oficina = oficinaEjb.findByCodigo(oficinaTF.getCodigo());
                    Oficina oficinaResponsable = oficinaEjb.findByCodigo(oficinaTF.getCodOfiResponsable());
                    if(oficinaResponsable != null) {
                       oficina.setOficinaResponsable(oficinaResponsable);
                       oficinaEjb.merge(oficina);
                    }else{
                        log.info("TIENE OFICINA RESPONSABLE, PERO NO LA ENCUENTRA: " + oficinaTF.getCodOfiResponsable());
                    }
                }

            }
        }
    }


    /**
     * Método que crea todas las relaciones organizativas de las oficinas recibidas
     * @param oficinas oficinas de la entidad
     * @throws Exception
     */
    public void crearRelacionesOrganizativas(Set<OficinaTF> oficinas) throws Exception{

        log.info("RELACIONES ORGANIZATIVAS");
        log.info("");

        for (OficinaTF oficinaTF : oficinas) {

            if (oficinaTF != null) {

                if(oficinaTF.getOrganizativasOfi() != null){

                    List<RelacionOrganizativaOfiTF> relacionOrganizativaOfiTFList = oficinaTF.getOrganizativasOfi();
                    Oficina oficina = oficinaEjb.findByCodigo(oficinaTF.getCodigo());

                    log.info("Relaciones organizativas " +oficinaTF.getDenominacion() + " - " + oficinaTF.getCodigo() +": "  + relacionOrganizativaOfiTFList.size());

                    //Borramos las relaciones existentes para el caso de la actualizacion
                    log.info("Relaciones ORG eliminadas: " + relacionOrganizativaOfiEjb.deleteByOficina(oficina.getId()));

                    for (RelacionOrganizativaOfiTF relacionOrganizativaOfiTF : relacionOrganizativaOfiTFList) {
                        log.info("");

                        RelacionOrganizativaOfi relacionOrganizativaOfi = new RelacionOrganizativaOfi();

                        CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionOrganizativaOfiTF.getEstado());
                        relacionOrganizativaOfi.setEstado(catEstadoEntidad);

                        relacionOrganizativaOfi.setOficina(oficina);

                        Organismo organismoOrg = organismoEjb.findByCodigo(relacionOrganizativaOfiTF.getUnidad());

                        relacionOrganizativaOfi.setOrganismo(organismoOrg);

                        log.info("Relacion ORG entre " + oficina.getDenominacion() + " - " + organismoOrg.getDenominacion());
                        relacionOrganizativaOfi = relacionOrganizativaOfiEjb.persist(relacionOrganizativaOfi);

                    }

                }

            }
            log.info("");
        }
    }

    /**
     * Método que crea todas las relaciones SIR de las oficinas recibidas
     * @param oficinas oficinas de la entidad
     * @throws Exception
     */
    public void  crearRelacionesSir(Set<OficinaTF> oficinas) throws Exception{

        log.info("RELACIONES SIR");
        log.info("");

        for (OficinaTF oficinaTF : oficinas) {

            if (oficinaTF != null) {

                if(oficinaTF.getSirOfi() != null){

                    List<RelacionSirOfiTF> relacionSirOfiTFList = oficinaTF.getSirOfi();
                    Oficina oficina = oficinaEjb.findByCodigo(oficinaTF.getCodigo());

                    log.info("Relaciones SIR " +oficinaTF.getDenominacion() + " - " + oficinaTF.getCodigo() +": "  + relacionSirOfiTFList.size());

                    //Borramos las relaciones existentes para el caso de la actualizacion
                    log.info("Relaciones SIR eliminadas: " + relacionSirOfiEjb.deleteByOficina(oficina.getId()));

                    for (RelacionSirOfiTF relacionSirOfiTF : relacionSirOfiTFList) {
                        log.info("");

                        RelacionSirOfi relacionSirOfi = new RelacionSirOfi();

                        CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionSirOfiTF.getEstado());
                        relacionSirOfi.setEstado(catEstadoEntidad);

                        relacionSirOfi.setOficina(oficina);

                        Organismo organismoOrg = organismoEjb.findByCodigo(relacionSirOfiTF.getUnidad());

                        relacionSirOfi.setOrganismo(organismoOrg);

                        log.info("Relacion SIR entre " + oficina.getDenominacion() + " - " + organismoOrg.getDenominacion());
                        relacionSirOfi = relacionSirOfiEjb.persist(relacionSirOfi);

                    }

                }

            }
            log.info("");
        }

    }


    /**
     *  Crea un {@link es.caib.regweb3.model.Oficina} a partir de una OficinaTF
     *  Este método se emplea tanto en la sincronización como en la actualización
     * @param oficinaTF
     * @throws Exception
     */
    /** OJO ESTE MÉTODO YA NO SE USA. Ahora se ha descompuesto en partes.
     * Los métodos que lo sustituyen son
     *  crearActualizarOficinas(todasOficinasEntidad);
        asignarOficinasResponsables(todasOficinasEntidad);
        crearRelacionesOrganizativas(todasOficinasEntidad);
        crearRelacionesSir(todasOficinasEntidad);*/
    public void sincronizarOficina(OficinaTF oficinaTF) throws Exception {
      // Comprobamos que la oficina que nos envian no sea null
      // (ocurre en el caso de que actualicemos y no se haya actualizado en el origen)

      Map<String, CatEstadoEntidad> cacheEstadoEntidad = montarCacheEstadoEntidad();
        if(oficinaTF != null){
            log.info("OFICINA ACTUALIZADA/SINCRONIZADA: " + oficinaTF.getDenominacion() );
            // Comprobamos primero si existe la oficina
            Oficina oficina = oficinaEjb.findByCodigo(oficinaTF.getCodigo());

            if(oficina == null){
                oficina = new Oficina();
                oficina.setCodigo(oficinaTF.getCodigo());
                // Se procesa la oficina para asignar sus valores
                procesarOficina(oficina, oficinaTF,cacheEstadoEntidad);

                // Guardamos la Oficina
                oficina = oficinaEjb.persist(oficina);
            } else {
                // Se procesa la oficina para asignar sus valores
                procesarOficina(oficina, oficinaTF, cacheEstadoEntidad);
            }

            // OficinaResponsable
            if(oficinaTF.getCodOfiResponsable()!=null){
                log.info("Oficina responsable: " + oficinaTF.getCodOfiResponsable());
                Oficina oficinaResponsable = oficinaEjb.findByCodigo(oficinaTF.getCodOfiResponsable());
                if(oficinaResponsable != null) {
                    oficina.setOficinaResponsable(oficinaResponsable);
                    log.info("Oficina: " + oficina);
                    oficina = oficinaEjb.merge(oficina);
                }else{
                    log.info("TIENE OFICINA RESPONSABLE, PERO NO LA ENCUENTRA");
                }
            }


            //RELACIONES

            //Borramos las relaciones existentes para el caso de la actualizacion
            log.info("Relaciones SIR eliminadas: " + relacionSirOfiEjb.deleteByOficina(oficina.getId()));


            // RelacionSirOfi
            if(oficinaTF.getSirOfi() != null){
                List<RelacionSirOfiTF> relacionSirOfiTFList = oficinaTF.getSirOfi();
                log.info("");
                log.info("Relaciones SIR " +oficinaTF.getDenominacion() +": " + oficinaTF.getSirOfi().size());

                for (RelacionSirOfiTF relacionSirOfiTF : relacionSirOfiTFList) {

                    RelacionSirOfi relacionSirOfi = new RelacionSirOfi();

                    CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionSirOfiTF.getEstado());
                    relacionSirOfi.setEstado(catEstadoEntidad);

                  /*  Oficina oficinaSir = oficinaEjb.findByCodigo(relacionSirOfiTF.getOficina());
                    log.info("Oficina Org: " + oficinaSir.getCodigo());*/
                    relacionSirOfi.setOficina(oficina);

                    Organismo organismoSir = organismoEjb.findByCodigo(relacionSirOfiTF.getUnidad());
                    log.info("Relacion SIR entre " + oficina.getDenominacion() + " - " + organismoSir.getDenominacion());
                    relacionSirOfi.setOrganismo(organismoSir);

                    //Guardarmos al relación

                    relacionSirOfi = relacionSirOfiEjb.persist(relacionSirOfi);

                }

            }

            log.info("");
            log.info("");

            //Borramos las relaciones existentes para el caso de la actualizacion
            log.info("Relaciones ORG eliminadas: " + relacionOrganizativaOfiEjb.deleteByOficina(oficina.getId()));

            if(oficinaTF.getOrganizativasOfi() != null){

                List<RelacionOrganizativaOfiTF> relacionOrganizativaOfiTFList = oficinaTF.getOrganizativasOfi();
                log.info("Relaciones organizativas " +oficinaTF.getDenominacion() + " - " + oficinaTF.getCodigo() +": "  + relacionOrganizativaOfiTFList.size());

               // List<RelacionOrganizativaOfi> relacionOrganizativaOfiList = new ArrayList<RelacionOrganizativaOfi>();
                for (RelacionOrganizativaOfiTF relacionOrganizativaOfiTF : relacionOrganizativaOfiTFList) {
                    log.info("");
                    log.info("Entramos en RelacionesOrganizativasOFI ");

                    RelacionOrganizativaOfi relacionOrganizativaOfi = new RelacionOrganizativaOfi();

                    CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionOrganizativaOfiTF.getEstado());
                    relacionOrganizativaOfi.setEstado(catEstadoEntidad);

                    /*Oficina oficinaOrg = oficinaEjb.findByCodigo(relacionOrganizativaOfiTF.getOficina());
                    log.info("Oficina Org: " + oficinaOrg.getCodigo());*/
                    relacionOrganizativaOfi.setOficina(oficina);


                    Organismo organismoOrg = organismoEjb.findByCodigo(relacionOrganizativaOfiTF.getUnidad());

                    relacionOrganizativaOfi.setOrganismo(organismoOrg);

                    log.info("Relacion ORG entre " + oficina.getDenominacion() + " - " + organismoOrg.getDenominacion());
                    relacionOrganizativaOfi = relacionOrganizativaOfiEjb.persist(relacionOrganizativaOfi);

                   // relacionOrganizativaOfiList.add(relacionOrganizativaOfi);
                }
               // oficina.setOrganizativasOfi(relacionOrganizativaOfiList);
            }

            /* borramos cache */
          cacheEstadoEntidad.clear();
          //oficina = oficinaEjb.merge(oficina);

        }

    }


  /**
   * Método que sincroniza los históricos de un organismo. Se debe ejecutar después de sincronizarlos todos.
   * @param organismo organismo al que guardar los históricos
   * @param unidadTF  unidad transferida equivalente al organismo que nos proporciona los historicos.
   * @throws Exception
   */
    public void sincronizarHistoricosOrganismo(Organismo organismo, UnidadTF unidadTF) throws Exception {
      List<String> historicos =unidadTF.getHistoricosUO();
      Set<Organismo> historicosOrg = organismo.getHistoricoUO();
      if(!historicos.isEmpty()){
        log.info("UnidadTF : " + unidadTF.getCodigo() +" Historicos: "+ historicos.size());
      }
      // Si l'organisme no te històrics, inicialitzam la variable.
      if(historicosOrg == null){
        historicosOrg = new HashSet<Organismo>();
      }

      for(String historico : historicos){
        Organismo orgUltima = organismoEjb.findByCodigo(historico);
        historicosOrg.add(orgUltima);
      }
       organismo.setHistoricoUO(historicosOrg);
       organismoEjb.merge(organismo);

    }


    /**
   * Método que obtiene organismos destinatarios que tienen oficinas donde registrar
     TODO borrar, solo se emplea para testear
   * @throws Exception
   */
    /*public void obtenerOrganismosDestinatarios(String codigo) throws Exception {
        Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
        List<UnidadTF> arbol = unidadesService.obtenerArbolUnidadesDestinatarias(codigo);

        log.info("Organismos destinatarios encontrados " + arbol.size());
    }*/


    /**
     * Función que actualiza un conjunto de datos del organismo
     * @param organismo organismo a actualizar
     * @param unidadTF  datos de la unidad transferida desde dir3caib
     * @param entidad entidad que se está actualizando
     * @param cacheEstadoEntidad
     * @param cacheProvincia
     * @param cacheComunidadAutonoma
     * @param cacheNivelAdministracion
     * @throws Exception
     */
    private void procesarOrganismo(Organismo organismo, UnidadTF unidadTF, Entidad entidad,
        Map<String, CatEstadoEntidad> cacheEstadoEntidad, Map<Long,CatProvincia> cacheProvincia,
        Map<Long,CatComunidadAutonoma> cacheComunidadAutonoma,
        Map<Long, CatNivelAdministracion> cacheNivelAdministracion) throws Exception{


        CatEstadoEntidad estado = cacheEstadoEntidad.get(unidadTF.getCodigoEstadoEntidad());

        organismo.setCodigo(unidadTF.getCodigo());
        organismo.setEstado(estado);
        organismo.setEntidad(entidad);
        organismo.setDenominacion(unidadTF.getDenominacion());
        organismo.setNivelJerarquico(unidadTF.getNivelJerarquico());



        //Nivel Administracion
        CatNivelAdministracion nivelAdministracion = cacheNivelAdministracion.get(unidadTF.getNivelAdministracion());
        organismo.setNivelAdministracion(nivelAdministracion);

        if(unidadTF.getCodAmbProvincia() != null){

          CatProvincia provincia = cacheProvincia.get(unidadTF.getCodAmbProvincia());
          organismo.setCodAmbProvincia(provincia);
        }else {
            organismo.setCodAmbProvincia(null);
        }

        if(unidadTF.getCodAmbComunidad() != null){

          CatComunidadAutonoma comunidadAutonoma = cacheComunidadAutonoma.get(unidadTF.getCodAmbComunidad());
          organismo.setCodAmbComunidad(comunidadAutonoma);
        }else {
            organismo.setCodAmbComunidad(null);
        }

        if(unidadTF.getCodigoAmbPais() != null){organismo.setCodPais(catPaisEjb.findByCodigo(unidadTF.getCodigoAmbPais()));}
        if(!StringUtils.isEmpty(unidadTF.getDescripcionLocalidad())){organismo.setLocalidad(catLocalidadEjb.findByNombre(unidadTF.getDescripcionLocalidad()));}
        if(unidadTF.getCodigoTipoVia() != null){organismo.setTipoVia(catTipoViaEjb.findByCodigo(unidadTF.getCodigoTipoVia()));}
        if(!StringUtils.isEmpty(unidadTF.getNombreVia())){organismo.setNombreVia(unidadTF.getNombreVia());}
        if(!StringUtils.isEmpty(unidadTF.getNumVia())){organismo.setNumVia(unidadTF.getNumVia());}
        if(!StringUtils.isEmpty(unidadTF.getCodPostal())){organismo.setCodPostal(unidadTF.getCodPostal());}

    }

    /**
     * Función que actualiza el estado y el organismo responsable de una oficina
     * @param oficina oficina a tratar
     * @param oficinaTF oficina transferida desde dir3caib
     * @param cacheEstadoEntidad caché de EstadoEntidad
     * @throws Exception
     */
    private void procesarOficina(Oficina oficina, OficinaTF oficinaTF, Map<String, CatEstadoEntidad> cacheEstadoEntidad) throws Exception {

        oficina.setDenominacion(oficinaTF.getDenominacion());


        CatEstadoEntidad estado = cacheEstadoEntidad.get(oficinaTF.getEstado());
        oficina.setEstado(estado);

        Organismo organismoResponsable = organismoEjb.findByCodigo(oficinaTF.getCodUoResponsable());
        oficina.setOrganismoResponsable(organismoResponsable);

        if(oficinaTF.getCodigoPais() != null){oficina.setCodPais(catPaisEjb.findByCodigo(oficinaTF.getCodigoPais()));}
        if(oficinaTF.getCodigoComunidad() != null){oficina.setCodComunidad(catComunidadAutonomaEjb.findByCodigo(oficinaTF.getCodigoComunidad()));}

        if(!StringUtils.isEmpty(oficinaTF.getDescripcionLocalidad())){oficina.setLocalidad(catLocalidadEjb.findByNombre(oficinaTF.getDescripcionLocalidad()));}

        if(oficinaTF.getCodigoTipoVia() != null){oficina.setTipoVia(catTipoViaEjb.findByCodigo(oficinaTF.getCodigoTipoVia()));}
        if(!StringUtils.isEmpty(oficinaTF.getNombreVia())){oficina.setNombreVia(oficinaTF.getNombreVia());}
        if(!StringUtils.isEmpty(oficinaTF.getNumVia())){oficina.setNumVia(oficinaTF.getNumVia());}
        if(!StringUtils.isEmpty(oficinaTF.getCodPostal())){oficina.setCodPostal(oficinaTF.getCodPostal());}

        if(oficinaTF.getServicios() != null && oficinaTF.getServicios().size() > 0){

            Set<CatServicio> servicios = new HashSet<CatServicio>();

            for (Long servicio : oficinaTF.getServicios()) {
                servicios.add(catServicioEjb.findByCodigo(servicio));
            }

            oficina.setServicios(servicios);
        }

    }

    private Map<String, CatEstadoEntidad>  montarCacheEstadoEntidad() throws Exception{

         Map<String, CatEstadoEntidad> cacheEstadoEntidad = new TreeMap<String,CatEstadoEntidad>();
          for (CatEstadoEntidad ca : catEstadoEntidadEjb.getAll()) {
            cacheEstadoEntidad.put(ca.getCodigoEstadoEntidad(), ca);
          }

          return cacheEstadoEntidad;

    }

    /**
     * Función que crea una nueva entrada en la tabla RWE_DESCARGA que indica que se ha producido una nueva descarga
     * (sincronización o actualización) de la entidad indicada.
     * @param tipo indica organismo o oficina.
     * @param entidad entidad descargada
     * @throws Exception
     */
    private void nuevaDescarga(String tipo, Entidad entidad) throws Exception {
        Descarga descarga = new Descarga();
        descarga.setTipo(tipo);
        descarga.setEntidad(entidad);
        Date hoy = new Date();
        descarga.setFechaImportacion(hoy);

        descarga = descargaEjb.persist(descarga);
    }

    /**
     * Función que crea una entrada en la tabla de RWE_PENDIENTE que indica que es un organismo que está pendiente
     * de procesar(reasignar sus libros a los organismos que lo sustituyen). Se crea según el estado del organismo
     * recibido y si tiene libros.
     * @param org organismo a tratar
     * @throws Exception
     */
    private void guardarPendiente(Organismo org) throws Exception {
        if(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO.equals(org.getEstado().getCodigoEstadoEntidad())
                || RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO.equals(org.getEstado().getCodigoEstadoEntidad())
                || RegwebConstantes.ESTADO_ENTIDAD_ANULADO.equals(org.getEstado().getCodigoEstadoEntidad())){
                    if(org.getLibros()!=null){
                        Pendiente pendiente = new Pendiente();
                        pendiente.setIdOrganismo(org.getId());
                        pendiente.setProcesado(false);
                        pendiente.setEstado(org.getEstado().getCodigoEstadoEntidad());
                        pendienteEjb.persist(pendiente);
                    }
        }
    }
}
