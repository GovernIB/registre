package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Persona;
import es.caib.regweb3.model.utils.ObjetoBasico;
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
public interface PersonaLocal extends BaseEjb<Persona, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/PersonaEJB";


    /**
     * Guarda una persona normalizando algunos campos
     *
     * @param persona
     * @return
     * @throws I18NException
     */
    Persona guardarPersona(Persona persona) throws I18NException;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Persona} de una {@link es.caib.regweb3.model.Entidad} y su TipoPersona
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Persona> getAllbyEntidadTipo(Long idEntidad, Long tipoPersona) throws I18NException;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Persona} Juridicas de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Persona> getFisicasByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Persona} Fisicas de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Persona> getJuridicasByEntidad(Long idEntidad) throws I18NException;

    /**
     * * Comprueba la existencia de un Documento en el sistema
     *
     * @param documento
     * @return
     * @throws I18NException
     */
    Boolean existeDocumentoNew(String documento, Long idEntidad) throws I18NException;

    /**
     * Comprueba la existencia de un Documento en el sistema para la edición de una Persona
     *
     * @param documento
     * @param idPersona
     * @return
     * @throws I18NException
     */
    Boolean existeDocumentoEdit(String documento, Long idPersona, Long idEntidad) throws I18NException;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Persona} según los parámetros
     *
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipo
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer inicio, Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long tipo) throws I18NException;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Persona} según los parámetros
     *
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @return
     * @throws I18NException
     */
    List<Persona> busquedaFisicas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long idTipoPersona) throws I18NException;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Persona} según los parámetros
     *
     * @param documento
     * @return
     * @throws I18NException
     */
    List<Persona> busquedaJuridicas(Long idEntidad, String razonSocial, String documento, Long idTipoPersona) throws I18NException;

    /**
     * Elimina las Personas de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * @param q
     * @param tipoPersona
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<ObjetoBasico> busquedaPersonas(String q, Long tipoPersona, Long idEntidad) throws I18NException;

    /**
     * Busca las Personas con un mismo Documento de una Entidad determinada
     *
     * @param documento
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Persona> findByDocumento(String documento, Long idEntidad) throws I18NException;

    /**
     * Busca Personas duplicadas según su Documento
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Persona> buscarDuplicados(Long idEntidad) throws I18NException;

    /**
     * Lista las {@link es.caib.regweb3.model.Persona} para Exportar a Excel
     *
     * @param idEntidad
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipo
     * @return
     * @throws I18NException
     */
    List<Persona> getExportarExcel(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long tipo) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    void capitalizarPersonasJuridicas(Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @throws I18NException
     */
    void capitalizarPersonasFisicas(Long idEntidad) throws I18NException;
}
