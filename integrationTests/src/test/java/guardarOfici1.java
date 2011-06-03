import org.junit.Test;

import java.net.URL;

public class guardarOfici1 extends AdminTestBase {
	@Test
	public void testGuardarOfici1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Models d'oficis de remissió");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("modelGestionar", "ofici1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
        URL url = getClass().getResource("examples/Ofici.rtf");
        selenium.type("fitxer", url.toString());
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		verifyEquals("S'ha desat el model d'ofici de remissió.", selenium.getText("//div[@id='menuDocAdm']/div[1]/b"));
	}
}
