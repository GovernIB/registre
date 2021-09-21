package es.caib.regweb3.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"es.caib.regweb3.persistence"})
public class Regweb3BusinessConfig {

    protected static final Logger log = LoggerFactory.getLogger(Regweb3BusinessConfig.class);

}
