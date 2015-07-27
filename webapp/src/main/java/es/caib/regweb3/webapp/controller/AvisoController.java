package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Controller
@RequestMapping(value = "/avisos")
public class AvisoController extends BaseController {

    
    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;


    /**
     * Controller para gestionar los diferentes avisos de registros pendientes para el usuario
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "")
    public ModelAndView avisos(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("modulos/avisos");
        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            List<Libro> librosAdministrados = getLibrosAdministrados(request);
            List<Libro> librosRegistro = getLibrosRegistroEntrada(request);

            Long pendientesVisar = (long) 0;
            Long oficiosRemisionInterna = (long) 0;
            Long oficiosRemisionExterna = (long) 0;

            /*Registros Pendientes de Visar*/
            if(librosAdministrados!= null && librosAdministrados.size() > 0){
                pendientesVisar = registroEntradaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
            }
            mav.addObject("pendientesVisar", pendientesVisar);

            /*Rserva de número*/
            Long pendientes = registroEntradaEjb.getByOficinaEstadoCount(oficinaActiva.getId(), RegwebConstantes.ESTADO_PENDIENTE);
            mav.addObject("pendientes", pendientes);

            /* OFICIOS PENDIENTES DE REMISIÓN */
            if(librosRegistro!= null && librosRegistro.size() > 0){
                oficiosRemisionInterna = registroEntradaEjb.oficiosPendientesRemisionInternaCount(librosRegistro);
                oficiosRemisionExterna = registroEntradaEjb.oficiosPendientesRemisionExternaCount(librosRegistro);
            }
            mav.addObject("oficiosRemisionInterna", oficiosRemisionInterna);
            mav.addObject("oficiosRemisionExterna", oficiosRemisionExterna);

            /*OFICIOS PENDIENTES DE LLEGADA*/
            Long oficiosPendientesLlegada = oficioRemisionEjb.oficiosPendientesLlegadaCount(getOrganismosOficinaActiva(request));
            mav.addObject("oficiosPendientesLlegada", oficiosPendientesLlegada);

        }

        return mav;
    }

    @RequestMapping(value = "/pendientesVisar")
    public ModelAndView pendientesVisar(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("avisos/avisosList");
        mav.addObject("titulo",getMessage("registroEntrada.pendientesVisar"));

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            List<Libro> librosAdministrados = getLibrosAdministrados(request);
            List<Libro> librosRegistro = getLibrosRegistroEntrada(request);

            List<RegistroEntrada> registros = new ArrayList<RegistroEntrada>();

            if(librosAdministrados!= null && librosAdministrados.size() > 0){
                registros = registroEntradaEjb.getByLibrosEstado(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
            }
            mav.addObject("registros", registros);

        }

        return mav;
    }

    @RequestMapping(value = "/pendientes")
    public ModelAndView pendientes(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("avisos/avisosList");
        mav.addObject("titulo",getMessage("registroEntrada.pendientes"));

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            List<RegistroEntrada> registros = registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(),RegwebConstantes.ESTADO_PENDIENTE);
            mav.addObject("registros", registros);

        }

        return mav;
    }


}
