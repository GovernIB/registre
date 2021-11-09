package org.plugin.geiser.api;

public enum TipoTransporte {
	
	SERVICIO_MENSAJEROS(1L, "01"),
	CORREO_POSTAL(2L, "02"),
	CORREO_POSTAL_CERTIFICADO(3L, "03"),
	BUROFAX(4L, "04"),
	EN_MANO(5L, "05"),
	FAX(6L, "06"),
	OTROS(7L, "07");
	
	private Long value;
	private String name;
	
	TipoTransporte(Long value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public Long getValue() {
		return value;
	}
	
	public String getName() {
		return name;
	}

	public static TipoTransporte getTipoTransporte(Long value) {
		if (value == null)
			return null;
		for (TipoTransporte tipo: TipoTransporte.values()) {
			if (value.equals(tipo.getValue())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in TipoTransporte for value " + value);
	}
	
	public static TipoTransporte getTipoTransporteName(String name) {
		if (name == null)
			return null;
		for (TipoTransporte tipo: TipoTransporte.values()) {
			if (name.equals(tipo.getName())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in TipoTransporte for name " + name);
	}
}
