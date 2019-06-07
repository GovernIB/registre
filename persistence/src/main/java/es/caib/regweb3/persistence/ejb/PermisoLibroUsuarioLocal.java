package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.PermisoLibroUsuario;
import es.caib.regweb3.model.UsuarioEntidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface PermisoLibroUsuarioLocal extends BaseEjb<PermisoLibroUsuario, Long> {

    /**
     * Retorna los {@link es.caib.regweb3.model.PermisoLibroUsuario} a partir de un {@link es.caib.regweb3.model.UsuarioEntidad}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<PermisoLibroUsuario> findByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.PermisoLibroUsuario} a partir de un {@link es.caib.regweb3.model.UsuarioEntidad}
     * y una lista de {@link es.caib.regweb3.model.Libro}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<PermisoLibroUsuario> findByUsuarioLibros(Long idUsuarioEntidad, List<Libro> libros) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.PermisoLibroUsuario} a partir de un {@link es.caib.regweb3.model.UsuarioEntidad}
     * y de un {@link es.caib.regweb3.model.Libro}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<PermisoLibroUsuario> findByUsuarioLibro(Long idUsuarioEntidad, Long idLibro) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.PermisoLibroUsuario} de un {@link es.caib.regweb3.model.Libro}
     * @param idLibro
     * @return
     * @throws Exception
     */
    List<PermisoLibroUsuario> findByLibro(Long idLibro) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.UsuarioEntidad} de un {@link es.caib.regweb3.model.Libro}
     * @param idLibro
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosEntidadByLibro(Long idLibro) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} en los que un Usuario tiene permisos de
     * Registro, Modificación o Administración
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosRegistro(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} en los que un Usuario tiene permisos de Consulta
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosConsulta(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} en los que un UsuarioEntidad puede Administrar
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosAdministrados(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} a los que un UsuarioEntidad
     * @param idUsuarioEntidad
     * @param idPermiso
     * @param activos Indica si queremos solo los libros activos o por el contrario Todos
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosPermiso(Long idUsuarioEntidad, Long idPermiso, Boolean activos) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} a los que un UsuarioEntidad
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismoLibroPermiso(Long idUsuarioEntidad, Long idPermiso) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} en los que puede registrar una Oficina y un UsuarioEntidad
     * @param usuario
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosRegistroOficina(Set<Organismo> organismos, UsuarioEntidad usuario) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Libro} de un conjunto de Organismos donde un UsuarioEntidad puede hacer alguna acción
     * según el permiso que se le pasa por parámetro.
     * @param idPermiso
     * @param idUsuarioEntidad
     * @param organismos
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosOrganismoPermiso(Set<Long> organismos, Long idUsuarioEntidad, Long idPermiso) throws Exception;

    /**
     * Obtiene los UsuarioEntidad que pueden registrar en los libros de los Organismos seleccionados
     * @param organismos
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosRegistroOrganismo(List<Long> organismos) throws Exception;

    /**
     * Obtiene los UsuarioEntidad que pueden registrar en los libros de los Organismos seleccionados
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosRegistroEntidad(Long idEntidad) throws Exception;

    /**
     * Comprueba si un Usuario es Administrador de un determinado Libro.
     * @param idUsuarioEntidad
     * @param idLibro
     * @return
     * @throws Exception
     */
    Boolean isAdministradorLibro(Long idUsuarioEntidad, Long idLibro) throws Exception;

    /**
     * Comprueba si un Usuario tiene el Permiso espeficicado sobre el Libro especificado.
     * @param idUsuarioEntidad
     * @param idLibro
     * @param idPermiso
     * @return true si tiene el Permiso, false si no lo tiene.
     * @throws Exception
     */
    Boolean tienePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception;

    /**
     * Retorna la lista de {@link es.caib.regweb3.model.UsuarioEntidad} que tienen permisos en una lista de libros
     * @param libros
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosEntidadEnLibros(List<Libro> libros) throws Exception;

    /**
     * Modifica el estado de un Permiso que tiene un Usuario sobre un Libro
     * @param idPermisoLibroUsuario
     * @param activo
     * @throws Exception
     */
    void actualizarPermiso(Long idPermisoLibroUsuario, Boolean activo) throws Exception;

    /**
     * Crea los {@link es.caib.regweb3.model.PermisoLibroUsuario} a False de un UsuarioEntidad para todos los Libros de una Entidad
     * @param idEntidad
     * @throws Exception
     */
    void crearPermisosUsuarioNuevo(UsuarioEntidad usuarioEntidad, Long idEntidad) throws Exception;

    /**
     * Crea los {@link es.caib.regweb3.model.PermisoLibroUsuario} a False de un Libro para todos los UsuariosEntidad de una Entidad
     * @param libro
     * @param idEntidad
     * @throws Exception
     */
    void crearPermisosLibroNuevo(Libro libro, Long idEntidad) throws Exception;

//    /**
//     * Crea los {@link es.caib.regweb3.model.PermisoLibroUsuario} a False de todas las Entidades que todavía no estan creados
//     * @throws Exception
//     */
//    public void crearPermisosNoExistentes() throws Exception;

    /**
     * Comprueba si un Usuario tiene Inicializado el Permiso espeficicado sobre el Libro especificado.
     * @param idUsuarioEntidad
     * @param idLibro
     * @param idPermiso
     * @return true si ya existe el Permiso en la Base de Datos, false si no existe.
     * @throws Exception
     */
    Boolean existePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception;

    /**
     * Obtiene los Usuarios que tienen el permiso indicado en una lista de libros
     * @param libros
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> getUsuariosPermiso(Set<Libro> libros, Long permiso) throws Exception;

    /**
     * Elimina las PermisoLibroUsuario de un Usuario
     * @param idUsuarioEntidad
     * @throws Exception
     */
    void eliminarByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina las PermisoLibroUsuario de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
