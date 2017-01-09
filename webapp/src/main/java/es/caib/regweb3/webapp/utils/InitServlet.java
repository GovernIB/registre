package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.persistence.utils.DataBaseUtils;
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

    // Sistema de Traduccions LOGIC
    // TODO Moure a persistence
    try {
      Class.forName(I18NLogicUtils.class.getName());
    } catch (Throwable th) {
      log.error("Error inicialitzant el sistema de traduccions logic: " + th.getMessage(), th);
    }
    
    // Inicialitzar Like de BBDD
    //TODO pendiente de refactorizar al mes si nadie se queja de comportamiento raro entre dialectos.
    try {
      String dialect = Configuracio.getHibernateDialect();
      if (dialect.indexOf("Oracle") != -1) {
        log.info("Setting Oracle Like Manager.");
        DataBaseUtils.setLikeManager(new DataBaseUtils.OracleLike());
      } else {

        if (dialect.indexOf("PostgreSQL") != -1) {
          log.info("Setting PostgreSQL Like Manager.");
          DataBaseUtils.setLikeManager(new DataBaseUtils.PostgreSQLLike());
        } else {
          log.info("Setting Default Like Manager.");
          DataBaseUtils.setLikeManager(new DataBaseUtils.DefaultLike());
        }
      }
    } catch(Throwable th) {
      log.error("Error desconegut establint LikeManager " + th.getMessage(), th);
    }

    // Mostrar Versió
    String ver = Versio.VERSIO + (Configuracio.isCAIB()?"-caib" : "");
    try {
      log.info("RegWeb3 Version: " + ver);
    } catch (Throwable e) {
      System.out.println("RegWeb3 Version: " + ver);
    }

  }

}
