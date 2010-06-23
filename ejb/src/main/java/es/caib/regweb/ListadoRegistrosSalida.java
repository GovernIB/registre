package es.caib.regweb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.Hashtable;

/**
 * Interfície per accedir al bean que genera els llistats de registres de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface ListadoRegistrosSalida extends EJBObject  {
	public void setoficinaDesde(String oficinaDesde) throws RemoteException;
	public void setoficinaHasta(String oficinaHasta) throws RemoteException;
	public void setoficinaFisica(String oficinaFisica) throws RemoteException;
	public void setfechaDesde(String fechaDesde) throws RemoteException;
	public void setfechaHasta(String fechaHasta) throws RemoteException;
	public String getOficinaDesde() throws RemoteException;
	public String getOficinaHasta() throws RemoteException;
	public String getOficinaFisica() throws RemoteException;
	public String getFechaDesde() throws RemoteException;
	public String getFechaHasta() throws RemoteException;
	public Hashtable getErrores() throws RemoteException;
	public boolean validarBusqueda() throws RemoteException;
	public void inizializar() throws RemoteException;
	public Vector recuperar(String usuario, int sizePagina, int pagina) throws RemoteException;
	public void setAny(String any) throws RemoteException;
	public String getExtracto() throws RemoteException;
	public void setExtracto(String extracto) throws RemoteException;
	public String getTipo() throws RemoteException;
	public void setTipo(String tipo) throws RemoteException;
	public String getRemitente() throws RemoteException;
	public void setRemitente(String remitente) throws RemoteException;
	public String getDestinatario() throws RemoteException;
	public void setDestinatario(String destinatario) throws RemoteException;
	public String getCodiRemitent() throws RemoteException;
	public void setCodiRemitent(String codiremitent) throws RemoteException;
	public String getDestino() throws RemoteException;
	public void setDestino(String destino) throws RemoteException;
	public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero)
	throws RemoteException, Exception;
	
	public boolean isCalcularTotalRegistres() throws RemoteException;
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) throws RemoteException;
	
	/** Obtenim el nombre total de resultats */
	public String getTotalFiles() throws RemoteException;
}