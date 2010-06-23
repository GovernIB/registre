package es.caib.regweb;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * Interfície home per accedir al bean que encapsula funcions massa genèriques per tenir el seu probi EJB.
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface RegistroSalidaHome extends EJBHome  {
	RegistroSalida create() throws RemoteException, CreateException;
}