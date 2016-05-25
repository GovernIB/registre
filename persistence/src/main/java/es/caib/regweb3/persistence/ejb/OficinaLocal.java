package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface OficinaLocal extends BaseEjb<Oficina, Long> {

    /**
     * Busca una Oficina a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    public Oficina findByCodigo(String codigo) throws Exception;

    /**
     * Busca una Oficina a partir de su código
     * @param codigo
     * @return Solo el id de la Oficina
     * @throws Exception
     */
    public Oficina findByCodigoLigero(String codigo) throws Exception;

    /**
     * Busca una Oficina a partir de su código y la Entidad a la que pertenece
     * @param codigo
     * @return
     * @throws Exception
     */
    public Oficina findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Busca una Oficina válida a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    public Oficina findByCodigoVigente(String codigo) throws Exception;

    /**
     * Obtiene las Oficinas cuyo Organismo responsable es el indicado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas cuyo Organismo responsable es el indicado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Oficina> findByOrganismoResponsableVO(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<Oficina> findByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada y tienen el estado indicado
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas responsables cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Oficina> responsableByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas dependientes cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Oficina> dependienteByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Dice si el organismo indicado tiene oficinas donde registrar
     * @param idOrganismo
     * @param oficinaVirtual = false las oficinas virtuales se consideran oficinas validas para poder crear libros.
     * @return
     * @throws Exception
     */
    public Boolean tieneOficinasOrganismo(Long idOrganismo, boolean oficinaVirtual) throws Exception;

    /**
     * Obtiene las Oficinas que dan servicio a los Libros seleccionados
     * @param libros
     * @return
     * @throws Exception
     */
    public LinkedHashSet<Oficina> oficinasRegistro(List<Libro> libros) throws Exception;

    /**
     * Obtiene las Oficinas que dan Servicio a un determinado Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public LinkedHashSet<Oficina> oficinasRegistroOrganismo(Long idOrganismo) throws Exception;

    /**
     * Elimina las Oficinas de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Oficina} según los parámetros
     *
     * @param pageNumber
     * @param codigo
     * @param denominacion
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber, Long idEntidad, String codigo, String denominacion, Long idCatEstadoEntidad) throws Exception;
}
