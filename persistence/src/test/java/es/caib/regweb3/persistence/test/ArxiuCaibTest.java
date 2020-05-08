package es.caib.regweb3.persistence.test;


import es.caib.plugins.arxiu.api.*;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.pluginsib.core.utils.XTrustProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
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
    public void create_expedient(){

        Expedient expedient = new Expedient();
        expedient.setNom("EXP_TEST");
        expedient.setIdentificador(null);

        ExpedientMetadades metadades = new ExpedientMetadades();
        metadades.setIdentificador(null);
        metadades.setDataObertura(new Date());
        metadades.setClassificacio("1234");
        metadades.setEstat(ExpedientEstat.OBERT);
        metadades.setOrgans(null);
        metadades.setInteressats(null);
        metadades.setSerieDocumental("S0002");

        expedient.setMetadades(metadades);

        ContingutArxiu expedienteCreado = iArxiuPlugin.expedientCrear(expedient);

        System.out.println("Id Expediente: " + expedienteCreado.getIdentificador());

        iArxiuPlugin.expedientEsborrar(expedienteCreado.getIdentificador());
    }

    @Test
    public void testDocumentContignut(){

        // 75567ead-1dda-4cf6-a861-d2d56bc0d886#613a186e-541c-4c99-9b29-ede70d52c6fa

        Document justificante = iArxiuPlugin.documentDetalls("613a186e-541c-4c99-9b29-ede70d52c6fa", null, true);

        System.out.println("Nombre documento: " + justificante.getNom());
        System.out.println("Nombre documento contingut: " + justificante.getContingut().getArxiuNom());
        System.out.println("mime documento: " + justificante.getContingut().getTipusMime());
        System.out.println("tamany documento: " + justificante.getContingut().getTamany());
        System.out.println("");
        System.out.println("");

        for(Firma firma:justificante.getFirmes()){
            System.out.println("Nombre firma: " + firma.getFitxerNom());
            System.out.println("mime firma: " + firma.getTipusMime());
            System.out.println("tamany firma: " + firma.getTamany());
            System.out.println("tipus firma: " + firma.getTipus().toString());
            System.out.println("");
        }
    }

    @Test
    public void testDocumentDetalle(){

        Document justificante = iArxiuPlugin.documentDetalls("3ad3ba07-ed11-48db-8745-0dea98b44c62", null, true);

        System.out.println("Nombre: " + justificante.getNom());
        System.out.println("Nombre fichero: " + justificante.getContingut().getArxiuNom());
    }

    @Test
    public void testOriginalFileUrl(){
        //ceff6b28-ae33-4e8f-a56a-97985b396630#08e2756e-9cea-4c46-a6d9-3e235a6c7cb1
        String originalFileUrl = iArxiuPlugin.getOriginalFileUrl("08e2756e-9cea-4c46-a6d9-3e235a6c7cb1");
        String printableFileUrl = iArxiuPlugin.getPrintableFileUrl("08e2756e-9cea-4c46-a6d9-3e235a6c7cb1");
        String validationFileUrl = iArxiuPlugin.getValidationFileUrl("08e2756e-9cea-4c46-a6d9-3e235a6c7cb1");
        String csvValidationWeb = iArxiuPlugin.getCsvValidationWeb("08e2756e-9cea-4c46-a6d9-3e235a6c7cb1");

        System.out.println("originalFileUrl: " + originalFileUrl);
        System.out.println("printableFileUrl: " + printableFileUrl);
        System.out.println("validationFileUrl: " + validationFileUrl);
        System.out.println("csvValidationWeb: " + csvValidationWeb);
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
