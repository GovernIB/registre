package es.caib.regweb3.webapp.scheduler;

/**
 * Created by mgonzalez on 14/12/2017.
 */

import es.caib.regweb3.persistence.ejb.SchedulerLocal;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;

/**
 * Created by earrivi on 06/06/2017.
 */
@Service
public class ContadoresEntidadScheduler {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/SchedulerEJB/local")
    private SchedulerLocal schedulerEjb;

    @Scheduled(cron = "0 0 0 1 1 ?")
    public void inicializarContadores(){

        try {
            schedulerEjb.reiniciarContadoresEntidad();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
