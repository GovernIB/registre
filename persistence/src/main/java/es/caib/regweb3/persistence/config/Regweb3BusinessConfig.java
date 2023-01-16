package es.caib.regweb3.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@ComponentScan(value = {"es.caib.regweb3.persistence", "es.gob.ad.registros.sir"})
public class Regweb3BusinessConfig {

    protected static final Logger log = LoggerFactory.getLogger(Regweb3BusinessConfig.class);

    @Bean(name = "interSessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSource());
        localSessionFactoryBean.setAnnotatedPackages("es.gob.registros.sir.interModel");
        localSessionFactoryBean.setHibernateProperties(hibernateProperties());
        //localSessionFactoryBean.setJtaTransactionManager(jtaTransactionManager());

        return localSessionFactoryBean;
    }

    @Bean
    public DataSource dataSource() throws NamingException {
        return (DataSource) new JndiTemplate().lookup("java:jboss/datasources/libSirDS");

    }

    /*@Bean(name="transactionManagerInter")
    public JtaTransactionManager jtaTransactionManager() {
        return new JtaTransactionManager();
    }*/

    @Bean(name="transactionManagerInter")
    public HibernateTransactionManager transactionManager() throws NamingException {

        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }


    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        //hibernateProperties.setProperty("hibernate.transaction.coordinator_class", "jta");
        //hibernateProperties.setProperty("hibernate.transaction.jta.platform", "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform");
        //hibernateProperties.setProperty("hibernate.current_session_context_class", "org.hibernate.context.internal.JTASessionContext");

        //hibernateProperties.setProperty("hibernate.transaction.manager_lookup_class", "org.hibernate.transaction.JBossTransactionManagerLookup");

        return hibernateProperties;
    }

}
