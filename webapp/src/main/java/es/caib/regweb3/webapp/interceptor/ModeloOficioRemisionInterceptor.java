package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.utils.RegwebConstantes;
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
 * Interceptor para la gestión de Modelo Oficio Remision
 *
 * @author jpernia
 * Date: 31/12/15
 */
public class ModeloOficioRemisionInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();

            // Cualquier accion con TipoAsunto
            if(!rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            //comprobar variable archivos path
            if(PropiedadGlobalUtil.getArchivosPath()==null){
                log.info("Error, no esta definida la variable archivos path");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.archivospath"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            return true;
        } finally {
            //log.info("Interceptor ModeloOficioRemision: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }

}