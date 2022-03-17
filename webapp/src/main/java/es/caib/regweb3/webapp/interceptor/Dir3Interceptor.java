package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Created by Fundació BIT.
 *
 * Interceptor para la gestión de Dir3
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class Dir3Interceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

            String url = request.getServletPath();

            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();

            // Cualquier accion con Dir3
            if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Validamos las propiedades de dir3 para poder atacar a dir3caib
            if (url.equals("/datosCatalogo")) {
                if (StringUtils.isEmpty(loginInfo.getDir3Caib().getServer())) {
                    log.info("La propiedad Dir3CaibServer no está definida");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibserver"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
                if (StringUtils.isEmpty(loginInfo.getDir3Caib().getUser())) {
                    log.info("La propiedad Dir3CaibUsername no está definida");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibusername"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
                if (StringUtils.isEmpty(loginInfo.getDir3Caib().getPassword())) {
                    log.info("La propiedad Dir3CaibPassword no está definida");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.propiedad.dir3caibpassword"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

            }

            return true;
        } finally {
            //log.info("Interceptor Dir3: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}
