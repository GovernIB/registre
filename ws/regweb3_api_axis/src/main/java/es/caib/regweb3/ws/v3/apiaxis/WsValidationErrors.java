/**
 * WsValidationErrors.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class WsValidationErrors  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError[] fieldFaults;

    public WsValidationErrors() {
    }

    public WsValidationErrors(
           es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError[] fieldFaults) {
        this.fieldFaults = fieldFaults;
    }


    /**
     * Gets the fieldFaults value for this WsValidationErrors.
     * 
     * @return fieldFaults
     */
    public es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError[] getFieldFaults() {
        return fieldFaults;
    }


    /**
     * Sets the fieldFaults value for this WsValidationErrors.
     * 
     * @param fieldFaults
     */
    public void setFieldFaults(es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError[] fieldFaults) {
        this.fieldFaults = fieldFaults;
    }

    public es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError getFieldFaults(int i) {
        return this.fieldFaults[i];
    }

    public void setFieldFaults(int i, es.caib.regweb3.ws.v3.apiaxis.WsFieldValidationError _value) {
        this.fieldFaults[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsValidationErrors)) return false;
        WsValidationErrors other = (WsValidationErrors) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fieldFaults==null && other.getFieldFaults()==null) || 
             (this.fieldFaults!=null &&
              java.util.Arrays.equals(this.fieldFaults, other.getFieldFaults())));
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
        if (getFieldFaults() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFieldFaults());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFieldFaults(), i);
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
        new org.apache.axis.description.TypeDesc(WsValidationErrors.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "WsValidationErrors"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldFaults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fieldFaults"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "wsFieldValidationError"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
