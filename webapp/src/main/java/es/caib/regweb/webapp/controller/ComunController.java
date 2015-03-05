package es.caib.regweb.webapp.controller;

import es.caib.regweb.model.*;
import es.caib.regweb.model.utils.ObjetoBasico;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.sir.FicheroIntercambioSICRES3;
import es.caib.regweb.persistence.utils.sir.SirUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.utils.UsuarioService;
import es.caib.regweb.ws.sir.api.wssir6b.RespuestaWS;
import es.caib.regweb.ws.sir.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb.ws.sir.api.wssir6b.WS_SIR6_B_PortType;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
public class ComunController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private UsuarioService usuarioService;
    
    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;
    
    @EJB(mappedName = "regweb/RolEJB/local")
    public RolLocal rolEjb;
    
    @EJB(mappedName = "regweb/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;
    
    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;
    
    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;
    
    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb/SirEJB/local")
    public SirLocal sirEjb;

    @EJB(mappedName = "regweb/RegistroMigradoEJB/local")
    public RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName = "regweb/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;
    

    @RequestMapping(value = "/noAutorizado")
    public ModelAndView noautorizado(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("noAutorizado");
    }

    @RequestMapping(value = "/accesoDenegado")
    public ModelAndView accesoDenegado(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("accesoDenegado");
    }

    @RequestMapping(value = "/rol/{rolId}")
    public String cambioRol(@PathVariable Long rolId, HttpServletRequest request) {

        HttpSession session = request.getSession();

        try {
            //Obtenemos los Roles del Usuario mediante el pluging de identificación
            List<Rol> rolesAutentido = usuarioService.obtenerRoles(getUsuarioAutenticado(request));

            Rol rolNuevo = rolEjb.findById(rolId);

            // Comprobamos que disponemos del Rol que queremos utilizar
            if(rolesAutentido.contains(rolNuevo)){
                session.setAttribute(RegwebConstantes.SESSION_ROL, rolNuevo);
                usuarioService.autorizarRol(rolNuevo, request);
            }else{
                Mensaje.saveMessageError(request, getMessage("error.rol.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/cambioEntidad/{entidadId}")
    public String cambioEntidad(@PathVariable Long entidadId, HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();

        List<Entidad> entidadesAutenticado = getEntidadesAutenticado(request);
        try {
            Entidad entidadNueva = entidadEjb.findById(entidadId);

            if(entidadesAutenticado.contains(entidadNueva)){
                session.removeAttribute(RegwebConstantes.SESSION_ENTIDAD);
                session.setAttribute(RegwebConstantes.SESSION_ENTIDAD, entidadNueva);
                if(isOperador(request)){
                    usuarioService.asignarOficinasRegistro(getUsuarioAutenticado(request),session);
                    session.setAttribute(RegwebConstantes.SESSION_MIGRADOS, registroMigradoEjb.tieneRegistrosMigrados(entidadNueva.getId()));
                    if(getOficinaActiva(request) != null) {
                        session.setAttribute(RegwebConstantes.SESSION_TIENEPREREGISTROS, preRegistroEjb.tienePreRegistros(getOficinaActiva(request).getCodigo()));
                    }
                }else{
                    session.setAttribute(RegwebConstantes.SESSION_MIGRADOS, false);
                    session.setAttribute(RegwebConstantes.SESSION_TIENEPREREGISTROS, false);
                }
            }else{
                Mensaje.saveMessageError(request, getMessage("error.entidad.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/cambioOficina/{oficinaId}")
    public String cambioOficina(@PathVariable Long oficinaId, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        UsuarioEntidad usuarioEntidad= getUsuarioEntidadActivo(request);

        Set<ObjetoBasico> oficinasAutenticado = getOficinasAutenticado(request);

        try {
            Oficina oficinaNueva = oficinaEjb.findById(oficinaId);
            if(oficinasAutenticado.contains(new ObjetoBasico(oficinaNueva.getId()))){
                session.setAttribute(RegwebConstantes.SESSION_OFICINA, oficinaNueva);
                session.setAttribute(RegwebConstantes.SESSION_TIENEPREREGISTROS, preRegistroEjb.tienePreRegistros(oficinaNueva.getCodigo()));
                usuarioEntidadEjb.actualizarOficinaUsuario(usuarioEntidad.getId(), oficinaNueva.getId());
            }else{
                Mensaje.saveMessageError(request, getMessage("error.oficina.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/error/404")
    public ModelAndView error(HttpServletRequest request) throws Exception{

        log.info("Dentro de error404 : " + request.getRequestURL());

        ModelAndView mav = new ModelAndView("error");

        mav.addObject("exception", "Error 404, la página solicitada no existe");
        mav.addObject("trazaError", "La página solicitada no existe");
        mav.addObject("url", request.getRequestURL());

        return mav;
    }

    @RequestMapping(value = "/aviso")
    public ModelAndView aviso(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("aviso");

        return mav;
    }

    /**
     * Controller para gestionar los diferentes avisos de registros pendientes para el usuario
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/avisos")
    public ModelAndView avisos(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("modulos/avisos");

        HttpSession session = request.getSession();
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);

        if(oficinaActiva != null) {
            //List<RegistroEntrada> pendientesVisar = registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.ESTADO_PENDIENTE_VISAR, RegwebConstantes.REGISTROS_PANTALLA_INICIO);
            //List<RegistroEntrada> pendientes = registroEntradaEjb.getByOficinaEstado(oficinaActiva.getId(), RegwebConstantes.ESTADO_PENDIENTE, RegwebConstantes.REGISTROS_PANTALLA_INICIO);

            /*OFICIOS PENDIENTES DE LLEGADA*/
            // Buscamos los Organismos en los que la OficinaActiva puede registrar
            Set<Organismo> organismos = new HashSet<Organismo>();  // Utilizamos un Set porque no permite duplicados
            organismos.add(oficinaActiva.getOrganismoResponsable());
            organismos.addAll(relacionOrganizativaOfiLocalEjb.getOrganismosByOficina(oficinaActiva.getId()));
            List<OficioRemision> oficiosPendientesLlegada = oficioRemisionEjb.oficiosPendientesLlegada(organismos);

            //mav.addObject("pendientesVisar", pendientesVisar.size());
           // mav.addObject("pendientes", pendientes.size());
            mav.addObject("oficiosPendientesLlegada", oficiosPendientesLlegada);
        }


        return mav;
    }

    @RequestMapping(value = "/sesion")
    public ModelAndView sesion(HttpServletRequest request, HttpServletResponse response) throws Exception{

        ModelAndView mav = new ModelAndView("sesion");

        //HttpSession session = 
        request.getSession();


        return mav;
    }

    /**
     * Obtiene el nombre traducido de un Transporte.
     */
    @RequestMapping(value = "/obtenerTransporte", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerTransporte(@RequestParam Long id) throws Exception {
      
      if (id != null) {
        return I18NUtils.tradueix("transporte." + id);
      } else {
        return null;
      }
      
/*
        Transporte transporte = transporteEjb.findById(id);

        if(transporte != null){
            Locale locale = LocaleContextHolder.getLocale();
            TraduccionTransporte traduccionTransporte = (TraduccionTransporte) transporte.getTraduccion(locale.getLanguage());
            return traduccionTransporte.getNombre();

        }
        */       
    }

    /**
     * Obtiene el nombre traducido de un TipoDocumentacionFisica.
     */
    
    @RequestMapping(value = "/obtenerTipoDocumentacionFisica", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
    @ResponseBody
    public String obtenerTipoDocumentacionFisica(@RequestParam Long id) throws Exception {

      /*
        TipoDocumentacionFisica tipoDocumentacionFisica = tipoDocumentacionFisicaEjb.findById(id);

        if(tipoDocumentacionFisica != null){
            Locale locale = LocaleContextHolder.getLocale();
            TraduccionTipoDocumentacionFisica traduccion = (TraduccionTipoDocumentacionFisica) tipoDocumentacionFisica.getTraduccion(locale.getLanguage());
            return traduccion.getNombre();

        }
        return null;
        */
      return I18NUtils.tradueix("tipoDocumentacionFisica." + id);
      
    }

    @RequestMapping(value = "/sir/{registroId}")
    public String pruebaSir(@PathVariable Long registroId, HttpServletRequest request, HttpServletResponse response) throws Exception{

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(registroId);

        WS_SIR6_BServiceLocator locator = new WS_SIR6_BServiceLocator();
        WS_SIR6_B_PortType ws_sir6_b = locator.getWS_SIR6_B();

        FicheroIntercambioSICRES3 fiSICRES3 =  sirEjb.writeFicheroIntercambioSICRES3(registroEntrada);
        RespuestaWS respuestaWs = ws_sir6_b.recepcionFicheroDeAplicacion(SirUtils.marshallObject(fiSICRES3));


        log.info("Respuesta codigo: "+ respuestaWs.getCodigo());
        log.info("Respuesta descripcion: "+ respuestaWs.getDescripcion());

        return "redirect:/inici";

    }
}
