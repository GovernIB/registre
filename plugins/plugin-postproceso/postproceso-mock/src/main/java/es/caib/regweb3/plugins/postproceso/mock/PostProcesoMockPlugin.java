package es.caib.regweb3.plugins.postproceso.mock;


import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author mgonzalez
 */
public class PostProcesoMockPlugin extends AbstractPluginProperties implements IPostProcesoPlugin {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    /**
     *
     */
    public PostProcesoMockPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public PostProcesoMockPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public PostProcesoMockPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }


    /**
     * crear un registro nuevo
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    @Override
    public void nuevoRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException {
        log.debug("Nuevo registro entrada: " +registroEntrada.getNumeroRegistroFormateado());

    }

    /**
     * crear un registro nuevo
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    @Override
    public void nuevoRegistroSalida(RegistroSalida registroSalida) throws I18NException{
        log.debug("Nuevo registro salida: " +registroSalida.getNumeroRegistroFormateado());


    }


    /**
     * actualizar un registro
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    @Override
    public void actualizarRegistroEntrada(RegistroEntrada registroEntrada) throws I18NException{
        log.debug("actualizar registro entrada:" +registroEntrada.getNumeroRegistroFormateado());


    }

    /**
     * actualizar un registro
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    @Override
    public void actualizarRegistroSalida(RegistroSalida registroSalida) throws I18NException{
        log.debug("Actualizar registro salida: " +registroSalida.getNumeroRegistroFormateado());


    }



    /**
     * nuevo interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws I18NException
     */
    @Override
    public void nuevoInteresadoEntrada(Interesado interesado, String numeroEntrada) throws I18NException{
        log.debug("Nuevo interesado entrada: " + interesado.getNombreCompleto());

    }


    /**
     * nuevo interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws I18NException
     */
    @Override
    public void nuevoInteresadoSalida(Interesado interesado, String numeroSalida) throws I18NException{
        log.debug("Nuevo interesado salida: " + interesado.getNombreCompleto());

    }

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroEntrada
     * @return
     * @throws I18NException
     */
    @Override
    public void actualizarInteresadoEntrada(Interesado interesado, String numeroEntrada) throws I18NException{
        log.debug("Actualizar interesado entrada:" + interesado.getNombreCompleto());

    }

    /**
     * actualizar interesado
     * @param interesado
     * @param numeroSalida
     * @return
     * @throws I18NException
     */
    @Override
    public void actualizarInteresadoSalida(Interesado interesado, String numeroSalida) throws I18NException{
        log.debug("actualizar interesado salida: " + interesado.getNombreCompleto());

    }

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroEntrada
     * @return
     * @throws I18NException
     */
    @Override
    public void eliminarInteresadoEntrada(Long idInteresado, String numeroEntrada) throws I18NException{
        log.debug("Eliminar interesado entrada: " + idInteresado);


    }

    /**
     * eliminar interesado
     * @param idInteresado
     * @param numeroSalida
     * @return
     * @throws I18NException
     */
    @Override
    public void eliminarInteresadoSalida(Long idInteresado, String numeroSalida) throws I18NException{
        log.debug("Eliminar interesado salida: " + idInteresado);

    }



}