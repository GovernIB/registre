package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
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

    //protected final Logger log = Logger.getLogger(getClass());
    
    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;
    
    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;
    
    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/AsientoRegistralSirEJB/local")
    public AsientoRegistralSirLocal asientosRegistralSirEjb;

    @EJB(mappedName = "regweb3/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    public DescargaLocal descargaEjb;

    @EJB(mappedName = "regweb3/OficioRemisionUtilsEJB/local")
    public OficioRemisionUtilsLocal oficioRemisionUtilsEjb;


    @RequestMapping(value = "/inici")
    public ModelAndView principal(HttpServletRequest request, Model model) throws Exception{

        ModelAndView mav = new ModelAndView("inicio");
        Oficina oficinaActiva = getOficinaActiva(request);


        // Solo obtenemos los datos para el dashboard si el Usuario es Operador
        if(isOperador(request) && oficinaActiva != null){

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            List<Libro> librosAdministrados = getLibrosAdministrados(request);
            // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
            List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request);

            /*Registros Pendientes de Visar y con Reserva de Numero*/
            if(librosAdministrados!= null && librosAdministrados.size() > 0){
                //List<RegistroBasico> pendientesVisar = registroEntradaEjb.getByLibrosEstado(librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                //model.addAttribute("pendientesVisar", pendientesVisar);
            }

            /* RESERVA DE NÚMERO */
            List<RegistroBasico> pendientes = registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.REGISTRO_PENDIENTE, RegwebConstantes.REGISTROS_PANTALLA_INICIO);
            mav.addObject("pendientes", pendientes);

            /* OFICIOS PENDIENTES DE REMISIÓN */

            // Obtenemos los Organismos Internos que tienen Registros pendientes de tramitar por medio de un Oficio de Revisión,
            mav.addObject("organismosOficioRemisionInterna", oficioRemisionUtilsEjb.organismosPendientesRemisionInterna(librosRegistroEntrada, getOrganismosOficioRemision(request, organismosOficinaActiva)));

            // Obtenemos los Organismos Externos que tienen Registros pendientes de tramitar por medio de un Oficio de Revisión,
            mav.addObject("organismosOficioRemisionExterna", oficioRemisionUtilsEjb.organismosPendientesRemisionExterna(librosRegistroEntrada));


            /* OFICIOS PENDIENTES DE LLEGADA */
            List<OficioRemision> oficiosPendientesLlegada = oficioRemisionEjb.oficiosPendientesLlegada(organismosOficinaActiva, RegwebConstantes.REGISTROS_PANTALLA_INICIO);

            mav.addObject("oficiosPendientesLlegada", oficiosPendientesLlegada);


            /* ASIENTOS REGISTRALES SIR PENDIENTES DE PROCESAR */
            /* Buscamos los Últimos AsientoRegistralSir que están pendientes de procesar*/
            if(isSir(request) && librosRegistroEntrada.size() > 0) { // Sólo muestra los AsientoRegistralSir si tiene permisos de RegistroEntrada
                List<AsientoRegistralSir> asientosRegistralesSir = asientosRegistralSirEjb.getUltimosARSPendientesProcesar(oficinaActiva.getCodigo(), RegwebConstantes.REGISTROS_PANTALLA_INICIO);
                mav.addObject("asientosRegistralesSir", asientosRegistralesSir);
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
