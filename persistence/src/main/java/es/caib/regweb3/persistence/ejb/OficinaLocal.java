package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.Dir3Caib;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
public interface OficinaLocal extends BaseEjb<Oficina, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/OficinaEJB";


    /**
     * Busca una Oficina a partir de su código
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Oficina findByCodigo(String codigo) throws I18NException;

    /**
     * Busca una Oficina a partir de su código, teniendo en cuenta que se trata de una instalación Multientidad
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Oficina findByCodigoMultiEntidad(String codigo) throws I18NException;

    /**
     * Obtiene una oficina teniendo en cuenta la configuración de si es multientidad o no.
     * @param codigo
     * @return
     * @throws I18NException
     */
    Oficina findByMultiEntidad(String codigo) throws I18NException;

    /**
     * Obtiene una oficina distinguiendo que mètodo usar en función de si la instancia es multientidad o no.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Oficina findByCodigoByEntidadMultiEntidad(String codigo, Long idEntidad) throws I18NException;


    /**
     * Obtiene la oficina del codigo indicado, la entidad indicada independientemente del estado.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Oficina findByCodigoEntidadSinEstado(String codigo, Long idEntidad) throws I18NException;

    /**
     * Busca una Oficina a partir de su código
     *
     * @param codigo
     * @return Solo el id de la Oficina
     * @throws I18NException
     */
    Oficina findByCodigoLigero(String codigo) throws I18NException;

    /**
     * Busca una Oficina a partir de su código y la Entidad a la que pertenece
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Oficina findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException;

    /**
     * Busca una Oficina válida a partir de su código
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Oficina findByCodigoVigente(String codigo) throws I18NException;

    /**
     * Obtiene las Oficinas cuyo Organismo responsable es el indicado
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws I18NException;

    /**
     * Obtiene las Oficinas funcionales de un Organismo
     *
     * @param idOrganismo
     * @param oficinaVirtual
     * @return
     * @throws I18NException
     */
    List<Oficina> oficinasFuncionales(Long idOrganismo, Boolean oficinaVirtual) throws I18NException;

    /**
     * Obtiene las Oficinas organizativas de un Organismo
     *
     * @param idOrganismo
     * @param oficinaVirtual
     * @return
     * @throws I18NException
     */
    List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws I18NException;

    /**
     * Obtiene las Oficinas SIR de un Organismo
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Oficina> oficinasSIR(Long idOrganismo) throws I18NException;

    /**
     * Obtiene las Oficinas SIR de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Oficina> oficinasSIREntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Oficina> findByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene todos las oficinas de una entidad solo id y denominacion
     * @param entidad
     * @return
     * @throws I18NException
     */
    List<Oficina> findByEntidadReduce(Long entidad) throws I18NException;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada y tienen el estado indicado
     *
     * @param idEntidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws I18NException;


    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada y tienen el estado indicado y elimina aquellas oficinas
     * que en un entorno multientidad estan repetidas en la entidad que les da servicio.
     *
     * @param idEntidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Oficina> findByEntidadByEstadoMultiEntidad(Long idEntidad, String estado) throws I18NException;

    /**
     * Obtiene las Oficinas responsables cuya Entidad responsable es la indicada
     *
     * @param idEntidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Oficina> responsableByEntidadEstado(Long idEntidad, String estado) throws I18NException;

    /**
     * Obtiene las Oficinas dependientes cuya Entidad responsable es la indicada
     *
     * @param idEntidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Oficina> dependienteByEntidadEstado(Long idEntidad, String estado) throws I18NException;

    /**
     * Retorna las Oficinas(Funcionales y Organizativas) que dan servicio a un Organismo,
     * teniendo en cuenta las oficinas que dan servicio a sus Organismos superiores en el Organigrama.
     *
     * @param idOrganismo
     * @param oficinaVirtual Boolean para tener en cuenta o no las REGISTRO_VIRTUAL_NO_PRESENCIAL
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> oficinasServicioCompleto(Long idOrganismo, Boolean oficinaVirtual) throws I18NException;

    /**
     * Booleano si tiene Oficinas(Funcionales y Organizativas) que dan servicio a un Organismo,
     * teniendo en cuenta las oficinas que dan servicio a sus Organismos superiores en el Organigrama.
     *
     * @param idOrganismo
     * @param oficinaVirtual
     * @return
     * @throws I18NException
     */
    Boolean tieneOficinasServicio(Long idOrganismo, Boolean oficinaVirtual) throws I18NException;


    /**
     * Obtiene las Oficinas que dan servicio a los Organismos seleccionados
     *
     * @param organismos
     * @param oficinaVirtual 'true' se incluirán las Oficinas virtuales
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> oficinasServicio(List<Organismo> organismos, Boolean oficinaVirtual) throws I18NException;

    /**
     * Obtiene las Oficinas que dan servicio SIR a los Libros seleccionados
     *
     * @param organismos
     * @return
     * @throws I18NException
     */
    LinkedHashSet<Oficina> oficinasSIR(List<Organismo> organismos) throws I18NException;

    /**
     * Consulta si una Oficina puede recibir via SIR
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Boolean isSIRRecepcion(Long idOficina) throws I18NException;

    /**
     * Consulta si una Oficina puede enviar via SIR
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Boolean isSIREnvio(Long idOficina) throws I18NException;

    /**
     * Consulta si una Oficina es SIR (Servicio Integrada en SIR)
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Boolean isSIR(Long idOficina) throws I18NException;


    /**
     * Consulta si una Oficina es SIR Completa(tiene todos los servicios SIR: Integrada en SIR, SIR Envio, SIR Recepción)
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Boolean isSIRCompleto(Long idOficina) throws I18NException;

    /**
     * Elimina las Oficinas de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Oficina} según los parámetros
     *
     * @param pageNumber
     * @param codigo
     * @param denominacion
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Long idEntidad, String codigo, String denominacion, Long idCatEstadoEntidad) throws I18NException;

    /**
     * Obtiene el id de la Entidad a la que pertenece la Oficina
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Long obtenerEntidad(String codigo) throws I18NException;


    /**
     * Obtiene el id de la Entidad a la que pertenece la Oficina en un entorno multientidad
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Long obtenerEntidadMultiEntidad(String codigo) throws I18NException;

    /**
     * Obtiene las oficinas SIR desde dir3caib(via WS) de la unidad indicada en el código
     *
     * @param codigo
     * @param dir3caib
     * @return
     * @throws I18NException
     */
    List<OficinaTF> obtenerOficinasSir(String codigo, Dir3Caib dir3caib) throws I18NException;
}
