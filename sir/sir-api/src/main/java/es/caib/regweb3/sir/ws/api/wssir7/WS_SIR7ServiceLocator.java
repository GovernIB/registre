/**
 * WS_SIR7ServiceLocator.java
 * <p/>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.sir.ws.api.wssir7;

public class WS_SIR7ServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7Service {

    public WS_SIR7ServiceLocator() {
    }


    public WS_SIR7ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WS_SIR7ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WS_SIR7
    private java.lang.String WS_SIR7_address = "http://localhost:8380/services/WS_SIR7";

    public java.lang.String getWS_SIR7Address() {
        return WS_SIR7_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WS_SIR7WSDDServiceName = "WS_SIR7";

    public java.lang.String getWS_SIR7WSDDServiceName() {
        return WS_SIR7WSDDServiceName;
    }

    public void setWS_SIR7WSDDServiceName(java.lang.String name) {
        WS_SIR7WSDDServiceName = name;
    }

    public es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType getWS_SIR7() throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WS_SIR7_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWS_SIR7(endpoint);
    }

    public es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType getWS_SIR7(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7SoapBindingStub _stub = new es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7SoapBindingStub(portAddress, this);
            _stub.setPortName(getWS_SIR7WSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWS_SIR7EndpointAddress(java.lang.String address) {
        WS_SIR7_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7SoapBindingStub _stub = new es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7SoapBindingStub(new java.net.URL(WS_SIR7_address), this);
                _stub.setPortName(getWS_SIR7WSDDServiceName());
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
        if ("WS_SIR7".equals(inputPortName)) {
            return getWS_SIR7();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.manager.cct.map.es", "WS_SIR7Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.manager.cct.map.es", "WS_SIR7"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("WS_SIR7".equals(portName)) {
            setWS_SIR7EndpointAddress(address);
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
