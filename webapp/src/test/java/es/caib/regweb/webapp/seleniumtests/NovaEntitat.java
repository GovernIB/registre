package es.caib.regweb.webapp.seleniumtests;

import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
 *
 * @author anadal
 *
 */
public class NovaEntitat extends RegistreSeleniumUtils {

  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
  }

  @Test
  public void testNovaEntitat() throws Exception {
    doLogin();

    canviaRolAdministrador();

    callUrl("/entidad/list/1");

    final String nomEntitat = "UNIVERSITAT DE LES ILLES BALEARS";
    final String codiDir3 = "U00000096";

    // Verificar que entitat no existeix
    {
      String htmlCode = getHtmlCode();
      if (htmlCode.indexOf(nomEntitat) != -1) {
        fail("L'entitat [" + nomEntitat + "] ja existeix.\n\n" + htmlCode);
      }
    }

    // Nova Entitat
    driver.findElement(By.linkText("Introduir entitat")).click();
    driver.findElement(By.id("entidad.nombre")).clear();
    driver.findElement(By.id("entidad.nombre")).sendKeys(nomEntitat);
    driver.findElement(By.id("entidad.descripcion")).clear();
    driver.findElement(By.id("entidad.descripcion")).sendKeys(nomEntitat);
    driver.findElement(By.id("entidad.codigoDir3")).clear();
    driver.findElement(By.id("entidad.codigoDir3")).sendKeys(codiDir3);

    System.out.println(" CODI ID USUARI = " + getRegistreUserId());

    Select select = new Select(driver.findElement(By.id("entidad.propietario.id")));
    select.selectByValue(getRegistreUserId().trim());

    driver.findElement(By.xpath("//input[@value='Desar']")).click();

    assertPaginaWebConte("S'ha realitzat correctament l'alta", SearchType.HTMLTAGS);

    assertPaginaWebConte(nomEntitat);

    // Cercam en quina fila es troba la nova entrada
    int fila = -1;
    String entitatId = null;
    for (int f = 1; f < 15; f++) {
      WebElement we = driver.findElement(By.xpath("//tr[" + f + "]/td[1]"));
      // System.out.println(" ZZZZZZZZZZZZZ ]" + we + "[");
      // System.out.println(" XXXXXXXXXXXXX ]" + we.getText() + "[");
      // System.out.println(" FFFFFFF ]" + by.findElement(SearchContext.) +
      // "[");
      if (nomEntitat.trim().equals(we.getText().trim())) {
        fila = f;

        WebElement we2 = driver.findElement(By.xpath("//tr[" + f + "]/td[3]"));
        // System.out.println(" TD = " + we2.getAttribute("innerHTML"));

        entitatId = getValueBetweenStringOfString(we2.getAttribute("innerHTML"),
            "/regweb/entidad/", "/edit");

        break;
      }
    }

    if (fila == -1) {
      fail("No s'ha trobat l'Entitat " + nomEntitat + " dins la columna de nom\n\n"
          + getHtmlCode());
    }
    System.out.println("FILA = " + fila);
    System.out.println("ENTITAT_ID = " + entitatId);

    // Editar Entrada
    driver.findElement(By.xpath("//tr[" + fila + "]/td[3]/a/span")).click();
    driver.findElement(By.xpath("//input[@value='Desar']")).click();

    assertPaginaWebConte("S'ha realitzat correctament la modificació", SearchType.HTMLTAGS);

    // Editar dins Administrador d'Entitat

    canviaRolAdministradorEntitat(); // driver.findElement(By.linkText("Admin. Entitat")).click();
    callUrl("/cambioEntidad/" + entitatId); // driver.findElement(By.linkText("Ajuntament Formentera")).click();
    callUrl("/entidad/" + entitatId + "/edit"); // driver.findElement(By.linkText("Editar entitat")).click();
    driver.findElement(By.linkText("Formats")).click();
    driver.findElement(By.id("entidad.sello")).clear();
    driver
        .findElement(By.id("entidad.sello"))
        .sendKeys(
            "${nomOficina}\nNúm: ${formatNumRegistre}\nData: ${dataRegistre}\nOrigen/Destinatari: ${origen}${destinatari}");
    driver.findElement(By.id("entidad.numRegistro")).clear();
    driver.findElement(By.id("entidad.numRegistro")).sendKeys(
        "${numLlibre}-${tipusRegistre}-${numRegistre}/${anyRegistre}");
    driver.findElement(By.linkText("Ajuda")).click();
    driver.findElement(By.linkText("Tancar")).click();
    driver.findElement(By.xpath("(//a[contains(text(),'Ajuda')])[2]")).click();
    driver
        .findElement(
            By.cssSelector("#myModalNumRegistro > div.modal-dialog > div.modal-content > div.modal-footer > a.btn.btn-warning"))
        .click();
    driver.findElement(By.xpath("(//a[contains(text(),'Configuració')])[2]")).click();
    driver.findElement(By.linkText("Dades")).click();
    driver.findElement(By.id("entidad.sir1")).click();
    driver.findElement(By.xpath("//input[@value='Desar']")).click();

    assertPaginaWebConte("S'ha realitzat correctament la modificació", SearchType.HTMLTAGS);



    // ------------------------------
    // TODO Pendent que es solucioni Tiquet   0000103 de mantis
    // Quan es crea un usuari encara que despres es borri ja no deixa borrar l'entitat
    /*
    callUrl("/entidad/usuarios");  //driver.findElement(By.linkText("Gestió usuaris")).click();
    callUrl("/usuario/existeUsuario"); //driver.findElement(By.linkText("Introduir usuari")).click();
    driver.findElement(By.id("identificador")).clear();
    driver.findElement(By.id("identificador")).sendKeys(getUsername());
    driver.findElement(By.xpath("//input[@value='Desar']")).click();
    driver.findElement
    (By.id("usuarioEntidad.usuario.identificador")).clear();
    driver.findElement
    (By.id("usuarioEntidad.usuario.identificador")).sendKeys("anadal");
    driver.findElement(By.xpath("//input[@value='Cercar']")).click();
     
    assertPaginaWebConte("S'ha creat el nou usuari associat a la seva Entitat", SearchType.HTMLTAGS);
    */

    // ------------------------------
    
    // TIPUS DOCUMENTAL 
    
    /*
    - TD01 - Resolución.
    - TD02 - Acuerdo.
    - TD03 - Contrato.
    - TD04 - Convenio.
    - TD05 - Declaración.
    // Documentos de transmisión
    - TD06 - Comunicación.
    - TD07 - Notificación.
    - TD08 - Publicación.
    - TD09 - Acuse de recibo.
    // Documentos de constancia
    - TD10 - Acta.
    - TD11 - Certificado.
    - TD12 - Diligencia.
    // Documentos de juicio
    - TD13 - Informe.
    // Documentos de ciudadano
    - TD14 - Solicitud.
    - TD15 - Denuncia.
    - TD16 - Alegación.
    - TD17 - Recursos.
    - TD18 - Comunicación ciudadano.
    - TD19 - Factura.
    - TD20 - Otros incautados.
    // Otros
    - TD99 - Otros
    */
    
    callUrl("/tipoDocumental/list/1");  // driver.findElement(By.linkText("Gestió Tipus documental")).click();
    callUrl("/tipoDocumental/new"); // driver.findElement(By.linkText("Introduir tipus de documental")).click();
    //driver.findElement(By.linkText("Marcatges")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | ibi_central | ]]
    // ERROR: Caught exception [Error: Dom locators are not implemented yet!]
    driver.findElement(By.id("codigoNTI")).clear();
    driver.findElement(By.id("codigoNTI")).sendKeys("TD01");
    driver.findElement(By.id("traducciones'ca'.nombre")).clear();
    driver.findElement(By.id("traducciones'ca'.nombre")).sendKeys("Resolució");
    driver.findElement(By.xpath("(//a[contains(text(),'Castellano')])[2]")).click();
    driver.findElement(By.id("traducciones'es'.nombre")).clear();
    driver.findElement(By.id("traducciones'es'.nombre")).sendKeys("Resolución");
    driver.findElement(By.xpath("//input[@value='Desar']")).click();

    // Despres de desar torna al llistat, revisar si s'ha creat TD01
    assertPaginaWebConte("TD01");
    
    
    // Borrar tipus Documental
    //acceptNextAlert = false;
    driver.findElement(By.cssSelector("span.fa.fa-eraser")).click();
    //assertEquals("/regweb/tipoDocumental/12444/delete", closeAlertAndGetItsText());
    driver.findElement(By.id("okButton")).click();

    
    
    
    assertPaginaWebConte("S'ha realitzat correctament la baixa", SearchType.HTMLTAGS);
    
    
    // ---------------------------------
    // TIPO ASUNTO
    
    // Crear    
    callUrl("/tipoAsunto/list/1"); // driver.findElement(By.linkText("Gestió Tipus assumpte")).click();

    callUrl("/tipoAsunto/new"); // driver.findElement(By.linkText("Introduir tipus assumpte")).click();
    driver.findElement(By.id("codigo")).clear();
    driver.findElement(By.id("codigo")).sendKeys("AS01");
    driver.findElement(By.id("traducciones'ca'.nombre")).clear();
    driver.findElement(By.id("traducciones'ca'.nombre")).sendKeys("Ajudes i subvencions");
    driver.findElement(By.xpath("(//a[contains(text(),'Castellano')])[2]")).click();
    driver.findElement(By.id("traducciones'es'.nombre")).clear();
    driver.findElement(By.id("traducciones'es'.nombre")).sendKeys("Ayudas y subvenciones");
    driver.findElement(By.xpath("//input[@value='Desar']")).click();
    
    assertPaginaWebConte("S'ha realitzat correctament l'alta", SearchType.HTMLTAGS);
    
    
    // Borrar 
    //acceptNextAlert = false;
    driver.findElement(By.cssSelector("span.fa.fa-eraser")).click();
    //assertEquals("/regweb/tipoAsunto/12450/delete", closeAlertAndGetItsText());
    driver.findElement(By.id("okButton")).click();

    // Tornam a Administrador
    canviaRolAdministrador();

    callUrl("/entidad/list/1");

    // Borrar Entitat
    driver.findElement(By.xpath("//tr[" + fila + "]/td[3]/a/span")).click();
    acceptNextAlert = false;
    driver.findElement(By.xpath("//input[@value='Eliminar']")).click();
    // assertEquals("/regweb/entidad/12411/delete", closeAlertAndGetItsText());
    driver.findElement(By.id("okButton")).click();

    assertPaginaWebConte("S'ha realitzat correctament la baixa", SearchType.HTMLTAGS);

    // Verificar que entitat no existeix
    {
      String htmlCode = getHtmlCode();
      if (htmlCode.indexOf(nomEntitat) != -1) {
        fail("L'entitat [" + nomEntitat + "] ja existeix.\n\n" + htmlCode);
      }
    }

  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  protected boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  protected boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  protected String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
