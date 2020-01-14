package es.caib.regweb3.webapp.controller.cola;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.persistence.ejb.ColaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
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
        cola.setEstado(RegwebConstantes.COLA_ESTADO_ERROR);

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
    public String reiniciarCola(@PathVariable Long idEntidad, @PathVariable Long tipo, HttpServletRequest request) throws Exception {

        try {

            colaEjb.reiniciarColabyEntidadTipo(idEntidad,tipo);

            Mensaje.saveMessageInfo(request, getMessage("cola.reiniciada"));

        } catch (I18NException | Exception ie) {
            Mensaje.saveMessageError(request, getMessage("cola.error.reiniciar"));
            ie.printStackTrace();
        }

        return "redirect:/cola/list/"+tipo;
    }

    /**
     * Marcar como procesado un elemento de la {@link Cola}
     */
    @RequestMapping(value = "/{colaId}/procesar/{tipo}")
    public String procesarCola(@PathVariable Long colaId, @PathVariable Long tipo, HttpServletRequest request) {

        try {

            // Marcamos el elemento como procesado
            Cola cola = colaEjb.findById(colaId);
            colaEjb.procesarElemento(cola);

            // Marcamos como distribuido el Registro
            RegistroEntrada registroEntrada = registroEntradaEjb.findById(cola.getIdObjeto());
            registroEntradaEjb.distribuirRegistroEntrada(registroEntrada, registroEntrada.getUsuario());


            Mensaje.saveMessageInfo(request, getMessage("cola.procesar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("cola.error.eliminar"));
            e.printStackTrace();
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
            Mensaje.saveMessageError(request, getMessage("cola.error.eliminar"));
            e.printStackTrace();
        }

        return "redirect:/cola/list/"+tipo;
    }


    /**
     * Reiniciar un {@link Cola} y le pone el contador a 0.
     */
    @RequestMapping(value = "/{colaId}/reiniciar/{tipo}")
    public String reiniciarElementoCola(@PathVariable Long colaId, @PathVariable Long tipo,  HttpServletRequest request) {

        try {

            colaEjb.reiniciarElementoCola(colaId);

            Mensaje.saveMessageInfo(request, getMessage("cola.reiniciado"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("cola.reiniciar.elemento.error"));
            e.printStackTrace();
        }

        return "redirect:/cola/list/"+tipo;
    }




    @ModelAttribute("tiposCola")
    public
    Long[] tiposCola() {
        return RegwebConstantes.COLA_TIPOS;
    }

    @ModelAttribute("estados")
    public
    Long[] estados() {
        return RegwebConstantes.COLA_ESTADOS;
    }
}
