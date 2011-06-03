import org.junit.Test;

public class guardarOrganisme2 extends AdminTestBase {
	@Test
	public void testGuardarOrganisme2() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Organismes");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("organismeGestionar", "2");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("descCurtaOrganisme", "Organisme 2");
		selenium.type("descLlargaOrganisme", "Organisme número dos");
		selenium.type("dataAlta", "11/11/1997");
		selenium.click("//input[@value=\"Dona d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
