package org.plugin.lema.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AcuseRecibo {
	
	private String nombreAcuse;
	private Contenido contenido;
	private String mimeType;
	private String metadatos;
}
