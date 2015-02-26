package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Organismo;
import es.caib.regweb.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface OrganismoLocal extends BaseEjb<Organismo, Long> {

    /**
     *  Obtiene el total de los organismos de una entidad
     * @param entidad código de la entidad
     * @return
     * @throws Exception
     */
    public Long getTotalByEntidad(Long entidad) throws Exception;
    
    
    /**
     * Obtiene los organismos de una entidad
     * @param entidad
     * @return
     * @throws Exception
     */
    public List<Organismo> getAllByEntidad(Long entidad) throws Exception;
    
    

   /**
     *  Obtiene los organismos paginados por entidad.
     * @param inicio código de la entidad
     * @param entidad código de la entidad
     * @return
     * @throws Exception
     */
    public List<Organismo> getPaginationByEntidad(int inicio, Long entidad) throws Exception;

   /**
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    public Organismo findByCodigo(String codigo) throws Exception;

    /**
     *  Devuelve el organismo por codigo si esta vigente
     * @param codigo
     * @return
     * @throws Exception
     */
    public Organismo findByCodigoVigente(String codigo) throws Exception;

    /**
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    public List<Organismo> findByEntidad(Long entidad) throws Exception;

     /**
     *
     * @param codigoEstado
     * @return
     * @throws Exception
     */
    public List<Organismo> findByEstado(String codigoEstado) throws Exception;

    /**
     * Devuelve los Organismos hijos de un Organismos dado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Organismo> getHijos(Long idOrganismo) throws Exception;

    /**
     *
     * @param idOrganismoSuperior
     * @return
     * @throws Exception
     */
    public List<Organismo> getOrganismosPrimerNivel(Long idOrganismoSuperior) throws Exception;

    /**
     *
     * @param nivel
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Organismo> getOrganismosByNivel(Long nivel, Long idEntidad, String estado) throws Exception;

   /**
     * Realiza la búsqueda por nombre de los organismos de una entidad
     * @param pageNumber
     * @param idEntidad
     * @param denominacion
     * @return Paginacion
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String denominacion, Long idCatEstadoEntidad) throws Exception;

   /**
     * Método que obtiene los organismos vigentes y en los que puede registrar la oficina activa.
     * @param idOficina
     * @return List
     * @throws Exception
     */
    public Set<Organismo> getByOficinaActiva(Long idOficina) throws Exception;

  /**
   * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
   * @param idOrganismo identificador del organismo
   * @return
   * @throws Exception
   */
    public List<String> organismoSir(Long idOrganismo) throws Exception;

   /**
   * Método que obtiene los organismos relacionados con un libro
   * @param idLibro identificador del libro
   * @return
   * @throws Exception
   */
    public List<Organismo> getByLibro(Long idLibro) throws Exception;
}
