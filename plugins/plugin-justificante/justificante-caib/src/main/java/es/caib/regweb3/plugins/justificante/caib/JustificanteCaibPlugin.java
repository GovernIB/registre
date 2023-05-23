package es.caib.regweb3.plugins.justificante.caib;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.plugins.justificante.utils.I18NJustificanteUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;


/**
 * Created by Fundació BIT.
 * @author jpernia
 * Date: 01/03/2017
 *
 */
public class JustificanteCaibPlugin extends AbstractPluginProperties implements IJustificantePlugin {

    public static final String PROPERTY_CAIB_BASE = IJustificantePlugin.JUSTIFICANTE_BASE_PROPERTY + "caib.";

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private String textoLegalValidez;
    private String textoLegalDocElectronica;
    private String avisoRegistroPresencial;
    private String estampat = null;
    private String rutaImatge = null;
    private Float fitWidth = null;
    private Float fitHeight = null;
    private Boolean sir = false;
    private Font lletraGovern8;
    private Font lletraGovern10bold;
    private Font lletraGovern9bold;
    private Font lletraGovern14bold;

    private static final BaseColor GOIB_VERDE = new BaseColor(51, 132, 79);
    private static final BaseColor GOIB_ROJO = new BaseColor(204, 7, 60);


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

                Paragraph parrafo = new Paragraph("");
                parrafo.setAlignment(Element.ALIGN_LEFT);
                document.add(parrafo);
                document.add(logoRW);
                // Logo Entitat
                Image logoGovern = Image.getInstance(rutaImatge);
                if(logoGovern != null) {
                    logoGovern.setAlignment(Element.ALIGN_RIGHT);
                    logoGovern.scaleToFit(fitWidth, fitHeight);
                    logoGovern.setAbsolutePosition(160f, 780f);
                    parrafo = new Paragraph("");
                    parrafo.setAlignment(Element.ALIGN_LEFT);
                    document.add(parrafo);
                    document.add(logoGovern);
                }
                // Sir
                if(sir) {
                    InputStream fileSIR = classLoader.getResourceAsStream("img/SIR_petit.jpg");
                    Image logoSIR = Image.getInstance(cb, ImageIO.read(fileSIR), 1);
                    logoSIR.setAlignment(Element.ALIGN_RIGHT);
                    logoSIR.scaleToFit(100, 100);
                    logoSIR.setAbsolutePosition(460f, 790f);
                    parrafo = new Paragraph("");
                    parrafo.setAlignment(Element.ALIGN_RIGHT);
                    document.add(parrafo);
                    document.add(logoSIR);
                }
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
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
     * Inicialitza propietats generals
     * @param locale
     * @throws Exception
     */
    private void inicializarPropiedades(IRegistro registro, Long tipoRegistro, Locale locale, String url, String specialValue, String csv, Boolean sir) throws I18NException {

        // Propiedades configuradas en el plugin
        rutaImatge = this.getProperty(PROPERTY_CAIB_BASE + "logoPath");
        fitWidth = Float.parseFloat(this.getProperty(PROPERTY_CAIB_BASE + "fitWidth", "50"));
        fitHeight= Float.parseFloat(this.getProperty(PROPERTY_CAIB_BASE + "fitHeight", "50"));

        String estampacion = null;
        try {
            estampacion = this.getPropertyRequired(PROPERTY_CAIB_BASE + "estampacion");
        } catch (Exception e) {
            throw new I18NException("La propiedad 'estampacion' no esta definida y es obligatoria");
        }
        estampat = MessageFormat.format(estampacion, url, specialValue, csv);

        // SIR?
        this.sir = sir;

        // Inicialitza estils de lletra
        FontFactory.register("img/LegacySanITC-Book.otf", "Legacy");
        lletraGovern8 = FontFactory.getFont("Legacy", 8);
        lletraGovern8.setColor(BaseColor.BLACK);
        lletraGovern8.setStyle(Font.NORMAL);
        lletraGovern9bold = FontFactory.getFont("Legacy", 9);
        lletraGovern9bold.setColor(BaseColor.BLACK);
        lletraGovern9bold.setStyle(Font.BOLD);
        lletraGovern10bold = FontFactory.getFont("Legacy", 10);
        lletraGovern10bold.setColor(BaseColor.BLACK);
        lletraGovern10bold.setStyle(Font.BOLD);
        lletraGovern14bold = FontFactory.getFont("Legacy", 14);
        lletraGovern14bold.setColor(BaseColor.WHITE);
        lletraGovern14bold.setStyle(Font.BOLD);

        // Obtenemos el texto legal correspondiente para cada tipo de Registro
        if(registro.getRegistroDetalle().getRecibidoSir()){ // Recibido por SIR
            textoLegalDocElectronica = tradueixMissatge(locale,"justificante.texto.documentosElectronicos.sir");
        }else if(registro.getRegistroDetalle().getPresencial()){ // Presencial
            textoLegalDocElectronica = tradueixMissatge(locale,"justificante.texto.documentosElectronicos.presencial");
        }else{ // Telemático
            textoLegalDocElectronica = tradueixMissatge(locale,"justificante.texto.documentosElectronicos.telematico");
        }

        // Obtenemos el texto legal correspondiente para cada tipo de Registro
        if(RegwebConstantes.REGISTRO_SALIDA.equals(tipoRegistro)){ // Registro Salida
            textoLegalValidez = tradueixMissatge(locale,"justificante.texto.validez.salida");
        }
        if(registro.getRegistroDetalle().getRecibidoSir()){ // Recibido por SIR
            textoLegalValidez = tradueixMissatge(locale,"justificante.texto.validez.sir");
        }else if(registro.getRegistroDetalle().getPresencial()){ // Presencial
            textoLegalValidez = tradueixMissatge(locale,"justificante.texto.validez.presencial");
        }else{ // Telemático
            textoLegalValidez = tradueixMissatge(locale,"justificante.texto.validez.telematico");
        }

        // Aviso registro presencial
        if(RegwebConstantes.REGISTRO_ENTRADA.equals(tipoRegistro) && registro.getRegistroDetalle().getPresencial() && registro.getRegistroDetalle().isInteresadoJuridico()){
            avisoRegistroPresencial = tradueixMissatge(locale,"justificante.texto.registro.presencial");
        }

    }

    @Override
    public byte[] generarJustificanteEntrada(RegistroEntrada registroEntrada, String url, String specialValue, String csv, String idioma, Boolean sir) throws I18NException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

        // Define idioma para el justificante
        Locale locale = new Locale(idioma);

        try{
            //Inicializamos las propiedades comunes
            inicializarPropiedades(registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, locale, url, specialValue,csv, sir);

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

            // Bloque principal información del registro (oficina, número, fecha..)
            informacioRegistre(registroEntrada, getDestino(registroEntrada), locale, document, RegwebConstantes.REGISTRO_ENTRADA);

            // Bloque de Interesados
            llistarInteressats(registroEntrada.getRegistroDetalle().getInteresados(), locale, document, false);

            // Bloque de Información adicional del registro (Destino, Idioma, Extracto...)
            adicionalRegistre(registroEntrada, locale, document, idioma);

            // Bloque de anexos del Registro
            llistarAnnexes(registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, locale, document);

            // Leyes
            parrafoLeyes(document, locale);

            // Aviso registro presencial, cuando debería ser telemático
            if(registroEntrada.getRegistroDetalle().getPresencial() && registroEntrada.getRegistroDetalle().isInteresadoJuridico()){
                avisoRegistroPresencial(document, locale);
            }

            document.close();

            return baos.toByteArray();

        }catch (DocumentException | FileNotFoundException e){
            e.printStackTrace();
            throw new I18NException("Error generando el pdf del justificante de entrada");
        }
    }


    @Override
    public byte[] generarJustificanteSalida(RegistroSalida registroSalida, String url, String specialValue, String csv, String idioma, Boolean sir) throws I18NException{

        // Define idioma para el justificante
        Locale locale = new Locale(idioma);

        try {

            // Inicializamos las propiedades comunes
            inicializarPropiedades(registroSalida, RegwebConstantes.REGISTRO_SALIDA, locale, url, specialValue,csv, sir);

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

            // Bloque principal información del registro (oficina, número, fecha..)
            informacioRegistre(registroSalida, getDestinatario(registroSalida), locale, document, RegwebConstantes.REGISTRO_SALIDA);

            // Bloque de Interesados
            llistarInteressats(registroSalida.getRegistroDetalle().getInteresados(), locale, document, true);

            // Bloque de Información adicional del registro (Destino, Idioma, Extracto...)
            adicionalRegistre(registroSalida, locale, document, idioma);

            // Anexos del Registro
            llistarAnnexes(registroSalida, RegwebConstantes.REGISTRO_SALIDA, locale, document);

            // Leyes
            parrafoLeyes(document, locale);

            document.close();

            return baos.toByteArray();

        }catch (DocumentException | FileNotFoundException e){
            e.printStackTrace();
            throw new I18NException("Error generando el pdf del justificante de salida");
        }

    }


    // Inicializa el Documento tanto para el registro de entrada como el de salida
    protected Document inicialitzaDocument(Document document) throws I18NException {

        // Build PDF document.
        document.open();

        //CONFIGURACIONES GENERALES FORMATO PDF
        document.setPageSize(PageSize.A4);
        document.addAuthor("REGWEB3 - Govern de les Illes Balears");
        document.addCreationDate();
        document.addCreator("iText library");

        return document;
    }

    /**
     * Lista los anexos tanto para el registro de entrada como el de salida
     * @param registro
     * @param tipoRegistro
     * @param locale
     * @param document
     * @throws Exception
     */
    protected void llistarAnnexes(IRegistro registro, Long tipoRegistro, Locale locale, Document document) throws I18NException, DocumentException {

        if(!registro.getRegistroDetalle().getAnexosFull().isEmpty()) {
            // Creamos estilo para el título Adjuntos
            Paragraph titulo = new Paragraph(tradueixMissatge(locale,"justificante.adjuntos"), lletraGovern10bold);
            document.add(titulo);
            document.add(Chunk.NEWLINE);
            document.add(getLineaVerde());

            // Añadimos los campos de la Información
            PdfPTable taulaAnnexe = new PdfPTable(new float[]{15, 15, 10, 10, 10, 25, 12});
            taulaAnnexe.setWidthPercentage(100);
            taulaAnnexe.setSpacingBefore(5);
            PdfPCell cellInfoAnnexe = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.nombreAdjunto"), lletraGovern9bold));
            cellInfoAnnexe.setBackgroundColor(BaseColor.WHITE);
            cellInfoAnnexe.setBorderColor(BaseColor.BLACK);
            cellInfoAnnexe.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            taulaAnnexe.addCell(cellInfoAnnexe);
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.archivo"), lletraGovern9bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.tamanyo"), lletraGovern9bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.validez"), lletraGovern9bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.tipoAdjunto"), lletraGovern9bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.hash"), lletraGovern9bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.observacionesAdjunto"), lletraGovern9bold)));

            PdfPCell cellInfoAnnexe2 = new PdfPCell(new Paragraph("", lletraGovern8));
            cellInfoAnnexe2.setBackgroundColor(BaseColor.WHITE);
            cellInfoAnnexe2.setBorderColor(BaseColor.BLACK);
            cellInfoAnnexe2.setBorderWidth(1f);
            cellInfoAnnexe2.setHorizontalAlignment(Element.ALIGN_LEFT);

            for(AnexoFull anexo : registro.getRegistroDetalle().getAnexosFull()) {
                // Variables per calcular i afegeix el tamany del document (Del DocumentCustody i/o del SignatureCustody)
                String tamanyFitxer = "1 KB";
                String nomFitxer = null;
                String hash = null;

                // Anexos confidenciales
                if(anexo.getAnexo().getConfidencial()){

                    cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), lletraGovern8));
                    taulaAnnexe.addCell(cellInfoAnnexe2);
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(anexo.getAnexo().getNombreFichero(), lletraGovern8)));
                    if(anexo.getAnexo().getTamanoFichero() > 1024){
                        tamanyFitxer = anexo.getAnexo().getTamanoFichero()/1024 + " KB";
                    }
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, lletraGovern8)));
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale, "tipoValidezDocumento." + anexo.getAnexo().getValidezDocumento()), lletraGovern8)));
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale, "tipoDocumento.0" + anexo.getAnexo().getTipoDocumento()), lletraGovern8)));
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(new String(anexo.getAnexo().getHash()), lletraGovern8)));
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(anexo.getAnexo().getObservaciones(), lletraGovern8)));

                }else if(!RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO.equals(anexo.getAnexo().getTipoDocumento())){

                    if(anexo.getAnexo().getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED){ // És un document amb firma detached
                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), lletraGovern8));
                        cellInfoAnnexe2.setRowspan(2);
                        taulaAnnexe.addCell(cellInfoAnnexe2);

                        // Document
                        hash = new String(anexo.getAnexo().getHash());
                        nomFitxer = anexo.getDocumentoCustody().getName();
                        if(anexo.getDocumentoCustody().getData().length > 1024){
                            tamanyFitxer = anexo.getDocumentoCustody().getData().length / 1024 + " KB";
                        }
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(nomFitxer, lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, lletraGovern8)));
                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(tradueixMissatge(locale,"tipoValidezDocumento." + anexo.getAnexo().getValidezDocumento()), lletraGovern8));
                        cellInfoAnnexe2.setRowspan(2);
                        taulaAnnexe.addCell(cellInfoAnnexe2);
                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(tradueixMissatge(locale,"tipoDocumento.0" + anexo.getAnexo().getTipoDocumento()), lletraGovern8));
                        cellInfoAnnexe2.setRowspan(2);
                        taulaAnnexe.addCell(cellInfoAnnexe2);
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(hash, lletraGovern8)));
                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getObservaciones(), lletraGovern8));
                        cellInfoAnnexe2.setRowspan(2);
                        taulaAnnexe.addCell(cellInfoAnnexe2);

                        // Firma
                        hash = tradueixMissatge(locale,"justificante.firma") + ": " + new String(RegwebUtils.obtenerHash(anexo.getSignatureCustody().getData()));
                        nomFitxer = tradueixMissatge(locale,"justificante.firma") + ": " + anexo.getSignatureCustody().getName();
                        if (anexo.getSignatureCustody().getData().length > 1024) {
                            tamanyFitxer = tradueixMissatge(locale,"justificante.firma") + ": " + anexo.getSignatureCustody().getData().length / 1024 + " KB";
                        }
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(nomFitxer, lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(hash, lletraGovern8)));

                    }else if(anexo.getAnexo().getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) { // És un document sense firma

                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), lletraGovern8));
                        taulaAnnexe.addCell(cellInfoAnnexe2);
                        hash = new String(anexo.getAnexo().getHash());
                        nomFitxer = anexo.getDocumentoCustody().getName();
                        if(anexo.getDocumentoCustody().getData().length > 1024){
                            tamanyFitxer = anexo.getDocumentoCustody().getData().length / 1024 + " KB";
                        }
                    }else if (anexo.getAnexo().getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) { // És un document amb firma attached

                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), lletraGovern8));
                        taulaAnnexe.addCell(cellInfoAnnexe2);
                        hash = new String(anexo.getAnexo().getHash());
                        nomFitxer = anexo.getSignatureCustody().getName();
                        if (anexo.getSignatureCustody().getData().length > 1024) {
                            tamanyFitxer = anexo.getSignatureCustody().getData().length / 1024 + " KB";
                        }
                    }

                    if(anexo.getAnexo().getModoFirma()!=RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(nomFitxer, lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale, "tipoValidezDocumento." + anexo.getAnexo().getValidezDocumento()), lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale, "tipoDocumento.0" + anexo.getAnexo().getTipoDocumento()), lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(hash, lletraGovern8)));
                        taulaAnnexe.addCell(new PdfPCell(new Paragraph(anexo.getAnexo().getObservaciones(), lletraGovern8)));
                    }
                }
            }
            document.add(taulaAnnexe);

            // Texto legal documentos electrónicos
            if(RegwebConstantes.REGISTRO_ENTRADA.equals(tipoRegistro)){

                Paragraph textoLegal = new Paragraph(tradueixMissatge(locale,"justificante.la") + " " + registro.getOficina().getDenominacion() + " " + textoLegalDocElectronica, lletraGovern8);
                document.add(textoLegal);
                document.add(Chunk.NEWLINE);

            }
        }
    }

    /**
     * Añade un párrafo de validez documental
     * @param document
     * @throws Exception
     */
    protected void parrafoLeyes(Document document, Locale locale) throws DocumentException, I18NException {

        Paragraph titulo = new Paragraph(tradueixMissatge(locale,"justificante.titulo.validez"), lletraGovern10bold);
        document.add(titulo);
        document.add(Chunk.NEWLINE);
        document.add(getLineaVerde());

        Paragraph texto = new Paragraph(textoLegalValidez, lletraGovern8);
        document.add(texto);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Añade un párrafo informativo de un registro presencial
     * @param document
     * @param locale
     * @throws DocumentException
     * @throws I18NException
     */
    private void avisoRegistroPresencial(Document document, Locale locale) throws DocumentException, I18NException {

        Paragraph titulo = new Paragraph(tradueixMissatge(locale,"justificante.titulo.registro.presencial"), lletraGovern10bold);
        document.add(titulo);
        document.add(Chunk.NEWLINE);
        document.add(getLineaVerde());

        Paragraph texto = new Paragraph(avisoRegistroPresencial, lletraGovern8);
        document.add(texto);
    }

    /**
     * Lista los Interesados y Representantes tanto para el registro de entrada como el de salida
     * @param interesados
     * @param locale
     * @param document
     * @param isSalida
     * @throws Exception
     */
    protected void llistarInteressats(List<Interesado> interesados, Locale locale, Document document, Boolean isSalida) throws I18NException, DocumentException {

        // Creamos estilo para el título Interesado
        String etiqueta;
        if(isSalida){ // Si és una sortida
            etiqueta = tradueixMissatge(locale,"justificante.destinatario");
        }else{  // Si és una entrada
            etiqueta = tradueixMissatge(locale,"justificante.interesado");
        }

        Paragraph paragraph = new Paragraph(etiqueta, lletraGovern10bold);
        document.add(paragraph);
        document.add(Chunk.NEWLINE);
        document.add(getLineaVerde());

        PdfPTable taulaInteresado = new PdfPTable(new float[] { 15, 30, 15, 30 });
        taulaInteresado.setSpacingBefore(5);
        // Añadimos una entrada para cada interesado
        int i;
        for(Interesado interesado : interesados) {
            // Añadimos título
            if(!interesado.getIsRepresentante()) {
                i=0;
                //document.add(tituloTabla);
                // Añadimos campos del interesado
                taulaInteresado.setWidthPercentage(100);
                taulaInteresado.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                taulaInteresado.getDefaultCell().setBorder(0);
                taulaInteresado.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                // Document
                if(StringUtils.isNotEmpty(interesado.getDocumento())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.documento"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getDocumento(), lletraGovern8));
                }
                // Tipus Document
                if(interesado.getTipoDocumentoIdentificacion()!= null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.tipoDocumento"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(I18NJustificanteUtils.tradueix(locale, "tipoDocumentoIdentificacion." + interesado.getTipoDocumentoIdentificacion(), null), lletraGovern8));
                }
                // Nom - Organ
                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.nombre"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getNombreCompleto(), lletraGovern8));
                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.razonSocial"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getRazonSocial(), lletraGovern8));
                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.organismo"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getRazonSocial(), lletraGovern8));
                }
                // Pais
                if(interesado.getPais() != null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.pais"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getPais().getDescripcionPais(), lletraGovern8));
                }
                // Adreça
                if(StringUtils.isNotEmpty(interesado.getDireccion())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.direccion"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getDireccion(), lletraGovern8));
                }
                // Municipi
                if(interesado.getLocalidad() != null && interesado.getCp() != null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.municipio"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getCp() + " - " + interesado.getLocalidad().getNombre(), lletraGovern8));
                }
                // Provincia
                if(interesado.getProvincia() != null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.provincia"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getProvincia().getDescripcionProvincia(), lletraGovern8));
                }
                // Telefon
                if(StringUtils.isNotEmpty(interesado.getTelefono())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.telefono"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getTelefono(), lletraGovern8));
                }
                // Mail
                if(StringUtils.isNotEmpty(interesado.getEmail())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.correo"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getEmail(), lletraGovern8));
                }
                // Canal Notificacio
                if((interesado.getCanal() != null) && (interesado.getCanal() != -1)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.canalNot"), lletraGovern9bold));
                    String canalNotif = tradueixMissatge(locale,"canalNotificacion." + interesado.getCanal());
                    taulaInteresado.addCell(new Paragraph(canalNotif, lletraGovern8));
                }
                // Direccio Electronica Habilitada
                if(StringUtils.isNotEmpty(interesado.getDireccionElectronica())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.deh"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getDireccionElectronica(), lletraGovern8));
                }
                // Observacions
                if(StringUtils.isNotEmpty(interesado.getObservaciones())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.observaciones"), lletraGovern9bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getObservaciones(), lletraGovern8));
                }
                // Completa la cel·la buida
                if(!esPar(i)) {
                    taulaInteresado.addCell("");
                    taulaInteresado.addCell("");
                }

                document.add(taulaInteresado);
                // Vaciamos el contenido del interesado para rellenarlo con uno nuevo
                taulaInteresado.deleteBodyRows();
                document.add(new Paragraph(" "));

                // Si el interesado tiene representante
                if(interesado.getRepresentante() != null) {

                    // Creamos estilo para el título Representante
                    PdfPTable titolRepresentant = new PdfPTable(1);
                    titolRepresentant.setWidthPercentage(90);

                    // Recorremos todos los interesados para buscar el reresentante
                    for(Interesado representante : interesados) {
                        // Encuentra su representante y lo muestra
                        if (interesado.getRepresentante().getId().equals(representante.getId())) {
                            i = 0;
                            PdfPCell cellRepresentant = new PdfPCell(new Paragraph(tradueixMissatge(locale, "justificante.representante") + " de " + interesado.getNombreCompleto(), lletraGovern9bold));
                            cellRepresentant.setBackgroundColor(BaseColor.WHITE);
                            cellRepresentant.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cellRepresentant.setBorder(Rectangle.BOTTOM);
                            cellRepresentant.setBorderColor(GOIB_VERDE);
                            cellRepresentant.setBorderWidth(1f);
                            titolRepresentant.addCell(cellRepresentant);
                            document.add(titolRepresentant);
                            PdfPTable taulaRepresentant = new PdfPTable(new float[] { 13, 28, 13, 28 });
                            taulaRepresentant.setSpacingBefore(3);
                            // Añadimos campos del Representante
                            taulaRepresentant.setWidthPercentage(90);
                            taulaRepresentant.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                            taulaRepresentant.getDefaultCell().setBorder(0);
                            taulaRepresentant.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                            // Document
                            if(StringUtils.isNotEmpty(representante.getDocumento())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.documento"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getDocumento(), lletraGovern8));
                            }
                            // Tipus Document
                            if(representante.getTipoDocumentoIdentificacion()!=null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.tipoDocumento"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(I18NJustificanteUtils.tradueix(locale, "tipoDocumentoIdentificacion." + representante.getTipoDocumentoIdentificacion(), null), lletraGovern8));
                            }
                            // Nom - Organ
                            if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.nombre"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getNombreCompleto(), lletraGovern8));
                            } else if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.razonSocial"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getRazonSocial(), lletraGovern8));
                            } else if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.organismo"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getRazonSocial(), lletraGovern8));
                            }
                            // Pais
                            if(representante.getPais() != null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.pais"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getPais().getDescripcionPais(), lletraGovern8));
                            }
                            // Adreça
                            if(StringUtils.isNotEmpty(representante.getDireccion())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.direccion"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getDireccion(), lletraGovern8));
                            }
                            // Municipi
                            if(representante.getLocalidad() != null && representante.getCp() != null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.municipio"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getCp() + " - " + representante.getLocalidad().getNombre(), lletraGovern8));
                            }
                            // Provincia
                            if(representante.getProvincia() != null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.provincia"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getProvincia().getDescripcionProvincia(), lletraGovern8));
                            }
                            // Telefon
                            if(StringUtils.isNotEmpty(representante.getTelefono())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.telefono"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getTelefono(), lletraGovern8));
                            }
                            // Mail
                            if(StringUtils.isNotEmpty(representante.getEmail())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.correo"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getEmail(), lletraGovern8));
                            }
                            // Canal Notificacio
                            if((representante.getCanal() != null) && (representante.getCanal() != -1)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.canalNot"), lletraGovern9bold));
                                String canalNotifRep = tradueixMissatge(locale,"canalNotificacion." + representante.getCanal());
                                taulaRepresentant.addCell(new Paragraph(canalNotifRep, lletraGovern8));
                            }
                            // Direccio Electronica Habilitada
                            if(StringUtils.isNotEmpty(representante.getDireccionElectronica())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.deh"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getDireccionElectronica(), lletraGovern8));
                            }
                            // Observacions
                            if(StringUtils.isNotEmpty(representante.getObservaciones())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.observaciones"), lletraGovern9bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getObservaciones(), lletraGovern8));
                            }
                            if(!esPar(i)) {
                                // Completa la cel·la buida
                                taulaRepresentant.addCell("");
                                taulaRepresentant.addCell("");
                            }
                            document.add(taulaRepresentant);
                            // Vaciamos el contenido del representante para rellenarlo con uno nuevo
                            taulaRepresentant.deleteBodyRows();
                            document.add(Chunk.NEWLINE);

                            titolRepresentant.deleteBodyRows();
                        }
                    }
                }
            }
        }
        document.add(Chunk.NEWLINE);
    }

    /**
     * Añade el título y la información de registro
     * @param registro
     * @param locale
     * @param document
     * @param tipoRegistro
     * @throws Exception
     */
    protected void informacioRegistre(IRegistro registro, String destino, Locale locale, Document document, Long tipoRegistro) throws I18NException, DocumentException {

        document.addTitle(tradueixMissatge(locale,"justificante.anexo.titulo"));
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);

        PdfPCell cellTitulo;

        if(registro.getRegistroDetalle().getRecibidoSir()){ // Recibido via SIR
            cellTitulo = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.titulo.sir"), lletraGovern14bold));
        }else if (tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)){
            cellTitulo = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.titulo.salida"), lletraGovern14bold));
        }else{
            cellTitulo = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.titulo.general"), lletraGovern14bold));
        }

        cellTitulo.setBackgroundColor(GOIB_VERDE);
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBorder(Rectangle.NO_BORDER);
        cellTitulo.setPaddingTop(8);
        cellTitulo.setPaddingBottom(10);
        titulo.addCell(cellTitulo);
        document.add(titulo);
        document.add(Chunk.NEWLINE);

        // Registro
        PdfPTable taulaRegistre = new PdfPTable(new float[] { 2, 4 });
        taulaRegistre.setWidthPercentage(100);
        taulaRegistre.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        taulaRegistre.getDefaultCell().setBorder(0);
        taulaRegistre.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.oficina"), lletraGovern9bold));
        taulaRegistre.addCell(new Paragraph(registro.getOficina().getDenominacion() + " - " + registro.getOficina().getCodigo(), lletraGovern8));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.fechaPresentacion"), lletraGovern9bold));
        taulaRegistre.addCell(new Paragraph(formatDate.format(registro.getFecha()), lletraGovern8));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.numRegistro"), lletraGovern9bold));
        taulaRegistre.addCell(new Paragraph(registro.getNumeroRegistroFormateado(), lletraGovern8));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale, "justificante.unidad"), lletraGovern9bold));
        taulaRegistre.addCell(new Paragraph(destino, lletraGovern8));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.tipusRegistre"), lletraGovern9bold));
        if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
            taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.entrada"), lletraGovern8));
        }else{
            taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.salida"), lletraGovern8));
        }

        document.add(taulaRegistre);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Bloque información adicional del registro
     * @param registro
     * @param locale
     * @param document
     * @param idioma
     * @throws Exception
     */
    protected void adicionalRegistre(IRegistro registro, Locale locale, Document document, String idioma) throws I18NException, DocumentException {

        Paragraph titulo = new Paragraph(tradueixMissatge(locale,"justificante.informacion"), lletraGovern10bold);
        document.add(titulo);
        document.add(Chunk.NEWLINE);
        document.add(getLineaVerde());

        // Añadimos los campos de la Información
        PdfPTable taulaInformacio = new PdfPTable(new float[] { 25, 25, 25, 25 });
        taulaInformacio.setSpacingBefore(5);
        taulaInformacio.setWidthPercentage(100);
        taulaInformacio.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        taulaInformacio.getDefaultCell().setBorder(0);
        taulaInformacio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        int i = 0;

        // Extracte
        if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getExtracto())) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.resumen"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(registro.getRegistroDetalle().getExtracto(), lletraGovern8));
        }
        // Tipus assumpte
        if(registro.getRegistroDetalle().getTipoAsunto() != null) {
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registro.getRegistroDetalle().getTipoAsunto().getTraduccion(idioma);
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.tipoAsunto"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(traduccionTipoAsunto.getNombre(), lletraGovern8));
        }
        // Codi assumpte
        if(registro.getRegistroDetalle().getCodigoAsunto()!=null) {
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registro.getRegistroDetalle().getCodigoAsunto().getTraduccion(idioma);
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.codigoAsunto"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(traduccionCodigoAsunto.getNombre(), lletraGovern8));
        }
        // Idioma
        if(registro.getRegistroDetalle().getIdioma()!=null) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.idioma"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(I18NJustificanteUtils.tradueix(locale, "idioma." + registro.getRegistroDetalle().getIdioma(), null), lletraGovern8));
        }
        // Tipo doc. física
        if(registro.getRegistroDetalle().getTipoDocumentacionFisica()!=null) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"justificante.docFisica"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale,"tipoDocumentacionFisica." + registro.getRegistroDetalle().getTipoDocumentacionFisica()), lletraGovern8));
        }
        // Referència externa
        if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getReferenciaExterna())) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.refExterna"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(registro.getRegistroDetalle().getReferenciaExterna(), lletraGovern8));
        }
        // Expedient
        if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getExpediente())) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.expediente"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(registro.getRegistroDetalle().getExpediente(), lletraGovern8));
        }
        // Transport
        if(registro.getRegistroDetalle().getTransporte() != null) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.transport"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(I18NJustificanteUtils.tradueix(locale, "transporte.0" + registro.getRegistroDetalle().getTransporte(), null), lletraGovern8));
        }
        // Número transport
        if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getNumeroTransporte())) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.numTransport"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(registro.getRegistroDetalle().getNumeroTransporte(), lletraGovern8));
        }

        // Oficina origen
        if(StringUtils.isNotEmpty(getOficinaOrigenDenominacion(registro))) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.oficinaOrigen"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(getOficinaOrigenDenominacion(registro), lletraGovern8));

            // Número Registre origen
            if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getNumeroRegistroOrigen())) {
                i += 1;
                taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.numRegOrigen"), lletraGovern9bold));
                taulaInformacio.addCell(new Paragraph(registro.getRegistroDetalle().getNumeroRegistroOrigen(), lletraGovern8));
            }
            // Data Registre origen
            if(registro.getRegistroDetalle().getFechaOrigen() != null) {
                i += 1;
                taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.dataOrigen"), lletraGovern9bold));
                taulaInformacio.addCell(new Paragraph(formatDate.format(registro.getRegistroDetalle().getFechaOrigen()), lletraGovern8));
            }
        }

        // Observacions
        if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getObservaciones())) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.observacions"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(registro.getRegistroDetalle().getObservaciones(), lletraGovern8));
        }
        // Código SIA
        if(registro.getRegistroDetalle().getCodigoSia() != null) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.codigoSIA"), lletraGovern9bold));
            taulaInformacio.addCell(new Paragraph(String.valueOf(registro.getRegistroDetalle().getCodigoSia()), lletraGovern8));
        }
        if(!esPar(i)) {
            // Completa la cel·la buida
            taulaInformacio.addCell("");
            taulaInformacio.addCell("");
        }

        document.add(taulaInformacio);
        document.add(Chunk.NEWLINE);

    }


    /**
     *  Obtiene los mensajes que aparecen traducidos en el pdf del Justificante
     * @param locale
     * @param missatge
     * @return
     * @throws Exception
     */
    protected String tradueixMissatge(Locale locale, String missatge) throws I18NException {

        try {
            ResourceBundle justificantemissatges = ResourceBundle.getBundle("justificante_caib_missatges", locale, UTF8CONTROL);
            return justificantemissatges.getString(missatge);
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

    public static boolean esPar(int numero) {
        //Si el resto de numero entre 2 es 0, el numero es par
        return numero % 2 == 0;
    }

    /**
     * Retorna la Oficina Origen, siempre que no sea la misma donde se registró
     * @param registro
     * @return
     */
    private String getOficinaOrigenDenominacion(IRegistro registro){

        if(registro.getRegistroDetalle().getOficinaOrigen() != null){

            if(!registro.getRegistroDetalle().getOficinaOrigen().getCodigo().equals(registro.getOficina().getCodigo())) { // Solo si no es la misma donde registró
                return registro.getRegistroDetalle().getOficinaOrigen().getDenominacion() + " - " + registro.getRegistroDetalle().getOficinaOrigen().getCodigo();

            }else{
                return null;
            }
        }else if(StringUtils.isNotEmpty(registro.getRegistroDetalle().getOficinaOrigenExternoCodigo())){
            return  registro.getRegistroDetalle().getOficinaOrigenExternoDenominacion() + " - " + registro.getRegistroDetalle().getOficinaOrigenExternoCodigo();
        }

        return null;
    }

    private String getDestino(RegistroEntrada registroEntrada){
        if(registroEntrada.getDestino() != null) {
            return registroEntrada.getDestino().getDenominacion() + " - " + registroEntrada.getDestino().getCodigo();
        }else{
            return registroEntrada.getDestinoExternoDenominacion() + " - " + registroEntrada.getDestinoExternoCodigo();
        }
    }

    private String getDestinatario(RegistroSalida registroSalida) {
        List<Interesado> destinatarios = registroSalida.getRegistroDetalle().getInteresados();
        for(Interesado interesado : destinatarios) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

                return interesado.getNombreCompleto();
            }
        }
        return null;
    }

    /**
     * Dibuja una línea roja
     * @return
     */
    private Element getLineaVerde() {

        LineSeparator linea = new LineSeparator();
        linea.setLineColor(GOIB_VERDE);
        linea.setLineWidth(1f);

        return linea;
    }
}