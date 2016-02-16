package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.manager.FicheroIntercambioManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.manager.impl.FicheroIntercambioManagerImpl;
import es.caib.regweb3.sir.ws.api.manager.impl.SicresXMLManagerImpl;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.utils.UsuarioService;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    
    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;
    
    @EJB(mappedName = "regweb3/RolEJB/local")
    public RolLocal rolEjb;
    
    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;
    
    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/RegistroMigradoEJB/local")
    public RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName = "regweb3/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;

    @EJB(mappedName = "regweb3/ReproEJB/local")
    public ReproLocal reproEjb;

    FicheroIntercambioManager ficheroIntercambioManager = new FicheroIntercambioManagerImpl();
    SicresXMLManager sicresXMLManager = new SicresXMLManagerImpl();
    

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

        try {
            Rol rolNuevo = rolEjb.findById(rolId);

            if(!usuarioService.cambioRol(rolNuevo, request)){
                Mensaje.saveMessageError(request, getMessage("error.rol.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";

    }

    @RequestMapping(value = "/cambioEntidad/{entidadId}")
    public String cambioEntidad(@PathVariable Long entidadId, HttpServletRequest request, HttpServletResponse response) {

        List<Entidad> entidadesAutenticado = getEntidadesAutenticado(request);

        try {
            Entidad entidadNueva = entidadEjb.findById(entidadId);

            if(entidadesAutenticado.contains(entidadNueva)){

                usuarioService.cambioEntidad(entidadNueva, request);
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
                usuarioService.tienePreRegistros(oficinaNueva, session);
                usuarioEntidadEjb.actualizarOficinaUsuario(usuarioEntidad.getId(), oficinaNueva.getId());
                log.info("Cambio Oficina activa: " + oficinaNueva.getDenominacion() + " - " + oficinaNueva.getCodigo());
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

    @RequestMapping(value = "/reprosUsuario/{tipoRegistro}")
    public ModelAndView reprosUsuario(@PathVariable Long tipoRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("modulos/menuRepros");

        List<Repro> reprosUsuario = reproEjb.getActivasbyUsuario(getUsuarioEntidadActivo(request).getId(), tipoRegistro);
        mav.addObject("reprosUsuario", reprosUsuario);

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
        FicheroIntercambio ficheroIntercambio = sicresXMLManager.crearFicheroIntercambioSICRES3(registroEntrada);

        ficheroIntercambioManager.enviarFicheroIntercambio(ficheroIntercambio);


        return "redirect:/inici";

    }


    /*@RequestMapping(value = "/crearPermisos")
    public String crearPermisos(HttpServletRequest request) throws Exception{

        Rol rolActivo = getRolActivo(request);
        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){

            log.info("Antes crearPermisosNoExistentes");
            log.info("-------------------------------------------");
            permisoLibroUsuarioEjb.crearPermisosNoExistentes();
            log.info("-------------------------------------------");
            log.info("despues crearPermisosNoExistentes");

        }

        return "redirect:/inici";

    }*/

}
