package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.Libro;
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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
public interface OrganismoLocal extends BaseEjb<Organismo, Long> {

    /**
     * Obtiene el total de los organismos de una entidad
     *
     * @param entidad código de la entidad
     * @return
     * @throws Exception
     */
    Long getTotalByEntidad(Long entidad) throws Exception;

    /**
     * Obtiene el Organismo completo
     *
     * @param id del Organismo
     * @return
     * @throws Exception
     */
    Organismo findByIdCompleto(Long id) throws Exception;

    /**
     * Obtiene el Organismo ligero
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Organismo findByIdLigero(Long idOrganismo) throws Exception;

    /**
     * Obtiene los organismos de una entidad
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    List<Organismo> getAllByEntidad(Long entidad) throws Exception;


    /**
     * Obtiene los organismos paginados por entidad.
     *
     * @param inicio  código de la entidad
     * @param entidad código de la entidad
     * @return
     * @throws Exception
     */
    List<Organismo> getPaginationByEntidad(int inicio, Long entidad) throws Exception;

    /**
     * Obtiene un Organismo a partir de su código Dir3
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    Organismo findByCodigo(String codigo) throws Exception;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Organismo findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada independientemente del estado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Organismo findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada independientemente del estado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Organismo findByCodigoEntidadSinEstadoLigero(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene un Organismo a partir de su código Dir3
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    Organismo findByCodigoLigero(String codigo) throws Exception;


    /**
     * Devuelve Organismo con el mismo código dir3 en otra Entidad y con libros
     *
     * @param codigo
     * @param idEntidadActiva
     * @return
     * @throws Exception
     */
    Organismo findByCodigoOtraEntidadConLibros(String codigo, Long idEntidadActiva) throws Exception;

    /**
     * Devuelve el organismo por codigo si esta vigente
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    Organismo findByCodigoEntidadLigero(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene todos los organismos de una entidad solo id y denominacion
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    List<Organismo> findByEntidadReduce(Long entidad) throws Exception;

    /**
     * Obtiene todos los organismos de una entidad del estado indicado
     *
     * @param entidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<Organismo> findByEntidadByEstado(Long entidad, String estado) throws Exception;

    /**
     * Obtiene un Organismo a partir de su código Dir3, teniendo en cuenta que se trata de una instalación multientidad
     * @param codigo
     * @return
     * @throws Exception
     */
    Organismo findByCodigoMultiEntidad(String codigo) throws Exception;


    /**
     * Método que retorna todos los organismos de una entidad menos aquellos que les da soporte otra entidad en un entorno multientidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> getAllByEntidadMultiEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene un organismo distinguiendo que mètodo usar en función de si la instancia es multientidad o no.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Organismo findByCodigoByEntidadMultiEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Comprueba si el Organismo indicado es gestionado por REGWEB3
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean isOrganismoInterno(String codigo, Long idEntidad) throws Exception;

    /**
     * Obtiene los organismo vigentes de una entidad que tienen Ofcinas
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    List<Organismo> organismosConOficinas(Long entidad) throws Exception;

    /**
     * @param nivel
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosByNivel(Long nivel, Long idEntidad, String estado) throws Exception;

    /**
     * Realiza la búsqueda por nombre de los organismos de una entidad
     *
     * @param pageNumber
     * @param organismo
     * @return Paginacion
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Long idEntidad, Organismo organismo) throws Exception;

    /**
     * Método que obtiene los organismos vigentes y en los que puede registrar la oficina activa.
     *
     * @param oficinaActiva
     * @return List
     * @throws Exception
     */
    LinkedHashSet<Organismo> getByOficinaActiva(Oficina oficinaActiva, String estado) throws Exception;


    /**
     * Método que obtiene todos los organismo de la oficina activa sin generar OficioRemisión.
     * Este método permitirá mostrar el botón distribuir en caso de que el organismo esté extinguido,
     * anulado o transitorio, además de vigente
     *
     * @param oficinaActiva
     * @return List
     * @throws Exception
     */
    LinkedHashSet<Organismo> getAllByOficinaActiva(Oficina oficinaActiva) throws Exception;

    /**
     * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
     *
     * @param idOrganismo identificador del organismo
     * @return
     * @throws Exception
     */
    List<String> organismoSir(Long idOrganismo) throws Exception;

    /**
     * Activa la opción de permitir usuarios de un Organismo
     *
     * @param idOrganismo
     * @throws Exception
     */
    void activarUsuarios(Long idOrganismo) throws Exception;

    /**
     * Desactiva la opción de permitir usuarios de un Organismo
     *
     * @param idOrganismo
     * @throws Exception
     */
    void desactivarUsuarios(Long idOrganismo) throws Exception;

    /**
     * Obtiene todos los Organismos que permiten asociar usuarios
     *
     * @param entidad
     * @return
     * @throws Exception
     */
    List<Organismo> getPermitirUsuarios(Long entidad) throws Exception;

    /**
     * Obtiene el id de la entidad a la que pertenece este organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Long getEntidad(Long idOrganismo) throws Exception;

    /**
     * Obtiene el OrganismoSuperior de un Organismo, si es que lo tiene
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Organismo getOrganismoSuperior(Long idOrganismo) throws Exception;

    /**
     * Obtiene el OrganismoRaiz de un Organismo, si es que lo tiene
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Organismo getOrganismoRaiz(Long idOrganismo) throws Exception;

    /**
     * Elimina los Organismos de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los organismos finales que sustituyen a un organismo extinguido, por ello recorremos todos sus históricos
     *
     * @param id                identificador del organismo
     * @param historicosFinales
     * @throws Exception
     */
    void obtenerHistoricosFinales(Long id, Set<Organismo> historicosFinales) throws Exception;

    /**
     * Comprueba si un Organismo EDP tiene algún libro que le pueda registrar, comprobando sus organismos superiores
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Boolean isEdpConLibro(Long idOrganismo) throws Exception;

    /**
     * Obtiene el Libro que registra al Organismos indicado, si no tiene, obtendrá el del Organismo
     * inmediatamente superior
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    Libro obtenerLibroRegistro(Long idOrganismo) throws Exception;


    /**
     * Obtiene la UnidadTF de dir3caib a partir del código indicado
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    UnidadTF obtenerDestinoExterno(String codigo) throws Exception;


    /**
     * Dado un código dir3 obtiene los sustitutos a los que se puede enviar mediante SIR
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    List<UnidadTF> obtenerSustitutosExternosSIR(String codigo) throws Exception;

    /**
     * Dado un código dir3 obtiene todos sus sustitutos.
     *
     * @param codigo
     * @return
     * @throws Exception
     */
    List<UnidadTF> obtenerSustitutosExternos(String codigo) throws Exception;

    /**
     * Comprueba si un organismo está integrado con SIR
     * 
     * @param codigoDir3
     * @return
     * @throws Exception
     */
	boolean isDestinoDir3Sir(String codigoDir3) throws Exception;


}
