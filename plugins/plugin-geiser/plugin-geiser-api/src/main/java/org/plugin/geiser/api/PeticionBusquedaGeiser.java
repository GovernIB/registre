package org.plugin.geiser.api;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionBusquedaGeiser extends Peticion {

	private Date fechaRegistroInicio;
	private Date fechaRegistroFinal;
	
}
