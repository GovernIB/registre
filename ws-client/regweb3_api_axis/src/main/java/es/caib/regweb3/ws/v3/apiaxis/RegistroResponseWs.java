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

    private java.lang.String codigoAsuntoCodigo;

    private java.lang.String codigoAsuntoDescripcion;

    private java.lang.String codigoUsuario;

    private java.lang.String contactoUsuario;

    private java.lang.String docFisicaCodigo;

    private java.lang.String docFisicaDescripcion;

    private java.lang.String entidadCodigo;

    private java.lang.String entidadDenominacion;

    private java.lang.String expone;

    private java.lang.String extracto;

    private java.util.Calendar fechaOrigen;

    private java.util.Calendar fechaRegistro;

    private java.lang.String idiomaCodigo;

    private java.lang.String idiomaDescripcion;

    private es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados;

    private java.lang.String libroCodigo;

    private java.lang.String libroDescripcion;

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

    private java.lang.String tipoAsuntoCodigo;

    private java.lang.String tipoAsuntoDescripcion;

    private java.lang.String tipoTransporteCodigo;

    private java.lang.String tipoTransporteDescripcion;

    private java.lang.String version;

    public RegistroResponseWs() {
    }

    public RegistroResponseWs(
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
           java.lang.String version) {
           this.anexos = anexos;
           this.aplicacion = aplicacion;
           this.codigoAsuntoCodigo = codigoAsuntoCodigo;
           this.codigoAsuntoDescripcion = codigoAsuntoDescripcion;
           this.codigoUsuario = codigoUsuario;
           this.contactoUsuario = contactoUsuario;
           this.docFisicaCodigo = docFisicaCodigo;
           this.docFisicaDescripcion = docFisicaDescripcion;
           this.entidadCodigo = entidadCodigo;
           this.entidadDenominacion = entidadDenominacion;
           this.expone = expone;
           this.extracto = extracto;
           this.fechaOrigen = fechaOrigen;
           this.fechaRegistro = fechaRegistro;
           this.idiomaCodigo = idiomaCodigo;
           this.idiomaDescripcion = idiomaDescripcion;
           this.interesados = interesados;
           this.libroCodigo = libroCodigo;
           this.libroDescripcion = libroDescripcion;
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
           this.tipoAsuntoCodigo = tipoAsuntoCodigo;
           this.tipoAsuntoDescripcion = tipoAsuntoDescripcion;
           this.tipoTransporteCodigo = tipoTransporteCodigo;
           this.tipoTransporteDescripcion = tipoTransporteDescripcion;
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
     * Gets the codigoAsuntoCodigo value for this RegistroResponseWs.
     * 
     * @return codigoAsuntoCodigo
     */
    public java.lang.String getCodigoAsuntoCodigo() {
        return codigoAsuntoCodigo;
    }


    /**
     * Sets the codigoAsuntoCodigo value for this RegistroResponseWs.
     * 
     * @param codigoAsuntoCodigo
     */
    public void setCodigoAsuntoCodigo(java.lang.String codigoAsuntoCodigo) {
        this.codigoAsuntoCodigo = codigoAsuntoCodigo;
    }


    /**
     * Gets the codigoAsuntoDescripcion value for this RegistroResponseWs.
     * 
     * @return codigoAsuntoDescripcion
     */
    public java.lang.String getCodigoAsuntoDescripcion() {
        return codigoAsuntoDescripcion;
    }


    /**
     * Sets the codigoAsuntoDescripcion value for this RegistroResponseWs.
     * 
     * @param codigoAsuntoDescripcion
     */
    public void setCodigoAsuntoDescripcion(java.lang.String codigoAsuntoDescripcion) {
        this.codigoAsuntoDescripcion = codigoAsuntoDescripcion;
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
     * Gets the docFisicaCodigo value for this RegistroResponseWs.
     * 
     * @return docFisicaCodigo
     */
    public java.lang.String getDocFisicaCodigo() {
        return docFisicaCodigo;
    }


    /**
     * Sets the docFisicaCodigo value for this RegistroResponseWs.
     * 
     * @param docFisicaCodigo
     */
    public void setDocFisicaCodigo(java.lang.String docFisicaCodigo) {
        this.docFisicaCodigo = docFisicaCodigo;
    }


    /**
     * Gets the docFisicaDescripcion value for this RegistroResponseWs.
     * 
     * @return docFisicaDescripcion
     */
    public java.lang.String getDocFisicaDescripcion() {
        return docFisicaDescripcion;
    }


    /**
     * Sets the docFisicaDescripcion value for this RegistroResponseWs.
     * 
     * @param docFisicaDescripcion
     */
    public void setDocFisicaDescripcion(java.lang.String docFisicaDescripcion) {
        this.docFisicaDescripcion = docFisicaDescripcion;
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
     * Gets the idiomaCodigo value for this RegistroResponseWs.
     * 
     * @return idiomaCodigo
     */
    public java.lang.String getIdiomaCodigo() {
        return idiomaCodigo;
    }


    /**
     * Sets the idiomaCodigo value for this RegistroResponseWs.
     * 
     * @param idiomaCodigo
     */
    public void setIdiomaCodigo(java.lang.String idiomaCodigo) {
        this.idiomaCodigo = idiomaCodigo;
    }


    /**
     * Gets the idiomaDescripcion value for this RegistroResponseWs.
     * 
     * @return idiomaDescripcion
     */
    public java.lang.String getIdiomaDescripcion() {
        return idiomaDescripcion;
    }


    /**
     * Sets the idiomaDescripcion value for this RegistroResponseWs.
     * 
     * @param idiomaDescripcion
     */
    public void setIdiomaDescripcion(java.lang.String idiomaDescripcion) {
        this.idiomaDescripcion = idiomaDescripcion;
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
     * Gets the libroCodigo value for this RegistroResponseWs.
     * 
     * @return libroCodigo
     */
    public java.lang.String getLibroCodigo() {
        return libroCodigo;
    }


    /**
     * Sets the libroCodigo value for this RegistroResponseWs.
     * 
     * @param libroCodigo
     */
    public void setLibroCodigo(java.lang.String libroCodigo) {
        this.libroCodigo = libroCodigo;
    }


    /**
     * Gets the libroDescripcion value for this RegistroResponseWs.
     * 
     * @return libroDescripcion
     */
    public java.lang.String getLibroDescripcion() {
        return libroDescripcion;
    }


    /**
     * Sets the libroDescripcion value for this RegistroResponseWs.
     * 
     * @param libroDescripcion
     */
    public void setLibroDescripcion(java.lang.String libroDescripcion) {
        this.libroDescripcion = libroDescripcion;
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
     * Gets the tipoAsuntoCodigo value for this RegistroResponseWs.
     * 
     * @return tipoAsuntoCodigo
     */
    public java.lang.String getTipoAsuntoCodigo() {
        return tipoAsuntoCodigo;
    }


    /**
     * Sets the tipoAsuntoCodigo value for this RegistroResponseWs.
     * 
     * @param tipoAsuntoCodigo
     */
    public void setTipoAsuntoCodigo(java.lang.String tipoAsuntoCodigo) {
        this.tipoAsuntoCodigo = tipoAsuntoCodigo;
    }


    /**
     * Gets the tipoAsuntoDescripcion value for this RegistroResponseWs.
     * 
     * @return tipoAsuntoDescripcion
     */
    public java.lang.String getTipoAsuntoDescripcion() {
        return tipoAsuntoDescripcion;
    }


    /**
     * Sets the tipoAsuntoDescripcion value for this RegistroResponseWs.
     * 
     * @param tipoAsuntoDescripcion
     */
    public void setTipoAsuntoDescripcion(java.lang.String tipoAsuntoDescripcion) {
        this.tipoAsuntoDescripcion = tipoAsuntoDescripcion;
    }


    /**
     * Gets the tipoTransporteCodigo value for this RegistroResponseWs.
     * 
     * @return tipoTransporteCodigo
     */
    public java.lang.String getTipoTransporteCodigo() {
        return tipoTransporteCodigo;
    }


    /**
     * Sets the tipoTransporteCodigo value for this RegistroResponseWs.
     * 
     * @param tipoTransporteCodigo
     */
    public void setTipoTransporteCodigo(java.lang.String tipoTransporteCodigo) {
        this.tipoTransporteCodigo = tipoTransporteCodigo;
    }


    /**
     * Gets the tipoTransporteDescripcion value for this RegistroResponseWs.
     * 
     * @return tipoTransporteDescripcion
     */
    public java.lang.String getTipoTransporteDescripcion() {
        return tipoTransporteDescripcion;
    }


    /**
     * Sets the tipoTransporteDescripcion value for this RegistroResponseWs.
     * 
     * @param tipoTransporteDescripcion
     */
    public void setTipoTransporteDescripcion(java.lang.String tipoTransporteDescripcion) {
        this.tipoTransporteDescripcion = tipoTransporteDescripcion;
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
            ((this.codigoAsuntoCodigo==null && other.getCodigoAsuntoCodigo()==null) || 
             (this.codigoAsuntoCodigo!=null &&
              this.codigoAsuntoCodigo.equals(other.getCodigoAsuntoCodigo()))) &&
            ((this.codigoAsuntoDescripcion==null && other.getCodigoAsuntoDescripcion()==null) || 
             (this.codigoAsuntoDescripcion!=null &&
              this.codigoAsuntoDescripcion.equals(other.getCodigoAsuntoDescripcion()))) &&
            ((this.codigoUsuario==null && other.getCodigoUsuario()==null) || 
             (this.codigoUsuario!=null &&
              this.codigoUsuario.equals(other.getCodigoUsuario()))) &&
            ((this.contactoUsuario==null && other.getContactoUsuario()==null) || 
             (this.contactoUsuario!=null &&
              this.contactoUsuario.equals(other.getContactoUsuario()))) &&
            ((this.docFisicaCodigo==null && other.getDocFisicaCodigo()==null) || 
             (this.docFisicaCodigo!=null &&
              this.docFisicaCodigo.equals(other.getDocFisicaCodigo()))) &&
            ((this.docFisicaDescripcion==null && other.getDocFisicaDescripcion()==null) || 
             (this.docFisicaDescripcion!=null &&
              this.docFisicaDescripcion.equals(other.getDocFisicaDescripcion()))) &&
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
            ((this.idiomaCodigo==null && other.getIdiomaCodigo()==null) || 
             (this.idiomaCodigo!=null &&
              this.idiomaCodigo.equals(other.getIdiomaCodigo()))) &&
            ((this.idiomaDescripcion==null && other.getIdiomaDescripcion()==null) || 
             (this.idiomaDescripcion!=null &&
              this.idiomaDescripcion.equals(other.getIdiomaDescripcion()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.libroCodigo==null && other.getLibroCodigo()==null) || 
             (this.libroCodigo!=null &&
              this.libroCodigo.equals(other.getLibroCodigo()))) &&
            ((this.libroDescripcion==null && other.getLibroDescripcion()==null) || 
             (this.libroDescripcion!=null &&
              this.libroDescripcion.equals(other.getLibroDescripcion()))) &&
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
            ((this.tipoAsuntoCodigo==null && other.getTipoAsuntoCodigo()==null) || 
             (this.tipoAsuntoCodigo!=null &&
              this.tipoAsuntoCodigo.equals(other.getTipoAsuntoCodigo()))) &&
            ((this.tipoAsuntoDescripcion==null && other.getTipoAsuntoDescripcion()==null) || 
             (this.tipoAsuntoDescripcion!=null &&
              this.tipoAsuntoDescripcion.equals(other.getTipoAsuntoDescripcion()))) &&
            ((this.tipoTransporteCodigo==null && other.getTipoTransporteCodigo()==null) || 
             (this.tipoTransporteCodigo!=null &&
              this.tipoTransporteCodigo.equals(other.getTipoTransporteCodigo()))) &&
            ((this.tipoTransporteDescripcion==null && other.getTipoTransporteDescripcion()==null) || 
             (this.tipoTransporteDescripcion!=null &&
              this.tipoTransporteDescripcion.equals(other.getTipoTransporteDescripcion()))) &&
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
        if (getCodigoAsuntoCodigo() != null) {
            _hashCode += getCodigoAsuntoCodigo().hashCode();
        }
        if (getCodigoAsuntoDescripcion() != null) {
            _hashCode += getCodigoAsuntoDescripcion().hashCode();
        }
        if (getCodigoUsuario() != null) {
            _hashCode += getCodigoUsuario().hashCode();
        }
        if (getContactoUsuario() != null) {
            _hashCode += getContactoUsuario().hashCode();
        }
        if (getDocFisicaCodigo() != null) {
            _hashCode += getDocFisicaCodigo().hashCode();
        }
        if (getDocFisicaDescripcion() != null) {
            _hashCode += getDocFisicaDescripcion().hashCode();
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
        if (getIdiomaCodigo() != null) {
            _hashCode += getIdiomaCodigo().hashCode();
        }
        if (getIdiomaDescripcion() != null) {
            _hashCode += getIdiomaDescripcion().hashCode();
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
        if (getLibroCodigo() != null) {
            _hashCode += getLibroCodigo().hashCode();
        }
        if (getLibroDescripcion() != null) {
            _hashCode += getLibroDescripcion().hashCode();
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
        if (getTipoAsuntoCodigo() != null) {
            _hashCode += getTipoAsuntoCodigo().hashCode();
        }
        if (getTipoAsuntoDescripcion() != null) {
            _hashCode += getTipoAsuntoDescripcion().hashCode();
        }
        if (getTipoTransporteCodigo() != null) {
            _hashCode += getTipoTransporteCodigo().hashCode();
        }
        if (getTipoTransporteDescripcion() != null) {
            _hashCode += getTipoTransporteDescripcion().hashCode();
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
        elemField.setFieldName("codigoAsuntoCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoAsuntoCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoAsuntoDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoAsuntoDescripcion"));
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
        elemField.setFieldName("docFisicaCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "docFisicaCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docFisicaDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "docFisicaDescripcion"));
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
        elemField.setFieldName("idiomaCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idiomaCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idiomaDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idiomaDescripcion"));
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
        elemField.setFieldName("libroCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "libroCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("libroDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "libroDescripcion"));
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
        elemField.setFieldName("tipoAsuntoCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAsuntoCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoAsuntoDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoAsuntoDescripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoTransporteCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoTransporteCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoTransporteDescripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoTransporteDescripcion"));
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
