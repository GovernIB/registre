package es.caib.regweb3.plugins.arxiu.caib.utils;

import es.caib.plugins.arxiu.api.DocumentExtensio;
import es.caib.plugins.arxiu.api.DocumentFormat;
import es.caib.plugins.arxiu.api.FirmaPerfil;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.regweb3.model.Anexo;
import org.fundaciobit.plugins.validatesignature.api.ValidateSignatureConstants;

public class Regweb3PluginArxiuUtils {

    public static DocumentFormat getDocumentFormat(DocumentExtensio extension){

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

    public static FirmaPerfil getFirmaPerfil(String singProfile) throws Exception{

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

    public static FirmaTipus getFirmaTipus(Anexo anexo) throws Exception{

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
}
