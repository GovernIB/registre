package es.caib.regweb3.plugins.justificante.apb.utils;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

public class LinkInCell implements PdfPCellEvent {
    protected String url;
    
    public LinkInCell(String url) {
        this.url = url;
    }
    
    public void cellLayout(PdfPCell cell, Rectangle position,
        PdfContentByte[] canvases) {
        PdfWriter writer = canvases[0].getPdfWriter();
        PdfAction action = new PdfAction(url);
        PdfAnnotation link = PdfAnnotation.createLink(
            writer, position, PdfAnnotation.HIGHLIGHT_INVERT, action);
        writer.addAnnotation(link);
    }
}