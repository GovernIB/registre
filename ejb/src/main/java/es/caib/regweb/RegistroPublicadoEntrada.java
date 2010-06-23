/*
 * RegistroModificadoEntrada.java
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
 * Interfície per accedir al bean que gestiona publicació del registre d'entrada
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface RegistroPublicadoEntrada extends EJBObject {
	
	public int getAnoEntrada() throws RemoteException;
	public void setAnoEntrada(int anoEntrada) throws RemoteException;
	public int getNumero() throws RemoteException;
	public void setNumero(int numero) throws RemoteException;
	public int getOficina() throws RemoteException;
	public void setOficina(int oficina) throws RemoteException;
	public int getNumeroBOCAIB() throws RemoteException;
	public void setNumeroBOCAIB(int numeroBOCAIB) throws RemoteException;
	public String getFecha() throws RemoteException;
	public void setFecha(int fecha) throws RemoteException;
	public int getPagina() throws RemoteException;
	public void setPagina(int pagina) throws RemoteException;
	public int getLineas() throws RemoteException;
	public void setLineas(int lineas) throws RemoteException;
	public String getContenido() throws RemoteException;
	public void setContenido(String contenido) throws RemoteException;
	public String getObservaciones() throws RemoteException;
	public void setObservaciones(String observaciones) throws RemoteException;
	
	public boolean leer() throws RemoteException, Exception;
	public void borrar() throws RemoteException, Exception;
	public void grabar() throws RemoteException, Exception;
}
