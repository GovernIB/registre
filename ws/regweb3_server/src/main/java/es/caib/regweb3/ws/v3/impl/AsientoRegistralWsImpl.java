package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.model.AsientoRegistralBean;
import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = AsientoRegistralWsImpl.NAME + "Ejb")
@RolesAllowed({RegwebConstantes.ROL_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = {"es.caib.regweb3.ws.utils.RegWebInInterceptor"})
@WebService(name = AsientoRegistralWsImpl.NAME_WS, portName = AsientoRegistralWsImpl.NAME_WS,
        serviceName = AsientoRegistralWsImpl.NAME_WS + "Service",
        endpointInterface = "es.caib.regweb3.ws.v3.impl.AsientoRegistralWs")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/" + AsientoRegistralWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class AsientoRegistralWsImpl implements AsientoRegistralWs{

    protected final Logger log = Logger.getLogger(getClass());

    public static final String NAME = "AsientoRegistral";

    public static final String NAME_WS = NAME + "Ws";

    @Override
    public AsientoRegistralBean crearAsientoRegistral(String entidad, AsientoRegistralBean asientoRegistral) {

        log.info("Dentro de AsientoRegitralWs");

        log.info("Entidad: " + entidad);

        return asientoRegistral;
    }
}
