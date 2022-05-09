package es.caib.regweb3.webapp.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb3.webapp.utils.AbstractIText5PdfView;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;

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

    protected final Logger log = Logger.getLogger(getClass());

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
    
    private ArrayList<String> sirEnviadosAnosValor = new ArrayList<String>();
    private ArrayList<String> sirEnviadosAnosNombre = new ArrayList<String>();
    private ArrayList<String> sirEnviadosMesesValor = new ArrayList<String>();
    private ArrayList<String> sirEnviadosMesesNombre = new ArrayList<String>();
    
    private ArrayList<String> sirRecibidosAnosValor = new ArrayList<String>();
    private ArrayList<String> sirRecibidosAnosNombre = new ArrayList<String>();
    private ArrayList<String> sirRecibidosMesesValor = new ArrayList<String>();
    private ArrayList<String> sirRecibidosMesesNombre = new ArrayList<String>();
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
        Integer sirEnviados = (Integer) model.get("sirEnviados");
        Integer sirRecibidos = (Integer) model.get("sirRecibidos");

        obtenerValoresEntrada(model);
        obtenerValoresSalida(model);
        obtenerValoresSirEnviados(model);
        obtenerValoresSirRecibidos(model);
        
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

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

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

        // DATOS A MOSTRAR
        // SIR ENVIADOS
        
        PdfPTable seccion3 = new PdfPTable(1);
        seccion3.setWidthPercentage(100);
        seccion3.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion3.getDefaultCell().setBorder(0);
        seccion3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion3.addCell(new Paragraph(getMessage("informe.registrosSirEnviados"), font14BoldItalic));
        document.add(seccion3);
        document.add(new Paragraph(" "));

        PdfPTable table3 = new PdfPTable(1);
        table3.setWidthPercentage(100);
        PdfPCell cell3 = new PdfPCell(new Paragraph(getMessage("informe.registros"),font10Bold));
        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table3.addCell(cell3);
        table3.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table3.addCell(new Paragraph(sirEnviados.toString(),font10));
        document.add(table3);
        document.add(new Paragraph(" "));
        
        //Muestra Años
        PdfPTable seccion31 = new PdfPTable(1);
        seccion31.setWidthPercentage(100);
        seccion31.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion31.getDefaultCell().setBorder(0);
        seccion31.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion31.addCell(new Paragraph(getMessage("informe.anys"), font10Bold));
        document.add(seccion31);
        document.add(new Paragraph(" "));

        PdfPTable tableAnysSirEnviados = new PdfPTable(2);
        tableAnysSirEnviados.setWidthPercentage(100);

        for(int i=0;i<sirEnviadosAnosNombre.size();i++){
        	tableAnysSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        	tableAnysSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String numAny = sirEnviadosAnosNombre.get(i);
            tableAnysSirEnviados.addCell(new Paragraph(numAny,font10));
            tableAnysSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableAnysSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorAny = sirEnviadosAnosValor.get(i);
            tableAnysSirEnviados.addCell(new Paragraph(valorAny,font10));
            document.add(tableAnysSirEnviados);
            tableAnysSirEnviados.deleteBodyRows();
        }
        tableAnysSirEnviados.deleteBodyRows();
        tableAnysSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableAnysSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysSirEnviados.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableAnysSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableAnysSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysSirEnviados.addCell(new Paragraph(registrosSalida.toString(),font10));
        document.add(tableAnysSirEnviados);
        document.add(new Paragraph(" "));

        //Muestra Meses
        PdfPTable seccion32 = new PdfPTable(1);
        seccion32.setWidthPercentage(100);
        seccion32.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion32.getDefaultCell().setBorder(0);
        seccion32.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion32.addCell(new Paragraph(getMessage("informe.mesos"), font10Bold));
        document.add(seccion32);
        document.add(new Paragraph(" "));

        PdfPTable tableMesesSirEnviados = new PdfPTable(2);
        tableMesesSirEnviados.setWidthPercentage(100);

        for(int i=0;i<sirEnviadosMesesNombre.size();i++){
        	tableMesesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        	tableMesesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String nomMes = sirEnviadosMesesNombre.get(i);
            tableMesesSirEnviados.addCell(new Paragraph(nomMes,font10));
            tableMesesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableMesesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorMes = sirEnviadosMesesValor.get(i);
            tableMesesSirEnviados.addCell(new Paragraph(valorMes,font10));
            document.add(tableMesesSirEnviados);
            tableMesesSirEnviados.deleteBodyRows();
        }
        tableMesesSirEnviados.deleteBodyRows();
        tableMesesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableMesesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesSirEnviados.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableMesesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableMesesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesSirEnviados.addCell(new Paragraph(registrosSalida.toString(),font10));
        document.add(tableMesesSirEnviados);
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        
        // Final SIR ENVIADOS
        if(sirEnviados.equals(0)){
            PdfPTable buit = new PdfPTable(1);
            buit.setWidthPercentage(100);
            buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            buit.getDefaultCell().setBorder(0);
            buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
            document.add(buit);
        }
        
        
        // DATOS A MOSTRAR
        // SIR RECIBIDOS
        
        PdfPTable seccion4 = new PdfPTable(1);
        seccion4.setWidthPercentage(100);
        seccion4.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion4.getDefaultCell().setBorder(0);
        seccion4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion4.addCell(new Paragraph(getMessage("informe.registrosSirRecibidos"), font14BoldItalic));
        document.add(seccion4);
        document.add(new Paragraph(" "));

        PdfPTable table4 = new PdfPTable(1);
        table4.setWidthPercentage(100);
        PdfPCell cell4 = new PdfPCell(new Paragraph(getMessage("informe.registros"),font10Bold));
        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        table4.addCell(cell4);
        table4.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table4.addCell(new Paragraph(sirRecibidos.toString(),font10));
        document.add(table4);
        document.add(new Paragraph(" "));
        
        //Muestra Años
        PdfPTable seccion41 = new PdfPTable(1);
        seccion41.setWidthPercentage(100);
        seccion41.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion41.getDefaultCell().setBorder(0);
        seccion41.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion41.addCell(new Paragraph(getMessage("informe.anys"), font10Bold));
        document.add(seccion41);
        document.add(new Paragraph(" "));

        PdfPTable tableAnysSirRecibidos = new PdfPTable(2);
        tableAnysSirRecibidos.setWidthPercentage(100);

        for(int i=0;i<sirRecibidosAnosNombre.size();i++){
        	tableAnysSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        	tableAnysSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String numAny = sirRecibidosAnosNombre.get(i);
            tableAnysSirRecibidos.addCell(new Paragraph(numAny,font10));
            tableAnysSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableAnysSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorAny = sirRecibidosAnosValor.get(i);
            tableAnysSirRecibidos.addCell(new Paragraph(valorAny,font10));
            document.add(tableAnysSirRecibidos);
            tableAnysSirRecibidos.deleteBodyRows();
        }
        tableAnysSirRecibidos.deleteBodyRows();
        tableAnysSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableAnysSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysSirRecibidos.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableAnysSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableAnysSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableAnysSirRecibidos.addCell(new Paragraph(sirRecibidos.toString(),font10));
        document.add(tableAnysSirRecibidos);
        document.add(new Paragraph(" "));

        //Muestra Meses
        PdfPTable seccion42 = new PdfPTable(1);
        seccion42.setWidthPercentage(100);
        seccion42.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        seccion42.getDefaultCell().setBorder(0);
        seccion42.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        seccion42.addCell(new Paragraph(getMessage("informe.mesos"), font10Bold));
        document.add(seccion42);
        document.add(new Paragraph(" "));

        PdfPTable tableMesesSirRecibidos = new PdfPTable(2);
        tableMesesSirRecibidos.setWidthPercentage(100);

        for(int i=0;i<sirRecibidosMesesNombre.size();i++){
        	tableMesesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        	tableMesesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String nomMes = sirRecibidosMesesNombre.get(i);
            tableMesesSirRecibidos.addCell(new Paragraph(nomMes,font10));
            tableMesesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tableMesesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            String valorMes = sirRecibidosMesesValor.get(i);
            tableMesesSirRecibidos.addCell(new Paragraph(valorMes,font10));
            document.add(tableMesesSirRecibidos);
            tableMesesSirRecibidos.deleteBodyRows();
        }
        tableMesesSirRecibidos.deleteBodyRows();
        tableMesesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        tableMesesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesSirRecibidos.addCell(new Paragraph(getMessage("informe.total"),font10));
        tableMesesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tableMesesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableMesesSirRecibidos.addCell(new Paragraph(sirRecibidos.toString(),font10));
        document.add(tableMesesSirRecibidos);
        document.add(new Paragraph(" "));
        
        // Final SIR ENVIADOS
        if(sirRecibidos.equals(0)){
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

    /**
    *
    * @param model
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   private void obtenerValoresSirEnviados(Map<String, Object> model) throws Exception{
       sirEnviadosAnosValor = (ArrayList<String>) model.get("sirEnviadosAnosValor");
       sirEnviadosAnosNombre = (ArrayList<String>) model.get("sirEnviadosAnosNombre");
       sirEnviadosMesesValor = (ArrayList<String>) model.get("sirEnviadosMesesValor");
       sirEnviadosMesesNombre = (ArrayList<String>) model.get("sirEnviadosMesesNombre");
   }
   
   /**
   *
   * @param model
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  private void obtenerValoresSirRecibidos(Map<String, Object> model) throws Exception{
      sirRecibidosAnosValor = (ArrayList<String>) model.get("sirRecibidosAnosValor");
      sirRecibidosAnosNombre = (ArrayList<String>) model.get("sirRecibidosAnosNombre");
      sirRecibidosMesesValor = (ArrayList<String>) model.get("sirRecibidosMesesValor");
      sirRecibidosMesesNombre = (ArrayList<String>) model.get("sirRecibidosMesesNombre");
  }
}

