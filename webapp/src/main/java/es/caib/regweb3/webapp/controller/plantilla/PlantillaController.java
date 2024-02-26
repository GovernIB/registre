package es.caib.regweb3.webapp.controller.plantilla;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.PlantillaJson;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.PlantillaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.PlantillaForm;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.PlantillaValidator;
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
 * Controller que gestiona todas las operaciones con {@link Plantilla}
 * @author jpernia
 */
@Controller
@RequestMapping(value = "/plantilla")
@SessionAttributes(types = Plantilla.class)
public class PlantillaController extends BaseController {


    @EJB(mappedName = PlantillaLocal.JNDI_NAME)
    private PlantillaLocal plantillaEjb;

    @Autowired
    private LoginService loginService;

    @Autowired
    private PlantillaValidator plantillaValidator;

    /**
     * Listado de todas las Plantillas de un Usuario Entidad
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listadoPlantilla() {
        return "redirect:/plantilla/list/1";
    }

    /**
     * Listado de Plantillas
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView listPlantilla(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("plantilla/plantillaList");

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        List<Plantilla> listado = plantillaEjb.getPaginationUsuario((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, usuarioEntidad.getId());
        Long total = plantillaEjb.getTotalbyUsuario(usuarioEntidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }

    //todo Pendiente terminar Nuevas Plantilla con Interesados
    /**
     * Crea una Plantilla
     * @param request
     * @return
     */
    @RequestMapping(value = "/nuevo", method = RequestMethod.POST)
    @ResponseBody
    public Long nuevaPlantilla(@RequestBody PlantillaForm plantillaForm, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        Plantilla plantilla = new Plantilla();
        plantilla.setNombre(plantillaForm.getNombreRepro());
        plantilla.setTipoRegistro(Long.valueOf(plantillaForm.getTipoRegistro()));
        plantilla.setUsuario(usuarioEntidad);

        switch (plantillaForm.getTipoRegistro()){

            case 1: //RegistroEntrada
                RegistroEntrada registroEntrada = registroEntradaEjb.findById(plantillaForm.getIdRegistro());
                registroEntrada.setFecha(null);
                registroEntrada.setNumeroRegistroFormateado(null);
                registroEntrada.setNumeroRegistro(null);
                registroEntrada.setEstado(null);
                registroEntrada.getRegistroDetalle().setId(null);

                plantilla.setRepro(RegistroUtils.serilizarXml(registroEntrada));
            break;

            case 2: //RegistroSalida
                RegistroSalida registroSalida = registroSalidaEjb.findById(plantillaForm.getIdRegistro());
                registroSalida.setFecha(null);
                registroSalida.setNumeroRegistroFormateado(null);
                registroSalida.setNumeroRegistro(null);
                registroSalida.setEstado(null);
                registroSalida.getRegistroDetalle().setId(null);

                plantilla.setRepro(RegistroUtils.serilizarXml(registroSalida));
            break;
        }

        int orden = 0;
        List<Plantilla> plantillas = plantillaEjb.getAllbyUsuario(usuarioEntidad.getId());
        if(plantillas.size() > 0){
            orden = plantillaEjb.maxOrdenPlantilla(usuarioEntidad.getId());
        }

        plantilla.setOrden(orden+1);

        try {
            plantilla = plantillaEjb.persist(plantilla);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plantilla.getId();
    }


    /**
     * Crea una Plantilla
     * @param plantillaJson
     * @param request
     * @return
     */
    @RequestMapping(value = "/new/{tipoRegistro}", method = RequestMethod.POST)
    @ResponseBody
    public Long nuevaPlantilla(@PathVariable Long tipoRegistro, @RequestBody PlantillaJson plantillaJson, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        Plantilla plantilla = new Plantilla();
        plantilla.setNombre(plantillaJson.getNombreRepro());
        plantilla.setTipoRegistro(tipoRegistro);
        plantilla.setUsuario(usuarioEntidad);

        switch (tipoRegistro.intValue()){

            case 1: //RegistroEntrada
                Organismo organismoDestino = organismoEjb.findByCodigoByEntidadMultiEntidad(plantillaJson.getDestinoCodigo(), usuarioEntidad.getEntidad().getId());

                if(organismoDestino == null  || !usuarioEntidad.getEntidad().getId().equals(organismoDestino.getEntidad().getId())){//Externo o multientidad
                    plantillaJson.setDestinoExterno(true);
                }else{
                    plantillaJson.setDestinoExterno(false);
                }

                break;

            case 2: //RegistroSalida

                Organismo organismoOrigen = organismoEjb.findByCodigoByEntidadMultiEntidad(plantillaJson.getOrigenCodigo(), usuarioEntidad.getEntidad().getId());

                if(organismoOrigen == null || !usuarioEntidad.getEntidad().getId().equals(organismoOrigen.getEntidad().getId())) { //Externo o multientidad
                    plantillaJson.setOrigenExterno(true);

                }else{ // es interno
                    plantillaJson.setOrigenExterno(false);
                }
            break;
        }

        if (!plantillaJson.getOficinaCodigo().equals("-1")) {

            Oficina oficina = oficinaEjb.findByCodigoByEntidadMultiEntidad(plantillaJson.getOficinaCodigo(), usuarioEntidad.getEntidad().getId());

            if(oficina == null ||!usuarioEntidad.getEntidad().getId().equals(oficina.getEntidad().getId())){ //Externo o multientidad
                plantillaJson.setOficinaExterna(true);
            }else{
                plantillaJson.setOficinaExterna(false);
            }
        }

        plantilla.setRepro(RegistroUtils.serilizarXml(plantillaJson));

        int orden = 0;
        List<Plantilla> plantillas = plantillaEjb.getAllbyUsuario(usuarioEntidad.getId());
        if(plantillas.size() > 0){
            orden = plantillaEjb.maxOrdenPlantilla(usuarioEntidad.getId());
        }

        plantilla.setOrden(orden+1);

        try {
            plantilla = plantillaEjb.persist(plantilla);

            loginService.asignarPlantillas(getLoginInfo(request));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return plantilla.getId();
    }

    /**
     * Carga el formulario para modificar una {@link Plantilla}
     */
    @RequestMapping(value = "/{plantillaId}/edit", method = RequestMethod.GET)
    public String editarPlantilla(@PathVariable("plantillaId") Long plantillaId, Model model, HttpServletRequest request) {

        Plantilla plantilla = null;
        try {
            plantilla = plantillaEjb.findById(plantillaId);

            // Comprueba que la Plantilla existe
            if (plantilla == null) {
                log.info("No existe esta plantilla");
                Mensaje.saveMessageError(request, getMessage("aviso.plantilla.noExiste"));
                return "redirect:/plantilla/list";
            }

            // Mira si la Plantilla pertenece al UsuarioEntidadActivo
            if (!plantilla.getUsuario().equals(getUsuarioEntidadActivo(request))) {

                Mensaje.saveMessageError(request, getMessage("aviso.plantilla.edit"));
                return "redirect:/plantilla/list";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(plantilla);

        return "plantilla/plantillaForm";
    }

    /**
     * Editar una {@link Plantilla}
     */
    @RequestMapping(value = "/{plantillaId}/edit", method = RequestMethod.POST)
    public String editarPlantilla(@ModelAttribute @Valid Plantilla plantilla, BindingResult result,
                              SessionStatus status, HttpServletRequest request) {

        plantillaValidator.validate(plantilla, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "plantilla/plantillaForm";
        } else { // Si no hay errores actualizamos el registro

            try {
                plantillaEjb.merge(plantilla);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));


            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/plantilla/list";
        }
    }


    /**
     * Cambia estado de una {@link Plantilla}
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idPlantilla}/cambiarEstado", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean cambiarEstadoPlantilla(@PathVariable Long idPlantilla)throws Exception {

        return plantillaEjb.cambiarEstado(idPlantilla);

    }


    /**
     * Sube el orden de una {@link Plantilla}
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idPlantilla}/subir", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean subirPlantilla(@PathVariable Long idPlantilla)throws Exception {

        return plantillaEjb.subirOrden(idPlantilla);

    }


    /**
     * Baja el orden de una {@link Plantilla}
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idPlantilla}/bajar", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean bajarPlantilla(@PathVariable Long idPlantilla)throws Exception {

        return plantillaEjb.bajarOrden(idPlantilla);

    }


    /**
     * Eliminar una {@link Plantilla}
     */
    @RequestMapping(value = "/{plantillaId}/delete")
    public String eliminarPlantilla(@PathVariable Long plantillaId, HttpServletRequest request) {

        try {

            Plantilla plantilla = plantillaEjb.findById(plantillaId);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            if(!plantilla.getUsuario().getId().equals(usuarioEntidad.getId())){
                Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                return "redirect:/plantilla/list";
            }

            List<Plantilla> plantillas = plantillaEjb.getAllbyUsuario(usuarioEntidad.getId());
            int ordenBorrado = plantilla.getOrden();

            plantillaEjb.remove(plantilla);

            for(int i = ordenBorrado; i< plantillas.size(); i++) {
                Plantilla plantillaCambiar = plantillas.get(i);
                plantillaEjb.modificarOrden(plantillaCambiar.getId(), plantillaCambiar.getOrden()-1);
            }

            loginService.asignarPlantillas(getLoginInfo(request));
            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/plantilla/list";
    }

    /**
     * Envia una {@link Plantilla} a un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/enviar/{plantillaId}/{usuarioEntidadId}")
    public String enviarPlantilla(@PathVariable Long plantillaId, @PathVariable Long usuarioEntidadId, HttpServletRequest request) {

        try {
            Plantilla plantillaEnviada = plantillaEjb.findById(plantillaId);
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(usuarioEntidadId);

            Plantilla plantilla = new Plantilla();
            plantilla.setNombre(plantillaEnviada.getNombre());
            plantilla.setUsuario(usuarioEntidad);
            plantilla.setTipoRegistro(plantillaEnviada.getTipoRegistro());
            plantilla.setRepro(plantillaEnviada.getRepro());

            int orden = 0;
            List<Plantilla> plantillas = plantillaEjb.getAllbyUsuario(usuarioEntidad.getId());
            if(plantillas.size() > 0){
                orden = plantillaEjb.maxOrdenPlantilla(usuarioEntidad.getId());
            }
            orden = orden + 1;
            plantilla.setOrden(orden);

            plantillaEjb.persist(plantilla);
            Mensaje.saveMessageInfo(request, getMessage("aviso.plantilla.enviada"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/plantilla/list";
    }


    @ModelAttribute("usuariosEntidadList")
    public List<UsuarioEntidad> usuariosEntidadList(HttpServletRequest request) throws Exception {

        return usuarioEntidadEjb.findUsuariosPlantilla(getEntidadActiva(request).getId(), getUsuarioAutenticado(request).getId(), RegwebConstantes.TIPO_USUARIO_PERSONA);

    }

    /**
     * Obtiene la {@link Plantilla} según su identificador.
     *
     */
    @RequestMapping(value = "/obtenerPlantilla", method = RequestMethod.GET)
    public @ResponseBody
    PlantillaJson obtenerPlantilla(@RequestParam Long idPlantilla, HttpServletRequest request) throws Exception {
        //todo: Mejorar las Plantilla sustituyendo los organismos extinguidos por sus sustitutos

        Entidad entidad = getEntidadActiva(request);

        return plantillaEjb.obtenerPlantilla(idPlantilla,entidad);

    }




    @InitBinder("plantilla")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.plantillaValidator);
    }

}

