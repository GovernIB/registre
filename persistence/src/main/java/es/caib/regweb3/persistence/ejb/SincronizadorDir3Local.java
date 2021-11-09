package es.caib.regweb3.persistence.ejb;


import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.sql.Timestamp;

/**
 * Created by Fundacio Bit
 *
 * @author earrivi
 * Date: 6/03/13
 */
@Local
@RolesAllowed({"RWE_ADMIN"})
public interface SincronizadorDir3Local {

  /**
   * @param entidadId
   * @param fechaActualizacion
   * @param fechaSincronizacion
   * @return
   * @throws Exception
   */
  int sincronizarActualizar(Long entidadId, Timestamp fechaActualizacion, Timestamp fechaSincronizacion) throws Exception;

}
