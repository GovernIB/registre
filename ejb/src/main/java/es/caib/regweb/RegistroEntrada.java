/*
 * RegistroEntrada.java
 *
 * Created on 6 de septiembre de 2002, 10:49
 */

package es.caib.regweb;

//import es.caib.signatura.api.Signature;
import java.io.OutputStream;
import java.rmi.*;
import javax.ejb.*;
import java.util.*;
import java.sql.SQLException;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.naming.NamingException;

/**
 * Interfície per accedir al bean que gestiona el registre d'entrada
 * @author  FJMARTINEZ
 * @version 1.0
 */

public interface RegistroEntrada extends EJBObject {
	
	public void setActualizacion(boolean actualizacion) throws RemoteException;
	/** Fijamos el usuario en el objeto Registro. Este es pasado como un String que se
	 * convierte a mayusculas.
	 * @param String usuario
	 * @throws RemoteException
	 */
	public void fijaUsuario(String usuario) throws RemoteException;
	public void fijaPasswordUser(String password) throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la Entidad Remitente si es
	 * <I>Altres</I>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getAltres() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de correo
	 * @throws RemoteException
	 * @return String
	 */
	public String getCorreo() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro si esta anulado
	 * @throws RemoteException
	 * @return String
	 */
	public String getRegistroAnulado() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el año de entrada.
	 * @throws RemoteException
	 * @return String
	 */
	public String getAnoEntrada() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el codigo de la procedencia
	 * geografica si esta es de Baleares.
	 * @throws RemoteException
	 * @return String
	 */
	public String getBalears() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el extracto del documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getComentario() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la fecha del documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getData() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la fecha de entrada del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDataEntrada() throws RemoteException;
	public String getDataVisado() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion del tipo
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionDocumento() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion del idioma del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionIdiomaDocumento() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion de la oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionOficina() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion de la oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionOficinaFisica() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion del Organismo
	 * destinatario del documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionOrganismoDestinatario() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion del Remitente,
	 * independientemente si corresponde a una <I>Entidad</I> o a <I>Altres</I>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionRemitente() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el Organismo destinatario del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDestinatari() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de disquete del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDisquet() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el codigo de la Entidad del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getEntidad1() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el codigo del muncipio 060 del
	* documento.
	* @throws RemoteException
	* @return String
	*/
	public String getMunicipi060() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripción del muncipio 060 del
	* documento.
	* @throws RemoteException
	* @return String
	*/
	public String getDescripcionMunicipi060() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de la Entidad del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getEntidad2() throws RemoteException;
	/** Recuperamos en una Hashtable desde el objeto Registro con los errores
	 * encontrados durante la validacion del objeto Registro. Esta Hashtable, la clave
	 * es el campo que ha generado el error, acompañado de una descripcion del mismo.
	 * @throws RemoteException
	 * @return Hashtable con errores.
	 */
	public Hashtable getErrores() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la procedencia geografica del
	 * documento si es de fuera de Baleares.
	 * @throws RemoteException
	 * @return String
	 */
	public String getFora() throws RemoteException;
	/** Recuperamos en un boolean desde el objeto Registro si este se ha grabado
	 * correctamente en la Base de Datos. True, si se ha grabado y false si no se ha
	 * grabado.
	 * @throws RemoteException
	 * @return boolean
	 */
	public boolean getGrabado() throws RemoteException;
	/** Recuperamos en un boolean desde el objeto Registro si este se ha actualizado
	 * correctamente en la Base de Datos. True, si se ha grabado y false si no se ha
	 * grabado.
	 * @throws RemoteException
	 * @return boolean
	 */
	public boolean getActualizado() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la hora y los minutos en los
	 * que se grabo el documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getHora() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el idioma del extracto.
	 * 1=Castellano, 2=Catalan.
	 * @throws RemoteException
	 * @return String
	 */
	public String getIdioex() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el codigo del idioma del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getIdioma() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la descripcion del idioma del
	 * extracto.
	 * @throws RemoteException
	 * @return String
	 */
	public String getIdiomaExtracto() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de entrada con el
	 * que se ha grabado el documento en el metodo <B>grabar()</B>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getNumero() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de entrada del
	 * documento durante el metodo <B>leer()</B>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getNumeroEntrada() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getOficina() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getOficinafisica() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro la procedencia geografica
	 * independientemente de que esta sea de fuera o de Baleares.
	 * @throws RemoteException
	 * @return String
	 */
	public String getProcedenciaGeografica() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de localizador del
	 * documento (numero de salida 1).
	 * @throws RemoteException
	 * @return String
	 */
	public String getSalida1() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el año localizador del
	 * documento (numero de salida 2).
	 * @throws RemoteException
	 * @return String
	 */
	public String getSalida2() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el tipo de documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getTipo() throws RemoteException;
	/** Recuperamos en un boolean desde el objeto Registro si el proceso de validacion
	 * ha finalizado satisfactoriamente.
	 * @throws RemoteException
	 * @return String
	 */
	public boolean getValidado() throws RemoteException;
	/** Recuperamos en un String desde el objeto Registro el numero de documentos
	 * que provienen del municipio 060.
	 * @throws RemoteException
	 * @return int
	 */
	public int getNumeroRegistros060()throws RemoteException;
	/** Fija en el objeto Registro el numero de documentos que provienen del municipio 060.
	 * @param anoEntrada String
	 * @throws RemoteException
	 */
	public void setNumeroRegistros060(int numeroRegistros060) throws RemoteException;

	/** Grabamos el objeto Registro en la base de datos.
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void grabar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	/** Actualizamos el objeto Registro en la base de datos.
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void actualizar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	/** Recuperamos de la base de datos un registro junto con los valores pertenecientes
	 * a las tablas auxiliares.
	 * @throws RemoteException
	 */
	public void leer() throws RemoteException;
	/** Fija en el objeto Registro la Entidad Remitente si esta es <I>Altres</I>.
	 * @param String Entidad Remitente
	 * @throws RemoteException
	 */
	public void setaltres(String altres) throws RemoteException;
	/** Fija en el objeto Registro el año de entrada del documento.
	 * @param anoEntrada String
	 * @throws RemoteException
	 */
	public void setAnoEntrada(String anoEntrada) throws RemoteException;
	/** Fija en el objeto Registro el codigo de procedencia geografica si es de
	 * Baleares.
	 * @param balears String
	 * @throws RemoteException
	 */
	public void setbalears(String balears) throws RemoteException;
	/** Fija en el objeto Registro el extracto del documento.
	 * @param comentario String
	 * @throws RemoteException
	 */
	public void setcomentario(String comentario) throws RemoteException;
	/** Fija en el objeto Registro la fecha del documento.
	 * @param data String
	 * @throws RemoteException
	 */
	public void setdata(String data) throws RemoteException;
	/** Fija en el objeto Registro registro anulado
	 * @param data String
	 * @throws RemoteException
	 */
	public void setRegistroAnulado(String registroAnulado) throws RemoteException;
	/** Fija en el objeto Registro el numero de correo.
	 * @param data String
	 * @throws RemoteException
	 */
	public void setCorreo(String data) throws RemoteException;
	/** Fija en el objeto Registro la fecha de entrada del documento.
	 * @param dataentrada String
	 * @throws RemoteException
	 */
	public void setdataentrada(String dataentrada) throws RemoteException;
	public void setDataVisado(String dataVisado) throws RemoteException;
	/** Fija en el objeto Registro la descripcion del tipo de documento.
	 * @param descripcionDocumento String
	 * @throws RemoteException
	 */
	public void setDescripcionDocumento(String descripcionDocumento) throws RemoteException;
	/** Fija en el objeto Registro la descripcion del idioma del documento.
	 * @param descripcionIdiomaDocumento String
	 * @throws RemoteException
	 */
	public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) throws RemoteException;
	/** Fija en el objeto Registro la descripcion de la oficina del documento.
	 * @param descripcionOficina String
	 * @throws RemoteException
	 */
	public void setDescripcionOficina(String descripcionOficina) throws RemoteException;
	/** Fija en el objeto Registro la descripcion del Organismo Destinatario del
	 * documento.
	 * @param descripcionOrganismoDestinatario String
	 * @throws RemoteException
	 */
	public void setDescripcionOrganismoDestinatario(String descripcionOrganismoDestinatario) throws RemoteException;
	/** Fija en el objeto Registro la descripcion del organismo remitente del documento.
	 * @param descripcionRemitente String
	 * @throws RemoteException
	 */
	public void setDescripcionRemitente(String descripcionRemitente) throws RemoteException;
	/** Fija en el objeto Registro el codigo del organismo destinatario del documento.
	 * @param destinatari String
	 * @throws RemoteException
	 */
	public void setdestinatari(String destinatari) throws RemoteException;
	/** Fija en el objeto Registro el numero de disquete del documento.
	 * @param disquet String
	 * @throws RemoteException
	 */
	public void setdisquet(String disquet) throws RemoteException;
	/** Fija en el objeto Registro el codigo de la Entidad remitente del documento.
	 * @param entidad1 String
	 * @throws RemoteException
	 */
	public void setentidad1(String entidad1) throws RemoteException;
	/** Fija en el objeto Registro el numero de la Entidad remitente del documento.
	 * @param entidad2 String
	 * @throws RemoteException
	 */
	public void setentidad2(String entidad2) throws RemoteException;
	
	/** Fija en el objeto Registro la procedencia geografica del documento si esta es de
	 * fuera de Baleares.
	 * @param fora String
	 * @throws RemoteException
	 */
	public void setfora(String fora) throws RemoteException;
	/** Fija en el objeto Registro la hora de entrada del documento.
	 * @param hora String
	 * @throws RemoteException
	 */
	public void sethora(String hora) throws RemoteException;
	/** Fija en el objeto Registro el codigo del idioma del extracto del documento.
	 * 1=Castellano, 2=Catalan.
	 * @param idioex String
	 * @throws RemoteException
	 */
	public void setidioex(String idioex) throws RemoteException;
	/** Fija en el objeto Registro el codigo del idioma del documento.
	 * @param idioma String
	 * @throws RemoteException
	 */
	public void setidioma(String idioma) throws RemoteException;
	/** Fija en el objeto Registro la descripcion del idioma del Extracto.
	 * @param idiomaExtracto String
	 * @throws RemoteException
	 */
	public void setIdiomaExtracto(String idiomaExtracto) throws RemoteException;
	/** Fija en el objeto Registro el numero de entrada del documento.
	 * @param numeroEntrada String
	 * @throws RemoteException
	 */
	public void setNumeroEntrada(String numeroEntrada) throws RemoteException;
	/** Fija en el objeto Registro el codigo de la oficina.
	 * @param oficina String
	 * @throws RemoteException
	 */
	public void setoficina(String oficina) throws RemoteException;
	/** Fija en el objeto Registro el codigo de la oficina.
	 * @param oficina String
	 * @throws RemoteException
	 */
	public void setoficinafisica(String oficinafisica) throws RemoteException;
	/** Fija en el objeto Registro el codigo del municipio 060.
	 * @param oficina String
	 * @throws RemoteException
	 */
	public void setMunicipi060(String oficina) throws RemoteException;
	/** Fija en el objeto Registro la descripcion de la procedencia geografica del
	 * documento, independientemente de que sea de Baleares o no.
	 * @param procedenciaGeografica String
	 * @throws RemoteException
	 */
	public void setProcedenciaGeografica(String procedenciaGeografica) throws RemoteException;
	/** Fija en el objeto Registro el numero de localizador del documento (numero de
	 * salida 1).
	 * @param salida1 String
	 * @throws RemoteException
	 */
	public void setsalida1(String salida1) throws RemoteException;
	/** Fija en el objeto Registro el año localizador del documento (numero de
	 * salida 2).
	 * @param descruipcionOficina String
	 * @throws RemoteException
	 */
	public void setsalida2(String descripcionOficina) throws RemoteException;
	/** Fija en el objeto Registro el tipo de documento.
	 * @param tipo String
	 * @throws RemoteException
	 */
	public void settipo(String tipo) throws RemoteException;
	/** Validamos el objeto Registro. Devuelve un boolean indicando si se ha validado
	 * correctamente.
	 * @throws RemoteException
	 * @return boolean
	 */
	public boolean validar() throws RemoteException;
	public boolean getLeido() throws RemoteException;
	public void setMotivo(String motivo) throws RemoteException;
	public void setEntidad1Nuevo(String entidad1) throws RemoteException;
	public void setEntidad2Nuevo(String entidad2) throws RemoteException;
	public void setAltresNuevo(String altres) throws RemoteException;
	public void setComentarioNuevo(String comentario) throws RemoteException;
	public String getEntidad1Nuevo() throws RemoteException;
	public String getEntidad2Nuevo() throws RemoteException;
	public String getAltresNuevo() throws RemoteException;
	public String getComentarioNuevo() throws RemoteException;
	public String getMotivo() throws RemoteException;
	public String getEntidad1Grabada() throws RemoteException;
	
	public void setBOIBdata(String BOIBdata) throws RemoteException;
	public void setBOIBnumeroBOCAIB(int BOIBnumeroBOCAIB) throws RemoteException;
	public void setBOIBpagina(int BOIBpagina) throws RemoteException;
	public void setBOIBlineas(int BOIBlineas) throws RemoteException;
	public void setBOIBtexto(String BOIBtexto) throws RemoteException;
	public void setBOIBobservaciones(String BOIBobservaciones) throws RemoteException;
	
	
	/**
	 * Aquest mètode obté la data del servidor de temps de l'autoritat de firma.
	 * El certificat, el content-type i la contraseÃ±a de la firma s'obtenen del descriptor ejb-jar.xml
	 * Aquest mètode NO dona d'alta al registre d'entrades.
	 * @throws java.rmi.RemoteException
	 * @return String amb la data del servidor de temps de tradise
	 * @author Sebastià Matas Riera.
	 */
	
    // public String TestTS( ) throws RemoteException, SQLException, ClassNotFoundException, NamingException, Exception;
	
	/**
	 * Actualiza el objeto Registro en la base de datos y obtiene el acuse de recibo en formato XML
	 * y la firma electrÃ³nica de dicho recibo utilizando la API de firma electrÃ³nica de la CAIB
	 * El certificado, el content-type y la contraseÃ±a de la firma se obtienen del fichero descriptor
	 * ejb-jar.xml
	 * Es el mÃ©todo a utilizar cuando se quieran registrar documentos electrÃ³nicos, aunque permite
	 * realizar el registro y obtener el acuse de recibo firmado de las entradas sin documentos adjuntos en formato
	 * electrÃ³nico
	 * @throws java.rmi.RemoteException
	 * @return Signature objeto de firma electrÃ³nica de la API Signatura
	 * @param signatureStream <code>OutputStream</code> con la firma electrÃ³nica serializada en un stream para su posterior almacenaje o transmisiÃ³n
	 * @param smime <code>OutputStream</code> con el fichero de acuse de recibo y la firma electrÃ³nica empaquetados en un fichero SMIME
	 * @param rebut <code>OutputStream</code> con el fichero acuse de recibo en formato XML
	 * @param documentName <code>Vector</code> de String con los nombres de los documentos presentados en el registro. Puede ser null
	 * @param documentHash <code>Vector</code> de String con el hashing de los documentos presentados en el registro. Puede ser null
	 * @param hashingAlg nombre del algoritmo de hashing utilizado para pasar el hash de los documentos. Puede ser null o cadena vacÃ­a
	 * @author JesÃºs Reyes (3dÃ­gits)
	 */
	//public Signature grabarConFirma(String hashingAlg, Vector documentHash, Vector documentName, OutputStream rebut, OutputStream smime, OutputStream signatureStream) throws RemoteException, SQLException, ClassNotFoundException, NamingException, Exception;
	
}
