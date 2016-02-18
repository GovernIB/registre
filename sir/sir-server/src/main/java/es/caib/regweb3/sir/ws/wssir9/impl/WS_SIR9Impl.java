package es.caib.regweb3.sir.ws.wssir9.impl;

import es.caib.regweb3.sir.ws.wssir9.RespuestaWS;
import es.caib.regweb3.sir.ws.wssir9.WS_SIR9_PortType;
import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@Stateless(name = WS_SIR9Impl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService(
        name = WS_SIR9Impl.NAME_WS,
        portName = WS_SIR9Impl.NAME_WS,
        serviceName = WS_SIR9Impl.NAME_WS + "Service"
)
@WebContext(
        contextRoot = "/regweb3/ws/sir",
        urlPattern = "/v3/" + WS_SIR9Impl.NAME,
        transportGuarantee = TransportGuarantee.NONE,
        secureWSDLAccess = false
)
public class WS_SIR9Impl implements WS_SIR9_PortType {

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "WS_SIR9";

    public static final String NAME_WS = NAME + "Ws";

    @Override
    public RespuestaWS envioMensajeDatosControlAAplicacion(String mensaje, String firma) {

        log.info("Dentro de WS_SIR9Impl: envioMensajeDatosControlAAplicacion");
        log.info("Mensaje: " + mensaje);
        log.info("Firma: " + firma);

        RespuestaWS respuesta = new RespuestaWS();
        respuesta.setCodigo("00");
        respuesta.setDescripcion("Exito");


        return respuesta;
    }
}
