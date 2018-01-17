package es.caib.regweb3.webapp.view;

import es.caib.regweb3.model.Persona;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 * User: jpernia
 * Date: 10/10/16
 */

public class ExportarPersonasExcel extends AbstractExcelView {

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

        //Obtenemos las personas
        List<Persona> personas = (List<Persona>) model.get("personas");

        int hojasExcel = (personas.size()/65000)+1;
        int resta = personas.size() % 65000;

        for (int k = 0; k < hojasExcel; k++) {
            boolean ultimaHoja = false;
            if (k == hojasExcel-1) {
                ultimaHoja = true;
            }

            HSSFSheet sheet = workbook.createSheet("REGWEB3_"+k);
            sheet.setFitToPage(true);

            //Estilo título
            HSSFCellStyle titulo;
            org.apache.poi.ss.usermodel.Font tituloFuente = workbook.createFont();
            tituloFuente.setFontHeightInPoints((short) 18);
            tituloFuente.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
            titulo = workbook.createCellStyle();
            titulo.setAlignment(CellStyle.ALIGN_CENTER);
            titulo.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            titulo.setFont(tituloFuente);

            //Estilo parametros de busqueda
            HSSFCellStyle paramCerca;
            org.apache.poi.ss.usermodel.Font paramCercaFuente = workbook.createFont();
            paramCercaFuente.setFontHeightInPoints((short) 12);
            paramCercaFuente.setBoldweight(org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD);
            paramCerca = workbook.createCellStyle();
            paramCerca.setAlignment(CellStyle.ALIGN_CENTER);
            paramCerca.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            paramCerca.setFont(paramCercaFuente);

            //Estilo cabecera
            HSSFCellStyle cabecera;
            org.apache.poi.ss.usermodel.Font cabeceraFuente = workbook.createFont();
            cabeceraFuente.setFontHeightInPoints((short) 10);
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
            filaFuente.setFontHeightInPoints((short) 8);
            fila = workbook.createCellStyle();
            fila.setBorderBottom(CellStyle.BORDER_THIN);
            fila.setBorderTop(CellStyle.BORDER_THIN);
            fila.setBorderLeft(CellStyle.BORDER_THIN);
            fila.setBorderRight(CellStyle.BORDER_THIN);
            fila.setFont(filaFuente);

            //Estilo títulos secciones
            HSSFCellStyle tituloSeccion;
            org.apache.poi.ss.usermodel.Font tituloSeccionFuente = workbook.createFont();
            tituloSeccionFuente.setFontHeightInPoints((short) 12);
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
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$F$1"));
            tittleCell.setCellValue(getMessage("persona.exportar.lista"));
            tittleCell.setCellStyle(titulo);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$F$2"));

            int rowNum = 1;
            HSSFRow header = sheet.createRow(rowNum++);
            header.setHeightInPoints(15);

            // Dades que se mostren d'una persona
            String[] capsalera = {"regweb.id", "persona.nombre", "persona.documento", "persona.tipoPersona", "persona.email", "persona.telefono"};

            // DADES A MOSTRAR
            // Capçalera
            HSSFRow titulos = sheet.createRow(rowNum++);
            titulos.setHeightInPoints(15);
            for (int i = 0; i < capsalera.length; i++) {
                HSSFCell titulosCol = titulos.createCell(i);
                titulosCol.setCellValue(getMessage(capsalera[i]));
                titulosCol.setCellStyle(cabecera);
            }

            // Persones
            int inici = k*65000;
            int fi;
            if(!ultimaHoja){
                fi = (k+1)*65000;
            }else{
                fi = (k*65000) + resta;
            }

            for (int i = inici; i < fi; i++) {
                HSSFRow row = sheet.createRow(rowNum++);
                Persona persona = personas.get(i);
                // Identificador
                row.createCell(0).setCellValue(persona.getId());
                // Nom
                if (persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_FISICA)) {
                    row.createCell(1).setCellValue(persona.getNombrePersonaFisicaCorto());
                } else {
                    row.createCell(1).setCellValue(persona.getRazonSocial());
                }
                // Document
                row.createCell(2).setCellValue(persona.getDocumento());
                // Tipo Persona
                if (persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_FISICA)) {
                    row.createCell(3).setCellValue(getMessage("persona.fisica.corto"));
                } else {
                    row.createCell(3).setCellValue(getMessage("persona.juridica.corto"));
                }
                // Mail
                row.createCell(4).setCellValue(persona.getEmail());
                // Telèfon
                row.createCell(5).setCellValue(persona.getTelefono());

                // Aplicam estils a les cel·les
                for (int g = 0; g < capsalera.length; g++) {
                    row.getCell(g).setCellStyle(fila);
                }
            }

            // Ajustamos el ancho de cada columna a su contenido
            for (int i = 0; i < capsalera.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String nombreFichero = getMessage("persona.exportar") + ".xls";

            // Cabeceras Response
            response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
            response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");

        }
    }

}