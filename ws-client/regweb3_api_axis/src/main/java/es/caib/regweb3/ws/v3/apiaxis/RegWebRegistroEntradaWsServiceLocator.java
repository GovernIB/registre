/**
 * RegWebRegistroEntradaWsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegWebRegistroEntradaWsServiceLocator extends org.apache.axis.client.Service implements es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWsService {

    public RegWebRegistroEntradaWsServiceLocator() {
    }


    public RegWebRegistroEntradaWsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public RegWebRegistroEntradaWsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for RegWebRegistroEntradaWs
    private java.lang.String RegWebRegistroEntradaWs_address = "http://localhost:8080/regweb3/ws/v3/RegWebRegistroEntrada";

    public java.lang.String getRegWebRegistroEntradaWsAddress() {
        return RegWebRegistroEntradaWs_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RegWebRegistroEntradaWsWSDDServiceName = "RegWebRegistroEntradaWs";

    public java.lang.String getRegWebRegistroEntradaWsWSDDServiceName() {
        return RegWebRegistroEntradaWsWSDDServiceName;
    }

    public void setRegWebRegistroEntradaWsWSDDServiceName(java.lang.String name) {
        RegWebRegistroEntradaWsWSDDServiceName = name;
    }

    public es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWs_PortType getRegWebRegistroEntradaWs() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RegWebRegistroEntradaWs_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRegWebRegistroEntradaWs(endpoint);
    }

    public es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWs_PortType getRegWebRegistroEntradaWs(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWsServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getRegWebRegistroEntradaWsWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setRegWebRegistroEntradaWsEndpointAddress(java.lang.String address) {
        RegWebRegistroEntradaWs_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWs_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWsServiceSoapBindingStub _stub = new es.caib.regweb3.ws.v3.apiaxis.RegWebRegistroEntradaWsServiceSoapBindingStub(new java.net.URL(RegWebRegistroEntradaWs_address), this);
                _stub.setPortName(getRegWebRegistroEntradaWsWSDDServiceName());
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
        if ("RegWebRegistroEntradaWs".equals(inputPortName)) {
            return getRegWebRegistroEntradaWs();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebRegistroEntradaWsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebRegistroEntradaWs"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("RegWebRegistroEntradaWs".equals(portName)) {
            setRegWebRegistroEntradaWsEndpointAddress(address);
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
