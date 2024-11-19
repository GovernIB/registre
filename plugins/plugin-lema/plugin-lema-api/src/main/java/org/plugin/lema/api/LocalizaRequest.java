package org.plugin.lema.api;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocalizaRequest {

	private String nifTitular;
	private String nifDestinatario;
	private String codigoDestino;
	private Date fechaDesde;
	private Date fechaHasta;
	private BigInteger tipoEnvio;
	private List<Opcion> opcionesLocaliza;
    
	private AuthenticationDto authentication;
    
}
