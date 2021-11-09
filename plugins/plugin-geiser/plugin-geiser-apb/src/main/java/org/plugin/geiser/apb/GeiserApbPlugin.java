package org.plugin.geiser.apb;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.plugin.geiser.api.GeiserPluginException;
import org.plugin.geiser.api.IGeiserPlugin;
import org.plugin.geiser.api.PeticionBusquedaGeiser;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroEnvioGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.RespuestaBusquedaGeiser;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.plugin.geiser.api.ws.AuthenticationType;
import org.plugin.geiser.api.ws.IRegistroWebService;
import org.plugin.geiser.api.ws.PeticionBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.PeticionBusquedaType;
import org.plugin.geiser.api.ws.PeticionConsultaType;
import org.plugin.geiser.api.ws.PeticionRegistroEnvioSimpleType;
import org.plugin.geiser.api.ws.PeticionRegistroType;
import org.plugin.geiser.api.ws.RegistroWebService;
import org.plugin.geiser.api.ws.ResultadoBusquedaEstadoTramitacionType;
import org.plugin.geiser.api.ws.ResultadoBusquedaType;
import org.plugin.geiser.api.ws.ResultadoConsultaType;
import org.plugin.geiser.api.ws.ResultadoRegistroType;
import org.plugin.geiser.api.ws.VersionRegeco;
import org.plugin.geiser.apb.helper.ConversionPluginHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeiserApbPlugin extends AbstractPluginProperties implements IGeiserPlugin {

	private static final String basePluginGeiserApb = GEISER_BASE_PROPERTY + "apb.";
    private static final String PROPERTY_URL = basePluginGeiserApb + "service.url";
    private static final String APP_CODE = basePluginGeiserApb + "app.code";
    private static final String APP_PASSWORD = basePluginGeiserApb + "app.password";
    private static final String CD_ASUNTO = basePluginGeiserApb + "codigo.asunto";
 
    
	public GeiserApbPlugin() {
        super();
    }
	
    public GeiserApbPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    public GeiserApbPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }
    
	@Override
	public RespuestaRegistroGeiser registrar(PeticionRegistroGeiser peticion) throws GeiserPluginException {
		ResultadoRegistroType resultado = new ResultadoRegistroType();
		try {
			AuthenticationType authentication = initAuthentication(
					peticion.getOficinaOrigen(), 
					peticion.getUsuario());
			peticion.setCdAsunto(getPropertyAsunto());
			PeticionRegistroType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionRegistroType.class);
			
			resultado = getRegecoClient().registrar(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de registro", ex);
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaRegistroGeiser.class);
	}

	@Override
	public RespuestaRegistroGeiser registrarEnviar(PeticionRegistroEnvioGeiser peticion) throws GeiserPluginException {
		ResultadoRegistroType resultado = new ResultadoRegistroType();
		try {
			AuthenticationType authentication = initAuthentication(
					peticion.getOficinaOrigen(), 
					peticion.getUsuario());
			peticion.setCdAsunto(getPropertyAsunto());
			PeticionRegistroEnvioSimpleType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionRegistroEnvioSimpleType.class);
			
			resultado = getRegecoClient().registrarEnviar(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de registro y envío", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaRegistroGeiser.class);
	}

	@Override
	public RespuestaConsultaGeiser consulta(PeticionConsultaGeiser peticion) throws GeiserPluginException {
		ResultadoConsultaType resultado = new ResultadoConsultaType();
		try {
			AuthenticationType authentication = initAuthentication(
					peticion.getOficinaOrigen(), 
					peticion.getUsuario());
			PeticionConsultaType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionConsultaType.class);
			
			resultado = getRegecoClient().consultar(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de consulta", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaConsultaGeiser.class);
	}

	@Override
	public RespuestaBusquedaGeiser buscar(PeticionBusquedaGeiser peticion) throws GeiserPluginException {
		ResultadoBusquedaType resultado = new ResultadoBusquedaType();
		try {
			AuthenticationType authentication = initAuthentication(
					peticion.getOficinaOrigen(), 
					peticion.getUsuario());
			PeticionBusquedaType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionBusquedaType.class);
			
			resultado = getRegecoClient().buscar(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de búsqueda", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaBusquedaGeiser.class);
	}

	@Override
	public RespuestaBusquedaTramitGeiser buscarEstadoTramitacion(PeticionBusquedaTramitGeiser peticion) throws GeiserPluginException {
		ResultadoBusquedaEstadoTramitacionType resultado = new ResultadoBusquedaEstadoTramitacionType();
		try {
			AuthenticationType authentication = initAuthentication(
					peticion.getOficinaOrigen(), 
					peticion.getUsuario());
			PeticionBusquedaEstadoTramitacionType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionBusquedaEstadoTramitacionType.class);
			
			resultado = getRegecoClient().buscarEstadoTramitacion(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("[GEISER] Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de búsqueda de estado de tramitación", ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaBusquedaTramitGeiser.class);
	}
	
	private AuthenticationType initAuthentication(String ambito, String usuario) throws Exception {
		AuthenticationType authentication = new AuthenticationType();
		authentication.setAplicacion(getPropertyCode());
		authentication.setPassword(getPropertyPassword());
		authentication.setUsuario(usuario);
		authentication.setCdAmbito(ambito); 
		authentication.setVersion(VersionRegeco.V_2);
		return authentication;
	}
	
	private String getPropertyUrl() {
		try {
    		return getPropertyRequired(PROPERTY_URL);
    	} catch (Exception e) {
			throw new GeiserPluginException("No s'ha definit la propietat '" + PROPERTY_URL + "'");
		}
    }
	
	private String getPropertyCode() {
		try {
    		return getPropertyRequired(APP_CODE);
    	} catch (Exception e) {
			throw new GeiserPluginException("No s'ha definit la propietat '" + APP_CODE + "'");
		}
    }

    private String getPropertyPassword() {
    	try {
    		return getPropertyRequired(APP_PASSWORD);
    	} catch (Exception e) {
			throw new GeiserPluginException("No s'ha definit la propietat '" + APP_PASSWORD + "'");
		}
    }

    private String getPropertyAsunto() {
    	try {
    		return getPropertyRequired(CD_ASUNTO);
    	} catch (Exception e) {
			throw new GeiserPluginException("No s'ha definit la propietat '" + CD_ASUNTO + "'");
		}
    }
    
	private IRegistroWebService getRegecoClient() throws Exception {
		URL url;
		IRegistroWebService registroWebService = null;
		try {
			url = new URL(getPropertyUrl() + "?wsdl");
			RegistroWebService service = new RegistroWebService(url);
			service.setHandlerResolver(new HandlerResolver() {

		        @SuppressWarnings("rawtypes")
				@Override
		        public List<Handler> getHandlerChain(PortInfo portInfo) {
		            List<Handler> handlerList = new ArrayList<>();
		            handlerList.add(new Handler1());
		            return handlerList;
		        }
		    });
			
			registroWebService = service.getRegistroWebServicePort();
		} catch (Exception ex) {
			logger.error("Ha habido un error creando el cliente de registro", ex.getMessage());
			ex.printStackTrace();
			throw ex;
 		}
		return registroWebService;
	}
	
	public class Handler1 implements SOAPHandler<SOAPMessageContext> {
		public Set<QName> getHeaders() {
			return Collections.emptySet();
		}

		public boolean handleMessage(SOAPMessageContext messageContext) {
			Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

			if (outboundProperty.booleanValue()) {
				System.out.println("\nxml petición:");
			} else {
				System.out.println("\nxml respuesta:");
			}
			// Debug 
			StringBuilder sb = new StringBuilder();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				messageContext.getMessage().writeTo(baos);
				sb.append(baos.toString());
			} catch (Exception ex) {
				sb.append("Error al processar el missatge XML: " + ex.getMessage());
			}
			logger.info(sb.toString());
			return true;
		}

		public boolean handleFault(SOAPMessageContext messageContext) {
			return true;
		}

		public void close(MessageContext messageContext) {
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(GeiserApbPlugin.class);
}
