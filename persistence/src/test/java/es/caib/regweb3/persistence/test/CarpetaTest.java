package es.caib.regweb3.persistence.test;

import es.caib.carpeta.apiinterna.client.api.NotificacionsApi;
import es.caib.carpeta.apiinterna.client.model.SendMessageResult;
import es.caib.carpeta.apiinterna.client.services.ApiClient;
import es.caib.carpeta.apiinterna.client.services.ApiException;
import es.caib.carpeta.apiinterna.client.services.Configuration;
import es.caib.carpeta.apiinterna.client.services.auth.HttpBasicAuth;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.pluginsib.core.utils.XTrustProvider;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CarpetaTest {

    @Test
    public void help() throws Exception{

        NotificacionsApi apiInstance = getApiInstance();

        String notificationCode = "REGISTREENTRADA"; // String | Codi de la notificació. Demanar a l'administrador de Carpeta.
        String langError = "ca"; // String | Idioma en que s'enviaran els missatges d'error
        try {
            String result = apiInstance.help(notificationCode, langError);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling NotificacionsApi#help");
            e.printStackTrace();
        }
    }

    @Test
    public void existCitizen() throws Exception{

        Boolean existe = getApiInstance().existCitizen("43201388M", RegwebConstantes.IDIOMA_CATALAN_CODIGO);

        System.out.println("Existe: " + existe);
    }

    @Test
    public void sendMessage() throws Exception{

        NotificacionsApi apiInstance = getApiInstance();

        String nif = "43201388M";
        String notificationCode = "REGISTREENTRADA"; // String | Codi de la notificació. Demanar a l'administrador de Carpeta.
        List<String> notificationParameters = Arrays.asList("GOIBE002/2023"); // List<String> | Paràmetres associats al Codi de la notificació
        String notificationLang = RegwebConstantes.IDIOMA_CATALAN_CODIGO; // String | Idioma en que s'enviaran les notificacions
        String langError = RegwebConstantes.IDIOMA_CATALAN_CODIGO; // String | Idioma en que s'enviaran els missatges d'error
        try {
            SendMessageResult result = apiInstance.sendNotificationToMobile(nif, notificationCode, notificationParameters, notificationLang, langError);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling NotificacionsApi#help");
            e.printStackTrace();
        }
    }

    private NotificacionsApi getApiInstance() throws Exception{
        XTrustProvider.install();
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://se.caib.es/carpetaapi/interna");
        // Configure HTTP basic authorization: BasicAuth
        HttpBasicAuth BasicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
        BasicAuth.setUsername("$regweb_carpeta");
        BasicAuth.setPassword("regweb_carpeta");

        return new NotificacionsApi();

    }
}
