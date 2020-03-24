package es.caib.regweb3.persistence.integracion;

import es.caib.plugins.arxiu.api.*;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.fundaciobit.pluginsib.core.utils.XTrustProvider;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ArxiuCaibUtils {

    public static final Logger log = Logger.getLogger(ArxiuCaibUtils.class);

    private IArxiuPlugin arxiuPlugin;


    /**
     *
     * @param registro
     * @param tipoRegistro
     * @param anexoFull
     * @return
     * @throws Exception
     */
    public String crearJustificante(IRegistro registro, Long tipoRegistro, AnexoFull anexoFull) throws Exception{

        String serieDocumental = "S0002"; // PropiedadGlobal getSerieDocumental()
        String codigoProcedimiento = "1234"; // PropiedadGlobal getCodigoProcedimiento()

        // Creamos el Expediente del Justificante
        String uuidExpedient = expedientCrear(registro, tipoRegistro, serieDocumental, codigoProcedimiento);
        log.info("Expediente creado: " + uuidExpedient);

        // Creamos el Documento del Justificante
        String uuidDocument = documentCrear(anexoFull, registro, tipoRegistro, uuidExpedient);
        log.info("Documento creado: " + uuidExpedient);

        //return uuidExpedient+"#"+uuidDocument;
        return uuidDocument;
    }

    /**
     *
     * @param registro
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    private String expedientCrear(IRegistro registro, Long tipoRegistro, String serieDocumental, String codigoProcedimiento) throws Exception {

        String nombreExpediente = getNombreExpediente(registro, tipoRegistro);

        Interesado interesado = registro.getRegistroDetalle().getInteresados().get(0);


        // TODO COMPROBAR EXISTENCIA DEL EXPEDIENTE ANTES DE CREAR
        // Generamos el Expedient
        Expedient expediente = getExpedient(null,
                nombreExpediente,
                null,
                Arrays.asList(registro.getOficina().getCodigo()),
                new Date(),
                codigoProcedimiento,
                ExpedientEstat.OBERT,
                Arrays.asList(interesado.getDocumentoNTI()),
                serieDocumental);

        // Creamos el Expediente en Arxiu
        ContingutArxiu expedienteCreado = getArxiuPlugin().expedientCrear(expediente);

        if(expedienteCreado == null){
            log.info("Expediente no creado");
        }else{
            log.info("Expediente creado: " + expedienteCreado.getNom());
        }

        return expedienteCreado.getIdentificador();
    }


    /**
     *
     * @param anexoFull
     * @param registro
     * @param tipoRegistro
     * @param uuidExpedient
     * @return
     * @throws Exception
     */
    private String documentCrear(AnexoFull anexoFull,IRegistro registro, Long tipoRegistro, String uuidExpedient) throws Exception{

        //Generamos el Documento
        Document documento = getDocument(registro, tipoRegistro, anexoFull);

        // Crear el Documento en Arxiu

        ContingutArxiu documentoCreado = getArxiuPlugin().documentCrear(documento, uuidExpedient);

        return documentoCreado.getIdentificador();
    }

    /**
     * Obtiene el CSV de un {@link es.caib.plugins.arxiu.api.Document}
     * @param identificadorDocumento
     * @return
     * @throws Exception
     */
    public String getCsv(String identificadorDocumento) throws Exception{
        return getArxiuPlugin().getCsv(identificadorDocumento);
    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.Expedient} a partir de los parámetros
     * @param identificador
     * @param nombre
     * @param ntiIdentificador
     * @param ntiOrgans
     * @param ntiDataObertura
     * @param ntiClassificacio
     * @param expedientEstat
     * @param ntiInteressats
     * @param serieDocumental
     * @return
     */
    private Expedient getExpedient(String identificador,
                                         String nombre,
                                         String ntiIdentificador,
                                         List<String> ntiOrgans,
                                         Date ntiDataObertura,
                                         String ntiClassificacio,
                                         ExpedientEstat expedientEstat,
                                         List<String> ntiInteressats,
                                         String serieDocumental) {

        Expedient expedient = new Expedient();
        expedient.setNom(nombre);
        expedient.setIdentificador(identificador);

        ExpedientMetadades metadades = new ExpedientMetadades();
        metadades.setIdentificador(ntiIdentificador);
        metadades.setDataObertura(ntiDataObertura);
        metadades.setClassificacio(ntiClassificacio);
        metadades.setEstat(expedientEstat);
        metadades.setOrgans(ntiOrgans);
        metadades.setInteressats(ntiInteressats);
        metadades.setSerieDocumental(serieDocumental);

        expedient.setMetadades(metadades);

        return expedient;
    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.Document} a partir de los parámetros
     * @param registro
     * @param tipoRegistro
     * @param anexoFull
     * @return
     * @throws Exception
     */
    private Document getDocument(IRegistro registro, Long tipoRegistro, AnexoFull anexoFull) throws Exception {

        // Documento
        Document document = new Document();
        document.setIdentificador(null);
        document.setEstat(DocumentEstat.DEFINITIU);

        // Metadatos
        DocumentMetadades metadades = new DocumentMetadades();
        metadades.setIdentificador(null);
        metadades.setSerieDocumental("S0002");
        metadades.setOrgans(Arrays.asList(registro.getOficina().getCodigo()));
        metadades.setDataCaptura(anexoFull.getAnexo().getFechaCaptura());

        // Metadata Origen
        switch (anexoFull.getAnexo().getOrigenCiudadanoAdmin()) {
            case 0:
                metadades.setOrigen(ContingutOrigen.CIUTADA);
                break;
            case 1:
                metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
                break;
        }

        // Metadata Estado elaboración
        switch (anexoFull.getAnexo().getValidezDocumento().intValue()) {

            case 1: // Tipo Validez Doc: Copia
                metadades.setEstatElaboracio(DocumentEstatElaboracio.ALTRES);
                break;
            case 2: // Tipo Validez Doc: Copia compulsada -> se convierte en Copia
                metadades.setEstatElaboracio(DocumentEstatElaboracio.ALTRES);
                break;
            case 3: // Tipo Validez Doc: Copia original
                metadades.setEstatElaboracio(DocumentEstatElaboracio.COPIA_DP);
                break;
            case 4: // Tipo Validez Doc: Original
                metadades.setEstatElaboracio(DocumentEstatElaboracio.ORIGINAL);
                break;
        }

        metadades.setTipusDocumental(DocumentTipus.toEnum(anexoFull.getAnexo().getTipoDocumental().getCodigoNTI()));

        // Metadata Extension
        DocumentExtensio extension = DocumentExtensio.toEnum("." + anexoFull.getExtension());
        if (extension != null) {
            metadades.setExtensio(extension);
            metadades.setFormat(getDocumentFormat(extension));
        }

        // Contingut
        DocumentContingut contingut = new DocumentContingut();

        if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

            document.setNom(anexoFull.getDocumentoCustody().getName());

            // Documento sin firma
            contingut.setArxiuNom(anexoFull.getDocumentoCustody().getName());
            contingut.setContingut(anexoFull.getDocumentoCustody().getData());
            contingut.setTipusMime(anexoFull.getDocMime());
            contingut.setTamany(anexoFull.getDocSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior


        } else if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED) {

            document.setNom(anexoFull.getSignatureCustody().getName());

            // Documento y Firma attached
            Firma firma = getFirma(anexoFull);
            document.setFirmes(new ArrayList<Firma>());
            document.getFirmes().add(firma);

            contingut = null;


        } else if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

            document.setNom(anexoFull.getDocumentoCustody().getName());

            // Documento
            contingut.setArxiuNom(anexoFull.getDocumentoCustody().getName());
            contingut.setContingut(anexoFull.getDocumentoCustody().getData());
            contingut.setTipusMime(anexoFull.getDocMime());
            contingut.setTamany(anexoFull.getDocSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior

            // Firma detached
            Firma firma = getFirma(anexoFull);
            document.setFirmes(new ArrayList<Firma>());
            document.getFirmes().add(firma);

        }

        document.setContingut(contingut);

        // Metadatos adicionales
        Map<String, Object> metaDadesAddicionals = new HashMap<String, Object>();
        metaDadesAddicionals.put("eni:numero_asiento_registral", registro.getNumeroRegistro());
        if (registro.getFecha() != null) {
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            df.setTimeZone(tz);
            metaDadesAddicionals.put("eni:fecha_asiento_registral", df.format(registro.getFecha()));
        }
        metaDadesAddicionals.put("eni:codigo_oficina_registro", registro.getOficina().getCodigo());
        metaDadesAddicionals.put("eni:tipo_asiento_registral", tipoRegistro.intValue());

        metadades.setMetadadesAddicionals(metaDadesAddicionals);

        document.setMetadades(metadades);

        return document;
    }


    /**
     * Obtiene una {@link es.caib.plugins.arxiu.api.Firma} a partir de los parámetros
     * @param anexoFull
     * @return
     * @throws Exception
     */
    private Firma getFirma(AnexoFull anexoFull) throws Exception {

        Firma firma = new Firma();
        firma.setFitxerNom(anexoFull.getSignatureCustody().getName());
        firma.setContingut(anexoFull.getSignatureCustody().getData());
        firma.setCsvRegulacio("");
        firma.setTamany(anexoFull.getSignatureCustody().getData().length);

        firma.setPerfil(getFirmaPerfil(anexoFull.getAnexo().getSignProfile()));
        firma.setTipus(getFirmaTipus(anexoFull.getAnexo()));

        firma.setTipusMime(anexoFull.getSignatureCustody().getMime());

        return firma;
    }

    /**
     *  ${registro.libro.codigo}-<#if registro.origen??>S<#else>E</#if>-${registro.numeroRegistro?string[\"0\"]}_${(registro.fecha)?string[\"yyyy\"]}
     * @param registro
     * @return
     * @throws Exception
     */
    public String getNombreExpediente(IRegistro registro, Long tipoRegistro) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");


        return registro.getLibro().getCodigo() +"-"+ getTipoRegistro(tipoRegistro) +"-"+ registro.getNumeroRegistro() +"_"+ sdf.format(registro.getFecha());

    }

    /**
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    private String getTipoRegistro(Long tipoRegistro) throws Exception {

        if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {
            return "E";
        } else {
            return "S";
        }

    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.DocumentFormat} a partir de los parámetros
     * @param extension
     * @return
     */
    private DocumentFormat getDocumentFormat(DocumentExtensio extension){

        switch (extension) {
            case AVI: return DocumentFormat.AVI;

            case CSS: return DocumentFormat.CSS;

            case CSV: return DocumentFormat.CSV;

            case DOCX: return DocumentFormat.SOXML;

            case GML: return DocumentFormat.GML;

            case GZ: return DocumentFormat.GZIP;

            case HTM: return DocumentFormat.XHTML; // HTML o XHTML!!!

            case HTML: return DocumentFormat.XHTML; // HTML o XHTML!!!

            case JPEG: return DocumentFormat.JPEG;

            case JPG: return DocumentFormat.JPEG;

            case MHT: return DocumentFormat.MHTML;

            case MHTML: return DocumentFormat.MHTML;

            case MP3: return DocumentFormat.MP3;

            case MP4: return DocumentFormat.MP4V; // MP4A o MP4V!!!

            case MPEG: return DocumentFormat.MP4V; // MP4A o MP4V!!!

            case ODG: return DocumentFormat.OASIS12;

            case ODP: return DocumentFormat.OASIS12;

            case ODS: return DocumentFormat.OASIS12;

            case ODT: return DocumentFormat.OASIS12;

            case OGA: return DocumentFormat.OGG;

            case OGG: return DocumentFormat.OGG;

            case PDF: return DocumentFormat.PDF; // PDF o PDFA!!!

            case PNG: return DocumentFormat.PNG;

            case PPTX: return DocumentFormat.SOXML;

            case RTF: return DocumentFormat.RTF;

            case SVG: return DocumentFormat.SVG;

            case TIFF: return DocumentFormat.TIFF;

            case TXT: return DocumentFormat.TXT;

            case WEBM: return DocumentFormat.WEBM;

            case XLSX: return DocumentFormat.SOXML;

            case ZIP: return DocumentFormat.ZIP;

            case CSIG: return DocumentFormat.CSIG;

            case XSIG: return DocumentFormat.XSIG;

            case XML: return DocumentFormat.XML;

        }

        return null;
    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.FirmaPerfil} a partir de los parámetros
     * @param singProfile
     * @return
     * @throws Exception
     */
    private FirmaPerfil getFirmaPerfil(String singProfile) throws Exception{

        if(ValidateSignatureConstants.SIGNPROFILE_BES.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_X1.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_X2.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_XL1.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_XL2.equals(singProfile) ||
                ValidateSignatureConstants.SIGNPROFILE_PADES_BASIC.equals(singProfile)){
            return FirmaPerfil.BES;
        }else if(ValidateSignatureConstants.SIGNPROFILE_EPES.equals(singProfile)){
            return FirmaPerfil.EPES;
        }else  if(ValidateSignatureConstants.SIGNPROFILE_PADES_LTV.equals(singProfile)){
            return FirmaPerfil.LTV;
        }else if(ValidateSignatureConstants.SIGNPROFILE_T.equals(singProfile)){
            return FirmaPerfil.T;
        }else  if(ValidateSignatureConstants.SIGNPROFILE_C.equals(singProfile)){
            return FirmaPerfil.C;
        }else if(ValidateSignatureConstants.SIGNPROFILE_X.equals(singProfile)){
            return FirmaPerfil.X;
        }else if(ValidateSignatureConstants.SIGNPROFILE_XL.equals(singProfile)){
            return FirmaPerfil.XL;
        }else  if(ValidateSignatureConstants.SIGNPROFILE_A.equals(singProfile)){
            return FirmaPerfil.A;
        }

        return null;
    }

    /**
     * Obtiene un {@link es.caib.plugins.arxiu.api.FirmaTipus} a partir de los parámetros
     * @param anexo
     * @return
     * @throws Exception
     */
    private FirmaTipus getFirmaTipus(Anexo anexo) throws Exception{

        if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) &&
                (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat()) || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF02

            return FirmaTipus.XADES_DET;
        }else if (ValidateSignatureConstants.SIGNTYPE_XAdES.equals(anexo.getSignType()) &&
                ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF03

            return FirmaTipus.XADES_ENV;
        }else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) &&
                (ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_DETACHED.equals(anexo.getSignFormat()) || ValidateSignatureConstants.SIGNFORMAT_EXPLICIT_EXTERNALLY_DETACHED.equals(anexo.getSignFormat()))) {//TF04

            return FirmaTipus.CADES_DET;
        } else if (ValidateSignatureConstants.SIGNTYPE_CAdES.equals(anexo.getSignType()) &&
                ValidateSignatureConstants.SIGNFORMAT_IMPLICIT_ENVELOPING_ATTACHED.equals(anexo.getSignFormat())) { //TF05
            return FirmaTipus.CADES_ATT;
        } else if (ValidateSignatureConstants.SIGNTYPE_PAdES.equals(anexo.getSignType())) {//TF06

            return FirmaTipus.PADES;
        } else  if (ValidateSignatureConstants.SIGNTYPE_ODF.equals(anexo.getSignType())) { //TF08

            return FirmaTipus.ODT;
        }else  if (ValidateSignatureConstants.SIGNTYPE_OOXML.equals(anexo.getSignType())) { //TF09

            return FirmaTipus.OOXML;
        }

        return null;

    }

    /**
     *
     * @return
     * @throws Exception
     */
    private IArxiuPlugin getArxiuPlugin() throws Exception {

        XTrustProvider.install();
        return arxiuPlugin;

    }

    /**
     * Asociamos el plugin de Arxiu
     * @param iArxiuPlugin
     */
    public void setArxiuPlugin(IArxiuPlugin iArxiuPlugin) {

        this.arxiuPlugin = iArxiuPlugin;
    }
}
