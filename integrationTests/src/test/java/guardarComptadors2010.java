import org.junit.Test;

public class guardarComptadors2010 extends AdminTestBase {
	@Test
	public void testGuardarComptadors2010() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Inicialització comptador d'Entrades/Sortides");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("anyGestionar", "2011");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("1Ini");
		selenium.click("//input[@value='Desa']");
		assertTrue(selenium.getConfirmation().matches("^ALERTA: Esteu segurs que voleu inicialitzar aquests comptadors[\\s\\S]$"));
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
