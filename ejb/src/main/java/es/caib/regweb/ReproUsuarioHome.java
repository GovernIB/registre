package es.caib.regweb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Interfície home per accedir al bean que gestiona el registre de repros de usuario
 * @author  VHERRERA, basado en el código FJMARTINEZ
 * @version 1.0
 */

public interface ReproUsuarioHome extends EJBHome {
	
	public ReproUsuario create() throws CreateException, RemoteException;

}
