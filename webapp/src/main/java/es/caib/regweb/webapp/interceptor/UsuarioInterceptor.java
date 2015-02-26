package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Rol;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Usuarios
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class UsuarioInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      //final long start = System.currentTimeMillis();
      try {
        String url = request.getServletPath();

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        // Listado usuarios de la aplicación
        if(url.equals("/usuario/list")){
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }
        }


        return true;
    } finally {
      //log.info("Interceptor Usuarioa: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
    }
    }


}
