package es.caib.regweb3.persistence.utils;

import java.io.IOException;

import javax.interceptor.Interceptors;
import javax.ws.rs.core.MediaType;

import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GenericApiClient {

	private static Client client;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public <T, R> R postRequest(
			String url, 
			T body, 
			Class<R> responseType) throws IOException {
		ClientResponse response = null;
		if (body != null) {
			String jsonBody = mapper.writeValueAsString(body);
			
			response = getClient()
				.resource(url)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);
		} else {
			response = getClient()
					.resource(url)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);
		}
		String json = response.getEntity(String.class);
		return mapper.readValue(json, responseType);
	}

	private Client getClient() {

		if (client == null) {
			client = new Client();
			
			// jerseyClient.addFilter(new LoggingFilter(System.out));
			mapper = new ObjectMapper();
			// Permet rebre un sol objecte en el lloc a on hi hauria d'haver una llista.
			mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			// Mecanisme de deserialització dels enums
			mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
			// Per a no serialitzar propietats amb valors NULL
			mapper.setSerializationInclusion(Include.NON_NULL);
			// No falla si hi ha propietats que no estan definides a l'objecte destí
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return client;
	}

}