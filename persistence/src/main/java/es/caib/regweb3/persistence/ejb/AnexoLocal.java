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

  
  public AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
      Long registroID, String tipoRegistro) throws I18NException, I18NValidationException;


  public AnexoFull crearJustificanteAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                              Long registroID, String tipoRegistro, String custodyID) throws I18NException, I18NValidationException;


    /**
     * Método que levanta la información de los anexos menos los archivos físicos.
     * @param anexoID
     * @return
     * @throws I18NException
     */
  public AnexoFull getAnexoFullLigero(Long anexoID) throws I18NException;

  /**
   * Método que levanta los archivos
   *
   * @param anexoID
   * @return
   * @throws I18NException
   */
  public AnexoFull getAnexoFull(Long anexoID) throws I18NException;

  
  
  
  public AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
      Long registroID, String tipoRegistro, boolean isJustificante,boolean noWeb) throws I18NException, I18NValidationException;
    


    /**
     * Método que devuelve todos los anexos de un registro de entrada sin el justificante
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public List<AnexoFull> getByRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     * Método que devuelve todos los anexos de un registro de salida sin el justificante
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public List<AnexoFull> getByRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException;

    /**
     *  Obtiene los anexos de un registroDetalle
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    /**
    *
    * @param idRegistroDetalle
    * @return
    * @throws Exception
    */
    public List<Anexo> getByRegistroDetalleLectura(Long idRegistroDetalle) throws Exception;

    /**
     *  Obtiene el id del Justificante que tiene un registroDetalle
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public Long getIdJustificante(Long idRegistroDetalle) throws Exception;

    
    public byte[] getArchivoContent(String custodiaID, boolean isJustificante) throws Exception;
    

    public byte[] getFirmaContent(String custodiaID, boolean isJustificante) throws Exception;
    

    /**
     * Obtiene el fichero existente en el sistema de archivos
     * @param custodiaID
     * @param isJustificante
     * @return
     */

    public DocumentCustody getArchivo(String custodiaID, boolean isJustificante) throws Exception;

    
    
    public DocumentCustody getDocumentInfoOnly(String custodiaID) throws Exception;
    
    
    public SignatureCustody getSignatureInfoOnly(String custodiaID) throws Exception;
    
    /**
     * Obtiene la firma existente en el sistema de archivos
     * @param custodiaID
     * @param isJustificante
     * @return
     */
    public SignatureCustody getFirma(String custodiaID, boolean isJustificante) throws Exception;

    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     *
     * @param custodiaID
     * @param isJustificante
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    public boolean eliminarCustodia(String custodiaID, boolean isJustificante) throws Exception;


    /**
     * Crea un Jusitificante como anexo al registro
     * @param usuarioEntidad
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    public AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro,
        String tipoRegistro, String idioma) throws I18NException, I18NValidationException;



}
