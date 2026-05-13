package es.caib.regweb3.plugins.justificante.apb.cmis;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

public class OpenCmisAlfrescoHelper {

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

	public String crearRutaDeCarpetes(Session session, String site, String rutaCarpetes, Map<String, Object> folderProperties) {
		
		String idCarpetaAnterior = null;
		
		try {
			String basePath = "/Sites/" + site + "/documentLibrary";
			Folder doclib = (Folder) session.getObjectByPath(basePath);
			idCarpetaAnterior = doclib.getId();
			
			if (rutaCarpetes!=null && !"".equals(rutaCarpetes)) {
				String[] carpetes = rutaCarpetes.split("/");
				String currentPath = basePath;
				
				for (int c=0; c<carpetes.length; c++) {		
					if (carpetes[c]!=null && !"".equals(carpetes[c])) {
						String nomCarpeta = carpetes[c];
						currentPath = currentPath + "/" + nomCarpeta;
						
						try {
							CmisObject child = session.getObjectByPath(currentPath);
							idCarpetaAnterior = child.getId();
						} catch (CmisObjectNotFoundException e) {
							List<String> secondaryTypes = null;
							Map<String, Object> properties = new HashMap<String, Object>();
							String tipusCarpeta = "cmis:folder";
							
							if (c == carpetes.length - 1) {
								secondaryTypes = new ArrayList<String>();
								secondaryTypes.add("P:" + CMIS_FOLDER_ASPECT);
								
								properties = folderProperties != null ? new HashMap<String, Object>(folderProperties) : new HashMap<String, Object>();
								
								tipusCarpeta = CMIS_FOLDER_TYPE;
							}
							
							properties.put(PropertyIds.NAME, nomCarpeta);
							properties.put(PropertyIds.OBJECT_TYPE_ID, tipusCarpeta);
							if (secondaryTypes != null) {
								properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, secondaryTypes);
							}
							
							Folder parentFolder = (Folder) session.getObject(idCarpetaAnterior);
							Folder newFolder = parentFolder.createFolder(properties);
							idCarpetaAnterior = newFolder.getId();
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

	public String crearDocument(
			Session session, 
			String site,
			org.fundaciobit.plugins.documentcustody.api.AnnexCustody document, 
			String fileName, 
			String path,
			Map<String, Object> fileProperties, 
			Map<String, Object> folderProperties) throws IOException {

		String parentFolder = null;
		String documentCreat = null;

		parentFolder = crearRutaDeCarpetes(session, site, path, folderProperties);

		if (parentFolder != null && !"".equals(parentFolder)) {
			document.setName(fileName);
			documentCreat = crearDocument(session, parentFolder, document);
		}
		
		return documentCreat;
	}
	
	private String crearDocument (
			Session session, 
			String idNodePare, 
			org.fundaciobit.plugins.documentcustody.api.AnnexCustody document) throws IOException {

		Folder parent = (Folder) session.getObject(idNodePare);
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		properties.put(PropertyIds.NAME, document.getName());
		
		ContentStream contentStream = new ContentStreamImpl(
				document.getName(),
				BigInteger.valueOf(document.getData().length),
				document.getMime(),
				new java.io.ByteArrayInputStream(document.getData()));
		
		Document doc = parent.createDocument(properties, contentStream, VersioningState.NONE);
		return doc.getId();
	}

	public String setPropietatsNode(Session session, String idNode, Map<String, Object> props, boolean updateAspects) throws IOException {

		Map<String, Object> properties = new HashMap<String, Object>();
		if (props != null) {
			properties.putAll(props);
		}
		
		if (updateAspects) {
			List<String> secondaryTypes = new ArrayList<String>();
			secondaryTypes.add("P:" + CMIS_DOCUMENT_ASPECT_BASE);
			secondaryTypes.add("P:" + CMIS_DOCUMENT_ASPECT_CUSTOM);
			properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, secondaryTypes);
			properties.put(PropertyIds.OBJECT_TYPE_ID, CMIS_DOCUMENT_TYPE);
		}
		
		Document doc = (Document) session.getObject(idNode);
		doc.updateProperties(properties);
		return doc.getId();
	}

	public org.fundaciobit.plugins.documentcustody.api.DocumentCustody getDocumentById(
			Session session,
			String custodyID,
			boolean delete) throws IOException {

		org.fundaciobit.plugins.documentcustody.api.DocumentCustody out = null;

		String queryString = "SELECT * FROM " + SIGLES_ENTITAT + "Registro:d_anexo WHERE " + SIGLES_ENTITAT
				+ "Registro:dr_custodyID = '" + custodyID + "' ";

		ItemIterable<QueryResult> results = session.query(queryString, false);

		for (QueryResult result : results) {
			String nodeId = result.getPropertyValueByQueryName("cmis:objectId");

			if (delete) {
				CmisObject obj = session.getObject(nodeId);
				obj.delete(true);
			} else {
				Document doc = (Document) session.getObject(nodeId);
				ContentStream contentStream = doc.getContentStream();
				InputStream inputStream = contentStream.getStream();

				out = new org.fundaciobit.plugins.documentcustody.api.DocumentCustody();
				out.setData(IOUtils.toByteArray(inputStream));
				out.setName(doc.getName());
				out.setMime(contentStream.getMimeType());
			}
		}

		return out;
	}

	public String getDocumentAttributeById(
			Session session, 
			String custodyID,
			String tipo,
			String attribute) throws IOException {

		String out = null;

		String queryString = "SELECT cmis:name FROM " + SIGLES_ENTITAT + "Registro:d_anexo WHERE " + SIGLES_ENTITAT
				+ "Registro:dr_custodyID = '" + custodyID + "' AND " + SIGLES_ENTITAT
				+ "Registro:dr_tipoArchivo='"+tipo+"'";

		ItemIterable<QueryResult> results = session.query(queryString, false);

		for (QueryResult result : results) {
			if (attribute != null && !"name".equalsIgnoreCase(attribute))
				return result.getPropertyValueByQueryName("cmis:name");
			else
				return result.getPropertyValueByQueryName("cmis:objectId");
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
