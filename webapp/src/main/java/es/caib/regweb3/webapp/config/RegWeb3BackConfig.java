package es.caib.regweb3.webapp.config;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.interceptor.*;
import es.caib.regweb3.webapp.utils.CommonsMultipartResolver;
import es.caib.regweb3.webapp.utils.RegWebSessionLocaleResolver;
import es.caib.regweb3.webapp.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;

@EnableWebMvc
@Configuration
@EnableScheduling
@ComponentScan(value = {"es.caib.regweb3.webapp"})
public class RegWeb3BackConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void init() {
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
    }

    @Bean
    public BeanNameViewResolver beanNameViewResolver(){
        BeanNameViewResolver beanNameViewResolver = new BeanNameViewResolver();
        beanNameViewResolver.setOrder(1);

        return beanNameViewResolver;
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
        internalResourceViewResolver.setSuffix(".jsp");
        internalResourceViewResolver.setOrder(2);

        return internalResourceViewResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("missatges", "logicmissatges");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/");

        registry.addResourceHandler("/font-awesome/**")
                .addResourceLocations("/font-awesome/");

        registry.addResourceHandler("/img/**")
                .addResourceLocations("/img/");

        registry.addResourceHandler("/ico/**")
                .addResourceLocations("/ico/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("/js/");

        registry.addResourceHandler("/doc/**")
                .addResourceLocations("/doc/");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.add(new MappingJackson2HttpMessageConverter());
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(inicioInterceptor())
                .excludePathPatterns("/css/**", "/font-awesome/**", "/ico/**", "/img/**", "/js/**", "/doc/**", "/anexo/guardarScan/**", "/anexo/scanwebresource/**", "/anexo/scanwebresource2/**")
                .addPathPatterns("/**");

        registry.addInterceptor(registroEntradaInterceptor()).addPathPatterns("/registroEntrada/**");
        registry.addInterceptor(registroSalidaInterceptor()).addPathPatterns("/registroSalida/**");
        registry.addInterceptor(oficioRemisionInterceptor()).addPathPatterns("/oficioRemision/**");
        registry.addInterceptor(personaInterceptor()).addPathPatterns("/persona/**");
        registry.addInterceptor(libroInterceptor()).addPathPatterns("/libro/**");
        registry.addInterceptor(usuarioInterceptor()).addPathPatterns("/usuario/**");
        registry.addInterceptor(entidadInterceptor()).addPathPatterns("/entidad/**");
        registry.addInterceptor(organismoInterceptor()).addPathPatterns("/organismo/**");
        registry.addInterceptor(registroMigradoInterceptor()).addPathPatterns("/registroMigrado/**");
        registry.addInterceptor(informeInterceptor()).addPathPatterns("/informe/**");
        registry.addInterceptor(tipoAsuntoInterceptor()).addPathPatterns("/tipoAsunto/**");
        registry.addInterceptor(dir3Interceptor()).addPathPatterns("/dir3/**");
        registry.addInterceptor(tipoDocumentalInterceptor()).addPathPatterns("/tipoDocumental/**");
        registry.addInterceptor(modeloReciboInterceptor()).addPathPatterns("/modeloRecibo/**");
        registry.addInterceptor(modeloOficioRemisionInterceptor()).addPathPatterns("/modeloOficioRemision/**");
        registry.addInterceptor(plantillaInterceptor()).addPathPatterns("/plantilla/**");
        registry.addInterceptor(adminInterceptor()).addPathPatterns("/admin/**");
        registry.addInterceptor(adminEntidadInterceptor()).addPathPatterns("/adminEntidad/**", "/eventos/**");
        registry.addInterceptor(registroSirInterceptor()).addPathPatterns("/registroSir/**");
        registry.addInterceptor(sirInterceptor()).addPathPatterns("/sir/**");
        registry.addInterceptor(integracionInterceptor()).addPathPatterns("/integracion/**");
        registry.addInterceptor(colaInterceptor()).addPathPatterns("/cola/**");
        registry.addInterceptor(notificacionInterceptor()).addPathPatterns("/notificacion/**");
        registry.addInterceptor(pluginInterceptor()).addPathPatterns("/plugin/**");
        registry.addInterceptor(propiedadGlobalInterceptor()).addPathPatterns("/propiedadGlobal/**");

        registry.addInterceptor(localeChangeInterceptor());

        super.addInterceptors(registry);
    }


    @Bean
    public RegWebLocaleChangeInterceptor localeChangeInterceptor() {

        RegWebLocaleChangeInterceptor regWebLocaleChangeInterceptor = new RegWebLocaleChangeInterceptor();
        regWebLocaleChangeInterceptor.setParamName("lang");

        return regWebLocaleChangeInterceptor;
    }

    @Bean
    public RegWebSessionLocaleResolver localeResolver() {

        RegWebSessionLocaleResolver regWebSessionLocaleResolver = new RegWebSessionLocaleResolver();
        regWebSessionLocaleResolver.setDefaultLocale(new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO));

        return regWebSessionLocaleResolver;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        return new CommonsMultipartResolver();
    }

    /*@Bean
    ConversationalSessionAttributeStore conversationalSessionAttributeStore(){

        ConversationalSessionAttributeStore conversationalSessionAttributeStore = new ConversationalSessionAttributeStore();
        conversationalSessionAttributeStore.setKeepAliveConversations(10);

        return conversationalSessionAttributeStore;
    }*/

   /* @Bean
    public ConversationIdRequestProcessor conversationIdRequestProcessor(){
        return new ConversationIdRequestProcessor();
    }*/


    /* ----------- Interceptors ----------- */

    @Bean
    InicioInterceptor inicioInterceptor() {return new InicioInterceptor();}

    @Bean
    RegistroEntradaInterceptor registroEntradaInterceptor() {return new RegistroEntradaInterceptor();}

    @Bean
    RegistroSalidaInterceptor registroSalidaInterceptor() {return new RegistroSalidaInterceptor();}

    @Bean
    OficioRemisionInterceptor oficioRemisionInterceptor() {return new OficioRemisionInterceptor();}

    @Bean
    PersonaInterceptor personaInterceptor() {return new PersonaInterceptor();}

    @Bean
    LibroInterceptor libroInterceptor() {return new LibroInterceptor();}

    @Bean
    UsuarioInterceptor usuarioInterceptor() {return new UsuarioInterceptor();}

    @Bean
    EntidadInterceptor entidadInterceptor() {return new EntidadInterceptor();}

    @Bean
    OrganismoInterceptor organismoInterceptor() {return new OrganismoInterceptor();}

    @Bean
    RegistroMigradoInterceptor registroMigradoInterceptor() {return new RegistroMigradoInterceptor();}

    @Bean
    InformeInterceptor informeInterceptor() {return new InformeInterceptor();}

    @Bean
    TipoAsuntoInterceptor tipoAsuntoInterceptor() {return new TipoAsuntoInterceptor();}

    @Bean
    Dir3Interceptor dir3Interceptor() {return new Dir3Interceptor();}

    @Bean
    TipoDocumentalInterceptor tipoDocumentalInterceptor() {return new TipoDocumentalInterceptor();}

    @Bean
    ModeloReciboInterceptor modeloReciboInterceptor() {return new ModeloReciboInterceptor();}

    @Bean
    ModeloOficioRemisionInterceptor modeloOficioRemisionInterceptor() {return new ModeloOficioRemisionInterceptor();}

    @Bean
    PlantillaInterceptor plantillaInterceptor() {return new PlantillaInterceptor();}

    @Bean
    AdminInterceptor adminInterceptor() {return new AdminInterceptor();}

    @Bean
    AdminEntidadInterceptor adminEntidadInterceptor() {return new AdminEntidadInterceptor();}

    @Bean
    RegistroSirInterceptor registroSirInterceptor() {return new RegistroSirInterceptor();}

    @Bean
    SirInterceptor sirInterceptor() {return new SirInterceptor();}

    @Bean
    IntegracionInterceptor integracionInterceptor() {return new IntegracionInterceptor();}

    @Bean
    ColaInterceptor colaInterceptor() {return new ColaInterceptor();}

    @Bean
    NotificacionInterceptor notificacionInterceptor() {return new NotificacionInterceptor();}

    @Bean
    PluginInterceptor pluginInterceptor() {return new PluginInterceptor();}

    @Bean
    PropiedadGlobalInterceptor propiedadGlobalInterceptor() {return new PropiedadGlobalInterceptor();}


    /* Views */

    @Bean
    IndicadoresPdf indicadoresPdf() {return new IndicadoresPdf();}

    @Bean
    SelloPdfView selloPdfView() {return new SelloPdfView();}

    @Bean
    ReciboRtfView reciboRtfView() {return new ReciboRtfView();}

    @Bean
    OficioRemisionRtfView oficioRemisionRtfView() {return new OficioRemisionRtfView();}

    @Bean
    RegistrosOrganismoPdf registrosOrganismoPdf() {return new RegistrosOrganismoPdf();}

    @Bean
    RegistrosOrganismoExcel registrosOrganismoExcel() {return new RegistrosOrganismoExcel();}

    @Bean
    IndicadoresExcel indicadoresExcel() {return new IndicadoresExcel();}

    @Bean
    IndicadoresOficinaPdf indicadoresOficinaPdf() {return new IndicadoresOficinaPdf();}

    @Bean
    IndicadoresOficinaExcel indicadoresOficinaExcel() {return new IndicadoresOficinaExcel();}

    @Bean
    ExportarPersonasExcel exportarPersonasExcel() {return new ExportarPersonasExcel();}

    @Bean
    ExportarUsuariosExcel exportarUsuariosExcel() {return new ExportarUsuariosExcel();}

}
