package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
public interface DistribucionLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/DistribucionEJB";

    /**
     * Método que envia a distribuir el registro en función del valor de la propiedad envioCola de cada plugin.
     *
     * @param re registro de entrada a distribuir
     * @param usuarioEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad, IDistribucionPlugin plugin) throws Exception, I18NException, I18NValidationException;

    /**
     * Procesar los registros(varios) que estan en la cola
     *
     * @param entidad
     * @throws Exception
     */
    void distribuirRegistrosEnCola(Entidad entidad) throws Exception;

    /**
     * Envia un email con los errores de la cola de distribución a los Administradores de la Entidad
     * @param entidad
     * @throws Exception
     */
    void enviarEmailErrorDistribucion(Entidad entidad) throws Exception;


    /**
     * Procesa un registro de la cola de manera individual
     *
     * @param elemento
     * @param entidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    Boolean distribuirRegistroEnCola(Cola elemento, Entidad entidad, Long tipoIntegracion) throws Exception;

    /**
     * Vuelve a Distribuir un registro de Entrada ya distribuido previamente
     * @param idRegistro
     * @param entidad
     * @return
     * @throws Exception
     */
    Boolean reDistribuirRegistro(Long idRegistro, Entidad entidad) throws Exception;
}
