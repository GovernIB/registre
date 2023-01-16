package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Integracion;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

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

    String JNDI_NAME = "java:app/regweb3-persistence/IntegracionEJB";


    /**
     * Obtiene las integraciones de una entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Integracion> getByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene las integraciones de un número de registro
     *
     * @param idEntidad
     * @param numeroRegistro
     * @return
     * @throws I18NException
     */
    List<Integracion> getByEntidadNumReg(Long idEntidad, String numeroRegistro) throws I18NException;

    /**
     * Búsqueda de integraciones
     *
     * @param integracion
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integracion integracion, Long idEntidad) throws I18NException;

    /**
     * Obtiene las últimas 10 Integraciones con error de los últimos 2 días
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Integracion> ultimasIntegracionesErrorTipo(Long idEntidad, Long tipo) throws I18NException;

    /**
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws I18NException
     */
    void addIntegracionOk(Date inicio, Long tipo, String descripcion, String peticion, Long tiempo, Long idEntidad, String numregformat) throws I18NException;

    /**
     * @param tipo
     * @param descripcion
     * @param peticion
     * @param th
     * @param tiempo
     * @param idEntidad
     * @param numregformat
     * @throws I18NException
     */
    void addIntegracionError(Long tipo, String descripcion, String peticion, Throwable th, String error, Long tiempo, Long idEntidad, String numregformat) throws I18NException;

    /**
     * Elimina las Integraciones con una antigüedad de 10 días
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer purgarIntegraciones(Long idEntidad) throws I18NException;

    /**
     * Elimina las integraciones de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
