/**
 * RegWebPersonasWsServiceSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegWebPersonasWsServiceSoapBindingStub extends org.apache.axis.client.Stub implements es.caib.regweb3.ws.v3.apiaxis.RegWebPersonasWs_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[6];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("borrarPersona");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), java.lang.Long.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
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
        oper.setName("crearPersona");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personaWs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "personaWs"), es.caib.regweb3.ws.v3.apiaxis.PersonaWs.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        oper.setReturnClass(java.lang.Long.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsValidationErrors"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsValidationErrors"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVersion");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("listarPersonas");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "entidadCodigoDir3"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "personaWs"));
        oper.setReturnClass(es.caib.regweb3.ws.v3.apiaxis.PersonaWs[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getVersionWs");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("actualizarPersona");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "personaWs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "personaWs"), es.caib.regweb3.ws.v3.apiaxis.PersonaWs.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsValidationErrors"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsValidationErrors"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"),
                      "es.caib.regweb3.ws.v3.apiaxis.WsI18NError",
                      new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"), 
                      true
                     ));
        _operations[5] = oper;

    }

    public RegWebPersonasWsServiceSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public RegWebPersonasWsServiceSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public RegWebPersonasWsServiceSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "personaWs");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.PersonaWs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsFieldValidationError");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError.class;
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

            qName = new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsValidationErrors");
            cachedSerQNames.add(qName);
            cls = es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors.class;
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

    public void borrarPersona(java.lang.Long personaID) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "borrarPersona"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {personaID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
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

    public java.lang.Long crearPersona(es.caib.regweb3.ws.v3.apiaxis.PersonaWs personaWs) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "crearPersona"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {personaWs});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.Long) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.Long) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.Long.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors) axisFaultException.detail;
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
        _call.setOperation(_operations[2]);
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

    public es.caib.regweb3.ws.v3.apiaxis.PersonaWs[] listarPersonas(java.lang.String entidadCodigoDir3) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "listarPersonas"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {entidadCodigoDir3});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (es.caib.regweb3.ws.v3.apiaxis.PersonaWs[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (es.caib.regweb3.ws.v3.apiaxis.PersonaWs[]) org.apache.axis.utils.JavaUtils.convert(_resp, es.caib.regweb3.ws.v3.apiaxis.PersonaWs[].class);
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
        _call.setOperation(_operations[4]);
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

    public void actualizarPersona(es.caib.regweb3.ws.v3.apiaxis.PersonaWs personaWs) throws java.rmi.RemoteException, es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors, es.caib.regweb3.ws.v3.apiaxis.WsI18NError {
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
        _call.setOperationName(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "actualizarPersona"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {personaWs});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsValidationErrors) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof es.caib.regweb3.ws.v3.apiaxis.WsI18NError) {
              throw (es.caib.regweb3.ws.v3.apiaxis.WsI18NError) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
