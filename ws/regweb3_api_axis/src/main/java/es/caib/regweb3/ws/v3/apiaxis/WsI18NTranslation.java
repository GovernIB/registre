/**
 * WsI18NTranslation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class WsI18NTranslation  implements java.io.Serializable {
    private java.lang.String code;

    private es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument[] args;

    public WsI18NTranslation() {
    }

    public WsI18NTranslation(
           java.lang.String code,
           es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument[] args) {
           this.code = code;
           this.args = args;
    }


    /**
     * Gets the code value for this WsI18NTranslation.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this WsI18NTranslation.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the args value for this WsI18NTranslation.
     * 
     * @return args
     */
    public es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument[] getArgs() {
        return args;
    }


    /**
     * Sets the args value for this WsI18NTranslation.
     * 
     * @param args
     */
    public void setArgs(es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument[] args) {
        this.args = args;
    }

    public es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument getArgs(int i) {
        return this.args[i];
    }

    public void setArgs(int i, es.caib.regweb3.ws.v3.apiaxis.WsI18NArgument _value) {
        this.args[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsI18NTranslation)) return false;
        WsI18NTranslation other = (WsI18NTranslation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.args==null && other.getArgs()==null) || 
             (this.args!=null &&
              java.util.Arrays.equals(this.args, other.getArgs())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getArgs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getArgs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getArgs(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsI18NTranslation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsI18NTranslation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("args");
        elemField.setXmlName(new javax.xml.namespace.QName("", "args"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsI18NArgument"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
