package es.caib.regweb3.ws.v3.impl;

import es.caib.regweb3.ws.utils.IBaseWs;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.validation.constraints.Null;

/**
 * 
 * @author anadal
 *
 */
@WebService
public interface RegWebHelloWorldWs extends IBaseWs {
  
  @WebMethod
  public String echo(@WebParam (name ="echo") @Null String echo);
}
