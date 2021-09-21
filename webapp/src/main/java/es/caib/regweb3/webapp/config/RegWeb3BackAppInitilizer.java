package es.caib.regweb3.webapp.config;

import es.caib.regweb3.persistence.config.Regweb3BusinessConfig;
import es.caib.regweb3.utils.config.Regweb3CommonsConfig;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class RegWeb3BackAppInitilizer extends AbstractAnnotationConfigDispatcherServletInitializer {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { Regweb3BusinessConfig.class, RegWeb3SecurityConfig.class, Regweb3CommonsConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { RegWeb3BackConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        try {
            ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
            String[] basenames = {
                    "missatges", // /WEB-INF/classes/
                    "logicmissatges"
            };
            ms.setDefaultEncoding("UTF-8");
            ms.setBasenames(basenames);
            I18NUtils.setMessageSource(ms);
        } catch (Throwable th) {
            log.error("Error inicialitzant el sistema de traduccions web: " + th.getMessage(), th);
        }
    }
}
