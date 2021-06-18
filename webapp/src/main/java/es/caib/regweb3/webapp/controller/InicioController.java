package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.BasicForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashSet;

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

    @EJB(mappedName = "regweb3/ColaEJB/local")
    private ColaLocal colaEjb;


    @RequestMapping(value = "/inici")
    public ModelAndView principal(HttpServletRequest request, Model model) throws Exception{

        ModelAndView mav = new ModelAndView("inicio");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        // DASHBOARD OPERADOR
        if(isOperador(request) && oficinaActiva != null){

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            /* RESERVA DE NÚMERO */
            mav.addObject("reservas", registroEntradaConsultaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.REGISTRO_RESERVA, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

            /* OFICIOS PENDIENTES DE REMISIÓN */
            if(entidadActiva.getOficioRemision()){


                //mav.addObject("organismosOficioRemisionEntradaInternos", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionInternos(oficinaActiva.getId(), librosRegistroEntrada, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("organismosOficioRemisionEntradaExternos", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(),  RegwebConstantes.EVENTO_OFICIO_EXTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

                // Oficios de entrada SIR
                if(entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {
                    mav.addObject("organismosOficioRemisionEntradaSir", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), RegwebConstantes.EVENTO_OFICIO_SIR, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                }

                // Obtenemos los Oficios pendientes de Llegada
                mav.addObject("oficiosPendientesLlegada", oficioRemisionEjb.oficiosPendientesLlegada(organismosOficinaActiva, RegwebConstantes.REGISTROS_PANTALLA_INICIO));


                // Obtenemos los Organismos que tienen Registros de salida pendientes de tramitar por medio de un Oficio de Revisión,
                mav.addObject("organismosOficioRemisionSalidaExternos", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), RegwebConstantes.EVENTO_OFICIO_EXTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

                // Oficios de salida SIR
                if(entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {
                    mav.addObject("organismosOficioRemisionSalidaSir", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), RegwebConstantes.EVENTO_OFICIO_SIR, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                }

            }

            /* REGISTROS SIR */
            if(entidadActiva.getSir() && oficinaActiva.getSirRecepcion()) {
                mav.addObject("pendientesProcesarSir", registroSirEjb.getUltimosPendientesProcesar(oficinaActiva.getCodigo(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("entradasRechazadosReenviados", registroEntradaConsultaEjb.getSirRechazadosReenviados(oficinaActiva.getId(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("salidasRechazadasReenviadas", registroSalidaConsultaEjb.getSirRechazadosReenviados(oficinaActiva.getId(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("pendientesDistribuir", trazabilidadEjb.getPendientesDistribuirSir(oficinaActiva.getId(),entidadActiva.getId(),RegwebConstantes.REGISTROS_PANTALLA_INICIO));
            }

            // Notificaciones
            mav.addObject("notificacionesPendientes", notificacionEjb.notificacionesPendientes(usuarioEntidad.getId()));

        }

        // DASHBOARD ADMINISTRADOR ENTIDAD
        if (isAdminEntidad(request) && entidadActiva != null) {

            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Últimas incidencias de Integraciones
            mav.addObject("incidenciasSir", integracionEjb.ultimasIntegracionesErrorTipo(entidadActiva.getId(), RegwebConstantes.INTEGRACION_SIR));
            mav.addObject("incidenciasJustificante", integracionEjb.ultimasIntegracionesErrorTipo(entidadActiva.getId(), RegwebConstantes.INTEGRACION_JUSTIFICANTE));
            mav.addObject("incidenciasFirma", integracionEjb.ultimasIntegracionesErrorTipo(entidadActiva.getId(), RegwebConstantes.INTEGRACION_FIRMA));
            mav.addObject("incidenciasWs", integracionEjb.ultimasIntegracionesErrorTipo(entidadActiva.getId(), RegwebConstantes.INTEGRACION_WS));
            model.addAttribute("integracion", new BasicForm());

            // Elementos de la Cola en estadro Error
            mav.addObject("erroresCola", colaEjb.getElementosError(entidadActiva.getId()));

            // Última sincronización de organismos
            mav.addObject("descargaUnidad", descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidadActiva.getId()));

            // Notificaciones
            mav.addObject("notificacionesPendientes", notificacionEjb.notificacionesPendientes(usuarioEntidad.getId()));

        }

        // Comprobación de si se ha hecho alguna sincronización del Catálogo DIR3
        if (isSuperAdmin(request) || isAdminEntidad(request)) {
            if(descargaEjb.findByTipo(RegwebConstantes.CATALOGO) == null){
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("catalogoDir3.catalogo.vacio"));

            }
        }

        // Si es SuperAdmin redireccionamos al listado de Entidades
        if (isSuperAdmin(request)){
            mav.setViewName("redirect:/entidad/list");
        }

        return mav;
    }


}
