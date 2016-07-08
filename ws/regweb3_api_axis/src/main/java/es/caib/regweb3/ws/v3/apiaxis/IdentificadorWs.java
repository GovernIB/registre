/**
 * IdentificadorWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class IdentificadorWs  implements java.io.Serializable {
    private java.util.Calendar fecha;

    private java.lang.Integer numero;

    private java.lang.String numeroRegistroFormateado;

    public IdentificadorWs() {
    }

    public IdentificadorWs(
           java.util.Calendar fecha,
           java.lang.Integer numero,
           java.lang.String numeroRegistroFormateado) {
           this.fecha = fecha;
           this.numero = numero;
           this.numeroRegistroFormateado = numeroRegistroFormateado;
    }


    /**
     * Gets the fecha value for this IdentificadorWs.
     * 
     * @return fecha
     */
    public java.util.Calendar getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this IdentificadorWs.
     * 
     * @param fecha
     */
    public void setFecha(java.util.Calendar fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the numero value for this IdentificadorWs.
     * 
     * @return numero
     */
    public java.lang.Integer getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this IdentificadorWs.
     * 
     * @param numero
     */
    public void setNumero(java.lang.Integer numero) {
        this.numero = numero;
    }


    /**
     * Gets the numeroRegistroFormateado value for this IdentificadorWs.
     * 
     * @return numeroRegistroFormateado
     */
    public java.lang.String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }


    /**
     * Sets the numeroRegistroFormateado value for this IdentificadorWs.
     * 
     * @param numeroRegistroFormateado
     */
    public void setNumeroRegistroFormateado(java.lang.String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdentificadorWs)) return false;
        IdentificadorWs other = (IdentificadorWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fecha==null && other.getFecha()==null) || 
             (this.fecha!=null &&
              this.fecha.equals(other.getFecha()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
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
        if (getFecha() != null) {
            _hashCode += getFecha().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getNumeroRegistroFormateado() != null) {
            _hashCode += getNumeroRegistroFormateado().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdentificadorWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "identificadorWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
