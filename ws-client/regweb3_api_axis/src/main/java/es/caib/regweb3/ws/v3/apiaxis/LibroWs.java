/**
 * LibroWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class LibroWs  implements java.io.Serializable {
    private java.lang.String codigoLibro;

    private java.lang.String codigoOrganismo;

    private java.lang.String nombreCorto;

    private java.lang.String nombreLargo;

    public LibroWs() {
    }

    public LibroWs(
           java.lang.String codigoLibro,
           java.lang.String codigoOrganismo,
           java.lang.String nombreCorto,
           java.lang.String nombreLargo) {
           this.codigoLibro = codigoLibro;
           this.codigoOrganismo = codigoOrganismo;
           this.nombreCorto = nombreCorto;
           this.nombreLargo = nombreLargo;
    }


    /**
     * Gets the codigoLibro value for this LibroWs.
     * 
     * @return codigoLibro
     */
    public java.lang.String getCodigoLibro() {
        return codigoLibro;
    }


    /**
     * Sets the codigoLibro value for this LibroWs.
     * 
     * @param codigoLibro
     */
    public void setCodigoLibro(java.lang.String codigoLibro) {
        this.codigoLibro = codigoLibro;
    }


    /**
     * Gets the codigoOrganismo value for this LibroWs.
     * 
     * @return codigoOrganismo
     */
    public java.lang.String getCodigoOrganismo() {
        return codigoOrganismo;
    }


    /**
     * Sets the codigoOrganismo value for this LibroWs.
     * 
     * @param codigoOrganismo
     */
    public void setCodigoOrganismo(java.lang.String codigoOrganismo) {
        this.codigoOrganismo = codigoOrganismo;
    }


    /**
     * Gets the nombreCorto value for this LibroWs.
     * 
     * @return nombreCorto
     */
    public java.lang.String getNombreCorto() {
        return nombreCorto;
    }


    /**
     * Sets the nombreCorto value for this LibroWs.
     * 
     * @param nombreCorto
     */
    public void setNombreCorto(java.lang.String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }


    /**
     * Gets the nombreLargo value for this LibroWs.
     * 
     * @return nombreLargo
     */
    public java.lang.String getNombreLargo() {
        return nombreLargo;
    }


    /**
     * Sets the nombreLargo value for this LibroWs.
     * 
     * @param nombreLargo
     */
    public void setNombreLargo(java.lang.String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LibroWs)) return false;
        LibroWs other = (LibroWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigoLibro==null && other.getCodigoLibro()==null) || 
             (this.codigoLibro!=null &&
              this.codigoLibro.equals(other.getCodigoLibro()))) &&
            ((this.codigoOrganismo==null && other.getCodigoOrganismo()==null) || 
             (this.codigoOrganismo!=null &&
              this.codigoOrganismo.equals(other.getCodigoOrganismo()))) &&
            ((this.nombreCorto==null && other.getNombreCorto()==null) || 
             (this.nombreCorto!=null &&
              this.nombreCorto.equals(other.getNombreCorto()))) &&
            ((this.nombreLargo==null && other.getNombreLargo()==null) || 
             (this.nombreLargo!=null &&
              this.nombreLargo.equals(other.getNombreLargo())));
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
        if (getCodigoLibro() != null) {
            _hashCode += getCodigoLibro().hashCode();
        }
        if (getCodigoOrganismo() != null) {
            _hashCode += getCodigoOrganismo().hashCode();
        }
        if (getNombreCorto() != null) {
            _hashCode += getNombreCorto().hashCode();
        }
        if (getNombreLargo() != null) {
            _hashCode += getNombreLargo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LibroWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "libroWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoLibro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoLibro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoOrganismo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoOrganismo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCorto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreCorto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreLargo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreLargo"));
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
