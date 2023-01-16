package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Notificacion;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface NotificacionLocal extends BaseEjb<Notificacion, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/NotificacionEJB";


    /**
     * Obtiene el total de notificaciones de un usuario y estado
     *
     * @param idUsuarioEntidad
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Long getByEstadoCount(Long idUsuarioEntidad, Long idEstado) throws I18NException;

    /**
     * Obtiene las notificaciones de una entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Notificacion> getByEntidad(Long idEntidad) throws I18NException;

    /**
     * Búsqueda de notificaciones
     *
     * @param notificacion
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Notificacion notificacion, Long idUsuarioEntidad) throws I18NException;

    /**
     * Marca como leída una notificación
     *
     * @param idNotificacion
     * @throws I18NException
     */
    void leerNotificacion(Long idNotificacion) throws I18NException;

    /**
     * Número de notificaciones nuevas
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Long notificacionesPendientes(Long idUsuarioEntidad) throws I18NException;

    /**
     * Envía una notificación a cada uno de los administradores de la entidad
     *
     * @param idEntidad
     * @param asunto    de la notificación
     * @param mensaje   de la notificación
     * @throws I18NException
     */
    void notificacionAdminEntidad(Long idEntidad, String asunto, String mensaje) throws I18NException;

    /**
     * Envía notificaciones a los usuarios de las oficinas con más de 10 registros sir pendientes de procesar
     *
     * @param idEntidad
     * @throws I18NException
     */
    void notificacionesRegistrosSirPendientes(Long idEntidad) throws I18NException;

    /**
     * Envía notificaciones a los usuarios de las oficinas con Registros Rechazados o devueltos al origen
     *
     * @param idEntidad
     * @throws I18NException
     */
    void notificacionesRechazadosDevueltos(Long idEntidad) throws I18NException;

    /**
     * Elimina las notificaciones de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Elimina las notificaciones del usuarioEntidad indicado, tanto si es remitente como destinatario.
     *
     * @param idUsuarioEntidad
     * @throws I18NException
     */
    void eliminarByUsuario(Long idUsuarioEntidad) throws I18NException;

}
