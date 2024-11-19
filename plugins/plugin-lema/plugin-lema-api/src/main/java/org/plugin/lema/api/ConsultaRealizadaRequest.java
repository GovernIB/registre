package org.plugin.lema.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConsultaRealizadaRequest {

	    protected String identificador;
	    protected Integer codigoOrigen;
	    protected String nifPeticion;
	    protected String nombrePeticion;
	    protected String concepto;
	    protected List<Opcion> opcionesConsultaRealizadas;
	    
}
