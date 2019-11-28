package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.ejb.OficioRemisionEntradaUtilsLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionSalidaUtilsLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;
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
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEntradaUtilsEJB/local")
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;


    /**
     * Controller para gestionar los diferentes avisos de registros pendientes para el usuario
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "")
    public ModelAndView avisos(HttpServletRequest request) throws Exception{
        Long start = System.currentTimeMillis();
        ModelAndView mav = new ModelAndView("modulos/avisos");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            List<Libro> librosAdministrados = getLibrosAdministradosOficina(request);
            List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request);
            List<Libro> librosRegistroSalida = getLibrosRegistroSalida(request);

            Long pendientesVisarEntrada = (long) 0;
            Long pendientesVisarSalida = (long) 0;
            Long oficiosEntradaInternosPendientesRemision = (long) 0;
            Long oficiosEntradaExternosPendientesRemision = (long) 0;
            Long oficiosSalidaInternosPendientesRemision = (long) 0;
            Long oficiosSalidaExternosPendientesRemision = (long) 0;

            /*Registros Pendientes de Visar*/
            if(librosAdministrados!= null && librosAdministrados.size() > 0){
                pendientesVisarEntrada = registroEntradaConsultaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                pendientesVisarSalida = registroSalidaConsultaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            }
            mav.addObject("pendientesVisarEntrada", pendientesVisarEntrada);
            mav.addObject("pendientesVisarSalida", pendientesVisarSalida);

            /*Reserva de número*/
            mav.addObject("reservas", registroEntradaConsultaEjb.getByOficinaEstadoCount(oficinaActiva.getId(), RegwebConstantes.REGISTRO_RESERVA));

            /*Pendientes de distribuir*/
            //mav.addObject("validos", registroEntradaEjb.getByOficinaEstadoCount(oficinaActiva.getId(), RegwebConstantes.REGISTRO_VALIDO));

            /* OFICIOS DE REMISIÓN */
            if(entidadActiva.getOficioRemision()){

                // OFICIOS PENDIENTES DE REMISIÓN ENTRADA
                if(librosRegistroEntrada!= null && librosRegistroEntrada.size() > 0){

                    oficiosEntradaInternosPendientesRemision = oficioRemisionEntradaUtilsEjb.oficiosEntradaInternosPendientesRemisionCount(oficinaActiva.getId(),librosRegistroEntrada, getOrganismosOficioRemision(organismosOficinaActiva));
                    oficiosEntradaExternosPendientesRemision = oficioRemisionEntradaUtilsEjb.oficiosEntradaExternosPendientesRemisionCount(oficinaActiva.getId(),librosRegistroEntrada);
                }

                // OFICIOS PENDIENTES DE REMISIÓN SALIDA
                if(librosRegistroSalida!= null && librosRegistroSalida.size() > 0){

                    oficiosSalidaInternosPendientesRemision = oficioRemisionSalidaUtilsEjb.oficiosSalidaPendientesRemisionCount(oficinaActiva.getId(),librosRegistroSalida, RegwebConstantes.EVENTO_OFICIO_INTERNO);
                    oficiosSalidaExternosPendientesRemision = oficioRemisionSalidaUtilsEjb.oficiosSalidaPendientesRemisionCount(oficinaActiva.getId(),librosRegistroSalida, RegwebConstantes.EVENTO_OFICIO_EXTERNO);
                }

                mav.addObject("oficiosEntradaInternosPendientesRemision", oficiosEntradaInternosPendientesRemision);
                mav.addObject("oficiosEntradaExternosPendientesRemision", oficiosEntradaExternosPendientesRemision);
                mav.addObject("oficiosSalidaInternosPendientesRemision", oficiosSalidaInternosPendientesRemision);
                mav.addObject("oficiosSalidaExternosPendientesRemision", oficiosSalidaExternosPendientesRemision);

                // OFICIOS PENDIENTES DE LLEGADA
                mav.addObject("oficiosPendientesLlegada", oficioRemisionEjb.oficiosPendientesLlegadaCount(organismosOficinaActiva));
            }

            // Registros de Entrada Rechazados o Reenviados por SIR
            if(entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {
                mav.addObject("entradasRechazadosReenviados", registroEntradaConsultaEjb.getSirRechazadosReenviadosCount(oficinaActiva.getId()));
                mav.addObject("salidasRechazadasReenviadas", registroSalidaConsultaEjb.getSirRechazadosReenviadosCount(oficinaActiva.getId()));
            }

        }


        //log.info("TIEMPO CARGA Avisos: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        return mav;
    }

}
