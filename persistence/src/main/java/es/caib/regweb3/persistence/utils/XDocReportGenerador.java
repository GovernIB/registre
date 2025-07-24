package es.caib.regweb3.persistence.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

/**
 * Clase que convierte un DOCX a PDF usando xdocreport y apache.poi
 * 
 * @author jamal
 *
 */
public class XDocReportGenerador {

	private static ByteArrayOutputStream baos;

	public static byte[] generarPdfDesdeDocx(InputStream is, Map<String, Object> datos) throws Exception {
		// Cargar plantilla DOCX con motor Velocity
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(is, TemplateEngineKind.Velocity);
		
		// Crear contexto y sustituir los datos con etiqueta ${}
		IContext context = report.createContext();
		
		if (datos != null) {
			for (Map.Entry<String, Object> entry : datos.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}
		
		// Configurar conversión a PDF
		Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);

		// Generar PDF
		baos = new ByteArrayOutputStream();
		report.convert(context, options, baos);

		byte[] pdfOriginal = baos.toByteArray();
		
		return insertarLogosEnPDF(pdfOriginal);
	}

	public static String generarHtmlDesdeDocx(InputStream is, Map<String, Object> datos) throws Exception {
		// Configurar conversión a HTML
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(is, TemplateEngineKind.Velocity);
		
		// Crear contexto y sustituir los datos con etiqueta ${}
		IContext context = report.createContext();
		
		if (datos != null) {
			context = report.createContext();
			for (Map.Entry<String, Object> entry : datos.entrySet()) {
				context.put(entry.getKey(), entry.getValue());
			}
		}

		// Configurar conversión a XHTML
		Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.XHTML);
		
		// Generar HTML
		baos = new ByteArrayOutputStream();
		report.convert(context, options, baos);

		String html = baos.toString(StandardCharsets.UTF_8.name());
		
		// Eliminar margenes docx del correo
		html = html.replaceAll("(?i)(margin-top|margin-left)\\s*:\\s*[^;\"']+;?", "");

		return html;
	}
	
	private static byte[] insertarLogosEnPDF(byte[] pdfOriginal) throws Exception {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    
	    PdfReader reader = new PdfReader(pdfOriginal);
	    PdfStamper stamper = new PdfStamper(reader, baos);
	    
	    Image imgIzquierda = Image.getInstance(obtenerLogoPorts());
	    Image imgDerecha = Image.getInstance(obtenerLogoEadmin());
	    
	    int totalPages = reader.getNumberOfPages();
	    
	    for (int i = 1; i <= totalPages; i++) {
	        PdfContentByte canvas = stamper.getOverContent(i);
	        
	        imgIzquierda.setAbsolutePosition(87, reader.getPageSize(i).getHeight() - 60); // Posició
	        imgIzquierda.scaleToFit(165, 90);  // Tamaño
	        canvas.addImage(imgIzquierda);
	        
	        imgDerecha.setAbsolutePosition(reader.getPageSize(i).getWidth() - 183, reader.getPageSize(i).getHeight() - 40); // Posició
	        imgDerecha.scaleToFit(100, 40);  // Tamaño
	        canvas.addImage(imgDerecha);
	    }

	    stamper.close();
	    reader.close();
	    
	    return baos.toByteArray();
	}
	
	private static byte[] obtenerLogoPorts() throws IOException {
    	ClassLoader classLoader = XDocReportGenerador.class.getClassLoader();
    	InputStream is = classLoader.getResourceAsStream("plantillas/img/portsdebalears.jpg");
    	
    	if (is == null) {
    	    throw new FileNotFoundException("No se encontró el logo portsdebalears.jpg");
    	}
    	
    	return leerBytes(is);
	}
	
	private static byte[] obtenerLogoEadmin() throws IOException {
    	ClassLoader classLoader = XDocReportGenerador.class.getClassLoader();
    	InputStream is = classLoader.getResourceAsStream("plantillas/img/LogoAmdElec.jpg");
    	
    	if (is == null) {
    	    throw new FileNotFoundException("No se encontró el logo LogoAdmElec.jpg");
    	}
		
    	return leerBytes(is);
	}
	
	private static byte[] leerBytes(InputStream is) throws IOException {
	    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	    int nRead;
	    byte[] data = new byte[1024];
	    while ((nRead = is.read(data, 0, data.length)) != -1) {
	        buffer.write(data, 0, nRead);
	    }
	    buffer.flush();
	    return buffer.toByteArray();
	}

}
