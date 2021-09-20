package es.caib.regweb3.webapp.interceptor;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LocaleChangeInterceptor específico para RegWEb
 * @author earrivi
 */
public class RegWebLocaleChangeInterceptor extends LocaleChangeInterceptor{

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    public UsuarioLocal usuarioEjb;

    protected final Logger log = LoggerFactory.getLogger(getClass());


    public RegWebLocaleChangeInterceptor(){
        super();
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {


        String newLocale = request.getParameter(getParamName());

        LoginInfo loginInfo = (LoginInfo) request.getSession().getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);

        if(loginInfo != null){

            Usuario usuario = loginInfo.getUsuarioAutenticado();

            if(newLocale != null && usuario != null) {

                Long idioma  = RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(newLocale);
                usuario.setIdioma(idioma);
                usuarioEjb.merge(usuario);

            }
        }
    }
}
