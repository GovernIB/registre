package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.BasicForm;
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

    @EJB(mappedName = "regweb3/RegistroSirEJB/local")
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEntradaUtilsEJB/local")
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;


    @RequestMapping(value = "/inici")
    public ModelAndView principal(HttpServletRequest request, Model model) throws Exception{

        ModelAndView mav = new ModelAndView("inicio");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        // DASHBOARD OPERADOR
        if(isOperador(request) && oficinaActiva != null){

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request);
            List<Libro> librosRegistroSalida = getLibrosRegistroSalida(request);


            /* RESERVA DE NÚMERO */
            mav.addObject("reservas", registroEntradaConsultaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.REGISTRO_RESERVA, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

            /* OFICIOS PENDIENTES DE REMISIÓN */
            if(entidadActiva.getOficioRemision()){

                // Comprueba si hay libros de Registro de entrada
                if(librosRegistroEntrada.size() > 0) {

                    // Obtenemos los Organismos que tienen Registros pendientes de tramitar por medio de un Oficio de Revisión
                    //mav.addObject("organismosOficioRemisionEntrada", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemision(oficinaActiva.getId(), librosRegistroEntrada,getOrganismosOficioRemision(organismosOficinaActiva), RegwebConstantes.REGISTROS_PANTALLA_INICIO));

                    mav.addObject("organismosOficioRemisionEntradaInternos", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionInternos(oficinaActiva.getId(), librosRegistroEntrada, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                    mav.addObject("organismosOficioRemisionEntradaExternos", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), librosRegistroEntrada, RegwebConstantes.EVENTO_OFICIO_EXTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                    mav.addObject("organismosOficioRemisionEntradaSir", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), librosRegistroEntrada,RegwebConstantes.EVENTO_OFICIO_SIR, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

                    // Obtenemos los Oficios pendientes de Llegada
                    mav.addObject("oficiosPendientesLlegada", oficioRemisionEjb.oficiosPendientesLlegada(organismosOficinaActiva, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                }

                if(librosRegistroSalida.size() > 0) {

                    // Obtenemos los Organismos que tienen Registros de salida pendientes de tramitar por medio de un Oficio de Revisión,
                    //mav.addObject("organismosOficioRemisionSalida", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemision(oficinaActiva.getId(), librosRegistroSalida, getOrganismosOficioRemisionSalida(organismosOficinaActiva), entidadActiva.getId(), 10));
                    mav.addObject("organismosOficioRemisionSalidaInternos", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), librosRegistroSalida, RegwebConstantes.EVENTO_OFICIO_INTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                    mav.addObject("organismosOficioRemisionSalidaExternos", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), librosRegistroSalida, RegwebConstantes.EVENTO_OFICIO_EXTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                    mav.addObject("organismosOficioRemisionSalidaSir", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), librosRegistroSalida, RegwebConstantes.EVENTO_OFICIO_SIR, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                }
            }

            /* REGISTROS SIR */
            if(entidadActiva.getSir() && oficinaActiva.getSirRecepcion()) {
                mav.addObject("pendientesProcesarSir", registroSirEjb.getUltimosPendientesProcesar(oficinaActiva.getCodigo(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("entradasRechazadosReenviados", registroEntradaConsultaEjb.getSirRechazadosReenviados(oficinaActiva.getId(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("salidasRechazadasReenviadas", registroSalidaConsultaEjb.getSirRechazadosReenviados(oficinaActiva.getId(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("pendientesDistribuir", trazabilidadEjb.getPendientesDistribuirSir(oficinaActiva.getId(),entidadActiva.getId(),getOrganismosOficioRemision(organismosOficinaActiva),RegwebConstantes.REGISTROS_PANTALLA_INICIO));
            }

        }

        // DASHBOARD ADMINISTRADOR ENTIDAD
        if (isAdminEntidad(request) && entidadActiva != null) {

            // Últimas incidencias de Integraciones
            mav.addObject("incidencias", integracionEjb.ultimasIntegracionesError(entidadActiva.getId()));
            model.addAttribute("integracion", new BasicForm());

            // Última sincronización de organismos
            mav.addObject("descargaUnidad", descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidadActiva.getId()));

        }

        // Comprobación de si se ha hecho alguna sincronización del Catálogo DIR3
        if (isSuperAdmin(request) || isAdminEntidad(request)) {
            mav.addObject("catalogo", descargaEjb.findByTipo(RegwebConstantes.CATALOGO));
        }


        return mav;
    }


}
