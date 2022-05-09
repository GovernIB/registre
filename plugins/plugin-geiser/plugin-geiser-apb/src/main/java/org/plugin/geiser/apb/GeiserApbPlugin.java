package org.plugin.geiser.apb;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.codec.binary.Base64;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.plugin.geiser.apb.helper.ConversionPluginHelper;
import org.plugin.geiser.api.AnexoGSample;
import org.plugin.geiser.api.EstadoRegistro;
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
import org.plugin.geiser.api.TipoAsiento;
import org.plugin.geiser.api.ws.ApunteRegistroType;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeiserApbPlugin extends AbstractPluginProperties implements IGeiserPlugin {

	private static final String basePluginGeiserApb = GEISER_BASE_PROPERTY + "apb.";
    private static final String PROPERTY_URL = basePluginGeiserApb + "service.url";
    private static final String APP_CODE = basePluginGeiserApb + "app.code";
    private static final String APP_PASSWORD = basePluginGeiserApb + "app.password";
    private static final String CD_ASUNTO = basePluginGeiserApb + "codigo.asunto";
    private static final String CD_OFICINAS = basePluginGeiserApb + "oficinas";
    private static final String USUARIO_GEISER = basePluginGeiserApb + "usuario.geiser";
    private static final String USUARIO_REGISTRE_ENTRADA = basePluginGeiserApb + "usuario.responsable.registros";
    private static final String CD_AMBITO = basePluginGeiserApb + "codigo.ambito";
    
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
					getPropertyCdAmbito(), 
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
				throw new GeiserPluginException("Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de registro. " + ex.getMessage(), ex.getCause());
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
					getPropertyCdAmbito(), 
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
				throw new GeiserPluginException("Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de registro y envío. " + ex.getMessage(), ex.getCause());
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
					getPropertyCdAmbito(), 
					peticion.getUsuario());
			PeticionConsultaType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionConsultaType.class);
			
			resultado = getRegecoClient().consultar(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de consulta. " + ex.getMessage(), ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaConsultaGeiser.class);
	}

	@Override
	public List<RespuestaBusquedaGeiser> buscar(String fechaInicio, String fechaFin) throws GeiserPluginException {
		List<RespuestaBusquedaGeiser> resultadosBusqueda = new ArrayList<RespuestaBusquedaGeiser>();
		try {
			String oficinasBusqueda = getProperty(CD_OFICINAS);
			if (oficinasBusqueda != null && !oficinasBusqueda.isEmpty()) {
				List<String> oficinas = Arrays.asList(oficinasBusqueda.split(","));
				for (String cdAmbido: oficinas) {
					AuthenticationType authentication = initAuthentication(
							cdAmbido, 
							null);
					PeticionBusquedaGeiser peticion = new PeticionBusquedaGeiser();
					
		        	peticion.setFechaRegistroInicio(fechaInicio);
		        	peticion.setFechaRegistroFinal(fechaFin);
		        	peticion.setEstado(EstadoRegistro.ENVIADO_PENDIENTE_CONFIRMACION);
		        	peticion.setTipoAsiento(TipoAsiento.ENTRADA);
		        	
					PeticionBusquedaType peticionType = new ConversionPluginHelper().convertir(
							peticion, 
							PeticionBusquedaType.class);
					
					ResultadoBusquedaType resultado = getRegecoClient().buscar(
							authentication, 
							peticionType);
					int codigoRespuesta = resultado.getRespuesta().getCodigo();
					if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
						throw new GeiserPluginException("Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
					
					RespuestaBusquedaGeiser resultadoConverted = new ConversionPluginHelper().convertir(
							resultado, 
							RespuestaBusquedaGeiser.class);
					resultadoConverted.setOficinaDestino(cdAmbido);
					resultadosBusqueda.add(resultadoConverted);
				}
			} else {
				throw new GeiserPluginException("No se ha indicado ninguna oficina para realizar una búsqueda de registros");
			}
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de búsqueda. " + ex.getMessage(), ex.getCause());
		}
		return resultadosBusqueda;
	}

	@Override
	public RespuestaBusquedaTramitGeiser buscarEstadoTramitacion(PeticionBusquedaTramitGeiser peticion) throws GeiserPluginException {
		ResultadoBusquedaEstadoTramitacionType resultado = new ResultadoBusquedaEstadoTramitacionType();
		try {
			AuthenticationType authentication = initAuthentication(
					getPropertyCdAmbito(), 
					peticion.getUsuario());
			PeticionBusquedaEstadoTramitacionType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionBusquedaEstadoTramitacionType.class);
			
			resultado = getRegecoClient().buscarEstadoTramitacion(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de búsqueda de estado de tramitación. " + ex.getMessage(), ex.getCause());
		}
		return new ConversionPluginHelper().convertir(
				resultado, 
				RespuestaBusquedaTramitGeiser.class);
	}

	@Override
	public String getUsuariCreacioRegistres() throws GeiserPluginException {
		return getProperty(USUARIO_REGISTRE_ENTRADA);
	}
	
	@Override
	public AnexoGSample obtenerJustificanteGEISER(PeticionConsultaGeiser peticion) throws GeiserPluginException {
		AnexoGSample justificante = new AnexoGSample();
		try {
			AuthenticationType authentication = initAuthentication(
					getPropertyCdAmbito(), 
					peticion.getUsuario());
			PeticionConsultaType peticionType = new ConversionPluginHelper().convertir(
					peticion, 
					PeticionConsultaType.class);
			
			ResultadoConsultaType resultado = getRegecoClient().consultar(
					authentication, 
					peticionType);
			int codigoRespuesta = resultado.getRespuesta().getCodigo();
			if (codigoRespuesta == 1 || codigoRespuesta == 2 || codigoRespuesta == 3 ||codigoRespuesta == 4 ||codigoRespuesta == 5)
				throw new GeiserPluginException("Respuesta: " + codigoRespuesta + " - " + resultado.getRespuesta().getMensaje());
			
			for (ApunteRegistroType apunte: resultado.getApuntes()) {
				if (apunte.getJustificanteFirmado() != null) {
					justificante.setAnexo(Base64.decodeBase64(apunte.getJustificanteFirmado()));
					justificante.setMime(apunte.getTipoMimeJustificanteCVS());
					justificante.setTitulo("Justificante_" + apunte.getNuRegistro() + ".pdf");
				}
			}
		} catch (Exception ex) {
			throw new GeiserPluginException("[GEISER] Ha habido un problema realizando el proceso de consulta. " + ex.getMessage(), ex.getCause());
		}
		return justificante;
	}
	
	private AuthenticationType initAuthentication(String ambito, String usuario) throws Exception {
		AuthenticationType authentication = new AuthenticationType();
		authentication.setAplicacion(getPropertyCode());
		authentication.setPassword(getPropertyPassword());
		authentication.setUsuario((usuario != null && !usuario.isEmpty()) ? usuario : getProperty(USUARIO_GEISER));
//		authentication.setCdAmbito((ambito != null && !ambito.isEmpty()) ? ambito : getProperty(CD_OFICINA)); 
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
    
    private String getPropertyCdAmbito() {
    	try {
    		return getPropertyRequired(CD_AMBITO);
    	} catch (Exception e) {
			throw new GeiserPluginException("No s'ha definit la propietat '" + CD_AMBITO + "'");
		}
    }
    
	private IRegistroWebService getRegecoClient() throws Exception {
		URL url;
		IRegistroWebService port = null;
		try {
			url = new URL(getPropertyUrl() + "?wsdl");
			RegistroWebService service = new RegistroWebService(url);
			port = service.getRegistroWebServicePort();
			BindingProvider bp = (BindingProvider)port;
			bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, getPropertyUrl());
			
			service.setHandlerResolver(new HandlerResolver() {

		        @SuppressWarnings("rawtypes")
				@Override
		        public List<Handler> getHandlerChain(PortInfo portInfo) {
		            List<Handler> handlerList = new ArrayList<>();
		            handlerList.add(new Handler1());
		            return handlerList;
		        }
		    });
		} catch (Exception ex) {
			logger.error("Ha habido un error creando el cliente de registro", ex.getMessage());
			ex.printStackTrace();
			throw ex;
 		}
		return port;
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
