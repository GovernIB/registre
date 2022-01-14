/**
 * CrearAsientoRegistral.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class CrearAsientoRegistral  implements java.io.Serializable {
    private java.lang.Long idSesion;

    private java.lang.String entidad;

    private es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistral;

    private java.lang.Long tipoOperacion;

    private java.lang.Boolean justificante;

    private java.lang.Boolean distribuir;

    public CrearAsientoRegistral() {
    }

    public CrearAsientoRegistral(
           java.lang.Long idSesion,
           java.lang.String entidad,
           es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistral,
           java.lang.Long tipoOperacion,
           java.lang.Boolean justificante,
           java.lang.Boolean distribuir) {
           this.idSesion = idSesion;
           this.entidad = entidad;
           this.asientoRegistral = asientoRegistral;
           this.tipoOperacion = tipoOperacion;
           this.justificante = justificante;
           this.distribuir = distribuir;
    }


    /**
     * Gets the idSesion value for this CrearAsientoRegistral.
     * 
     * @return idSesion
     */
    public java.lang.Long getIdSesion() {
        return idSesion;
    }


    /**
     * Sets the idSesion value for this CrearAsientoRegistral.
     * 
     * @param idSesion
     */
    public void setIdSesion(java.lang.Long idSesion) {
        this.idSesion = idSesion;
    }


    /**
     * Gets the entidad value for this CrearAsientoRegistral.
     * 
     * @return entidad
     */
    public java.lang.String getEntidad() {
        return entidad;
    }


    /**
     * Sets the entidad value for this CrearAsientoRegistral.
     * 
     * @param entidad
     */
    public void setEntidad(java.lang.String entidad) {
        this.entidad = entidad;
    }


    /**
     * Gets the asientoRegistral value for this CrearAsientoRegistral.
     * 
     * @return asientoRegistral
     */
    public es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs getAsientoRegistral() {
        return asientoRegistral;
    }


    /**
     * Sets the asientoRegistral value for this CrearAsientoRegistral.
     * 
     * @param asientoRegistral
     */
    public void setAsientoRegistral(es.caib.regweb3.ws.v3.apiaxis.AsientoRegistralWs asientoRegistral) {
        this.asientoRegistral = asientoRegistral;
    }


    /**
     * Gets the tipoOperacion value for this CrearAsientoRegistral.
     * 
     * @return tipoOperacion
     */
    public java.lang.Long getTipoOperacion() {
        return tipoOperacion;
    }


    /**
     * Sets the tipoOperacion value for this CrearAsientoRegistral.
     * 
     * @param tipoOperacion
     */
    public void setTipoOperacion(java.lang.Long tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }


    /**
     * Gets the justificante value for this CrearAsientoRegistral.
     * 
     * @return justificante
     */
    public java.lang.Boolean getJustificante() {
        return justificante;
    }


    /**
     * Sets the justificante value for this CrearAsientoRegistral.
     * 
     * @param justificante
     */
    public void setJustificante(java.lang.Boolean justificante) {
        this.justificante = justificante;
    }


    /**
     * Gets the distribuir value for this CrearAsientoRegistral.
     * 
     * @return distribuir
     */
    public java.lang.Boolean getDistribuir() {
        return distribuir;
    }


    /**
     * Sets the distribuir value for this CrearAsientoRegistral.
     * 
     * @param distribuir
     */
    public void setDistribuir(java.lang.Boolean distribuir) {
        this.distribuir = distribuir;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CrearAsientoRegistral)) return false;
        CrearAsientoRegistral other = (CrearAsientoRegistral) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idSesion==null && other.getIdSesion()==null) || 
             (this.idSesion!=null &&
              this.idSesion.equals(other.getIdSesion()))) &&
            ((this.entidad==null && other.getEntidad()==null) || 
             (this.entidad!=null &&
              this.entidad.equals(other.getEntidad()))) &&
            ((this.asientoRegistral==null && other.getAsientoRegistral()==null) || 
             (this.asientoRegistral!=null &&
              this.asientoRegistral.equals(other.getAsientoRegistral()))) &&
            ((this.tipoOperacion==null && other.getTipoOperacion()==null) || 
             (this.tipoOperacion!=null &&
              this.tipoOperacion.equals(other.getTipoOperacion()))) &&
            ((this.justificante==null && other.getJustificante()==null) || 
             (this.justificante!=null &&
              this.justificante.equals(other.getJustificante()))) &&
            ((this.distribuir==null && other.getDistribuir()==null) || 
             (this.distribuir!=null &&
              this.distribuir.equals(other.getDistribuir())));
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
        if (getIdSesion() != null) {
            _hashCode += getIdSesion().hashCode();
        }
        if (getEntidad() != null) {
            _hashCode += getEntidad().hashCode();
        }
        if (getAsientoRegistral() != null) {
            _hashCode += getAsientoRegistral().hashCode();
        }
        if (getTipoOperacion() != null) {
            _hashCode += getTipoOperacion().hashCode();
        }
        if (getJustificante() != null) {
            _hashCode += getJustificante().hashCode();
        }
        if (getDistribuir() != null) {
            _hashCode += getDistribuir().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CrearAsientoRegistral.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "crearAsientoRegistral"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idSesion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idSesion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("asientoRegistral");
        elemField.setXmlName(new javax.xml.namespace.QName("", "asientoRegistral"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "asientoRegistralWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justificante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "justificante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distribuir");
        elemField.setXmlName(new javax.xml.namespace.QName("", "distribuir"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
