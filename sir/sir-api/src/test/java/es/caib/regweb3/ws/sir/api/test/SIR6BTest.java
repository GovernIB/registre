package es.caib.regweb3.ws.sir.api.test;

import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.manager.FicheroIntercambioManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.manager.impl.FicheroIntercambioManagerImpl;
import es.caib.regweb3.sir.ws.api.manager.impl.SicresXMLManagerImpl;
import es.caib.regweb3.sir.ws.api.wssir6b.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_BSoapBindingStub;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_DirectApi;
import es.caib.regweb3.sir.ws.api.wssir6b.WS_SIR6_B_PortType;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ejb.EJB;
import java.io.File;

/**
 * @author anadal
 */
public class SIR6BTest extends SIRTestUtils {

    SicresXMLManager sicresXMLManager = new SicresXMLManagerImpl();
    FicheroIntercambioManager ficheroIntercambioManager = new FicheroIntercambioManagerImpl();

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    /**
     * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Test
    public void testDirectSir() throws Exception {
        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(getIdRegistroEntrada());
            FicheroIntercambio ficheroIntercambio = sicresXMLManager.crearFicheroIntercambioSICRES3(registroEntrada);

            String xml = ficheroIntercambio.marshallObject();
            /*String f = getTestArchivosPath();
            File file = new File(f);
            System.out.println("PATH = " + file.exists());
            String str = new String(FileUtils.readFileToByteArray(file));*/

            // Get target URL
            String strURL = getEndPoint(SIR_6_B); // "http://localhost:9999/services/WS_SIR6_B";
            // Get SOAP action

            RespuestaWS resp = WS_SIR6_B_DirectApi.recepcionFicheroDeAplicacion(xml, strURL);

            System.out.println("Code: " + resp.getCodigo());
            System.out.println("Desc: " + resp.getDescripcion());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void testSir() throws Exception {

        System.out.println(" ----------- testSIR ---------------- ");
        try {
            WS_SIR6_B_PortType sir6BApi = getSir6B();

            WS_SIR6_BSoapBindingStub binding = (WS_SIR6_BSoapBindingStub) sir6BApi;
            binding.setTimeout(10000);

            String f = getTestArchivosPath();

            File file = new File(f);

            System.out.println("PATH = " + file.exists());

            String str = new String(FileUtils.readFileToByteArray(file));

            System.out.println(str);

            RespuestaWS resp = sir6BApi.recepcionFicheroDeAplicacion(str);

            System.out.println("CODE: " + resp.getCodigo());
            System.out.println("DESC: " + resp.getDescripcion());
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
