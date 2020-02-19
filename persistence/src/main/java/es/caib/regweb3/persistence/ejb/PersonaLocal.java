package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Persona;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface PersonaLocal extends BaseEjb<Persona, Long> {

    /**
     * Guarda una persona normalizando algunos campos
     * @param persona
     * @return
     * @throws Exception
     */
    Persona guardarPersona(Persona persona) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Persona} de una {@link es.caib.regweb3.model.Entidad} y su TipoPersona
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Persona> getAllbyEntidadTipo(Long idEntidad, Long tipoPersona) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Persona} Juridicas de una {@link es.caib.regweb3.model.Entidad}
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Persona> getFisicasByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Persona} Fisicas de una {@link es.caib.regweb3.model.Entidad}
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Persona> getJuridicasByEntidad(Long idEntidad) throws Exception;

    /**
     * * Comprueba la existencia de un Documento en el sistema
     * @param documento
     * @return
     * @throws Exception
     */
    Boolean existeDocumentoNew(String documento, Long idEntidad) throws Exception;

    /**
     * Comprueba la existencia de un Documento en el sistema para la edición de una Persona
     * @param documento
     * @param idPersona
     * @return
     * @throws Exception
     */
    Boolean existeDocumentoEdit(String documento, Long idPersona, Long idEntidad) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Persona} según los parámetros
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipo
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer inicio, Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long tipo) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Persona} según los parámetros
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @return
     * @throws Exception
     */
    List<Persona> busquedaFisicas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long idTipoPersona) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Persona} según los parámetros
     * @param documento
     * @return
     * @throws Exception
     */
    List<Persona> busquedaJuridicas(Long idEntidad, String razonSocial, String documento, Long idTipoPersona) throws Exception;

    /**
     * Elimina las Personas de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     *
     * @param q
     * @param tipoPersona
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<ObjetoBasico> busquedaPersonas(String q, Long tipoPersona, Long idEntidad) throws Exception;

    /**
     * Busca las Personas con un mismo Documento de una Entidad determinada
     * @param documento
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Persona> findByDocumento(String documento, Long idEntidad) throws Exception;

    /**
     * Busca Personas duplicadas según su Documento
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Persona> buscarDuplicados(Long idEntidad) throws Exception;

    /**
     * Lista las {@link es.caib.regweb3.model.Persona} para Exportar a Excel
     * @param idEntidad
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipo
     * @return
     * @throws Exception
     */
    List<Persona> getExportarExcel(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long tipo) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    void capitalizarPersonasJuridicas(Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @throws Exception
     */
    void capitalizarPersonasFisicas(Long idEntidad) throws Exception;
}
