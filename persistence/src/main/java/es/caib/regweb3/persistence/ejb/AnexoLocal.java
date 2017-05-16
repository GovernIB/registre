package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
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
  public AnexoFull getAnexoFull(Long anexoID) throws I18NException;

  /**
   * Método que levanta los archivos
   *
   * @param anexoID
   * @return
   * @throws I18NException
   */
  public AnexoFull getAnexoFullCompleto(Long anexoID) throws I18NException;

  
  
  
  public AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
      Long registroID, String tipoRegistro, boolean isJustificante) throws I18NException, I18NValidationException;
    


    /**
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroEntrada(RegistroEntrada registroEntrada) throws Exception;

    /**
     *
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroSalida(RegistroSalida registroSalida) throws Exception;

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
     * @param idRegistro
     * @param tipoRegistro
     * @param baos
     * @param custodyID
     * @param csv
     * @return
     * @throws Exception
     */
    /*
    public AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, Long idRegistro, String tipoRegistro,
        byte[] baos, String custodyID, String csv) throws Exception;
        */
    public AnexoFull crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro,
        String tipoRegistro, String idioma) throws I18NException, I18NValidationException;
    


}
