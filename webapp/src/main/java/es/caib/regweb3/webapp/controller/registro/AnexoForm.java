package es.caib.regweb3.webapp.controller.registro;


import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.utils.AnexoFull;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 
 * @author anadal
 *
 */
public class AnexoForm extends AnexoFull {

  
  private CommonsMultipartFile documentoFile;
  
  private CommonsMultipartFile firmaFile;
  
  
  String tipoRegistro;
  
  Long registroID;

  Boolean oficioRemisionSir;

  Boolean permitirAnexoDetached;
  
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
  
  

  public String getTipoRegistro() {
    return tipoRegistro;
  }


  public void setTipoRegistro(String tipoRegistro) {
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
}
