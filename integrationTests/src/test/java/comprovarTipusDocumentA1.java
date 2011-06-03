import org.junit.Test;

public class comprovarTipusDocumentA1 extends AdminTestBase {
	@Test
	public void testComprovarTipusDocumentA1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Tipus de documents");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("tipDocGestionar", "A1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		verifyEquals("A1", selenium.getValue("codTipusDoc"));
		verifyEquals("Descripció tipus A1", selenium.getValue("descTipusDoc"));
	}
}
