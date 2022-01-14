/**
 * AsientoRegistralSesionWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class AsientoRegistralSesionWs  implements java.io.Serializable {
    private java.lang.Long estado;

    private es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistralWs;

    public AsientoRegistralSesionWs() {
    }

    public AsientoRegistralSesionWs(
           java.lang.Long estado,
           es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistralWs) {
           this.estado = estado;
           this.asientoRegistralWs = asientoRegistralWs;
    }


    /**
     * Gets the estado value for this AsientoRegistralSesionWs.
     * 
     * @return estado
     */
    public java.lang.Long getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this AsientoRegistralSesionWs.
     * 
     * @param estado
     */
    public void setEstado(java.lang.Long estado) {
        this.estado = estado;
    }


    /**
     * Gets the asientoRegistralWs value for this AsientoRegistralSesionWs.
     * 
     * @return asientoRegistralWs
     */
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs getAsientoRegistralWs() {
        return asientoRegistralWs;
    }


    /**
     * Sets the asientoRegistralWs value for this AsientoRegistralSesionWs.
     * 
     * @param asientoRegistralWs
     */
    public void setAsientoRegistralWs(es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistralWs) {
        this.asientoRegistralWs = asientoRegistralWs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AsientoRegistralSesionWs)) return false;
        AsientoRegistralSesionWs other = (AsientoRegistralSesionWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.asientoRegistralWs==null && other.getAsientoRegistralWs()==null) || 
             (this.asientoRegistralWs!=null &&
              this.asientoRegistralWs.equals(other.getAsientoRegistralWs())));
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
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getAsientoRegistralWs() != null) {
            _hashCode += getAsientoRegistralWs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AsientoRegistralSesionWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "asientoRegistralSesionWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asientoRegistralWs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "asientoRegistralWs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "asientoRegistralWs"));
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
