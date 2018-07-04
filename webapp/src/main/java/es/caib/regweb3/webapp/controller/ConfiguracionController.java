package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.model.Configuracion;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.persistence.ejb.ConfiguracionLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.ConfiguracionForm;
import es.caib.regweb3.webapp.utils.ArchivoFormManager;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Configuracion}
 * @author jpernia
 * Date: 07/07/15
 */
@Controller
@SessionAttributes(types = ConfiguracionForm.class)
@RequestMapping(value = "/configuracion")
public class ConfiguracionController extends BaseController {

    @EJB(mappedName = "regweb3/ArchivoEJB/local")
    private ArchivoLocal archivoEjb;

    @EJB(mappedName = "regweb3/ConfiguracionEJB/local")
    private ConfiguracionLocal configuracionEjb;

    /**
     * Carga el formulario para editar la {@link es.caib.regweb3.model.Configuracion}
     */
    @RequestMapping(value = "/editar", method = RequestMethod.GET)
    public String configuracion(Model model, HttpServletRequest request) {

        ConfiguracionForm configuracionForm = new ConfiguracionForm();

        try {

            //Si el usuario es SUPERADMIN PUEDE EDITAR LA CONFIGURACION
            Rol rolActivo = getRolActivo(request);
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                Mensaje.saveMessageError(request, getMessage("usuario.asignar.permisos.denegado"));
                return "redirect:/inici";
            }

            //Si ya se ha creado una configuracion previa
            if(configuracionEjb.getTotal()>0) {

                Configuracion configuracion = configuracionEjb.getAll().get(0);
                configuracionForm.setConfiguracion(configuracion);

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(configuracionForm);

        return "configuracion/configuracionForm";
    }

    /**
     * Editar la {@link es.caib.regweb3.model.Configuracion}
     */
    @RequestMapping(value = "/editar", method = RequestMethod.POST)
    public String configuracion(@ModelAttribute @Valid ConfiguracionForm configuracionForm,BindingResult result,Model model,
                                SessionStatus status, HttpServletRequest request) {

        String destino = "redirect:/inici";

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            return "configuracion/configuracionForm";
        }
        // Si no hay errores actualizamos la configuracion
        ArchivoFormManager afm;

        try {
            Configuracion configuracion = configuracionForm.getConfiguracion();

            //Modificación con archivo Logo Menú o Logo Pie
            if((configuracionForm.getLogoMenu() != null)||(configuracionForm.getLogoPie() != null)){

                Archivo eliminarLogoMenu = null;
                Archivo eliminarLogoPie = null;

                if(configuracionForm.getLogoMenu() != null && !configuracionForm.getLogoMenu().isEmpty() ){

                    afm = new ArchivoFormManager(archivoEjb,configuracionForm.getLogoMenu(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                    if(configuracionForm.getConfiguracion().getId()!=null) {
                        Configuracion configuracionGuardada = configuracionEjb.findById(configuracionForm.getConfiguracion().getId());
                        eliminarLogoMenu = configuracionGuardada.getLogoMenu();
                    }

                    // Asociamos el nuevo archivo
                    configuracion.setLogoMenu(afm.prePersist(null));
                }

                if(configuracionForm.getLogoPie() != null && !configuracionForm.getLogoPie().isEmpty()){

                    afm = new ArchivoFormManager(archivoEjb,configuracionForm.getLogoPie(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                    if(configuracionForm.getConfiguracion().getId()!=null) {
                        Configuracion configuracionGuardada = configuracionEjb.findById(configuracionForm.getConfiguracion().getId());
                        eliminarLogoPie = configuracionGuardada.getLogoPie();
                    }

                    // Asociamos el nuevo archivo
                    configuracion.setLogoPie(afm.prePersist(null));
                }


                // Si se selecciona el check de borrar Logo Menu y/o Logo Pie, se pone a null
                if(configuracionForm.isBorrarLogoMenu()){
                    Configuracion configuracionGuardada = configuracionEjb.findById(configuracionForm.getConfiguracion().getId());
                    eliminarLogoMenu = configuracionGuardada.getLogoMenu();
                    configuracion.setLogoMenu(null);
                }
                if(configuracionForm.isBorrarLogoPie()){
                    Configuracion configuracionGuardada = configuracionEjb.findById(configuracionForm.getConfiguracion().getId());
                    eliminarLogoPie = configuracionGuardada.getLogoPie();
                    configuracion.setLogoPie(null);
                }

                configuracionEjb.merge(configuracion);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                status.setComplete();

                // Eliminamos el anterior Logo Menu
                if(eliminarLogoMenu != null){
                    FileSystemManager.eliminarArchivo(eliminarLogoMenu.getId());
                    archivoEjb.remove(eliminarLogoMenu);
                }

                // Eliminamos el anterior Logo Pie
                if(eliminarLogoPie != null){
                    FileSystemManager.eliminarArchivo(eliminarLogoPie.getId());
                    archivoEjb.remove(eliminarLogoPie);
                }


            }else{

                Archivo eliminarLogoMenu = null;
                Archivo eliminarLogoPie = null;

                if(configuracionForm.getConfiguracion().getId()!=null) {
                    Configuracion configuracionGuardada = configuracionEjb.findById(configuracionForm.getConfiguracion().getId());
                    // Si se selecciona el check de borrar Logo Menu y/o Logo Pie, se pone a null
                    if (configuracionForm.isBorrarLogoMenu()) {
                        eliminarLogoMenu = configuracionGuardada.getLogoMenu();
                        configuracion.setLogoMenu(null);
                    }
                    if (configuracionForm.isBorrarLogoPie()) {
                        eliminarLogoPie = configuracionGuardada.getLogoPie();
                        configuracion.setLogoPie(null);
                    }

                    if (configuracionForm.getConfiguracion().getLogoMenu() != null) {
                        configuracion.setLogoMenu(archivoEjb.findById(configuracionForm.getConfiguracion().getLogoMenu().getId()));
                    }
                }

                configuracionEjb.merge(configuracionForm.getConfiguracion());
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
                status.setComplete();

                if(configuracionForm.getConfiguracion().getId()!=null) {
                    // Eliminamos el anterior Logo Menu
                    if (eliminarLogoMenu != null) {
                        FileSystemManager.eliminarArchivo(eliminarLogoMenu.getId());
                        archivoEjb.remove(eliminarLogoMenu);
                    }

                    // Eliminamos el anterior Logo Pie
                    if (eliminarLogoPie != null) {
                        FileSystemManager.eliminarArchivo(eliminarLogoPie.getId());
                        archivoEjb.remove(eliminarLogoPie);
                    }
                }
            }

            // Asigna la Configuración del SuperAdministrador
            if(getRolActivo(request).getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                getLoginInfo(request).setConfiguracion(configuracion);
            }

            destino = "redirect:/inici";


        }catch (Exception e) {
            e.printStackTrace();
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
        }

        return destino;

    }


    @InitBinder("configuracionForm")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }


}

