package es.caib.regweb3.plugins.arxiu.caib.test;


import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaOperacio;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author earrivi
 */
public class ArxiuCaibTest {

    protected static IArxiuPlugin iArxiuPlugin;
    private static Properties testProperties = new Properties();

    /**
     *
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        testProperties.load(new FileInputStream("test.properties"));
        iArxiuPlugin =  (IArxiuPlugin) org.fundaciobit.pluginsib.core.utils.PluginsManager.instancePluginByClassName(getTestArxiuClass(), RegwebConstantes.REGWEB3_PROPERTY_BASE, testProperties);

    }

    @Test
    public void testifExpedientExist() {

        List<ConsultaFiltre> filtros = new ArrayList<>();

        ConsultaFiltre filtroNombre = new ConsultaFiltre();
        filtroNombre.setMetadada("");
        filtroNombre.setValorOperacio1("");
        filtroNombre.setOperacio(ConsultaOperacio.IGUAL);

        filtros.add(filtroNombre);

        ConsultaResultat result = iArxiuPlugin.expedientConsulta(filtros, null, null);

        if(result.getResultats().size() > 0){

            System.out.println("Nombre expdiente: " + result.getResultats().get(0).getNom());
            System.out.println("Identificador expdiente: " + result.getResultats().get(0).getIdentificador());
        }


    }

    public static String getTestArxiuClass() {
        return testProperties.getProperty("es.caib.regweb3.plugin.arxiu.class");
    }

    public static String getTestSerieDocumental() {
        return testProperties.getProperty("es.caib.regweb3.plugin.arxiu.serieDocumental");
    }


}
