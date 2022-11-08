package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.AnexoSimple;
import es.caib.regweb3.persistence.utils.Paginacion;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.MetadataFormatException;

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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA","RWE_WS_CIUDADANO"})
public interface AnexoLocal extends BaseEjb<Anexo, Long> {

    /**
     * @param anexoFull
     * @param usuarioEntidad
     * @param registroID
     * @param tipoRegistro
     * @param custodyID
     * @param validarAnexo
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    AnexoFull crearAnexo(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                         Long registroID, Long tipoRegistro, String custodyID, Boolean validarAnexo) throws I18NException, I18NValidationException;

    AnexoFull crearAnexoConfidencial(AnexoFull anexoFull, UsuarioEntidad usuarioEntidad,
                                            Long registroID, Long tipoRegistro) throws I18NException, I18NValidationException;


    /**
     * Método que levanta la información de los anexos menos los archivos físicos.
     *
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
                              Long registroID, Long tipoRegistro, boolean isJustificante, boolean noWeb) throws I18NException, I18NValidationException;


    /**
     * Método que devuelve todos los anexos de un registro de entrada sin el justificante
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    List<AnexoFull> getByRegistroEntrada(RegistroEntrada registroEntrada) throws Exception, I18NException;

    /**
     * Método que devuelve todos los anexos de un registro de salida sin el justificante
     *
     * @param registroSalida
     * @return
     * @throws Exception
     */
    List<AnexoFull> getByRegistroSalida(RegistroSalida registroSalida) throws Exception, I18NException;

    /**
     * Obtiene los anexos de un registroDetalle
     *
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    List<Anexo> getByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    /**
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    List<Anexo> getByRegistroDetalleLectura(Long idRegistroDetalle) throws Exception;

    /**
     * Método que elimina los anexos de todos los registros(E/S) con estado "REGISTRO_OFICIO_ACEPTADO" que son
     * los que han sido confirmados en destino al enviarlos via SIR.
     *
     * @param idEntidad
     * @param numElementos
     * @return
     * @throws Exception
     */
    void purgarAnexosRegistrosAceptados(Long idEntidad, Integer numElementos) throws Exception, I18NException;


    /**
     * Método que elimina los anexos asociados a registros que ya se han distribuido.
     * @param idEntidad
     * @param meses
     * @param numElementos
     * @throws Exception
     * @throws I18NException
     */
    void purgarAnexosRegistrosDistribuidos(Long idEntidad, Integer meses, Integer numElementos) throws Exception, I18NException;


    /**
     * Elimina el archivo físico de custodia pero dejamos la info del anexo en la tabla de anexos y lo marcamos como purgado.
     *
     * @param custodiaId
     * @param isJustificante
     * @param idEntidad
     * @throws Exception
     * @throws I18NException
     */
    void purgarAnexo(String custodiaId, boolean isJustificante, Long idEntidad) throws Exception, I18NException;


    /**
     * Obtiene los anexos distribuidos hace x meses que seran los candidatos a purgar.
     *
     * @param meses numero de meses
     * @param numElementos numero maximo de anexos que se purgaran en la iteración
     * @return
     * @throws Exception
     */
    List<String> obtenerCustodyIdAnexosDistribuidos(Integer meses, Integer numElementos) throws Exception;

    /**
     * Obtiene el id del Justificante que tiene un registroDetalle
     *
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    Long getIdJustificante(Long idRegistroDetalle) throws Exception;

    /**
     * Obtiene el contenido físico del documento como byte[]
     *
     * @param anexo
     * @param idEntidad
     * @return
     */
    byte[] getArchivoContent(Anexo anexo, Long idEntidad) throws I18NException, Exception;


    /**
     * Obtiene la info + contenido físico(byte[]) del fichero existente en el sistema de archivos
     *
     * @param anexo
     * @return
     */
    DocumentCustody getArchivo(Anexo anexo, Long idEntidad) throws I18NException, Exception;


    /**
     * Obtiene solo la info del Documento (sin byte[])
     *
     * @param custodiaID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    DocumentCustody getDocumentInfoOnly(String custodiaID, Long idEntidad) throws Exception, I18NException;


    /**
     * Obtiene solo la info de la firma (sin byte[])
     *
     * @param custodiaID
     * @return
     * @throws Exception
     * @throws I18NException
     */
    SignatureCustody getSignatureInfoOnly(String custodiaID, Long idEntidad) throws Exception, I18NException;

    /**
     * @param anexo
     * @param idEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    SignatureCustody getSignatureInfoOnly(Anexo anexo, Long idEntidad) throws Exception, I18NException;

    /**
     * Obtiene la firma existente en el sistema de archivos
     *
     * @param anexo
     * @param idEntidad
     * @return
     */
    SignatureCustody getFirma(Anexo anexo, Long idEntidad) throws I18NException, Exception;

    /**
     * Elimina completamente una custodia ( = elimicion completa de Anexo)
     *
     * @param custodiaID
     * @param anexo
     * @return true si l'arxiu no existeix o s'ha borrat. false en els altres
     * casos.
     */
    boolean eliminarCustodia(String custodiaID, Anexo anexo, Long idEntidad) throws Exception, I18NException;

    /**
     * Obtiene la url de validacion del documento. Si no soporta url, devuelve null
     *
     * @param anexo
     * @return
     */
    String getUrlValidation(Anexo anexo, Long idEntidad) throws I18NException, Exception;

    /**
     * Obtiene Url dela Web Validacion CSV. Si no soporta url, devuelve null
     * @param anexo
     * @param idEntidad
     * @return
     * @throws I18NException
     * @throws Exception
     */
    String getCsvValidationWeb(Anexo anexo, Long idEntidad) throws I18NException, Exception;

    /**
     * Obtiene un Anexo con firma attached desde la url de validación
     *
     * @param anexo
     * @param idEntidad
     * @return SignatureCustody
     */
    AnexoSimple descargarFirmaDesdeUrlValidacion(Anexo anexo, Long idEntidad) throws I18NException, Exception;

    /**
     * Descarga un Justificante desde la url de validación, si no está custodiado, descarga el original.
     * @param anexo
     * @param idEntidad
     * @return
     * @throws I18NException
     * @throws Exception
     */
    AnexoSimple descargarJustificante(Anexo anexo, Long idEntidad) throws I18NException, Exception;

    /**
     * Actualiza algunos metadatos de los anexos con información que se obtiene de GEISER
     * 
     * @param registro
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
	AnexoFull actualizarMetadatosAnexo(IRegistro registro, AnexoFull anexoFull, UsuarioEntidad usuarioEntidad) throws I18NException;
	
    /**
     * Descarga el justificante del registro de GEISER
     * 
     * @param registro
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
	AnexoSimple obtenerJustificanteGEISER(IRegistro registro, UsuarioEntidad usuarioEntidad) throws I18NException;

	/**
	 * Revisa la firma de los anexos procedentes de Sistra, algunos vienen firmados pero con el modoFirma = 0 y se guardan como NO firmados
	 * 
	 * @throws Exception
	 * @throws I18NException
	 * @throws MetadataFormatException 
	 * @throws NotSupportedCustodyException 
	 * @throws CustodyException 
	 */
	void actualizarAnexosSistraPendientesVerificacionFirma(Long idEntidad) throws I18NException, CustodyException, NotSupportedCustodyException, MetadataFormatException;

	/**
	 * Recupera anexos con una posible firma sin validar
	 * @param busqueda 
	 * @param idEntidad
	 * 
	 */
	Paginacion getPendientesVerificacionFirma(Anexo busqueda, Long idEntidad);

	/**
	 * Verifica y corrige la firma de un anexo
	 * 
	 * @throws Exception
	 * @throws I18NException
	 * @throws MetadataFormatException 
	 * @throws NotSupportedCustodyException 
	 * @throws CustodyException 
	 */
	void actualizarAnexoSistraPendienteVerificacionFirmaManual(Long idEntidad, Long idAnexo)
			throws I18NException, CustodyException, NotSupportedCustodyException, MetadataFormatException;
}
