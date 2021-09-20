package es.caib.regweb3.webapp.seleniumtests;

import junit.framework.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 * TODO Moure a GenApp-Web
 * 
 * @author anadal
 *
 */
public class SeleniumUtils {

  private static Properties testProperties = new Properties();

  public final Logger log = LoggerFactory.getLogger(this.getClass());

  public enum SearchType {
    HTMLCODE, HTMLTAGS
  };

  static {

    // Propietats del Servidor
    try {
      System.out.println("Cercant fitxer de propietats dins "
          + new File(".").getAbsolutePath());
      testProperties.load(new FileInputStream("test.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public WebDriver driver;

  public String getBaseUrl() {
    return testProperties.getProperty("baseUrl", "http://localhost:8080");
  }

  public String getContext() {
    return testProperties.getProperty("context", "");
  }

  public String getUsername() {
    return testProperties.getProperty("username");
  }
  
  public int getTimeout() {
    return Integer.parseInt(testProperties.getProperty("timeoutseconds", "30"));
  }

  public String getPassword() {
    return testProperties.getProperty("password");
  }

  public boolean isCAIB() {
    return "true".equals(testProperties.getProperty("iscaib"));
  }
  
  protected void initDriver() {
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(getTimeout(), TimeUnit.SECONDS);
  }
  

  public void doLogin() {
    if (isCAIB()) {
      driver.get(getBaseUrl() + getContext());
      driver.findElement(By.id("j_username")).clear();
      driver.findElement(By.id("j_username")).sendKeys(getUsername());
      driver.findElement(By.id("j_password")).clear();
      driver.findElement(By.id("j_password")).sendKeys(getPassword());
      driver.findElement(By.name("formUCboton")).click();
    } else {
      String url = getBaseUrl();
      int pos = url.indexOf("//");
      url = url.substring(0, pos + 2) + getUsername() + ":" + getPassword() + "@"
          + url.substring(pos + 2);
      String loginurl = url + getContext() + "/";
      //System.out.println(" UUUUUU   = " + loginurl);
      driver.get(loginurl );
    }
  }



  public void callUrlAbsolute(String url) {
    driver.get(url);
  }

  public void callUrlWithContext(String url) {
    driver.get(getBaseUrl() + url);
  }

  public void callUrl(String url) {
    driver.get(getBaseUrl() + getContext() + url);
  }

  public String getHtmlCode() {
    return driver.findElement(By.tagName("html")).getAttribute("innerHTML");
  }

  public String getHtmlStrings() {
    return driver.findElement(By.tagName("html")).getText();
  }

  public void assertPaginaWebConte(String text) {
    assertPaginaWebConte(text, SearchType.HTMLCODE);
  }

  public void assertPaginaWebConte(String text, SearchType type) {

    String html = (type == SearchType.HTMLCODE) ? getHtmlCode() : getHtmlStrings();
    Assert.assertTrue("No s'ha trobat el text ]" + text + "[ dins la pàgina web:\n" + html
        + "\n", html.contains(text));
  }
  
  
  public void assertPaginaWebNoConte(String text) {
    assertPaginaWebNoConte(text, SearchType.HTMLCODE);
  }
  
  public void assertPaginaWebNoConte(String text, SearchType type) {

    String html = (type == SearchType.HTMLCODE) ? getHtmlCode() : getHtmlStrings();
    Assert.assertTrue("S'ha trobat el text ]" + text + "[ dins la pàgina web:\n" + html
        + "\n",!html.contains(text));
  }

  public String getValueBetweenStrings(String pre, String post) {
    return getValueBetweenStrings(pre, post, SearchType.HTMLCODE);
  }

  public String getValueBetweenStrings(String pre, String post, SearchType type) {

    String html = (type == SearchType.HTMLCODE) ? getHtmlCode() : getHtmlStrings();

    return getValueBetweenStringOfString(html, pre, post);

  }

  public String getValueBetweenStringOfString(String html, String pre, String post) {
    int pos1 = html.indexOf(pre);
    if (pos1 == -1) {
      log.warn("No s'ha trobat el text PRE ]" + pre + "[ dins la pàgina web:\n" + html + "\n");
      return null;
    }

    int pos2 = html.indexOf(post, pos1 + pre.length());
    if (pos2 == -1) {
      log.warn("No s'ha trobat el text POST]" + post + "[ dins la pàgina web:\n" + html + "\n");
      return null;
    }

    return html.substring(pos1 + pre.length(), pos2);
  }

}
