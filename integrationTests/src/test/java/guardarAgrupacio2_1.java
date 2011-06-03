import org.junit.Test;

public class guardarAgrupacio2_1 extends AdminTestBase {
	@Test
	public void testGuardarAgrupacio2_1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Agrupacions geogràfiques");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("tipusAgruGeoGestionar", "2");
		selenium.type("codiAgruGeoGestionar", "1");
		selenium.click("//input[@value=\"Cercar o donar d'alta\"]");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("descAgruGeo", "Mallorca");
		selenium.type("codTipusAgruGeoSuperior", "0");
		selenium.type("codAgruGeoSuperior", "0");
		selenium.type("codiPostal", "0");
		selenium.click("//input[@value='Desa']");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
	}
}
