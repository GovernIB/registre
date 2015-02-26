package es.caib.regweb.webapp.interceptor;

import es.caib.regweb.model.Libro;
import es.caib.regweb.model.Rol;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
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

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            String url = request.getServletPath();
            HttpSession session = request.getSession();
            Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);
            List<Libro> librosAdm = (List<Libro>) session.getAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS);

            if(librosAdm != null){
                if(librosAdm.size() == 0){
                    librosAdm = null;
                }
            }

            // Comprobamos que el usuario dispone del Rol RWE_USUARI o RWE_ADMIN
            if(url.equals("/informe/registroLopd")||url.equals("/informe/usuarioLopd")) {
                if (!(librosAdm != null || rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN))) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }

            // Comprobamos que el usuario dispone del RWE_ADMIN
            if(url.equals("/informe/indicadores")||url.equals("/informe/libroRegistro")) {
                if (!rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)) {
                    log.info("Error de rol");
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                    response.sendRedirect("/regweb/aviso");
                    return false;
                }
            }


            return true;
        } finally {
            //log.info("Interceptor PreRegistro: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }


}