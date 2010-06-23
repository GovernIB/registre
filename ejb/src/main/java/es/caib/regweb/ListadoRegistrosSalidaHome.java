package es.caib.regweb;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

/**
 * Interfície home per accedir al bean que genera els llistats de registres d'entrada
 * @author  FJMARTINEZ
 * @version 1.0
 */


public interface ListadoRegistrosSalidaHome extends EJBHome  {
	ListadoRegistrosSalida create() throws RemoteException, CreateException;
}