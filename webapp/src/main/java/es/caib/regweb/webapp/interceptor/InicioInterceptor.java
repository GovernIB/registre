package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Usuario;
import es.caib.regweb.persistence.ejb.EntidadLocal;
import es.caib.regweb.persistence.ejb.PendienteLocal;
import es.caib.regweb.persistence.ejb.UsuarioLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.UsuarioService;
import org.apache.log4j.Logger;
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
 */
public class InicioInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private UsuarioService usuarioService;

    @EJB(mappedName = "regweb/UsuarioEJB/local")
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = "regweb/PendienteEJB/local")
    private PendienteLocal pendienteEjb;

    @EJB(mappedName = "regweb/EntidadEJB/local")
    private EntidadLocal entidadEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
      //long start = System.currentTimeMillis();
      try {

        HttpSession session = request.getSession();
        //Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        //Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        if(request.getRequestURI().equals("/regweb/noAutorizado") ){
          return true;
        }

        if(request.getUserPrincipal()!= null) { // Si hay un usuario autenticado correctamente

            // Buscamos en la sesion si hay algún usuario autenticado
            Usuario usuarioAutenticado = (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);

            if(usuarioAutenticado == null) { // Si la sesión es nueva

                // Obtenemos el usuario autenticado de la bbdd mediante el login
                String identificador = request.getUserPrincipal().getName();
                Usuario usuario = usuarioEjb.findByIdentificador(identificador);

                if(usuario != null){ // El usuario existe en REGWEB

                    // Si el usuario es de tipo aplicación, no puede acceder.
                    if(usuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)){
                        log.info(usuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB.");
                        response.sendRedirect("/regweb/noAutorizado");
                        return false;
                    }

                    // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                    usuarioService.configurarUsuario(usuario,request);

                }else{ // El usuario NO existe en REGWEB

                    log.info("El usuario no existe en REGWEB, lo crearemos.");

                    Usuario nuevoUsuario = usuarioService.crearUsuario(identificador);

                    if(nuevoUsuario != null){
                        log.info("Usuario creado: " + nuevoUsuario.getNombreCompleto());

                        // Si el usuario es de tipo aplicación, no puede acceder.
                        if(nuevoUsuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)){
                            log.info(nuevoUsuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB.");
                            response.sendRedirect("/regweb/noAutorizado");
                            return false;
                        }

                        // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                        usuarioService.configurarUsuario(nuevoUsuario,request);
                    }else{

                        log.info("No está autorizado: " + identificador);
                        response.sendRedirect("/regweb/noAutorizado");
                        return false;
                    }

                }
            }else{ // Si la sesion ya existe y el usuario está autenticado
               /* if(request.getRequestURI().equals("/regweb/aviso")||
                   *//*request.getRequestURI().equals("/regweb/entidad/pendientesprocesar") ||*//*
                   request.getRequestURI().equals("/regweb/rol/1") ||
                   request.getRequestURI().equals("/regweb/rol/2") ||
                   request.getRequestURI().equals("/regweb/rol/3") *//*||*//*
                   *//*request.getRequestURI().equals("/regweb/error/404")*//*) {
                   return true;
                }*/

                //Comprobamos que no haya cambios de organigrama pendientes de procesar
               /* List<Pendiente> orgPendientesProcesar = pendienteEjb.findPendientesProcesar();
                if(orgPendientesProcesar.size()>0){
                  *//* Si tienen los roles RWE_USUARI o RWE_SUPERADMIN no puede gestionar los cambios de estructura *//*
                  if(rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI) || rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                    log.info("Es operador o superadmin y quedan pendientes de procesar" );
                   *//* response.sendRedirect("/regweb/accesodenedado.html");
                    return false;*//*
                    Mensaje.saveMessageAviso(request, getMessage("aviso.pendientes.noautorizado"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                  }
                  // Si es administrador de Entidad
                  if(rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN) && entidadEjb.esAutorizado(entidadActiva.getId(), usuarioAutenticado.getId())){
                      log.info("usuario es propietario");
                      response.sendRedirect("/regweb/entidad/pendientesprocesar");
                      return false;
                  }
                }*/

            }

        }else{
            response.sendRedirect("/regweb/noAutorizado");
            return false;
        }

        return true;
      } finally {
        // log.info("Interceptor Inicio: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
      }
    }

}
