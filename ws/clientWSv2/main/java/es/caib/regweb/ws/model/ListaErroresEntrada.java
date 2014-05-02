/**
 * ListaErroresEntrada.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.model;

public class ListaErroresEntrada  implements java.io.Serializable {
    private ErrorEntrada[] errores;

    public ListaErroresEntrada() {
    }

    public ListaErroresEntrada(
           ErrorEntrada[] errores) {
           this.errores = errores;
    }


    /**
     * Gets the errores value for this ListaErroresEntrada.
     * 
     * @return errores
     */
    public ErrorEntrada[] getErrores() {
        return errores;
    }


    /**
     * Sets the errores value for this ListaErroresEntrada.
     * 
     * @param errores
     */
    public void setErrores(ErrorEntrada[] errores) {
        this.errores = errores;
    }

    public ErrorEntrada getErrores(int i) {
        return this.errores[i];
    }

    public void setErrores(int i, ErrorEntrada _value) {
        this.errores[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListaErroresEntrada)) return false;
        ListaErroresEntrada other = (ListaErroresEntrada) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.errores==null && other.getErrores()==null) || 
             (this.errores!=null &&
              java.util.Arrays.equals(this.errores, other.getErrores())));
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
        if (getErrores() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrores());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getErrores(), i);
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
        new org.apache.axis.description.TypeDesc(ListaErroresEntrada.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "listaErroresEntrada"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "errorEntrada"));
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

}
