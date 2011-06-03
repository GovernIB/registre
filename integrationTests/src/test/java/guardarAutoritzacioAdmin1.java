import org.junit.Test;

public class guardarAutoritzacioAdmin1 extends AdminTestBase {
	@Test
	public void testGuardarAutoritzacioAdmin1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Autoritzacions");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("usuariAutoritzar", "admin");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("1AE");
		selenium.click("1CE");
		selenium.click("1AS");
		selenium.click("1CS");
		selenium.click("1VE");
		selenium.click("1VS");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
