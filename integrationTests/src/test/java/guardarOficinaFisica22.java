import org.junit.Test;

public class guardarOficinaFisica22 extends AdminTestBase {
	@Test
	public void testGuardarOficinaFisica22() throws Exception {
        selenium.open("/regweb/index.jsp");
        assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
        selenium.click("link=Administració de l'aplicació");
        selenium.waitForPageToLoad("10000");
        assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
        selenium.click("link=Oficines físiques");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("oficinaGestionar", "2");
		selenium.type("oficinaGestionarFisica", "2");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("descOficina", "Oficina fisica 2");
		selenium.type("dataAlta", "01/04/2011");
		selenium.click("//input[@value=\"Dona d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
