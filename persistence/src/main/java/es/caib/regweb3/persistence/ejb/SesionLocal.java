package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Sesion;
import es.caib.regweb3.model.UsuarioEntidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 31/10/19
 */
@Local
public interface SesionLocal extends BaseEjb<Sesion, Long> {

    /**
     * Crea una nueva Sesión con el estado NO_INICIADA
     * @param usuario
     * @return
     * @throws Exception
     */
    Sesion nuevaSesion(UsuarioEntidad usuario) throws Exception;

    /**
     * Busca una Sesión a partir de id y el usuario
     * @param idSesion
     * @param usuario
     * @return
     */
    Sesion findByIdSesionUsuario(Long idSesion, UsuarioEntidad usuario);

    /**
     * Busca una Sesión a partir de id, usuario y estado
     * @param idSesion
     * @param usuario
     * @param estado
     * @return
     */
    Sesion findByIdSesionUsuarioEstado(Long idSesion, UsuarioEntidad usuario, Long estado);

    /**
     * Cambia el estado de una Sesión
     * @param idSesion
     * @param usuario
     * @param estado
     * @throws Exception
     */
    void cambiarEstado(Long idSesion, UsuarioEntidad usuario, Long estado) throws Exception;

    /**
     * Crea una nueva Sesión con estado INICIADO
     * @param idSesion
     * @param usuario
     * @throws Exception
     */
    void iniciarSesion(Long idSesion, UsuarioEntidad usuario) throws Exception;

    /**
     * Marca la Sesión como finalizada, añadiendole el numero de registro
     * @param idSesion
     * @param usuario
     * @param tipoRegistro
     * @param numeroRegistro
     * @throws Exception
     */
    void finalizarSesion(Long idSesion, UsuarioEntidad usuario, Long tipoRegistro, String numeroRegistro) throws Exception;

    /**
     * Purgado de sesiones
     * @param idEntidad
     */
    void purgarSesiones(Long idEntidad) throws Exception;
}
