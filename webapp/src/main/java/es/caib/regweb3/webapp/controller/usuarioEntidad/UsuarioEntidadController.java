package es.caib.regweb3.webapp.controller.usuarioEntidad;

import es.caib.regweb3.model.Archivo;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.ArchivoLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.UsuarioEntidadBusquedaForm;
import es.caib.regweb3.webapp.form.UsuarioEntidadForm;
import es.caib.regweb3.webapp.utils.ArchivoFormManager;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.UsuarioEntidadValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link UsuarioEntidad}
 *
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@SessionAttributes(types = UsuarioEntidadForm.class)
@RequestMapping(value = "/usuarioEntidad")
public class UsuarioEntidadController extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UsuarioEntidadValidator usuarioEntidadValidator;

    @EJB(mappedName = ArchivoLocal.JNDI_NAME)
    private ArchivoLocal archivoEjb;


    /**
     * Listado de todos los usuarios de una Entidad
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listadoUsuariosEntidad(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        UsuarioEntidad usuarioEntidad = new UsuarioEntidad();
        usuarioEntidad.setClave(null);
        usuarioEntidad.setApodera(null);
        usuarioEntidad.setAsistencia(null);
        usuarioEntidad.setNotificacionEspontanea(null);
        usuarioEntidad.setBitcita(null);
        usuarioEntidad.setExterno(false);
        UsuarioEntidadBusquedaForm usuarioEntidadBusqueda = new UsuarioEntidadBusquedaForm(usuarioEntidad, 1);

        model.addAttribute("usuarioEntidadBusqueda", usuarioEntidadBusqueda);
        model.addAttribute("entidad", entidad);
        model.addAttribute("organismos", organismoEjb.getPermitirUsuarios(entidad.getId()));
        model.addAttribute("permisos", RegwebConstantes.PERMISOS);
        model.addAttribute("roles", RegwebConstantes.ROLES);

        return "usuarioEntidad/usuarioEntidadList";
    }

    /**
     * Listado de usuarios de una Entidad
     *
     * @param busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView usuariosEntidad(@ModelAttribute UsuarioEntidadBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("usuarioEntidad/usuarioEntidadList");
        Organismo organismo = busqueda.getOrganismo();
        Entidad entidad = getEntidadActiva(request);


        if(busqueda.getExportarUsuarios()){ // Creamos un excel con los resultados
            mav = new ModelAndView("exportarUsuariosExcel");
            Paginacion paginacion = usuarioEntidadEjb.busqueda(null, entidad.getId(), busqueda.getUsuarioEntidad(), organismo.getId(), busqueda.getPermiso(), busqueda.getRol());

            mav.addObject("resultados", paginacion);

        }else if(busqueda.getExportarPermisos()) { // Creamos un excel los permisos de los usuarios
            mav = new ModelAndView("exportarPermisosUsuariosExcel");
            Paginacion paginacion = usuarioEntidadEjb.busqueda(null, entidad.getId(), busqueda.getUsuarioEntidad(), organismo.getId(), busqueda.getPermiso(), busqueda.getRol());

            mav.addObject("resultados", paginacion);
            mav.addObject("organismo", organismoEjb.findById(organismo.getId()));

        }else{ // Búsqueda normal

            Paginacion paginacion = usuarioEntidadEjb.busqueda(busqueda.getPageNumber(),
                    entidad.getId(), busqueda.getUsuarioEntidad(), organismo.getId(), busqueda.getPermiso(), busqueda.getRol());
            busqueda.setPageNumber(1);
            mav.addObject("entidad", entidad);
            mav.addObject("paginacion", paginacion);
            mav.addObject("organismos", organismoEjb.getPermitirUsuarios(entidad.getId()));
            mav.addObject("usuarioEntidadBusqueda", busqueda);
            mav.addObject("permisos", RegwebConstantes.PERMISOS);
            mav.addObject("roles", RegwebConstantes.ROLES);
        }

        return mav;
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/edit", method = RequestMethod.GET)
    public String editarUsuarioEntidad(@PathVariable("idUsuarioEntidad") Long idUsuarioEntidad, Model model, HttpServletRequest request) throws I18NException {

        UsuarioEntidadForm usuarioEntidadForm = new UsuarioEntidadForm();

        if(isOperador(request)){ //Si es Operador, solo puede modificar su usuario.
            UsuarioEntidad usuarioAutenticado = getUsuarioEntidadActivo(request);

            if(!idUsuarioEntidad.equals(usuarioAutenticado.getId())){
                Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                return "redirect:/inici";
            }
        }

        UsuarioEntidad usuarioEntidad = null;

        try {
            usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);
            usuarioEntidadForm.setUsuarioEntidad(usuarioEntidad);
        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(usuarioEntidadForm);

        return "usuarioEntidad/usuarioEntidadForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.UsuarioEntidad}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/edit", method = RequestMethod.POST)
    public String editarUsuarioEntidad(@ModelAttribute @Valid UsuarioEntidadForm usuarioEntidadForm, BindingResult result, SessionStatus status, HttpServletRequest request) {

        usuarioEntidadValidator.validate(usuarioEntidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            return "usuarioEntidad/usuarioEntidadForm";
        }else { // Si no hay errores actualizamos el registro

            try {
                UsuarioEntidad usuarioEntidad = usuarioEntidadForm.getUsuarioEntidad();
                Archivo certificadoExistente = usuarioEntidadEjb.findById(usuarioEntidadForm.getUsuarioEntidad().getId()).getCertificadoCurso();
                Boolean eliminarCertificado = false;

                // Gestión Certificado
                if(usuarioEntidadForm.getCertificadoCurso() != null && !usuarioEntidadForm.getCertificadoCurso().isEmpty()){
                    // Guardamos el nuevo certificado
                    ArchivoFormManager afm = new ArchivoFormManager(archivoEjb, usuarioEntidadForm.getCertificadoCurso(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                    usuarioEntidad.setCertificadoCurso(afm.prePersist(null));
                    usuarioEntidad.setFechaCertificado(new Date());

                    // Actualizamos el usuario
                    usuarioEntidadEjb.merge(usuarioEntidad);

                    if(certificadoExistente != null){
                        eliminarCertificado = true;
                    }

                }else if(usuarioEntidadForm.isBorrarCertificado()){ // Se ha marcado la eliminación del certificado
                    eliminarCertificado = true;
                    usuarioEntidad.setCertificadoCurso(null);
                    usuarioEntidad.setFechaCertificado(null);
                }

                // Actualizamos el usuario
                usuarioEntidadEjb.merge(usuarioEntidad);

                if(eliminarCertificado){
                    FileSystemManager.eliminarArchivo(certificadoExistente.getId());
                    archivoEjb.remove(certificadoExistente);
                }

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
            } catch(I18NException i18ne) {
                log.error(I18NUtils.getMessage(i18ne), i18ne);
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();

            if (isOperador(request)) {
                return "redirect:/inici";
            } else{
                return "redirect:/usuarioEntidad/list";
            }

        }
    }


    @ModelAttribute("categorias")
    public Long[] categorias() {
        return RegwebConstantes.CATEGORIA_LABORAL;
    }

    @ModelAttribute("funciones")
    public Long[] funciones()  {
        return RegwebConstantes.FUNCION_OFICINA;
    }


    @InitBinder({"usuarioEntidad"})
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

        //binder.setValidator(this.usuarioEntidadValidator);
    }
}
