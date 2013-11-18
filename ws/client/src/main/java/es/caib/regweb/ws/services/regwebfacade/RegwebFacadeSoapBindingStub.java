/**
 * RegwebFacadeSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services.regwebfacade;

public class RegwebFacadeSoapBindingStub extends org.apache.axis.client.Stub implements es.caib.regweb.ws.services.regwebfacade.RegwebFacade_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[14];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("actualizarEntrada");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarEntradaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("actualizarSalida");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarSalidaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("anularEntrada");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anular"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularEntradaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("anularSalida");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anular"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularSalidaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("grabarEntrada");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarEntradaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("grabarSalida");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarSalidaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("leerEntrada");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerEntradaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("leerSalida");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerSalidaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("validarEntrada");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosEntrada"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarEntradaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("validarSalida");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "parametrosSalida"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"), es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarSalidaReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("buscarOficinasFisicasDescripcion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "filtro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicasDescripcionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("buscarOficinasFisicas");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuarioRegistro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "tipo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicasReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("buscarDocumentos");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarDocumentosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("buscarTodosDestinatarios");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados"));
        oper.setReturnClass(es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
        oper.setReturnQName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarTodosDestinatariosReturn"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "fault"),
                      "es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException",
                      new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException"), 
                      true
                     ));
        _operations[13] = oper;

    }

    public RegwebFacadeSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public RegwebFacadeSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public RegwebFacadeSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "errorEntrada");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ErrorEntrada.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "listaErroresEntrada");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ListaErroresEntrada.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroPublicadoEntradaWS");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroPublicadoEntradaWS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "errorSalida");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ErrorSalida.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "listaErroresSalida");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ListaErroresSalida.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "listaResultados");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.ListaResultados.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "RegwebFacadeException");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS actualizarEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarEntrada"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosEntrada});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS actualizarSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "actualizarSalida"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosSalida});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean anularEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularEntrada"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosEntrada, new java.lang.Boolean(anular)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public boolean anularSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida, boolean anular) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "anularSalida"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosSalida, new java.lang.Boolean(anular)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS grabarEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarEntrada"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosEntrada});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS grabarSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "grabarSalida"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosSalida});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS leerEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerEntrada"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosEntrada});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS leerSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "leerSalida"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosSalida});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS validarEntrada(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS parametrosEntrada) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarEntrada"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosEntrada});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroEntradaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS validarSalida(es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS parametrosSalida) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "validarSalida"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parametrosSalida});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ParametrosRegistroSalidaWS.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarOficinasFisicasDescripcion(java.lang.String usuario, java.lang.String password, java.lang.String filtro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicasDescripcion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {usuario, password, filtro, tipo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarOficinasFisicas(java.lang.String usuario, java.lang.String password, java.lang.String usuarioRegistro, java.lang.String tipo) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarOficinasFisicas"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {usuario, password, usuarioRegistro, tipo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarDocumentos(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarDocumentos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {usuario, password});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb.ws.services.regwebfacade.ListaResultados buscarTodosDestinatarios(java.lang.String usuario, java.lang.String password) throws java.rmi.RemoteException, es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:RegwebFacade", "buscarTodosDestinatarios"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {usuario, password});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb.ws.services.regwebfacade.ListaResultados) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb.ws.services.regwebfacade.ListaResultados.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) {
              throw (es.caib.regweb.ws.services.regwebfacade.RegwebFacadeException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
