package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class InicioController extends BaseController{
    
    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSirEJB/local")
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEntradaUtilsEJB/local")
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;


    @RequestMapping(value = "/inici")
    public ModelAndView principal(HttpServletRequest request, Model model) throws Exception{

        ModelAndView mav = new ModelAndView("inicio");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        // Solo obtenemos los datos para el dashboard si el Usuario es Operador
        if(isOperador(request) && oficinaActiva != null){

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request);
            List<Libro> librosRegistroSalida = getLibrosRegistroSalida(request);


            /* RESERVA DE NÚMERO */
            mav.addObject("reservas", registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.REGISTRO_RESERVA, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

            /* OFICIOS PENDIENTES DE REMISIÓN */
            if(entidadActiva.getOficioRemision()){

                // Comprueba si hay libros de Registro de entrada
                if(librosRegistroEntrada.size() > 0) {

                    // Obtenemos los Organismos que tienen Registros pendientes de tramitar por medio de un Oficio de Revisión
                    mav.addObject("organismosOficioRemisionEntrada", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemision(oficinaActiva.getId(), librosRegistroEntrada,getOrganismosOficioRemision(request, organismosOficinaActiva)));

                    // Obtenemos los Oficios pendientes de Llegada
                    mav.addObject("oficiosPendientesLlegada", oficioRemisionEjb.oficiosPendientesLlegada(organismosOficinaActiva, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                }

                if(librosRegistroSalida.size() > 0) {

                    // Obtenemos los Organismos que tienen Registros de salida pendientes de tramitar por medio de un Oficio de Revisión,
                    mav.addObject("organismosOficioRemisionSalida", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemision(oficinaActiva.getId(), librosRegistroSalida, getOrganismosOficioRemisionSalida(organismosOficinaActiva)));
                }
            }

            /* Obtenemos los Registros Sir pendientes de procesar */
            if(entidadActiva.getSir() && oficinaActiva.getSir()) {
                mav.addObject("registrosSir", registroSirEjb.getUltimosPendientesProcesar(oficinaActiva.getCodigo(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
            }

        }

        // Comprobación de si se ha hecho alguna sincronización del Catálogo DIR3
        if (isSuperAdmin(request) || isAdminEntidad(request)) {
            Descarga catalogo = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);
            mav.addObject("catalogo", catalogo);
        }


        return mav;
    }


}
