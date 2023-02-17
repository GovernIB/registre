package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.PermisoOrganismoUsuario;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
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
public interface UsuarioEntidadLocal extends BaseEjb<UsuarioEntidad, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/UsuarioEntidadEJB";


    /**
     * Obtiene X valores comenzando en la posicion pasada por parametro
     *
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * Obtiene el total de registros para la paginacion
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotal(Long idEntidad) throws I18NException;

    /**
     * Crea un UsuarioEntidad, a partir de un identificador y la Entidad
     *
     * @param identificador
     * @param idEntidad
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador.
     *
     * @param identificador
     * @return
     * @throws I18NException
     */
    UsuarioEntidad findByIdentificador(String identificador) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador y a una Entidad
     *
     * @param identificador
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    UsuarioEntidad findByIdentificadorEntidad(String identificador, Long idEntidad) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador y el código DIR3 de la entidad
     *
     * @param identificador
     * @param codigoEntidad
     * @return
     * @throws I18NException
     */
    UsuarioEntidad findByIdentificadorCodigoEntidad(String identificador, String codigoEntidad) throws I18NException;


    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un documento.
     *
     * @param documento
     * @return
     * @throws I18NException
     */
    UsuarioEntidad findByDocumento(String documento) throws I18NException;


    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> findByEntidad(Long idEntidad) throws I18NException;

    /**
     * Retorna todos los UsuarioEntidad activos de una entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Usuario> findActivosByEntidad(Long idEntidad) throws I18NException;

    /**
     * Devuelve los UsuarioEntidad de una Entidad que tiene el ROL RWE_ADMIN
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> findAdministradoresByEntidad(Long idEntidad) throws I18NException;

    /**
     * Busca un UsuarioEntidad a partir del idUsuario y idEntidad
     *
     * @param idUsuario
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    UsuarioEntidad findByUsuarioEntidad(Long idUsuario, Long idEntidad) throws I18NException;

    /**
     * Busca un UsuarioEntidad activo a partir del idUsuario y idEntidad
     *
     * @param idUsuario
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    UsuarioEntidad findByUsuarioEntidadActivo(Long idUsuario, Long idEntidad) throws I18NException;

    /**
     * Obtiene todas las Entidades en las cuales el Usuario está asociado
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Entidad> getEntidadesByUsuario(Long idUsuario) throws I18NException;

    /**
     * Retorna los {@link es.caib.regweb3.model.Entidad} de un Usuario activos.
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Entidad> findByUsuario(Long idUsuario) throws I18NException;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.UsuarioEntidad} según los parámetros
     *
     * @param pageNumber
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipoUsuario
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Long idEntidad, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario, Long idOrganismo, Long permiso) throws I18NException;

    /**
     * Devuelve los usuarios de la Entidad activos que no son el usuario actual y según el tipo de usuario
     *
     * @param idEntidad
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> findUsuariosPlantilla(Long idEntidad, Long idUsuario, Long tipoUsuario) throws I18NException;

    /**
     * Modifica la última Oficina utilizada por el usuario
     *
     * @param idUsuario
     * @param idOficina
     * @throws I18NException
     */
    void actualizarOficinaUsuario(Long idUsuario, Long idOficina) throws I18NException;

    /**
     * Devuelve los UsuarioEntidad de una Entidad que tiene el ROL RWE_USUARIO activos o no activos
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> findOperadoresByEntidad(Long idEntidad) throws I18NException;

    /**
     * Elimina las UsuarioEntidad de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Lista las {@link es.caib.regweb3.model.PermisoLibroUsuario} para Exportar a Excel
     *
     * @param idEntidad
     * @param identificador
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipo
     * @param idOrganismo
     * @param permisoRegEntrada
     * @param permisoRegSalida
     * @param permisoSir
     * @return
     * @throws I18NException
     */
    List<PermisoOrganismoUsuario> getExportarExcel(Long idEntidad, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipo, Long idOrganismo, Long permisoRegEntrada, Long permisoRegSalida, Long permisoSir) throws I18NException;

    /**
     * Se activa o desactiva que un Usuario sea OAMR
     * @param idUsuarioEntidad
     * @throws I18NException
     */
    void activarOAMR(Long idUsuarioEntidad, Boolean activo) throws I18NException;

    /**
     * Obtiene todos los usuarios marcados como OAMR de una entidad
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UsuarioEntidad> getOAMRByEntidad(Long idEntidad) throws I18NException;

}
