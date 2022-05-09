package es.caib.regweb3.webapp.view;

import es.caib.regweb3.utils.RegwebConstantes;
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

public class IndicadoresExcel extends AbstractExcelView {

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
    @SuppressWarnings("unchecked")
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //Obtenemos mapas y arraays de valores
        Long tipo = (Long) model.get("tipo");
        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        Long campoCalendario = (Long) model.get("campoCalendario");
        Integer registrosEntrada = (Integer) model.get("registrosEntrada");
        Integer registrosSalida = (Integer) model.get("registrosSalida");
        Integer sirEnviados = (Integer) model.get("sirEnviados");
        Integer sirRecibidos = (Integer) model.get("sirRecibidos");
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
        
        // SIR ENVIADOS
        ArrayList<String> sirEnviadosAnosValor = (ArrayList<String>) model.get("sirEnviadosAnosValor");
        ArrayList<String> sirEnviadosAnosNombre = (ArrayList<String>) model.get("sirEnviadosAnosNombre");
        ArrayList<String> sirEnviadosMesesValor = (ArrayList<String>) model.get("sirEnviadosMesesValor");
        ArrayList<String> sirEnviadosMesesNombre = (ArrayList<String>) model.get("sirEnviadosMesesNombre");
        ArrayList<String> sirEnviadosConselleriaValor = (ArrayList<String>) model.get("sirEnviadosConselleriaValor");
        ArrayList<String> sirEnviadosConselleriaNombre = (ArrayList<String>) model.get("sirEnviadosConselleriaNombre");
        ArrayList<String> sirEnviadosOficinaValor = (ArrayList<String>) model.get("sirEnviadosOficinaValor");
        ArrayList<String> sirEnviadosOficinaNombre = (ArrayList<String>) model.get("sirEnviadosOficinaNombre");
        
        // SIR RECIBIDOS
        ArrayList<String> sirRecibidosAnosValor = (ArrayList<String>) model.get("sirRecibidosAnosValor");
        ArrayList<String> sirRecibidosAnosNombre = (ArrayList<String>) model.get("sirRecibidosAnosNombre");
        ArrayList<String> sirRecibidosMesesValor = (ArrayList<String>) model.get("sirRecibidosMesesValor");
        ArrayList<String> sirRecibidosMesesNombre = (ArrayList<String>) model.get("sirRecibidosMesesNombre");
        ArrayList<String> sirRecibidosConselleriaValor = (ArrayList<String>) model.get("sirRecibidosConselleriaValor");
        ArrayList<String> sirRecibidosConselleriaNombre = (ArrayList<String>) model.get("sirRecibidosConselleriaNombre");
        ArrayList<String> sirRecibidosOficinaValor = (ArrayList<String>) model.get("sirRecibidosOficinaValor");
        ArrayList<String> sirRecibidosOficinaNombre = (ArrayList<String>) model.get("sirRecibidosOficinaNombre");
        
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

        String tipoRegistro = null;
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA)){
            tipoRegistro = getMessage("informe.ambosTipos");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)){
            tipoRegistro = getMessage("informe.entrada");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)){
            tipoRegistro = getMessage("informe.salida");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_ENVIADO)){
            tipoRegistro = getMessage("informe.sirEnviado");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_RECIBIDO)){
            tipoRegistro = getMessage("informe.sirRecibido");
        }

        //Título
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$G$1"));
        tittleCell.setCellValue(getMessage("informe.indicadores"));
        tittleCell.setCellStyle(titulo);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$G$2"));
        tipusCell.setCellValue(getMessage("informe.tipo") +": " + tipoRegistro);
        tipusCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$3:$G$3"));
        fechaInicioCell.setCellValue(getMessage("informe.fechaInicio") +": " + fechaInicio);
        fechaInicioCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$4:$G$4"));
        fechaFinCell.setCellValue(getMessage("informe.fechaFin") +": " + fechaFin);
        fechaFinCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$5:$G$5"));
        String campCalendari = "";
        if(campoCalendario == 0){
            campCalendari = getMessage("informe.ambosCalendario");
            }else if(campoCalendario == 1){
                    campCalendari = getMessage("informe.anys");
                }else if(campoCalendario == 2){
                    campCalendari = getMessage("informe.mesos");
                }
        mostrarCell.setCellValue(getMessage("informe.mostrar") +": " + campCalendari);
        mostrarCell.setCellStyle(paramCerca);
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$6:$G$6"));

        int rowNum = 7;
        HSSFRow header = sheet.createRow(rowNum++);
        header.setHeightInPoints(15);

        int tamanyMaxColum = 1;

        // DATOS A MOSTRAR
        // ENTRADA
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA) || tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)){

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
            if(campoCalendario == 0 || campoCalendario ==1){
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
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
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
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Consellerias
            if(entradaConselleriaNombre.size() > 0){
                HSSFRow conselleriaRowTitle = sheet.createRow(rowNum++);
                conselleriaRowTitle.setHeightInPoints(15);
                HSSFCell conselleriaCell = conselleriaRowTitle.createCell(0);
                conselleriaCell.setCellValue(getMessage("informe.porOrganismos"));
                conselleriaCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow conselleriaNom = sheet.createRow(rowNum++);
                for (int i = 0; i < entradaConselleriaNombre.size(); i++){
                    HSSFCell conselleriaNomCol = conselleriaNom.createCell(i);
                    conselleriaNomCol.setCellValue(entradaConselleriaNombre.get(i));
                    conselleriaNomCol.setCellStyle(cabecera);
                }
                HSSFRow conselleriaValor = sheet.createRow(rowNum++);
                for(int i=0;i<entradaConselleriaValor.size();i++){
                    HSSFCell conselleriaValorCol = conselleriaValor.createCell(i);
                    conselleriaValorCol.setCellValue(entradaConselleriaValor.get(i));
                    conselleriaValorCol.setCellStyle(fila);
                }
                if(entradaConselleriaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = entradaConselleriaNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Libros de Registro
            if(entradaLibroNombre.size() > 0){
                HSSFRow libroRowTitle = sheet.createRow(rowNum++);
                libroRowTitle.setHeightInPoints(15);
                HSSFCell libroCell = libroRowTitle.createCell(0);
                libroCell.setCellValue(getMessage("informe.porLibroRegistro"));
                libroCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow libroNom = sheet.createRow(rowNum++);
                for (int i = 0; i < entradaLibroNombre.size(); i++){
                    HSSFCell libroNomCol = libroNom.createCell(i);
                    libroNomCol.setCellValue(entradaLibroNombre.get(i));
                    libroNomCol.setCellStyle(cabecera);
                }
                HSSFRow libroValor = sheet.createRow(rowNum++);
                for(int i=0;i<entradaLibroValor.size();i++){
                    HSSFCell libroValorCol = libroValor.createCell(i);
                    libroValorCol.setCellValue(entradaLibroValor.get(i));
                    libroValorCol.setCellStyle(fila);
                }
                if(entradaLibroNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = entradaLibroNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Oficinas
            if(entradaOficinaNombre.size() > 0){
                HSSFRow oficinaRowTitle = sheet.createRow(rowNum++);
                oficinaRowTitle.setHeightInPoints(15);
                HSSFCell oficinaCell = oficinaRowTitle.createCell(0);
                oficinaCell.setCellValue(getMessage("informe.porOficina"));
                oficinaCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow oficinaNom = sheet.createRow(rowNum++);
                for (int i = 0; i < entradaOficinaNombre.size(); i++){
                    HSSFCell oficinaNomCol = oficinaNom.createCell(i);
                    oficinaNomCol.setCellValue(entradaOficinaNombre.get(i));
                    oficinaNomCol.setCellStyle(cabecera);
                }
                HSSFRow oficinaValor = sheet.createRow(rowNum++);
                for(int i=0;i<entradaOficinaValor.size();i++){
                    HSSFCell oficinaValorCol = oficinaValor.createCell(i);
                    oficinaValorCol.setCellValue(entradaOficinaValor.get(i));
                    oficinaValorCol.setCellStyle(fila);
                }
                if(entradaOficinaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = entradaOficinaNombre.size();
                }
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

        }

        // SALIDA
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADASALIDA) || tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)){

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
            if(campoCalendario == 0 || campoCalendario ==1){
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
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
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
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Consellerias
            if(salidaConselleriaNombre.size() > 0){
                HSSFRow conselleriaSalRowTitle = sheet.createRow(rowNum++);
                conselleriaSalRowTitle.setHeightInPoints(15);
                HSSFCell conselleriaSalCell = conselleriaSalRowTitle.createCell(0);
                conselleriaSalCell.setCellValue(getMessage("informe.porOrganismos"));
                conselleriaSalCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow conselleriaSalNom = sheet.createRow(rowNum++);
                for (int i = 0; i < salidaConselleriaNombre.size(); i++){
                    HSSFCell conselleriaSalNomCol = conselleriaSalNom.createCell(i);
                    conselleriaSalNomCol.setCellValue(salidaConselleriaNombre.get(i));
                    conselleriaSalNomCol.setCellStyle(cabecera);
                }
                HSSFRow conselleriaSalValor = sheet.createRow(rowNum++);
                for(int i=0;i<salidaConselleriaValor.size();i++){
                    HSSFCell conselleriaSalValorCol = conselleriaSalValor.createCell(i);
                    conselleriaSalValorCol.setCellValue(salidaConselleriaValor.get(i));
                    conselleriaSalValorCol.setCellStyle(fila);
                }
                if(salidaConselleriaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = salidaConselleriaNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Libros de Registro
            if(salidaLibroNombre.size() > 0){
                HSSFRow libroSalRowTitle = sheet.createRow(rowNum++);
                libroSalRowTitle.setHeightInPoints(15);
                HSSFCell libroSalCell = libroSalRowTitle.createCell(0);
                libroSalCell.setCellValue(getMessage("informe.porLibroRegistro"));
                libroSalCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow libroSalNom = sheet.createRow(rowNum++);
                for (int i = 0; i < salidaLibroNombre.size(); i++){
                    HSSFCell libroSalNomCol = libroSalNom.createCell(i);
                    libroSalNomCol.setCellValue(salidaLibroNombre.get(i));
                    libroSalNomCol.setCellStyle(cabecera);
                }
                HSSFRow libroSalValor = sheet.createRow(rowNum++);
                for(int i=0;i<salidaLibroValor.size();i++){
                    HSSFCell libroSalValorCol = libroSalValor.createCell(i);
                    libroSalValorCol.setCellValue(salidaLibroValor.get(i));
                    libroSalValorCol.setCellStyle(fila);
                }
                if(salidaLibroNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = salidaLibroNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Oficinas
            if(salidaOficinaNombre.size() > 0){
                HSSFRow oficinaSalRowTitle = sheet.createRow(rowNum++);
                oficinaSalRowTitle.setHeightInPoints(15);
                HSSFCell oficinaSalCell = oficinaSalRowTitle.createCell(0);
                oficinaSalCell.setCellValue(getMessage("informe.porOficina"));
                oficinaSalCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow oficinaSalNom = sheet.createRow(rowNum++);
                for (int i = 0; i < salidaOficinaNombre.size(); i++){
                    HSSFCell oficinaSalNomCol = oficinaSalNom.createCell(i);
                    oficinaSalNomCol.setCellValue(salidaOficinaNombre.get(i));
                    oficinaSalNomCol.setCellStyle(cabecera);
                }
                HSSFRow oficinaSalValor = sheet.createRow(rowNum++);
                for(int i=0;i<salidaOficinaValor.size();i++){
                    HSSFCell oficinaSalValorCol = oficinaSalValor.createCell(i);
                    oficinaSalValorCol.setCellValue(salidaOficinaValor.get(i));
                    oficinaSalValorCol.setCellStyle(fila);
                }
                if(salidaOficinaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = salidaOficinaNombre.size();
                }
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

        }
        
        // SIR ENVIADO
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_ENVIADO)){

            HSSFRow envRegRow = sheet.createRow(rowNum++);
            envRegRow.setHeightInPoints(15);
            HSSFCell envRegCell = envRegRow.createCell(0);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
            envRegCell.setCellValue(getMessage("informe.registrosSirEnviados"));
            envRegCell.setCellStyle(tituloSeccion);

            //Espai buit
            rowNum = rowNum + 1;

            //Registres totals
            HSSFRow regEnvNom = sheet.createRow(rowNum++);
            HSSFCell regEnvNomCol = regEnvNom.createCell(0);
            regEnvNomCol.setCellValue(getMessage("informe.registros"));
            regEnvNomCol.setCellStyle(cabecera);
            HSSFRow registreEnvRow = sheet.createRow(rowNum++);
            registreEnvRow.createCell(0).setCellValue(sirEnviados.toString());
            registreEnvRow.getCell(0).setCellStyle(fila);

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Años
            if(campoCalendario == 0 || campoCalendario ==1){
                HSSFRow anyEnvRowTitle = sheet.createRow(rowNum++);
                anyEnvRowTitle.setHeightInPoints(15);
                HSSFCell anyEnvCell = anyEnvRowTitle.createCell(0);
                anyEnvCell.setCellValue(getMessage("informe.anys"));
                anyEnvCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow anyEnvNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirEnviadosAnosNombre.size(); i++){
                    HSSFCell anyEnvNomCol = anyEnvNom.createCell(i);
                    anyEnvNomCol.setCellValue(sirEnviadosAnosNombre.get(i));
                    anyEnvNomCol.setCellStyle(cabecera);
                }
                HSSFCell anyEnvNomCol = anyEnvNom.createCell(sirEnviadosAnosNombre.size());
                anyEnvNomCol.setCellValue(getMessage("informe.total"));
                anyEnvNomCol.setCellStyle(cabecera);
                HSSFRow anyEnvValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirEnviadosAnosValor.size();i++){
                    HSSFCell anyEnvValorCol = anyEnvValor.createCell(i);
                    anyEnvValorCol.setCellValue(sirEnviadosAnosValor.get(i));
                    anyEnvValorCol.setCellStyle(fila);
                }
                HSSFCell anyEnvValorCol = anyEnvValor.createCell(sirEnviadosAnosValor.size());
                anyEnvValorCol.setCellValue(sirEnviados.toString());
                anyEnvValorCol.setCellStyle(fila);

                if(sirEnviadosAnosNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirEnviadosAnosNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
                HSSFRow mesEnvRowTitle = sheet.createRow(rowNum++);
                mesEnvRowTitle.setHeightInPoints(15);
                HSSFCell mesEnvCell = mesEnvRowTitle.createCell(0);
                mesEnvCell.setCellValue(getMessage("informe.mesos"));
                mesEnvCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow mesEnvNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirEnviadosMesesNombre.size(); i++){
                    HSSFCell mesEnvNomCol = mesEnvNom.createCell(i);
                    mesEnvNomCol.setCellValue(sirEnviadosMesesNombre.get(i));
                    mesEnvNomCol.setCellStyle(cabecera);
                }
                HSSFRow mesEnvValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirEnviadosMesesValor.size();i++){
                    HSSFCell mesEnvValorCol = mesEnvValor.createCell(i);
                    mesEnvValorCol.setCellValue(sirEnviadosMesesValor.get(i));
                    mesEnvValorCol.setCellStyle(fila);
                }
                if(sirEnviadosMesesNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirEnviadosMesesNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Consellerias
            if(sirEnviadosConselleriaNombre.size() > 0){
                HSSFRow conselleriaEnvRowTitle = sheet.createRow(rowNum++);
                conselleriaEnvRowTitle.setHeightInPoints(15);
                HSSFCell conselleriaEnvCell = conselleriaEnvRowTitle.createCell(0);
                conselleriaEnvCell.setCellValue(getMessage("informe.porOrganismos"));
                conselleriaEnvCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow conselleriaEnvNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirEnviadosConselleriaNombre.size(); i++){
                    HSSFCell conselleriaEnvNomCol = conselleriaEnvNom.createCell(i);
                    conselleriaEnvNomCol.setCellValue(sirEnviadosConselleriaNombre.get(i));
                    conselleriaEnvNomCol.setCellStyle(cabecera);
                }
                HSSFRow conselleriaEnvValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirEnviadosConselleriaValor.size();i++){
                    HSSFCell conselleriaEnvValorCol = conselleriaEnvValor.createCell(i);
                    conselleriaEnvValorCol.setCellValue(sirEnviadosConselleriaValor.get(i));
                    conselleriaEnvValorCol.setCellStyle(fila);
                }
                if(sirEnviadosConselleriaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirEnviadosConselleriaNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Oficinas
            if(sirEnviadosOficinaNombre.size() > 0){
                HSSFRow oficinaEnvRowTitle = sheet.createRow(rowNum++);
                oficinaEnvRowTitle.setHeightInPoints(15);
                HSSFCell oficinaEnvCell = oficinaEnvRowTitle.createCell(0);
                oficinaEnvCell.setCellValue(getMessage("informe.porOficina"));
                oficinaEnvCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow oficinaEnvNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirEnviadosOficinaNombre.size(); i++){
                    HSSFCell oficinaEnvNomCol = oficinaEnvNom.createCell(i);
                    oficinaEnvNomCol.setCellValue(sirEnviadosOficinaNombre.get(i));
                    oficinaEnvNomCol.setCellStyle(cabecera);
                }
                HSSFRow oficinaEnvValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirEnviadosOficinaValor.size();i++){
                    HSSFCell oficinaEnvValorCol = oficinaEnvValor.createCell(i);
                    oficinaEnvValorCol.setCellValue(sirEnviadosOficinaValor.get(i));
                    oficinaEnvValorCol.setCellStyle(fila);
                }
                if(sirEnviadosOficinaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirEnviadosOficinaNombre.size();
                }
            }
        }
        
        // SIR RECIBIDO
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SIR_RECIBIDO)){

            HSSFRow recRegRow = sheet.createRow(rowNum++);
            recRegRow.setHeightInPoints(15);
            HSSFCell recRegCell = recRegRow.createCell(0);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
            recRegCell.setCellValue(getMessage("informe.registrosSirEnviados"));
            recRegCell.setCellStyle(tituloSeccion);

            //Espai buit
            rowNum = rowNum + 1;

            //Registres totals
            HSSFRow regRecNom = sheet.createRow(rowNum++);
            HSSFCell regRecNomCol = regRecNom.createCell(0);
            regRecNomCol.setCellValue(getMessage("informe.registros"));
            regRecNomCol.setCellStyle(cabecera);
            HSSFRow registreRecRow = sheet.createRow(rowNum++);
            registreRecRow.createCell(0).setCellValue(sirRecibidos.toString());
            registreRecRow.getCell(0).setCellStyle(fila);

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Años
            if(campoCalendario == 0 || campoCalendario ==1){
                HSSFRow anyRecRowTitle = sheet.createRow(rowNum++);
                anyRecRowTitle.setHeightInPoints(15);
                HSSFCell anyRecCell = anyRecRowTitle.createCell(0);
                anyRecCell.setCellValue(getMessage("informe.anys"));
                anyRecCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow anyRecNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirRecibidosAnosNombre.size(); i++){
                    HSSFCell anyRecNomCol = anyRecNom.createCell(i);
                    anyRecNomCol.setCellValue(sirRecibidosAnosNombre.get(i));
                    anyRecNomCol.setCellStyle(cabecera);
                }
                HSSFCell anyRecNomCol = anyRecNom.createCell(sirRecibidosAnosNombre.size());
                anyRecNomCol.setCellValue(getMessage("informe.total"));
                anyRecNomCol.setCellStyle(cabecera);
                HSSFRow anyRecValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirRecibidosAnosValor.size();i++){
                    HSSFCell anyRecValorCol = anyRecValor.createCell(i);
                    anyRecValorCol.setCellValue(sirRecibidosAnosValor.get(i));
                    anyRecValorCol.setCellStyle(fila);
                }
                HSSFCell anyRecValorCol = anyRecValor.createCell(sirRecibidosAnosValor.size());
                anyRecValorCol.setCellValue(sirRecibidos.toString());
                anyRecValorCol.setCellStyle(fila);

                if(sirRecibidosAnosNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirRecibidosAnosNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Meses
            if(campoCalendario == 0 || campoCalendario ==2){
                HSSFRow mesRecRowTitle = sheet.createRow(rowNum++);
                mesRecRowTitle.setHeightInPoints(15);
                HSSFCell mesRecCell = mesRecRowTitle.createCell(0);
                mesRecCell.setCellValue(getMessage("informe.mesos"));
                mesRecCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow mesRecNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirRecibidosMesesNombre.size(); i++){
                    HSSFCell mesRecNomCol = mesRecNom.createCell(i);
                    mesRecNomCol.setCellValue(sirRecibidosMesesNombre.get(i));
                    mesRecNomCol.setCellStyle(cabecera);
                }
                HSSFRow mesRecValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirRecibidosMesesValor.size();i++){
                    HSSFCell mesRecValorCol = mesRecValor.createCell(i);
                    mesRecValorCol.setCellValue(sirRecibidosMesesValor.get(i));
                    mesRecValorCol.setCellStyle(fila);
                }
                if(sirRecibidosMesesNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirRecibidosMesesNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Consellerias
            if(sirRecibidosConselleriaNombre.size() > 0){
                HSSFRow conselleriaRecRowTitle = sheet.createRow(rowNum++);
                conselleriaRecRowTitle.setHeightInPoints(15);
                HSSFCell conselleriaRecCell = conselleriaRecRowTitle.createCell(0);
                conselleriaRecCell.setCellValue(getMessage("informe.porOrganismos"));
                conselleriaRecCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow conselleriaRecNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirRecibidosConselleriaNombre.size(); i++){
                    HSSFCell conselleriaRecNomCol = conselleriaRecNom.createCell(i);
                    conselleriaRecNomCol.setCellValue(sirRecibidosConselleriaNombre.get(i));
                    conselleriaRecNomCol.setCellStyle(cabecera);
                }
                HSSFRow conselleriaRecValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirRecibidosConselleriaValor.size();i++){
                    HSSFCell conselleriaRecValorCol = conselleriaRecValor.createCell(i);
                    conselleriaRecValorCol.setCellValue(sirRecibidosConselleriaValor.get(i));
                    conselleriaRecValorCol.setCellStyle(fila);
                }
                if(sirRecibidosConselleriaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirRecibidosConselleriaNombre.size();
                }
            }

            //Espai buit
            rowNum = rowNum + 1;

            //Muestra Oficinas
            if(sirRecibidosOficinaNombre.size() > 0){
                HSSFRow oficinaRecRowTitle = sheet.createRow(rowNum++);
                oficinaRecRowTitle.setHeightInPoints(15);
                HSSFCell oficinaRecCell = oficinaRecRowTitle.createCell(0);
                oficinaRecCell.setCellValue(getMessage("informe.porOficina"));
                oficinaRecCell.setCellStyle(tituloSeccion);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$"+rowNum+":$G$"+rowNum));
                //Espai buit
                rowNum = rowNum + 1;

                HSSFRow oficinaRecNom = sheet.createRow(rowNum++);
                for (int i = 0; i < sirRecibidosOficinaNombre.size(); i++){
                    HSSFCell oficinaRecNomCol = oficinaRecNom.createCell(i);
                    oficinaRecNomCol.setCellValue(sirRecibidosOficinaNombre.get(i));
                    oficinaRecNomCol.setCellStyle(cabecera);
                }
                HSSFRow oficinaRecValor = sheet.createRow(rowNum++);
                for(int i=0;i<sirRecibidosOficinaValor.size();i++){
                    HSSFCell oficinaRecValorCol = oficinaRecValor.createCell(i);
                    oficinaRecValorCol.setCellValue(sirRecibidosOficinaValor.get(i));
                    oficinaRecValorCol.setCellStyle(fila);
                }
                if(sirRecibidosOficinaNombre.size() > tamanyMaxColum){
                    tamanyMaxColum = sirRecibidosOficinaNombre.size();
                }
            }
        }

        // Ajustamos el ancho de cada columna a su contenido
        for (int i = 0; i < tamanyMaxColum; i++) {
            sheet.autoSizeColumn(i);
        }

        String nombreFichero = getMessage("informe.nombreFichero.indicadores")+ tipoRegistro +".xls";

        // Cabeceras Response
        response.setHeader("Content-Disposition","attachment; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");
    }

}
