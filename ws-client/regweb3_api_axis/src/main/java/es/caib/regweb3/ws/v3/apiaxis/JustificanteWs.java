/**
 * JustificanteWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class JustificanteWs  implements java.io.Serializable {
    private byte[] justificante;

    public JustificanteWs() {
    }

    public JustificanteWs(
           byte[] justificante) {
           this.justificante = justificante;
    }


    /**
     * Gets the justificante value for this JustificanteWs.
     * 
     * @return justificante
     */
    public byte[] getJustificante() {
        return justificante;
    }


    /**
     * Sets the justificante value for this JustificanteWs.
     * 
     * @param justificante
     */
    public void setJustificante(byte[] justificante) {
        this.justificante = justificante;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JustificanteWs)) return false;
        JustificanteWs other = (JustificanteWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.justificante==null && other.getJustificante()==null) || 
             (this.justificante!=null &&
              java.util.Arrays.equals(this.justificante, other.getJustificante())));
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
        if (getJustificante() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJustificante());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJustificante(), i);
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
        new org.apache.axis.description.TypeDesc(JustificanteWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "justificanteWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justificante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "justificante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
