package es.caib.regweb3.persistence.ejb;

/**
 *
 * @author anadal
 *
 */
public interface AbstractTimerLocal {

    void startScheduler();

    void stopScheduler();

    void executeTask();
}
