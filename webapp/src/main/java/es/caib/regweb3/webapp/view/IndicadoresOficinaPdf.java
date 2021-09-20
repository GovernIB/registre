package es.caib.regweb3.webapp.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb3.webapp.utils.AbstractIText5PdfView;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 6/05/14
 */
public class IndicadoresOficinaPdf extends AbstractIText5PdfView{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private ArrayList<String> entradaAnosValor = new ArrayList<String>();
    private ArrayList<String> entradaAnosNombre = new ArrayList<String>();
    private ArrayList<String> salidaAnosValor = new ArrayList<String>();
    private ArrayList<String> salidaAnosNombre = new ArrayList<String>();
    private ArrayList<String> entradaMesesValor = new ArrayList<String>();
    private ArrayList<String> entradaMesesNombre = new ArrayList<String>();
    private ArrayList<String> salidaMesesValor = new ArrayList<String>();
    private ArrayList<String> salidaMesesNombre = new ArrayList<String>();
    private ArrayList<String> entradaIdiomaValor = new ArrayList<String>();
    private ArrayList<String> entradaIdiomaNombre = new ArrayList<String>();
    private ArrayList<String> salidaIdiomaValor = new ArrayList<String>();
    private ArrayList<String> salidaIdiomaNombre = new ArrayList<String>();
    /**
     * Retorna el mensaje traducido según el idioma del usuario
     * @param key
     * @return
     */
    protected String getMessage(String key){
        return I18NUtils.tradueix(key);
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Obtenemos mapas y arrays de valores

        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        String nombreOficina = (String) model.get("nombreOficina");
        Integer registrosEntrada = (Integer) model.get("registrosEntrada");
        Integer registrosSalida = (Integer) model.get("registrosSalida");
        String codigoOficina = (String) model.get("codigoOficina");

        obtenerValoresEntrada(model);
        obtenerValoresSalida(model);

        //Configuraciones generales formato pdf
        document.setPageSize(PageSize.A4);

        document.addAuthor("REGWEB3");
        document.addCreationDate();
        document.addCreator("iText library");
        document.newPage();
        Font font10Bold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font font14Bold = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font font14BoldItalic = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC);

        // Título
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        titulo.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        titulo.getDefaultCell().setBorder(0);
        titulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        document.addTitle(getMessage("informe.indicadoresOficina"));
        titulo.addCell(new Paragraph(getMessage("informe.indicadoresOficina"), font14Bold));
        titulo.addCell(new Paragraph(getMessage("informe.oficina") +": " + nombreOficina));
        titulo.addCell(new Paragraph(getMessage("informe.fechaInicio") +": " + fechaInicio));
        titulo.addCell(new Paragraph(getMessage("informe.fechaFin") +": " + fechaFin));
        document.add(titulo);
        document.add(new Paragraph(" "));

        // DATOS A MOSTRAR
        // ENTRADA
        PdfPTable seccion = new PdfPTable(1);
        seccion.setWidthPercentage(100);
        seccion.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion.getDefaultCell().setBorder(0);
        seccion.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion.addCell(new Paragraph(getMessage("informe.registrosEntrada"), font14BoldItalic));
        document.add(seccion);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell1 = new PdfPCell(new Paragraph(getMessage("informe.registros"),font10Bold));
        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell1);
        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(new Paragraph(registrosEntrada.toString(),font10));
        document.add(table);
        document.add(new Paragraph(" "));

        //Muestra Años
        PdfPTable seccion11 = new PdfPTable(1);
        seccion11.setWidthPercentage(100);
        seccion11.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion11.getDefaultCell().setBorder(0);
        seccion11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion11.addCell(new Paragraph(getMessage("informe.anys"), font10Bold));
        document.add(seccion11);
        document.add(new Paragraph(" "));

        PdfPTable tableAnysEntrada = new PdfPTable(2);
        tableAnysEntrada.setWidthPercentage(100);

        for(int i=0;i<entradaAnosNombre.size();i++){
            tableAnysEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableAnysEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String numAny = entradaAnosNombre.get(i);
            tableAnysEntrada.addCell(new Paragraph(numAny,font10));
            tableAnysEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableAnysEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorAny = entradaAnosValor.get(i);
            tableAnysEntrada.addCell(new Paragraph(valorAny,font10));
            document.add(tableAnysEntrada);
            tableAnysEntrada.deleteBodyRows();
        }
        tableAnysEntrada.deleteBodyRows();
        tableAnysEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableAnysEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysEntrada.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableAnysEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableAnysEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysEntrada.addCell(new Paragraph(registrosEntrada.toString(),font10));
        document.add(tableAnysEntrada);
        document.add(new Paragraph(" "));


        //Muestra Meses
        PdfPTable seccion12 = new PdfPTable(1);
        seccion12.setWidthPercentage(100);
        seccion12.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion12.getDefaultCell().setBorder(0);
        seccion12.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion12.addCell(new Paragraph(getMessage("informe.mesos"), font10Bold));
        document.add(seccion12);
        document.add(new Paragraph(" "));

        PdfPTable tableMesesEntrada = new PdfPTable(2);
        tableMesesEntrada.setWidthPercentage(100);

        for(int i=0;i<entradaMesesNombre.size();i++){
            tableMesesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableMesesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String nomMes = entradaMesesNombre.get(i);
            tableMesesEntrada.addCell(new Paragraph(nomMes,font10));
            tableMesesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableMesesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorMes = entradaMesesValor.get(i);
            tableMesesEntrada.addCell(new Paragraph(valorMes,font10));
            document.add(tableMesesEntrada);
            tableMesesEntrada.deleteBodyRows();
        }
        tableMesesEntrada.deleteBodyRows();
        tableMesesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableMesesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesEntrada.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableMesesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableMesesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesEntrada.addCell(new Paragraph(registrosEntrada.toString(),font10));
        document.add(tableMesesEntrada);
        document.add(new Paragraph(" "));

//            //Muestra Consellerias
//            if(entradaConselleriaNombre.size() > 0){
//                PdfPTable seccion13 = new PdfPTable(1);
//                seccion13.setWidthPercentage(100);
//                seccion13.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                seccion13.getDefaultCell().setBorder(0);
//                seccion13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                seccion13.addCell(new Paragraph(getMessage("informe.porOrganismos"), font10Bold));
//                document.add(seccion13);
//                document.add(new Paragraph(" "));
//
//                PdfPTable tableConselleriesEntrada = new PdfPTable(2);
//                tableConselleriesEntrada.setWidthPercentage(100);
//
//                for(int i=0;i<entradaConselleriaNombre.size();i++){
//                    tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
//                    tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    String nomConsell = entradaConselleriaNombre.get(i);
//                    tableConselleriesEntrada.addCell(new Paragraph(nomConsell,font10));
//                    tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                    tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    String valorConsell = entradaConselleriaValor.get(i);
//                    tableConselleriesEntrada.addCell(new Paragraph(valorConsell,font10));
//                    document.add(tableConselleriesEntrada);
//                    tableConselleriesEntrada.deleteBodyRows();
//                }
//                tableConselleriesEntrada.deleteBodyRows();
//                tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
//                tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                tableConselleriesEntrada.addCell(new Paragraph(getMessage("informe.total"),font10));
//                tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                tableConselleriesEntrada.addCell(new Paragraph(registrosEntrada.toString(),font10));
//                document.add(tableConselleriesEntrada);
//                document.add(new Paragraph(" "));
//            }
//
//            //Muestra Tipos de Asunto
//            if(entradaAsuntoNombre.size() > 0){
//                PdfPTable seccion14 = new PdfPTable(1);
//                seccion14.setWidthPercentage(100);
//                seccion14.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                seccion14.getDefaultCell().setBorder(0);
//                seccion14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                seccion14.addCell(new Paragraph(getMessage("informe.porTiposAsunto"), font10Bold));
//                document.add(seccion14);
//                document.add(new Paragraph(" "));
//
//                PdfPTable tableAssumptesEntrada = new PdfPTable(2);
//                tableAssumptesEntrada.setWidthPercentage(100);
//
//                for(int i=0;i<entradaAsuntoNombre.size();i++){
//                    tableAssumptesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
//                    tableAssumptesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    String nomAsunto = entradaAsuntoNombre.get(i);
//                    tableAssumptesEntrada.addCell(new Paragraph(nomAsunto,font10));
//                    tableAssumptesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                    tableAssumptesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    String valorAsunto = entradaAsuntoValor.get(i);
//                    tableAssumptesEntrada.addCell(new Paragraph(valorAsunto,font10));
//                    document.add(tableAssumptesEntrada);
//                    tableAssumptesEntrada.deleteBodyRows();
//                }
//                document.add(new Paragraph(" "));
//            }
//
//            //Muestra Libros de Registro
//            if(entradaLibroNombre.size() > 0){
//                PdfPTable seccion15 = new PdfPTable(1);
//                seccion15.setWidthPercentage(100);
//                seccion15.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                seccion15.getDefaultCell().setBorder(0);
//                seccion15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//                seccion15.addCell(new Paragraph(getMessage("informe.porLibroRegistro"), font10Bold));
//                document.add(seccion15);
//                document.add(new Paragraph(" "));
//
//                PdfPTable tableLlibresEntrada = new PdfPTable(2);
//                tableLlibresEntrada.setWidthPercentage(100);
//
//                for(int i=0;i<entradaLibroNombre.size();i++){
//                    tableLlibresEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
//                    tableLlibresEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    String nomLibro = entradaLibroNombre.get(i);
//                    tableLlibresEntrada.addCell(new Paragraph(nomLibro,font10));
//                    tableLlibresEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                    tableLlibresEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                    String valorLibro = entradaLibroValor.get(i);
//                    tableLlibresEntrada.addCell(new Paragraph(valorLibro,font10));
//                    document.add(tableLlibresEntrada);
//                    tableLlibresEntrada.deleteBodyRows();
//                }
//                document.add(new Paragraph(" "));
//            }

//        //Muestra Oficinas
//        if(entradaOficinaNombre.size() > 0){
//            PdfPTable seccion16 = new PdfPTable(1);
//            seccion16.setWidthPercentage(100);
//            seccion16.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//            seccion16.getDefaultCell().setBorder(0);
//            seccion16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
//            seccion16.addCell(new Paragraph(getMessage("informe.porOficina"), font10Bold));
//            document.add(seccion16);
//            document.add(new Paragraph(" "));
//
//            PdfPTable tableOficinesEntrada = new PdfPTable(2);
//            tableOficinesEntrada.setWidthPercentage(100);
//
//            for(int i=0;i<entradaOficinaNombre.size();i++){
//                tableOficinesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
//                tableOficinesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                String nomOficina = entradaOficinaNombre.get(i);
//                tableOficinesEntrada.addCell(new Paragraph(nomOficina,font10));
//                tableOficinesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
//                tableOficinesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//                String valorOficina = entradaOficinaValor.get(i);
//                tableOficinesEntrada.addCell(new Paragraph(valorOficina,font10));
//                document.add(tableOficinesEntrada);
//                tableOficinesEntrada.deleteBodyRows();
//            }
//            document.add(new Paragraph(" "));
//        }

        //Muestra Idiomas
        if(entradaIdiomaNombre.size() > 0){
            PdfPTable seccion112 = new PdfPTable(1);
            seccion112.setWidthPercentage(100);
            seccion112.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            seccion112.getDefaultCell().setBorder(0);
            seccion112.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            seccion112.addCell(new Paragraph(getMessage("informe.porIdioma"), font10Bold));
            document.add(seccion112);
            document.add(new Paragraph(" "));

            PdfPTable tableIdiomesEntrada = new PdfPTable(entradaIdiomaNombre.size());
            tableIdiomesEntrada.setWidthPercentage(100);
            tableIdiomesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableIdiomesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            for (String nomIdioma : entradaIdiomaNombre) {
                tableIdiomesEntrada.addCell(new Paragraph(nomIdioma, font10));
            }
            document.add(tableIdiomesEntrada);
            tableIdiomesEntrada.deleteBodyRows();
            tableIdiomesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableIdiomesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            for (String valorIdioma : entradaIdiomaValor) {
                tableIdiomesEntrada.addCell(new Paragraph(valorIdioma, font10));
            }
            document.add(tableIdiomesEntrada);
            document.add(new Paragraph(" "));
        }


        // Final Entrades
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        if(registrosEntrada.equals(0)){
            PdfPTable buit = new PdfPTable(1);
            buit.setWidthPercentage(100);
            buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            buit.getDefaultCell().setBorder(0);
            buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
            document.add(buit);
        }


        // SALIDA

        PdfPTable seccion2 = new PdfPTable(1);
        seccion2.setWidthPercentage(100);
        seccion2.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion2.getDefaultCell().setBorder(0);
        seccion2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion2.addCell(new Paragraph(getMessage("informe.registrosSalida"), font14BoldItalic));
        document.add(seccion2);
        document.add(new Paragraph(" "));

        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100);
        PdfPCell cell2 = new PdfPCell(new Paragraph(getMessage("informe.registros"),font10Bold));
        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(cell2);
        table2.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table2.addCell(new Paragraph(registrosSalida.toString(),font10));
        document.add(table2);
        document.add(new Paragraph(" "));

        //Muestra Años
        PdfPTable seccion21 = new PdfPTable(1);
        seccion21.setWidthPercentage(100);
        seccion21.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion21.getDefaultCell().setBorder(0);
        seccion21.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion21.addCell(new Paragraph(getMessage("informe.anys"), font10Bold));
        document.add(seccion21);
        document.add(new Paragraph(" "));

        PdfPTable tableAnysSortida = new PdfPTable(2);
        tableAnysSortida.setWidthPercentage(100);

        for(int i=0;i<salidaAnosNombre.size();i++){
            tableAnysSortida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableAnysSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String numAny = salidaAnosNombre.get(i);
            tableAnysSortida.addCell(new Paragraph(numAny,font10));
            tableAnysSortida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableAnysSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorAny = salidaAnosValor.get(i);
            tableAnysSortida.addCell(new Paragraph(valorAny,font10));
            document.add(tableAnysSortida);
            tableAnysSortida.deleteBodyRows();
        }
        tableAnysSortida.deleteBodyRows();
        tableAnysSortida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableAnysSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysSortida.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableAnysSortida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableAnysSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysSortida.addCell(new Paragraph(registrosSalida.toString(),font10));
        document.add(tableAnysSortida);
        document.add(new Paragraph(" "));

        //Muestra Meses
        PdfPTable seccion22 = new PdfPTable(1);
        seccion22.setWidthPercentage(100);
        seccion22.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion22.getDefaultCell().setBorder(0);
        seccion22.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion22.addCell(new Paragraph(getMessage("informe.mesos"), font10Bold));
        document.add(seccion22);
        document.add(new Paragraph(" "));

        PdfPTable tableMesesSalida = new PdfPTable(2);
        tableMesesSalida.setWidthPercentage(100);

        for(int i=0;i<salidaMesesNombre.size();i++){
            tableMesesSalida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableMesesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String nomMes = salidaMesesNombre.get(i);
            tableMesesSalida.addCell(new Paragraph(nomMes,font10));
            tableMesesSalida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableMesesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorMes = salidaMesesValor.get(i);
            tableMesesSalida.addCell(new Paragraph(valorMes,font10));
            document.add(tableMesesSalida);
            tableMesesSalida.deleteBodyRows();
        }
        tableMesesSalida.deleteBodyRows();
        tableMesesSalida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableMesesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesSalida.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableMesesSalida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableMesesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesSalida.addCell(new Paragraph(registrosSalida.toString(),font10));
        document.add(tableMesesSalida);
        document.add(new Paragraph(" "));


        //Muestra Idiomas
        if(salidaIdiomaNombre.size() > 0){
            PdfPTable seccion212 = new PdfPTable(1);
            seccion212.setWidthPercentage(100);
            seccion212.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            seccion212.getDefaultCell().setBorder(0);
            seccion212.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            seccion212.addCell(new Paragraph(getMessage("informe.porIdioma"), font10Bold));
            document.add(seccion212);
            document.add(new Paragraph(" "));

            PdfPTable tableIdiomesSalida = new PdfPTable(salidaIdiomaNombre.size());
            tableIdiomesSalida.setWidthPercentage(100);
            tableIdiomesSalida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
            tableIdiomesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            for (String nomIdioma : salidaIdiomaNombre) {
                tableIdiomesSalida.addCell(new Paragraph(nomIdioma, font10));
            }
            document.add(tableIdiomesSalida);
            tableIdiomesSalida.deleteBodyRows();
            tableIdiomesSalida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableIdiomesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            for (String valorIdioma : salidaIdiomaValor) {
                tableIdiomesSalida.addCell(new Paragraph(valorIdioma, font10));
            }
            document.add(tableIdiomesSalida);
            document.add(new Paragraph(" "));
        }


        // Final Sortides
        if(registrosSalida.equals(0)){
            PdfPTable buit = new PdfPTable(1);
            buit.setWidthPercentage(100);
            buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            buit.getDefaultCell().setBorder(0);
            buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
            document.add(buit);
        }

        String nombreFichero = getMessage("informe.nombreFichero.indicadoresOficina")+ codigoOficina + ".pdf";

        // Cabeceras Response
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");


    }

    /**
     *
     * @param model
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void obtenerValoresEntrada(Map<String, Object> model) throws Exception{
        entradaAnosValor = (ArrayList<String>) model.get("entradaAnosValor");
        entradaAnosNombre = (ArrayList<String>) model.get("entradaAnosNombre");
        entradaMesesValor = (ArrayList<String>) model.get("entradaMesesValor");
        entradaMesesNombre = (ArrayList<String>) model.get("entradaMesesNombre");
        entradaIdiomaValor = (ArrayList<String>) model.get("entradaIdiomaValor");
        entradaIdiomaNombre = (ArrayList<String>) model.get("entradaIdiomaNombre");
    }

    /**
     *
     * @param model
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private void obtenerValoresSalida(Map<String, Object> model) throws Exception{
        salidaAnosValor = (ArrayList<String>) model.get("salidaAnosValor");
        salidaAnosNombre = (ArrayList<String>) model.get("salidaAnosNombre");
        salidaMesesValor = (ArrayList<String>) model.get("salidaMesesValor");
        salidaMesesNombre = (ArrayList<String>) model.get("salidaMesesNombre");
        salidaIdiomaValor = (ArrayList<String>) model.get("salidaIdiomaValor");
        salidaIdiomaNombre = (ArrayList<String>) model.get("salidaIdiomaNombre");
    }

}

