
package es.caib.regweb3.ws.api.v3;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.6.4
 * 2019-05-28T09:07:59.558+02:00
 * Generated source version: 2.6.4
 */

@WebFault(name = "WsI18NError", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/")
public class WsI18NException extends Exception {
    
    private es.caib.regweb3.ws.api.v3.WsI18NError wsI18NError;

    public WsI18NException() {
        super();
    }
    
    public WsI18NException(String message) {
        super(message);
    }
    
    public WsI18NException(String message, Throwable cause) {
        super(message, cause);
    }

    public WsI18NException(String message, es.caib.regweb3.ws.api.v3.WsI18NError wsI18NError) {
        super(message);
        this.wsI18NError = wsI18NError;
    }

    public WsI18NException(String message, es.caib.regweb3.ws.api.v3.WsI18NError wsI18NError, Throwable cause) {
        super(message, cause);
        this.wsI18NError = wsI18NError;
    }

    public es.caib.regweb3.ws.api.v3.WsI18NError getFaultInfo() {
        return this.wsI18NError;
    }
}
