package es.caib.regweb3.webapp.view;

import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Fundació BIT.
 * User: jpernia
 * Date: 30/10/19
 */

public class ExportarUsuariosExcel extends AbstractExcelView {

    protected final Logger log = LoggerFactory.getLogger(getClass());

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

        //Obtenemos los usuarios
        Paginacion resultados = (Paginacion) model.get("resultados");

        int hojasExcel = (resultados.getListado().size()/65000)+1;
        int resta = resultados.getListado().size() % 65000;

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
            cabecera.setAlignment(CellStyle.ALIGN_CENTER);
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
            fila.setAlignment(CellStyle.ALIGN_CENTER);

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

            HSSFRow fechaInicioRow = sheet.createRow(3);
            fechaInicioRow.setHeightInPoints(15);

            HSSFRow fechaFinRow = sheet.createRow(4);
            fechaFinRow.setHeightInPoints(15);

            HSSFRow mostrarRow = sheet.createRow(5);
            mostrarRow.setHeightInPoints(15);


            //Título
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$E$1"));
            tittleCell.setCellValue(getMessage("usuario.exportar.lista"));
            tittleCell.setCellStyle(titulo);
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$E$2"));

            int rowNum = 1;

            HSSFRow header = sheet.createRow(rowNum++);
            header.setHeightInPoints(15);

            // Dades que se mostren d'un usuari
            String[] capsalera = new String[]{"usuario.identificador", "usuario.nombre", "usuario.documento", "usuario.tipoUsuario","usuario.oamr", "usuario.email"};

            // DADES A MOSTRAR
            // Capçalera
            HSSFRow titulos = sheet.createRow(rowNum++);
            titulos.setHeightInPoints(15);
            for (int i = 0; i < capsalera.length; i++) {
                HSSFCell titulosCol = titulos.createCell(i);
                titulosCol.setCellValue(getMessage(capsalera[i]));
                titulosCol.setCellStyle(cabecera);
            }

            // Usuaris
            int inici = k*65000;
            int fi;
            if(!ultimaHoja){
                fi = (k+1)*65000;
            }else{
                fi = (k*65000) + resta;
            }

            for (int i = inici; i < fi; i++) {

                HSSFRow row = sheet.createRow(rowNum++);
                UsuarioEntidad usuario = (UsuarioEntidad) resultados.getListado().get(i);
                // Identificador
                row.createCell(0).setCellValue(usuario.getUsuario().getIdentificador());
                // Nom
                row.createCell(1).setCellValue(usuario.getUsuario().getNombreCompleto());
                // Document
                row.createCell(2).setCellValue(usuario.getUsuario().getDocumento());
                // Tipo Usuario
                if (usuario.getUsuario().getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_PERSONA)) {
                    row.createCell(3).setCellValue(getMessage("usuario.tipo.persona"));
                } else {
                    row.createCell(3).setCellValue(getMessage("usuario.tipo.aplicacion"));
                }
                // OAMR
                if (usuario.getOamr()) {
                    row.createCell(4).setCellValue(getMessage("regweb.si"));
                } else {
                    row.createCell(4).setCellValue(getMessage("regweb.no"));
                }
                // Mail
                row.createCell(5).setCellValue(usuario.getUsuario().getEmail());

                // Aplicam estils a les cel·les
                for (int g = 0; g < capsalera.length; g++) {
                    row.getCell(g).setCellStyle(fila);
                }
            }

            // Ajustamos el ancho de cada columna a su contenido
            for (int i = 0; i < capsalera.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String nombreFichero = getMessage("usuario.exportar.fichero") + ".xls";

            // Cabeceras Response
            response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
            response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");

        }
    }

}
