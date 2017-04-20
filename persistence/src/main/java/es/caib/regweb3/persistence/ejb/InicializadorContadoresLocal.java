package es.caib.regweb3.persistence.ejb;

import javax.ejb.Local;

/**
 * Created by mgonzalez on 17/03/2017.
 */
@Local
public interface InicializadorContadoresLocal {

    public void createTimer();

    public void clearTimers();

}
