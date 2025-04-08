package es.caib.regweb3.integraciones.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GenericRestClient {


    private final RestTemplate restTemplate;

    public GenericRestClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Función genérica para realizar peticiones POST
     * @param url        URL completa a la que se enviará la petición
     * @param request    Cuerpo de la petición (JSON u objeto serializable)
     * @param responseType Clase de la respuesta esperada
     * @param <T>        Tipo de respuesta esperada
     * @return          Respuesta del servidor en el tipo esperado
     */
    public <T> T postRequest(String url, Object request, Class<T> responseType) {
        return restTemplate.postForObject(url, request, responseType);
    }
}
