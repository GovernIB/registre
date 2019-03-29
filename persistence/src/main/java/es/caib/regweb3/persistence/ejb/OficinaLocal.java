package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface OficinaLocal extends BaseEjb<Oficina, Long> {

    /**
     * Busca una Oficina a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    Oficina findByCodigo(String codigo) throws Exception;

    /**
     * Obtiene la oficina del codigo indicado, la entidad indicada independientemente del estado.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Oficina findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws Exception;

    /**
     * Busca una Oficina a partir de su código
     * @param codigo
     * @return Solo el id de la Oficina
     * @throws Exception
     */
    Oficina findByCodigoLigero(String codigo) throws Exception;

    /**
     * Busca una Oficina a partir de su código y la Entidad a la que pertenece
     * @param codigo
     * @return
     * @throws Exception
     */
    Oficina findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Busca una Oficina válida a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    Oficina findByCodigoVigente(String codigo) throws Exception;

    /**
     * Obtiene las Oficinas cuyo Organismo responsable es el indicado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas funcionales de un Organismo
     * @param idOrganismo
     * @param oficinaVirtual
     * @return
     * @throws Exception
     */
    List<Oficina> oficinasFuncionales(Long idOrganismo, Boolean oficinaVirtual) throws Exception;

    /**
     * Obtiene las Oficinas organizativas de un Organismo
     * @param idOrganismo
     * @param oficinaVirtual
     * @return
     * @throws Exception
     */
    List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws Exception;

    /**
     * Obtiene las Oficinas SIR de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Oficina> oficinasSIR(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas SIR de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Oficina> oficinasSIREntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Oficina> findByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada y tienen el estado indicado
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas responsables cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<Oficina> responsableByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas dependientes cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<Oficina> dependienteByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Retorna las Oficinas(Funcionales y Organizativas) que dan servicio a un Organismo,
     * teniendo en cuenta las oficinas que dan servicio a sus Organismos superiores en el Organigrama.
     * @param idOrganismo
     * @param oficinaVirtual Boolean para tener en cuenta o no las REGISTRO_VIRTUAL_NO_PRESENCIAL
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> oficinasServicio(Long idOrganismo, Boolean oficinaVirtual) throws Exception;

    /**
     * Booleano si tiene Oficinas(Funcionales y Organizativas) que dan servicio a un Organismo,
     * teniendo en cuenta las oficinas que dan servicio a sus Organismos superiores en el Organigrama.
     * @param idOrganismo
     * @param oficinaVirtual
     * @return
     * @throws Exception
     */
    Boolean tieneOficinasServicio(Long idOrganismo, Boolean oficinaVirtual) throws Exception;


    /**
     * Obtiene las Oficinas que dan servicio a los Libros seleccionados
     * @param libros
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> oficinasRegistro(List<Libro> libros) throws Exception;

    /**
     * Obtiene las Oficinas que dan servicio SIR a los Libros seleccionados
     * @param libros
     * @return
     * @throws Exception
     */
    LinkedHashSet<Oficina> oficinasSIR(List<Libro> libros) throws Exception;

    /**
     * Consulta si una Oficina puede recibir via SIR
     * @param idOficina
     * @return
     * @throws Exception
     */
    Boolean isSIRRecepcion(Long idOficina) throws Exception;

    /**
     * Consulta si una Oficina puede enviar via SIR
     * @param idOficina
     * @return
     * @throws Exception
     */
    Boolean isSIREnvio(Long idOficina) throws Exception;

    /**
     * Elimina las Oficinas de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Oficina} según los parámetros
     *
     * @param pageNumber
     * @param codigo
     * @param denominacion
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Long idEntidad, String codigo, String denominacion, Long idCatEstadoEntidad) throws Exception;

    /**
     * Obtiene los usuarios que le dan servicio a una Oficina con el permiso indicado
     * @param idOficina
     * @return
     * @throws Exception
     */
    LinkedHashSet<UsuarioEntidad> usuariosPermisoOficina(Long idOficina) throws Exception;

    /**
     * Obtiene el id de la Entidad a la que pertenece la Oficina
     * @param codigo
     * @return
     * @throws Exception
     */
    Long obtenerEntidad(String codigo) throws Exception;
}
