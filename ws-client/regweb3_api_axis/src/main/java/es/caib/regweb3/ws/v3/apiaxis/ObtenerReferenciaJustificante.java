/**
 * ObtenerReferenciaJustificante.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class ObtenerReferenciaJustificante  implements java.io.Serializable {
    private java.lang.String entidad;

    private java.lang.String numeroRegistroFormateado;

    public ObtenerReferenciaJustificante() {
    }

    public ObtenerReferenciaJustificante(
           java.lang.String entidad,
           java.lang.String numeroRegistroFormateado) {
           this.entidad = entidad;
           this.numeroRegistroFormateado = numeroRegistroFormateado;
    }


    /**
     * Gets the entidad value for this ObtenerReferenciaJustificante.
     * 
     * @return entidad
     */
    public java.lang.String getEntidad() {
        return entidad;
    }


    /**
     * Sets the entidad value for this ObtenerReferenciaJustificante.
     * 
     * @param entidad
     */
    public void setEntidad(java.lang.String entidad) {
        this.entidad = entidad;
    }


    /**
     * Gets the numeroRegistroFormateado value for this ObtenerReferenciaJustificante.
     * 
     * @return numeroRegistroFormateado
     */
    public java.lang.String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }


    /**
     * Sets the numeroRegistroFormateado value for this ObtenerReferenciaJustificante.
     * 
     * @param numeroRegistroFormateado
     */
    public void setNumeroRegistroFormateado(java.lang.String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtenerReferenciaJustificante)) return false;
        ObtenerReferenciaJustificante other = (ObtenerReferenciaJustificante) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.entidad==null && other.getEntidad()==null) || 
             (this.entidad!=null &&
              this.entidad.equals(other.getEntidad()))) &&
            ((this.numeroRegistroFormateado==null && other.getNumeroRegistroFormateado()==null) || 
             (this.numeroRegistroFormateado!=null &&
              this.numeroRegistroFormateado.equals(other.getNumeroRegistroFormateado())));
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
        if (getEntidad() != null) {
            _hashCode += getEntidad().hashCode();
        }
        if (getNumeroRegistroFormateado() != null) {
            _hashCode += getNumeroRegistroFormateado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ObtenerReferenciaJustificante.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "obtenerReferenciaJustificante"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroRegistroFormateado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistroFormateado"));
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
