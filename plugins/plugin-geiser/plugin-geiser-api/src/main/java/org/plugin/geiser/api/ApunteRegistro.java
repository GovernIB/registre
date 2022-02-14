package org.plugin.geiser.api;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApunteRegistro {

	private String nuRegistro;
	private String nuRegistroOrigen;
	private Date fechaRegistro;
	private String timeStampRegistro;
	private TipoAsiento tipoAsiento;
	private String resumen;
	private EstadoRegistro estado;
	private DocumentacionFisica documentacionFisica;
	private String cdAmbitoOrigen;
	private String nombreAmbitoOrigen;
	private String organoOrigen;
	private String organoOrigenDenominacion;
	private String cdAmbitoActual;
	private String nombreAmbitoActual;
	private String cdAmbitoDestino;
	private String nombreAmbitoDestino;
	private String organoDestino;
	private String organoDestinoDenominacion;
	
	private String cdAsunto;
	private String referenciaExterna;
	private String nuExpediente;
	private String nuTransporte;
	private String nombreUsuario;
	private String contactoUsuario;
	
	private TipoTransporte tipoTransporte;
	private String expone;
	private String solicita;
	
	private String observaciones;
	private List<AnexoG> anexos;
	private List<InteresadoG> interesados;
	
}
