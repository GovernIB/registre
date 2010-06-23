/*
 * ListadoRegistrosEntrada.java
 *
 * Created on 6 de septiembre de 2002, 10:49
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;
import java.util.*;

/**
 * Interfície per accedir al bean que genera els llistats de registres d'entrada
 * @author  FJMARTINEZ
 * @version 1.0 
 */

public interface ListadoRegistrosEntrada extends EJBObject {
	
	/** Devuelve un Vector cargado con objetos Registro. Se seleccionan unicamente
	 * aquellos registros del usuario pasado como parametro y se comprueba que el
	 * usuario tenga autorizacion para consulta de entradas. El Vector esta ordenado
	 * por año de entrada y numero de oficina.
	 *
	 * @return Vector de objetos Registro.
	 * @param String usuario.
	 *
	 * @throws RemoteException 
	 */
	public Vector recuperar(String usuario, int sizePagina, int pagina) throws RemoteException;
	
	public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero)
	throws RemoteException, Exception;
	/** Fijamos el año en el objeto ListadoRegistros
	 *
	 * @param String año
	 *
	 * @throws RemoteException
	 */
	public void setAny(String any) throws RemoteException;
	/** Fijamos la oficina inicial en el objeto ListadoRegistros
	 *
	 * @param String oficinaDesde
	 *
	 * @throws RemoteException
	 */
	public void setoficinaDesde(String oficinaDesde) throws RemoteException;
	
	/** Fijamos la oficina final en el objeto ListadoRegistros
	 *
	 * @param String oficinaHasta
	 *
	 * @throws RemoteException
	 */
	public void setoficinaHasta(String oficinaHasta)throws RemoteException;
	
	/** Fijamos la oficina fisica en el objeto ListadoRegistros
	 *
	 * @param String oficinaFisica
	 *
	 * @throws RemoteException
	 */
	public void setoficinaFisica(String oficinaFisica)throws RemoteException;

	/** Fijamos la fecha inicial en el objeto ListadoRegistros
	 *
	 * @param String fechaDesde
	 *
	 * @throws RemoteException
	 */
	public void setfechaDesde(String fechaDesde) throws RemoteException;
	
	/** Fijamos la fecha final en el objeto ListadoRegistros
	 *
	 * @param String fechaHasta
	 *
	 * @throws RemoteException
	 */
	public void setfechaHasta(String fechaHasta) throws RemoteException;
	
	/** Fijamos el código del municipio 060
	 *
	 * @param String fechaHasta
	 *
	 * @throws RemoteException
	 */
	public void setCodiMunicipi060(String codiMun060) throws RemoteException;
	
	/** Recuperamos la oficina inicial en el objeto ListadoRegistros
	 *
	 * @return String oficinaDesde
	 *
	 * @throws RemoteException
	 */
	public String getOficinaDesde() throws RemoteException;
	
	/** Recuperamos la oficina final en el objeto ListadoRegistros
	 *
	 * @return String oficinaHasta
	 *
	 * @throws RemoteException
	 */
	public String getOficinaHasta() throws RemoteException;
	
	/** Recuperamos la oficina fisica en el objeto ListadoRegistros
	 *
	 * @return String oficinaFisica
	 *
	 * @throws RemoteException
	 */
	public String getOficinaFisica() throws RemoteException;

	/** Recuperamos la oficina el codi del municipi 060
	 *
	 * @return String codiMun060
	 *
	 * @throws RemoteException
	 */
	public String getCodiMunicipi060() throws RemoteException;
	
	/** Recuperamos la fecha inicial en el objeto ListadoRegistros
	 *
	 * @return String fechaDesde
	 *
	 * @throws RemoteException
	 */
	public String getFechaDesde() throws RemoteException;
	
	/** Recuperamos la fecha final en el objeto ListadoRegistros
	 *
	 * @return String fechaHasta
	 *
	 * @throws RemoteException
	 */
	public String getFechaHasta() throws RemoteException;
	
	/** Recuperamos los errores de la validacion del forumlario de busqueda de registros
	 *
	 * @return Hashtable errores
	 *
	 * @throws RemoteException
	 */
	public Hashtable getErrores() throws RemoteException;
	
	/** Validamos el formulario para la busqueda de registros
	 *
	 * @return boolean validado
	 *
	 * @throws RemoteException
	 */
	public boolean validarBusqueda() throws RemoteException;
	public String getExtracto() throws RemoteException;
	public void setExtracto(String extracto) throws RemoteException;
	public String getTipo() throws RemoteException;
	public void setTipo(String tipo) throws RemoteException;
	public String getRemitente() throws RemoteException;
	public void setRemitente(String remitente) throws RemoteException;
	public String getDestinatario() throws RemoteException;
	public void setDestinatario(String destinatario) throws RemoteException;
	public String getCodiDestinatari() throws RemoteException;
	public void setCodiDestinatari(String codidestinatari) throws RemoteException;
	public String getProcedencia() throws RemoteException;
	public void setProcedencia(String remitente) throws RemoteException;
	
	public boolean isCalcularTotalRegistres() throws RemoteException;
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) throws RemoteException;
	
	/** Obtenim el nombre total de resultats */
	public String getTotalFiles() throws RemoteException;
	
	public void inizializar() throws RemoteException;
}
