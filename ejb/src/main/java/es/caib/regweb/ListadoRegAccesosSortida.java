/*
 * Created on 19 de junio de 2002, 18:56
 */

package es.caib.regweb;

import java.rmi.*;

import javax.ejb.*;

import java.util.*;

/**
 * Interfície per accedir al Bean que permet fer consultes sobre el registre d'accessos al registre d'entrades 
 * @author  Sebastià Matas, basat en codi de FJMARTINEZ
 * @version 1.0
 */

public interface ListadoRegAccesosSortida  extends EJBObject  {
	
	public String getAnyRegistre()throws RemoteException;
	public void setAnyRegistre(String anyRegistre)throws RemoteException;
	public String getNumRegistre() throws RemoteException;
	public void setNumRegistre(String numRegistre) throws RemoteException;
	public void setoficinaDesde(String oficinaDesde) throws RemoteException;
	public void setoficinaHasta(String oficinaHasta) throws RemoteException;
	public void setfechaDesde(String fechaDesde) throws RemoteException;
	public void setfechaHasta(String fechaHasta) throws RemoteException;
	public String getOficinaDesde() throws RemoteException;
	public String getOficinaHasta() throws RemoteException;
	public String getFechaDesde() throws RemoteException;
	public String getFechaHasta() throws RemoteException;
	
	
	/**
	 * Mètode per veure els errors que s'han generat en l'execució del Bean.
	 * @return Torna els errors que s'han generat.
	 */
	public Hashtable getErrores() throws RemoteException;

	/**
	 * Mètode per validar els camps de cerca
	 * @return Torna true si els camps son vàlids
	 */

	public boolean validarBusqueda() throws RemoteException;
	
	/**
	 * Inicialització del Bean
	 */	
	public void inizializar() throws RemoteException;

	/**
	 * Recuperam els registres segons els filtres de cerca.
	 * @param usuario Usuari que fa la consulta
	 * @param sizePagina Nombre de registres a tornar
	 * @param pagina Nombre de pàgina a tornar
	 * @return Vector que conté les classes amb la informació de cada registre.
	 */
	public Vector recuperar(String usuario, int sizePagina, int pagina) throws java.rmi.RemoteException ;

	/**
	 * Recuperam els registres segons els filtres de cerca.
	 * @param usuario Usuari que fa la consulta
	 * @param maxRegistros Nombre màxim de registres a tornar
	 * @param oficina Oficina del registre a tornar
	 * @param any Any del registre a tornar
	 * @param accion ?????????????
	 * @param numero Nombre de registre a tornar
	 * En teoría només hauria de tornar un registre!!!!
	 * 
	 * @return Vector que conté les classes amb la informació de cada registre.
	 */
	public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero)
	throws java.rmi.RemoteException, Exception;
	
	public boolean isCalcularTotalRegistres() throws RemoteException;
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) throws RemoteException;
	
	/** Obtenim el nombre total de resultats */
	public String getTotalFiles() throws RemoteException;

	
}