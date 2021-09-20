package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      //final long start = System.currentTimeMillis();
      try {
        String url = request.getServletPath();

        HttpSession session = request.getSession();
          LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
          Rol rolActivo = loginInfo.getRolActivo();

        // Listado usuarios de la aplicación
        if(url.equals("/usuario/list")){
            if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }
        }


        return true;
    } finally {
      //log.info("Interceptor Usuarioa: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
    }
    }


}
