/**
 * WS_SIR8_BServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.sir.ws.wssir8b;

public class WS_SIR8_BServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_BService {

    public WS_SIR8_BServiceLocator() {
    }


    public WS_SIR8_BServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_SIR8_BServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_SIR8_B
    private java.lang.String WS_SIR8_B_address = "http://localhost/services/WS_SIR8_B";

    public java.lang.String getWS_SIR8_BAddress() {
        return WS_SIR8_B_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_SIR8_BWSDDServiceName = "WS_SIR8_B";

    public java.lang.String getWS_SIR8_BWSDDServiceName() {
        return WS_SIR8_BWSDDServiceName;
    }

    public void setWS_SIR8_BWSDDServiceName(java.lang.String name) {
        WS_SIR8_BWSDDServiceName = name;
    }

    public es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_B_PortType getWS_SIR8_B() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_SIR8_B_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_SIR8_B(endpoint);
    }

    public es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_B_PortType getWS_SIR8_B(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_BSoapBindingStub _stub = new es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_BSoapBindingStub(portAddress, this);
            _stub.setPortName(getWS_SIR8_BWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_SIR8_BEndpointAddress(java.lang.String address) {
        WS_SIR8_B_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_B_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_BSoapBindingStub _stub = new es.caib.regweb3.sir.ws.wssir8b.WS_SIR8_BSoapBindingStub(new java.net.URL(WS_SIR8_B_address), this);
                _stub.setPortName(getWS_SIR8_BWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
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
        if ("WS_SIR8_B".equals(inputPortName)) {
            return getWS_SIR8_B();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.manager.cct.map.es", "WS_SIR8_BService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.manager.cct.map.es", "WS_SIR8_B"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("WS_SIR8_B".equals(portName)) {
            setWS_SIR8_BEndpointAddress(address);
        } else { // Unknown Port Name
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
