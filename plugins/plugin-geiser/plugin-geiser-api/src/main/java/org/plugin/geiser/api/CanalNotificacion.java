package org.plugin.geiser.api;

public enum CanalNotificacion {
	
	DIRECCION_POSTAL(1L, "01"),
	DIRECCION_ELECTRONICA_HABILITADA(2L, "02"),
	COMPARECENCIA_ELECTRONICA(3L, "03");
	
	private Long value;
	private String name;
	
	CanalNotificacion(Long value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public Long getValue() {
		return value;
	}
	
	public String getName() {
		return name;
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
	
	public static CanalNotificacion getCanalNotificacionName(String name) {
		if (name == null)
			return null;
		for (CanalNotificacion tipo: CanalNotificacion.values()) {
			if (name.equals(tipo.getName())) {
				return tipo;
			}
		}
		throw new IllegalArgumentException("No matching type in CanalNotificacion for name " + name);
	}
}
