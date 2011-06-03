import org.junit.Test;

public class comprovarOrganisme1 extends AdminTestBase {
	@Test
	public void testComprovarOrganisme1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Organismes");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("organismeGestionar", "1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		verifyEquals("Organisme 1", selenium.getValue("descCurtaOrganisme"));
		verifyEquals("Organisme número u", selenium.getValue("descLlargaOrganisme"));
		verifyTrue(selenium.isTextPresent("11/11/1999"));
	}
}
