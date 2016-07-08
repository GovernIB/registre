/**
 * LibroOficinaWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class LibroOficinaWs  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.LibroWs libroWs;

    private es.caib.regweb3.ws.v3.apiaxis.OficinaWs oficinaWs;

    public LibroOficinaWs() {
    }

    public LibroOficinaWs(
           es.caib.regweb3.ws.v3.apiaxis.LibroWs libroWs,
           es.caib.regweb3.ws.v3.apiaxis.OficinaWs oficinaWs) {
           this.libroWs = libroWs;
           this.oficinaWs = oficinaWs;
    }


    /**
     * Gets the libroWs value for this LibroOficinaWs.
     * 
     * @return libroWs
     */
    public es.caib.regweb3.ws.v3.apiaxis.LibroWs getLibroWs() {
        return libroWs;
    }


    /**
     * Sets the libroWs value for this LibroOficinaWs.
     * 
     * @param libroWs
     */
    public void setLibroWs(es.caib.regweb3.ws.v3.apiaxis.LibroWs libroWs) {
        this.libroWs = libroWs;
    }


    /**
     * Gets the oficinaWs value for this LibroOficinaWs.
     * 
     * @return oficinaWs
     */
    public es.caib.regweb3.ws.v3.apiaxis.OficinaWs getOficinaWs() {
        return oficinaWs;
    }


    /**
     * Sets the oficinaWs value for this LibroOficinaWs.
     * 
     * @param oficinaWs
     */
    public void setOficinaWs(es.caib.regweb3.ws.v3.apiaxis.OficinaWs oficinaWs) {
        this.oficinaWs = oficinaWs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LibroOficinaWs)) return false;
        LibroOficinaWs other = (LibroOficinaWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.libroWs==null && other.getLibroWs()==null) || 
             (this.libroWs!=null &&
              this.libroWs.equals(other.getLibroWs()))) &&
            ((this.oficinaWs==null && other.getOficinaWs()==null) || 
             (this.oficinaWs!=null &&
              this.oficinaWs.equals(other.getOficinaWs())));
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
        if (getLibroWs() != null) {
            _hashCode += getLibroWs().hashCode();
        }
        if (getOficinaWs() != null) {
            _hashCode += getOficinaWs().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LibroOficinaWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroOficinaWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("libroWs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "libroWs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oficinaWs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oficinaWs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "oficinaWs"));
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
