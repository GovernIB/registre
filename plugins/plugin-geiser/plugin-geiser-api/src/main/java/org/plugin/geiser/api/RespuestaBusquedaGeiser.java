package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaBusquedaGeiser {

	private Respuesta respuesta;
	private int nuTotalApuntes;
	private String uidIterator;
	private List<ApunteRegistro> apuntes;
	private String oficinaDestino;
	
}
