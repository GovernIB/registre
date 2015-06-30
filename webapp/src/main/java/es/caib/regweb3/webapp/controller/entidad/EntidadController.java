package es.caib.regweb3.webapp.controller.entidad;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.editor.UsuarioEntidadEditor;
import es.caib.regweb3.webapp.form.EntidadForm;
import es.caib.regweb3.webapp.form.LibroOrganismo;
import es.caib.regweb3.webapp.form.PermisoLibroUsuarioForm;
import es.caib.regweb3.webapp.form.UsuarioEntidadBusquedaForm;
import es.caib.regweb3.webapp.login.RegwebLoginPluginManager;
import es.caib.regweb3.webapp.scan.ScannerManager;
import es.caib.regweb3.webapp.utils.*;
import es.caib.regweb3.webapp.validator.EntidadValidator;
import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.RolesInfo;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.*;

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
    public DescargaLocal descargaEjb;

    @EJB(mappedName = "regweb3/SincronizadorDir3EJB/local")
    private SincronizadorDir3Local sincronizadorDIR3Ejb;

    @EJB(mappedName = "regweb3/PendienteEJB/local")
    public PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/ArchivoEJB/local")
    public ArchivoLocal archivoEjb;

    @EJB(mappedName = "regweb3/UsuarioEJB/local")
    public UsuarioLocal usuarioEjb;

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

        EntidadForm entidadForm = new EntidadForm();

        model.addAttribute(entidadForm);

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
                //Guardamos la nueva Entidad
                entidadEjb.persist(entidad);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
                status.setComplete();
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

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

            model.addAttribute("tipoScan", ScannerManager.getTipusScanejat(request.getLocale(), getMessage("scan.noScan")));
            model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidad.getPropietario(), entidad));

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
    public String editarEntidad(@ModelAttribute @Valid EntidadForm entidadForm,BindingResult result,Model model,
                                SessionStatus status, HttpServletRequest request) {

        String destino = "redirect:/entidad/list";

        entidadValidator.validate(entidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

           try {
                model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidadForm.getEntidad().getPropietario(), entidadForm.getEntidad()));
                model.addAttribute("tipoScan", ScannerManager.getTipusScanejat(request.getLocale(), getMessage("scan.noScan")));
            } catch (Exception e) {
                e.printStackTrace();
            }
           return "entidad/entidadForm";
        }
        // Si no hay errores actualizamos el registro
          ArchivoFormManager afm = null;

          try {
                Entidad entidad = entidadForm.getEntidad();

              //Modificación con archivo Logo Menú, Logo Pie o imagen sello
                if((entidadForm.getLogoMenu() != null)||(entidadForm.getLogoPie() != null) || entidadForm.getLogoSello()!=null){ 

                    Archivo eliminarLogoMenu = null;
                    Archivo eliminarLogoPie = null;
                    Archivo eliminarImagenSello = null;

                    if(entidadForm.getLogoMenu() != null && !entidadForm.getLogoMenu().isEmpty() ){

                        afm = new ArchivoFormManager(archivoEjb,entidadForm.getLogoMenu(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                        Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
                        eliminarLogoMenu = entidadGuardada.getLogoMenu();

                        // Asociamos el nuevo archivo
                        entidad.setLogoMenu(afm.prePersist());
                    }

                    if(entidadForm.getLogoPie() != null && !entidadForm.getLogoPie().isEmpty()){

                        afm = new ArchivoFormManager(archivoEjb,entidadForm.getLogoPie(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                        Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
                        eliminarLogoPie = entidadGuardada.getLogoPie();

                        // Asociamos el nuevo archivo
                        entidad.setLogoPie(afm.prePersist());
                    }

                    if( entidadForm.getLogoSello()!= null && !entidadForm.getLogoSello().isEmpty()){
                    	afm = new ArchivoFormManager(archivoEjb,entidadForm.getLogoSello(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                    	Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
                    	eliminarImagenSello = entidadGuardada.getLogoSello();
                    	
                    	// Asociamos el nuevo archivo
                        entidad.setLogoSello(afm.prePersist());
                    }
                    
                    
                    // Si se selecciona el check de borrar Logo Menu , Logo Pie y/o Imagen Sello, se pone a null
                    if(entidadForm.isBorrarLogoMenu()){
                        Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
                        eliminarLogoMenu = entidadGuardada.getLogoMenu();
                        entidad.setLogoMenu(null);
                    }
                    if(entidadForm.isBorrarLogoPie()){
                        Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
                        eliminarLogoPie = entidadGuardada.getLogoPie();
                        entidad.setLogoPie(null);
                    }
                    if(entidadForm.isBorrarLogoSello()){
                    	Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
                        eliminarImagenSello = entidadGuardada.getLogoSello();
                        entidad.setLogoSello(null);
                    }

                    entidadEjb.merge(entidad);
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
                    //Eliminamos la anterior imagen del sello
                    if( eliminarImagenSello!=null){
                    	FileSystemManager.eliminarArchivo(eliminarImagenSello.getId());
                    	archivoEjb.remove(eliminarImagenSello);
                    }


                }else{

                    Archivo eliminarLogoMenu = null;
                    Archivo eliminarLogoPie = null;
                    Archivo eliminarImagenSello = null;
                    Entidad entidadGuardada = entidadEjb.findById(entidadForm.getEntidad().getId());
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
    public void actualizar(@PathVariable Long entidadId, Model model, HttpServletRequest request) {

       try{
          Descarga ultimaDescarga = descargaEjb.findByTipoEntidad(RegwebConstantes.UNIDAD,entidadId);
          Timestamp fechaUltimaActualizacion = null;
          if (ultimaDescarga.getFechaImportacion() != null) {
            fechaUltimaActualizacion = new Timestamp(ultimaDescarga.getFechaImportacion().getTime());
          }

          // Determinamos si es actualizacion o sincronizacion
          if(ultimaDescarga != null){
            model.addAttribute("descarga", ultimaDescarga);
          }
          // Establecemos la fecha de la primera sincronizacion
          Descarga primeraDescarga = descargaEjb.findByTipoEntidadInverse(RegwebConstantes.UNIDAD, entidadId);
          Timestamp fechaSincronizacion = null;
          if (primeraDescarga.getFechaImportacion() != null) {
            fechaSincronizacion = new Timestamp(primeraDescarga.getFechaImportacion().getTime());
          }


          int actualizados = sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, fechaUltimaActualizacion, fechaSincronizacion);
          Mensaje.saveMessageInfo(request, getMessage("regweb.sincronizados.numero") + actualizados);

       }catch(Exception e){
           log.error("Error actualizacion", e);
           Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nook"));
       }

    }

    /**
    * Sincronizamos una {@link es.caib.regweb3.model.Entidad} de dir3caib
    */
    @RequestMapping(value = "/{entidadId}/sincronizar")
    public String sincronizar(@PathVariable Long entidadId, Model model,  HttpServletRequest request) {

          try{
            int sincronizados = sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, null, null);
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
    public String asignarUsuario(@PathVariable Long idUsuarioEntidad, Model model,HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);

        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(usuarioEntidad.getUsuario().getIdentificador());
        
        List<String> roles = new ArrayList<String>();
        Collections.addAll(roles, rolesInfo.getRoles());
        

        if(roles.contains("RWE_USUARI") || roles.contains("RWE_LOPD") || !usuarioEntidad.getActivo()){

            List<Libro> libros = libroEjb.getLibrosEntidad(entidad.getId());

            PermisoLibroUsuarioForm permisoLibroUsuarioForm = new PermisoLibroUsuarioForm();
            permisoLibroUsuarioForm.setUsuarioEntidad(usuarioEntidad);

            permisoLibroUsuarioForm.setPermisoLibroUsuarios(permisoLibroUsuarioEjb.findByUsuario(usuarioEntidad.getId()));

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
            status.setComplete();
        }catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("organismo.usuario.asignar.error"));
            e.printStackTrace();
        }

        return "redirect:/entidad/usuarios";
    }

    /**
     * Eliminar la asignación de un Usuario a una Entidad
     */
    @RequestMapping(value = "/permisos/{idUsuarioEntidad}/delete")
    public String eliminarAsignacion(@PathVariable Long idUsuarioEntidad, HttpServletRequest request) {

        try {

            // Desactivamos este usuario de la Entidad
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);
            usuarioEntidad.setActivo(false);

            usuarioEntidadEjb.merge(usuarioEntidad);

            //Eliminamos todos sus PermisoLibroUsuario
            List<PermisoLibroUsuario> permisos = permisoLibroUsuarioEjb.findByUsuario(idUsuarioEntidad);

            for (PermisoLibroUsuario permiso : permisos) {
                permisoLibroUsuarioEjb.remove(permiso);
            }

            Mensaje.saveMessageInfo(request, "S'ha eliminat l'usuari de l'entitat i els seus permisos");

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "No s'ha eliminat el registre perque està relacionat amb un altra entitat.");
            e.printStackTrace();
        }

        return "redirect:/entidad/usuarios";
    }

    /**
     * Eliminar la asignación de un Usuario a un Organismo
     */
    @RequestMapping(value = "/{idEntidad}/eliminar")
    public String eliminarEntidad(@PathVariable Long idEntidad, HttpServletRequest request) {
    log.info("idEntidad: " + idEntidad);
        try {

            entidadEjb.eliminarEntidad(idEntidad);

            Mensaje.saveMessageInfo(request, "S'ha eliminat l'entitat");

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "No s'ha eliminat el registre perque està relacionat amb un altra entitat.");
            e.printStackTrace();
        }

        return "redirect:/entidad/list";
    }

    /**
     * Eliminar todos los Registros de una Entidad
     */
    @RequestMapping(value = "/{idEntidad}/eliminarRegistros")
    public String eliminarRegistrosEntidad(@PathVariable Long idEntidad, HttpServletRequest request) {
        log.info("idEntidad: " + idEntidad);
        try {

            entidadEjb.eliminarRegistros(idEntidad);

            Mensaje.saveMessageInfo(request, "S'han eliminat els registres de  l'entitat");

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "Error: No s'ha eliminat els registres");
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

        /* Preparamos todos los organismos a procesar (extinguidos, anulados, transitorios*/
        List<Pendiente> pendientesDeProcesar = pendienteEjb.findPendientesProcesar();
        //log.info("Pendientes procesar " + pendientesDeProcesar.size());
        if(!pendientesDeProcesar.isEmpty()){
          List<Organismo> organismosExtinguidos = new ArrayList<Organismo>();// Organismos extinguidos a procesar por usuario
          // Para mostrar información al usuario
          Map<String,Organismo> extinguidosAutomaticos= new HashMap<String, Organismo>();   // Organismos extinguidos y sustitutos procesados automaticamente
          for(Pendiente pendiente :pendientesDeProcesar){
            if(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO.equals(pendiente.getEstado())){
                // Obtenemos el organismo extinguido
                Organismo organismoExtinguido = organismoEjb.findById(pendiente.getIdOrganismo());
                //Obtenemos libros de organismo extinguido
                List<Libro> libros = organismoExtinguido.getLibros();
                //Si solo tiene un organismo que le sustituye se asignan los libros a ese organismo
                if(organismoExtinguido.getHistoricoUO().size() == 1){// Se procesa internamente
                   log.info("Entramos en historicos 1 de extinguido" + organismoExtinguido.getDenominacion());
                   if(libros.size()>0){
                      log.info("Entramos en hay libros de historico 1 " + organismoExtinguido.getDenominacion());
                      // Asignamos libros misma numeración
                      Organismo orgSustituye = new ArrayList<Organismo>(organismoExtinguido.getHistoricoUO()).get(0);

                      //Asignamos el libro al organismo que lo sustituye
                      for(Libro libro: libros){
                        libro.setOrganismo(orgSustituye);
                        libroEjb.merge(libro);
                      }
                      // asignamos los libros para mostrarlos en el jsp
                      orgSustituye.setLibros(libros);
                      // Añadimos todos los organimos procesados automáticamente
                      extinguidosAutomaticos.put(organismoExtinguido.getDenominacion(),orgSustituye);
                   }
                   //actualizar pendiente
                   pendiente.setProcesado(true);
                   pendiente.setFecha(RegwebUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                   pendienteEjb.merge(pendiente);
                   //Mensaje.saveMessageInfo(request, getMessage("organismo.extinguido.procesado.automatico"));
                   log.info("MAP " +extinguidosAutomaticos.get(organismoExtinguido.getDenominacion()));
                }else { // tiene más de un historico
                    log.info("Entramos en historicos +1 de extinguido " + organismoExtinguido.getDenominacion());
                    if(libros.size()>0){//Si tiene libros se añade para procesarlo
                        log.info("Tiene libros " + organismoExtinguido.getDenominacion());
                      organismosExtinguidos.add(organismoExtinguido);
                    }
                }
            }else{  // ANULADOS
              // TODO ANULADOS
            }
          }
          if(extinguidosAutomaticos.size()>0 || organismosExtinguidos.size()>0) {
              model.addAttribute("extinguidosAutomaticos", extinguidosAutomaticos);
              model.addAttribute("organismosAProcesar", organismosExtinguidos);
          }else{
              log.info("no hay organismos a procesar ");
              Mensaje.saveMessageInfo(request, getMessage("organismos.procesados.vacio"));
          }
          model.addAttribute("esPendiente", true);



        }else {
            log.debug("else no pendientes de procesar");
            Mensaje.saveMessageInfo(request, getMessage("organismo.nopendientesprocesar"));
            return "redirect:/organismo/list";
        }
        //return "organismo/organismosActualizados";
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
                pendiente.setFecha(RegwebUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
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
        List<Organismo> organismosEntidad = organismoEjb.findByEntidad(entidad.getId());

        List<Organismo> organismosEntidadVigentes = organismoEjb.findByEntidadEstadoConOficinas(entidad.getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        if(organismosEntidad.size()>0) {
            model.addAttribute("organismosAProcesar", organismosEntidad);
            model.addAttribute("organismosSustituyentes", organismosEntidadVigentes);
            model.addAttribute("esPendiente", false);
        }else{
            Mensaje.saveMessageInfo(request, getMessage("organismos.procesados.vacio"));
        }

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
    public List<Usuario> propietarios() throws Exception {

        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
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
    public List<UsuarioEntidad> administradoresEntidadModificar(Usuario propietario, Entidad entidad) throws Exception {

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
    //TODO borrar, solo se emplea para testear
   /* @RequestMapping(value="/organismosdestinatarios", method= RequestMethod.GET)
    public void obtenerOrganismoDestinatarios()throws Exception {
         sincronizadorDIR3Ejb.obtenerOrganismosDestinatarios("A04006741");

    }
*/

    @InitBinder("entidadForm")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

        binder.registerCustomEditor(UsuarioEntidad.class, "entidad.administradores",new UsuarioEntidadEditor());
        binder.setValidator(this.entidadValidator);
    }


}
