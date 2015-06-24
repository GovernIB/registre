package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.TipoDocumental;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.plugins.documentcustody.DocumentCustody;
import org.fundaciobit.plugins.documentcustody.SignatureCustody;

/**
 * 
 * @author anadal
 *
 */
public class AnexoFull {

  Anexo anexo;
 
  private boolean documentoFileDelete;
  
  private boolean signatureFileDelete;
  
  private DocumentCustody documentoCustody;

  private SignatureCustody signatureCustody;
  
  

  /**
   * @param anexoFull
   */
  public AnexoFull(AnexoFull anexoFull) {
    super();
    this.anexo = anexoFull.anexo;
    this.documentoFileDelete = anexoFull.documentoFileDelete;
    this.signatureFileDelete = anexoFull.signatureFileDelete;
    this.documentoCustody = anexoFull.documentoCustody;
    this.signatureCustody = anexoFull.signatureCustody;
  }


  public AnexoFull() {
    this.anexo = new Anexo();
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


  
}
