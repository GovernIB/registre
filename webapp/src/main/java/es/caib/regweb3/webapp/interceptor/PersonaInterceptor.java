package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Created by Fundació BIT.
 * <p>
 * Interceptor para la gestión de Personas
 *
 * @author earrivi
 * Date: 5/06/14
 */
public class PersonaInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());


    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //long start = System.currentTimeMillis();
        try {
            //String url = request.getServletPath();

            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();
            Entidad entidadActiva = loginInfo.getEntidadActiva();

            // Comprobamos que el usuario dispone del Rol RWE_USUARI
            if (rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)) {
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            // Comprobamos que el usuario dispone del una EntidadActiva
            if (entidadActiva == null) {
                log.info("No existe una EntidadActiva");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.entidadActiva"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }


            return true;
        } finally {
            //log.info("Interceptor Persona: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }
    }

}
