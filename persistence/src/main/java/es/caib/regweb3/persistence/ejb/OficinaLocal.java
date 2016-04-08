package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.utils.ObjetoBasico;
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
    public List<ObjetoBasico> findByOrganismoResponsableVO(Long idOrganismo) throws Exception;

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
     * @return
     * @throws Exception
     */
    public Boolean tieneOficinasOrganismo(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas que dan servicio a los Libros seleccionados
     * @param libros
     * @return
     * @throws Exception
     */
    public LinkedHashSet<ObjetoBasico> oficinasRegistro(List<Libro> libros) throws Exception;

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
    public Paginacion busqueda(Integer pageNumber, String codigo, String denominacion) throws Exception;
}
