package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Repro;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.ReproLocal;
import es.caib.regweb3.persistence.ejb.RolLocal;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.utils.UsuarioService;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Controller
public class ComunController extends BaseController {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private UsuarioService usuarioService;
    
    @EJB(mappedName = "regweb3/RolEJB/local")
    private RolLocal rolEjb;

    @EJB(mappedName = "regweb3/ReproEJB/local")
    private ReproLocal reproEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;


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
            return comprobarConfiguracionPluginsPropiedadesGlobalesByRol(request,rolNuevo);

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

        LinkedHashSet<Oficina> oficinasAutenticado = getOficinasAutenticado(request);

        try {
            Oficina oficinaNueva = oficinaEjb.findById(oficinaId);
            if(oficinasAutenticado.contains(new Oficina(oficinaNueva.getId()))){
                usuarioService.asignarOficinaActiva(oficinaNueva,session);
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

        return new ModelAndView("aviso");
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
     * Método que comprueba que esten configurados los plugins necesarios para el funcionamiento
     * básico de la aplicación, en función del rol
     * @param request
     * @param rol
     * @return
     * @throws Exception
     */
    private String comprobarConfiguracionPluginsPropiedadesGlobalesByRol(HttpServletRequest request, Rol rol) throws Exception{

        //Si es operador
        if(rol.getNombre().equals(RegwebConstantes.ROL_USUARI)) {
            //Comprobamos la existencia de plugins necesarios para el funcionamiento de la aplicación
            Entidad entidadActiva = getEntidadActiva(request);
            try {
                //Plugin Generación Justificante
                if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE)) {
                    log.info("No existe el plugin de generación del justificante");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginjustificante"));
                    return "redirect:/aviso";
                }
                //Plugin Custodia Justificante
                if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE)) {
                    log.info("No existe el plugin de custodia del justificante");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodiajustificante"));
                    return "redirect:/aviso";

                }
                // Plugin Custodia
                if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA)) {
                    log.info("No existe el plugin de custodia");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodia"));
                    return "redirect:/aviso";

                }
                //Plugin Firma en Servidor
                if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_FIRMA_SERVIDOR)) {
                    log.info("No existe el plugin de firma en servidor");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginfirma"));
                    return "redirect:/aviso";

                }

                if (entidadActiva.getSir() && Configuracio.getSirServerBase() == null) {
                    log.info("Error, falta propiedad sirserverbase");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.sirserverbase"));
                    return "redirect:/aviso";
                }

              /*  //Comprobaciones url de prergistro
                if(Configuracio.isCAIB() && Configuracio.getUrlPreregistre()== null){
                    log.info("No está definida la url de preregistro. ");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.preregistro"));
                    return "redirect:/aviso";
                }*/

            }  catch (I18NException i18ne) {
                throw new Exception(i18ne);
            }
        }

        return "redirect:/inici";
    }

}
