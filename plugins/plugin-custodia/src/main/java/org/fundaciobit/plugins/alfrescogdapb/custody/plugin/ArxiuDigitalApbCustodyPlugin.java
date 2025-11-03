package org.fundaciobit.plugins.alfrescogdapb.custody.plugin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.APIHelper;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.ArxiuConstants;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.ArxiuFirmaDetallDto;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.AutenticacioDto;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.SignatureConstants;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.Utils;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.ValidaSignaturaPeticio;
import org.fundaciobit.plugins.alfrescogdapb.custody.helper.ValidaSignaturaResposta;
import org.fundaciobit.plugins.alfrescogdapb.custody.plugin.api.ArxiuDigitalApi;
import org.fundaciobit.plugins.alfrescogdapb.custody.plugin.cmis.OpenCmisAlfrescoHelper;
import org.fundaciobit.plugins.certificate.InformacioCertificat;
//import org.fundaciobit.pluginsib.validatecertificate.InformacioCertificat;
import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.CustodyException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.NotSupportedCustodyException;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.validatesignature.afirmacxf.AfirmaCxfValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureDetailInfo;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alfresco.client.AlfrescoClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * Implemetació del plugin de custòdia per APB
 * 
 * @author Limit Tecnologies <limit@limit.es>
 *
 */
public class ArxiuDigitalApbCustodyPlugin extends AbstractPluginProperties implements IDocumentCustodyPlugin {

	public static final String ARXIUDIGITALCAIB_PROPERTY_BASE = DOCUMENTCUSTODY_BASE_PROPERTY + "arxiudigitalapb.";
	
	private ArxiuDigitalApi api;
	private String url;
	private String cmisUrl;
	private String user;
	private String password;
	private String tipoarchivo;
	//Dades @firma
	private String base_url;
	private String base_url_publica;
	
	private String id_aplicacio;
	private String ignoreCert;
	private String transformers;
	private String authUsername;
	private String authPassword;
	private String endpoint;
	private String keystore_location;
	private String keystore_type;
	private String keystore_password;
	private String cert_alias;
	private String cert_password;
	private String repositori;
	private IValidateSignaturePlugin validaSignaturaPlugin;
	private OpenCmisAlfrescoHelper openCmisAlfrescoHelper;
	private AlfrescoClient alfrescoClient;
	
	//pluginsib-core-2.0.0
	public ArxiuDigitalApbCustodyPlugin() throws CustodyException {
		super();
		try {
			url = getArxiuUrl();
			base_url_publica = getApiPublicaUrl();
			cmisUrl = getArxiuCmisUrl();
			user = getArxiuUser();
			password = getArxiuPassword();
			tipoarchivo = getArxiuType();
			base_url = getFirmaBase_url();
			
			id_aplicacio = getPropertyValidateApplicationID();
			ignoreCert = getPropertyValidateIgnore();
			transformers = getPropertyValidateTransformers(); 
			authUsername = getPropertyValidateUsername();
			authPassword = getPropertyValidatePassword();
			endpoint = getPropertyValidateEndPoint();
			keystore_location = getPropertyValidateKsPath();
			keystore_type = getPropertyValidateKsType();
			keystore_password = getPropertyValidateKsPassword();
			cert_alias = getPropertyValidateKsCert();
			cert_password = getPropertyValidateKsCertPasswrod();
			
			repositori = getRepositoryAlfresco();
			
			openCmisAlfrescoHelper = new OpenCmisAlfrescoHelper(getEntitatAlfresco());
			
			this.api = new ArxiuDigitalApi(url,base_url_publica,user,password,user);
			
			api.login();
			
		} catch (Exception e) {
			throw new CustodyException("No s'han pogut recueprar les propietats del plugin ArxiuDigitalApbCustodyPlugin", e);
		}
	}
	public ArxiuDigitalApbCustodyPlugin(Properties properties) {
		super("", properties);
	}
	//Per regweb
	public ArxiuDigitalApbCustodyPlugin(String propertyKeyBase, Properties properties) throws CustodyException {
		super(propertyKeyBase, properties);
	
		try {
			url = getArxiuUrl();
			base_url_publica = getApiPublicaUrl();
			cmisUrl = getArxiuCmisUrl();
			user = getArxiuUser();
			password = getArxiuPassword();
			tipoarchivo = getArxiuType();
			base_url = getFirmaBase_url();
			
			id_aplicacio = getPropertyValidateApplicationID();
			ignoreCert = getPropertyValidateIgnore();
			transformers = getPropertyValidateTransformers(); 
			authUsername = getPropertyValidateUsername();
			authPassword = getPropertyValidatePassword();
			endpoint = getPropertyValidateEndPoint();
			keystore_location = getPropertyValidateKsPath();
			keystore_type = getPropertyValidateKsType();
			keystore_password = getPropertyValidateKsPassword();
			cert_alias = getPropertyValidateKsCert();
			cert_password = getPropertyValidateKsCertPasswrod();
			
			repositori = getRepositoryAlfresco();
						
			openCmisAlfrescoHelper = new OpenCmisAlfrescoHelper(getEntitatAlfresco());
			
			this.api = new ArxiuDigitalApi(url,base_url_publica,user,password,user);
			
			api.login();
			
		} catch (Exception e) {
			throw new CustodyException("No s'han pogut recueprar les propietats del plugin ArxiuDigitalApbCustodyPlugin", e);
		}
		
	}
	//Per distribucio
	public ArxiuDigitalApbCustodyPlugin(String propertyKeyBase) throws CustodyException {
		super(propertyKeyBase);
		try {
			url = getArxiuUrl();
			base_url_publica = getApiPublicaUrl();
			cmisUrl = getArxiuCmisUrl();
			user = getArxiuUser();
			password = getArxiuPassword();
			tipoarchivo = getArxiuType();
			
			base_url = getFirmaBase_url();
			id_aplicacio = getFirmaAppID();
			keystore_location = getFirmaKeyStoreLoc();
			keystore_type = getFirmaKeyStoreTyp();
			keystore_password = getFirmaKeyStorePass();
			cert_alias = getFirmaCertAlias();
			cert_password = getFirmaCertPass();
			
			repositori = getRepositoryAlfresco();
			
			openCmisAlfrescoHelper = new OpenCmisAlfrescoHelper(getEntitatAlfresco());
			
			this.api = new ArxiuDigitalApi(url,base_url_publica,user,password,user);
			
			api.login();
			
		} catch (Exception e) {
			throw new CustodyException("No s'han pogut recueprar les propietats del plugin ArxiuDigitalApbCustodyPlugin", e);
		}
	}

	public String reserveCustodyID(
			Map<String, Object> parameters) throws CustodyException {
		long t0 = System.currentTimeMillis();
		logger.debug("Reservant el custòdia ID (creació document sense contingut) " +
						"[ " + parameters.toString() + "]");

		List<Map<String, Object>> propertiesSearch;
		Map<String, Object> jsonBody = new HashMap<String, Object>();
		Map<String, Object> urlParams = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> bodySearchValue = new HashMap<String, Object>();	
		try {
			Object registre = parameters.get("registro");
			Object anexo = parameters.get("anexo");
			String csv = (String) Utils.invokeMethod(anexo, "getCsv");
			
			if (csv != null && !csv.isEmpty()) {
				long t01 = System.currentTimeMillis();
				logger.debug("El document ja existeix al SGD, recuperant el document per csv [csv=" + csv + "]");
				response = getDocumentInfoByCsv(csv);
				long t10 = System.currentTimeMillis();
				logger.debug("La recuperació de la informació del document amb CSV ha tardat " + (t10 - t01) + "ms");
			} else {
				long t01 = System.currentTimeMillis();
				logger.debug("El document no existeix al SGD, procedim a la seva creació");
				String registreTipus = registre.getClass().getName().toLowerCase();
				Object oficina = Utils.invokeMethod(registre, "getOficina");
				String codiOficina = (String) Utils.invokeMethod(oficina, "getCodigo");


				//Adaptar GEISER (registre SIR sense número/data)
				String numeroRegistro = (String) Utils.invokeMethod(registre, "getNumeroRegistroFormateado");
				Date fechaRegistro = (Date) Utils.invokeMethod(registre, "getFecha");
				
//				if(registreTipus.endsWith("entrada") && numeroRegistro != null) //No se pueden modificar los metadatos de registro para un registro de entrada.
//					jsonBody.put("apbNTI:tipoAsiento", "0");
				if(registreTipus.endsWith("salida"))
					jsonBody.put("apbNTI:tipoAsiento", "1");
				
				if (numeroRegistro != null)
					jsonBody.put("apbNTI:numeroRegistro", Utils.invokeMethod(registre, "getNumeroRegistroFormateado"));
				if (fechaRegistro != null)
					jsonBody.put("apbNTI:fechaAsiento", Utils.getFormatData().format(fechaRegistro));
				
				jsonBody.put("apbNTI:oficinaRegistro", Utils.invokeMethod(oficina, "getCodigo"));

				parameters.put("generarCSV", "true");
				urlParams.put("isDraft", "true");
				
				response = api.insertDocument(jsonBody, urlParams);
				long t10 = System.currentTimeMillis();
				logger.debug("La creació del document sense contingut ha tardat " + (t10 - t01) + "ms");
			}
			
			if (response != null && !response.isEmpty()) {
				long t1 = System.currentTimeMillis();
				logger.debug("El mètode reserveCustodyID ha tardat " + (t1 - t0) + "ms en executar-se");
				return response.get("uid") + "#" + response.get("id");
			} else {
				throw new CustodyException("No s'ha pogut crear la reserva per al document");
			}
		} catch (Exception ex) {
			throw new CustodyException("No s'ha pogut crear la reserva per al document", ex);
		}
	}
	
	public void deleteCustody(
			String custodyID) throws CustodyException, NotSupportedCustodyException {
		if(custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		api.deleteDocument(custodyID);
	}

	public void saveAll(
			String custodyID, 
			Map<String, Object> parameters, 
			DocumentCustody document,
			SignatureCustody signatureCustody,
			Metadata[] metadata) throws CustodyException, NotSupportedCustodyException, MetadataFormatException {
		long t0 = System.currentTimeMillis();
		logger.debug("Actualitzant document [custodyID=" + custodyID + "]");
		Map<String, Object> metadades = new HashMap<String, Object>();
		Map<String, Object> jsonBody = new HashMap<String, Object>();
		Map<String, Object> urlParams = new HashMap<String, Object>();
		Map<String, Object> firmes = new HashMap<String, Object>();
		Object registre = null, registroDetalle = null, updateOnlyEstado = false;
		String descripcion;
		String numeroRegistro = null;
		List<String> firmaSeparada = new ArrayList<String>();
		Map<String, Object> res = null;
		try {
			Object updateOnlySignature = parameters.get("updateOnlySignature");
			if (updateOnlySignature != null && signatureCustody != null) {
				updateMetadadesFirma(
						jsonBody, 
						signatureCustody);
				Map<String, Object> jsonProperties = new HashMap<String, Object>();
				jsonProperties.put("properties", jsonBody);
				res = api.updateDocumentPublic(
						custodyID, 
						jsonProperties, 
						urlParams);
				if (res == null) {
//					deleteDocument(custodyID);
					throw new CustodyException("No s'ha pogut guardar el document amb custodyID " + custodyID);
				}
				return;
			}
			if (parameters != null) {
				registre = parameters.get("registro");
				registroDetalle = Utils.invokeMethod(parameters.get("anexo"), "getRegistroDetalle");
				numeroRegistro = (String) Utils.invokeMethod(registre, "getNumeroRegistroFormateado");
				updateOnlyEstado = parameters.get("updateOnlyEstado");
			}

			Map<String, Object> metadadesDocument = api.getMetadataDocument(custodyID);
			
			if (isBorrador(metadadesDocument) && updateOnlyEstado != null && (boolean)updateOnlyEstado && numeroRegistro != null) {
				api.makeFinal(custodyID);
				return;
			}
			
			for (Metadata m : metadata) {
				logger.debug("Metadades document + [custodyId=" + custodyID + "], \n" + m.getKey() + ": " + m.getValue() + "\n");
				metadades.put(m.getKey(), m.getValue());
			}
			Object tipoDocumentalNti = metadades.get("anexo.tipoDocumental.codigo");
			Object origenNti = metadades.get("eni:origen");
			
			boolean isFicheroTecnico = isFicheroTecnico(metadades);
			
			String registreTipus = registre.getClass().getName().toLowerCase();
			if(tipoarchivo.equals("justificante")) {
				upadatContingutAndMetadadesJustificant(
						custodyID,
						jsonBody, 
						signatureCustody, 
						registreTipus);
			} else {
				String csv = (String) Utils.invokeMethod(parameters.get("anexo"), "getCsv");
				if (csv != null && !csv.isEmpty() && numeroRegistro != null) {
					updateMetadadesAnnexAmbCsv(
							csv,
							registre);
					return;
				} else if (csv == null){
					updateContingutAndMetadadesAnnex(
							custodyID,
							document,
							jsonBody,
							metadades,
							signatureCustody,
							firmaSeparada,
							urlParams,
							parameters);
				} else {
					return; // no fer res fins que s'envii a GEISER si el registre ja té un csv
				}
			}
			//Guardam les metadades de firmes si el document es un justificant o un anexe amb firma
			if (tipoarchivo.equals("justificante") || signatureCustody != null) {
				updateMetadadesFirma(
						jsonBody, 
						signatureCustody);
			}
			
			//Metadades generals
			if (document != null || signatureCustody != null) {
				jsonBody.put("apbNTI:idioma_Doc", Utils.getIdiomaCodi(((Long) Utils.invokeMethod(registroDetalle, "getIdioma")).intValue()));
				jsonBody.put("apbNTI:organo_Eni", getOrganoEni() != null ? getOrganoEni() : "EA0001301");
			}
			Object fechaInicio = metadades.get("eni:fecha_inicio");
			if (fechaInicio != null)
				jsonBody.put("apbNTI:fechaInicio_Eni", fechaInicio);
			
			if (numeroRegistro != null) { // Pendent guardar geiser
				descripcion = Utils.generateDescripcion(registroDetalle, numeroRegistro);	
				if (descripcion != null)
					jsonBody.put("apbNTI:descripcion_Doc", descripcion);
			}
			Metadata metadataTipoAsiento = getOnlyOneMetadata(custodyID, "apbNTI:tipoAsiento");
			
			if(metadataTipoAsiento.getValue() == null && registreTipus.endsWith("entrada") && numeroRegistro != null)
				jsonBody.put("apbNTI:tipoAsiento", "0");
//			if(registreTipus.endsWith("salida") && numeroRegistro != null)
//				jsonBody.put("apbNTI:tipoAsiento", "1");
			
			res = api.updateDocument(
					custodyID, 
					jsonBody, 
					urlParams);
			if (res == null) {
//				deleteDocument(custodyID);
				throw new CustodyException("No s'ha pogut guardar el document amb custodyID " + custodyID);
			}
			
			if (!isFicheroTecnico && tipoDocumentalNti != null && origenNti != null && isBorrador(metadadesDocument) && numeroRegistro != null)
				api.makeFinal(custodyID);
			long t1 = System.currentTimeMillis();
			logger.debug("L'actualització del document ha tardat " + (t1 - t0) + "ms");
		} catch (Exception ex) {
//			deleteDocument(custodyID);
			throw new CustodyException("No s'ha pogut actualizar el contingut del document.", ex);
		}
	}
	
	private boolean isFicheroTecnico(Map<String, Object> metadades) {
		Object tipoDocumento = metadades.get("anexo.tipoDocumento.codigo");
		if (tipoDocumento != null && tipoDocumento instanceof String && tipoDocumento.equals("tipoDocumento.03")) return true;
	
		return false;
	}
	
	public void deleteDocument(String custodyID) throws CustodyException, NotSupportedCustodyException {
		long t0 = System.currentTimeMillis();
		if (custodyID != null && custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		logger.debug("Esborrant el document [custodyID=" + custodyID + "]");
		try {
			//Cmis
			alfrescoClient = new AlfrescoClient.Builder()
					.connect(cmisUrl, 
							 user, 
							 password).build();
			DocumentCustody documentEliminat = openCmisAlfrescoHelper.getDocumentById(
					alfrescoClient, 
					custodyID, 
					true);
			//Zona provisional
			if (documentEliminat == null) {
				api.deleteDocument(custodyID);
			}
			long t1 = System.currentTimeMillis();
			logger.debug("El document s'ha esborrat correctament en " + (t1 - t0) + "ms");
		} catch (Exception e) {
			throw new CustodyException("No s'ha pogut esborrar el document amb custodyID " + custodyID + " " + e.getMessage());
		}

	}

	public byte[] getDocument(String custodyID) throws CustodyException {
		return retrieveContent(custodyID);
	}
	
	public byte[] getSignature(String custodyID) throws CustodyException {
		return retrieveContent(custodyID);
	}
	
	//Recupera el contingut origial del document
	public DocumentCustody getDocumentInfo(String custodyID) throws CustodyException {
		logger.debug("Entram dins getDocumentInfo [custodyID=" + custodyID);
		DocumentCustody document = retrieveDocument(custodyID, true);
		logger.debug("Sortim de getDocumentInfo");
		return document;
	}

	public DocumentCustody getDocumentInfoOnly(String custodyID) throws CustodyException {
		logger.debug("Entram dins getDocumentInfoOnly custodyID: " + custodyID);
		DocumentCustody document = retrieveDocument(custodyID, false);
		logger.debug("Sortim de getDocumentInfoOnly");
		return document;	
	}

	//Es fa servir per recuperar el justificant / firma
	public SignatureCustody getSignatureInfo(String custodyID) throws CustodyException {
		logger.debug("Entram dins getSignatureInfo [custodyID=" + custodyID + "]");
		SignatureCustody signature = retrieveSignature(
				custodyID, 
				true);
		logger.debug("Sortim de getSignatureInfo amb signature [custodyID=" + signature + "]");
		return signature;
	}

	//Recuperar signatura del document
	public SignatureCustody getSignatureInfoOnly(String custodyID) throws CustodyException {
		logger.debug("Entram dins getSignatureInfoOnly custodyID: " + custodyID);
		SignatureCustody signature = retrieveSignature(
				custodyID, 
				false);
		logger.debug("Sortim de getSignatureInfoOnly");
		return signature;
	}
	
	private DocumentCustody retrieveDocument(
			String custodyID, 
			boolean retrieveContent) throws CustodyException {
		long t0 = System.currentTimeMillis();
		if(custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}		
		logger.debug("Recuperant les metadades" + (retrieveContent ? " i el contingut del document " : " del document ") + "[custodyID=" + custodyID + "]");
		
		DocumentCustody doc = new DocumentCustody();
		List<String> uuidFirmas = null;
		try {
			//Site
			alfrescoClient = new AlfrescoClient.Builder()
					.connect(cmisUrl, 
							 user, 
							 password).build();
			
			doc = openCmisAlfrescoHelper.getDocumentById(
					alfrescoClient, 
					custodyID, 
					false);
			if (doc == null) {
				//Zona provisional
				doc = new DocumentCustody();
				Map<String, Object> docInfo = api.getMetadataDocument(custodyID);
				byte[] data = api.getDocument(custodyID);
				
				if (docInfo != null && data != null) {
					uuidFirmas = (List<String>) docInfo.get("firmas");
					HashMap content = (HashMap) docInfo.get("content");
					HashMap properties = (HashMap) docInfo.get("properties");
					
					// No retornar si és un document firmat attached (es retorna amb retrieveSignature)
					if (properties.containsKey(ArxiuConstants.ID_FIRMANTE) && (uuidFirmas == null || uuidFirmas.isEmpty())) {
						return null;
					}
					
					if (!properties.containsKey(ArxiuConstants.ID_FIRMANTE) || (uuidFirmas != null && !uuidFirmas.isEmpty()) || retrieveContent) {
						doc.setLength(Long.parseLong((String) content.get("size")));
						doc.setMime((String) (content.get("mimetype")));
						doc.setName((String) Utils.getEndsWith(properties, "name"));
						doc.setData(data);
					} else if (!properties.containsKey(ArxiuConstants.ID_FIRMANTE) || (uuidFirmas != null && !uuidFirmas.isEmpty()) || !retrieveContent) {
						doc.setLength(Long.parseLong((String) content.get("size")));
						doc.setMime((String) (content.get("mimetype")));
						doc.setName((String) Utils.getEndsWith(properties, "name"));
					} else {
						long t1 = System.currentTimeMillis();
						logger.debug("Les metadades del document [custodyID=" + custodyID + "] s'han recuperat en " + (t1 - t0) + "ms");
						return null;
					}
				}
			}
			long t1 = System.currentTimeMillis();
			logger.debug("Les metadades del document [custodyID=" + custodyID + "] s'han recuperat en " + (t1 - t0) + "ms");
		} catch (IOException ex) {
			throw new CustodyException("No s'ha pogut recuperar la informació del document amb custodyID " + custodyID, ex);
			
		}
		return doc;
	}
	
	private byte[] retrieveContent(String custodyID) throws CustodyException {
		long t0 = System.currentTimeMillis();
		logger.debug("Recuperant el contingut del document [custodyID=" + custodyID + "]");
		try {
			byte [] contingut;
			alfrescoClient = new AlfrescoClient.Builder()
					.connect(cmisUrl, 
							 user, 
							 password).build();
			DocumentCustody document = openCmisAlfrescoHelper.getDocumentById(
					alfrescoClient, 
					custodyID, 
					true);
			if (document == null) {
				contingut = api.getDocument(custodyID);
			} else {
				contingut = document.getData();
			}
			long t1 = System.currentTimeMillis();
			logger.debug("La recuperació del contingut del document [custodyID=" + custodyID + "] ha tardat: " + (t1 - t0) + "ms");
			
			return contingut;
		} catch (Exception e) {
			throw new CustodyException("No s'ha pogut recuperar el document amb custodyID " + custodyID + " " + e.getMessage());
		}
	}

	// Es fa servir per recuperar el justificant / firma
	private SignatureCustody retrieveSignature(
			String custodyID,
			boolean retrieveContent) throws CustodyException {
		long t0 = System.currentTimeMillis();
		logger.debug("Recuperant les metadades" + (retrieveContent ? " i el contingut de la signatura " : " de la signatura ") + "[custodyID=" + custodyID + "]");
		List<String> uuidFirma = null, contingutFirmes = new ArrayList<String>();
		Map<String, Object> docInfo, docInfoFirma, contentFirma, propertiesFirma;
		String report;
		SignatureCustody signature = new SignatureCustody();
		DocumentCustody doc = new DocumentCustody();
		String mimeFirma = null, nomFirma = null;
		String lengthFirma = "0";
		byte[] dataFirma = null;

		if (custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}
		HashMap content = null;
		HashMap properties = null;
		try {
			alfrescoClient = new AlfrescoClient.Builder().connect(
					cmisUrl, 
					user, 
					password).build();

			doc = openCmisAlfrescoHelper.getDocumentById(
					alfrescoClient, 
					custodyID, 
					false);
			//Zona provisional
			if (doc != null) {
				if (retrieveContent)
					signature.setData(doc.getData());
				signature.setLength(doc.getLength());
				signature.setName(doc.getName());
				signature.setMime(doc.getMime());
			} else {
				docInfo = api.getMetadataDocument(custodyID);
				if (docInfo != null) {
					uuidFirma = (List<String>) docInfo.get("firmas");
				}
				if (uuidFirma != null && !uuidFirma.isEmpty()) {
					logger.info("Recuperant el contingut de la firma: " + uuidFirma);
					dataFirma = api.getDocument(uuidFirma.get(0));
					docInfoFirma = api.getMetadataDocument(uuidFirma.get(0));
					contentFirma = (Map<String, Object>) docInfoFirma.get("content");
					propertiesFirma = (Map<String, Object>) docInfoFirma.get("properties");
					contingutFirmes = Arrays.asList(Base64.encodeBase64String(dataFirma));
	
					if (propertiesFirma != null) {
						nomFirma = (String) propertiesFirma.get(ArxiuConstants.NAME);
					}
					if (contentFirma != null) {
						mimeFirma = (String) contentFirma.get("mimetype");
						lengthFirma = (String) contentFirma.get("size");
					}
					if (retrieveContent)
						signature.setData(dataFirma);
					signature.setName(nomFirma);
					signature.setMime(mimeFirma);
					signature.setLength(Long.valueOf(lengthFirma));
				} else {
					byte[] data;
					docInfo = api.getMetadataDocument(custodyID);
					if (docInfo != null) {
						content = (HashMap) docInfo.get("content");
						properties = (HashMap) docInfo.get("properties");
					}
					if (properties != null && (properties.containsKey(ArxiuConstants.ID_FIRMANTE) || properties.containsKey(ArxiuConstants.PERFIL_FIRMA))) {
						// Comprovam si s'ha de retornar el document imprimible
						if (getVersioImprimible() != null && getVersioImprimible().equals("true") && retrieveContent) {
							byte[] versioImprimible = generarVersioImprimible(custodyID);
							if (versioImprimible != null) {
								data = versioImprimible;
							} else {
								data = api.getDocument(custodyID);
							}
							signature.setData(data);
						} else {
							signature.setData(api.getDocument(custodyID));
						}
						signature.setName((String) (properties.get(ArxiuConstants.NAME)));
						if (content != null) {
							signature.setLength(Long.parseLong((String) content.get("size")));
							signature.setMime((String) (content.get("mimetype")));
						}
					} else {
						return null;
					}
				}
			}
			long t1 = System.currentTimeMillis();
			logger.debug("Les metadades de la signatura [custodyID=" + custodyID + "] s'han recuperat en " + (t1 - t0) + "ms");
		} catch (Exception ex) {
			throw new CustodyException("No s'ha pogut recuperar la signatura del document amb custodyID " + custodyID, ex);
		}
		return signature;
	}
	
	public Map<String, List<Metadata>> getAllMetadata(String custodyID)
			throws CustodyException, NotSupportedCustodyException {
		long t0 = System.currentTimeMillis();
		logger.debug("Recuperant les metadades del document [custodyID=" + custodyID + "]");
		Map<String, Object> docInfo;
		Map<String, List<Metadata>> properties;
		DocumentCustody doc = new DocumentCustody();
		try {
			if(custodyID.contains("#")) {
				custodyID = custodyID.substring(0, custodyID.indexOf("#"));
			}
			
			alfrescoClient = new AlfrescoClient.Builder().connect(
					cmisUrl, 
					user, 
					password).build();

			doc = openCmisAlfrescoHelper.getDocumentById(
					alfrescoClient, 
					custodyID, 
					false);
			//Zona provisional
			if (doc != null) {
				return null;
			} else {
				docInfo = api.getMetadataDocument(custodyID);
				properties = (Map<String, List<Metadata>>) docInfo.get("properties");
			}
			long t1 = System.currentTimeMillis();
			logger.debug("Les metadades del document [custodyID=" + custodyID + "] s'han recuperat en " + (t1 - t0) + "ms");
		} catch (Exception ex) {
			throw new RuntimeException("No s'han pogut recuperar les metadades del document amb custodyID: " + custodyID, ex);
		}
		return properties;
	}

	public Metadata getOnlyOneMetadata(String custodyID, String key)
			throws CustodyException, NotSupportedCustodyException {
		long t0 = System.currentTimeMillis();
		logger.debug("Recuperant la metadada [metadada=" + key + "] del document [custodyID=" + custodyID + "]");
		String[] keys = key.split(":");
		DocumentCustody doc = new DocumentCustody();
		Metadata metadata = null;
		try {
			alfrescoClient = new AlfrescoClient.Builder().connect(
					cmisUrl, 
					user, 
					password).build();

			doc = openCmisAlfrescoHelper.getDocumentById(
					alfrescoClient, 
					custodyID, 
					false);
			
			//Zona provisional
			if (doc != null) {
				return metadata;
			} else {
				Map<String, Object> metas = api.getMetadataDocument(custodyID);
				Map<String, Object> properties = (Map<String, Object>) metas.get("properties");
				String value = (String) Utils.getEndsWith(properties, keys[keys.length-1]);
				metadata = new Metadata(key,value);
			}
			long t1 = System.currentTimeMillis();
			logger.debug("La metadada del document [custodyID=" + custodyID + "] s'ha recuperat en " + (t1 - t0) + "ms");
			return metadata;
		} catch (Exception e) {
			throw new RuntimeException("No s'han pogut recuperar les metadades del document amb ID: " + custodyID);
		}
	}

	public String getOriginalFileUrl(String custodyID, Map<String, Object> arg1) throws CustodyException {
		if(custodyID.contains("#")) {
			custodyID = custodyID.substring(0, custodyID.indexOf("#"));
		}	
		String validacioUrl = getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "validacio.url");
		if (validacioUrl != null && !validacioUrl.isEmpty() && custodyID != null)
			return validacioUrl + "/" + custodyID;
		else 
			return null;
	}

	private Map<String, Object> recuperaFirma(byte [] document) throws CustodyException{
		long t01 = System.currentTimeMillis();
		logger.debug("Recuperrant al informació de les firmes del document");
		Map<String, Object> firmes = new HashMap<String, Object>();
	    firmes.put("apbNTI:nombreFirmante", new ArrayList<String>());
	    firmes.put("apbNTI:idFirmante", new ArrayList<String>());
	    ValidateSignatureResponse validateSignatureResponse = null;
		try {
			ValidateSignatureRequest validationRequest = new ValidateSignatureRequest();
			validationRequest.setSignatureData(document);
			SignatureRequestedInformation sri = new SignatureRequestedInformation();
			sri.setReturnSignatureTypeFormatProfile(true);
			//Incompatiblitat amb plugins-api
			sri.setReturnCertificateInfo(true);
			sri.setReturnValidationChecks(false);
			sri.setValidateCertificateRevocation(false);
			sri.setReturnCertificates(false);
			sri.setReturnTimeStampInfo(false);
			validationRequest.setSignatureRequestedInformation(sri);
			
			IValidateSignaturePlugin api = new AfirmaCxfValidateSignaturePlugin("plugins.documentcustody.arxiudigitalapb.", getPropertiesValidateSignature());
			
			logger.info("Validant la firma del document per recuperar la informació bàsica...");
			long t0 = System.currentTimeMillis();
			validateSignatureResponse = api.validateSignature(validationRequest);
			long t1 = System.currentTimeMillis();
			logger.info("La validació per recuperar la informació bàsica ha tardat: " + (t1 - t0) +"ms");
			
			String format = validateSignatureResponse.getSignFormat();
		
			String signType = validateSignatureResponse.getSignType();
			String signProfile = getProfileSGD(validateSignatureResponse.getSignProfile());
			
			//String signFormat = getSignFormat(signType, document);
			String signFormat = validateSignatureResponse.getSignFormat();
			String formatSignature = signType + " " + signFormat;
			
			if (formatSignature.contains("XAdES") && formatSignature.contains("internally")) { //Detached
				firmes.put("apbNTI:tipoFirma_Eni", "TF02");
			}else if(formatSignature.contains("XAdES") && formatSignature.contains("enveloped")) { //attached
				firmes.put("apbNTI:tipoFirma_Eni", "TF03");
			}else if (formatSignature.contains("CAdES") && (formatSignature.contains("implicit_enveloped") || formatSignature.contains("attached"))) { //Attached
				firmes.put("apbNTI:tipoFirma_Eni", "TF05");
			}else if (formatSignature.contains("CAdES") && (formatSignature.contains("explicit_enveloped") || formatSignature.contains("explicit/detached"))) { //Detached
				firmes.put("apbNTI:tipoFirma_Eni", "TF04");
			}else if (formatSignature.contains("PAdES")) {
				firmes.put("apbNTI:tipoFirma_Eni", "TF06");
			}else {
				firmes.put("apbNTI:tipoFirma_Eni","");
			}
		    firmes.put("apbNTI:perfilFirma", signProfile);
		    logger.info("Format: " + formatSignature + "; Profile: " + signProfile + "; Type: " + signType);
		    
		    if (validateSignatureResponse.getSignatureDetailInfo() != null) {
				for (SignatureDetailInfo signatureInfo: validateSignatureResponse.getSignatureDetailInfo()) {
					InformacioCertificat certificateInfo = signatureInfo.getCertificateInfo();
					if (certificateInfo != null) {
						if (certificateInfo.getNifResponsable() != null)
							((ArrayList) firmes.get("apbNTI:idFirmante")).add(certificateInfo.getNifResponsable());
						else
							((ArrayList) firmes.get("apbNTI:idFirmante")).add(certificateInfo.getEntitatSubscriptoraNif());
						if (certificateInfo.getNomCompletResponsable() != null)
							((ArrayList) firmes.get("apbNTI:nombreFirmante")).add(certificateInfo.getNomCompletResponsable());
						else
							((ArrayList) firmes.get("apbNTI:nombreFirmante")).add(certificateInfo.getEntitatSubscriptoraNom());
					}
				}
			}
		    long t10 = System.currentTimeMillis();
			logger.debug("La informació de les firmes s'ha recuperat en " + (t10 - t01) + "ms");
		} catch (Exception ex) {
			logger.error("S'ha produit un error validant la firma del document", ex);
		}
		
	    // A més de firmes de certificat, recuperar firma àgil
    	try {
	    	logger.info("Validació firma àgil del document...");
    		String proxyUrl = getSignaturaAgilProxyEndpointURL();
    		String validaSignaturaUrl = proxyUrl + "/valida";
    		
    		ValidaSignaturaPeticio peticio = new ValidaSignaturaPeticio();
    		
    		peticio.setContingut(document);
    		peticio.setAutenticacio(getAutenticacio());
    		
		    ValidaSignaturaResposta respotaFirmaAgil = APIHelper.proxyValidacioPost(
		    		validaSignaturaUrl, 
		    		peticio, 
					ValidaSignaturaResposta.class);
			
		    List<ArxiuFirmaDetallDto> detalls = respotaFirmaAgil.getFirmaDetalls();
		    
		    if (respotaFirmaAgil.isValida() && detalls != null && ! detalls.isEmpty()) {

		    	logger.info("S'han trobat " + detalls.size() + " signatures àgils");
		    	
		    	for (ArxiuFirmaDetallDto firma : respotaFirmaAgil.getFirmaDetalls()) {
		    		if (firma.getResponsableNif() != null)
						((ArrayList) firmes.get("apbNTI:idFirmante")).add(firma.getResponsableNif());
					if (firma.getResponsableNom() != null)
						((ArrayList) firmes.get("apbNTI:nombreFirmante")).add(firma.getResponsableNom());
				}
		    	
		    }
    	} catch (Exception ex) {
    		logger.error("S'ha produit un error validant la firma àgil del document", ex);
		}
    	
    	
	    return firmes;
	}
	
	private AutenticacioDto getAutenticacio() {
		AutenticacioDto autenticacio = new AutenticacioDto();
		autenticacio.setEndpoint(getSignaturaAgilEndpointURL());
		autenticacio.setUsuari(getSignaturaAgilUsername());
		autenticacio.setContrasenya(getSignaturaAgilPassword());
		return autenticacio;
	}

	private byte [] generarVersioImprimible(String identificador) throws CustodyException {
		String uuid_url;
		String concsv_user;
		String concsv_password;
		byte[] reportBytes = null;
	    Client client = null;
		InputStream is;
		WebResource webResource;
		try {
			uuid_url = getPropertyUrlUuid();
			concsv_user = getPropertyUsername();
			concsv_password = getPropertyPassword();
			if (uuid_url.endsWith("/")) {
				webResource = getVersioImprimibleClient(client, concsv_user, concsv_password).resource(
						uuid_url + identificador);
			} else {
				webResource = getVersioImprimibleClient(client, concsv_user, concsv_password).resource(
						uuid_url + "/" + identificador);
			}
			is = webResource.get(InputStream.class);
			reportBytes = IOUtils.toByteArray(is);	
		} catch (Exception ex) {
			String errorDescripcio = "No s'ha pogut recuperar el contingut de la versió imprimible: " + ex.getMessage();
			throw new CustodyException(errorDescripcio);
		}
		return reportBytes;
	}
	
//	private AfirmaClient getAfirmaClient() {
//		return new AfirmaClientImpl(
//					base_url,
//					id_aplicacio,
//					keystore_location,
//					keystore_type,
//					keystore_password,
//					cert_alias,
//					cert_password);
//	}
	
	private Client getVersioImprimibleClient(
			Client client,
			String concsv_user, 
			String concsv_password) {
		if (client == null) {
			client = Client.create();
			if (concsv_user != null) {
				client.addFilter(new HTTPBasicAuthFilter(concsv_user, concsv_password));
			}
		}
		return client;
	}
	
	private byte[] getResourceContent(String resourceName) throws Exception {
		InputStream is = getClass().getResourceAsStream(resourceName);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
	}
	
	private Map<String, Object> getDocumentInfoByCsv(String csv) throws CustodyException {
		List<Map<String, Object>> propertiesSearch;
		Map<String, Object> response = new HashMap<String, Object>();
		Map<String, Object> jsonBody = new HashMap<String, Object>();
		Map<String, Object> urlParams = new HashMap<String, Object>();
		Map<String, Object> bodySearchValue = new HashMap<String, Object>();	
		String uuid = "", id_eni = "";
		
		try {
			bodySearchValue.put("value", csv);
			jsonBody.put("apbNTI:csv", bodySearchValue);
			propertiesSearch = api.searchDocument(jsonBody);
		} catch (Exception e) {
			throw new CustodyException("No s'ha pogut recuperar/cercar el document a l'arxiu digital: " + e);
		}
		
		for (Map<String, Object> map : propertiesSearch) {
			uuid = (String) map.get(ArxiuConstants.NODE_UUID);
			id_eni = (String) map.get(ArxiuConstants.ID_ENI);
		}
		response.put("uid", uuid);
		response.put("id", id_eni);
		
		return response;
	}

	private void updateMetadadesAnnexAmbCsv(String csv, Object registre) throws CustodyException {
		Map<String, Object> bodyUpdate = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> bodySearch = new HashMap<String, Object>();
		Map<String, Object> bodySearchValue = new HashMap<String, Object>();
		List<Map<String, Object>> propertiesSearch;
		try {
			String registreTipus = registre.getClass().getName().toLowerCase();
			if(registreTipus.endsWith("salida")) {
				Object oficina = Utils.invokeMethod(registre, "getOficina");
				String codigoOficinaReg = (String) Utils.invokeMethod(oficina, "getCodigo");
				String numRegistroFormatReg = (String) Utils.invokeMethod(registre, "getNumeroRegistroFormateado");
				
				TimeZone tz = TimeZone.getTimeZone("Europe/Madrid");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				df.setTimeZone(tz);
				String fechaAsientoReg = df.format((Date) Utils.invokeMethod(registre, "getFecha"));
				
				bodyUpdate.put("apbNTI:tipoAsiento", "1");
				bodyUpdate.put("apbNTI:fechaAsiento", fechaAsientoReg);
				bodyUpdate.put("apbNTI:numeroRegistro", numRegistroFormatReg);
				bodyUpdate.put("apbNTI:oficinaRegistro", codigoOficinaReg);
				
				bodySearchValue.put("value", csv);
				bodySearch.put("apbNTI:csv", bodySearchValue);
				propertiesSearch = api.searchDocument(bodySearch);
				
				String uuid = null, tipoAsiento = null;
				for (Map<String, Object> map : propertiesSearch) {
					uuid = (String) Utils.getEndsWith(map, "node-uuid");
					tipoAsiento = (String) Utils.getEndsWith(map, "tipoAsiento");
				}
				//Si el registre ja té metadades les buidam
				if (tipoAsiento != null && !tipoAsiento.equals(" "))
					bodyUpdate.remove("apbNTI:tipoAsiento");
				
				if (uuid != null) {
					api.updateDocument(
							uuid, 
							bodyUpdate, 
							params);
				}
			}
			
		} catch (Exception e) {
			throw new CustodyException("No s'han pogut actualitzar les metadades del registre del document [csv=" + csv + "]");
		}
	}
	
	private void upadatContingutAndMetadadesJustificant(
			String custodyID,
			Map<String, Object> jsonBody,
			SignatureCustody signatureCustody,
			String registreTipus) {
		long t0 = System.currentTimeMillis();
		logger.debug("Actualitzant el contingut i les metadades del justificant " +
				"[custodyID=" + custodyID + "]");
		try {
			jsonBody.put("cm:name", Utils.getJustificantNom(signatureCustody.getName(), registreTipus.endsWith("entrada") ? true : false));
			jsonBody.put("cm:content", DatatypeConverter.printBase64Binary(signatureCustody.getData()));
			jsonBody.put("apbNTI:origen_EniDoc", getJustificantOrigenDocument());
			jsonBody.put("apbNTI:estadoElaboracion_EniDoc", getJustificantEstadoElaboracion());
			jsonBody.put("apbNTI:tipoDocumental_EniDoc", getJustificantTipusDocumental());
			jsonBody.put("apbNTI:titulo_Doc", getJustificantTitolDoc());
			long t1 = System.currentTimeMillis();
			logger.debug("L'actualització del contingut i metadades del justificant ha tardat " + (t1 - t0) + "ms");
		} catch (Exception ex) {
			throw new RuntimeException("S'ha produit un error generant el cos del JSON pel justificant", ex);
		}
	}
	
	private void updateContingutAndMetadadesAnnex(
			String custodyID,
			DocumentCustody document, 
			Map<String, Object> jsonBody, 
			Map<String, Object> metadades, 
			SignatureCustody signatureCustody, 
			List<String> firmaSeparada,
			Map<String, Object> urlParams,
			Map<String, Object> parameters) {
		String attached[] = {"pades", "enveloping", "enveloped", "smime", "PAdES"};
		String dettached[] = {"detached", "cades", "xades"};
		long t0 = System.currentTimeMillis();
		logger.debug("Actualitzant el contingut i les metadades de l'annex " +
				"[custodyID=" + custodyID + "]");
		try {
			Object tipoDocumentalNti = metadades.get("anexo.tipoDocumental.codigo");
			Object origenNti = metadades.get("eni:origen");
			
			if (document != null || signatureCustody != null) {
				//Sense firma o firma attached
				if (document != null){
					jsonBody.put("cm:content", DatatypeConverter.printBase64Binary(document.getData()));
					jsonBody.put("cm:name", 
							Utils.getDocumentName(
									(String) metadades.get("anexo.titulo"), 
									(String) metadades.get("anexo.formato"), 
									document.getName(),
									getControlColisions()));
	//				jsonBody.put("cm:name", Utils.getNewName(custodyID, document.getName()));
				} else if (signatureCustody != null && Arrays.asList(attached).contains(signatureCustody.getSignatureType())) {
					jsonBody.put("cm:content", DatatypeConverter.printBase64Binary(signatureCustody.getData()));
					jsonBody.put("cm:name", 
							Utils.getDocumentName(
									(String) metadades.get("anexo.titulo"), 
									(String) metadades.get("anexo.formato"), 
									signatureCustody.getName(),
									getControlColisions()));
	//				jsonBody.put("cm:name", Utils.getNewName(custodyID, signatureCustody.getName()));
				} 
				//Firma dettached
				if (signatureCustody != null && Arrays.asList(dettached).contains(signatureCustody.getSignatureType())) {
					firmaSeparada.add(DatatypeConverter.printBase64Binary(signatureCustody.getData()));
					jsonBody.put("apbNTI:firma", firmaSeparada);
					urlParams.put("signed", "true");
				} 
				
				jsonBody.put("apbNTI:estadoElaboracion_EniDoc", (String) metadades.get("eni:estado_elaboracion"));
				// ORIGEN
				if (origenNti != null)
					jsonBody.put("apbNTI:origen_EniDoc", origenNti);
				else
					jsonBody.put("apbNTI:origen_EniDoc", "0"); // Temporal. Modificable per Regweb posteriorment.
				// TIPO DOCUMENTAL
				if (tipoDocumentalNti != null)
					jsonBody.put("apbNTI:tipoDocumental_EniDoc", tipoDocumentalNti);
				else
					jsonBody.put("apbNTI:tipoDocumental_EniDoc", "TD99"); // Temporal. Modificable per Regweb posteriorment.
				if (metadades.get("anexo.titulo") != null) {
					String titulo = (String) metadades.get("anexo.titulo");
					jsonBody.put("apbNTI:titulo_Doc", titulo);
				}
			} else {
				//Actualizar solo numero y fecha registro si vienen informados (GEISER)
//				String numeroRegistro = (String) metadades.get("anexo.numeroregistro");
//				Date fechaRegistro = (Date) metadades.get("anexo.fecharegistro");
				Metadata metadataTipoAsiento = getOnlyOneMetadata(custodyID, "apbNTI:tipoAsiento");
				if (metadataTipoAsiento.getValue() != null) {
					// ORIGEN
					if (origenNti != null)
						jsonBody.put("apbNTI:origen_EniDoc", origenNti);
					else
						jsonBody.put("apbNTI:origen_EniDoc", "0"); // Temporal. Modificable per Regweb posteriorment.
					// TIPO DOCUMENTAL
					if (tipoDocumentalNti != null)
						jsonBody.put("apbNTI:tipoDocumental_EniDoc", tipoDocumentalNti);
					else
						jsonBody.put("apbNTI:tipoDocumental_EniDoc", "TD99"); // Temporal. Modificable per Regweb posteriorment.
				}
				
				if (metadataTipoAsiento.getValue() == null || metadataTipoAsiento.getValue().equals("1")) { // Sin informar o salida
//					Actualizar con los metadatos de registro
					Object registre = parameters.get("registro");
					String numeroRegistro = (String) Utils.invokeMethod(registre, "getNumeroRegistroFormateado");
					if (numeroRegistro != null)
						jsonBody.put("apbNTI:numeroRegistro", numeroRegistro);
					
					Date fechaRegistro = (Date) Utils.invokeMethod(registre, "getFecha");
					if (fechaRegistro != null)
						jsonBody.put("apbNTI:fechaAsiento", Utils.getFormatData().format(fechaRegistro));
				}
			}
			long t1 = System.currentTimeMillis();
			logger.debug("L'actualització del contingut i metadades de l'annexs ha tardat " + (t1 - t0) + "ms");
		} catch (Exception ex) {
			throw new RuntimeException("S'ha produit un error generant el cos del JSON per l'annex", ex);
		}
	}
	 
	private void updateMetadadesFirma(
			Map<String, Object> jsonBody, 
			SignatureCustody signatureCustody) throws CustodyException {
		Map<String, String> element = new HashMap<String, String>();
		try {	
			byte [] documentBytes = signatureCustody.getData();
			Map<String, Object> firmes = recuperaFirma(documentBytes);
			//Converteix tots els firmants en un string
			element = convertArrayToString(firmes);
			String perfilFirma = (String) firmes.get("apbNTI:perfilFirma");
			String tipoFirma = (String) firmes.get("apbNTI:tipoFirma_Eni");
			
			if (tipoFirma != null)
				jsonBody.put("apbNTI:tipoFirma_Eni", tipoFirma);
			if (perfilFirma != null)
				jsonBody.put("apbNTI:perfilFirma", perfilFirma);
			
			jsonBody.put("apbNTI:nombreFirmante", element.get("nombreFirmante"));
			jsonBody.put("apbNTI:idFirmante", element.get("idFirmante"));
		} catch (Exception ex) {
			throw new CustodyException(ex);
		}
	}
	
	private Map<String, String> convertArrayToString(Map<String, Object> firmes) {
		
		ArrayList<String> element = new ArrayList<String>();
		String nombreFirmante = "", idFirmante = "";
		Map<String, String> elementStr = new HashMap<String, String>();
		
		element = (ArrayList<String>) firmes.get("apbNTI:nombreFirmante");
		
		for (String string : element) {
			nombreFirmante += string;
			nombreFirmante += ",";
		}
		if (nombreFirmante != "") {
			elementStr.put("nombreFirmante", nombreFirmante.substring(0, nombreFirmante.length() - 1));
		} else { 
			elementStr.put("nombreFirmante", "");
		}
		element = (ArrayList<String>) firmes.get("apbNTI:idFirmante");
		
		for (String string : element) {
			idFirmante += string ;
			idFirmante += ",";
		}
		if (idFirmante != "") {
			elementStr.put("idFirmante", idFirmante.substring(0, idFirmante.length() - 1));
		} else {
			elementStr.put("idFirmante", "");
		}
		
		return elementStr;
	}
	
	private String getProfileSGD(String profileValidate) {
		String profile = null;
		switch (profileValidate) {
		case "AdES-BES":
			profile = SignatureConstants.SIGNPROFILE_BASELINE_B_LEVEL;
			break;
		case "AdES-EPES":
			profile = SignatureConstants.SIGNPROFILE_EPES;
			break;
		case "AdES-T":
			profile = SignatureConstants.SIGNPROFILE_T;
			break;
		case "AdES-C":
			profile = SignatureConstants.SIGNPROFILE_C;
			break;
		case "AdES-X":
			profile = SignatureConstants.SIGNPROFILE_X;
			break;
		case "AdES-X1":
			profile = SignatureConstants.SIGNPROFILE_X;
			break;
		case "AdES-X2":
			profile = SignatureConstants.SIGNPROFILE_X;
			break;
		case "AdES-XL":
			profile = SignatureConstants.SIGNPROFILE_XL;
			break;
		case "AdES-XL1":
			profile = SignatureConstants.SIGNPROFILE_XL;
			break;
		case "AdES-XL2":
			profile = SignatureConstants.SIGNPROFILE_XL;
			break;
		case "AdES-A":
			profile = SignatureConstants.SIGNPROFILE_A;
			break;
		case "PAdES-Basic":
			profile = SignatureConstants.SIGNPROFILE_BASIC;	
			break;
		case "AdES-LTV":
			profile = SignatureConstants.SIGNPROFILE_PADES_LTV;
			break;
		case "PAdES-LTV":
			profile = SignatureConstants.SIGNPROFILE_PADES_LTV;
			break;
		}
		return profile;
	}
	private boolean isBorrador(Map<String, Object> metadadesDocument) {
		//comprovar si és borrador i definitiu -> makefinal
		List<String> aspectsDocument = (List<String>) Utils.getEndsWith(metadadesDocument, "aspects");
		return aspectsDocument.contains("{http://www.portsdebalears.com/model/nti/1.0}borrador");
	}
	private Properties getPropertiesValidateSignature() {
		Properties properties = null;
		try {
			properties = new Properties();
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.applicationID", id_aplicacio);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.ignoreservercertificates", ignoreCert);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.TransformersTemplatesPath", transformers);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.username", authUsername);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.password", authPassword);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.endpoint", endpoint);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.path", keystore_location);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.type", keystore_type);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.password", keystore_password);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.cert.alias",cert_alias);
			properties.setProperty(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.cert.password", cert_password);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return properties;
	}
	/// Metodes per obtenir propietats
	private String getPropertyValidateApplicationID() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.applicationID");
	}
	private String getPropertyValidateIgnore() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.ignoreservercertificates");
	}
	private String getPropertyValidateUsername() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.username");
	}
	private String getPropertyValidatePassword() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.password");
	}
	private String getPropertyValidateTransformers() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.TransformersTemplatesPath");
	}
	private String getPropertyValidateEndPoint() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.endpoint");
	}
	private String getPropertyValidateKsPath() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.path");
	}
	private String getPropertyValidateKsType() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.type");
	}
	private String getPropertyValidateKsPassword() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.password");
	}
	private String getPropertyValidateKsCert() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.cert.alias");
	}
	private String getPropertyValidateKsCertPasswrod() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "plugins.validatesignature.afirmacxf.authorization.ks.cert.password");
	}
		
	/// Metodes per obtenir propietats
	private String getPropertyUrlUuid() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "conversio.imprimible.url.uuid");
	}
	private String getPropertyUsername() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "conversio.imprimible.usuari");
	}
	private String getPropertyPassword() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "conversio.imprimible.contrasenya");
	}
	private String getArxiuUrl() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "baseurl");
	}
	private String getApiPublicaUrl() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "api.publica.baseurl");
	}
	private String getArxiuUser() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "user");
	}
	private String getArxiuPassword() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "password");
	}
	private String getArxiuLdapUser() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "ldapuser");
	}
	private String getVersioImprimible() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "versioImprimible");
	}
	private String getApplicationID() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "applicationID");
	}
	private String getTemplateID() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "templateID");
	}
	private String getUsername() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "usernameEvisor");
	}
	private String getPassword() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "passwordEvisor");
	}
	private String getArxiuType() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "tipoarchivo");
	}
	private String getJustificantOrigenDocument() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "origen_document_EL");
	}
	private String getJustificantEstadoElaboracion() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "estat_elaboracio_EL");
	}
	private String getJustificantTipusDocumental() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "tipus_documental_EL");
	}
	private String getJustificantTitolDoc() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "titol_doc_EL");
	}
	private String getTrustStorePwd() throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "truststore_pwd");
	}
	private String getTrustStore()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "truststore");
	}
	private String getFirmaBase_url()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaBaseurl");
	}
	private String getFirmaAppID()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaAppID");
	}
	private String getFirmaKeyStoreLoc()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaKeyPath");
	}
	private String getFirmaKeyStoreTyp()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaKeyType");
	}
	private String getFirmaKeyStorePass()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaKeyPass");
	}
	private String getFirmaCertAlias()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaCertAlias");
	}
	private String getFirmaCertPass()throws Exception {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "afirmaCertPass");
	}
	private String getRepositoryAlfresco() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "repository");
	}
	private String getEntitatAlfresco() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "entitat");
	}
	private String getArxiuCmisUrl() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "cmisUrl");
	}
	private String getPropertyPluginValidaSignatura() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + ".plugin.validatesignature.class");
	}
	private String getOrganoEni() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "eni.organo");
	}

	public String getSignaturaAgilProxyEndpointURL() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "firmaagil.proxy.endpoint");
	}
	
	public String getSignaturaAgilEndpointURL() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "firmaagil.endpoint");
	}
    
    private String getSignaturaAgilUsername() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "firmaagil.username");
	}
    
    private String getSignaturaAgilPassword() {
		return getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "firmaagil.password");
	}
    
	private boolean getControlColisions() throws Exception {
		String controlarColisionsStr = getPropertyString(ARXIUDIGITALCAIB_PROPERTY_BASE + "controlar.colisions");
		return !controlarColisionsStr.isEmpty() ? Boolean.valueOf(controlarColisionsStr) : true;
	}
	/**
	 * Si troba la propietat la retiorna si no la troba retorna un string buit
	 * @param property
	 * @return
	 */
	private String getPropertyString(String property){
		String value = getProperty(property);
		return (value != null) ? value : "";
	}
	public String getCsv(String arg0, Map<String, Object> arg1) throws CustodyException {
		return null;
	}
	public String getCsvGenerationDefinition(String arg0, Map<String, Object> arg1) throws CustodyException {
		return null;
	}
	public String getCsvValidationWeb(String arg0, Map<String, Object> arg1) throws CustodyException {
		return null;
	}
	public String getEniFileUrl(String arg0, Map<String, Object> arg1) throws CustodyException {
		return null;
	}
	public String getPrintableFileUrl(String arg0, Map<String, Object> arg1) throws CustodyException {
		return null;
	}
	public String getValidationFileUrl(String arg0, Map<String, Object> arg1) throws CustodyException {
		return null;
	}
	public String addAnnex(String arg0, AnnexCustody arg1, Map<String, Object> arg2)
			throws CustodyException, NotSupportedCustodyException {		return null;
	}
	public void addMetadata(String arg0, Metadata arg1, Map<String, Object> arg2)
			throws CustodyException, NotSupportedCustodyException, MetadataFormatException {		
	}
	public void addMetadata(String arg0, Metadata[] arg1, Map<String, Object> arg2)
			throws CustodyException, NotSupportedCustodyException, MetadataFormatException {		
	}
	public void deleteAllAnnexes(String arg0) throws CustodyException, NotSupportedCustodyException {		
	}
	public void deleteAllMetadata(String arg0) throws CustodyException {		
	}
	public void deleteAnnex(String arg0, String arg1) throws CustodyException, NotSupportedCustodyException {		
	}
	public List<Metadata> deleteMetadata(String arg0, String arg1) throws CustodyException {
		return null;
	}
	public List<Metadata> deleteMetadata(String arg0, String[] arg1) throws CustodyException {
		return null;
	}
	public void deleteSignature(String arg0) throws CustodyException, NotSupportedCustodyException {		
	}
	public List<String> getAllAnnexes(String arg0) throws CustodyException {
		return null;
	}
	public byte[] getAnnex(String arg0, String arg1) throws CustodyException {
		return null;
	}
	public AnnexCustody getAnnexInfo(String arg0, String arg1) throws CustodyException {
		return null;
	}
	public AnnexCustody getAnnexInfoOnly(String arg0, String arg1) throws CustodyException {
		return null;
	}
	public List<Metadata> getMetadata(String arg0, String arg1) throws CustodyException, NotSupportedCustodyException {
		return null;
	}
	
	public String[] getSupportedSignatureTypes() {
		return null;
	}
	public void saveDocument(String arg0, Map<String, Object> arg1, DocumentCustody arg2)
			throws CustodyException, NotSupportedCustodyException {		
	}
	public void saveSignature(String arg0, Map<String, Object> arg1, SignatureCustody arg2)
			throws CustodyException, NotSupportedCustodyException {		
	}
	public boolean supportsAnnexes() {
		return false;
	}
	public Boolean supportsAutomaticRefreshSignature() {
		return null;
	}
	public boolean supportsDeleteAnnex() {
		return false;
	}
	public boolean supportsDeleteDocument() {
		return false;
	}
	public boolean supportsDeleteMetadata() {
		return false;
	}
	public boolean supportsDeleteSignature() {
		return false;
	}
	public boolean supportsMetadata() {
		return false;
	}
	public void updateMetadata(String arg0, Metadata arg1, Map<String, Object> arg2)
			throws CustodyException, NotSupportedCustodyException, MetadataFormatException {		
	}
	public void updateMetadata(String arg0, Metadata[] arg1, Map<String, Object> arg2)
			throws CustodyException, NotSupportedCustodyException, MetadataFormatException {		
	}
	public boolean supportsDeleteCustody() {
		return false;
	}

	public String getValidationUrl(String custodyID, Map<String, Object> parameters) throws CustodyException {
		return null;
	}

	public String getSpecialValue(String custodyID, Map<String, Object> parameters) throws CustodyException {
		return custodyID;
	}

	private static final Logger logger = LoggerFactory.getLogger(ArxiuDigitalApbCustodyPlugin.class);	
}
