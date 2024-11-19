package org.plugin.lema.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocalizaResponse {

	private String codigoRespuesta;
	private String descripcionRespuesta;
	private List<Envio> envios;
	
}
