package es.caib.regweb3.plugins.justificante.caib;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.AbstractPluginProperties;
import org.fundaciobit.plugins.utils.Base64;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * Created by Fundació BIT.
 * @author jpernia
 * Date: 01/03/2017
 *
 */
public class JustificanteCaibPlugin extends AbstractPluginProperties implements IJustificantePlugin {

    public static final String PROPERTY_CAIB_BASE = IJustificantePlugin.JUSTIFICANTE_BASE_PROPERTY + "caib.";

    protected final Logger log = Logger.getLogger(getClass());

    private String estampat = null;
    private String rutaImatge = null;
    private String declaracion = null;
    private String ley = null;
    private String validez = null;


    /**
     *  Crea l'event d'Estampació del csv i els Logos per a que es faci a totes les pàgines que va creant el pdf
     */
    class EstampaCSV extends PdfPageEventHelper {

        Font font7 = FontFactory.getFont(FontFactory.HELVETICA, 7);

        public void onStartPage(PdfWriter writer, Document document) {

            try{
                // LOGOS
                PdfPTable logos = new PdfPTable(2);
                logos.setWidthPercentage(100);
                // Regweb3
                ClassLoader classLoader = getClass().getClassLoader();
                InputStream fileRW = classLoader.getResourceAsStream("img/logo-regweb3.jpg");
                PdfContentByte cb = writer.getDirectContent();
                Image logoRW = Image.getInstance(cb, ImageIO.read(fileRW), 1);
                logoRW.setAlignment(Element.ALIGN_LEFT);
                logoRW.scaleToFit(100, 110);
                logoRW.setAbsolutePosition(35f, 790f);
                Paragraph parrafo;
                parrafo = new Paragraph("");
                parrafo.setAlignment(Element.ALIGN_LEFT);
                document.add(parrafo);
                document.add(logoRW);
                // Logo Entitat
                Image logoGovern = Image.getInstance(rutaImatge);
                if(logoGovern != null) {
                    logoGovern.setAlignment(Element.ALIGN_RIGHT);
                    logoGovern.scaleToFit(50, 50);
                    logoGovern.setAbsolutePosition(160f, 780f);
                    parrafo = new Paragraph("");
                    parrafo.setAlignment(Element.ALIGN_LEFT);
                    document.add(parrafo);
                    document.add(logoGovern);
                }
                // Sir
                InputStream fileSIR = classLoader.getResourceAsStream("img/SIR_petit.jpg");
                Image logoSIR = Image.getInstance(cb, ImageIO.read(fileSIR), 1);
                logoSIR.setAlignment(Element.ALIGN_RIGHT);
                logoSIR.scaleToFit(100, 100);
                logoSIR.setAbsolutePosition(460f, 790f);
                parrafo = new Paragraph("");
                parrafo.setAlignment(Element.ALIGN_RIGHT);
                document.add(parrafo);
                document.add(logoSIR);
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
            } catch (DocumentException ex) {
                // Atrapamos excepciones concernientes al documento.
            } catch (java.io.IOException ex) {
                // Atrapamos excepciones concernientes al I/O.
            }

        }

        public void onEndPage(PdfWriter writer, Document document) {
            // Añade el CSV como texto vertical
            PdfContentByte cb = writer.getDirectContent();
            Phrase p = new Phrase(estampat, font7);
            ColumnText.showTextAligned(cb, Element.ALIGN_MIDDLE, p, 20, 30, 90);

        }

    }


    /**
     *
     */
    public JustificanteCaibPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public JustificanteCaibPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public JustificanteCaibPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }


    /**
     * Inicialitza propietats amb strings de les propietats de Plugin de Justificante
     * @param locale
     * @throws Exception
     */
    private void inicializarPropiedades(Locale locale, String url, String specialValue, String csv) throws Exception{
        rutaImatge = this.getProperty(PROPERTY_CAIB_BASE + "logoPath");
        declaracion = this.getProperty(PROPERTY_CAIB_BASE + "declaracion." + locale);
        ley = this.getProperty(PROPERTY_CAIB_BASE + "ley." + locale);
        validez = this.getProperty(PROPERTY_CAIB_BASE + "validez." + locale);
        String estampacion = this.getPropertyRequired(PROPERTY_CAIB_BASE + "estampacion");
        estampat = MessageFormat.format(estampacion, url, specialValue, csv);
    }

    @Override
    public byte[] generarJustificanteEntrada(RegistroEntrada registroEntrada, String url, String specialValue, String csv, String idioma) throws Exception{

        // Define idioma para el justificante
        Locale locale = new Locale(idioma);

        //Inicializamos las propiedades comunes
        inicializarPropiedades(locale, url, specialValue,csv);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

        // Aplica preferencias
        Document document = new Document(PageSize.A4);
        FileOutputStream ficheroPdf = new FileOutputStream("fichero.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        PdfWriter.getInstance(document,ficheroPdf).setInitialLeading(20);

        // Crea el evento para generar la estampación de csv en cada página
        EstampaCSV event = new EstampaCSV();
        writer.setPageEvent(event);

        // Inicializa Documento
        document = inicialitzaDocument(document);

        // Comienza a crear el Justificante
        String denominacionOficina = registroEntrada.getOficina().getDenominacion();
        String codigoOficina = registroEntrada.getOficina().getCodigo();
        String numeroRegistroFormateado = registroEntrada.getNumeroRegistroFormateado();
        Long tipoDocumentacionFisica = registroEntrada.getRegistroDetalle().getTipoDocumentacionFisica();
        String extracte = registroEntrada.getRegistroDetalle().getExtracto();
        String nomDesti;
        if(registroEntrada.getDestino()!=null) {
            nomDesti = registroEntrada.getDestino().getNombreCompleto() + " - " + registroEntrada.getDestino().getCodigo();
        }else{
            nomDesti = registroEntrada.getDestinoExternoDenominacion() + " - " + registroEntrada.getDestinoExternoCodigo();
        }
        String expedient = registroEntrada.getRegistroDetalle().getExpediente();
        Date fechaRegistro = registroEntrada.getFecha();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataRegistre = formatDate.format(fechaRegistro);
        //String dataActual = formatDate.format(Calendar.getInstance().getTime());  (S'empra al codi de barres)

        // Título e Información Registro
        informacioRegistre(locale, document, denominacionOficina, codigoOficina, dataRegistre, numeroRegistroFormateado, tipoDocumentacionFisica);

        // Interesados
        List<Interesado> interesados = registroEntrada.getRegistroDetalle().getInteresados();
        llistarInteressats(interesados, locale, document);

        // Información adicional del Registro
        adicionalRegistre(locale, document, extracte, nomDesti, expedient, registroEntrada.getClass().getSimpleName());

        // Anexos del Registro
        List<AnexoFull> anexos = registroEntrada.getRegistroDetalle().getAnexosFull();
        llistarAnnexes(anexos, locale, document, denominacionOficina);

        document.close();

        return baos.toByteArray();
    }


    @Override
    public byte[] generarJustificanteSalida(RegistroSalida registroSalida, String url, String specialValue, String csv, String idioma) throws Exception{

        // Define idioma para el justificante
        Locale locale = new Locale(idioma);

        //Inicializamos las propiedades comunes
        inicializarPropiedades(locale, url, specialValue,csv);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

        // Aplica preferencias
        Document document = new Document(PageSize.A4);
        FileOutputStream ficheroPdf = new FileOutputStream("fichero.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        PdfWriter.getInstance(document,ficheroPdf).setInitialLeading(20);

        // Crea el evento para generar la estampación de csv en cada página
        EstampaCSV event = new EstampaCSV();
        writer.setPageEvent(event);

        // Inicializa Documento
        document = inicialitzaDocument(document);

        // Comienza a crear el Justificante
        String denominacionOficina = registroSalida.getOficina().getDenominacion();
        String codigoOficina = registroSalida.getOficina().getCodigo();
        String numeroRegistroFormateado = registroSalida.getNumeroRegistroFormateado();
        Long tipoDocumentacionFisica = registroSalida.getRegistroDetalle().getTipoDocumentacionFisica();
        String extracte = registroSalida.getRegistroDetalle().getExtracto();
        String nomOrigen = registroSalida.getOrigen().getNombreCompleto();
        String expedient = registroSalida.getRegistroDetalle().getExpediente();
        Date fechaRegistro = registroSalida.getFecha();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataRegistre = formatDate.format(fechaRegistro);
        //String dataActual = formatDate.format(Calendar.getInstance().getTime());   (S'empra al Codi de Barres)

        // Título e Información Registro
        informacioRegistre(locale, document, denominacionOficina, codigoOficina, dataRegistre, numeroRegistroFormateado, tipoDocumentacionFisica);

        // Interesados
        List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();
        llistarInteressats(interesados, locale, document);

        // Información adicional del Registro
        adicionalRegistre(locale, document, extracte, nomOrigen, expedient, registroSalida.getClass().getSimpleName());

        // Anexos del Registro
        List<AnexoFull> anexos = registroSalida.getRegistroDetalle().getAnexosFull();
        llistarAnnexes(anexos, locale, document, denominacionOficina);

        document.close();

        return baos.toByteArray();
    }


    // Inicializa el Documento tanto para el registro de entrada como el de salida
    protected Document inicialitzaDocument(Document document) throws Exception {

        // Build PDF document.
        document.open();

        //CONFIGURACIONES GENERALES FORMATO PDF
        document.setPageSize(PageSize.A4);
        document.addAuthor("REGWEB3");
        document.addCreationDate();
        document.addCreator("iText library");

        return document;

    }

    // Lista los anexos tanto para el registro de entrada como el de salida
    protected void llistarAnnexes(List<AnexoFull> anexos, Locale locale, Document document, String denominacio) throws Exception {

        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        Font font8Bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);

        log.info("anexes: " + anexos.size());
        if(anexos.size()>0) {
            // Creamos estilo para el título Adjuntos
            PdfPTable titolAnnexe = new PdfPTable(1);
            titolAnnexe.setWidthPercentage(100);
            PdfPCell cellAnnexe = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.adjuntos"), font8Bold));
            cellAnnexe.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellAnnexe.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellAnnexe.setBorder(Rectangle.BOTTOM);
            cellAnnexe.setBorderColor(BaseColor.BLACK);
            cellAnnexe.setBorderWidth(2f);
            titolAnnexe.addCell(cellAnnexe);
            document.add(titolAnnexe);
            document.add(new Paragraph(" "));

            // Añadimos los campos de la Información
            PdfPTable taulaAnnexe = new PdfPTable(new float[]{20, 10, 15, 15, 25, 15});
            taulaAnnexe.setWidthPercentage(100);
            PdfPCell cellInfoAnnexe = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.nombreAdjunto"), font8));
            cellInfoAnnexe.setBackgroundColor(BaseColor.WHITE);
            cellInfoAnnexe.setBorderColor(BaseColor.BLACK);
            cellInfoAnnexe.setBorderWidth(1f);
            cellInfoAnnexe.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            taulaAnnexe.addCell(cellInfoAnnexe);
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.tamanyo"), font8)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.validez"), font8)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.tipoAdjunto"), font8)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.hash"), font8)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.observacionesAdjunto"), font8)));

            PdfPCell cellInfoAnnexe2 = new PdfPCell(new Paragraph("", font8));
            cellInfoAnnexe2.setBackgroundColor(BaseColor.WHITE);
            cellInfoAnnexe2.setBorderColor(BaseColor.BLACK);
            cellInfoAnnexe2.setBorderWidth(1f);
            cellInfoAnnexe2.setHorizontalAlignment(Element.ALIGN_LEFT);
            for(AnexoFull anexo : anexos) {
                cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), font8));
                taulaAnnexe.addCell(cellInfoAnnexe2);

                // Calcula i afegeix el tamany del document (Del DocumentCustody i si és null del SignatureCustody)
                String tamanyFitxer = null;
                if(anexo.getDocumentoCustody()!=null){
                    if(anexo.getDocumentoCustody().getData().length < 1024){
                        tamanyFitxer = "1 KB";
                    }else{
                        tamanyFitxer = String.valueOf(anexo.getDocumentoCustody().getData().length/1024) + " KB";
                    }
                }else {
                    if (anexo.getSignatureCustody() != null) {
                        if (anexo.getSignatureCustody().getData().length < 1024) {
                            tamanyFitxer = "1 KB";
                        } else {
                            tamanyFitxer = String.valueOf(anexo.getSignatureCustody().getData().length / 1024) + " KB";
                        }
                    }
                }
                taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, font8)));
                taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"tipoValidezDocumento." + anexo.getAnexo().getValidezDocumento()), font8)));
                taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"tipoDocumento.0" + anexo.getAnexo().getTipoDocumento()), font8)));
                taulaAnnexe.addCell(new PdfPCell(new Paragraph(Base64.encode(anexo.getAnexo().getHash()), font8)));
                taulaAnnexe.addCell(new PdfPCell(new Paragraph(anexo.getAnexo().getObservaciones(), font8)));
            }
            document.add(taulaAnnexe);
        }

        // Pie de anexo
        PdfPTable peuAnnexe = new PdfPTable(1);
        peuAnnexe.setWidthPercentage(100);
        PdfPCell cellPeuAnnexe = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.la") + " " + denominacio + " " + declaracion, font8));
        cellPeuAnnexe.setBackgroundColor(BaseColor.WHITE);
        cellPeuAnnexe.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellPeuAnnexe.setBorder(Rectangle.TOP);
        cellPeuAnnexe.setBorderColor(BaseColor.BLACK);
        cellPeuAnnexe.setBorderWidth(1f);
        peuAnnexe.addCell(cellPeuAnnexe);
        document.add(peuAnnexe);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Parágrafo Ley
        PdfPTable titolLlei = new PdfPTable(1);
        titolLlei.setWidthPercentage(100);
        PdfPCell cellLlei = new PdfPCell(new Paragraph(ley, font8));
        cellLlei.setBackgroundColor(BaseColor.WHITE);
        cellLlei.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellLlei.setBorderColor(BaseColor.WHITE);
        cellLlei.setBorderWidth(0f);
        titolLlei.addCell(cellLlei);
        document.add(titolLlei);

        // Parágrafo Plazos
        PdfPTable titolPlazos = new PdfPTable(1);
        titolPlazos.setWidthPercentage(100);
        PdfPCell cellPlazos = new PdfPCell(new Paragraph(validez, font8));
        cellPlazos.setBackgroundColor(BaseColor.WHITE);
        cellPlazos.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellPlazos.setBorderColor(BaseColor.WHITE);
        cellPlazos.setBorderWidth(0f);
        titolPlazos.addCell(cellPlazos);
        document.add(titolPlazos);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

    }

    // Lista los Interesados y Representantes tanto para el registro de entrada como el de salida
    protected void llistarInteressats(List<Interesado> interesados, Locale locale, Document document) throws Exception {

        Font font8gris = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        font8gris.setColor(BaseColor.GRAY);
        Font font8negre = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        Font font8bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);

        // Creamos estilo para el título Interesado
        PdfPTable titolInteressat = new PdfPTable(1);
        titolInteressat.setWidthPercentage(100);
        PdfPCell cellInteressat = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.interesado"), font8bold));
        cellInteressat.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellInteressat.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellInteressat.setBorder(Rectangle.BOTTOM);
        cellInteressat.setBorderColor(BaseColor.BLACK);
        cellInteressat.setBorderWidth(2f);
        titolInteressat.addCell(cellInteressat);

        // Creamos estilo para el título Representante
        PdfPTable titolRepresentant = new PdfPTable(1);
        titolRepresentant.setWidthPercentage(90);

        PdfPTable taulaInteresado = new PdfPTable(new float[] { 15, 30, 15, 30 });
        // Añadimos una entrada para cada interesado
        for(Interesado interesado : interesados) {
            // Añadimos título
            if(!interesado.getIsRepresentante()) {
                document.add(titolInteressat);
                // Añadimos campos del interesado
                taulaInteresado.setWidthPercentage(100);
                taulaInteresado.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                taulaInteresado.getDefaultCell().setBorder(0);
                taulaInteresado.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                // Document
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.identificacion"), font8gris));
                taulaInteresado.addCell(new Paragraph(interesado.getDocumento(), font8negre));
                // Pais
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.pais"), font8gris));
                if(interesado.getPais() != null) {
                    taulaInteresado.addCell(new Paragraph(interesado.getPais().getDescripcionPais(), font8negre));
                } else{ taulaInteresado.addCell(""); }
                // Nom - Organ
                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.nombre"), font8gris));
                    taulaInteresado.addCell(new Paragraph(interesado.getNombreCompleto(), font8negre));
                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.razonSocial"), font8gris));
                    taulaInteresado.addCell(new Paragraph(interesado.getRazonSocial(), font8negre));
                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.organismo"), font8gris));
                    taulaInteresado.addCell(new Paragraph(interesado.getRazonSocial(), font8negre));
                }
                // Telefon
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.telefono"), font8gris));
                taulaInteresado.addCell(new Paragraph(interesado.getTelefono(), font8negre));
                // Adreça
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.direccion"), font8gris));
                taulaInteresado.addCell(new Paragraph(interesado.getDireccion(), font8negre));
                // Mail
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.correo"), font8gris));
                taulaInteresado.addCell(new Paragraph(interesado.getEmail(), font8negre));
                // Municipi
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.municipio"), font8gris));
                if(interesado.getLocalidad() != null && interesado.getCp() != null) {
                    taulaInteresado.addCell(new Paragraph(interesado.getCp() + " - " + interesado.getLocalidad().getNombre(), font8negre));
                } else{ taulaInteresado.addCell(""); }
                // Canal Notificacio
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.canalNot"), font8gris));
                if(interesado.getCanal() != null) {
                    String canalNotif = tradueixMissatge(locale,"canalNotificacion." + interesado.getCanal());
                    taulaInteresado.addCell(new Paragraph(canalNotif, font8negre));
                } else{ taulaInteresado.addCell(""); }
                // Provincia
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.provincia"), font8gris));
                if(interesado.getProvincia() != null) {
                    taulaInteresado.addCell(new Paragraph(interesado.getProvincia().getDescripcionProvincia(), font8negre));
                } else{ taulaInteresado.addCell(""); }
                // Observacions
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.observaciones"), font8gris));
                taulaInteresado.addCell(new Paragraph(interesado.getObservaciones(), font8negre));
                // Direccio Electronica Habilitada
                taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.deh"), font8gris));
                taulaInteresado.addCell(new Paragraph(interesado.getDireccionElectronica(), font8negre));
                // Completa la cel·la buida
                taulaInteresado.addCell("");
                taulaInteresado.addCell("");

                document.add(taulaInteresado);
                // Vaciamos el contenido del interesado para rellenarlo con uno nuevo
                taulaInteresado.deleteBodyRows();
                document.add(new Paragraph(" "));

                // Si el interesado tiene representante
                if(interesado.getRepresentante() != null) {
                    // Recorremos todos los interesados para buscar el reresentante
                    for(Interesado representante : interesados) {
                        // Encuentra su representante y lo muestra
                        if (interesado.getRepresentante().getId().equals(representante.getId())) {
                            PdfPCell cellRepresentant = new PdfPCell(new Paragraph(tradueixMissatge(locale, "justificante.representante") + " de " + interesado.getNombreCompleto(), font8gris));
                            cellRepresentant.setBackgroundColor(BaseColor.WHITE);
                            cellRepresentant.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cellRepresentant.setBorder(Rectangle.BOTTOM);
                            cellRepresentant.setBorderColor(BaseColor.BLACK);
                            cellRepresentant.setBorderWidth(2f);
                            titolRepresentant.addCell(cellRepresentant);
                            document.add(titolRepresentant);
                            PdfPTable taulaRepresentant = new PdfPTable(new float[] { 13, 28, 13, 28 });
                            // Añadimos campos del Representante
                            taulaRepresentant.setWidthPercentage(90);
                            taulaRepresentant.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                            taulaRepresentant.getDefaultCell().setBorder(0);
                            taulaRepresentant.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                            // Document
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.identificacion"), font8gris));
                            taulaRepresentant.addCell(new Paragraph(representante.getDocumento(), font8negre));
                            // Pais
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.pais"), font8gris));
                            if(representante.getPais() != null) {
                                taulaRepresentant.addCell(new Paragraph(representante.getPais().getDescripcionPais(), font8negre));
                            } else{ taulaRepresentant.addCell(""); }
                            // Nom - Organ
                            if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.nombre"), font8gris));
                                taulaRepresentant.addCell(new Paragraph(representante.getNombreCompleto(), font8negre));
                            } else if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.razonSocial"), font8gris));
                                taulaRepresentant.addCell(new Paragraph(representante.getRazonSocial(), font8negre));
                            } else if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.organismo"), font8gris));
                                taulaRepresentant.addCell(new Paragraph(representante.getRazonSocial(), font8negre));
                            }
                            // Telefon
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.telefono"), font8gris));
                            taulaRepresentant.addCell(new Paragraph(representante.getTelefono(), font8negre));
                            // Adreça
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.direccion"), font8gris));
                            taulaRepresentant.addCell(new Paragraph(representante.getDireccion(), font8negre));
                            // Mail
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.correo"), font8gris));
                            taulaRepresentant.addCell(new Paragraph(representante.getEmail(), font8negre));
                            // Municipi
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.municipio"), font8gris));
                            if(representante.getLocalidad() != null && representante.getCp() != null) {
                                taulaRepresentant.addCell(new Paragraph(representante.getCp() + " - " + representante.getLocalidad().getNombre(), font8negre));
                            } else{ taulaRepresentant.addCell(""); }
                            // Canal Notificacio
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.canalNot"), font8gris));
                            if(representante.getCanal() != null) {
                                String canalNotifRep = tradueixMissatge(locale,"canalNotificacion." + representante.getCanal());
                                taulaRepresentant.addCell(new Paragraph(canalNotifRep, font8negre));
                            } else{ taulaRepresentant.addCell(""); }
                            // Provincia
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.provincia"), font8gris));
                            if(representante.getProvincia() != null) {
                                taulaRepresentant.addCell(new Paragraph(representante.getProvincia().getDescripcionProvincia(), font8negre));
                            } else{ taulaRepresentant.addCell(""); }
                            // Observacions
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.observaciones"), font8gris));
                            taulaRepresentant.addCell(new Paragraph(representante.getObservaciones(), font8negre));
                            // Direccio Electronica Habilitada
                            taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.deh"), font8gris));
                            taulaRepresentant.addCell(new Paragraph(representante.getDireccionElectronica(), font8negre));
                            // Completa la cel·la buida
                            taulaRepresentant.addCell("");
                            taulaRepresentant.addCell("");

                            document.add(taulaRepresentant);
                            // Vaciamos el contenido del representante para rellenarlo con uno nuevo
                            taulaRepresentant.deleteBodyRows();
                            document.add(new Paragraph(" "));

                            titolRepresentant.deleteBodyRows();
                        }
                    }
                }
            }
        }
        document.add(new Paragraph(" "));
    }

    // Añade el título y la información de registro
    protected void informacioRegistre(Locale locale, Document document, String denominacionOficina, String codigoOficina,
                                      String dataRegistre, String numeroRegistroFormateado, Long tipoDocumentacionFisica) throws Exception {

        Font font8gris = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        font8gris.setColor(BaseColor.GRAY);
        Font font8negre = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        Font font14bold = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
        Font font8bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);

        document.addTitle(tradueixMissatge(locale,"justificante.anexo.titulo"));
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        PdfPCell cellTitulo= new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.mensaje.titulo"), font14bold));
        cellTitulo.setBackgroundColor(BaseColor.WHITE);
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBorder(Rectangle.BOTTOM);
        cellTitulo.setBorderColor(BaseColor.BLACK);
        cellTitulo.setBorderWidth(2f);
        cellTitulo.setPaddingBottom(10);
        titulo.addCell(cellTitulo);
        document.add(titulo);

        // Registro
        PdfPTable taulaRegistre = new PdfPTable(new float[] { 1, 3 });
        taulaRegistre.setWidthPercentage(100);
        taulaRegistre.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        taulaRegistre.getDefaultCell().setBorder(0);
        taulaRegistre.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.oficina"), font8gris));
        String oficina = denominacionOficina + " - " + codigoOficina;
        taulaRegistre.addCell(new Paragraph(oficina, font8negre));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.fechaPresentacion"), font8gris));
        taulaRegistre.addCell(new Paragraph(dataRegistre, font8negre));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.numRegistro"), font8gris));
        taulaRegistre.addCell(new Paragraph(numeroRegistroFormateado, font8bold));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.docFisica"), font8gris));
        String docFisica = tradueixMissatge(locale,"tipoDocumentacionFisica." + tipoDocumentacionFisica);
        taulaRegistre.addCell(new Paragraph(docFisica, font8negre));
        document.add(taulaRegistre);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

    }

    // Añade más información al registro
    protected void adicionalRegistre(Locale locale, Document document, String extracte, String nomDesti, String expedient,
                                     String tipoRegistro) throws Exception {

        Font font8gris = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        font8gris.setColor(BaseColor.GRAY);
        Font font8negre = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        Font font8bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);

        // Creamos estilo para el título Información
        PdfPTable titolInformacio = new PdfPTable(1);
        titolInformacio.setWidthPercentage(100);
        PdfPCell cellInformacio = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.informacion"), font8bold));
        cellInformacio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellInformacio.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellInformacio.setBorder(Rectangle.BOTTOM);
        cellInformacio.setBorderColor(BaseColor.BLACK);
        cellInformacio.setBorderWidth(2f);
        titolInformacio.addCell(cellInformacio);
        document.add(titolInformacio);

        // Añadimos los campos de la Información
        PdfPTable taulaInformacio = new PdfPTable(new float[] { 25, 75 });
        taulaInformacio.setWidthPercentage(100);
        taulaInformacio.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        taulaInformacio.getDefaultCell().setBorder(0);
        taulaInformacio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.tipoAsiento"), font8gris));
        if(tipoRegistro.equals("RegistroEntrada")) {
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.entrada"), font8negre));
        }else{
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.salida"), font8negre));
        }
        taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.resumen"), font8gris));
        taulaInformacio.addCell(new Paragraph(extracte, font8negre));
        taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.unidad"), font8gris));
        taulaInformacio.addCell(new Paragraph(nomDesti, font8negre));
        taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.expediente"), font8gris));
        taulaInformacio.addCell(new Paragraph(expedient, font8negre));
        document.add(taulaInformacio);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

    }

    // COMENTADO POR SI AL FINAL AÑADIMOS ALGO DE CODIGO DE BARRAS, si no se usa ELIMINARLO
    // Añade la información del pie, CSV, etc
    /*protected void csvRegistre(Document document, String dataActual, String numeroRegistroFormateado,
                               PdfWriter writer, String url, String specialValue, String csv) throws Exception {

        Font font9Underline = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.UNDERLINE);
        Font font9 = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font font8 = FontFactory.getFont(FontFactory.HELVETICA, 8);
        Font font7 = FontFactory.getFont(FontFactory.HELVETICA, 7);

        // Añadimos la separación
        PdfPTable csv = new PdfPTable(1);
        csv.setWidthPercentage(100);
        PdfPCell cellCsv= new PdfPCell(new Paragraph("", font8));
        cellCsv.setBackgroundColor(BaseColor.WHITE);
        cellCsv.setBorder(Rectangle.BOTTOM);
        cellCsv.setBorderColor(BaseColor.BLACK);
        cellCsv.setBorderWidth(2f);
        csv.addCell(cellCsv);
        document.add(csv);
        document.add(new Paragraph(" "));
        // Añadimos los campos
        // Primera fila
        PdfPTable taulaCsv = new PdfPTable(new float[]{25, 45, 30 });
        taulaCsv.setWidthPercentage(100);
        PdfPCell cellInfoCsv = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.ambito"), font9Underline));
        cellInfoCsv.setBackgroundColor(BaseColor.WHITE);
        cellInfoCsv.setBorderWidth(0f);
        cellInfoCsv.setBorderColor(BaseColor.WHITE);
        cellInfoCsv.setHorizontalAlignment(Element.ALIGN_LEFT);
        taulaCsv.addCell(cellInfoCsv);
        cellInfoCsv = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.numRegistro"), font9Underline));
        cellInfoCsv.setBorderWidth(0f);
        taulaCsv.addCell(cellInfoCsv);
        cellInfoCsv = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.fechaDocumento"), font9Underline));
        cellInfoCsv.setBorderWidth(0f);
        taulaCsv.addCell(cellInfoCsv);
        cellInfoCsv = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.aplicacion"), font9));
        cellInfoCsv.setBorderWidth(0f);
        taulaCsv.addCell(cellInfoCsv);
        cellInfoCsv = new PdfPCell(new Paragraph(numeroRegistroFormateado, font9));
        cellInfoCsv.setBorderWidth(0f);
        taulaCsv.addCell(cellInfoCsv);
        cellInfoCsv = new PdfPCell(new Paragraph(dataActual, font9));
        cellInfoCsv.setBorderWidth(0f);
        taulaCsv.addCell(cellInfoCsv);
        document.add(taulaCsv);
        // Segona fila
        PdfPTable taulaCsv1 = new PdfPTable(new float[]{100 });
        taulaCsv1.setWidthPercentage(100);
        PdfPCell cellInfoCsv1 = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.validacion"), font9Underline));
        cellInfoCsv1.setBorderWidth(0f);
        taulaCsv1.addCell(cellInfoCsv1);
        cellInfoCsv1 = new PdfPCell(new Paragraph(urlVerificacio, font9));
        cellInfoCsv1.setBorderWidth(0f);
        taulaCsv1.addCell(cellInfoCsv1);
        document.add(taulaCsv1);

        // Código de barras
        PdfContentByte cb = writer.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setCode(urlVerificacio);
        code128.setCodeType(Barcode128.CODE128);
        Image code128Image = code128.createImageWithBarcode(cb, null, null);
        code128Image.scalePercent(75);
        code128Image.setAlignment(Element.ALIGN_MIDDLE);
        document.add(code128Image);


        // Texto Vertical
        PdfContentByte cb = writer.getDirectContent();
        Phrase p = new Phrase(estampat, font7);
        ColumnText.showTextAligned(cb, Element.ALIGN_MIDDLE, p, 20, 30, 90);

    }*/

    /**
     *  Obtiene los mensajes que aparecen traducidos en el pdf del Justificante
     * @param locale
     * @param missatge
     * @return
     * @throws Exception
     */
    protected String tradueixMissatge(Locale locale, String missatge) throws Exception {

        try {
            ResourceBundle justificantemissatges = ResourceBundle.getBundle("justificantemissatges", locale);
            return new String(justificantemissatges.getString(missatge).getBytes("ISO-8859-1"), "UTF-8");
        }catch (Exception e) {
            try{
                ResourceBundle logicmissatges = ResourceBundle.getBundle("logicmissatges", locale, UTF8CONTROL);
                return logicmissatges.getString(missatge);
            }catch (Exception e2){
                return "{"+locale+"_"+missatge+"}";
            }
        }

    }

    public static final UTF8Control UTF8CONTROL=new UTF8Control();

    /**
     *  Pone los mensajes de logicmissatges en UTF-8
     */
    public static class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            // The below is a copy of the default implementation.
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    // Only this line is changed to make it to read properties files as
                    // UTF-8.
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }

}