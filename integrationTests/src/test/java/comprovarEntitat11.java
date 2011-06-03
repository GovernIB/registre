import org.junit.Test;

public class comprovarEntitat11 extends AdminTestBase {
	@Test
	public void testComprovarEntitat11() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Entitats");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("entitatGestionar", "1");
		selenium.type("subentitatGestionar", "1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		verifyEquals("1", selenium.getValue("codEntidad"));
		verifyEquals("1", selenium.getValue("codEntitat"));
		verifyEquals("1", selenium.getValue("subcodEntitat"));
		verifyEquals("Entitat 1 castellà", selenium.getValue("descEntidad"));
		verifyEquals("Entitat 1 català", selenium.getValue("descEntitat"));
	}
}
