package es.caib.regweb3.persistence.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;

public class DocumentHelper {

	public static byte[] generarPdf(RegistroEntrada registroEntrada, Long codigoSia) throws Exception {
		try {
			InputStream is = obtenerPlantilla();
			Map<String, Object> datos = configurarParametros(registroEntrada, codigoSia);
			return XDocReportGenerador.generarPdfDesdeDocx(is, datos);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String generarHtml(RegistroEntrada registroEntrada, Long codigoSia) throws Exception {
		try {
			InputStream is = obtenerPlantilla();
			Map<String, Object> datos = configurarParametros(registroEntrada, codigoSia);
			return XDocReportGenerador.generarHtmlDesdeDocx(is, datos);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private static Map<String, Object> configurarParametros(RegistroEntrada registroEntrada, Long codigoSia) throws FileNotFoundException {
		Map<String, Object> datos = new HashMap<String, Object>();
		List<String> nombres = new ArrayList<String>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fechaRegistro = formatter.format(registroEntrada.getFecha());
    	String url = PropiedadGlobalUtil.getUrlBaseSede() + codigoSia;
    	
    	for (Interesado	interesado: registroEntrada.getRegistroDetalle().getInteresados()) {
    		nombres.add(interesado.getNombreCompleto());
		}
    	
		datos.put("interesados", StringUtils.join(nombres, ", "));
		datos.put("fecha", fechaRegistro);
		datos.put("enlace", url);
		
		return datos;
	}
	
	private static InputStream obtenerPlantilla() throws FileNotFoundException {
    	ClassLoader classLoader = DocumentHelper.class.getClassLoader();
    	InputStream is = classLoader.getResourceAsStream("plantillas/Revocación.docx");
    	
    	if (is == null) {
    	    throw new FileNotFoundException("No se encontró la plantilla");
    	}
		return is;
	}
	
}
