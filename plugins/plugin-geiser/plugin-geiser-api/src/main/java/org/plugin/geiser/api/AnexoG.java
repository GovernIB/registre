package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnexoG {
	
	private String titulo;
	private TipoDocumento tipoDocumento;
	private String tipoDocumental;
	private ValidezDocumento validezDocumento;
	private String observaciones;
	private String hash;
	private String anexoBase64;
	private String tipoMime;
	private Long tamanioFichero;
	private TipoFirma tipoFirma;
	private String nombreFirma;
	private String hashFirma;
	private String tipoMimeFirma;
	private String tamanioFicheroFirma;
	private String firmaBase64;
	
}
