package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.RolesInfo;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface UsuarioLocal extends BaseEjb<Usuario, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/UsuarioEJB";


    /**
     * Crea un nuevo usuario en REGWEB3, a partir del identificador de Seycon, obtiene sus
     * datos personales de la bbdd de Seycon.
     *
     * @param identificador
     * @return
     * @throws I18NException
     */
    Usuario crearUsuario(String identificador) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.Usuario} asociado a un identificador.
     *
     * @param identificador
     * @return
     * @throws I18NException
     */
    Usuario findByIdentificador(String identificador) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.Usuario} asociado a un documento.
     *
     * @param documento
     * @return
     * @throws I18NException
     */
    Usuario findByDocumento(String documento) throws I18NException;

    /**
     * Comprueba si el identificador dado existe en algún usuario excepto el selccionado.
     *
     * @param identificador
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    Boolean existeIdentificadorEdit(String identificador, Long idUsuario) throws I18NException;

    /**
     * Comprueba si el documento dado existe en algún usuario
     *
     * @param documento
     * @return
     * @throws I18NException
     */
    Boolean existeDocumentoNew(String documento) throws I18NException;

    /**
     * Comprueba si el documento dado existe en algún usuario excepto el selccionado.
     *
     * @param documento
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    Boolean existeDocumentoEdit(String documento, Long idUsuario) throws I18NException;

    /**
     * Realiza una busqueda de {@link es.caib.regweb3.model.Usuario} según los parámetros
     *
     * @param pageNumber
     * @param nombre
     * @param identificador
     * @param apellido1
     * @param apellido2
     * @param documento
     * @param tipoUsuario
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipoUsuario) throws I18NException;

    /**
     * Asocia el idioma por defecto de la aplicación a los usurios sin idioma
     *
     * @return
     * @throws I18NException
     */
    Integer asociarIdioma() throws I18NException;

    /**
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Actualiza los Roles del usuario en la bbdd de REGWEB3
     *
     * @param usuario
     * @param rolesUsuario
     * @throws I18NException
     * @throws I18NException
     */
    void actualizarRoles(Usuario usuario, List<Rol> rolesUsuario) throws I18NException;

    /**
     * Obtiene los Roles del usuario de WS autenticado mediante el plugin de Login.
     * Actualiza los Roles del usuario en la bbdd de REGWEB3
     *
     * @param usuario
     * @throws I18NException
     * @throws I18NException
     */
    void actualizarRolesWs(Usuario usuario, RolesInfo rolesInfo) throws I18NException;
}
