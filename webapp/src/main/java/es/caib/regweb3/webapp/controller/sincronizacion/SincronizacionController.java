package es.caib.regweb3.webapp.controller.sincronizacion;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created 26/02/2025
 * Controller que gestiona todas las operaciones con {@link Descarga}
 *
 * @author earrivi
 */
@Controller
@RequestMapping(value = "/sincronizacion")
public class SincronizacionController extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LoginService loginService;

    @EJB(mappedName = DescargaLocal.JNDI_NAME)
    private DescargaLocal descargaEjb;

    @EJB(mappedName = SincronizadorDir3Local.JNDI_NAME)
    private SincronizadorDir3Local sincronizadorDIR3Ejb;

    @EJB(mappedName = PendienteLocal.JNDI_NAME)
    private PendienteLocal pendienteEjb;

    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;


    /**
     * Listado de todas las Sincronizaciones
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String sincronizacionList() {
        return "redirect:/sincronizacion/list/1";
    }


    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView sincronizacionList(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sincronizacion/sincronizacionList");
        Entidad entidad = getEntidadActiva(request);

        List<Descarga> listado = descargaEjb.getPaginationByEntidad((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = descargaEjb.getTotalByEntidad(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);
        mav.addObject("entidad", entidad);

        return mav;
    }

    /**
     * Actualizamos una {@link es.caib.regweb3.model.Entidad} de dir3caib
     */
    @RequestMapping(value = "/{entidadId}/actualizar")
    public String actualizar(@PathVariable Long entidadId, HttpServletRequest request) throws Exception {

        try {

            //Marcamos la entidad de mantenimiento
            entidadEjb.marcarEntidadMantenimiento(entidadId, true);

            Descarga ultimaDescarga = descargaEjb.ultimaDescarga(RegwebConstantes.DESCARGA_UNIDAD, entidadId);
            Timestamp fechaUltimaActualizacion = null;
            if (ultimaDescarga.getFechaImportacion() != null) {
                fechaUltimaActualizacion = new Timestamp(ultimaDescarga.getFechaImportacion().getTime());
            }

            // Establecemos la fecha de la primera sincronizacion
            Descarga primeraDescarga = descargaEjb.primeraDescarga(RegwebConstantes.DESCARGA_UNIDAD, entidadId);
            Timestamp fechaSincronizacion = null;
            if (primeraDescarga.getFechaImportacion() != null) {
                fechaSincronizacion = new Timestamp(primeraDescarga.getFechaImportacion().getTime());
            }

            sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, fechaUltimaActualizacion, fechaSincronizacion);

            // actualizamos nombre y codigo de la entidad, si la unidad raiz a la que representa se ha extinguido.
            actualizarEntidadExtincionUnidadRaiz(entidadId, request);

            // Comprobamos si hay algún confilto pendiente de procesar
            if(pendienteEjb.findPendientesProcesar(entidadId).isEmpty()){
                Mensaje.saveMessageInfo(request, getMessage("organismo.sincronizar.ok"));
                return "redirect:/sincronizacion/list";
            }else{
                return ("redirect:/sincronizacion/procesarPendientes");
            }

        } catch (Exception e) {
            log.error("Error actualizacion", e);
            entidadEjb.marcarEntidadMantenimiento(entidadId, false);
            Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nook") + ": " + e.getMessage());
            return "redirect:/organismo/list";
        }

    }

    /**
     * Sincronizamos una {@link es.caib.regweb3.model.Entidad} de dir3caib
     * @param entidadId
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{entidadId}/sincronizar")
    public JsonResponse sincronizar(@PathVariable Long entidadId, HttpServletRequest request) throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {
            //Marcamos la entidad en mantenimiento
            entidadEjb.marcarEntidadMantenimiento(entidadId, true);

            //Iniciamos proceso sincronización
            sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, null, null);

            jsonResponse.setError(getMessage("regweb.sincronizados"));
            jsonResponse.setStatus("SUCCESS");

            Mensaje.saveMessageInfo(request, getMessage("organismo.sincronizar.ok"));

            //Asociamos el Organismo raíz creado al Libro de la Entidad
            Entidad entidad = entidadEjb.findById(entidadId);
            entidad.getLibro().setOrganismo(organismoEjb.findByCodigo(entidad.getCodigoDir3()));
            entidadEjb.merge(entidad);

            entidadEjb.marcarEntidadMantenimiento(entidadId, false);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("regweb.sincronizacion.nook") + ": " + e.getMessage());
        }

        return jsonResponse;

    }

    /**
     * Función que gestiona los organismos extinguidos. Procesa los que son automáticos(1 historico) y
     * prepara los datos para los que no son automáticos y los envía al jsp para procesar manualmente
     * Esta función se llama justo después del proceso de sincronización de una entidad desde dir3.
     * En el InicioInterceptor mira si hay organismos pendientes de procesar y si hay viene aquí.
     */
    @RequestMapping(value = "/procesarPendientes", method = RequestMethod.GET)
    public String procesarPendientes(HttpServletRequest request, Model model) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        /* Preparamos todos los organismos a procesar (extinguidos, anulados, transitorios,vigentes)*/
        List<Pendiente> pendientesDeProcesar = pendienteEjb.findPendientesProcesar(entidad.getId());

        if (!pendientesDeProcesar.isEmpty()) {

            // Para mostrar información al usuario
            Map<String, Organismo> extinguidosAutomaticos = new HashMap<String, Organismo>();// Organismos extinguidos y sustitutos procesados automaticamente

            for (Pendiente pendiente : pendientesDeProcesar) {

                if (RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_VIGENTE.equals(pendiente.getEstado())) {
                    // Obtenemos el organismo extinguido
                    Organismo organismoExtinguido = organismoEjb.findByIdLigero(pendiente.getIdOrganismo());

                    //Obtenemos los permisos del organismo extinguido
                    List<PermisoOrganismoUsuario> permisos = permisoOrganismoUsuarioEjb.findByOrganismo(organismoExtinguido.getId());

                    // Si tiene permisos, hay que cambiarle el Organismo del que dependen
                    if(permisos.size() > 0){
                        log.info("Buscando sustitutos de: " + organismoExtinguido.getDenominacion() + " - " + organismoExtinguido.getCodigo());
                        Set<Organismo> sustitutosOficina = obtenerSustitutosOficina(organismoExtinguido.getId());

                        if(sustitutosOficina.size() == 0){ // Error, no hay ningún Organismos sustituto

                            //todo eliminar permisos del Organismo extinguido

                        }else { // Se procesa automáticamente

                            // Para todos los sustitutos, asignamos los usuarios que tenía el anterior Organismo
                            for(Organismo organismoSustituto:sustitutosOficina){

                                // Activamos la posibilidad de asociarle usuarios
                                organismoEjb.activarUsuarios(organismoSustituto.getId());

                                // Actualizamos el Organismo sustituto todos los permisos
                                for(PermisoOrganismoUsuario permiso:permisos){
                                    permiso.setOrganismo(organismoSustituto);
                                    permisoOrganismoUsuarioEjb.merge(permiso);
                                }

                                // Añadimos todos los organimos procesados automáticamente
                                extinguidosAutomaticos.put(organismoExtinguido.getDenominacion(), organismoSustituto);
                            }

                            // Actualizamos el destino de los registros pendientes de llegada uno de los Organismos sustitutos
                            Organismo organismoSustituto = new ArrayList<>(sustitutosOficina).get(0);
                            oficioRemisionEjb.actualizarDestinoPendientesLlegada(organismoExtinguido.getId(), organismoSustituto.getId());

                        }
                    }

                    // Actualizamos el Pendiente
                    pendiente.setProcesado(true);
                    pendiente.setFecha(TimeUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                    pendienteEjb.merge(pendiente);
                }
            }

            model.addAttribute("extinguidosAutomaticos", extinguidosAutomaticos); // organismos que se les ha asignado automaticamente permisos.

            // Quitamos el modo mantenimiento de la Entidad
            //entidadEjb.marcarEntidadMantenimiento(entidad.getId(), false);

        } else {
            // Quitamos el modo mantenimiento de la Entidad
            //entidadEjb.marcarEntidadMantenimiento(entidad.getId(), false);

            Mensaje.saveMessageInfo(request, getMessage("organismo.sincronizar.ok"));
            return "redirect:/sincronizacion/list";
        }

        return "organismo/resumenSincronizacion";
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.TipoAsunto}
     */
    @RequestMapping(value = "/{idDescarga}/delete")
    public String eliminar(@PathVariable Long idDescarga, HttpServletRequest request) {

        try {
            Descarga descarga = descargaEjb.findById(idDescarga);
            descargaEjb.remove(descarga);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/sincronizacion/list";
    }

    /**
     * MétFunciónodo que actualiza el nombre y el código dir3 de la entidad, debido a que la unidad raiz a la que representa se ha extinguido en dir3caib.
     *
     * @param entidadId
     * @param request
     * @throws Exception
     */
    private void actualizarEntidadExtincionUnidadRaiz(Long entidadId, HttpServletRequest request) throws Exception {
        Entidad entidad = entidadEjb.findById(entidadId);
        Organismo organismoRaizEntidad = organismoEjb.findByCodigoEntidadSinEstado(entidad.getCodigoDir3(), entidadId);
        Set<Organismo> historicosFinales = new HashSet<Organismo>();
        organismoEjb.obtenerHistoricosFinales(organismoRaizEntidad.getId(), historicosFinales);
        if (!historicosFinales.isEmpty()) {
            if (historicosFinales.size() == 1) {
                entidad.setCodigoDir3((historicosFinales.iterator().next()).getCodigo());
                entidad.setNombre((historicosFinales.iterator().next()).getDenominacion());
                entidadEjb.merge(entidad);
                loginService.cambioEntidad(entidad, getLoginInfo(request));
            } else {
                throw new Exception("La raiz se ha dividido en más de un organismo, houston tenemos un problema");
            }

        }
    }

    /**
     * Obtiene el/los organismos con oficinas que sustituyen a uno Extinguido
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    private Set<Organismo> obtenerSustitutosOficina(Long idOrganismo) throws Exception{

        Set<Organismo> sustitutos =  new HashSet<>();
        Set<Organismo> sustitutosOficina =  new HashSet<>();
        organismoEjb.obtenerHistoricosFinales(idOrganismo,sustitutos);

        for (Organismo orgHistorico : sustitutos) {
            if (oficinaEjb.tieneOficinasServicio(orgHistorico.getId(), RegwebConstantes.OFICINA_VIRTUAL_SI)) {
                sustitutosOficina.add(orgHistorico);
            }
        }

        return sustitutosOficina;
    }

}
