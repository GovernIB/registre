package es.caib.regweb3.ws.model;


import javax.xml.bind.annotation.XmlRootElement;


/**
 * 
 * @author anadal
 * 
 */
@XmlRootElement
@Deprecated
public class TipoAsuntoWs {

  private String codigo;

  private String nombre;

  private boolean activo;

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public boolean isActivo() {
    return activo;
  }

  public void setActivo(boolean activo) {
    this.activo = activo;
  }

}
