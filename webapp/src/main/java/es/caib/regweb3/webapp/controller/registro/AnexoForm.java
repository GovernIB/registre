package es.caib.regweb3.webapp.controller.registro;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.utils.AnexoFull;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * @author anadal
 */
public class AnexoForm extends AnexoFull {

    private CommonsMultipartFile documentoFile;
    private CommonsMultipartFile firmaFile;
    private Long tipoRegistro;
    private Long registroID;
    private Boolean oficioRemisionSir;
    private Boolean permitirAnexoDetached;
    //propiedades de los anexos escaneados
    private String pixelType;
    private String pppResolution;
    private int numAnexosEscaneados;
    private int numDocumento; // En escaneos masivos indica el numero de documento que estamos tratando

    public AnexoForm() {
        super();
    }


    public AnexoForm(Anexo anexo) {
        super(anexo);
    }


    public AnexoForm(AnexoFull anexoFull) {
        super(anexoFull);
    }


    public void setDocumentoFile(CommonsMultipartFile documentoFile) {
        this.documentoFile = documentoFile;
    }

    public CommonsMultipartFile getDocumentoFile() {
        return documentoFile;
    }


    public CommonsMultipartFile getFirmaFile() {
        return firmaFile;
    }

    public void setFirmaFile(CommonsMultipartFile firmaFile) {
        this.firmaFile = firmaFile;
    }


    public Long getTipoRegistro() {
        return tipoRegistro;
    }


    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }


    public Long getRegistroID() {
        return registroID;
    }


    public void setRegistroID(Long registroID) {
        this.registroID = registroID;
    }

    public Boolean getOficioRemisionSir() {
        return oficioRemisionSir;
    }

    public void setOficioRemisionSir(Boolean oficioRemisionSir) {
        this.oficioRemisionSir = oficioRemisionSir;
    }

    public Boolean getPermitirAnexoDetached() {
        return permitirAnexoDetached;
    }

    public void setPermitirAnexoDetached(Boolean permitirAnexoDetached) {
        this.permitirAnexoDetached = permitirAnexoDetached;
    }

    public String getPixelType() {
        return pixelType;
    }

    public void setPixelType(String pixelType) {
        this.pixelType = pixelType;
    }

    public String getPppResolution() {
        return pppResolution;
    }

    public void setPppResolution(String pppResolution) {
        this.pppResolution = pppResolution;
    }

    public int getNumAnexosEscaneados() {
        return numAnexosEscaneados;
    }

    public void setNumAnexosEscaneados(int numAnexosEscaneados) {
        this.numAnexosEscaneados = numAnexosEscaneados;
    }

    public int getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(int numDocumento) {
        this.numDocumento = numDocumento;
    }
}
