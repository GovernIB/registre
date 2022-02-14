package es.caib.regweb3.persistence.utils;


import java.util.List;

import javax.ejb.EJB;
import javax.interceptor.Interceptors;

import org.fundaciobit.genapp.common.i18n.I18NException;
import org.plugin.geiser.api.GeiserPluginException;
import org.plugin.geiser.api.IGeiserPlugin;
import org.plugin.geiser.api.PeticionBusquedaTramitGeiser;
import org.plugin.geiser.api.PeticionConsultaGeiser;
import org.plugin.geiser.api.PeticionRegistroEnvioGeiser;
import org.plugin.geiser.api.PeticionRegistroGeiser;
import org.plugin.geiser.api.RespuestaBusquedaGeiser;
import org.plugin.geiser.api.RespuestaBusquedaTramitGeiser;
import org.plugin.geiser.api.RespuestaConsultaGeiser;
import org.plugin.geiser.api.RespuestaRegistroGeiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import org.springframework.stereotype.Component;

import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.utils.RegwebConstantes;

@Component
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class GeiserPluginHelper {

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;
    @Autowired ConversionHelper conversioHelper;
    

    public RespuestaRegistroGeiser postProcesoNuevoRegistroGeiser(IRegistro r, UsuarioEntidad usuarioEntidad) throws GeiserPluginException, I18NException {
    	RespuestaRegistroGeiser respuesta = null;
    	IGeiserPlugin geiserPlugin = (IGeiserPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_GEISER);
        if (geiserPlugin != null) {
        	if (r instanceof RegistroEntrada) {
        		synchronized (Semaforo.class) {
		        	respuesta = geiserPlugin.registrar(
		        			conversioHelper.convertir(
		        					(RegistroEntrada)r, 
		        					PeticionRegistroGeiser.class));
        		}
        	} else {
        		synchronized (Semaforo.class) {
	        		respuesta = geiserPlugin.registrar(
	            			conversioHelper.convertir(
	            					(RegistroSalida)r, 
	            					PeticionRegistroGeiser.class));
        		}
        	}
        }
		return respuesta;
    }
    
    public RespuestaRegistroGeiser postProcesoNuevoRegistroSirGeiser(RegistroSir rsir, Long entidadId) throws GeiserPluginException, I18NException {
    	RespuestaRegistroGeiser respuesta = null;
    	IGeiserPlugin geiserPlugin = getIGeiserPlugin(entidadId);
        if (geiserPlugin != null) {
        	synchronized (Semaforo.class) {
            	respuesta = geiserPlugin.registrarEnviar(
            			conversioHelper.convertir(
            					rsir, 
            					PeticionRegistroEnvioGeiser.class));
			}
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
    
    private IGeiserPlugin getIGeiserPlugin(Long entidadId) throws I18NException {
    	return (IGeiserPlugin) pluginEjb.getPlugin(entidadId, RegwebConstantes.PLUGIN_GEISER);
    }

}
