package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaBusquedaGeiser {

	private Respuesta respuesta;
	private int nuTotalApuntes;
	private int uidIterator;
	private List<ApunteRegistro> apuntes;
	
}
