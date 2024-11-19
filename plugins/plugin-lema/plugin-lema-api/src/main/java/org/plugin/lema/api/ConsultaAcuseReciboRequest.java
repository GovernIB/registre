package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaAcuseReciboRequest {

	private String nifReceptor;
	private String identificador;
	private Integer codigoOrigen;
	private IdentificadorAcuseRecibo identificadorAcuse;

	private AuthenticationDto authentication;
	
}
