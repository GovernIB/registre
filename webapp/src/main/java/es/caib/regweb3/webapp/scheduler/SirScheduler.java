package es.caib.regweb3.webapp.scheduler;

import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import org.apache.log4j.Logger;

import javax.ejb.EJB;


/**
 * Created by earrivi on 06/06/2017.
 */
//@Service
public class SirScheduler {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SchedulerEJB/local")
    private SchedulerLocal schedulerEjb;



   // @Scheduled(cron = "0 0 * * * *") // {0 0 * * * * Cada hora, cada día} -  {*/60 * * * * * cada 60 secs }
    public void reintentarEnvioSir(){

        try {
            schedulerEjb.reintentarEnviosSinConfirmacion();
            schedulerEjb.reintentarEnviosConError();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
