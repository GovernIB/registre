package es.caib.regweb3.ws.utils;

import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Versio;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jws.WebMethod;


/**
 * 
 * @author anadal
 *
 */
public class BaseWsImpl implements RegwebConstantes {
  
  protected final Log log = LogFactory.getLog(getClass());
  
  // -------------------------------------------------------------------
  // -------------------------------------------------------------------
  // --------------------------| UTILITATS |----------------------------
  // -------------------------------------------------------------------
  // -------------------------------------------------------------------

  
  @WebMethod
  public String getVersion() {
    return Versio.VERSIO + (Configuracio.isCAIB()?"-caib" :"");
  }


  @WebMethod
  public int getVersionWs() {
    return VersionsWs.VERSIO_WS_3;
  }
}
