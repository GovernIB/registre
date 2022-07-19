package es.caib.regweb3.plugins.distribucion.mock;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Properties;

/**
 * Plugin de distribución por defecto
 * @author mgonzalez
 */
public class DistribucionMockPlugin extends AbstractPluginProperties implements IDistribucionPlugin {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String basePluginMock = DISTRIBUCION_BASE_PROPERTY + "distribucionmock.";

    /**
     *
     */
    public DistribucionMockPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public DistribucionMockPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public DistribucionMockPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    @Override
    public Boolean distribuir(RegistroEntrada registro, Locale lang) throws Exception {

        return true;

    }

    @Override
    public Boolean getEnvioCola() throws Exception {
        return false;
    }
}