package es.caib.regweb3.plugins.justificante.apb.test;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.*;

public class JustificanteApbPluginTest {

	/**
	 * Crea l'event d'Estampació dels logos per a que es faci a totes les pàgines
	 * que va creant el pdf
	 */
	
	private String rutaImatge = "C:\\Users\\jamalj\\Documents\\Documentación\\regweb\\logos\\portsdebalears.jpg";
    private String rutaImatgeAdm = "C:\\Users\\jamalj\\Documents\\Documentación\\regweb\\logos\\LogoAmdElec.jpg";

	Font font7 = FontFactory.getFont(FontFactory.HELVETICA, 7);

	private void onStartPage(PdfWriter writer, Document document) {

		try {
			// LOGOS
			PdfPTable logos = new PdfPTable(2);
			logos.setWidthPercentage(100);
			// Regweb3
			ClassLoader classLoader = getClass().getClassLoader();
			// InputStream fileRW = classLoader.getResourceAsStream(rutaImatgeAdm);
			PdfContentByte cb = writer.getDirectContent();
			Image logoRW = Image.getInstance(cb, ImageIO.read(new FileInputStream(rutaImatgeAdm)), 1);
			logoRW.setAlignment(Element.ALIGN_RIGHT);
			logoRW.scaleToFit(90, 50);
			logoRW.setAbsolutePosition(471f, 811f);
			Paragraph parrafo;
			parrafo = new Paragraph("");
			parrafo.setAlignment(Element.ALIGN_RIGHT);
			document.add(parrafo);
			document.add(logoRW);
			// Logo Entitat
			Image logoAPB = Image.getInstance(rutaImatge);
			if (logoAPB != null) {
				logoAPB.setAlignment(Element.ALIGN_LEFT);
				logoAPB.scaleToFit(210, 135);
				logoAPB.setAbsolutePosition(36f, 780f);
				parrafo = new Paragraph("");
				parrafo.setAlignment(Element.ALIGN_LEFT);
				document.add(parrafo);
				document.add(logoAPB);
			}
			// Sir
			if (true) {
				InputStream fileSIR = classLoader.getResourceAsStream("img/SIR_petit.jpg");
				Image logoSIR = Image.getInstance(cb, ImageIO.read(fileSIR), 1);
				logoSIR.setAlignment(Element.ALIGN_RIGHT);
				logoSIR.scaleToFit(100, 100);
				logoSIR.setAbsolutePosition(360f, 805f);
				parrafo = new Paragraph("");
				parrafo.setAlignment(Element.ALIGN_RIGHT);
				document.add(parrafo);
				document.add(logoSIR);
			}
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
		} catch (DocumentException ex) {
			// Atrapamos excepciones concernientes al documento.
		} catch (java.io.IOException ex) {
			// Atrapamos excepciones concernientes al I/O.
		}

	}

	@Test
	public void test() throws Exception {
		String csv = null;
		Chunk csvLink = null;

		try {
			Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.NORMAL);
			csv = "dsdasd-dsadas-dasdad-adsads-dads-dsad";
			csvLink = new Chunk(csv);
			csvLink.setFont(font8);
			csvLink.setAnchor("http://seu.portsdebalears.gob.es/csv?hash=" + csv).setFont(font8);

			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
			Document document = new Document(PageSize.A4);
			PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
			// Inicializa Documento
			document = inicialitzaDocument(document);
			onStartPage(pdfWriter, document);


			document.addTitle("justificante");
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

			// Añadimos los campos de la Información
			PdfPTable taulaAnnexe = new PdfPTable(new float[] { 15, 40, 10, 10, 10, 12 });
			taulaAnnexe.setWidthPercentage(100);
			PdfPCell cellInfoAnnexe = new PdfPCell(new Paragraph("justificante.nombreAdjunto"));
			cellInfoAnnexe.setBackgroundColor(BaseColor.WHITE);
			cellInfoAnnexe.setBorderColor(BaseColor.BLACK);
			cellInfoAnnexe.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			taulaAnnexe.addCell(cellInfoAnnexe);
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("justificante.csv")));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("justificante.tamanyo")));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("justificante.validez")));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("justificante.tipoAdjunto")));
//			taulaAnnexe.addCell(new PdfPCell(new Paragraph("justificante.hash")));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("justificante.observacionesAdjunto")));

			PdfPCell cellInfoAnnexe2 = new PdfPCell(new Paragraph("", font8));
			cellInfoAnnexe2.setBackgroundColor(BaseColor.WHITE);
			cellInfoAnnexe2.setBorderColor(BaseColor.BLACK);
			cellInfoAnnexe2.setBorderWidth(1f);
			cellInfoAnnexe2.setHorizontalAlignment(Element.ALIGN_LEFT);
			taulaAnnexe.addCell(cellInfoAnnexe2);
			if (csvLink != null) {
				Paragraph csvLn = new Paragraph();
				csvLn.add(csvLink);
				csvLn.setFont(font8);
				cellInfoAnnexe2 = new PdfPCell(csvLn);
				cellInfoAnnexe2.setRowspan(2);
				taulaAnnexe.addCell(cellInfoAnnexe2);

			}
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("1231231", font8)));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("tipoValidezDocumento.", font8)));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("tipoDocumento.0", font8)));
//			taulaAnnexe.addCell(new PdfPCell(new Paragraph("dsadasdadsadad", font8)));
			taulaAnnexe.addCell(new PdfPCell(new Paragraph("Observaciones", font8)));

			document.add(taulaAnnexe);

			document.close();

			byte[] pdf = baos.toByteArray();

			final File saveFile = File.createTempFile("TSA-", ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$
			final OutputStream os = new FileOutputStream(saveFile);
			os.write(pdf);
			os.flush();
			os.close();
			System.out.println("Temporal para comprobacion manual: " + saveFile.getAbsolutePath()); //$NON-NLS-1$

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Inicializa el Documento tanto para el registro de entrada como el de salida
	protected static Document inicialitzaDocument(Document document) throws Exception {

		// Build PDF document.
		document.open();

		// CONFIGURACIONES GENERALES FORMATO PDF
		document.setPageSize(PageSize.A4);
		document.addAuthor("REGWEB3");
		document.addCreationDate();
		document.addCreator("iText library");

		return document;

	}

}
