package es.caib.regweb3.model.utils;

import es.caib.plugins.arxiu.api.Document;
import es.caib.plugins.arxiu.api.Firma;
import es.caib.plugins.arxiu.api.FirmaPerfil;
import es.caib.plugins.arxiu.api.FirmaTipus;
import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.plugins.documentcustody.api.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.api.SignatureCustody;
import org.fundaciobit.pluginsib.core.utils.Metadata;

import javax.persistence.Transient;
import java.util.List;

/**
 * Clase que representa un Anexo + toda la parte de custodia
 * La custodia de un anexo está formada por
 * DocumentCustody: Almacena el documento del anexo sin firma
 * SignatureCustody: Almacena la firma del documento(cuando es detached) o el documento+firma cuando la firma es attached
 * Metadatas: guarda la lista de metadatos asociados al anexo.
 *
 * @author anadal
 */
public class AnexoFull{

    private Anexo anexo;

    // DocumentCustody
    private boolean documentoFileDelete;
    private boolean signatureFileDelete;
    private DocumentCustody documentoCustody;
    private SignatureCustody signatureCustody;
    private List<Metadata> metadatas;

    //ArxiuCaib
    private Document document;


    /**
     * @param anexoFull
     */
    public AnexoFull(AnexoFull anexoFull) {
        super();
        this.anexo = new Anexo(anexoFull.getAnexo());
        this.documentoFileDelete = anexoFull.documentoFileDelete;
        this.signatureFileDelete = anexoFull.signatureFileDelete;
        this.documentoCustody = anexoFull.documentoCustody;
        this.signatureCustody = anexoFull.signatureCustody;
        this.metadatas = anexoFull.metadatas;
        this.document = anexoFull.getDocument();
    }


    public AnexoFull() {
        this.anexo = new Anexo();
        this.document = new Document();
        this.anexo.setModoFirma(RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED);
        this.anexo.setTipoDocumental(new TipoDocumental());
    }


    public AnexoFull(Anexo anexo) {
        this.anexo = anexo;
    }


    public DocumentCustody getDocumentoCustody() {
        return documentoCustody;
    }


    public void setDocumentoCustody(DocumentCustody documentoCustody) {
        this.documentoCustody = documentoCustody;
    }


    public SignatureCustody getSignatureCustody() {
        return signatureCustody;
    }


    public void setSignatureCustody(SignatureCustody signatureCustody) {
        this.signatureCustody = signatureCustody;
    }


    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }


    public boolean isDocumentoFileDelete() {
        return documentoFileDelete;
    }

    public void setDocumentoFileDelete(boolean documentoFileDelete) {
        this.documentoFileDelete = documentoFileDelete;
    }


    public boolean isSignatureFileDelete() {
        return signatureFileDelete;
    }


    public void setSignatureFileDelete(boolean signatureFileDelete) {
        this.signatureFileDelete = signatureFileDelete;
    }


    public List<Metadata> getMetadatas() {
        return metadatas;
    }


    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = metadatas;
    }


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    @Transient
    public byte[] getDocData() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getDocumentoCustody() != null) {
                return getDocumentoCustody().getData();
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                return getDocument().getContingut().getContingut();
            }
        }

        return null;
    }

    @Transient
    public byte[] getSignData() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getSignatureCustody() != null) {
                return getSignatureCustody().getData();
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                return getDocument().getFirmes().get(0).getContingut();
            }
        }

        return null;
    }

    @Transient
    public long getDocSize() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getDocumentoCustody() != null) {
                long size = getDocumentoCustody().getLength();

                if (size < 1024) {
                    return 1;
                } else {
                    return size / 1024;
                }
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                long size = getDocument().getContingut().getTamany();

                if (size < 1024) {
                    return 1;
                } else {
                    return size / 1024;
                }
            }
        }

        return -1;
    }

    @Transient
    public long getSignSize() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getSignatureCustody() != null) {
                long size = getSignatureCustody().getLength();

                if (size < 1024) {
                    return 1;
                } else {
                    return size / 1024;
                }
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                if(getDocument().getContingut() != null){
                    long size = getDocument().getContingut().getTamany();

                    if (size < 1024) {
                        return 1;
                    } else {
                        return size / 1024;
                    }
                }
            }
        }

        return -1;
    }

    @Transient
    public String getExtension() {

        if(getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA || getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_DETACHED) {

            if (getDocumentoCustody() != null) {

                int indexPunt = getDocumentoCustody().getName().lastIndexOf(".");
                if (indexPunt != -1 && indexPunt < getDocumentoCustody().getName().length() - 1) {
                    return getDocumentoCustody().getName().substring(indexPunt + 1);
                } else {
                    return null;
                }
            }

        }else if(getAnexo().getModoFirma()== RegwebConstantes.MODO_FIRMA_ANEXO_ATTACHED){

            if (getSignatureCustody() != null) {

                int indexPunt = getSignatureCustody().getName().lastIndexOf(".");
                if (indexPunt != -1 && indexPunt < getSignatureCustody().getName().length() - 1) {
                    return getSignatureCustody().getName().substring(indexPunt + 1);
                } else {
                    return null;
                }
            }
        }

        return null;
    }

    @Transient
    public String getDocMime() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getDocumentoCustody() != null) {
                return getDocumentoCustody().getMime();
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                if(getDocument().getContingut() != null){
                    return getDocument().getContingut().getTipusMime();
                }
            }
        }

        return "";
    }

    @Transient
    public String getSignMime() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getSignatureCustody() != null) {
                return getSignatureCustody().getMime();
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                if(getDocument().getContingut() != null){
                    return getDocument().getContingut().getTipusMime();
                }
            }
        }

        return "";
    }

    @Transient
    public String getSignaturaTituloCorto() {

        if (getSignatureCustody() != null) {
            String tituloCorto = getSignatureCustody().getName();

            if (tituloCorto.length() > 100) {
                tituloCorto = getSignatureCustody().getName().substring(0, 100) + "...";
            }
            return tituloCorto;
        } else {
            return "";
        }
    }

    @Transient
    public String getDocumentTituloCorto() {

        if (getDocumentoCustody() != null) {
            String tituloCorto = getDocumentoCustody().getName();

            if (tituloCorto.length() > 100) {
                tituloCorto = getDocumentoCustody().getName().substring(0, 100) + "...";
            }
            return tituloCorto;
        } else {
            return "";
        }
    }

    @Transient
    public void arxiuDocumentToCustody(){

        getAnexo().setPerfilCustodia(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY);

        String custodyId = getAnexo().getCustodiaID() +"#"+getAnexo().getExpedienteID();
        getAnexo().setCustodiaID(custodyId);

        SignatureCustody sc = new SignatureCustody();
        sc.setData(getDocument().getContingut().getContingut());
        sc.setLength(getDocument().getContingut().getTamany());
        sc.setMime(getDocument().getContingut().getTipusMime());
        sc.setName(getDocument().getNom());
        sc.setSignatureType(getAnexo().getSignType());
        sc.setAttachedDocument(null);

        setSignatureCustody(sc);
    }

    @Transient
    public Firma signatureCustodytoFirma(){

        if(getSignatureCustody() != null){

            // Creamos la Firma
            Firma firma = new Firma();
            firma.setFitxerNom(getSignatureCustody().getName());
            firma.setContingut(getSignatureCustody().getData());
            firma.setTamany(getSignatureCustody().getData().length);
            firma.setPerfil(FirmaPerfil.EPES);
            firma.setTipus(FirmaTipus.PADES);
            firma.setTipusMime(getSignatureCustody().getMime());
            firma.setCsvRegulacio("");

            return firma;
        }

        return null;
    }


    @Transient
    public String getSignFileName() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getSignatureCustody() != null) {
                return getSignatureCustody().getName();
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                if(getDocument().getNom() != null){
                    return getDocument().getNom();
                }
            }
        }

        return "";
    }

    @Transient
    public String getDocFileName() {

        if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_DOCUMENT_CUSTODY)){

            if (getDocumentoCustody() != null) {
                return getDocumentoCustody().getName();
            }

        }else if(anexo.getPerfilCustodia().equals(RegwebConstantes.PERFIL_CUSTODIA_ARXIU)){

            if(getDocument() != null){
                if(getDocument().getNom() != null){
                    return getDocument().getNom();
                }
            }
        }

        return "";
    }

    @Transient
    public String getFileName() {

        return !this.getDocFileName().isEmpty()?this.getDocFileName():this.getSignFileName();
    }


    @Transient
    public byte[] getData(){
        return this.getDocData()!=null?this.getDocData():this.getSignData();
    }

    @Transient
    public String getMime(){

        return !this.getDocMime().isEmpty()?this.getDocMime():this.getSignMime();

    }

    @Transient
    public long getSize(){

        return this.getDocSize()!=-1?this.getDocSize():this.getSignSize();

    }

}