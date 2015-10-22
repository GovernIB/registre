package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Fundació BIT.
 * Interceptor común para Registros
 * @author earrivi
 * Date: 21/10/2015
 */
public class AbstractRegistroCommonInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);


        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if(!rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone de una EntidadActiva
        if(entidadActiva == null){
            log.info("No existe una EntidadActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidadActiva"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone de una OficinaActiva
        if(oficinaActiva == null){
            log.info("No existe una OficinaActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficinaActiva"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        return true;

    }

}
