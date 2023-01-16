package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Sesion;
import es.caib.regweb3.model.UsuarioEntidad;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 31/10/19
 */
@Local
public interface SesionLocal extends BaseEjb<Sesion, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/SesionEJB";


    /**
     * Crea una nueva Sesión con el estado NO_INICIADA
     *
     * @param usuario
     * @return
     * @throws I18NException
     */
    Sesion nuevaSesion(UsuarioEntidad usuario) throws I18NException;

    /**
     * Busca una Sesión a partir de id y el usuario
     *
     * @param idSesion
     * @param usuario
     * @return
     */
    Sesion findByIdSesionUsuario(Long idSesion, UsuarioEntidad usuario);

    /**
     * Busca una Sesión a partir de id, usuario y estado
     *
     * @param idSesion
     * @param usuario
     * @param estado
     * @return
     */
    Sesion findByIdSesionUsuarioEstado(Long idSesion, UsuarioEntidad usuario, Long estado);

    /**
     * Cambia el estado de una Sesión
     *
     * @param idSesion
     * @param usuario
     * @param estado
     * @throws I18NException
     */
    void cambiarEstado(Long idSesion, UsuarioEntidad usuario, Long estado) throws I18NException;

    /**
     * Crea una nueva Sesión con estado INICIADO
     *
     * @param idSesion
     * @param usuario
     * @throws I18NException
     */
    void iniciarSesion(Long idSesion, UsuarioEntidad usuario) throws I18NException;

    /**
     * Marca la Sesión como finalizada, añadiendole el numero de registro
     *
     * @param idSesion
     * @param usuario
     * @param tipoRegistro
     * @param numeroRegistro
     * @throws I18NException
     */
    void finalizarSesion(Long idSesion, UsuarioEntidad usuario, Long tipoRegistro, String numeroRegistro) throws I18NException;

    /**
     * Purgado de sesiones
     *
     * @param idEntidad
     */
    void purgarSesiones(Long idEntidad) throws I18NException;
}
