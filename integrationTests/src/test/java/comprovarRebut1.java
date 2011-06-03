import org.junit.Test;

public class comprovarRebut1 extends AdminTestBase {
	@Test
	public void testComprovarRebut1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Models de rebuts");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("modelGestionar", "rebut1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		verifyEquals("rebut1", selenium.getText("//div[@id='menuDocAdm']/table/tbody/tr[2]/td[1]"));
	}
}
