import org.junit.Test;

public class guardarAgrupacio90_10 extends AdminTestBase {
	@Test
	public void testGuardarAgrupacio90_10() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Agrupacions geogràfiques");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("tipusAgruGeoGestionar", "90");
		selenium.type("codiAgruGeoGestionar", "10");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		verifyTrue(selenium.isTextPresent("(90 - 10) no existeix"));
		selenium.type("descAgruGeo", "Santa Margalida");
		selenium.type("codTipusAgruGeoSuperior", "2");
		selenium.type("codAgruGeoSuperior", "1");
		selenium.type("codiPostal", "07450");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
