import org.junit.Test;

public class comprovarAutoritzacioAdmin1 extends AdminTestBase {
	@Test
	public void testComprovarAutoritzacioAdmin1() throws Exception {
		selenium.open("/regweb/index.jsp");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Administració de l'aplicació");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.click("link=Autoritzacions");
		selenium.waitForPageToLoad("10000");
		assertEquals("Registre de Entrades / Sortides", selenium.getTitle());
		selenium.type("usuariAutoritzar", "admin");
		selenium.click("//input[@value='Cercar']");
		selenium.waitForPageToLoad("10000");
		verifyTrue(selenium.isChecked("1AE"));
		verifyTrue(selenium.isChecked("1CE"));
		verifyTrue(selenium.isChecked("1AS"));
		verifyTrue(selenium.isChecked("1CS"));
		verifyTrue(selenium.isChecked("1VE"));
		verifyTrue(selenium.isChecked("1VS"));
	}

}
