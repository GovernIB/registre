package es.caib.regweb3.webapp.view;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.utils.AbstractIText5PdfView;
import es.caib.regweb3.webapp.utils.ElementSello;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Fundació BI
 * User: earrivi
 * Date: 11/04/14
 */
public class SelloPdfView extends AbstractIText5PdfView {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Retorna el mensaje traducido según el idioma del usuario
     * @param key
     * @return
     */
    protected String getMessage(String key){
        return I18NUtils.tradueix(key);
    }

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
    private String tipusRegistreCompletCatala = null;
    private String tipusRegistreCompletCastella = null;
    private String extracte = null;
    private String llibre = null;
    private String nomUsuari = null;
    private String nomUsuariComplet = null;
    private String entitat = null;
    private String decodificacioEntitat = null;
    private String numRegformat = null;
    
    private ElementSello logoSello = null;
    private Entidad entidad = null;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object registro = model.get("registro");

        // Coordenadas del sello
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
        document.addAuthor("REGWEB3");
        document.addCreationDate();
        document.addCreator("iText library");
        document.newPage();
        document.addTitle("Segell");

        // Inicializamos el contenido del sello
        String sello = null;

        // Formatos de fecha
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat formatDiaDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHoraDate = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat formatMonthName = new SimpleDateFormat("MMMM");
        SimpleDateFormat formatDay = new SimpleDateFormat("dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH");
        SimpleDateFormat formatMin = new SimpleDateFormat("mm");
        SimpleDateFormat formatSeg = new SimpleDateFormat("ss");

        // Si es Registro de Entrada
        if(registro.getClass().getSimpleName().equals("RegistroEntrada")){

            // Obtenemos el registro de entrada
            RegistroEntrada registroEntrada = (RegistroEntrada) model.get("registro");
            // Obtiene la entidad
            entidad = (Entidad) model.get("entidad");
            // Obtiene el formato del sello definido en la entidad
            sello = entidad.getSello();
            // Obtiene los datos del registro
            codiOficina = registroEntrada.getOficina().getCodigo();
            nomOficina = registroEntrada.getOficina().getDenominacion();
            numRegistre = String.valueOf(registroEntrada.getNumeroRegistro());
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
            tipusRegistreCompletCatala = getMessage("sello.tipoRegistroCompletoCatalan.entrada");
            tipusRegistreCompletCastella = getMessage("sello.tipoRegistroCompletoCastellano.entrada");
            nomUsuari = registroEntrada.getUsuario().getUsuario().getNombre();
            nomUsuariComplet = registroEntrada.getUsuario().getNombreCompleto();
            entitat = entidad.getNombre();
            decodificacioEntitat = entidad.getDescripcion();
            numRegformat = registroEntrada.getNumeroRegistroFormateado();
            llibre = registroEntrada.getLibro().getNombre();
            extracte = registroEntrada.getRegistroDetalle().getExtracto();
            if(registroEntrada.getDestino() != null){
                destinatari = registroEntrada.getDestino().getDenominacion();
            }else{
                destinatari = registroEntrada.getDestinoExternoDenominacion();
            }
        }

        // Si es Registro de Salida
        if(registro.getClass().getSimpleName().equals("RegistroSalida")){

            // Obtenemos el registro de salida
            RegistroSalida registroSalida = (RegistroSalida) model.get("registro");
            // Obtiene la entidad
            entidad = (Entidad) model.get("entidad");
            // Obtiene el formato del sello definido en la entidad
            sello = entidad.getSello();
            // Obtiene los datos del registro
            codiOficina = registroSalida.getOficina().getCodigo();
            nomOficina = registroSalida.getOficina().getDenominacion();
            numRegistre = String.valueOf(registroSalida.getNumeroRegistro());
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
            tipusRegistre = "S";
            tipusRegistreCompletCatala = getMessage("sello.tipoRegistroCompletoCatalan.salida");
            tipusRegistreCompletCastella = getMessage("sello.tipoRegistroCompletoCastellano.salida");
            extracte = registroSalida.getRegistroDetalle().getExtracto();
            llibre = registroSalida.getLibro().getNombre();
            nomUsuari = registroSalida.getUsuario().getUsuario().getNombre();
            nomUsuariComplet = registroSalida.getUsuario().getNombreCompleto();
            entitat = entidad.getNombre();
            decodificacioEntitat = entidad.getDescripcion();
            numRegformat = registroSalida.getNumeroRegistroFormateado();
            llibre = registroSalida.getLibro().getNombre();
            extracte = registroSalida.getRegistroDetalle().getExtracto();
            if(registroSalida.getOrigen() != null){
                origen = registroSalida.getOrigen().getDenominacion();
            }else{
                origen = registroSalida.getOrigenExternoDenominacion();
            }
        }

        // Si el formato de sello está definido
        if(sello != null){

            // Guardará el sello en lineas
            java.util.List<ElementSello> linies = new ArrayList<ElementSello>();
            // Separa cada linea del sello cuando encuentra un Retorno de carro + Salto de linea
            String[] liniesSello = sello.split("\\r?\\n");
            // Recorre cada una de las lineas del sello
            for(String liniaSello: liniesSello) {
                // Añade cada linea del sello con su valor correspondiente a linies
                linies.addAll(processaLinia(liniaSello));
            }

            // Si hay algo que mostrar en el sello
            if (!linies.isEmpty()) {

                // Busca la línea más larga para controlar que no salga del pdf
                float max = ElementSello.maxPosx;
                float may = ElementSello.maxPosy;

                // Realiza el cálculo de las coordenadas para que no salga del pdf
                float fx = calculaOrigenX(x, max, orientacion);
                float fy = calculaOrigenY(y, may, orientacion);

                // Si tiene definido LogoSello
                if (logoSello != null && entidad.getLogoSello() != null) {
                    File file = FileSystemManager.getArchivo(entidad.getLogoSello().getId());
                    Image imatgeSello = Image.getInstance(file.getAbsolutePath());
                    float heigh = imatgeSello.getHeight();
                    float width;
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

                // Recorre cada una de las lineas de ElementSello
                for(ElementSello element: linies) {
                    Font font = new Font(element.getBf(), element.getFontSize(), element.getFontStyle(), element.getColor());
                    Phrase frase = new Phrase(element.getText(), font);
                    PdfContentByte canvas = writer.getDirectContent();

                    ColumnText.showTextAligned(canvas, element.getAlineacio(), frase, fx + element.getPosx(), fy + element.getPosy(), 0);
                }
            }

            //Auto-Impresión
            writer.addJavaScript("this.print(true, this.pageNum, this.pageNum);");
            writer.getDirectContent().setAction(PdfAction.javaScript("this.print(true, this.pageNum, this.pageNum);", writer), 0f, 0f, 800f, 830f);

            // Resetea los valores de ElementSello
            ElementSello.clear();
        }

        // Crea el pdf
        String nombreFichero = "Segell_" + numRegformat + ".pdf";

        // Cabeceras Response
        response.setContentType("application/pdf");
        response.setHeader("Cache-Control",  "store");
        response.setHeader("Pragma", "cache");
        response.setHeader("Content-Disposition","inline; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");
    }

    /**
     * Calcula el origen de la coordenada Y para imprimir el sello, teniendo en cuenta el tamaño del valor del sello y la posición elegida
     * para el sello
     * @param y
     * @param max
     * @param orientacion
     * @return
     */
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

    /**
     * Calcula el origen de la coordenada X para imprimir el sello, teniendo en cuenta el tamaño del valor del sello y la posición elegida
     * para el sello
     * @param x
     * @param max
     * @param orientacion
     * @return
     */
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

    /**
     * Recorre y Genera una linea del sello sustituyendo el parámetro contenido por su valor
     * @param liniaSello
     * @return
     * @throws Exception
     */
    private java.util.List<ElementSello> processaLinia(String liniaSello) throws Exception {

        java.util.List<ElementSello> linies = new ArrayList<ElementSello>();
        java.util.List<ElementSello> partLinia = new ArrayList<ElementSello>();

        if (StringUtils.isEmpty(liniaSello)) {
            partLinia.add(new ElementSello("", false));
        } else {
            while (!StringUtils.isEmpty(liniaSello)) {
                if (liniaSello.contains("${")) {
                    // Texto anterior del primer parámetro
                    String preParam = liniaSello.substring(0, liniaSello.indexOf("${"));
                    if (!StringUtils.isEmpty(preParam)) {
                        partLinia.add(new ElementSello(preParam, false));
                    }
                    // Substituye el parámetro de la línea del sello por su valor
                    String param = liniaSello.substring(liniaSello.indexOf("${") + 2, liniaSello.indexOf("}"));
                    ElementSello ls = new ElementSello(param, true);
                    // Si el parámetro es el logoSello
                    if (ls.getParam().equalsIgnoreCase("logoSello")) {
                        logoSello = ls;
                    } else {  // Si es un parámetro diferente de logoSello
                        ls.setText(getParamValue(ls.getParam()));
                        // Revisa si cambia la posición Y, y actualiza en este caso
                        if (ls.isChangedPosy()) {
                            actualitzaPos(partLinia);
                            linies.addAll(partLinia);
                            partLinia = new ArrayList<ElementSello>();
                            partLinia.add(ls);
                        } else {
                            partLinia.add(ls);
                        }
                    }
                    // Texto posterior al primer paràmetre
                    liniaSello = liniaSello.substring(liniaSello.indexOf("}") + 1);
                } else {
                    partLinia.add(new ElementSello(liniaSello, false));
                    break;
                }
            }
        }

        // Recalcula la alineación adecuada de la linea
        actualitzaPos(partLinia);
        linies.addAll(partLinia);

        return linies;
    }

    /**
     * Alinea todos los elementos que componen una misma linea (texto y parámetros)
     * @param linies
     */
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

    /**
     * Asigna el valor al parámetro del formato de sello
     * @param paramName
     * @return
     */
    private String getParamValue(String paramName) {
        if ("codiOficina".equalsIgnoreCase(paramName)) {
            return codiOficina;
        } else if ("nomOficina".equalsIgnoreCase(paramName)) {
            return nomOficina;
        } else if ("numRegistre".equalsIgnoreCase(paramName)) {
            return numRegistre;
        } else if ("formatNumRegistre".equalsIgnoreCase(paramName)) {
            return numRegformat;
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
            return (!StringUtils.isEmpty(destinatari)) ? destinatari : "";
        } else if ("origen".equalsIgnoreCase(paramName)) {
            return (!StringUtils.isEmpty(origen)) ? origen : "";
        } else if ("tipusRegistre".equalsIgnoreCase(paramName)) {
            return tipusRegistre;
        } else if ("extracte".equalsIgnoreCase(paramName)) {
            return (!StringUtils.isEmpty(extracte)) ? extracte : "";
        } else if ("llibre".equalsIgnoreCase(paramName)) {
            return llibre;
        } else if ("nomUsuari".equalsIgnoreCase(paramName)) {
            return nomUsuari;
        } else if ("nomUsuariComplet".equalsIgnoreCase(paramName)) {
            return nomUsuariComplet;
        } else if ("decodificacioEntitat".equalsIgnoreCase(paramName)) {
            return decodificacioEntitat;
        } else if ("formatNumRegistre".equalsIgnoreCase(paramName)) {
            return numRegformat;
        } else if ("tipusRegistreComplet".equalsIgnoreCase(paramName)) {
            return tipusRegistreComplet;
        } else if ("tipusRegistreCompletCatala".equalsIgnoreCase(paramName)) {
            return tipusRegistreCompletCatala;
        } else if ("tipusRegistreCompletCastella".equalsIgnoreCase(paramName)) {
            return tipusRegistreCompletCastella;
        }
        return paramName;
    }
}
