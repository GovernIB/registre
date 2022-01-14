/**
 * WsI18NError.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class WsI18NError  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.WsI18NTranslation translation;

    public WsI18NError() {
    }

    public WsI18NError(
           es.caib.regweb3.ws.v3.apiaxis.WsI18NTranslation translation) {
        this.translation = translation;
    }


    /**
     * Gets the translation value for this WsI18NError.
     * 
     * @return translation
     */
    public es.caib.regweb3.ws.v3.apiaxis.WsI18NTranslation getTranslation() {
        return translation;
    }


    /**
     * Sets the translation value for this WsI18NError.
     * 
     * @param translation
     */
    public void setTranslation(es.caib.regweb3.ws.v3.apiaxis.WsI18NTranslation translation) {
        this.translation = translation;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsI18NError)) return false;
        WsI18NError other = (WsI18NError) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.translation==null && other.getTranslation()==null) || 
             (this.translation!=null &&
              this.translation.equals(other.getTranslation())));
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
        if (getTranslation() != null) {
            _hashCode += getTranslation().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsI18NError.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsI18NError"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("translation");
        elemField.setXmlName(new javax.xml.namespace.QName("", "translation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsI18NTranslation"));
        elemField.setNillable(true);
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
