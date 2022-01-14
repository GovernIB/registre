/**
 * InteresadoWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class InteresadoWs  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs interesado;

    private es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs representante;

    public InteresadoWs() {
    }

    public InteresadoWs(
           es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs interesado,
           es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs representante) {
           this.interesado = interesado;
           this.representante = representante;
    }


    /**
     * Gets the interesado value for this InteresadoWs.
     * 
     * @return interesado
     */
    public es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs getInteresado() {
        return interesado;
    }


    /**
     * Sets the interesado value for this InteresadoWs.
     * 
     * @param interesado
     */
    public void setInteresado(es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs interesado) {
        this.interesado = interesado;
    }


    /**
     * Gets the representante value for this InteresadoWs.
     * 
     * @return representante
     */
    public es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs getRepresentante() {
        return representante;
    }


    /**
     * Sets the representante value for this InteresadoWs.
     * 
     * @param representante
     */
    public void setRepresentante(es.caib.regweb3.ws.v3.apiaxis.DatosInteresadoWs representante) {
        this.representante = representante;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InteresadoWs)) return false;
        InteresadoWs other = (InteresadoWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.interesado==null && other.getInteresado()==null) || 
             (this.interesado!=null &&
              this.interesado.equals(other.getInteresado()))) &&
            ((this.representante==null && other.getRepresentante()==null) || 
             (this.representante!=null &&
              this.representante.equals(other.getRepresentante())));
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
        if (getInteresado() != null) {
            _hashCode += getInteresado().hashCode();
        }
        if (getRepresentante() != null) {
            _hashCode += getRepresentante().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InteresadoWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "interesadoWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interesado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "interesado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "datosInteresadoWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("representante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "representante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "datosInteresadoWs"));
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
