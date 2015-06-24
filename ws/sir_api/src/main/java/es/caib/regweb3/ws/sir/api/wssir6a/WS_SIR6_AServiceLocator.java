/**
 * WS_SIR6_AServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.sir.api.wssir6a;

public class WS_SIR6_AServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_AService {

    public WS_SIR6_AServiceLocator() {
    }


    public WS_SIR6_AServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_SIR6_AServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_SIR6_A
    private java.lang.String WS_SIR6_A_address = "http://localhost:8380/services/WS_SIR6_A";

    public java.lang.String getWS_SIR6_AAddress() {
        return WS_SIR6_A_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_SIR6_AWSDDServiceName = "WS_SIR6_A";

    public java.lang.String getWS_SIR6_AWSDDServiceName() {
        return WS_SIR6_AWSDDServiceName;
    }

    public void setWS_SIR6_AWSDDServiceName(java.lang.String name) {
        WS_SIR6_AWSDDServiceName = name;
    }

    public es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_A_PortType getWS_SIR6_A() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_SIR6_A_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_SIR6_A(endpoint);
    }

    public es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_A_PortType getWS_SIR6_A(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_ASoapBindingStub _stub = new es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_ASoapBindingStub(portAddress, this);
            _stub.setPortName(getWS_SIR6_AWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_SIR6_AEndpointAddress(java.lang.String address) {
        WS_SIR6_A_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_A_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_ASoapBindingStub _stub = new es.caib.regweb3.ws.sir.api.wssir6a.WS_SIR6_ASoapBindingStub(new java.net.URL(WS_SIR6_A_address), this);
                _stub.setPortName(getWS_SIR6_AWSDDServiceName());
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
        if ("WS_SIR6_A".equals(inputPortName)) {
            return getWS_SIR6_A();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.manager.cct.map.es", "WS_SIR6_AService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.manager.cct.map.es", "WS_SIR6_A"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WS_SIR6_A".equals(portName)) {
            setWS_SIR6_AEndpointAddress(address);
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
