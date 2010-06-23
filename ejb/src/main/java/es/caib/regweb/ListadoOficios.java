/*
 * ListadoRegOficios.java
 *
 * Created on 6 de agosto de 2009, 10:49
 */

package es.caib.regweb;

import java.rmi.*;

import javax.ejb.*;
import java.util.*;

/**
 * Interfície per accedir al bean que genera els llistats d'oficis de remisió 
 * @author  AROGEL
 * @version 1.0 
 */

public interface ListadoOficios extends EJBObject {
	
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

	/** Fijamos la oficinaFisica en el objeto ListadoRegOficios
	 *
	 * @param String oficinaFisica
	 *
	 * @throws RemoteException
	 */
	public void setOficinaFisica(String oficinaFisica) throws RemoteException;
	
	/** Recuperamos la oficinaFisica en el objeto ListadoRegOficios
	 *
	 * @return String oficinaFisica
	 *
	 * @throws RemoteException
	 */
	public String getOficinaFisica() throws RemoteException;

	/** Fijamos el año en el objeto ListadoRegOficios
	 *
	 * @param String anyo
	 *
	 * @throws RemoteException
	 */
	public void setAnyo(String anyo) throws RemoteException;
	
	/** Recuperamos el año en el objeto ListadoRegOficios
	 *
	 * @return String anyo
	 *
	 * @throws RemoteException
	 */
	public String getAnyo() throws RemoteException;

	public Hashtable getErrores() throws RemoteException;

	public Vector recuperarRegistros(String usuario) throws RemoteException, Exception;

	public void inizializar() throws RemoteException;
}
