package es.caib.regweb3.persistence.utils;


import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.geiser.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;
import java.util.List;

@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GeiserPluginHelper {

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;
    @Autowired ConversionHelper conversioHelper;
    

    public RespuestaRegistroGeiser postProcesoNuevoRegistroGeiser(IRegistro r, Long entidadId, boolean forzarExcepcion) throws GeiserPluginException, I18NException {
    	RespuestaRegistroGeiser respuesta = null;
    	IGeiserPlugin geiserPlugin = (IGeiserPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_GEISER);
        if (geiserPlugin != null) {
        	if (r instanceof RegistroEntrada) {
        		// Comentado para evitar posibles bloqueos al registrar dede NOTIB
        		//synchronized (Semaforo.class) {
		        	respuesta = geiserPlugin.registrar(
		        			conversioHelper.convertir(
		        					(RegistroEntrada)r, 
		        					PeticionRegistroGeiser.class),
		        			forzarExcepcion);
        		//}
        	} else {
        		//synchronized (Semaforo.class) {
	        		respuesta = geiserPlugin.registrar(
	            			conversioHelper.convertir(
	            					(RegistroSalida)r, 
	            					PeticionRegistroGeiser.class),
	            			forzarExcepcion);
        		//}
        	}
        }
		return respuesta;
    }
    
    public RespuestaRegistroGeiser postProcesoNuevoRegistroSirGeiser(RegistroSir rsir, Long entidadId) throws GeiserPluginException, I18NException {
    	RespuestaRegistroGeiser respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
			// Comentado para evitar posibles bloqueos al registrar dede NOTIB
        	//synchronized (Semaforo.class) {
            	respuesta = geiserPlugin.registrarEnviar(
            			conversioHelper.convertir(
            					rsir, 
            					PeticionRegistroEnvioGeiser.class));
			//}
        }
		return respuesta;
    }
    
    public RespuestaConsultaGeiser postProcesoConsultarRegistroSirGeiser(RegistroSir rsir, Long entidadId) throws I18NException {
    	RespuestaConsultaGeiser respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
        	respuesta = geiserPlugin.consulta(
        			conversioHelper.convertir(
        					rsir, 
        					PeticionConsultaGeiser.class));
        }
		return respuesta;
    }
    
    public RespuestaBusquedaTramitGeiser postProcesoBuscarEstadoTRegistroSirGeiser(PeticionBusquedaTramitGeiser peticion, RegistroSir rsir, Long entidadId) throws I18NException {
    	RespuestaBusquedaTramitGeiser respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
        	if (peticion != null) {
        		respuesta = geiserPlugin.buscarEstadoTramitacion(peticion);
        	} else {
	        	respuesta = geiserPlugin.buscarEstadoTramitacion(
	        			conversioHelper.convertir(
	        					rsir, 
	        					PeticionBusquedaTramitGeiser.class));
        	}
        }
		return respuesta;
    }
    
    public List<RespuestaBusquedaGeiser> postProcesoBusquedaRegistroSirGeiser(String fechaInicio, String fechaFin,  Long entidadId) throws I18NException {
    	List<RespuestaBusquedaGeiser> respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
        	respuesta = geiserPlugin.buscar(fechaInicio, fechaFin);
        }
		return respuesta;
    }
    
    public String getUsuarioResponsableCreacionRegistros(Long entidadId) throws I18NException {
    	String respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
        	respuesta = geiserPlugin.getUsuariCreacioRegistres();
        }
		return respuesta;
    }
    
    public AnexoGSample postProcesoObtenerJustificanteGEISER(PeticionConsultaGeiser consulta, Long entidadId) throws I18NException {
    	AnexoGSample respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
        	respuesta = geiserPlugin.obtenerJustificanteGEISER(consulta);
        }
		return respuesta;
    }
    
    private IGeiserPlugin getIGeiserPlugin(Long entidadId) throws I18NException {
    	return (IGeiserPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_GEISER);
    }

}
