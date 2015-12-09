package es.caib.regweb3.webapp.seleniumtests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * 
 * @author earrivi
 *
 */
public class NuevoUsuarioExistente extends RegistreSeleniumUtils {

  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
  }

  @Test
  public void testNuevoUsuarioExistente() throws Exception {
    doLogin();

    canviaRolAdministradorEntitat();

    callUrl("/entidad/usuarios");

    BufferedReader br = null;

    try {

      String sCurrentLine;

      br = new BufferedReader(new FileReader("usuarios.txt"));

      // Añadir usuario existente en Seycon
      while ((sCurrentLine = br.readLine()) != null) {
        driver.findElement(By.linkText("Introduir usuari")).click();
        driver.findElement(By.id("identificador")).clear();
        driver.findElement(By.id("identificador")).sendKeys(sCurrentLine);
        driver.findElement(By.xpath("//input[@value='Desar']")).click();
      }

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)br.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }



    //assertPaginaWebConte("S´ha creat el nou usuari associat a la seva Entitat", SearchType.HTMLTAGS);

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
