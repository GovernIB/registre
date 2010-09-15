package es.caib.regweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="BAGECOM")
@org.hibernate.annotations.Table(appliesTo = "BAGECOM", comment = "Oficina")
public class Oficina implements java.io.Serializable {
  private int codigo;
  private String nombre;
  private int fechaBaja;

  public Oficina() {
  }

  @Id
  @Column(name="FAACAGCO")
  public int getCodigo() {
    return this.codigo;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

  @Column(name="FAADAGCO")
  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Column(name="FAAFBAJA")
  public int getFechaBaja() {
    return fechaBaja;
  }

  public void setFechaBaja(int fechaBaja) {
    this.fechaBaja = fechaBaja;
  }
}

