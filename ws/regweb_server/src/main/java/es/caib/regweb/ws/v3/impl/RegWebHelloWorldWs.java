package es.caib.regweb.ws.v3.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.validation.constraints.Null;

import es.caib.regweb.ws.utils.IBaseWs;

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
