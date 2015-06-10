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
 * Interceptor para la gestión de Modelo Recibo
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class ModeloReciboInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

            // Cualquier accion con Modelo Recibo
            if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb/aviso");
                return false;
            }

            return true;
        } finally {
            //log.info("Interceptor ModeloRecibo: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}