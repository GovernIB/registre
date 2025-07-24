package es.caib.regweb3.persistence.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.itextpdf.text.pdf.codec.Base64;

public class DocumentHelper {

	public static byte[] generarPdf(String nombrePlantilla, Map<String, Object> datos) throws Exception {
		try {
			InputStream is = obtenerPlantilla(nombrePlantilla);
			return XDocReportGenerador.generarPdfDesdeDocx(is, datos);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String generarHtml(String nombrePlantilla, Map<String, Object> datos) throws Exception {
		try {
			InputStream is = obtenerPlantilla(nombrePlantilla);
			String html = XDocReportGenerador.generarHtmlDesdeDocx(is, datos);
		
			html = embederImagenesEnHtml(html, obtenerPlantilla(nombrePlantilla));
			
			return html;
		} catch (Exception e) {
			throw e;
		}
	}
	
//	private static Map<String, Object> configurarParametros(RegistroEntrada registroEntrada, Long codigoSia) throws FileNotFoundException {
//		Map<String, Object> datos = new HashMap<String, Object>();
//		List<String> nombres = new ArrayList<String>();
//		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//		String fechaRegistro = formatter.format(registroEntrada.getFecha());
//    	String url = PropiedadGlobalUtil.getUrlBaseSede() + codigoSia;
//    	
//    	for (Interesado	interesado: registroEntrada.getRegistroDetalle().getInteresados()) {
//    		nombres.add(interesado.getNombreCompleto());
//		}
//    	
//		datos.put("interesados", StringUtils.join(nombres, ", "));
//		datos.put("fecha", fechaRegistro);
//		datos.put("enlace", url);
//		
//		return datos;
//	}
	
	private static String embederImagenesEnHtml(String html, InputStream docx) throws IOException {
	    // Directorio temporal de imágenes (como las que se extraen de DOCX)
	    Path mediaFolder = Files.createTempDirectory("docx-media");

	    // Extraer las imágenes desde el DOCX
	    try (ZipInputStream zis = new ZipInputStream(docx)) {
	        ZipEntry entry;
	        while ((entry = zis.getNextEntry()) != null) {
	            if (entry.getName().startsWith("word/media/")) {
	                Path imagePath = mediaFolder.resolve(entry.getName().replace("word/media/", ""));
	                Files.copy(zis, imagePath, StandardCopyOption.REPLACE_EXISTING);
	            }
	        }
	    }

	    // Buscar y reemplazar cada imagen en el HTML
	    Pattern imgPattern = Pattern.compile("<img[^>]+src=[\"']word/media/([^\"']+)[\"'][^>]*>");
	    Matcher matcher = imgPattern.matcher(html);
	    StringBuffer sb = new StringBuffer();
	    while (matcher.find()) {
	        String imgName = matcher.group(1);
	        Path imgPath = mediaFolder.resolve(imgName);
	        String base64 = Base64.encodeBytes(Files.readAllBytes(imgPath));
	        String mimeType = Files.probeContentType(imgPath); // ej: "image/png"
	        String imgTag = "<img src=\"data:" + mimeType + ";base64," + base64 + "\" />";
	        matcher.appendReplacement(sb, Matcher.quoteReplacement(imgTag));
	    }
	    matcher.appendTail(sb);
	    return sb.toString();
	}

	
	private static InputStream obtenerPlantilla(String nombrePlantilla) throws FileNotFoundException {
    	ClassLoader classLoader = DocumentHelper.class.getClassLoader();
    	InputStream is = classLoader.getResourceAsStream("plantillas/" + nombrePlantilla);
    	
    	if (is == null) {
    	    throw new FileNotFoundException("No se encontró la plantilla");
    	}
		return is;
	}
	
}
