/*
 * RegistroModificadoEntradaHome.java
 *
 * Created on 6 de septiembre de 2002, 11:53
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;

/**
 * Interfície home per accedir al bean que gestiona publicació del registre d'entrada
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface RegistroPublicadoEntradaHome extends EJBHome {
	
	public RegistroPublicadoEntrada create() throws CreateException, RemoteException;
	
}
