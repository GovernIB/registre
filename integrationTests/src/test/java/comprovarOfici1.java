import org.junit.Test;

public class comprovarOfici1 extends AdminTestBase {
	@Test
	public void testComprovarOfici1() throws Exception {
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
		verifyEquals("ofici1", selenium.getText("//div[@id='menuDocAdm']/table/tbody/tr[2]/td[1]"));
	}
}
