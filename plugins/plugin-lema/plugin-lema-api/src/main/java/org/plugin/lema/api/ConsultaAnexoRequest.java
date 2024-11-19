package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaAnexoRequest {

	private String nifReceptor;
	private String identificador;
	private Integer codigoOrigen;
	private byte[] referenciaAnexo;
	
	private AuthenticationDto authentication;

}
