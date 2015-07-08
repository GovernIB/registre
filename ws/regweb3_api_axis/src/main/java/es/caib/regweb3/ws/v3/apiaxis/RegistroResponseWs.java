/**
 * RegistroResponseWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class RegistroResponseWs  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos;

    private java.lang.String aplicacion;

    private java.lang.String codigoAsunto;

    private java.lang.String codigoUsuario;

    private java.lang.String contactoUsuario;

    private java.lang.String docFisica;

    private java.lang.String entidadCodigo;

    private java.lang.String entidadDenominacion;

    private java.lang.String expone;

    private java.lang.String extracto;

    private java.util.Calendar fechaOrigen;

    private java.util.Calendar fechaRegistro;

    private java.lang.String idioma;

    private es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados;

    private java.lang.String libro;

    private java.lang.String nombreUsuario;

    private java.lang.String numExpediente;

    private java.lang.String numTransporte;

    private int numeroRegistro;

    private java.lang.String numeroRegistroFormateado;

    private java.lang.String numeroRegistroOrigen;

    private java.lang.String observaciones;

    private java.lang.String oficinaCodigo;

    private java.lang.String oficinaDenominacion;

    private java.lang.String refExterna;

    private java.lang.String solicita;

    private java.lang.String tipoAsunto;

    private java.lang.String tipoTransporte;

    private java.lang.String version;

    public RegistroResponseWs() {
    }

    public RegistroResponseWs(
           es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos,
           java.lang.String aplicacion,
           java.lang.String codigoAsunto,
           java.lang.String codigoUsuario,
           java.lang.String contactoUsuario,
           java.lang.String docFisica,
           java.lang.String entidadCodigo,
           java.lang.String entidadDenominacion,
           java.lang.String expone,
           java.lang.String extracto,
           java.util.Calendar fechaOrigen,
           java.util.Calendar fechaRegistro,
           java.lang.String idioma,
           es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados,
           java.lang.String libro,
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
           java.lang.String tipoAsunto,
           java.lang.String tipoTransporte,
           java.lang.String version) {
           this.anexos = anexos;
           this.aplicacion = aplicacion;
           this.codigoAsunto = codigoAsunto;
           this.codigoUsuario = codigoUsuario;
           this.contactoUsuario = contactoUsuario;
           this.docFisica = docFisica;
           this.entidadCodigo = entidadCodigo;
           this.entidadDenominacion = entidadDenominacion;
           this.expone = expone;
           this.extracto = extracto;
           this.fechaOrigen = fechaOrigen;
           this.fechaRegistro = fechaRegistro;
           this.idioma = idioma;
           this.interesados = interesados;
           this.libro = libro;
           this.nombreUsuario = nombreUsuario;
           this.numExpediente = numExpediente;
           this.numTransporte = numTransporte;
           this.numeroRegistro = numeroRegistro;
           this.numeroRegistroFormateado = numeroRegistroFormateado;
           this.numeroRegistroOrigen = numeroRegistroOrigen;
           this.observaciones = observaciones;
           this.oficinaCodigo = oficinaCodigo;
           this.oficinaDenominacion = oficinaDenominacion;
           this.refExterna = refExterna;
           this.solicita = solicita;
           this.tipoAsunto = tipoAsunto;
           this.tipoTransporte = tipoTransporte;
           this.version = version;
    }


    /**
     * Gets the anexos value for this RegistroResponseWs.
     * 
     * @return anexos
     */
    public es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] getAnexos() {
        return anexos;
    }


    /**
     * Sets the anexos value for this RegistroResponseWs.
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
     * Gets the aplicacion value for this RegistroResponseWs.
     * 
     * @return aplicacion
     */
    public java.lang.String getAplicacion() {
        return aplicacion;
    }


    /**
     * Sets the aplicacion value for this RegistroResponseWs.
     * 
     * @param aplicacion
     */
    public void setAplicacion(java.lang.String aplicacion) {
        this.aplicacion = aplicacion;
    }


    /**
     * Gets the codigoAsunto value for this RegistroResponseWs.
     * 
     * @return codigoAsunto
     */
    public java.lang.String getCodigoAsunto() {
        return codigoAsunto;
    }


    /**
     * Sets the codigoAsunto value for this RegistroResponseWs.
     * 
     * @param codigoAsunto
     */
    public void setCodigoAsunto(java.lang.String codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
    }


    /**
     * Gets the codigoUsuario value for this RegistroResponseWs.
     * 
     * @return codigoUsuario
     */
    public java.lang.String getCodigoUsuario() {
        return codigoUsuario;
    }


    /**
     * Sets the codigoUsuario value for this RegistroResponseWs.
     * 
     * @param codigoUsuario
     */
    public void setCodigoUsuario(java.lang.String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }


    /**
     * Gets the contactoUsuario value for this RegistroResponseWs.
     * 
     * @return contactoUsuario
     */
    public java.lang.String getContactoUsuario() {
        return contactoUsuario;
    }


    /**
     * Sets the contactoUsuario value for this RegistroResponseWs.
     * 
     * @param contactoUsuario
     */
    public void setContactoUsuario(java.lang.String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
    }


    /**
     * Gets the docFisica value for this RegistroResponseWs.
     * 
     * @return docFisica
     */
    public java.lang.String getDocFisica() {
        return docFisica;
    }


    /**
     * Sets the docFisica value for this RegistroResponseWs.
     * 
     * @param docFisica
     */
    public void setDocFisica(java.lang.String docFisica) {
        this.docFisica = docFisica;
    }


    /**
     * Gets the entidadCodigo value for this RegistroResponseWs.
     * 
     * @return entidadCodigo
     */
    public java.lang.String getEntidadCodigo() {
        return entidadCodigo;
    }


    /**
     * Sets the entidadCodigo value for this RegistroResponseWs.
     * 
     * @param entidadCodigo
     */
    public void setEntidadCodigo(java.lang.String entidadCodigo) {
        this.entidadCodigo = entidadCodigo;
    }


    /**
     * Gets the entidadDenominacion value for this RegistroResponseWs.
     * 
     * @return entidadDenominacion
     */
    public java.lang.String getEntidadDenominacion() {
        return entidadDenominacion;
    }


    /**
     * Sets the entidadDenominacion value for this RegistroResponseWs.
     * 
     * @param entidadDenominacion
     */
    public void setEntidadDenominacion(java.lang.String entidadDenominacion) {
        this.entidadDenominacion = entidadDenominacion;
    }


    /**
     * Gets the expone value for this RegistroResponseWs.
     * 
     * @return expone
     */
    public java.lang.String getExpone() {
        return expone;
    }


    /**
     * Sets the expone value for this RegistroResponseWs.
     * 
     * @param expone
     */
    public void setExpone(java.lang.String expone) {
        this.expone = expone;
    }


    /**
     * Gets the extracto value for this RegistroResponseWs.
     * 
     * @return extracto
     */
    public java.lang.String getExtracto() {
        return extracto;
    }


    /**
     * Sets the extracto value for this RegistroResponseWs.
     * 
     * @param extracto
     */
    public void setExtracto(java.lang.String extracto) {
        this.extracto = extracto;
    }


    /**
     * Gets the fechaOrigen value for this RegistroResponseWs.
     * 
     * @return fechaOrigen
     */
    public java.util.Calendar getFechaOrigen() {
        return fechaOrigen;
    }


    /**
     * Sets the fechaOrigen value for this RegistroResponseWs.
     * 
     * @param fechaOrigen
     */
    public void setFechaOrigen(java.util.Calendar fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }


    /**
     * Gets the fechaRegistro value for this RegistroResponseWs.
     * 
     * @return fechaRegistro
     */
    public java.util.Calendar getFechaRegistro() {
        return fechaRegistro;
    }


    /**
     * Sets the fechaRegistro value for this RegistroResponseWs.
     * 
     * @param fechaRegistro
     */
    public void setFechaRegistro(java.util.Calendar fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    /**
     * Gets the idioma value for this RegistroResponseWs.
     * 
     * @return idioma
     */
    public java.lang.String getIdioma() {
        return idioma;
    }


    /**
     * Sets the idioma value for this RegistroResponseWs.
     * 
     * @param idioma
     */
    public void setIdioma(java.lang.String idioma) {
        this.idioma = idioma;
    }


    /**
     * Gets the interesados value for this RegistroResponseWs.
     * 
     * @return interesados
     */
    public es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] getInteresados() {
        return interesados;
    }


    /**
     * Sets the interesados value for this RegistroResponseWs.
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
     * Gets the libro value for this RegistroResponseWs.
     * 
     * @return libro
     */
    public java.lang.String getLibro() {
        return libro;
    }


    /**
     * Sets the libro value for this RegistroResponseWs.
     * 
     * @param libro
     */
    public void setLibro(java.lang.String libro) {
        this.libro = libro;
    }


    /**
     * Gets the nombreUsuario value for this RegistroResponseWs.
     * 
     * @return nombreUsuario
     */
    public java.lang.String getNombreUsuario() {
        return nombreUsuario;
    }


    /**
     * Sets the nombreUsuario value for this RegistroResponseWs.
     * 
     * @param nombreUsuario
     */
    public void setNombreUsuario(java.lang.String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }


    /**
     * Gets the numExpediente value for this RegistroResponseWs.
     * 
     * @return numExpediente
     */
    public java.lang.String getNumExpediente() {
        return numExpediente;
    }


    /**
     * Sets the numExpediente value for this RegistroResponseWs.
     * 
     * @param numExpediente
     */
    public void setNumExpediente(java.lang.String numExpediente) {
        this.numExpediente = numExpediente;
    }


    /**
     * Gets the numTransporte value for this RegistroResponseWs.
     * 
     * @return numTransporte
     */
    public java.lang.String getNumTransporte() {
        return numTransporte;
    }


    /**
     * Sets the numTransporte value for this RegistroResponseWs.
     * 
     * @param numTransporte
     */
    public void setNumTransporte(java.lang.String numTransporte) {
        this.numTransporte = numTransporte;
    }


    /**
     * Gets the numeroRegistro value for this RegistroResponseWs.
     * 
     * @return numeroRegistro
     */
    public int getNumeroRegistro() {
        return numeroRegistro;
    }


    /**
     * Sets the numeroRegistro value for this RegistroResponseWs.
     * 
     * @param numeroRegistro
     */
    public void setNumeroRegistro(int numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }


    /**
     * Gets the numeroRegistroFormateado value for this RegistroResponseWs.
     * 
     * @return numeroRegistroFormateado
     */
    public java.lang.String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }


    /**
     * Sets the numeroRegistroFormateado value for this RegistroResponseWs.
     * 
     * @param numeroRegistroFormateado
     */
    public void setNumeroRegistroFormateado(java.lang.String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }


    /**
     * Gets the numeroRegistroOrigen value for this RegistroResponseWs.
     * 
     * @return numeroRegistroOrigen
     */
    public java.lang.String getNumeroRegistroOrigen() {
        return numeroRegistroOrigen;
    }


    /**
     * Sets the numeroRegistroOrigen value for this RegistroResponseWs.
     * 
     * @param numeroRegistroOrigen
     */
    public void setNumeroRegistroOrigen(java.lang.String numeroRegistroOrigen) {
        this.numeroRegistroOrigen = numeroRegistroOrigen;
    }


    /**
     * Gets the observaciones value for this RegistroResponseWs.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this RegistroResponseWs.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the oficinaCodigo value for this RegistroResponseWs.
     * 
     * @return oficinaCodigo
     */
    public java.lang.String getOficinaCodigo() {
        return oficinaCodigo;
    }


    /**
     * Sets the oficinaCodigo value for this RegistroResponseWs.
     * 
     * @param oficinaCodigo
     */
    public void setOficinaCodigo(java.lang.String oficinaCodigo) {
        this.oficinaCodigo = oficinaCodigo;
    }


    /**
     * Gets the oficinaDenominacion value for this RegistroResponseWs.
     * 
     * @return oficinaDenominacion
     */
    public java.lang.String getOficinaDenominacion() {
        return oficinaDenominacion;
    }


    /**
     * Sets the oficinaDenominacion value for this RegistroResponseWs.
     * 
     * @param oficinaDenominacion
     */
    public void setOficinaDenominacion(java.lang.String oficinaDenominacion) {
        this.oficinaDenominacion = oficinaDenominacion;
    }


    /**
     * Gets the refExterna value for this RegistroResponseWs.
     * 
     * @return refExterna
     */
    public java.lang.String getRefExterna() {
        return refExterna;
    }


    /**
     * Sets the refExterna value for this RegistroResponseWs.
     * 
     * @param refExterna
     */
    public void setRefExterna(java.lang.String refExterna) {
        this.refExterna = refExterna;
    }


    /**
     * Gets the solicita value for this RegistroResponseWs.
     * 
     * @return solicita
     */
    public java.lang.String getSolicita() {
        return solicita;
    }


    /**
     * Sets the solicita value for this RegistroResponseWs.
     * 
     * @param solicita
     */
    public void setSolicita(java.lang.String solicita) {
        this.solicita = solicita;
    }


    /**
     * Gets the tipoAsunto value for this RegistroResponseWs.
     * 
     * @return tipoAsunto
     */
    public java.lang.String getTipoAsunto() {
        return tipoAsunto;
    }


    /**
     * Sets the tipoAsunto value for this RegistroResponseWs.
     * 
     * @param tipoAsunto
     */
    public void setTipoAsunto(java.lang.String tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }


    /**
     * Gets the tipoTransporte value for this RegistroResponseWs.
     * 
     * @return tipoTransporte
     */
    public java.lang.String getTipoTransporte() {
        return tipoTransporte;
    }


    /**
     * Sets the tipoTransporte value for this RegistroResponseWs.
     * 
     * @param tipoTransporte
     */
    public void setTipoTransporte(java.lang.String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }


    /**
     * Gets the version value for this RegistroResponseWs.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this RegistroResponseWs.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistroResponseWs)) return false;
        RegistroResponseWs other = (RegistroResponseWs) obj;
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
            ((this.entidadCodigo==null && other.getEntidadCodigo()==null) || 
             (this.entidadCodigo!=null &&
              this.entidadCodigo.equals(other.getEntidadCodigo()))) &&
            ((this.entidadDenominacion==null && other.getEntidadDenominacion()==null) || 
             (this.entidadDenominacion!=null &&
              this.entidadDenominacion.equals(other.getEntidadDenominacion()))) &&
            ((this.expone==null && other.getExpone()==null) || 
             (this.expone!=null &&
              this.expone.equals(other.getExpone()))) &&
            ((this.extracto==null && other.getExtracto()==null) || 
             (this.extracto!=null &&
              this.extracto.equals(other.getExtracto()))) &&
            ((this.fechaOrigen==null && other.getFechaOrigen()==null) || 
             (this.fechaOrigen!=null &&
              this.fechaOrigen.equals(other.getFechaOrigen()))) &&
            ((this.fechaRegistro==null && other.getFechaRegistro()==null) || 
             (this.fechaRegistro!=null &&
              this.fechaRegistro.equals(other.getFechaRegistro()))) &&
            ((this.idioma==null && other.getIdioma()==null) || 
             (this.idioma!=null &&
              this.idioma.equals(other.getIdioma()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.libro==null && other.getLibro()==null) || 
             (this.libro!=null &&
              this.libro.equals(other.getLibro()))) &&
            ((this.nombreUsuario==null && other.getNombreUsuario()==null) || 
             (this.nombreUsuario!=null &&
              this.nombreUsuario.equals(other.getNombreUsuario()))) &&
            ((this.numExpediente==null && other.getNumExpediente()==null) || 
             (this.numExpediente!=null &&
              this.numExpediente.equals(other.getNumExpediente()))) &&
            ((this.numTransporte==null && other.getNumTransporte()==null) || 
             (this.numTransporte!=null &&
              this.numTransporte.equals(other.getNumTransporte()))) &&
            this.numeroRegistro == other.getNumeroRegistro() &&
            ((this.numeroRegistroFormateado==null && other.getNumeroRegistroFormateado()==null) || 
             (this.numeroRegistroFormateado!=null &&
              this.numeroRegistroFormateado.equals(other.getNumeroRegistroFormateado()))) &&
            ((this.numeroRegistroOrigen==null && other.getNumeroRegistroOrigen()==null) || 
             (this.numeroRegistroOrigen!=null &&
              this.numeroRegistroOrigen.equals(other.getNumeroRegistroOrigen()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.oficinaCodigo==null && other.getOficinaCodigo()==null) || 
             (this.oficinaCodigo!=null &&
              this.oficinaCodigo.equals(other.getOficinaCodigo()))) &&
            ((this.oficinaDenominacion==null && other.getOficinaDenominacion()==null) || 
             (this.oficinaDenominacion!=null &&
              this.oficinaDenominacion.equals(other.getOficinaDenominacion()))) &&
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
        if (getEntidadCodigo() != null) {
            _hashCode += getEntidadCodigo().hashCode();
        }
        if (getEntidadDenominacion() != null) {
            _hashCode += getEntidadDenominacion().hashCode();
        }
        if (getExpone() != null) {
            _hashCode += getExpone().hashCode();
        }
        if (getExtracto() != null) {
            _hashCode += getExtracto().hashCode();
        }
        if (getFechaOrigen() != null) {
            _hashCode += getFechaOrigen().hashCode();
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
        if (getLibro() != null) {
            _hashCode += getLibro().hashCode();
        }
        if (getNombreUsuario() != null) {
            _hashCode += getNombreUsuario().hashCode();
        }
        if (getNumExpediente() != null) {
            _hashCode += getNumExpediente().hashCode();
        }
        if (getNumTransporte() != null) {
            _hashCode += getNumTransporte().hashCode();
        }
        _hashCode += getNumeroRegistro();
        if (getNumeroRegistroFormateado() != null) {
            _hashCode += getNumeroRegistroFormateado().hashCode();
        }
        if (getNumeroRegistroOrigen() != null) {
            _hashCode += getNumeroRegistroOrigen().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getOficinaCodigo() != null) {
            _hashCode += getOficinaCodigo().hashCode();
        }
        if (getOficinaDenominacion() != null) {
            _hashCode += getOficinaDenominacion().hashCode();
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
        new org.apache.axis.description.TypeDesc(RegistroResponseWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "registroResponseWs"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadDenominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("fechaOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaOrigen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("nombreUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nombreUsuario"));
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
        elemField.setFieldName("numeroRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("numeroRegistroOrigen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistroOrigen"));
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
        elemField.setFieldName("oficinaCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oficinaCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oficinaDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oficinaDenominacion"));
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
