package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.ws.utils.IBaseWs;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.validation.constraints.Null;

/**
 * 
 * @author anadal
 *
 */
public interface RegWebHelloWorldWithSecurityWs extends IBaseWs {
  
  @WebMethod
  public String echo(@WebParam (name ="echo") @Null String echo);

}
