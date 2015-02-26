package es.caib.regweb.ws.v3.impl;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import es.caib.regweb.utils.CompileConstants;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.utils.AuthenticatedBaseWsImpl;

import javax.validation.constraints.Null;



/**
 * 
 * @author anadal
 * 
 */
@SecurityDomain(RegwebConstantes.SECURITY_DOMAIN)
@Stateless(name = RegWebHelloWorldWithSecurityWsImpl.NAME + "Ejb")
@RolesAllowed({ RegwebConstantes.ROL_USUARI, RegwebConstantes.ROL_ADMIN, RegwebConstantes.ROL_SUPERADMIN })
@SOAPBinding(style = SOAPBinding.Style.RPC)
@org.apache.cxf.interceptor.InInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@org.apache.cxf.interceptor.InFaultInterceptors(interceptors = { "es.caib.regweb.ws.utils.RegWebInInterceptor" })
@WebService(name = RegWebHelloWorldWithSecurityWsImpl.NAME_WS, portName = RegWebHelloWorldWithSecurityWsImpl.NAME_WS, serviceName = RegWebHelloWorldWithSecurityWsImpl.NAME_WS
    + "Service")
@WebContext(contextRoot = "/regweb/ws", urlPattern = "/v3/"
    + RegWebHelloWorldWithSecurityWsImpl.NAME, transportGuarantee = TransportGuarantee.NONE, secureWSDLAccess = false, authMethod = CompileConstants.AUTH_METHOD)
public class RegWebHelloWorldWithSecurityWsImpl extends AuthenticatedBaseWsImpl {

  public static final String NAME = "RegWebHelloWorldWithSecurity";

  public static final String NAME_WS = NAME + "Ws";
  

  @Resource
  private WebServiceContext wsContext;


  @RolesAllowed({ RegwebConstantes.ROL_SUPERADMIN , RegwebConstantes.ROL_ADMIN, RegwebConstantes.ROL_USUARI})
  @WebMethod
  public String echo(@WebParam (name ="echo") @Null String echo) throws Throwable {

    log.info("RegWebHelloWorldWithSecurityWsImpl :: echo = " + echo);
    return "USER: " + wsContext.getUserPrincipal().getName() + " | ECHO: " + echo;

  }
  

}
