package org.plugin.lema.api;

import org.fundaciobit.pluginsib.core.IPlugin;

public interface ILemaPlugin extends IPlugin {

	public static final String LEMA_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "lema.";

	public LocalizaResponse localiza(LocalizaRequest request) throws LemaPluginException;

//	public LocalizaRealizadaResponse localizaRealizadas(LocalizaRealizadasRequest request);
	
	public PeticionAccesoResponse peticionAcceso(PeticionAccesoRequest request) throws LemaPluginException;

//	public ConsultaRealizadaResponse consultaRealizadas(ConsultaRealizadaRequest request);
	
	public ConsultaAcuseReciboResponse consultaAcuseRecibo(ConsultaAcuseReciboRequest request);

	public ConsultaAnexoResponse consultaAnexo(ConsultaAnexoRequest request);

	public String consultaPropietat(String propietat);
	
}
