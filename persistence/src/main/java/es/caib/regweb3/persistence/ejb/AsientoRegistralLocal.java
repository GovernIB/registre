package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.JustificanteReferencia;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 06/03/2019
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface AsientoRegistralLocal {

    /**
     * Cra un neuvo UsuarioEntidad haciendo uso de @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
     *
     * @param identificador
     * @param idEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws Exception, I18NException;

    /**
     * Registra una salida haciendo uso de @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @param interesados
     * @param anexos
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                   UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Registra una entrada haciendo uso de @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @param interesados
     * @param anexos
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Se crea el Justificante del Registro de manera Asincrona
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @throws I18NValidationException
     * @throws I18NException
     */
    void crearJustificante(UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NValidationException, I18NException;

    /**
     * Distribuye un registro de entrada de manera Asincrona
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     * @throws I18NException
     */
    void distribuirRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;

    /**
     * Obtiene la referencia del justificante de un Registro
     *
     * @param numeroRegistroformateado
     * @param entidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    JustificanteReferencia obtenerReferenciaJustificante(String numeroRegistroformateado, Entidad entidad) throws Exception, I18NException;

    /**
     * Procesa el Registro de Salida creado según el TipoOperación indicado
     *
     * @param tipoOperacion
     * @param registroSalida
     * @return
     * @throws I18NException
     * @throws Exception
     * @throws I18NValidationException
     */
    RegistroSalida procesarRegistroSalida(Long tipoOperacion, RegistroSalida registroSalida) throws I18NException, Exception, I18NValidationException;
}

