package org.plugin.geiser.api;

public enum TipoDocumentoAnexo {
	
	FORMULARIO("01"),
	DOCUMENTO_ADJUNTO("02"),
	FICHERO_TECNICO_INTERNO("03");
	
	private String value;
	
	TipoDocumentoAnexo(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public static TipoDocumentoAnexo getTipoDocumento(String value) {
		if (value == null)
			return null;
		for (TipoDocumentoAnexo tipo: TipoDocumentoAnexo.values()) {
			if (value.equals(tipo.getValue())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in TipoDocumentoAnexo for value " + value);
	}
}
