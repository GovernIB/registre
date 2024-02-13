package es.caib.regweb3.sir.ws.apiaxis;

import es.caib.regweb3.sir.ws.wssir8b.RespuestaWS;
import es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_BServiceLocator;
import es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_B_PortType;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by earrivi on 08/03/2017.
 */
public class WS_SIR_8B_Test {

    @Test
    public void envioFicherosAAplicacion() throws Exception {

        String registre3 = "http://registre3.fundaciobit.org/regweb3/ws/sir/v3/WS_SIR8_B?method=envioFicherosAAplicacion";
        String local = "http://10.215.2.201:8080/regweb3/ws/sir/v3/WS_SIR8_B?method=envioFicherosAAplicacion";

        WS_SIR8_BServiceLocator locator = new WS_SIR8_BServiceLocator();
        locator.setWS_SIR8_BEndpointAddress(local);
        WS_SIR8_B_PortType service = locator.getWS_SIR8_B();


        String registro = IOUtils.toString(new FileInputStream(new File("C:\\Users\\e43146650f\\Documents\\Proyectos\\REGWEB3\\registre-4.0\\codigo\\sir\\sir-server\\src\\test\\java\\es\\caib\\regweb3\\sir\\ws\\apiaxis\\sircall.xml")), "UTF-8");


        RespuestaWS respuesta = service.envioFicherosAAplicacion(registro,null);

        System.out.println("Codigo: " + respuesta.getCodigo());
        System.out.println("Respuesta: " + respuesta.getDescripcion());

    }
}
