package es.caib.regweb.webapp.view;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.persistence.utils.FileSystemManager;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.AbstractIText5PdfView;
import es.caib.regweb.webapp.utils.ElementSello;


/**
 * Created by Fundació BI
 * User: earrivi
 * Date: 11/04/14
 */
public class SelloPdfView extends AbstractIText5PdfView {

    protected final Logger log = Logger.getLogger(getClass());

    private String codiOficina = null;
    private String nomOficina = null;
    private String numRegistre = null;
    
    private String dataRegistre = null;
    private String dataDiaRegistre = null;
    private String dataHoraRegistre = null;
    private String anyRegistre = null;
    private String mesRegistre = null;
    private String nomMesRegistre = null;
    private String diaRegistre = null;
    private String horaRegistre = null;
    private String minutRegistre = null;
    private String segonRegistre = null;
    
    private String destinatari = null;
    private String origen = null;
    private String tipusRegistre = null;
    private String tipusRegistreComplet = null;
    private String extracte = null;
    private String llibre = null;
    private String nomUsuari = null;
    private String nomUsuariComplet = null;
    private String entitat = null;
    private String decodificacioEntitat = null;
    private String formatNumRegistre = null;
    
    private ElementSello logoSello = null;
    private Entidad entidad = null;
    
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object registro = model.get("registro");

        String x = (String) model.get("x");
        String y = (String) model.get("y");
        String orientacion = (String) model.get("orientacion");
        tipusRegistreComplet = (String) model.get("tipoRegistro");

        // Configuraciones generales formato pdf
        if(orientacion.equals("V")){
            document.setPageSize(PageSize.A4);
        }
        if(orientacion.equals("H")){
            document.setPageSize(PageSize.A4.rotate());
        }

        document.addAuthor("REGWEB");
        document.addCreationDate();
        document.addCreator("iText library");
        document.newPage();

        document.addTitle("Segell");

//        Font helvetica = new Font(Font.FontFamily.HELVETICA, 10);
        String sello = null;
        
        //String numRegistreCompost = null;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatDiaDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHoraDate = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatMonthName = new SimpleDateFormat("MMMM"); // Afegir el locale?
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatMin = new SimpleDateFormat("mm");
        SimpleDateFormat formatSeg = new SimpleDateFormat("ss");
        
        // Registre Entrada
        if(registro.getClass().getSimpleName().equals("RegistroEntrada")){

            RegistroEntrada registroEntrada = (RegistroEntrada) model.get("registro");
            entidad = registroEntrada.getUsuario().getEntidad();
            
            sello = entidad.getSello();
            formatNumRegistre = entidad.getNumRegistro();

            if(	registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO) ||
            	registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){
            	
	            codiOficina = registroEntrada.getOficina().getCodigo();
	            nomOficina = registroEntrada.getOficina().getDenominacion();
	            numRegistre = String.valueOf(registroEntrada.getNumeroRegistro());
	            // Data registre
	            dataRegistre = formatDate.format(registroEntrada.getFecha());
	            dataDiaRegistre = formatDiaDate.format(registroEntrada.getFecha());
	            dataHoraRegistre = formatHoraDate.format(registroEntrada.getFecha());
	            anyRegistre = formatYear.format(registroEntrada.getFecha());
	            mesRegistre = formatMonth.format(registroEntrada.getFecha());
	            nomMesRegistre = formatMonthName.format(registroEntrada.getFecha());
	            diaRegistre = formatDay.format(registroEntrada.getFecha());
	            horaRegistre = formatHour.format(registroEntrada.getFecha());
	            minutRegistre = formatMin.format(registroEntrada.getFecha());
	            segonRegistre = formatSeg.format(registroEntrada.getFecha());
	            
	            tipusRegistre = "E";
	            
	            nomUsuari = registroEntrada.getUsuario().getUsuario().getNombre();
	            nomUsuariComplet = registroEntrada.getUsuario().getNombreCompleto();
	            entitat = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getNombre();
	            decodificacioEntitat = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getDescripcion();
	            
	            formatNumRegistre = registroEntrada.getNumeroRegistroFormateado();
	            
	            if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)){
	
	            	if(registroEntrada.getDestino() != null){
	                    destinatari = registroEntrada.getDestino().getDenominacion();
	                }else{
	                    destinatari = registroEntrada.getDestinoExternoDenominacion();
	                }
	                extracte = registroEntrada.getRegistroDetalle().getExtracto();
	                llibre = registroEntrada.getLibro().getNombre();
	                
	            }else if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){
	
	            	llibre = registroEntrada.getLibro().getNombre();
	
	            }
            }
        }

        // Registre Sortida
        if(registro.getClass().getSimpleName().equals("RegistroSalida")){

            RegistroSalida registroSalida = (RegistroSalida) model.get("registro");
            entidad = registroSalida.getUsuario().getEntidad();
            
            sello = entidad.getSello();
            formatNumRegistre = entidad.getNumRegistro();

            codiOficina = registroSalida.getOficina().getCodigo();
            nomOficina = registroSalida.getOficina().getDenominacion();
            numRegistre = String.valueOf(registroSalida.getNumeroRegistro());
            // Data registre
            dataRegistre = formatDate.format(registroSalida.getFecha());
            dataDiaRegistre = formatDiaDate.format(registroSalida.getFecha());
            dataHoraRegistre = formatHoraDate.format(registroSalida.getFecha());
            anyRegistre = formatYear.format(registroSalida.getFecha());
            mesRegistre = formatMonth.format(registroSalida.getFecha());
            nomMesRegistre = formatMonthName.format(registroSalida.getFecha());
            diaRegistre = formatDay.format(registroSalida.getFecha());
            horaRegistre = formatHour.format(registroSalida.getFecha());
            minutRegistre = formatMin.format(registroSalida.getFecha());
            segonRegistre = formatSeg.format(registroSalida.getFecha());
            
            if(registroSalida.getOrigen() != null){
                origen = registroSalida.getOrigen().getDenominacion();
            }else{
                origen = registroSalida.getOrigenExternoDenominacion();
            }
            tipusRegistre = "S";
            extracte = registroSalida.getRegistroDetalle().getExtracto();
            llibre = registroSalida.getLibro().getNombre();
            nomUsuari = registroSalida.getUsuario().getUsuario().getNombre();
            nomUsuariComplet = registroSalida.getUsuario().getNombreCompleto();
            entitat = registroSalida.getOficina().getOrganismoResponsable().getEntidad().getNombre();
            decodificacioEntitat = registroSalida.getOficina().getOrganismoResponsable().getEntidad().getDescripcion();

            formatNumRegistre = registroSalida.getNumeroRegistroFormateado();
        }

        if(sello != null){

        	java.util.List<ElementSello> linies = new ArrayList<ElementSello>();
        	
        	String[] liniesSello = sello.split("\\r?\\n");
        	for(String liniaSello: liniesSello) {
        		linies.addAll(processaLinia(liniaSello));
        	}

        	if (!linies.isEmpty()) {
        		
	            // Cerca la línia més llarga per controlar que no surti del pdf
	        	float max = ElementSello.maxPosx;
	        	float may = ElementSello.maxPosy;
	        	
	        	float fx = calculaOrigenX(x, max, orientacion);
        		float fy = calculaOrigenY(y, may, orientacion);// - linies.get(0).getPosy();
	        	
        		// LogoSello
        		if (logoSello != null && entidad.getLogoSello() != null) {
            		File file = FileSystemManager.getArchivo(entidad.getLogoSello().getId());
//            		java.awt.image.BufferedImage logo = ImageIO.read(file);
//            		Image imatgeSello = Image.getInstance(logo, null);
            		Image imatgeSello = Image.getInstance(file.getAbsolutePath());
            		float heigh = imatgeSello.getHeight();
            		float width = imatgeSello.getWidth();
            		if (logoSello.getAmple() != null) {
            			float proporcio = logoSello.getAmple()/imatgeSello.getWidth();
            			width = logoSello.getAmple();
            			heigh *= proporcio;
            			imatgeSello.scaleToFit(width, heigh);
            		}
            		imatgeSello.setAbsolutePosition(fx + logoSello.getPosx(), fy - logoSello.getPosy() - heigh); 
            		PdfContentByte canvas = writer.getDirectContent();
            		canvas.addImage(imatgeSello); 
            	}
        	
        		// ElementsSello
	            for(ElementSello element: linies) {
	            	Font font = new Font(element.getBf(), element.getFontSize(), element.getFontStyle(), element.getColor());
	                Phrase frase = new Phrase(element.getText(), font);
	                PdfContentByte canvas = writer.getDirectContent();
	                //int yu = (int) (Float.valueOf(y)-12*j);
	                ColumnText.showTextAligned(canvas, element.getAlineacio(), frase, fx + element.getPosx(), fy + element.getPosy(), 0);
	            }
            }

            //Auto-Impresión
            writer.addJavaScript("this.print(true, this.pageNum, this.pageNum);");
            writer.getDirectContent().setAction(PdfAction.javaScript("this.print(true, this.pageNum, this.pageNum);", writer), 0f, 0f, 800f, 830f);

            ElementSello.clear();
        }

        String nombreFichero = "Segell_" + formatNumRegistre + ".pdf";

        // Cabeceras Response
        response.setContentType("application/pdf");
        response.setHeader("Cache-Control",  "store");
        response.setHeader("Pragma", "cache");
        response.setHeader("Content-Disposition","inline; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");
    }
    
    private float calculaOrigenY(String y, float max, String orientacion) {
        Float pixelY = Float.valueOf(y);
        Float maxpixelY = orientacion.equals("V") ? 827f : 582f;
        if(pixelY > maxpixelY){
        	pixelY = maxpixelY;
        }else{
        	Float segellpixelY = pixelY + max - 15;
            if(segellpixelY < 0){
            	pixelY -= segellpixelY;
                if(pixelY > maxpixelY){
                	pixelY = maxpixelY;
                }
            }
        }
        return pixelY;
	}

	private float calculaOrigenX(String x, float max, String orientacion) {
		Float pixelX = Float.valueOf(x);
        if(pixelX < 15){
        	pixelX = 15f;
        }else{
            int maxpixelX = orientacion.equals("V") ? 582 : 827;
            Float segellpixelX = max + pixelX;
            if(segellpixelX > maxpixelX){
                Float diferenciaX = segellpixelX - maxpixelX;
                pixelX -= diferenciaX;
                if(pixelX < 15){
                	pixelX = 15f;
                }
            }
        }
        return pixelX;
	}

	private java.util.List<ElementSello> processaLinia(String liniaSello) throws Exception {
    	java.util.List<ElementSello> linies = new ArrayList<ElementSello>();
    	
    	java.util.List<ElementSello> partLinia = new ArrayList<ElementSello>();
    	
    	if (liniaSello == null || "".equals(liniaSello)) {
    		partLinia.add(new ElementSello("", false));
    	} else {
	    	while (!"".equals(liniaSello)) {
	    		if (liniaSello.contains("${")) {
    				// Text abans del primer paràmetre
	    			String preParam = liniaSello.substring(0, liniaSello.indexOf("${"));
	    			if (!"".equals(preParam)) {
	    				partLinia.add(new ElementSello(preParam, false));
	    			}
	    			// Paràmetre
	    			String param = liniaSello.substring(liniaSello.indexOf("${") + 2, liniaSello.indexOf("}"));
	    			ElementSello ls = new ElementSello(param, true);
	    			if (ls.getParam().equalsIgnoreCase("logoSello")) {
	    				logoSello = ls;
	    			} else {
		    			ls.setText(getParamValue(ls.getParam()));
		    			if (ls.isChangedPosy()) {
		    				actualitzaPos(partLinia);
		    				linies.addAll(partLinia);
		    				partLinia = new ArrayList<ElementSello>();
		    				partLinia.add(ls);
		    			} else {
		    				partLinia.add(ls);
		    			}
	    			}
	    			// Text posterior al primer paràmetre
	    			liniaSello = liniaSello.substring(liniaSello.indexOf("}") + 1);
	    		} else {
	    			partLinia.add(new ElementSello(liniaSello, false));
	    		}
    		}
    	}
    	
    	actualitzaPos(partLinia);
    	linies.addAll(partLinia);
    	
		return linies;
    }
    
    // Alineam totes les parts d'una mateixa línia
    private void actualitzaPos(java.util.List<ElementSello> linies) {
    	if (!linies.isEmpty()) {
	    	int alt = 0;
	    	for (ElementSello linia: linies) {
	    		alt = Math.max(alt, Math.round(linia.getAltFila()));
	    	}
	    	ElementSello.posyRef -= alt;
	    	for (ElementSello linia: linies) {
	    		linia.setPosy(ElementSello.posyRef);
	    	}
	    	ElementSello.posxRef = ElementSello.posxRefLinia;
    	}
    }
    
    private String getParamValue(String paramName) {
    	if ("codiOficina".equalsIgnoreCase(paramName)) {
    		return codiOficina;
    	} else if ("nomOficina".equalsIgnoreCase(paramName)) {
    		return nomOficina;
    	} else if ("numRegistre".equalsIgnoreCase(paramName)) {
    		return numRegistre;
    	} else if ("formatNumRegistre".equalsIgnoreCase(paramName)) {
    		return formatNumRegistre;
    	} else if ("dataRegistre".equalsIgnoreCase(paramName)) {
    		return dataRegistre;
    	} else if ("dataDiaRegistre".equalsIgnoreCase(paramName)) {
    		return dataDiaRegistre;
    	} else if ("dataHoraRegistre".equalsIgnoreCase(paramName)) {
    		return dataHoraRegistre;
    	} else if ("anyRegistre".equalsIgnoreCase(paramName)) {
    		return anyRegistre;
    	} else if ("mesRegistre".equalsIgnoreCase(paramName)) {
    		return mesRegistre;
    	} else if ("nomMesRegistre".equalsIgnoreCase(paramName)) {
    		return nomMesRegistre;
    	} else if ("diaRegistre".equalsIgnoreCase(paramName)) {
    		return diaRegistre;
    	} else if ("horaRegistre".equalsIgnoreCase(paramName)) {
    		return horaRegistre;
    	} else if ("minutRegistre".equalsIgnoreCase(paramName)) {
    		return minutRegistre;
    	} else if ("segonRegistre".equalsIgnoreCase(paramName)) {
    		return segonRegistre;
    	} else if ("entitat".equalsIgnoreCase(paramName)) {
    		return entitat;
    	} else if ("destinatari".equalsIgnoreCase(paramName)) {
            return (destinatari!=null && destinatari.length()>0) ? destinatari : "";
    	} else if ("origen".equalsIgnoreCase(paramName)) {
    		return (origen!=null && origen.length()>0) ? origen : "";
    	} else if ("tipusRegistre".equalsIgnoreCase(paramName)) {
    		return tipusRegistre;
    	} else if ("extracte".equalsIgnoreCase(paramName)) {
    		return (extracte!=null && extracte.length()>0) ? extracte : "";
    	} else if ("llibre".equalsIgnoreCase(paramName)) {
    		return llibre;
    	} else if ("nomUsuari".equalsIgnoreCase(paramName)) {
    		return nomUsuari;
    	} else if ("nomUsuariComplet".equalsIgnoreCase(paramName)) {
    		return nomUsuariComplet;
    	} else if ("decodificacioEntitat".equalsIgnoreCase(paramName)) {
    		return decodificacioEntitat;
    	} else if ("formatNumRegistre".equalsIgnoreCase(paramName)) {
    		return formatNumRegistre;
    	} else if ("tipusRegistreComplet".equalsIgnoreCase(paramName)) {
    		return tipusRegistreComplet;
    	}
    	return paramName;
    }
}
