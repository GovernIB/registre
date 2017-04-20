package es.caib.regweb3.persistence.ejb;

/**
 *
 * @author anadal
 *
 */
public interface AbstractTimerLocal {

    public void startScheduler();

    public void stopScheduler();

    public void executeTask();
}
