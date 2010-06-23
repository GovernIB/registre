/*
 * Valores.java
 *
 * Created on 6 de septiembre de 2002, 10:49
 */

package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;
import java.util.*;

import javax.servlet.http.*;

/**
 * Interfície per accedir al bean que encapsula funcions massa genèriques per tenir el seu probi EJB.
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface Valores extends EJBObject {
	
	/** Devuelve un Vector cargado con las oficinas a las que puede acceder un
	 * determinado usuario. El vector esta compuesto por parejas de elementos, el
	 * primer elemento es el <I>numero de oficina</I> y el segundo es la <I>descripcion de la
	 * oficina</I>.<p>
	 * NOTA SMR: Per no alterar la signatura del bean, dia 1/8/2007 he modificat aquesta funció
	 * per a que quan se li passin com a paràmetres "tots" i "totes", torni totes les oficines, sense
	 * mirar les autoritzacions de l'usuari (emprant el mètode getOficines). 
	 * @return Vector, con pares de elementos (numero de oficina y descripcion).
	 * @param usuario String
	 * @param autorizacion AE=Altas entradas  CE=Consultas entradas
	 * @throws RemoteException
	 */
	public Vector BuscarOficinas(String usuario, String autorizacion) throws RemoteException;

    /** Devuelve un Vector cargado con las oficinas fisicas a las que puede acceder un
	 * determinado usuario. El vector esta compuesto por tripletes de elementos, el
	 * primer elemento es el <I>numero de oficina</I>, el segundo el <I>numero de oficina física</I>
	 * y el tercero es la <I>descripcion de la oficina física</I>.<p>
	 * NOTA SMR: Per no alterar la signatura del bean, dia 1/8/2007 he modificat aquesta funció
	 * per a que quan se li passin com a paràmetres "tots" i "totes", torni totes les oficines, sense
	 * mirar les autoritzacions de l'usuari (emprant el mètode getOficinesFisiques). 
	 * @return Vector, con tripletes de elementos (numero de oficina, num. de oficia fisica y descripcion).
	 * @param usuario String
	 * @param autorizacion AE=Altas entradas  CE=Consultas entradas
	 * @throws RemoteException
	 */
	public Vector BuscarOficinasFisicas(String usuario, String autorizacion) throws RemoteException;

    /** Devuelve un Vector cargado con las oficinas fisicas a las que puede acceder un
	 * determinado usuario. El vector esta compuesto por cuadruplas de elementos, el
	 * primer elemento es el <I>numero de oficina</I>, el segundo el <I>numero de oficina física</I>,
	 * el tercero es la <I>descripcion de la oficina física</I> y el cuarto es la <I>descripcion de la oficina</I>.<p>
	 * NOTA SMR: Per no alterar la signatura del bean, dia 1/8/2007 he modificat aquesta funció
	 * per a que quan se li passin com a paràmetres "tots" i "totes", torni totes les oficines, sense
	 * mirar les autoritzacions de l'usuari (emprant el mètode getOficinesFisiquesDescripcion). 
	 * @return Vector, con cadruplas de elementos (numero de oficina, num. de oficia fisica, descripcion of. fisica y descripcion of.).
	 * @param usuario String
	 * @param autorizacion AE=Altas entradas  CE=Consultas entradas
	 * @throws RemoteException
	 */
	public Vector BuscarOficinasFisicasDescripcion(String usuario, String autorizacion) throws RemoteException;
	
	/** Devuelve un Vector cargado con las modelos de oficio a las que puede acceder un
	 * determinado usuario. El vector esta compuesto por el <I>nombre del modelo de oficio</I>.<p>
	 * NOTA: Actualmente sólo esta implementado que cuando se le pase como parámetros "tots" y "totes"
	 * devuelve todos los modelos sin mirar las autorizaciones del usuario (usando el método getModels)
	 * @return Vector, con nombres de modelo
	 * @param usuario String
	 * @param autorizacion AE=Altas entradas  CE=Consultas entradas
	 * @throws RemoteException
	 */
	public Vector BuscarModelos(String usuario, String autorizacion) throws RemoteException;
	

	/** Devuelve un Vector cargado con las modelos de recibo a las que puede acceder un
	 * determinado usuario. El vector esta compuesto por el <I>nombre del modelo de recibo</I>.<p>
	 * NOTA: Actualmente sólo esta implementado que cuando se le pase como parámetros "tots" y "totes"
	 * devuelve todos los modelos sin mirar las autorizaciones del usuario (usando el método getModelsRebuts)
	 * @return Vector, con nombres de modelo
	 * @param usuario String
	 * @param autorizacion AE=Altas entradas  CE=Consultas entradas
	 * @throws RemoteException
	 */
	public Vector BuscarModelosRecibos(String usuario, String autorizacion) throws RemoteException;
	
	/** Devuelve un vector cargado con los tipos de documentos validos, ordenando por
	 * tipo de documento. El vector esta compuesto por parejas de elementos, siendo el
	 * primer elemento el tipo de documento y el segundo un <I>String</I> compuesto
	 * de <I>tipo de documento</I> - <I>descripcion del documento</I>.
	 * @return Vector, con pares de elementos (tipo documento y tipo documento - descripcion
	 * documento).
	 * @throws RemoteException
	 */
	public String recuperarTipoDocumento(String tipo) throws RemoteException;
	
	public Vector BuscarDocumentos() throws RemoteException;
	
	/** Devuelve un Vector cargado con los idiomas validos para el documento. El vector
	 * esta formado por parejas de elementos, siendo el primer elemento el <I>codigo del
	 * idioma</I>  y el segundo elemento la <I>descripcion del idioma</I>.
	 * @return Vector, con pares de elementos (codigo idioma y descripcion idioma).
	 * @throws RemoteException
	 */
	public Vector BuscarIdiomas() throws RemoteException;
	
	/** Devuelve un Vector cargado con las procedencias geograficas de Baleares. El
	 * vector esta formado por parejas de elementos, siendo el primer elemento el
	 * <I>codigo de la agrupacion geografica</I> y el segundo elemento la <I>denominacion de la
	 * agrupacion geografica</I>. La primera pareja de elementos del Vector estara en
	 * blancos.
	 * @return Vector, con pares de elementos (codigo y descripcion de la agrupacion
	 * geografica).
	 * @throws RemoteException
	 */
	public Vector BuscarBaleares() throws RemoteException;
	
	/** Devuelve un Vector cargado con los Organismos Destinatarios a los que puede
	 * enviar documentos una determinada oficina. El Vector esta formado por parejas de
	 * elementos siendo el primer elemento el <I>codigo de Organismo</I>  y el segundo
	 * elemento <I>la descripcion reducida del Organismo</I>.
	 * @return Vector, con pares de elementos (codigo y descripcion reducida del Organismo)
	 * @param Oficina String Oficina
	 * @throws RemoteException
	 */
	public Vector BuscarDestinatarios(String usuario) throws RemoteException;
	

	/** Devuelve un Vector cargado con los Organismos Destinatarios a los que no puede
	 * enviar oficios de remision una determinada oficina. El Vector esta formado por parejas de
	 * elementos siendo el primer elemento el <I>codigo de Organismo</I>  y el segundo
	 * elemento <I>la descripcion reducida del Organismo</I>.
	 * @return Vector, con pares de elementos (codigo y descripcion reducida del Organismo)
	 * @param Oficina String Oficina
	 * @throws RemoteException
	 */
	public Vector BuscarNoRemision(String usuario) throws RemoteException;
	
	/** Devuelve en un String la fecha del sistema en formato dd/mm/yyyy.
	 * @throws RemoteException
	 * @return String, con la fecha del sistema.
	 */
	public String getFecha() throws RemoteException;
	
	/** Devuelve en un String la hora y los minutos del sistema en formato hh:mm.
	 * @throws RemoteException
	 * @return String, con la hora y minutos del sistema (HH:mm).
	 */
	public String getHorasMinutos() throws RemoteException;
	
	/** Devuelve un Vector cargado con las Entidades que pueden enviar documentos. Este
	 * metodo recibe un <I>String</I> para realizar la busqueda tanto por la
	 * denominacion en catalan como en castellano. Por cada Entidad Remitente
	 * seleccionada se añadiran 3 elementos al Vector, <I>codigo de la Entidad en
	 * catalan</I>, <I>numero de la Entidad</I> y la <I>denominacion en catalan</I>.
	 * @return Vector, cargado con Entidades Remitentes (codigo Entidad, numero Entidad y
	 * denominacion en catalan).
	 * @param String Cadena de busqueda.
	 * @throws RemoteException
	 */
	public Vector buscarRemitentes(String subcadena, String subcadenaTexto) throws RemoteException;
	
	/** Devuelve un String con el ultimo numero de disquete utilizado para una oficina.
	 * Se le debe de pasar en un String el numero de oficina.
	 * @return String, con el ultimo numero de disquete.
	 * @param String Oficina con la cual buscar el ultimo numero de disquete.
	 * @param String Tipo   "E"=Entrada     "S"=Salida
	 * @param String fEntrada, contendra la fecha de entrada o de salida
	 * @param String usuario
	 * @throws RemoteException
	 */
	public String buscarDisquete(String oficina, String tipo, String fEntrada, String usuario, HttpSession session) throws RemoteException;
	
	/** Devuelve un String con la descricpion del organismo destinatario
	 *
	 * @param Organismo
	 *
	 * @return String
	 */
	public String recuperarDestinatario(String organismo) throws RemoteException;
	
	/** Devuelve en String la descripcion de la Entidad Remitente
	 *
	 * @param String Entidad1
	 * @param String Entidad2
	 *
	 * @return String
	 */
	public String recuperaRemitente(String entidad1, String entidad2) throws RemoteException;
	public String recuperaRemitenteCastellano(String entidad1, String entidad2) throws RemoteException;
	/** Devuelve en String la descripcion de la Oficina
	 *
	 * @param String Oficina
	 *
	 * @return String
	 */
	public String recuperaDescripcionOficina(String oficina) throws RemoteException;
	/** Devuelve en String la descripcion de la Oficina fisica
	 *
	 * @param String Oficina
	 *
	 * @return String
	 */
	public String recuperaDescripcionOficinaFisica(String oficina, String oficinafisica) throws RemoteException;
	
	/** Devuelve un boolean indicando el estado del bloqueo del disquete
	 *
	 * @param String Oficina
	 * @param String Tipo   (E=Entrada S=Salida)
	 * @param String Fentrada   Fecha del registro
	 * @param String Usuario
	 *
	 * @return boolean  (false=no esta bloqueado   true=bloqueado)
	 */
	public boolean estaBloqueado(String oficina, String tipo, String fEntrada, String usuario) throws RemoteException;
	
	/** Devuelve un boolean indicando la accion del bloqueo del disquete
	 *
	 * @param String Oficina
	 * @param String Tipo   (E=Entrada S=Salida)
	 * @param String Fentrada   Fecha del registro
	 * @param String Usuario
	 *
	 * @return boolean  (false=no esta bloqueado   true=bloqueado)
	 */
	public boolean bloquearDisquete(String oficina, String tipo, String fEntrada, String usuario) throws RemoteException;
	
	/** Devuelve un boolean indicando la accion de la liberacion del disquete
	 *
	 * @param String Oficina
	 * @param String Tipo   (E=Entrada S=Salida)
	 * @param String Fentrada   Fecha del registro
	 * @param String Usuario
	 *
	 * @return boolean  (false=no esta bloqueado   true=bloqueado)
	 */
	public boolean liberarDisquete(String oficina, String tipo, String anyo, String usuario) throws RemoteException;
	/** Devuelve un boolean indicando si el usuario esta autorizado a visar modificaciones
	 *
	 * @param String Usuario
	 * @param String programa
	 *
	 * @return boolean  (false=no esta autorizado   true=autorizado)
	 */
	public boolean usuarioAutorizadoVisar(String usuario, String programa) throws RemoteException;
	
	/** Devuelve un Vector cargado con los ayuntamenientos con acuerdos del 060. El vector
	 * esta formado por parejas de elementos, siendo el primer elemento el <I>codigo del
	 * ayuntamiento</I>  y el segundo elemento la <I>Nombre del ayuntamiento</I>.
	 * @return Vector, con pares de elementos (codigo ayuntamiento y nombre ayuntamiento).
	 * @throws RemoteException
	 */
	public Vector Buscar_060() throws RemoteException;
	/** Devuelve un Vector cargado con los ayuntamenientos con acuerdos del 060. El vector
	 * esta formado por parejas de elementos, siendo el primer elemento el <I>codigo del
	 * ayuntamiento</I>  y el segundo elemento la <I>Nombre del ayuntamiento</I>.
	 * 
	 * @param String valor. Valor del parametro "000"
	 * @param boolean todos. Si es false, solo muestra los municipios activos 
	 * 
	 * @return Vector, con pares de elementos (codigo ayuntamiento y nombre ayuntamiento).
	 * @throws RemoteException
	 */
	public Vector Buscar_060(String valor, boolean todos) throws RemoteException;
	
}
