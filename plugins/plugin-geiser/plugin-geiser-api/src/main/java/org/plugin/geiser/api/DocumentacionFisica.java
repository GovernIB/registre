package org.plugin.geiser.api;

public enum DocumentacionFisica {
	
	DOCUMENTACION_FISICA_REQUERIDA(1L, "1"),
	DOCUMENTACION_FISICA_COMPLEMENTARIA(2L, "2"),
	SIN_DOCUMENTACION_FISICA(3L, "3");
	
	private Long value;
	private String name;
	
	DocumentacionFisica(Long value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public Long getValue() {
		return value;
	}
	
	public String getName() {
		return name;
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
	
	public static DocumentacionFisica getDocumentacionFisicaName(String name) {
		if (name == null)
			return null;
		for (DocumentacionFisica doc: DocumentacionFisica.values()) {
			if (name.equals(doc.getName())) {
				return doc;
			}
		}
		throw new IllegalArgumentException("No matching type in DocumentacionFisica for name " + name);
	}
}
