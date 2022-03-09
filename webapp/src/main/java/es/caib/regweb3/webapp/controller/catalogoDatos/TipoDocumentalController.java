package es.caib.regweb3.webapp.controller.catalogoDatos;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.model.TraduccionTipoDocumental;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.TipoDocumentalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created 19/03/14
 * Controller que gestiona todas las operaciones del TipoDocumental
 * @author earrivi
 */
@Controller
@RequestMapping(value = "/tipoDocumental")
@SessionAttributes(types = {TipoDocumental.class})
public class TipoDocumentalController extends BaseController {

    @Autowired
    private TipoDocumentalValidator tipoDocumentalValidator;

    /**
     * Listado de todos los TipoDocumental
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listadoTipoDocumental() {
        return "redirect:/tipoDocumental/list/1";
    }

    /**
     * Listado de tipos documentales
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView listTipoDocumental(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("catalogoDatos/tipoDocumentalList");

        Entidad entidad = getEntidadActiva(request);

        List<TipoDocumental> listado = tipoDocumentalEjb.getPagination((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = tipoDocumentalEjb.getTotal(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.TipoDocumental}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoTipoDocumental(Model model, HttpServletRequest request) throws Exception {

        TipoDocumental tipoDocumental = new TipoDocumental();
        Entidad entidad = getEntidadActiva(request);
        tipoDocumental.setEntidad(entidad);

        for(Long idioma: RegwebConstantes.IDIOMAS_UI){
            tipoDocumental.setTraduccion(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma), new TraduccionTipoDocumental());
        }
        model.addAttribute(tipoDocumental);

        return "/catalogoDatos/tipoDocumentalForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.TipoDocumental}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoTipoDocumental(@ModelAttribute TipoDocumental tipoDocumental, BindingResult result, SessionStatus status, HttpServletRequest request) {

        tipoDocumentalValidator.validate(tipoDocumental, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "catalogoDatos/tipoDocumentalForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                tipoDocumentalEjb.persist(tipoDocumental);
                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/tipoDocumental/list";
        }
    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.TipoDocumental}
     */
    @RequestMapping(value = "/{tipoDocumentalId}/edit", method = RequestMethod.GET)
    public String editarTipoDocumental(@PathVariable("tipoDocumentalId") Long tipoDocumentalId, Model model, HttpServletRequest request) {

        TipoDocumental tipoDocumental = null;
        try {

            Entidad entidadActiva = getEntidadActiva(request);

            tipoDocumental = tipoDocumentalEjb.findById(tipoDocumentalId);

            // Comprueba que el TipoDocumental existe
            if(tipoDocumental == null) {
                log.info("No existe este tipo documental");
                Mensaje.saveMessageError(request, getMessage("aviso.tipoDocumental.edit"));
                return "redirect:/tipoDocumental/list";
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(tipoDocumental);
        return "catalogoDatos/tipoDocumentalForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.TipoDocumental}
     */
    @RequestMapping(value = "/{tipoDocumentalId}/edit", method = RequestMethod.POST)
    public String editarTipoDocumental(@ModelAttribute @Valid TipoDocumental tipoDocumental,BindingResult result,
                                   SessionStatus status, HttpServletRequest request) {

        tipoDocumentalValidator.validate(tipoDocumental, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "catalogoDatos/tipoDocumentalForm";
        }else { // Si no hay errores actualizamos el registro

            try {
                tipoDocumentalEjb.merge(tipoDocumental);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));


            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/tipoDocumental/list";
        }
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.TipoDocumental}
     */
    @RequestMapping(value = "/{tipoDocumentalId}/delete")
    public String eliminarTipoDocumental(@PathVariable Long tipoDocumentalId, HttpServletRequest request) {

        try {

            TipoDocumental tipoDocumental = tipoDocumentalEjb.findById(tipoDocumentalId);
            tipoDocumentalEjb.remove(tipoDocumental);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/tipoDocumental/list";
    }


     @ModelAttribute("idiomas")
     public Long[] idiomas() throws Exception {
         return RegwebConstantes.IDIOMAS_UI;
     }

    @InitBinder("tipoDocumental")
    public void initBinderTipoDocumental(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setValidator(this.tipoDocumentalValidator);
    }
}
