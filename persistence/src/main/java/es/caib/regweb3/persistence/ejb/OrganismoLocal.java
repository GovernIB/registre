package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
public interface OrganismoLocal extends BaseEjb<Organismo, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/OrganismoEJB";


    /**
     * Obtiene el total de los organismos de una entidad
     *
     * @param entidad código de la entidad
     * @return
     * @throws I18NException
     */
    Long getTotalByEntidad(Long entidad) throws I18NException;

    /**
     * Obtiene el Organismo completo
     *
     * @param id del Organismo
     * @return
     * @throws I18NException
     */
    Organismo findByIdCompleto(Long id) throws I18NException;

    /**
     * Obtiene el Organismo ligero
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Organismo findByIdLigero(Long idOrganismo) throws I18NException;

    /**
     * Obtiene los organismos de una entidad
     *
     * @param entidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getAllByEntidad(Long entidad) throws I18NException;


    /**
     * Obtiene los organismos paginados por entidad.
     *
     * @param inicio  código de la entidad
     * @param entidad código de la entidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getPaginationByEntidad(int inicio, Long entidad) throws I18NException;

    /**
     * Obtiene un Organismo a partir de su código Dir3
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Organismo findByCodigo(String codigo) throws I18NException;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada independientemente del estado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws I18NException;

    /**
     * Obtiene el organismo de codigo indicado y de la entidad indicada independientemente del estado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoEntidadSinEstadoLigero(String codigo, Long idEntidad) throws I18NException;

    /**
     * Obtiene un Organismo a partir de su código Dir3
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoLigero(String codigo) throws I18NException;


    /**
     * Devuelve Organismo con el mismo código dir3 en otra Entidad y con libros
     *
     * @param codigo
     * @param idEntidadActiva
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoOtraEntidadConLibros(String codigo, Long idEntidadActiva) throws I18NException;

    /**
     * Devuelve el organismo por codigo si esta vigente
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoEntidadLigero(String codigo, Long idEntidad) throws I18NException;

    /**
     * Obtiene todos los organismos de una entidad solo id y denominacion
     *
     * @param entidad
     * @return
     * @throws I18NException
     */
    List<Organismo> findByEntidadReduce(Long entidad) throws I18NException;

    /**
     * Obtiene todos los organismos de una entidad del estado indicado
     *
     * @param entidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Organismo> findByEntidadByEstado(Long entidad, String estado) throws I18NException;

    /**
     * Obtiene un Organismo a partir de su código Dir3, teniendo en cuenta que se trata de una instalación multientidad
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoMultiEntidad(String codigo) throws I18NException;

    /**
     * Método que retorna todos los organismos de una entidad menos aquellos que les da soporte otra entidad en un entorno multientidad
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getAllByEntidadMultiEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene un organismo distinguiendo que mètodo usar en función de si la instancia es multientidad o no.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Organismo findByCodigoByEntidadMultiEntidad(String codigo, Long idEntidad) throws I18NException;

    /**
     * Comprueba si el Organismo indicado es gestionado por REGWEB3
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean isOrganismoInterno(String codigo, Long idEntidad) throws I18NException;

    /**
     * Obtiene los organismo vigentes de una entidad que tienen Ofcinas
     *
     * @param entidad
     * @return
     * @throws I18NException
     */
    List<Organismo> organismosConOficinas(Long entidad) throws I18NException;

    /**
     * @param nivel
     * @param idEntidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosByNivel(Long nivel, Long idEntidad, String estado) throws I18NException;

    /**
     * Realiza la búsqueda por nombre de los organismos de una entidad
     *
     * @param pageNumber
     * @param organismo
     * @return Paginacion
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Long idEntidad, Organismo organismo) throws I18NException;

    /**
     * Método que obtiene los organismos vigentes y en los que puede registrar la oficina activa.
     *
     * @param oficinaActiva
     * @return List
     * @throws I18NException
     */
    LinkedHashSet<Organismo> getByOficinaActiva(Oficina oficinaActiva, String estado) throws I18NException;


    /**
     * Método que obtiene todos los organismo de la oficina activa sin generar OficioRemisión.
     * Este método permitirá mostrar el botón distribuir en caso de que el organismo esté extinguido,
     * anulado o transitorio, además de vigente
     *
     * @param oficinaActiva
     * @return List
     * @throws I18NException
     */
    LinkedHashSet<Organismo> getAllByOficinaActiva(Oficina oficinaActiva) throws I18NException;

    /**
     * Método que nos devuelve los códigos DIR3 de las oficinas SIR de un organismo
     *
     * @param idOrganismo identificador del organismo
     * @return
     * @throws I18NException
     */
    List<String> organismoSir(Long idOrganismo) throws I18NException;

    /**
     * Activa la opción de permitir usuarios de un Organismo
     *
     * @param idOrganismo
     * @throws I18NException
     */
    void activarUsuarios(Long idOrganismo) throws I18NException;

    /**
     * Desactiva la opción de permitir usuarios de un Organismo
     *
     * @param idOrganismo
     * @throws I18NException
     */
    void desactivarUsuarios(Long idOrganismo) throws I18NException;

    /**
     * Obtiene todos los Organismos que permiten asociar usuarios
     *
     * @param entidad
     * @return
     * @throws I18NException
     */
    List<Organismo> getPermitirUsuarios(Long entidad) throws I18NException;

    /**
     * Obtiene el id de la entidad a la que pertenece este organismo
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Long getEntidad(Long idOrganismo) throws I18NException;

    /**
     * Obtiene el OrganismoSuperior de un Organismo, si es que lo tiene
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Organismo getOrganismoSuperior(Long idOrganismo) throws I18NException;

    /**
     * Obtiene el OrganismoRaiz de un Organismo, si es que lo tiene
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Organismo getOrganismoRaiz(Long idOrganismo) throws I18NException;

    /**
     * Elimina los Organismos de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene los organismos finales que sustituyen a un organismo extinguido, por ello recorremos todos sus históricos
     *
     * @param id                identificador del organismo
     * @param historicosFinales
     * @throws I18NException
     */
    void obtenerHistoricosFinales(Long id, Set<Organismo> historicosFinales) throws I18NException;

    /**
     * Comprueba si un Organismo EDP tiene algún libro que le pueda registrar, comprobando sus organismos superiores
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Boolean isEdpConLibro(Long idOrganismo) throws I18NException;

    /**
     * Obtiene el Libro que registra al Organismos indicado, si no tiene, obtendrá el del Organismo
     * inmediatamente superior
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Libro obtenerLibroRegistro(Long idOrganismo) throws I18NException;


    /**
     * Obtiene la UnidadTF de dir3caib a partir del código indicado
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    UnidadTF obtenerDestinoExterno(String codigo, Long idEntidad) throws I18NException;


    /**
     * Dado un código dir3 obtiene los sustitutos a los que se puede enviar mediante SIR
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UnidadTF> obtenerSustitutosExternosSIR(String codigo, Long idEntidad) throws I18NException;

    /**
     * Dado un código dir3 obtiene todos sus sustitutos.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<UnidadTF> obtenerSustitutosExternos(String codigo, Long idEntidad) throws I18NException;


}
