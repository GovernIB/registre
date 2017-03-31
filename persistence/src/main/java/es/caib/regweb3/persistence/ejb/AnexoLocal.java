package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Anexo;
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

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

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
      Long registroID, String tipoRegistro) throws I18NException, I18NValidationException;
    


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

    
    public byte[] getArchivoContent(String custodiaID) throws Exception;
    

    public byte[] getFirmaContent(String custodiaID) throws Exception;
    

    /**
     * Obtiene el fichero existente en el sistema de archivos
     *
     * @param custodiaID
     * @return
     */

    public DocumentCustody getArchivo(String custodiaID) throws Exception;

    
    
    public DocumentCustody getDocumentInfoOnly(String custodiaID) throws Exception;
    
    
    public SignatureCustody getSignatureInfoOnly(String custodiaID) throws Exception;
    
    /**
     * Obtiene la firma existente en el sistema de archivos
     * @param custodiaID
     * @return
     */
    public SignatureCustody getFirma(String custodiaID) throws Exception;

    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     *
     * @param custodiaID
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    public boolean eliminarCustodia(String custodiaID) throws Exception;


    /**
     * Crea un Jusitificante como anexo al registro
     * @param baos
     * @param idEntidad
     * @param nombreFichero
     * @param usuarioEntidad
     * @param idRegistro
     * @param locale
     * @param tituloAnexo
     * @param observacionesAnexo
     * @param tipoRegistro
     * @return Boolean
     * @throws Exception
     */
    public Boolean crearJustificante(ByteArrayOutputStream baos, Long idEntidad, String nombreFichero,
                                     UsuarioEntidad usuarioEntidad, Long idRegistro, Locale locale, String tituloAnexo,
                                     String observacionesAnexo, String tipoRegistro) throws Exception;



}
