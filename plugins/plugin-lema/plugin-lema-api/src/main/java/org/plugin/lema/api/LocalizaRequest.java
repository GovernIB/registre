package org.plugin.lema.api;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocalizaRequest {

    protected String nifTitular;
    protected String nifDestinatario;
    protected String codigoDestino;
    protected Date fechaDesde;
    protected Date fechaHasta;
    protected BigInteger tipoEnvio;
    protected List<Opcion> opcionesLocaliza;
    
}
