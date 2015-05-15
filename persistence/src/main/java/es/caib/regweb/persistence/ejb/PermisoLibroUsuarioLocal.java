package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Libro;
import es.caib.regweb.model.Organismo;
import es.caib.regweb.model.PermisoLibroUsuario;
import es.caib.regweb.model.UsuarioEntidad;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface PermisoLibroUsuarioLocal extends BaseEjb<PermisoLibroUsuario, Long> {

    /**
     * Retorna los {@link es.caib.regweb.model.PermisoLibroUsuario} a partir de un {@link es.caib.regweb.model.UsuarioEntidad}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<PermisoLibroUsuario> findByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.PermisoLibroUsuario} a partir de un {@link es.caib.regweb.model.UsuarioEntidad}
     * y de un {@link es.caib.regweb.model.Libro}
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<PermisoLibroUsuario> findByUsuarioLibro(Long idUsuarioEntidad, Long idLibro) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.PermisoLibroUsuario} de un {@link es.caib.regweb.model.Libro}
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<PermisoLibroUsuario> findByLibro( Long idLibro) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.UsuarioEntidad} de un {@link es.caib.regweb.model.Libro}
     * @param idLibro
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> getUsuariosEntidadByLibro( Long idLibro) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.Libro} en los que un Usuario tiene permisos de Registro de Entrada o Salida
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosRegistro(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.Libro} en los que un UsuarioEntidad puede Administrar
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosAdministrados(Long idUsuarioEntidad) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.Libro} a los que un UsuarioEntidad
     * @param idUsuarioEntidad
     * @param idPermiso
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosPermiso(Long idUsuarioEntidad, Long idPermiso) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.Libro} en los que puede registrar una Oficina y un UsuarioEntidad
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosRegistroOficina(Set<Organismo> organismos, UsuarioEntidad usuario) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.Libro} de un conjunto de Organismos donde un UsuarioEntidad puede hacer alguna acción
     * según el permiso que se le pasa por parámetro.
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosOrganismoPermiso(Set<Organismo> organismos, UsuarioEntidad usuario, Long idPermiso) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb.model.Libro} de una Entidad donde un UsuarioEntidad puede hacer alguna acción
     * según el permiso que se le pasa por parámetro.
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosEntidadPermiso(Long idEntidad, UsuarioEntidad usuario, Long idPermiso) throws Exception;

    /**
     * Comprueba si un Usuario es Administrador de un determinado Libro.
     * @param idUsuarioEntidad
     * @param idLibro
     * @return
     * @throws Exception
     */
    public Boolean isAdministradorLibro(Long idUsuarioEntidad, Long idLibro) throws Exception;

    /**
     * Comprueba si un Usuario tiene el Permiso espeficicado sobre el Libro especificado.
     * @param idUsuarioEntidad
     * @param idLibro
     * @param idPermiso
     * @return true si tiene el Permiso, false si no lo tiene.
     * @throws Exception
     */
    public Boolean tienePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception;

    /**
     * Retorna la lista de {@link es.caib.regweb.model.UsuarioEntidad} que tienen permisos en una lista de libros
     * @param libros
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> getUsuariosEntidadEnLibros(List<Libro> libros) throws Exception;

    /**
     * Modifica el estado de un Permiso que tiene un Usuario sobre un Libro
     * @param idPermisoLibroUsuario
     * @param activo
     * @throws Exception
     */
    public void actualizarPermiso(Long idPermisoLibroUsuario, Boolean activo) throws Exception;

    /**
     * Crea los {@link es.caib.regweb.model.PermisoLibroUsuario} a False de un UsuarioEntidad para todos los Libros de una Entidad
     * @param idEntidad
     * @throws Exception
     */
    public void crearPermisosUsuarioNuevo(UsuarioEntidad usuarioEntidad, Long idEntidad) throws Exception;

    /**
     * Crea los {@link es.caib.regweb.model.PermisoLibroUsuario} a False de un Libro para todos los UsuariosEntidad de una Entidad
     * @param libro
     * @param idEntidad
     * @throws Exception
     */
    public void crearPermisosLibroNuevo(Libro libro, Long idEntidad) throws Exception;

    /**
     * Crea los {@link es.caib.regweb.model.PermisoLibroUsuario} a False de todas las Entidades que todavía no estan creados
     * @throws Exception
     */
    public void crearPermisosNoExistentes() throws Exception;

    /**
     * Comprueba si un Usuario tiene Inicializado el Permiso espeficicado sobre el Libro especificado.
     * @param idUsuarioEntidad
     * @param idLibro
     * @param idPermiso
     * @return true si ya existe el Permiso en la Base de Datos, false si no existe.
     * @throws Exception
     */
    public Boolean existePermiso(Long idUsuarioEntidad, Long idLibro, Long idPermiso) throws Exception;

}
