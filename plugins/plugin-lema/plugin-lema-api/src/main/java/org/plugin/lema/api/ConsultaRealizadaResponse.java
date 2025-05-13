package org.plugin.lema.api;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConsultaRealizadaResponse {
	
	private String codigoRespuesta;
	private String descripcionRespuesta;
	private String identificador;
	private String codigoOrigen;
	private boolean postal;
	private Sia codigoProcedimiento;
	private XMLGregorianCalendar fechaUltimoEstado;
	private DetalleDocumento documento;
	private List<Opcion> opcionesRespuestaConsultaRealizadas;
	private Anexos anexos;
}
