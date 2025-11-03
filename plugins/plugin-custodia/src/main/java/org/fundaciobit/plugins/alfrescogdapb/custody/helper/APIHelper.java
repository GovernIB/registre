package org.fundaciobit.plugins.alfrescogdapb.custody.helper;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

public class APIHelper {

	private static Client jerseyProxyClient;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static <T, R> R proxyValidacioPost(
			String url, 
			T body, 
			Class<R> reference) throws IOException {
		ClientResponse response = null;
		if (body != null) {
			String jsonBody = mapper.writeValueAsString(body);
			
			response = getJerseyRolsacClient()
				.resource(url)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);
		} else {
			response = getJerseyRolsacClient()
					.resource(url)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);
		}
		String json = response.getEntity(String.class);
		return mapper.readValue(json, reference);
	}

	private static Client getJerseyRolsacClient() {

		if (jerseyProxyClient == null) {
			jerseyProxyClient = new Client();
//			if (rolsacServiceUsername != null) {
//				jerseyProxyClient.addFilter(new HTTPBasicAuthFilter(rolsacServiceUsername, rolsacServicePassword));
//			}
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
		return jerseyProxyClient;
	}
}
