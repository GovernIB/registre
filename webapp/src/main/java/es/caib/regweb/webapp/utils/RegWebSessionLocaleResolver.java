package es.caib.regweb.webapp.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import es.caib.regweb.model.Usuario;
import es.caib.regweb.utils.Configuracio;
import es.caib.regweb.utils.RegwebConstantes;
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

      Usuario usuario = (Usuario)request.getSession().getAttribute(RegwebConstantes.SESSION_USUARIO);
            

      String idioma;
      if (usuario == null || usuario.getIdioma() == null) {
        idioma = Configuracio.getDefaultLanguage();
      } else {
        idioma = usuario.getIdioma().getLang();
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
