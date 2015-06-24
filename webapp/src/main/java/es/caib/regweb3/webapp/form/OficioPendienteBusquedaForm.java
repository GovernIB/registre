package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroEntrada;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class OficioPendienteBusquedaForm {

  private RegistroEntrada registroEntrada;
  private Integer pageNumber;
  private Integer anyo;


  public OficioPendienteBusquedaForm() {}

  public OficioPendienteBusquedaForm(RegistroEntrada registroEntrada, Integer pageNumber) {
    this.registroEntrada = registroEntrada;
    this.pageNumber = pageNumber;
  }

  public RegistroEntrada getRegistroEntrada() {
    return registroEntrada;
  }

  public void setRegistroEntrada(RegistroEntrada registroEntrada) {
    this.registroEntrada = registroEntrada;
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
