package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.OficioRemision;
import es.caib.regweb3.model.utils.ObjetoBasico;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class OficioRemisionBusquedaForm {

  private OficioRemision oficioRemision;
  private Integer pageNumber;
  private Integer anyo;
  private ObjetoBasico tipoOficioRemision;
  private Integer estadoOficioRemision;


  public OficioRemisionBusquedaForm() {}

  public OficioRemisionBusquedaForm(OficioRemision oficioRemision, Integer pageNumber) {
    this.oficioRemision = oficioRemision;
    this.pageNumber = pageNumber;
  }

    public OficioRemision getOficioRemision() {
        return oficioRemision;
    }

    public void setOficioRemision(OficioRemision oficioRemision) {
        this.oficioRemision = oficioRemision;
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

  public ObjetoBasico getTipoOficioRemision() {
    return tipoOficioRemision;
  }

  public void setTipoOficioRemision(ObjetoBasico tipoOficioRemision) {
    this.tipoOficioRemision = tipoOficioRemision;
  }

  public Integer getEstadoOficioRemision() {
    return estadoOficioRemision;
  }

  public void setEstadoOficioRemision(Integer estadoOficioRemision) {
    this.estadoOficioRemision = estadoOficioRemision;
  }
}
