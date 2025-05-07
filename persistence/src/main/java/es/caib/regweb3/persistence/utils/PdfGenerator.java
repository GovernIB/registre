package es.caib.regweb3.persistence.utils;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.utils.RegwebConstantes;

public class PdfGenerator {

    public static byte[] generarPdf(RegistroEntrada registroEntrada, Long codigoSia) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        Locale locale = new Locale(RegwebConstantes.IDIOMA_CASTELLANO_CODIGO);
        try {
//        	String titulo = "Rechazo de registro " + registroEntrada.getNumeroRegistro() + " por instancia genérica";
//        	String cuerpo = "Existe un trámite a medida '" + codigoSia + "' para realizar el registro";
        	String url = PropiedadGlobalUtil.getUrlBaseSede() + codigoSia;
//        	String titulo = I18NLogicUtils.tradueix(locale, "clasificar.registro.documento.titulo", new String[] {registroEntrada.getNumeroRegistro()});
        	String cuerpo = I18NLogicUtils.tradueix(locale, "clasificar.registro.documento.cuerpo", new String[] {registroEntrada.getNumeroRegistro()});
        	String botonEnlace = I18NLogicUtils.tradueix(locale, "clasificar.registro.documento.enlace");
        	
        	PdfWriter.getInstance(document, baos);
            document.open();

//            document.add(new Paragraph(titulo));
//            document.add(new Paragraph(" "));
            document.add(new Paragraph(cuerpo));
            document.add(new Paragraph(" "));
            Anchor link = new Anchor(botonEnlace);
            link.setReference(url);
            document.add(link);
            
        } catch (Exception e) {
			throw e;
    	} finally {
            document.close();
        }

        return baos.toByteArray();
    }
}
