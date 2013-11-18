/**
 * RegwebFacadeService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services.regwebfacade;

public interface RegwebFacadeService extends javax.xml.rpc.Service {
    public java.lang.String getRegwebFacadeAddress();

    public es.caib.regweb.ws.services.regwebfacade.RegwebFacade_PortType getRegwebFacade() throws javax.xml.rpc.ServiceException;

    public es.caib.regweb.ws.services.regwebfacade.RegwebFacade_PortType getRegwebFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
