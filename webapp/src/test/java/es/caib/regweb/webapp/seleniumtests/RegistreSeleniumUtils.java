package es.caib.regweb.webapp.seleniumtests;

/**
 * 
 * @author anadal
 *
 */
public class RegistreSeleniumUtils extends SeleniumUtils {

  private String registreUserId;

  @Override
  public void doLogin() {
    super.doLogin();
    registreUserId = getValueBetweenStrings(getContext() + "/usuario/", "/edit");
  }

  public String getRegistreUserId() {
    return registreUserId;
  }

  public void setRegistreUserId(String registreUserId) {
    this.registreUserId = registreUserId;
  }
  
  public void canviaRolAdministrador() {
    callUrl("/rol/1");
  }

  public void canviaRolAdministradorEntitat() {
    callUrl("/rol/2");
  }

  public void canviaRolOperador() {
    callUrl("/rol/3");
  }

}
