import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;
import org.junit.After;
import org.junit.Before;

/**
 * 
 */
public class AdminTestBase extends SeleneseTestCase {

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://admin:admin@localhost:8080/regweb/");
		selenium.start();
	}

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }

}
