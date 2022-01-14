/**
 * AsientoRegistralWs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb3.ws.v3.apiaxis;

public class AsientoRegistralWs  implements java.io.Serializable {
    private es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos;

    private java.lang.String aplicacion;

    private java.lang.String aplicacionTelematica;

    private java.lang.String codigoAsunto;

    private java.lang.String codigoAsuntoDenominacion;

    private java.lang.String codigoError;

    private java.lang.Long codigoSia;

    private java.lang.String codigoUsuario;

    private java.lang.String descripcionError;

    private java.lang.String entidadCodigo;

    private java.lang.String entidadDenominacion;

    private java.lang.String entidadRegistralDestinoCodigo;

    private java.lang.String entidadRegistralDestinoDenominacion;

    private java.lang.String entidadRegistralInicioCodigo;

    private java.lang.String entidadRegistralInicioDenominacion;

    private java.lang.String entidadRegistralOrigenCodigo;

    private java.lang.String entidadRegistralOrigenDenominacion;

    private java.lang.Long estado;

    private java.lang.String expone;

    private java.util.Calendar fechaRecepcion;

    private java.util.Calendar fechaRegistro;

    private java.util.Calendar fechaRegistroDestino;

    private java.lang.Long id;

    private java.lang.String identificadorIntercambio;

    private java.lang.Long idioma;

    private es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados;

    private java.lang.String libroCodigo;

    private java.lang.String motivo;

    private java.lang.String numeroExpediente;

    private int numeroRegistro;

    private java.lang.String numeroRegistroDestino;

    private java.lang.String numeroRegistroFormateado;

    private java.lang.String numeroTransporte;

    private java.lang.String observaciones;

    private java.lang.Boolean presencial;

    private java.lang.String referenciaExterna;

    private java.lang.String resumen;

    private java.lang.String solicita;

    private java.lang.Long tipoDocumentacionFisicaCodigo;

    private java.lang.String tipoEnvioDocumentacion;

    private java.lang.Long tipoRegistro;

    private java.lang.String tipoTransporte;

    private java.lang.String unidadTramitacionDestinoCodigo;

    private java.lang.String unidadTramitacionDestinoDenominacion;

    private java.lang.String unidadTramitacionOrigenCodigo;

    private java.lang.String unidadTramitacionOrigenDenominacion;

    private java.lang.String version;

    public AsientoRegistralWs() {
    }

    public AsientoRegistralWs(
           es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] anexos,
           java.lang.String aplicacion,
           java.lang.String aplicacionTelematica,
           java.lang.String codigoAsunto,
           java.lang.String codigoAsuntoDenominacion,
           java.lang.String codigoError,
           java.lang.Long codigoSia,
           java.lang.String codigoUsuario,
           java.lang.String descripcionError,
           java.lang.String entidadCodigo,
           java.lang.String entidadDenominacion,
           java.lang.String entidadRegistralDestinoCodigo,
           java.lang.String entidadRegistralDestinoDenominacion,
           java.lang.String entidadRegistralInicioCodigo,
           java.lang.String entidadRegistralInicioDenominacion,
           java.lang.String entidadRegistralOrigenCodigo,
           java.lang.String entidadRegistralOrigenDenominacion,
           java.lang.Long estado,
           java.lang.String expone,
           java.util.Calendar fechaRecepcion,
           java.util.Calendar fechaRegistro,
           java.util.Calendar fechaRegistroDestino,
           java.lang.Long id,
           java.lang.String identificadorIntercambio,
           java.lang.Long idioma,
           es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] interesados,
           java.lang.String libroCodigo,
           java.lang.String motivo,
           java.lang.String numeroExpediente,
           int numeroRegistro,
           java.lang.String numeroRegistroDestino,
           java.lang.String numeroRegistroFormateado,
           java.lang.String numeroTransporte,
           java.lang.String observaciones,
           java.lang.Boolean presencial,
           java.lang.String referenciaExterna,
           java.lang.String resumen,
           java.lang.String solicita,
           java.lang.Long tipoDocumentacionFisicaCodigo,
           java.lang.String tipoEnvioDocumentacion,
           java.lang.Long tipoRegistro,
           java.lang.String tipoTransporte,
           java.lang.String unidadTramitacionDestinoCodigo,
           java.lang.String unidadTramitacionDestinoDenominacion,
           java.lang.String unidadTramitacionOrigenCodigo,
           java.lang.String unidadTramitacionOrigenDenominacion,
           java.lang.String version) {
           this.anexos = anexos;
           this.aplicacion = aplicacion;
           this.aplicacionTelematica = aplicacionTelematica;
           this.codigoAsunto = codigoAsunto;
           this.codigoAsuntoDenominacion = codigoAsuntoDenominacion;
           this.codigoError = codigoError;
           this.codigoSia = codigoSia;
           this.codigoUsuario = codigoUsuario;
           this.descripcionError = descripcionError;
           this.entidadCodigo = entidadCodigo;
           this.entidadDenominacion = entidadDenominacion;
           this.entidadRegistralDestinoCodigo = entidadRegistralDestinoCodigo;
           this.entidadRegistralDestinoDenominacion = entidadRegistralDestinoDenominacion;
           this.entidadRegistralInicioCodigo = entidadRegistralInicioCodigo;
           this.entidadRegistralInicioDenominacion = entidadRegistralInicioDenominacion;
           this.entidadRegistralOrigenCodigo = entidadRegistralOrigenCodigo;
           this.entidadRegistralOrigenDenominacion = entidadRegistralOrigenDenominacion;
           this.estado = estado;
           this.expone = expone;
           this.fechaRecepcion = fechaRecepcion;
           this.fechaRegistro = fechaRegistro;
           this.fechaRegistroDestino = fechaRegistroDestino;
           this.id = id;
           this.identificadorIntercambio = identificadorIntercambio;
           this.idioma = idioma;
           this.interesados = interesados;
           this.libroCodigo = libroCodigo;
           this.motivo = motivo;
           this.numeroExpediente = numeroExpediente;
           this.numeroRegistro = numeroRegistro;
           this.numeroRegistroDestino = numeroRegistroDestino;
           this.numeroRegistroFormateado = numeroRegistroFormateado;
           this.numeroTransporte = numeroTransporte;
           this.observaciones = observaciones;
           this.presencial = presencial;
           this.referenciaExterna = referenciaExterna;
           this.resumen = resumen;
           this.solicita = solicita;
           this.tipoDocumentacionFisicaCodigo = tipoDocumentacionFisicaCodigo;
           this.tipoEnvioDocumentacion = tipoEnvioDocumentacion;
           this.tipoRegistro = tipoRegistro;
           this.tipoTransporte = tipoTransporte;
           this.unidadTramitacionDestinoCodigo = unidadTramitacionDestinoCodigo;
           this.unidadTramitacionDestinoDenominacion = unidadTramitacionDestinoDenominacion;
           this.unidadTramitacionOrigenCodigo = unidadTramitacionOrigenCodigo;
           this.unidadTramitacionOrigenDenominacion = unidadTramitacionOrigenDenominacion;
           this.version = version;
    }


    /**
     * Gets the anexos value for this AsientoRegistralWs.
     * 
     * @return anexos
     */
    public es.caib.regweb3.ws.v3.apiaxis.AnexoWs[] getAnexos() {
        return anexos;
    }


    /**
     * Sets the anexos value for this AsientoRegistralWs.
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
     * Gets the aplicacion value for this AsientoRegistralWs.
     * 
     * @return aplicacion
     */
    public java.lang.String getAplicacion() {
        return aplicacion;
    }


    /**
     * Sets the aplicacion value for this AsientoRegistralWs.
     * 
     * @param aplicacion
     */
    public void setAplicacion(java.lang.String aplicacion) {
        this.aplicacion = aplicacion;
    }


    /**
     * Gets the aplicacionTelematica value for this AsientoRegistralWs.
     * 
     * @return aplicacionTelematica
     */
    public java.lang.String getAplicacionTelematica() {
        return aplicacionTelematica;
    }


    /**
     * Sets the aplicacionTelematica value for this AsientoRegistralWs.
     * 
     * @param aplicacionTelematica
     */
    public void setAplicacionTelematica(java.lang.String aplicacionTelematica) {
        this.aplicacionTelematica = aplicacionTelematica;
    }


    /**
     * Gets the codigoAsunto value for this AsientoRegistralWs.
     * 
     * @return codigoAsunto
     */
    public java.lang.String getCodigoAsunto() {
        return codigoAsunto;
    }


    /**
     * Sets the codigoAsunto value for this AsientoRegistralWs.
     * 
     * @param codigoAsunto
     */
    public void setCodigoAsunto(java.lang.String codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
    }


    /**
     * Gets the codigoAsuntoDenominacion value for this AsientoRegistralWs.
     * 
     * @return codigoAsuntoDenominacion
     */
    public java.lang.String getCodigoAsuntoDenominacion() {
        return codigoAsuntoDenominacion;
    }


    /**
     * Sets the codigoAsuntoDenominacion value for this AsientoRegistralWs.
     * 
     * @param codigoAsuntoDenominacion
     */
    public void setCodigoAsuntoDenominacion(java.lang.String codigoAsuntoDenominacion) {
        this.codigoAsuntoDenominacion = codigoAsuntoDenominacion;
    }


    /**
     * Gets the codigoError value for this AsientoRegistralWs.
     * 
     * @return codigoError
     */
    public java.lang.String getCodigoError() {
        return codigoError;
    }


    /**
     * Sets the codigoError value for this AsientoRegistralWs.
     * 
     * @param codigoError
     */
    public void setCodigoError(java.lang.String codigoError) {
        this.codigoError = codigoError;
    }


    /**
     * Gets the codigoSia value for this AsientoRegistralWs.
     * 
     * @return codigoSia
     */
    public java.lang.Long getCodigoSia() {
        return codigoSia;
    }


    /**
     * Sets the codigoSia value for this AsientoRegistralWs.
     * 
     * @param codigoSia
     */
    public void setCodigoSia(java.lang.Long codigoSia) {
        this.codigoSia = codigoSia;
    }


    /**
     * Gets the codigoUsuario value for this AsientoRegistralWs.
     * 
     * @return codigoUsuario
     */
    public java.lang.String getCodigoUsuario() {
        return codigoUsuario;
    }


    /**
     * Sets the codigoUsuario value for this AsientoRegistralWs.
     * 
     * @param codigoUsuario
     */
    public void setCodigoUsuario(java.lang.String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }


    /**
     * Gets the descripcionError value for this AsientoRegistralWs.
     * 
     * @return descripcionError
     */
    public java.lang.String getDescripcionError() {
        return descripcionError;
    }


    /**
     * Sets the descripcionError value for this AsientoRegistralWs.
     * 
     * @param descripcionError
     */
    public void setDescripcionError(java.lang.String descripcionError) {
        this.descripcionError = descripcionError;
    }


    /**
     * Gets the entidadCodigo value for this AsientoRegistralWs.
     * 
     * @return entidadCodigo
     */
    public java.lang.String getEntidadCodigo() {
        return entidadCodigo;
    }


    /**
     * Sets the entidadCodigo value for this AsientoRegistralWs.
     * 
     * @param entidadCodigo
     */
    public void setEntidadCodigo(java.lang.String entidadCodigo) {
        this.entidadCodigo = entidadCodigo;
    }


    /**
     * Gets the entidadDenominacion value for this AsientoRegistralWs.
     * 
     * @return entidadDenominacion
     */
    public java.lang.String getEntidadDenominacion() {
        return entidadDenominacion;
    }


    /**
     * Sets the entidadDenominacion value for this AsientoRegistralWs.
     * 
     * @param entidadDenominacion
     */
    public void setEntidadDenominacion(java.lang.String entidadDenominacion) {
        this.entidadDenominacion = entidadDenominacion;
    }


    /**
     * Gets the entidadRegistralDestinoCodigo value for this AsientoRegistralWs.
     * 
     * @return entidadRegistralDestinoCodigo
     */
    public java.lang.String getEntidadRegistralDestinoCodigo() {
        return entidadRegistralDestinoCodigo;
    }


    /**
     * Sets the entidadRegistralDestinoCodigo value for this AsientoRegistralWs.
     * 
     * @param entidadRegistralDestinoCodigo
     */
    public void setEntidadRegistralDestinoCodigo(java.lang.String entidadRegistralDestinoCodigo) {
        this.entidadRegistralDestinoCodigo = entidadRegistralDestinoCodigo;
    }


    /**
     * Gets the entidadRegistralDestinoDenominacion value for this AsientoRegistralWs.
     * 
     * @return entidadRegistralDestinoDenominacion
     */
    public java.lang.String getEntidadRegistralDestinoDenominacion() {
        return entidadRegistralDestinoDenominacion;
    }


    /**
     * Sets the entidadRegistralDestinoDenominacion value for this AsientoRegistralWs.
     * 
     * @param entidadRegistralDestinoDenominacion
     */
    public void setEntidadRegistralDestinoDenominacion(java.lang.String entidadRegistralDestinoDenominacion) {
        this.entidadRegistralDestinoDenominacion = entidadRegistralDestinoDenominacion;
    }


    /**
     * Gets the entidadRegistralInicioCodigo value for this AsientoRegistralWs.
     * 
     * @return entidadRegistralInicioCodigo
     */
    public java.lang.String getEntidadRegistralInicioCodigo() {
        return entidadRegistralInicioCodigo;
    }


    /**
     * Sets the entidadRegistralInicioCodigo value for this AsientoRegistralWs.
     * 
     * @param entidadRegistralInicioCodigo
     */
    public void setEntidadRegistralInicioCodigo(java.lang.String entidadRegistralInicioCodigo) {
        this.entidadRegistralInicioCodigo = entidadRegistralInicioCodigo;
    }


    /**
     * Gets the entidadRegistralInicioDenominacion value for this AsientoRegistralWs.
     * 
     * @return entidadRegistralInicioDenominacion
     */
    public java.lang.String getEntidadRegistralInicioDenominacion() {
        return entidadRegistralInicioDenominacion;
    }


    /**
     * Sets the entidadRegistralInicioDenominacion value for this AsientoRegistralWs.
     * 
     * @param entidadRegistralInicioDenominacion
     */
    public void setEntidadRegistralInicioDenominacion(java.lang.String entidadRegistralInicioDenominacion) {
        this.entidadRegistralInicioDenominacion = entidadRegistralInicioDenominacion;
    }


    /**
     * Gets the entidadRegistralOrigenCodigo value for this AsientoRegistralWs.
     * 
     * @return entidadRegistralOrigenCodigo
     */
    public java.lang.String getEntidadRegistralOrigenCodigo() {
        return entidadRegistralOrigenCodigo;
    }


    /**
     * Sets the entidadRegistralOrigenCodigo value for this AsientoRegistralWs.
     * 
     * @param entidadRegistralOrigenCodigo
     */
    public void setEntidadRegistralOrigenCodigo(java.lang.String entidadRegistralOrigenCodigo) {
        this.entidadRegistralOrigenCodigo = entidadRegistralOrigenCodigo;
    }


    /**
     * Gets the entidadRegistralOrigenDenominacion value for this AsientoRegistralWs.
     * 
     * @return entidadRegistralOrigenDenominacion
     */
    public java.lang.String getEntidadRegistralOrigenDenominacion() {
        return entidadRegistralOrigenDenominacion;
    }


    /**
     * Sets the entidadRegistralOrigenDenominacion value for this AsientoRegistralWs.
     * 
     * @param entidadRegistralOrigenDenominacion
     */
    public void setEntidadRegistralOrigenDenominacion(java.lang.String entidadRegistralOrigenDenominacion) {
        this.entidadRegistralOrigenDenominacion = entidadRegistralOrigenDenominacion;
    }


    /**
     * Gets the estado value for this AsientoRegistralWs.
     * 
     * @return estado
     */
    public java.lang.Long getEstado() {
        return estado;
    }


    /**
     * Sets the estado value for this AsientoRegistralWs.
     * 
     * @param estado
     */
    public void setEstado(java.lang.Long estado) {
        this.estado = estado;
    }


    /**
     * Gets the expone value for this AsientoRegistralWs.
     * 
     * @return expone
     */
    public java.lang.String getExpone() {
        return expone;
    }


    /**
     * Sets the expone value for this AsientoRegistralWs.
     * 
     * @param expone
     */
    public void setExpone(java.lang.String expone) {
        this.expone = expone;
    }


    /**
     * Gets the fechaRecepcion value for this AsientoRegistralWs.
     * 
     * @return fechaRecepcion
     */
    public java.util.Calendar getFechaRecepcion() {
        return fechaRecepcion;
    }


    /**
     * Sets the fechaRecepcion value for this AsientoRegistralWs.
     * 
     * @param fechaRecepcion
     */
    public void setFechaRecepcion(java.util.Calendar fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }


    /**
     * Gets the fechaRegistro value for this AsientoRegistralWs.
     * 
     * @return fechaRegistro
     */
    public java.util.Calendar getFechaRegistro() {
        return fechaRegistro;
    }


    /**
     * Sets the fechaRegistro value for this AsientoRegistralWs.
     * 
     * @param fechaRegistro
     */
    public void setFechaRegistro(java.util.Calendar fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    /**
     * Gets the fechaRegistroDestino value for this AsientoRegistralWs.
     * 
     * @return fechaRegistroDestino
     */
    public java.util.Calendar getFechaRegistroDestino() {
        return fechaRegistroDestino;
    }


    /**
     * Sets the fechaRegistroDestino value for this AsientoRegistralWs.
     * 
     * @param fechaRegistroDestino
     */
    public void setFechaRegistroDestino(java.util.Calendar fechaRegistroDestino) {
        this.fechaRegistroDestino = fechaRegistroDestino;
    }


    /**
     * Gets the id value for this AsientoRegistralWs.
     * 
     * @return id
     */
    public java.lang.Long getId() {
        return id;
    }


    /**
     * Sets the id value for this AsientoRegistralWs.
     * 
     * @param id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }


    /**
     * Gets the identificadorIntercambio value for this AsientoRegistralWs.
     * 
     * @return identificadorIntercambio
     */
    public java.lang.String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }


    /**
     * Sets the identificadorIntercambio value for this AsientoRegistralWs.
     * 
     * @param identificadorIntercambio
     */
    public void setIdentificadorIntercambio(java.lang.String identificadorIntercambio) {
        this.identificadorIntercambio = identificadorIntercambio;
    }


    /**
     * Gets the idioma value for this AsientoRegistralWs.
     * 
     * @return idioma
     */
    public java.lang.Long getIdioma() {
        return idioma;
    }


    /**
     * Sets the idioma value for this AsientoRegistralWs.
     * 
     * @param idioma
     */
    public void setIdioma(java.lang.Long idioma) {
        this.idioma = idioma;
    }


    /**
     * Gets the interesados value for this AsientoRegistralWs.
     * 
     * @return interesados
     */
    public es.caib.regweb3.ws.v3.apiaxis.InteresadoWs[] getInteresados() {
        return interesados;
    }


    /**
     * Sets the interesados value for this AsientoRegistralWs.
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
     * Gets the libroCodigo value for this AsientoRegistralWs.
     * 
     * @return libroCodigo
     */
    public java.lang.String getLibroCodigo() {
        return libroCodigo;
    }


    /**
     * Sets the libroCodigo value for this AsientoRegistralWs.
     * 
     * @param libroCodigo
     */
    public void setLibroCodigo(java.lang.String libroCodigo) {
        this.libroCodigo = libroCodigo;
    }


    /**
     * Gets the motivo value for this AsientoRegistralWs.
     * 
     * @return motivo
     */
    public java.lang.String getMotivo() {
        return motivo;
    }


    /**
     * Sets the motivo value for this AsientoRegistralWs.
     * 
     * @param motivo
     */
    public void setMotivo(java.lang.String motivo) {
        this.motivo = motivo;
    }


    /**
     * Gets the numeroExpediente value for this AsientoRegistralWs.
     * 
     * @return numeroExpediente
     */
    public java.lang.String getNumeroExpediente() {
        return numeroExpediente;
    }


    /**
     * Sets the numeroExpediente value for this AsientoRegistralWs.
     * 
     * @param numeroExpediente
     */
    public void setNumeroExpediente(java.lang.String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }


    /**
     * Gets the numeroRegistro value for this AsientoRegistralWs.
     * 
     * @return numeroRegistro
     */
    public int getNumeroRegistro() {
        return numeroRegistro;
    }


    /**
     * Sets the numeroRegistro value for this AsientoRegistralWs.
     * 
     * @param numeroRegistro
     */
    public void setNumeroRegistro(int numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }


    /**
     * Gets the numeroRegistroDestino value for this AsientoRegistralWs.
     * 
     * @return numeroRegistroDestino
     */
    public java.lang.String getNumeroRegistroDestino() {
        return numeroRegistroDestino;
    }


    /**
     * Sets the numeroRegistroDestino value for this AsientoRegistralWs.
     * 
     * @param numeroRegistroDestino
     */
    public void setNumeroRegistroDestino(java.lang.String numeroRegistroDestino) {
        this.numeroRegistroDestino = numeroRegistroDestino;
    }


    /**
     * Gets the numeroRegistroFormateado value for this AsientoRegistralWs.
     * 
     * @return numeroRegistroFormateado
     */
    public java.lang.String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }


    /**
     * Sets the numeroRegistroFormateado value for this AsientoRegistralWs.
     * 
     * @param numeroRegistroFormateado
     */
    public void setNumeroRegistroFormateado(java.lang.String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }


    /**
     * Gets the numeroTransporte value for this AsientoRegistralWs.
     * 
     * @return numeroTransporte
     */
    public java.lang.String getNumeroTransporte() {
        return numeroTransporte;
    }


    /**
     * Sets the numeroTransporte value for this AsientoRegistralWs.
     * 
     * @param numeroTransporte
     */
    public void setNumeroTransporte(java.lang.String numeroTransporte) {
        this.numeroTransporte = numeroTransporte;
    }


    /**
     * Gets the observaciones value for this AsientoRegistralWs.
     * 
     * @return observaciones
     */
    public java.lang.String getObservaciones() {
        return observaciones;
    }


    /**
     * Sets the observaciones value for this AsientoRegistralWs.
     * 
     * @param observaciones
     */
    public void setObservaciones(java.lang.String observaciones) {
        this.observaciones = observaciones;
    }


    /**
     * Gets the presencial value for this AsientoRegistralWs.
     * 
     * @return presencial
     */
    public java.lang.Boolean getPresencial() {
        return presencial;
    }


    /**
     * Sets the presencial value for this AsientoRegistralWs.
     * 
     * @param presencial
     */
    public void setPresencial(java.lang.Boolean presencial) {
        this.presencial = presencial;
    }


    /**
     * Gets the referenciaExterna value for this AsientoRegistralWs.
     * 
     * @return referenciaExterna
     */
    public java.lang.String getReferenciaExterna() {
        return referenciaExterna;
    }


    /**
     * Sets the referenciaExterna value for this AsientoRegistralWs.
     * 
     * @param referenciaExterna
     */
    public void setReferenciaExterna(java.lang.String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }


    /**
     * Gets the resumen value for this AsientoRegistralWs.
     * 
     * @return resumen
     */
    public java.lang.String getResumen() {
        return resumen;
    }


    /**
     * Sets the resumen value for this AsientoRegistralWs.
     * 
     * @param resumen
     */
    public void setResumen(java.lang.String resumen) {
        this.resumen = resumen;
    }


    /**
     * Gets the solicita value for this AsientoRegistralWs.
     * 
     * @return solicita
     */
    public java.lang.String getSolicita() {
        return solicita;
    }


    /**
     * Sets the solicita value for this AsientoRegistralWs.
     * 
     * @param solicita
     */
    public void setSolicita(java.lang.String solicita) {
        this.solicita = solicita;
    }


    /**
     * Gets the tipoDocumentacionFisicaCodigo value for this AsientoRegistralWs.
     * 
     * @return tipoDocumentacionFisicaCodigo
     */
    public java.lang.Long getTipoDocumentacionFisicaCodigo() {
        return tipoDocumentacionFisicaCodigo;
    }


    /**
     * Sets the tipoDocumentacionFisicaCodigo value for this AsientoRegistralWs.
     * 
     * @param tipoDocumentacionFisicaCodigo
     */
    public void setTipoDocumentacionFisicaCodigo(java.lang.Long tipoDocumentacionFisicaCodigo) {
        this.tipoDocumentacionFisicaCodigo = tipoDocumentacionFisicaCodigo;
    }


    /**
     * Gets the tipoEnvioDocumentacion value for this AsientoRegistralWs.
     * 
     * @return tipoEnvioDocumentacion
     */
    public java.lang.String getTipoEnvioDocumentacion() {
        return tipoEnvioDocumentacion;
    }


    /**
     * Sets the tipoEnvioDocumentacion value for this AsientoRegistralWs.
     * 
     * @param tipoEnvioDocumentacion
     */
    public void setTipoEnvioDocumentacion(java.lang.String tipoEnvioDocumentacion) {
        this.tipoEnvioDocumentacion = tipoEnvioDocumentacion;
    }


    /**
     * Gets the tipoRegistro value for this AsientoRegistralWs.
     * 
     * @return tipoRegistro
     */
    public java.lang.Long getTipoRegistro() {
        return tipoRegistro;
    }


    /**
     * Sets the tipoRegistro value for this AsientoRegistralWs.
     * 
     * @param tipoRegistro
     */
    public void setTipoRegistro(java.lang.Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }


    /**
     * Gets the tipoTransporte value for this AsientoRegistralWs.
     * 
     * @return tipoTransporte
     */
    public java.lang.String getTipoTransporte() {
        return tipoTransporte;
    }


    /**
     * Sets the tipoTransporte value for this AsientoRegistralWs.
     * 
     * @param tipoTransporte
     */
    public void setTipoTransporte(java.lang.String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }


    /**
     * Gets the unidadTramitacionDestinoCodigo value for this AsientoRegistralWs.
     * 
     * @return unidadTramitacionDestinoCodigo
     */
    public java.lang.String getUnidadTramitacionDestinoCodigo() {
        return unidadTramitacionDestinoCodigo;
    }


    /**
     * Sets the unidadTramitacionDestinoCodigo value for this AsientoRegistralWs.
     * 
     * @param unidadTramitacionDestinoCodigo
     */
    public void setUnidadTramitacionDestinoCodigo(java.lang.String unidadTramitacionDestinoCodigo) {
        this.unidadTramitacionDestinoCodigo = unidadTramitacionDestinoCodigo;
    }


    /**
     * Gets the unidadTramitacionDestinoDenominacion value for this AsientoRegistralWs.
     * 
     * @return unidadTramitacionDestinoDenominacion
     */
    public java.lang.String getUnidadTramitacionDestinoDenominacion() {
        return unidadTramitacionDestinoDenominacion;
    }


    /**
     * Sets the unidadTramitacionDestinoDenominacion value for this AsientoRegistralWs.
     * 
     * @param unidadTramitacionDestinoDenominacion
     */
    public void setUnidadTramitacionDestinoDenominacion(java.lang.String unidadTramitacionDestinoDenominacion) {
        this.unidadTramitacionDestinoDenominacion = unidadTramitacionDestinoDenominacion;
    }


    /**
     * Gets the unidadTramitacionOrigenCodigo value for this AsientoRegistralWs.
     * 
     * @return unidadTramitacionOrigenCodigo
     */
    public java.lang.String getUnidadTramitacionOrigenCodigo() {
        return unidadTramitacionOrigenCodigo;
    }


    /**
     * Sets the unidadTramitacionOrigenCodigo value for this AsientoRegistralWs.
     * 
     * @param unidadTramitacionOrigenCodigo
     */
    public void setUnidadTramitacionOrigenCodigo(java.lang.String unidadTramitacionOrigenCodigo) {
        this.unidadTramitacionOrigenCodigo = unidadTramitacionOrigenCodigo;
    }


    /**
     * Gets the unidadTramitacionOrigenDenominacion value for this AsientoRegistralWs.
     * 
     * @return unidadTramitacionOrigenDenominacion
     */
    public java.lang.String getUnidadTramitacionOrigenDenominacion() {
        return unidadTramitacionOrigenDenominacion;
    }


    /**
     * Sets the unidadTramitacionOrigenDenominacion value for this AsientoRegistralWs.
     * 
     * @param unidadTramitacionOrigenDenominacion
     */
    public void setUnidadTramitacionOrigenDenominacion(java.lang.String unidadTramitacionOrigenDenominacion) {
        this.unidadTramitacionOrigenDenominacion = unidadTramitacionOrigenDenominacion;
    }


    /**
     * Gets the version value for this AsientoRegistralWs.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this AsientoRegistralWs.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AsientoRegistralWs)) return false;
        AsientoRegistralWs other = (AsientoRegistralWs) obj;
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
            ((this.aplicacionTelematica==null && other.getAplicacionTelematica()==null) || 
             (this.aplicacionTelematica!=null &&
              this.aplicacionTelematica.equals(other.getAplicacionTelematica()))) &&
            ((this.codigoAsunto==null && other.getCodigoAsunto()==null) || 
             (this.codigoAsunto!=null &&
              this.codigoAsunto.equals(other.getCodigoAsunto()))) &&
            ((this.codigoAsuntoDenominacion==null && other.getCodigoAsuntoDenominacion()==null) || 
             (this.codigoAsuntoDenominacion!=null &&
              this.codigoAsuntoDenominacion.equals(other.getCodigoAsuntoDenominacion()))) &&
            ((this.codigoError==null && other.getCodigoError()==null) || 
             (this.codigoError!=null &&
              this.codigoError.equals(other.getCodigoError()))) &&
            ((this.codigoSia==null && other.getCodigoSia()==null) || 
             (this.codigoSia!=null &&
              this.codigoSia.equals(other.getCodigoSia()))) &&
            ((this.codigoUsuario==null && other.getCodigoUsuario()==null) || 
             (this.codigoUsuario!=null &&
              this.codigoUsuario.equals(other.getCodigoUsuario()))) &&
            ((this.descripcionError==null && other.getDescripcionError()==null) || 
             (this.descripcionError!=null &&
              this.descripcionError.equals(other.getDescripcionError()))) &&
            ((this.entidadCodigo==null && other.getEntidadCodigo()==null) || 
             (this.entidadCodigo!=null &&
              this.entidadCodigo.equals(other.getEntidadCodigo()))) &&
            ((this.entidadDenominacion==null && other.getEntidadDenominacion()==null) || 
             (this.entidadDenominacion!=null &&
              this.entidadDenominacion.equals(other.getEntidadDenominacion()))) &&
            ((this.entidadRegistralDestinoCodigo==null && other.getEntidadRegistralDestinoCodigo()==null) || 
             (this.entidadRegistralDestinoCodigo!=null &&
              this.entidadRegistralDestinoCodigo.equals(other.getEntidadRegistralDestinoCodigo()))) &&
            ((this.entidadRegistralDestinoDenominacion==null && other.getEntidadRegistralDestinoDenominacion()==null) || 
             (this.entidadRegistralDestinoDenominacion!=null &&
              this.entidadRegistralDestinoDenominacion.equals(other.getEntidadRegistralDestinoDenominacion()))) &&
            ((this.entidadRegistralInicioCodigo==null && other.getEntidadRegistralInicioCodigo()==null) || 
             (this.entidadRegistralInicioCodigo!=null &&
              this.entidadRegistralInicioCodigo.equals(other.getEntidadRegistralInicioCodigo()))) &&
            ((this.entidadRegistralInicioDenominacion==null && other.getEntidadRegistralInicioDenominacion()==null) || 
             (this.entidadRegistralInicioDenominacion!=null &&
              this.entidadRegistralInicioDenominacion.equals(other.getEntidadRegistralInicioDenominacion()))) &&
            ((this.entidadRegistralOrigenCodigo==null && other.getEntidadRegistralOrigenCodigo()==null) || 
             (this.entidadRegistralOrigenCodigo!=null &&
              this.entidadRegistralOrigenCodigo.equals(other.getEntidadRegistralOrigenCodigo()))) &&
            ((this.entidadRegistralOrigenDenominacion==null && other.getEntidadRegistralOrigenDenominacion()==null) || 
             (this.entidadRegistralOrigenDenominacion!=null &&
              this.entidadRegistralOrigenDenominacion.equals(other.getEntidadRegistralOrigenDenominacion()))) &&
            ((this.estado==null && other.getEstado()==null) || 
             (this.estado!=null &&
              this.estado.equals(other.getEstado()))) &&
            ((this.expone==null && other.getExpone()==null) || 
             (this.expone!=null &&
              this.expone.equals(other.getExpone()))) &&
            ((this.fechaRecepcion==null && other.getFechaRecepcion()==null) || 
             (this.fechaRecepcion!=null &&
              this.fechaRecepcion.equals(other.getFechaRecepcion()))) &&
            ((this.fechaRegistro==null && other.getFechaRegistro()==null) || 
             (this.fechaRegistro!=null &&
              this.fechaRegistro.equals(other.getFechaRegistro()))) &&
            ((this.fechaRegistroDestino==null && other.getFechaRegistroDestino()==null) || 
             (this.fechaRegistroDestino!=null &&
              this.fechaRegistroDestino.equals(other.getFechaRegistroDestino()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.identificadorIntercambio==null && other.getIdentificadorIntercambio()==null) || 
             (this.identificadorIntercambio!=null &&
              this.identificadorIntercambio.equals(other.getIdentificadorIntercambio()))) &&
            ((this.idioma==null && other.getIdioma()==null) || 
             (this.idioma!=null &&
              this.idioma.equals(other.getIdioma()))) &&
            ((this.interesados==null && other.getInteresados()==null) || 
             (this.interesados!=null &&
              java.util.Arrays.equals(this.interesados, other.getInteresados()))) &&
            ((this.libroCodigo==null && other.getLibroCodigo()==null) || 
             (this.libroCodigo!=null &&
              this.libroCodigo.equals(other.getLibroCodigo()))) &&
            ((this.motivo==null && other.getMotivo()==null) || 
             (this.motivo!=null &&
              this.motivo.equals(other.getMotivo()))) &&
            ((this.numeroExpediente==null && other.getNumeroExpediente()==null) || 
             (this.numeroExpediente!=null &&
              this.numeroExpediente.equals(other.getNumeroExpediente()))) &&
            this.numeroRegistro == other.getNumeroRegistro() &&
            ((this.numeroRegistroDestino==null && other.getNumeroRegistroDestino()==null) || 
             (this.numeroRegistroDestino!=null &&
              this.numeroRegistroDestino.equals(other.getNumeroRegistroDestino()))) &&
            ((this.numeroRegistroFormateado==null && other.getNumeroRegistroFormateado()==null) || 
             (this.numeroRegistroFormateado!=null &&
              this.numeroRegistroFormateado.equals(other.getNumeroRegistroFormateado()))) &&
            ((this.numeroTransporte==null && other.getNumeroTransporte()==null) || 
             (this.numeroTransporte!=null &&
              this.numeroTransporte.equals(other.getNumeroTransporte()))) &&
            ((this.observaciones==null && other.getObservaciones()==null) || 
             (this.observaciones!=null &&
              this.observaciones.equals(other.getObservaciones()))) &&
            ((this.presencial==null && other.getPresencial()==null) || 
             (this.presencial!=null &&
              this.presencial.equals(other.getPresencial()))) &&
            ((this.referenciaExterna==null && other.getReferenciaExterna()==null) || 
             (this.referenciaExterna!=null &&
              this.referenciaExterna.equals(other.getReferenciaExterna()))) &&
            ((this.resumen==null && other.getResumen()==null) || 
             (this.resumen!=null &&
              this.resumen.equals(other.getResumen()))) &&
            ((this.solicita==null && other.getSolicita()==null) || 
             (this.solicita!=null &&
              this.solicita.equals(other.getSolicita()))) &&
            ((this.tipoDocumentacionFisicaCodigo==null && other.getTipoDocumentacionFisicaCodigo()==null) || 
             (this.tipoDocumentacionFisicaCodigo!=null &&
              this.tipoDocumentacionFisicaCodigo.equals(other.getTipoDocumentacionFisicaCodigo()))) &&
            ((this.tipoEnvioDocumentacion==null && other.getTipoEnvioDocumentacion()==null) || 
             (this.tipoEnvioDocumentacion!=null &&
              this.tipoEnvioDocumentacion.equals(other.getTipoEnvioDocumentacion()))) &&
            ((this.tipoRegistro==null && other.getTipoRegistro()==null) || 
             (this.tipoRegistro!=null &&
              this.tipoRegistro.equals(other.getTipoRegistro()))) &&
            ((this.tipoTransporte==null && other.getTipoTransporte()==null) || 
             (this.tipoTransporte!=null &&
              this.tipoTransporte.equals(other.getTipoTransporte()))) &&
            ((this.unidadTramitacionDestinoCodigo==null && other.getUnidadTramitacionDestinoCodigo()==null) || 
             (this.unidadTramitacionDestinoCodigo!=null &&
              this.unidadTramitacionDestinoCodigo.equals(other.getUnidadTramitacionDestinoCodigo()))) &&
            ((this.unidadTramitacionDestinoDenominacion==null && other.getUnidadTramitacionDestinoDenominacion()==null) || 
             (this.unidadTramitacionDestinoDenominacion!=null &&
              this.unidadTramitacionDestinoDenominacion.equals(other.getUnidadTramitacionDestinoDenominacion()))) &&
            ((this.unidadTramitacionOrigenCodigo==null && other.getUnidadTramitacionOrigenCodigo()==null) || 
             (this.unidadTramitacionOrigenCodigo!=null &&
              this.unidadTramitacionOrigenCodigo.equals(other.getUnidadTramitacionOrigenCodigo()))) &&
            ((this.unidadTramitacionOrigenDenominacion==null && other.getUnidadTramitacionOrigenDenominacion()==null) || 
             (this.unidadTramitacionOrigenDenominacion!=null &&
              this.unidadTramitacionOrigenDenominacion.equals(other.getUnidadTramitacionOrigenDenominacion()))) &&
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
        if (getAplicacionTelematica() != null) {
            _hashCode += getAplicacionTelematica().hashCode();
        }
        if (getCodigoAsunto() != null) {
            _hashCode += getCodigoAsunto().hashCode();
        }
        if (getCodigoAsuntoDenominacion() != null) {
            _hashCode += getCodigoAsuntoDenominacion().hashCode();
        }
        if (getCodigoError() != null) {
            _hashCode += getCodigoError().hashCode();
        }
        if (getCodigoSia() != null) {
            _hashCode += getCodigoSia().hashCode();
        }
        if (getCodigoUsuario() != null) {
            _hashCode += getCodigoUsuario().hashCode();
        }
        if (getDescripcionError() != null) {
            _hashCode += getDescripcionError().hashCode();
        }
        if (getEntidadCodigo() != null) {
            _hashCode += getEntidadCodigo().hashCode();
        }
        if (getEntidadDenominacion() != null) {
            _hashCode += getEntidadDenominacion().hashCode();
        }
        if (getEntidadRegistralDestinoCodigo() != null) {
            _hashCode += getEntidadRegistralDestinoCodigo().hashCode();
        }
        if (getEntidadRegistralDestinoDenominacion() != null) {
            _hashCode += getEntidadRegistralDestinoDenominacion().hashCode();
        }
        if (getEntidadRegistralInicioCodigo() != null) {
            _hashCode += getEntidadRegistralInicioCodigo().hashCode();
        }
        if (getEntidadRegistralInicioDenominacion() != null) {
            _hashCode += getEntidadRegistralInicioDenominacion().hashCode();
        }
        if (getEntidadRegistralOrigenCodigo() != null) {
            _hashCode += getEntidadRegistralOrigenCodigo().hashCode();
        }
        if (getEntidadRegistralOrigenDenominacion() != null) {
            _hashCode += getEntidadRegistralOrigenDenominacion().hashCode();
        }
        if (getEstado() != null) {
            _hashCode += getEstado().hashCode();
        }
        if (getExpone() != null) {
            _hashCode += getExpone().hashCode();
        }
        if (getFechaRecepcion() != null) {
            _hashCode += getFechaRecepcion().hashCode();
        }
        if (getFechaRegistro() != null) {
            _hashCode += getFechaRegistro().hashCode();
        }
        if (getFechaRegistroDestino() != null) {
            _hashCode += getFechaRegistroDestino().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdentificadorIntercambio() != null) {
            _hashCode += getIdentificadorIntercambio().hashCode();
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
        if (getLibroCodigo() != null) {
            _hashCode += getLibroCodigo().hashCode();
        }
        if (getMotivo() != null) {
            _hashCode += getMotivo().hashCode();
        }
        if (getNumeroExpediente() != null) {
            _hashCode += getNumeroExpediente().hashCode();
        }
        _hashCode += getNumeroRegistro();
        if (getNumeroRegistroDestino() != null) {
            _hashCode += getNumeroRegistroDestino().hashCode();
        }
        if (getNumeroRegistroFormateado() != null) {
            _hashCode += getNumeroRegistroFormateado().hashCode();
        }
        if (getNumeroTransporte() != null) {
            _hashCode += getNumeroTransporte().hashCode();
        }
        if (getObservaciones() != null) {
            _hashCode += getObservaciones().hashCode();
        }
        if (getPresencial() != null) {
            _hashCode += getPresencial().hashCode();
        }
        if (getReferenciaExterna() != null) {
            _hashCode += getReferenciaExterna().hashCode();
        }
        if (getResumen() != null) {
            _hashCode += getResumen().hashCode();
        }
        if (getSolicita() != null) {
            _hashCode += getSolicita().hashCode();
        }
        if (getTipoDocumentacionFisicaCodigo() != null) {
            _hashCode += getTipoDocumentacionFisicaCodigo().hashCode();
        }
        if (getTipoEnvioDocumentacion() != null) {
            _hashCode += getTipoEnvioDocumentacion().hashCode();
        }
        if (getTipoRegistro() != null) {
            _hashCode += getTipoRegistro().hashCode();
        }
        if (getTipoTransporte() != null) {
            _hashCode += getTipoTransporte().hashCode();
        }
        if (getUnidadTramitacionDestinoCodigo() != null) {
            _hashCode += getUnidadTramitacionDestinoCodigo().hashCode();
        }
        if (getUnidadTramitacionDestinoDenominacion() != null) {
            _hashCode += getUnidadTramitacionDestinoDenominacion().hashCode();
        }
        if (getUnidadTramitacionOrigenCodigo() != null) {
            _hashCode += getUnidadTramitacionOrigenCodigo().hashCode();
        }
        if (getUnidadTramitacionOrigenDenominacion() != null) {
            _hashCode += getUnidadTramitacionOrigenDenominacion().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AsientoRegistralWs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://impl.v3.ws.regweb3.caib.es/", "asientoRegistralWs"));
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
        elemField.setFieldName("aplicacionTelematica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "aplicacionTelematica"));
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
        elemField.setFieldName("codigoAsuntoDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoAsuntoDenominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoError"));
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
        elemField.setFieldName("codigoUsuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codigoUsuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionError"));
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
        elemField.setFieldName("entidadRegistralDestinoCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadRegistralDestinoCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadRegistralDestinoDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadRegistralDestinoDenominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadRegistralInicioCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadRegistralInicioCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadRegistralInicioDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadRegistralInicioDenominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadRegistralOrigenCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadRegistralOrigenCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadRegistralOrigenDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadRegistralOrigenDenominacion"));
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
        elemField.setFieldName("fechaRecepcion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaRecepcion"));
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
        elemField.setFieldName("fechaRegistroDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fechaRegistroDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificadorIntercambio");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identificadorIntercambio"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("libroCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "libroCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motivo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroExpediente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroExpediente"));
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
        elemField.setFieldName("numeroRegistroDestino");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroRegistroDestino"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("numeroTransporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroTransporte"));
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
        elemField.setFieldName("presencial");
        elemField.setXmlName(new javax.xml.namespace.QName("", "presencial"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referenciaExterna");
        elemField.setXmlName(new javax.xml.namespace.QName("", "referenciaExterna"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resumen");
        elemField.setXmlName(new javax.xml.namespace.QName("", "resumen"));
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
        elemField.setFieldName("tipoDocumentacionFisicaCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoDocumentacionFisicaCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoEnvioDocumentacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoEnvioDocumentacion"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoTransporte");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipoTransporte"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadTramitacionDestinoCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidadTramitacionDestinoCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadTramitacionDestinoDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidadTramitacionDestinoDenominacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadTramitacionOrigenCodigo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidadTramitacionOrigenCodigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unidadTramitacionOrigenDenominacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "unidadTramitacionOrigenDenominacion"));
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
