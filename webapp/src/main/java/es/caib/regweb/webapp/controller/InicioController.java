package es.caib.regweb.webapp.controller;

import es.caib.regweb.model.*;
import es.caib.regweb.model.utils.RegistroBasico;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Controller
public class InicioController extends BaseController{

    //protected final Logger log = Logger.getLogger(getClass());
    
    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;
    
    @EJB(mappedName = "regweb/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;
    
    @EJB(mappedName = "regweb/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;
    
    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;

    @EJB(mappedName = "regweb/LopdEJB/local")
    public LopdLocal lopdEjb;

    @EJB(mappedName = "regweb/DescargaEJB/local")
    public DescargaLocal descargaEjb;


    @RequestMapping(value = "/inici")
    public ModelAndView principal(HttpServletRequest request, Model model) throws Exception{

        ModelAndView mav = new ModelAndView("inicio");
        Oficina oficinaActiva = getOficinaActiva(request);


        // Solo obtenemos los datos para el dashboard si el Usuario es Operador
        if(isOperador(request) && oficinaActiva != null){

            List<Libro> librosAdministrados = getLibrosAdministrados(request);

            /*Registros Pendientes de Visar y con Reserva de Numero*/
            if(librosAdministrados!= null && librosAdministrados.size() > 0){
                //List<RegistroBasico> pendientesVisar = registroEntradaEjb.getByLibrosEstado(librosAdministrados, RegwebConstantes.ESTADO_PENDIENTE_VISAR);
                //model.addAttribute("pendientesVisar", pendientesVisar);
            }

            /* RESERVA DE NÚMERO */
            List<RegistroBasico> pendientes = registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.ESTADO_PENDIENTE, RegwebConstantes.REGISTROS_PANTALLA_INICIO);
            model.addAttribute("pendientes", pendientes);

            /* OFICIOS PENDIENTES DE REMISIÓN */
            // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
            List<Libro> librosRegistro = getLibrosRegistroEntrada(request);

            // Obtenemos los Organismos Internos que tienen Registros pendientes de tramitar por medio de un Oficio de Revisión,
            Set<String> organismosOficioRemisionInterna = new HashSet<String>();
            for (Libro libro : librosRegistro) {
                organismosOficioRemisionInterna.addAll(registroEntradaEjb.oficiosPendientesRemisionInterna(libro));
            }
            model.addAttribute("organismosOficioRemisionInterna", organismosOficioRemisionInterna);

            // Obtenemos los Organismos Externos que tienen Registros pendientes de tramitar por medio de un Oficio de Revisión,
            Set<String> organismosOficioRemisionExterna = new HashSet<String>();
            for (Libro libro : librosRegistro) {
                organismosOficioRemisionExterna.addAll(registroEntradaEjb.oficiosPendientesRemisionExterna(libro));
            }
            model.addAttribute("organismosOficioRemisionExterna", organismosOficioRemisionExterna);


            /* OFICIOS PENDIENTES DE LLEGADA */
            // Buscamos los Organismos en los que la OficinaActiva puede registrar
            Set<Organismo> organismos = new HashSet<Organismo>();  // Utilizamos un Set porque no permite duplicados
            organismos.add(oficinaActiva.getOrganismoResponsable());
            organismos.addAll(relacionOrganizativaOfiLocalEjb.getOrganismosByOficina(oficinaActiva.getId()));
            List<OficioRemision> oficiosPendientesLlegada = oficioRemisionEjb.oficiosPendientesLlegada(organismos);

            model.addAttribute("oficiosPendientesLlegada", oficiosPendientesLlegada);


            /* PREREGISTROS PENDIENTES DE PROCESAR */
            /* Buscamos los Últimos PreRegistros que están pendientes de procesar */
            List<PreRegistro> preRegistros = preRegistroEjb.getUltimosPreRegistrosPendientesProcesar(oficinaActiva.getCodigo(), RegwebConstantes.REGISTROS_PANTALLA_INICIO);

            model.addAttribute("preRegistros", preRegistros);

        }

        // Comprobación de si se ha hecho alguna sincronización del Catálogo DIR3
        if (isSuperAdmin(request) || isAdminEntidad(request)) {
            Descarga catalogo = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);
            model.addAttribute("catalogo", catalogo);
        }


        return mav;
    }


}
