package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModeloRecibo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface ModeloReciboLocal extends BaseEjb<ModeloRecibo, Long> {

  /**
   *
   * @param idEntidad
   * @return
   * @throws Exception
   */
  Long getTotal(Long idEntidad) throws Exception;

   /**
    *
    * @param idEntidad
    * @return
    * @throws Exception
    */
   List<ModeloRecibo> getByEntidad(Long idEntidad) throws Exception;

  /**
   *
   * @param inicio
   * @param idEntidad
   * @return
   * @throws Exception
   */
  List<ModeloRecibo> getPagination(int inicio, Long idEntidad) throws Exception;

    /**
     * Elimina los {@link es.caib.regweb3.model.ModeloRecibo} de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
