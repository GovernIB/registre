package es.caib.regweb3.persistence.utils;

import java.io.File;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NArgumentString;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signature.api.CommonInfoSignature;
import org.fundaciobit.plugins.signature.api.FileInfoSignature;
import org.fundaciobit.plugins.signature.api.ITimeStampGenerator;
import org.fundaciobit.plugins.signature.api.PdfVisibleSignature;
import org.fundaciobit.plugins.signature.api.PolicyInfoSignature;
import org.fundaciobit.plugins.signature.api.SecureVerificationCodeStampInfo;
import org.fundaciobit.plugins.signature.api.SignaturesSet;
import org.fundaciobit.plugins.signature.api.SignaturesTableHeader;
import org.fundaciobit.plugins.signature.api.StatusSignature;
import org.fundaciobit.plugins.signature.api.StatusSignaturesSet;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;
import org.fundaciobit.plugins.validatesignature.api.SignatureRequestedInformation;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureRequest;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureResponse;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.utils.RegwebConstantes;

/**
 * 
 * @author anadal (08/05/2017)
 *
 */
public class AnexoFirmaUtils implements ValidateSignatureConstants {

  protected static final Logger log = Logger.getLogger(AnexoFirmaUtils.class);
  
  /**
   * Per  
   * @return
   */
  public static AnexoFull checkDocumentAndSignature(PluginLocal pluginEjb, AnexoFull input, long idEntidad,
      boolean sir, Locale locale) throws I18NException {
    
    try {
    
    SignatureCustody sign = input.getSignatureCustody();
    DocumentCustody doc = input.getDocumentoCustody();
    
    if (sign == null && doc == null) {
        // TODO XYZ ZZZ Traduir emprant lang
        throw new I18NException("No s'ha passat cap document ni firma.");
    }

    if (!sir && sign == null) {
      return input;
    }
    
    
    if (sir && sign == null) {
      
      ISignatureServerPlugin signaturePlugin;
      signaturePlugin = (ISignatureServerPlugin)pluginEjb.getPlugin(idEntidad,
          RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);
      
      
      // És un document PLA: Firmar emprant CAdES-EPES
      firmaCAdESEPES(input, locale, signaturePlugin);

      return input;
   }
    
    
    
    // TODO CACHE !!!!!
    IValidateSignaturePlugin validatePlugin;
    validatePlugin = (IValidateSignaturePlugin)pluginEjb.getPlugin(null,
         RegwebConstantes.PLUGIN_VALIDACION_FIRMAS);
    if (validatePlugin == null) {
      // TODO XYZ ZZZ traduir
      throw new I18NException("error.desconegut",
          "El plugin de Validació de Firmes no s'ha definit. Consulti amb l'Administrador de sistemes.");
    }
    
    // Verificar que ofereix servei de informació de firmes
    SignatureRequestedInformation sri = validatePlugin.getSupportedSignatureRequestedInformation();
    if (!Boolean.TRUE.equals(sri.getReturnSignatureTypeFormatProfile())) {
      // TODO XYZ ZZZ traduir
      throw new I18NException("error.desconegut",
          "El plugin de Validació de Firmes no proveeix informació de firmes.");
    }

    sri = new SignatureRequestedInformation();
    sri.setReturnSignatureTypeFormatProfile(true);
    
    
    ValidateSignatureRequest validationRequest = new ValidateSignatureRequest();
    validationRequest.setLanguage(locale.getLanguage());
    validationRequest.setSignatureData(sign.getData());
    validationRequest.setSignatureRequestedInformation(sri);
    if (doc != null) {
      validationRequest.setSignedDocumentData(doc.getData());
    }
    
    ValidateSignatureResponse resp;
    try {
      resp = validatePlugin.validateSignature(validationRequest);
    } catch (Exception e) {
      // XYZ ZZZ TODO Traduir
      throw new I18NException(e, e.getMessage());
    }
    

    final String perfil = resp.getSignProfile();
    final String tipo = resp.getSignType();
    final String formato = resp.getSignFormat();
    
    log.info("XYZ ZZZ tipo = " + tipo);
    log.info("XYZ ZZZ perfil = " + perfil);
    log.info("XYZ ZZZ formato = " + formato);
    
    
    
    if (perfil == null || tipo == null || formato == null) {
      // TODO XYZ ZZZ  traduir
      throw new I18NException("L'arxiu enviat o no és una firma o el plugin de validació ("
          + validatePlugin.getClass() + ") no ha retornat informació del tipus de firma "
          + "(T:" + tipo + ", P:" + perfil + ", F:" + formato + ")");
    }
    
    if (!sir) {
      Anexo anexo = input.getAnexo(); 
      anexo.setSignFormat(tipo);
      anexo.setSignFormat(formato);
      anexo.setSignProfile(perfil);
      
      return input;
    }
       

    // Acceptam qualsevol tipus excepte BES i PADES-BASIC
    if (!SIGNPROFILE_BES.equals(perfil) && !SIGNPROFILE_PADES_BASIC.equals(perfil)
        && ( SIGNTYPE_CAdES.equals(tipo) || SIGNTYPE_XAdES.equals(tipo) 
            || SIGNTYPE_PAdES.equals(tipo) || SIGNTYPE_ODF.equals(tipo) ) ) {

      // Ficar dins Anexo tipo, formato i perfil
      Anexo anexo = input.getAnexo(); 
      anexo.setSignFormat(tipo);
      anexo.setSignFormat(formato);
      anexo.setSignProfile(perfil);
      
      return input;
    }

    ISignatureServerPlugin signaturePlugin;
    signaturePlugin = (ISignatureServerPlugin)pluginEjb.getPlugin(idEntidad,
        RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);

    if (input.getDocumentoCustody() == null) {
      // CheckFirma amb document attached
      checkAttachedSignature(input, resp , locale, signaturePlugin);
    } else {
      // CheckFirma amb document detached
      checkDetachedSignature(input, resp , locale, signaturePlugin);
    }

    return input;
    } catch(I18NException i18ne) {
      throw i18ne;
    } catch(Exception e) {
      throw new I18NException(e, "error.desconegut", new I18NArgumentString(e.getMessage()));
    }

  }
  
  
  protected static void checkAttachedSignature(AnexoFull input,
      ValidateSignatureResponse resp,
      Locale locale, ISignatureServerPlugin signaturePlugin)
          throws I18NException, Exception {
    final String tipo = resp.getSignType();
    final String formato = resp.getSignFormat();
    
    /* 
    if (SIGNTYPE_PAdES.equals(tipo)) {
      // TODO XYZ ZZZ Falta 
    } else if (SIGNTYPE_CAdES.equals(tipo)) {
      // TODO XYZ ZZZ Falta 
    } else if (SIGNTYPE_XAdES.equals(tipo)) {
      // TODO XYZ ZZZ Falta 
    } else 
    */
    {
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
      firmaCAdESEPES(input, locale, signaturePlugin);
      
      // TODO XYZ ZZZ Falta ficar dins AnexoFUll tipo, forma i perfil
      // Ficar dins Anexo tipo, formato i perfil
      /*
      Anexo anexo = input.getAnexo(); 
      anexo.setSignFormat(SIGNTYPE_CAdES);
      anexo.setSignFormat(SIGNFORMAT_EXPLICIT_DETACHED);
      anexo.setSignProfile(SIGNPROFILE_EPES);
      */

    }
    
  }
  
  
  protected static void checkDetachedSignature(AnexoFull input,
      ValidateSignatureResponse resp,  Locale locale,
      ISignatureServerPlugin signaturePlugin) throws I18NException, Exception {
    // TODO XYZ ZZZ Falta Refirmar amb EPES XAdES Detached
    // TODO XYZ ZZZ Falta Refirmar amb EPES XAdES Detached
    
    // TODO XYZ ZZZ SOLUCIO TEMPORAL: ELIMINAM LA FIRMA i FIRMAM AMB CADES-EPES
    
    // (1) Eliminam la firma
    input.setSignatureCustody(null);      
    
    // (2) Enviam a firmar 
    firmaCAdESEPES(input, locale, signaturePlugin);
    
    // TODO XYZ ZZZ Falta ficar dins AnexoFUll tipo, forma i perfil
    // Ficar dins Anexo tipo, formato i perfil
    /*
    Anexo anexo = input.getAnexo(); 
    anexo.setSignFormat(SIGNTYPE_CAdES);
    anexo.setSignFormat(SIGNFORMAT_EXPLICIT_DETACHED);
    anexo.setSignProfile(SIGNPROFILE_EPES);
    */
    
    
  }
  
  
  protected static void firmaCAdESEPES(AnexoFull input, 
      Locale locale, ISignatureServerPlugin signaturePlugin) throws I18NException, Exception {
    

    
    final String signType = FileInfoSignature.SIGN_TYPE_CADES;
    final int signMode = FileInfoSignature.SIGN_MODE_EXPLICIT;
    final boolean epes = true;
    // TODO XYZ ZZZ igual per Justificante
    final String username = "firmaanexo";
    
    SignatureCustody sc = signFile(input.getDocumentoCustody(), signType, signMode,
        epes, signaturePlugin, username, locale);
    
    
    // Ficar dins Anexo tipo, formato i perfil
    Anexo anexo = input.getAnexo(); 
    anexo.setSignFormat(SIGNTYPE_CAdES);
    anexo.setSignFormat(SIGNFORMAT_EXPLICIT_DETACHED);
    anexo.setSignProfile(SIGNPROFILE_EPES);
        
    input.setSignatureCustody(sc);
    
  }
  
  
  
  protected static SignatureCustody signFile(DocumentCustody doc, String signType, int signMode,
      boolean epes, ISignatureServerPlugin plugin, String username,
      Locale locale) throws I18NException, Exception {

    File source = null;
    File destination = null;
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
          filtreCertificats, username, administrationID, policyInfoSignature);
  
      final String signID = String.valueOf(System.currentTimeMillis());
  
      String name = doc.getName();
      String reason = "Convertir Document/Firma a perfil EPES per enviar a SIR";
      String location = null; // "Palma";
      String signerEmail = null; //"anadal@ibit.org";
      int signNumber = 1;
      
  
      String signAlgorithm = FileInfoSignature.SIGN_ALGORITHM_SHA1;
  
      
      PdfVisibleSignature pdfInfoSignature = null;
      final ITimeStampGenerator timeStampGenerator = null;
  
      // Valors per defcte
      final SignaturesTableHeader signaturesTableHeader = null;
      final SecureVerificationCodeStampInfo csvStampInfo = null;
  
      source = File.createTempFile("regweb_signfile_sir", "document");
      final boolean userRequiresTimeStamp = false;
      final int signaturesTableLocation = FileInfoSignature.SIGNATURESTABLELOCATION_WITHOUT;
      FileUtils.writeByteArrayToFile(source, doc.getData());
      FileInfoSignature fileInfo = new FileInfoSignature(signID, source, doc.getMime(), name,
          reason, location, signerEmail, signNumber, locale.getLanguage(), signType, signAlgorithm,
          signMode, signaturesTableLocation, signaturesTableHeader, pdfInfoSignature,
          csvStampInfo, userRequiresTimeStamp, timeStampGenerator);
  
      final String signaturesSetID = String.valueOf(System.currentTimeMillis());
      SignaturesSet signaturesSet = new SignaturesSet(signaturesSetID, commonInfoSignature,
          new FileInfoSignature[] { fileInfo });
  
     
  
      // Check si passa filtre
      if (!plugin.filter(signaturesSet)) {
        // TODO XYZ ZZZ Traduir
        throw new I18NException("error.desconegut", "El pluguin no suporta aquest tipus de firma.");
      }
      
  
      final String timestampUrlBase = null;
      signaturesSet = plugin.signDocuments(signaturesSet, timestampUrlBase);
      StatusSignaturesSet sss = signaturesSet.getStatusSignaturesSet();
  
      if (sss.getStatus() != StatusSignaturesSet.STATUS_FINAL_OK) {
        // TODO XYZ ZZZ Traduir
        throw new I18NException(sss.getErrorException(),"error.desconegut",
            new I18NArgumentString("Error realitzant una firma: " + sss.getErrorMsg()));
      } else {
        FileInfoSignature fis = signaturesSet.getFileInfoSignatureArray()[0];
        StatusSignature status = fis.getStatusSignature();
        if (status.getStatus() != StatusSignaturesSet.STATUS_FINAL_OK) {
       // TODO XYZ ZZZ Traduir
          throw new I18NException(status.getErrorException(), "error.desconegut",
            new I18NArgumentString("Error realitzant una firma: " + status.getErrorMsg()));
        } else {
          destination = status.getSignedData();
          
          SignatureCustody sc = new SignatureCustody();
          sc.setData(FileUtils.readFileToByteArray(destination));
          sc.setLength(sc.getData().length);
          String mime;
          String custSignType;
          Boolean attachedDocument;
          if (FileInfoSignature.SIGN_TYPE_PADES.equals(signType)) {
            mime = FileInfoSignature.PDF_MIME_TYPE;
            custSignType = SignatureCustody.PADES_SIGNATURE;
            attachedDocument = null;
          } else if (FileInfoSignature.SIGN_TYPE_XADES.equals(signType)) {
            mime = "application/xml";
            custSignType = SignatureCustody.XADES_SIGNATURE;
            attachedDocument = (signMode == FileInfoSignature.SIGN_MODE_EXPLICIT);
          } else  if (FileInfoSignature.SIGN_TYPE_CADES.equals(signType)) {
            custSignType = SignatureCustody.CADES_SIGNATURE;
            mime = "application/octet-stream";
            attachedDocument = (signMode == FileInfoSignature.SIGN_MODE_EXPLICIT);
          } else {
            // XYZ ZZZ TODO Traduir 
            throw new I18NException("error.desconegut","Tipus de firma desconeguda " + signType);
          }
          sc.setMime(mime);
          sc.setSignatureType(custSignType);
          sc.setAttachedDocument(attachedDocument);
          
          return sc;
  
        }
      }
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
  
  
}
