package es.caib.regweb3.webapp.controller.entidad;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.editor.UsuarioEntidadEditor;
import es.caib.regweb3.webapp.form.EntidadForm;
import es.caib.regweb3.webapp.form.LibroOrganismo;
import es.caib.regweb3.webapp.form.PermisoLibroUsuarioForm;
import es.caib.regweb3.webapp.form.UsuarioEntidadBusquedaForm;
import es.caib.regweb3.webapp.utils.*;
import es.caib.regweb3.webapp.validator.EntidadValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.RolesInfo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@SessionAttributes(types = EntidadForm.class)
@RequestMapping(value = "/entidad")
public class EntidadController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private EntidadValidator entidadValidator;

    @Autowired
    private UsuarioService usuarioService;

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;

    @EJB(mappedName = "regweb3/SincronizadorDir3EJB/local")
    private SincronizadorDir3Local sincronizadorDIR3Ejb;

    @EJB(mappedName = "regweb3/PendienteEJB/local")
    private PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    private EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/ArchivoEJB/local")
    private ArchivoLocal archivoEjb;

    @EJB(mappedName = "regweb3/UsuarioEJB/local")
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    @EJB(mappedName = "regweb3/ContadorEJB/local")
    private ContadorLocal contadorEjb;

    /**
     * Listado de todas las Entidades
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/entidad/list/1";
    }

    /**
     * Listado de entidades
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable Integer pageNumber)throws Exception {

        ModelAndView mav = new ModelAndView("entidad/entidadList");
        List<Entidad> listado = entidadEjb.getPagination((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION);
        Long total = entidadEjb.getTotal();

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }

    /**
     * Listado de todos los usurios de una Entidad
     */
    @RequestMapping(value = "/usuarios", method = RequestMethod.GET)
    public String listadoUsuariosEntidad(Model model, HttpServletRequest request) {

        Entidad entidad = getEntidadActiva(request);

        UsuarioEntidadBusquedaForm usuarioEntidadBusqueda =  new UsuarioEntidadBusquedaForm(new UsuarioEntidad(),1);

        model.addAttribute("usuarioEntidadBusqueda",usuarioEntidadBusqueda);
        model.addAttribute("entidad",entidad);

        return "entidad/usuariosList";
    }

    /**
     * Listado de usuarios de una Entidad
     * @param  busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/usuarios", method = RequestMethod.POST)
    public ModelAndView usuariosEntidad(@ModelAttribute UsuarioEntidadBusquedaForm busqueda,
        HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("entidad/usuariosList");
        Usuario usuario =  busqueda.getUsuarioEntidad().getUsuario();
        Entidad entidad = getEntidadActiva(request);

        Paginacion paginacion = usuarioEntidadEjb.busqueda(busqueda.getPageNumber(),
            entidad.getId(), usuario.getIdentificador(),usuario.getNombre(),
            usuario.getApellido1(), usuario.getApellido2(), usuario.getDocumento(),
            usuario.getTipoUsuario());

        busqueda.setPageNumber(1);
        mav.addObject("entidad", entidad);
        mav.addObject("paginacion", paginacion);
        mav.addObject("usuarioEntidadBusqueda", busqueda);

        return mav;
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevaEntidad(Model model) throws Exception {

        Entidad entidad =  new Entidad();
        entidad.setOficioRemision(true);

        model.addAttribute(new EntidadForm(entidad));

        return "entidad/entidadForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevaEntidad(@ModelAttribute EntidadForm entidadForm, BindingResult result, Model model,SessionStatus status, HttpServletRequest request) {

        entidadValidator.validate(entidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            return "entidad/entidadForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                Entidad entidad = entidadForm.getEntidad();

                //Guardamos la nueva Entidad y sus propiedades por defecto
                entidadEjb.nuevaEntidad(entidad);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/entidad/list";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/edit", method = RequestMethod.GET)
    public String editarEntidad(@PathVariable("entidadId") Long entidadId, Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        EntidadForm entidadForm = new EntidadForm();
        try {
            Entidad entidad = entidadEjb.findById(entidadId);

            // Comprueba que la Entidad existe
            if(entidad == null){
                log.info("No existe esta entidad");
                Mensaje.saveMessageError(request, getMessage("aviso.entidad.noExiste"));
                return "redirect:/inici";
            }

            entidadForm.setEntidad(entidad);

            // Comprobamos que el usuario puede editar la Entidad solicitada
            //Si el usuario es SUPERADMIN PUEDE EDITAR CUALQUIER ENTIDAD
            Rol rolActivo = getRolActivo(request);
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){

                List<Entidad> entidades = getEntidadesAutenticado(request);
                if(!entidades.contains(entidad)){
                    Mensaje.saveMessageError(request, getMessage("entidad.acceso.denedado"));
                    return "redirect:/inici";
                }

                //Si no es administrador de entidad de la entidad, no la puede editar
                if (!entidad.getId().equals(entidadActiva.getId())) {
                    log.info("Error, editar entidad");
                    Mensaje.saveMessageError(request, getMessage("aviso.entidad.edit"));
                    return "redirect:/inici";
                }

                //Si la entidad está anulada, no se puede editar
                if (!entidad.getActivo()) {
                    log.info("Error, entidad anulada");
                    Mensaje.saveMessageError(request, getMessage("aviso.entidad.anulada"));
                    return "redirect:/inici";
                }
            }

            model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidad.getPropietario(), entidad));
            model.addAttribute("tieneOrganismos", entidadEjb.tieneOrganismos(entidadId));

        } catch(I18NException i18ne) {
          log.error(I18NUtils.getMessage(i18ne), i18ne);
        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(entidadForm);

        return "entidad/entidadForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/edit", method = RequestMethod.POST)
    public String editarEntidad(@ModelAttribute @Valid EntidadForm entidadForm, @PathVariable("entidadId") Long entidadId, BindingResult result,Model model,
                                SessionStatus status, HttpServletRequest request) {

        String destino = "redirect:/entidad/list";

        entidadValidator.validate(entidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

           try {
               model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidadForm.getEntidad().getPropietario(), entidadForm.getEntidad()));
               model.addAttribute("tieneOrganismos", entidadEjb.tieneOrganismos(entidadId));
           } catch(I18NException i18ne) {
             log.error(I18NUtils.getMessage(i18ne), i18ne);
           } catch (Exception e) {
                e.printStackTrace();
            }
           return "entidad/entidadForm";
        }
        // Si no hay errores actualizamos el registro
          ArchivoFormManager afm;

          try {
              Entidad entidad = entidadForm.getEntidad();
              Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());

              // Gestionar el cambio de propietario
              if(!entidadEjb.findById(entidadId).getPropietario().getId().equals(entidad.getPropietario().getId())){
                  log.info("Cambio de propietario: " + entidad.getPropietario().getId());

                  // Si ha cambiado, comprobamos si ya existia
                  UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(entidad.getPropietario().getId(),entidadId);

                  if(usuarioEntidad == null){ // SI no existe, lo creamos
                      log.info("Creamos el nuevo UsuarioEntidad");
                      usuarioEntidad = new UsuarioEntidad();
                      usuarioEntidad.setEntidad(entidad);
                      usuarioEntidad.setUsuario(entidad.getPropietario());

                      usuarioEntidadEjb.persist(usuarioEntidad);
                  }else if(!usuarioEntidad.getActivo()){ //Si existe, pero está inactivo, lo activamos.
                      log.info("Ya existía el UsuarioEntidad, lo activamos");
                      usuarioEntidad.setActivo(true);
                      usuarioEntidadEjb.merge(usuarioEntidad);
                  }


              }else{
                  log.info("Mismo propietario: " + entidad.getPropietario().getId());
              }

              //Modificación con archivo Logo Menú, Logo Pie o imagen sello
                if((entidadForm.getLogoMenu() != null)||(entidadForm.getLogoPie() != null) || entidadForm.getLogoSello()!=null){ 

                    Archivo eliminarLogoMenu = null;
                    Archivo eliminarLogoPie = null;
                    Archivo eliminarImagenSello = null;

                    if(entidadForm.getLogoMenu() != null && !entidadForm.getLogoMenu().isEmpty() ){

                        afm = new ArchivoFormManager(archivoEjb,entidadForm.getLogoMenu(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                        // Redimensionamos el logoMenu, si es necesario (tamaño demasiado grande)
                        byte[] logoRedimensionat = redimensionaLogoMenu(entidadForm.getLogoMenu().getBytes());
                        eliminarLogoMenu = entidadGuardada.getLogoMenu();

                        // Asociamos el nuevo archivo
                        entidad.setLogoMenu(afm.prePersist(logoRedimensionat));
                    }

                    if(entidadForm.getLogoPie() != null && !entidadForm.getLogoPie().isEmpty()){

                        afm = new ArchivoFormManager(archivoEjb,entidadForm.getLogoPie(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                        eliminarLogoPie = entidadGuardada.getLogoPie();

                        // Asociamos el nuevo archivo
                        entidad.setLogoPie(afm.prePersist(null));
                    }

                    if( entidadForm.getLogoSello()!= null && !entidadForm.getLogoSello().isEmpty()){
                    	afm = new ArchivoFormManager(archivoEjb,entidadForm.getLogoSello(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                    	eliminarImagenSello = entidadGuardada.getLogoSello();
                    	
                    	// Asociamos el nuevo archivo
                        entidad.setLogoSello(afm.prePersist(null));
                    }
                    
                    
                    // Si se selecciona el check de borrar Logo Menu , Logo Pie y/o Imagen Sello, se pone a null
                    if(entidadForm.isBorrarLogoMenu()){
                        eliminarLogoMenu = entidadGuardada.getLogoMenu();
                        entidad.setLogoMenu(null);
                    }
                    if(entidadForm.isBorrarLogoPie()){
                        eliminarLogoPie = entidadGuardada.getLogoPie();
                        entidad.setLogoPie(null);
                    }
                    if(entidadForm.isBorrarLogoSello()){
                        eliminarImagenSello = entidadGuardada.getLogoSello();
                        entidad.setLogoSello(null);
                    }

                    entidadEjb.merge(entidad);
                    Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

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
                    //Eliminamos la anterior imagen del sello
                    if( eliminarImagenSello!=null){
                    	FileSystemManager.eliminarArchivo(eliminarImagenSello.getId());
                    	archivoEjb.remove(eliminarImagenSello);
                    }


                }else{

                    Archivo eliminarLogoMenu = null;
                    Archivo eliminarLogoPie = null;
                    Archivo eliminarImagenSello = null;

                    // Si se selecciona el check de borrar Logo Menu y/o Logo Pie, se pone a null
                    if(entidadForm.isBorrarLogoMenu()){
                        eliminarLogoMenu = entidadGuardada.getLogoMenu();
                        entidad.setLogoMenu(null);
                    }
                    if(entidadForm.isBorrarLogoPie()){
                        eliminarLogoPie = entidadGuardada.getLogoPie();
                        entidad.setLogoPie(null);
                    }
                    if( entidadForm.isBorrarLogoSello()){
                    	eliminarImagenSello = entidadGuardada.getLogoSello();
                    	entidad.setLogoSello(null);
                    }

                    if(entidadForm.getEntidad().getLogoMenu() != null){
                        entidad.setLogoMenu(archivoEjb.findById(entidadForm.getEntidad().getLogoMenu().getId()));
                    }

                    entidadEjb.merge(entidadForm.getEntidad());
                    Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

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
                    
                    //Eliminamos el anterior sello
                    if( eliminarImagenSello!=null){
                    	FileSystemManager.eliminarArchivo(eliminarImagenSello.getId());
                    	archivoEjb.remove(eliminarImagenSello);
                    }
                }

                if(isAdminEntidad(request)){ // Si un Administrador de Entidad, la edita.

                    destino = "redirect:/inici";
                    if(getEntidadActiva(request).equals(entidad)){ //Si la entidad modificada en la activa, la actualizamos.
                        setEntidadActiva(entidad,request);
                    }

                }




            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return destino;
        
    }

    /**
     * Anula un {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/anular")
    public String anularEntidad(@PathVariable Long entidadId, HttpServletRequest request) {

        try {

            Entidad entidad = entidadEjb.findById(entidadId);
            entidad.setActivo(false);

            entidadEjb.merge(entidad);

            Mensaje.saveMessageInfo(request, getMessage("entidad.anular.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("entidad.anular.error"));
            e.printStackTrace();
        }

        return "redirect:/entidad/list";
    }

    /**
     * Activar un {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/activar")
    public String activarEntidad(@PathVariable Long entidadId, HttpServletRequest request) {

        try {

            Entidad entidad = entidadEjb.findById(entidadId);
            entidad.setActivo(true);

            entidadEjb.merge(entidad);

            Mensaje.saveMessageInfo(request, getMessage("entidad.activar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("entidad.activar.error"));
            e.printStackTrace();
        }

        return "redirect:/entidad/list";
    }

    /**
    * Actualizamos una {@link es.caib.regweb3.model.Entidad} de dir3caib
    */
    @RequestMapping(value = "/{entidadId}/actualizar")
    @ResponseBody
    public /*Boolean*/ JsonResponse actualizar(@PathVariable Long entidadId, HttpServletRequest request) {
        JsonResponse jsonResponse = new JsonResponse();
        try{

          Descarga ultimaDescarga = descargaEjb.findByTipoEntidad(RegwebConstantes.UNIDAD,entidadId);
          Timestamp fechaUltimaActualizacion = null;
          if (ultimaDescarga.getFechaImportacion() != null) {
            fechaUltimaActualizacion = new Timestamp(ultimaDescarga.getFechaImportacion().getTime());
          }

          // Establecemos la fecha de la primera sincronizacion
          Descarga primeraDescarga = descargaEjb.findByTipoEntidadInverse(RegwebConstantes.UNIDAD, entidadId);
          Timestamp fechaSincronizacion = null;
          if (primeraDescarga.getFechaImportacion() != null) {
            fechaSincronizacion = new Timestamp(primeraDescarga.getFechaImportacion().getTime());
          }


           int actualizados= sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, fechaUltimaActualizacion, fechaSincronizacion);
           if(actualizados == -1){
               log.info("No se puede actualizar regweb hasta que no se haya actualizado dir3caib préviamente");
              // Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nopermitido"));
               //return false;
               jsonResponse.setStatus("NOTALLOWED");
               return jsonResponse;
           }
            // actualizamos nombre y codigo de la entidad, si la unidad raiz a la que representa se ha extinguido.
            actualizarEntidadExtincionUnidadRaiz(entidadId, request);
            // via ajax s'en va a "entidad/pendientesprocesar"
       }catch(Exception e){
           log.error("Error actualizacion", e);
           Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nook"));
           jsonResponse.setStatus("FAIL");
            return jsonResponse;
       }

        jsonResponse.setStatus("SUCCESS");
        return jsonResponse;
    }

    /**
    * Sincronizamos una {@link es.caib.regweb3.model.Entidad} de dir3caib
    */
    @RequestMapping(value = "/{entidadId}/sincronizar")
    public String sincronizar(@PathVariable Long entidadId, Model model,  HttpServletRequest request) {

          try{
            int sincronizados = sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, null, null);
            if(sincronizados == -1){
                  log.info("No se puede sincronizar regweb hasta que no se haya actualizado dir3caib préviamente");
                  Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nopermitido"));
            }
            Mensaje.saveMessageInfo(request, getMessage("regweb.sincronizados.numero") + sincronizados);
          }catch(Exception e){
             log.error("Error sincro", e);
             Mensaje.saveMessageError(request, getMessage("regweb.sincronizacion.nook"));
          }
          return  "redirect:/organismo/list";
    }


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/permisos/{idUsuarioEntidad}", method = RequestMethod.GET)
    public String asignarUsuario(@PathVariable Long idUsuarioEntidad, Model model,
        HttpServletRequest request) throws Exception, I18NException {

        Entidad entidad = getEntidadActiva(request);

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);
        List<Libro> libros = libroEjb.getLibrosEntidad(entidad.getId());

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(usuarioEntidad.getUsuario().getIdentificador());
        
        List<String> roles = new ArrayList<String>();
        Collections.addAll(roles, rolesInfo.getRoles());

        if (libros.size() == 0) {
            Mensaje.saveMessageError(request, getMessage("permisos.libro.ninguno"));
            return "redirect:/entidad/usuarios";
        }

        if (roles.contains("RWE_USUARI") || !usuarioEntidad.getActivo()) {

            PermisoLibroUsuarioForm permisoLibroUsuarioForm = new PermisoLibroUsuarioForm();
            permisoLibroUsuarioForm.setUsuarioEntidad(usuarioEntidad);

            permisoLibroUsuarioForm.setPermisoLibroUsuarios(permisoLibroUsuarioEjb.findByUsuarioLibros(usuarioEntidad.getId(), libros));

            model.addAttribute(permisoLibroUsuarioForm);
            model.addAttribute("entidad", entidad);
            model.addAttribute("libros", libros);
            model.addAttribute("permisos", RegwebConstantes.PERMISOS);

            return "usuario/permisoLibroUsuarioForm";
        }else{
            Mensaje.saveMessageError(request, getMessage("usuario.asignar.permisos.denegado"));
            return "redirect:/entidad/usuarios";
        }
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/permisos/{idUsuarioEntidad}", method = RequestMethod.POST)
    public String asignarUsuario(@ModelAttribute PermisoLibroUsuarioForm permisoLibroUsuarioForm,
                                 @PathVariable Integer idUsuarioEntidad, SessionStatus status, HttpServletRequest request) {

        try {

            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(Long.valueOf(idUsuarioEntidad));

            for (PermisoLibroUsuario plu : permisoLibroUsuarioForm.getPermisoLibroUsuarios()) {
                plu.setUsuario(usuarioEntidad);

                // Si ya existe el Permiso, actualiza el valor de ctivo. Si no existe, crea el Permiso en BBDD
                if(plu.getId()!=null) {
                    permisoLibroUsuarioEjb.actualizarPermiso(plu.getId(), plu.getActivo());
                }else{
                    permisoLibroUsuarioEjb.merge(plu);
                }

            }

            Mensaje.saveMessageInfo(request, getMessage("usuario.asignar.permisos.ok"));

        }catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("organismo.usuario.asignar.error"));
            e.printStackTrace();
        }

        status.setComplete();
        return "redirect:/entidad/usuarios";
    }

    /**
     * Eliminar la asignación de un Usuario a una Entidad
     */
    @RequestMapping(value = "/permisos/{idUsuarioEntidad}/delete", method = RequestMethod.GET)
    public String eliminarAsignacion(@PathVariable Long idUsuarioEntidad, HttpServletRequest request) {

        try {
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);

            if (entidadEjb.esAdministrador(usuarioEntidad.getEntidad().getId(), usuarioEntidad)) {
                Mensaje.saveMessageError(request, getMessage("usuarioEntidad.administrador"));
                return "redirect:/entidad/usuarios";
            }

            // Eliminamos todos sus PermisoLibroUsuario
            permisoLibroUsuarioEjb.eliminarByUsuario(idUsuarioEntidad);

            // Comprobar si el usuario tiene Registros en la Entidad
            if (usuarioEntidadEjb.puedoEliminarlo(idUsuarioEntidad)) {
                // Si no tiene registros relacinados, lo eliminamos definitivamente.
                usuarioEntidadEjb.remove(usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("usuario.eliminado"));
            } else {
                // Desactivamos este usuario de la Entidad
                usuarioEntidad.setActivo(false);
                usuarioEntidadEjb.merge(usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("usuario.desactivado"));
            }

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "No s'ha eliminat el registre perque està relacionat amb un altra entitat.");
            e.printStackTrace();
        }

        return "redirect:/entidad/usuarios";
    }

    /**
     * Reiniciar Contadoresde una Entidad
     */
    @RequestMapping(value = "/{idEntidad}/reiniciarContadores")
    public String reiniciarContadoresEntidad(@PathVariable Long idEntidad, HttpServletRequest request) {

        try {

            // Reinicia los contadores de los Libros de la Entidad
            libroEjb.reiniciarContadoresEntidad(idEntidad);

            // Reinicia el contador SIR de la Entidad
            Entidad entidad = entidadEjb.findById(idEntidad);
            contadorEjb.reiniciarContador(entidad.getContadorSir().getId());

            Mensaje.saveMessageInfo(request, getMessage("aviso.contadores.reiniciar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("error.contadores.reiniciar"));
            e.printStackTrace();
        }

        return "redirect:/entidad/list";
    }

    /**
     * Eliminar la Entidad
     */
    @RequestMapping(value = "/{idEntidad}/eliminar", method = RequestMethod.GET)
    public String eliminarEntidad(@PathVariable Long idEntidad, HttpServletRequest request) {

        try {

            entidadEjb.eliminarEntidad(idEntidad);
            entidadEjb.remove(entidadEjb.findById(idEntidad));

            Mensaje.saveMessageInfo(request, getMessage("aviso.entidad.baja"));

        } catch ( I18NException i18ne) {
          Mensaje.saveMessageError(request, getMessage("error.entidad.relacion"));
          log.error(I18NUtils.getMessage(i18ne), i18ne);
        } catch (Exception e) {
          Mensaje.saveMessageError(request, getMessage("error.entidad.relacion"));
          e.printStackTrace();
        }

        return "redirect:/entidad/list";
    }

    /**
     * Eliminar todos los Registros de una Entidad
     */
    @RequestMapping(value = "/{idEntidad}/eliminarRegistros")
    public String eliminarRegistrosEntidad(@PathVariable Long idEntidad, HttpServletRequest request) {

        try {

            entidadEjb.eliminarRegistros(idEntidad);

            Mensaje.saveMessageInfo(request, getMessage("aviso.registros.eliminados"));
        } catch(I18NException i18ne) {
          Mensaje.saveMessageError(request, getMessage("error.registros.eliminar"));
          log.error(I18NUtils.getMessage(i18ne), i18ne);
        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("error.registros.eliminar"));
            e.printStackTrace();
        }

        return "redirect:/entidad/list";
    }



  /**
   * Función que gestiona los organismos extinguidos. Procesa los que son automáticos(1 historico) y
   * prepara los datos para los que no son automáticos y los envía al jsp para procesar manualmente
   * Esta función se llama justo después del proceso de sincronización de una entidad desde dir3.
   * En el InicioInterceptor mira si hay organismos pendientes de procesar y si hay viene aquí.
   */
    @RequestMapping(value = "/pendientesprocesar", method = RequestMethod.GET)
    public String mostrarPendientesProcesar(HttpServletRequest request, Model model) throws Exception {
        //log.info("Entro en pendiente de procesar " + new Date() );
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

        /* Preparamos todos los organismos a procesar (extinguidos, anulados, transitorios,vigentes)*/
        List<Pendiente> pendientesDeProcesar = pendienteEjb.findPendientesProcesar();
        //log.info("Pendientes procesar " + pendientesDeProcesar.size());
        if(!pendientesDeProcesar.isEmpty()){
          List<Organismo> organismosExtinguidos = new ArrayList<Organismo>();// Organismos extinguidos a procesar por usuario
          // Para mostrar información al usuario
          Map<String,Organismo> extinguidosAutomaticos= new HashMap<String, Organismo>();// Organismos extinguidos y sustitutos procesados automaticamente
          List<Organismo> organismosConError= new ArrayList<Organismo>();// Organismos con error pendientes de solucionar (por ejemplo sin oficinas)
          for(Pendiente pendiente :pendientesDeProcesar){
            if(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_VIGENTE.equals(pendiente.getEstado())){
                // Obtenemos el organismo extinguido
                Organismo organismoExtinguido = organismoEjb.findById(pendiente.getIdOrganismo());
                //Obtenemos libros de organismo extinguido
                List<Libro> libros = organismoExtinguido.getLibros();
                //Si solo tiene un organismo que le sustituye se asignan los libros a ese organismo

                Set<Organismo> historicosUOconOficinas= new HashSet<Organismo>();
                //Obtenemos los historicos hasta el final para que el usuario decida donde poner el libro.
                Set<Organismo> historicosFinales= new HashSet<Organismo>();
                organismoEjb.obtenerHistoricosFinales(organismoExtinguido.getId(), historicosFinales);
                log.info("HISTORICOS FINALES "+ historicosFinales.size());
                //Además de estos históricos finales sólo interesan los que tienen oficinas, ya que para asignarle los libros debe tener oficinas
                for(Organismo orgHistorico:historicosFinales){
                    if (oficinaEjb.tieneOficinasServicio(orgHistorico.getId(), RegwebConstantes.OFICINA_VIRTUAL_SI)) {
                        historicosUOconOficinas.add(orgHistorico);
                    }
                }
                //Reasignamos los historicosConOficina pero no se guardan en BD
                organismoExtinguido.setHistoricoUO(historicosUOconOficinas);
                //Si no tiene historicos  y tiene libros por ubicar, lo marcamos como error
                if(historicosUOconOficinas.size()==0){
                    if(libros.size()>0) {
                        organismosConError.add(organismoExtinguido);
                    }
                } else { //Si tiene históricos
                    if (historicosUOconOficinas.size() == 1) {// Se procesa internamente de manera autómatica porque solo hay 1 histórico
                        log.info("Se procesa automaticamente porque solo tiene 1 historico" + organismoExtinguido.getDenominacion() + ":" + organismoExtinguido.getCodigo());
                        if (libros.size() > 0) {
                            log.info("El organismo " + organismoExtinguido.getDenominacion() + " con 1 histórico tiene libros");
                            // Asignamos libros misma numeración
                            Organismo orgSustituye = new ArrayList<Organismo>(organismoExtinguido.getHistoricoUO()).get(0);

                            //Asignamos el libro al organismo que lo sustituye
                            for (Libro libro : libros) {
                                libro.setOrganismo(orgSustituye);
                                libroEjb.merge(libro);
                            }
                            // asignamos los libros para mostrarlos en el jsp
                            orgSustituye.setLibros(libros);
                            log.info("Libros del organismo: " + organismoExtinguido.getDenominacion() + ":" + organismoExtinguido.getCodigo() + "han sido reasigandos al organismo:  " + orgSustituye.getDenominacion() + ":" + orgSustituye.getCodigo());
                            // Añadimos todos los organimos procesados automáticamente
                            extinguidosAutomaticos.put(organismoExtinguido.getDenominacion(), orgSustituye);

                        }
                        //actualizar pendiente
                        pendiente.setProcesado(true);
                        pendiente.setFecha(TimeUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                        pendienteEjb.merge(pendiente);
                        log.info("MAP de extinguidos automaticos " + extinguidosAutomaticos.get(organismoExtinguido.getDenominacion()));
                    } else { // tiene más de un historico
                        log.info("Entramos en historicos +1 de extinguido(no se procesan automaticamente): " + organismoExtinguido.getDenominacion() + ":" + organismoExtinguido.getCodigo());
                        if (libros.size() > 0) {//Si tiene libros se añade para procesarlo
                            log.info("Tiene libros el organismo:" + organismoExtinguido.getDenominacion() + ":" + organismoExtinguido.getCodigo());
                            organismosExtinguidos.add(organismoExtinguido);
                        } else {// no tiene libros, no se hace nada pero se actualiza el estado a procesado
                            pendiente.setProcesado(true);
                            pendiente.setFecha(TimeUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                            pendienteEjb.merge(pendiente);
                        }

                    }
                }
            }else{  // ANULADOS
              // TODO ANULADOS
            }
          }
          // extinguidosAutomaticos --> organismos que se les ha asignado automaticamente los libros.
          // organismosExtinguidos --> organismos que estan pendientes de procesar por el usuario que será el que decida
          // donde colocar finalmente los libros.
          if(extinguidosAutomaticos.size()>0 || organismosExtinguidos.size()>0 ||organismosConError.size()>0) {
              model.addAttribute("extinguidosAutomaticos", extinguidosAutomaticos);
              model.addAttribute("organismosAProcesar", organismosExtinguidos);
              model.addAttribute("organismosConError", organismosConError);
              if(organismosConError.size()>0){
                  Entidad entidad=getEntidadActiva(request);
                  List<Organismo> organismosEntidadVigentes = organismoEjb.organismosConOficinas(entidad.getId());
                  model.addAttribute("organismosSustituyentes", organismosEntidadVigentes);
              }
          }else{
              log.info("no hay organismos a procesar ");
              Mensaje.saveMessageInfo(request, getMessage("organismo.nopendientesprocesar"));
              return "redirect:/organismo/list";
          }
          Mensaje.saveMessageInfo(request, getMessage("organismo.nopendientesprocesar"));
          log.info("Extinguidos automaticos: " + extinguidosAutomaticos.size());
          log.info("organismosAProcesar: " + organismosExtinguidos.size());
          log.info("organismosConError: " + organismosConError.size());
          //con esPendiente indicamos que venimos de una sincro/actualizacion y hay que mostrar el resumen de los autómaticos.
          model.addAttribute("esPendiente", true);
        }else {
            log.debug("else no pendientes de procesar");
            Mensaje.saveMessageInfo(request, getMessage("organismo.nopendientesprocesar"));
            return "redirect:/organismo/list";
        }
        model.addAttribute("tituloPagina",getMessage("organismos.resultado.actualizacion"));
        return "organismo/organismosACambiarLibro";
    }


     /**
      * Procesa un organismo con libros  y monta la respuesta en json
      * para después mostrar los resultados
      * Realmente lo que hace es asignar el libro al nuevo organismo indicado.
      */
     @RequestMapping(value="/procesarlibroorganismo/{organismoId}/{esPendiente}", method= RequestMethod.POST)
     @ResponseBody
     public JsonResponse procesarLibroOrganismo(@RequestBody List<LibroOrganismo> libroOrganismos, @PathVariable("organismoId") Long organismoId, @PathVariable("esPendiente") boolean esPendiente, BindingResult result, HttpServletRequest request) {
         JsonResponse jsonResponse = new JsonResponse();
         try {
            log.info("PROCESANDO "+ organismoId);
            List<LibroOrganismo> nombresLibrosOrganismos= new ArrayList<LibroOrganismo>();  // Contendrá la lista de nombres de libro y organismo

            // Comenzamos a procesar los libros organismos que recibimos del request.
            for(LibroOrganismo libroOrganismo:libroOrganismos){

              log.info("Libro: " + libroOrganismo.getLibro());
              log.info("Organismo: " + libroOrganismo.getOrganismo());

                //Obtenemos libro, el organismo que lo sustituye
              Libro libro = libroEjb.findById(new Long(libroOrganismo.getLibro()));
              if(!libroOrganismo.getOrganismo().equals( "-1")) {
                  Organismo organismoSustituye = organismoEjb.findById(new Long(libroOrganismo.getOrganismo()));

                  libro.setOrganismo(organismoSustituye);
                  libroEjb.merge(libro);

                  //asignamos los nombres del libro y del organismo que lo sustituye, para ello
                  // emplearemos la clase LibroOrganismo para mostrarlos una vez procesado
                  LibroOrganismo nombreLibroOrganismo = new LibroOrganismo();
                  nombreLibroOrganismo.setLibro(libro.getNombre());
                  nombreLibroOrganismo.setOrganismo(organismoSustituye.getDenominacion());

                  //lo añadimos a la lista
                  nombresLibrosOrganismos.add(nombreLibroOrganismo);
              }
            }
            if(esPendiente) {
                Pendiente pendiente = pendienteEjb.findByIdOrganismo(organismoId);
                pendiente.setProcesado(true);
                pendiente.setFecha(TimeUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                pendienteEjb.merge(pendiente);
            }

            // Necesitamos su nombre
            Organismo extinguido= organismoEjb.findById(organismoId);

            // MONTAMOS LA RESPUESTA JSON
            jsonResponse.setStatus("SUCCESS");

            OrganismoJson organismoJson = new OrganismoJson();
            organismoJson.setId(organismoId.toString());
            organismoJson.setNombre(extinguido.getDenominacion());
            organismoJson.setLibroOrganismos(nombresLibrosOrganismos);

            jsonResponse.setResult(organismoJson);
         }catch (Exception e){
            jsonResponse.setStatus("FAIL");
            e.printStackTrace();
         }
         return jsonResponse;
     }

    @RequestMapping(value = "/librosCambiar", method = RequestMethod.GET)
    public String librosCambiar(HttpServletRequest request, Model model) throws Exception {


        Entidad entidad = getEntidadActiva(request);

        List<Organismo> organismosEntidad = organismoEjb.findByEntidadLibros(entidad.getId());
        log.info("Organismos Entidad " + organismosEntidad.size());


        List<Organismo> organismosEntidadVigentes = organismoEjb.organismosConOficinas(entidad.getId());
        log.info("Organismos entidad con Oficinas " + organismosEntidadVigentes.size());
        if(organismosEntidad.size()>0) {

            // Inicializamos sus Historicos, ya la relación está a FetchType.LAZY
            for (Organismo organismo : organismosEntidad) {
                Hibernate.initialize(organismo.getHistoricoUO());
            }

            model.addAttribute("organismosAProcesar", organismosEntidad);
            model.addAttribute("organismosSustituyentes", organismosEntidadVigentes);
            model.addAttribute("esPendiente", false);
        }
        model.addAttribute("tituloPagina", getMessage("entidad.cambiarlibros"));
        model.addAttribute("tieneLibros", libroEjb.tieneLibrosEntidad(entidad.getId()));
        return "/organismo/organismosACambiarLibro";
    }

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_UI;
    }

    @ModelAttribute("configuraciones")
    public long[] configuraciones() throws Exception {
        return RegwebConstantes.CONFIGURACIONES_PERSONA;
    }

    @ModelAttribute("propietarios")
    public List<Usuario> propietarios() throws Exception, I18NException {

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
        String[] usuarios = loginPlugin.getUsernamesByRol(RegwebConstantes.ROL_ADMIN);

        List<Usuario> administradoresEntidad = new ArrayList<Usuario>();

        for (String identificador : usuarios) {            
            Usuario usuario = usuarioEjb.findByIdentificador(identificador);
            if(usuario!= null) {
              administradoresEntidad.add(usuario);
            }
        }
        return administradoresEntidad;
    }


    /**
     * Retorna los posibles Administradores al modificar una Entidad
     * @param propietario
     * @param entidad
     * @return
     * @throws Exception
     */
    private List<UsuarioEntidad> administradoresEntidadModificar(Usuario propietario,
        Entidad entidad) throws Exception, I18NException {

        // Antes de nada, actualizamos los Roles contra Seycon de los UsuarioEntidad
        List<UsuarioEntidad> usuarios = usuarioEntidadEjb.findActivosByEntidad(entidad.getId());
        for (UsuarioEntidad usuario : usuarios) {
            usuarioService.actualizarRoles(usuario.getUsuario());
        }

        // Obtenemos todos los UsuarioEntidad con Rol RWE_ADMIN
        List<UsuarioEntidad> administradoresEntidad = usuarioEntidadEjb.findAdministradoresByEntidad(entidad.getId());
        /*for (UsuarioEntidad usuario : administradoresEntidad) {
            usuarioService.actualizarRoles(usuario.getUsuario());
        }*/

        // Eliminamos el Propietario de la Entidad
        UsuarioEntidad usuarioPropietario = usuarioEntidadEjb.findByUsuarioEntidad(propietario.getId(),entidad.getId());
        if(administradoresEntidad.contains(usuarioPropietario)){
            administradoresEntidad.remove(usuarioPropietario);
        }

        return administradoresEntidad;
    }

    /**
     * Listado de todas las Descargas
     */
    @RequestMapping(value = "/descargas/list", method = RequestMethod.GET)
    public String descargaEntidadList() {
        return "redirect:/entidad/descargas/list/1";
    }

    @RequestMapping(value = "/descargas/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView descargaEntidadList(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("/entidad/descargasList");
        Entidad entidad = getEntidadActiva(request);

        List<Descarga> listado = descargaEjb.getPaginationByEntidad((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = descargaEjb.getTotalByEntidad(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        mav.addObject("entidad", entidad);


        return mav;
    }


    @InitBinder("entidadForm")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

        binder.registerCustomEditor(UsuarioEntidad.class, "entidad.administradores",new UsuarioEntidadEditor());
        binder.setValidator(this.entidadValidator);
    }



    private byte[] redimensionaLogoMenu(byte[] logoMenu) throws IOException {

        // Obtenemos la imagen del Logo
        ByteArrayInputStream in = new ByteArrayInputStream(logoMenu);
        BufferedImage imatgeLogo = ImageIO.read(in);

        int ampladaOriginal = imatgeLogo.getWidth();
        int alsadaOriginal = imatgeLogo.getHeight();

        // Si no passa cap dels paràmetres màxims, no fa res
        if(ampladaOriginal > RegwebConstantes.LOGOMENU_AMPLADA_MAX || alsadaOriginal > RegwebConstantes.LOGOMENU_ALSADA_MAX) {

            double scale;
            double scaleHeight = 1.0;
            double scaleWidth = 1.0;
            // Si pasa alguno de los parámetros, calcula el valor de la escala
            if (ampladaOriginal > RegwebConstantes.LOGOMENU_AMPLADA_MAX) {
                scaleWidth = (double) RegwebConstantes.LOGOMENU_AMPLADA_MAX / ampladaOriginal;
            }
            if (alsadaOriginal > RegwebConstantes.LOGOMENU_ALSADA_MAX) {
                scaleHeight = (double) RegwebConstantes.LOGOMENU_ALSADA_MAX / alsadaOriginal;
            }
            // Elige la escala que debe tomar, la más pequeña (el tamaño que más se pasa del límite)
            if (scaleHeight <= scaleWidth) {
                scale = scaleHeight;
            } else {
                scale = scaleWidth;
            }
            // Escala la anchura y el altura
            int nuevoImageWidth = (int) (ampladaOriginal * scale);
            int nuevoImageHeight = (int) (alsadaOriginal * scale);

            Image logoMenuModificat = imatgeLogo.getScaledInstance(nuevoImageWidth, nuevoImageHeight, Image.SCALE_SMOOTH);
            BufferedImage logoMenuModBuff = new BufferedImage(nuevoImageWidth, nuevoImageHeight, BufferedImage.TYPE_INT_RGB);
            logoMenuModBuff.getGraphics().drawImage(logoMenuModificat, 0, 0, null, null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(logoMenuModBuff, "jpg", buffer);

            return buffer.toByteArray();

        }else{ // Retorna la imagen original
            return logoMenu;
        }
    }

    /**
     * Método que actualiza el nombre y el código dir3 de la entidad, debido a que la unidad raiz a la que representa se ha extinguido en dir3caib.
     *
     * @param entidadId
     * @param request
     * @throws Exception
     */
    private void actualizarEntidadExtincionUnidadRaiz(Long entidadId, HttpServletRequest request) throws Exception {
        Entidad entidad = entidadEjb.findById(entidadId);
        Organismo organismoRaizEntidad = organismoEjb.findByCodigoEntidadSinEstado(entidad.getCodigoDir3(), entidadId);
        Set<Organismo> historicosFinales = new HashSet<Organismo>();
        organismoEjb.obtenerHistoricosFinales(organismoRaizEntidad.getId(), historicosFinales);
        if (historicosFinales.size() > 0) {
            if (historicosFinales.size() == 1) {
                entidad.setCodigoDir3((historicosFinales.iterator().next()).getCodigo());
                entidad.setNombre((historicosFinales.iterator().next()).getDenominacion());
                entidadEjb.merge(entidad);
                usuarioService.cambioEntidad(entidad, request);
            } else {
                throw new Exception("La raiz se ha dividido en más de un organismo, houston tenemos un problema");
            }

        }
    }


}
