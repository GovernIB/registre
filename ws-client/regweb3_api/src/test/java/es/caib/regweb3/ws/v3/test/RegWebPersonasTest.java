package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.PersonaWs;
import es.caib.regweb3.ws.api.v3.RegWebPersonasWs;
import es.caib.regweb3.ws.api.v3.WsI18NException;
import es.caib.regweb3.ws.api.v3.WsValidationException;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * 
 * 
 * @author anadal
 * 
 */
public class RegWebPersonasTest extends RegWebTestUtils {

  protected static RegWebPersonasWs personasApi;

  /**
   * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
   * 
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    personasApi = getPersonasApi();
  }

  @Test
  public void testVersio() throws Exception {
    String version = personasApi.getVersion();
    if (version.indexOf('-') != -1) {
      Assert.assertEquals("3.0.0-caib", version);
    } else {
      Assert.assertEquals("3.0.0", version);
    }
  }

  @Test
  public void testVersioWs() throws Exception {
    Assert.assertEquals(3, personasApi.getVersionWs());
  }

  @Test
  public void testCreatePersona()  {
    
    PersonaWs personaWs = new PersonaWs();
    
    try {
      personaWs.setEntidadDir3ID(null);

      personasApi.crearPersona(personaWs);
      
      Assert.fail("Hauria d'haver llançat un error");

    } catch (WsI18NException i18ne) {
      System.err.println(WsClientUtils.toString(i18ne));
      Assert.assertEquals("error.valor.requerido.ws", i18ne.getFaultInfo().getTranslation().getCode());
    } catch (WsValidationException ve) {
      System.err.println(WsClientUtils.toString(ve));
      Assert.fail("WsValidationException no esperada");
    } catch (Exception e) {
      System.err.println("Error desconegut(" + e.getClass().getName() + "): " + e.getMessage());
      e.printStackTrace();
      Assert.fail("WsValidationException no esperada");
    }

    
    try {

      personaWs.setEntidadDir3ID(getTestEntidadCodigoDir3());
      personaWs.setDocumento("12345678Z");
      personaWs.setTipoDocumentoIdentificacionNTI(String.valueOf(RegwebConstantes.TIPODOCUMENTOID_NIF));
      personaWs.setCanal(RegwebConstantes.CANAL_DIRECCION_ELECTRONICA);
      personaWs.setTipoPersonaID(RegwebConstantes.TIPO_USUARIO_PERSONA);

      personasApi.crearPersona(personaWs);
      
      Assert.fail("Hauria d'haver llançat un error");

    } catch (WsI18NException i18ne) {
      System.err.println(WsClientUtils.toString(i18ne));
      Assert.fail("WsValidationException no esperada");
    } catch (WsValidationException ve) {
      System.err.println(WsClientUtils.toString(ve));
      Assert.assertEquals(1, ve.getFaultInfo().getFieldFaults().size());
    } catch (Exception e) {
      System.err.println("Error desconegut: " + e.getMessage());
      e.printStackTrace();
      Assert.fail("WsValidationException no esperada");
    }
    
    
    Long id = null;
    try {

      personaWs.setNombre("ProvaNom");
      personaWs.setApellido1("ProvaLlinatge1");
      personaWs.setDireccionElectronica("prova@prova.com");

      id = personasApi.crearPersona(personaWs);

      System.out.println("Creat usuari amb ID = " + id);
      
      //personasApi.borrarPersona(id);


    } catch (WsI18NException i18ne) {
      System.err.println(WsClientUtils.toString(i18ne));
      Assert.fail("WsI18NException no esperada");
    } catch (WsValidationException ve) {
      System.err.println(WsClientUtils.toString(ve));
      Assert.assertEquals(3, ve.getFaultInfo().getFieldFaults().size());
    } catch (Exception e) {
      System.err.println("Error desconegut: " + e.getMessage());
      e.printStackTrace();
      Assert.fail("WsValidationException no esperada");
    } finally {
      if (id != null) {
        try {
          personasApi.borrarPersona(id);
        } catch (WsI18NException e) {
          System.err.println(WsClientUtils.toString(e));
          Assert.fail("WsI18NException no esperada borrant persona");
        }
      }
    }

    // test de marilen post eliminar representante, representado,...
    Long id2 = null;
    try {
      personaWs.setEntidadDir3ID(getTestEntidadCodigoDir3());
      personaWs.setNombre("ProvaNom");
      personaWs.setApellido1("ProvaLlinatge1");
      personaWs.setDireccionElectronica("prova@prova.com");
      personaWs.setTipoPersonaID(RegwebConstantes.TIPO_PERSONA_FISICA);

      id2 = personasApi.crearPersona(personaWs);

      System.out.println("Creat usuari amb ID = " + id2);

      //personasApi.borrarPersona(id);


    } catch (WsI18NException i18ne) {
      System.err.println(WsClientUtils.toString(i18ne));
      Assert.fail("WsI18NException no esperada");
    } catch (WsValidationException ve) {
      System.err.println(WsClientUtils.toString(ve));
      Assert.assertEquals(3, ve.getFaultInfo().getFieldFaults().size());
    } catch (Exception e) {
      System.err.println("Error desconegut: " + e.getMessage());
      e.printStackTrace();
      Assert.fail("WsValidationException no esperada");
    } finally {

    }
    
    
  }

}
