package es.caib.regweb3.plugins.justificante.apb;

import com.alfresco.client.AlfrescoClient;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.plugins.justificante.apb.api.ArxiuApi;
import es.caib.regweb3.plugins.justificante.apb.cmis.OpenCmisAlfrescoHelper;
import es.caib.regweb3.plugins.justificante.apb.utils.Utils;
import es.caib.regweb3.plugins.justificante.utils.I18NJustificanteUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * Created by Fundació BIT.
 * @author jpernia
 * Date: 01/03/2017
 *
 */
public class JustificanteApbPlugin extends AbstractPluginProperties implements IJustificantePlugin {

    public static final String PROPERTY_APB_BASE = IJustificantePlugin.JUSTIFICANTE_BASE_PROPERTY + "apb.";

    protected final Logger log = Logger.getLogger(getClass());
    ArxiuApi api;
    private String declaracion = null;
    private String ley = null;
    private String rutaImatge = null;
    private String rutaImatgeAdm = null;
    private Boolean sir = false;
    private Font font8;
    private Font font8Bold;
    private Font font16Bold;
    private String baseUrl;
	private String user;
	private String password;
	private String ldapUser;
	private String consultaCSV;
	private String cmisUrl;
	private OpenCmisAlfrescoHelper openCmisAlfrescoHelper;
	private AlfrescoClient alfrescoClient;
    /**
     *  Crea l'event d'Estampació dels logos per a que es faci a totes les pàgines que va creant el pdf
     */
    class EstampaLogos extends PdfPageEventHelper {

        Font font7 = FontFactory.getFont(FontFactory.HELVETICA, 7);

        public void onStartPage(PdfWriter writer, Document document) {

            try{
                // LOGOS
                PdfPTable logos = new PdfPTable(2);
                logos.setWidthPercentage(100);
                // Regweb3
                ClassLoader classLoader = getClass().getClassLoader();
                //InputStream fileRW = classLoader.getResourceAsStream(rutaImatgeAdm);
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
                if(sir) {
                    InputStream fileSIR = classLoader.getResourceAsStream("img/SIR_petit.jpg");
                    Image logoSIR = Image.getInstance(cb, ImageIO.read(fileSIR), 1);
                    logoSIR.setAlignment(Element.ALIGN_RIGHT);
                    logoSIR.scaleToFit(100, 100);
//                    logoSIR.setAbsolutePosition(365f, 790f);
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

    }

    /**
     *
     */
    public JustificanteApbPlugin() {
        super();
    }


    /**
     * @param propertyKeyBase
     * @param properties
     */
    public JustificanteApbPlugin(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }

    /**
     * @param propertyKeyBase
     */
    public JustificanteApbPlugin(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    /**
     * Inicialitza propietats amb strings de les propietats de Plugin de Justificante
     * @param locale
     * @throws Exception
     */
    private void inicializarPropiedades(Locale locale, Boolean entidadSir) throws Exception{
    	rutaImatge = this.getProperty(PROPERTY_APB_BASE + "logoApbPath");
    	rutaImatgeAdm = this.getProperty(PROPERTY_APB_BASE + "logoAdmPath");
        declaracion = this.getProperty(PROPERTY_APB_BASE + "declaracion." + locale);
        ley = this.getProperty(PROPERTY_APB_BASE + "ley." + locale);
        sir = entidadSir;
        // Inicialitza estils de lletra
        font8Bold = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);
        font8 = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL);
        font16Bold = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
        //Propietats arxiu
        baseUrl = this.getProperty(PROPERTY_APB_BASE + "alfresco.baseUrl");
    	user = this.getProperty(PROPERTY_APB_BASE + "alfresco.user");
        password = this.getProperty(PROPERTY_APB_BASE + "alfresco.password");
        ldapUser = this.getProperty(PROPERTY_APB_BASE + "alfresco.ldapUser");
        
        consultaCSV = this.getProperty(PROPERTY_APB_BASE + "csv.url");
        cmisUrl = this.getProperty(PROPERTY_APB_BASE + "cmis.url");
        String entitat = this.getProperty(PROPERTY_APB_BASE + "cmis.entitat");
        openCmisAlfrescoHelper = new OpenCmisAlfrescoHelper(entitat);
        
        api = new ArxiuApi(baseUrl, user, password, ldapUser);
    }


    @Override
    public byte[] generarJustificanteEntrada(RegistroEntrada registroEntrada, String url, String specialValue, String csv, String idioma) throws Exception{
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Define idioma para el justificante
        Locale locale = new Locale(idioma);

        //Inicializamos las propiedades comunes
        inicializarPropiedades(locale, registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getSir());

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

        // Aplica preferencias
        Document document = new Document(PageSize.A4);
        FileOutputStream ficheroPdf = new FileOutputStream("fichero.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        PdfWriter.getInstance(document,ficheroPdf).setInitialLeading(20);

        // Crea el evento para generar la estampación de csv en cada página
        EstampaLogos event = new EstampaLogos();
        writer.setPageEvent(event);

        // Inicializa Documento
        document = inicialitzaDocument(document);
        RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();
        // Comienza a crear el Justificante
        Long codiSia = registroDetalle.getCodigoSia();
        
        // Oficina de registre 
        String denominacionOficina = registroEntrada.getOficina().getDenominacion();
        String codigoOficina = registroEntrada.getOficina().getCodigo();
        String numeroRegistroFormateado = registroEntrada.getNumeroRegistroFormateado();
        Date fechaRegistro = registroEntrada.getFecha();
        String dataRegistre = null;
        if (fechaRegistro != null)
        	dataRegistre = formatDate.format(fechaRegistro);
        
        Long tipoDocumentacionFisica = registroDetalle.getTipoDocumentacionFisica();
        String extracte = registroDetalle.getExtracto();
        String nomDesti;
        if(registroEntrada.getDestino()!=null) {
            nomDesti = registroEntrada.getDestino().getDenominacion() + " - " + registroEntrada.getDestino().getCodigo();
        }else{
            nomDesti = registroEntrada.getDestinoExternoDenominacion() + " - " + registroEntrada.getDestinoExternoCodigo();
        }

        String expedient = registroDetalle.getExpediente();

        // Altres camps addicionals
        // Tipus Assumpte
        String tipoAsunto = null;
        if(registroDetalle.getTipoAsunto()!=null) {
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
            tipoAsunto = traduccionTipoAsunto.getNombre();
        }
        // Codi assumpte
        String codigoAsunto = null;
        if(registroDetalle.getCodigoAsunto()!=null) {
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
            codigoAsunto = traduccionCodigoAsunto.getNombre();
        }
        // Idioma
        String nomIdioma = null;
        if(registroDetalle.getIdioma()!=null) {
            nomIdioma = I18NJustificanteUtils.tradueix(locale, "idioma." + registroDetalle.getIdioma(), null);
        }
        // Referència externa
        String refExterna = registroDetalle.getReferenciaExterna();
        // Transport
        String transport = null;
        if(registroDetalle.getTransporte()!=null) {
            transport = I18NJustificanteUtils.tradueix(locale, "transporte.0" + registroDetalle.getTransporte(), null);
        }
        // Número transport
        String numTransport = registroDetalle.getNumeroTransporte();
        // Oficina origen, Registre origen i Data origen
        String oficinaOrigen = null;
        String numRegOrigen = null;
        String dataOrigen = null;
        Oficina oficinaOrigenDto = registroDetalle.getOficinaOrigen();
        String oficinaOrigenExternoCodigo = registroDetalle.getOficinaOrigenExternoCodigo();
        String oficinaOrigenExternoDenominacion = registroDetalle.getOficinaOrigenExternoDenominacion();
        
        if(oficinaOrigenDto != null) {
        	String oficinaOrigenCodigo = oficinaOrigenDto.getCodigo();
            if(oficinaOrigenCodigo != null && !oficinaOrigenCodigo.equals(registroEntrada.getOficina().getCodigo())) {
                oficinaOrigen = oficinaOrigenDto.getDenominacion() + " - " + oficinaOrigenCodigo;
                numRegOrigen = registroDetalle.getNumeroRegistroOrigen();
                Date fechaOrigen = registroDetalle.getFechaOrigen();
                dataOrigen = formatDate.format(fechaOrigen);
            }
        }

        // Si es externo (SIR)
        if (oficinaOrigen == null && oficinaOrigenExternoCodigo != null) {
        	oficinaOrigen = oficinaOrigenExternoDenominacion + " - " + oficinaOrigenExternoCodigo;
        	numRegOrigen = registroDetalle.getNumeroRegistroOrigen();
        	Date fechaOrigen = registroDetalle.getFechaOrigen();
            dataOrigen = formatDate.format(fechaOrigen);
        }
        
        // Observacions
        String observacions = registroDetalle.getObservaciones();

        // Título e Información Registro
    	String codigoOrgan = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getCodigoDir3();
    	String nombreOrgan = registroEntrada.getOficina().getOrganismoResponsable().getEntidad().getNombre();
    	
        informacioRegistre(locale, document, denominacionOficina, codigoOficina, dataRegistre, numeroRegistroFormateado,
                tipoDocumentacionFisica, registroEntrada.getClass().getSimpleName(), codigoOrgan, nombreOrgan, oficinaOrigen, numRegOrigen, dataOrigen);

        // Interesados
        List<Interesado> interesados = registroDetalle.getInteresados();
        llistarInteressats(interesados, locale, document, false);

        // Información adicional del Registro
        adicionalRegistre(locale, document, extracte, nomDesti, expedient, tipoAsunto, codigoAsunto, nomIdioma, refExterna, transport,
                numTransport, oficinaOrigen, numRegOrigen, dataOrigen, observacions, null, codiSia);

        // Anexos
        List<AnexoFull> anexos = registroDetalle.getAnexosFull();
        llistarAnnexes(anexos, locale, document, denominacionOficina);

        document.close();

        return baos.toByteArray();
    }


    @Override
   public byte[] generarJustificanteSalida(RegistroSalida registroSalida, String url, String specialValue, String csv, String idioma) throws Exception{

        // Define idioma para el justificante
        Locale locale = new Locale(idioma);

        //Inicializamos las propiedades comunes
        inicializarPropiedades(locale, registroSalida.getOficina().getOrganismoResponsable().getEntidad().getSir());

        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

        // Aplica preferencias
        Document document = new Document(PageSize.A4);
        FileOutputStream ficheroPdf = new FileOutputStream("fichero.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
        PdfWriter.getInstance(document,ficheroPdf).setInitialLeading(20);

        // Crea el evento para generar la estampación de csv en cada página
        EstampaLogos event = new EstampaLogos();
        writer.setPageEvent(event);

        // Inicializa Documento
        document = inicialitzaDocument(document);
        RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();
        // Comienza a crear el Justificante
        Long codiSia = registroDetalle.getCodigoSia();
        String denominacionOficina = registroSalida.getOficina().getDenominacion();
        String codigoOficina = registroSalida.getOficina().getCodigo();
        String numeroRegistroFormateado = registroSalida.getNumeroRegistroFormateado();
        Long tipoDocumentacionFisica = registroDetalle.getTipoDocumentacionFisica();
        String extracte = registroDetalle.getExtracto();
        String nomOrigen = "";
        if(registroDetalle.getCodigoEntidadRegistralDestino()!=null){
            nomOrigen = registroDetalle.getDecodificacionEntidadRegistralDestino() + " - " + registroDetalle.getCodigoEntidadRegistralDestino();
        }else{
            List<Interesado> interesadosDestino = registroDetalle.getInteresados();
            for(Interesado interesado : interesadosDestino) {
                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                    if(nomOrigen.length()==0) {
                        nomOrigen = interesado.getNombreCompleto();
                    }else{
                        nomOrigen = nomOrigen + ", " + interesado.getNombreCompleto();
                    }
                }
            }
        }
        String expedient = registroDetalle.getExpediente();
        Date fechaRegistro = registroSalida.getFecha();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dataRegistre = formatDate.format(fechaRegistro);

        // Altres camps addicionals
        // Tipus assumpte
        String tipoAsunto = null;
        if(registroDetalle.getTipoAsunto()!=null) {
            TraduccionTipoAsunto traduccionTipoAsunto = (TraduccionTipoAsunto) registroDetalle.getTipoAsunto().getTraduccion(idioma);
            tipoAsunto = traduccionTipoAsunto.getNombre();
        }
        // Codi assumpte
        String codigoAsunto = null;
        if(registroDetalle.getCodigoAsunto()!=null) {
            TraduccionCodigoAsunto traduccionCodigoAsunto = (TraduccionCodigoAsunto) registroDetalle.getCodigoAsunto().getTraduccion(idioma);
            codigoAsunto = traduccionCodigoAsunto.getNombre();
        }
        // Idioma
        String nomIdioma = null;
        if(registroDetalle.getIdioma()!=null) {
            nomIdioma = I18NJustificanteUtils.tradueix(locale, "idioma." + registroDetalle.getIdioma(), null);
        }
        // Referencia externa
        String refExterna = registroDetalle.getReferenciaExterna();
        // Transport
        String transport = null;
        if(registroDetalle.getTransporte()!=null) {
            transport = I18NJustificanteUtils.tradueix(locale, "transporte.0" + registroDetalle.getTransporte(), null);
        }
        // Número transport
        String numTransport = registroDetalle.getNumeroTransporte();
        // Oficina origen, Registre origen i Data origen
        String oficinaOrigen = null;
        String numRegOrigen = null;
        String dataOrigen = null;
        String origen = null;
        Oficina oficinaOrigenDto = registroDetalle.getOficinaOrigen();
        String oficinaOrigenExternoCodigo = registroDetalle.getOficinaOrigenExternoCodigo();
        String oficinaOrigenExternoDenominacion = registroDetalle.getOficinaOrigenExternoDenominacion();
        
        if(oficinaOrigenDto !=null){
            if(!oficinaOrigenDto.getCodigo().equals(registroSalida.getOficina().getCodigo())) {
                oficinaOrigen = oficinaOrigenDto.getDenominacion() + " - " + oficinaOrigenDto.getCodigo();
                numRegOrigen = registroDetalle.getNumeroRegistroOrigen();
                Date fechaOrigen = registroDetalle.getFechaOrigen();
                dataOrigen = formatDate.format(fechaOrigen);
            }
        }

        if (oficinaOrigen == null && oficinaOrigenExternoCodigo != null) {
        	oficinaOrigen = oficinaOrigenExternoDenominacion + " - " + oficinaOrigenExternoCodigo;
        	numRegOrigen = registroDetalle.getNumeroRegistroOrigen();
        	Date fechaOrigen = registroDetalle.getFechaOrigen();
            dataOrigen = formatDate.format(fechaOrigen);
        }
        
        //Origen registro
        if (registroSalida.getOrigen() != null) {
        	origen = registroSalida.getOrigen().getCodigo() + " - " + registroSalida.getOrigen().getDenominacion();
        }
        // Observacions
        String observacions = registroDetalle.getObservaciones();

        // Título e Información Registro
    	String codigoOrgan = registroSalida.getOficina().getOrganismoResponsable().getDenominacion();
    	String nombreOrgan = registroSalida.getOficina().getOrganismoResponsable().getCodigo();
    	
        informacioRegistre(locale, document, denominacionOficina, codigoOficina, dataRegistre, numeroRegistroFormateado,
                tipoDocumentacionFisica, registroSalida.getClass().getSimpleName(), codigoOrgan, nombreOrgan, oficinaOrigen, numRegOrigen, dataOrigen);

        // Interesados
        List<Interesado> interesados = registroDetalle.getInteresados();
        llistarInteressats(interesados, locale, document, true);

        // Información adicional del Registro
        adicionalRegistre(locale, document, extracte, nomOrigen, expedient, tipoAsunto, codigoAsunto, nomIdioma, refExterna, transport,
                numTransport, oficinaOrigen, numRegOrigen, dataOrigen, observacions, origen, codiSia);

        // Anexos
        List<AnexoFull> anexos = registroDetalle.getAnexosFull();
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
    @SuppressWarnings({"unchecked" })
    protected void llistarAnnexes(List<AnexoFull> anexos, Locale locale, Document document,
                                  String denominacio) throws Exception {

    	List<String> altres = new ArrayList<String>();
    	//Comprovar si només hi ha fitxers tècnics o altres.
    	for(AnexoFull anexo : anexos) {
    		if (anexo.getAnexo().getTipoDocumento() != 3) {
    			altres.add("altres");
    		}
    	}
        if(anexos.size()>0 && !altres.isEmpty()) {
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
            PdfPTable taulaAnnexe = new PdfPTable(new float[]{15, 25, 10, 10, 10, 15, 12});
            taulaAnnexe.setWidthPercentage(100);
            PdfPCell cellInfoAnnexe = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.nombreAdjunto"), font8Bold));
            cellInfoAnnexe.setBackgroundColor(BaseColor.WHITE);
            cellInfoAnnexe.setBorderColor(BaseColor.BLACK);
            cellInfoAnnexe.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            taulaAnnexe.addCell(cellInfoAnnexe);
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.csv"), font8Bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.tamanyo"), font8Bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.validez"), font8Bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.tipoAdjunto"), font8Bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.hash"), font8Bold)));
            taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.observacionesAdjunto"), font8Bold)));

            PdfPCell cellInfoAnnexe2 = new PdfPCell(new Paragraph("", font8));
            cellInfoAnnexe2.setBackgroundColor(BaseColor.WHITE);
            cellInfoAnnexe2.setBorderColor(BaseColor.BLACK);
            cellInfoAnnexe2.setBorderWidth(1f);
            cellInfoAnnexe2.setHorizontalAlignment(Element.ALIGN_LEFT);

            for(AnexoFull anexo : anexos) {
                // Variables per calcular i afegeix el tamany del document (Del DocumentCustody i/o del SignatureCustody)
                String tamanyFitxer = null;
//                String nomFitxer = null;
                String hash = null;
                String csv = null;
                Chunk csvLink = null;
                String custodyID;
                Map<String, Object> docInfo, propertiesDoc;
                
                
                //CSV
                custodyID = anexo.getAnexo().getCustodiaID();
                if(custodyID.contains("#")) {
					custodyID = custodyID.substring(0, custodyID.indexOf("#"));
				}
                DocumentCustody doc = new DocumentCustody();
              //Site
    			alfrescoClient = new AlfrescoClient.Builder()
    					.connect(cmisUrl, 
    							 user, 
    							 password).build();
    			
    			doc = openCmisAlfrescoHelper.getDocumentById(
    					alfrescoClient, 
    					custodyID, 
    					false);
    			if (doc == null) {
	                docInfo = api.getMetadataDocument(custodyID);
	                if (docInfo != null) {
	                	propertiesDoc =  (Map<String, Object>) docInfo.get("properties");
	                	csv = (String) propertiesDoc.get("{http://www.portsdebalears.com/model/nti/1.0}csv");
	                	csvLink = new Chunk(csv);
	                	csvLink.setFont(font8);
	                	csvLink.setAnchor(consultaCSV + csv);
	                }
    			} else {
    				csv = "";
    			}
                // És un document amb firma detached i formulari no tècnic
                if(anexo.getAnexo().getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && anexo.getAnexo().getTipoDocumento() != 3){
                    cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), font8));
                    cellInfoAnnexe2.setRowspan(2);
                    taulaAnnexe.addCell(cellInfoAnnexe2);

                    // Document
//                    hash = new String(anexo.getAnexo().getHash());
//                    hash = Utils.decodeBase64Hash(anexo.getAnexo().getHash());
                    hash = Utils.decodeBase64Hash(RegwebUtils.obtenerHash(anexo.getDocumentoCustody().getData()));
//                    nomFitxer = anexo.getDocumentoCustody().getName();
                    if(anexo.getDocumentoCustody().getData().length < 1024){
                        tamanyFitxer = "1 KB";
                    }else{
                        tamanyFitxer = String.valueOf(anexo.getDocumentoCustody().getData().length/1024) + " KB";
                    }
                    
                    //CSV
                    if (csvLink != null) {
             		   Paragraph csvLn = new Paragraph();
             		   csvLn.add(csvLink);
             		   
             		   taulaAnnexe.addCell(new PdfPCell(csvLn));
             	   	} 
                    
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, font8)));
                    cellInfoAnnexe2 = new PdfPCell(new Paragraph(tradueixMissatge(locale,"tipoValidezDocumento." + anexo.getAnexo().getValidezDocumento()), font8));
                    cellInfoAnnexe2.setRowspan(2);
                    taulaAnnexe.addCell(cellInfoAnnexe2);
                    cellInfoAnnexe2 = new PdfPCell(new Paragraph(tradueixMissatge(locale,"tipoDocumento.0" + anexo.getAnexo().getTipoDocumento()), font8));
                    cellInfoAnnexe2.setRowspan(2);
                    taulaAnnexe.addCell(cellInfoAnnexe2);
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(hash, font8)));
                    cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getObservaciones(), font8));
                    cellInfoAnnexe2.setRowspan(2);
                    taulaAnnexe.addCell(cellInfoAnnexe2);

                    // Firma
//                    hash = tradueixMissatge(locale,"justificante.firma") + ": " + new String(RegwebUtils.obtenerHash(anexo.getSignatureCustody().getData()));
                    hash = tradueixMissatge(locale,"justificante.firma") + ": " + Utils.decodeBase64Hash(RegwebUtils.obtenerHash(anexo.getSignatureCustody().getData()));
//                    nomFitxer = tradueixMissatge(locale,"justificante.firma") + ": " + anexo.getSignatureCustody().getName();
                    if (anexo.getSignatureCustody().getData().length < 1024) {
                        tamanyFitxer = "1 KB";
                    } else {
                        tamanyFitxer = tradueixMissatge(locale,"justificante.firma") + ": " + String.valueOf(anexo.getSignatureCustody().getData().length / 1024) + " KB";
                    }
                    
                    if (csvLink != null) {
             		   Paragraph csvLn = new Paragraph();
             		   csvLn.add(csvLink);
             		   
             		   taulaAnnexe.addCell(new PdfPCell(csvLn));
             	   	}
                    
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, font8)));
                    taulaAnnexe.addCell(new PdfPCell(new Paragraph(hash, font8)));

                } else {
                    // És un document sense firma i formulari no tècnic
                    if(anexo.getAnexo().getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA && anexo.getAnexo().getTipoDocumento() != 3){
                        cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), font8));
                        taulaAnnexe.addCell(cellInfoAnnexe2);
//                        hash = new String(anexo.getAnexo().getHash());
                        hash = Utils.decodeBase64Hash(RegwebUtils.obtenerHash(anexo.getDocumentoCustody().getData()));
//                        nomFitxer = anexo.getDocumentoCustody().getName();
                        DocumentCustody documentInfo = anexo.getDocumentoCustody();
                        if (documentInfo != null && documentInfo.getData().length < 1024) {
                            tamanyFitxer = "1 KB";
                        } else if (documentInfo != null) {
                            tamanyFitxer = String.valueOf(documentInfo.getData().length/1024) + " KB";
                        } else {
                        	tamanyFitxer = " - ";
                        }
                    }else {
                        // És un document amb firma attached i formulari no tècnic
                        if (anexo.getAnexo().getModoFirma()==RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED && anexo.getAnexo().getTipoDocumento() != 3) {
                            cellInfoAnnexe2 = new PdfPCell(new Paragraph(anexo.getAnexo().getTitulo(), font8));
                            taulaAnnexe.addCell(cellInfoAnnexe2);
//                            hash = new String(anexo.getAnexo().getHash());
                            SignatureCustody signaturaInfo = anexo.getSignatureCustody();
                            if (signaturaInfo != null) {
                              hash = Utils.decodeBase64Hash(RegwebUtils.obtenerHash(signaturaInfo.getData()));
                              if (signaturaInfo != null && (signaturaInfo.getData()).length < 1024) {
                                tamanyFitxer = "1 KB";
                              } else if (signaturaInfo != null) {
                                tamanyFitxer = String.valueOf((signaturaInfo.getData()).length / 1024) + " KB";
                              } 
                            } else {
                              tamanyFitxer = " - ";
                            } 
                        }
                    }
                }
                if(anexo.getAnexo().getModoFirma()!=RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED && anexo.getAnexo().getTipoDocumento() != 3) {
	                   //CSV
                	   if (csvLink != null) {
                		   Paragraph csvLn = new Paragraph();
                		   csvLn.add(csvLink);
                		   
                		   taulaAnnexe.addCell(new PdfPCell(new Paragraph(csvLn)));
                	   }
//                	   PdfPCell cell = new PdfPCell(new Phrase(csv));
//                	   cell.setCellEvent(new LinkInCell(consultaCSV + csv));
//                	   taulaAnnexe.addCell(cell);
                	   taulaAnnexe.addCell(new PdfPCell(new Paragraph(tamanyFitxer, font8)));
	                   taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale, "tipoValidezDocumento." + anexo.getAnexo().getValidezDocumento()), font8)));
	                   taulaAnnexe.addCell(new PdfPCell(new Paragraph(tradueixMissatge(locale, "tipoDocumento.0" + anexo.getAnexo().getTipoDocumento()), font8)));
	                   taulaAnnexe.addCell(new PdfPCell(new Paragraph(hash, font8)));
	                   taulaAnnexe.addCell(new PdfPCell(new Paragraph(anexo.getAnexo().getObservaciones(), font8)));
                }
            }
            document.add(taulaAnnexe);
        }

        // Pie de anexo
        PdfPTable peuAnnexe = new PdfPTable(1);
        peuAnnexe.setWidthPercentage(100);
        // Obtenim el missatge de Declaración de les propietats del Plugin
        if(declaracion!=null) {
            PdfPCell cellPeuAnnexe = new PdfPCell(new Paragraph(denominacio + " " + declaracion, font8));
            cellPeuAnnexe.setBackgroundColor(BaseColor.WHITE);
            cellPeuAnnexe.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellPeuAnnexe.setBorder(Rectangle.TOP);
            cellPeuAnnexe.setBorderColor(BaseColor.BLACK);
            cellPeuAnnexe.setBorderWidth(1f);
            peuAnnexe.addCell(cellPeuAnnexe);
            document.add(peuAnnexe);
        }
        //document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        PdfPTable titolLlei = new PdfPTable(1);
        titolLlei.setWidthPercentage(100);
        // Obtenim el missatge de Ley de les propietats del Plugin
        PdfPCell cellLlei = new PdfPCell(new Paragraph(ley, font8));
        cellLlei.setBackgroundColor(BaseColor.WHITE);
        cellLlei.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellLlei.setBorderColor(BaseColor.WHITE);
        cellLlei.setBorderWidth(0f);
        titolLlei.addCell(cellLlei);
        document.add(titolLlei);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

    }

    // Lista los interesados y representantes tanto para el registro de entrada como el de salida
    protected void llistarInteressats(List<Interesado> interesados, Locale locale, Document document, Boolean isSalida) throws Exception {

        // Creamos estilo para el título Interesado
        PdfPTable titolInteressat = new PdfPTable(1);
        titolInteressat.setWidthPercentage(100);
        String etiqueta;
        if(isSalida){ // Si és una sortida
            etiqueta = tradueixMissatge(locale,"justificante.destinatario");
        }else{  // Si és una entrada
            etiqueta = tradueixMissatge(locale,"justificante.interesado");
        }
        PdfPCell cellInteressat = new PdfPCell(new Paragraph(etiqueta, font8Bold));
        cellInteressat.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellInteressat.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellInteressat.setBorder(Rectangle.NO_BORDER);
        //cellInteressat.setBorderColor(BaseColor.BLACK);
        //cellInteressat.setBorderWidth(2f);
        titolInteressat.addCell(cellInteressat);

        // Creamos estilo para el título Representante
        PdfPTable titolRepresentant = new PdfPTable(1);
        titolRepresentant.setWidthPercentage(90);

        PdfPTable taulaInteresado = new PdfPTable(new float[] { 15, 30, 15, 30 });
        // Añadimos una entrada para cada interesado
        int i;
        for(Interesado interesado : interesados) {
            // Añadimos título
            if(!interesado.getIsRepresentante()) {
                i=0;
                document.add(titolInteressat);
                // Añadimos campos del interesado
                taulaInteresado.setWidthPercentage(100);
                taulaInteresado.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
                taulaInteresado.getDefaultCell().setBorder(0);
                taulaInteresado.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                // Document
                if(StringUtils.isNotEmpty(interesado.getDocumento())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.documento"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getDocumento(), font8));
                }
                // Tipus Document
                if(interesado.getTipoDocumentoIdentificacion()!= null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.tipoDocumento"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(I18NJustificanteUtils.tradueix(locale, "tipoDocumentoIdentificacion." + interesado.getTipoDocumentoIdentificacion(), null), font8));
                }
                // Nom - Organ
                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.nombre"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getNombreCompleto(), font8));
                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.razonSocial"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getRazonSocial(), font8));
                } else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.organismo"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getRazonSocial(), font8));
                }
                // Pais
                if(interesado.getPais() != null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.pais"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getPais().getDescripcionPais(), font8));
                }
                // Adreça
                if(StringUtils.isNotEmpty(interesado.getDireccion())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.direccion"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getDireccion(), font8));
                }
                // Municipi
                if(interesado.getLocalidad() != null && interesado.getCp() != null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.municipio"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getCp() + " - " + interesado.getLocalidad().getNombre(), font8));
                }
                // Provincia
                if(interesado.getProvincia() != null) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.provincia"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getProvincia().getDescripcionProvincia(), font8));
                }
                // Telefon
                if(StringUtils.isNotEmpty(interesado.getTelefono())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.telefono"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getTelefono(), font8));
                }
                // Mail
                if(StringUtils.isNotEmpty(interesado.getEmail())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.correo"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getEmail(), font8));
                }
                // Canal Notificacio
                /*if((interesado.getCanal() != null) && (interesado.getCanal() != -1)) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale,"justificante.canalNot"), font8Bold));
                    String canalNotif = tradueixMissatge(locale,"canalNotificacion." + interesado.getCanal());
                    taulaInteresado.addCell(new Paragraph(canalNotif, font8));
                }
                // Direccio Electronica Habilitada
                if(StringUtils.isNotEmpty(interesado.getDireccionElectronica())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.deh"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getDireccionElectronica(), font8));
                }*/
                // Observacions
                if(StringUtils.isNotEmpty(interesado.getObservaciones())) {
                    i += 1;
                    taulaInteresado.addCell(new Paragraph(tradueixMissatge(locale, "justificante.observaciones"), font8Bold));
                    taulaInteresado.addCell(new Paragraph(interesado.getObservaciones(), font8));
                }
                // Completa la cel·la buida
                if(!esPar(i)) {
                    taulaInteresado.addCell("");
                    taulaInteresado.addCell("");
                }

                document.add(taulaInteresado);
                // Vaciamos el contenido del interesado para rellenarlo con uno nuevo
                taulaInteresado.deleteBodyRows();
                //document.add(new Paragraph(" "));

                // Si el interesado tiene representante
                if(interesado.getRepresentante() != null) {
                    // Recorremos todos los interesados para buscar el reresentante
                    for(Interesado representante : interesados) {
                        // Encuentra su representante y lo muestra
                        if (interesado.getRepresentante().getId().equals(representante.getId())) {
                            i = 0;
                            PdfPCell cellRepresentant = new PdfPCell(new Paragraph(tradueixMissatge(locale, "justificante.representante") + " de " + interesado.getNombreCompleto(), font8Bold));
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
                            if(StringUtils.isNotEmpty(representante.getDocumento())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.documento"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getDocumento(), font8));
                            }
                            // Tipus Document
                            if(representante.getTipoDocumentoIdentificacion()!=null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.tipoDocumento"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(I18NJustificanteUtils.tradueix(locale, "tipoDocumentoIdentificacion." + representante.getTipoDocumentoIdentificacion(), null), font8));
                            }
                            // Nom - Organ
                            if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.nombre"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getNombreCompleto(), font8));
                            } else if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.razonSocial"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getRazonSocial(), font8));
                            } else if(representante.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.organismo"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getRazonSocial(), font8));
                            }
                            // Pais
                            if(representante.getPais() != null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.pais"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getPais().getDescripcionPais(), font8));
                            }
                            // Adreça
                            if(StringUtils.isNotEmpty(representante.getDireccion())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.direccion"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getDireccion(), font8));
                            }
                            // Municipi
                            if(representante.getLocalidad() != null && representante.getCp() != null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.municipio"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getCp() + " - " + representante.getLocalidad().getNombre(), font8));
                            }
                            // Provincia
                            if(representante.getProvincia() != null) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.provincia"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getProvincia().getDescripcionProvincia(), font8));
                            }
                            // Telefon
                            if(StringUtils.isNotEmpty(representante.getTelefono())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.telefono"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getTelefono(), font8));
                            }
                            // Mail
                            if(StringUtils.isNotEmpty(representante.getEmail())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.correo"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getEmail(), font8));
                            }
                            // Canal Notificacio
                            /*if((representante.getCanal() != null) && (representante.getCanal() != -1)) {
                             
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale,"justificante.canalNot"), font8Bold));
                                String canalNotifRep = tradueixMissatge(locale,"canalNotificacion." + representante.getCanal());
                                taulaRepresentant.addCell(new Paragraph(canalNotifRep, font8));
                            }
                            // Direccio Electronica Habilitada
                            if(StringUtils.isNotEmpty(representante.getDireccionElectronica())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.deh"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getDireccionElectronica(), font8));
                            }*/
                            // Observacions
                            if(StringUtils.isNotEmpty(representante.getObservaciones())) {
                                i += 1;
                                taulaRepresentant.addCell(new Paragraph(tradueixMissatge(locale, "justificante.observaciones"), font8Bold));
                                taulaRepresentant.addCell(new Paragraph(representante.getObservaciones(), font8));
                            }
                            if(!esPar(i)) {
                                // Completa la cel·la buida
                                taulaRepresentant.addCell("");
                                taulaRepresentant.addCell("");
                            }
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
                                      String dataRegistre, String numeroRegistroFormateado, Long tipoDocumentacionFisica,
                                      String tipoRegistro, String codigoOrgan, String nombreOrgan,
                                      String oficinaOrigen, String numRegOrigen, String dataOrigen) throws Exception {
    	
        document.addTitle(tradueixMissatge(locale,"justificante.anexo.titulo"));
        PdfPTable titulo = new PdfPTable(1);
        titulo.setWidthPercentage(100);
        PdfPCell cellTitulo= new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.mensaje.titulo"), font16Bold));
        cellTitulo.setBackgroundColor(BaseColor.WHITE);
        cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellTitulo.setBorder(Rectangle.NO_BORDER);
        //cellTitulo.setBorderColor(BaseColor.BLACK);
        //cellTitulo.setBorderWidth(2f);
        cellTitulo.setPaddingBottom(10f);
        titulo.addCell(cellTitulo);
        document.add(titulo);
        
        // REGISTRO
        PdfPTable taulaRegistre = new PdfPTable(new float[] { 1, 3 });
        PdfPCell salt = new PdfPCell(new Paragraph(" "));
		salt.setColspan(2);
		salt.setBorder(Rectangle.NO_BORDER);
		salt.setFixedHeight(5f);
		
        taulaRegistre.setWidthPercentage(100);
        taulaRegistre.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        taulaRegistre.getDefaultCell().setBorder(0);
        taulaRegistre.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        
        //Afegit
        // Tipus registre
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.tipusRegistre"), font8Bold));
        if(tipoRegistro.equals("RegistroEntrada")) {
            taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.entrada"), font8));
        }else{
            taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.salida"), font8));
        }
        // Administració
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.administracion"), font8Bold));
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.administracion.value"), font8));
        // Documentació física
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.docFisica"), font8Bold));
        String docFisica = tradueixMissatge(locale,"tipoDocumentacionFisica." + tipoDocumentacionFisica);
        taulaRegistre.addCell(new Paragraph(docFisica, font8));
        taulaRegistre.addCell(salt);
        
        // Oficina registre
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.oficina"), font8Bold));
        String oficina = denominacionOficina + " - " + codigoOficina;
        taulaRegistre.addCell(new Paragraph(oficina, font8));
        // Data presentació
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.fechaPresentacion"), font8Bold));
        taulaRegistre.addCell(new Paragraph((dataRegistre != null ? dataRegistre : " - "), font8));
        // Número de registre
        taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.numRegistro"), font8Bold));
        taulaRegistre.addCell(new Paragraph((numeroRegistroFormateado != null ? numeroRegistroFormateado : " - "), font8));
        
        // Dades registre origen
        if (numRegOrigen != null && !numRegOrigen.equals(numeroRegistroFormateado)) {
        	taulaRegistre.addCell(salt);
        	// Oficina registre origen
        	taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.oficina.origen"), font8Bold));
            taulaRegistre.addCell(new Paragraph(oficinaOrigen, font8));
            // Data presentació origen
            taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.fechaPresentacion.origen"), font8Bold));
            taulaRegistre.addCell(new Paragraph((dataOrigen != null ? dataOrigen : " - "), font8));
            // Número de registre a l'origen
            taulaRegistre.addCell(new Paragraph(tradueixMissatge(locale,"justificante.numRegistro.origen"), font8Bold));
            taulaRegistre.addCell(new Paragraph(numRegOrigen, font8)); 
        }
        document.add(taulaRegistre);
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

    }

    // Añade más información al registro
    protected void adicionalRegistre(Locale locale, Document document, String extracte, String nomDesti, String expedient,
                                     String tipoAsunto, String codigoAsunto, String idioma, String refExterna, String transport,
                                     String numTransport, String oficinaOrigen, String numRegOrigen, String dataOrigen,
                                     String observacions, String origen, Long codiSia) throws Exception {

        // Creamos estilo para el título Información
        PdfPTable titolInformacio = new PdfPTable(1);
        titolInformacio.setWidthPercentage(100);
        PdfPCell cellInformacio = new PdfPCell(new Paragraph(tradueixMissatge(locale,"justificante.informacion"), font8Bold));
        cellInformacio.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellInformacio.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellInformacio.setBorder(Rectangle.NO_BORDER);
        //cellInformacio.setBorderColor(BaseColor.BLACK);
        //cellInformacio.setBorderWidth(2f);
        titolInformacio.addCell(cellInformacio);
        document.add(titolInformacio);

        // Añadimos los campos de la Información
        PdfPTable taulaInformacio = new PdfPTable(new float[] { 25, 25, 25, 25 });
        taulaInformacio.setWidthPercentage(100);
        taulaInformacio.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        taulaInformacio.getDefaultCell().setBorder(0);
        taulaInformacio.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

        int i= 0;

        // Destí - Centre Directiu
        if(StringUtils.isNotEmpty(nomDesti)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.unidad"), font8Bold));
            taulaInformacio.addCell(new Paragraph(nomDesti, font8));
        }
        // Extracte
        if(StringUtils.isNotEmpty(extracte)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.resumen"), font8Bold));
            taulaInformacio.addCell(new Paragraph(extracte, font8));
        }
        // Tipus assumpte
        if(StringUtils.isNotEmpty(tipoAsunto)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.tipoAsunto"), font8Bold));
            taulaInformacio.addCell(new Paragraph(tipoAsunto, font8));
        }
        // Codi assumpte
        if(StringUtils.isNotEmpty(codigoAsunto)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.codigoAsunto"), font8Bold));
            taulaInformacio.addCell(new Paragraph(codigoAsunto, font8));
        }
        // Idioma
        if(StringUtils.isNotEmpty(idioma)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.idioma"), font8Bold));
            taulaInformacio.addCell(new Paragraph(idioma, font8));
        }
        // Referència externa
        if(StringUtils.isNotEmpty(refExterna)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.refExterna"), font8Bold));
            taulaInformacio.addCell(new Paragraph(refExterna, font8));
        }
        // Expedient
        if(StringUtils.isNotEmpty(expedient)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.expediente"), font8Bold));
            taulaInformacio.addCell(new Paragraph(expedient, font8));
        }
        // Transport
        if(StringUtils.isNotEmpty(transport)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.transport"), font8Bold));
            taulaInformacio.addCell(new Paragraph(transport, font8));
        }
        // Número transport
        if(StringUtils.isNotEmpty(numTransport)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.numTransport"), font8Bold));
            taulaInformacio.addCell(new Paragraph(numTransport, font8));
        }
        // Oficina origen
        if(StringUtils.isNotEmpty(oficinaOrigen)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.oficinaOrigen"), font8Bold));
            taulaInformacio.addCell(new Paragraph(oficinaOrigen, font8));
        }
        // Número Registre origen
        if(StringUtils.isNotEmpty(numRegOrigen)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.numRegOrigen"), font8Bold));
            taulaInformacio.addCell(new Paragraph(numRegOrigen, font8));
        }
        // Data Registre origen
        if(StringUtils.isNotEmpty(dataOrigen)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.dataOrigen"), font8Bold));
            taulaInformacio.addCell(new Paragraph(dataOrigen, font8));
        }
        // Observacions
        if(StringUtils.isNotEmpty(observacions)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.observacions"), font8Bold));
            taulaInformacio.addCell(new Paragraph(observacions, font8));
        }
        // Origen
        if(origen != null && StringUtils.isNotEmpty(origen)) {
            i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.origen"), font8Bold));
            taulaInformacio.addCell(new Paragraph(origen, font8));
        }
        if (codiSia != null) {
        	i += 1;
            taulaInformacio.addCell(new Paragraph(tradueixMissatge(locale, "justificante.codigosia"), font8Bold));
            taulaInformacio.addCell(new Paragraph(String.valueOf(codiSia), font8));
        }
        if(!esPar(i)) {
            // Completa la cel·la buida
            taulaInformacio.addCell("");
            taulaInformacio.addCell("");
        }
        document.add(taulaInformacio);
        document.add(new Paragraph(" "));
        //document.add(new Paragraph(" "));

    }

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

    public static boolean esPar(int numero) {
        //Si el resto de numero entre 2 es 0, el numero es par
        return numero % 2 == 0;
    }

}