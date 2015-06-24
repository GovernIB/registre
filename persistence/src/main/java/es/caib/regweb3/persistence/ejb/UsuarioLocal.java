package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface UsuarioLocal extends BaseEjb<Usuario, Long> {

    /**
     * Retorna el {@link es.caib.regweb3.model.Usuario} asociado a un identificador.
     * @param identificador
     * @return
     * @throws Exception
     */
    public Usuario findByIdentificador(String identificador) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.Usuario} asociado a un documento.
     * @param documento
     * @return
     * @throws Exception
     */
    public Usuario findByDocumento(String documento) throws Exception;

    /**
     * Comprueba si el identificador dado existe en algún usuario excepto el selccionado.
     * @param identificador
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public Boolean existeIdentificadorEdit(String identificador, Long idUsuario) throws Exception;

    /**
     * Comprueba si el documento dado existe en algún usuario excepto el selccionado.
     * @param documento
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public Boolean existeDocumentioEdit(String documento, Long idUsuario) throws Exception;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Usuario} según los parámetros
     * @param pageNumber
     * @param nombre
     * @param identificador
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipoUsuario
     * @return
     * @throws Exception
     */
    public Paginacion busqueda(Integer pageNumber,String identificador,String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws Exception;
}
