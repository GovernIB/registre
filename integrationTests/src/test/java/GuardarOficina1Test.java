

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class GuardarOficina1Test extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://admin:admin@localhost:8080/regweb/index.jsp");
		selenium.start();
	}

	@Test
	public void testGuardarOficina1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		verifyEquals("Inici", selenium.getText("//ul[@id='mollaPa']/li"));
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		verifyEquals("Administració", selenium.getText("//ul[@id='mollaPa']/li[2]"));
		selenium.click("link=Oficines");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		verifyEquals("Gestió oficines", selenium.getText("//ul[@id='mollaPa']/li[3]"));
		selenium.type("oficinaGestionar", "1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		verifyTrue(selenium.isTextPresent("(1) no existeix"));
		selenium.type("descOficina", "Oficina 1");
		selenium.type("dataAlta", "01/01/2011");
		selenium.click("//input[@value=\"Dona d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		verifyEquals("Administració", selenium.getText("//ul[@id='mollaPa']/li[2]"));
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
