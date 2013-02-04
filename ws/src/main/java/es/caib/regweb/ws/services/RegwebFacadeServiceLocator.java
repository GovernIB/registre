/**
 * RegwebFacadeServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services;

public class RegwebFacadeServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb.ws.services.RegwebFacadeService {

    public RegwebFacadeServiceLocator() {
    }


    public RegwebFacadeServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RegwebFacadeServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegwebFacade
    private java.lang.String RegwebFacade_address = "http://intranet.caib.es/regweb/services/v1/RegwebFacade";

    public java.lang.String getRegwebFacadeAddress() {
        return RegwebFacade_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegwebFacadeWSDDServiceName = "RegwebFacade";

    public java.lang.String getRegwebFacadeWSDDServiceName() {
        return RegwebFacadeWSDDServiceName;
    }

    public void setRegwebFacadeWSDDServiceName(java.lang.String name) {
        RegwebFacadeWSDDServiceName = name;
    }

    public es.caib.regweb.ws.services.RegwebFacade getRegwebFacade() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegwebFacade_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegwebFacade(endpoint);
    }

    public es.caib.regweb.ws.services.RegwebFacade getRegwebFacade(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb.ws.services.RegwebFacadeSoapBindingStub _stub = new es.caib.regweb.ws.services.RegwebFacadeSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegwebFacadeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegwebFacadeEndpointAddress(java.lang.String address) {
        RegwebFacade_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb.ws.services.RegwebFacade.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb.ws.services.RegwebFacadeSoapBindingStub _stub = new es.caib.regweb.ws.services.RegwebFacadeSoapBindingStub(new java.net.URL(RegwebFacade_address), this);
                _stub.setPortName(getRegwebFacadeWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("RegwebFacade".equals(inputPortName)) {
            return getRegwebFacade();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:services", "RegwebFacadeService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:services", "RegwebFacade"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RegwebFacade".equals(portName)) {
            setRegwebFacadeEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
