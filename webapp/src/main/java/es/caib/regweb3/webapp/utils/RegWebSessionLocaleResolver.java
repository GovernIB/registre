package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
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

      Usuario usuario = (Usuario)request.getSession().getAttribute(RegwebConstantes.SESSION_USUARIO);
            

      String idioma = null;
      if (usuario != null && usuario.getIdioma() != null) {
        idioma = RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(usuario.getIdioma());
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
