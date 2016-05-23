package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * @author anadal
 * Date: 6/03/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface AnexoLocal extends BaseEjb<Anexo, Long> {

  
  public AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
      Long registroID, String tipoRegistro) throws I18NException, I18NValidationException;
    

  
  public AnexoFull getAnexoFull(Long anexoID) throws I18NException;

    /**
     * Método que levanta todos los anexos completo con el archivo de custodia.
     *
     * @param anexoID
     * @return
     * @throws I18NException
     */
    public AnexoFull getAnexoFullCompleto(Long anexoID) throws I18NException;
  
  
  
  public AnexoFull actualizarAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
      Long registroID, String tipoRegistro) throws I18NException, I18NValidationException;
    
  
  
  
  /**
   *  Eliminar un anexo
   * @param idAnexo
   * @param idRegistroDetalle
   * @return
   * @throws Exception
   */
    /* public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle) throws Exception;*/

    /**
      *
      * @param anexo
      * @return
      * @throws Exception
      */
    //public boolean actualizarAnexo(Anexo anexo) throws Exception;

  /**
   * Actualiza un anexo y guarda sus archivos asociados
   * @param idAnexo
   * @param ficheroAnexado
   * @param nombreFicheroAnexado
   * @param tipoMIMEFicheroAnexado
   * @param tamanoFicheroAnexado
   * @param firmaAnexada
   * @param nombreFirmaAnexada
   * @param tipoMIMEFirmaAnexada
   * @param tamanoFirmaAnexada
   * @param modoFirma
   * @param fechaCaptura
   * @return
   * @throws Exception
   */
   /* public Anexo actualizarAnexoConArchivos(Long idAnexo, byte[] ficheroAnexado,String nombreFicheroAnexado, String tipoMIMEFicheroAnexado, Long tamanoFicheroAnexado,
                               byte[] firmaAnexada, String nombreFirmaAnexada, String tipoMIMEFirmaAnexada, Long tamanoFirmaAnexada,
                               Integer modoFirma, Date fechaCaptura ) throws Exception;
                               */


    /**
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroEntrada(Long idRegistro) throws Exception;

    /**
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroSalida(Long idRegistro) throws Exception;

    /**
     *  Obtiene los anexos de un registroDetalle
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    public IDocumentCustodyPlugin getInstance() throws Exception;

    /**
     * Obtiene el fichero existente en el sistema de archivos
     *
     * @param custodiaID
     * @return
     */

    public DocumentCustody getArchivo(String custodiaID) throws Exception;

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
     * Solo elimina el archivo asociado al documento.
     *
     * @param custodiaID
     * @return
     * @throws Exception
     */
    public boolean eliminarDocumento(String custodiaID) throws Exception;

    /**
     * Solo elimina la el archivo asociado a la firma
     *
     * @param custodiaID
     * @return
     * @throws Exception
     */
    public boolean eliminarFirma(String custodiaID) throws Exception;

    /**
     * Crea o actualiza un anexos en el sistema de custodia
     *
     * @param name
     * @param file
     * @param signatureName
     * @param signature
     * @param signatureMode
     * @param custodyID         Si vale null significa que creamos el archivo. Otherwise actualizamos el fichero.
     * @param custodyParameters JSON del registre
     * @return Identificador de custodia
     * @throws Exception
     */
    public String crearArchivo(String name, byte[] file, String signatureName,
                               byte[] signature, int signatureMode, String custodyID, String custodyParameters) throws Exception;


}
