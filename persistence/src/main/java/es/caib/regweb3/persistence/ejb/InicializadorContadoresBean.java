package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.quartz.impl.triggers.CronTriggerImpl;

import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.ejb.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by mgonzalez on 17/03/2017.
 */

@Stateless(name = "InicializadoresContadoresEJB")
@SecurityDomain("seycon")
@RunAs("RWE_SUPERADMIN")
public class InicializadorContadoresBean implements InicializadorContadoresLocal {

    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    protected final Logger log = Logger.getLogger(getClass());


    private static final String NAME_TIMER = "InitContadoresTimer";

    @Resource
    private SessionContext context;



    @Override
    public void createTimer() {
        try {
            Date nextExecution = nextExecution();

            if (nextExecution != null) {
                log.info("Primera inicialización de contadores será " + nextExecution);
            } else {
                log.info("inicialización de contadores desactivada");
            }

        } catch (ParseException e) {
            log.error("Error creando inicializador: " + e.getMessage(), e);
        }

    }

    @Timeout
    @TransactionTimeout(value = 18000)
    public void timeOutHandler(Timer timer){
        try {
            long timeRemaining = timer.getTimeRemaining();

            timer.cancel();

            removeTimer(NAME_TIMER);

            nextExecution();

            // Si han passat més de 30segons de l'hora pravista d'execució
            // llavors no l'executam.
            if (timeRemaining > -30000) {
                inicializar();
            } else {
                log.warn("Timer programado para "
                        + new Date(System.currentTimeMillis() + timeRemaining)
                        + " no se ejecutará.");
            }

        } catch (Throwable e) {
            log.error("Error inicializando: " +e.getMessage(), e);
        }

    }

    protected Date nextExecution() throws ParseException {

        String cronExpression = PropiedadGlobalUtil.getCronExpressionInicializarContadores();
        Date currTime = new Date();
        CronTriggerImpl tr = new CronTriggerImpl();
        tr.setCronExpression(cronExpression);
        Date nextFireAt = tr.getFireTimeAfter(currTime);

        TimerService timerService = context.getTimerService();

        Timer timer2 = timerService.createTimer(nextFireAt, NAME_TIMER);

        if (log.isDebugEnabled()) {
            log.debug("Reference time: " + currTime);
            log.debug("Next fire after reference time: " + nextFireAt);
            log.debug("timeoutHandler : " + timer2.getInfo());
        }
        return nextFireAt;


    }

    protected void removeTimer(String name) {
        TimerService timerService = context.getTimerService();
        for (Object obj : timerService.getTimers()) {
            javax.ejb.Timer timer = (javax.ejb.Timer) obj;
            String scheduled = (String) timer.getInfo();
            //System.out.println("-> Timer Found : " + scheduled);
            if (scheduled.equals(name)) {
                log.info("Removing old timer : " + scheduled + "(" + timer.getNextTimeout() + ")");
                timer.cancel();
            }
        }
    }

    @Override
    public void clearTimers() {
        removeTimer(NAME_TIMER);
    }

    @Override
    public void inicializar() {
        try{

            List<Entidad> entidades = entidadEjb.getAll();
            for(Entidad entidad: entidades) {
                libroEjb.reiniciarContadoresEntidadTask(entidad.getId());
            }

            log.info("Ejecutado reiniciarContadoresEntidad");
        } catch (Throwable e) {
          log.error("Error Inicializando contadores entidad ...", e);
        }



    }
}
