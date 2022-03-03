package es.caib.regweb3.webapp.controller.codigoAsunto;

import es.caib.regweb3.model.CodigoAsunto;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.TraduccionCodigoAsunto;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.CodigoAsuntoValidator;
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

@Controller
@RequestMapping(value = "/codigoAsunto")
@SessionAttributes(types = {CodigoAsunto.class})
public class CodigoAsuntoController extends BaseController {

    @Autowired
    private CodigoAsuntoValidator codigoAsuntoValidator;

    @EJB(mappedName = CodigoAsuntoLocal.JNDI_NAME)
    private CodigoAsuntoLocal codigoAsuntoEjb;

    /**
     * Listado de todos los CodigoAsunto
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listadoCodigoAsunto() {
        return "redirect:/codigoAsunto/list/1";
    }

    /**
     * Listado de CodigoAsunto
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView listCodigoAsunto(@PathVariable Integer pageNumber, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("catalogoDatos/codigoAsuntoList");

        Entidad entidad = getEntidadActiva(request);

        List<CodigoAsunto> listado = codigoAsuntoEjb.getPagination((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = codigoAsuntoEjb.getTotalEntidad(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.CodigoAsunto}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoCodigoAsunto(Model model, HttpServletRequest request) throws Exception {

        CodigoAsunto codigoAsunto = new CodigoAsunto();
        codigoAsunto.setEntidad(getEntidadActiva(request));

        for(Long idioma: RegwebConstantes.IDIOMAS_UI){
            codigoAsunto.setTraduccion(RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(idioma), new TraduccionCodigoAsunto());
        }

        model.addAttribute(codigoAsunto);

        return "/catalogoDatos/codigoAsuntoForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.CodigoAsunto}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoCodigoAsunto(@ModelAttribute CodigoAsunto codigoAsunto, BindingResult result, SessionStatus status, HttpServletRequest request) {


        codigoAsuntoValidator.validate(codigoAsunto, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "catalogoDatos/codigoAsuntoForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                codigoAsuntoEjb.persist(codigoAsunto);
                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/codigoAsunto/list";
        }
    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.CodigoAsunto}
     */
    @RequestMapping(value = "/{codigoAsuntoId}/edit", method = RequestMethod.GET)
    public String editarCodigoAsunto(@PathVariable("codigoAsuntoId") Long codigoAsuntoId, Model model, HttpServletRequest request) throws Exception{

        CodigoAsunto codigoAsunto = null;
        try {

            codigoAsunto = codigoAsuntoEjb.findById(codigoAsuntoId);

            // Comprueba que  existe
            if(codigoAsunto == null) {
                Mensaje.saveMessageError(request, getMessage("aviso.codigoAsunto.edit"));
                return "redirect:/codigoAsunto/list";
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(codigoAsunto);

        return "catalogoDatos/codigoAsuntoForm";
    }


    /**
     * Editar un {@link es.caib.regweb3.model.CodigoAsunto}
     */
    @RequestMapping(value = "/{codigoAsuntoId}/edit", method = RequestMethod.POST)
    public String editarCodigoAsunto(@ModelAttribute @Valid CodigoAsunto codigoAsunto,
                                   BindingResult result, SessionStatus status, HttpServletRequest request) {

        codigoAsuntoValidator.validate(codigoAsunto, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "catalogoDatos/codigoAsuntoForm";
        }else { // Si no hay errores actualizamos el registro
            try {
                codigoAsuntoEjb.merge(codigoAsunto);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));


            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/codigoAsunto/list";
        }
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.CodigoAsunto}
     */
    @RequestMapping(value = "/{codigoAsuntoId}/delete")
    public String eliminarCodigoAsunto(@PathVariable Long codigoAsuntoId, HttpServletRequest request) {

        try {

            CodigoAsunto codigoAsunto = codigoAsuntoEjb.findById(codigoAsuntoId);
            codigoAsuntoEjb.remove(codigoAsunto);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/codigoAsunto/list";
    }

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_UI;
    }

    @InitBinder("codigoAsunto")
    public void initBinderTipoAsunto(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setValidator(this.codigoAsuntoValidator);
    }
    
}
