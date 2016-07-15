package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.UsuarioEntidad;

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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface EntidadLocal extends BaseEjb<Entidad, Long> {


    /**
     * Creamos una nueva Entidad y sus propiedades por defecto
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    public Entidad nuevaEntidad(Entidad entidad) throws Exception;

    /**
     * Retorna la Entidad cuyo CódigoDir3 es el indicado por parámetro
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    public Entidad findByCodigoDir3(String codigo) throws Exception;

    /**
     * Comprueba su una {@link es.caib.regweb3.model.Entidad} tiene algún {@link es.caib.regweb3.model.Organismo} asociado.
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Boolean tieneOrganismos(Long idEntidad) throws Exception;

    /**
     * Obtiene las entidades de un Usuario
     *
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<Entidad> getEntidadesAdministrador(Long idUsuario) throws Exception;

    /**
     * Obtiene las entidades de un Usuario de las que es Propietario
     *
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<Entidad> getEntidadesPropietario(Long idUsuario) throws Exception;

    /**
     * Comprueba si el codigoDir3 dado existe en alguna entidad excepto la selccionado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws Exception;

    /**
     * Comprueba si un Usuario es Administrador de la Entidad
     *
     * @param idEntidad
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    public Boolean esAdministrador(Long idEntidad, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Determina si un usuario es propietario o administrador de esa entidad
     *
     * @param idEntidad
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public Boolean esAutorizado(Long idEntidad, Long idUsuario) throws Exception;

    /**
     * Comprueba si una Entidad está marcada como Sir
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Boolean isSir(Long idEntidad) throws Exception;

    /**
     * Elimina todos los Registros y relaciones de una Entidad.
     *
     * @param idEntidad
     * @throws Exception
     */
    public void eliminarRegistros(Long idEntidad) throws Exception;


    /**
     * Elimina una Entidad y todos sus datos dependientes
     *
     * @param idEntidad
     * @throws Exception
     */
    public void eliminarEntidad(Long idEntidad) throws Exception;
}
