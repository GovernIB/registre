/**
 * RegWebInfoWsServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegWebInfoWsServiceSoapBindingStub extends org.apache.axis.client.Stub implements es.caib.regweb3.ws.v3.apiaxis.RegWebInfoWs_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[11];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarLibroOrganismo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "organismo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.LibroWs.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarCodigoAsunto");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "codigoTipoAsunto"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "codigoAsuntoWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("obtenerLibrosOficinaUsuario");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "usuario"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipoRegistro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), java.lang.Long.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroOficinaWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVersion");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarTipoAsunto");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "tipoAsuntoWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarLibros");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "oficinaCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "autorizacion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), java.lang.Long.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.LibroWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarOrganismos");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "organismoWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.OrganismoWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarTipoDocumental");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "tipoDocumentalWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVersionWs");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarOficinas");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "autorizacion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), java.lang.Long.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "oficinaWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.OficinaWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("obtenerLibrosOficina");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "tipoRegistro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), java.lang.Long.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroOficinaWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[10] = oper;

    }

    public RegWebInfoWsServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public RegWebInfoWsServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public RegWebInfoWsServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.1");
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
            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "codigoAsuntoWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroOficinaWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.LibroWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "oficinaWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.OficinaWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "organismoWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.OrganismoWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "tipoAsuntoWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "tipoDocumentalWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsI18NArgument");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.WsI18NError.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsI18NTranslation");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.WsI18NTranslation.class;
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

    public es.caib.regweb3.ws.v3.apiaxis.LibroWs listarLibroOrganismo(java.lang.String entidad, java.lang.String organismo) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarLibroOrganismo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidad, organismo});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroWs) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroWs) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.LibroWs.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs[] listarCodigoAsunto(java.lang.String entidadCodigoDir3, java.lang.String codigoTipoAsunto) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarCodigoAsunto"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3, codigoTipoAsunto});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.CodigoAsuntoWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[] obtenerLibrosOficinaUsuario(java.lang.String entidadCodigoDir3, java.lang.String usuario, java.lang.Long tipoRegistro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "obtenerLibrosOficinaUsuario"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3, usuario, tipoRegistro});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String getVersion() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "getVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs[] listarTipoAsunto(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarTipoAsunto"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.LibroWs[] listarLibros(java.lang.String entidadCodigoDir3, java.lang.String oficinaCodigoDir3, java.lang.Long autorizacion) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarLibros"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3, oficinaCodigoDir3, autorizacion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.LibroWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.OrganismoWs[] listarOrganismos(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarOrganismos"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.OrganismoWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.OrganismoWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.OrganismoWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs[] listarTipoDocumental(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarTipoDocumental"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.TipoDocumentalWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public int getVersionWs() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "getVersionWs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.OficinaWs[] listarOficinas(java.lang.String entidadCodigoDir3, java.lang.Long autorizacion) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarOficinas"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3, autorizacion});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.OficinaWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.OficinaWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.OficinaWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[] obtenerLibrosOficina(java.lang.String entidadCodigoDir3, java.lang.Long tipoRegistro) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "obtenerLibrosOficina"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3, tipoRegistro});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.LibroOficinaWs[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
