package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TipoDocumental;

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
public interface TipoDocumentalLocal extends BaseEjb<TipoDocumental, Long> {

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long getTotal(Long idEntidad) throws Exception;

    /**
     *
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<TipoDocumental> getPagination(int inicio, Long idEntidad) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.TipoDocumental} asociado a un codigo.
     * @param codigoNTI
     * @param idEntidad
     * @return
     * @throws Exception
     */
    TipoDocumental findByCodigoEntidad(String codigoNTI, Long idEntidad) throws Exception;

    /**
     * Comprueba la existencia del codigo en algún TipoDocumental
     * @param codigoNTI
     * @param idTipoDocumental
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean existeCodigoEdit(String codigoNTI, Long idTipoDocumental, Long idEntidad) throws Exception;

  /**
   * Obtiene los tipos documentales de una entidad
   * @param idEntidad
   * @return
   * @throws Exception
   */
  List<TipoDocumental> getByEntidad(Long idEntidad) throws Exception;

    /**
     * Elimina los TipoDocumental de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Crea un TipoDocumental con sus traducciones en Catalán y Castellano
     * @param codigo
     * @param idEntidad
     * @param nombreCa
     * @param nombreES
     * @return
     * @throws Exception
     */
    TipoDocumental nuevoTraduccion(String codigo, Long idEntidad, String nombreCa, String nombreES) throws Exception;


}
