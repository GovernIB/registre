package es.caib.regweb3.model.utils;

public class TramiteDto {

	private String codigoSia;
	
	private String nombre;

	public TramiteDto(String codigoSia, String nombre) {
		super();
		this.codigoSia = codigoSia;
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "TramiteDto [codigoSia=" + codigoSia + ", nombre=" + nombre + "]";
	}

	public String getCodigoSia() {
		return codigoSia;
	}

	public void setCodigoSia(String codigoSia) {
		this.codigoSia = codigoSia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
