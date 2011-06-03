import junit.framework.Test;
import junit.framework.TestSuite;

public class AdministracioTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(guardarOficina1.class);
		suite.addTestSuite(comprovarOficina1.class);
		suite.addTestSuite(guardarOficinaFisica11.class);
		suite.addTestSuite(comprovarOficinaFisica11.class);
		suite.addTestSuite(guardarAutoritzacioAdmin1.class);
		suite.addTestSuite(comprovarAutoritzacioAdmin1.class);
		suite.addTestSuite(guardarOrganisme1.class);
		suite.addTestSuite(comprovarOrganisme1.class);
		suite.addTestSuite(guardarOrganismeOficina11.class);
		suite.addTestSuite(comprovarOrganismeOficina11.class);
		suite.addTestSuite(guardarEntitat11.class);
		suite.addTestSuite(comprovarEntitat11.class);
		suite.addTestSuite(guardarTipusDocumentA1.class);
		suite.addTestSuite(comprovarTipusDocumentA1.class);
		suite.addTestSuite(guardarComptadors2010.class);
		suite.addTestSuite(comprovarComptadors2010.class);
		suite.addTestSuite(guardarOfici1.class);
		suite.addTestSuite(comprovarOfici1.class);
		suite.addTestSuite(guardarRebut1.class);
		suite.addTestSuite(comprovarRebut1.class);
		suite.addTestSuite(guardarAgrupacio2_1.class);
		suite.addTestSuite(comprovarAgrupacio2_1.class);
		suite.addTestSuite(guardarAgrupacio90_10.class);
		suite.addTestSuite(comprovarAgrupacio90_10.class);
		suite.addTestSuite(guardarOficina2.class);
		suite.addTestSuite(guardarOficinaFisica22.class);
		suite.addTestSuite(guardarOrganisme2.class);
		suite.addTestSuite(guardarOrganismeOficina22.class);
		suite.addTestSuite(guardarTotsElsPermisos.class);
		suite.addTestSuite(guardarComptadors2011.class);
		return suite;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
}
