import org.junit.Test;

public class guardarTotsElsPermisos extends AdminTestBase {
	@Test
	public void testGuardarTotsElsPermisos() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Autoritzacions");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("usuariAutoritzar", "registro1");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("1AE");
		selenium.click("1CE");
		selenium.click("1AS");
		selenium.click("1CS");
		selenium.click("2CE");
		selenium.click("2CS");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("usuariAutoritzar", "registro2");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("2AE");
		selenium.click("1CE");
		selenium.click("2CE");
		selenium.click("2AS");
		selenium.click("1CS");
		selenium.click("2CS");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("usuariAutoritzar", "lopd");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("AEtots");
		selenium.click("CEtots");
		selenium.click("AStots");
		selenium.click("CStots");
		selenium.click("VEtots");
		selenium.click("VStots");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("usuariAutoritzar", "admin");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("2AE");
		selenium.click("2AS");
		selenium.click("2VE");
		selenium.click("2VS");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
