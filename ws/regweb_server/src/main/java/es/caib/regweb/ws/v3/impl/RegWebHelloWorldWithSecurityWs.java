package es.caib.regweb.ws.v3.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.validation.constraints.Null;

import es.caib.regweb.ws.utils.IBaseWs;

/**
 * 
 * @author anadal
 *
 */
public interface RegWebHelloWorldWithSecurityWs extends IBaseWs {
  
  @WebMethod
  public String echo(@WebParam (name ="echo") @Null String echo);

}
