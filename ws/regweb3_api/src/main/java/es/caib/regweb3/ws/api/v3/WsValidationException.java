
package es.caib.regweb3.ws.api.v3;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.2
 * 2021-05-10T15:46:04.677+02:00
 * Generated source version: 3.0.2
 */

@WebFault(name = "WsValidationErrors", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/")
public class WsValidationException extends Exception {
    
    private es.caib.regweb3.ws.api.v3.WsValidationErrors wsValidationErrors;

    public WsValidationException() {
        super();
    }
    
    public WsValidationException(String message) {
        super(message);
    }
    
    public WsValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public WsValidationException(String message, es.caib.regweb3.ws.api.v3.WsValidationErrors wsValidationErrors) {
        super(message);
        this.wsValidationErrors = wsValidationErrors;
    }

    public WsValidationException(String message, es.caib.regweb3.ws.api.v3.WsValidationErrors wsValidationErrors, Throwable cause) {
        super(message, cause);
        this.wsValidationErrors = wsValidationErrors;
    }

    public es.caib.regweb3.ws.api.v3.WsValidationErrors getFaultInfo() {
        return this.wsValidationErrors;
    }
}
