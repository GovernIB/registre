package es.caib.regweb.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.oficina.RelacionOrganizativaOfiTF;
import es.caib.dir3caib.ws.api.oficina.RelacionSirOfiTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb.model.*;
import es.caib.regweb.persistence.utils.Dir3CaibUtils;
import es.caib.regweb.utils.RegwebConstantes;
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

    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb/CatEstadoEntidadEJB/local")
    public CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb/RelacionSirOfiEJB/local")
    public RelacionSirOfiLocal relacionSirOfiEjb;

    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiEjb;

    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb/PendienteEJB/local")
    public PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb/DescargaEJB/local")
    public DescargaLocal descargaEjb;



    /*
     * Método que sincroniza o actualiza una entidad de regweb desde dir3caib. Lo hace en función de si se indica la
     * fecha de actualización o no. Si no se indica se sincroniza y si se indica se actualiza
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

        // Si no hay elementos en el arbol es que no hemos actualizado nada, por tanto no registramos la nueva descarga
        if(arbol.size() != 0) {
            // Guardamos los datos de la ultima descarga
            nuevaDescarga(RegwebConstantes.UNIDAD, entidad);
        }

        log.info("");
        log.info("Finalizada la importacion de Organismos");
        log.info("");
        int oficinasActualizadas = 0;
        // Obtenemos por cada Organismo, las Oficinas dependientes de el
        for(Organismo organismo: organismoEjb.findByEntidad(entidadId)){
            List<OficinaTF> oficinas = oficinasService.obtenerArbolOficinas(organismo.getCodigo(),
                fechaActualizacion,fechaSincronizacion);

            // Creamos el arbol de oficinas
            for(OficinaTF oficinaTF:oficinas){
                sincronizarOficina(oficinaTF);
            }
            oficinasActualizadas += oficinas.size();
        }
        // Si no hay elementos en el arbol es que no hemos actualizado nada, por tanto no registramos la nueva descarga
        // Guardamos la fecha de importacion con dir3caib
        if( oficinasActualizadas != 0 ) {
            nuevaDescarga(RegwebConstantes.OFICINA, entidad);
        }

        return arbol.size();


    }

    /**
     * Crea un {@link es.caib.regweb.model.Organismo} a partir de una UnidadTF y lo relaciona con su {@link es.caib.regweb.model.Entidad}
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
      log.info(" Comunidad Autonoma : " + cacheComunidadAutonoma.size());

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
          log.info("ACTUALIZADA/SINCRONIZADA  " + unidadTF.getDenominacion() );
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
     *  Crea un {@link es.caib.regweb.model.Oficina} a partir de una OficinaTF
     *  Este método se emplea tanto en la sincronización como en la actualización
     * @param oficinaTF
     * @throws Exception
     */
    public void sincronizarOficina(OficinaTF oficinaTF) throws Exception {
      // Comprobamos que la oficina que nos envian no sea null
      // (ocurre en el caso de que actualicemos y no se haya actualizado en el origen)

      Map<String, CatEstadoEntidad> cacheEstadoEntidad = montarCacheEstadoEntidad();
        if(oficinaTF != null){
            log.info("ACTUALIZADA/SINCRONIZADA  " + oficinaTF.getDenominacion() );
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
            Oficina oficinaResponsable = oficinaEjb.findByCodigo(oficinaTF.getCodOfiResponsable());
            if(oficinaTF.getCodOfiResponsable()!=null){
                log.info("Oficina responsable: " + oficinaTF.getCodOfiResponsable());
            }
            oficina.setOficinaResponsable(oficinaResponsable);
            log.info("Oficina " + oficina);
            oficina = oficinaEjb.merge(oficina);

            //RELACIONES

            //Borramos las relaciones existentes para el caso de la actualizacion
            relacionSirOfiEjb.deleteByOficina(oficina.getId());


            // RelacionSirOfi
            if(oficinaTF.getSirOfi() != null){

                List<RelacionSirOfiTF> relacionSirOfiTFList = oficinaTF.getSirOfi();
                log.info("oficinaTF.getSirOfi().size() ----" + oficinaTF.getSirOfi().size());

                for (RelacionSirOfiTF relacionSirOfiTF : relacionSirOfiTFList) {

                    RelacionSirOfi relacionSirOfi = new RelacionSirOfi();

                    CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionSirOfiTF.getEstado());
                    relacionSirOfi.setEstado(catEstadoEntidad);

                    Oficina oficinaSir = oficinaEjb.findByCodigo(relacionSirOfiTF.getOficina());
                    log.info("Oficina Org: " + oficinaSir.getCodigo());
                    relacionSirOfi.setOficina(oficinaSir);

                    Organismo organismoSir = organismoEjb.findByCodigo(relacionSirOfiTF.getUnidad());
                    log.info("Organimos Org: " + organismoSir.getCodigo());
                    relacionSirOfi.setOrganismo(organismoSir);


                    //Guardarmos al relación

                    relacionSirOfi = relacionSirOfiEjb.persist(relacionSirOfi);

                }

            }

            //Borramos las relaciones existentes para el caso de la actualizacion
            relacionOrganizativaOfiEjb.deleteByOficina(oficina.getId());
        //    oficina.setOrganizativasOfi(null);
            // RelacionOrganizativaOfi
            if(oficinaTF.getOrganizativasOfi() != null){

                List<RelacionOrganizativaOfiTF> relacionOrganizativaOfiTFList = oficinaTF.getOrganizativasOfi();
                log.info("oficinaTF.getOrganizativasOfi().size() ----" + relacionOrganizativaOfiTFList.size());

                List<RelacionOrganizativaOfi> relacionOrganizativaOfiList = new ArrayList<RelacionOrganizativaOfi>();
                for (RelacionOrganizativaOfiTF relacionOrganizativaOfiTF : relacionOrganizativaOfiTFList) {

                    RelacionOrganizativaOfi relacionOrganizativaOfi = new RelacionOrganizativaOfi();

                    CatEstadoEntidad catEstadoEntidad = catEstadoEntidadEjb.findByCodigo(relacionOrganizativaOfiTF.getEstado());
                    relacionOrganizativaOfi.setEstado(catEstadoEntidad);

                    Oficina oficinaOrg = oficinaEjb.findByCodigo(relacionOrganizativaOfiTF.getOficina());
                    log.info("Oficina Org: " + oficinaOrg.getCodigo());
                    relacionOrganizativaOfi.setOficina(oficinaOrg);

                    log.info("Organismo " + relacionOrganizativaOfiTF.getUnidad());
                    Organismo organismoOrg = organismoEjb.findByCodigo(relacionOrganizativaOfiTF.getUnidad());

                    log.info("Organimos Org: " + organismoOrg.getCodigo());
                    relacionOrganizativaOfi.setOrganismo(organismoOrg);
                    relacionOrganizativaOfi = relacionOrganizativaOfiEjb.persist(relacionOrganizativaOfi);

                    relacionOrganizativaOfiList.add(relacionOrganizativaOfi);
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
        }

        if(unidadTF.getCodAmbComunidad() != null){

          CatComunidadAutonoma comunidadAutonoma = cacheComunidadAutonoma.get(unidadTF.getCodAmbComunidad());
          organismo.setCodAmbComunidad(comunidadAutonoma);
        }

    }

    private void procesarOficina(Oficina oficina, OficinaTF oficinaTF, Map<String, CatEstadoEntidad> cacheEstadoEntidad) throws Exception {
      oficina.setDenominacion(oficinaTF.getDenominacion());

      CatEstadoEntidad estado = cacheEstadoEntidad.get(oficinaTF.getEstado());
      oficina.setEstado(estado);

      Organismo organismoResponsable = organismoEjb.findByCodigo(oficinaTF.getCodUoResponsable());
      oficina.setOrganismoResponsable(organismoResponsable);

    }

    private Map<String, CatEstadoEntidad>  montarCacheEstadoEntidad() throws Exception{

         Map<String, CatEstadoEntidad> cacheEstadoEntidad = new TreeMap<String,CatEstadoEntidad>();
          for (CatEstadoEntidad ca : catEstadoEntidadEjb.getAll()) {
            cacheEstadoEntidad.put(ca.getCodigoEstadoEntidad(), ca);
          }
          log.info(" Estado Entidad : " + cacheEstadoEntidad.size());
          return cacheEstadoEntidad;

    }

    private void nuevaDescarga(String tipo, Entidad entidad) throws Exception {
        Descarga descarga = new Descarga();
        descarga.setTipo(tipo);
        descarga.setEntidad(entidad);
        Date hoy = new Date();
        descarga.setFechaImportacion(hoy);

        descarga = descargaEjb.persist(descarga);
    }

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
