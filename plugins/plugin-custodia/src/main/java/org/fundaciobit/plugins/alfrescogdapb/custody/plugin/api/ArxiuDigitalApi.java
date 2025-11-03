package org.fundaciobit.plugins.alfrescogdapb.custody.plugin.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fundaciobit.plugins.alfrescogdapb.custody.helper.RespostaArxiuError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * 
 * API integraci� amb l'arxiu d'APB
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class ArxiuDigitalApi {
	
	private String baseUrl;
	private String baseUrlPublica;
	private String user;
	private String password;
	private String ldapUser;

	Client client = Client.create();
	
	public ArxiuDigitalApi(
			String baseUrl,
			String baseUrlPublica,
			String user,
			String password,
			String ldapUser) {
		super();
		this.baseUrl = baseUrl;
		this.baseUrlPublica = baseUrlPublica;
		this.user = user;
		this.password = password;
		this.ldapUser = ldapUser;
	}
	
	/**
	 * inicia sessi�
	 * 
	 * @return URL ticket
	 */
	public String login() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String ticket = null;
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		String uri = baseUrl + "api/login?u=" + user + "&pw=" + password;
		try {
			String res = get(uri, parameters).getEntity(String.class);
			Pattern p = Pattern.compile("TICKET_\\w*");
			Matcher m = p.matcher(res);
			if (m.find()) {
				ticket = m.group();
			}
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha pogut recuperar el ticket",
					ex);
		}
		return ticket;
	}

	/* ======================================================
	 * ================== METODES DOCUMENT ==================
	 * ======================================================
	 */

	/**
	 * Crea un document a Alfresco
	 * 
	 * @param body cos de la petici�
	 * @param parameters par�metres de la url
	 * 
	 * @return resposta arxiu
	 */
	public Map<String, Object> insertDocument(
			Map<String, Object> body,
			Map<String, Object> parameters) {
		String ticket = this.login();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		if (ldapUser == null || ldapUser.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat ldapUser");
		
		String uri = baseUrl + "gdapb/insertDocument/" + ldapUser + "/";
		try {
			ClientResponse response = post(
					uri, 
					body, 
					parameters);
			return new ObjectMapper().readValue(
					response.getEntity(String.class),
					new TypeReference<Map<String, Object>>() {});
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha pogut crear el document",
					ex);
		}
	}


	/**
	 * Actualitza el document a Alfresco
	 * 
	 * @param custodyID el uuid del docuemnt a actualitzar
	 * @param body cos de la petici�
	 * @param parameters par�metres de la url
	 * 
	 * @return resposta arxiu
	 */
	public Map<String, Object> updateDocument(
			String custodyID,
			Map<String, Object> body,
			Map<String, Object> parameters) {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		if (ldapUser == null || ldapUser.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat ldapUser");
		
		String uri = baseUrl + "gdapb/updateDocument/" + ldapUser + "/" + custodyID;
		try {
			ClientResponse response = post(
					uri, 
					body, 
					parameters);
			return new ObjectMapper().readValue(
					response.getEntity(String.class),
					new TypeReference<Map<String, Object>>() {});
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha pogut actualitzar el document [uuid=" + custodyID +"]",
					ex);
		}
	}
	
	public Map<String, Object> updateDocumentPublic(
			String custodyID,
			Map<String, Object> body,
			Map<String, Object> parameters) {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		parameters.put("alf_ticket", ticket);
		
		if (baseUrlPublica == null || baseUrlPublica.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat api.publica.baseurl");
		if (ldapUser == null || ldapUser.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat ldapUser");
		
		String uri = baseUrlPublica + "nodes/" + custodyID;
		
		try {
			ClientResponse response = put(
					uri, 
					body, 
					parameters);
			return new ObjectMapper().readValue(
					response.getEntity(String.class),
					new TypeReference<Map<String, Object>>() {});
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha pogut actualitzar el document [uuid=" + custodyID +"]",
					ex);
		}		
	}
	
	/**
	 * Recupera el codi font d'un document
	 * 
	 * @param custodyID el uuid del documenta a recuperar
	 * 
	 * @return el document en un array de bytes
	 */
	public byte[] getDocument(String custodyID) {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		
		String uri = baseUrl + "gdapb/getDocument?doc_id=" + custodyID;
		try {
			ClientResponse response = get(uri, parameters);
			return response.getEntity(byte[].class);
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha recuperar el document [uuid=" + custodyID +"]",
					ex);
		}
	}

	/**
	 * Recupera les metadades d'un document
	 * 
	 * @param custodyID
	 * @return
	 */
	public Map<String, Object> getMetadataDocument(String custodyID) {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		if (ldapUser == null || ldapUser.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat ldapUser");

		String uri = baseUrl + "gdapb/getMetadataDocument/" + ldapUser + "/" + custodyID;
		try {
			ClientResponse response = get(uri, parameters);
			return new ObjectMapper().readValue(
					response.getEntity(String.class),
					new TypeReference<Map<String, Object>>() {});
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'han pogut recuperar les metadades del document [uuid=" + custodyID +"]",
					ex);
		}
	}
	
	/**
	 * Cerca un document a partir d'una metadata
	 * 
	 * @param body el cos de la petici�
	 * 
	 * @return una llista de les metadades del document
	 * @throws Exception
	 */
	public List<Map<String, Object>> searchDocument(Map<String, Object> body) throws Exception {
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		if (ldapUser == null || ldapUser.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat ldapUser");
		
		String uri = baseUrl + "gdapb/searchDocument/" + ldapUser;
		try {
			ClientResponse response = post(
					uri, 
					body, 
					parameters);
			return new ObjectMapper().readValue(
					response.getEntity(String.class),
					new TypeReference<List<Map<String, Object>>>() {});
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'han pogut recuperar les metadades del document [" + body.toString() + "]",
					ex);
		}
	}
	
	/**
	 * Esborra un document
	 * 
	 * @param custodyID el uuid del document a esborrar
	 * @return
	 */
	public String deleteDocument(String custodyID) {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		
		String uri = baseUrl + "gdapb/deleteDocument/a/" + custodyID;
		try {
			ClientResponse response = delete(uri, parameters);
			return response.getEntity(String.class);
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha pogut esborrar document [uuid=" + custodyID +"]",
					ex);
		}
	}


	public void makeFinal(String custodyID){
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		if (baseUrl == null || baseUrl.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat baseUrl");
		if (ldapUser == null || ldapUser.isEmpty())
			throw new RuntimeException("No s'ha definit la propietat ldapUser");

		String uri = baseUrl + "gdapb/makeFinal/" + ldapUser + "/" + custodyID;
		try {
			get(uri,parameters);
		} catch (Exception ex) {
			throw new RuntimeException(
					"No s'ha pogut convertir el document en final",
					ex);
		}
	}

	
	/* =========================================================
	 * =================== METODES EXPEDIENT ===================
	 * =========================================================
	 */

	/**
	 * Borrar un expedient
	 * @param custodyID
	 * @return
	 */
	public String deleteRecord(String custodyID) {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		
		String uri = baseUrl + "gdapb/deleteRecord?doc_id=" + custodyID;
		
		ClientResponse response = get(uri, parameters);
		return response.getEntity(String.class);
	}

	/* ===========================================================
	 * ======================= METODES REST ======================
	 * ===========================================================
	 */
	
	/**
	 * Fa una crida GET a la direcci� indicada
	 * @param url
	 * @return
	 */
	private ClientResponse get(String uri, Map<String, Object> parameters) {

		WebResource webResource = client.resource(uri);
		
		for (Map.Entry<String, Object> param : parameters.entrySet()) {
			webResource = webResource.queryParam(param.getKey(), String.valueOf(param.getValue()));
		}
		
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		
		if (!success(response.getStatus())) {
			throwArxiuException(response);
		}
		
		return response;
	}

	/**
	 * Fa una crida POST a la direcci� inidicada enviant el body i els parametres.
	 * @param url
	 * @param body
	 * @param parameters
	 * @return
	 */
	private ClientResponse post(String uri, Map<String, Object> body, Map<String, Object> parameters) {

		WebResource  webResource = client.resource(uri);
		String jbody;
		ClientResponse response;

		for (Map.Entry<String, Object> param : parameters.entrySet()) {
			webResource = webResource.queryParam(param.getKey(), String.valueOf(param.getValue()));
		}

		try {
			jbody = new ObjectMapper().writeValueAsString(body);
		    response = webResource.type("application/json").post(ClientResponse.class, jbody);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("[ARXIU] Error processant la resposta.", e);
		}

		if (!success(response.getStatus())) {
			throwArxiuException(response);
		}

		return response;
	}
	
	/**
	 * Fa una crida POST a la direcci� inidicada enviant el body i els parametres.
	 * @param url
	 * @param body
	 * @param parameters
	 * @return
	 */
	private ClientResponse put(String uri, Map<String, Object> body, Map<String, Object> parameters) {

		WebResource  webResource = client.resource(uri);
		String jbody;
		ClientResponse response;

		for (Map.Entry<String, Object> param : parameters.entrySet()) {
			webResource = webResource.queryParam(param.getKey(), String.valueOf(param.getValue()));
		}

		try {
			jbody = new ObjectMapper().writeValueAsString(body);
		    response = webResource.type("application/json").put(ClientResponse.class, jbody);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("[ARXIU] Error processant la resposta.", e);
		}

		if (!success(response.getStatus())) {
			throwArxiuException(response);
		}

		return response;
	}
	
	/**
	 * Fa una crida DELETE a la direcci� indicada
	 * @param url
	 * @return
	 */
	private ClientResponse delete(String uri, Map<String, Object> parameters) {

		WebResource webResource = client.resource(uri);
		
		for (Map.Entry<String, Object> param : parameters.entrySet()) {
			webResource = webResource.queryParam(param.getKey(), String.valueOf(param.getValue()));
		}
		
		ClientResponse response = webResource.delete(ClientResponse.class);
		
		if (!success(response.getStatus())) {
			throwArxiuException(response);
		}
		
		return response;

	}

	private void throwArxiuException(ClientResponse response) throws RuntimeException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			RespostaArxiuError error = mapper.readValue(response.getEntity(String.class), RespostaArxiuError.class);
			throw new RuntimeException("[ARXIU] La resposta de l'arxiu no és correcta. [" + 
					"Status code: " + error.getStatus().getCode() + ", " + 
					"Message: " + error.getMessage() + "]");
		} catch (IOException e) {
			throw new RuntimeException("[ARXIU] La resposta de l'arxiu no és correcta. [" + 
					"Status code: " + response.getStatus() + "]");
		} 
	}

	/* ==================================
	 * ========= Helper methods =========
	 * ==================================
	 */
	
	/**
	 * Comprova l'estat de la resposta
	 * @param res
	 * @return
	 */
	private boolean success(int res) {
		return (res >= 200 && res < 300);
	}

}
