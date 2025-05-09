package org.plugin.notib.apb;

import java.util.Properties;

import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.plugin.notib.apb.helper.APIHelper;
import org.plugin.notib.api.INotibPlugin;
import org.plugin.notib.api.NotibPluginException;

import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.notib.client.domini.RespostaConsultaEstatEnviamentV2;
import es.caib.notib.client.domini.RespostaConsultaEstatNotificacioV2;

public class NotibApbPlugin extends AbstractPluginProperties implements INotibPlugin {

	private APIHelper api = null;

	private static final String basePluginNotibApb = NOTIB_BASE_PROPERTY + "apb.";
	
	private static final String URL = basePluginNotibApb + "endpoint";
	private static final String USUARIO  = basePluginNotibApb + "username";
	private static final String CONTRASENYA = basePluginNotibApb + "password";
	
	private static final String RETARDO = basePluginNotibApb + "retard.num.dies";
	private static final String FORZAR_ENTIDAD = basePluginNotibApb + "forsar.entitat";
	private static final String CADUCIDAD = basePluginNotibApb + "caducitat.num.dies";
	
	public NotibApbPlugin() {
        super();
    }
	
    public NotibApbPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    public NotibApbPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }
    
	@Override
	public RespostaAlta altaNotificacion(NotificacioV2 request) {
		RespostaAlta respuesta = null;
		try {
			String altaUrl = getProperty(URL) + "/notificacio/v2/alta";
			String username = getProperty(USUARIO);
			String password = getProperty(CONTRASENYA);
			String retardo = getProperty(RETARDO);
			String forzarEntidad = getProperty(FORZAR_ENTIDAD);
			String caducidad = getProperty(CADUCIDAD);
			
			if (api == null)
				api = new APIHelper(
						altaUrl, 
						username, 
						password);
			
			if ((request.getRetard() == null || request.getRetard() == 0) && retardo != null)
				request.setRetard(Integer.valueOf(retardo));
			
			if (caducidad != null && ! caducidad.isEmpty() && request.getCaducitatDiesNaturals() == null) 
				request.setCaducitatDiesNaturals(Integer.valueOf(caducidad));
			
			if (forzarEntidad != null && ! forzarEntidad.isEmpty()) {
				request.setEmisorDir3Codi(forzarEntidad);
				request.setOrganGestor(forzarEntidad);
			}
			respuesta = api.notibPost(
					request, 
					RespostaAlta.class);
			
//			if (respuesta.isError())
//				throw new NotibPluginException(respuesta.getErrorDescripcio());
			
		} catch (Exception ex) {
			throw new NotibPluginException("[NOTIB] Ha habido un problema realizando el proceso de alta. " + ex.getMessage(), ex.getCause());
		}
		return respuesta;
	}

	@Override
	public RespostaConsultaEstatNotificacioV2 consultarNotificacion(String identificador) {
		RespostaConsultaEstatNotificacioV2 respuesta = null;
		try {
			String altaUrl = getProperty(URL) + "/notificacio/v2/consultaEstatNotificacio";
			String username = getProperty(USUARIO);
			String password = getProperty(CONTRASENYA);
			
			if (api == null)
				api = new APIHelper(
						altaUrl, 
						username, 
						password);
			
			respuesta = api.notibGet(
					identificador, 
					RespostaConsultaEstatNotificacioV2.class);
			
//			if (respuesta.isError())
//				throw new NotibPluginException(respuesta.getErrorDescripcio());
			
		} catch (Exception ex) {
			throw new NotibPluginException("[NOTIB] Ha habido un problema consultando la notificación + " + identificador + ". " + ex.getMessage(), ex.getCause());
		}
		return respuesta;
	}

	@Override
	public RespostaConsultaEstatEnviamentV2 consultarEnvio(String referenciaEnviament) {
		RespostaConsultaEstatEnviamentV2 respuesta = null;
		try {
			String altaUrl = getProperty(URL) + "/notificacio/v2/consultaEstatEnviament";
			String username = getProperty(USUARIO);
			String password = getProperty(CONTRASENYA);
			
			if (api == null)
				api = new APIHelper(
						altaUrl, 
						username, 
						password);
			
			respuesta = api.notibGet(
					referenciaEnviament, 
					RespostaConsultaEstatEnviamentV2.class);
			
//			if (respuesta.isError())
//				throw new NotibPluginException(respuesta.getErrorDescripcio());
			
		} catch (Exception ex) {
			throw new NotibPluginException("[NOTIB] Ha habido un problema consultando el envío + " + referenciaEnviament + ". " + ex.getMessage(), ex.getCause());
		}
		return respuesta;
	}

	@Override
	public String consultarPropiedad(String propiedad) {
		return getProperty(basePluginNotibApb + propiedad);
	}
	
	@Override
	public String getUsuarioIntegracionNotib() {
		return getProperty(USUARIO);
	}

}
