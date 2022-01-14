package es.caib.regweb3.ws.model;


import javax.xml.bind.annotation.XmlRootElement;


/**
 * 
 * @author anadal
 * 
 */
@XmlRootElement
public class TipoDocumentalWs {

  private String codigoNTI;

  public String getCodigoNTI() {
    return codigoNTI;
  }

  public void setCodigoNTI(String codigoNTI) {
    this.codigoNTI = codigoNTI;
  }
}
