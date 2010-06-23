/*
 * ListadoRegOficiosSalida.java
 *
 * Created on 6 de agosto de 2009, 10:49
 */

package es.caib.regweb;

import java.rmi.*;

import javax.ejb.*;
import java.util.*;

/**
 * Interfície per accedir al bean que genera els llistats d'oficis de remisió pendents d'arribar
 * @author  AROGEL
 * @version 1.0 
 */

public interface ListadoRegOficiosSalida extends EJBObject {
	
	/** Fijamos la oficina en el objeto ListadoRegOficios
	 *
	 * @param String oficina
	 *
	 * @throws RemoteException
	 */
	public void setOficina(String oficina) throws RemoteException;
	
	/** Recuperamos la oficina en el objeto ListadoRegOficios
	 *
	 * @return String oficina
	 *
	 * @throws RemoteException
	 */
	public String getOficina() throws RemoteException;
	public Hashtable getErrores() throws RemoteException;

	public Vector recuperarRegistros(String usuario) throws RemoteException, Exception;

	public void inizializar() throws RemoteException;
}
