package org.plugin.geiser.api;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaRegistroGeiser {

	private Respuesta respuesta;
	private String nuRegistro;
	private Date fechaRegistro;
	
}
