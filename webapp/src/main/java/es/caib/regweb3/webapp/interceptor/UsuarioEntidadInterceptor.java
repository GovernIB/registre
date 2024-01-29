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
 * Created byDGSMAD
 * <p>
 * Interceptor para la gestión de UsuariosEntidad
 *
 * @author earrivi
 * Date: 12/01/23
 */
public class UsuarioEntidadInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String url = request.getServletPath();

        HttpSession session = request.getSession();
        LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
        Rol rolActivo = loginInfo.getRolActivo();

        // Listado usuarios de la aplicación
        if (url.equals("/usuarioEntidad/list") && (!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN))) {

            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb3/aviso");
            return false;

        }

        return true;

    }
}
