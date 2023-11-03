package es.caib.regweb3.persistence.ejb;


import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.DocumentContingut;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.integracion.JustificanteArxiu;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.RegwebUtils;
import es.caib.regweb3.utils.StringUtils;
import org.fundaciobit.genapp.common.i18n.I18NArgumentCode;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.plugins.documentcustody.api.IDocumentCustodyPlugin;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signature.api.FileInfoSignature;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.pluginsib.core.utils.Metadata;
import org.fundaciobit.pluginsib.core.utils.MetadataConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 20/03/18
 */

@Stateless(name = "JustificanteEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class JustificanteBean implements JustificanteLocal {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private PluginLocal pluginEjb;
    @EJB private TipoDocumentalLocal tipoDocumentalEjb;
    @EJB private AnexoLocal anexoEjb;
    @EJB private SignatureServerLocal signatureServerEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private ColaLocal colaEjb;
    @EJB private CarpetaLocal carpetaEjb;
    @Autowired ArxiuCaibUtils arxiuCaibUtils;


    @Override
    public AnexoFull crearJustificante(Entidad entidad, UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException {

        // Comprobamos si ya se ha generado el Justificante
        if (registro.getRegistroDetalle().getTieneJustificante()) {
            throw new I18NException("aviso.justificante.existe");
        }

        if(entidad.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            return crearJustificanteDocumentCustody(entidad, usuarioEntidad, registro, tipoRegistro, idioma, false);

        }else if(entidad.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            return crearJustificanteApiArxiu(entidad, usuarioEntidad, registro, tipoRegistro, idioma);

        }

        return null;
    }

    @Override
    public AnexoFull crearJustificanteWS(Entidad entidad, UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException{

        // Comprobamos si ya se ha generado el Justificante
        if (registro.getRegistroDetalle().getTieneJustificante()) {
            throw new I18NException("aviso.justificante.existe");
        }

        // Si la custodia en diferido está activa generamos el justificante en Filesystem y enviamos a la Cola de custodia
        if(PropiedadGlobalUtil.getCustodiaDiferida(entidad.getId())){
            return crearJustificanteDocumentCustody(entidad, usuarioEntidad, registro, tipoRegistro, idioma, true);
        }else{
            return crearJustificante(entidad, usuarioEntidad, registro, tipoRegistro, idioma);
        }

    }

    /**
     * Crear el Justificante del Registro utilizando el API {@link org.fundaciobit.plugins.documentcustody}
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    private AnexoFull crearJustificanteDocumentCustody(Entidad entidad, UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma, Boolean custodiaDiferida) throws I18NException, I18NValidationException{

        String custodyID = null;
        boolean error = false;
        IDocumentCustodyPlugin documentCustodyPlugin = null;
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Generar Justificante DocumentCustody";
        String numRegFormat = "";
        Locale locale = new Locale(idioma);
        final Long idEntidad = entidad.getId();

        try {

            //log.info("------------------------------------------------------------");
            //log.info("Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado());
            //log.info("");

            // Integración
            peticion.append("usuario: ").append(usuarioEntidad.getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
            peticion.append("idioma: ").append(idioma).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registro.getOficina().getDenominacion()).append(System.getProperty("line.separator"));

            numRegFormat = registro.getNumeroRegistroFormateado();

            // Carregam el plugin de generació del pdf del Justificant
            IJustificantePlugin justificantePlugin = (IJustificantePlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_JUSTIFICANTE);

            // Comprova que existeix el plugin de justificant
            if (justificantePlugin == null) {
                // No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.1"));
            }

            // Carregam el plugin de Custodia del Justificante
            if(custodiaDiferida){ // Se custodiará posteriomente mediante la Cola, ahora se guardará temporalmente en FileSystem
                documentCustodyPlugin = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_FS_JUSTIFICANTE);

            }else{
                documentCustodyPlugin = (IDocumentCustodyPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_CUSTODIA_JUSTIFICANTE);
            }

            // Comprova que existeix el plugin de Custodia del Justificante
            if (documentCustodyPlugin == null) {
                // No s´ha definit cap plugin de Custòdia-Justificant. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.7"));
            }

            peticion.append("clase: ").append(documentCustodyPlugin.getClass().getName()).append(System.getProperty("line.separator"));

            // Cerca el Plugin de Justificant definit a les Propietats Globals
            ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

            // Comprova que existeix el plugin de signatura
            if (signaturePlugin == null) {
                // No s´ha definit cap plugin de Firma. Consulti amb el seu Administrador.
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
            }

            // Crea el anexo del justificante firmado
            Anexo anexo = crearAnexoJustificante(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY, locale, registro.getRegistroDetalle(), entidad);
            AnexoFull anexoFull = new AnexoFull(anexo);

            // Generam la Custòdia per tenir el CSV
            Map<String, Object> custodyParameters = getCustodyParameters(registro, anexoFull, usuarioEntidad);
            custodyID = documentCustodyPlugin.reserveCustodyID(custodyParameters);
            Metadata mcsv = documentCustodyPlugin.getOnlyOneMetadata(custodyID, MetadataConstants.ENI_CSV);
            String csv = null;
            if (mcsv != null) {
                csv = mcsv.getValue();
                anexoFull.getAnexo().setCsv(csv);
                anexoFull.getAnexo().setCustodiado(true);
            }

            peticion.append("custodyID: ").append(custodyID).append(System.getProperty("line.separator"));

            // Obtenemos la url y specialValue
            String url = documentCustodyPlugin.getOriginalFileUrl(custodyID, custodyParameters);
            String specialValue = documentCustodyPlugin.getSpecialValue(custodyID, custodyParameters);

            // Creamos el pdf del Justificante
            byte[] pdfJustificant;
            if (registro instanceof RegistroEntrada) {
                pdfJustificant = justificantePlugin.generarJustificanteEntrada((RegistroEntrada) registro, url, specialValue, csv, idioma, entidad.getSir());
            } else {
                pdfJustificant = justificantePlugin.generarJustificanteSalida((RegistroSalida) registro, url, specialValue, csv, idioma, entidad.getSir());
            }

            // Firma el justificant
            String nombreFichero = RegistroUtils.getNombreFicheroJustificante(idioma, registro.getNumeroRegistroFormateado());
            SignatureCustody sign = signatureServerEjb.signJustificante(pdfJustificant, idioma, idEntidad, peticion, registro.getNumeroRegistroFormateado(), nombreFichero);
            sign.setName(nombreFichero);

            // Cream l'annex justificant
            anexoFull.setSignatureCustody(sign);
            anexoFull.setSignatureFileDelete(false);
            anexoFull = anexoEjb.crearAnexo(anexoFull, usuarioEntidad, entidad, registro.getId(), tipoRegistro, custodyID, false);

            // Cola distribución
            if(custodiaDiferida){ // Si no se va a custodiar inicialmente, lo metemos en la cola de Custodia en diferido
                colaEjb.enviarAColaCustodia(registro, tipoRegistro, usuarioEntidad);
            }

            //log.info("");
            //log.info("Fin Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio.getTime()));
            //log.info("------------------------------------------------------------");

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidad.getId(), numRegFormat);

            return anexoFull;

        } catch (I18NValidationException | I18NException i18nve) {
            error = true;
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), i18nve, null, System.currentTimeMillis() - inicio.getTime(), entidad.getId(), numRegFormat);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            log.error(i18nve.getMessage(), i18nve);
            throw i18nve;
        } catch (Exception e) {
            error = true;
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - inicio.getTime(), entidad.getId(), numRegFormat);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
            throw new I18NException(e, "justificante.error", new I18NArgumentString(e.getMessage()));
        } finally {

            if (error && (documentCustodyPlugin != null && custodyID != null)) {
                try {
                    documentCustodyPlugin.deleteCustody(custodyID);
                } catch (Throwable th) {
                    log.warn("Error esborrant justificant a custodia: " + th.getMessage(), th);
                }
            }
        }
    }

    /**
     * Crear el Justificante del Registro utilizando el API {@link es.caib.plugins.arxiu}
     * @param usuarioEntidad
     * @param registro
     * @param tipoRegistro
     * @param idioma
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     */
    private AnexoFull crearJustificanteApiArxiu(Entidad entidad, UsuarioEntidad usuarioEntidad, IRegistro registro, Long tipoRegistro, String idioma) throws I18NException, I18NValidationException{

        JustificanteArxiu justificanteArxiu = null;
        IArxiuPlugin iArxiuPlugin = null;
        AnexoFull anexoFull = new AnexoFull();
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Generar Justificante Api Arxiu";
        String numRegFormat = registro.getNumeroRegistroFormateado();
        Locale locale = new Locale(idioma);

        try{

            //log.info("------------------------------------------------------------");
            //log.info("Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado());
            //log.info("");

            // Integración
            peticion.append("usuario: ").append(usuarioEntidad.getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registro.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("tipoRegistro: ").append(tipoRegistro).append(System.getProperty("line.separator"));
            peticion.append("idioma: ").append(idioma).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registro.getOficina().getDenominacion()).append(System.getProperty("line.separator"));

            // Cargamos el plugin de Arxiu
            iArxiuPlugin = arxiuCaibUtils.cargarPlugin(entidad.getId());

            // Comprova que existeix el plugin de Arxiu del Justificante
            if (iArxiuPlugin == null) {
                throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.10"));
            }

            peticion.append("clase: ").append(iArxiuPlugin.getClass().getName()).append(System.getProperty("line.separator"));

            // Generar el Pdf y firmarlo
            Firma firma = generarFirmarPdfJustificante(entidad, registro, idioma, peticion);

            // Crea el anexo del justificante firmado
            Anexo anexo = crearAnexoJustificante(RegwebConstantes.PERFIL_CUSTODIA_ARXIU, locale, registro.getRegistroDetalle(), entidad);

            // Hash
            anexo.setHash(RegwebUtils.obtenerHash(firma.getContingut()));

            // Guardamos el Justificante en Arxiu
            justificanteArxiu = arxiuCaibUtils.crearJustificanteArxiuCaib(registro, tipoRegistro, firma);

            // Asociamos el ExpedienteId, CustodyId y Csv al anexo que vamos a crear
            anexo.setExpedienteID(justificanteArxiu.getExpediente().getIdentificador());
            anexo.setCustodiaID(justificanteArxiu.getDocumento().getIdentificador());
            if(justificanteArxiu.getDocumento().getDocumentMetadades() != null){
                anexo.setCsv(justificanteArxiu.getDocumento().getDocumentMetadades().getCsv());
                anexo.setCustodiado(true);
            }

            peticion.append("expedienteID: ").append(anexo.getExpedienteID()).append(System.getProperty("line.separator"));
            peticion.append("documentoID: ").append(anexo.getCustodiaID()).append(System.getProperty("line.separator"));
            peticion.append("csv: ").append(anexo.getCsv()).append(System.getProperty("line.separator"));

            // Guardamos el Anexo
            anexo = anexoEjb.persist(anexo);
            peticion.append("idAnexo: ").append(anexo.getId()).append(System.getProperty("line.separator"));

            // Componemos el AnexoFull
            Document document  = crearDocumentJustificante(firma);
            anexoFull.setDocument(document);
            anexoFull.setAnexo(anexo);

            //log.info("");
            //log.info("Fin Generando Justificante para el registro: " + registro.getNumeroRegistroFormateado() + " en: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio.getTime()));
            //log.info("------------------------------------------------------------");

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidad.getId(), numRegFormat);

            // Avisar a Carpeta Ciutadana de que el Justificante custodiado está disponible
            if(tipoRegistro.equals(REGISTRO_ENTRADA) && StringUtils.isNotEmpty(registro.getRegistroDetalle().getDocumentoInteresado())){
                carpetaEjb.enviarNotificacionCarpeta(registro, entidad.getId());
            }

            return anexoFull;

        }catch (Exception e){

            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_JUSTIFICANTE, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - inicio.getTime(), entidad.getId(), numRegFormat);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            log.error(e.getMessage(), e);
            throw new I18NException(e, "justificante.error", new I18NArgumentString(e.getMessage()));
        }
    }

    /**
     *
     * @param entidad
     * @param registro
     * @param idioma
     * @param peticion
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    private Firma generarFirmarPdfJustificante(Entidad entidad, IRegistro registro, String idioma, StringBuilder peticion) throws I18NException {

        // Carregam el plugin del Justificant per generar el pdf
        IJustificantePlugin justificantePlugin = (IJustificantePlugin) pluginEjb.getPlugin(entidad.getId(), RegwebConstantes.PLUGIN_JUSTIFICANTE);

        // Comprova que existeix el plugin de justificant
        if (justificantePlugin == null) {
            // No s´ha definit cap plugin de Justificant. Consulti amb el seu Administrador.
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.1"));
        }

        // Generamos el pdf del Justificante mediante el Plugin
        byte[] pdfJustificant;
        if (registro instanceof RegistroEntrada) {
            pdfJustificant = justificantePlugin.generarJustificanteEntrada((RegistroEntrada) registro, "", "", "", idioma, entidad.getSir());
        } else {
            pdfJustificant = justificantePlugin.generarJustificanteSalida((RegistroSalida) registro, "", "", "", idioma, entidad.getSir());
        }

        // Nombre fichero justificante
        String nombreFichero = RegistroUtils.getNombreFicheroJustificante(idioma, registro.getNumeroRegistroFormateado());

        // Firma el justificant
        return signatureServerEjb.signJustificanteApiArxiu(pdfJustificant, idioma, entidad.getId(), peticion, registro.getNumeroRegistroFormateado(), nombreFichero);

    }

    /**
     * Genera una instancia de la clase {@link es.caib.plugins.arxiu.api.Document} que representa a un Justificante
     * @param firma
     * @return
     */
    private Document crearDocumentJustificante(Firma firma){

        Document document = new Document();
        DocumentContingut dc = new DocumentContingut();
        document.setNom(firma.getFitxerNom());
        dc.setContingut(firma.getContingut());
        dc.setTamany(firma.getContingut().length);
        dc.setTipusMime(FileInfoSignature.PDF_MIME_TYPE);
        document.setContingut(dc);

        return document;
    }

    /**
     * @param registro
     * @param anexoFull
     * @param usuarioEntidad
     * @return
     */
    private Map<String, Object> getCustodyParameters(IRegistro registro, AnexoFull anexoFull, UsuarioEntidad usuarioEntidad) {


        Map<String, Object> custodyParameters = new HashMap<String, Object>();

        custodyParameters.put("registro", registro);
        custodyParameters.put("anexo", anexoFull.getAnexo());
        custodyParameters.put("anexoFull", anexoFull);
        custodyParameters.put("usuarioEntidad", usuarioEntidad);

        Interesado interesado = registro.getRegistroDetalle().getInteresados().get(0);
        custodyParameters.put("ciudadano_nombre", interesado.getNombreCompleto());

        String documentAdministratiu = interesado.getDocumento();
        if (documentAdministratiu == null) {
            documentAdministratiu = interesado.getCodigoDir3();
        }

        custodyParameters.put("ciudadano_idadministrativo", documentAdministratiu);

        return custodyParameters;
    }

    /**
     * Crea el objeto de Anexo para un Justificante
     * @param perfilCustodia
     * @param locale
     * @param registroDetalle
     * @param entidad
     * @return
     */
    private Anexo crearAnexoJustificante(Long perfilCustodia, Locale locale, RegistroDetalle registroDetalle, Entidad entidad) throws I18NException {

        Anexo anexo = new Anexo(perfilCustodia);

        String tituloAnexo = I18NLogicUtils.tradueix(locale, "justificante.anexo.titulo");
        String observaciones = I18NLogicUtils.tradueix(locale, "justificante.anexo.observaciones");

        anexo.setEntidad(entidad);
        anexo.setTitulo(tituloAnexo);
        anexo.setValidezDocumento(RegwebConstantes.TIPOVALIDEZDOCUMENTO_ORIGINAL);
        anexo.setTipoDocumental(tipoDocumentalEjb.findByCodigoEntidad("TD99", entidad.getId()));
        anexo.setTipoDocumento(RegwebConstantes.TIPO_DOCUMENTO_DOC_ADJUNTO);
        anexo.setOrigenCiudadanoAdmin(RegwebConstantes.ANEXO_ORIGEN_ADMINISTRACION);
        anexo.setObservaciones(observaciones);
        anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
        anexo.setJustificante(true);
        anexo.setRegistroDetalle(registroDetalle);
        anexo.setFechaCaptura(new Date());
        anexo.setFirmaValida(false);
        anexo.setSignType("PAdES");
        anexo.setSignFormat("implicit_enveloped/attached");
        anexo.setSignProfile("AdES-EPES");
        anexo.setEstadoFirma(RegwebConstantes.ANEXO_FIRMA_VALIDA);
        anexo.setFechaValidacion(new Date());

        return anexo;
    }
}
