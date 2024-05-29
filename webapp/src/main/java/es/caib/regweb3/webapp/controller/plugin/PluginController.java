package es.caib.regweb3.webapp.controller.plugin;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Plugin;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.PluginForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.PluginValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fundació Bit
 * Date: 05/05/16
 * Controller que gestiona todas las operaciones con {@link Plugin}
 *
 * @author earivi
 */
@Controller
@RequestMapping(value = "/plugin")
@SessionAttributes("plugin")
public class PluginController extends BaseController {

    @Autowired
    private PluginValidator pluginValidator;

    @EJB(mappedName = PluginLocal.JNDI_NAME)
    private PluginLocal pluginEjb;


    /**
     * Listado de todas las {@link Plugin}
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model, HttpServletRequest request) throws Exception{

        PluginForm pluginBusqueda = new PluginForm(null,1);

        List<Plugin> listado = new ArrayList<Plugin>();
        Paginacion paginacion = null;

        if (isSuperAdmin(request)) { // Si es SuperAdministrador, cargamos los plugins de REGWEB3
            listado = pluginEjb.getPaginationREGWEB3(0, null);
            Long total = pluginEjb.getTotalREGWEB3(null);

            paginacion = new Paginacion(total.intValue(), 1);

        } else if (isAdminEntidad(request)) { // Si es AdminEntidad, cargamos los plugins de la Entidad
            Entidad entidadActiva = getEntidadActiva(request);

            listado = pluginEjb.getPaginationByEntidad(0, entidadActiva.getId(), null);
            Long total = pluginEjb.getTotalByEntidad(entidadActiva.getId(), null);

            paginacion = new Paginacion(total.intValue(), 1);
        }

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("listado", listado);
        model.addAttribute("pluginBusqueda", pluginBusqueda);

        return "plugin/pluginList";
    }

    /**
     * Listado de todos las {@link Plugin}
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView listado(@ModelAttribute PluginForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("plugin/pluginList");
        List<Plugin> listado = new ArrayList<Plugin>();
        Paginacion paginacion = null;

        if (isSuperAdmin(request)) { // Si es SuperAdministrador, cargamos los plugins de REGWEB3
            listado = pluginEjb.getPaginationREGWEB3((busqueda.getPageNumber() - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, busqueda.getTipo());
            Long total = pluginEjb.getTotalREGWEB3(busqueda.getTipo());

            paginacion = new Paginacion(total.intValue(), busqueda.getPageNumber());

        } else if (isAdminEntidad(request)) { // Si es AdminEntidad, cargamos los plugins de la Entidad
            Entidad entidadActiva = getEntidadActiva(request);

            listado = pluginEjb.getPaginationByEntidad((busqueda.getPageNumber() - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, entidadActiva.getId(), busqueda.getTipo());
            Long total = pluginEjb.getTotalByEntidad(entidadActiva.getId(), busqueda.getTipo());

            paginacion = new Paginacion(total.intValue(), busqueda.getPageNumber());
        }

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);
        mav.addObject("pluginBusqueda", busqueda);

        return mav;
    }


    /**
     * Carga el formulario para un nuevo {@link Plugin}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoPlugin(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        if(getTiposDisponiblesEntidad(request).isEmpty()){
            Mensaje.saveMessageAviso(request, getMessage("plugin.definidos.todos"));
            return "redirect:/plugin/list";
        }

        Plugin plugin = null;

        if (isSuperAdmin(request)) { // Si es SuperAdministrador, el plugin será para REGWEB3
            plugin = new Plugin();

        } else if (isAdminEntidad(request)) { // Si es AdminEntidad, el plugin será solo para la Entidad
            plugin = new Plugin(entidad.getId());
        }

        model.addAttribute(plugin);

        return "plugin/pluginForm";
    }

    /**
     * Guardar una nueva {@link Plugin}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoPlugin(@ModelAttribute Plugin plugin, BindingResult result, SessionStatus status, HttpServletRequest request) {

        pluginValidator.validate(plugin, result);

        if (result.hasErrors()) {

            return "plugin/pluginForm";
        } else { // Si no hay errores guardamos el registro

            try {
                pluginEjb.persist(plugin);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

            } catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/plugin/list";
        }
    }


    /**
     * Carga el formulario para modificar una {@link Plugin}
     */
    @RequestMapping(value = "/{pluginId}/edit", method = RequestMethod.GET)
    public String editarPlugin(@PathVariable("pluginId") Long pluginId, Model model, HttpServletRequest request) {

        Plugin plugin;

        try {
            plugin = pluginEjb.findById(pluginId);
            model.addAttribute(plugin);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "plugin/pluginForm";
    }

    /**
     * Editar una {@link Plugin}
     */
    @RequestMapping(value = "/{pluginId}/edit", method = RequestMethod.POST)
    public String editarPlugin(@ModelAttribute @Valid Plugin plugin, BindingResult result, SessionStatus status, HttpServletRequest request) {

        pluginValidator.validate(plugin, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "plugin/pluginForm";
        } else { // Si no hay errores actualizamos el registro

            try {

                pluginEjb.merge(plugin);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/plugin/list";

        }
    }

    /**
     * Eliminar una {@link Plugin}
     */
    @RequestMapping(value = "/{pluginId}/delete")
    public String eliminarPlugin(@PathVariable Long pluginId, HttpServletRequest request) {

        try {

            Plugin plugin = pluginEjb.findById(pluginId);

            pluginEjb.remove(plugin);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/plugin/list";
    }

    @ModelAttribute("tiposPlugin")
    public Long[] tiposPlugin() throws Exception {

        if(Configuracio.isCAIB()){
            return RegwebConstantes.TIPOS_PLUGIN_CAIB;
        }else{
            return RegwebConstantes.TIPOS_PLUGIN;
        }
    }

    @ModelAttribute("tiposDisponibles")
    private List<Long> getTiposDisponiblesEntidad(HttpServletRequest request) throws I18NException {

        List<Long> tiposDefinidos = pluginEjb.getTiposPluginDefinidos(getEntidadActiva(request));
        List<Long> tiposDisponibles = new ArrayList<>();

        if(Configuracio.isCAIB()){
            tiposDisponibles = Arrays.stream(RegwebConstantes.TIPOS_PLUGIN_CAIB)
                    .filter(element -> !tiposDefinidos.contains(element))
                    .collect(Collectors.toList());

        }else{
            tiposDisponibles = Arrays.stream(RegwebConstantes.TIPOS_PLUGIN)
                    .filter(element -> !tiposDefinidos.contains(element))
                    .collect(Collectors.toList());
        }

        return tiposDisponibles;
    }


    @InitBinder("plugin")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.pluginValidator);
    }
}
