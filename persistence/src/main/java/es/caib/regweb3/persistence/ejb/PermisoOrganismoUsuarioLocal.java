package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
public interface PermisoOrganismoUsuarioLocal extends BaseEjb<PermisoOrganismoUsuario, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/PermisoOrganismoUsuarioEJB";


    /**
     * Crea los permisos de un Usuario relacionado con un Organismo
     *
     * @param usuarioEntidad
     * @param organismo
     * @throws I18NException
     */
    void crearPermisosUsuarioOrganismo(UsuarioEntidad usuarioEntidad, Organismo organismo) throws I18NException;

    /**
     * Crear los permisos para el nuevo organismo a todos los usuarios OAMR con los permisos de lectura activados
     * @param organismo
     * @throws I18NException
     */
    void crearPermisosUsuarioOARM(Organismo organismo) throws I18NException;

    /**
     * Elimina todos los permisos de un determinado Organismo
     *
     * @param idOrganismo
     * @throws I18NException
     */
    void eliminarPermisosOrganismo(Long idOrganismo) throws I18NException;

    /**
     * Elimina todos los permidos de un Usuario y un Organismo
     *
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @throws I18NException
     */
    void eliminarPermisosUsuarioOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws I18NException;

    /**
     * Retorna los {@link PermisoOrganismoUsuario} a partir de un {@link UsuarioEntidad}
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    List<PermisoOrganismoUsuario> findByUsuario(Long idUsuarioEntidad) throws I18NException;


    /**
     * Obtiene los Organismos asociados de un UsuarioEntidad
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosByUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * Retorna los {@link PermisoOrganismoUsuario} a partir de un {@link UsuarioEntidad}
     * y de un {@link Organismo}
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    List<PermisoOrganismoUsuario> findByUsuarioOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws I18NException;

    /**
     * Retorna los {@link PermisoOrganismoUsuario} de un {@link Organismo}
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<PermisoOrganismoUsuario> findByOrganismo(Long idOrganismo) throws I18NException;

    /**
     * Comprueba si un Organismos tiene permisos
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Boolean tienePermisos(Long idOrganismo) throws I18NException;

    /**
     * Retorna los {@link UsuarioEntidad} de un {@link Organismo}
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getUsuariosEntidadByOrganismo(Long idOrganismo) throws I18NException;

    /**
     * etorna los {@link UsuarioEntidad} de una lista de {@link Organismo}
     *
     * @param organismos
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getUsuariosEntidadByOrganismos(List<Organismo> organismos) throws I18NException;

    /**
     * Retorna los {@link Organismo} en los que un UsuarioEntidad tiene acceso según el permiso dado
     *
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosPermiso(Long idUsuarioEntidad, Long idPermiso) throws I18NException;

    /**
     * Retorna los {@link Oficina} en los que un UsuarioEntidad puede Registrar
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> getOficinasRegistro(Long idUsuarioEntidad) throws I18NException;

    /**
     * Retorna los {@link Oficina} en los que un UsuarioEntidad tiene acceso según el permiso dado
     *
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> getOficinasPermiso(Long idUsuarioEntidad, Long idPermiso) throws I18NException;

    /**
     * Obtiene los Organismos donde el UsuarioEntidad puede registrar
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosRegistro(Long idUsuarioEntidad) throws I18NException;

    /**
     * Obtiene los Organismos donde el UsuarioEntidad puede consultar
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosConsulta(Long idUsuarioEntidad) throws I18NException;

    /**
     * Retorna los {@link Oficina} en los que un UsuarioEntidad puede Consultar
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> getOficinasConsulta(Long idUsuarioEntidad) throws I18NException;

    /**
     * Retorna los {@link Oficina} a los que un UsuarioEntidad tiene acceso SIR
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> getOficinasSir(Long idUsuarioEntidad) throws I18NException;

    /**
     * Retorna las {@link Oficina} en los que un UsuarioEntidad es responsable
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> getOficinasResponsable(Long idUsuarioEntidad) throws I18NException;

    /**
     * Comprueba si un usuario tiene un permiso en un grupo de organismos
     *
     * @param organismos
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws I18NException
     */
    Boolean tienePermiso(Set<Long> organismos, Long idUsuarioEntidad, Long idPermiso) throws I18NException;

    /**
     * Obtiene los UsuarioEntidad que pueden registrar en los organismos de los Organismos seleccionados
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getUsuariosRegistroEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene los Usuarios que pueden registrar en una lista de Organismos
     *
     * @param organismos
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getUsuariosRegistroOrganismo(List<Long> organismos) throws I18NException;

    /**
     * Comprueba si un Usuario es Administrador de un determinado Organismo.
     *
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Boolean isAdministradorOrganismo(Long idUsuarioEntidad, Long idOrganismo) throws I18NException;

    /**
     * Comprueba si un Usuario tiene el Permiso espeficicado sobre el Organismo especificado.
     *
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @param idPermiso
     * @param organismoActivo
     * @return true si tiene el Permiso, false si no lo tiene.
     * @throws I18NException
     */
    Boolean tienePermiso(Long idUsuarioEntidad, Long idOrganismo, Long idPermiso, Boolean organismoActivo) throws I18NException;

    /**
     * Comprueba si un UsuarioEntidad puede registrar en un determinado Organismo
     *
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Boolean puedeRegistrar(Long idUsuarioEntidad, Long idOrganismo) throws I18NException;

    /**
     * Obtiene los Organismo que administra un usuario
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosAdministrados(Long idUsuarioEntidad) throws I18NException;

    /**
     * Modifica el estado de un Permiso que tiene un Usuario sobre un Organismo
     *
     * @param idPermisoOrganismoUsuario
     * @param activo
     * @throws I18NException
     */
    void actualizarPermiso(Long idPermisoOrganismoUsuario, Boolean activo) throws I18NException;

    /**
     * Comprueba si un Usuario tiene Inicializado el Permiso espeficicado sobre el Organismo especificado.
     *
     * @param idUsuarioEntidad
     * @param idOrganismo
     * @param idPermiso
     * @return true si ya existe el Permiso en la Base de Datos, false si no existe.
     * @throws I18NException
     */
    Boolean existePermiso(Long idUsuarioEntidad, Long idOrganismo, Long idPermiso) throws I18NException;

    /**
     * Obtiene los Usuarios que tienen el permiso indicado en una lista de organismos
     *
     * @param organismos
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getUsuariosPermiso(Set<Organismo> organismos, Long permiso) throws I18NException;

    /**
     * Elimina las PermisoOrganismoUsuario de un Usuario
     *
     * @param idUsuarioEntidad
     * @throws I18NException
     */
    void eliminarByUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * Elimina las PermisoOrganismoUsuario de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Migra los Permisos existentes en la nueva tabla PermiloOrganismoUsuario
     *
     * @param libro
     * @return
     */
    Integer migrarPermisos(Libro libro) throws I18NException;
}
