package es.caib.regweb3.persistence.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ClientUtils {

    protected static final Logger log = Logger.getLogger(ClientUtils.class);

    /**
     *
     * @param endPointBase
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public static ClientResponse commonCall(String endPointBase, String username, String password) throws Exception {

        ClientResponse response;

        try {

            log.info("endPoint: " + endPointBase);

            // Inicialitza un client per fer una connexió http al servidor
            final Client client;

            if (endPointBase.toLowerCase().startsWith("https")) {  //Entra si tenim una connexió a servidor segura

                HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
                ClientConfig config = new DefaultClientConfig();
                SSLContext ctx = SSLContext.getInstance("SSL");
                ctx.init(null, new TrustManager[]{new InsecureTrustManager()}, null);
                config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));

                client = Client.create(config);

            } else {   //Entra si NO tenim una connexió segura al servidor

                client = Client.create();
            }

            // Si tenim username
            if (username != null) {
                client.addFilter(new HTTPBasicAuthFilter(username, password));
            }

            WebResource webResource = client.resource(endPointBase);

            response = webResource.type("application/json").get(ClientResponse.class);

        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }

        // Status de HTTP OK
        if (response.getStatus() == 200) {

            return response;

        } else {   // Error de Comunicació o no controlat

            String raw_msg = response.getEntity(String.class);
            throw new Exception("Error desconegut (Codi de servidor " + response.getStatus() + "): " + raw_msg);

        }

    }

    private static class InsecureTrustManager implements X509TrustManager {
        /**
         * {@inheritDoc}
         */
        @Override
        public void checkClientTrusted(final java.security.cert.X509Certificate[] chain, final String authType) throws CertificateException {
            // Everyone is trusted!
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void checkServerTrusted(final java.security.cert.X509Certificate[] chain, final String authType) throws CertificateException {
            // Everyone is trusted!
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
