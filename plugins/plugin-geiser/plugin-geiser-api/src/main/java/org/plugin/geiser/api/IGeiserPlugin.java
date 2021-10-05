package org.plugin.geiser.api;

import org.fundaciobit.pluginsib.core.IPlugin;

public interface IGeiserPlugin extends IPlugin {
	
	public static final String GEISER_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "geiser.";
	
	/**
	 * Operación que permite crear un asiento registral en GEISER con los datos recibidos en la petición.
	 * 
	 * @param peticionRegistroGeiser
	 * 			Una estructura datos de tipo {@link org.plugin.geiser.api.PeticionRegistroGeiser}
	 * @return Una estructura datos de tipo @RespuestaRegistroGeiser
	 * 
	 * @throws GeiserPluginException
	 */
	public RespuestaRegistroGeiser registrar(PeticionRegistroGeiser peticion) throws GeiserPluginException;
	
	/**
	 * Operación que permite crear un asiento registral en GEISER con los datos recibidos en la petición y hacer de forma automática un envío como si se hiciese desde la aplicación.
	 * 
	 * @param peticionRegistroEnvioGeiser
	 * 			Una estructura datos de tipo {@link org.plugin.geiser.api.PeticionRegistroEnvioGeiser}
	 * @return Una estructura datos de tipo @RespuestaRegistroGeiser
	 * 
	 * @throws GeiserPluginException
	 */
	public RespuestaRegistroGeiser registrarEnviar(PeticionRegistroEnvioGeiser peticion) throws GeiserPluginException;
	
	/**
	 * Operación que permite realizar la consulta de un asiento registral dado su número de registro.
	 * 
	 * @param peticionConsultaGeiser
	 * 			Una estructura datos de tipo {@link org.plugin.geiser.api.PeticionConsultaGeiser}
	 * @return Una estructura datos de tipo @RespuestaConsultaGeiser
	 * 
	 * @throws GeiserPluginException
	 */
	public RespuestaConsultaGeiser consulta(PeticionConsultaGeiser peticion) throws GeiserPluginException;
	
	/**
	 * Operación que permite realizar búsquedas de asientos registrales efectuados.
	 * 
	 * @param peticionBusquedaGeiser
	 * 			Una estructura datos de tipo {@link org.plugin.geiser.api.PeticionBusquedaGeiser}
	 * @return Una estructura datos de tipo @RespuestaBusquedaGeiser
	 * 
	 * @throws GeiserPluginException
	 */
	public RespuestaBusquedaGeiser buscar(PeticionBusquedaGeiser peticion) throws GeiserPluginException;
	
	/**
	 * Operación que permite consultar el estado de tramitación en el que se encuentran los asientos registrales.
	 * 
	 * @param peticionBusquedaTramitGeiser
	 * 			Una estructura datos de tipo {@link org.plugin.geiser.api.PeticionBusquedaTramitGeiser}
	 * @return Una estructura datos de tipo @RespuestaBusquedaTramitGeiser
	 * 
	 * @throws GeiserPluginException
	 */
	public RespuestaBusquedaTramitGeiser buscarEstadoTramitacion(PeticionBusquedaTramitGeiser peticion) throws GeiserPluginException;
	
}
