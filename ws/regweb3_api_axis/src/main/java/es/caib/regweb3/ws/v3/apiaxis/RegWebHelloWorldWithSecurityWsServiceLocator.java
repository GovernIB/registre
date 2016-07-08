/**
 * RegWebHelloWorldWithSecurityWsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegWebHelloWorldWithSecurityWsServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWsService {

    public RegWebHelloWorldWithSecurityWsServiceLocator() {
    }


    public RegWebHelloWorldWithSecurityWsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RegWebHelloWorldWithSecurityWsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegWebHelloWorldWithSecurityWs
    private java.lang.String RegWebHelloWorldWithSecurityWs_address = "http://localhost:8080/regweb3/ws/v3/RegWebHelloWorldWithSecurity";

    public java.lang.String getRegWebHelloWorldWithSecurityWsAddress() {
        return RegWebHelloWorldWithSecurityWs_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegWebHelloWorldWithSecurityWsWSDDServiceName = "RegWebHelloWorldWithSecurityWs";

    public java.lang.String getRegWebHelloWorldWithSecurityWsWSDDServiceName() {
        return RegWebHelloWorldWithSecurityWsWSDDServiceName;
    }

    public void setRegWebHelloWorldWithSecurityWsWSDDServiceName(java.lang.String name) {
        RegWebHelloWorldWithSecurityWsWSDDServiceName = name;
    }

    public es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWs_PortType getRegWebHelloWorldWithSecurityWs() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegWebHelloWorldWithSecurityWs_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegWebHelloWorldWithSecurityWs(endpoint);
    }

    public es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWs_PortType getRegWebHelloWorldWithSecurityWs(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWsServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegWebHelloWorldWithSecurityWsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegWebHelloWorldWithSecurityWsEndpointAddress(java.lang.String address) {
        RegWebHelloWorldWithSecurityWs_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWs_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWithSecurityWsServiceSoapBindingStub(new java.net.URL(RegWebHelloWorldWithSecurityWs_address), this);
                _stub.setPortName(getRegWebHelloWorldWithSecurityWsWSDDServiceName());
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
        if ("RegWebHelloWorldWithSecurityWs".equals(inputPortName)) {
            return getRegWebHelloWorldWithSecurityWs();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebHelloWorldWithSecurityWsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebHelloWorldWithSecurityWs"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RegWebHelloWorldWithSecurityWs".equals(portName)) {
            setRegWebHelloWorldWithSecurityWsEndpointAddress(address);
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
