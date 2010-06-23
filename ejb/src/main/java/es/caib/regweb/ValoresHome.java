/*
 * ValoresHome.java
 *
 * Created on 6 de septiembre de 2002, 11:53
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;

/**
 * Interfície home per accedir al bean que encapsula funcions massa genèriques per tenir el seu probi EJB.
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface ValoresHome extends EJBHome {
	
	public Valores create() throws CreateException, RemoteException;
	
}
