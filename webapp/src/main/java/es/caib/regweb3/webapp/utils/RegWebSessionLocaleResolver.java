package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
/**
 * 
 * @author anadal
 *
 */
@Component
public class RegWebSessionLocaleResolver extends SessionLocaleResolver {

  protected final Logger log = Logger.getLogger(getClass());

  @Override
  protected Locale determineDefaultLocale(HttpServletRequest request) {

    try {
      HttpSession session = request.getSession();
      LoginInfo loginInfo = (LoginInfo) session.getAttribute(RegwebConstantes.SESSION_LOGIN_INFO);

      String idioma = null;
      if(loginInfo!=null){
        Usuario usuario = loginInfo.getUsuarioAutenticado();
        if (usuario != null && usuario.getIdioma() != null) {
          idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(usuario.getIdioma());
        }
      }

        
      if (idioma == null) {
        idioma = Configuracio.getDefaultLanguage();
      }
          
      Locale loc = new Locale(idioma);
      LocaleContextHolder.setLocale(loc);
      try {
        this.setLocale(request, null, loc);
      } catch(Exception e) {
         WebUtils.setSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME, loc);
      }
      return loc;
    } catch(Exception e) {
      log.error(e.getMessage(), e);
      return super.determineDefaultLocale(request);  
    }
    

  }
  
  
}
