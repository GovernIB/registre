package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Persona;
import es.caib.regweb.persistence.utils.Paginacion;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface PersonaLocal extends BaseEjb<Persona, Long> {

    /**
     * Obtiene todas las {@link es.caib.regweb.model.Persona} de una {@link es.caib.regweb.model.Entidad} y un {@link es.caib.regweb.model.TipoPersona}
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<Persona> getAllbyEntidadTipo(Long idEntidad, Long tipoPersona) throws Exception;

    /**
     * * Comprueba la existencia de un Documento en el sistema
     * @param documento
     * @return
     * @throws Exception
     */
    public Boolean existeDocumentoNew(String documento) throws Exception;

    /**
     * Comprueba la existencia de un Documento en el sistema para la edición de una Persona
     * @param documento
     * @param idPersona
     * @return
     * @throws Exception
     */
    public Boolean existeDocumentoEdit(String documento, Long idPersona) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb.model.Persona} según los parámetros
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer inicio,Long idEntidad, String nombre, String apellido1, String apellido2, String documento) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb.model.Persona} según los parámetros
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @return
     * @throws Exception
     */
    public List<Persona> busquedaFisicas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, Long idTipoPersona) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb.model.Persona} según los parámetros
     * @param documento
     * @return
     * @throws Exception
     */
    public List<Persona> busquedaJuridicas(Long idEntidad, String razonSocial, String documento, Long idTipoPersona) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb.model.Persona} según los parámetros
     * @param nombre
     * @param apellido1
     * @param apellido2
     * @param documento
     * @return
     * @throws Exception
     */
    public List<Persona> busquedaPersonas(Long idEntidad, String nombre, String apellido1, String apellido2, String documento, String razonSocial) throws Exception;
}
