import org.junit.Test;

public class comprovarAgrupacio2_1 extends AdminTestBase {

	@Test
	public void testComprovarAgrupacio2_1() throws Exception {
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
		verifyEquals("Mallorca", selenium.getValue("descAgruGeo"));
		verifyEquals("2", selenium.getValue("codTipuAgruGeo"));
		verifyEquals("1", selenium.getValue("codAgruGeo"));
	}
}
