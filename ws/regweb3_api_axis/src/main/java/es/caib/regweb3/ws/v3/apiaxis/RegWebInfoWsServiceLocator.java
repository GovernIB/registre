/**
 * RegWebInfoWsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegWebInfoWsServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWsService {

    public RegWebInfoWsServiceLocator() {
    }


    public RegWebInfoWsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RegWebInfoWsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegWebInfoWs
    private java.lang.String RegWebInfoWs_address = "http://localhost:8080/regweb3/ws/v3/RegWebInfo";

    public java.lang.String getRegWebInfoWsAddress() {
        return RegWebInfoWs_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegWebInfoWsWSDDServiceName = "RegWebInfoWs";

    public java.lang.String getRegWebInfoWsWSDDServiceName() {
        return RegWebInfoWsWSDDServiceName;
    }

    public void setRegWebInfoWsWSDDServiceName(java.lang.String name) {
        RegWebInfoWsWSDDServiceName = name;
    }

    public es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWs_PortType getRegWebInfoWs() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegWebInfoWs_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegWebInfoWs(endpoint);
    }

    public es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWs_PortType getRegWebInfoWs(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWsServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegWebInfoWsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegWebInfoWsEndpointAddress(java.lang.String address) {
        RegWebInfoWs_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWs_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWsServiceSoapBindingStub(new java.net.URL(RegWebInfoWs_address), this);
                _stub.setPortName(getRegWebInfoWsWSDDServiceName());
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
        if ("RegWebInfoWs".equals(inputPortName)) {
            return getRegWebInfoWs();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebInfoWsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebInfoWs"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RegWebInfoWs".equals(portName)) {
            setRegWebInfoWsEndpointAddress(address);
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
