package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.utils.AuthenticatedBaseWsImpl;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.Null;
import javax.xml.ws.WebServiceContext;



/**
 * 
 * @author anadal
 * 
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebHelloWorldWithSecurityWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.RWE_USUARI, RegwebConstantes.RWE_ADMIN, RegwebConstantes.RWE_SUPERADMIN})
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb3.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb3.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebHelloWorldWithSecurityWsImpl.NAME_WS, portName = RegWebHelloWorldWithSecurityWsImpl.NAME_WS, serviceName = RegWebHelloWorldWithSecurityWsImpl.NAME_WS
    + "Service")
@WebContext(contextRoot = "/regweb3/ws", urlPattern = "/v3/"
    + RegWebHelloWorldWithSecurityWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = "WSBASIC")
public class RegWebHelloWorldWithSecurityWsImpl extends AuthenticatedBaseWsImpl
  implements RegWebHelloWorldWithSecurityWs {

  public static final String NAME = "RegWebHelloWorldWithSecurity";

  public static final String NAME_WS = NAME + "Ws";
  

  @Resource
  private WebServiceContext wsContext;


  @RolesAllowed({ RegwebConstantes.RWE_SUPERADMIN, RegwebConstantes.RWE_ADMIN, RegwebConstantes.RWE_USUARI})
  @WebMethod
  public String echo(@WebParam (name ="echo") @Null String echo)  {

    log.info("RegWebHelloWorldWithSecurityWsImpl :: echo = " + echo);
    return "USER: " + wsContext.getUserPrincipal().getName() + " | ECHO: " + echo;

  }
  

}
