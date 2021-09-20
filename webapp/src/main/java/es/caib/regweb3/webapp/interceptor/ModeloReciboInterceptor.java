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
 * Interceptor para la gestión de Modelo Recibo
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class ModeloReciboInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();

            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();

            // Imprimir Recibo
            if((url.contains("imprimir"))) {
                if (!rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }else{  // Cualquier accion con Modelo Recibo que no sea Imprimir
                if (!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            return true;
        } finally {
            //log.info("Interceptor ModeloRecibo: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}