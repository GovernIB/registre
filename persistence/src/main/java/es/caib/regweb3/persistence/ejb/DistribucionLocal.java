package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface DistribucionLocal {

    /**
     * Método que obtiene los destinatarios a los que distribuir el registro
     *
     * @param re registro de entrada a distribuir
     * @param usuarioEntidad
     * @return lista de destinatarios a los que se debe distribuir el registro
     * @throws Exception
     * @throws I18NException
     */
    RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException;

    /**
     * Procesar los registros(varios) que estan en la cola
     *
     * @param idEntidad
     * @throws Exception
     */
    void procesarRegistrosEnCola(Long idEntidad) throws Exception;

    /**
     * Envia un email con los errores de la cola de distribución a los Administradores de la Entidad
     * @param entidad
     * @throws Exception
     */
    void enviarEmailErrorDistribucion(Entidad entidad) throws Exception;


    /**
     * Procesa un registro de la cola de manera individual
     *
     * @param idObjeto
     * @param idEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    Boolean procesarRegistroEnCola(Long idObjeto, Long idEntidad, Long tipoIntegracion) throws Exception;
}
