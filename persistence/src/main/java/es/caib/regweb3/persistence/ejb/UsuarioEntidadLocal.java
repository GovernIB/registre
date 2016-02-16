package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface UsuarioEntidadLocal extends BaseEjb<UsuarioEntidad, Long> {

    /**
     * Obtiene X valores comenzando en la posicion pasada por parametro
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> getPagination(int inicio, Long idEntidad) throws Exception;

    /**
     * Obtiene el total de registros para la paginacion
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long getTotal(Long idEntidad) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador.
     * @param identificador
     * @return
     * @throws Exception
     */
    public UsuarioEntidad findByIdentificador(String identificador) throws Exception;

    /**
     *  Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador y a una Entidad
     * @param identificador
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public UsuarioEntidad findByIdentificadorEntidad(String identificador, Long idEntidad) throws Exception;

    /**
     *  Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un identificador y el código DIR3 de la entidad
     * @param identificador
     * @param codigoEntidad
     * @return
     * @throws Exception
     */
    public UsuarioEntidad findByIdentificadorCodigoEntidad(String identificador, String codigoEntidad) throws Exception;


    /**
     * Retorna el {@link es.caib.regweb3.model.UsuarioEntidad} asociado a un documento.
     * @param documento
     * @return
     * @throws Exception
     */
    public UsuarioEntidad findByDocumento(String documento) throws Exception;


    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> findByEntidad(Long idEntidad) throws Exception;

    /**
     * Retorna todos los UsuarioEntidad activos de una entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> findActivosByEntidad(Long idEntidad) throws Exception;

    /**
     * Devuelve los UsuarioEntidad de una Entidad que tiene el ROL RWE_ADMIN
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> findAdministradoresByEntidad(Long idEntidad) throws Exception;

    /**
     *  Busca un UsuarioEntidad a partir del idUsuario y idEntidad
     * @param idUsuario
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public UsuarioEntidad findByUsuarioEntidad(Long idUsuario, Long idEntidad) throws Exception;

    /**
     * Busca un UsuarioEntidad activo a partir del idUsuario y idEntidad
     * @param idUsuario
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public UsuarioEntidad findByUsuarioEntidadActivo(Long idUsuario, Long idEntidad) throws Exception;

    /**
     * Obtiene todas las Entidades en las cuales el Usuario está asociado
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<Entidad> getEntidadesByUsuario(Long idUsuario) throws Exception;

    /**
    * Retorna los {@link es.caib.regweb3.model.UsuarioEntidad} de un Usuario activos.
    * @param idUsuario
    * @return
    * @throws Exception
    */
    public List<UsuarioEntidad> findByUsuario(Long idUsuario) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.UsuarioEntidad} según los parámetros
     * @param pageNumber
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber,Long idEntidad,String identificador,String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws Exception;

    /**
     *
     * @param idEntidad
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> findByEntidadSinActivo(Long idEntidad, Long idUsuario) throws Exception;

    /**
     * Modifica la última Oficina utilizada por el usuario
     * @param idUsuario
     * @param idOficina
     * @throws Exception
     */
    public void actualizarOficinaUsuario(Long idUsuario, Long idOficina) throws Exception;

    /**
     * Devuelve los UsuarioEntidad de una Entidad que tiene el ROL RWE_USUARIO activos o no activos
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<UsuarioEntidad> findOperadoresByEntidad(Long idEntidad) throws Exception;

    /**
     * Elimina las UsuarioEntidad de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Comprueba las diferentes dependencias del UsuarioEntidad para saber si es posible eliminarlo
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    public Boolean puedoEliminarlo(Long idUsuarioEntidad) throws Exception;
}
