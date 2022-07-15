package es.caib.regweb3.sir.ws.wssir9.impl;

import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.ws.ejb.RecepcionLocal;
import es.caib.regweb3.sir.ws.wssir9.RespuestaWS;
import es.caib.regweb3.sir.ws.wssir9.WS_SIR9_PortType;
import org.apache.cxf.interceptor.InInterceptors;
import org.jboss.ws.api.annotation.TransportGuarantee;
import org.jboss.ws.api.annotation.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Implementación del servicio (WS_SIR9) que recibe en REGWEB3 los ficheros de mensaje de datos de control
 * en formato SICRES3, desde un nodo distribuido.
 *
 * Created by Fundació BIT.
 * @author earrivi
 */
@Stateless(name = WS_SIR9Impl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@InInterceptors(interceptors = {"es.caib.regweb3.sir.ws.utils.SirInInterceptor"})
@WebService(
        name = WS_SIR9Impl.NAME_WS,
        portName = WS_SIR9Impl.NAME_WS,
        serviceName = WS_SIR9Impl.NAME_WS + "Service",
        targetNamespace = "http://impl.manager.cct.map.es"
)
@WebContext(
        contextRoot = "/regweb3/ws/sir",
        urlPattern = "/v3/" + WS_SIR9Impl.NAME,
        transportGuarantee = TransportGuarantee.NONE
)
public class WS_SIR9Impl implements WS_SIR9_PortType {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String NAME = "WS_SIR9";

    public static final String NAME_WS = NAME;

    @EJB(mappedName = WebServicesMethodsLocal.JNDI_NAME)
    public WebServicesMethodsLocal webServicesMethodsEjb;

    @EJB(mappedName = RecepcionLocal.JNDI_NAME)
    public RecepcionLocal recepcionEjb;


    @Override
    @WebMethod(operationName = "envioMensajeDatosControlAAplicacion")
    public RespuestaWS envioMensajeDatosControlAAplicacion(@WebParam(name = "value0")String mensaje, @WebParam(name = "value1")String firma) {

        RespuestaWS respuestaWS = null;

        try{
            // Envia el mensaje datos control a REGWEB3
            recepcionEjb.recibirMensajeDatosControl(mensaje, webServicesMethodsEjb);

            // Creamos la respuesta exitosa
            respuestaWS = crearRespuestaWS(Errores.OK);

        } catch (ServiceException e) {
            log.info("Error en la recepcion del mensaje de datos de control", e);
            respuestaWS = crearRespuestaWS(e.getError());
        } catch (Throwable e) {
            log.info("Error en la recepcion del mensaje de datos de control", e);
            respuestaWS = crearRespuestaWS(Errores.ERROR_NO_CONTROLADO);
        }

        return respuestaWS;
    }

    /**
     * Crea la respuesta de retorno del servicio.
     *
     * @param error
     * @return Información de respuesta.
     */
    private RespuestaWS crearRespuestaWS(Errores error) {
        RespuestaWS respuesta = new RespuestaWS();
        respuesta.setCodigo(error.getValue());
        respuesta.setDescripcion(error.getName());
        return respuesta;
    }
}
