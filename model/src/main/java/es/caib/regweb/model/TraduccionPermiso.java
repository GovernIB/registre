package es.caib.regweb.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created 11/03/14 12:55
 *
 * @author mgonzalez
 */
@Embeddable
public class TraduccionPermiso implements Traduccion {

  private String nombre;
  private String descripcion;


  @Column(name = "NOMBRE", nullable = false)
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }


  @Column(name = "DESCRIPCION", nullable = false)
  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
}
