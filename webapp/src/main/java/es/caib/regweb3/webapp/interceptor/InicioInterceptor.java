package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.PendienteLocal;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.TipoDocumentalLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Fundació BIT.
 * <p>
 * Interceptor que se ejecuta en cualquier petición de la aplicación.
 *
 * @author earrivi
 * Date: 6/02/14
 * <p>
 * TODO S'ha de moure la funcionalitat d'aquesta classe
 * a es.caib.regweb3.webapp.securit.AuthenticationSuccessListener
 */
public class InicioInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private LoginService loginService;

    @EJB(mappedName = "regweb3/UsuarioEJB/local")
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    @EJB(mappedName = "regweb3/PendienteEJB/local")
    private PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb3/TipoDocumentalEJB/local")
    private TipoDocumentalLocal tipoDocumentalEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // log.info(" ++++++++++++++++++++ ENTRA InicioInterceptor ++++++++++++++");
        //long start = System.currentTimeMillis();
        try {

            HttpSession session = request.getSession();
            LoginInfo loginInfo = null;

            if (request.getRequestURI().equals("/regweb3/noAutorizado")) {
                return true;
            }

            if (request.getUserPrincipal() != null) { // Si hay un usuario autenticado correctamente

                // Buscamos en la sesion si hay algún usuario autenticado
                loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);

                if (loginInfo == null) { // Si la sesión es nueva

                    // Obtenemos el usuario autenticado de la bbdd mediante el login
                    String identificador = request.getUserPrincipal().getName();
                    Usuario usuario = usuarioEjb.findByIdentificador(identificador);

                    if (usuario != null) { // El usuario existe en REGWEB3
                        // Si el usuario es de tipo aplicación, no puede acceder.
                        if (usuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)) {
                            log.info(usuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB3.");
                            response.sendRedirect("/regweb3/noAutorizado");
                            return false;
                        }

                        // Si no tiene idioma por defecto, se lo ponemos
                        if (usuario.getIdioma() == null) {
                            usuario.setIdioma(RegwebConstantes.IDIOMA_CATALAN_ID);
                            usuarioEjb.merge(usuario);
                        }

                        // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                        loginInfo = loginService.configurarUsuario(usuario, request);

                    } else { // El usuario NO existe en REGWEB3

                        Usuario nuevoUsuario = loginService.crearUsuario(identificador);

                        if (nuevoUsuario != null) {
                            log.info("Usuario creado: " + nuevoUsuario.getNombreCompleto());

                            // Si el usuario es de tipo aplicación, no puede acceder.
                            if (nuevoUsuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)) {
                                log.info(nuevoUsuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB3.");
                                response.sendRedirect("/regweb3/noAutorizado");
                                return false;
                            }

                            // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                            loginInfo = loginService.configurarUsuario(nuevoUsuario, request);

                        } else {

                            log.info("No está autorizado: " + identificador);
                            response.sendRedirect("/regweb3/noAutorizado");
                            return false;
                        }

                    }
                }

                // Rutas que se saltarán las comprobaciones
                if (request.getRequestURI().startsWith("/regweb3/rol/") || request.getRequestURI().equals("/regweb3/aviso")
                        || request.getRequestURI().startsWith("/regweb3/cambioEntidad") || request.getRequestURI().startsWith("/regweb3/entidad/pendientesprocesar")
                        || request.getRequestURI().startsWith("/regweb3/entidad/procesarlibroorganismo") || request.getRequestURI().startsWith("/regweb3/error")
                        || request.getRequestURI().contains(".jsp") || request.getRequestURI().startsWith("/regweb3/rest")) {

                    return true;
                }


                //Obtenemos si la entidad tiene libros pendientes de procesar
                boolean tienePendientesDeProcesar = false;
                if(loginInfo.getEntidadActiva()!=null) {
                    tienePendientesDeProcesar = pendienteEjb.findPendientesProcesar(loginInfo.getEntidadActiva().getId()).size() > 0;
                }

                switch (loginInfo.getRolActivo().getNombre()) {

                    case RegwebConstantes.RWE_USUARI:

                        Entidad entidadActiva =  loginInfo.getEntidadActiva();

                        //No permitir que se hagan registros si la entidad está en mantenimiento
                        if (entidadActiva.getMantenimiento() || tienePendientesDeProcesar) {
                            log.info("Tareas de Mantenimiento");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.tareasmantenimiento"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                        //Plugin Generación Justificante
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE)) {
                            log.info("No existe el plugin de generación del justificante");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginjustificante"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }
                        //Plugin Custodia Justificante
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE)) {
                            log.info("No existe el plugin de custodia del justificante");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodiajustificante"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;

                        }
                        // Plugin Custodia
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA)) {
                            log.info("No existe el plugin de custodia");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodia"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;

                        }
                        //Plugin Firma en Servidor
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_FIRMA_SERVIDOR)) {
                            log.info("No existe el plugin de firma en servidor");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginfirma"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;

                        }
                        // Sir ServerBAse
                        if (entidadActiva.getSir() && Configuracio.getSirServerBase() == null) {
                            log.info("Error, falta propiedad sirserverbase");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.sirserverbase"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                        // Dir3Caib Server
                        if (PropiedadGlobalUtil.getDir3CaibServer() == null || PropiedadGlobalUtil.getDir3CaibServer().isEmpty()) {
                            log.info("La propiedad Dir3CaibServer no está definida");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibserver"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }
                        // Dir3Caib Username
                        if (PropiedadGlobalUtil.getDir3CaibUsername() == null || PropiedadGlobalUtil.getDir3CaibUsername().isEmpty()) {
                            log.info("La propiedad Dir3CaibUsername no está definida");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibusername"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }
                        // Dir3Caib Password
                        if (PropiedadGlobalUtil.getDir3CaibPassword() == null || PropiedadGlobalUtil.getDir3CaibPassword().isEmpty()) {
                            log.info("La propiedad Dir3CaibPassword no está definida");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibpassword"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                        // Tipo documental existente
                        if(tipoDocumentalEjb.getByEntidad(entidadActiva.getId()).size()==0){
                            log.info("Aviso: No hay ningún Tipo Documental para la Entidad Activa");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.tipoDocumental"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                        //Comprobamos que se haya definido un formato para el número de registro en la Entidad
                        if(entidadActiva.getNumRegistro() == null || entidadActiva.getNumRegistro().length()==0){
                            log.info("No hay configurado el formato del numero de registro para la Entidad activa");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.formatoRegistro"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                    break;

                    case RegwebConstantes.RWE_ADMIN:

                        //Si no ha asignado todos los libros le redirige a la pagina de nuevo para procesarlos
                        if (tienePendientesDeProcesar) {
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.sincronizacion.librospendientes"));
                            response.sendRedirect("/regweb3/entidad/pendientesprocesar");
                            return false;
                        }

                    break;

                    case RegwebConstantes.RWE_SUPERADMIN:

                        //Validamos variable es.caib.regweb3.archivos.path
                        if (request.getRequestURI().equals("/regweb3/configuracion/editar") && FileSystemManager.getArchivosPath() == null) {
                            log.info("Error, editar entidad");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.archivospath"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                    break;


                }

            } else {
                response.sendRedirect("/regweb3/noAutorizado");
                return false;
            }

            return true;
        } catch (I18NException i18ne) {
            throw new Exception(I18NUtils.getMessage(i18ne), i18ne);

        } finally {
            // log.info("Interceptor Inicio: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }

}
