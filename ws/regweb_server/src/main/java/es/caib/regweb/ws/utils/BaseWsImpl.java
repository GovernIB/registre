package es.caib.regweb.ws.utils;

import javax.jws.WebMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.regweb.utils.Configuracio;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.Versio;


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
