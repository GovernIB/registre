/**
 * CodigoAsuntoWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class CodigoAsuntoWs  implements java.io.Serializable {
    private java.lang.String codigo;

    private java.lang.String nombre;

    private es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs tipoAsunto;

    public CodigoAsuntoWs() {
    }

    public CodigoAsuntoWs(
           java.lang.String codigo,
           java.lang.String nombre,
           es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs tipoAsunto) {
           this.codigo = codigo;
           this.nombre = nombre;
           this.tipoAsunto = tipoAsunto;
    }


    /**
     * Gets the codigo value for this CodigoAsuntoWs.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this CodigoAsuntoWs.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the nombre value for this CodigoAsuntoWs.
     * 
     * @return nombre
     */
    public java.lang.String getNombre() {
        return nombre;
    }


    /**
     * Sets the nombre value for this CodigoAsuntoWs.
     * 
     * @param nombre
     */
    public void setNombre(java.lang.String nombre) {
        this.nombre = nombre;
    }


    /**
     * Gets the tipoAsunto value for this CodigoAsuntoWs.
     * 
     * @return tipoAsunto
     */
    public es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs getTipoAsunto() {
        return tipoAsunto;
    }


    /**
     * Sets the tipoAsunto value for this CodigoAsuntoWs.
     * 
     * @param tipoAsunto
     */
    public void setTipoAsunto(es.caib.regweb3.ws.v3.apiaxis.TipoAsuntoWs tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CodigoAsuntoWs)) return false;
        CodigoAsuntoWs other = (CodigoAsuntoWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.nombre==null && other.getNombre()==null) || 
             (this.nombre!=null &&
              this.nombre.equals(other.getNombre()))) &&
            ((this.tipoAsunto==null && other.getTipoAsunto()==null) || 
             (this.tipoAsunto!=null &&
              this.tipoAsunto.equals(other.getTipoAsunto())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getNombre() != null) {
            _hashCode += getNombre().hashCode();
        }
        if (getTipoAsunto() != null) {
            _hashCode += getTipoAsunto().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CodigoAsuntoWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "codigoAsuntoWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombre");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAsunto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAsunto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "tipoAsuntoWs"));
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
