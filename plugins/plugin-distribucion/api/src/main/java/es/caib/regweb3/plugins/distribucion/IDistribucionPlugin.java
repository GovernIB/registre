package es.caib.regweb3.plugins.distribucion;

import es.caib.regweb3.model.RegistroEntrada;
import org.fundaciobit.pluginsib.core.IPlugin;

import java.util.Locale;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 27/10/2015
 */
public interface IDistribucionPlugin extends IPlugin {

    public static final String DISTRIBUCION_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "distribucion.";


    /**
     * Método que envia/distribuye el registro de entrada a la lista de destinatarios  indicada
     * @param registro registro de entrada que se distribuye
     * @return
     * @throws Exception
     */
    Boolean distribuir(RegistroEntrada registro, Locale locale) throws Exception;

    /**
     * Método que indica si se envia el registro a la cola de distribución o no
     * @return
     * @throws Exception
     */
    Boolean getEnvioCola() throws Exception;

}
