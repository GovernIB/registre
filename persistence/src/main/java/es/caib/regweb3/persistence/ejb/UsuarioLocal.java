package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.RolesInfo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface UsuarioLocal extends BaseEjb<Usuario, Long> {

    /**
     * Crea un nuevo usuario en REGWEB3, a partir del identificador de Seycon, obtiene sus
     * datos personales de la bbdd de Seycon.
     * @param identificador
     * @return
     * @throws Exception
     */
    Usuario crearUsuario(String identificador) throws Exception, I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.Usuario} asociado a un identificador.
     * @param identificador
     * @return
     * @throws Exception
     */
    Usuario findByIdentificador(String identificador) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.Usuario} asociado a un documento.
     * @param documento
     * @return
     * @throws Exception
     */
    Usuario findByDocumento(String documento) throws Exception;

    /**
     * Comprueba si el identificador dado existe en algún usuario excepto el selccionado.
     * @param identificador
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Boolean existeIdentificadorEdit(String identificador, Long idUsuario) throws Exception;

    /**
     * Comprueba si el documento dado existe en algún usuario excepto el selccionado.
     * @param documento
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Boolean existeDocumentioEdit(String documento, Long idUsuario) throws Exception;

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
    Paginacion busqueda(Integer pageNumber, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws Exception;

    /**
     * Asocia el idioma por defecto de la aplicación a los usurios sin idioma
     * @return
     * @throws Exception
     */
    Integer asociarIdioma() throws Exception;

    /**
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Actualiza los Roles del usuario en la bbdd de REGWEB3
     * @param usuario
     * @param rolesUsuario
     * @throws Exception
     * @throws I18NException
     */
    void actualizarRoles(Usuario usuario, List<Rol> rolesUsuario) throws Exception, I18NException;

    /**
     * Obtiene los Roles del usuario de WS autenticado mediante el plugin de Login.
     * Actualiza los Roles del usuario en la bbdd de REGWEB3
     * @param usuario
     * @throws Exception
     * @throws I18NException
     */
    void actualizarRolesWs(Usuario usuario, RolesInfo rolesInfo) throws Exception, I18NException;
}
