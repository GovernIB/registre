package es.caib.regweb3.webapp.config;

import es.caib.regweb3.persistence.config.Regweb3BusinessConfig;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Versio;
import es.caib.regweb3.utils.config.Regweb3CommonsConfig;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
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
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] {characterEncodingFilter};
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

        // Sistema de Traduccions LOGIC
        // TODO Moure a persistence
        try {
            Class.forName(I18NLogicUtils.class.getName());
        } catch (Throwable th) {
            log.error("Error inicialitzant el sistema de traduccions logic: " + th.getMessage(), th);
        }

        // Mostrar Versió
        String ver = Versio.VERSIO + (Configuracio.isCAIB() ? "-caib" : "");
        try {
            log.info("REGWEB3 Version: " + ver);
        } catch (Throwable e) {
            System.out.println("REGWEB3 Version: " + ver);
        }
    }
}
