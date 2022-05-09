package es.caib.regweb3.webapp.view;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Fundació BIT.
 * User: jpernia
 * Date: 9/5/14
 */

public class IndicadoresOficinaExcel extends AbstractExcelView {

    protected final Logger log = Logger.getLogger(getClass());

    /**
     * Retorna el mensaje traducido según el idioma del usuario
     * @param key
     * @return
     */
    protected String getMessage(String key){
        return I18NUtils.tradueix(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Obtenemos mapas y arraays de valores
        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        Integer registrosEntrada = (Integer) model.get("registrosEntrada");
        Integer registrosSalida = (Integer) model.get("registrosSalida");
        Integer sirEnviats = (Integer) model.get("sirEnviats");
        Integer sirRebuts = (Integer) model.get("sirRebuts");
        String nombreOficina = (String) model.get("nombreOficina");
        String codigoOficina = (String) model.get("codigoOficina");
        ArrayList<String> entradaAnosValor = (ArrayList<String>) model.get("entradaAnosValor");
        ArrayList<String> entradaAnosNombre = (ArrayList<String>) model.get("entradaAnosNombre");
        ArrayList<String> salidaAnosValor = (ArrayList<String>) model.get("salidaAnosValor");
        ArrayList<String> salidaAnosNombre = (ArrayList<String>) model.get("salidaAnosNombre");
        ArrayList<String> entradaMesesValor = (ArrayList<String>) model.get("entradaMesesValor");
        ArrayList<String> entradaMesesNombre = (ArrayList<String>) model.get("entradaMesesNombre");
        ArrayList<String> salidaMesesValor = (ArrayList<String>) model.get("salidaMesesValor");
        ArrayList<String> salidaMesesNombre = (ArrayList<String>) model.get("salidaMesesNombre");
        ArrayList<String> entradaIdiomaValor = (ArrayList<String>) model.get("entradaIdiomaValor");
        ArrayList<String> entradaIdiomaNombre = (ArrayList<String>) model.get("entradaIdiomaNombre");
        ArrayList<String> salidaIdiomaValor = (ArrayList<String>) model.get("salidaIdiomaValor");
        ArrayList<String> salidaIdiomaNombre = (ArrayList<String>) model.get("salidaIdiomaNombre");

        HSSFSheet sheet = workbook.createSheet("REGWEB3");
        sheet.setFitToPage(true);

        //Estilo título
        HSSFCellStyle titulo;
        org.apache.poi.ss.usermodel.Font tituloFuente = workbook.createFont();
        tituloFuente.setFontHeightInPoints((short)18);
        tituloFuente.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
        titulo = workbook.createCellStyle();
        titulo.setAlignment(CellStyle.ALIGN_CENTER);
        titulo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titulo.setFont(tituloFuente);

        //Estilo parametros de busqueda
        HSSFCellStyle paramCerca;
        org.apache.poi.ss.usermodel.Font paramCercaFuente = workbook.createFont();
        paramCercaFuente.setFontHeightInPoints((short)12);
        paramCercaFuente.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
        paramCerca = workbook.createCellStyle();
        paramCerca.setAlignment(CellStyle.ALIGN_CENTER);
        paramCerca.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        paramCerca.setFont(paramCercaFuente);


        //Estilo cabecera
        HSSFCellStyle cabecera;
        org.apache.poi.ss.usermodel.Font cabeceraFuente = workbook.createFont();
        cabeceraFuente.setFontHeightInPoints((short)10);
        cabeceraFuente.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
        cabecera = workbook.createCellStyle();
        cabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cabecera.setFillPattern(CellStyle.SOLID_FOREGROUND);
        cabecera.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        cabecera.setBorderBottom(CellStyle.BORDER_THIN);
        cabecera.setBorderTop(CellStyle.BORDER_THIN);
        cabecera.setBorderLeft(CellStyle.BORDER_THIN);
        cabecera.setBorderRight(CellStyle.BORDER_THIN);
        cabecera.setFont(cabeceraFuente);

        //Estilo tabla
        HSSFCellStyle fila;
        org.apache.poi.ss.usermodel.Font filaFuente = workbook.createFont();
        filaFuente.setFontHeightInPoints((short)8);
        fila = workbook.createCellStyle();
        fila.setBorderBottom(CellStyle.BORDER_THIN);
        fila.setBorderTop(CellStyle.BORDER_THIN);
        fila.setBorderLeft(CellStyle.BORDER_THIN);
        fila.setBorderRight(CellStyle.BORDER_THIN);
        fila.setFont(filaFuente);

        //Estilo títulos secciones
        HSSFCellStyle tituloSeccion;
        org.apache.poi.ss.usermodel.Font tituloSeccionFuente = workbook.createFont();
        tituloSeccionFuente.setFontHeightInPoints((short)12);
        tituloSeccionFuente.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
        tituloSeccion = workbook.createCellStyle();
        tituloSeccion.setAlignment(CellStyle.ALIGN_LEFT);
        tituloSeccion.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        tituloSeccion.setFont(tituloSeccionFuente);

        // Creamos las filas para el encabezado
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(25);
        HSSFCell tittleCell = titleRow.createCell(0);
        HSSFRow tipusRow = sheet.createRow(2);
        tipusRow.setHeightInPoints(15);
        HSSFCell tipusCell = tipusRow.createCell(0);
        HSSFRow fechaInicioRow = sheet.createRow(3);
        fechaInicioRow.setHeightInPoints(15);
        HSSFCell fechaInicioCell = fechaInicioRow.createCell(0);
        HSSFRow fechaFinRow = sheet.createRow(4);
        fechaFinRow.setHeightInPoints(15);
        HSSFCell fechaFinCell = fechaFinRow.createCell(0);
        HSSFRow mostrarRow = sheet.createRow(5);
        mostrarRow.setHeightInPoints(15);
        HSSFCell mostrarCell = mostrarRow.createCell(0);


        //Título
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$G$1"));
        tittleCell.setCellValue(getMessage("informe.indicadoresOficina"));
        tittleCell.setCellStyle(titulo);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$G$2"));
        tipusCell.setCellValue(getMessage("informe.oficina") +": " + nombreOficina);
        tipusCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$3:$G$3"));
        fechaInicioCell.setCellValue(getMessage("informe.fechaInicio") +": " + fechaInicio);
        fechaInicioCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$4:$G$4"));
        fechaFinCell.setCellValue(getMessage("informe.fechaFin") +": " + fechaFin);
        fechaFinCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$5:$G$5"));

        mostrarCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$6:$G$6"));

        int rowNum = 7;
        HSSFRow header = sheet.createRow(rowNum++);
        header.setHeightInPoints(15);

        int tamanyMaxColum = 1;

        // DATOS A MOSTRAR
        // ENTRADA

        HSSFRow entrRegRow = sheet.createRow(rowNum++);
        entrRegRow.setHeightInPoints(15);
        HSSFCell entrRegCell = entrRegRow.createCell(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
        entrRegCell.setCellValue(getMessage("informe.registrosEntrada"));
        entrRegCell.setCellStyle(tituloSeccion);

        //Espai buit
        rowNum = rowNum + 1;

        //Registres totals
        HSSFRow regEntrNom = sheet.createRow(rowNum++);
        HSSFCell regEntrNomCol = regEntrNom.createCell(0);
        regEntrNomCol.setCellValue(getMessage("informe.registros"));
        regEntrNomCol.setCellStyle(cabecera);
        HSSFRow registreRow = sheet.createRow(rowNum++);
        registreRow.createCell(0).setCellValue(registrosEntrada.toString());
        registreRow.getCell(0).setCellStyle(fila);

        //Espai buit
        rowNum = rowNum + 1;

        //Muestra Años
        HSSFRow anyRowTitle = sheet.createRow(rowNum++);
        anyRowTitle.setHeightInPoints(15);
        HSSFCell anyCell = anyRowTitle.createCell(0);
        anyCell.setCellValue(getMessage("informe.anys"));
        anyCell.setCellStyle(tituloSeccion);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
        //Espai buit
        rowNum = rowNum + 1;

        HSSFRow anyNom = sheet.createRow(rowNum++);
        for (int i = 0; i < entradaAnosNombre.size(); i++){
            HSSFCell anyNomCol = anyNom.createCell(i);
            anyNomCol.setCellValue(entradaAnosNombre.get(i));
            anyNomCol.setCellStyle(cabecera);
        }
        HSSFCell anyNomCol = anyNom.createCell(entradaAnosNombre.size());
        anyNomCol.setCellValue(getMessage("informe.total"));
        anyNomCol.setCellStyle(cabecera);
        HSSFRow anyValor = sheet.createRow(rowNum++);
        for(int i=0;i<entradaAnosValor.size();i++){
            HSSFCell anyValorCol = anyValor.createCell(i);
            anyValorCol.setCellValue(entradaAnosValor.get(i));
            anyValorCol.setCellStyle(fila);
        }
        HSSFCell anyValorCol = anyValor.createCell(entradaAnosValor.size());
        anyValorCol.setCellValue(registrosEntrada.toString());
        anyValorCol.setCellStyle(fila);

        if(entradaAnosNombre.size() > tamanyMaxColum){
            tamanyMaxColum = entradaAnosNombre.size();
        }

        //Espai buit
        rowNum = rowNum + 1;

        //Muestra Meses
        HSSFRow mesRowTitle = sheet.createRow(rowNum++);
        mesRowTitle.setHeightInPoints(15);
        HSSFCell mesCell = mesRowTitle.createCell(0);
        mesCell.setCellValue(getMessage("informe.mesos"));
        mesCell.setCellStyle(tituloSeccion);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
        //Espai buit
        rowNum = rowNum + 1;

        HSSFRow mesNom = sheet.createRow(rowNum++);
        for (int i = 0; i < entradaMesesNombre.size(); i++){
            HSSFCell mesNomCol = mesNom.createCell(i);
            mesNomCol.setCellValue(entradaMesesNombre.get(i));
            mesNomCol.setCellStyle(cabecera);
        }
        HSSFRow mesValor = sheet.createRow(rowNum++);
        for(int i=0;i<entradaMesesValor.size();i++){
            HSSFCell mesValorCol = mesValor.createCell(i);
            mesValorCol.setCellValue(entradaMesesValor.get(i));
            mesValorCol.setCellStyle(fila);
        }
        if(entradaMesesNombre.size() > tamanyMaxColum){
            tamanyMaxColum = entradaMesesNombre.size();
        }

        //Espai buit
        rowNum = rowNum + 1;


        //Muestra Idiomas
        if(entradaIdiomaNombre.size() > 0){
            HSSFRow idiomaRowTitle = sheet.createRow(rowNum++);
            idiomaRowTitle.setHeightInPoints(15);
            HSSFCell idiomaCell = idiomaRowTitle.createCell(0);
            idiomaCell.setCellValue(getMessage("informe.porIdioma"));
            idiomaCell.setCellStyle(tituloSeccion);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
            //Espai buit
            rowNum = rowNum + 1;

            HSSFRow idiomaNom = sheet.createRow(rowNum++);
            for (int i = 0; i < entradaIdiomaNombre.size(); i++){
                HSSFCell idiomaNomCol = idiomaNom.createCell(i);
                idiomaNomCol.setCellValue(entradaIdiomaNombre.get(i));
                idiomaNomCol.setCellStyle(cabecera);
            }
            HSSFRow idiomaValor = sheet.createRow(rowNum++);
            for(int i=0;i<entradaIdiomaValor.size();i++){
                HSSFCell idiomaValorCol = idiomaValor.createCell(i);
                idiomaValorCol.setCellValue(entradaIdiomaValor.get(i));
                idiomaValorCol.setCellStyle(fila);
            }
            if(entradaIdiomaNombre.size() > tamanyMaxColum){
                tamanyMaxColum = entradaIdiomaNombre.size();
            }
        }

        //Espai buit
        rowNum = rowNum + 1;


        // SALIDA

        HSSFRow salRegRow = sheet.createRow(rowNum++);
        salRegRow.setHeightInPoints(15);
        HSSFCell salRegCell = salRegRow.createCell(0);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
        salRegCell.setCellValue(getMessage("informe.registrosSalida"));
        salRegCell.setCellStyle(tituloSeccion);

        //Espai buit
        rowNum = rowNum + 1;

        //Registres totals
        HSSFRow regSalNom = sheet.createRow(rowNum++);
        HSSFCell regSalNomCol = regSalNom.createCell(0);
        regSalNomCol.setCellValue(getMessage("informe.registros"));
        regSalNomCol.setCellStyle(cabecera);
        HSSFRow registreSalRow = sheet.createRow(rowNum++);
        registreSalRow.createCell(0).setCellValue(registrosSalida.toString());
        registreSalRow.getCell(0).setCellStyle(fila);

        //Espai buit
        rowNum = rowNum + 1;

        //Muestra Años
        HSSFRow anySalRowTitle = sheet.createRow(rowNum++);
        anySalRowTitle.setHeightInPoints(15);
        HSSFCell anySalCell = anySalRowTitle.createCell(0);
        anySalCell.setCellValue(getMessage("informe.anys"));
        anySalCell.setCellStyle(tituloSeccion);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
        //Espai buit
        rowNum = rowNum + 1;

        HSSFRow anySalNom = sheet.createRow(rowNum++);
        for (int i = 0; i < salidaAnosNombre.size(); i++){
            HSSFCell anySalNomCol = anySalNom.createCell(i);
            anySalNomCol.setCellValue(salidaAnosNombre.get(i));
            anySalNomCol.setCellStyle(cabecera);
        }
        HSSFCell anySalNomCol = anySalNom.createCell(salidaAnosNombre.size());
        anySalNomCol.setCellValue(getMessage("informe.total"));
        anySalNomCol.setCellStyle(cabecera);
        HSSFRow anySalValor = sheet.createRow(rowNum++);
        for(int i=0;i<salidaAnosValor.size();i++){
            HSSFCell anySalValorCol = anySalValor.createCell(i);
            anySalValorCol.setCellValue(salidaAnosValor.get(i));
            anySalValorCol.setCellStyle(fila);
        }
        HSSFCell anySalValorCol = anySalValor.createCell(salidaAnosValor.size());
        anySalValorCol.setCellValue(registrosSalida.toString());
        anySalValorCol.setCellStyle(fila);

        if(salidaAnosNombre.size() > tamanyMaxColum){
            tamanyMaxColum = salidaAnosNombre.size();
        }

        //Espai buit
        rowNum = rowNum + 1;

        //Muestra Meses
        HSSFRow mesSalRowTitle = sheet.createRow(rowNum++);
        mesSalRowTitle.setHeightInPoints(15);
        HSSFCell mesSalCell = mesSalRowTitle.createCell(0);
        mesSalCell.setCellValue(getMessage("informe.mesos"));
        mesSalCell.setCellStyle(tituloSeccion);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
        //Espai buit
        rowNum = rowNum + 1;

        HSSFRow mesSalNom = sheet.createRow(rowNum++);
        for (int i = 0; i < salidaMesesNombre.size(); i++){
            HSSFCell mesSalNomCol = mesSalNom.createCell(i);
            mesSalNomCol.setCellValue(salidaMesesNombre.get(i));
            mesSalNomCol.setCellStyle(cabecera);
        }
        HSSFRow mesSalValor = sheet.createRow(rowNum++);
        for(int i=0;i<salidaMesesValor.size();i++){
            HSSFCell mesSalValorCol = mesSalValor.createCell(i);
            mesSalValorCol.setCellValue(salidaMesesValor.get(i));
            mesSalValorCol.setCellStyle(fila);
        }
        if(salidaMesesNombre.size() > tamanyMaxColum){
            tamanyMaxColum = salidaMesesNombre.size();
        }

        //Espai buit
        rowNum = rowNum + 1;


        //Muestra Idiomas
        if(salidaIdiomaNombre.size() > 0){
            HSSFRow idiomaSalRowTitle = sheet.createRow(rowNum++);
            idiomaSalRowTitle.setHeightInPoints(15);
            HSSFCell idiomaSalCell = idiomaSalRowTitle.createCell(0);
            idiomaSalCell.setCellValue(getMessage("informe.porIdioma"));
            idiomaSalCell.setCellStyle(tituloSeccion);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
            //Espai buit
            rowNum = rowNum + 1;

            HSSFRow idiomaSalNom = sheet.createRow(rowNum++);
            for (int i = 0; i < salidaIdiomaNombre.size(); i++){
                HSSFCell idiomaSalNomCol = idiomaSalNom.createCell(i);
                idiomaSalNomCol.setCellValue(salidaIdiomaNombre.get(i));
                idiomaSalNomCol.setCellStyle(cabecera);
            }
            HSSFRow idiomaSalValor = sheet.createRow(rowNum++);
            for(int i=0;i<salidaIdiomaValor.size();i++){
                HSSFCell idiomaSalValorCol = idiomaSalValor.createCell(i);
                idiomaSalValorCol.setCellValue(salidaIdiomaValor.get(i));
                idiomaSalValorCol.setCellStyle(fila);
            }
            if(salidaIdiomaNombre.size() > tamanyMaxColum){
                tamanyMaxColum = salidaIdiomaNombre.size();
            }
        }


        // Ajustamos el ancho de cada columna a su contenido
        for (int i = 0; i < tamanyMaxColum; i++) {
            sheet.autoSizeColumn(i);
        }

        String nombreFichero = getMessage("informe.nombreFichero.indicadoresOficina")+ codigoOficina + ".xls";

        // Cabeceras Response
        response.setHeader("Content-Disposition","attachment; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
    }

}
