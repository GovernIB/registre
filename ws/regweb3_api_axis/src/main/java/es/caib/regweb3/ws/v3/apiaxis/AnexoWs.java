/**
 * AnexoWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class AnexoWs  implements java.io.Serializable {
    private java.lang.String titulo;

    private java.lang.String nombreFicheroAnexado;

    private byte[] ficheroAnexado;

    private java.lang.String tipoMIMEFicheroAnexado;

    private java.lang.String tipoDocumental;

    private java.lang.String validezDocumento;

    private java.lang.String tipoDocumento;

    private java.lang.String observaciones;

    private java.lang.Integer origenCiudadanoAdmin;

    private java.util.Calendar fechaCaptura;

    private java.lang.Integer modoFirma;

    private java.lang.String nombreFirmaAnexada;

    private byte[] firmaAnexada;

    private java.lang.String tipoMIMEFirmaAnexada;

    private java.lang.String csv;

    private java.lang.Boolean justificante;

    public AnexoWs() {
    }

    public AnexoWs(
           java.lang.String titulo,
           java.lang.String nombreFicheroAnexado,
           byte[] ficheroAnexado,
           java.lang.String tipoMIMEFicheroAnexado,
           java.lang.String tipoDocumental,
           java.lang.String validezDocumento,
           java.lang.String tipoDocumento,
           java.lang.String observaciones,
           java.lang.Integer origenCiudadanoAdmin,
           java.util.Calendar fechaCaptura,
           java.lang.Integer modoFirma,
           java.lang.String nombreFirmaAnexada,
           byte[] firmaAnexada,
           java.lang.String tipoMIMEFirmaAnexada,
           java.lang.String csv,
           java.lang.Boolean justificante) {
           this.titulo = titulo;
           this.nombreFicheroAnexado = nombreFicheroAnexado;
           this.ficheroAnexado = ficheroAnexado;
           this.tipoMIMEFicheroAnexado = tipoMIMEFicheroAnexado;
           this.tipoDocumental = tipoDocumental;
           this.validezDocumento = validezDocumento;
           this.tipoDocumento = tipoDocumento;
           this.observaciones = observaciones;
           this.origenCiudadanoAdmin = origenCiudadanoAdmin;
           this.fechaCaptura = fechaCaptura;
           this.modoFirma = modoFirma;
           this.nombreFirmaAnexada = nombreFirmaAnexada;
           this.firmaAnexada = firmaAnexada;
           this.tipoMIMEFirmaAnexada = tipoMIMEFirmaAnexada;
           this.csv = csv;
           this.justificante = justificante;
    }


    /**
     * Gets the titulo value for this AnexoWs.
     * 
     * @return titulo
     */
    public java.lang.String getTitulo() {
        return titulo;
    }


    /**
     * Sets the titulo value for this AnexoWs.
     * 
     * @param titulo
     */
    public void setTitulo(java.lang.String titulo) {
        this.titulo = titulo;
    }


    /**
     * Gets the nombreFicheroAnexado value for this AnexoWs.
     * 
     * @return nombreFicheroAnexado
     */
    public java.lang.String getNombreFicheroAnexado() {
        return nombreFicheroAnexado;
    }


    /**
     * Sets the nombreFicheroAnexado value for this AnexoWs.
     * 
     * @param nombreFicheroAnexado
     */
    public void setNombreFicheroAnexado(java.lang.String nombreFicheroAnexado) {
        this.nombreFicheroAnexado = nombreFicheroAnexado;
    }


    /**
     * Gets the ficheroAnexado value for this AnexoWs.
     * 
     * @return ficheroAnexado
     */
    public byte[] getFicheroAnexado() {
        return ficheroAnexado;
    }


    /**
     * Sets the ficheroAnexado value for this AnexoWs.
     * 
     * @param ficheroAnexado
     */
    public void setFicheroAnexado(byte[] ficheroAnexado) {
        this.ficheroAnexado = ficheroAnexado;
    }


    /**
     * Gets the tipoMIMEFicheroAnexado value for this AnexoWs.
     * 
     * @return tipoMIMEFicheroAnexado
     */
    public java.lang.String getTipoMIMEFicheroAnexado() {
        return tipoMIMEFicheroAnexado;
    }


    /**
     * Sets the tipoMIMEFicheroAnexado value for this AnexoWs.
     * 
     * @param tipoMIMEFicheroAnexado
     */
    public void setTipoMIMEFicheroAnexado(java.lang.String tipoMIMEFicheroAnexado) {
        this.tipoMIMEFicheroAnexado = tipoMIMEFicheroAnexado;
    }


    /**
     * Gets the tipoDocumental value for this AnexoWs.
     * 
     * @return tipoDocumental
     */
    public java.lang.String getTipoDocumental() {
        return tipoDocumental;
    }


    /**
     * Sets the tipoDocumental value for this AnexoWs.
     * 
     * @param tipoDocumental
     */
    public void setTipoDocumental(java.lang.String tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }


    /**
     * Gets the validezDocumento value for this AnexoWs.
     * 
     * @return validezDocumento
     */
    public java.lang.String getValidezDocumento() {
        return validezDocumento;
    }


    /**
     * Sets the validezDocumento value for this AnexoWs.
     * 
     * @param validezDocumento
     */
    public void setValidezDocumento(java.lang.String validezDocumento) {
        this.validezDocumento = validezDocumento;
    }


    /**
     * Gets the tipoDocumento value for this AnexoWs.
     * 
     * @return tipoDocumento
     */
    public java.lang.String getTipoDocumento() {
        return tipoDocumento;
    }


    /**
     * Sets the tipoDocumento value for this AnexoWs.
     * 
     * @param tipoDocumento
     */
    public void setTipoDocumento(java.lang.String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }


    /**
     * Gets the observaciones value for this AnexoWs.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this AnexoWs.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the origenCiudadanoAdmin value for this AnexoWs.
     * 
     * @return origenCiudadanoAdmin
     */
    public java.lang.Integer getOrigenCiudadanoAdmin() {
        return origenCiudadanoAdmin;
    }


    /**
     * Sets the origenCiudadanoAdmin value for this AnexoWs.
     * 
     * @param origenCiudadanoAdmin
     */
    public void setOrigenCiudadanoAdmin(java.lang.Integer origenCiudadanoAdmin) {
        this.origenCiudadanoAdmin = origenCiudadanoAdmin;
    }


    /**
     * Gets the fechaCaptura value for this AnexoWs.
     * 
     * @return fechaCaptura
     */
    public java.util.Calendar getFechaCaptura() {
        return fechaCaptura;
    }


    /**
     * Sets the fechaCaptura value for this AnexoWs.
     * 
     * @param fechaCaptura
     */
    public void setFechaCaptura(java.util.Calendar fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }


    /**
     * Gets the modoFirma value for this AnexoWs.
     * 
     * @return modoFirma
     */
    public java.lang.Integer getModoFirma() {
        return modoFirma;
    }


    /**
     * Sets the modoFirma value for this AnexoWs.
     * 
     * @param modoFirma
     */
    public void setModoFirma(java.lang.Integer modoFirma) {
        this.modoFirma = modoFirma;
    }


    /**
     * Gets the nombreFirmaAnexada value for this AnexoWs.
     * 
     * @return nombreFirmaAnexada
     */
    public java.lang.String getNombreFirmaAnexada() {
        return nombreFirmaAnexada;
    }


    /**
     * Sets the nombreFirmaAnexada value for this AnexoWs.
     * 
     * @param nombreFirmaAnexada
     */
    public void setNombreFirmaAnexada(java.lang.String nombreFirmaAnexada) {
        this.nombreFirmaAnexada = nombreFirmaAnexada;
    }


    /**
     * Gets the firmaAnexada value for this AnexoWs.
     * 
     * @return firmaAnexada
     */
    public byte[] getFirmaAnexada() {
        return firmaAnexada;
    }


    /**
     * Sets the firmaAnexada value for this AnexoWs.
     * 
     * @param firmaAnexada
     */
    public void setFirmaAnexada(byte[] firmaAnexada) {
        this.firmaAnexada = firmaAnexada;
    }


    /**
     * Gets the tipoMIMEFirmaAnexada value for this AnexoWs.
     * 
     * @return tipoMIMEFirmaAnexada
     */
    public java.lang.String getTipoMIMEFirmaAnexada() {
        return tipoMIMEFirmaAnexada;
    }


    /**
     * Sets the tipoMIMEFirmaAnexada value for this AnexoWs.
     * 
     * @param tipoMIMEFirmaAnexada
     */
    public void setTipoMIMEFirmaAnexada(java.lang.String tipoMIMEFirmaAnexada) {
        this.tipoMIMEFirmaAnexada = tipoMIMEFirmaAnexada;
    }


    /**
     * Gets the csv value for this AnexoWs.
     * 
     * @return csv
     */
    public java.lang.String getCsv() {
        return csv;
    }


    /**
     * Sets the csv value for this AnexoWs.
     * 
     * @param csv
     */
    public void setCsv(java.lang.String csv) {
        this.csv = csv;
    }


    /**
     * Gets the justificante value for this AnexoWs.
     * 
     * @return justificante
     */
    public java.lang.Boolean getJustificante() {
        return justificante;
    }


    /**
     * Sets the justificante value for this AnexoWs.
     * 
     * @param justificante
     */
    public void setJustificante(java.lang.Boolean justificante) {
        this.justificante = justificante;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnexoWs)) return false;
        AnexoWs other = (AnexoWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.titulo==null && other.getTitulo()==null) || 
             (this.titulo!=null &&
              this.titulo.equals(other.getTitulo()))) &&
            ((this.nombreFicheroAnexado==null && other.getNombreFicheroAnexado()==null) || 
             (this.nombreFicheroAnexado!=null &&
              this.nombreFicheroAnexado.equals(other.getNombreFicheroAnexado()))) &&
            ((this.ficheroAnexado==null && other.getFicheroAnexado()==null) || 
             (this.ficheroAnexado!=null &&
              java.util.Arrays.equals(this.ficheroAnexado, other.getFicheroAnexado()))) &&
            ((this.tipoMIMEFicheroAnexado==null && other.getTipoMIMEFicheroAnexado()==null) || 
             (this.tipoMIMEFicheroAnexado!=null &&
              this.tipoMIMEFicheroAnexado.equals(other.getTipoMIMEFicheroAnexado()))) &&
            ((this.tipoDocumental==null && other.getTipoDocumental()==null) || 
             (this.tipoDocumental!=null &&
              this.tipoDocumental.equals(other.getTipoDocumental()))) &&
            ((this.validezDocumento==null && other.getValidezDocumento()==null) || 
             (this.validezDocumento!=null &&
              this.validezDocumento.equals(other.getValidezDocumento()))) &&
            ((this.tipoDocumento==null && other.getTipoDocumento()==null) || 
             (this.tipoDocumento!=null &&
              this.tipoDocumento.equals(other.getTipoDocumento()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.origenCiudadanoAdmin==null && other.getOrigenCiudadanoAdmin()==null) || 
             (this.origenCiudadanoAdmin!=null &&
              this.origenCiudadanoAdmin.equals(other.getOrigenCiudadanoAdmin()))) &&
            ((this.fechaCaptura==null && other.getFechaCaptura()==null) || 
             (this.fechaCaptura!=null &&
              this.fechaCaptura.equals(other.getFechaCaptura()))) &&
            ((this.modoFirma==null && other.getModoFirma()==null) || 
             (this.modoFirma!=null &&
              this.modoFirma.equals(other.getModoFirma()))) &&
            ((this.nombreFirmaAnexada==null && other.getNombreFirmaAnexada()==null) || 
             (this.nombreFirmaAnexada!=null &&
              this.nombreFirmaAnexada.equals(other.getNombreFirmaAnexada()))) &&
            ((this.firmaAnexada==null && other.getFirmaAnexada()==null) || 
             (this.firmaAnexada!=null &&
              java.util.Arrays.equals(this.firmaAnexada, other.getFirmaAnexada()))) &&
            ((this.tipoMIMEFirmaAnexada==null && other.getTipoMIMEFirmaAnexada()==null) || 
             (this.tipoMIMEFirmaAnexada!=null &&
              this.tipoMIMEFirmaAnexada.equals(other.getTipoMIMEFirmaAnexada()))) &&
            ((this.csv==null && other.getCsv()==null) || 
             (this.csv!=null &&
              this.csv.equals(other.getCsv()))) &&
            ((this.justificante==null && other.getJustificante()==null) || 
             (this.justificante!=null &&
              this.justificante.equals(other.getJustificante())));
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
        if (getTitulo() != null) {
            _hashCode += getTitulo().hashCode();
        }
        if (getNombreFicheroAnexado() != null) {
            _hashCode += getNombreFicheroAnexado().hashCode();
        }
        if (getFicheroAnexado() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFicheroAnexado());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFicheroAnexado(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipoMIMEFicheroAnexado() != null) {
            _hashCode += getTipoMIMEFicheroAnexado().hashCode();
        }
        if (getTipoDocumental() != null) {
            _hashCode += getTipoDocumental().hashCode();
        }
        if (getValidezDocumento() != null) {
            _hashCode += getValidezDocumento().hashCode();
        }
        if (getTipoDocumento() != null) {
            _hashCode += getTipoDocumento().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getOrigenCiudadanoAdmin() != null) {
            _hashCode += getOrigenCiudadanoAdmin().hashCode();
        }
        if (getFechaCaptura() != null) {
            _hashCode += getFechaCaptura().hashCode();
        }
        if (getModoFirma() != null) {
            _hashCode += getModoFirma().hashCode();
        }
        if (getNombreFirmaAnexada() != null) {
            _hashCode += getNombreFirmaAnexada().hashCode();
        }
        if (getFirmaAnexada() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFirmaAnexada());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFirmaAnexada(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTipoMIMEFirmaAnexada() != null) {
            _hashCode += getTipoMIMEFirmaAnexada().hashCode();
        }
        if (getCsv() != null) {
            _hashCode += getCsv().hashCode();
        }
        if (getJustificante() != null) {
            _hashCode += getJustificante().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnexoWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "anexoWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreFicheroAnexado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreFicheroAnexado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ficheroAnexado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ficheroAnexado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMIMEFicheroAnexado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMIMEFicheroAnexado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumental");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumental"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validezDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "validezDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origenCiudadanoAdmin");
        elemField.setXmlName(new javax.xml.namespace.QName("", "origenCiudadanoAdmin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaCaptura");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaCaptura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modoFirma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "modoFirma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreFirmaAnexada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreFirmaAnexada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firmaAnexada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firmaAnexada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoMIMEFirmaAnexada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMIMEFirmaAnexada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("csv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "csv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
