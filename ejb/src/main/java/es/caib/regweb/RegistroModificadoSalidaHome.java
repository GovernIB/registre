/*
 * RegistroModificadoSalidHome.java
 *
 * Created on 6 de septiembre de 2002, 11:53
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;
/**
 * Interfície home per accedir al bean que gestiona la modificació del registre de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface RegistroModificadoSalidaHome extends EJBHome {
	
	public RegistroModificadoSalida create() throws CreateException, RemoteException;
	
}
