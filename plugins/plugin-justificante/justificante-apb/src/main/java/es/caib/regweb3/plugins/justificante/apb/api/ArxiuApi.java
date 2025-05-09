package es.caib.regweb3.plugins.justificante.apb.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Api per establir la connexió amb l'api de ricoh
 * 
 * @author Limit Tecnologies <limit@limit.es>
 *
 */
public class ArxiuApi {
	
	private String baseUrl;
	private String user;
	private String password;
	private String ldapUser;

	Client client = Client.create();
	
	public ArxiuApi(
			String baseUrl, 
			String user, 
			String password, 
			String ldapUser) {	
		super();
		this.baseUrl = baseUrl;
		this.user = user;
		this.password = password;
		this.ldapUser = ldapUser;
		
	}
	
	 /**
	  * Login api (Assigna un valor al ticket per identificar usuari)
	  */
	 public String login(){
		 
	 Map<String, Object> parameters = new HashMap<String, Object>();
	 String ticket = null;
	 
	 String uri = baseUrl + "api/login?u="+user+"&pw="+password;
	 
		 try {
			 String res = ArxiuClient.get(uri, parameters).getEntity(String.class);
			 
			 // Cuan la request retorni el tiquet el treim amb una regex.
			 Pattern p = Pattern.compile("TICKET_\\w*");
			 Matcher m = p.matcher(res);
			
			 if(m.find()){
				 ticket = m.group();
			 } 
		 }catch (Exception ex) {
			 logger.error("No s'ha pogut recuperar el ticket", ex);
		 }
		 return ticket;
	 }

	/**
	 * Cerca un document a partir d'una metadata
	 * 
	 * @param body	(Contingut de la petició)
	 * @param parameters	(ID expedient on crear document)
	 * 
	 * @return Map amb l'id i uid del document trobat
	 * @throws IOException 
	 * @throws UniformInterfaceException 
	 * @throws ClientHandlerException 
	 */
	public List<Map<String, Object>> searchDocument(
			Map<String, Object> body) throws ClientHandlerException, UniformInterfaceException, IOException {
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		String uri = baseUrl + "gdapb/searchDocument/" + ldapUser;
		ClientResponse response = ArxiuClient.post(uri, body, parameters);
		// Transforma la response en un objecte Map i el retorna
		return new ObjectMapper().readValue(
				response.getEntity(String.class),
				new TypeReference<List<Map<String, Object>>>() {});
	}
	
	/**
	 * Retorna una llista amb les metadades d'un document
	 * 
	 * @param custodyID	(uuid del document del que recuperar les metadades)
	 * 
	 * @return	Llista amb les metadades del docuemnt
	 */
	public Map<String, Object> getMetadataDocument(String custodyID) {
		String ticket = this.login();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("alf_ticket", ticket);
		Map<String, Object> metadades = null;
		String uri = baseUrl + "gdapb/getMetadataDocument/" + ldapUser + "/" + custodyID;
		
		ClientResponse response = ArxiuClient.get(uri, parameters);

		if (ArxiuClient.success(response.getStatus())) {
			try {
				metadades = new ObjectMapper().readValue(
						response.getEntity(String.class), 
						new TypeReference<Map<String, Object>>() {});
			} catch (Exception ex) {
				 logger.error("Error llegint el cos de la resposta", ex);
			}
		}
		return metadades;
	}
	private static final Logger logger = LoggerFactory.getLogger(ArxiuApi.class);
}
