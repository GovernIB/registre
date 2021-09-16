package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Descarga;

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
public interface DescargaLocal extends BaseEjb<Descarga, Long> {

  /**
   * Busca una descarga solo por el tipo.
   * @param tipo
   * @return
   * @throws Exception
   */
  Descarga findByTipo(String tipo) throws Exception;

  /**
   *  Obtiene el valor de la última descarga de un tipo y de una entidad
   * @param tipo indica el tipo (UNIDAD, OFICINA)
   * @return  la descarga encontrada
   * @throws Exception
   */
  Descarga ultimaDescarga(String tipo, Long idEntidad) throws Exception;

  /**
   *  Obtiene el valor de la primera descarga de un tipo y de una entidad
   *  Nos sirve para determinar la fecha de la primera sincronizacion
   * @param tipo indica el tipo (UNIDAD, OFICINA)
   * @return  la descarga encontrada
   * @throws Exception
   */
  Descarga findByTipoEntidadInverse(String tipo, Long idEntidad) throws Exception;

  /**
   * Calcula el total por entidad
   * @param idEntidad
   * @return
   * @throws Exception
   */
  Long getTotalByEntidad(Long idEntidad) throws Exception;

  /**
   * Obtiene la paginación por entidad
   * @param inicio
   * @param idEntidad
   * @return
   * @throws Exception
   */
  List<Descarga> getPaginationByEntidad(int inicio, Long idEntidad) throws Exception;

  void deleteByTipo(String tipo) throws Exception;

  /**
   * Obtiene las descargas de una entidad ordenadas por código;
   * @param idEntidad
   * @return
   * @throws Exception
   */
  List<Descarga> findByEntidad(Long idEntidad) throws Exception;

  /**
   * Eimina todas las Descargas de una Entidad
   * @param idEntidad
   * @return
   * @throws Exception
   */
  Integer eliminarByEntidad(Long idEntidad) throws Exception;

  /**
   * @param inicio
   * @param idEntidad
   * @return
   * @throws Exception
   */
  List<Descarga> getPagination(int inicio, Long idEntidad) throws Exception;

  /**
   * @param idEntidad
   * @return
   * @throws Exception
   */
  Long getTotalEntidad(Long idEntidad) throws Exception;
}
