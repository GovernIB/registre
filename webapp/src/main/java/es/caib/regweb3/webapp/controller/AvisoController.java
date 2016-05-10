package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
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

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;


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

            Long pendientesVisarEntrada = (long) 0;
            Long pendientesVisarSalida = (long) 0;
            Long oficiosRemisionInterna = (long) 0;
            Long oficiosRemisionExterna = (long) 0;

            /*Registros Pendientes de Visar*/
            if(librosAdministrados!= null && librosAdministrados.size() > 0){
                pendientesVisarEntrada = registroEntradaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                pendientesVisarSalida = registroSalidaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
            }
            mav.addObject("pendientesVisarEntrada", pendientesVisarEntrada);
            mav.addObject("pendientesVisarSalida", pendientesVisarSalida);

            /*Rserva de número*/
            Long pendientes = registroEntradaEjb.getByOficinaEstadoCount(oficinaActiva.getId(), RegwebConstantes.ESTADO_PENDIENTE);
            mav.addObject("pendientes", pendientes);

            /* OFICIOS PENDIENTES DE REMISIÓN */
            if(librosRegistro!= null && librosRegistro.size() > 0){
                oficiosRemisionInterna = registroEntradaEjb.oficiosPendientesRemisionInternaCount(librosRegistro, getOrganismosOficioRemision(request));
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

    @RequestMapping(value = "/pendientesVisar/{tipoRegistro}")
    public ModelAndView pendientesVisar(@PathVariable String tipoRegistro, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("avisos/pendientesVisarList");

        Oficina oficinaActiva = getOficinaActiva(request);
        List<Libro> librosAdministrados = getLibrosAdministrados(request);

        if(isOperador(request) && oficinaActiva != null && (librosAdministrados!= null && librosAdministrados.size() > 0)) {

            if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO)){

                mav.addObject("titulo",getMessage("registroEntrada.pendientesVisar"));
                List<RegistroEntrada> registrosEntrada = registroEntradaEjb.getByLibrosEstado(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                mav.addObject("registros", registrosEntrada);
                mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO);

            }else if(tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO)){

                mav.addObject("titulo", getMessage("registroSalida.pendientesVisar"));
                List<RegistroSalida> registrosSalida = registroSalidaEjb.getByLibrosEstado(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                mav.addObject("registros", registrosSalida);
                mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO);

            }else{
                mav.addObject("registros", null);
            }

        }

        return mav;
    }

    @RequestMapping(value = "/pendientes")
    public ModelAndView pendientes(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("avisos/pendientesList");

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            List<RegistroEntrada> registros = registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(),RegwebConstantes.ESTADO_PENDIENTE);
            mav.addObject("registros", registros);

        }

        return mav;
    }


}
