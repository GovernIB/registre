/**
 * ParametrosRegistroPublicadoEntradaWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services.regwebfacade;

public class ParametrosRegistroPublicadoEntradaWS  implements java.io.Serializable {
    private int anoEntrada;

    private int numero;

    private int oficina;

    private int numeroBOCAIB;

    private int fecha;

    private int pagina;

    private int lineas;

    private java.lang.String contenido;

    private java.lang.String observaciones;

    private boolean leido;

    private boolean errorfecha;

    public ParametrosRegistroPublicadoEntradaWS() {
    }

    public ParametrosRegistroPublicadoEntradaWS(
           int anoEntrada,
           int numero,
           int oficina,
           int numeroBOCAIB,
           int fecha,
           int pagina,
           int lineas,
           java.lang.String contenido,
           java.lang.String observaciones,
           boolean leido,
           boolean errorfecha) {
           this.anoEntrada = anoEntrada;
           this.numero = numero;
           this.oficina = oficina;
           this.numeroBOCAIB = numeroBOCAIB;
           this.fecha = fecha;
           this.pagina = pagina;
           this.lineas = lineas;
           this.contenido = contenido;
           this.observaciones = observaciones;
           this.leido = leido;
           this.errorfecha = errorfecha;
    }


    /**
     * Gets the anoEntrada value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return anoEntrada
     */
    public int getAnoEntrada() {
        return anoEntrada;
    }


    /**
     * Sets the anoEntrada value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param anoEntrada
     */
    public void setAnoEntrada(int anoEntrada) {
        this.anoEntrada = anoEntrada;
    }


    /**
     * Gets the numero value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return numero
     */
    public int getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }


    /**
     * Gets the oficina value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return oficina
     */
    public int getOficina() {
        return oficina;
    }


    /**
     * Sets the oficina value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param oficina
     */
    public void setOficina(int oficina) {
        this.oficina = oficina;
    }


    /**
     * Gets the numeroBOCAIB value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return numeroBOCAIB
     */
    public int getNumeroBOCAIB() {
        return numeroBOCAIB;
    }


    /**
     * Sets the numeroBOCAIB value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param numeroBOCAIB
     */
    public void setNumeroBOCAIB(int numeroBOCAIB) {
        this.numeroBOCAIB = numeroBOCAIB;
    }


    /**
     * Gets the fecha value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return fecha
     */
    public int getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param fecha
     */
    public void setFecha(int fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the pagina value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return pagina
     */
    public int getPagina() {
        return pagina;
    }


    /**
     * Sets the pagina value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param pagina
     */
    public void setPagina(int pagina) {
        this.pagina = pagina;
    }


    /**
     * Gets the lineas value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return lineas
     */
    public int getLineas() {
        return lineas;
    }


    /**
     * Sets the lineas value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param lineas
     */
    public void setLineas(int lineas) {
        this.lineas = lineas;
    }


    /**
     * Gets the contenido value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return contenido
     */
    public java.lang.String getContenido() {
        return contenido;
    }


    /**
     * Sets the contenido value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param contenido
     */
    public void setContenido(java.lang.String contenido) {
        this.contenido = contenido;
    }


    /**
     * Gets the observaciones value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the leido value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return leido
     */
    public boolean isLeido() {
        return leido;
    }


    /**
     * Sets the leido value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param leido
     */
    public void setLeido(boolean leido) {
        this.leido = leido;
    }


    /**
     * Gets the errorfecha value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @return errorfecha
     */
    public boolean isErrorfecha() {
        return errorfecha;
    }


    /**
     * Sets the errorfecha value for this ParametrosRegistroPublicadoEntradaWS.
     * 
     * @param errorfecha
     */
    public void setErrorfecha(boolean errorfecha) {
        this.errorfecha = errorfecha;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParametrosRegistroPublicadoEntradaWS)) return false;
        ParametrosRegistroPublicadoEntradaWS other = (ParametrosRegistroPublicadoEntradaWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.anoEntrada == other.getAnoEntrada() &&
            this.numero == other.getNumero() &&
            this.oficina == other.getOficina() &&
            this.numeroBOCAIB == other.getNumeroBOCAIB() &&
            this.fecha == other.getFecha() &&
            this.pagina == other.getPagina() &&
            this.lineas == other.getLineas() &&
            ((this.contenido==null && other.getContenido()==null) || 
             (this.contenido!=null &&
              this.contenido.equals(other.getContenido()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            this.leido == other.isLeido() &&
            this.errorfecha == other.isErrorfecha();
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
        _hashCode += getAnoEntrada();
        _hashCode += getNumero();
        _hashCode += getOficina();
        _hashCode += getNumeroBOCAIB();
        _hashCode += getFecha();
        _hashCode += getPagina();
        _hashCode += getLineas();
        if (getContenido() != null) {
            _hashCode += getContenido().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        _hashCode += (isLeido() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isErrorfecha() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParametrosRegistroPublicadoEntradaWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroPublicadoEntradaWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anoEntrada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anoEntrada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numero");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oficina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oficina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroBOCAIB");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroBOCAIB"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "pagina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineas");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lineas"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contenido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contenido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "leido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorfecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errorfecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
