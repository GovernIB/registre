package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionAccesoRequest {

	private String identificador;
	private String concepto;
	private String nifReceptor;
	private String nombreReceptor;
	private Integer codigoOrigen;
	
}
