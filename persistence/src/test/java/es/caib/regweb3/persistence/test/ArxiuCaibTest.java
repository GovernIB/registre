package es.caib.regweb3.persistence.test;


import es.caib.plugins.arxiu.api.ConsultaFiltre;
import es.caib.plugins.arxiu.api.ConsultaOperacio;
import es.caib.plugins.arxiu.api.ConsultaResultat;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.pluginsib.core.utils.XTrustProvider;
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
        XTrustProvider.install();
    }

    @Test
    public void testIfExpedientExist() {

        List<ConsultaFiltre> filtros = new ArrayList<>();

        ConsultaFiltre filtroNombre = new ConsultaFiltre();
        filtroNombre.setMetadada("name");
        filtroNombre.setValorOperacio1("SALU-E-314_2020");
        filtroNombre.setOperacio(ConsultaOperacio.CONTE);

        ConsultaFiltre filtroSerieDocumental = new ConsultaFiltre();
        filtroSerieDocumental.setMetadada("eni:cod_clasificacion");
        filtroSerieDocumental.setValorOperacio1("S0002");
        filtroSerieDocumental.setOperacio(ConsultaOperacio.IGUAL);


        //String queryDM = "(+TYPE:\"eni:expediente\" AND @eni\\:fecha_inicio:[2018-05-01T00:00:00.000Z TO "+formatDate.format(hoy)+"T23:59:59.000Z] AND @eni\\:cod_clasificacion:\""+custody.getPropertySerieDocumentalEL()+"\") ";

        //filtros.add(filtroNombre);
        filtros.add(filtroSerieDocumental);

        ConsultaResultat result = iArxiuPlugin.expedientConsulta(filtros, 0, 5);

        System.out.println("Registros: " + result.getNumRetornat());

        if(result.getResultats().size() > 0){

            System.out.println("Nombre expdiente: " + result.getResultats().get(0).getNom());
            System.out.println("Identificador expdiente: " + result.getResultats().get(0).getIdentificador());
        }else{
            System.out.println("No se ha encontrado el expediente: " + filtroNombre.getValorOperacio1());
        }


    }

    public static String getTestArxiuClass() {
        return testProperties.getProperty("es.caib.regweb3.plugin.arxiu.class");
    }

    public static String getTestSerieDocumental() {
        return testProperties.getProperty("es.caib.regweb3.plugin.arxiu.serieDocumental");
    }


}
