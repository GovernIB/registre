package es.caib.regweb3.webapp.view;

import es.caib.regweb3.model.PermisoLibroUsuario;
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
 * Date: 30/10/19
 */

public class ExportarUsuariosExcel extends AbstractExcelView {

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

        //Obtenemos los usuarios
        List<PermisoLibroUsuario> permisos = (List<PermisoLibroUsuario>) model.get("permisos");

        //Obtenemos el libro elegegido en la búsqueda
        String libro = (String) model.get("libro");

        int hojasExcel = (permisos.size()/65000)+1;
        int resta = permisos.size() % 65000;

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
            if(libro!=null) {
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$H$1"));
                tittleCell.setCellValue(getMessage("usuario.exportar.lista"));
                tittleCell.setCellStyle(titulo);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$H$2"));
            }else{
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$E$1"));
                tittleCell.setCellValue(getMessage("usuario.exportar.lista"));
                tittleCell.setCellStyle(titulo);
                sheet.addMergedRegion(CellRangeAddress.valueOf("$A$2:$E$2"));
            }

            int rowNum = 1;

            //Libro
            if(libro!=null) {
                HSSFRow lineaLibro = sheet.createRow(rowNum++);
                lineaLibro.setHeightInPoints(15);
                HSSFCell libroCol = lineaLibro.createCell(0);
                libroCol.setCellValue(getMessage("libro.libro")+ ": " + libro);
                libroCol.setCellStyle(fila);
            }

            HSSFRow header = sheet.createRow(rowNum++);
            header.setHeightInPoints(15);

            // Dades que se mostren d'un usuari
            String[] capsalera;
            if(libro!=null) {
                capsalera = new String[]{"usuario.identificador", "usuario.nombre", "usuario.documento", "usuario.tipoUsuario", "usuario.email", "permiso.entrada", "permiso.salida", "permiso.sir"};
            }else{
                capsalera = new String[]{"usuario.identificador", "usuario.nombre", "usuario.documento", "usuario.tipoUsuario", "usuario.email"};
            }

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
                PermisoLibroUsuario permisoLibroUsuario = permisos.get(i);
                // Identificador
                row.createCell(0).setCellValue(permisoLibroUsuario.getUsuario().getUsuario().getIdentificador());
                // Nom
                row.createCell(1).setCellValue(permisoLibroUsuario.getUsuario().getUsuario().getNombreCompleto());
                // Document
                row.createCell(2).setCellValue(permisoLibroUsuario.getUsuario().getUsuario().getDocumento());
                // Tipo Usuario
                if (permisoLibroUsuario.getUsuario().getUsuario().getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_PERSONA)) {
                    row.createCell(3).setCellValue(getMessage("usuario.tipo.persona"));
                } else {
                    row.createCell(3).setCellValue(getMessage("usuario.tipo.aplicacion"));
                }
                // Mail
                row.createCell(4).setCellValue(permisoLibroUsuario.getUsuario().getUsuario().getEmail());

                if(libro!=null) {
                    // Permiso
                    row.createCell(5).setCellValue("");
                    row.createCell(6).setCellValue("");
                    row.createCell(7).setCellValue("");
                    boolean mismoUsuario = true;
                    while (mismoUsuario) {
                        if (permisoLibroUsuario.getPermiso().equals(RegwebConstantes.PERMISO_REGISTRO_ENTRADA)) {
                            row.createCell(5).setCellValue("X");
                        } else {
                            if (permisoLibroUsuario.getPermiso().equals(RegwebConstantes.PERMISO_REGISTRO_SALIDA)) {
                                row.createCell(6).setCellValue("X");
                            } else {
                                if (permisoLibroUsuario.getPermiso().equals(RegwebConstantes.PERMISO_SIR)) {
                                    row.createCell(7).setCellValue("X");
                                }
                            }
                        }

                        if (i < permisos.size() - 1) {
                            if (!permisoLibroUsuario.getUsuario().getId().equals(permisos.get(i + 1).getUsuario().getId())) {
                                mismoUsuario = false;
                            } else {
                                i = i + 1;
                                permisoLibroUsuario = permisos.get(i);
                            }
                        } else {
                            mismoUsuario = false;
                        }
                    }
                }

                // Aplicam estils a les cel·les
                for (int g = 0; g < capsalera.length; g++) {
                    row.getCell(g).setCellStyle(fila);
                }
            }

            // Ajustamos el ancho de cada columna a su contenido
            for (int i = 0; i < capsalera.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String nombreFichero;
            if(libro == null) {
                nombreFichero = getMessage("usuario.exportar.fichero") + ".xls";
            }else{
                nombreFichero = getMessage("usuario.exportar.fichero") + "_" + libro + ".xls";
            }

            // Cabeceras Response
            response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero);
            response.setHeader("Content-Type", "application/vnd.ms-excel;charset=UTF-8");

        }
    }

}
