package org.plugin.lema.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocalizaRealizadaResponse {

	private String codigoRespuesta;
	private String descripcionRespuesta;
	private List<EnvioRealizada> envios;
	
}
