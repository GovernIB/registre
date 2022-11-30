package es.caib.regweb3.webapp.controller.evento;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 26/09/19
 */
@Controller
public class EventoController extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/eventos")
    public ModelAndView eventos(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("eventos");

        Entidad entidadActiva = getEntidadActiva(request);

        mav.addObject("totalEntradas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where entidad.id = "+entidadActiva.getId()));

        mav.addObject("totalSalidas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where entidad.id = "+entidadActiva.getId()));

        mav.addObject("entradasPendientes",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento is null and (estado=1 or estado=3) and entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasPendientes",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento is null and (estado=1 or estado=3) and entidad.id = "+entidadActiva.getId()));

        mav.addObject("entradasEventoAsignado",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento!=0 and evento != null and entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasEventoAsignado",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento!=0 and evento != null and entidad.id = "+entidadActiva.getId()));

        /*mav.addObject("entradasSinEvento",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento is null and (estado!=1 or estado!=3) and entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasSinEvento",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento is null and (estado!=1 or estado!=3) and entidad.id = "+entidadActiva.getId()));*/

        mav.addObject("entradasEventoProcesado",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where evento=0 and entidad.id = "+entidadActiva.getId()));

        mav.addObject("salidasEventoProcesado",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where evento=0 and entidad.id = "+entidadActiva.getId()));


        mav.addObject("oficinas", oficinaEjb.findByEntidad(entidadActiva.getId()));

        return mav;
    }

    @RequestMapping(value = "/eventoOficioInternoEntradas")
    public String actualizarEventoOficioInterno(HttpServletRequest request) {

        try {

            Entidad entidadActiva = getEntidadActiva(request);
            List<Oficina> oficinas = oficinaEjb.findByEntidad(entidadActiva.getId());
            Integer total = 0;
            Integer parcial = 0;
            for (Oficina oficina : oficinas) {
                parcial = registroEntradaEjb.actualizarEventoOficioInterno(oficina);
                total = total + parcial;
            }

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a 'Oficio Interno' de la entidad ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoOficioInternoEntradas/{idOficina}")
    public String eventoOficioInternoEntradas(@PathVariable Long idOficina, HttpServletRequest request) {

        try {

            Oficina oficina = oficinaEjb.findById(idOficina);
            Integer total = registroEntradaEjb.actualizarEventoOficioInterno(oficina);

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a 'Oficio Interno' de la oficina " + oficina.getDenominacion());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoOficioExternoEntradas")
    public String actualizarEventoOficioExterno(HttpServletRequest request) {

        try {

            Entidad entidadActiva = getEntidadActiva(request);
            List<Oficina> oficinas = oficinaEjb.findByEntidad(entidadActiva.getId());
            Integer total = 0;
            Integer parcial = 0;
            for (Oficina oficina : oficinas) {
                parcial = registroEntradaEjb.actualizarEventoOficioExterno(oficina);
                total = total + parcial;
            }

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a OficioExterno de entrada de la entidad ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoOficioExternoEntradas/{idOficina}")
    public String eventoOficioExternoEntradas(@PathVariable Long idOficina, HttpServletRequest request) {

        try {

            Oficina oficina = oficinaEjb.findById(idOficina);
            Integer total = registroEntradaEjb.actualizarEventoOficioExterno(oficina);

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a 'Oficio Externo' de la oficina " + oficina.getDenominacion());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoDistribuirEntradas")
    public String actualizarEventoDistribuir(HttpServletRequest request) {

        try {

            Entidad entidadActiva = getEntidadActiva(request);
            List<Oficina> oficinas = oficinaEjb.findByEntidad(entidadActiva.getId());
            Integer total = 0;
            Integer parcial =0;
            for (Oficina oficina : oficinas) {
                parcial = registroEntradaEjb.actualizarEventoDistribuir(oficina);
                total = total + parcial;
            }

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de entrada a Distribuir de la entidad");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoDistribuirEntradas/{idOficina}")
    public String eventoDistribuirEntradas(@PathVariable Long idOficina, HttpServletRequest request) {

        try {

            Oficina oficina = oficinaEjb.findById(idOficina);
            Integer total = registroEntradaEjb.actualizarEventoDistribuir(oficina);

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de salida a 'Distribuir' de la oficina " + oficina.getDenominacion());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoDistribuirSalidas/{idOficina}")
    public String actualizarEventoDistribuirSalidas(@PathVariable Long idOficina, HttpServletRequest request) {

        try {

            Oficina oficina = oficinaEjb.findById(idOficina);
            Integer total = registroSalidaEjb.actualizarEventoDistribuirSalidas(oficina, oficina.getOrganismoResponsable().getEntidad());

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de salida a Distribuir de la oficina " + oficina.getDenominacion());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoDistribuirSalidas")
    public String actualizarEventoDistribuirSalidas(HttpServletRequest request) {

        try {

            Entidad entidadActiva = getEntidadActiva(request);
            List<Oficina> oficinas = oficinaEjb.findByEntidad(entidadActiva.getId());
            Integer total = 0;
            Integer parcial = 0;
            for (Oficina oficina : oficinas) {
                parcial = registroSalidaEjb.actualizarEventoDistribuirSalidas(oficina, oficina.getOrganismoResponsable().getEntidad());
                total += parcial;
                parcial = registroSalidaEjb.actualizarEventoDistribuirSalidasPersona(oficina, oficina.getOrganismoResponsable().getEntidad());
                total += parcial;
            }

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de salida a Distribuir de la entidad ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoDistribuirSalidasPersonas/{idOficina}")
    public String eventoDistribuirSalidasPersonas(@PathVariable Long idOficina, HttpServletRequest request) {

        try {

            Oficina oficina = oficinaEjb.findById(idOficina);
            Integer total = registroSalidaEjb.actualizarEventoDistribuirSalidasPersona(oficina, oficina.getOrganismoResponsable().getEntidad());

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de salida a Distribuir de la oficina " + oficina.getDenominacion());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

    @RequestMapping(value = "/eventoDistribuirSalidasPersonas")
    public String eventoDistribuirSalidasPersonas(HttpServletRequest request) {

        try {

            Entidad entidadActiva = getEntidadActiva(request);
            List<Oficina> oficinas = oficinaEjb.findByEntidad(entidadActiva.getId());
            Integer total = 0;
            Integer parcial = 0;
            for (Oficina oficina : oficinas) {
                parcial = registroSalidaEjb.actualizarEventoDistribuirSalidasPersona(oficina, oficina.getOrganismoResponsable().getEntidad());
                total = total + parcial;
            }

            Mensaje.saveMessageInfo(request, "Se han actualizado " +total+" registros de salida a Distribuir de la entidad ");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/eventos";
    }

}
