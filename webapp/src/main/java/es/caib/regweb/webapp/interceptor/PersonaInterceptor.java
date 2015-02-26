package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Rol;
import es.caib.regweb.persistence.ejb.PermisoLibroUsuarioLocal;
import es.caib.regweb.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Personas
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class PersonaInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());


    @EJB(mappedName = "regweb/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
     //long start = System.currentTimeMillis();
      try {
        //String url = request.getServletPath();

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        //Usuario usuarioAutenticado = (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone del una EntidadActiva
        if(entidadActiva == null){
            log.info("No existe una EntidadActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidadActiva"));
            response.sendRedirect("/regweb/aviso");
            return false;
        }


        return true;
    } finally {
      //log.info("Interceptor Persona: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
    }
    }

}
