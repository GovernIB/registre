package org.plugin.geiser.api;

public enum DocumentacionFisica {
	
	DOCUMENTACION_FISICA_REQUERIDA(1L),
	DOCUMENTACION_FISICA_COMPLEMENTARIA(2L),
	SIN_DOCUMENTACION_FISICA(3L);
	
	private Long value;
	
	DocumentacionFisica(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return value;
	}
	
	public static DocumentacionFisica getDocumentacionFisica(Long value) {
		if (value == null)
			return null;
		for (DocumentacionFisica doc: DocumentacionFisica.values()) {
			if (value.equals(doc.getValue())) {
				return doc;
			}
		}
		throw new IllegalArgumentException("No matching type in DocumentacionFisica for value " + value);
	}
}
