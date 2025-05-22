package es.caib.regweb3.persistence.test;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import es.caib.regweb3.persistence.utils.DocumentHelper;
import es.caib.regweb3.persistence.utils.XDocReportGenerador;

public class PdfConverterTest {

	@Test
	public void test() throws Exception {
		ClassLoader classLoader = DocumentHelper.class.getClassLoader();
    	InputStream is = classLoader.getResourceAsStream("plantillas/Revocación.docx");
    	
    	assertNotNull(is);
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String fechaRegistro = formatter.format(new Date());
    	String url = "http://seuelecng-proves.portsdebalears.com/seuapb/";
    	Map<String, Object> datos = new HashMap<String, Object>();
		datos.put("interesados", "Ana López");
		datos.put("fecha", fechaRegistro);
		datos.put("enlace", url);
		
		byte[] bytes = XDocReportGenerador.generarPdfDesdeDocx(is, datos);
		
		assertNotNull(bytes);
		
		guardarEnTemporal(bytes, "doc_", ".pdf");
		
		String mensaje = XDocReportGenerador.generarHtmlDesdeDocx(is, datos);
		
		assertNotNull(mensaje);
		
		guardarEnTemporal(mensaje.getBytes(), "doc_", ".html");
	}
	
	private static File guardarEnTemporal(byte[] contenido, String prefijo, String sufijo) throws IOException {
        File tempFile = File.createTempFile(prefijo, sufijo);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(contenido);
        }
        System.out.println("Archivo guardado temporalmente en: " + tempFile.getAbsolutePath());
        return tempFile;
    }
	
}
