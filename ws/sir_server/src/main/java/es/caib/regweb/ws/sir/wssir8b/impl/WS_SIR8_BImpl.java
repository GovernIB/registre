package es.caib.regweb.ws.sir.wssir8b.impl;


import es.caib.regweb.model.PreRegistro;
import es.caib.regweb.persistence.ejb.PreRegistroLocal;
import es.caib.regweb.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb.persistence.utils.Respuesta;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.sir.utils.PassiveCallbackHandler;
import es.caib.regweb.ws.sir.wssir8b.RespuestaWS;
import es.caib.regweb.ws.sir.wssir8b.WS_SIR8_B_PortType;
import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;
import org.springframework.stereotype.Component;

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
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
/*@DeclareRoles({ "RWE_USUARI" })
@RunAs("RWE_USUARI")*/
@Stateless(name= WS_SIR8_BImpl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
@WebService(
        name=WS_SIR8_BImpl.NAME_WS,
        portName = WS_SIR8_BImpl.NAME_WS,
        serviceName = WS_SIR8_BImpl.NAME_WS + "Service",
        targetNamespace = "http://impl.manager.cct.map.es"
)
@WebContext(
        contextRoot="/regweb/ws/sir",
        urlPattern="/v3/" + WS_SIR8_BImpl.NAME,
        transportGuarantee= TransportGuarantee.NONE,
        secureWSDLAccess = false
)
@Component
public class WS_SIR8_BImpl implements WS_SIR8_B_PortType {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;

    @EJB(mappedName = "regweb/WebServicesMethodsEJB/local")
    public WebServicesMethodsLocal webServicesMethodsEjb;

    public static final String NAME = "WS_SIR8_B";

    public static final String NAME_WS = NAME + "Ws";


    @Override
    @WebMethod
    public RespuestaWS envioFicherosAAplicacion(@WebParam(name = "registro")String registro, @WebParam(name = "firmaRegistro")String firmaRegistro) {

        LoginContext lc = null;
        // Realizamos el login con un usuario existente en Seycon. Esto se hace porque los ws están sin autenticar
        try {
            lc = new LoginContext(RegwebConstantes.SECURITY_DOMAIN, new PassiveCallbackHandler("earrivi", "earrivi"));
            lc.login();

            Set<Principal> principalsCred = lc.getSubject().getPrincipals();
            if (principalsCred == null ||principalsCred.isEmpty()) {
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

        // 1.- Recibir el nuevo asiento registral
        // 2.- Comprobar que está bien formado
        // 3.- Responder el Ok técnico
        // 4.- Crear y guardar el PreRegistro

        try {
            synchronized (this){
                Respuesta respuesta = webServicesMethodsEjb.crearPreRegistro(registro);
                PreRegistro preRegistro = (PreRegistro) respuesta.getObject();

                if(preRegistro != null){
                    respuestaWS = crearRespuestaWS("00","Exito");
                }else{
                    respuestaWS = crearRespuestaWS("01","Error: " + respuesta.getMensaje());
                }
            }
        } catch (Exception e) {
            respuestaWS = crearRespuestaWS("01","Error");
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
