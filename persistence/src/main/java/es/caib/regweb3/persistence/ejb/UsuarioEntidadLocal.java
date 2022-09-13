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
     * @throws Exception
     */
    List<UsuarioEntidad> getPagination(int inicio, Long idEntidad) throws Exception;

    /**
     * Obtiene el total de registros para la paginacion
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long getTotal(Long idEntidad) throws Exception;

    /**
     * Crea un UsuarioEntidad, a partir de un identificador y la Entidad
     *
     * @param identificador
     * @param idEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    UsuarioEntidad comprobarUsuarioEntidad(String identificador, Long idEntidad) throws Exception, I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador.
     *
     * @param identificador
     * @return
     * @throws Exception
     */
    UsuarioEntidad findByIdentificador(String identificador) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador y a una Entidad
     *
     * @param identificador
     * @param idEntidad
     * @return
     * @throws Exception
     */
    UsuarioEntidad findByIdentificadorEntidad(String identificador, Long idEntidad) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador y el código DIR3 de la entidad
     *
     * @param identificador
     * @param codigoEntidad
     * @return
     * @throws Exception
     */
    UsuarioEntidad findByIdentificadorCodigoEntidad(String identificador, String codigoEntidad) throws Exception;


    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un documento.
     *
     * @param documento
     * @return
     * @throws Exception
     */
    UsuarioEntidad findByDocumento(String documento) throws Exception;


    /**
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> findByEntidad(Long idEntidad) throws Exception;

    /**
     * Retorna todos los UsuarioEntidad activos de una entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Usuario> findActivosByEntidad(Long idEntidad) throws Exception;

    /**
     * Devuelve los UsuarioEntidad de una Entidad que tiene el ROL RWE_ADMIN
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> findAdministradoresByEntidad(Long idEntidad) throws Exception;

    /**
     * Busca un UsuarioEntidad a partir del idUsuario y idEntidad
     *
     * @param idUsuario
     * @param idEntidad
     * @return
     * @throws Exception
     */
    UsuarioEntidad findByUsuarioEntidad(Long idUsuario, Long idEntidad) throws Exception;

    /**
     * Busca un UsuarioEntidad activo a partir del idUsuario y idEntidad
     *
     * @param idUsuario
     * @param idEntidad
     * @return
     * @throws Exception
     */
    UsuarioEntidad findByUsuarioEntidadActivo(Long idUsuario, Long idEntidad) throws Exception;

    /**
     * Obtiene todas las Entidades en las cuales el Usuario está asociado
     *
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Entidad> getEntidadesByUsuario(Long idUsuario) throws Exception;

    /**
     * Retorna los {@link es.caib.regweb3.model.Entidad} de un Usuario activos.
     *
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Entidad> findByUsuario(Long idUsuario) throws Exception;

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
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Long idEntidad, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario, Long idOrganismo, Long permiso) throws Exception;

    /**
     * Devuelve los usuarios de la Entidad activos que no son el usuario actual y según el tipo de usuario
     *
     * @param idEntidad
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> findUsuariosPlantilla(Long idEntidad, Long idUsuario, Long tipoUsuario) throws Exception;

    /**
     * Modifica la última Oficina utilizada por el usuario
     *
     * @param idUsuario
     * @param idOficina
     * @throws Exception
     */
    void actualizarOficinaUsuario(Long idUsuario, Long idOficina) throws Exception;

    /**
     * Devuelve los UsuarioEntidad de una Entidad que tiene el ROL RWE_USUARIO activos o no activos
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<UsuarioEntidad> findOperadoresByEntidad(Long idEntidad) throws Exception;

    /**
     * Elimina las UsuarioEntidad de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

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
     * @throws Exception
     */
    List<PermisoOrganismoUsuario> getExportarExcel(Long idEntidad, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipo, Long idOrganismo, Long permisoRegEntrada, Long permisoRegSalida, Long permisoSir) throws Exception;

}
