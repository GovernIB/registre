package org.plugin.geiser.api;

public enum TipoDocumento {
	
	NIF(1L),
	CIF(2L),
	PASAPORTE(3L),
	NIE(4L),
	OTROS_PERSONA_FISICA(5L),
	OTROS_ORIGEN_VALUE(6L);
	
	Long value;
	
	TipoDocumento(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return value;
	}
	
	public static TipoDocumento getTipoDocumento(Long value) {
		if (value == null)
			return null;
		for (TipoDocumento tipo: TipoDocumento.values()) {
			if (value.equals(tipo.getValue())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in TipoDocumento for value " + value);
	}
}
