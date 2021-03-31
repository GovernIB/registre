/**
 * AsientoWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class AsientoWs  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.FileInfoWs[] anexos;

    private java.lang.String codigoDestino;

    private java.lang.String codigoOficinaOrigen;

    private java.lang.Long codigoSia;

    private java.lang.String codigoUnidadOrigen;

    private java.lang.String denominacionDestino;

    private java.lang.String denominacionOficinaOrigen;

    private java.lang.String denominacionUnidadOrigen;

    private java.lang.String descripcionEstado;

    private java.lang.Long estado;

    private java.lang.String expone;

    private java.lang.String extracto;

    private java.util.Calendar fechaRegistro;

    private java.lang.Long idioma;

    private es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados;

    private es.caib.regweb3.ws.v3.apiaxis.FileInfoWs justificante;

    private java.lang.String numeroRegistro;

    private java.lang.Boolean presencial;

    private java.lang.String solicita;

    private java.lang.String tipoDocumetacionFisica;

    private java.lang.Long tipoRegistro;

    public AsientoWs() {
    }

    public AsientoWs(
           es.caib.regweb3.ws.v3.apiaxis.FileInfoWs[] anexos,
           java.lang.String codigoDestino,
           java.lang.String codigoOficinaOrigen,
           java.lang.Long codigoSia,
           java.lang.String codigoUnidadOrigen,
           java.lang.String denominacionDestino,
           java.lang.String denominacionOficinaOrigen,
           java.lang.String denominacionUnidadOrigen,
           java.lang.String descripcionEstado,
           java.lang.Long estado,
           java.lang.String expone,
           java.lang.String extracto,
           java.util.Calendar fechaRegistro,
           java.lang.Long idioma,
           es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados,
           es.caib.regweb3.ws.v3.apiaxis.FileInfoWs justificante,
           java.lang.String numeroRegistro,
           java.lang.Boolean presencial,
           java.lang.String solicita,
           java.lang.String tipoDocumetacionFisica,
           java.lang.Long tipoRegistro) {
           this.anexos = anexos;
           this.codigoDestino = codigoDestino;
           this.codigoOficinaOrigen = codigoOficinaOrigen;
           this.codigoSia = codigoSia;
           this.codigoUnidadOrigen = codigoUnidadOrigen;
           this.denominacionDestino = denominacionDestino;
           this.denominacionOficinaOrigen = denominacionOficinaOrigen;
           this.denominacionUnidadOrigen = denominacionUnidadOrigen;
           this.descripcionEstado = descripcionEstado;
           this.estado = estado;
           this.expone = expone;
           this.extracto = extracto;
           this.fechaRegistro = fechaRegistro;
           this.idioma = idioma;
           this.interesados = interesados;
           this.justificante = justificante;
           this.numeroRegistro = numeroRegistro;
           this.presencial = presencial;
           this.solicita = solicita;
           this.tipoDocumetacionFisica = tipoDocumetacionFisica;
           this.tipoRegistro = tipoRegistro;
    }


    /**
     * Gets the anexos value for this AsientoWs.
     * 
     * @return anexos
     */
    public es.caib.regweb3.ws.v3.apiaxis.FileInfoWs[] getAnexos() {
        return anexos;
    }


    /**
     * Sets the anexos value for this AsientoWs.
     * 
     * @param anexos
     */
    public void setAnexos(es.caib.regweb3.ws.v3.apiaxis.FileInfoWs[] anexos) {
        this.anexos = anexos;
    }

    public es.caib.regweb3.ws.v3.apiaxis.FileInfoWs getAnexos(int i) {
        return this.anexos[i];
    }

    public void setAnexos(int i, es.caib.regweb3.ws.v3.apiaxis.FileInfoWs _value) {
        this.anexos[i] = _value;
    }


    /**
     * Gets the codigoDestino value for this AsientoWs.
     * 
     * @return codigoDestino
     */
    public java.lang.String getCodigoDestino() {
        return codigoDestino;
    }


    /**
     * Sets the codigoDestino value for this AsientoWs.
     * 
     * @param codigoDestino
     */
    public void setCodigoDestino(java.lang.String codigoDestino) {
        this.codigoDestino = codigoDestino;
    }


    /**
     * Gets the codigoOficinaOrigen value for this AsientoWs.
     * 
     * @return codigoOficinaOrigen
     */
    public java.lang.String getCodigoOficinaOrigen() {
        return codigoOficinaOrigen;
    }


    /**
     * Sets the codigoOficinaOrigen value for this AsientoWs.
     * 
     * @param codigoOficinaOrigen
     */
    public void setCodigoOficinaOrigen(java.lang.String codigoOficinaOrigen) {
        this.codigoOficinaOrigen = codigoOficinaOrigen;
    }


    /**
     * Gets the codigoSia value for this AsientoWs.
     * 
     * @return codigoSia
     */
    public java.lang.Long getCodigoSia() {
        return codigoSia;
    }


    /**
     * Sets the codigoSia value for this AsientoWs.
     * 
     * @param codigoSia
     */
    public void setCodigoSia(java.lang.Long codigoSia) {
        this.codigoSia = codigoSia;
    }


    /**
     * Gets the codigoUnidadOrigen value for this AsientoWs.
     * 
     * @return codigoUnidadOrigen
     */
    public java.lang.String getCodigoUnidadOrigen() {
        return codigoUnidadOrigen;
    }


    /**
     * Sets the codigoUnidadOrigen value for this AsientoWs.
     * 
     * @param codigoUnidadOrigen
     */
    public void setCodigoUnidadOrigen(java.lang.String codigoUnidadOrigen) {
        this.codigoUnidadOrigen = codigoUnidadOrigen;
    }


    /**
     * Gets the denominacionDestino value for this AsientoWs.
     * 
     * @return denominacionDestino
     */
    public java.lang.String getDenominacionDestino() {
        return denominacionDestino;
    }


    /**
     * Sets the denominacionDestino value for this AsientoWs.
     * 
     * @param denominacionDestino
     */
    public void setDenominacionDestino(java.lang.String denominacionDestino) {
        this.denominacionDestino = denominacionDestino;
    }


    /**
     * Gets the denominacionOficinaOrigen value for this AsientoWs.
     * 
     * @return denominacionOficinaOrigen
     */
    public java.lang.String getDenominacionOficinaOrigen() {
        return denominacionOficinaOrigen;
    }


    /**
     * Sets the denominacionOficinaOrigen value for this AsientoWs.
     * 
     * @param denominacionOficinaOrigen
     */
    public void setDenominacionOficinaOrigen(java.lang.String denominacionOficinaOrigen) {
        this.denominacionOficinaOrigen = denominacionOficinaOrigen;
    }


    /**
     * Gets the denominacionUnidadOrigen value for this AsientoWs.
     * 
     * @return denominacionUnidadOrigen
     */
    public java.lang.String getDenominacionUnidadOrigen() {
        return denominacionUnidadOrigen;
    }


    /**
     * Sets the denominacionUnidadOrigen value for this AsientoWs.
     * 
     * @param denominacionUnidadOrigen
     */
    public void setDenominacionUnidadOrigen(java.lang.String denominacionUnidadOrigen) {
        this.denominacionUnidadOrigen = denominacionUnidadOrigen;
    }


    /**
     * Gets the descripcionEstado value for this AsientoWs.
     * 
     * @return descripcionEstado
     */
    public java.lang.String getDescripcionEstado() {
        return descripcionEstado;
    }


    /**
     * Sets the descripcionEstado value for this AsientoWs.
     * 
     * @param descripcionEstado
     */
    public void setDescripcionEstado(java.lang.String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }


    /**
     * Gets the estado value for this AsientoWs.
     * 
     * @return estado
     */
    public java.lang.Long getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this AsientoWs.
     * 
     * @param estado
     */
    public void setEstado(java.lang.Long estado) {
        this.estado = estado;
    }


    /**
     * Gets the expone value for this AsientoWs.
     * 
     * @return expone
     */
    public java.lang.String getExpone() {
        return expone;
    }


    /**
     * Sets the expone value for this AsientoWs.
     * 
     * @param expone
     */
    public void setExpone(java.lang.String expone) {
        this.expone = expone;
    }


    /**
     * Gets the extracto value for this AsientoWs.
     * 
     * @return extracto
     */
    public java.lang.String getExtracto() {
        return extracto;
    }


    /**
     * Sets the extracto value for this AsientoWs.
     * 
     * @param extracto
     */
    public void setExtracto(java.lang.String extracto) {
        this.extracto = extracto;
    }


    /**
     * Gets the fechaRegistro value for this AsientoWs.
     * 
     * @return fechaRegistro
     */
    public java.util.Calendar getFechaRegistro() {
        return fechaRegistro;
    }


    /**
     * Sets the fechaRegistro value for this AsientoWs.
     * 
     * @param fechaRegistro
     */
    public void setFechaRegistro(java.util.Calendar fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    /**
     * Gets the idioma value for this AsientoWs.
     * 
     * @return idioma
     */
    public java.lang.Long getIdioma() {
        return idioma;
    }


    /**
     * Sets the idioma value for this AsientoWs.
     * 
     * @param idioma
     */
    public void setIdioma(java.lang.Long idioma) {
        this.idioma = idioma;
    }


    /**
     * Gets the interesados value for this AsientoWs.
     * 
     * @return interesados
     */
    public es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] getInteresados() {
        return interesados;
    }


    /**
     * Sets the interesados value for this AsientoWs.
     * 
     * @param interesados
     */
    public void setInteresados(es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados) {
        this.interesados = interesados;
    }

    public es.caib.regweb3.ws.v3.apiaxis.InteresadoWs getInteresados(int i) {
        return this.interesados[i];
    }

    public void setInteresados(int i, es.caib.regweb3.ws.v3.apiaxis.InteresadoWs _value) {
        this.interesados[i] = _value;
    }


    /**
     * Gets the justificante value for this AsientoWs.
     * 
     * @return justificante
     */
    public es.caib.regweb3.ws.v3.apiaxis.FileInfoWs getJustificante() {
        return justificante;
    }


    /**
     * Sets the justificante value for this AsientoWs.
     * 
     * @param justificante
     */
    public void setJustificante(es.caib.regweb3.ws.v3.apiaxis.FileInfoWs justificante) {
        this.justificante = justificante;
    }


    /**
     * Gets the numeroRegistro value for this AsientoWs.
     * 
     * @return numeroRegistro
     */
    public java.lang.String getNumeroRegistro() {
        return numeroRegistro;
    }


    /**
     * Sets the numeroRegistro value for this AsientoWs.
     * 
     * @param numeroRegistro
     */
    public void setNumeroRegistro(java.lang.String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }


    /**
     * Gets the presencial value for this AsientoWs.
     * 
     * @return presencial
     */
    public java.lang.Boolean getPresencial() {
        return presencial;
    }


    /**
     * Sets the presencial value for this AsientoWs.
     * 
     * @param presencial
     */
    public void setPresencial(java.lang.Boolean presencial) {
        this.presencial = presencial;
    }


    /**
     * Gets the solicita value for this AsientoWs.
     * 
     * @return solicita
     */
    public java.lang.String getSolicita() {
        return solicita;
    }


    /**
     * Sets the solicita value for this AsientoWs.
     * 
     * @param solicita
     */
    public void setSolicita(java.lang.String solicita) {
        this.solicita = solicita;
    }


    /**
     * Gets the tipoDocumetacionFisica value for this AsientoWs.
     * 
     * @return tipoDocumetacionFisica
     */
    public java.lang.String getTipoDocumetacionFisica() {
        return tipoDocumetacionFisica;
    }


    /**
     * Sets the tipoDocumetacionFisica value for this AsientoWs.
     * 
     * @param tipoDocumetacionFisica
     */
    public void setTipoDocumetacionFisica(java.lang.String tipoDocumetacionFisica) {
        this.tipoDocumetacionFisica = tipoDocumetacionFisica;
    }


    /**
     * Gets the tipoRegistro value for this AsientoWs.
     * 
     * @return tipoRegistro
     */
    public java.lang.Long getTipoRegistro() {
        return tipoRegistro;
    }


    /**
     * Sets the tipoRegistro value for this AsientoWs.
     * 
     * @param tipoRegistro
     */
    public void setTipoRegistro(java.lang.Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AsientoWs)) return false;
        AsientoWs other = (AsientoWs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.anexos==null && other.getAnexos()==null) || 
             (this.anexos!=null &&
              java.util.Arrays.equals(this.anexos, other.getAnexos()))) &&
            ((this.codigoDestino==null && other.getCodigoDestino()==null) || 
             (this.codigoDestino!=null &&
              this.codigoDestino.equals(other.getCodigoDestino()))) &&
            ((this.codigoOficinaOrigen==null && other.getCodigoOficinaOrigen()==null) || 
             (this.codigoOficinaOrigen!=null &&
              this.codigoOficinaOrigen.equals(other.getCodigoOficinaOrigen()))) &&
            ((this.codigoSia==null && other.getCodigoSia()==null) || 
             (this.codigoSia!=null &&
              this.codigoSia.equals(other.getCodigoSia()))) &&
            ((this.codigoUnidadOrigen==null && other.getCodigoUnidadOrigen()==null) || 
             (this.codigoUnidadOrigen!=null &&
              this.codigoUnidadOrigen.equals(other.getCodigoUnidadOrigen()))) &&
            ((this.denominacionDestino==null && other.getDenominacionDestino()==null) || 
             (this.denominacionDestino!=null &&
              this.denominacionDestino.equals(other.getDenominacionDestino()))) &&
            ((this.denominacionOficinaOrigen==null && other.getDenominacionOficinaOrigen()==null) || 
             (this.denominacionOficinaOrigen!=null &&
              this.denominacionOficinaOrigen.equals(other.getDenominacionOficinaOrigen()))) &&
            ((this.denominacionUnidadOrigen==null && other.getDenominacionUnidadOrigen()==null) || 
             (this.denominacionUnidadOrigen!=null &&
              this.denominacionUnidadOrigen.equals(other.getDenominacionUnidadOrigen()))) &&
            ((this.descripcionEstado==null && other.getDescripcionEstado()==null) || 
             (this.descripcionEstado!=null &&
              this.descripcionEstado.equals(other.getDescripcionEstado()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.expone==null && other.getExpone()==null) || 
             (this.expone!=null &&
              this.expone.equals(other.getExpone()))) &&
            ((this.extracto==null && other.getExtracto()==null) || 
             (this.extracto!=null &&
              this.extracto.equals(other.getExtracto()))) &&
            ((this.fechaRegistro==null && other.getFechaRegistro()==null) || 
             (this.fechaRegistro!=null &&
              this.fechaRegistro.equals(other.getFechaRegistro()))) &&
            ((this.idioma==null && other.getIdioma()==null) || 
             (this.idioma!=null &&
              this.idioma.equals(other.getIdioma()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.justificante==null && other.getJustificante()==null) || 
             (this.justificante!=null &&
              this.justificante.equals(other.getJustificante()))) &&
            ((this.numeroRegistro==null && other.getNumeroRegistro()==null) || 
             (this.numeroRegistro!=null &&
              this.numeroRegistro.equals(other.getNumeroRegistro()))) &&
            ((this.presencial==null && other.getPresencial()==null) || 
             (this.presencial!=null &&
              this.presencial.equals(other.getPresencial()))) &&
            ((this.solicita==null && other.getSolicita()==null) || 
             (this.solicita!=null &&
              this.solicita.equals(other.getSolicita()))) &&
            ((this.tipoDocumetacionFisica==null && other.getTipoDocumetacionFisica()==null) || 
             (this.tipoDocumetacionFisica!=null &&
              this.tipoDocumetacionFisica.equals(other.getTipoDocumetacionFisica()))) &&
            ((this.tipoRegistro==null && other.getTipoRegistro()==null) || 
             (this.tipoRegistro!=null &&
              this.tipoRegistro.equals(other.getTipoRegistro())));
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
        if (getAnexos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAnexos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAnexos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCodigoDestino() != null) {
            _hashCode += getCodigoDestino().hashCode();
        }
        if (getCodigoOficinaOrigen() != null) {
            _hashCode += getCodigoOficinaOrigen().hashCode();
        }
        if (getCodigoSia() != null) {
            _hashCode += getCodigoSia().hashCode();
        }
        if (getCodigoUnidadOrigen() != null) {
            _hashCode += getCodigoUnidadOrigen().hashCode();
        }
        if (getDenominacionDestino() != null) {
            _hashCode += getDenominacionDestino().hashCode();
        }
        if (getDenominacionOficinaOrigen() != null) {
            _hashCode += getDenominacionOficinaOrigen().hashCode();
        }
        if (getDenominacionUnidadOrigen() != null) {
            _hashCode += getDenominacionUnidadOrigen().hashCode();
        }
        if (getDescripcionEstado() != null) {
            _hashCode += getDescripcionEstado().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getExpone() != null) {
            _hashCode += getExpone().hashCode();
        }
        if (getExtracto() != null) {
            _hashCode += getExtracto().hashCode();
        }
        if (getFechaRegistro() != null) {
            _hashCode += getFechaRegistro().hashCode();
        }
        if (getIdioma() != null) {
            _hashCode += getIdioma().hashCode();
        }
        if (getInteresados() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInteresados());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInteresados(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJustificante() != null) {
            _hashCode += getJustificante().hashCode();
        }
        if (getNumeroRegistro() != null) {
            _hashCode += getNumeroRegistro().hashCode();
        }
        if (getPresencial() != null) {
            _hashCode += getPresencial().hashCode();
        }
        if (getSolicita() != null) {
            _hashCode += getSolicita().hashCode();
        }
        if (getTipoDocumetacionFisica() != null) {
            _hashCode += getTipoDocumetacionFisica().hashCode();
        }
        if (getTipoRegistro() != null) {
            _hashCode += getTipoRegistro().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AsientoWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "asientoWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anexos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anexos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "fileInfoWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoOficinaOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoOficinaOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoSia");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoSia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoUnidadOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoUnidadOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominacionDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominacionDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominacionOficinaOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominacionOficinaOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("denominacionUnidadOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "denominacionUnidadOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionEstado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionEstado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "estado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expone");
        elemField.setXmlName(new javax.xml.namespace.QName("", "expone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extracto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "extracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idioma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idioma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interesados");
        elemField.setXmlName(new javax.xml.namespace.QName("", "interesados"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "interesadoWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("justificante");
        elemField.setXmlName(new javax.xml.namespace.QName("", "justificante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "fileInfoWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("presencial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "presencial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solicita");
        elemField.setXmlName(new javax.xml.namespace.QName("", "solicita"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoDocumetacionFisica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumetacionFisica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
