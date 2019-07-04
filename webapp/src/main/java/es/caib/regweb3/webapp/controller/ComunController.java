package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Plantilla;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.PlantillaLocal;
import es.caib.regweb3.persistence.ejb.RolLocal;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Controller
public class ComunController extends BaseController {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private LoginService loginService;
    
    @EJB(mappedName = "regweb3/RolEJB/local")
    private RolLocal rolEjb;

    @EJB(mappedName = "regweb3/PlantillaEJB/local")
    private PlantillaLocal plantillaEjb;


    @RequestMapping(value = "/eventos")
    public ModelAndView eventos(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("eventos");

        Entidad entidadActiva = getEntidadActiva(request);

        mav.addObject("totalEntradas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("totalSalidas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("entradasPendientes",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento is null and (estado=1 or estado=3) and usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasPendientes",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento is null and (estado=1 or estado=3) and usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("entradasEvento",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento!=0 and usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasEvento",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento!=0 and usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("entradasProcesadas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento=0 and usuario.entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasProcesadas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento=0 and usuario.entidad.id = "+entidadActiva.getId()));


        return mav;
    }

    @RequestMapping(value = "/eventoOficioInterno/{idOficina}")
    public String actualizarEventoOficioInterno(@PathVariable Long idOficina, HttpServletRequest request) {

        try {
            Oficina oficina = oficinaEjb.findById(idOficina);

            Integer total = registroEntradaEjb.actualizarEventoOficioInterno(oficina);

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a OficioInterno de entrada de la oficina " +oficina.getDenominacion());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    @RequestMapping(value = "/eventoDistribuir/{idOficina}")
    public String actualizarEventoDistribuir(@PathVariable Long idOficina, HttpServletRequest request) {

        try {

            Oficina oficina = oficinaEjb.findById(idOficina);

            Integer total = registroEntradaEjb.actualizarEventoDistribuir(oficina);

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a Distribuir de la oficina " +oficina.getDenominacion());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    @RequestMapping(value = "/noAutorizado")
    public ModelAndView noautorizado(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("noAutorizado");
    }

    @RequestMapping(value = "/accesoDenegado")
    public ModelAndView accesoDenegado(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("accesoDenegado");
    }

    @RequestMapping(value = "/rol/{rolId}")
    public String cambioRol(@PathVariable Long rolId, HttpServletRequest request) {

        try {
            Rol rolNuevo = rolEjb.findById(rolId);

            if(!loginService.cambioRol(rolNuevo, getLoginInfo(request))){
                Mensaje.saveMessageError(request, getMessage("error.rol.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/cambioEntidad/{entidadId}")
    public String cambioEntidad(@PathVariable Long entidadId, HttpServletRequest request, HttpServletResponse response) {

        List<Entidad> entidadesAutenticado = getEntidadesAutenticado(request);

        try {
            Entidad entidadNueva = entidadEjb.findById(entidadId);

            if(entidadesAutenticado.contains(entidadNueva)){

                loginService.cambioEntidad(entidadNueva, getLoginInfo(request));
            }else{
                Mensaje.saveMessageError(request, getMessage("error.entidad.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/cambioOficina/{oficinaId}")
    public String cambioOficina(@PathVariable Long oficinaId, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();

        LinkedHashSet<Oficina> oficinasAutenticado = getOficinasAutenticado(request);

        try {
            Oficina oficinaNueva = oficinaEjb.findById(oficinaId);
            if(oficinasAutenticado.contains(new Oficina(oficinaNueva.getId()))){
                loginService.asignarOficinaActiva(oficinaNueva, getLoginInfo(request));
                log.info("Cambio Oficina activa: " + oficinaNueva.getDenominacion() + " - " + oficinaNueva.getCodigo());
            }else{
                Mensaje.saveMessageError(request, getMessage("error.oficina.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/error/404")
    public ModelAndView error(HttpServletRequest request) throws Exception{

        log.info("Dentro de error404 : " + request.getRequestURL());

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", "Error 404, la página solicitada no existe");
        mav.addObject("trazaError", "La página solicitada no existe");
        mav.addObject("url", request.getRequestURL());

        return mav;
    }

    @RequestMapping(value = "/aviso")
    public ModelAndView aviso(HttpServletRequest request) throws Exception{

        return new ModelAndView("aviso");
    }

    @RequestMapping(value = "/plantillasUsuario/{tipoRegistro}")
    public ModelAndView plantillasUsuario(@PathVariable Long tipoRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("modulos/menuPlantillas");

        List<Plantilla> plantillasUsuario = plantillaEjb.getActivasbyUsuario(getUsuarioEntidadActivo(request).getId(), tipoRegistro);
        mav.addObject("plantillasUsuario", plantillasUsuario);
        mav.addObject("tipoRegistro", tipoRegistro);

        return mav;
    }


    @RequestMapping(value = "/sesion")
    public ModelAndView sesion(HttpServletRequest request, HttpServletResponse response) throws Exception{

        ModelAndView mav = new ModelAndView("sesion");

        //HttpSession session = 
        request.getSession();


        return mav;
    }

}
