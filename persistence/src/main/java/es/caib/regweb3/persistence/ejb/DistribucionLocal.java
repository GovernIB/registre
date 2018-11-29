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
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface DistribucionLocal  {

  /**
   * Método que obtiene los destinatarios a los que distribuir el registro
   *
   * @param re             registro de entrada a distribuir
   * @param usuarioEntidad
   * @return lista de destinatarios a los que se debe distribuir el registro
   * @throws Exception
   * @throws I18NException
   */
  RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException, I18NValidationException;

  /**
   * Método que agrupa la distribución de los registros en cola.
   * @param entidadId
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
   */
  void distribuirRegistrosEnCola(Long entidadId) throws Exception, I18NException, I18NValidationException;

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
   * Método que envia un registro a la cola de Distribución
   * @param re
   * @throws Exception
   * @throws I18NException
   * @throws I18NValidationException
   */
  void enviarAColaDistribucion(RegistroEntrada re, UsuarioEntidad usuarioEntidad, int maxReintentos) throws Exception, I18NException, I18NValidationException;

  /**
   * Método que realiza la distribución de los elementos de la cola pendientes de distribuir de una entidad
   * @param entidadId
   * @throws Exception
   * @throws I18NException
   */
  void iniciarDistribucionLista(Long entidadId, List<UsuarioEntidad> administradores, IDistribucionPlugin plugin) throws Exception, I18NException, I18NValidationException;
}
