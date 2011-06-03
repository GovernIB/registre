import org.junit.Test;

public class comprovarOficina1 extends AdminTestBase {
	@Test
	public void testComprovarOficina1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Oficines");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("oficinaGestionar", "1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		verifyEquals("Oficina 1", selenium.getValue("descOficina"));
	}
}
