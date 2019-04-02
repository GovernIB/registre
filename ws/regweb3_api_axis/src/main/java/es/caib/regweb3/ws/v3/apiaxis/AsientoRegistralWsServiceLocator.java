/**
 * AsientoRegistralWsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class AsientoRegistralWsServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWsService {

    public AsientoRegistralWsServiceLocator() {
    }


    public AsientoRegistralWsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AsientoRegistralWsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AsientoRegistralWs
    private java.lang.String AsientoRegistralWs_address = "http://localhost:8080/regweb3/ws/v3/AsientoRegistral";

    public java.lang.String getAsientoRegistralWsAddress() {
        return AsientoRegistralWs_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AsientoRegistralWsWSDDServiceName = "AsientoRegistralWs";

    public java.lang.String getAsientoRegistralWsWSDDServiceName() {
        return AsientoRegistralWsWSDDServiceName;
    }

    public void setAsientoRegistralWsWSDDServiceName(java.lang.String name) {
        AsientoRegistralWsWSDDServiceName = name;
    }

    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs_PortType getAsientoRegistralWs() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AsientoRegistralWs_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAsientoRegistralWs(endpoint);
    }

    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs_PortType getAsientoRegistralWs(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWsServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getAsientoRegistralWsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAsientoRegistralWsEndpointAddress(java.lang.String address) {
        AsientoRegistralWs_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWsServiceSoapBindingStub(new java.net.URL(AsientoRegistralWs_address), this);
                _stub.setPortName(getAsientoRegistralWsWSDDServiceName());
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
        if ("AsientoRegistralWs".equals(inputPortName)) {
            return getAsientoRegistralWs();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "AsientoRegistralWsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "AsientoRegistralWs"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AsientoRegistralWs".equals(portName)) {
            setAsientoRegistralWsEndpointAddress(address);
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
