package es.caib.regweb3.webapp.controller.distribucion;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.persistence.ejb.LibroLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/distribucion")
public class DistribucionController extends BaseController {

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    /**
     * Listado de Registros distribuidos
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/distribuidos/list", method = RequestMethod.GET)
    public String pendientesDistribuir(Model model, HttpServletRequest request)throws Exception {

        Entidad entidad = getEntidadActiva(request);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());
        registroEntradaBusqueda.getRegistroEntrada().getRegistroDetalle().setExtracto("");
        registroEntradaBusqueda.getRegistroEntrada().setEstado(RegwebConstantes.REGISTRO_DISTRIBUIDO);

        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("librosConsulta", libroEjb.getLibrosEntidad(entidad.getId()));

        return "distribucion/registrosDistribuidos";
    }

    /**
     * Listado de Registros distribuidos
     */
    @RequestMapping(value = "/distribuidos/list", method = RequestMethod.POST)
    public ModelAndView pendientesDistribuir(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("distribucion/registrosDistribuidos");
        Entidad entidad = getEntidadActiva(request);

        registroEntradaBusquedaValidator.validate(busqueda, result);

        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("librosConsulta", libroEjb.getLibrosEntidad(entidad.getId()));
            mav.addObject("registroEntradaBusqueda", busqueda);

            return mav;

        }else {

            RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            Paginacion paginacion = registroEntradaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroEntrada, "", "", "", "", null, false, "", null, entidad.getId());

            busqueda.setPageNumber(1);

            mav.addObject("librosConsulta", libroEjb.getLibrosEntidad(entidad.getId()));
            mav.addObject("paginacion", paginacion);
            mav.addObject("pendientesDistribuirBusqueda", busqueda);
        }

        return mav;

    }

    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}
