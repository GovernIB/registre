package es.caib.regweb3.webapp.controller.sir;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.persistence.ejb.LibroLocal;
import es.caib.regweb3.persistence.ejb.OficinaLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.RegistroSirLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OficioRemisionBusquedaForm;
import es.caib.regweb3.webapp.form.RegistroSirBusquedaForm;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/sir")
public class SirController extends BaseController {

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/RegistroSirEJB/local")
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    /**
     * Listado de oficios de remisión sir enviados
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/monitorEnviados", method = RequestMethod.GET)
    public ModelAndView enviados(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorEnviados");

        // Obtenemos todos los Libros activos de la Entidad
        List<Libro> libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());

        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(new OficioRemision(), 1);

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("librosConsulta", libros);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusqueda);

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/monitorEnviados", method = RequestMethod.POST)
    public ModelAndView enviados(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorEnviados");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Obtenemos todos los Libros activos de la Entidad
        List<Libro> libros = libroEjb.getLibrosEntidad(getEntidadActiva(request).getId());

        // Ajustam la dataFi per a que ens trobi els oficis del mateix dia
        Date dataFi = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), dataFi,null, oficioRemision, null, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), true);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION_SIR);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("librosConsulta", libros);
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;

    }


    /**
     * Listado de RegistroSir recibidos
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/monitorRecibidos", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorRecibidos");
        Entidad entidad = getEntidadActiva(request);

        RegistroSirBusquedaForm registroSirBusquedaForm = new RegistroSirBusquedaForm(new RegistroSir(),1);
        model.addAttribute("estados", EstadoRegistroSir.values());
        model.addAttribute("registroSirBusqueda", registroSirBusquedaForm);
        model.addAttribute("anys", getAnys());
        model.addAttribute("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;
    }

    /**
     * Realiza la busqueda de {@link RegistroSir} según los parametros del formulario
     */
    @RequestMapping(value = "/monitorRecibidos", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroSirBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("sir/monitorRecibidos");
        Entidad entidad = getEntidadActiva(request);

        RegistroSir registroSir = busqueda.getRegistroSir();

        Paginacion paginacion = registroSirEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()), registroSir, registroSir.getCodigoEntidadRegistral(), busqueda.getEstado());

        busqueda.setPageNumber(1);

        mav.addObject("estados", EstadoRegistroSir.values());
        mav.addObject("paginacion", paginacion);
        mav.addObject("registroSirBusqueda", busqueda);
        mav.addObject("anys", getAnys());
        mav.addObject("oficinasSir", oficinaEjb.oficinasSIREntidad(entidad.getId()));

        return mav;

    }

    /**
     * Reinicia el contador de reintentos SIR
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOficio}/reiniciar", method = RequestMethod.GET)
    public String reiniciar(@PathVariable Long idOficio, HttpServletRequest request)throws Exception {

        oficioRemisionEjb.reiniciarIntentos(idOficio);

        Mensaje.saveMessageInfo(request, getMessage("registroSir.reiniciar.ok"));

        return "redirect:/sir/monitorEnviados";
    }

}
