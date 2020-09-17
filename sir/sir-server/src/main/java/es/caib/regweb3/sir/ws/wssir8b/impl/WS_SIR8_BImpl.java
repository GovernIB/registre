package es.caib.regweb3.sir.ws.wssir8b.impl;


import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.ws.ejb.RecepcionLocal;
import es.caib.regweb3.sir.ws.wssir8b.RespuestaWS;
import es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_B_PortType;
import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Implementación del servicio (WS_SIR8_B) que recibe en REGWEB3 los ficheros de datos de intercambio
 * y sus anexos en formato SICRES 3.0, desde un nodo distribuido.
 *
 * Created by Fundació BIT.
 * @author earrivi
 */
/*@DeclareRoles({ "RWE_USUARI" })
@RunAs("RWE_USUARI")*/

@Stateless(name = WS_SIR8_BImpl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
/*@org.apache.cxf.interceptor.InInterceptors(interceptors = {"org.apache.cxf.interceptor.LoggingInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"org.apache.cxf.interceptor.LoggingInInterceptor"})*/
@WebService(
        name = WS_SIR8_BImpl.NAME_WS,
        portName = WS_SIR8_BImpl.NAME_WS,
        serviceName = WS_SIR8_BImpl.NAME_WS + "Service",
        targetNamespace = "http://impl.manager.cct.map.es"
)
@WebContext(
        contextRoot = "/regweb3/ws/sir",
        urlPattern = "/v3/" + WS_SIR8_BImpl.NAME,
        transportGuarantee = TransportGuarantee.NONE,
        secureWSDLAccess = false
)
@HandlerChain(file = "/handler-chain.xml")
public class WS_SIR8_BImpl implements WS_SIR8_B_PortType {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(name = "RecepcionEJB")
    private RecepcionLocal recepcionEjb;

    @EJB(mappedName = "regweb3/WebServicesMethodsEJB/local")
    private WebServicesMethodsLocal webServicesMethodsEjb;

    public static final String NAME = "WS_SIR8_B";

    public static final String NAME_WS = NAME;


    @Override
    @WebMethod(operationName = "envioFicherosAAplicacion")
    public RespuestaWS envioFicherosAAplicacion(@WebParam(name = "registro") String registro, @WebParam(name = "firmaRegistro") String firmaRegistro) {

        log.info("-------------------- Recibiendo fichero de intercambio en WS_SIR8_B --------------------");
        if(registro.contains("<Anexo>")){
            log.info("Fichero de intercambio: " + registro.replace(registro.substring(registro.indexOf("<Anexo>"), registro.lastIndexOf("</Anexo>")), ""));
        }else{
            log.info("Fichero de intercambio: " + registro);
        }

        log.info("------------------------------------------------------------------------------------------");

        RespuestaWS respuestaWS = null;

        try {

            // Envia el fichero de intercambio a REGWEB3
            recepcionEjb.recibirFicheroIntercambio(registro, webServicesMethodsEjb);

            // Creamos la respuesta exitosa
            respuestaWS = crearRespuestaWS(Errores.OK);

        } catch (ServiceException e) {
            log.info("Error recibiendo el Fichero de Intercambio", e);
            respuestaWS = crearRespuestaWS(e.getError());
        }catch (Exception e){

            if(e.getMessage().equals(Errores.ERROR_0037.getName()) || e.getMessage().equals(Errores.ERROR_COD_ENTIDAD_INVALIDO.getName())){ //Error de validación
                log.info("Error de validacion en el Fichero de Intercambio: " + e.getLocalizedMessage());
                respuestaWS = crearRespuestaWS(Errores.ERROR_0037);
            }else{
                log.info("Error inesperado recibiendo en el Fichero de Intercambio", e);
                respuestaWS = crearRespuestaWS(Errores.ERROR_NO_CONTROLADO);
            }
        }

        //log.info("Respuesta envioFichero: " + respuestaWS.getCodigo() +" - "+ respuestaWS.getDescripcion());

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
