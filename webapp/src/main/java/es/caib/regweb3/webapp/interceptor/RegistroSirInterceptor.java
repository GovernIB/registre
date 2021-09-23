package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.utils.Propiedades;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.security.LoginInfo;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Fundació BIT.
 * <p>
 * Interceptor para el RegistroSir
 *
 * @author jpernia
 * Date: 5/12/14
 */
public class RegistroSirInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Propiedades propiedades;

    @EJB(mappedName = UsuarioEntidadLocal.JNDI_NAME)
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        HttpSession session = request.getSession();
        LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
        Rol rolActivo = loginInfo.getRolActivo();
        Oficina oficinaActiva = loginInfo.getOficinaActiva();


        if (rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
            return true;
        }

        // Comprobamos que el usuario dispone de una OficinaActiva
        if (oficinaActiva == null) {
            log.info("No existe una OficinaActiva");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficinaActiva"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos que la oficinaActiva esté integrada en SIR
        if (!oficinaActiva.getSirEnvio() && !oficinaActiva.getSirRecepcion()) {
            log.info("La oficinaActiva no está integrada en SIR");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficinaActiva.sir"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        // Comprobamos que el usuario dispone del Rol RWE_USUARI
        if (!rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)) {
            log.info("Error de rol");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        //comprobar variable archivos path
        if (StringUtils.isEmpty(propiedades.getArchivosPath())) {
            log.info("Error, no esta definida la variable archivos path");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.archivospath"));
            response.sendRedirect("/regweb3/aviso");
            return false;
        }

        return true;

    }


}