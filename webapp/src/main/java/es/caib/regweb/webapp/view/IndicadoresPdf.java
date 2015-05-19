package es.caib.regweb.webapp.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.utils.AbstractIText5PdfView;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 6/05/14
 */
public class IndicadoresPdf extends AbstractIText5PdfView {

    protected final Logger log = Logger.getLogger(getClass());

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

        //Obtenemos mapas y arraays de valores
        Long tipo = (Long) model.get("tipo");
        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        Long campoCalendario = (Long) model.get("campoCalendario");
        Integer registrosEntrada = (Integer) model.get("registrosEntrada");
        Integer registrosSalida = (Integer) model.get("registrosSalida");
        ArrayList<String> entradaAnosValor = (ArrayList<String>) model.get("entradaAnosValor");
        ArrayList<String> entradaAnosNombre = (ArrayList<String>) model.get("entradaAnosNombre");
        ArrayList<String> salidaAnosValor = (ArrayList<String>) model.get("salidaAnosValor");
        ArrayList<String> salidaAnosNombre = (ArrayList<String>) model.get("salidaAnosNombre");
        ArrayList<String> entradaMesesValor = (ArrayList<String>) model.get("entradaMesesValor");
        ArrayList<String> entradaMesesNombre = (ArrayList<String>) model.get("entradaMesesNombre");
        ArrayList<String> salidaMesesValor = (ArrayList<String>) model.get("salidaMesesValor");
        ArrayList<String> salidaMesesNombre = (ArrayList<String>) model.get("salidaMesesNombre");
        ArrayList<String> entradaConselleriaValor = (ArrayList<String>) model.get("entradaConselleriaValor");
        ArrayList<String> entradaConselleriaNombre = (ArrayList<String>) model.get("entradaConselleriaNombre");
        ArrayList<String> salidaConselleriaValor = (ArrayList<String>) model.get("salidaConselleriaValor");
        ArrayList<String> salidaConselleriaNombre = (ArrayList<String>) model.get("salidaConselleriaNombre");
        ArrayList<String> entradaAsuntoValor = (ArrayList<String>) model.get("entradaAsuntoValor");
        ArrayList<String> entradaAsuntoNombre = (ArrayList<String>) model.get("entradaAsuntoNombre");
        ArrayList<String> salidaAsuntoValor = (ArrayList<String>) model.get("salidaAsuntoValor");
        ArrayList<String> salidaAsuntoNombre = (ArrayList<String>) model.get("salidaAsuntoNombre");
        ArrayList<String> entradaLibroValor = (ArrayList<String>) model.get("entradaLibroValor");
        ArrayList<String> entradaLibroNombre = (ArrayList<String>) model.get("entradaLibroNombre");
        ArrayList<String> salidaLibroValor = (ArrayList<String>) model.get("salidaLibroValor");
        ArrayList<String> salidaLibroNombre = (ArrayList<String>) model.get("salidaLibroNombre");
        ArrayList<String> entradaOficinaValor = (ArrayList<String>) model.get("entradaOficinaValor");
        ArrayList<String> entradaOficinaNombre = (ArrayList<String>) model.get("entradaOficinaNombre");
        ArrayList<String> salidaOficinaValor = (ArrayList<String>) model.get("salidaOficinaValor");
        ArrayList<String> salidaOficinaNombre = (ArrayList<String>) model.get("salidaOficinaNombre");
        ArrayList<String> entradaIdiomaValor = (ArrayList<String>) model.get("entradaIdiomaValor");
        ArrayList<String> entradaIdiomaNombre = (ArrayList<String>) model.get("entradaIdiomaNombre");
        ArrayList<String> salidaIdiomaValor = (ArrayList<String>) model.get("salidaIdiomaValor");
        ArrayList<String> salidaIdiomaNombre = (ArrayList<String>) model.get("salidaIdiomaNombre");

        //Configuraciones generales formato pdf
        document.setPageSize(PageSize.A4);

        document.addAuthor("REGWEB");
        document.addCreationDate();
        document.addCreator("iText library");
        document.newPage();
        Font font10Bold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font font14Bold = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font font14BoldItalic = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC);

        String tipoRegistro = null;
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA)){
            tipoRegistro = getMessage("informe.ambosTipos");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)){
            tipoRegistro = getMessage("informe.entrada");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)){
            tipoRegistro = getMessage("informe.salida");
        }

        // Título
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        titulo.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        titulo.getDefaultCell().setBorder(0);
        titulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        document.addTitle(getMessage("informe.indicadores"));
        titulo.addCell(new Paragraph(getMessage("informe.indicadores"), font14Bold));
        titulo.addCell(new Paragraph(getMessage("informe.tipo") +": " + tipoRegistro));
        titulo.addCell(new Paragraph(getMessage("informe.fechaInicio") +": " + fechaInicio));
        titulo.addCell(new Paragraph(getMessage("informe.fechaFin") +": " + fechaFin));
        String campCalendari = "";
        if(campoCalendario == 0){
            campCalendari = getMessage("informe.ambosCalendario");
        }else if(campoCalendario == 1){
                campCalendari = getMessage("informe.anys");
            }else if(campoCalendario == 2){
                    campCalendari = getMessage("informe.mesos");
                }
        titulo.addCell(new Paragraph(getMessage("informe.mostrar") +": " + campCalendari));
        document.add(titulo);
        document.add(new Paragraph(" "));

        // DATOS A MOSTRAR
        // ENTRADA
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA) || tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)){

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
            if(campoCalendario == 0 || campoCalendario ==1){
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
            }

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
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
            }

            //Muestra Consellerias
            if(entradaConselleriaNombre.size() > 0){
                PdfPTable seccion13 = new PdfPTable(1);
                seccion13.setWidthPercentage(100);
                seccion13.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion13.getDefaultCell().setBorder(0);
                seccion13.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion13.addCell(new Paragraph(getMessage("informe.porOrganismos"), font10Bold));
                document.add(seccion13);
                document.add(new Paragraph(" "));

                PdfPTable tableConselleriesEntrada = new PdfPTable(2);
                tableConselleriesEntrada.setWidthPercentage(100);

                for(int i=0;i<entradaConselleriaNombre.size();i++){
                    tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomConsell = entradaConselleriaNombre.get(i);
                    tableConselleriesEntrada.addCell(new Paragraph(nomConsell,font10));
                    tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorConsell = entradaConselleriaValor.get(i);
                    tableConselleriesEntrada.addCell(new Paragraph(valorConsell,font10));
                    document.add(tableConselleriesEntrada);
                    tableConselleriesEntrada.deleteBodyRows();
                }
                tableConselleriesEntrada.deleteBodyRows();
                tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesEntrada.addCell(new Paragraph(getMessage("informe.total"),font10));
                tableConselleriesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                tableConselleriesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesEntrada.addCell(new Paragraph(registrosEntrada.toString(),font10));
                document.add(tableConselleriesEntrada);
                document.add(new Paragraph(" "));
            }

            //Muestra Tipos de Asunto
            if(entradaAsuntoNombre.size() > 0){
                PdfPTable seccion14 = new PdfPTable(1);
                seccion14.setWidthPercentage(100);
                seccion14.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion14.getDefaultCell().setBorder(0);
                seccion14.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion14.addCell(new Paragraph(getMessage("informe.porTiposAsunto"), font10Bold));
                document.add(seccion14);
                document.add(new Paragraph(" "));

                PdfPTable tableAssumptesEntrada = new PdfPTable(2);
                tableAssumptesEntrada.setWidthPercentage(100);

                for(int i=0;i<entradaAsuntoNombre.size();i++){
                    tableAssumptesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableAssumptesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomAsunto = entradaAsuntoNombre.get(i);
                    tableAssumptesEntrada.addCell(new Paragraph(nomAsunto,font10));
                    tableAssumptesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableAssumptesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorAsunto = entradaAsuntoValor.get(i);
                    tableAssumptesEntrada.addCell(new Paragraph(valorAsunto,font10));
                    document.add(tableAssumptesEntrada);
                    tableAssumptesEntrada.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

            //Muestra Libros de Registro
            if(entradaLibroNombre.size() > 0){
                PdfPTable seccion15 = new PdfPTable(1);
                seccion15.setWidthPercentage(100);
                seccion15.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion15.getDefaultCell().setBorder(0);
                seccion15.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion15.addCell(new Paragraph(getMessage("informe.porLibroRegistro"), font10Bold));
                document.add(seccion15);
                document.add(new Paragraph(" "));

                PdfPTable tableLlibresEntrada = new PdfPTable(2);
                tableLlibresEntrada.setWidthPercentage(100);

                for(int i=0;i<entradaLibroNombre.size();i++){
                    tableLlibresEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableLlibresEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomLibro = entradaLibroNombre.get(i);
                    tableLlibresEntrada.addCell(new Paragraph(nomLibro,font10));
                    tableLlibresEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableLlibresEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorLibro = entradaLibroValor.get(i);
                    tableLlibresEntrada.addCell(new Paragraph(valorLibro,font10));
                    document.add(tableLlibresEntrada);
                    tableLlibresEntrada.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

            //Muestra Oficinas
            if(entradaOficinaNombre.size() > 0){
                PdfPTable seccion16 = new PdfPTable(1);
                seccion16.setWidthPercentage(100);
                seccion16.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion16.getDefaultCell().setBorder(0);
                seccion16.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion16.addCell(new Paragraph(getMessage("informe.porOficina"), font10Bold));
                document.add(seccion16);
                document.add(new Paragraph(" "));

                PdfPTable tableOficinesEntrada = new PdfPTable(2);
                tableOficinesEntrada.setWidthPercentage(100);

                for(int i=0;i<entradaOficinaNombre.size();i++){
                    tableOficinesEntrada.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableOficinesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomOficina = entradaOficinaNombre.get(i);
                    tableOficinesEntrada.addCell(new Paragraph(nomOficina,font10));
                    tableOficinesEntrada.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableOficinesEntrada.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorOficina = entradaOficinaValor.get(i);
                    tableOficinesEntrada.addCell(new Paragraph(valorOficina,font10));
                    document.add(tableOficinesEntrada);
                    tableOficinesEntrada.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

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

            if(registrosEntrada.equals("0")){
                PdfPTable buit = new PdfPTable(1);
                buit.setWidthPercentage(100);
                buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                buit.getDefaultCell().setBorder(0);
                buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
                document.add(buit);
            }

        }

        // SALIDA
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA) || tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)){

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
            if(campoCalendario == 0 || campoCalendario ==1){
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
            }

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
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
            }

            //Muestra Consellerias
            if(salidaConselleriaNombre.size() > 0){
                PdfPTable seccion23 = new PdfPTable(1);
                seccion23.setWidthPercentage(100);
                seccion23.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion23.getDefaultCell().setBorder(0);
                seccion23.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion23.addCell(new Paragraph(getMessage("informe.porOrganismos"), font10Bold));
                document.add(seccion23);
                document.add(new Paragraph(" "));

                PdfPTable tableConselleriesSortida = new PdfPTable(2);
                tableConselleriesSortida.setWidthPercentage(100);

                for(int i=0;i<salidaConselleriaNombre.size();i++){
                    tableConselleriesSortida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableConselleriesSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomConsell = salidaConselleriaNombre.get(i);
                    tableConselleriesSortida.addCell(new Paragraph(nomConsell,font10));
                    tableConselleriesSortida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableConselleriesSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorConsell = salidaConselleriaValor.get(i);
                    tableConselleriesSortida.addCell(new Paragraph(valorConsell,font10));
                    document.add(tableConselleriesSortida);
                    tableConselleriesSortida.deleteBodyRows();
                }
                tableConselleriesSortida.deleteBodyRows();
                tableConselleriesSortida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                tableConselleriesSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesSortida.addCell(new Paragraph(getMessage("informe.total"),font10));
                tableConselleriesSortida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                tableConselleriesSortida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesSortida.addCell(new Paragraph(registrosSalida.toString(),font10));
                document.add(tableConselleriesSortida);
                document.add(new Paragraph(" "));
            }

            //Muestra Tipos de Asunto
            if(salidaAsuntoNombre.size() > 0){
                PdfPTable seccion24 = new PdfPTable(1);
                seccion24.setWidthPercentage(100);
                seccion24.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion24.getDefaultCell().setBorder(0);
                seccion24.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion24.addCell(new Paragraph(getMessage("informe.porTiposAsunto"), font10Bold));
                document.add(seccion24);
                document.add(new Paragraph(" "));

                PdfPTable tableAssumptesSalida = new PdfPTable(2);
                tableAssumptesSalida.setWidthPercentage(100);

                for(int i=0;i<salidaAsuntoNombre.size();i++){
                    tableAssumptesSalida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableAssumptesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomAsunto = salidaAsuntoNombre.get(i);
                    tableAssumptesSalida.addCell(new Paragraph(nomAsunto,font10));
                    tableAssumptesSalida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableAssumptesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorAsunto = salidaAsuntoValor.get(i);
                    tableAssumptesSalida.addCell(new Paragraph(valorAsunto,font10));
                    document.add(tableAssumptesSalida);
                    tableAssumptesSalida.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

            //Muestra Libros de Registro
            if(salidaLibroNombre.size() > 0){
                PdfPTable seccion25 = new PdfPTable(1);
                seccion25.setWidthPercentage(100);
                seccion25.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion25.getDefaultCell().setBorder(0);
                seccion25.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion25.addCell(new Paragraph(getMessage("informe.porLibroRegistro"), font10Bold));
                document.add(seccion25);
                document.add(new Paragraph(" "));

                PdfPTable tableLlibresSalida = new PdfPTable(2);
                tableLlibresSalida.setWidthPercentage(100);

                for(int i=0;i<salidaLibroNombre.size();i++){
                    tableLlibresSalida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableLlibresSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomLibro = salidaLibroNombre.get(i);
                    tableLlibresSalida.addCell(new Paragraph(nomLibro,font10));
                    tableLlibresSalida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableLlibresSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorLibro = salidaLibroValor.get(i);
                    tableLlibresSalida.addCell(new Paragraph(valorLibro,font10));
                    document.add(tableLlibresSalida);
                    tableLlibresSalida.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

            //Muestra Oficinas
            if(salidaOficinaNombre.size() > 0){
                PdfPTable seccion26 = new PdfPTable(1);
                seccion26.setWidthPercentage(100);
                seccion26.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion26.getDefaultCell().setBorder(0);
                seccion26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion26.addCell(new Paragraph(getMessage("informe.porOficina"), font10Bold));
                document.add(seccion26);
                document.add(new Paragraph(" "));

                PdfPTable tableOficinesSalida = new PdfPTable(2);
                tableOficinesSalida.setWidthPercentage(100);

                for(int i=0;i<salidaOficinaNombre.size();i++){
                    tableOficinesSalida.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableOficinesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomOficina = salidaOficinaNombre.get(i);
                    tableOficinesSalida.addCell(new Paragraph(nomOficina,font10));
                    tableOficinesSalida.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableOficinesSalida.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorOficina = salidaOficinaValor.get(i);
                    tableOficinesSalida.addCell(new Paragraph(valorOficina,font10));
                    document.add(tableOficinesSalida);
                    tableOficinesSalida.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

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
            if(registrosSalida.equals("0")){
                PdfPTable buit = new PdfPTable(1);
                buit.setWidthPercentage(100);
                buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                buit.getDefaultCell().setBorder(0);
                buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
                document.add(buit);
            }

        }

        String nombreFichero = getMessage("informe.nombreFichero.indicadores")+ tipoRegistro +".pdf";

        // Cabeceras Response
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");


    }
}
