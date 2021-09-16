package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Integracion;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface IntegracionLocal extends BaseEjb<Integracion, Long> {

    /**
     * Obtiene las integraciones de una entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Integracion> getByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene las integraciones de un número de registro
     * @param idEntidad
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    List<Integracion> getByEntidadNumReg(Long idEntidad, String numeroRegistro) throws Exception;

    /**
     * Búsqueda de integraciones
     * @param integracion
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integracion integracion, Long idEntidad) throws Exception;

    /**
     * Obtiene las últimas 10 Integraciones con error de los últimos 2 días
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Integracion> ultimasIntegracionesErrorTipo(Long idEntidad, Long tipo) throws Exception;

    /**
     *
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws Exception
     */
    void addIntegracionOk(Date inicio, Long tipo, String descripcion, String peticion, Long tiempo, Long idEntidad, String numregformat) throws Exception;

    /**
     *
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param th
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws Exception
     */
    void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws Exception;

    /**
     * Elimina las Integraciones con una antigüedad de 10 días
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer purgarIntegraciones(Long idEntidad) throws Exception;

    /**
     * Elimina las integraciones de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
