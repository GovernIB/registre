package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroDetalle;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface InteresadoLocal extends BaseEjb<Interesado, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/InteresadoEJB";


    /**
     * Guarda una interesado normalizando algunos campos
     *
     * @param interesado
     * @return
     * @throws I18NException
     */
    Interesado guardarInteresado(Interesado interesado) throws I18NException;

    /**
     * Obtiene los Interesados de un RegistroDetalle
     *
     * @param registroDetalle
     * @return
     * @throws I18NException
     */
    List<Interesado> findByRegistroDetalle(Long registroDetalle) throws I18NException;

    /**
     * Obtiene un Interesado a partir del CodigoDir3 y Su registroDetalle
     *
     * @param codigoDir3
     * @param idRegistroDetalle
     * @return
     * @throws I18NException
     */
    Interesado findByCodigoDir3RegistroDetalle(String codigoDir3, Long idRegistroDetalle) throws I18NException;

    /**
     * Eliminanos el Interesado del RegistroDetalle y luego de la bbdd
     *
     * @param idInteresado
     * @param idRegistroDetalle
     * @throws I18NException
     */
    void eliminarInteresadoRegistroDetalle(Long idInteresado, Long idRegistroDetalle) throws I18NException;

    /**
     * * Comprueba la existencia de un Documento en el sistema
     *
     * @param documento
     * @return
     * @throws I18NException
     */
    Boolean existeDocumentoNew(String documento) throws I18NException;

    /**
     * Comprueba la existencia de un Documento en el sistema para la edición de un Interesado
     *
     * @param documento
     * @param idInteresado
     * @return
     * @throws I18NException
     */
    Boolean existeDocumentoEdit(String documento, Long idInteresado) throws I18NException;

    /**
     * Comprueba si un RegistroDetalle tiene algún Interesado de Tipo Administración
     *
     * @param idRegistroDetalle
     * @return
     * @throws I18NException
     */
    String existeInteresadoAdministracion(Long idRegistroDetalle) throws I18NException;

    /**
     * Guardamos todos los Interesados de un Registro de Entrada o Salida
     *
     * @param interesadosSesion
     * @param registroDetalle
     * @return
     * @throws I18NException
     */
    List<Interesado> guardarInteresados(List<Interesado> interesadosSesion, RegistroDetalle registroDetalle) throws I18NException;

    /**
     * Método que invoca al plugin de post proceso cuando se crea un interesado. Se indica el tipo de registro y el numero de registro
     *
     * @param interesado
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    void postProcesoNuevoInteresado(Interesado interesado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws I18NException;

    /**
     * Método que invoca al plugin de post proceso cuando se actualiza un interesado. Se indica el tipo de registro y el numero de registro
     *
     * @param interesado
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    void postProcesoActualizarInteresado(Interesado interesado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws I18NException;

    /**
     * Método que invoca al plugin de post proceso cuando se elimina un interesado. Se indica el tipo de registro y el numero de registro
     *
     * @param idInteresado
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    void postProcesoEliminarInteresado(Long idInteresado, Long idRegistroDetalle, Long tipoRegistro, Long entidadId) throws I18NException;

    /**
     * @throws I18NException
     */
    void capitalizarInteresadosFisicas() throws I18NException;

    /**
     * @throws I18NException
     */
    void capitalizarInteresadosJuridicos() throws I18NException;
}
