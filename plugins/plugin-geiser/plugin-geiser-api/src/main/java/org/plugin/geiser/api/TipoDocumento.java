package org.plugin.geiser.api;

public enum TipoDocumento {
	
	NIF(1L, "N"),
	CIF(2L, "C"),
	PASAPORTE(3L, "P"),
	NIE(4L, "E"),
	OTROS_PERSONA_FISICA(5L, "X"),
	OTROS_ORIGEN_VALUE(6L, "O");
	
	private Long value;
	private String name;
	
	TipoDocumento(Long value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public Long getValue() {
		return value;
	}
	
	
	public String getName() {
		return name;
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
	
	public static TipoDocumento getTipoDocumentoName(String name) {
		if (name == null)
			return null;
		for (TipoDocumento tipo: TipoDocumento.values()) {
			if (name.equals(tipo.getName())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in TipoDocumento for name " + name);
	}
}
