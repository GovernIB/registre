package org.plugin.lema.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConsultaAcuseReciboResponse {
	
	protected String codigoRespuesta;
	protected String descripcionRespuesta;
	protected AcuseRecibo acuseRecibo;
	protected List<Opcion> opcionesRespuestaConsultaAcuseRecibo;
	
}
