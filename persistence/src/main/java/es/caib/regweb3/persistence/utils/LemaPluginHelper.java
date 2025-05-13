package es.caib.regweb3.persistence.utils;


import javax.ejb.EJB;
import javax.interceptor.Interceptors;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.lema.api.ConsultaAcuseReciboRequest;
import org.plugin.lema.api.ConsultaAcuseReciboResponse;
import org.plugin.lema.api.ConsultaAnexoRequest;
import org.plugin.lema.api.ConsultaAnexoResponse;
import org.plugin.lema.api.ConsultaRealizadaRequest;
import org.plugin.lema.api.ConsultaRealizadaResponse;
import org.plugin.lema.api.ILemaPlugin;
import org.plugin.lema.api.LemaPluginException;
import org.plugin.lema.api.LocalizaRequest;
import org.plugin.lema.api.LocalizaResponse;
import org.plugin.lema.api.PeticionAccesoRequest;
import org.plugin.lema.api.PeticionAccesoResponse;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

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
public class LemaPluginHelper {

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    public LocalizaResponse localizaPendientes(LocalizaRequest request, Entidad entidad) throws LemaPluginException, I18NException {
    	LocalizaResponse respuesta = null;
    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
        if (lemaPlugin != null) {
        	synchronized (Semaforo.class) {
	        	respuesta = lemaPlugin.localiza(request);
    		}
        }
		return respuesta;
    }

//    public LocalizaRealizadaResponse localizaRealizadas(LocalizaRealizadasRequest request, Entidad entidad) throws LemaPluginException, I18NException {
//    	LocalizaRealizadaResponse respuesta = null;
//    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
//        if (lemaPlugin != null) {
//        	synchronized (Semaforo.class) {
//	        	respuesta = lemaPlugin.localizaRealizadas(request);
//    		}
//        }
//		return respuesta;
//    }
    
    public ConsultaRealizadaResponse consultaRealizada(ConsultaRealizadaRequest request, Entidad entidad) throws LemaPluginException, I18NException {
    	ConsultaRealizadaResponse respuesta = null;
    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
        if (lemaPlugin != null) {
	        respuesta = lemaPlugin.consultaRealizada(request);
        }
		return respuesta;
    }
    
    
    public PeticionAccesoResponse peticionAcceso(PeticionAccesoRequest request, Entidad entidad) throws LemaPluginException, I18NException {
    	PeticionAccesoResponse respuesta = null;
    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
        if (lemaPlugin != null) {
	        respuesta = lemaPlugin.peticionAcceso(request);
        }
		return respuesta;
    }

	public ConsultaAcuseReciboResponse consultaAcuseRecibo(ConsultaAcuseReciboRequest request, Entidad entidad) throws I18NException {
		ConsultaAcuseReciboResponse respuesta = null;
    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
        if (lemaPlugin != null) {
	        respuesta = lemaPlugin.consultaAcuseRecibo(request);
        }
		return respuesta;
	}
	
	public ConsultaAnexoResponse consultaAnexo(ConsultaAnexoRequest request, Entidad entidad) throws I18NException {
		ConsultaAnexoResponse respuesta = null;
    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
        if (lemaPlugin != null) {
	        respuesta = lemaPlugin.consultaAnexo(request);
        }
		return respuesta;
	}

	public String getCorreosAviso(String propietat, Entidad entidad) throws I18NException {
		String respuesta = null;
    	ILemaPlugin lemaPlugin = getILemaPlugin(entidad.getId());
        if (lemaPlugin != null) {
	        respuesta = lemaPlugin.consultaPropietat(propietat);
        }
		return respuesta;
	}
	
    private ILemaPlugin getILemaPlugin(Long entidadId) throws I18NException {
    	return (ILemaPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_LEMA);
    }

}
