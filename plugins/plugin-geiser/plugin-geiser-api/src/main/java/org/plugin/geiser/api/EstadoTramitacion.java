package org.plugin.geiser.api;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class EstadoTramitacion {

	private TipoAsiento tipoAsiento;
	private String nuRegistro;
	private String nuRegistroOrigen;
	private String nuRegistroInterno;
	private Date fechaPresentacion;
	private Date fechaRegistro;
	private EstadoTramit estado;
	private Date fechaEstado;
	private List<String> identificadorIntercambioSIR;
	
}
