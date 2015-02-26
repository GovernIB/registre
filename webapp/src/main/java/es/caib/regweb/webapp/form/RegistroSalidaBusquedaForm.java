package es.caib.regweb.webapp.form;

import es.caib.regweb.model.RegistroSalida;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class RegistroSalidaBusquedaForm {

  private RegistroSalida registroSalida;
  private Integer pageNumber;
  private Integer anyo;


  public RegistroSalidaBusquedaForm() {}

  public RegistroSalidaBusquedaForm(RegistroSalida registroSalida, Integer pageNumber) {
    this.registroSalida = registroSalida;
    this.pageNumber = pageNumber;
  }

  public RegistroSalida getRegistroSalida() {
    return registroSalida;
  }

  public void setRegistroSalida(RegistroSalida registroSalida) {
    this.registroSalida = registroSalida;
  }

  public Integer getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(Integer pageNumber) {
    this.pageNumber = pageNumber;
  }

  public Integer getAnyo() {
    return anyo;
  }

  public void setAnyo(Integer anyo) {
    this.anyo = anyo;
  }
}
