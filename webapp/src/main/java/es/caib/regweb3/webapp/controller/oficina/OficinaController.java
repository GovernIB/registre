package es.caib.regweb3.webapp.controller.oficina;

import es.caib.regweb3.model.CatEstadoEntidad;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.CatEstadoEntidadLocal;
import es.caib.regweb3.persistence.ejb.CatIslaLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OficinaBusquedaForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SessionAttributes(types = Oficina.class)
@RequestMapping(value = "/oficina")
public class OficinaController extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = CatEstadoEntidadLocal.JNDI_NAME)
    private CatEstadoEntidadLocal catEstadoEntidadEjb;

    @EJB(mappedName = CatIslaLocal.JNDI_NAME)
    public CatIslaLocal catIslaEjb;

    @EJB(mappedName = CatProvinciaLocal.JNDI_NAME)
    public CatProvinciaLocal catProvinciaEjb;

    /**
     * Listado de todos los Oficinas
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Oficina oficina = new Oficina();
        oficina.setEstado(vigente);

        OficinaBusquedaForm oficinaBusqueda = new OficinaBusquedaForm(oficina, entidad.getId(), 1);

        model.addAttribute("entidad", entidad);
        model.addAttribute("oficinaBusqueda", oficinaBusqueda);
        model.addAttribute("estados",catEstadoEntidadEjb.getAll());
        model.addAttribute("islas", catIslaEjb.getByProvincia(catProvinciaEjb.findByCodigo(RegwebConstantes.PROVINCIA_BALEARES)));

        return "oficina/oficinaList";
    }

    /**
     * Listado de oficinas
     *
     * @param busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OficinaBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficina/oficinaList");
        Entidad entidad = getEntidadActiva(request);
        Oficina oficina = busqueda.getOficina();

        if(busqueda.getExportarOficinas()){
            mav = new ModelAndView("exportarOficinasExcel");
            Paginacion oficinas = oficinaEjb.busqueda(null, entidad.getId(), oficina, busqueda.getSir());
            mav.addObject("tipoExportacion", "oficinas");
            mav.addObject("resultados", oficinas);

        }else { // Búsqueda normal
            Paginacion paginacion = oficinaEjb.busqueda(busqueda.getPageNumber(), entidad.getId(), oficina, busqueda.getSir());

            busqueda.setPageNumber(1);
            mav.addObject("entidad", entidad);
            mav.addObject("paginacion", paginacion);
            mav.addObject("oficinaBusqueda", busqueda);
            mav.addObject("estados", catEstadoEntidadEjb.getAll());
            mav.addObject("islas", catIslaEjb.getByProvincia(catProvinciaEjb.findByCodigo(RegwebConstantes.PROVINCIA_BALEARES)));
        }

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idOficina}/detalle", method = RequestMethod.GET)
    public String detalleOficina(@PathVariable Long idOficina, Model model) throws Exception{

        Oficina oficina = oficinaEjb.findByIdCompleto(idOficina);

        model.addAttribute("oficina",oficina);
        model.addAttribute("organismos",organismoEjb.getOrganismosRegistro(oficina));
        model.addAttribute("islas", catIslaEjb.getByProvincia(catProvinciaEjb.findByCodigo(RegwebConstantes.PROVINCIA_BALEARES)));

        return "oficina/oficinaDetalle";

    }


    /**
     * Editar una {@link es.caib.regweb3.model.Oficina}
     */
    @RequestMapping(value = "/{idOficina}/detalle", method = RequestMethod.POST)
    public String editarOficina(@ModelAttribute @Valid Oficina oficina, BindingResult result, SessionStatus status, HttpServletRequest request) {


        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            return "oficina/oficinaDetalle";
        }else { // Si no hay errores actualizamos el registro

            try {

                if(oficina.getIsla().getId() == null){
                    oficina.setIsla(null);
                }

                oficinaEjb.merge(oficina);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
            } catch(I18NException i18ne) {
                log.error(I18NUtils.getMessage(i18ne), i18ne);
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();

            return "redirect:/oficina/list";

        }
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idOficina}/gestionarOficinaLibSir", method = RequestMethod.GET)
    public String gestionarOficinaLibSir(@PathVariable Long idOficina, HttpServletRequest request) throws Exception{

        try{
            Boolean activa = oficinaEjb.gestionarOficinaLibSir(idOficina);

            if(activa){
                Mensaje.saveMessageInfo(request, getMessage("oficina.desactivarLibSir.ok"));
            }else{
                Mensaje.saveMessageInfo(request, getMessage("oficina.activarLibSir.ok"));
            }

        }catch (Exception e){
            e.printStackTrace();
            Mensaje.saveMessageError(request,getMessage("regweb.error.general"));
        }

        return "redirect:/oficina/"+idOficina+"/detalle";
    }

    /**
     * Exporta un listado de usuarios por Oficina
     */
    @RequestMapping(value = "/{idOficina}/usuarios", method = RequestMethod.GET)
    public ModelAndView exportarUsuariosOficina(@PathVariable Long idOficina, Model model, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("exportarOficinasExcel");
        List<UsuarioEntidad> usuarios = usuarioEntidadEjb.getUsuariosOficina(getEntidadActiva(request).getId(), idOficina);

        // Lo convertimos en un objeto Paginación para el export común
        Paginacion resultados = new Paginacion(0, 0);
        resultados.setListado(usuarios);

        model.addAttribute("tipoExportacion", "usuarios");
        model.addAttribute("resultados", resultados);
        model.addAttribute("oficina", oficinaEjb.findById(idOficina));

        return mav;

    }

    @InitBinder("oficina")
    public void oficinaBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }
}
