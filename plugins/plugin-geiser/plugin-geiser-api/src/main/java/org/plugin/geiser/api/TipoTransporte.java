package org.plugin.geiser.api;

public enum TipoTransporte {
	
	SERVICIO_MENSAJEROS(1L),
	CORREO_POSTAL(2L),
	CORREO_POSTAL_CERTIFICADO(3L),
	BUROFAX(4L),
	EN_MANO(5L),
	FAX(6L),
	OTROS(7L);
	
	Long value;
	
	TipoTransporte(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return value;
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
}
