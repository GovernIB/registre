package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.LinkedHashSet;
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
     * Obtiene el Organismo completo
     *
     * @param id del Organismo
     * @return
     * @throws Exception
     */
    public Organismo findByIdCompleto(Long id) throws Exception;

    /**
     * Obtiene el Organismo ligero
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public Organismo findByIdLigero(Long idOrganismo) throws Exception;
    
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
    * Obtiene un Organismo a partir de su código Dir3
     * @param codigo
     * @return
     * @throws Exception
     */
    public Organismo findByCodigo(String codigo) throws Exception;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada independientemente del estado.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Organismo findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene un Organismo a partir de su código Dir3
     * @param codigo
     * @return
     * @throws Exception
     */
    public Organismo findByCodigoLigero(String codigo) throws Exception;

    /**
     *  Devuelve el organismo por codigo si esta vigente
     * @param codigo
     * @return
     * @throws Exception
     */
    public Organismo findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene los organismos de una entidad que tienen libros
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    public List<Organismo> findByEntidadLibros(Long entidad) throws Exception;

    /**
     * Obtiene todos los organismos de una entidad solo id y denominacion
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    public List<Organismo> findByEntidadReduce(Long entidad) throws Exception;

    /**
     * Obtiene todos los organismos de una entidad del estado indicado
     * @param entidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Organismo> findByEntidadByEstado(Long entidad, String estado) throws Exception;

    /**
     * Obtiene los organismo vigentes de una entidad que tienen Ofcinas
     * @param entidad
     * @return
     * @throws Exception
     */
    public List<Organismo> organismosConOficinas(Long entidad) throws Exception;

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
   public Paginacion busqueda(Integer pageNumber, Long idEntidad, String codigo, String denominacion, Long idCatEstadoEntidad) throws Exception;

   /**
     * Método que obtiene los organismos vigentes y en los que puede registrar la oficina activa.
     * @param oficinaActiva
     * @return List
     * @throws Exception
     */
    public LinkedHashSet<Organismo> getByOficinaActiva(Oficina oficinaActiva) throws Exception;

  /**
   * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
   * @param idOrganismo identificador del organismo
   * @return
   * @throws Exception
   */
    public List<String> organismoSir(Long idOrganismo) throws Exception;

    /**
     * Elimina los Organismos de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los organismos finales que sustituyen a un organismo extinguido, por ello recorremos todos sus históricos
     *
     * @param id                identificador del organismo
     * @param historicosFinales
     * @throws Exception
     */
    public void obtenerHistoricosFinales(Long id, Set<Organismo> historicosFinales) throws Exception;
}
