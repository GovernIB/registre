package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Descarga;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN"})
public interface DescargaLocal extends BaseEjb<Descarga, Long> {
  /**
   * Busca una descarga solo por el tipo.
   * @param tipo
   * @return
   * @throws Exception
   */
  public Descarga findByTipo(String tipo) throws Exception;
  /**
   *  Obtiene el valor de la última descarga de un tipo y de una entidad
   * @param tipo indica el tipo (UNIDAD, OFICINA)
   * @return  la descarga encontrada
   * @throws Exception
   */
  public Descarga findByTipoEntidad(String tipo, Long idEntidad) throws Exception;
  /**
   *  Obtiene el valor de la primera descarga de un tipo y de una entidad
   *  Nos sirve para determinar la fecha de la primera sincronizacion
   * @param tipo indica el tipo (UNIDAD, OFICINA)
   * @return  la descarga encontrada
   * @throws Exception
   */
  public Descarga findByTipoEntidadInverse(String tipo, Long idEntidad) throws Exception;

  public void deleteByTipo(String tipo) throws Exception;
}
