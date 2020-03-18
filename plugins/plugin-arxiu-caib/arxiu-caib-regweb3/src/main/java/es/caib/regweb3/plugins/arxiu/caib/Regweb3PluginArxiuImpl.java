package es.caib.regweb3.plugins.arxiu.caib;

import es.caib.plugins.arxiu.api.*;
import es.caib.regweb3.model.IRegistro;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.plugins.arxiu.caib.utils.Regweb3PluginArxiuUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.pluginsib.core.utils.AbstractPluginProperties;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Regweb3PluginArxiuImpl extends AbstractPluginProperties implements Regweb3PluginArxiu{

    private static final String basePluginRegweb3Arxiu = "plugin.arxiu.";

    private static final String URL = basePluginRegweb3Arxiu + "base.url";
    private static final String USER = basePluginRegweb3Arxiu + "usuari";
    private static final String PASS = basePluginRegweb3Arxiu + "contrasenya";
    private static final String CODI_APLICACIO = basePluginRegweb3Arxiu + "aplicacio.codi";
    private static final String NOMBRE_PROCEDIMIENTO = basePluginRegweb3Arxiu + "nombreProcedimiento";
    private static final String SERIE_DOCUMENTAL = basePluginRegweb3Arxiu + "serieDocumental";
    private static final String CODIGO_PROCEDIMIENTO = basePluginRegweb3Arxiu + "codigoProcedimiento";
    private static final String ARXIU_CLASS = basePluginRegweb3Arxiu + "class";

    private IArxiuPlugin arxiuPlugin;


    public Regweb3PluginArxiuImpl() {
    }

    public Regweb3PluginArxiuImpl(String propertyKeyBase) {
        super(propertyKeyBase);
    }

    public Regweb3PluginArxiuImpl(String propertyKeyBase, Properties properties) {
        super(propertyKeyBase, properties);
    }


    public String crearJustificante(IRegistro registro, Long tipoRegistro, AnexoFull anexoFull) throws Exception{

        // Creamos el Expediente del Justificante
        String uuidExpedient = expedientCrear(registro, tipoRegistro);

        // Creamos el Documento del Justificante
        String uuidDocument = documentCrear(anexoFull, registro, tipoRegistro, uuidExpedient);


        return uuidExpedient+"#"+uuidDocument;
    }

    /**
     *
     * @param registro
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    private String expedientCrear(IRegistro registro, Long tipoRegistro) throws Exception {

        String nombreExpediente = getNombreExpediente(registro, tipoRegistro);
        String serieDocumental = getPropertySerieDocumental();
        Interesado interesado = registro.getRegistroDetalle().getInteresados().get(0);

        // Generamos el Expedient
        Expedient expediente = getExpedient(null,
                nombreExpediente,
                null,
                Arrays.asList(registro.getOficina().getCodigo()),
                new Date(),
                getPropertyCodigoProcedimiento(),
                ExpedientEstat.OBERT,
                Arrays.asList(interesado.getDocumentoNTI()),
                serieDocumental);

        // Creamos el Expediente en Arxiu
        ContingutArxiu expedienteCreado = getArxiuPlugin().expedientCrear(expediente);

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
    private String documentCrear(AnexoFull anexoFull,IRegistro registro, Long tipoRegistro,  String uuidExpedient) throws Exception{

        //Generamos el Documento
        Document documento = getDocument(registro, tipoRegistro, anexoFull);

        // Crear el Documento en Arxiu
        ContingutArxiu documentoCreado = getArxiuPlugin().documentCrear(documento, uuidExpedient);

        return documentoCreado.getIdentificador();
    }


    private IArxiuPlugin getArxiuPlugin() throws Exception {


        return (IArxiuPlugin) org.fundaciobit.pluginsib.core.utils.PluginsManager.instancePluginByClassName(ARXIU_CLASS, RegwebConstantes.REGWEB3_PROPERTY_BASE, null);

    }


    private String getPropertyUrl() throws Exception {

        return getProperty(URL);
    }

    private String getPropertyPassword() throws Exception {

        return getProperty(PASS);
    }

    private String getPropertyUsuario() throws Exception {

        return getProperty(USER);
    }

    private String getPropertyCodigoAplicacion() throws Exception {

        return getProperty(CODI_APLICACIO);
    }

    private String getPropertyCodigoProcedimiento() throws Exception {

        return getProperty(CODIGO_PROCEDIMIENTO);
    }

    private String getPropertyNombreProcedimiento() throws Exception {

        return getProperty(NOMBRE_PROCEDIMIENTO);
    }

    private String getPropertySerieDocumental() throws Exception {

        return getProperty(SERIE_DOCUMENTAL);
    }

    /**
     *  ${registro.libro.codigo}-<#if registro.origen??>S<#else>E</#if>-${registro.numeroRegistro?string[\"0\"]}_${(registro.fecha)?string[\"yyyy\"]}
     * @param registro
     * @return
     * @throws Exception
     */
    private String getNombreExpediente(IRegistro registro, Long tipoRegistro) throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");


        return registro.getLibro().getCodigo() +"-"+ getTipoRegistro(tipoRegistro) +"-"+ registro.getNumeroRegistro() +"_"+ sdf.format(registro.getFecha());

    }



    /**
     *
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
     *
     * @param registro
     * @param tipoRegistro
     * @param anexoFull
     * @return
     * @throws Exception
     */
    private Document getDocument(IRegistro registro, Long tipoRegistro, AnexoFull anexoFull) throws Exception{

        // Documento
        Document document = new Document();
        document.setIdentificador(null);
        document.setNom(anexoFull.getAnexo().getTitulo());
        document.setEstat(DocumentEstat.DEFINITIU);

        // Metadatos
        DocumentMetadades metadades = new DocumentMetadades();
        metadades.setIdentificador(null);
        metadades.setSerieDocumental(getPropertySerieDocumental());
        metadades.setOrgans(Arrays.asList(registro.getOficina().getCodigo()));
        metadades.setDataCaptura(anexoFull.getAnexo().getFechaCaptura());

        // Metadata Origen
        switch (anexoFull.getAnexo().getOrigenCiudadanoAdmin()){
            case 0:
                metadades.setOrigen(ContingutOrigen.CIUTADA);
            break;
            case 1:
                metadades.setOrigen(ContingutOrigen.ADMINISTRACIO);
            break;
        }

        // Metadata Estado elaboración
        switch (anexoFull.getAnexo().getValidezDocumento().intValue()){

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
        DocumentExtensio extension = DocumentExtensio.toEnum("."+anexoFull.getExtension());
        if (extension != null) {
            metadades.setExtensio(extension);
            metadades.setFormat(Regweb3PluginArxiuUtils.getDocumentFormat(extension));
        }

        // Contingut
        DocumentContingut contingut = new DocumentContingut();

        if( anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {

            // Documento sin firma
            contingut.setArxiuNom(anexoFull.getDocumentoCustody().getName());
            contingut.setContingut(anexoFull.getDocumentoCustody().getData());
            contingut.setTipusMime(anexoFull.getDocMime());
            contingut.setTamany(anexoFull.getDocSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior


        }else if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){

            // Documento y Firma
            contingut.setArxiuNom(anexoFull.getSignatureCustody().getName());
            contingut.setContingut(anexoFull.getSignatureCustody().getData());
            contingut.setTipusMime(anexoFull.getSignMime());
            contingut.setTamany(anexoFull.getSignSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior

        }else if(anexoFull.getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED){

            // Documento
            contingut.setArxiuNom(anexoFull.getDocumentoCustody().getName());
            contingut.setContingut(anexoFull.getDocumentoCustody().getData());
            contingut.setTipusMime(anexoFull.getDocMime());
            contingut.setTamany(anexoFull.getDocSize()); //TODO No estamos seguros de que sea correcto, parece que Arxiu le easgina el tamaño a posterior

            // Firma detached
            Firma firma = getFirma(anexoFull);
            document.setFirmes(Collections.singletonList(firma));

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
     *
     * @param anexoFull
     * @return
     * @throws Exception
     */
    private Firma getFirma(AnexoFull anexoFull) throws Exception{

        Firma firma = new Firma();
        firma.setFitxerNom(anexoFull.getSignatureCustody().getName());
        firma.setContingut(anexoFull.getSignatureCustody().getData());
        firma.setCsvRegulacio("");
        firma.setTamany(anexoFull.getSignatureCustody().getData().length);

        firma.setPerfil(Regweb3PluginArxiuUtils.getFirmaPerfil(anexoFull.getAnexo().getSignProfile()));
        firma.setTipus(Regweb3PluginArxiuUtils.getFirmaTipus(anexoFull.getAnexo()));

        firma.setTipusMime(anexoFull.getSignatureCustody().getMime());

        return firma;
    }

    /**
     *
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    private String getTipoRegistro(Long tipoRegistro) throws Exception{

        if(tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)){
            return "E";
        }else{
            return "S";
        }

    }
}
