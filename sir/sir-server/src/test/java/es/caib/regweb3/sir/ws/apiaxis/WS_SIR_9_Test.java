package es.caib.regweb3.sir.ws.apiaxis;

import es.caib.regweb3.sir.ws.wssir9.RespuestaWS;
import es.caib.regweb3.sir.ws.wssir9.WS_SIR9ServiceLocator;
import es.caib.regweb3.sir.ws.wssir9.WS_SIR9_PortType;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by earrivi on 08/03/2017.
 */
public class WS_SIR_9_Test {

    @Test
    public void envioMensajeDatosControlAAplicacion() throws Exception {

        String registre3 = "http://registre3.fundaciobit.org/regweb3/ws/sir/v3/WS_SIR9?method=envioMensajeDatosControlAAplicacion";
        String local = "http://localhost:8080/regweb3/ws/sir/v3/WS_SIR9?method=envioMensajeDatosControlAAplicacion";

        WS_SIR9ServiceLocator locator = new WS_SIR9ServiceLocator();
        locator.setWS_SIR9EndpointAddress(registre3);

        WS_SIR9_PortType service = locator.getWS_SIR9();


        String ack = IOUtils.toString(new FileInputStream(new File("C:\\Users\\earrivi\\Documents\\Proyectos\\OTAE\\REGWEB3\\repositorio\\registre\\sir\\sir-server\\src\\test\\java\\es\\caib\\regweb3\\sir\\ws\\apiaxis\\ack.xml")), "UTF-8");


        RespuestaWS respuesta = service.envioMensajeDatosControlAAplicacion(ack,null);

        System.out.println("Codigo: " + respuesta.getCodigo());
        System.out.println("Respuesta: " + respuesta.getDescripcion());

    }
}
