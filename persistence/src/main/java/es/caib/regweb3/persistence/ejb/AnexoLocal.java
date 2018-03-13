package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * @author anadal
 * @author anadal (Adaptació DocumentCustody 3.0.0)
 * Date: 6/03/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface AnexoLocal extends BaseEjb<Anexo, Long> {

  
  AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                       Long registroID, String tipoRegistro) throws I18NException, I18NValidationException;


  AnexoFull crearJustificanteAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                   Long registroID, String tipoRegistro, String custodyID) throws I18NException, I18NValidationException;


    /**
     * Método que levanta la información de los anexos menos los archivos físicos.
     * @param anexoID
     * @return
     * @throws I18NException
     */
    AnexoFull getAnexoFullLigero(Long anexoID, Long idEntidad) throws I18NException;

  /**
   * Método que levanta toda la información de los anexos incluidos los archivos físicos
   *
   * @param anexoID
   * @param idEntidad
   * @return
   * @throws I18NException
   */
  AnexoFull getAnexoFull(Long anexoID, Long idEntidad) throws I18NException;

  
  
  
  AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                            Long registroID, String tipoRegistro, boolean isJustificante, boolean noWeb) throws I18NException, I18NValidationException;
    


    /**
     * Método que devuelve todos los anexos de un registro de entrada sin el justificante
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    List<AnexoFull> getByRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     * Método que devuelve todos los anexos de un registro de salida sin el justificante
     * @param registroSalida
     * @return
     * @throws Exception
     */
    List<AnexoFull> getByRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException;

    /**
     *  Obtiene los anexos de un registroDetalle
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    /**
    *
    * @param idRegistroDetalle
    * @return
    * @throws Exception
    */
    List<Anexo> getByRegistroDetalleLectura(Long idRegistroDetalle) throws Exception;
  /**
   * Elimina los anexos de custodia del registro detalle indicado menos el justificante
   * @param idRegistroDetalle
   * @return
   * @throws Exception
   */
    //TODO PENDIENTE CERRAR CON FELIP
   // void eliminarAnexosCustodiaRegistroDetalle(Long idRegistroDetalle) throws Exception, I18NException;


    /**
     *  Obtiene el id del Justificante que tiene un registroDetalle
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    Long getIdJustificante(Long idRegistroDetalle) throws Exception;

     /**
     * Obtiene el contenido físico del documento como byte[]
     *
     * @param custodiaID
     * @return
     */
    byte[] getArchivoContent(String custodiaID, boolean isJustificante, Long idEntidad) throws I18NException, Exception;


    /**
     * Obtiene el contenido físico de la firma como byte[]
     *
     * @param custodiaID
     * @return
     */
    byte[] getFirmaContent(String custodiaID, boolean isJustificante, Long idEntidad) throws Exception, I18NException;


    /**
     * Obtiene la info + contenido físico(byte[]) del fichero existente en el sistema de archivos
     *
     * @param custodiaID
     * @return
     */
    DocumentCustody getArchivo(String custodiaID, boolean isJustificante, Long idEntidad) throws I18NException, Exception;


    /**
     * Obtiene solo la info del Documento (sin byte[])
     * @param custodiaID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    DocumentCustody getDocumentInfoOnly(String custodiaID, Long idEntidad) throws Exception, I18NException;


    /**
     * Obtiene solo la info de la firma (sin byte[])
     * @param custodiaID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    SignatureCustody getSignatureInfoOnly(String custodiaID, Long idEntidad) throws Exception, I18NException;

  /**
   *
   * @param custodiaID
   * @param isJustificante
   * @param idEntidad
   * @return
   * @throws Exception
   * @throws I18NException
   */
    SignatureCustody getSignatureInfoOnly(String custodiaID, boolean isJustificante, Long idEntidad) throws Exception, I18NException;

    /**
     * Obtiene la firma existente en el sistema de archivos
     * @param custodiaID
     * @param isJustificante
     * @return
     */
    SignatureCustody getFirma(String custodiaID, boolean isJustificante, Long idEntidad) throws I18NException, Exception;

    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     *
     * @param custodiaID
     * @param isJustificante
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    boolean eliminarCustodia(String custodiaID, boolean isJustificante, Long idEntidad) throws Exception, I18NException;


    /**
     * Crea un Jusitificante como anexo al registro
     * @param usuarioEntidad
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro,
                                String tipoRegistro, String idioma) throws I18NException, I18NValidationException;

    /**
     * Obtiene la url de validacion del documento. Si no soporta url, devuelve null
     * @param custodiaID
     * @param isJustificante
     * @return
     */
    String getUrlValidation(String custodiaID, boolean isJustificante, Long idEntidad) throws I18NException, Exception;

  /**
   * Obtiene el SignatureCustody de un Anexo
   * @param custodiaID
   * @param isJustificante
   * @return SignatureCustody
   */
    SignatureCustody descargarFirmaDesdeUrlValidacion(String custodiaID, boolean isJustificante, Long idEntidad) throws I18NException, Exception;

}
