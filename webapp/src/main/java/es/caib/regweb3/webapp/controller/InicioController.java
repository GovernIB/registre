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
    
    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = RegistroSirLocal.JNDI_NAME)
    private RegistroSirLocal registroSirEjb;

    @EJB(mappedName = DescargaLocal.JNDI_NAME)
    private DescargaLocal descargaEjb;

    @EJB(mappedName = OficioRemisionEntradaUtilsLocal.JNDI_NAME)
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName =OficioRemisionSalidaUtilsLocal.JNDI_NAME)
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @EJB(mappedName = TrazabilidadLocal.JNDI_NAME)
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = IntegracionLocal.JNDI_NAME)
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = ColaLocal.JNDI_NAME)
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

            mav.addObject("organismosOficioRemisionEntradaExternos", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(entidadActiva.getId(), oficinaActiva.getId(),  RegwebConstantes.EVENTO_OFICIO_EXTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

            // Oficios de entrada SIR
            if(entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {
                mav.addObject("organismosOficioRemisionEntradaSir", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(entidadActiva.getId(), oficinaActiva.getId(), RegwebConstantes.EVENTO_OFICIO_SIR, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
            }

            // Obtenemos los Oficios pendientes de Llegada
            mav.addObject("oficiosPendientesLlegada", oficioRemisionEjb.oficiosPendientesLlegada(organismosOficinaActiva, RegwebConstantes.REGISTROS_PANTALLA_INICIO));


            // Obtenemos los Organismos que tienen Registros de salida pendientes de tramitar por medio de un Oficio de Revisión,
            mav.addObject("organismosOficioRemisionSalidaExternos", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(entidadActiva.getId(), oficinaActiva.getId(), RegwebConstantes.EVENTO_OFICIO_EXTERNO, RegwebConstantes.REGISTROS_PANTALLA_INICIO));

            // Oficios de salida SIR
            if(entidadActiva.getSir() && oficinaActiva.getSirEnvio()) {
                mav.addObject("organismosOficioRemisionSalidaSir", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(entidadActiva.getId(), oficinaActiva.getId(), RegwebConstantes.EVENTO_OFICIO_SIR, RegwebConstantes.REGISTROS_PANTALLA_INICIO));
            }

            /* REGISTROS SIR */
            if(entidadActiva.getSir() && oficinaActiva.getSirRecepcion()) {
                mav.addObject("pendientesProcesarSir", registroSirEjb.getUltimosPendientesProcesar(oficinaActiva.getCodigo(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("entradasRechazadosReenviados", registroEntradaConsultaEjb.getSirRechazadosReenviados(entidadActiva.getId(), oficinaActiva.getId(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
                mav.addObject("salidasRechazadasReenviadas", registroSalidaConsultaEjb.getSirRechazadosReenviados(entidadActiva.getId(), oficinaActiva.getId(), RegwebConstantes.REGISTROS_PANTALLA_INICIO));
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

            // SIR
            if(entidadActiva.getSir()){

                // SIR: Envios con estado OFICIO_SIR_ENVIADO o OFICIO_SIR_REENVIADO y 10 reintentos
                mav.addObject("enviadosSir", oficioRemisionEjb.getEnviadosSinAckMaxReintentos(entidadActiva.getId()));

                // SIR: Envios con estado OFICIO_SIR_ENVIADO o OFICIO_SIR_REENVIADO y 10 reintentos
                mav.addObject("enviadosErrorSir", oficioRemisionEjb.getEnviadosErrorMaxReintentos(entidadActiva.getId()));
            }

            // Elementos de la Cola en estadro Error
            mav.addObject("erroresCola", colaEjb.getElementosError(entidadActiva.getId()));

            // Notificaciones
            mav.addObject("notificacionesPendientes", notificacionEjb.notificacionesPendientes(usuarioEntidad.getId()));

        }

        // Comprobación de si se ha hecho alguna sincronización del Catálogo DIR3
        if ((isSuperAdmin(request) || isAdminEntidad(request)) && (descargaEjb.findByTipo(RegwebConstantes.CATALOGO) == null)){

            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("catalogoDir3.catalogo.vacio"));
        }

        // Si es SuperAdmin redireccionamos al listado de Entidades
        if (isSuperAdmin(request)){
            mav.setViewName("redirect:/entidad/list");
        }

        return mav;
    }


}
