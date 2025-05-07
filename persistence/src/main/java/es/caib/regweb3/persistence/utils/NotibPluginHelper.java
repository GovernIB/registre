package es.caib.regweb3.persistence.utils;


import javax.ejb.EJB;
import javax.interceptor.Interceptors;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.notib.api.INotibPlugin;
import org.plugin.notib.api.NotibPluginException;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.notib.client.domini.RespostaConsultaEstatEnviamentV2;
import es.caib.notib.client.domini.RespostaConsultaEstatNotificacioV2;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * Created by Limit Tecnologies S.L.
 * 
 * @author Jamal
 */
@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class NotibPluginHelper {

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    public RespostaAlta altaNotificacion(NotificacioV2 request, Entidad entidad) throws NotibPluginException, I18NException {
    	RespostaAlta respuesta = null;
    	INotibPlugin notibPlugin = getINotibPlugin(entidad.getId());
        if (notibPlugin != null) {
	        respuesta = notibPlugin.altaNotificacion(request);
        }
		return respuesta;
    }
    
    public String getUsuarioIntegracionNotib(Long entidadId) throws I18NException {
    	String respuesta = null;
    	INotibPlugin notibPlugin = getINotibPlugin(entidadId);
        if (notibPlugin != null) {
        	respuesta = notibPlugin.getUsuarioIntegracionNotib();
        }
		return respuesta;
    }
	public RespostaConsultaEstatEnviamentV2 consultarEnvio(String referenciaEnviament, Long entidadId) throws I18NException {
		RespostaConsultaEstatEnviamentV2 respuesta = null;
    	INotibPlugin notibPlugin = getINotibPlugin(entidadId);
        if (notibPlugin != null) {
	        respuesta = notibPlugin.consultarEnvio(referenciaEnviament);
        }
		return respuesta;
	}
	
	public RespostaConsultaEstatNotificacioV2 consultarNotificacion(String identificador, Long entidadId) throws I18NException {
		RespostaConsultaEstatNotificacioV2  respuesta = null;
    	INotibPlugin notibPlugin = getINotibPlugin(entidadId);
        if (notibPlugin != null) {
	        respuesta = notibPlugin.consultarNotificacion(identificador);
        }
		return respuesta;
	}

    private INotibPlugin getINotibPlugin(Long entidadId) throws I18NException {
    	return (INotibPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_NOTIB);
    }


}
