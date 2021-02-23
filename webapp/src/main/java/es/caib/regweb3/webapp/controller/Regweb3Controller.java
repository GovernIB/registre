package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Plantilla;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.PlantillaLocal;
import es.caib.regweb3.persistence.ejb.RolLocal;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Controller
public class Regweb3Controller extends BaseController {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private LoginService loginService;
    
    @EJB(mappedName = "regweb3/RolEJB/local")
    private RolLocal rolEjb;

    @EJB(mappedName = "regweb3/PlantillaEJB/local")
    private PlantillaLocal plantillaEjb;



    @RequestMapping(value = "/rol/{rolId}")
    public String cambioRol(@PathVariable Long rolId, HttpServletRequest request) {

        try {
            Rol rolNuevo = rolEjb.findById(rolId);

            if(!loginService.cambioRol(rolNuevo, getLoginInfo(request))){
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

                loginService.cambioEntidad(entidadNueva, getLoginInfo(request));
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

        LinkedHashSet<Oficina> oficinasAutenticado = getOficinasAcceso(request);

        try {
            Oficina oficinaNueva = oficinaEjb.findById(oficinaId);
            if(oficinasAutenticado.contains(new Oficina(oficinaNueva.getId()))){
                loginService.asignarOficinaActiva(oficinaNueva, getLoginInfo(request));
                log.info("Cambio Oficina activa: " + oficinaNueva.getDenominacion() + " - " + oficinaNueva.getCodigo());
            }else{
                Mensaje.saveMessageError(request, getMessage("error.oficina.autorizacion"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/inici";
    }


    @RequestMapping(value = "/plantillasUsuario/{tipoRegistro}")
    public ModelAndView plantillasUsuario(@PathVariable Long tipoRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("modulos/menuPlantillas");

        List<Plantilla> plantillasUsuario = plantillaEjb.getActivasbyUsuario(getUsuarioEntidadActivo(request).getId(), tipoRegistro);
        mav.addObject("plantillasUsuario", plantillasUsuario);
        mav.addObject("tipoRegistro", tipoRegistro);

        return mav;
    }

}
