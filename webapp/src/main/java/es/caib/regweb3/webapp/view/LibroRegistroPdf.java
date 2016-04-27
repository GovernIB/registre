package es.caib.regweb3.webapp.view;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.AbstractIText5PdfView;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;

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
        Long tipo = (Long) model.get("tipo");
        String fechaInicio = (String) model.get("fechaInicio");
        String fechaFin = (String) model.get("fechaFin");
        String numRegistro = (String) model.get("numRegistro");
        String extracto = (String) model.get("extracto");
        Long estado = (Long) model.get("estado");
        String nombreInteresado = (String) model.get("nombreInteresado");
        String apell1Interesado = (String) model.get("apell1Interesado");
        String apell2Interesado = (String) model.get("apell2Interesado");
        String docInteresado = (String) model.get("docInteresado");
        String oficinaReg = (String) model.get("oficinaReg");
        Boolean anexos = (Boolean) model.get("anexos");
        String observaciones = (String) model.get("observaciones");
        String usuario = (String) model.get("usuario");
        String organDest = (String) model.get("organDest");
        String tipoAsunto = (String) model.get("tipoAsunto");

        Set<String> campos = (Set<String>) model.get("campos");
        ArrayList<ArrayList<String>> registrosLibro = (ArrayList<ArrayList<String>>) model.get("registrosLibro");

        //Configuraciones generales formato pdf
        if(campos.size() > 4){
            document.setPageSize(PageSize.A4.rotate());
        } else{
            document.setPageSize(PageSize.A4);
        }

        document.addAuthor("REGWEB3");
        document.addCreationDate();
        document.addCreator("iText library");
        document.newPage();
        Font font10Bold = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font font14Bold = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);

        String tipoRegistro = "";
        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)){
            tipoRegistro = getMessage("informe.entrada");
        }else if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)){
            tipoRegistro = getMessage("informe.salida");
        }
        String estadoRegistro = "";
        if(estado.equals((long) -1)){
            estadoRegistro = getMessage("informe.tots");
        }else if(estado.equals(RegwebConstantes.ESTADO_VALIDO)){
            estadoRegistro = getMessage("registro.estado.1");
        }else if(estado.equals(RegwebConstantes.ESTADO_PENDIENTE)){
            estadoRegistro = getMessage("registro.estado.2");
        }else if(estado.equals(RegwebConstantes.ESTADO_PENDIENTE_VISAR)){
            estadoRegistro = getMessage("registro.estado.3");
        }else if(estado.equals(RegwebConstantes.ESTADO_OFICIO_EXTERNO)){
            estadoRegistro = getMessage("registro.estado.4");
        }else if(estado.equals(RegwebConstantes.ESTADO_OFICIO_INTERNO)){
            estadoRegistro = getMessage("registro.estado.5");
        }else if(estado.equals(RegwebConstantes.ESTADO_ENVIADO)){
            estadoRegistro = getMessage("registro.estado.6");
        }else if(estado.equals(RegwebConstantes.ESTADO_TRAMITADO)){
            estadoRegistro = getMessage("registro.estado.7");
        }else if(estado.equals(RegwebConstantes.ESTADO_ANULADO)){
            estadoRegistro = getMessage("registro.estado.8");
        }
        String tieneAnexos;
        if(anexos){
            tieneAnexos = getMessage("regweb.si");
        }else{
            tieneAnexos = getMessage("regweb.indiferent");
        }

        // Título
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        titulo.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        titulo.getDefaultCell().setBorder(0);
        titulo.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        document.addTitle(getMessage("informe.llibreRegistres.informe"));
        titulo.addCell(new Paragraph(getMessage("informe.llibreRegistres"), font14Bold));
        titulo.addCell(new Paragraph(getMessage("informe.criteris")));
        document.add(titulo);
        document.add(new Paragraph(" "));

        //Tabla criterios de busqueda
        String[] nomCriteris = {"informe.tipo", "informe.fechaInicio", "informe.fechaFin", "informe.numRegistro", "informe.extracte",
                "informe.estat", "informe.nombreInteresado", "informe.apell1Interesado", "informe.apell2Interesado", "informe.docInteresado",
                "informe.oficinaReg", "informe.anexos", "informe.observacions", "informe.usuario", "informe.tipAsun", "informe.organDest"};
        String[] valorCriteris = {tipoRegistro, fechaInicio, fechaFin, numRegistro, extracto, estadoRegistro, nombreInteresado, apell1Interesado,
                apell2Interesado, docInteresado, oficinaReg, tieneAnexos, observaciones, usuario, tipoAsunto, organDest};

        if(tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)) {
            nomCriteris = (String[]) ArrayUtils.removeElement(nomCriteris, "informe.organDest");
            nomCriteris = (String[]) ArrayUtils.add(nomCriteris, "informe.organOrig");
        }

        PdfPTable criterio = new PdfPTable(nomCriteris.length);
        criterio.setWidthPercentage(100);
        criterio.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        criterio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        for (String nomCriteri : nomCriteris) {
            criterio.addCell(new Paragraph(getMessage(nomCriteri), font10));
        }
        document.add(criterio);

        PdfPTable criterio2 = new PdfPTable(nomCriteris.length);
        criterio2.setWidthPercentage(100);
        criterio2.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        criterio2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        for (String valorCriteri : valorCriteris) {
            criterio2.addCell(new Paragraph(valorCriteri, font10));
        }
        document.add(criterio2);
        document.add(new Paragraph(" "));

        PdfPTable resultats = new PdfPTable(1);
        resultats.setWidthPercentage(100);
        resultats.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        resultats.getDefaultCell().setBorder(0);
        resultats.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        resultats.addCell(new Paragraph(getMessage("informe.resultats")));
        document.add(resultats);
        document.add(new Paragraph(" "));


        //Registros
        PdfPTable table = new PdfPTable(campos.size());
        table.setWidthPercentage(100);

        if((campos != null)&&registrosLibro.size()>0){

            if(campos.size()!=0){

                for (String valorCamp : campos) {
                    if (valorCamp.equals("codAs")) {
                        PdfPCell cell1 = new PdfPCell(new Paragraph(getMessage("informe.codigoAsunto"), font10Bold));
                        cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell1);
                    }else if (valorCamp.equals("anyRe")) {
                        PdfPCell cell2 = new PdfPCell(new Paragraph(getMessage("informe.anoRegistro"), font10Bold));
                        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell2);
                    }else if (valorCamp.equals("estat")) {
                        PdfPCell cell3 = new PdfPCell(new Paragraph(getMessage("informe.estado"), font10Bold));
                        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell3);
                    }else if (valorCamp.equals("exped")) {
                        PdfPCell cell4 = new PdfPCell(new Paragraph(getMessage("informe.expediente"), font10Bold));
                        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell4);
                    }else if (valorCamp.equals("extra")) {
                        PdfPCell cell5 = new PdfPCell(new Paragraph(getMessage("informe.extracto"), font10Bold));
                        cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell5);
                    }else if (valorCamp.equals("datOr")) {
                        PdfPCell cell6 = new PdfPCell(new Paragraph(getMessage("informe.fechaOrigen"), font10Bold));
                        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell6);
                    }else if (valorCamp.equals("numRe")) {
                        PdfPCell cell7 = new PdfPCell(new Paragraph(getMessage("informe.numeroRegistro"), font10Bold));
                        cell7.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell7);
                    }else if (valorCamp.equals("ofici")) {
                        PdfPCell cell8 = new PdfPCell(new Paragraph(getMessage("informe.oficina"), font10Bold));
                        cell8.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell8);
                    }else if (valorCamp.equals("tipAs")) {
                        PdfPCell cell9 = new PdfPCell(new Paragraph(getMessage("informe.tipoAsunto"), font10Bold));
                        cell9.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell9);
                    }else if (valorCamp.equals("obser")) {
                        PdfPCell cell10 = new PdfPCell(new Paragraph(getMessage("informe.observaciones"), font10Bold));
                        cell10.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell10);
                    }else if (valorCamp.equals("llibr")) {
                        PdfPCell cell11 = new PdfPCell(new Paragraph(getMessage("informe.libro"), font10Bold));
                        cell11.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell11);
                    }else if (valorCamp.equals("data")) {
                        PdfPCell cell12 = new PdfPCell(new Paragraph(getMessage("informe.fechaRegistro"), font10Bold));
                        cell12.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell12);
                    }else if (valorCamp.equals("docFi")) {
                        PdfPCell cell13 = new PdfPCell(new Paragraph(getMessage("informe.documentacionFisica"), font10Bold));
                        cell13.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell13);
                    }else if (valorCamp.equals("orgDe")) {
                        if (tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_ENTRADA)) {
                            PdfPCell cell14 = new PdfPCell(new Paragraph(getMessage("informe.organismoDestino"), font10Bold));
                            cell14.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell14);
                        }
                        if (tipo.equals(RegwebConstantes.INFORME_TIPO_REGISTRO_SALIDA)) {
                            PdfPCell cell14 = new PdfPCell(new Paragraph(getMessage("informe.organismoOrigen"), font10Bold));
                            cell14.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                            table.addCell(cell14);
                        }
                    }else if (valorCamp.equals("idiom")) {
                        PdfPCell cell15 = new PdfPCell(new Paragraph(getMessage("informe.idioma"), font10Bold));
                        cell15.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell15);
                    }else if (valorCamp.equals("refEx")) {
                        PdfPCell cell16 = new PdfPCell(new Paragraph(getMessage("informe.referenciaExterna"), font10Bold));
                        cell16.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell16);
                    }else if (valorCamp.equals("trans")) {
                        PdfPCell cell17 = new PdfPCell(new Paragraph(getMessage("informe.transporte"), font10Bold));
                        cell17.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell17);
                    }else if (valorCamp.equals("numTr")) {
                        PdfPCell cell18 = new PdfPCell(new Paragraph(getMessage("informe.numeroTransporte"), font10Bold));
                        cell18.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell18);
                    }else if (valorCamp.equals("orgOr")) {
                        PdfPCell cell19 = new PdfPCell(new Paragraph(getMessage("informe.oficinaOrigen"), font10Bold));
                        cell19.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell19);
                    }else if (valorCamp.equals("numOr")) {
                        PdfPCell cell20 = new PdfPCell(new Paragraph(getMessage("informe.numeroRegistroOrigen"), font10Bold));
                        cell20.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell20);
                    }else if (valorCamp.equals("nomIn")) {
                        PdfPCell cell21 = new PdfPCell(new Paragraph(getMessage("informe.interesados"), font10Bold));
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
            buit.addCell(new Paragraph(getMessage("informe.registros.vacio"), font10Bold));
            document.add(buit);
        }

        String nombreFichero = getMessage("informe.nombreFichero.libroRegistro") + tipoRegistro +".pdf";

        // Cabeceras Response
        response.setHeader("Content-Disposition","attachment; filename="+nombreFichero);
        response.setHeader("Content-Type", "application/pdf;charset=UTF-8");

    }
}