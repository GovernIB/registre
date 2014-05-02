/**
 * RegwebFacadeSoapBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services;

public class RegwebFacadeSoapBindingSkeleton implements es.caib.regweb.ws.services.RegwebFacade, org.apache.axis.wsdl.Skeleton {
    private es.caib.regweb.ws.services.RegwebFacade impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.model.ParametrosRegistroEntradaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("actualizarEntrada", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarEntradaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarEntrada"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("actualizarEntrada") == null) {
            _myOperations.put("actualizarEntrada", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("actualizarEntrada")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.model.ParametrosRegistroSalidaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("actualizarSalida", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarSalidaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarSalida"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("actualizarSalida") == null) {
            _myOperations.put("actualizarSalida", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("actualizarSalida")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.model.ParametrosRegistroEntradaWS.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anular"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("anularEntrada", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularEntradaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularEntrada"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("anularEntrada") == null) {
            _myOperations.put("anularEntrada", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("anularEntrada")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.model.ParametrosRegistroSalidaWS.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anular"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("anularSalida", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularSalidaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularSalida"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("anularSalida") == null) {
            _myOperations.put("anularSalida", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("anularSalida")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.model.ParametrosRegistroEntradaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("grabarEntrada", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarEntradaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarEntrada"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("grabarEntrada") == null) {
            _myOperations.put("grabarEntrada", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("grabarEntrada")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.model.ParametrosRegistroSalidaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("grabarSalida", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarSalidaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarSalida"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("grabarSalida") == null) {
            _myOperations.put("grabarSalida", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("grabarSalida")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.model.ParametrosRegistroEntradaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("leerEntrada", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerEntradaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerEntrada"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("leerEntrada") == null) {
            _myOperations.put("leerEntrada", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("leerEntrada")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.model.ParametrosRegistroSalidaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("leerSalida", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerSalidaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerSalida"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("leerSalida") == null) {
            _myOperations.put("leerSalida", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("leerSalida")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.model.ParametrosRegistroEntradaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("validarEntrada", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarEntradaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarEntrada"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("validarEntrada") == null) {
            _myOperations.put("validarEntrada", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("validarEntrada")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.model.ParametrosRegistroSalidaWS.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("validarSalida", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarSalidaReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarSalida"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("validarSalida") == null) {
            _myOperations.put("validarSalida", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("validarSalida")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "filtro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("buscarOficinasFisicasDescripcion", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicasDescripcionReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicasDescripcion"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("buscarOficinasFisicasDescripcion") == null) {
            _myOperations.put("buscarOficinasFisicasDescripcion", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("buscarOficinasFisicasDescripcion")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuarioRegistro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("buscarOficinasFisicas", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicasReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicas"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("buscarOficinasFisicas") == null) {
            _myOperations.put("buscarOficinasFisicas", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("buscarOficinasFisicas")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("buscarDocumentos", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarDocumentosReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarDocumentos"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("buscarDocumentos") == null) {
            _myOperations.put("buscarDocumentos", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("buscarDocumentos")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("buscarTodosDestinatarios", _params, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarTodosDestinatariosReturn"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        _oper.setElementQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarTodosDestinatarios"));
        _myOperationsList.add(_oper);
        if (_myOperations.get("buscarTodosDestinatarios") == null) {
            _myOperations.put("buscarTodosDestinatarios", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("buscarTodosDestinatarios")).add(_oper);
        _fault = new org.apache.axis.description.FaultDesc();
        _fault.setName("RegwebFacadeException");
        _fault.setQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"));
        _fault.setClassName("es.caib.regweb.ws.model.RegwebFacadeException");
        _fault.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"));
        _oper.addFault(_fault);
        
    }

    public RegwebFacadeSoapBindingSkeleton() {
        this.impl = new es.caib.regweb.ws.services.RegwebFacadeSoapBindingImpl();
    }

    public RegwebFacadeSoapBindingSkeleton(es.caib.regweb.ws.services.RegwebFacade impl) {
        this.impl = impl;
    }
    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS actualizarEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroEntradaWS ret = impl.actualizarEntrada(parametrosEntrada);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS actualizarSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroSalidaWS ret = impl.actualizarSalida(parametrosSalida);
        return ret;
    }

    public boolean anularEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        boolean ret = impl.anularEntrada(parametrosEntrada, anular);
        return ret;
    }

    public boolean anularSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        boolean ret = impl.anularSalida(parametrosSalida, anular);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS grabarEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroEntradaWS ret = impl.grabarEntrada(parametrosEntrada);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS grabarSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroSalidaWS ret = impl.grabarSalida(parametrosSalida);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS leerEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroEntradaWS ret = impl.leerEntrada(parametrosEntrada);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS leerSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroSalidaWS ret = impl.leerSalida(parametrosSalida);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroEntradaWS validarEntrada(es.caib.regweb.ws.model.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroEntradaWS ret = impl.validarEntrada(parametrosEntrada);
        return ret;
    }

    public es.caib.regweb.ws.model.ParametrosRegistroSalidaWS validarSalida(es.caib.regweb.ws.model.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ParametrosRegistroSalidaWS ret = impl.validarSalida(parametrosSalida);
        return ret;
    }

    public es.caib.regweb.ws.model.ListaResultados buscarOficinasFisicasDescripcion(java.lang.String usuario, java.lang.String password, java.lang.String filtro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ListaResultados ret = impl.buscarOficinasFisicasDescripcion(usuario, password, filtro, tipo);
        return ret;
    }

    public es.caib.regweb.ws.model.ListaResultados buscarOficinasFisicas(java.lang.String usuario, java.lang.String password, java.lang.String usuarioRegistro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ListaResultados ret = impl.buscarOficinasFisicas(usuario, password, usuarioRegistro, tipo);
        return ret;
    }

    public es.caib.regweb.ws.model.ListaResultados buscarDocumentos(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ListaResultados ret = impl.buscarDocumentos(usuario, password);
        return ret;
    }

    public es.caib.regweb.ws.model.ListaResultados buscarTodosDestinatarios(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.model.RegwebFacadeException
    {
        es.caib.regweb.ws.model.ListaResultados ret = impl.buscarTodosDestinatarios(usuario, password);
        return ret;
    }
    



}
