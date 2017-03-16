package org.fundaciobit.plugins.postproceso;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import org.fundaciobit.plugins.IPlugin;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 *
 */
public interface IPostProcesoPlugin extends IPlugin {

    public static final String POSTPROCESO_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "postproceso.";


    /**
     * crear un registro nuevo
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public void nuevoRegistroEntrada(RegistroEntrada registroEntrada) throws Exception;

    /**
     * crear un registro nuevo
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public void nuevoRegistroSalida(RegistroSalida registroSalida) throws Exception;


    /**
     * actualizar un registro
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    public void actualizarRegistroEntrada(RegistroEntrada registroEntrada) throws Exception;

    /**
     * actualizar un registro
     * @param registroSalida
     * @return
     * @throws Exception
     */
    public void actualizarRegistroSalida(RegistroSalida registroSalida) throws Exception;



    /**
     * nuevo interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws Exception
     */
    public void nuevoInteresadoEntrada(Interesado interesado, String numeroEntrada) throws Exception;



    /**
     * nuevo interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws Exception
     */
    public void nuevoInteresadoSalida(Interesado interesado, String numeroSalida) throws Exception;

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws Exception
     */
    public void actualizarInteresadoEntrada(Interesado interesado, String numeroEntrada) throws Exception;

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws Exception
     */
    public void actualizarInteresadoSalida(Interesado interesado, String numeroSalida) throws Exception;

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroEntrada
     * @return
     * @throws Exception
     */
    public void eliminarInteresadoEntrada(Long idInteresado, String numeroEntrada) throws Exception;

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroSalida
     * @return
     * @throws Exception
     */
    public void eliminarInteresadoSalida(Long idInteresado, String numeroSalida) throws Exception;


}
