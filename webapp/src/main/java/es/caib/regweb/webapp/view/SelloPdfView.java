package es.caib.regweb.webapp.view;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.AbstractIText5PdfView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Map;


/**
 * Created by Fundació BI
 * User: earrivi
 * Date: 11/04/14
 */
public class SelloPdfView extends AbstractIText5PdfView {

    protected final Logger log = Logger.getLogger(getClass());

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Object registro = model.get("registro");

        String x = (String) model.get("x");
        String y = (String) model.get("y");
        String orientacion = (String) model.get("orientacion");

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

        Font helvetica = new Font(Font.FontFamily.HELVETICA, 10);

        String sello = null;
        //String numRegistreCompost = null;
        String codiOficina = null;
        String nomOficina = null;
        String numRegistre = null;
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        String anyRegistre = null;
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String dataRegistre = null;
        String destinatari = null;
        String origen = null;
        String tipusRegistre = null;
        String extracte = null;
        String llibre = null;
        //String numLlibre = null;
        //String nomLlibre = null;
        String nomUsuari = null;
        String nomUsuariComplet = null;
        String entitat = null;
        String decodificacioEntitat = null;
        String formatNumRegistre = null;

        // Registre Entrada
        if(registro.getClass().getSimpleName().equals("RegistroEntrada")){

            RegistroEntrada registroEntrada = (RegistroEntrada) model.get("registro");

            sello = registroEntrada.getUsuario().getEntidad().getSello();
            formatNumRegistre = registroEntrada.getUsuario().getEntidad().getNumRegistro();

            if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)){

                codiOficina = registroEntrada.getOficina().getCodigo();
                nomOficina = registroEntrada.getOficina().getDenominacion();
                numRegistre = String.valueOf(registroEntrada.getNumeroRegistro());
                anyRegistre = formatYear.format(registroEntrada.getFecha());
                dataRegistre = formatDate.format(registroEntrada.getFecha());
                if(registroEntrada.getDestino() != null){
                    destinatari = registroEntrada.getDestino().getDenominacion();
                }else{
                    destinatari = registroEntrada.getDestinoExternoDenominacion();
                }
                tipusRegistre = "E";
                extracte = registroEntrada.getRegistroDetalle().getExtracto();
                llibre = registroEntrada.getLibro().getNombre();
                nomUsuari = registroEntrada.getUsuario().getUsuario().getNombre();
                nomUsuariComplet = registroEntrada.getUsuario().getNombreCompleto();
                entitat = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getNombre();
                decodificacioEntitat = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getDescripcion();
                //numLlibre = registroEntrada.getLibro().getCodigo();
                //nomLlibre = registroEntrada.getLibro().getNombre();

                formatNumRegistre = registroEntrada.getNumeroRegistroFormateado();

            }else if(registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){

                codiOficina = registroEntrada.getOficina().getCodigo();
                nomOficina = registroEntrada.getOficina().getDenominacion();
                numRegistre = String.valueOf(registroEntrada.getNumeroRegistro());
                anyRegistre = formatYear.format(registroEntrada.getFecha());
                dataRegistre = formatDate.format(registroEntrada.getFecha());
                //destinatari = registroEntrada.getOrganismoDestino().getDenominacion();
                tipusRegistre = "E";
                //extracte = registroEntrada.getExtracto();
                llibre = registroEntrada.getLibro().getNombre();
                nomUsuari = registroEntrada.getUsuario().getUsuario().getNombre();
                nomUsuariComplet = registroEntrada.getUsuario().getNombreCompleto();
                entitat = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getNombre();
                decodificacioEntitat = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getDescripcion();
                //numLlibre = registroEntrada.getLibro().getCodigo();

                formatNumRegistre = registroEntrada.getNumeroRegistroFormateado();
            }


        }

        // Registre Sortida
        if(registro.getClass().getSimpleName().equals("RegistroSalida")){

            RegistroSalida registroSalida = (RegistroSalida) model.get("registro");

            sello = registroSalida.getUsuario().getEntidad().getSello();
            formatNumRegistre = registroSalida.getUsuario().getEntidad().getNumRegistro();

            codiOficina = registroSalida.getOficina().getCodigo();
            nomOficina = registroSalida.getOficina().getDenominacion();
            numRegistre = String.valueOf(registroSalida.getNumeroRegistro());
            anyRegistre = formatYear.format(registroSalida.getFecha());
            dataRegistre = formatDate.format(registroSalida.getFecha());
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
            //numLlibre = registroSalida.getLibro().getCodigo();


            formatNumRegistre = registroSalida.getNumeroRegistroFormateado();

        }

        if(sello != null){

            sello = sello.replace("${codiOficina}", codiOficina);
            sello = sello.replace("${nomOficina}", nomOficina);
            sello = sello.replace("${numRegistre}", numRegistre);
            sello = sello.replace("${formatNumRegistre}", formatNumRegistre);
            sello = sello.replace("${anyRegistre}", anyRegistre);
            sello = sello.replace("${dataRegistre}", dataRegistre);
            sello = sello.replace("${entitat}", entitat);
            if(destinatari!=null && destinatari.length()>0){
                sello = sello.replace("${destinatari}", destinatari);
            }else{
                sello = sello.replace("${destinatari}", "");
            }
            if(origen!=null && origen.length()>0){
                sello = sello.replace("${origen}", origen);
            }else{
                sello = sello.replace("${origen}", "");
            }
            sello = sello.replace("${tipusRegistre}", tipusRegistre);
            if(extracte!=null && extracte.length()>0){sello = sello.replace("${extracte}", extracte);}
            sello = sello.replace("${llibre}", llibre);
            sello = sello.replace("${nomUsuari}", nomUsuari);
            sello = sello.replace("${nomUsuariComplet}", nomUsuariComplet);
            sello = sello.replace("${decodificacioEntitat}", decodificacioEntitat);
            sello = sello.replace("${formatNumRegistre}", formatNumRegistre);

            //Posam el contingut del segell en linies
            boolean sortir = false;
            int n=0;
            int k=0;
            String segellPdf[] = new String[20];
            String linia = "";
            if(sello.length()>0){
                while(!sortir){
                    linia = "";
                    for(int i = k; i < sello.length(); i++) {
                        if(i!=sello.length()-1){
                            if (sello.charAt(i) == '\n') {
                                k=i;
                                break;
                            } else{
                                linia = linia + sello.charAt(i);
                            }
                        } else{
                            linia = linia + sello.charAt(i);
                            sortir=true;
                            break;
                        }
                    }
                    k = k +1;
                    segellPdf[n]=linia;
                    n=n+1;
                }
            }

            // Cerca la línia més llarga per controlar que no surti del pdf
            int max = 0;
            int linies = 0;
            for(int j=0; j<n; j++) {
                if(segellPdf[j].length()>max){
                    max = segellPdf[j].length();
                }
                if(!segellPdf[j].equals("")){
                    linies = linies +1;
                }
            }

            if(orientacion.equals("V")){
                // HORITZONTAL (X): Si el segell surt del pdf, endarrereix el segell
                Float pixelX = Float.valueOf(x);
                if(pixelX<15){
                    x = Integer.toString(15);
                }else{
                    int maxpixelX = 582;
                    Float segellpixelX = (max * 6) + pixelX;
                    if(segellpixelX > maxpixelX){
                        Float diferenciaX = segellpixelX - maxpixelX;
                        int nuevaX = (int) (Float.valueOf(x) - diferenciaX);
                        if(nuevaX<15){
                            nuevaX = 15;
                        }
                        x = Integer.toString(nuevaX);
                    }
                }
                // VERTICAL (Y): Si el segell surt del pdf, puja el segell
                Float pixelY = Float.valueOf(y);
                if(pixelY>827){
                    y = Integer.toString(827);
                }else{
                    Float segellpixelY = pixelY - (linies * 12) - 15;
                    if(segellpixelY < 0){
                        int nuevaY = (int) (Float.valueOf(y) - segellpixelY);
                        if(nuevaY>827){
                            nuevaY = 827;
                        }
                        y = Integer.toString(nuevaY);
                    }
                }
            }

            if(orientacion.equals("H")){
                // HORITZONTAL (X): Si el segell surt del pdf, endarrereix el segell
                Float pixelX = Float.valueOf(x);
                if(pixelX<15){
                    x = Integer.toString(15);
                }else{
                    int maxpixelX = 827;
                    Float segellpixelX = (max * 6) + pixelX;
                    if(segellpixelX > maxpixelX){
                        Float diferenciaX = segellpixelX - maxpixelX;
                        int nuevaX = (int) (Float.valueOf(x) - diferenciaX);
                        if(nuevaX<15){
                            nuevaX = 15;
                        }
                        x = Integer.toString(nuevaX);
                    }
                }
                // VERTICAL (Y): Si el segell surt del pdf, puja el segell
                Float pixelY = Float.valueOf(y);
                if(pixelY>582){
                    y = Integer.toString(582);
                }else{
                    Float segellpixelY = pixelY - (linies * 12) - 15;
                    if(segellpixelY < 0){
                        int nuevaY = (int) (Float.valueOf(y) - segellpixelY);
                        if(nuevaY>582){
                            nuevaY = 582;
                        }
                        y = Integer.toString(nuevaY);
                    }
                }
            }


            for(int j=0; j<n; j++) {
                Phrase frase = new Phrase(segellPdf[j], helvetica);
                PdfContentByte canvas = writer.getDirectContent();
                //int yu = (int) (Float.valueOf(y)-12*j);
                ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, frase, Float.valueOf(x), Float.valueOf(y)-12*j, 0);
            }

            //Auto-Impresión
            writer.addJavaScript("this.print(true, this.pageNum, this.pageNum);");
            writer.getDirectContent().setAction(PdfAction.javaScript("this.print(true, this.pageNum, this.pageNum);", writer), 0f, 0f, 800f, 830f);

        }

        String nombreFichero = "Segell_" + formatNumRegistre + ".pdf";

        // Cabeceras Response
        response.setContentType("application/pdf");
        response.setHeader("Cache-Control",  "store");
        response.setHeader("Pragma", "cache");
        response.setHeader("Content-Disposition","inline; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");
    }
}
