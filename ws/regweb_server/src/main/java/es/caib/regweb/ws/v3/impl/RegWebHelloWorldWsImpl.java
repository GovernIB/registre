package es.caib.regweb.ws.v3.impl;


import es.caib.regweb.ws.utils.BaseWsImpl;
import org.jboss.wsf.spi.annotation.TransportGuarantee;
import org.jboss.wsf.spi.annotation.WebContext;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.Null;


/**
 * 
 * @author anadal
 * 
 */
@Stateless(name= RegWebHelloWorldWsImpl.NAME + "Ejb")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebService
(
    name=RegWebHelloWorldWsImpl.NAME_WS,
    portName = RegWebHelloWorldWsImpl.NAME_WS,
    serviceName = RegWebHelloWorldWsImpl.NAME_WS + "Service"
)
@WebContext
(
    contextRoot="/regweb/ws",
    urlPattern="/v3/" + RegWebHelloWorldWsImpl.NAME,    
    transportGuarantee= TransportGuarantee.NONE,
    secureWSDLAccess = false
)
public class RegWebHelloWorldWsImpl extends BaseWsImpl implements RegWebHelloWorldWs {
  
  public static final String NAME = "RegWebHelloWorld";
  
  public static final String NAME_WS = NAME + "Ws";
  
  @WebMethod
  public String echo(@WebParam (name ="echo") @Null String echo) {   
    log.info("RegWebHelloWorldWsImpl :: echo = " + echo);
    return echo;
  }

}
