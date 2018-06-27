package es.caib.regweb3.webapp.controller.cola;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by mgonzalez on 19/04/2018.
 * Controller que gestiona las diferentes colas de objetos definidas en Regweb3
 */
@Controller
@RequestMapping(value = "/cola")
public class ColaController extends BaseController {

    @EJB(mappedName = "regweb3/ColaEJB/local")
    private ColaLocal colaEjb;



    /**
     * Listado de todas las {@link Cola}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.GET)
    public String listado(@PathVariable("tipo") Long tipo, Model model, HttpServletRequest request) throws Exception{

        Cola cola = new Cola();
        cola.setTipo(tipo);

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = colaEjb.busqueda(cola, entidadActiva.getId());

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("colaBusqueda", cola);
        model.addAttribute("tipo", tipo);

        return "cola/colaList";
    }

    /**
     * Listado de todos las {@link Cola}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.POST)
    public ModelAndView listado(@PathVariable("tipo") Long tipo, @ModelAttribute Cola busqueda, HttpServletRequest request) throws Exception {


        ModelAndView mav = new ModelAndView("cola/colaList");

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = colaEjb.busqueda(busqueda, entidadActiva.getId());

        mav.addObject("paginacion", paginacion);
        mav.addObject("colaBusqueda", busqueda);
        mav.addObject("tipo", busqueda.getTipo());


        return mav;
    }


    /**
     * Método que vuelve a activar los objetos de la cola para que puedan ser enviados nuevamente.
     * @param idEntidad
     * @param tipo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idEntidad}/reiniciarCola/{tipo}", method = RequestMethod.GET)
    public String  reiniciarCola(@PathVariable Long idEntidad, @PathVariable Long tipo, @ModelAttribute Cola busqueda, Model model,  HttpServletRequest request) throws Exception {
        try {

            Paginacion paginacion= colaEjb.reiniciarColabyEntidadTipo(idEntidad,tipo, busqueda);
            model.addAttribute("colaBusqueda", busqueda);
            model.addAttribute("paginacion", paginacion);
            Mensaje.saveMessageInfo(request, getMessage("cola.reiniciada"));


        } catch (I18NValidationException e) {
            Mensaje.saveMessageError(request, getMessage("cola.error.reiniciar"));
            e.printStackTrace();

        } catch (I18NException ie) {
            Mensaje.saveMessageError(request, getMessage("cola.error.reiniciar"));
            ie.printStackTrace();

        } catch (Exception iie){
            Mensaje.saveMessageError(request, getMessage("cola.error.reiniciar"));
            iie.printStackTrace();

        }

        return "redirect:/cola/list/"+tipo;
    }

    /**
     * Eliminar una {@link Cola} y le cambia el estado al registro de entrada asociado
     */
    @RequestMapping(value = "/{colaId}/delete/{tipo}/{estado}")
    public String eliminarElementoCola(@PathVariable Long colaId, @PathVariable Long tipo, @PathVariable Long estado,  HttpServletRequest request) {

        try {

            Cola cola = colaEjb.findById(colaId);
            colaEjb.remove(cola);
            //Actualizamos el registro de entrada al estado indicado(valido o distribuido)
            registroEntradaEjb.cambiarEstado(cola.getIdObjeto(), estado);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/cola/list/"+tipo;
    }




    @ModelAttribute("tipos")
    public
    Long[] tipos() {
        return RegwebConstantes.COLA_TIPOS;
    }

    @ModelAttribute("estados")
    public
    Long[] estados() {
        return RegwebConstantes.COLA_ESTADOS;
    }
}
