package es.caib.regweb3.utils.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan(value = {"es.caib.regweb3.utils"})
@PropertySources({
        @PropertySource("file:${es.caib.regweb3.properties}"),
        @PropertySource("file:${es.caib.regweb3.system.properties}")
})
public class Regweb3CommonsConfig {
}

