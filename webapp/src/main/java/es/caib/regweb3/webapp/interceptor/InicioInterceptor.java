package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
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

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    private EntidadLocal entidadEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // log.info(" ++++++++++++++++++++ ENTRA InicioInterceptor ++++++++++++++");
        //long start = System.currentTimeMillis();
        try {

            HttpSession session = request.getSession();
            LoginInfo loginInfo = null;
            Entidad entidadActiva = null;

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
                        || request.getRequestURI().startsWith("/regweb3/cambioEntidad") || request.getRequestURI().startsWith("/regweb3/entidad/procesarPendientes")
                        || request.getRequestURI().startsWith("/regweb3/entidad/procesarlibroorganismo") || request.getRequestURI().startsWith("/regweb3/error")
                        || request.getRequestURI().contains(".jsp") || request.getRequestURI().startsWith("/regweb3/rest")) {

                    return true;
                }

                switch (loginInfo.getRolActivo().getNombre()) {

                    case RegwebConstantes.RWE_USUARI:

                        entidadActiva =  loginInfo.getEntidadActiva();

                        if(entidadActiva == null){
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("oficinaActiva.null"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                        //No permitir que se hagan registros si la entidad está en mantenimiento
                        if (entidadActiva.getMantenimiento()) {
                            log.info("Tareas de Mantenimiento");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.tareasmantenimiento"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                    break;

                    case RegwebConstantes.RWE_ADMIN:

                        entidadActiva =  loginInfo.getEntidadActiva();

                        if(entidadActiva == null){
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("entidad.acceso.denedado"));
                            response.sendRedirect("/regweb3/aviso");
                            return false;
                        }

                        //Plugin Generación Justificante
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE)) {
                            log.info("No existe el plugin de generación del justificante");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginjustificante"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                        }

                        //Plugin Custodia Justificante
                        if(entidadActiva.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

                            if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE)) {
                                log.info("No existe el plugin de custodia del justificante");
                                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodiajustificante"));
                                entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                            }

                        }else if(entidadActiva.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

                            if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_ARXIU_JUSTIFICANTE)) {
                                log.info("No existe el plugin de custodia del justificante");
                                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodiajustificante"));
                                entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                            }
                        }

                        // Plugin Custodia
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_CUSTODIA_ANEXOS)) {
                            log.info("No existe el plugin de custodia");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.plugincustodia"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                        }
                        //Plugin Firma en Servidor
                        if (!pluginEjb.existPlugin(entidadActiva.getId(), RegwebConstantes.PLUGIN_FIRMA_SERVIDOR)) {
                            log.info("No existe el plugin de firma en servidor");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.pluginfirma"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                        }
                        // Sir ServerBAse
                        if (entidadActiva.getSir() && Configuracio.getSirServerBase() == null) {
                            log.info("Error, falta propiedad sirserverbase");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.sirserverbase"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                            response.sendRedirect("/regweb3/aviso");
                        }

                        // Dir3Caib Server
                        if (StringUtils.isEmpty(PropiedadGlobalUtil.getDir3CaibServer(entidadActiva.getId()))) {
                            log.info("La propiedad Dir3CaibServer no está definida");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibserver"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                        }
                        // Dir3Caib Username
                        if (StringUtils.isEmpty(PropiedadGlobalUtil.getDir3CaibUsername(entidadActiva.getId()))) {
                            log.info("La propiedad Dir3CaibUsername no está definida");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibusername"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                        }
                        // Dir3Caib Password
                        if (StringUtils.isEmpty(PropiedadGlobalUtil.getDir3CaibPassword(entidadActiva.getId()))) {
                            log.info("La propiedad Dir3CaibPassword no está definida");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibpassword"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
                        }

                        //Comprobamos que se haya definido un formato para el número de registro en la Entidad
                        if(StringUtils.isEmpty(entidadActiva.getNumRegistro())){
                            log.info("No hay configurado el formato del numero de registro para la Entidad activa");
                            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidad.formatoRegistro"));
                            entidadEjb.marcarEntidadMantenimiento(entidadActiva.getId(), true);
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

        }
    }

}
