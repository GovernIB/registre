import org.junit.Test;

public class comprovarComptadors2010 extends AdminTestBase {
	@Test
	public void testComprovarComptadors2010() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Inicialització comptador d'Entrades/Sortides");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("anyGestionar", "2011");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		verifyEquals("1-Oficina 1", selenium.getText("//form[@id='altaComptadorOficina']/table/tbody/tr[3]/td[1]"));
		verifyEquals("0", selenium.getText("//form[@id='altaComptadorOficina']/table/tbody/tr[3]/td[2]"));
		verifyEquals("0", selenium.getText("//form[@id='altaComptadorOficina']/table/tbody/tr[3]/td[3]"));
		verifyEquals("0", selenium.getText("//form[@id='altaComptadorOficina']/table/tbody/tr[3]/td[4]"));
	}
}
