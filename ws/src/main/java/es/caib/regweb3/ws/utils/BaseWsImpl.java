package es.caib.regweb3.ws.utils;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Versio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebMethod;


/**
 * @author anadal
 */
public class BaseWsImpl implements RegwebConstantes {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    // --------------------------| UTILITATS |----------------------------
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------


    @WebMethod
    public String getVersion() {
        return Versio.VERSIO;
    }


    @WebMethod
    public int getVersionWs() {
        return VersionsWs.VERSIO_WS_3;
    }
}
