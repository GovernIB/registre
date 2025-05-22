package es.caib.regweb3.model.utils;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
public class DocumentoVisor {
	private String nombre;
	private String mimeType;
	private String contenido;
	
	public DocumentoVisor(String nombre, String mimeType, String contenido) {
		super();
		this.nombre = nombre;
		this.mimeType = mimeType;
		this.contenido = contenido;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	public String getArxiuExtensio() {
		int index = nombre.lastIndexOf(".");
		if (index != -1) {
			return nombre.substring(index + 1);
		} else {
			return "";
		}
	}
}


