package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaConsultaGeiser {

	private Respuesta respuesta;
	private List<ApunteRegistro> apuntes;
	
}
