package es.caib.regweb3.plugins.postproceso;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.v3.IPluginIB;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 *
 */
public interface IPostProcesoPlugin extends IPluginIB {

    public static final String POSTPROCESO_BASE_PROPERTY = IPLUGINSIB_BASE_PROPERTIES + "postproceso.";


    /**
     * crear un registro nuevo
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    void nuevoRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException;

    /**
     * crear un registro nuevo
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    void nuevoRegistroSalida(RegistroSalida registroSalida) throws I18NException;


    /**
     * actualizar un registro
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    void actualizarRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException;

    /**
     * actualizar un registro
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    void actualizarRegistroSalida(RegistroSalida registroSalida) throws I18NException;



    /**
     * nuevo interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws I18NException
     */
    void nuevoInteresadoEntrada(Interesado interesado, String numeroEntrada) throws I18NException;



    /**
     * nuevo interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws I18NException
     */
    void nuevoInteresadoSalida(Interesado interesado, String numeroSalida) throws I18NException;

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws I18NException
     */
    void actualizarInteresadoEntrada(Interesado interesado, String numeroEntrada) throws I18NException;

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws I18NException
     */
    void actualizarInteresadoSalida(Interesado interesado, String numeroSalida) throws I18NException;

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroEntrada
     * @return
     * @throws I18NException
     */
    void eliminarInteresadoEntrada(Long idInteresado, String numeroEntrada) throws I18NException;

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroSalida
     * @return
     * @throws I18NException
     */
    void eliminarInteresadoSalida(Long idInteresado, String numeroSalida) throws I18NException;


}
