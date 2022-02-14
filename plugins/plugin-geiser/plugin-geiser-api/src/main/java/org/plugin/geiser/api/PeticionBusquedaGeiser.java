package org.plugin.geiser.api;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionBusquedaGeiser extends Peticion {

	private String fechaRegistroInicio;
	private String fechaRegistroFinal;
	private EstadoRegistro estado;
	private TipoAsiento tipoAsiento;
	private String cdOrganoDestino;
	
}
