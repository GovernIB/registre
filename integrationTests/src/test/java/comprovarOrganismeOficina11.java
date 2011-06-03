import org.junit.Test;

public class comprovarOrganismeOficina11 extends AdminTestBase {
	@Test
	public void testComprovarOrganismeOficina11() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Organismes per oficina");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("oficinaGestionar", "1");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		verifyTrue(selenium.isChecked("org1"));
		verifyFalse(selenium.isChecked("rem1"));
		verifyTrue(selenium.isTextPresent("1 - Organisme número u"));
	}
}
