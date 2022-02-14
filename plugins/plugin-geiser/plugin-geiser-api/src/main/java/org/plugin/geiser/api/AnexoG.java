package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnexoG {
	
	private String titulo;
	private TipoDocumentoAnexo tipoDocumentoAnexo;
	private String tipoDocumental;
	private ValidezDocumento validezDocumento;
	private String observaciones;
	private String hashBase64;
	private byte[] hash;
	private String anexoBase64;
	private String tipoMime;
	private Long tamanioFichero;
	private TipoFirma tipoFirma;
	private String nombreFirma;
	private String hashFirmaBase64;
	private byte[] hashFirma;
	private String tipoMimeFirma;
	private Long tamanioFicheroFirma;
	private String firmaBase64;
	private String identificador;
	private byte[] anexo;
	private byte[] firma;
}
