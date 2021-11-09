package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroDetalle;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface InteresadoLocal extends BaseEjb<Interesado, Long> {

    /**
     * Guarda una interesado normalizando algunos campos
     * @param interesado
     * @return
     * @throws Exception
     */
    Interesado guardarInteresado(Interesado interesado) throws Exception;

    /**
     * Obtiene los Interesados de un RegistroDetalle
     * @param registroDetalle
     * @return
     * @throws Exception
     */
    List<Interesado> findByRegistroDetalle(Long registroDetalle) throws Exception;

    /**
     * Obtiene un Interesado a partir del CodigoDir3 y Su registroDetalle
     * @param codigoDir3
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    Interesado findByCodigoDir3RegistroDetalle(String codigoDir3, Long idRegistroDetalle) throws Exception;

    /**
     * Eliminanos el Interesado del RegistroDetalle y luego de la bbdd
     * @param idInteresado
     * @param idRegistroDetalle
     * @throws Exception
     */
    void eliminarInteresadoRegistroDetalle(Long idInteresado, Long idRegistroDetalle) throws Exception;

    /**
     * * Comprueba la existencia de un Documento en el sistema
     * @param documento
     * @return
     * @throws Exception
     */
    Boolean existeDocumentoNew(String documento) throws Exception;

    /**
     * Comprueba la existencia de un Documento en el sistema para la edición de un Interesado
     * @param documento
     * @param idInteresado
     * @return
     * @throws Exception
     */
    Boolean existeDocumentoEdit(String documento, Long idInteresado) throws Exception;

    /**
     * Comprueba si un RegistroDetalle tiene algún Interesado de Tipo Administración
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    String existeInteresadoAdministracion(Long idRegistroDetalle) throws Exception;

    /**
     * Guardamos todos los Interesados de un Registro de Entrada o Salida
     * @param interesadosSesion
     * @param registroDetalle
     * @return
     * @throws Exception
     */
    List<Interesado> guardarInteresados(List<Interesado> interesadosSesion, RegistroDetalle registroDetalle) throws Exception;

    /**
     * Método que invoca al plugin de post proceso cuando se crea un interesado. Se indica el tipo de registro y el numero de registro
     * @param interesado
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    void postProcesoNuevoInteresado(Interesado interesado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws Exception, I18NException;

    /**
     * Método que invoca al plugin de post proceso cuando se actualiza un interesado. Se indica el tipo de registro y el numero de registro
     * @param interesado
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    void postProcesoActualizarInteresado(Interesado interesado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws Exception, I18NException;

    /**
     * Método que invoca al plugin de post proceso cuando se elimina un interesado. Se indica el tipo de registro y el numero de registro
     * @param idInteresado
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    void postProcesoEliminarInteresado(Long idInteresado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws Exception, I18NException;

    /**
     *
     * @throws Exception
     */
    void capitalizarInteresadosFisicas() throws Exception;

    /**
     *
     * @throws Exception
     */
    void capitalizarInteresadosJuridicos() throws Exception;
}
