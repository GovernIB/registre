package org.plugin.lema.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConsultaAnexoResponse {
	
	protected String codigoRespuesta;
	protected String descripcionRespuesta;
	protected DocumentoAnexo documentoAnexo;
	protected List<Opcion> opcionesRespuestaConsultaAcuseRecibo;
	
}
