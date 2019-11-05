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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA","RWE_WS_CIUDADANO"})
public interface SesionLocal extends BaseEjb<Sesion, Long> {

    /**
     *
     * @param usuario
     * @return
     * @throws Exception
     */
    Sesion nuevaSesion(UsuarioEntidad usuario) throws Exception;

    /**
     *
     * @param idSesion
     * @param usuario
     * @return
     */
    Sesion findByIdSesionUsuario(Long idSesion, UsuarioEntidad usuario);

    /**
     *
     * @param idSesion
     * @param usuario
     * @param estado
     * @throws Exception
     */
    void cambiarEstado(Long idSesion, UsuarioEntidad usuario, Long estado) throws Exception;

    /**
     *
     * @param idSesion
     * @param usuario
     * @throws Exception
     */
    void iniciarSesion(Long idSesion, UsuarioEntidad usuario) throws Exception;

    /**
     *
     * @param idSesion
     * @param usuario
     * @param tipoRegistro
     * @param numeroRegistro
     * @throws Exception
     */
    void finalizarSesion(Long idSesion, UsuarioEntidad usuario, Long tipoRegistro, String numeroRegistro) throws Exception;

    /**
     *
     * @param estado
     * @throws Exception
     */
    void purgarSesionesEstado(Long estado) throws Exception;
}
