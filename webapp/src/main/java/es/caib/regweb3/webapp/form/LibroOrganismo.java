package es.caib.regweb3.webapp.form;


/**
 * Created 2/10/14 11:13
 *
 * @author mgonzalez
 */
public class LibroOrganismo{

    private String libro;
    private String organismo;


  public LibroOrganismo(String libro, String organismo, String extinguido) {
    this.libro = libro;
    this.organismo = organismo;

  }

  public LibroOrganismo() {
  }

  public String getLibro() {
    return libro;
  }

  public void setLibro(String libro) {
    this.libro = libro;
  }

  public String getOrganismo() {
    return organismo;
  }

  public void setOrganismo(String organismo) {
    this.organismo = organismo;
  }
}
