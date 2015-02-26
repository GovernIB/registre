/**
 * AnexoWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.v3.apiaxis;

public class AnexoWs  implements java.io.Serializable {
    private java.lang.String certificado;

    private java.util.Calendar fechaCaptura;

    private byte[] ficheroAnexado;

    private byte[] firmaAnexada;

    private java.lang.String firmacsv;

    private java.lang.Integer modoFirma;

    private java.lang.String nombreFicheroAnexado;

    private java.lang.String nombreFirmaAnexada;

    private java.lang.String observaciones;

    private java.lang.Integer origenCiudadanoAdmin;

    private java.lang.Long tamanoFicheroAnexado;

    private java.lang.Long tamanoFirmaAnexada;

    private java.lang.String timestamp;

    private java.lang.String tipoDocumental;

    private java.lang.String tipoDocumento;

    private java.lang.String tipoMIMEFicheroAnexado;

    private java.lang.String tipoMIMEFirmaAnexada;

    private java.lang.String titulo;

    private java.lang.String validacionOCSP;

    private java.lang.String validezDocumento;

    public AnexoWs() {
    }

    public AnexoWs(
           java.lang.String certificado,
           java.util.Calendar fechaCaptura,
           byte[] ficheroAnexado,
           byte[] firmaAnexada,
           java.lang.String firmacsv,
           java.lang.Integer modoFirma,
           java.lang.String nombreFicheroAnexado,
           java.lang.String nombreFirmaAnexada,
           java.lang.String observaciones,
           java.lang.Integer origenCiudadanoAdmin,
           java.lang.Long tamanoFicheroAnexado,
           java.lang.Long tamanoFirmaAnexada,
           java.lang.String timestamp,
           java.lang.String tipoDocumental,
           java.lang.String tipoDocumento,
           java.lang.String tipoMIMEFicheroAnexado,
           java.lang.String tipoMIMEFirmaAnexada,
           java.lang.String titulo,
           java.lang.String validacionOCSP,
           java.lang.String validezDocumento) {
           this.certificado = certificado;
           this.fechaCaptura = fechaCaptura;
           this.ficheroAnexado = ficheroAnexado;
           this.firmaAnexada = firmaAnexada;
           this.firmacsv = firmacsv;
           this.modoFirma = modoFirma;
           this.nombreFicheroAnexado = nombreFicheroAnexado;
           this.nombreFirmaAnexada = nombreFirmaAnexada;
           this.observaciones = observaciones;
           this.origenCiudadanoAdmin = origenCiudadanoAdmin;
           this.tamanoFicheroAnexado = tamanoFicheroAnexado;
           this.tamanoFirmaAnexada = tamanoFirmaAnexada;
           this.timestamp = timestamp;
           this.tipoDocumental = tipoDocumental;
           this.tipoDocumento = tipoDocumento;
           this.tipoMIMEFicheroAnexado = tipoMIMEFicheroAnexado;
           this.tipoMIMEFirmaAnexada = tipoMIMEFirmaAnexada;
           this.titulo = titulo;
           this.validacionOCSP = validacionOCSP;
           this.validezDocumento = validezDocumento;
    }


    /**
     * Gets the certificado value for this AnexoWs.
     * 
     * @return certificado
     */
    public java.lang.String getCertificado() {
        return certificado;
    }


    /**
     * Sets the certificado value for this AnexoWs.
     * 
     * @param certificado
     */
    public void setCertificado(java.lang.String certificado) {
        this.certificado = certificado;
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
     * Gets the firmacsv value for this AnexoWs.
     * 
     * @return firmacsv
     */
    public java.lang.String getFirmacsv() {
        return firmacsv;
    }


    /**
     * Sets the firmacsv value for this AnexoWs.
     * 
     * @param firmacsv
     */
    public void setFirmacsv(java.lang.String firmacsv) {
        this.firmacsv = firmacsv;
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
     * Gets the tamanoFicheroAnexado value for this AnexoWs.
     * 
     * @return tamanoFicheroAnexado
     */
    public java.lang.Long getTamanoFicheroAnexado() {
        return tamanoFicheroAnexado;
    }


    /**
     * Sets the tamanoFicheroAnexado value for this AnexoWs.
     * 
     * @param tamanoFicheroAnexado
     */
    public void setTamanoFicheroAnexado(java.lang.Long tamanoFicheroAnexado) {
        this.tamanoFicheroAnexado = tamanoFicheroAnexado;
    }


    /**
     * Gets the tamanoFirmaAnexada value for this AnexoWs.
     * 
     * @return tamanoFirmaAnexada
     */
    public java.lang.Long getTamanoFirmaAnexada() {
        return tamanoFirmaAnexada;
    }


    /**
     * Sets the tamanoFirmaAnexada value for this AnexoWs.
     * 
     * @param tamanoFirmaAnexada
     */
    public void setTamanoFirmaAnexada(java.lang.Long tamanoFirmaAnexada) {
        this.tamanoFirmaAnexada = tamanoFirmaAnexada;
    }


    /**
     * Gets the timestamp value for this AnexoWs.
     * 
     * @return timestamp
     */
    public java.lang.String getTimestamp() {
        return timestamp;
    }


    /**
     * Sets the timestamp value for this AnexoWs.
     * 
     * @param timestamp
     */
    public void setTimestamp(java.lang.String timestamp) {
        this.timestamp = timestamp;
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
     * Gets the validacionOCSP value for this AnexoWs.
     * 
     * @return validacionOCSP
     */
    public java.lang.String getValidacionOCSP() {
        return validacionOCSP;
    }


    /**
     * Sets the validacionOCSP value for this AnexoWs.
     * 
     * @param validacionOCSP
     */
    public void setValidacionOCSP(java.lang.String validacionOCSP) {
        this.validacionOCSP = validacionOCSP;
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
            ((this.certificado==null && other.getCertificado()==null) || 
             (this.certificado!=null &&
              this.certificado.equals(other.getCertificado()))) &&
            ((this.fechaCaptura==null && other.getFechaCaptura()==null) || 
             (this.fechaCaptura!=null &&
              this.fechaCaptura.equals(other.getFechaCaptura()))) &&
            ((this.ficheroAnexado==null && other.getFicheroAnexado()==null) || 
             (this.ficheroAnexado!=null &&
              java.util.Arrays.equals(this.ficheroAnexado, other.getFicheroAnexado()))) &&
            ((this.firmaAnexada==null && other.getFirmaAnexada()==null) || 
             (this.firmaAnexada!=null &&
              java.util.Arrays.equals(this.firmaAnexada, other.getFirmaAnexada()))) &&
            ((this.firmacsv==null && other.getFirmacsv()==null) || 
             (this.firmacsv!=null &&
              this.firmacsv.equals(other.getFirmacsv()))) &&
            ((this.modoFirma==null && other.getModoFirma()==null) || 
             (this.modoFirma!=null &&
              this.modoFirma.equals(other.getModoFirma()))) &&
            ((this.nombreFicheroAnexado==null && other.getNombreFicheroAnexado()==null) || 
             (this.nombreFicheroAnexado!=null &&
              this.nombreFicheroAnexado.equals(other.getNombreFicheroAnexado()))) &&
            ((this.nombreFirmaAnexada==null && other.getNombreFirmaAnexada()==null) || 
             (this.nombreFirmaAnexada!=null &&
              this.nombreFirmaAnexada.equals(other.getNombreFirmaAnexada()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.origenCiudadanoAdmin==null && other.getOrigenCiudadanoAdmin()==null) || 
             (this.origenCiudadanoAdmin!=null &&
              this.origenCiudadanoAdmin.equals(other.getOrigenCiudadanoAdmin()))) &&
            ((this.tamanoFicheroAnexado==null && other.getTamanoFicheroAnexado()==null) || 
             (this.tamanoFicheroAnexado!=null &&
              this.tamanoFicheroAnexado.equals(other.getTamanoFicheroAnexado()))) &&
            ((this.tamanoFirmaAnexada==null && other.getTamanoFirmaAnexada()==null) || 
             (this.tamanoFirmaAnexada!=null &&
              this.tamanoFirmaAnexada.equals(other.getTamanoFirmaAnexada()))) &&
            ((this.timestamp==null && other.getTimestamp()==null) || 
             (this.timestamp!=null &&
              this.timestamp.equals(other.getTimestamp()))) &&
            ((this.tipoDocumental==null && other.getTipoDocumental()==null) || 
             (this.tipoDocumental!=null &&
              this.tipoDocumental.equals(other.getTipoDocumental()))) &&
            ((this.tipoDocumento==null && other.getTipoDocumento()==null) || 
             (this.tipoDocumento!=null &&
              this.tipoDocumento.equals(other.getTipoDocumento()))) &&
            ((this.tipoMIMEFicheroAnexado==null && other.getTipoMIMEFicheroAnexado()==null) || 
             (this.tipoMIMEFicheroAnexado!=null &&
              this.tipoMIMEFicheroAnexado.equals(other.getTipoMIMEFicheroAnexado()))) &&
            ((this.tipoMIMEFirmaAnexada==null && other.getTipoMIMEFirmaAnexada()==null) || 
             (this.tipoMIMEFirmaAnexada!=null &&
              this.tipoMIMEFirmaAnexada.equals(other.getTipoMIMEFirmaAnexada()))) &&
            ((this.titulo==null && other.getTitulo()==null) || 
             (this.titulo!=null &&
              this.titulo.equals(other.getTitulo()))) &&
            ((this.validacionOCSP==null && other.getValidacionOCSP()==null) || 
             (this.validacionOCSP!=null &&
              this.validacionOCSP.equals(other.getValidacionOCSP()))) &&
            ((this.validezDocumento==null && other.getValidezDocumento()==null) || 
             (this.validezDocumento!=null &&
              this.validezDocumento.equals(other.getValidezDocumento())));
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
        if (getCertificado() != null) {
            _hashCode += getCertificado().hashCode();
        }
        if (getFechaCaptura() != null) {
            _hashCode += getFechaCaptura().hashCode();
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
        if (getFirmacsv() != null) {
            _hashCode += getFirmacsv().hashCode();
        }
        if (getModoFirma() != null) {
            _hashCode += getModoFirma().hashCode();
        }
        if (getNombreFicheroAnexado() != null) {
            _hashCode += getNombreFicheroAnexado().hashCode();
        }
        if (getNombreFirmaAnexada() != null) {
            _hashCode += getNombreFirmaAnexada().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getOrigenCiudadanoAdmin() != null) {
            _hashCode += getOrigenCiudadanoAdmin().hashCode();
        }
        if (getTamanoFicheroAnexado() != null) {
            _hashCode += getTamanoFicheroAnexado().hashCode();
        }
        if (getTamanoFirmaAnexada() != null) {
            _hashCode += getTamanoFirmaAnexada().hashCode();
        }
        if (getTimestamp() != null) {
            _hashCode += getTimestamp().hashCode();
        }
        if (getTipoDocumental() != null) {
            _hashCode += getTipoDocumental().hashCode();
        }
        if (getTipoDocumento() != null) {
            _hashCode += getTipoDocumento().hashCode();
        }
        if (getTipoMIMEFicheroAnexado() != null) {
            _hashCode += getTipoMIMEFicheroAnexado().hashCode();
        }
        if (getTipoMIMEFirmaAnexada() != null) {
            _hashCode += getTipoMIMEFirmaAnexada().hashCode();
        }
        if (getTitulo() != null) {
            _hashCode += getTitulo().hashCode();
        }
        if (getValidacionOCSP() != null) {
            _hashCode += getValidacionOCSP().hashCode();
        }
        if (getValidezDocumento() != null) {
            _hashCode += getValidezDocumento().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnexoWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://registrosalida.impl.v3.ws.regweb.caib.es/", "anexoWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("certificado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "certificado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("ficheroAnexado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ficheroAnexado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
        elemField.setFieldName("firmacsv");
        elemField.setXmlName(new javax.xml.namespace.QName("", "firmacsv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("nombreFicheroAnexado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreFicheroAnexado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("tamanoFicheroAnexado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tamanoFicheroAnexado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tamanoFirmaAnexada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tamanoFirmaAnexada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timestamp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "timestamp"));
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
        elemField.setFieldName("tipoDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("tipoMIMEFirmaAnexada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoMIMEFirmaAnexada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("titulo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "titulo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validacionOCSP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "validacionOCSP"));
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
