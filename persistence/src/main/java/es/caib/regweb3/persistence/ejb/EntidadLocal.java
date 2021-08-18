package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.UsuarioEntidad;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface EntidadLocal extends BaseEjb<Entidad, Long> {


    /**
     * Obtiene una Entidad con unos campos minimnos
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Entidad findByIdLigero(Long idEntidad) throws Exception;

    /**
     * Creamos una nueva Entidad y sus propiedades por defecto
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    Entidad nuevaEntidad(Entidad entidad) throws Exception;

    /**
     * Retorna la Entidad cuyo CódigoDir3 es el indicado por parámetro
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    Entidad findByCodigoDir3(String codigo) throws Exception;

    /**
     * Comprueba su una {@link es.caib.regweb3.model.Entidad} tiene algún {@link es.caib.regweb3.model.Organismo} asociado.
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean tieneOrganismos(Long idEntidad) throws Exception;

    /**
     * Obtiene las entidades de un Usuario
     *
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Entidad> getEntidadesAdministrador(Long idUsuario) throws Exception;

    /**
     * Obtiene las entidades de un Usuario de las que es Propietario
     *
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Entidad> getEntidadesPropietario(Long idUsuario) throws Exception;

    /**
     * Obtiene las entidades activas.
     * @return
     * @throws Exception
     */
     List<Entidad> getEntidadesActivas() throws Exception;

    /**
     * Comprueba si el codigoDir3 dado existe en alguna entidad excepto la selccionado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws Exception;

    /**
     * Comprueba si un Usuario es Administrador de la Entidad
     *
     * @param idEntidad
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean esAdministrador(Long idEntidad, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Determina si un usuario es propietario o administrador de esa entidad
     *
     * @param idEntidad
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Boolean esAutorizado(Long idEntidad, Long idUsuario) throws Exception;

    /**
     * Comprueba si una Entidad está marcada como Sir
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean isSir(Long idEntidad) throws Exception;

    /**
     * Obtiene todas las entidades que están marcadas como SIR
     * @return
     * @throws Exception
     */
    List<Entidad> getEntidadesSir() throws Exception;

    /**
     * Comprueba las diferentes dependencias del UsuarioEntidad para saber si es posible eliminarlo
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean puedoEliminarlo(Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina todos los Registros y relaciones de una Entidad.
     *
     * @param idEntidad
     * @throws Exception
     */
    void eliminarRegistros(Long idEntidad) throws Exception, I18NException;


    /**
     * Elimina una Entidad y todos sus datos dependientes
     *
     * @param idEntidad
     * @throws Exception
     */
    void eliminarEntidad(Long idEntidad) throws Exception, I18NException;

    /**
     * Marca/Desmarca una entidad que está en mantenimiento durante el proceso de sincronización
     * @param idEntidad
     * @param mantenimiento
     * @throws Exception
     */
    void marcarEntidadMantenimiento(Long idEntidad, Boolean mantenimiento) throws Exception;

    /**
     * Determina si una implementación es multientidad (más de una entidad con sir activado)
     * @throws Exception
     */
    boolean isMultiEntidad() throws Exception;

}
