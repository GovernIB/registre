package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Respuesta {
	
	private int codigo;
	private TipoRespuesta tipoRespuesta;
	private String mensaje;
	
}
