package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.persistence.utils.RegwebPluginsManager;
import org.fundaciobit.plugins.signature.api.*;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.ejb.Stateless;
import java.io.File;

/**
 * Created by jpernia on 04/04/2017.
 */
@Stateless(name = "SignatureServerEJB")
@SecurityDomain("seycon")
public class SignatureServerBean implements SignatureServerLocal{

    /**Método que genera la Firma de un File para una Entidad en concreto
     * @param pdfsource
     * @param languageUI
     * @param idEntidadActiva
     * @return
     * @throws Exception
     */
    public File signFile(File pdfsource, String languageUI, Long idEntidadActiva) throws Exception {

        // Cerca el Plugin de Justificant definit a les Propietats Globals
        ISignatureServerPlugin pluginInstance = RegwebPluginsManager.getPluginSignatureServer(idEntidadActiva);


        final String signType = FileInfoSignature.SIGN_TYPE_PADES;
        final int signMode = FileInfoSignature.SIGN_MODE_IMPLICIT;
        boolean userRequiresTimeStamp = false;
        final String username = "regweb3"; // configuracio

        String filtreCertificats = "";

        String administrationID = null; // No te sentit en API Firma En Servidor
        PolicyInfoSignature policyInfoSignature = null;
        CommonInfoSignature commonInfoSignature = new CommonInfoSignature(languageUI,
                filtreCertificats, username, administrationID, policyInfoSignature);

        String signID = "999";
        String name = "justificante.pdf";
        String reason = "FIRMA_JUSTIFICANT"; // Hem de canviar raó justificant????
        String location = null;
        String signerEmail = null;
        int signNumber = 1;
        String languageSign = languageUI;

        String signAlgorithm = FileInfoSignature.SIGN_ALGORITHM_SHA1;

        int signaturesTableLocation = FileInfoSignature.SIGNATURESTABLELOCATION_WITHOUT;
        PdfVisibleSignature pdfInfoSignature = null;
//      final IRubricGenerator rubricGenerator = null;
//        if (FileInfoSignature.SIGN_TYPE_PADES.equals(signType) && rubricGenerator != null) {
//            signaturesTableLocation = FileInfoSignature.SIGNATURESTABLELOCATION_LASTPAGE;
//            PdfRubricRectangle pdfRubricRectangle = new PdfRubricRectangle(106, 650, 555, 710);
//            pdfInfoSignature = new PdfVisibleSignature(pdfRubricRectangle, rubricGenerator);
//        }
        final ITimeStampGenerator timeStampGenerator = null;

        // Valors per defcte
        final SignaturesTableHeader signaturesTableHeader = null;
        final SecureVerificationCodeStampInfo csvStampInfo = null;

        FileInfoSignature fileInfo = new FileInfoSignature(signID, pdfsource,
                FileInfoSignature.PDF_MIME_TYPE, name, reason, location, signerEmail, signNumber,
                languageSign, signType, signAlgorithm, signMode, signaturesTableLocation,
                signaturesTableHeader, pdfInfoSignature, csvStampInfo, userRequiresTimeStamp,
                timeStampGenerator);

        final String signaturesSetID = String.valueOf(System.currentTimeMillis());
        SignaturesSet signaturesSet = new SignaturesSet(signaturesSetID, commonInfoSignature,
                new FileInfoSignature[] { fileInfo });

        String timestampUrlBase = null;
        signaturesSet = pluginInstance.signDocuments(signaturesSet, timestampUrlBase);
        StatusSignaturesSet sss = signaturesSet.getStatusSignaturesSet();

        if (sss.getStatus() != StatusSignaturesSet.STATUS_FINAL_OK) {
            System.err.println("Error General MSG = " + sss.getErrorMsg());
            if (sss.getErrorException() != null) {
                sss.getErrorException().printStackTrace();
            }
            throw new Exception(sss.getErrorMsg());
        } else {
            FileInfoSignature fis = signaturesSet.getFileInfoSignatureArray()[0];
            StatusSignature status = fis.getStatusSignature();
            if (status.getStatus() != StatusSignaturesSet.STATUS_FINAL_OK) {
                if (status.getErrorException() != null) {
                    status.getErrorException().printStackTrace();
                }
                System.err.println("Error Firma 1. MSG = " + status.getErrorMsg());
                throw new Exception(status.getErrorMsg());
            } else {
                return status.getSignedData();
            }
        }

    }

}
