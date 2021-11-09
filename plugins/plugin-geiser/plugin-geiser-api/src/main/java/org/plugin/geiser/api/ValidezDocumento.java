package org.plugin.geiser.api;

public enum ValidezDocumento {

	COPIA("01"),
	COPIA_COMPULSADA("02"),
	COPIA_ORIGINAL("03"),
	ORIGINAL("04");
	
	private final String value;

	ValidezDocumento(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

	
	public static ValidezDocumento getValidezDocumento(String value) {

        if (value != null) {

            for (ValidezDocumento validezDocumento : ValidezDocumento.values()) {
                if (value.equals(validezDocumento.getValue())) return validezDocumento;
            }

        }

        return null;
    }
}
