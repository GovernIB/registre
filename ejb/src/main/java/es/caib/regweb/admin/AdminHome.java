/*
 * AdminHome.java
 *
 * Created on 6 de septiembre de 2002, 11:53
 */

package es.caib.regweb.admin;

import java.rmi.*;
import javax.ejb.*;

/**
 * Interfície home per accedir al bean que permet fer consultes sobre el registre d'accessos al registre d'entrades 
 * @author  Sebastià Matas Riera
 * @version 1.0
 */

public interface AdminHome extends EJBHome {
	
	public Admin create() throws CreateException, RemoteException;
	
}