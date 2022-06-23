package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Descarga;
import es.caib.regweb3.model.Rol;
import es.caib.regweb3.persistence.ejb.DescargaLocal;
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
 *
 * Interceptor para la gestión de Organismos
 *
 * @author earrivi
 * Date: 3/03/15
 */
public class  OrganismoInterceptor extends HandlerInterceptorAdapter {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/DescargaEJB/local")
    private DescargaLocal descargaEjb;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {

           // log.info("Entro en Organismo Interceptor");
            HttpSession session = request.getSession();
            LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);
            Rol rolActivo = loginInfo.getRolActivo();

            if(rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {
                // Comprobamos que el catalogo ha sido sincronizado al menos una vez
                Descarga catalogo = descargaEjb.findByTipo(RegwebConstantes.CATALOGO);
                if (catalogo == null) {
                    Mensaje.saveMessageAviso(request, I18NUtils.tradueix("catalogoDir3.catalogo.vacio"));
                    response.sendRedirect("/regweb3/aviso");
                    return false;
                }
            }else if (!rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)){
                log.info("Error de rol");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.rol"));
                response.sendRedirect("/regweb3/aviso");
                return false;
            }

            return true;

        } finally {
            //log.info("Interceptor Organismo: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        }

    }


}
