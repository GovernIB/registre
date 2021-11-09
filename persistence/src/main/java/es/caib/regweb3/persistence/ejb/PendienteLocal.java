package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Pendiente;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created 14/10/14 9:56
 *
 * @author mgonzalez
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface PendienteLocal extends BaseEjb<Pendiente, Long>  {

  /**
   * Obtiene el registro pendiente del organismo en cuestión
   * @param idOrganismo
   * @return
   * @throws Exception
   */
  Pendiente findByIdOrganismo(Long idOrganismo) throws Exception;
  /**
   * Función que devuelve los pendientes en función de su estado
   * @param estado
   * @return
   * @throws Exception
   */
  List<Pendiente> findByEstadoProcesado(String estado, Boolean procesado) throws Exception;

  /**
   * Función que devuelve los pendientes de procesar
   * @return
   * @throws Exception
   */
  List<Pendiente> findPendientesProcesar(Long idEntidad) throws Exception;


}
