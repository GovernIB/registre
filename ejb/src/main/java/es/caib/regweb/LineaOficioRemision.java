/*
 * OficioRemision.java
 *
 * Created on 6 de agosto de 2009, 10:49
 */

package es.caib.regweb;

import java.io.OutputStream;
import java.rmi.*;
import javax.ejb.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.naming.NamingException;

/**
 * Interfície per accedir al bean que gestiona els oficis de remisió
 * @author  AROGEL
 * @version 1.0
 */

public interface LineaOficioRemision extends EJBObject {
	 
    public String getAnoEntrada() throws RemoteException;
	public void setAnoEntrada(String anoEntrada) throws RemoteException;
	public String getNumeroEntrada() throws RemoteException;
	public void setNumeroEntrada(String numeroEntrada) throws RemoteException; 
	public String getOficinaEntrada() throws RemoteException;
	public void setOficinaEntrada(String oficinaEntrada) throws RemoteException;
	public String getUsuarioEntrada() throws RemoteException;
	public void setUsuarioEntrada(String usuarioEntrada) throws RemoteException;
	public String getDescartadoEntrada() throws RemoteException;
	public void setDescartadoEntrada(String descartadoEntrada) throws RemoteException;
	public String getMotivosDescarteEntrada() throws RemoteException;
	public void setMotivosDescarteEntrada(String motivosDescarteEntrada) throws RemoteException;
	public String getAnoOficio() throws RemoteException;
	public void setAnoOficio(String anoOficio) throws RemoteException;
	public String getNumeroOficio() throws RemoteException;
	public void setNumeroOficio(String numeroOficio) throws RemoteException;
	public String getOficinaOficio() throws RemoteException;
	public void setOficinaOficio(String oficinaOficio) throws RemoteException;
	public String getUsuario() throws RemoteException;
	public void setUsuario(String usuario) throws RemoteException;
	public Hashtable getErrores() throws RemoteException;
	public void setErrores(Hashtable errores) throws RemoteException;
	public boolean isLeidos() throws RemoteException;
	public void setLeidos(boolean leidos) throws RemoteException;
    public void grabar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
    public void actualizar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
    public void leer() throws RemoteException;
    public boolean getGrabado() throws RemoteException;
    public boolean getActualizado() throws RemoteException;
    
	
	
}
