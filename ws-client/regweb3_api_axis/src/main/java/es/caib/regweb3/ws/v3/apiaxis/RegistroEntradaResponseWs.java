/**
 * RegistroEntradaResponseWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegistroEntradaResponseWs  extends es.caib.regweb3.ws.v3.apiaxis.RegistroResponseWs  implements java.io.Serializable {
    private java.lang.String destinoCodigo;

    private java.lang.String destinoDenominacion;

    public RegistroEntradaResponseWs() {
    }

    public RegistroEntradaResponseWs(
           es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos,
           java.lang.String aplicacion,
           java.lang.String codigoAsuntoCodigo,
           java.lang.String codigoAsuntoDescripcion,
           java.lang.String codigoUsuario,
           java.lang.String contactoUsuario,
           java.lang.String docFisicaCodigo,
           java.lang.String docFisicaDescripcion,
           java.lang.String entidadCodigo,
           java.lang.String entidadDenominacion,
           java.lang.String expone,
           java.lang.String extracto,
           java.util.Calendar fechaOrigen,
           java.util.Calendar fechaRegistro,
           java.lang.String idiomaCodigo,
           java.lang.String idiomaDescripcion,
           es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados,
           java.lang.String libroCodigo,
           java.lang.String libroDescripcion,
           java.lang.String nombreUsuario,
           java.lang.String numExpediente,
           java.lang.String numTransporte,
           int numeroRegistro,
           java.lang.String numeroRegistroFormateado,
           java.lang.String numeroRegistroOrigen,
           java.lang.String observaciones,
           java.lang.String oficinaCodigo,
           java.lang.String oficinaDenominacion,
           java.lang.String refExterna,
           java.lang.String solicita,
           java.lang.String tipoAsuntoCodigo,
           java.lang.String tipoAsuntoDescripcion,
           java.lang.String tipoTransporteCodigo,
           java.lang.String tipoTransporteDescripcion,
           java.lang.String version,
           java.lang.String destinoCodigo,
           java.lang.String destinoDenominacion) {
        super(
            anexos,
            aplicacion,
            codigoAsuntoCodigo,
            codigoAsuntoDescripcion,
            codigoUsuario,
            contactoUsuario,
            docFisicaCodigo,
            docFisicaDescripcion,
            entidadCodigo,
            entidadDenominacion,
            expone,
            extracto,
            fechaOrigen,
            fechaRegistro,
            idiomaCodigo,
            idiomaDescripcion,
            interesados,
            libroCodigo,
            libroDescripcion,
            nombreUsuario,
            numExpediente,
            numTransporte,
            numeroRegistro,
            numeroRegistroFormateado,
            numeroRegistroOrigen,
            observaciones,
            oficinaCodigo,
            oficinaDenominacion,
            refExterna,
            solicita,
            tipoAsuntoCodigo,
            tipoAsuntoDescripcion,
            tipoTransporteCodigo,
            tipoTransporteDescripcion,
            version);
        this.destinoCodigo = destinoCodigo;
        this.destinoDenominacion = destinoDenominacion;
    }


    /**
     * Gets the destinoCodigo value for this RegistroEntradaResponseWs.
     * 
     * @return destinoCodigo
     */
    public java.lang.String getDestinoCodigo() {
        return destinoCodigo;
    }


    /**
     * Sets the destinoCodigo value for this RegistroEntradaResponseWs.
     * 
     * @param destinoCodigo
     */
    public void setDestinoCodigo(java.lang.String destinoCodigo) {
        this.destinoCodigo = destinoCodigo;
    }


    /**
     * Gets the destinoDenominacion value for this RegistroEntradaResponseWs.
     * 
     * @return destinoDenominacion
     */
    public java.lang.String getDestinoDenominacion() {
        return destinoDenominacion;
    }


    /**
     * Sets the destinoDenominacion value for this RegistroEntradaResponseWs.
     * 
     * @param destinoDenominacion
     */
    public void setDestinoDenominacion(java.lang.String destinoDenominacion) {
        this.destinoDenominacion = destinoDenominacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroEntradaResponseWs)) return false;
        RegistroEntradaResponseWs other = (RegistroEntradaResponseWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.destinoCodigo==null && other.getDestinoCodigo()==null) || 
             (this.destinoCodigo!=null &&
              this.destinoCodigo.equals(other.getDestinoCodigo()))) &&
            ((this.destinoDenominacion==null && other.getDestinoDenominacion()==null) || 
             (this.destinoDenominacion!=null &&
              this.destinoDenominacion.equals(other.getDestinoDenominacion())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getDestinoCodigo() != null) {
            _hashCode += getDestinoCodigo().hashCode();
        }
        if (getDestinoDenominacion() != null) {
            _hashCode += getDestinoDenominacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroEntradaResponseWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "registroEntradaResponseWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinoCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinoCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinoDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinoDenominacion"));
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
