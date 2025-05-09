package es.caib.regweb3.plugins.justificante.apb.cmis;

import com.alfresco.client.AlfrescoClient;
import com.alfresco.client.api.core.NodesAPI;
import com.alfresco.client.api.core.SitesAPI;
import com.alfresco.client.api.core.model.body.NodeBodyCreate;
import com.alfresco.client.api.core.model.body.NodeBodyUpdate;
import com.alfresco.client.api.core.model.representation.NodeRepresentation;
import com.alfresco.client.api.core.model.representation.SiteContainerRepresentation;
import com.alfresco.client.api.search.SearchAPI;
import com.alfresco.client.api.search.body.QueryBody;
import com.alfresco.client.api.search.body.RequestQuery;
import com.alfresco.client.api.search.model.ResultNodeRepresentation;
import com.alfresco.client.api.search.model.ResultSetRepresentation;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gson.internal.LinkedTreeMap;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Limit Tecnologies
 * 
 * @author andreus
 * 
 */
public class OpenCmisAlfrescoHelper {

	public final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public final JsonFactory JSON_FACTORY = new JacksonFactory();

	public String SIGLES_ENTITAT;
	
	public String CMIS_DOCUMENT_TYPE;
	public String CMIS_DOCUMENT_ASPECT_BASE;
	public String CMIS_DOCUMENT_ASPECT_CUSTOM;
	
	public String CMIS_FOLDER_TYPE;
	public String CMIS_FOLDER_ASPECT;

	public OpenCmisAlfrescoHelper(String siglesEntitat) {
		this.SIGLES_ENTITAT = siglesEntitat;
		
		this.CMIS_DOCUMENT_TYPE = siglesEntitat + "Registro:anexo";
		this.CMIS_DOCUMENT_ASPECT_BASE="cm:titled";
		this.CMIS_DOCUMENT_ASPECT_CUSTOM=siglesEntitat + "Registro:d_anexo";
		
		this.CMIS_FOLDER_TYPE = siglesEntitat + "Registro:registro";
		this.CMIS_FOLDER_ASPECT = siglesEntitat + "Registro:c_registro";
	}

	/**
	 * Crea recursivament una ruta de carpetes partint de la carpeta principal
	 * de documents del registre. El path es dividir� en carpetes que s�anir�n
	 * creant de forma anidada. Les carpetes creades ser�n del tipus cmis:folder
	 * excepte la darrera, que sera de tipus Registro:registro
	 * Retorna la ultima carpeta creada.
	 */
	public String crearRutaDeCarpetes(AlfrescoClient client, String site, String rutaCarpetes, Map<String, Object> folderProperties) {
		
		String idCarpetaAnterior  = null;
		
		try {

			// Retrieve Site
			SitesAPI sitesAPI = client.getSitesAPI();
			Response<SiteContainerRepresentation> doclibContainerResponse = sitesAPI
					.getSiteContainerCall(site, "documentLibrary").execute();
			SiteContainerRepresentation doclibContainer = doclibContainerResponse.body();
			idCarpetaAnterior  = doclibContainer.getId();
			
			if (rutaCarpetes!=null && !"".equals(rutaCarpetes)) {

				String[] carpetes = rutaCarpetes.split("/");
				NodesAPI nodesAPI = client.getNodesAPI();
				
				for (int c=0; c<carpetes.length; c++) {		
					if (carpetes[c]!=null && !"".equals(carpetes[c])) {
						String nomCarpeta = carpetes[c];
						NodeRepresentation node = nodesAPI.getNodeCall(idCarpetaAnterior, null, nomCarpeta, null).execute().body();
						if (node!=null) {
							idCarpetaAnterior = node.getId();
						}else{
							
							List<String> aspectes = null;
							Map<String, Object> properties = null;
							String tipusCarpeta = "cm:folder";
							
							if (c==carpetes.length-1) {
								aspectes = new ArrayList<String>();
								aspectes.add(CMIS_FOLDER_ASPECT);
								
								properties = folderProperties;
								
								tipusCarpeta = CMIS_FOLDER_TYPE;
							}
							
							NodeBodyCreate nodeBodyCreate = new NodeBodyCreate(nomCarpeta, tipusCarpeta, properties, aspectes);
							//NodeBodyCreate nodeBodyCreate = new NodeBodyCreate(nomCarpeta, "cm:folder");
							Response<NodeRepresentation> nodeResponse = nodesAPI.createNodeCall(idCarpetaAnterior, nodeBodyCreate).execute();
							NodeRepresentation folder = nodeResponse.body();
							idCarpetaAnterior = folder.getId();
						}
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("Error crearRutaDeCarpetes " + rutaCarpetes);
			ex.printStackTrace();
		}
		
		return idCarpetaAnterior;
	}

	/**
	 * Crea un document dins la ruta indicada, amb data de l'annex i amb les
	 * propietats indicades
	 * 
	 * @param document
	 *            Conte el array de bites del fitxer final.
	 * @param fileName
	 *            Nom del fitxer
	 * @param path
	 *            Ruta a on es crear� el document
	 * @param fileProperties
	 *            Metadades del document
	 * @return El document creat o null si no s�ha pogut crear.
	 * @throws IOException 
	 */
	public String crearDocument(
			AlfrescoClient client, 
			String site,
			org.fundaciobit.plugins.documentcustody.api.AnnexCustody document, 
			String fileName, 
			String path,
			Map<String, Object> fileProperties, 
			Map<String, Object> folderProperties) throws IOException {

		String parentFolder = null;
		String documentCreat = null;

		// Si la carpeta proposada no existeix, es crear� la ruta necessaria
		parentFolder = crearRutaDeCarpetes(client, site, path, folderProperties);

		if (parentFolder != null && !"".equals(parentFolder)) {
			document.setName(fileName);
			documentCreat = crearDocument(client, parentFolder, document);
		}
		
		//No podem actualitzar les propietats amb el mateix client utilitzat per pujar l�arxiu o dona un BadRequest
		//if (documentCreat != null && !"".equals(documentCreat))
			//setPropietatsNode(client, documentCreat, toLinkedTreeMap(fileProperties));
		
		return documentCreat;
	}

	public LinkedTreeMap<String,Object> toLinkedTreeMap(Map<String, Object> properties) {
		LinkedTreeMap<String, Object> props = new LinkedTreeMap<String,Object>();
		
		if (properties!=null && properties.size()>0)
			for (Map.Entry<String, Object> entry : properties.entrySet())
				props.put(entry.getKey(), entry.getValue());
		
		return props;
	}
	
	private String crearDocument (
			AlfrescoClient client, 
			String idNodePare, 
			org.fundaciobit.plugins.documentcustody.api.AnnexCustody document) throws IOException {

		RequestBody requestBody = RequestBody.create (MediaType.parse(document.getMime()), document.getData());
		MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
		multipartBuilder.addFormDataPart("filedata", document.getName(), requestBody);

		RequestBody fileRequestBody = multipartBuilder.build();
		HashMap<String, RequestBody> map = new HashMap<String, RequestBody>();
		map.put("filedata", fileRequestBody);
		map.put("name", RequestBody.create(MediaType.parse("multipart/form-data"), document.getName()));
		
		// Create Content
		NodesAPI nodesAPI = client.getNodesAPI();
		Response<NodeRepresentation> createdNodeResponse = nodesAPI.createNodeCall(idNodePare, map).execute();
		return createdNodeResponse.body().getId();
	}

	public String setPropietatsNode(AlfrescoClient client, String idNode, Map<String, Object> props, boolean updateAspects) throws IOException {

		NodesAPI nodesAPI = client.getNodesAPI();
	
		NodeBodyUpdate nodeBodyUpdate = new NodeBodyUpdate(toLinkedTreeMap(props));
		
		if (updateAspects) {
			
			List<String> aspectes = new ArrayList<String>();
			aspectes.add(CMIS_DOCUMENT_ASPECT_BASE);
			aspectes.add(CMIS_DOCUMENT_ASPECT_CUSTOM);
			
			nodeBodyUpdate = new NodeBodyUpdate(null, CMIS_DOCUMENT_TYPE, toLinkedTreeMap(props), aspectes);
		}
		
		NodeRepresentation updatedNode = nodesAPI.updateNodeCall(idNode, nodeBodyUpdate).execute().body();
		return updatedNode.getId();
	}

	/**
	 * Recupera tots els documents de una custodia si se li passa nomes el
	 * custodyID Si se li afegeix el sufixe "D" o "S", recuperar� nomes el
	 * document o la firma respectivament
	 * 
	 * @throws IOException
	 */
	public org.fundaciobit.plugins.documentcustody.api.DocumentCustody getDocumentById(
			AlfrescoClient client,
			String custodyID,
			boolean delete) throws IOException {

		org.fundaciobit.plugins.documentcustody.api.DocumentCustody out = null;

		SearchAPI searchAPI = client.getSearchAPI();

		String queryString = "select * " + "FROM " + SIGLES_ENTITAT + "Registro:d_anexo " + "WHERE " + SIGLES_ENTITAT
				+ "Registro:dr_custodyID = '" + custodyID + "' ";
				//+ "AND " + SIGLES_ENTITAT + "Registro:dr_tipoArchivo='"+tipo+"'";

		RequestQuery cmisQuery = new RequestQuery().query(queryString).language(RequestQuery.LanguageEnum.CMIS);
		QueryBody cmisbody = new QueryBody().query(cmisQuery);
		ResultSetRepresentation<ResultNodeRepresentation> cmisResult = searchAPI.searchCall(cmisbody).execute().body();

		if (cmisResult != null) {

			for (ResultNodeRepresentation node : cmisResult.getList()) {

				NodesAPI nodesAPI = client.getNodesAPI();
				
				if (delete) {
					
					nodesAPI.deleteNodeCall(node.getId()).execute();
					
				}else{
				
					Call<ResponseBody> downloadCall = nodesAPI.getNodeContentCall(node.getId());
					InputStream inputStream = downloadCall.execute().body().byteStream();
	
					out = new org.fundaciobit.plugins.documentcustody.api.DocumentCustody();
					out.setData(IOUtils.toByteArray(inputStream));
					out.setName(node.getName());
					out.setMime(node.getContent().getMimeType());
				}
			}
		}

		return out;
	}

	public String getDocumentAttributeById(
			AlfrescoClient client, 
			String custodyID,
			String tipo,
			String attribute) throws IOException {

		String out = null;

		SearchAPI searchAPI = client.getSearchAPI();

		String queryString = "select cmis:name " + "FROM " + SIGLES_ENTITAT + "Registro:d_anexo " + "WHERE " + SIGLES_ENTITAT
				+ "Registro:dr_custodyID = '" + custodyID + "' " + "AND " + SIGLES_ENTITAT
				+ "Registro:dr_tipoArchivo='"+tipo+"'";

		RequestQuery cmisQuery = new RequestQuery().query(queryString).language(RequestQuery.LanguageEnum.CMIS);
		QueryBody cmisbody = new QueryBody().query(cmisQuery);
		ResultSetRepresentation<ResultNodeRepresentation> cmisResult = searchAPI.searchCall(cmisbody).execute().body();

		if (cmisResult != null) {

			for (ResultNodeRepresentation node : cmisResult.getList()) {
				if (attribute!=null && !"name".equalsIgnoreCase(attribute))
					return node.getName();
				else
					return node.getId();
			}
		}

		return out;
	}



	public String getAlfrescoUrl() {
		return System.getProperty("es.caib.regweb.annex.plugins.documentcustody.alfresco.url");
	}

	public String getUsername() {
		return System.getProperty("es.caib.regweb.annex.plugins.documentcustody.alfresco.access.user");
	}

	public String getPassword() {
		return System.getProperty("es.caib.regweb.annex.plugins.documentcustody.alfresco.access.pass");
	}

}
