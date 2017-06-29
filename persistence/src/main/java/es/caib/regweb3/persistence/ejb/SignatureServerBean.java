package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.*;
import org.fundaciobit.plugins.documentcustody.api.AnnexCustody;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.plugins.signature.api.*;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jpernia on 04/04/2017.
 * @author anadal (08/05/2017) Mètode checkDocumentAndSignature
 */
@Stateless(name = "SignatureServerEJB")
@SecurityDomain("seycon")
public class SignatureServerBean implements SignatureServerLocal, ValidateSignatureConstants {

    protected final Logger log = Logger.getLogger(getClass());
    

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    /**Método que genera la Firma de un File para una Entidad en concreto
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @return
     * @throws Exception
     */
    @Override
    public SignatureCustody signJustificante(byte[] pdfsource, String languageUI,
        Long idEntidadActiva) throws Exception, I18NException {

        // Cerca el Plugin de Justificant definit a les Propietats Globals
        ISignatureServerPlugin signaturePlugin = (ISignatureServerPlugin) pluginEjb.getPlugin(idEntidadActiva, RegwebConstantes.PLUGIN_FIRMA_SERVIDOR);


        String reason = "FIRMA_JUSTIFICANT"; // Hem de canviar raó justificant????


        final String signType = FileInfoSignature.SIGN_TYPE_PADES;
        final int signMode = FileInfoSignature.SIGN_MODE_EXPLICIT;
        final boolean epes = true;
        
        DocumentCustody doc = new DocumentCustody();
        doc.setData(pdfsource);
        doc.setLength(pdfsource.length);
        doc.setMime(FileInfoSignature.PDF_MIME_TYPE);
        doc.setName("justificante.pdf");
        

        SignatureCustody sc = signFile(doc, signType, signMode, epes, signaturePlugin,
            new Locale(languageUI), reason);
        

        return sc;

    }
    
    
    
    
  /**
   * @author anadal
   * 
   * force = true  => Es crida des d'enviar a SIR
   * force = false => Es crida des d'edició d'annexes 
   * 
   * A continuació es mostra el flux d'accions quan es produeixen errors en les cridades
   * a Firmar i Validar
   *
   *  =========== NO SIR =======
   *  (1) No Es firma: OK  
   *  (2) Es Firma: OK <<PENDENT. EN EL FUTUR s'HAURÀ DE VALIDAR>>
   *
   *  =========== SIR(boolean force) =======
   *
   *  (1) Document PLA
   *     (1.1) Firma CADES-EPES-Detached OK => OK
   *     (1.2) Firma CADES-EPES-Detached Falla
   *         (1.2.1) Si force=true => Llançar Excepció
   *         (1.2.2) Si force=false => Retornar msg d'avís i continuar  
   *  (2) És una Firma ATTACHED
   *     (2.1) Firma CADES-EPES-Detached OK => OK
   *     (2.2) Firma CADES-EPES-Detached Falla
   *         (2.2.1) Si force=true => Llançar Excepció
   *         (2.2.2) Si force=false => Retornar msg d'avís i continuar  
   *   (3) És una Firma DETACHED: Eliminam la firma detached i tornam a firmar
   *     (3.1) Firma CADES-EPES-Detached OK => OK
   *     (3.2) Firma CADES-EPES-Detached Falla
   *         (3.2.1) Si force=true => Llançar Excepció
   *         (3.2.2) Si force=false => Retornar msg d'avís i continuar  
   */
  @Override
  public I18NTranslation checkDocumentAndSignature(AnexoFull input, long idEntidad,
      boolean sir, Locale locale, boolean force) throws I18NException {

    boolean error = false;

    try {

      SignatureCustody sign = input.getSignatureCustody();
      DocumentCustody doc = input.getDocumentoCustody();

      log.info("checkDocumentAndSignature::Document = " + doc);
      log.info("checkDocumentAndSignature::Signature = " + sign);

      if (sign == null && doc == null) {
        throw new I18NException("error.checkanexosir.nifirmanidoc");
      }

      if (!sir) {
        // TODO NOTA: <<PENDENT. EN EL FUTUR s'HAURÀ DE VALIDAR QUAN SIGUI UNA
        // FIRMA >>
        return null; // OK
      }

      // A PARTIR D'AQUI TOT ES SIR

      if ((doc != null && sign == null)) {

        try {

          ISignatureServerPlugin signaturePlugin = getInstanceSignatureServerPlugin(idEntidad);
          // És un document PLA: Firmar emprant CAdES-EPES

          firmaCAdESEPESDetached(input, doc, locale, signaturePlugin);

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
          signAttachedSignature(input, locale, signaturePlugin);
        } else {
          // CheckFirma amb document detached
          signDetachedSignature(input, locale, signaturePlugin);
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


    
    protected I18NTranslation processError(I18NException i18ne, boolean force) throws I18NException {
      
      if (force) {
        throw i18ne;
      }
      
      I18NTranslation trans = i18ne.getTraduccio();
      return trans;
  
    };
    

    //No s'utilitza 
    /*
    protected ValidateSignatureResponse callToValidaFirma(Locale locale, SignatureCustody sign,
        DocumentCustody doc) throws I18NException {
      ValidateSignatureResponse resp;
      
        // TODO CACHE DE PLUGIN!!!!!
      IValidateSignaturePlugin validatePlugin;
      validatePlugin = (IValidateSignaturePlugin) pluginEjb.getPlugin(null,
          RegwebConstantes.PLUGIN_VALIDACION_FIRMAS);
      if (validatePlugin == null) {
        // El plugin de Validació de Firmes no s'ha definit. Consulti amb l'Administrador
        throw new I18NException("error.plugin.nodefinit", new I18NArgumentCode("plugin.tipo.8"));
      }

      // Verificar que ofereix servei de informació de firmes
      SignatureRequestedInformation sri = validatePlugin
          .getSupportedSignatureRequestedInformation();
      if (!Boolean.TRUE.equals(sri.getReturnSignatureTypeFormatProfile())) {
        // El plugin de Validació/Informació de Firmes no proveeix informació de firmes.
        throw new I18NException("error.plugin.validasign.noinfo");
            
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

      
      try {
        resp = validatePlugin.validateSignature(validationRequest);
      } catch (Exception e) {
        throw new I18NException(e, "error.checkanexosir.validantfirma",
            new I18NArgumentString(e.getMessage()));
      }

      return resp;
    }
    */

    // No s'utilitza
    /*
    protected String addInFileName(String fileName, final String add) {
      int index = fileName.lastIndexOf('.');
      if (index == -1) {
        fileName = fileName + add;
      } else {
        fileName = fileName.substring(0,index) + add + fileName.substring(index);
      }
      return fileName;
    }
    */

  protected void signAttachedSignature(AnexoFull input, Locale locale,
      ISignatureServerPlugin signaturePlugin)  throws I18NException {
      

      if(log.isDebugEnabled()) {
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
        firmaCAdESEPESDetached(input, doc, locale, signaturePlugin);

        input.getSignatureCustody().setName(doc.getName().replace('.', '_') + "_EPES.csig");

    }

    protected void signDetachedSignature(AnexoFull input,
        Locale locale, ISignatureServerPlugin signaturePlugin)
        throws I18NException {

      // ELIMINAM LA FIRMA i FIRMAM AMB CADES-EPES
      // (1) Enviam a firmar
      firmaCAdESEPESDetached(input, input.getDocumentoCustody(), locale, signaturePlugin);
      
      input.getSignatureCustody().setName("signature_CAdES_EPES.csig");

    }

    protected void firmaCAdESEPESDetached(AnexoFull input, AnnexCustody docToSign, Locale locale,
        ISignatureServerPlugin signaturePlugin) throws I18NException {

      final String signType = FileInfoSignature.SIGN_TYPE_CADES;
      final int signMode = FileInfoSignature.SIGN_MODE_EXPLICIT;
      final boolean epes = true;
      final String reason = "Convertir Document/Firma a perfil EPES per enviar a SIR";

      SignatureCustody sc = signFile(docToSign, signType, signMode, epes,
          signaturePlugin, locale, reason);

      // Ficar dins Anexo tipo, formato i perfil
      Anexo anexo = input.getAnexo();
      anexo.setSignType(SIGNTYPE_CAdES);
      anexo.setSignFormat(SIGNFORMAT_EXPLICIT_DETACHED);
      anexo.setSignProfile(SIGNPROFILE_EPES);

      input.setSignatureCustody(sc);

    }
    
    
    @Override
    public void firmaPAdESEPES(AnexoFull input, long idEntidad, Locale locale) throws I18NException {
      
      DocumentCustody doc = input.getDocumentoCustody();
      ISignatureServerPlugin signaturePlugin = getInstanceSignatureServerPlugin(idEntidad);

      AnnexCustody documentToSign = input.getDocumentoCustody();

      firmaPAdESEPES(input, documentToSign, locale, signaturePlugin);

    }

  /**
   * Método que añade una firma detached a todos los anexos que se le pasan en la variable anexosEnviarASir
   * Es obligatorio
   * @param anexosEnviarASir
   * @param idEntidad
   * @param locale
   * @param force
   * @throws I18NException
   */
  public List<AnexoFull> firmarAnexosEnvioSir(List<AnexoFull> anexosEnviarASir, Long idEntidad, Locale locale, boolean force) throws I18NException{

    // Creamos firmas temporales de los anexos para enviar a SIR


    List<AnexoFull> anexosADevolver = new ArrayList<AnexoFull>();
    for(AnexoFull anexoFull: anexosEnviarASir) {
      AnexoFull anexoModif = new AnexoFull(anexoFull);

      DocumentCustody dc = anexoModif.getDocumentoCustody();
      SignatureCustody sc = anexoModif.getSignatureCustody();

      log.info("MODO FIRMA ANEXO MODIF antes " + anexoModif.getAnexo().getModoFirma());

      //anexoModif.setDocumentoCustody(dc);
      //anexoModif.setSignatureCustody(sc);
      //anexoModif.setAnexo(new Anexo(anexoFull.getAnexo()));
      //anexoModif.getAnexo().setHash(anexoFull.getAnexo().getHash());


      log.info("MODO FIRMA ANEXO MODIF despues " + anexoModif.getAnexo().getModoFirma());


      if ((dc!=null && sc == null) || (dc==null && sc != null)) {// Document pla o attached
        checkDocumentAndSignature(anexoModif,
                idEntidad, true,
                locale,
                force);
        anexosADevolver.add(anexoModif);
      }else{ //Detached
        //Arreglam la firma
        AnexoFull anexoFullFirma = anexoModif;

        anexoFullFirma.setDocumentoCustody(null);
        checkDocumentAndSignature(anexoFullFirma,
                idEntidad, true,
                locale,
                force);

        anexosADevolver.add(anexoFullFirma);

        //arreglam el document pla
        anexoModif.setSignatureCustody(null);
        checkDocumentAndSignature(anexoModif,
                idEntidad, true,
                locale,
                force);
        anexosADevolver.add(anexoModif);

      }
    }

    return anexosADevolver;

  }
    
    
    

    protected void firmaPAdESEPES(AnexoFull input, AnnexCustody documentToSign, Locale locale,
        ISignatureServerPlugin signaturePlugin) throws I18NException {

      final String signType = FileInfoSignature.SIGN_TYPE_PADES;
      final int signMode = FileInfoSignature.SIGN_MODE_IMPLICIT;
      final boolean epes = true;
      final String reason = "Convertir Document/Firma a perfil EPES per enviar a SIR";

      SignatureCustody sc = signFile(documentToSign, signType, signMode, epes,
          signaturePlugin, locale, reason);

      // Ficar dins Anexo tipo, formato i perfil
      Anexo anexo = input.getAnexo();
      anexo.setSignType(SIGNTYPE_PAdES);
      anexo.setSignFormat(SIGNFORMAT_IMPLICIT_ENVELOPED_ATTACHED);
      anexo.setSignProfile(SIGNPROFILE_EPES);

      input.setSignatureCustody(sc);
      
      input.setDocumentoCustody(null);
      
      System.gc();

    }
    
    
    

    protected SignatureCustody signFile(AnnexCustody doc, String signType,
        int signMode, boolean epes, ISignatureServerPlugin plugin, Locale locale, String reason)
        throws I18NException {

      File source = null;
      File destination = null;
      final String username = CONFIG_USERNAME;
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
          policyInfoSignature
              .setPolicyUrlDocument("https://sede.060.gob.es/politica_de_firma_anexo_1.pdf");
        }

        CommonInfoSignature commonInfoSignature = new CommonInfoSignature(locale.getLanguage(),
            filtreCertificats, username, administrationID, policyInfoSignature);

        final String signID = String.valueOf(System.currentTimeMillis());

        final String name = doc.getName();
        
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
        FileUtils.writeByteArrayToFile(source, doc.getData());
        FileInfoSignature fileInfo = new FileInfoSignature(signID, source, doc.getMime(), name,
            reason, location, signerEmail, signNumber, locale.getLanguage(), signType,
            signAlgorithm, signMode, signaturesTableLocation, signaturesTableHeader,
            pdfInfoSignature, csvStampInfo, userRequiresTimeStamp, timeStampGenerator);

        final String signaturesSetID = String.valueOf(System.currentTimeMillis());
        SignaturesSet signaturesSet = new SignaturesSet(signaturesSetID, commonInfoSignature,
            new FileInfoSignature[] { fileInfo });

        // Check si passa filtre
        if (!plugin.filter(signaturesSet)) {
          // "El plugin no suporta aquest tipus de firma/mode (" + signType + ", " + signMode + ")
          // o s'ha produit un error greou durant la firma"
          throw new I18NException("error.plugin.firma.nosuportat", signType, String.valueOf(signMode));
        }

        final String timestampUrlBase = null;
        signaturesSet = plugin.signDocuments(signaturesSet, timestampUrlBase);
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

            SignatureCustody sc = new SignatureCustody();
            sc.setData(FileUtils.readFileToByteArray(destination));
            sc.setLength(sc.getData().length);
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
            sc.setName(signFileName);
            sc.setMime(mime);
            sc.setSignatureType(custSignType);
            sc.setAttachedDocument(attachedDocument);

            return sc;

          }
        }
      } catch(I18NException i18ne) {
        throw i18ne;
      } catch(Exception e) { 
        throw new I18NException(e, "error.realitzantfirma",
            new I18NArgumentString(e.getMessage()));
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
    
    
    
    public static boolean is_pdf(byte[] data) {
      
      if (data != null && data.length > 4 &&
              data[0] == 0x25 && // %
              data[1] == 0x50 && // P
              data[2] == 0x44 && // D
              data[3] == 0x46 && // F
              data[4] == 0x2D) { // -
        

        byte[] endOfPdf = new byte[10];
        for (int i = 0; i <10; i++) {
          endOfPdf[i] = data[data.length - 10 + i];
        }
        
        String str = new String(endOfPdf); 

        if (str.indexOf("%%EOF") != -1) {
          return true;
        }

      }
      return false;
  }

}
