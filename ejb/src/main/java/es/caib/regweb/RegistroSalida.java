package es.caib.regweb;

import java.rmi.*;
import javax.ejb.*;
import java.util.*;
import java.sql.SQLException;

/**
 * Interfície per accedir al bean que gestiona el registre de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */


public interface RegistroSalida extends EJBObject {
	
	public void setActualizacion(boolean actualizacion) throws RemoteException;
	
	/** Fijamos el usuario en el objeto RegistroSalida. Este es pasado como un String que se
	 * convierte a mayusculas.
	 * @param String usuario
	 * @throws RemoteException
	 */
	public void fijaUsuario(String usuario) throws RemoteException;
	public void fijaPasswordUser(String password) throws RemoteException;
	
	/** Recuperamos en un String desde el objeto Registro si esta anulado
	 * @throws RemoteException
	 * @return String
	 */
	public String getRegistroAnulado() throws RemoteException;
	
	/** Recuperamos en un String desde el objeto RegistroSalida la Entidad Remitente si es
	 * <I>Altres</I>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getAltres() throws RemoteException;
	
	/** Recuperamos en un String desde el objeto RegistroSalida numero de correo
	 * @throws RemoteException
	 * @return String
	 */
	public String getCorreo() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el año de salida.
	 * @throws RemoteException
	 * @return String
	 */
	public String getAnoSalida() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el codigo de la procedencia
	 * geografica si esta es de Baleares.
	 * @throws RemoteException
	 * @return String
	 */
	public String getBalears() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el extracto del documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getComentario() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la fecha del documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getData() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la fecha de salida del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDataSalida() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion del tipo
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionDocumento() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion del idioma del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionIdiomaDocumento() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion de la oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionOficina() throws RemoteException;
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion de la oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionOficinaFisica() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion del Organismo
	 * destinatario del documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionOrganismoRemitente() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion del Destinatario,
	 * independientemente si corresponde a una <I>Entidad</I> o a <I>Altres</I>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDescripcionDestinatario() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el Organismo remitente del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getRemitent() throws RemoteException;
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de disquete del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDisquet() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el codigo de la Entidad del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getEntidad1() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de la Entidad del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getEntidad2() throws RemoteException;
	
	
	/** Recuperamos en una Hashtable desde el objeto RegistroSalida con los errores
	 * encontrados durante la validacion del objeto RegistroSalida. Esta Hashtable, la clave
	 * es el campo que ha generado el error, acompañado de una descripcion del mismo.
	 * @throws RemoteException
	 * @return Hashtable con errores.
	 */
	public Hashtable getErrores() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el destino geografico del
	 * documento si es de fuera de Baleares.
	 * @throws RemoteException
	 * @return String
	 */
	public String getFora() throws RemoteException;
	
	
	/** Recuperamos en un boolean desde el objeto RegistroSalida si este se ha grabado
	 * correctamente en la Base de Datos. True, si se ha grabado y false si no se ha
	 * grabado.
	 * @throws RemoteException
	 * @return boolean
	 */
	public boolean getGrabado() throws RemoteException;
	public boolean getActualizado() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la hora y los minutos en los
	 * que se grabo el documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getHora() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el idioma del extracto.
	 * 1=Castellano, 2=Catalan.
	 * @throws RemoteException
	 * @return String
	 */
	public String getIdioex() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el codigo del idioma del
	 * documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getIdioma() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida la descripcion del idioma del
	 * extracto.
	 * @throws RemoteException
	 * @return String
	 */
	public String getIdiomaExtracto() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de salida con el
	 * que se ha grabado el documento en el metodo <B>grabar()</B>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getNumero() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de salida del
	 * documento durante el metodo <B>leer()</B>.
	 * @throws RemoteException
	 * @return String
	 */
	public String getNumeroSalida() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getOficina() throws RemoteException;
	
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de oficina.
	 * @throws RemoteException
	 * @return String
	 */
	public String getOficinafisica() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el destino geografico
	 * independientemente de que este sea de fuera o de Baleares.
	 * @throws RemoteException
	 * @return String
	 */
	public String getDestinoGeografico() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el numero de localizador del
	 * documento (numero de entrada 1).
	 * @throws RemoteException
	 * @return String
	 */
	public String getEntrada1() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroEntrada el año localizador del
	 * documento (numero de entrada 2).
	 * @throws RemoteException
	 * @return String
	 */
	public String getEntrada2() throws RemoteException;
	
	
	/** Recuperamos en un String desde el objeto RegistroSalida el tipo de documento.
	 * @throws RemoteException
	 * @return String
	 */
	public String getTipo() throws RemoteException;
	
	
	/** Recuperamos en un boolean desde el objeto RegistroSalida si el proceso de validacion
	 * ha finalizado satisfactoriamente.
	 * @throws RemoteException
	 * @return String
	 */
	public boolean getValidado() throws RemoteException;
	
	
	/** Grabamos el objeto RegistroSalida en la base de datos.
	 * @throws RemoteException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void grabar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	public void actualizar() throws RemoteException, SQLException, ClassNotFoundException, Exception;
	
	/** Recuperamos de la base de datos un registro de entrada junto con los valores pertenecientes
	 * a las tablas auxiliares.
	 * @throws RemoteException
	 */
	public void leer() throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la Entidad destiataria si esta es <I>Altres</I>.
	 * @param String Entidad Remitente
	 * @throws RemoteException
	 */
	public void setaltres(String altres) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el año de salida del documento.
	 * @param anoEntrada String
	 * @throws RemoteException
	 */
	public void setAnoSalida(String anoSalida) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el codigo de destino geográfico si es de
	 * Baleares.
	 * @param balears String
	 * @throws RemoteException
	 */
	public void setbalears(String balears) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el extracto del documento.
	 * @param comentario String
	 * @throws RemoteException
	 */
	public void setcomentario(String comentario) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la fecha del documento.
	 * @param data String
	 * @throws RemoteException
	 */
	public void setdata(String data) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la fecha de salida del documento.
	 * @param dataentrada String
	 * @throws RemoteException
	 */
	public void setdatasalida(String datasalida) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la descripcion del tipo de documento.
	 * @param descripcionDocumento String
	 * @throws RemoteException
	 */
	public void setDescripcionDocumento(String descripcionDocumento) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la descripcion del idioma del documento.
	 * @param descripcionIdiomaDocumento String
	 * @throws RemoteException
	 */
	public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la descripcion de la oficina del documento.
	 * @param descripcionOficina String
	 * @throws RemoteException
	 */
	public void setDescripcionOficina(String descripcionOficina) throws RemoteException;
	
	/** Fija en el objeto Registro registro anulado
	 * @param data String
	 * @throws RemoteException
	 */
	public void setRegistroAnulado(String registroAnulado) throws RemoteException;
	
	/** Fija en el objeto RegistroSalida la descripcion del Organismo Remitente del
	 * documento.
	 * @param descripcionOrganismoDestinatario String
	 * @throws RemoteException
	 */
	public void setDescripcionOrganismoRemitente(String descripcionOrganismoRemitente) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la descripcion del organismo destinatario del documento.
	 * @param descripcionRemitente String
	 * @throws RemoteException
	 */
	public void setDescripcionDestinatario(String descripcionDestinatario) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el codigo del organismo remitente del documento.
	 * @param remitent String
	 * @throws RemoteException
	 */
	public void setremitent(String remitent) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el numero de disquete del documento.
	 * @param disquet String
	 * @throws RemoteException
	 */
	public void setdisquet(String disquet) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el codigo de la Entidad destinataria del documento.
	 * @param entidad1 String
	 * @throws RemoteException
	 */
	public void setentidad1(String entidad1) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el numero de la Entidad destinataria del documento.
	 * @param entidad2 String
	 * @throws RemoteException
	 */
	public void setentidad2(String entidad2) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el destino geográfico del documento si este es de
	 * fuera de Baleares.
	 * @param fora String
	 * @throws RemoteException
	 */
	public void setfora(String fora) throws RemoteException;
	
	/** Fija en el objeto RegistroSalida la hora de salida del documento.
	 * @param hora String
	 * @throws RemoteException
	 */
	public void sethora(String hora) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el codigo del idioma del extracto del documento.
	 * 1=Castellano, 2=Catalan.
	 * @param idioex String
	 * @throws RemoteException
	 */
	public void setidioex(String idioex) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el codigo del idioma del documento.
	 * @param idioma String
	 * @throws RemoteException
	 */
	public void setidioma(String idioma) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la descripcion del idioma del Extracto.
	 * @param idiomaExtracto String
	 * @throws RemoteException
	 */
	public void setIdiomaExtracto(String idiomaExtracto) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el numero de salida del documento.
	 * @param numeroSalida String
	 * @throws RemoteException
	 */
	public void setNumeroSalida(String numeroSalida) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el codigo de la oficina.
	 * @param oficina String
	 * @throws RemoteException
	 */
	public void setoficina(String oficina) throws RemoteException;
	
	/** Fija en el objeto RegistroSalida el codigo de la oficina.
	 * @param oficina String
	 * @throws RemoteException
	 */
	public void setoficinafisica(String oficinafisica) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida la descripcion del destino geografico del
	 * documento, independientemente de que sea de Baleares o no.
	 * @param procedenciaGeografica String
	 * @throws RemoteException
	 */
	public void setDestinoGeografico(String destinoGeografico) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el numero de localizador del documento (numero de
	 * entrada 1).
	 * @param entrada1 String
	 * @throws RemoteException
	 */
	public void setentrada1(String entrada1) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el año localizador del documento (numero de
	 * entrada 2).
	 * @param descruipcionOficina String
	 * @throws RemoteException
	 */
	public void setentrada2(String descruipcionOficina) throws RemoteException;
	
	
	/** Fija en el objeto RegistroSalida el numero de correo
	 * @param tipo String
	 * @throws RemoteException
	 */
	public void setCorreo(String correo) throws RemoteException;
	
	/** Fija en el objeto RegistroSalida el tipo de documento.
	 * @param tipo String
	 * @throws RemoteException
	 */
	public void settipo(String tipo) throws RemoteException;
	
	/** Validamos el objeto RegistroSalida. Devuelve un boolean indicando si se ha validado
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
	public String getDataVisado() throws RemoteException;
	public void setDataVisado(String dataVisado) throws RemoteException;
	
}
