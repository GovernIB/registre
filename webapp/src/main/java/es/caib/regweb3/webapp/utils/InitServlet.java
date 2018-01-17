package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Versio;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Properties;
import java.util.Set;


/**
 * Servlet emprat per inicialitzar el Back
 * 
 * @author anadal
 * 
 */
@Component
public class InitServlet extends HttpServlet {

  protected final Logger log = Logger.getLogger(getClass());
  

  @Override
  public void init(ServletConfig config) throws ServletException {
    
    // Sistema de Traduccions WEB
    try {
      ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
      String[] basenames = { 
          "es/caib/regweb3/webapp/missatges", // /WEB-INF/classes/
          "logicmissatges"
         };
      ms.setDefaultEncoding("UTF-8");
      ms.setBasenames(basenames);
      I18NUtils.setMessageSource(ms);
    } catch (Throwable th) {
      log.error("Error inicialitzant el sistema de traduccions web: " + th.getMessage(), th);
    }

    // Inicializar Contadores Libros de la entidad especificada
    /*try {

      InicializadorContadoresLocal inicializadorContadores;
      inicializadorContadores = (InicializadorContadoresLocal) new InitialContext()
              .lookup("regweb3/InicializadoresContadoresEJB/local");

      inicializadorContadores.clearTimers();

      inicializadorContadores.createTimer();
    } catch (Throwable th) {
      log.error("Error desconegut inicialitzant Inicializador Contadores: " + th.getMessage(), th);
    }*/

    // Sistema de Traduccions LOGIC
    // TODO Moure a persistence
    try {
      Class.forName(I18NLogicUtils.class.getName());
    } catch (Throwable th) {
      log.error("Error inicialitzant el sistema de traduccions logic: " + th.getMessage(), th);
    }


    // Mostrar Versió
    String ver = Versio.VERSIO + (Configuracio.isCAIB()?"-caib" : "");
    try {
      log.info("RegWeb3 Version: " + ver);
    } catch (Throwable e) {
      System.out.println("RegWeb3 Version: " + ver);
    }
    
    
    if (log.isDebugEnabled()) {
      Properties prop = System.getProperties();
      
      Set<Object> keys = prop.keySet();
      StringBuilder str = new StringBuilder("\r\n");
      for (Object keyO : keys) {
         String key = (String)keyO;
         if (key.startsWith("es.caib.regweb3.")) {
            str.append(key + " => " + prop.getProperty(key) + "\r\n");
         }
      }
      
      log.debug("RegWeb3 Properties: " + str.toString());
    }

  }

}
