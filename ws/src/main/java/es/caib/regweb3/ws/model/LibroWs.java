package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author anadal
 * 
 */
@XmlRootElement
public class LibroWs {

  private String codigoLibro;
  private String nombreCorto;
  private String nombreLargo;
  private String codigoOrganismo;

  public LibroWs() {
  }

  public LibroWs(String codigoLibro, String nombreCorto, String nombreLargo, String codigoOrganismo) {
    this.codigoLibro = codigoLibro;
    this.nombreCorto = nombreCorto;
    this.nombreLargo = nombreLargo;
    this.codigoOrganismo = codigoOrganismo;
  }

  public String getCodigoLibro() {
    return codigoLibro;
  }

  public void setCodigoLibro(String codigoLibro) {
    this.codigoLibro = codigoLibro;
  }

  public String getCodigoOrganismo() {
    return codigoOrganismo;
  }

  public void setCodigoOrganismo(String codigoOrganismo) {
    this.codigoOrganismo = codigoOrganismo;
  }

  public String getNombreCorto() {
    return nombreCorto;
  }

  public void setNombreCorto(String nombreCorto) {
    this.nombreCorto = nombreCorto;
  }

  public String getNombreLargo() {
    return nombreLargo;
  }

  public void setNombreLargo(String nombreLargo) {
    this.nombreLargo = nombreLargo;
  }

}
