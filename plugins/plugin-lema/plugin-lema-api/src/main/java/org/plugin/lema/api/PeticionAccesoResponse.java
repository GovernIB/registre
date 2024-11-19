package org.plugin.lema.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeticionAccesoResponse {

	private String codigoRespuesta;
	private String descripcionRespuesta;
	private XMLGregorianCalendar fechaEvento;
	private DetalleDocumento documento;
	private Anexos anexos;
	private List<Opcion> opcionesRespuestaPeticionAcceso;
    private List<DocumentoLegal> documentosLegales = new ArrayList<DocumentoLegal>();

}