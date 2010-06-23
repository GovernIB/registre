/*
 * ListadoRegOficiosHome.java
 *
 * Created on 6 de agosto de 2009, 11:53
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;

/**
 * Interfície home per accedir al bean que genera els llistats d'oficis de remisió 
 * @author  AROGEL
 * @version 1.0
 */

public interface ListadoOficiosHome extends EJBHome {
	
	public ListadoOficios create() throws CreateException, RemoteException;
	
}
