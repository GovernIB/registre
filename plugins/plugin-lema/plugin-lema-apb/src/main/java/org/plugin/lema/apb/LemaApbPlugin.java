package org.plugin.lema.apb;

import java.util.Properties;

import javax.xml.ws.soap.SOAPFaultException;

import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.plugin.lema.apb.helper.APIHelper;
import org.plugin.lema.api.AuthenticationDto;
import org.plugin.lema.api.ConsultaAcuseReciboRequest;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.ConsultaAnexoRequest;
import org.plugin.lema.api.ConsultaAnexoResponse;
import org.plugin.lema.api.ILemaPlugin;
import org.plugin.lema.api.LemaPluginException;
import org.plugin.lema.api.LocalizaRequest;
import org.plugin.lema.api.LocalizaResponse;
import org.plugin.lema.api.PeticionAccesoRequest;
import org.plugin.lema.api.PeticionAccesoResponse;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
public class LemaApbPlugin extends AbstractPluginProperties implements ILemaPlugin {

	private static final String basePluginLemaApb = LEMA_BASE_PROPERTY + "apb.";
	private static final String PROPERTY_PROXY_URL = basePluginLemaApb + "proxy.endpoint";
	private static final String PROPERTY_LOCALIZA_ENDPOINT = basePluginLemaApb + "service.localiza.endpoint";

	private static final String KEYSTORE_FILE = basePluginLemaApb + "keystore.file";
	private static final String KEYSTORE_TYPE = basePluginLemaApb + "keystore.type";
	private static final String KEYSTORE_ALIAS = basePluginLemaApb + "keystore.alias";
	private static final String KEYSTORE_PASS = basePluginLemaApb + "keystore.pass";

	private static final String TITULAR_NIF = basePluginLemaApb + "titular.nif";
	private static final String RECEPTOR_NIF = basePluginLemaApb + "receptor.nif";
	
	public LemaApbPlugin() {
		super();
	}

	public LemaApbPlugin(String propertyKeyBase, Properties properties) {
		super(propertyKeyBase, properties);
	}

	public LemaApbPlugin(String propertyKeyBase) {
		super(propertyKeyBase);
	}

	@Override
	public LocalizaResponse localiza(LocalizaRequest request) throws LemaPluginException {
		LocalizaResponse respuesta = null;

		try {
			String proxyUrl = getPropertyRequired(PROPERTY_PROXY_URL);
			String localizaCall = proxyUrl + "/localiza";
			
			request.setNifTitular((String) getPropertyAsunto(TITULAR_NIF));
			request.setAuthentication(getAuthentication());
			
			respuesta = APIHelper.proxyLemaPost(
					localizaCall, 
					request, 
					LocalizaResponse.class);
		
		} catch (SOAPFaultException ex) {
			throw new LemaPluginException(
					"[LEMA] Ha habido un problema localizando los envíos pendientes. " + ex.getMessage(),
					ex.getCause());
		} catch (Exception ex) {
			throw new LemaPluginException(
					"[LEMA] Ha habido un problema localizando los envíos pendientes. " + ex.getMessage(),
					ex.getCause());
		}
		return respuesta;

	}
	
	@Override
	public PeticionAccesoResponse peticionAcceso(PeticionAccesoRequest request) throws LemaPluginException {
		PeticionAccesoResponse respuesta = null;

		try {
			String proxyUrl = getPropertyRequired(PROPERTY_PROXY_URL);
			String peticionAccesoCall = proxyUrl + "/peticionAcceso";
			
			request.setNifReceptor((String) getPropertyAsunto(RECEPTOR_NIF));
			request.setAuthentication(getAuthentication());
			
			respuesta = APIHelper.proxyLemaPost(
					peticionAccesoCall, 
					request, 
					PeticionAccesoResponse.class);	

		} catch (SOAPFaultException ex) {
			throw new LemaPluginException("[LEMA] Ha habido un problema accediento al envío pendiente [identificador="
					+ request.getIdentificador() + "] - " + ex.getMessage(), ex.getCause());
		} catch (Exception ex) {
			throw new LemaPluginException("[LEMA] Ha habido un problema accediento al envío pendiente [identificador="
					+ request.getIdentificador() + "]" + ex.getMessage(), ex.getCause());
		}
		
		return respuesta;
	}

	@Override
	public ConsultaAcuseReciboResponse consultaAcuseRecibo(ConsultaAcuseReciboRequest request) {
		ConsultaAcuseReciboResponse respuesta = null;
		try {
			String proxyUrl = getPropertyRequired(PROPERTY_PROXY_URL);
			String consultaAcuseCall = proxyUrl + "/consultaAcuse";
			
			request.setNifReceptor((String) getPropertyAsunto(RECEPTOR_NIF));
			request.setAuthentication(getAuthentication());
			
			respuesta = APIHelper.proxyLemaPost(
					consultaAcuseCall, 
					request, 
					ConsultaAcuseReciboResponse.class);

		} catch (SOAPFaultException ex) {
			throw new LemaPluginException(
					"[LEMA] Ha habido un problema localizando el acuse de recibo del envío [identificador="
							+ request.getIdentificador() + "]" + ex.getMessage(), ex.getCause());
		} catch (Exception ex) {
			throw new LemaPluginException(
					"[LEMA] Ha habido un problema localizando el acuse de recibo del envío [identificador="
							+ request.getIdentificador() + "]" + ex.getMessage(), ex.getCause());
		}

		return respuesta;
	}

	@Override
	public ConsultaAnexoResponse consultaAnexo(ConsultaAnexoRequest request) {
		ConsultaAnexoResponse respuesta = null;
		try {
			String proxyUrl = getPropertyRequired(PROPERTY_PROXY_URL);
			String consultaAnexoCall = proxyUrl + "/consultaAnexo";
			
			request.setNifReceptor((String) getPropertyAsunto(RECEPTOR_NIF));
			request.setAuthentication(getAuthentication());
			
			respuesta = APIHelper.proxyLemaPost(
					consultaAnexoCall, 
					request, 
					ConsultaAnexoResponse.class);
			
		} catch (SOAPFaultException ex) {
			throw new LemaPluginException("[LEMA] Ha habido un problema localizando el anexo [referencia="
					+ request.getReferenciaAnexo() + "] del envío [identificador=" + request.getIdentificador() + "]" + ex.getMessage(), ex.getCause());
		} catch (Exception ex) {
			throw new LemaPluginException("[LEMA] Ha habido un problema localizando el anexo [referencia="
					+ request.getReferenciaAnexo() + "] del envío [identificador=" + request.getIdentificador() + "]" + ex.getMessage(), ex.getCause());
		}

		return respuesta;
	}

	private AuthenticationDto getAuthentication() throws Exception {
		AuthenticationDto auth = new AuthenticationDto();
		auth.setEndpointLocation(getPropertyRequired(PROPERTY_LOCALIZA_ENDPOINT));
		auth.setKeystoreFile(getPropertyRequired(KEYSTORE_FILE));
		auth.setKeystoreAlias(getPropertyRequired(KEYSTORE_ALIAS));
		auth.setKeystorePass(getPropertyRequired(KEYSTORE_PASS));
		auth.setKeystoreType(getPropertyRequired(KEYSTORE_TYPE));
		return auth;
	}
	
	@Override
	public String consultaPropietat(String propietat) {
		return (String) getProperty(basePluginLemaApb + propietat);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getPropertyAsunto(String atribut) {
		try {
			return (T) getPropertyRequired(atribut);
		} catch (Exception e) {
			throw new LemaPluginException("No s'ha definit la propietat '" + atribut + "'");
		}
	}

}
