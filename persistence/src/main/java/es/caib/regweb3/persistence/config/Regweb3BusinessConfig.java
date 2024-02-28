package es.caib.regweb3.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@ComponentScan(value = {"es.caib.regweb3.persistence","es.gob.ad.registros.sir"})
public class Regweb3BusinessConfig {

    protected static final Logger log = LoggerFactory.getLogger(Regweb3BusinessConfig.class);

    @Bean(name = "interSessionFactory")
    public LocalSessionFactoryBean sessionFactory() throws NamingException {
        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
        localSessionFactoryBean.setDataSource(dataSourceLibsir());
        localSessionFactoryBean.setPackagesToScan("es.gob.ad.registros.sir");
        localSessionFactoryBean.setHibernateProperties(hibernateProperties());

        return localSessionFactoryBean;
    }

    @Bean
    public DataSource dataSourceLibsir() throws NamingException {
        return (DataSource) new JndiTemplate().lookup("java:jboss/datasources/libSirDS");

    }

    @Bean(name="transactionManagerInter")
    public HibernateTransactionManager transactionManager() throws NamingException {

        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());

        return transactionManager;
    }

    @Bean
    public TaskScheduler threadPoolTaskScheduler() {

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("LibSirRegWeb3ThreadPoolTaskScheduler");

        return threadPoolTaskScheduler;
    }


    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
     //   hibernateProperties.setProperty("hibernate.show_sql", "true");

        return hibernateProperties;
    }

}
