package org.plugin.geiser.api;

public enum CanalNotificacion {
	
	DIRECCION_POSTAL(1L),
	DIRECCION_ELECTRONICA_HABILITADA(2L),
	COMPARECENCIA_ELECTRONICA(3L);
	
	Long value;
	
	CanalNotificacion(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return value;
	}
	
	public static CanalNotificacion getCanalNotificacion(Long value) {
		if (value == null || value == -1L)
			return null;
		for (CanalNotificacion tipo: CanalNotificacion.values()) {
			if (value.equals(tipo.getValue())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in CanalNotificacion for value " + value);
	}
}
