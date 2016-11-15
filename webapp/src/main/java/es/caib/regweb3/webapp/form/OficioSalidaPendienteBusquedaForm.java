package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroSalida;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class OficioSalidaPendienteBusquedaForm {

  private RegistroSalida registroSalida;
  private Organismo destinatario;
  private Integer pageNumber;
  private Integer anyo;


  public OficioSalidaPendienteBusquedaForm() {}

  public OficioSalidaPendienteBusquedaForm(RegistroSalida registroSalida, Integer pageNumber) {
    this.registroSalida = registroSalida;
    this.pageNumber = pageNumber;
  }

  public RegistroSalida getRegistroSalida() {
    return registroSalida;
  }

  public void setRegistroSalida(RegistroSalida registroSalida) {
    this.registroSalida = registroSalida;
  }

  public Organismo getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(Organismo destinatario) {
    this.destinatario = destinatario;
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
