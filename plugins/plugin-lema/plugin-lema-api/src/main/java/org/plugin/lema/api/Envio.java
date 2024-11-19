package org.plugin.lema.api;

import java.math.BigInteger;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Envio {

	protected String identificador;
	protected BigInteger codigoOrigen;
	protected String concepto;
	protected String descripcion;
	protected Organismo organismoEmisor;
	protected Organismo organismoEmisorRaiz;
	protected XMLGregorianCalendar fechaPuestaDisposicion;
	protected BigInteger tipoEnvio;
	protected BigInteger vinculo;
	protected Persona titular;
	protected String metadatosPublicos;
	protected List<Opcion> opcionesEnvio;
}
