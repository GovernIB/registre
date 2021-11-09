package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA","RWE_WS_CIUDADANO"})
public interface PermisoOrganismoUsuarioLocal extends BaseEjb<PermisoOrganismoUsuario, Long> {

    /**
     * Crea los permisos de un Usuario relacionado con un Organismo
     * @param usuarioEntidad
     * @param organismo
     * @throws Exception
     */
    void crearPermisosUsuarioOrganismo(UsuarioEntidad usuarioEntidad, Organismo organismo) throws Exception;

    /**
     * Elimina todos los permisos de un determinado Organismo
     * @param idOrganismo
     * @throws Exception
     */
    void eliminarPermisosOrganismo(Long idOrganismo) throws Exception;

    /**
     * Elimina todos los permidos de un Usuario y un Organismo
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @throws Exception
     */
    void eliminarPermisosUsuarioOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws Exception;

    /**
     * Retorna los {@link PermisoOrganismoUsuario} a partir de un {@link UsuarioEntidad}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<PermisoOrganismoUsuario> findByUsuario(Long idUsuarioEntidad) throws Exception;


    /**
     * Obtiene los Organismos asociados de un UsuarioEntidad
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosByUsuario(Long idUsuarioEntidad)throws Exception;

    /**
     * Retorna los {@link PermisoOrganismoUsuario} a partir de un {@link UsuarioEntidad}
     * y de un {@link Organismo}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<PermisoOrganismoUsuario> findByUsuarioOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws Exception;

    /**
     * Retorna los {@link PermisoOrganismoUsuario} de un {@link Organismo}
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<PermisoOrganismoUsuario> findByOrganismo(Long idOrganismo) throws Exception;

    /**
     * Comprueba si un Organismos tiene permisos
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Boolean tienePermisos(Long idOrganismo) throws Exception;

    /**
     * Retorna los {@link UsuarioEntidad} de un {@link Organismo}
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosEntidadByOrganismo(Long idOrganismo) throws Exception;

    /**
     * etorna los {@link UsuarioEntidad} de una lista de {@link Organismo}
     * @param organismos
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosEntidadByOrganismos(List<Organismo> organismos) throws Exception;

    /**
     * Retorna los {@link Organismo} en los que un UsuarioEntidad tiene acceso según el permiso dado
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosPermiso(Long idUsuarioEntidad, Long idPermiso) throws Exception;

    /**
     * Retorna los {@link Oficina} en los que un UsuarioEntidad puede Registrar
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> getOficinasRegistro(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link Oficina} en los que un UsuarioEntidad tiene acceso según el permiso dado
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> getOficinasPermiso(Long idUsuarioEntidad, Long idPermiso) throws Exception;

    /**
     * Obtiene los Organismos donde el UsuarioEntidad puede registrar
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosRegistro(Long idUsuarioEntidad) throws Exception;

    /**
     * Obtiene los Organismos donde el UsuarioEntidad puede consultar
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosConsulta(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link Oficina} en los que un UsuarioEntidad puede Consultar
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> getOficinasConsulta(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link Oficina} a los que un UsuarioEntidad tiene acceso SIR
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> getOficinasSir(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna las {@link Oficina} en los que un UsuarioEntidad es responsable
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> getOficinasResponsable(Long idUsuarioEntidad) throws Exception;

    /**
     * Comprueba si un usuario tiene un permiso en un grupo de organismos
     * @param organismos
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws Exception
     */
    Boolean tienePermiso(Set<Long> organismos, Long idUsuarioEntidad, Long idPermiso) throws Exception;

    /**
     * Obtiene los UsuarioEntidad que pueden registrar en los organismos de los Organismos seleccionados
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosRegistroEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los Usuarios que pueden registrar en una lista de Organismos
     * @param organismos
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosRegistroOrganismo(List<Long> organismos) throws Exception;

    /**
     * Comprueba si un Usuario es Administrador de un determinado Organismo.
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Boolean isAdministradorOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws Exception;

    /**
     * Comprueba si un Usuario tiene el Permiso espeficicado sobre el Organismo especificado.
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @param idPermiso
     * @param organismoActivo
     * @return true si tiene el Permiso, false si no lo tiene.
     * @throws Exception
     */
    Boolean tienePermiso(Long idUsuarioEntidad, Long idOrganismo, Long idPermiso, Boolean organismoActivo) throws Exception;

    /**
     * Comprueba si un UsuarioEntidad puede registrar en un determinado Organismo
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Boolean puedeRegistrar(Long idUsuarioEntidad, Long idOrganismo) throws Exception;

    /**
     * Obtiene los Organismo que administra un usuario
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosAdministrados(Long idUsuarioEntidad) throws Exception;

    /**
     * Modifica el estado de un Permiso que tiene un Usuario sobre un Organismo
     * @param idPermisoOrganismoUsuario
     * @param activo
     * @throws Exception
     */
    void actualizarPermiso(Long idPermisoOrganismoUsuario, Boolean activo) throws Exception;

    /**
     * Comprueba si un Usuario tiene Inicializado el Permiso espeficicado sobre el Organismo especificado.
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @param idPermiso
     * @return true si ya existe el Permiso en la Base de Datos, false si no existe.
     * @throws Exception
     */
    Boolean existePermiso(Long idUsuarioEntidad, Long idOrganismo, Long idPermiso) throws Exception;

    /**
     * Obtiene los Usuarios que tienen el permiso indicado en una lista de organismos
     * @param organismos
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosPermiso(Set<Organismo> organismos, Long permiso) throws Exception;

    /**
     * Elimina las PermisoOrganismoUsuario de un Usuario
     * @param idUsuarioEntidad
     * @throws Exception
     */
    void eliminarByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina las PermisoOrganismoUsuario de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Migra los Permisos existentes en la nueva tabla PermiloOrganismoUsuario
     * @param libro
     * @return
     */
    Integer migrarPermisos(Libro libro) throws Exception;
}
