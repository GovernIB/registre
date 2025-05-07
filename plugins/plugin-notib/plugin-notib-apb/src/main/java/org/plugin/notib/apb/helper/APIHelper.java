package org.plugin.notib.apb.helper;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.RespostaAlta;

public class APIHelper {

	private static Client jerseyProxyClient;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private String url, username, password;
	
	public APIHelper() {}
	
	public APIHelper(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public <T, R> R notibPost(
			T body, 
			Class<R> reference) throws IOException {
		ClientResponse response = null;
		if (body != null) {
			String jsonBody = mapper.writeValueAsString(body);
			
			response = getJerseyNotibClient()
				.resource(url)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonBody);
		} else {
			response = getJerseyNotibClient()
					.resource(url)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);
		}
		String json = response.getEntity(String.class);
		return mapper.readValue(json, reference);
	}
	
	public <T, R> R notibGet(
			String parameter, 
			Class<R> reference) throws IOException {
		ClientResponse response = null;
		if (parameter != null) {			
			response = getJerseyNotibClient()
				.resource(url + "/" + parameter)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.get(ClientResponse.class);
		} else {
			response = getJerseyNotibClient()
					.resource(url)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);
		}
		String json = response.getEntity(String.class);
		return mapper.readValue(json, reference);
	}

	private Client getJerseyNotibClient() {

		if (jerseyProxyClient == null) {
			jerseyProxyClient = new Client();
			
			jerseyProxyClient.addFilter(new HTTPBasicAuthFilter(username, password));
			
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
		} else {
			jerseyProxyClient.addFilter(new HTTPBasicAuthFilter(username, password));
		}
		return jerseyProxyClient;
	}

	public static RespostaAlta proxyLemaPost(String altaUrl, String username, String password, NotificacioV2 request,
			Class<RespostaAlta> class1) {
		// TODO Auto-generated method stub
		return null;
	}
}
