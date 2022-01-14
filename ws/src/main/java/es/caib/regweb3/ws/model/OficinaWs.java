package es.caib.regweb3.ws.model;


/**
 * 
 * @author anadal
 * 
 */
public class OficinaWs {

  private String codigo;

  private String nombre;

  public OficinaWs() {
  }

  public OficinaWs(String codigo, String nombre) {
    this.codigo = codigo;
    this.nombre = nombre;
  }

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

}
