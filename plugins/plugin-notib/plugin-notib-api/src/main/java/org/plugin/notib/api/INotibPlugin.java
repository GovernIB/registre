package org.plugin.notib.api;

import org.fundaciobit.pluginsib.core.IPlugin;

import es.caib.notib.client.domini.NotificacioV2;
import es.caib.notib.client.domini.RespostaAlta;
import es.caib.notib.client.domini.RespostaConsultaEstatEnviamentV2;
import es.caib.notib.client.domini.RespostaConsultaEstatNotificacioV2;

public interface INotibPlugin extends IPlugin {
	
	public static final String NOTIB_BASE_PROPERTY = IPLUGIN_BASE_PROPERTIES + "notib.";
	
	public RespostaAlta altaNotificacion(NotificacioV2 request);

	public String getUsuarioIntegracionNotib();

	public RespostaConsultaEstatNotificacioV2 consultarNotificacion(String identificador);

	public RespostaConsultaEstatEnviamentV2 consultarEnvio(String referenciaEnviament);

	String consultarPropiedad(String propiedad);
	
}
