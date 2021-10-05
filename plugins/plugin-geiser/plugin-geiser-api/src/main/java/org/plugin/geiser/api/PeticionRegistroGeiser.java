package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionRegistroGeiser extends Peticion {

	private String resumen;
	private TipoAsiento tipoAsiento;
	private String organoOrigen;
	private String organoDestino;
	private DocumentacionFisica documentacionFisica;
	private String cdAsunto;
	private String tipoAsunto;
	private String refExterna;
	private String nuExpediente;
	private TipoTransporte tipoTransporte;
	private String nuTransporte;
	private String observaciones;
	private String codigoSia;
	private List<InteresadoG> interesados;
	private List<AnexoG> anexos;
	
}
