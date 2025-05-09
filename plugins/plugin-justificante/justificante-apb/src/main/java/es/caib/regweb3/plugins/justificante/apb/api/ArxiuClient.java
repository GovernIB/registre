package es.caib.regweb3.plugins.justificante.apb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ArxiuClient {

	private static Client client = Client.create();
	

	/**
	 * Fa una crida GET a la direcció indicada
	 * 
	 * @param url
	 * 
	 * @return	ClientResponse amb el resultat de la petició
	 */
	protected static ClientResponse get(
			String uri, 
			Map<String, Object> parameters) {

		WebResource webResource = client.resource(uri);
		ClientResponse response = null;
		
		if(parameters != null)
			for (Map.Entry<String, Object> param : parameters.entrySet()) {
				webResource = webResource.queryParam(param.getKey(), String.valueOf(param.getValue()));
			}
		
		response = webResource.accept("application/json").get(ClientResponse.class);
		
		if (!success(response.getStatus())) {
			logger.error(response.getLocation().toString());
			logger.error("La resposta de l'arxiu no es correcta. Status code: "
										+ response.getStatus() + " " 
										+ response.getStatusInfo().getReasonPhrase());
			logger.error("La resposta de l'arxiu no es correcta. Status code: " + response);
		}
		return response;
	}

	/**
	 * Fa una crida POST a la direcció inidicada enviant el body i els parametres.
	 * 
	 * @param url
	 * @param body	(cos de la petició)
	 * @param parameters	(Parámetres de la url)
	 * 
	 * @return ClientResponse amb el resultat de la petició
	 * @throws JsonProcessingException 
	 */
	protected static ClientResponse post(
			String uri, 
			Map<String, Object> body, 
			Map<String, Object> parameters) throws JsonProcessingException {
		
		WebResource  webResource = client.resource(uri);
		String jbody;
		ClientResponse response;
		if(parameters != null)
			for (Map.Entry<String, Object> param : parameters.entrySet()) {
				webResource = webResource.queryParam(param.getKey(), String.valueOf(param.getValue()));
			}

		jbody = new ObjectMapper().writeValueAsString(body);
		response = webResource.type("application/json").post(ClientResponse.class, jbody);

		if (!success(response.getStatus())) {
			logger.error(response.getLocation().toString());
			logger.error("La resposta de l'arxiu no es correcta. Status code: "
											+ response.getStatus() + " " 
											+ response.getStatusInfo().getReasonPhrase());
			logger.error("La resposta de l'arxiu no es correcta. Status code: " + response);
		}

		return response;
	}
	
	/**
	 * Comprova l'estat de la resposta
	 * 
	 * @param res
	 * 
	 * @return true o false
	 */
	protected static boolean success(int res) {
		return (res >= 200 && res < 300);
	}
	
	private static final Logger logger = LoggerFactory.getLogger(ArxiuClient.class);
}
