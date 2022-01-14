/**
 * TipoDocumentalWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class TipoDocumentalWs  implements java.io.Serializable {
    private java.lang.String codigoNTI;

    public TipoDocumentalWs() {
    }

    public TipoDocumentalWs(
           java.lang.String codigoNTI) {
           this.codigoNTI = codigoNTI;
    }


    /**
     * Gets the codigoNTI value for this TipoDocumentalWs.
     * 
     * @return codigoNTI
     */
    public java.lang.String getCodigoNTI() {
        return codigoNTI;
    }


    /**
     * Sets the codigoNTI value for this TipoDocumentalWs.
     * 
     * @param codigoNTI
     */
    public void setCodigoNTI(java.lang.String codigoNTI) {
        this.codigoNTI = codigoNTI;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TipoDocumentalWs)) return false;
        TipoDocumentalWs other = (TipoDocumentalWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoNTI==null && other.getCodigoNTI()==null) || 
             (this.codigoNTI!=null &&
              this.codigoNTI.equals(other.getCodigoNTI())));
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
        if (getCodigoNTI() != null) {
            _hashCode += getCodigoNTI().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TipoDocumentalWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "tipoDocumentalWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoNTI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoNTI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
