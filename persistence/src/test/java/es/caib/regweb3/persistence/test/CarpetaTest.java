package es.caib.regweb3.persistence.test;

import es.caib.carpeta.apiinterna.client.api.NotificacionsApi;
import es.caib.carpeta.apiinterna.client.model.SendMessageResult;
import es.caib.carpeta.apiinterna.client.services.ApiClient;
import es.caib.carpeta.apiinterna.client.services.ApiException;
import es.caib.carpeta.apiinterna.client.services.Configuration;
import es.caib.carpeta.apiinterna.client.services.auth.HttpBasicAuth;
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
    public void sendMessage() throws Exception{

        NotificacionsApi apiInstance = getApiInstance();

        String nif = "43146650F";
        String notificationCode = "REGISTREENTRADA"; // String | Codi de la notificació. Demanar a l'administrador de Carpeta.
        List<String> notificationParameters = Arrays.asList("GOIBE001/2023"); // List<String> | Paràmetres associats al Codi de la notificació
        String notificationLang = "ca"; // String | Idioma en que s'enviaran les notificacions
        String langError = "ca"; // String | Idioma en que s'enviaran els missatges d'error
        try {
            SendMessageResult result = apiInstance.sendNotificationToMobile(nif, notificationCode, notificationParameters, notificationLang, langError);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling NotificacionsApi#help");
            e.printStackTrace();
        }
    }

    private NotificacionsApi getApiInstance() throws Exception{

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setBasePath("https://dev.caib.es/carpetaapi/interna");
        // Configure HTTP basic authorization: BasicAuth
        HttpBasicAuth BasicAuth = (HttpBasicAuth) defaultClient.getAuthentication("BasicAuth");
        BasicAuth.setUsername("$regweb_carpeta");
        BasicAuth.setPassword("regweb_carpeta");

        return new NotificacionsApi();

    }
}
