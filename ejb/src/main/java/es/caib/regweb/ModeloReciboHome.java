package es.caib.regweb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Interfície home per accedir al bean que gestiona els models d'ofici
 * @author  AROGEL
 * @version 1.0
 */

public interface ModeloReciboHome extends EJBHome {
	
	public ModeloRecibo create() throws CreateException, RemoteException;

}
