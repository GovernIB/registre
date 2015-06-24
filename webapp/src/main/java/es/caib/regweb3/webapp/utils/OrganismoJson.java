package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.webapp.form.LibroOrganismo;

import java.util.List;

/**
 * Created 7/11/14 9:32
 *
 * @author mgonzalez
 */
public class OrganismoJson {
  String id;
  String nombre;
  List<LibroOrganismo> libroOrganismos;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public List<LibroOrganismo> getLibroOrganismos() {
    return libroOrganismos;
  }

  public void setLibroOrganismos(List<LibroOrganismo> libroOrganismos) {
    this.libroOrganismos = libroOrganismos;
  }
}
