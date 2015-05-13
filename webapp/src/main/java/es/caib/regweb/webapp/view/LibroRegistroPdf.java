package es.caib.regweb.webapp.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.AbstractIText5PdfView;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 6/05/14
 */
public class LibroRegistroPdf extends AbstractIText5PdfView {


    protected final Logger log = Logger.getLogger(getClass());

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Obtenemos mapas y arraays de valores
        String tipo = (String) model.get("tipo");
        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        Set<String> campos = (Set<String>) model.get("campos");
        ArrayList<ArrayList<String>> registrosLibro = (ArrayList<ArrayList<String>>) model.get("registrosLibro");

        //Configuraciones generales formato pdf
        if(campos.size() > 4){
            document.setPageSize(PageSize.A4.rotate());
        } else{
            document.setPageSize(PageSize.A4);
        }

        document.addAuthor("REGWEB");
        document.addCreationDate();
        document.addCreator("iText library");
        document.newPage();
        Font font10Bold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font font14Bold = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);

        // Título
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        titulo.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        titulo.getDefaultCell().setBorder(0);
        titulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        document.addTitle("Informe Llibre de Registres");
        titulo.addCell(new Paragraph("Llibre de Registres", font14Bold));
        titulo.addCell(new Paragraph("Tipus: " + tipo));
        titulo.addCell(new Paragraph("Data Inici: " + fechaInicio));
        titulo.addCell(new Paragraph("Data Fi: " + fechaFin));
        document.add(titulo);
        document.add(new Paragraph(" "));

        //Registros
        PdfPTable table = new PdfPTable(campos.size());
        table.setWidthPercentage(100);

        if((campos != null)&&registrosLibro.size()>0){

            if(campos.size()!=0){

                for (String valorCamp : campos) {
                    if (valorCamp.equals("codAs")) {
                        PdfPCell cell1 = new PdfPCell(new Paragraph("CODI ASSUMPTE", font10Bold));
                        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell1);
                    }else if (valorCamp.equals("anyRe")) {
                        PdfPCell cell2 = new PdfPCell(new Paragraph("ANY REGISTRE", font10Bold));
                        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell2);
                    }else if (valorCamp.equals("estat")) {
                        PdfPCell cell3 = new PdfPCell(new Paragraph("ESTAT", font10Bold));
                        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell3);
                    }else if (valorCamp.equals("exped")) {
                        PdfPCell cell4 = new PdfPCell(new Paragraph("EXPEDIENT", font10Bold));
                        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell4);
                    }else if (valorCamp.equals("extra")) {
                        PdfPCell cell5 = new PdfPCell(new Paragraph("EXTRACTE", font10Bold));
                        cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell5);
                    }else if (valorCamp.equals("datOr")) {
                        PdfPCell cell6 = new PdfPCell(new Paragraph("DATA ORIGEN", font10Bold));
                        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell6);
                    }else if (valorCamp.equals("numRe")) {
                        PdfPCell cell7 = new PdfPCell(new Paragraph("NUM REGISTRE", font10Bold));
                        cell7.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell7);
                    }else if (valorCamp.equals("ofici")) {
                        PdfPCell cell8 = new PdfPCell(new Paragraph("OFICINA", font10Bold));
                        cell8.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell8);
                    }else if (valorCamp.equals("tipAs")) {
                        PdfPCell cell9 = new PdfPCell(new Paragraph("TIPUS ASSUMPTE", font10Bold));
                        cell9.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell9);
                    }else if (valorCamp.equals("obser")) {
                        PdfPCell cell10 = new PdfPCell(new Paragraph("OBSERVACIONS", font10Bold));
                        cell10.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell10);
                    }else if (valorCamp.equals("llibr")) {
                        PdfPCell cell11 = new PdfPCell(new Paragraph("LLIBRE", font10Bold));
                        cell11.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell11);
                    }else if (valorCamp.equals("data")) {
                        PdfPCell cell12 = new PdfPCell(new Paragraph("DATA REGISTRE", font10Bold));
                        cell12.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell12);
                    }else if (valorCamp.equals("docFi")) {
                        PdfPCell cell13 = new PdfPCell(new Paragraph("DOC FÍSICA", font10Bold));
                        cell13.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell13);
                    }else if (valorCamp.equals("orgDe")) {
                        if (tipo.equals(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO)) {
                            PdfPCell cell14 = new PdfPCell(new Paragraph("ORGANISME DESTÍ", font10Bold));
                            cell14.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell14);
                        }
                        if (tipo.equals(RegwebConstantes.REGISTRO_SALIDA_ESCRITO)) {
                            PdfPCell cell14 = new PdfPCell(new Paragraph("ORGANISME ORIGEN", font10Bold));
                            cell14.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell14);
                        }
                    }else if (valorCamp.equals("idiom")) {
                        PdfPCell cell15 = new PdfPCell(new Paragraph("IDIOMA", font10Bold));
                        cell15.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell15);
                    }else if (valorCamp.equals("refEx")) {
                        PdfPCell cell16 = new PdfPCell(new Paragraph("REF EXTERNA", font10Bold));
                        cell16.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell16);
                    }else if (valorCamp.equals("trans")) {
                        PdfPCell cell17 = new PdfPCell(new Paragraph("TRANSPORT", font10Bold));
                        cell17.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell17);
                    }else if (valorCamp.equals("numTr")) {
                        PdfPCell cell18 = new PdfPCell(new Paragraph("NUM TRANSPORT", font10Bold));
                        cell18.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell18);
                    }else if (valorCamp.equals("orgOr")) {
                        PdfPCell cell19 = new PdfPCell(new Paragraph("OFICINA ORIGEN", font10Bold));
                        cell19.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell19);
                    }else if (valorCamp.equals("numOr")) {
                        PdfPCell cell20 = new PdfPCell(new Paragraph("NUM REGISTRE ORIGEN", font10Bold));
                        cell20.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell20);
                    }else if (valorCamp.equals("nomIn")) {
                        PdfPCell cell21 = new PdfPCell(new Paragraph("INTERESSATS", font10Bold));
                        cell21.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell21);
                    }
                }
            }

            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            for (ArrayList<String> aRegistrosLibro : registrosLibro) {
                for (int j = 0; j < aRegistrosLibro.size(); j++) {
                    table.addCell(new Paragraph(aRegistrosLibro.get(j), font10));
                }
            }

            document.add(table);

        }
        if(registrosLibro.size()==0){
            PdfPTable buit = new PdfPTable(1);
            buit.setWidthPercentage(100);
            buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            buit.getDefaultCell().setBorder(0);
            buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            buit.addCell(new Paragraph("No hi ha registres per mostrar", font10Bold));
            document.add(buit);
        }

        String nombreFichero = "Llibre_Registre_"+ tipo +".pdf";

        // Cabeceras Response
        response.setHeader("Content-Disposition","attachment; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");

    }
}