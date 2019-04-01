package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.DestinatarioWrapper;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface DistribucionLocal  {

  /**
   * Método que obtiene los destinatarios a los que distribuir el registro
   *
   * @param re             registro de entrada a distribuir
   * @param usuarioEntidad
   * @param forzarEnvio Fuerza que se distribuya directamente sin pasar por la Cola, aunque esté así configurado en el Plugin
   * @return lista de destinatarios a los que se debe distribuir el registro
   * @throws Exception
   * @throws I18NException
   */
  RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad, Boolean forzarEnvio) throws Exception, I18NException, I18NValidationException;

  /**
   * Método que agrupa la distribución de los registros en cola.
   * @param entidadId
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
   */
 // void distribuirRegistrosEnCola(Long entidadId) throws Exception, I18NException, I18NValidationException;

  /**
   * Método que envia un registro de entrada a un conjunto de destinatarios
   *
   * @param re      registro de entrada
   * @param wrapper contiene los destinatarios a los que enviar el registro de entrada
   * @return
   * @throws Exception
   * @throws I18NException
   */
  Boolean enviar(RegistroEntrada re, DestinatarioWrapper wrapper,
                        Long entidadId, String idioma) throws Exception, I18NException, I18NValidationException;


  /**
   * Método que distribuye un registro de entrada de manera atómica generando el justificante
   * e invocando al webservice de distribucion
   * @param registroEntrada
   * @param distribucionPlugin
   * @return
   * @throws Exception
   * @throws I18NValidationException
   * @throws I18NException
   */
  Boolean distribuirRegistroEntrada(RegistroEntrada registroEntrada, IDistribucionPlugin distribucionPlugin, String descripcion, long tiempo, StringBuilder peticion) throws Exception, I18NValidationException, I18NException;

  /**
   *
   * @param idEntidad
   * @throws Exception
   */
  void distribuirRegistrosEnCola(Long idEntidad) throws Exception;
}
