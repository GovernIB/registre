package es.caib.regweb3.webapp.controller.entidad;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RolUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.editor.UsuarioEntidadEditor;
import es.caib.regweb3.webapp.form.EntidadForm;
import es.caib.regweb3.webapp.form.LibroOrganismo;
import es.caib.regweb3.webapp.utils.*;
import es.caib.regweb3.webapp.validator.EntidadValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
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
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.*;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Entidad}
 *
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@SessionAttributes(types = EntidadForm.class)
@RequestMapping(value = "/entidad")
public class EntidadController extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntidadValidator entidadValidator;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RolUtils rolUtils;

    @EJB(mappedName = DescargaLocal.JNDI_NAME)
    private DescargaLocal descargaEjb;

    @EJB(mappedName = SincronizadorDir3Local.JNDI_NAME)
    private SincronizadorDir3Local sincronizadorDIR3Ejb;

    @EJB(mappedName = PendienteLocal.JNDI_NAME)
    private PendienteLocal pendienteEjb;

    @EJB(mappedName = LibroLocal.JNDI_NAME)
    private LibroLocal libroEjb;

    @EJB(mappedName =ArchivoLocal.JNDI_NAME)
    private ArchivoLocal archivoEjb;

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = PluginLocal.JNDI_NAME)
    private PluginLocal pluginEjb;

    @EJB(mappedName = ContadorLocal.JNDI_NAME)
    private ContadorLocal contadorEjb;

    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;




    /**
     * Crear Libro
     */
    @RequestMapping(value = "/crearLibro", method = RequestMethod.GET)
    public String crearLibro(HttpServletRequest request) throws Exception {

        List<Entidad> entidades = entidadEjb.getAll();

        for(Entidad entidad:entidades){
            if(entidad.getLibro() == null){
                Libro libro = new Libro();
                libro.setCodigo(entidad.getCodigoDir3().substring(0,3));
                libro.setNombre(entidad.getNombre());
                libro.setOrganismo(organismoEjb.findByCodigoEntidadLigero(entidad.getCodigoDir3(), entidad.getId()));
                entidad.setLibro(libroEjb.crearLibro(libro));

                entidadEjb.merge(entidad);

                Mensaje.saveMessageInfo(request,"Se ha creado el libro a la Entidad: " + entidad.getNombre() + " modifique el código y nombre del libro, editando la entidad");
            }else{
                Mensaje.saveMessageError(request,"Ya existe un libro en la Entidad: " + entidad.getNombre());
            }
        }

        return "redirect:/inici";
    }

    /**
     * Listado de todas las Entidades
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/entidad/list/1";
    }

    /**
     * Listado de entidades
     *
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView list(@PathVariable Integer pageNumber) throws Exception {

        ModelAndView mav = new ModelAndView("entidad/entidadList");
        List<Entidad> listado = entidadEjb.getPagination((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION);
        Long total = entidadEjb.getTotal();

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevaEntidad(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = new Entidad();

        model.addAttribute(new EntidadForm(entidad));

        try {
            model.addAttribute("propietarios", propietarios());
        } catch (I18NException i18ne) {
            log.error(I18NUtils.getMessage(i18ne), i18ne);
            return "redirect:/entidad/list";
        }

        return "entidad/entidadForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevaEntidad(@ModelAttribute EntidadForm entidadForm, BindingResult result, Model model, SessionStatus status, HttpServletRequest request) throws Exception {

        entidadValidator.validate(entidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            try {
                model.addAttribute("propietarios", propietarios());
            } catch (I18NException i18ne) {
                log.error(I18NUtils.getMessage(i18ne), i18ne);
                Mensaje.saveMessageError(request, getMessage("error.jsp.desconegut"));
                return "redirect:/entidad/list";
            }

            return "entidad/entidadForm";

        } else { // Si no hay errores guardamos el registro

            try {
                Entidad entidad = entidadForm.getEntidad();

                //Guardamos la nueva Entidad y sus propiedades por defecto
                entidadEjb.nuevaEntidad(entidad);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
            } catch (Exception e) {
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

        Entidad entidadActiva = getEntidadActiva(request);
        EntidadForm entidadForm = new EntidadForm();

        try {
            Entidad entidad = entidadEjb.findById(entidadId);

            // Comprueba que la Entidad existe
            if (entidad == null) {
                log.info("No existe esta entidad");
                Mensaje.saveMessageError(request, getMessage("aviso.entidad.noExiste"));
                return "redirect:/inici";
            }

            entidadForm.setEntidad(entidad);

            // Comprobamos que el usuario puede editar la Entidad solicitada
            //Si el usuario es SUPERADMIN PUEDE EDITAR CUALQUIER ENTIDAD
            Rol rolActivo = getRolActivo(request);
            if (!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)) {

                List<Entidad> entidades = getEntidadesAutenticado(request);
                if (!entidades.contains(entidad)) {
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

            // Solo si es RWE_SUPERADMIN puede editar los Propietarios
            if (rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)) {
                model.addAttribute("propietarios", propietarios());
            }

            // Solo si es RWE_ADMIN puede editar los Administradores de entidad
            if (rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
                rolUtils.actualizarRolesUsuariosAdmin(entidad.getId());
                model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidad.getPropietario(), entidad));
            }

            model.addAttribute("tieneOrganismos", entidadEjb.tieneOrganismos(entidadId));


        } catch (I18NException i18ne) {
            log.error(I18NUtils.getMessage(i18ne), i18ne);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(entidadForm);

        return "entidad/entidadForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.Entidad}
     */
    @RequestMapping(value = "/{entidadId}/edit", method = RequestMethod.POST)
    public String editarEntidad(@ModelAttribute @Valid EntidadForm entidadForm, @PathVariable("entidadId") Long entidadId, BindingResult result, Model model,
                                SessionStatus status, HttpServletRequest request) {

        String destino = "redirect:/entidad/list";

        entidadValidator.validate(entidadForm, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            try {
                // Solo si es RWE_SUPERADMIN puede editar los Propietarios
                if (getRolActivo(request).getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)) {
                    model.addAttribute("propietarios", propietarios());
                }

                // Solo si es RWE_ADMIN puede editar los Administradores de entidad
                if (getRolActivo(request).getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
                    model.addAttribute("administradoresEntidad", administradoresEntidadModificar(entidadForm.getEntidad().getPropietario(), entidadForm.getEntidad()));
                }

                model.addAttribute("tieneOrganismos", entidadEjb.tieneOrganismos(entidadId));
            } catch (I18NException i18ne) {
                log.error(I18NUtils.getMessage(i18ne), i18ne);
                Mensaje.saveMessageError(request, I18NUtils.getMessage(i18ne));
                return "redirect:/entidad/list";
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
            if (!entidadEjb.findById(entidadId).getPropietario().getId().equals(entidad.getPropietario().getId())) {
                log.info("Cambio de propietario: " + entidad.getPropietario().getId());

                // Si ha cambiado, comprobamos si ya existia
                UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(entidad.getPropietario().getId(), entidadId);

                if (usuarioEntidad == null) { // SI no existe, lo creamos
                    log.info("Creamos el nuevo UsuarioEntidad");
                    usuarioEntidad = new UsuarioEntidad();
                    usuarioEntidad.setEntidad(entidad);
                    usuarioEntidad.setUsuario(entidad.getPropietario());
                    usuarioEntidad.setFechaAlta(new Date());

                    usuarioEntidadEjb.persist(usuarioEntidad);
                } else if (!usuarioEntidad.getActivo()) { //Si existe, pero está inactivo, lo activamos.
                    log.info("Ya existía el UsuarioEntidad, lo activamos");
                    usuarioEntidad.setActivo(true);
                    usuarioEntidadEjb.merge(usuarioEntidad);
                }


            }

            //Modificación con archivo Logo Menú, Logo Pie o imagen sello
            if ((entidadForm.getLogoMenu() != null) || (entidadForm.getLogoPie() != null) || entidadForm.getLogoSello() != null) {

                Archivo eliminarLogoMenu = null;
                Archivo eliminarLogoPie = null;
                Archivo eliminarImagenSello = null;

                if (entidadForm.getLogoMenu() != null && !entidadForm.getLogoMenu().isEmpty()) {

                    afm = new ArchivoFormManager(archivoEjb, entidadForm.getLogoMenu(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);

                    // Redimensionamos el logoMenu, si es necesario (tamaño demasiado grande)
                    byte[] logoRedimensionat = redimensionaLogoMenu(entidadForm.getLogoMenu().getBytes());
                    eliminarLogoMenu = entidadGuardada.getLogoMenu();

                    // Asociamos el nuevo archivo
                    entidad.setLogoMenu(afm.prePersist(logoRedimensionat));
                }

                if (entidadForm.getLogoPie() != null && !entidadForm.getLogoPie().isEmpty()) {

                    afm = new ArchivoFormManager(archivoEjb, entidadForm.getLogoPie(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                    eliminarLogoPie = entidadGuardada.getLogoPie();

                    // Asociamos el nuevo archivo
                    entidad.setLogoPie(afm.prePersist(null));
                }

                if (entidadForm.getLogoSello() != null && !entidadForm.getLogoSello().isEmpty()) {
                    afm = new ArchivoFormManager(archivoEjb, entidadForm.getLogoSello(), RegwebConstantes.ARCHIVOS_LOCATION_PROPERTY);
                    eliminarImagenSello = entidadGuardada.getLogoSello();

                    // Asociamos el nuevo archivo
                    entidad.setLogoSello(afm.prePersist(null));
                }


                // Si se selecciona el check de borrar Logo Menu , Logo Pie y/o Imagen Sello, se pone a null
                if (entidadForm.isBorrarLogoMenu()) {
                    eliminarLogoMenu = entidadGuardada.getLogoMenu();
                    entidad.setLogoMenu(null);
                }
                if (entidadForm.isBorrarLogoPie()) {
                    eliminarLogoPie = entidadGuardada.getLogoPie();
                    entidad.setLogoPie(null);
                }
                if (entidadForm.isBorrarLogoSello()) {
                    eliminarImagenSello = entidadGuardada.getLogoSello();
                    entidad.setLogoSello(null);
                }

                entidadEjb.merge(entidad);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

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
                //Eliminamos la anterior imagen del sello
                if (eliminarImagenSello != null) {
                    FileSystemManager.eliminarArchivo(eliminarImagenSello.getId());
                    archivoEjb.remove(eliminarImagenSello);
                }


            } else {

                Archivo eliminarLogoMenu = null;
                Archivo eliminarLogoPie = null;
                Archivo eliminarImagenSello = null;

                // Si se selecciona el check de borrar Logo Menu y/o Logo Pie, se pone a null
                if (entidadForm.isBorrarLogoMenu()) {
                    eliminarLogoMenu = entidadGuardada.getLogoMenu();
                    entidad.setLogoMenu(null);
                }
                if (entidadForm.isBorrarLogoPie()) {
                    eliminarLogoPie = entidadGuardada.getLogoPie();
                    entidad.setLogoPie(null);
                }
                if (entidadForm.isBorrarLogoSello()) {
                    eliminarImagenSello = entidadGuardada.getLogoSello();
                    entidad.setLogoSello(null);
                }

                if (entidadForm.getEntidad().getLogoMenu() != null) {
                    entidad.setLogoMenu(archivoEjb.findById(entidadForm.getEntidad().getLogoMenu().getId()));
                }

                entidadEjb.merge(entidadForm.getEntidad());
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

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

                //Eliminamos el anterior sello
                if (eliminarImagenSello != null) {
                    FileSystemManager.eliminarArchivo(eliminarImagenSello.getId());
                    archivoEjb.remove(eliminarImagenSello);
                }
            }

            if (isAdminEntidad(request)) { // Si un Administrador de Entidad, la edita.

                destino = "redirect:/inici";
                if (getEntidadActiva(request).equals(entidad)) { //Si la entidad modificada en la activa, la actualizamos.
                    setEntidadActiva(entidad, request);
                }

            }


        } catch (Exception e) {
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

            // Comprova que l'entitat no existeixi com organisme dins la BBDD i tengui llibres (per les EDP externes)
            Organismo organismo = organismoEjb.findByCodigoOtraEntidadConLibros(entidad.getCodigoDir3(), entidadId);

            if (organismo != null && libroEjb.getLibrosActivosOrganismoDiferente(organismo.getCodigo(), entidadId).size() > 0) {  // Ja existeix un organisme amb el codi dir3 i llibres

                Mensaje.saveMessageError(request, getMessage("entidad.activar.nok"));

            } else {  // Si no existeix ja l'organisme i té llibres

                entidad.setActivo(true);

                entidadEjb.merge(entidad);

                Mensaje.saveMessageInfo(request, getMessage("entidad.activar.ok"));
            }

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
    public String actualizar(@PathVariable Long entidadId, HttpServletRequest request) throws Exception {

        try {

            //Marcamos la entidad de mantenimiento
            entidadEjb.marcarEntidadMantenimiento(entidadId, true);

            Descarga ultimaDescarga = descargaEjb.ultimaDescarga(RegwebConstantes.UNIDAD, entidadId);
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

            int actualizados = sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, fechaUltimaActualizacion, fechaSincronizacion);
            if (actualizados == -1) {
                entidadEjb.marcarEntidadMantenimiento(entidadId, false);
                log.info("No se puede actualizar regweb hasta que no se haya actualizado dir3caib previamente");

                Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.imposible"));
                return "redirect:/organismo/list";
            }

            // actualizamos nombre y codigo de la entidad, si la unidad raiz a la que representa se ha extinguido.
            actualizarEntidadExtincionUnidadRaiz(entidadId, request);


        } catch (Exception e) {
            log.error("Error actualizacion", e);
            entidadEjb.marcarEntidadMantenimiento(entidadId, false);
            Mensaje.saveMessageError(request, getMessage("regweb.actualizacion.nook") + ": " + e.getMessage());
            return "redirect:/organismo/list";
        }

        return "redirect:/entidad/procesarPendientes";
    }

    /**
     * Sincronizamos una {@link es.caib.regweb3.model.Entidad} de dir3caib
     * @param entidadId
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{entidadId}/sincronizar")
    public JsonResponse sincronizar(@PathVariable Long entidadId) throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {
            //Marcamos la entidad en mantenimiento
            entidadEjb.marcarEntidadMantenimiento(entidadId, true);

            //Iniciamos proceso sincronización
            int sincronizados = sincronizadorDIR3Ejb.sincronizarActualizar(entidadId, null, null);
            if (sincronizados == -1) {
                log.info("No se puede sincronizar regweb hasta que no se haya actualizado dir3caib previamente");
                jsonResponse.setStatus("FAIL");
                jsonResponse.setError(getMessage("regweb.actualizacion.nopermitido"));
            } else {
                jsonResponse.setError(getMessage("regweb.sincronizados.numero") + sincronizados);
                jsonResponse.setStatus("SUCCESS");

                //Asociamos el Organismo raíz creado al Libro de la Entidad
                Entidad entidad = entidadEjb.findById(entidadId);
                entidad.getLibro().setOrganismo(organismoEjb.findByCodigo(entidad.getCodigoDir3()));
                entidadEjb.merge(entidad);
            }
            entidadEjb.marcarEntidadMantenimiento(entidadId, false);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("regweb.sincronizacion.nook") + ": " + e.getMessage());
        }

        return jsonResponse;

    }

    /**
     * Reiniciar Contadoresde una Entidad
     */
    @RequestMapping(value = "/{idEntidad}/reiniciarContadores")
    public String reiniciarContadoresEntidad(@PathVariable Long idEntidad, HttpServletRequest request) {

        try {

            // Reinicia los contadores del Libro de la entidad
            Entidad entidad = entidadEjb.findById(idEntidad);
            contadorEjb.reiniciarContadoresLibro(entidad.getLibro());

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

        } catch (I18NException i18ne) {
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
        } catch (I18NException i18ne) {
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
    @RequestMapping(value = "/procesarPendientes", method = RequestMethod.GET)
    public String procesarPendientes(HttpServletRequest request, Model model) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        /* Preparamos todos los organismos a procesar (extinguidos, anulados, transitorios,vigentes)*/
        List<Pendiente> pendientesDeProcesar = pendienteEjb.findPendientesProcesar(entidad.getId());

        if (!pendientesDeProcesar.isEmpty()) {

            // Para mostrar información al usuario
            Map<String, Organismo> extinguidosAutomaticos = new HashMap<String, Organismo>();// Organismos extinguidos y sustitutos procesados automaticamente

            for (Pendiente pendiente : pendientesDeProcesar) {

                if (RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_TRANSITORIO.equals(pendiente.getEstado()) || RegwebConstantes.ESTADO_ENTIDAD_VIGENTE.equals(pendiente.getEstado())) {
                    // Obtenemos el organismo extinguido
                    Organismo organismoExtinguido = organismoEjb.findByIdLigero(pendiente.getIdOrganismo());

                    //Obtenemos los permisos del organismo extinguido
                    List<PermisoOrganismoUsuario> permisos = permisoOrganismoUsuarioEjb.findByOrganismo(organismoExtinguido.getId());

                    // Si tiene permisos, hay que cambiarle el Organismo del que dependen
                    if(permisos.size() > 0){
                        log.info("Buscando sustitutos de: " + organismoExtinguido.getDenominacion() + " - " + organismoExtinguido.getCodigo());
                        Set<Organismo> sustitutosOficina = obtenerSustitutosOficina(organismoExtinguido.getId());

                        if(sustitutosOficina.size() == 0){ // Error, no hay ningún Organismos sustituto

                            //todo eliminar permisos del Organismo extinguido

                        }else { // Se procesa automáticamente

                            // Para todos los sustitutos, asignamos los usuarios que tenía el anterior Organismo
                            for(Organismo organismoSustituto:sustitutosOficina){

                                // Activamos la posibilidad de asociarle usuarios
                                organismoEjb.activarUsuarios(organismoSustituto.getId());

                                // Actualizamos el Organismo sustituto todos los permisos
                                for(PermisoOrganismoUsuario permiso:permisos){
                                    permiso.setOrganismo(organismoSustituto);
                                    permisoOrganismoUsuarioEjb.merge(permiso);
                                }

                                // Añadimos todos los organimos procesados automáticamente
                                extinguidosAutomaticos.put(organismoExtinguido.getDenominacion(), organismoSustituto);
                            }

                            // Actualizamos el destino de los registros pendientes de llegada uno de los Organismos sustitutos
                            Organismo organismoSustituto = new ArrayList<>(sustitutosOficina).get(0);
                            oficioRemisionEjb.actualizarDestinoPendientesLlegada(organismoExtinguido.getId(), organismoSustituto.getId());

                        }

                    }

                    // Actualizamos el Pendiente
                    pendiente.setProcesado(true);
                    pendiente.setFecha(TimeUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                    pendienteEjb.merge(pendiente);

                }
            }

            model.addAttribute("extinguidosAutomaticos", extinguidosAutomaticos); // organismos que se les ha asignado automaticamente permisos.

            // Quitamos el modo mantenimiento de la Entidad
            //entidadEjb.marcarEntidadMantenimiento(entidad.getId(), false);

        } else {
            // Quitamos el modo mantenimiento de la Entidad
            //entidadEjb.marcarEntidadMantenimiento(entidad.getId(), false);

            Mensaje.saveMessageInfo(request, getMessage("organismo.nopendientesprocesar"));
            return "redirect:/organismo/list";
        }

        return "organismo/resumenSincronizacion";
    }


    /**
     * Procesa un organismo con libros  y monta la respuesta en json
     * para después mostrar los resultados
     * Realmente lo que hace es asignar el libro al nuevo organismo indicado.
     */
    @RequestMapping(value = "/procesarlibroorganismo/{organismoId}/{esPendiente}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse procesarLibroOrganismo(@RequestBody List<LibroOrganismo> libroOrganismos, @PathVariable("organismoId") Long organismoId, @PathVariable("esPendiente") boolean esPendiente, BindingResult result, HttpServletRequest request) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
            log.info("PROCESANDO " + organismoId);
            List<LibroOrganismo> nombresLibrosOrganismos = new ArrayList<LibroOrganismo>();  // Contendrá la lista de nombres de libro y organismo

            // Comenzamos a procesar los libros organismos que recibimos del request.
            for (LibroOrganismo libroOrganismo : libroOrganismos) {

                log.info("Libro: " + libroOrganismo.getLibro());
                log.info("Organismo: " + libroOrganismo.getOrganismo());

                //Obtenemos libro, el organismo que lo sustituye
                Libro libro = libroEjb.findById(new Long(libroOrganismo.getLibro()));
                if (!libroOrganismo.getOrganismo().equals("-1")) {
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
            if (esPendiente) {
                Pendiente pendiente = pendienteEjb.findByIdOrganismo(organismoId);
                pendiente.setProcesado(true);
                pendiente.setFecha(TimeUtils.formateaFecha(new Date(), RegwebConstantes.FORMATO_FECHA_HORA));
                pendienteEjb.merge(pendiente);
            }

            // Necesitamos su nombre
            Organismo extinguido = organismoEjb.findByIdLigero(organismoId);

            // MONTAMOS LA RESPUESTA JSON
            jsonResponse.setStatus("SUCCESS");

            OrganismoJson organismoJson = new OrganismoJson();
            organismoJson.setId(organismoId.toString());
            organismoJson.setNombre(extinguido.getDenominacion());
            organismoJson.setLibroOrganismos(nombresLibrosOrganismos);

            jsonResponse.setResult(organismoJson);
            entidadEjb.marcarEntidadMantenimiento(getEntidadActiva(request).getId(), false);
        } catch (Exception e) {
            jsonResponse.setStatus("FAIL");
            e.printStackTrace();
        }
        return jsonResponse;
    }


    /**
     * Obtiene los Usuarios que pueden ser Propietarios de una Entidad
     *
     * @return
     * @throws Exception
     * @throws I18NException
     */
    private List<Usuario> propietarios() throws Exception, I18NException {

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION);
        String[] usuarios = loginPlugin.getUsernamesByRol(RegwebConstantes.RWE_ADMIN);

        List<Usuario> administradoresEntidad = new ArrayList<Usuario>();

        for (String identificador : usuarios) {
            Usuario usuario = usuarioEjb.findByIdentificador(identificador);
            if (usuario != null) {
                administradoresEntidad.add(usuario);
            }
        }
        return administradoresEntidad;
    }


    /**
     * Retorna los posibles Administradores al modificar una Entidad
     *
     * @param propietario
     * @param entidad
     * @return
     * @throws Exception
     */
    private List<UsuarioEntidad> administradoresEntidadModificar(Usuario propietario,
                                                                 Entidad entidad) throws Exception, I18NException {

        // Obtenemos todos los UsuarioEntidad con Rol RWE_ADMIN
        List<UsuarioEntidad> administradoresEntidad = usuarioEntidadEjb.findAdministradoresByEntidad(entidad.getId());

        // Eliminamos el Propietario de la Entidad
        UsuarioEntidad usuarioPropietario = usuarioEntidadEjb.findByUsuarioEntidad(propietario.getId(), entidad.getId());
        administradoresEntidad.remove(usuarioPropietario);

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

        List<Descarga> listado = descargaEjb.getPaginationByEntidad((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, entidad.getId());
        Long total = descargaEjb.getTotalByEntidad(entidad.getId());

        Paginacion paginacion = new Paginacion(total.intValue(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        mav.addObject("entidad", entidad);


        return mav;
    }

    /**
     * @param logoMenu
     * @return
     * @throws IOException
     */
    private byte[] redimensionaLogoMenu(byte[] logoMenu) throws IOException {

        // Obtenemos la imagen del Logo
        ByteArrayInputStream in = new ByteArrayInputStream(logoMenu);
        BufferedImage imatgeLogo = ImageIO.read(in);

        int ampladaOriginal = imatgeLogo.getWidth();
        int alsadaOriginal = imatgeLogo.getHeight();

        // Si no passa cap dels paràmetres màxims, no fa res
        if (ampladaOriginal > RegwebConstantes.LOGOMENU_AMPLADA_MAX || alsadaOriginal > RegwebConstantes.LOGOMENU_ALSADA_MAX) {

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

        } else { // Retorna la imagen original
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
                loginService.cambioEntidad(entidad, getLoginInfo(request));
            } else {
                throw new Exception("La raiz se ha dividido en más de un organismo, houston tenemos un problema");
            }

        }
    }

    /**
     * Obtiene el/los organismos con oficinas que sustituyen a uno Extinguido
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    private Set<Organismo> obtenerSustitutosOficina(Long idOrganismo) throws Exception{

        Set<Organismo> sustitutos =  new HashSet<>();
        Set<Organismo> sustitutosOficina =  new HashSet<>();
        organismoEjb.obtenerHistoricosFinales(idOrganismo,sustitutos);

        for (Organismo orgHistorico : sustitutos) {
            if (oficinaEjb.tieneOficinasServicio(orgHistorico.getId(), RegwebConstantes.OFICINA_VIRTUAL_SI)) {
                sustitutosOficina.add(orgHistorico);
            }
        }

        return sustitutosOficina;
    }

    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_UI;
    }

    @ModelAttribute("configuraciones")
    public long[] configuraciones() throws Exception {
        return RegwebConstantes.CONFIGURACIONES_PERSONA;
    }

    @ModelAttribute("perfilesCustodia")
    public long[] perfilesCustodia() throws Exception {
        return RegwebConstantes.PERFILES_CUSTODIA;
    }

    @InitBinder({"entidadForm"})
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

        binder.registerCustomEditor(UsuarioEntidad.class, "entidad.administradores", new UsuarioEntidadEditor());
        binder.setValidator(this.entidadValidator);
    }

    @InitBinder({"permisoLibroUsuarioForm"})
    public void initBinder2(WebDataBinder binder) {
        // Per resoldre el problema dels 256 objectes dins un form
        binder.setAutoGrowCollectionLimit(500);
    }
}
