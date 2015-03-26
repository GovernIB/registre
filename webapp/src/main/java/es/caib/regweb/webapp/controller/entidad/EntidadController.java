package es.caib.regweb.webapp.controller.entidad;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.FileSystemManager;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.utils.Configuracio;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.login.RegwebLoginPluginManager;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.editor.UsuarioEntidadEditor;
import es.caib.regweb.webapp.form.EntidadForm;
import es.caib.regweb.webapp.form.LibroOrganismo;
import es.caib.regweb.webapp.form.PermisoLibroUsuarioForm;
import es.caib.regweb.webapp.form.UsuarioEntidadBusquedaForm;
import es.caib.regweb.webapp.utils.*;
import es.caib.regweb.webapp.validator.EntidadValidator;

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
import javax.validation.Valid;

import java.util.*;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb.model.Entidad}
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@SessionAttributes(types = EntidadForm.class)
@RequestMapping(value = "/entidad")
public class EntidadController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());
    
    @EJB(mappedName = "regweb/DescargaEJB/local")
    public DescargaLocal descargaEjb;

    @Autowired
    private EntidadValidator entidadValidator;

    @Autowired
    private UsuarioService usuarioService;

    @EJB(mappedName = "regweb/SincronizadorDir3EJB/local")
    private SincronizadorDir3Local sincronizadorDIR3Ejb;

    @EJB(mappedName = "regweb/PendienteEJB/local")
    public PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/ArchivoEJB/local")
    public ArchivoLocal archivoEjb;

    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb/UsuarioEJB/local")
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
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.Entidad}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevaEntidad(Model model) throws Exception {

        EntidadForm entidadForm = new EntidadForm();

        model.addAttribute(entidadForm);

        return "entidad/entidadForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb.model.Entidad}
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
     * Carga el formulario para modificar un {@link es.caib.regweb.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/edit", method = RequestMethod.GET)
    public String editarEntidad(@PathVariable("entidadId") Long entidadId, Model model, HttpServletRequest request) {

        EntidadForm entidadForm = new EntidadForm();
        try {
            Entidad entidad = entidadEjb.findById(entidadId);
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
            }
            model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidad.getPropietario(), entidad));
            model.addAttribute("tipoScan", Configuracio.getTipusScanejat());

        }catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(entidadForm);

        return "entidad/entidadForm";
    }

    /**
     * Editar un {@link es.caib.regweb.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/edit", method = RequestMethod.POST)
    public String editarEntidad(@ModelAttribute @Valid EntidadForm entidadForm,BindingResult result,Model model,
                                SessionStatus status, HttpServletRequest request) {

        String destino = "redirect:/entidad/list";

        entidadValidator.validate(entidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

           try {
                model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidadForm.getEntidad().getPropietario(), entidadForm.getEntidad()));
                model.addAttribute("tipoScan", Configuracio.getTipusScanejat());
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
     * Anula un {@link es.caib.regweb.model.Entidad}
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
     * Activar un {@link es.caib.regweb.model.Entidad}
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
    * Actualizamos una {@link es.caib.regweb.model.Entidad} de dir3caib
    */
    @RequestMapping(value = "/{entidadId}/actualizar")
    public void actualizar(@PathVariable Long entidadId, Model model, HttpServletRequest request) {

       try{
          Descarga ultimaDescarga = descargaEjb.findByTipoEntidad(RegwebConstantes.UNIDAD,entidadId);
          Date fechaUltimaActualizacion =  ultimaDescarga.getFechaImportacion();

          // Determinamos si es actualizacion o sincronizacion
          if(ultimaDescarga != null){
            model.addAttribute("descarga", ultimaDescarga);
          }
          // Establecemos la fecha de la primera sincronizacion
          Descarga primeraDescarga = descargaEjb.findByTipoEntidadInverse(RegwebConstantes.UNIDAD, entidadId);
          Date fechaSincronizacion = primeraDescarga.getFechaImportacion();


          sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, fechaUltimaActualizacion, fechaSincronizacion);

       }catch(Exception e){
           log.error("Error sincro", e);
           Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nook"));
       }

    }

    /**
    * Sincronizamos una {@link es.caib.regweb.model.Entidad} de dir3caib
    */
    @RequestMapping(value = "/{entidadId}/sincronizar")
    public String sincronizar(@PathVariable Long entidadId, Model model,  HttpServletRequest request) {

          try{
            sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, null, null);
          }catch(Exception e){
             log.error("Error sincro", e);
             Mensaje.saveMessageError(request, getMessage("regweb.sincronizacion.nook"));
          }
          return  "redirect:/organismo/list";
    }


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.PermisoLibroUsuario}
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

            ArrayList<PermisoLibroUsuario> plus = new ArrayList<PermisoLibroUsuario>();

            // Creamos la matriz de permisos-libros
            for ( int row = 0; row < libros.size(); row ++ ){

                //Obtenemos los permisos del Libro
                List<PermisoLibroUsuario> plu = permisoLibroUsuarioEjb.findByUsuarioLibro(usuarioEntidad.getId(), libros.get(row).getId());

                if(plu.size() == 0){ //Si no tiene los creamos

                    for ( int column = 0; column < RegwebConstantes.PERMISOS.length; column++ ){
                        PermisoLibroUsuario plu2 = new PermisoLibroUsuario();
                        plu2.setLibro(libros.get(row));
                        plu2.setPermiso(RegwebConstantes.PERMISOS[column]);
                        plus.add(plu2);
                    }
                }else{ //Si tiene, los añadimos.
                    plus.addAll(plu);
                }
            }

            permisoLibroUsuarioForm.setPermisoLibroUsuarios(plus);

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
     * Guardar un nuevo {@link es.caib.regweb.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/permisos/{idUsuarioEntidad}", method = RequestMethod.POST)
    public String asignarUsuario(@ModelAttribute PermisoLibroUsuarioForm permisoLibroUsuarioForm,@PathVariable Integer idUsuarioEntidad, SessionStatus status, HttpServletRequest request) {

        try {

            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(permisoLibroUsuarioForm.getUsuarioEntidad().getId());

            final boolean debug = log.isDebugEnabled();
            
            for (PermisoLibroUsuario plu : permisoLibroUsuarioForm.getPermisoLibroUsuarios()) {
                if  (debug) {
                  log.debug("Inicio: " + plu.getLibro().getNombre());
                }
                plu.setUsuario(usuarioEntidad);

                /*log.info("Libro: " + plu.getLibro().getId());
                log.info("Permiso: " + plu.getPermiso().getId());
                log.info("Activo: " + plu.getActivo());
                log.info("  ");
                log.info("  ");*/
                permisoLibroUsuarioEjb.merge(plu);
                if  (debug) {
                  log.info("Fin: " + plu.getLibro().getNombre());
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
     * Eliminar la asignación de un Usuario a un Organismo
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
     * Mostrar actualizados
     */
  /**
   * Función que gestiona los organismos extinguidos. Procesa los que son automáticos(1 historico) y
   * prepara los datos para los que no son automaticos y los envía al jsp para procesar manualmente
   */
    @RequestMapping(value = "/pendientesprocesar", method = RequestMethod.GET)
    public String mostrarActualizados(HttpServletRequest request, Model model) throws Exception {
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
                      log.info("Entramos en hay libros de historico 1");
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
                      organismosExtinguidos.add(organismoExtinguido);
                    }
                }
            }else{  // ANULADOS
              // TODO ANULADOS
            }
          }

          model.addAttribute("extinguidosAutomaticos", extinguidosAutomaticos);
          model.addAttribute("extinguidos", organismosExtinguidos);


        }else {
            log.debug("else no pendientes de procesar");
            Mensaje.saveMessageInfo(request, getMessage("organismo.nopendientesprocesar"));
            return "redirect:/organismo/list";
        }
        return "organismo/organismosActualizados";
    }


     /**
      * Procesa un organismo Extinguido con más de un organismo que lo sustituye, y monta la respuesta en json
      * para después mostrar los resultados
      */
     @RequestMapping(value="/procesarextinguido/{extinguidoId}", method= RequestMethod.POST)
     @ResponseBody
     public JsonResponse procesarExtinguido(@RequestBody List<LibroOrganismo> libroOrganismos, @PathVariable("extinguidoId") Long extinguidoId, BindingResult result, HttpServletRequest request) {
         JsonResponse jsonResponse = new JsonResponse();
         try {
            log.info("PROCESANDO "+ extinguidoId);
            List<LibroOrganismo> nombresLibrosOrganismos= new ArrayList<LibroOrganismo>();  // Contendrá la lista de nombres de libro y organismo

            // Comenzamos a procesar los libros organismos que recibimos del request.
            for(LibroOrganismo libroOrganismo:libroOrganismos){

              log.info("Libro: " + libroOrganismo.getLibro());
              log.info("Organismo: " + libroOrganismo.getOrganismo());

                //Obtenemos libro, el organismo que lo sustituye
              Libro libro = libroEjb.findById(new Long(libroOrganismo.getLibro()));
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
            Pendiente pendiente = pendienteEjb.findByIdOrganismo(extinguidoId);
            pendiente.setProcesado(true);
            pendiente.setFecha(RegwebUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
            pendienteEjb.merge(pendiente);

            // Necesitamos su nombre
            Organismo extinguido= organismoEjb.findById(extinguidoId);

            // MONTAMOS LA RESPUESTA JSON
            jsonResponse.setStatus("SUCCESS");

            OrganismoJson organismoJson = new OrganismoJson();
            organismoJson.setId(extinguidoId.toString());
            organismoJson.setNombre(extinguido.getDenominacion());
            organismoJson.setLibroOrganismos(nombresLibrosOrganismos);

            jsonResponse.setResult(organismoJson);
         }catch (Exception e){
            jsonResponse.setStatus("FAIL");
            e.printStackTrace();
         }
         return jsonResponse;
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
        List<UsuarioEntidad> usuarios = usuarioEntidadEjb.findByEntidad(entidad.getId());
        for (UsuarioEntidad usuario : usuarios) {
            usuarioService.actualizarRoles(usuario.getUsuario());
        }

        // Obtenemos todos los UsuarioEntidad con Rol RWE_ADMIN
        List<UsuarioEntidad> administradoresEntidad = usuarioEntidadEjb.findAdministradoresByEntidad(entidad.getId());
        for (UsuarioEntidad usuario : administradoresEntidad) {
            usuarioService.actualizarRoles(usuario.getUsuario());
        }

        // Eliminamos el Propietario de la Entidad
        UsuarioEntidad usuarioPropietario = usuarioEntidadEjb.findByUsuarioEntidad(propietario.getId(),entidad.getId());
        if(administradoresEntidad.contains(usuarioPropietario)){
            administradoresEntidad.remove(usuarioPropietario);
        }

        return administradoresEntidad;
    }


    @InitBinder("entidadForm")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

        binder.registerCustomEditor(UsuarioEntidad.class, "entidad.administradores",new UsuarioEntidadEditor());
        binder.setValidator(this.entidadValidator);
    }


}
