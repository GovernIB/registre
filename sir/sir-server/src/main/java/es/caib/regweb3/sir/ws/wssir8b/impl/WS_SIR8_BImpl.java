package es.caib.regweb3.sir.ws.wssir8b.impl;


import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.ws.manager.EnvioManager;
import es.caib.regweb3.sir.ws.manager.impl.EnvioManagerImpl;
import es.caib.regweb3.sir.ws.utils.PassiveCallbackHandler;
import es.caib.regweb3.sir.ws.wssir8b.RespuestaWS;
import es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_B_PortType;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.util.Set;

/**
 * Implementación del servicio (WS_SIR8_B) que recibe en REGWEB3 los ficheros de datos de intercambio
 * en formato SICRES 3.0 y sus anexos, desde un nodo distribuido.
 *
 * Created by Fundació BIT.
 * @author earrivi
 */
/*@DeclareRoles({ "RWE_USUARI" })
@RunAs("RWE_USUARI")*/
@Stateless(name = WS_SIR8_BImpl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
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
public class WS_SIR8_BImpl implements WS_SIR8_B_PortType {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/WebServicesMethodsEJB/local")
    public WebServicesMethodsLocal webServicesMethodsEjb;


    private EnvioManager envioManager = new EnvioManagerImpl();

    public static final String NAME = "WS_SIR8_B";

    public static final String NAME_WS = NAME + "Ws";


    @Override
    @WebMethod
    public RespuestaWS envioFicherosAAplicacion(@WebParam(name = "registro") String registro, @WebParam(name = "firmaRegistro") String firmaRegistro) {

        LoginContext lc = null;
        // Realizamos el login con un usuario existente en Seycon. Esto se hace porque los ws están sin autenticar
        try {
            lc = new LoginContext(RegwebConstantes.SECURITY_DOMAIN, new PassiveCallbackHandler("mgonzalez", "mgonzalez"));
            lc.login();

            Set<Principal> principalsCred = lc.getSubject().getPrincipals();
            if (principalsCred == null || principalsCred.isEmpty()) {
                log.info(" getPrincipals() == BUIT");
            } else {
                for (Principal object : principalsCred) {
                    log.info(" getPrincipals() == " + object.getName() + "(" + object.getClass() + ")");

                }
            }

        } catch (LoginException le) {
            // Authentication failed.
            log.error("CAIB3 Login ERROR" + le.getMessage());
        }

        log.info("Dentro de WS_SIR8_BImpl: envioFicherosAplicacion");
        log.info("Mensaje: " + registro);
        log.info("Firma: " + firmaRegistro);

        RespuestaWS respuestaWS = null;


        try {

            // Envia el fichero de intercambio a REGWEB3
            envioManager.envioFichero(registro, webServicesMethodsEjb);

            respuestaWS = crearRespuestaWS("00", "Exito");


        } catch (Exception e) {
            respuestaWS = crearRespuestaWS("01", "Error");
            e.printStackTrace();
        }

        log.info("Respuesta: " + respuestaWS.getDescripcion());

        return respuestaWS;
    }

    /**
     * Crea la respuesta de retorno del servicio.
     *
     * @param codigo
     * @param valor
     * @return Información de respuesta.
     */
    private RespuestaWS crearRespuestaWS(String codigo, String valor) {
        RespuestaWS respuesta = new RespuestaWS();
        respuesta.setCodigo(codigo);
        respuesta.setDescripcion(valor);
        return respuesta;
    }
}
