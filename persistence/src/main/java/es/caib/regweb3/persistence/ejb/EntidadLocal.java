package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.UsuarioEntidad;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface EntidadLocal extends BaseEjb<Entidad, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/EntidadEJB";

    /**
     * Obtiene una Entidad con unos campos minimnos
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Entidad findByIdLigero(Long idEntidad) throws I18NException;

    /**
     * Creamos una nueva Entidad y sus propiedades por defecto
     *
     * @param entidad
     * @return
     * @throws I18NException
     */
    Entidad nuevaEntidad(Entidad entidad) throws I18NException;

    /**
     * Retorna la Entidad cuyo CódigoDir3 es el indicado por parámetro
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Entidad findByCodigoDir3(String codigo) throws I18NException;

    /**
     * Comprueba su una {@link es.caib.regweb3.model.Entidad} tiene algún {@link es.caib.regweb3.model.Organismo} asociado.
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean tieneOrganismos(Long idEntidad) throws I18NException;

    /**
     * Obtiene las entidades de un Usuario
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Entidad> getEntidadesAdministrador(Long idUsuario) throws I18NException;

    /**
     * Obtiene las entidades de un Usuario de las que es Propietario
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Entidad> getEntidadesPropietario(Long idUsuario) throws I18NException;

    /**
     * Obtiene las entidades activas.
     *
     * @return
     * @throws I18NException
     */
    List<Entidad> getEntidadesActivas() throws I18NException;

    /**
     * Comprueba si el codigoDir3 dado existe en alguna entidad excepto la selccionado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean existeCodigoDir3Edit(String codigo, Long idEntidad) throws I18NException;

    /**
     * Comprueba si un Usuario es Administrador de la Entidad
     *
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean esAdministrador(UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Determina si un usuario es propietario o administrador de esa entidad
     *
     * @param idEntidad
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    Boolean esAutorizado(Long idEntidad, Long idUsuario) throws I18NException;

    /**
     * Comprueba si una Entidad está marcada como Sir
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean isSir(Long idEntidad) throws I18NException;

    /**
     * Obtiene todas las entidades que están marcadas como SIR
     *
     * @return
     * @throws I18NException
     */
    List<Entidad> getEntidadesSir() throws I18NException;

    /**
     * Comprueba las diferentes dependencias del UsuarioEntidad para saber si es posible eliminarlo
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean puedoEliminarlo(Long idUsuarioEntidad) throws I18NException;

    /**
     * Elimina todos los Registros y relaciones de una Entidad.
     *
     * @param idEntidad
     * @throws I18NException
     */
    void eliminarRegistros(Long idEntidad) throws I18NException;


    /**
     * Elimina una Entidad y todos sus datos dependientes
     *
     * @param idEntidad
     * @throws I18NException
     */
    void eliminarEntidad(Long idEntidad) throws I18NException;

    /**
     * Marca/Desmarca una entidad que está en mantenimiento durante el proceso de sincronización
     *
     * @param idEntidad
     * @param mantenimiento
     * @throws I18NException
     */
    void marcarEntidadMantenimiento(Long idEntidad, Boolean mantenimiento) throws I18NException;

    /**
     * Comprueba si la Entidad tiene el plugin de Custodia Justificante DocumentCustody y lo consideraremos como "Custodiado Fake" a efectos prácticos (SIR, Distribución, etc..)
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean isJustificanteCustodiadoLocal(Long idEntidad) throws I18NException;

}
