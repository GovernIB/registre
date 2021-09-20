package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Organismo;
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
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * Interceptor para los Informes
 *
 * @author jpernia
 * Date: 5/12/14
 */
public class InformeInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();
            List<Organismo> organismosResponsable = loginInfo.getOrganismosResponsable();


            // Comprobamos que el usuario dispone del Rol RWE_ADMIN
            if(url.equals("/informe/registroLopd")||url.equals("/informe/usuarioLopd")) {
                if (!(organismosResponsable != null || rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Comprobamos que el usuario dispone del RWE_ADMIN
            if(url.equals("/informe/indicadores")) {
                if (!(organismosResponsable != null || rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Comprobamos que el usuario dispone del RWE_ADMIN o RWE_USUARI
            if(url.equals("/informe/registrosOrganismo")) {
                if (!(rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN) || rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }

            // Informe Registro Lopd
            if(url.contains("informeRegistroLopd")){

                if (!(organismosResponsable != null || rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }

            }

            return true;
        } finally {
            //log.info("Interceptor Informe: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}