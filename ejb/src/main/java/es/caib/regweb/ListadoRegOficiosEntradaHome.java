/*
 * ListadoRegOficiosEntradaHome.java
 *
 * Created on 6 de agosto de 2009, 11:53
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;

/**
 * Interfície home per accedir al bean que genera els llistats de registres d'entrada pendents de remisio
 * @author  AROGEL
 * @version 1.0
 */

public interface ListadoRegOficiosEntradaHome extends EJBHome {
	
	public ListadoRegOficiosEntrada create() throws CreateException, RemoteException;
	
}
