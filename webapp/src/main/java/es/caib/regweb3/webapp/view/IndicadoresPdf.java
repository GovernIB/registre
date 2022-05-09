package es.caib.regweb3.webapp.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb3.utils.RegwebConstantes;
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
public class IndicadoresPdf extends AbstractIText5PdfView{

    protected final Logger log = Logger.getLogger(getClass());

    private ArrayList<String> entradaAnosValor = new ArrayList<String>();
    private ArrayList<String> entradaAnosNombre = new ArrayList<String>();
    private ArrayList<String> salidaAnosValor = new ArrayList<String>();
    private ArrayList<String> salidaAnosNombre = new ArrayList<String>();
    private ArrayList<String> entradaMesesValor = new ArrayList<String>();
    private ArrayList<String> entradaMesesNombre = new ArrayList<String>();
    private ArrayList<String> salidaMesesValor = new ArrayList<String>();
    private ArrayList<String> salidaMesesNombre = new ArrayList<String>();
    private ArrayList<String> entradaConselleriaValor = new ArrayList<String>();
    private ArrayList<String> entradaConselleriaNombre = new ArrayList<String>();
    private ArrayList<String> salidaConselleriaValor = new ArrayList<String>();
    private ArrayList<String> salidaConselleriaNombre = new ArrayList<String>();
    private ArrayList<String> entradaLibroValor = new ArrayList<String>();
    private ArrayList<String> entradaLibroNombre = new ArrayList<String>();
    private ArrayList<String> salidaLibroValor = new ArrayList<String>();
    private ArrayList<String> salidaLibroNombre = new ArrayList<String>();
    private ArrayList<String> entradaOficinaValor = new ArrayList<String>();
    private ArrayList<String> entradaOficinaNombre = new ArrayList<String>();
    private ArrayList<String> salidaOficinaValor = new ArrayList<String>();
    private ArrayList<String> salidaOficinaNombre = new ArrayList<String>();
    private ArrayList<String> entradaIdiomaValor = new ArrayList<String>();
    private ArrayList<String> entradaIdiomaNombre = new ArrayList<String>();
    private ArrayList<String> salidaIdiomaValor = new ArrayList<String>();
    private ArrayList<String> salidaIdiomaNombre = new ArrayList<String>();
    
    // SIR ENVIADOS
    private ArrayList<String> sirEnviadosAnosValor = new ArrayList<String>();
    private ArrayList<String> sirEnviadosAnosNombre = new ArrayList<String>();
    private ArrayList<String> sirEnviadosMesesValor = new ArrayList<String>();
    private ArrayList<String> sirEnviadosMesesNombre = new ArrayList<String>();
    private ArrayList<String> sirEnviadosConselleriaValor = new ArrayList<String>();
    private ArrayList<String> sirEnviadosConselleriaNombre = new ArrayList<String>();
    private ArrayList<String> sirEnviadosOficinaValor = new ArrayList<String>();
    private ArrayList<String> sirEnviadosOficinaNombre = new ArrayList<String>();
    
    // SIR RECIBIDOS
    private ArrayList<String> sirRecibidosAnosValor = new ArrayList<String>();
    private ArrayList<String> sirRecibidosAnosNombre = new ArrayList<String>();
    private ArrayList<String> sirRecibidosMesesValor = new ArrayList<String>();
    private ArrayList<String> sirRecibidosMesesNombre = new ArrayList<String>();
    private ArrayList<String> sirRecibidosConselleriaValor = new ArrayList<String>();
    private ArrayList<String> sirRecibidosConselleriaNombre = new ArrayList<String>();
    private ArrayList<String> sirRecibidosOficinaValor = new ArrayList<String>();
    private ArrayList<String> sirRecibidosOficinaNombre = new ArrayList<String>();
    
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
        String tipoInforme = null;

        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        Long campoCalendario = (Long) model.get("campoCalendario");

        Integer registrosEntrada = 0, registrosSalida = 0, sirEnviados = 0, sirRecibidos = 0;

        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA)){ // Entrada y Salida

            tipoInforme = getMessage("informe.ambosTipos");

            registrosEntrada = (Integer) model.get("registrosEntrada");
            registrosSalida = (Integer) model.get("registrosSalida");

            obtenerValoresEntrada(model);
            obtenerValoresSalida(model);

        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)){ // Entrada

            tipoInforme = getMessage("informe.entrada");
            registrosEntrada = (Integer) model.get("registrosEntrada");

            obtenerValoresEntrada(model);

        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)){ // Salida

            tipoInforme = getMessage("informe.salida");
            registrosSalida = (Integer) model.get("registrosSalida");

            obtenerValoresSalida(model);

        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_ENVIADO)){ // SIR ENVIADOS

            tipoInforme = getMessage("informe.sirEnviado");
            sirEnviados = (Integer) model.get("sirEnviados");

            obtenerValoresSirEnviados(model);

        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_RECIBIDO)){ // SIR RECIBIDOS

            tipoInforme = getMessage("informe.sirRecibido");
            sirRecibidos = (Integer) model.get("sirRecibidos");

            obtenerValoresSirRecibidos(model);

        }


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
        document.addTitle(getMessage("informe.indicadores"));
        titulo.addCell(new Paragraph(getMessage("informe.indicadores"), font14Bold));
        titulo.addCell(new Paragraph(getMessage("informe.tipo") +": " + tipoInforme));
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

            if(registrosEntrada.equals(0)){
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
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            if(registrosSalida.equals(0)){
                PdfPTable buit = new PdfPTable(1);
                buit.setWidthPercentage(100);
                buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                buit.getDefaultCell().setBorder(0);
                buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
                document.add(buit);
            }

        }
        
        // SIR ENVIADOS
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_ENVIADO)){

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
            if(campoCalendario == 0 || campoCalendario ==1){
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
                tableAnysSirEnviados.addCell(new Paragraph(sirEnviados.toString(),font10));
                document.add(tableAnysSirEnviados);
                document.add(new Paragraph(" "));
            }

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
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
                tableMesesSirEnviados.addCell(new Paragraph(sirEnviados.toString(),font10));
                document.add(tableMesesSirEnviados);
                document.add(new Paragraph(" "));
            }

            //Muestra Consellerias
            if(sirEnviadosConselleriaNombre.size() > 0){
                PdfPTable seccion33 = new PdfPTable(1);
                seccion33.setWidthPercentage(100);
                seccion33.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion33.getDefaultCell().setBorder(0);
                seccion33.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion33.addCell(new Paragraph(getMessage("informe.porOrganismos"), font10Bold));
                document.add(seccion33);
                document.add(new Paragraph(" "));

                PdfPTable tableConselleriesSirEnviados = new PdfPTable(2);
                tableConselleriesSirEnviados.setWidthPercentage(100);

                for(int i=0;i<sirEnviadosConselleriaNombre.size();i++){
                	tableConselleriesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableConselleriesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomConsell = sirEnviadosConselleriaNombre.get(i);
                    tableConselleriesSirEnviados.addCell(new Paragraph(nomConsell,font10));
                    tableConselleriesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableConselleriesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorConsell = sirEnviadosConselleriaValor.get(i);
                    tableConselleriesSirEnviados.addCell(new Paragraph(valorConsell,font10));
                    document.add(tableConselleriesSirEnviados);
                    tableConselleriesSirEnviados.deleteBodyRows();
                }
                tableConselleriesSirEnviados.deleteBodyRows();
                tableConselleriesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                tableConselleriesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesSirEnviados.addCell(new Paragraph(getMessage("informe.total"),font10));
                tableConselleriesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                tableConselleriesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesSirEnviados.addCell(new Paragraph(sirEnviados.toString(),font10));
                document.add(tableConselleriesSirEnviados);
                document.add(new Paragraph(" "));
            }

            //Muestra Oficinas
            if(sirEnviadosOficinaNombre.size() > 0){
                PdfPTable seccion36 = new PdfPTable(1);
                seccion36.setWidthPercentage(100);
                seccion36.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion36.getDefaultCell().setBorder(0);
                seccion36.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion36.addCell(new Paragraph(getMessage("informe.porOficina"), font10Bold));
                document.add(seccion36);
                document.add(new Paragraph(" "));

                PdfPTable tableOficinesSirEnviados = new PdfPTable(2);
                tableOficinesSirEnviados.setWidthPercentage(100);

                for(int i=0;i<sirEnviadosOficinaNombre.size();i++){
                	tableOficinesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                	tableOficinesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomOficina = sirEnviadosOficinaNombre.get(i);
                    tableOficinesSirEnviados.addCell(new Paragraph(nomOficina,font10));
                    tableOficinesSirEnviados.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableOficinesSirEnviados.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorOficina = sirEnviadosOficinaValor.get(i);
                    tableOficinesSirEnviados.addCell(new Paragraph(valorOficina,font10));
                    document.add(tableOficinesSirEnviados);
                    tableOficinesSirEnviados.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

            // Final SIR enviados 
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            if(sirEnviados.equals(0)){
                PdfPTable buit = new PdfPTable(1);
                buit.setWidthPercentage(100);
                buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                buit.getDefaultCell().setBorder(0);
                buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
                document.add(buit);
            }

        }
        
        // SIR RECIBIDOS
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_RECIBIDO)){

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
            if(campoCalendario == 0 || campoCalendario ==1){
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
            }

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
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
            }

            //Muestra Consellerias
            if(sirRecibidosConselleriaNombre.size() > 0){
                PdfPTable seccion43 = new PdfPTable(1);
                seccion43.setWidthPercentage(100);
                seccion43.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion43.getDefaultCell().setBorder(0);
                seccion43.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion43.addCell(new Paragraph(getMessage("informe.porOrganismos"), font10Bold));
                document.add(seccion43);
                document.add(new Paragraph(" "));

                PdfPTable tableConselleriesSirRecibidos = new PdfPTable(2);
                tableConselleriesSirRecibidos.setWidthPercentage(100);

                for(int i=0;i<sirRecibidosConselleriaNombre.size();i++){
                	tableConselleriesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                    tableConselleriesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomConsell = sirRecibidosConselleriaNombre.get(i);
                    tableConselleriesSirRecibidos.addCell(new Paragraph(nomConsell,font10));
                    tableConselleriesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableConselleriesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorConsell = sirRecibidosConselleriaValor.get(i);
                    tableConselleriesSirRecibidos.addCell(new Paragraph(valorConsell,font10));
                    document.add(tableConselleriesSirRecibidos);
                    tableConselleriesSirRecibidos.deleteBodyRows();
                }
                tableConselleriesSirRecibidos.deleteBodyRows();
                tableConselleriesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                tableConselleriesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesSirRecibidos.addCell(new Paragraph(getMessage("informe.total"),font10));
                tableConselleriesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                tableConselleriesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                tableConselleriesSirRecibidos.addCell(new Paragraph(sirRecibidos.toString(),font10));
                document.add(tableConselleriesSirRecibidos);
                document.add(new Paragraph(" "));
            }

            //Muestra Oficinas
            if(sirRecibidosOficinaNombre.size() > 0){
                PdfPTable seccion46 = new PdfPTable(1);
                seccion46.setWidthPercentage(100);
                seccion46.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                seccion46.getDefaultCell().setBorder(0);
                seccion46.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                seccion46.addCell(new Paragraph(getMessage("informe.porOficina"), font10Bold));
                document.add(seccion46);
                document.add(new Paragraph(" "));

                PdfPTable tableOficinesSirRecibidos = new PdfPTable(2);
                tableOficinesSirRecibidos.setWidthPercentage(100);

                for(int i=0;i<sirRecibidosOficinaNombre.size();i++){
                	tableOficinesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
                	tableOficinesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String nomOficina = sirRecibidosOficinaNombre.get(i);
                    tableOficinesSirRecibidos.addCell(new Paragraph(nomOficina,font10));
                    tableOficinesSirRecibidos.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                    tableOficinesSirRecibidos.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    String valorOficina = sirRecibidosOficinaValor.get(i);
                    tableOficinesSirRecibidos.addCell(new Paragraph(valorOficina,font10));
                    document.add(tableOficinesSirRecibidos);
                    tableOficinesSirRecibidos.deleteBodyRows();
                }
                document.add(new Paragraph(" "));
            }

            // Final SIR recibidos
            if(sirRecibidos.equals(0)){
                PdfPTable buit = new PdfPTable(1);
                buit.setWidthPercentage(100);
                buit.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                buit.getDefaultCell().setBorder(0);
                buit.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
                document.add(buit);
            }

        }

        String nombreFichero = getMessage("informe.nombreFichero.indicadores")+ tipoInforme +".pdf";

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
        entradaConselleriaValor = (ArrayList<String>) model.get("entradaConselleriaValor");
        entradaConselleriaNombre = (ArrayList<String>) model.get("entradaConselleriaNombre");
        entradaLibroValor = (ArrayList<String>) model.get("entradaLibroValor");
        entradaLibroNombre = (ArrayList<String>) model.get("entradaLibroNombre");
        entradaOficinaValor = (ArrayList<String>) model.get("entradaOficinaValor");
        entradaOficinaNombre = (ArrayList<String>) model.get("entradaOficinaNombre");
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
        salidaConselleriaValor = (ArrayList<String>) model.get("salidaConselleriaValor");
        salidaConselleriaNombre = (ArrayList<String>) model.get("salidaConselleriaNombre");
        salidaLibroValor = (ArrayList<String>) model.get("salidaLibroValor");
        salidaLibroNombre = (ArrayList<String>) model.get("salidaLibroNombre");
        salidaOficinaValor = (ArrayList<String>) model.get("salidaOficinaValor");
        salidaOficinaNombre = (ArrayList<String>) model.get("salidaOficinaNombre");
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
       sirEnviadosConselleriaValor = (ArrayList<String>) model.get("sirEnviadosConselleriaValor");
       sirEnviadosConselleriaNombre = (ArrayList<String>) model.get("sirEnviadosConselleriaNombre");
       sirEnviadosOficinaValor = (ArrayList<String>) model.get("sirEnviadosOficinaValor");
       sirEnviadosOficinaNombre = (ArrayList<String>) model.get("sirEnviadosOficinaNombre");
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
      sirRecibidosConselleriaValor = (ArrayList<String>) model.get("sirRecibidosConselleriaValor");
      sirRecibidosConselleriaNombre = (ArrayList<String>) model.get("sirRecibidosConselleriaNombre");
      sirRecibidosOficinaValor = (ArrayList<String>) model.get("sirRecibidosOficinaValor");
      sirRecibidosOficinaNombre = (ArrayList<String>) model.get("sirRecibidosOficinaNombre");
  }
}
