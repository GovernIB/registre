package es.caib.regweb3.webapp.controller.repro;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ReproJson;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.OrganismoLocal;
import es.caib.regweb3.persistence.ejb.ReproLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.ReproValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created 16/07/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Repro}
 * @author jpernia
 */
@Controller
@RequestMapping(value = "/repro")
@SessionAttributes(types = Repro.class)
public class ReproController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());
    
    @EJB(mappedName = "regweb3/ReproEJB/local")
    public ReproLocal reproEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/UsuarioEJB/local")
    public UsuarioLocal usuarioEjb;

    @Autowired
    private ReproValidator reproValidator;

    /**
     * Listado de todas las Repros de un Usuario Entidad
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listadoRepro() {
        return "redirect:/repro/list/1";
    }

    /**
     * Listado de Repros
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView listRepro(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("repro/reproList");

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        List<Repro> listado = reproEjb.getPaginationUsuario((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, usuarioEntidad.getId());
        Long total = reproEjb.getTotalbyUsuario(usuarioEntidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }


    /**
     * Crea una Repro
     * @param reproJson
     * @param request
     * @return
     */
    @RequestMapping(value = "/new/{tipoRegistro}", method = RequestMethod.POST)
    @ResponseBody
    public Long nuevaRepro(@PathVariable Long tipoRegistro, @RequestBody ReproJson reproJson, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        Repro repro = new Repro();
        repro.setNombre(reproJson.getNombreRepro());
        repro.setTipoRegistro(tipoRegistro);
        repro.setUsuario(usuarioEntidad);

        switch (tipoRegistro.intValue()){

            case 1: //RegistroEntrada
                log.info("Repro entrada");
                Organismo organismoDestino = organismoEjb.findByCodigoEntidad(reproJson.getDestinoCodigo(), usuarioEntidad.getEntidad().getId());

                if(organismoDestino != null) { // es interno
                    log.info("Destino: " +reproJson.getDestinoDenominacion() + " Interno");
                    reproJson.setDestinoExterno(false);

                }else{ // es externo
                    reproJson.setDestinoExterno(true);
                    log.info("Destino: " +reproJson.getDestinoDenominacion() + " Externo");
                }

            break;

            case 2: //RegistroSalida
                log.info("Repro salida");
                Organismo organismoOrigen = organismoEjb.findByCodigoEntidad(reproJson.getOrigenCodigo(), usuarioEntidad.getEntidad().getId());

                if(organismoOrigen != null) { // es interno
                    log.info("Origen: " + reproJson.getOrigenDenominacion() + " Interno");
                    reproJson.setOrigenExterno(false);

                }else{ // es externo
                    reproJson.setOrigenExterno(true);
                    log.info("Origen: " +reproJson.getOrigenDenominacion() + " Externo");
                }

            break;
        }

        log.info("OficinaCodigo: " + reproJson.getOficinaCodigo());
        if (!reproJson.getOficinaCodigo().equals("-1")) {

            Oficina oficina = oficinaEjb.findByCodigo(reproJson.getOficinaCodigo());

            if (oficina != null) { // es interna
                log.info("Oficina: " + reproJson.getOficinaDenominacion() + " Interno");
                reproJson.setOficinaExterna(false);
            } else { // es externa
                log.info("Oficina: " + reproJson.getOficinaDenominacion() + " Externa");
                reproJson.setOficinaExterna(true);
            }
        }

        repro.setRepro(RegistroUtils.serilizarXml(reproJson));

        int orden = 0;
        List<Repro> repros = reproEjb.getAllbyUsuario(usuarioEntidad.getId());
        if(repros.size() > 0){
            orden = reproEjb.maxOrdenRepro(usuarioEntidad.getId());
        }

        repro.setOrden(orden+1);

        try {
            repro = reproEjb.persist(repro);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return repro.getId();
    }

    /**
     * Carga el formulario para modificar una {@link es.caib.regweb3.model.Repro}
     */
    @RequestMapping(value = "/{reproId}/edit", method = RequestMethod.GET)
    public String editarRepro(@PathVariable("reproId") Long reproId, Model model, HttpServletRequest request) {

        Repro repro = null;
        try {
            repro = reproEjb.findById(reproId);

            // Comprueba que la Repro existe
            if (repro == null) {
                log.info("No existe esta repro");
                Mensaje.saveMessageError(request, getMessage("aviso.repro.noExiste"));
                return "redirect:/repro/list";
            }

            // Mira si la Repro pertenece al UsuarioEntidadActivo
            if (!repro.getUsuario().equals(getUsuarioEntidadActivo(request))) {

                Mensaje.saveMessageError(request, getMessage("aviso.repro.edit"));
                return "redirect:/repro/list";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(repro);

        return "repro/reproForm";
    }

    /**
     * Editar una {@link es.caib.regweb3.model.Repro}
     */
    @RequestMapping(value = "/{reproId}/edit", method = RequestMethod.POST)
    public String editarRepro(@ModelAttribute @Valid Repro repro, BindingResult result,
                              SessionStatus status, HttpServletRequest request) {

        reproValidator.validate(repro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "repro/reproForm";
        } else { // Si no hay errores actualizamos el registro

            try {
                reproEjb.merge(repro);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));


            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/repro/list";
        }
    }


    /**
     * Cambia estado de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRepro}/cambiarEstado", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean cambiarEstadoRepro(@PathVariable Long idRepro)throws Exception {

        return reproEjb.cambiarEstado(idRepro);

    }


    /**
     * Sube el orden de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRepro}/subir", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean subirRepro(@PathVariable Long idRepro)throws Exception {

        return reproEjb.subirOrden(idRepro);

    }


    /**
     * Baja el orden de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRepro}/bajar", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean bajarRepro(@PathVariable Long idRepro)throws Exception {

        return reproEjb.bajarOrden(idRepro);

    }


    /**
     * Eliminar una {@link es.caib.regweb3.model.Repro}
     */
    @RequestMapping(value = "/{reproId}/delete")
    public String eliminarRepro(@PathVariable Long reproId, HttpServletRequest request) {

        try {

            Repro repro = reproEjb.findById(reproId);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            if(!repro.getUsuario().getId().equals(usuarioEntidad.getId())){
                Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                return "redirect:/repro/list";
            }

            List<Repro> repros = reproEjb.getAllbyUsuario(usuarioEntidad.getId());
            int ordenBorrado = repro.getOrden();

            reproEjb.remove(repro);

            for(int i=ordenBorrado; i<repros.size(); i++) {
                Repro reproCambiar = repros.get(i);
                reproEjb.modificarOrden(reproCambiar.getId(),reproCambiar.getOrden()-1);
            }


            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/repro/list";
    }

    /**
     * Envia una {@link es.caib.regweb3.model.Repro} a un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/enviar/{reproId}/{usuarioEntidadId}")
    public String enviarRepro(@PathVariable Long reproId, @PathVariable Long usuarioEntidadId, HttpServletRequest request) {

        try {
            Repro reproEnviada = reproEjb.findById(reproId);
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(usuarioEntidadId);

            Repro repro = new Repro();
            repro.setNombre(reproEnviada.getNombre());
            repro.setUsuario(usuarioEntidad);
            repro.setTipoRegistro(reproEnviada.getTipoRegistro());
            repro.setRepro(reproEnviada.getRepro());

            int orden = 0;
            List<Repro> repros = reproEjb.getAllbyUsuario(usuarioEntidad.getId());
            if(repros.size() > 0){
                orden = reproEjb.maxOrdenRepro(usuarioEntidad.getId());
            }
            orden = orden + 1;
            repro.setOrden(orden);

            reproEjb.persist(repro);
            Mensaje.saveMessageInfo(request, getMessage("aviso.repro.enviada"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/repro/list";
    }


    @ModelAttribute("usuariosEntidadList")
    public List<UsuarioEntidad> usuariosEntidadList(HttpServletRequest request) throws Exception {

        return usuarioEntidadEjb.findByEntidadSinActivo(getEntidadActiva(request).getId(), getUsuarioAutenticado(request).getId(), RegwebConstantes.TIPO_USUARIO_PERSONA);

    }


    /**
     * Obtiene las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/obtenerRepros", method = RequestMethod.GET)
    public @ResponseBody
    List<Repro> obtenerRepros(@RequestParam Long idUsuario, @RequestParam Long tipoRegistro, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        return reproEjb.getActivasbyUsuario(usuarioEntidad.getId(), tipoRegistro);
    }

    /**
     * Obtiene la {@link es.caib.regweb3.model.Repro} según su identificador.
     *
     */
    @RequestMapping(value = "/obtenerRepro", method = RequestMethod.GET)
    public @ResponseBody
    ReproJson obtenerRepro(@RequestParam Long idRepro, HttpServletRequest request) throws Exception {
        //todo: Mejorar las Repro sustituyendo los organismos extinguidos por sus sustitutos
        Repro repro = reproEjb.findById(idRepro);
        ReproJson reproJson = RegistroUtils.desSerilizarReproXml(repro.getRepro());
        Entidad entidad = getEntidadActiva(request);

        switch (repro.getTipoRegistro().intValue()){

            case 1: //RegistroEntrada

                // Comprobamos la unidad destino
                if(reproJson.getDestinoCodigo()!= null && reproJson.isDestinoExterno()){ // Preguntamos a DIR3 si está Vigente
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
                    UnidadTF unidad = unidadesService.obtenerUnidad(reproJson.getDestinoCodigo(), null, null);

                    if(!unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                        reproJson.setDestinoExterno(null);
                        reproJson.setDestinoCodigo(null);
                        reproJson.setDestinoDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }

                }else{ // Comprobamos en REGWEB3 si está vigente
                    Organismo organismoDestino = organismoEjb.findByCodigoEntidad(reproJson.getDestinoCodigo(), entidad.getId());

                    if(organismoDestino == null){ // Ya no es vigente
                        reproJson.setDestinoExterno(null);
                        reproJson.setDestinoCodigo(null);
                        reproJson.setDestinoDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }
                }
            break;

            case 2: //RegistroSalida
                log.info("Repro salida");

                // Comprobamos la unidad origen
                if(reproJson.getOrigenCodigo()!= null && reproJson.isOrigenExterno()){ // Preguntamos a DIR3 si está Vigente
                    Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService();
                    UnidadTF unidad = unidadesService.obtenerUnidad(reproJson.getOrigenCodigo(), null, null);

                    if(!unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                        reproJson.setOrigenExterno(null);
                        reproJson.setOrigenCodigo(null);
                        reproJson.setOrigenDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }

                }else{ // Comprobamos en REGWEB3 si está vigente
                    Organismo organismoOrigen = organismoEjb.findByCodigoEntidad(reproJson.getOrigenCodigo(), entidad.getId());
                    if(organismoOrigen == null){ // Ya no es vigente
                        reproJson.setOrigenExterno(null);
                        reproJson.setOrigenCodigo(null);
                        reproJson.setOrigenDenominacion(null);
                        repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                        reproEjb.merge(repro);
                    }
                }

            break;


        }

        // Oficina Origen
        if(reproJson.getOficinaCodigo()!= null  && !reproJson.getOficinaCodigo().equals("-1") && reproJson.isOficinaExterna()){// Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
            OficinaTF oficina = oficinasService.obtenerOficina(reproJson.getOficinaCodigo(),null,null);

            if(!oficina.getEstado().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){// Ya no es vigente
                reproJson.setOficinaCodigo(null);
                reproJson.setOficinaDenominacion(null);
                reproJson.setOficinaExterna(null);
                repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                reproEjb.merge(repro);
            }

        }else{// Comprobamos en REGWEB3 si está vigente
            Oficina oficinaOrigen = oficinaEjb.findByCodigoVigente(reproJson.getOficinaCodigo());
            if(oficinaOrigen == null){
                reproJson.setOficinaCodigo(null);
                reproJson.setOficinaDenominacion(null);
                reproJson.setOficinaExterna(null);
                repro.setRepro(RegistroUtils.serilizarXml(reproJson));
                reproEjb.merge(repro);
            }
        }


        return reproJson;
    }




    @InitBinder("repro")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.reproValidator);
    }

}

