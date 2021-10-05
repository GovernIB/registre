package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaConsultaGeiser extends Respuesta {

	private List<ApunteRegistro> apuntes;
	
}
