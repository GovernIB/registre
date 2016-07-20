/**
 * RegistroWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegistroWs  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos;

    private java.lang.String aplicacion;

    private java.lang.String codigoAsunto;

    private java.lang.String codigoUsuario;

    private java.lang.String contactoUsuario;

    private java.lang.Long docFisica;

    private java.lang.String expone;

    private java.lang.String extracto;

    private java.util.Calendar fecha;

    private java.lang.String idioma;

    private es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados;

    private java.lang.String libro;

    private java.lang.String numExpediente;

    private java.lang.String numTransporte;

    private java.lang.Integer numero;

    private java.lang.String numeroRegistroFormateado;

    private java.lang.String observaciones;

    private java.lang.String oficina;

    private java.lang.String refExterna;

    private java.lang.String solicita;

    private java.lang.String tipoAsunto;

    private java.lang.String tipoTransporte;

    private java.lang.String version;

    public RegistroWs() {
    }

    public RegistroWs(
           es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos,
           java.lang.String aplicacion,
           java.lang.String codigoAsunto,
           java.lang.String codigoUsuario,
           java.lang.String contactoUsuario,
           java.lang.Long docFisica,
           java.lang.String expone,
           java.lang.String extracto,
           java.util.Calendar fecha,
           java.lang.String idioma,
           es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados,
           java.lang.String libro,
           java.lang.String numExpediente,
           java.lang.String numTransporte,
           java.lang.Integer numero,
           java.lang.String numeroRegistroFormateado,
           java.lang.String observaciones,
           java.lang.String oficina,
           java.lang.String refExterna,
           java.lang.String solicita,
           java.lang.String tipoAsunto,
           java.lang.String tipoTransporte,
           java.lang.String version) {
           this.anexos = anexos;
           this.aplicacion = aplicacion;
           this.codigoAsunto = codigoAsunto;
           this.codigoUsuario = codigoUsuario;
           this.contactoUsuario = contactoUsuario;
           this.docFisica = docFisica;
           this.expone = expone;
           this.extracto = extracto;
           this.fecha = fecha;
           this.idioma = idioma;
           this.interesados = interesados;
           this.libro = libro;
           this.numExpediente = numExpediente;
           this.numTransporte = numTransporte;
           this.numero = numero;
           this.numeroRegistroFormateado = numeroRegistroFormateado;
           this.observaciones = observaciones;
           this.oficina = oficina;
           this.refExterna = refExterna;
           this.solicita = solicita;
           this.tipoAsunto = tipoAsunto;
           this.tipoTransporte = tipoTransporte;
           this.version = version;
    }


    /**
     * Gets the anexos value for this RegistroWs.
     * 
     * @return anexos
     */
    public es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] getAnexos() {
        return anexos;
    }


    /**
     * Sets the anexos value for this RegistroWs.
     * 
     * @param anexos
     */
    public void setAnexos(es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos) {
        this.anexos = anexos;
    }

    public es.caib.regweb3.ws.v3.apiaxis.AnexoWs getAnexos(int i) {
        return this.anexos[i];
    }

    public void setAnexos(int i, es.caib.regweb3.ws.v3.apiaxis.AnexoWs _value) {
        this.anexos[i] = _value;
    }


    /**
     * Gets the aplicacion value for this RegistroWs.
     * 
     * @return aplicacion
     */
    public java.lang.String getAplicacion() {
        return aplicacion;
    }


    /**
     * Sets the aplicacion value for this RegistroWs.
     * 
     * @param aplicacion
     */
    public void setAplicacion(java.lang.String aplicacion) {
        this.aplicacion = aplicacion;
    }


    /**
     * Gets the codigoAsunto value for this RegistroWs.
     * 
     * @return codigoAsunto
     */
    public java.lang.String getCodigoAsunto() {
        return codigoAsunto;
    }


    /**
     * Sets the codigoAsunto value for this RegistroWs.
     * 
     * @param codigoAsunto
     */
    public void setCodigoAsunto(java.lang.String codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
    }


    /**
     * Gets the codigoUsuario value for this RegistroWs.
     * 
     * @return codigoUsuario
     */
    public java.lang.String getCodigoUsuario() {
        return codigoUsuario;
    }


    /**
     * Sets the codigoUsuario value for this RegistroWs.
     * 
     * @param codigoUsuario
     */
    public void setCodigoUsuario(java.lang.String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }


    /**
     * Gets the contactoUsuario value for this RegistroWs.
     * 
     * @return contactoUsuario
     */
    public java.lang.String getContactoUsuario() {
        return contactoUsuario;
    }


    /**
     * Sets the contactoUsuario value for this RegistroWs.
     * 
     * @param contactoUsuario
     */
    public void setContactoUsuario(java.lang.String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
    }


    /**
     * Gets the docFisica value for this RegistroWs.
     * 
     * @return docFisica
     */
    public java.lang.Long getDocFisica() {
        return docFisica;
    }


    /**
     * Sets the docFisica value for this RegistroWs.
     * 
     * @param docFisica
     */
    public void setDocFisica(java.lang.Long docFisica) {
        this.docFisica = docFisica;
    }


    /**
     * Gets the expone value for this RegistroWs.
     * 
     * @return expone
     */
    public java.lang.String getExpone() {
        return expone;
    }


    /**
     * Sets the expone value for this RegistroWs.
     * 
     * @param expone
     */
    public void setExpone(java.lang.String expone) {
        this.expone = expone;
    }


    /**
     * Gets the extracto value for this RegistroWs.
     * 
     * @return extracto
     */
    public java.lang.String getExtracto() {
        return extracto;
    }


    /**
     * Sets the extracto value for this RegistroWs.
     * 
     * @param extracto
     */
    public void setExtracto(java.lang.String extracto) {
        this.extracto = extracto;
    }


    /**
     * Gets the fecha value for this RegistroWs.
     * 
     * @return fecha
     */
    public java.util.Calendar getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this RegistroWs.
     * 
     * @param fecha
     */
    public void setFecha(java.util.Calendar fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the idioma value for this RegistroWs.
     * 
     * @return idioma
     */
    public java.lang.String getIdioma() {
        return idioma;
    }


    /**
     * Sets the idioma value for this RegistroWs.
     * 
     * @param idioma
     */
    public void setIdioma(java.lang.String idioma) {
        this.idioma = idioma;
    }


    /**
     * Gets the interesados value for this RegistroWs.
     * 
     * @return interesados
     */
    public es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] getInteresados() {
        return interesados;
    }


    /**
     * Sets the interesados value for this RegistroWs.
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
     * Gets the libro value for this RegistroWs.
     * 
     * @return libro
     */
    public java.lang.String getLibro() {
        return libro;
    }


    /**
     * Sets the libro value for this RegistroWs.
     * 
     * @param libro
     */
    public void setLibro(java.lang.String libro) {
        this.libro = libro;
    }


    /**
     * Gets the numExpediente value for this RegistroWs.
     * 
     * @return numExpediente
     */
    public java.lang.String getNumExpediente() {
        return numExpediente;
    }


    /**
     * Sets the numExpediente value for this RegistroWs.
     * 
     * @param numExpediente
     */
    public void setNumExpediente(java.lang.String numExpediente) {
        this.numExpediente = numExpediente;
    }


    /**
     * Gets the numTransporte value for this RegistroWs.
     * 
     * @return numTransporte
     */
    public java.lang.String getNumTransporte() {
        return numTransporte;
    }


    /**
     * Sets the numTransporte value for this RegistroWs.
     * 
     * @param numTransporte
     */
    public void setNumTransporte(java.lang.String numTransporte) {
        this.numTransporte = numTransporte;
    }


    /**
     * Gets the numero value for this RegistroWs.
     * 
     * @return numero
     */
    public java.lang.Integer getNumero() {
        return numero;
    }


    /**
     * Sets the numero value for this RegistroWs.
     * 
     * @param numero
     */
    public void setNumero(java.lang.Integer numero) {
        this.numero = numero;
    }


    /**
     * Gets the numeroRegistroFormateado value for this RegistroWs.
     * 
     * @return numeroRegistroFormateado
     */
    public java.lang.String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }


    /**
     * Sets the numeroRegistroFormateado value for this RegistroWs.
     * 
     * @param numeroRegistroFormateado
     */
    public void setNumeroRegistroFormateado(java.lang.String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }


    /**
     * Gets the observaciones value for this RegistroWs.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this RegistroWs.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the oficina value for this RegistroWs.
     * 
     * @return oficina
     */
    public java.lang.String getOficina() {
        return oficina;
    }


    /**
     * Sets the oficina value for this RegistroWs.
     * 
     * @param oficina
     */
    public void setOficina(java.lang.String oficina) {
        this.oficina = oficina;
    }


    /**
     * Gets the refExterna value for this RegistroWs.
     * 
     * @return refExterna
     */
    public java.lang.String getRefExterna() {
        return refExterna;
    }


    /**
     * Sets the refExterna value for this RegistroWs.
     * 
     * @param refExterna
     */
    public void setRefExterna(java.lang.String refExterna) {
        this.refExterna = refExterna;
    }


    /**
     * Gets the solicita value for this RegistroWs.
     * 
     * @return solicita
     */
    public java.lang.String getSolicita() {
        return solicita;
    }


    /**
     * Sets the solicita value for this RegistroWs.
     * 
     * @param solicita
     */
    public void setSolicita(java.lang.String solicita) {
        this.solicita = solicita;
    }


    /**
     * Gets the tipoAsunto value for this RegistroWs.
     * 
     * @return tipoAsunto
     */
    public java.lang.String getTipoAsunto() {
        return tipoAsunto;
    }


    /**
     * Sets the tipoAsunto value for this RegistroWs.
     * 
     * @param tipoAsunto
     */
    public void setTipoAsunto(java.lang.String tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }


    /**
     * Gets the tipoTransporte value for this RegistroWs.
     * 
     * @return tipoTransporte
     */
    public java.lang.String getTipoTransporte() {
        return tipoTransporte;
    }


    /**
     * Sets the tipoTransporte value for this RegistroWs.
     * 
     * @param tipoTransporte
     */
    public void setTipoTransporte(java.lang.String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }


    /**
     * Gets the version value for this RegistroWs.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this RegistroWs.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroWs)) return false;
        RegistroWs other = (RegistroWs) obj;
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
            ((this.aplicacion==null && other.getAplicacion()==null) || 
             (this.aplicacion!=null &&
              this.aplicacion.equals(other.getAplicacion()))) &&
            ((this.codigoAsunto==null && other.getCodigoAsunto()==null) || 
             (this.codigoAsunto!=null &&
              this.codigoAsunto.equals(other.getCodigoAsunto()))) &&
            ((this.codigoUsuario==null && other.getCodigoUsuario()==null) || 
             (this.codigoUsuario!=null &&
              this.codigoUsuario.equals(other.getCodigoUsuario()))) &&
            ((this.contactoUsuario==null && other.getContactoUsuario()==null) || 
             (this.contactoUsuario!=null &&
              this.contactoUsuario.equals(other.getContactoUsuario()))) &&
            ((this.docFisica==null && other.getDocFisica()==null) || 
             (this.docFisica!=null &&
              this.docFisica.equals(other.getDocFisica()))) &&
            ((this.expone==null && other.getExpone()==null) || 
             (this.expone!=null &&
              this.expone.equals(other.getExpone()))) &&
            ((this.extracto==null && other.getExtracto()==null) || 
             (this.extracto!=null &&
              this.extracto.equals(other.getExtracto()))) &&
            ((this.fecha==null && other.getFecha()==null) || 
             (this.fecha!=null &&
              this.fecha.equals(other.getFecha()))) &&
            ((this.idioma==null && other.getIdioma()==null) || 
             (this.idioma!=null &&
              this.idioma.equals(other.getIdioma()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.libro==null && other.getLibro()==null) || 
             (this.libro!=null &&
              this.libro.equals(other.getLibro()))) &&
            ((this.numExpediente==null && other.getNumExpediente()==null) || 
             (this.numExpediente!=null &&
              this.numExpediente.equals(other.getNumExpediente()))) &&
            ((this.numTransporte==null && other.getNumTransporte()==null) || 
             (this.numTransporte!=null &&
              this.numTransporte.equals(other.getNumTransporte()))) &&
            ((this.numero==null && other.getNumero()==null) || 
             (this.numero!=null &&
              this.numero.equals(other.getNumero()))) &&
            ((this.numeroRegistroFormateado==null && other.getNumeroRegistroFormateado()==null) || 
             (this.numeroRegistroFormateado!=null &&
              this.numeroRegistroFormateado.equals(other.getNumeroRegistroFormateado()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.oficina==null && other.getOficina()==null) || 
             (this.oficina!=null &&
              this.oficina.equals(other.getOficina()))) &&
            ((this.refExterna==null && other.getRefExterna()==null) || 
             (this.refExterna!=null &&
              this.refExterna.equals(other.getRefExterna()))) &&
            ((this.solicita==null && other.getSolicita()==null) || 
             (this.solicita!=null &&
              this.solicita.equals(other.getSolicita()))) &&
            ((this.tipoAsunto==null && other.getTipoAsunto()==null) || 
             (this.tipoAsunto!=null &&
              this.tipoAsunto.equals(other.getTipoAsunto()))) &&
            ((this.tipoTransporte==null && other.getTipoTransporte()==null) || 
             (this.tipoTransporte!=null &&
              this.tipoTransporte.equals(other.getTipoTransporte()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion())));
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
        if (getAplicacion() != null) {
            _hashCode += getAplicacion().hashCode();
        }
        if (getCodigoAsunto() != null) {
            _hashCode += getCodigoAsunto().hashCode();
        }
        if (getCodigoUsuario() != null) {
            _hashCode += getCodigoUsuario().hashCode();
        }
        if (getContactoUsuario() != null) {
            _hashCode += getContactoUsuario().hashCode();
        }
        if (getDocFisica() != null) {
            _hashCode += getDocFisica().hashCode();
        }
        if (getExpone() != null) {
            _hashCode += getExpone().hashCode();
        }
        if (getExtracto() != null) {
            _hashCode += getExtracto().hashCode();
        }
        if (getFecha() != null) {
            _hashCode += getFecha().hashCode();
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
        if (getLibro() != null) {
            _hashCode += getLibro().hashCode();
        }
        if (getNumExpediente() != null) {
            _hashCode += getNumExpediente().hashCode();
        }
        if (getNumTransporte() != null) {
            _hashCode += getNumTransporte().hashCode();
        }
        if (getNumero() != null) {
            _hashCode += getNumero().hashCode();
        }
        if (getNumeroRegistroFormateado() != null) {
            _hashCode += getNumeroRegistroFormateado().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getOficina() != null) {
            _hashCode += getOficina().hashCode();
        }
        if (getRefExterna() != null) {
            _hashCode += getRefExterna().hashCode();
        }
        if (getSolicita() != null) {
            _hashCode += getSolicita().hashCode();
        }
        if (getTipoAsunto() != null) {
            _hashCode += getTipoAsunto().hashCode();
        }
        if (getTipoTransporte() != null) {
            _hashCode += getTipoTransporte().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistroWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "registroWs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anexos");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anexos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "anexoWs"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aplicacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "aplicacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoAsunto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoAsunto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contactoUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "contactoUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docFisica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "docFisica"));
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
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idioma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idioma"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("libro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "libro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numExpediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numExpediente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTransporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numTransporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observaciones");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observaciones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oficina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oficina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refExterna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "refExterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("tipoAsunto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAsunto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoTransporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoTransporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
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
