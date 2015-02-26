package es.caib.regweb.webapp.form;



/**
 * Created 25/04/14 14:42
 *
 * @author mgonzalez
 */
public class OrganismoREBusquedaForm {

  private String codigoOrganismo;
  private String denominacion;
  private Long codComunidadAutonoma;
  private Long codNivelAdministracion;

  public String getCodigoOrganismo() {
    return codigoOrganismo;
  }

  public void setCodigoOrganismo(String codigoOrganismo) {
    this.codigoOrganismo = codigoOrganismo;
  }

  public String getDenominacion() {
    return denominacion;
  }

  public void setDenominacion(String denominacion) {
    this.denominacion = denominacion;
  }

  public Long getCodComunidadAutonoma() {
    return codComunidadAutonoma;
  }

  public void setCodComunidadAutonoma(Long codComunidadAutonoma) {
    this.codComunidadAutonoma = codComunidadAutonoma;
  }

  public Long getCodNivelAdministracion() {
    return codNivelAdministracion;
  }

  public void setCodNivelAdministracion(Long codNivelAdministracion) {
    this.codNivelAdministracion = codNivelAdministracion;
  }
}
