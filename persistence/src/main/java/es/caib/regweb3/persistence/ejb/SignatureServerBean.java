package es.caib.regweb3.persistence.ejb;

import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.FirmaPerfil;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.fundaciobit.genapp.common.i18n.*;
import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signature.api.*;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.validatesignature.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.xml.ws.WebServiceException;
import java.io.File;
import java.util.*;

/**
 * Created by jpernia on 04/04/2017.
 *
 * @author anadal (08/05/2017) Mètode checkDocumentAndSignature
 */
@Stateless(name = "SignatureServerEJB")
@RolesAllowed({"RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class SignatureServerBean implements SignatureServerLocal, ValidateSignatureConstants {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private PluginLocal pluginEjb;
    @EJB private IntegracionLocal integracionEjb;

    /**
     * Método que genera la Firma de un File para una Entidad en concreto
     *
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @return
     * @throws I18NException
     */
    @Override
    public SignatureCustody signJustificante(byte[] pdfsource, String languageUI,
                                             Long idEntidadActiva, StringBuilder peticion, String numeroRegistro, String fileName) throws I18NException {

        // Cerca el Plugin de Justificant definit a les Propietats Globals
        ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidadActiva, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

        // Comprova que existegix el plugin de justificant
        if (signaturePlugin == null) {
            // No s´ha definit cap plugin de Firma. Consulti amb el seu Administrador.
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
        }

        String reason = "FIRMA_JUSTIFICANT"; // Hem de canviar raó justificant????

        final String signType = FileInfoSignature.SIGN_TYPE_PADES;
        final int signMode = FileInfoSignature.SIGN_MODE_EXPLICIT;
        final boolean epes = true;

        // Firmamos el Justificante
        byte[] firma = signFile(fileName, pdfsource, FileInfoSignature.PDF_MIME_TYPE, signType, signMode, epes, signaturePlugin, new Locale(languageUI),
                reason, idEntidadActiva, new Date(), peticion, numeroRegistro);

        // Creamos el SignatureCustody
        return crearSignatureCustody(signType, signMode, firma);

    }

    /**
     * Método que genera la Firma de un Justificante
     *
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @return
     * @throws I18NException
     */
    @Override
    public Firma signJustificanteApiArxiu(byte[] pdfsource, String languageUI,
                                          Long idEntidadActiva, StringBuilder peticion, String numeroRegistro, String fileName) throws I18NException {

        // Cerca el Plugin de Justificant definit a les Propietats Globals
        ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidadActiva, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

        // Comprova que existegix el plugin de justificant
        if (signaturePlugin == null) {
            // No s´ha definit cap plugin de Firma. Consulti amb el seu Administrador.
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
        }

        String reason = "FIRMA_JUSTIFICANT"; // Hem de canviar raó justificant????

        final String signType = FileInfoSignature.SIGN_TYPE_PADES;
        final int signMode = FileInfoSignature.SIGN_MODE_EXPLICIT;
        final boolean epes = true;

        // Firmamos el Justificante
        byte[] firmaJustificante = signFile(fileName, pdfsource, FileInfoSignature.PDF_MIME_TYPE, signType, signMode, epes, signaturePlugin, new Locale(languageUI),
                reason, idEntidadActiva, new Date(), peticion, numeroRegistro);

        // Creamos la Firma
        Firma firma = new Firma();
        firma.setFitxerNom(fileName);
        firma.setContingut(firmaJustificante);
        firma.setTamany(firmaJustificante.length);
        firma.setPerfil(FirmaPerfil.EPES);
        firma.setTipus(FirmaTipus.PADES);
        firma.setTipusMime(FileInfoSignature.PDF_MIME_TYPE);
        firma.setCsvRegulacio("");

        return firma;

    }


    /**
     * @author anadal
     * <p>
     * force = true  => Es crida des d'enviar a SIR
     * force = false => Es crida des d'edició d'annexes
     * <p>
     * A continuació es mostra el flux d'accions quan es produeixen errors en les cridades
     * a Firmar i Validar
     * <p>
     * =========== NO SIR =======
     * (1) No Es firma: OK
     * (2) Es Firma: OK <<PENDENT. EN EL FUTUR s'HAURÀ DE VALIDAR>>
     * <p>
     * =========== SIR(boolean force) =======
     * <p>
     * (1) Document PLA
     * (1.1) Firma CADES-EPES-Detached OK => OK
     * (1.2) Firma CADES-EPES-Detached Falla
     * (1.2.1) Si force=true => Llançar Excepció
     * (1.2.2) Si force=false => Retornar msg d'avís i continuar
     * (2) És una Firma ATTACHED
     * (2.1) Firma CADES-EPES-Detached OK => OK
     * (2.2) Firma CADES-EPES-Detached Falla
     * (2.2.1) Si force=true => Llançar Excepció
     * (2.2.2) Si force=false => Retornar msg d'avís i continuar
     * (3) És una Firma DETACHED: Eliminam la firma detached i tornam a firmar
     * (3.1) Firma CADES-EPES-Detached OK => OK
     * (3.2) Firma CADES-EPES-Detached Falla
     * (3.2.1) Si force=true => Llançar Excepció
     * (3.2.2) Si force=false => Retornar msg d'avís i continuar
     */
    @Override
    public I18NTranslation checkDocumentAndSignature(AnexoFull input, long idEntidad,
                                                     boolean sir, Locale locale, boolean force, String numeroRegistro) throws I18NException {

        boolean error = false;

        try {

            SignatureCustody sign = input.getSignatureCustody();
            DocumentCustody doc = input.getDocumentoCustody();

            if (sign == null && doc == null) {
                throw new I18NException("error.checkanexosir.nifirmanidoc");
            }

            if (!sir) {
                // TODO NOTA: <<PENDENT. EN EL FUTUR s'HAURÀ DE VALIDAR QUAN SIGUI UNA
                // FIRMA >>
                return null; // OK
            }

            // A PARTIR D'AQUI TOT ES SIR

            if ((doc != null && sign == null)) { // Caso documento sin firmar

                try {

                    ISignatureServerPlugin signaturePlugin = getInstanceSignatureServerPlugin(idEntidad);
                    // És un document PLA: Firmar emprant CAdES-EPES

                    firmaCAdESEPESDetached(input, doc, locale, signaturePlugin, idEntidad, numeroRegistro);

                    input.getSignatureCustody().setName("signature_CAdES_EPES.csig");

                    return null; // Cas 1.1 OK
                } catch (I18NException e) {
                    // Cas 1.2 Error
                    error = true;

                    String msg = I18NLogicUtils.getMessage(e, locale);

                    I18NException i18ne = new I18NException("error.checkanexosir.refirmant", msg);
                    error = true;
                    return processError(i18ne, force);
                }
            }

            // Es Atached ==> Llavors es refirma CADES Detached
            // Es Detached => Llavors s'elimina la firma i es firma CADES Detached
            try {
                ISignatureServerPlugin signaturePlugin = getInstanceSignatureServerPlugin(idEntidad);

                if (input.getDocumentoCustody() == null) {
                    // CheckFirma amb document attached
                    signAttachedSignature(input, locale, signaturePlugin, idEntidad, numeroRegistro);
                } else {
                    // CheckFirma amb document detached
                    signDetachedSignature(input, locale, signaturePlugin, idEntidad, numeroRegistro);
                }

                // CAS (2.1.2.1) ReFirma OK => OK
                return null;
            } catch (I18NException e) {

                // CAS (2.1.2.2) ReFirma Error => Anar a punts (1.2.1) o (1.2.2)

                String msg = I18NLogicUtils.getMessage(e, locale);

                I18NException i18ne = new I18NException("error.checkanexosir.refirmant", msg);
                error = true;
                return processError(i18ne, force);

            }

        } catch (I18NException i18ne) {
            error = true;
            log.error("Error Capturat: " + I18NCommonUtils.getMessage(i18ne, locale), i18ne);
            throw i18ne;
        } finally {
            if (!error) {
                if (input.getSignatureCustody() == null) {
                    input.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA);
                } else {
                    if (input.getDocumentoCustody() == null) {
                        input.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED);
                    } else {
                        input.getAnexo().setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED);
                    }
                }
            }
        }

    }


    /**
     * Valida un document mitjancant el plugin de validar Firma
     *
     * @param input
     * @param idEntidad
     * @param locale
     * @param force
     * @return
     * @throws I18NException
     */
    public I18NTranslation checkDocument(AnexoFull input, long idEntidad, Locale locale, boolean force) throws I18NException {

        SignatureCustody sign = input.getSignatureCustody();
        DocumentCustody doc = input.getDocumentoCustody();

        if (sign == null && doc == null) {
            throw new I18NException("error.checkanexosir.nifirmanidoc");
        }

        if (sign != null) {
            // VALIDAR FIRMA
            ValidateSignatureResponse resp = new ValidateSignatureResponse();
            try {
                resp = callToValidaFirma(locale, sign, doc, idEntidad);
            } catch (I18NException i18ne) {
                input.getAnexo().setEstadoFirma(RegwebConstantes.ANEXO_FIRMA_ERROR);
                if (i18ne.getCause() != null) {
                    input.getAnexo().setMotivoNoValidacion(StringUtils.recortarCadena(i18ne.getCause().toString(),255));
                }
                return processError(i18ne, force);
            } catch (WebServiceException we) {
                input.getAnexo().setEstadoFirma(RegwebConstantes.ANEXO_FIRMA_NOINFO);
                if (we.getCause() != null) {
                    input.getAnexo().setMotivoNoValidacion(StringUtils.recortarCadena(we.getCause().toString(),255));
                }
                log.info("WebServiceException CheckDocument");
                log.error(we.getMessage());
            }

            final String perfil = resp.getSignProfile();
            final String tipo = resp.getSignType();
            final String formato = resp.getSignFormat();

            //Guardamos el resultado de la validación de la firma
            Anexo anexo = input.getAnexo();
            anexo.setSignType(tipo);
            anexo.setSignFormat(formato);
            anexo.setSignProfile(perfil);
            anexo.setEstadoFirma(resp.getValidationStatus().getStatus());
            anexo.setFechaValidacion(new Date());
            anexo.setFirmaValida(resp.getValidationStatus().getStatus() == RegwebConstantes.ANEXO_FIRMA_VALIDA);
            if (resp.getValidationStatus().getStatus() == RegwebConstantes.ANEXO_FIRMA_INVALIDA) {//Indica que no es valida la firma
                anexo.setMotivoNoValidacion(StringUtils.recortarCadena(resp.getValidationStatus().getErrorMsg(), 255));
            } else if (resp.getValidationStatus().getStatus() == RegwebConstantes.ANEXO_FIRMA_ERROR) {//Indica que ha habido una excepción en el proceso de validación
                if (resp.getValidationStatus().getErrorException() != null) {
                    anexo.setMotivoNoValidacion(StringUtils.recortarCadena(resp.getValidationStatus().getErrorException().getCause().toString(), 255));
                }
            }

        }
        return null;
    }


    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    protected ISignatureServerPlugin getInstanceSignatureServerPlugin(long idEntidad)
            throws I18NException {
        ISignatureServerPlugin signaturePlugin;
        signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidad,
                RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

        if (signaturePlugin == null) {
            // El plugin de Firma en servidor no s'ha definit. Consulti amb l'Administrador
            throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.4"));
        }
        return signaturePlugin;
    }


    /**
     * @param i18ne
     * @param force
     * @return
     * @throws I18NException
     */
    protected I18NTranslation processError(I18NException i18ne, boolean force) throws I18NException {

        if (force) {
            throw i18ne;
        }

        return i18ne.getTraduccio();

    }


    protected ValidateSignatureResponse callToValidaFirma(Locale locale, SignatureCustody sign,
                                                          DocumentCustody doc, Long idEntidad) throws I18NException {
        ValidateSignatureResponse resp;

        //long start = System.currentTimeMillis();

        // TODO CACHE DE PLUGIN!!!!!
        IValidateSignaturePlugin validatePlugin;
        validatePlugin = (IValidateSignaturePlugin) pluginEjb.getPlugin(idEntidad,
                RegwebConstantes.PLUGIN_VALIDACION_FIRMAS);

        if (validatePlugin == null) {// El plugin de Validació de Firmes no s'ha definit.

            // Creamos una respuesta de "No validación"
            resp = new ValidateSignatureResponse();
            ValidationStatus validationStatus = new ValidationStatus();
            validationStatus.setStatus(-2);
            validationStatus.setErrorMsg(I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "error.plugin.validasign.noDefinido"));
            resp.setValidationStatus(validationStatus);

            return resp;
            //throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.8"));
        }

        // Verificar que ofereix servei de informació de firmes
        SignatureRequestedInformation sri = validatePlugin.getSupportedSignatureRequestedInformation();

        if (!Boolean.TRUE.equals(sri.getReturnSignatureTypeFormatProfile())) {
            // El plugin de Validació/Informació de Firmes no proveeix informació de firmes.
            throw new I18NException("error.plugin.validasign.noinfo");
        }

        sri = new SignatureRequestedInformation();
        sri.setReturnSignatureTypeFormatProfile(true);
        sri.setReturnCertificateInfo(false);
        sri.setReturnCertificates(false);
        sri.setReturnTimeStampInfo(false);
        sri.setReturnValidationChecks(false);
        sri.setValidateCertificateRevocation(false);

        ValidateSignatureRequest validationRequest = new ValidateSignatureRequest();
        validationRequest.setLanguage(locale.getLanguage());
        validationRequest.setSignatureData(sign.getData());
        validationRequest.setSignatureRequestedInformation(sri);
        if (doc != null) {
            validationRequest.setSignedDocumentData(doc.getData());
        }

        try {
            resp = validatePlugin.validateSignature(validationRequest);
        } catch (WebServiceException we) {
            throw we;
        } catch (Exception e) {
            e.printStackTrace();
            throw new I18NException(e, "error.checkanexosir.validantfirma",
                    new I18NArgumentString(e.getMessage()));

        }
        //log.info("Total validacion Firma: " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - start));
        return resp;
    }


    /**
     * @param input
     * @param locale
     * @param signaturePlugin
     * @param idEntidad
     * @throws I18NException
     */
    protected void signAttachedSignature(AnexoFull input, Locale locale,
                                         ISignatureServerPlugin signaturePlugin, Long idEntidad, String numeroRegistro) throws I18NException {


        if (log.isDebugEnabled()) {
            log.debug("ENTRA A checkAttachedSignature( ); ");
        }

        // Altre tipus de Firma: ODT o OOXML

        // (1) Movem la firma al document i buidam la firma
        DocumentCustody doc = new DocumentCustody();

        SignatureCustody sign = input.getSignatureCustody();
        doc.setData(sign.getData());
        doc.setLength(sign.getLength());
        doc.setMime(sign.getMime());
        doc.setName(sign.getName());

        input.setSignatureCustody(null);
        input.setDocumentoCustody(doc);

        // (2) Enviam a firmar
        firmaCAdESEPESDetached(input, doc, locale, signaturePlugin, idEntidad, numeroRegistro);

        //input.getSignatureCustody().setName(doc.getName().replace('.', '_') + "_EPES.csig");
        // El nombre de la firma es el mismo que el fichero pero cambiando su extension a .csig
        input.getSignatureCustody().setName(doc.getName().replace(".pdf", ".csig"));

    }

    /**
     * @param input
     * @param locale
     * @param signaturePlugin
     * @param idEntidad
     * @throws I18NException
     */
    protected void signDetachedSignature(AnexoFull input,
                                         Locale locale, ISignatureServerPlugin signaturePlugin, Long idEntidad, String numeroRegistro)
            throws I18NException {

        // ELIMINAM LA FIRMA i FIRMAM AMB CADES-EPES
        // (1) Enviam a firmar
        firmaCAdESEPESDetached(input, input.getDocumentoCustody(), locale, signaturePlugin, idEntidad, numeroRegistro);

        input.getSignatureCustody().setName("signature_CAdES_EPES.csig");

    }

    /**
     * @param input
     * @param docToSign
     * @param locale
     * @param signaturePlugin
     * @param idEntidad
     * @throws I18NException
     */
    protected void firmaCAdESEPESDetached(AnexoFull input, AnnexCustody docToSign, Locale locale,
                                          ISignatureServerPlugin signaturePlugin, Long idEntidad, String numeroRegistro) throws I18NException {

        final String signType = FileInfoSignature.SIGN_TYPE_CADES;
        final int signMode = FileInfoSignature.SIGN_MODE_EXPLICIT;
        final boolean epes = true;
        final String reason = "Convertir Document/Firma a perfil EPES per enviar a SIR";

        // Firmamos el fichero
        byte[] firma = signFile(docToSign.getName(), docToSign.getData(), docToSign.getMime(), signType, signMode, epes,
                signaturePlugin, locale, reason, idEntidad, new Date(), new StringBuilder(), numeroRegistro);

        // Creamos el SignatureCustody
        SignatureCustody sc = crearSignatureCustody(signType, signMode, firma);

        // Ficar dins Anexo tipo, formato i perfil
        Anexo anexo = input.getAnexo();
        anexo.setSignType(SIGNTYPE_CAdES);
        anexo.setSignFormat(SIGNFORMAT_EXPLICIT_DETACHED);
        anexo.setSignProfile(SIGNPROFILE_EPES);

        input.setSignatureCustody(sc);

    }

    /**
     * @param input     Parametre d'entrada sortida. Si tot al final aquest
     *                  objecte contindrà la signatura.
     * @param idEntidad
     * @param locale
     * @throws I18NException
     */
    @Override
    public void firmaPAdESEPES(AnexoFull input, long idEntidad, Locale locale, String numeroRegistro) throws I18NException {

        DocumentCustody doc = input.getDocumentoCustody();
        ISignatureServerPlugin signaturePlugin = getInstanceSignatureServerPlugin(idEntidad);

        AnnexCustody documentToSign = input.getDocumentoCustody();

        firmaPAdESEPES(input, documentToSign, locale, signaturePlugin, idEntidad, numeroRegistro);

    }

    /**
     * Método que añade una firma detached a todos los anexos que se le pasan en la variable anexosEnviarASir
     * Es obligatorio
     *
     * @param anexosEnviarASir
     * @param idEntidad
     * @param locale
     * @param force
     * @throws I18NException
     */
    public List<AnexoFull> firmarAnexosEnvioSir(List<AnexoFull> anexosEnviarASir, Long idEntidad, Locale locale, boolean force, String numeroRegistro) throws I18NException {

        List<AnexoFull> anexosADevolver = new ArrayList<AnexoFull>();

        for (AnexoFull anexoFull : anexosEnviarASir) {

            AnexoFull anexo = new AnexoFull(anexoFull);

            // Si el anexo es un Justificante creado con el ApiArxiu, lo transformamos
            if (anexo.getAnexo().getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)) {
                anexo.arxiuDocumentToCustody();
            }

            DocumentCustody dc = anexo.getDocumentoCustody();
            SignatureCustody sc = anexo.getSignatureCustody();

            if ((dc != null && sc == null) || (dc == null && sc != null)) {// Document pla o attached

                checkDocumentAndSignature(anexo, idEntidad, true, locale, force, numeroRegistro);
                anexosADevolver.add(anexo);

            } else { //Detached

                // Firmamos la Firma
                AnexoFull firma = new AnexoFull(anexo);

                firma.setDocumentoCustody(null);
                checkDocumentAndSignature(firma, idEntidad, true, locale, force, numeroRegistro);

                anexosADevolver.add(firma);

                // Firmamos el documento
                anexo.setSignatureCustody(null);
                checkDocumentAndSignature(anexo, idEntidad, true, locale, force, numeroRegistro);

                anexosADevolver.add(anexo);

            }
        }

        return anexosADevolver;

    }


    /**
     * @param input
     * @param documentToSign
     * @param locale
     * @param signaturePlugin
     * @param idEntidad
     * @throws I18NException
     */
    protected void firmaPAdESEPES(AnexoFull input, AnnexCustody documentToSign, Locale locale,
                                  ISignatureServerPlugin signaturePlugin, Long idEntidad, String numeroRegistro) throws I18NException {

        final String signType = FileInfoSignature.SIGN_TYPE_PADES;
        final int signMode = FileInfoSignature.SIGN_MODE_IMPLICIT;
        final boolean epes = true;
        final String reason = "Convertir Document/Firma a perfil EPES per enviar a SIR";

        // Firmamos el fichero
        byte[] firma = signFile(documentToSign.getName(), documentToSign.getData(), documentToSign.getMime(), signType, signMode, epes,
                signaturePlugin, locale, reason, idEntidad, new Date(), new StringBuilder(), numeroRegistro);

        // Creamos el SignatureCustody
        SignatureCustody sc = crearSignatureCustody(signType, signMode, firma);

        // Ficar dins Anexo tipo, formato i perfil
        Anexo anexo = input.getAnexo();
        anexo.setSignType(SIGNTYPE_PAdES);
        anexo.setSignFormat(SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED);
        anexo.setSignProfile(SIGNPROFILE_EPES);

        input.setSignatureCustody(sc);

        input.setDocumentoCustody(null);

        System.gc();

    }


    /**
     * @param fileName
     * @param data
     * @param mimeType
     * @param signType
     * @param signMode
     * @param epes
     * @param plugin
     * @param locale
     * @param reason
     * @param idEntidadActiva
     * @return
     * @throws I18NException
     */
    protected byte[] signFile(String fileName, byte[] data, String mimeType, String signType,
                              int signMode, boolean epes, ISignatureServerPlugin plugin, Locale locale, String reason, Long idEntidadActiva, Date inicio, StringBuilder peticion, String numeroRegistro)
            throws I18NException {

        File source = null;
        File destination = null;
        final String username = CONFIG_USERNAME;
        

        // Integración
        peticion.append("clase firma: ").append(plugin.getClass().getName()).append(System.getProperty("line.separator"));
        peticion.append("signType: ").append(signType).append(System.getProperty("line.separator"));
        peticion.append("signMode: ").append(signMode).append(System.getProperty("line.separator"));

        try {
            // String pdfsource, String mime, String pdfdest,
            String filtreCertificats = "";
            String administrationID = null; // No te sentit en API Firma En Servidor
            PolicyInfoSignature policyInfoSignature = null;
            if (epes) {
                policyInfoSignature = new PolicyInfoSignature();
                policyInfoSignature.setPolicyIdentifier("urn:oid:2.16.724.1.3.1.1.2.1.9");
                policyInfoSignature.setPolicyIdentifierHash("G7roucf600+f03r/o0bAOQ6WAs0=");
                policyInfoSignature.setPolicyIdentifierHashAlgorithm("SHA1");
                policyInfoSignature.setPolicyUrlDocument("https://sede.060.gob.es/politica_de_firma_anexo_1.pdf");
            }

            CommonInfoSignature commonInfoSignature = new CommonInfoSignature(locale.getLanguage(),
                    filtreCertificats, username, administrationID);

            final String signID = String.valueOf(System.currentTimeMillis());

            final String name = fileName;

            final String location = null; // "Palma";
            final String signerEmail = null; // "anadal@ibit.org";
            final int signNumber = 1;

            String signAlgorithm = FileInfoSignature.SIGN_ALGORITHM_SHA1;

            PdfVisibleSignature pdfInfoSignature = null;
            final ITimeStampGenerator timeStampGenerator = null;

            // Valors per defcte
            final SignaturesTableHeader signaturesTableHeader = null;
            final SecureVerificationCodeStampInfo csvStampInfo = null;

            source = File.createTempFile("regweb_signfile", "document");
            final boolean userRequiresTimeStamp = false;
            final int signaturesTableLocation = FileInfoSignature.SIGNATURESTABLELOCATION_WITHOUT;
            FileUtils.writeByteArrayToFile(source, data);

            File previusSignatureDetachedFile = null;
            int signOperation = FileInfoSignature.SIGN_OPERATION_SIGN;
            String expedientCode = null;
            String expedientName = null;
            String expedientUrl = null;
            String procedureCode = null;
            String procedureName = null;
            FileInfoSignature fileInfo = new FileInfoSignature(signID, source, previusSignatureDetachedFile,
                    mimeType, name, reason, location, signerEmail, signNumber, locale.getLanguage(),
                    signOperation, signType, signAlgorithm, signMode, signaturesTableLocation, signaturesTableHeader,
                    pdfInfoSignature, csvStampInfo, userRequiresTimeStamp, timeStampGenerator, policyInfoSignature,
                    expedientCode, expedientName, expedientUrl, procedureCode, procedureName);


            final String signaturesSetID = String.valueOf(System.currentTimeMillis());
            SignaturesSet signaturesSet = new SignaturesSet(signaturesSetID, commonInfoSignature,
                    new FileInfoSignature[]{fileInfo});

            // Check si passa filtre
            Map<String, Object> parameters = new HashMap<String, Object>();
            String error = plugin.filter(signaturesSet, parameters);
            if (error != null) {
                // "El plugin no suporta aquest tipus de firma/mode (" + signType + ", " + signMode + ")
                // o s'ha produit un error greou durant la firma"
                throw new I18NException("error.plugin.firma.nosuportat", signType, String.valueOf(signMode), error);
            }

            final String timestampUrlBase = null;
            signaturesSet = plugin.signDocuments(signaturesSet, timestampUrlBase, parameters);
            StatusSignaturesSet sss = signaturesSet.getStatusSignaturesSet();

            if (sss.getStatus() != StatusSignaturesSet.STATUS_FINAL_OK) {
                throw new I18NException(sss.getErrorException(), "error.realitzantfirma",
                        new I18NArgumentString(sss.getErrorMsg()));
            } else {
                FileInfoSignature fis = signaturesSet.getFileInfoSignatureArray()[0];
                StatusSignature status = fis.getStatusSignature();
                if (status.getStatus() != StatusSignaturesSet.STATUS_FINAL_OK) {
                    throw new I18NException(status.getErrorException(), "error.realitzantfirma",
                            new I18NArgumentString(status.getErrorMsg()));
                } else {
                    destination = status.getSignedData();

                    byte[] firma = FileUtils.readFileToByteArray(destination);

                    // Integracion
                    integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_FIRMA, reason, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), idEntidadActiva, numeroRegistro);

                    return firma;

                }
            }

        } catch (I18NException i18ne) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_FIRMA, reason, peticion.toString(), i18ne, null, System.currentTimeMillis() - inicio.getTime(), idEntidadActiva, numeroRegistro);
            } catch (Exception e) {
                e.printStackTrace();
            }
            throw i18ne;
        } catch (Exception e) {
            try {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_FIRMA, reason, peticion.toString(), e, null, System.currentTimeMillis() - inicio.getTime(), idEntidadActiva, numeroRegistro);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new I18NException(e, "error.realitzantfirma", new I18NArgumentString(e.getMessage()));
        } finally {
            if (source != null) {
                if (!source.delete()) {
                    source.deleteOnExit();
                }
            }
            if (destination != null) {
                if (!destination.delete()) {
                    destination.deleteOnExit();
                }
            }

        }
    }

    /**
     * Crea un {@link org.fundaciobit.plugins.documentcustody.api.SignatureCustody} a partir de los parámetros
     *
     * @param signType
     * @param signMode
     * @param firma
     * @return
     * @throws I18NException
     */
    private SignatureCustody crearSignatureCustody(String signType, int signMode, byte[] firma) throws I18NException {

        String mime;
        String custSignType;
        Boolean attachedDocument;
        String signFileName;
        if (FileInfoSignature.SIGN_TYPE_PADES.equals(signType)) {
            mime = FileInfoSignature.PDF_MIME_TYPE;
            custSignType = SignatureCustody.PADES_SIGNATURE;
            attachedDocument = null;
            signFileName = "signed.pdf";
        } else if (FileInfoSignature.SIGN_TYPE_XADES.equals(signType)) {
            mime = "application/xml";
            custSignType = SignatureCustody.XADES_SIGNATURE;
            attachedDocument = (signMode == FileInfoSignature.SIGN_MODE_EXPLICIT);
            signFileName = "signature.xml";
        } else if (FileInfoSignature.SIGN_TYPE_CADES.equals(signType)) {
            custSignType = SignatureCustody.CADES_SIGNATURE;
            mime = "application/octet-stream";
            attachedDocument = (signMode == FileInfoSignature.SIGN_MODE_EXPLICIT);
            signFileName = "signature.csig";
        } else {
            throw new I18NException(new Exception(), "error.realitzantfirma",
                    new I18NArgumentString("Tipus de firma desconeguda (" + signType + ")"));
        }

        SignatureCustody sc = new SignatureCustody();
        sc.setData(firma);
        sc.setLength(sc.getData().length);
        sc.setName(signFileName);
        sc.setMime(mime);
        sc.setSignatureType(custSignType);
        sc.setAttachedDocument(attachedDocument);

        return sc;
    }

    public boolean is_pdf(byte[] data) {

        if (data != null && data.length > 4 &&
                data[0] == 0x25 && // %
                data[1] == 0x50 && // P
                data[2] == 0x44 && // D
                data[3] == 0x46 && // F
                data[4] == 0x2D) { // -


            byte[] endOfPdf = new byte[10];
            System.arraycopy(data, data.length - 10, endOfPdf, 0, 10);

            String str = new String(endOfPdf);

            return str.contains("%%EOF");

        }
        return false;
    }

}
