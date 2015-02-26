package es.caib.regweb.webapp.seleniumtests;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
/**
 * 
 * @author anadal
 *
 */
public class NouUsuari extends RegistreSeleniumUtils {

  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
  }

  @Test
  public void testNouUsuari() throws Exception {
    doLogin();

    canviaRolAdministrador();

    callUrl("/usuario/list"); // findElement(By.linkText("Gestió usuaris")).click();

    // Verificar que usuari no existeix
    final String identificador = "test";

    driver.findElement(By.id("usuario.identificador")).clear();
    driver.findElement(By.id("usuario.identificador")).sendKeys(identificador);
    driver.findElement(By.xpath("//input[@value='Cercar']")).click();

    assertPaginaWebConte("No hi ha cap Usuari", SearchType.HTMLTAGS);

    // Crear usuari
    driver.findElement(By.linkText("Introduir usuari")).click();
    driver.findElement(By.id("nombre")).clear();
    driver.findElement(By.id("nombre")).sendKeys("NomTest");
    driver.findElement(By.id("documento")).clear();
    driver.findElement(By.id("documento")).sendKeys("12345678Z");
    driver.findElement(By.id("apellido1")).clear();
    driver.findElement(By.id("apellido1")).sendKeys("Llinatge1Test");
    driver.findElement(By.id("apellido2")).clear();
    driver.findElement(By.id("apellido2")).sendKeys("Llinatge2Test");
    driver.findElement(By.id("identificador")).clear();

    driver.findElement(By.id("identificador")).sendKeys(identificador);
    driver.findElement(By.id("email")).clear();
    final String mail = "test@fundaciobit.org";
    driver.findElement(By.id("email")).sendKeys(mail);
    driver.findElement(By.xpath("//input[@value='Desar']")).click();

    assertPaginaWebConte("S'ha realitzat correctament l'alta", SearchType.HTMLTAGS);

    // Cercam l'usuari que acabam de realitzar i validar que surt al llistat

    driver.findElement(By.id("usuario.identificador")).clear();
    driver.findElement(By.id("usuario.identificador")).sendKeys(identificador);
    driver.findElement(By.xpath("//input[@value='Cercar']")).click();

    assertPaginaWebConte(mail);

    // Borram l'usuari
    // TODO confirmRW
    String idusuari = getValueBetweenStrings("javascript:confirmRW(&quot;/regweb/usuario/",
        "/delete");
    System.out.println("ID = " + idusuari);

    acceptNextAlert = false;

    driver.findElement(By.cssSelector("span.fa.fa-eraser")).click();
    // TODO No funciona amb PopUps
    // assertEquals("/regweb/usuario/" + idusuari + "/delete",
    // closeAlertAndGetItsText());
    driver.findElement(By.id("okButton")).click();

    assertPaginaWebConte("S'ha realitzat correctament la baixa", SearchType.HTMLTAGS);

    // Cercam l'usuari que acabam de borrar
    driver.findElement(By.id("usuario.identificador")).clear();
    driver.findElement(By.id("usuario.identificador")).sendKeys(identificador);
    driver.findElement(By.xpath("//input[@value='Cercar']")).click();

    assertPaginaWebConte("No hi ha cap Usuari", SearchType.HTMLTAGS);

    System.out.println(" OK ");

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
