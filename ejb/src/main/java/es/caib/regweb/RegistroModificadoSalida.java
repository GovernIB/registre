/*
 * RegistroModificadoSalida.java
 *
 * Created on 6 de septiembre de 2002, 10:49
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;
import java.util.*;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * Interfície per accedir al bean que gestiona la modificació del registre de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface RegistroModificadoSalida extends EJBObject {
	
	public void setAnoSalida(int anoEntrada) throws RemoteException;
	public void setOficina(int oficina) throws RemoteException;
	public void setEntidad1(String entidad1) throws RemoteException;
	public void setExtracto(String extracto) throws RemoteException;
	public void setUsuarioModificacion(String usuarioModificacion) throws RemoteException;
	public void setUsuarioVisado(String usuarioVisado) throws RemoteException;
	public void setIndVisExtracto(String indVisExtracto) throws RemoteException;
	public void setIndVisRemitente(String indVisRemitente) throws RemoteException;
	public void setEntidad2(int entidad2) throws RemoteException;
	public void setNumeroRegistro(int numeroRegistro) throws RemoteException;
	public void setRemitente(String remitente) throws RemoteException;
	public void setMotivo(String motivo) throws RemoteException;
	public void setFechaModificacion(String fechaModificacion) throws RemoteException;
	public void setHoraModificacion(String horaModificacion) throws RemoteException;
	public void setVisarRemitente(boolean hayVisadoRemitente) throws RemoteException;
	public void setVisarExtracto(boolean hayVisadoExtracto) throws RemoteException;
	public void fijaPasswordUser(String password) throws RemoteException;
	
	public boolean generarModificacion(Connection conn) throws RemoteException;
	public List recuperarRegistros(String oficina, String usuario) throws RemoteException;
	public void leer() throws RemoteException;
	public boolean visar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	
	public boolean getLeido() throws RemoteException;
	public int getNumeroRegistro() throws RemoteException;
	public int getAnoSalida() throws RemoteException;
	public int getOficina() throws RemoteException;
	public String getMotivo() throws RemoteException;
	public String getEntidad1() throws RemoteException;
	public String getEntidad1Catalan() throws RemoteException;
	public int getEntidad2() throws RemoteException;
	public String getRemitente() throws RemoteException;
	public String getExtracto() throws RemoteException;
}
