package es.caib.regweb3.persistence.utils;

import java.nio.file.Path;
import java.util.Map;

public class ContenidoEmail {
	private final String html;
	private final Map<String, Path> imagenes;

	public ContenidoEmail(String html, Map<String, Path> imagenes) {
		this.html = html;
		this.imagenes = imagenes;
	}

	public String getHtml() {
		return html;
	}

	public Map<String, Path> getImagenes() {
		return imagenes;
	}
}
