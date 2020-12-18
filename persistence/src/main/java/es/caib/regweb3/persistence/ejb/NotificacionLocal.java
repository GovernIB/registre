package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Notificacion;
import es.caib.regweb3.persistence.utils.Paginacion;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface NotificacionLocal extends BaseEjb<Notificacion, Long> {

    /**
     * Obtiene el total de notificaciones de un usuario y estado
     * @param idUsuarioEntidad
     * @param idEstado
     * @return
     * @throws Exception
     */
    Long getByEstadoCount(Long idUsuarioEntidad, Long idEstado) throws Exception;

    /**
     * Obtiene las notificaciones de una entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Notificacion> getByEntidad(Long idEntidad) throws Exception;

    /**
     * Búsqueda de notificaciones
     * @param notificacion
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Notificacion notificacion, Long idUsuarioEntidad) throws Exception;

    /**
     * Marca como leída una notificación
     * @param idNotificacion
     * @throws Exception
     */
    void leerNotificacion(Long idNotificacion) throws Exception;

    /**
     * Número de notificaciones nuevas
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
     Long notificacionesPendientes(Long idUsuarioEntidad) throws Exception;

    /**
     * Envía una notificación a cada uno de los administradores de la entidad
     * @param idEntidad
     * @param asunto de la notificación
     * @param mensaje de la notificación
     * @throws Exception
     */
     void notificacionAdminEntidad(Long idEntidad, String asunto, String mensaje) throws Exception;

    /**
     * Envía notificaciones a los usuarios de las oficinas con más de 10 registros sir pendientes de procesar
     * @param idEntidad
     * @throws Exception
     */
     void notificacionesRegistrosSirPendientes(Long idEntidad) throws Exception;

    /**
     * Envía notificaciones a los usuarios de las oficinas con Registros Rechazados o devueltos al origen
     * @param idEntidad
     * @throws Exception
     */
     void notificacionesRechazadosDevueltos(Long idEntidad) throws Exception;

    /**
     * Elimina las notificaciones de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Elimina las notificaciones del usuarioEntidad indicado, tanto si es remitente como destinatario.
     * @param idUsuarioEntidad
     * @throws Exception
     */
    void eliminarByUsuario(Long idUsuarioEntidad) throws Exception;

}
