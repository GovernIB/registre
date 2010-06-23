package es.caib.regweb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.EJBObject;

/**
 * Interfície per accedir al bean que gestiona els models d'ofici
 * @author  AROGEL
 * @version 1.0
 */
public interface ModeloOficio extends EJBObject {

	/** Eliminamos el objeto ModeloOficio en la base de datos que se le indica a través de los parámetros.
	 * @param String nomModel
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void eliminar(String nomModel) throws RemoteException, SQLException, ClassNotFoundException, Exception;
	
	/** Leemos el objeto ModeloOficio en la base de datos.
	 * @param String nomModel
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void leer(String nomModel) throws RemoteException, SQLException, ClassNotFoundException, Exception;
	
	/** Grabamos el objeto ModeloOficio en la base de datos.
	 * @param String nomModel
	 * @param String ReproValor
	 * @param String tipRepro
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 * @return boolean
	 */
	public boolean grabar(String nomModel, String conType, byte[] dades) throws RemoteException, SQLException, ClassNotFoundException, Exception;
	
	public byte[] getDatos() throws RemoteException;
	public String getContentType() throws RemoteException;

	/**
	 * Recuperam els models d'ofici guardats a BD
	 */
	//public Vector recuperarModels() throws RemoteException ;
	
}
