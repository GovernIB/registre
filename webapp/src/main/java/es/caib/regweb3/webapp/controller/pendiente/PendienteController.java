package es.caib.regweb3.webapp.controller.pendiente;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Pendiente;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.PendienteLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Fundació Bit
 * Date: 17/04/18
 * Controller que gestiona todas las operaciones con {@link Pendiente}
 *
 * @author earivi
 */
@Controller
@RequestMapping(value = "/pendiente")
public class PendienteController extends BaseController {

    @EJB(mappedName = PendienteLocal.JNDI_NAME)
    private PendienteLocal pendienteEjb;

    /**
     * Listado de todas las Descargas
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String pendientesProcesarList(Model model, HttpServletRequest request) throws Exception{
        Entidad entidadActiva = getEntidadActiva(request);

        Pendiente pendienteBusqueda = new Pendiente();
        pendienteBusqueda.setProcesado(false);

        List<Pendiente> listado = pendienteEjb.getPaginationByEntidad(0, entidadActiva.getId(), false);
        Long total = pendienteEjb.getTotalByEntidad(entidadActiva.getId(),false);
        Paginacion paginacion = new Paginacion(total.intValue(), 1);

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("listado", listado);
        model.addAttribute("pendienteBusqueda", pendienteBusqueda);

        return "pendiente/pendientesProcesarList";
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView pendientesProcesarList(@ModelAttribute Pendiente busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("/pendiente/pendientesProcesarList");
        Entidad entidadActiva = getEntidadActiva(request);

        List<Pendiente> listado = pendienteEjb.getPaginationByEntidad((busqueda.getPageNumber() - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, entidadActiva.getId(), busqueda.getProcesado());
        Long total = pendienteEjb.getTotalByEntidad(entidadActiva.getId(), busqueda.getProcesado());
        Paginacion paginacion = new Paginacion(total.intValue(), busqueda.getPageNumber());

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);
        mav.addObject("pendienteBusqueda", busqueda);

        return mav;
    }

    @RequestMapping(value = "/{idPendiente}/delete")
    public String eliminarPersona(@PathVariable Long idPendiente, HttpServletRequest request) {

        try {

            Pendiente pendiente = pendienteEjb.findById(idPendiente);

            pendienteEjb.remove(pendiente);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/pendiente/list";
    }
}
