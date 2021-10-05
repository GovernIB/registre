package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaBusquedaGeiser extends Respuesta {

	private int nuTotalApuntes;
	private int uidIterator;
	private List<ApunteRegistro> apuntes;
	
}
