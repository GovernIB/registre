package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.ModeloRecibo;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface ModeloReciboLocal extends BaseEjb<ModeloRecibo, Long> {

  /**
   *
   * @param idEntidad
   * @return
   * @throws Exception
   */
   public Long getTotal(Long idEntidad) throws Exception;

   /**
    *
    * @param idEntidad
    * @return
    * @throws Exception
    */
    public List<ModeloRecibo> getByEntidad(Long idEntidad) throws Exception;

  /**
   *
   * @param inicio
   * @param idEntidad
   * @return
   * @throws Exception
   */
   public List<ModeloRecibo> getPagination(int inicio, Long idEntidad) throws Exception;

}
