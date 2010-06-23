package es.caib.regweb;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.EJBObject;

/**
 * Interfície per accedir al bean que gestiona el registre d'entrada
 * @author  VHERRERA, basado en el código de FJMARTINEZ
 * @version 1.0
 */
public interface ReproUsuario extends EJBObject {

	/** Eliminamos el objeto Registro en la base de datos que se le indica a través de los parámetros.
	 * @param String codUsuario
	 * @param String nomRepro
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void eliminar(String codUsuario, String nomRepro) throws RemoteException, SQLException, ClassNotFoundException, Exception;
	/** Eliminamos el objeto Registro en la base de datos al que hace referencia el EJB.
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */	
	//public void eliminar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	
	/** Leemos el objeto Registro en la base de datos.
	 * @param String codUsuario
	 * @param String nomRepro
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void leer(String codUsuario, String nomRepro) throws RemoteException, SQLException, ClassNotFoundException, Exception;
	/** Grabamos el objeto Registro en la base de datos.
	 * @param String codUsuario
	 * @param String nomRepro
	 * @param String ReproValor
	 * @param String tipRepro
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 * @return boolean
	 */
	public boolean grabar(String codUsuario, String nomRepro, String ReproValor, String tipRepro) throws RemoteException, SQLException, ClassNotFoundException, Exception;
	/** Grabamos el objeto Registro en la base de datos.
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	//public boolean grabar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	/** Recuperamos en un String desde el objeto Registro el valor de la Repro
	 * @throws RemoteException
	 * @return String
	 */
	//public String getRepro() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el código del usuario al que pertenece la Repro
	 * @throws RemoteException
	 * @return String
	 */
	//public String getUsuario() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el nombre de la Repro
	 * @throws RemoteException
	 * @return String
	 */
	//public String getNombre() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el tipo de  Repro
	 * @throws RemoteException
	 * @return String
	 */
	//public String getTipRepro()throws RemoteException;
	/** Fija en el objeto Registro el valor de la Repro
	 * @param String repro
	 * @throws RemoteException
	 */
	//public void setRepro(String repro) throws RemoteException;
	/** Fija en el objeto Registro el valor del código de usuario al que pertenece la repro
	 * @param String usuario
	 * @throws RemoteException
	 */
	//public void setCodUsuario(String usuario) throws RemoteException;
	/** Fija en el objeto Registro el valor del nombre de la repro
	 * @param String repro
	 * @throws RemoteException
	 */
	//public void setNombre(String nombre) throws RemoteException;
	/** Fija en el objeto Registro el tipo de repro
	 * @param String tipoRepro
	 * @throws RemoteException
	 */
	//public void setTipRepro(String tipoRepro)throws RemoteException;
	/**
	 * Recuperam les Repros del usuari guardades a BD
	 * @param usuario Codi usuari que fa la consulta
     */
	public Vector recuperarRepros(String usuario) throws RemoteException ;
	/**
	 * Recuperam les Repros d'un tipus del usuari guardades a BD
	 * @param usuario Codi usuari que fa la consulta
	 * @param tipo Tipus de repro. Si es 'null' recupera todas las repros del usuario.
     */
	public Vector recuperarRepros(String usuario, String tipo) throws RemoteException ;

}
