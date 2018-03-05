package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.EntidadLocal;
import es.caib.regweb3.persistence.ejb.PendienteLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.utils.UsuarioService;
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
 *
 * Interceptor que se ejecuta en cualquier petición de la aplicación.
 *
 * @author earrivi
 * Date: 6/02/14
 * 
 * TODO S'ha de moure la funcionalitat d'aquesta classe 
 * a es.caib.regweb3.webapp.securit.AuthenticationSuccessListener
 * 
 */
public class InicioInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private UsuarioService usuarioService;

    @EJB(mappedName = "regweb3/UsuarioEJB/local")
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = "regweb3/PendienteEJB/local")
    private PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    private EntidadLocal entidadEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
     // log.info(" ++++++++++++++++++++ ENTRA InicioInterceptor ++++++++++++++");
      //long start = System.currentTimeMillis();
      try {

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        if(request.getRequestURI().equals("/regweb3/noAutorizado") ){
          return true;
        }

        if(request.getUserPrincipal()!= null) { // Si hay un usuario autenticado correctamente

            // Buscamos en la sesion si hay algún usuario autenticado
            Usuario usuarioAutenticado = (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);

            if(usuarioAutenticado == null) { // Si la sesión es nueva

                // Obtenemos el usuario autenticado de la bbdd mediante el login
                String identificador = request.getUserPrincipal().getName();
                Usuario usuario = usuarioEjb.findByIdentificador(identificador);

                if(usuario != null){ // El usuario existe en REGWEB3

                    // Si el usuario es de tipo aplicación, no puede acceder.
                    if(usuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)){
                        log.info(usuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB3.");
                        response.sendRedirect("/regweb3/noAutorizado");
                        return false;
                    }

                    // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                    usuarioService.configurarUsuario(usuario,request);

                }else{ // El usuario NO existe en REGWEB3

                    log.info("El usuario "+ identificador +" no existe en REGWEB3, lo crearemos.");

                    Usuario nuevoUsuario = usuarioService.crearUsuario(identificador);

                    if(nuevoUsuario != null){
                        log.info("Usuario creado: " + nuevoUsuario.getNombreCompleto());

                        // Si el usuario es de tipo aplicación, no puede acceder.
                        if(nuevoUsuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)){
                            log.info(nuevoUsuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB3.");
                            response.sendRedirect("/regweb3/noAutorizado");
                            return false;
                        }

                        // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                        usuarioService.configurarUsuario(nuevoUsuario,request);
                    }else{

                        log.info("No está autorizado: " + identificador);
                        response.sendRedirect("/regweb3/noAutorizado");
                        return false;
                    }

                }
            }

            // Validamos las propiedades de dir3 para poder atacar a dir3caib
            if(request.getRequestURI().equals("/regweb3/dir3/datosCatalogo")){
                if(PropiedadGlobalUtil.getDir3CaibServer() == null || PropiedadGlobalUtil.getDir3CaibServer().isEmpty()){
                    log.info("La propiedad Dir3CaibServer no está definida");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibserver"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
                if(PropiedadGlobalUtil.getDir3CaibUsername() == null || PropiedadGlobalUtil.getDir3CaibUsername().isEmpty()){
                    log.info("La propiedad Dir3CaibUsername no está definida");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibusername"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
                if(PropiedadGlobalUtil.getDir3CaibPassword() == null || PropiedadGlobalUtil.getDir3CaibPassword().isEmpty()){
                    log.info("La propiedad Dir3CaibPassword no está definida");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibpassword"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

            }

            //Validamos variable es.caib.regweb3.archivos.path
            //comprobar variable archivos path
            if(request.getRequestURI().equals("/regweb3/configuracion/editar")&& FileSystemManager.getArchivosPath() == null && rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)) {
                log.info("Error, editar entidad");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.archivospath"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }


        }else{
            response.sendRedirect("/regweb3/noAutorizado");
            return false;
        }

        return true;
      } catch(I18NException i18ne) {
        throw new Exception(I18NUtils.getMessage(i18ne), i18ne);
        
      } finally {
        // log.info("Interceptor Inicio: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
      }
    }

}
