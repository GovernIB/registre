import org.junit.Test;

public class comprovarAgrupacio90_10 extends AdminTestBase {

	@Test
	public void testComprovarAgrupacio90_10() throws Exception {
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
		verifyEquals("90", selenium.getValue("codTipuAgruGeo"));
		verifyEquals("10", selenium.getValue("codAgruGeo"));
		verifyEquals("Santa Margalida", selenium.getValue("descAgruGeo"));
		verifyEquals("2", selenium.getValue("codTipusAgruGeoSuperior"));
		verifyEquals("1", selenium.getValue("codAgruGeoSuperior"));
		verifyEquals("7450", selenium.getValue("codiPostal"));
		verifyTrue(selenium.isTextPresent("(Mallorca)"));
	}
}
