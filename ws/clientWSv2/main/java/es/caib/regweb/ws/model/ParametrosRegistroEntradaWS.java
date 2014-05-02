/**
 * ParametrosRegistroEntradaWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.model;

import es.caib.regweb.ws.model.ListaErroresEntrada;

public class ParametrosRegistroEntradaWS  implements java.io.Serializable {
    private java.lang.String usuarioConexion;

    private java.lang.String password;
    
    private java.lang.String usuarioRegistro;

    private java.lang.Boolean actualizacion;

    private java.lang.String altres;

    private java.lang.String altresNuevo;

    private java.lang.String anoEntrada;

    private java.lang.String balears;

    private java.lang.String comentario;

    private java.lang.String comentarioNuevo;

    private java.lang.String correo;

    private java.lang.String data;

    private java.lang.String dataentrada;

    private java.lang.String dataVisado;

    private java.lang.String descripcionDocumento;

    private java.lang.String descripcionIdiomaDocumento;

    private java.lang.String descripcionMunicipi060;

    private java.lang.String descripcionOficina;

    private java.lang.String descripcionOficinaFisica;

    private java.lang.String descripcionOrganismoDestinatario;

    private java.lang.String descripcionRemitente;

    private java.lang.String destinatari;

    private java.lang.String disquet;

    private java.lang.String emailRemitent;

    private java.lang.String entidad1;

    private java.lang.String entidad1Grabada;

    private java.lang.String entidad1Nuevo;

    private java.lang.String entidad2;

    private java.lang.String entidad2Nuevo;

    private java.lang.String entidadCastellano;

    private java.lang.String fora;

    private java.lang.String hora;

    private java.lang.String idioex;

    private java.lang.String idioma;

    private java.lang.String idiomaExtracto;

    private java.lang.Boolean leido;

    private java.lang.String localitzadorsDocs;

    private java.lang.String motivo;

    private java.lang.String municipi060;

    private java.lang.String numeroDocumentosRegistro060;

    private java.lang.String numeroEntrada;

    private java.lang.String oficina;

    private java.lang.String oficinafisica;

    private java.lang.String origenRegistro;

    private ParametrosRegistroPublicadoEntradaWS paramRegPubEnt;

    private java.lang.String procedenciaGeografica;

    private java.lang.Boolean registroActualizado;

    private java.lang.String registroAnulado;

    private java.lang.Boolean registroGrabado;

    private java.lang.String salida1;

    private java.lang.String salida2;

    private java.lang.String tipo;

    private java.lang.Boolean validado;

    private ListaErroresEntrada errores;

    public ParametrosRegistroEntradaWS() {
    }

    public ParametrosRegistroEntradaWS(
           java.lang.String usuarioConexion,
           java.lang.String password,
           java.lang.String usuarioRegistro,
           java.lang.Boolean actualizacion,
           java.lang.String altres,
           java.lang.String altresNuevo,
           java.lang.String anoEntrada,
           java.lang.String balears,
           java.lang.String comentario,
           java.lang.String comentarioNuevo,
           java.lang.String correo,
           java.lang.String data,
           java.lang.String dataentrada,
           java.lang.String dataVisado,
           java.lang.String descripcionDocumento,
           java.lang.String descripcionIdiomaDocumento,
           java.lang.String descripcionMunicipi060,
           java.lang.String descripcionOficina,
           java.lang.String descripcionOficinaFisica,
           java.lang.String descripcionOrganismoDestinatario,
           java.lang.String descripcionRemitente,
           java.lang.String destinatari,
           java.lang.String disquet,
           java.lang.String emailRemitent,
           java.lang.String entidad1,
           java.lang.String entidad1Grabada,
           java.lang.String entidad1Nuevo,
           java.lang.String entidad2,
           java.lang.String entidad2Nuevo,
           java.lang.String entidadCastellano,
           java.lang.String fora,
           java.lang.String hora,
           java.lang.String idioex,
           java.lang.String idioma,
           java.lang.String idiomaExtracto,
           java.lang.Boolean leido,
           java.lang.String localitzadorsDocs,
           java.lang.String motivo,
           java.lang.String municipi060,
           java.lang.String numeroDocumentosRegistro060,
           java.lang.String numeroEntrada,
           java.lang.String oficina,
           java.lang.String oficinafisica,
           java.lang.String origenRegistro,
           ParametrosRegistroPublicadoEntradaWS paramRegPubEnt,
           java.lang.String procedenciaGeografica,
           java.lang.Boolean registroActualizado,
           java.lang.String registroAnulado,
           java.lang.Boolean registroGrabado,
           java.lang.String salida1,
           java.lang.String salida2,
           java.lang.String tipo,
           java.lang.Boolean validado,
           ListaErroresEntrada errores) {
           this.usuarioConexion = usuarioConexion;
           this.password = password;
           this.actualizacion = actualizacion;
           this.altres = altres;
           this.altresNuevo = altresNuevo;
           this.anoEntrada = anoEntrada;
           this.balears = balears;
           this.comentario = comentario;
           this.comentarioNuevo = comentarioNuevo;
           this.correo = correo;
           this.data = data;
           this.dataentrada = dataentrada;
           this.dataVisado = dataVisado;
           this.descripcionDocumento = descripcionDocumento;
           this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
           this.descripcionMunicipi060 = descripcionMunicipi060;
           this.descripcionOficina = descripcionOficina;
           this.descripcionOficinaFisica = descripcionOficinaFisica;
           this.descripcionOrganismoDestinatario = descripcionOrganismoDestinatario;
           this.descripcionRemitente = descripcionRemitente;
           this.destinatari = destinatari;
           this.disquet = disquet;
           this.emailRemitent = emailRemitent;
           this.entidad1 = entidad1;
           this.entidad1Grabada = entidad1Grabada;
           this.entidad1Nuevo = entidad1Nuevo;
           this.entidad2 = entidad2;
           this.entidad2Nuevo = entidad2Nuevo;
           this.entidadCastellano = entidadCastellano;
           this.fora = fora;
           this.hora = hora;
           this.idioex = idioex;
           this.idioma = idioma;
           this.idiomaExtracto = idiomaExtracto;
           this.leido = leido;
           this.localitzadorsDocs = localitzadorsDocs;
           this.motivo = motivo;
           this.municipi060 = municipi060;
           this.numeroDocumentosRegistro060 = numeroDocumentosRegistro060;
           this.numeroEntrada = numeroEntrada;
           this.oficina = oficina;
           this.oficinafisica = oficinafisica;
           this.origenRegistro = origenRegistro;
           this.paramRegPubEnt = paramRegPubEnt;
           this.procedenciaGeografica = procedenciaGeografica;
           this.registroActualizado = registroActualizado;
           this.registroAnulado = registroAnulado;
           this.registroGrabado = registroGrabado;
           this.salida1 = salida1;
           this.salida2 = salida2;
           this.tipo = tipo;
           this.validado = validado;
           this.errores = errores;
    }


    /**
     * Gets the usuario value for this ParametrosRegistroEntradaWS.
     * 
     * @return usuario
     */
    public java.lang.String getUsuarioConexion() {
        return usuarioConexion;
    }


    /**
     * Sets the usuario value for this ParametrosRegistroEntradaWS.
     * 
     * @param usuario
     */
    public void setUsuarioConexion(java.lang.String usuarioConexion) {
        this.usuarioConexion = usuarioConexion;
    }


    /**
     * Gets the password value for this ParametrosRegistroEntradaWS.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this ParametrosRegistroEntradaWS.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    
    /**
     * Gets the usuario value for this ParametrosRegistroEntradaWS.
     * 
     * @return usuarioRegistro
     */
    public java.lang.String getUsuarioRegistro() {
        return usuarioRegistro;
    }


    /**
     * Sets the usuario value for this ParametrosRegistroEntradaWS.
     * 
     * @param usuarioRegistro
     */
    public void setUsuarioRegistro(java.lang.String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }

    /**
     * Gets the actualizacion value for this ParametrosRegistroEntradaWS.
     * 
     * @return actualizacion
     */
    public java.lang.Boolean getActualizacion() {
        return actualizacion;
    }


    /**
     * Sets the actualizacion value for this ParametrosRegistroEntradaWS.
     * 
     * @param actualizacion
     */
    public void setActualizacion(java.lang.Boolean actualizacion) {
        this.actualizacion = actualizacion;
    }


    /**
     * Gets the altres value for this ParametrosRegistroEntradaWS.
     * 
     * @return altres
     */
    public java.lang.String getAltres() {
        return altres;
    }


    /**
     * Sets the altres value for this ParametrosRegistroEntradaWS.
     * 
     * @param altres
     */
    public void setAltres(java.lang.String altres) {
        this.altres = altres;
    }


    /**
     * Gets the altresNuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @return altresNuevo
     */
    public java.lang.String getAltresNuevo() {
        return altresNuevo;
    }


    /**
     * Sets the altresNuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @param altresNuevo
     */
    public void setAltresNuevo(java.lang.String altresNuevo) {
        this.altresNuevo = altresNuevo;
    }


    /**
     * Gets the anoEntrada value for this ParametrosRegistroEntradaWS.
     * 
     * @return anoEntrada
     */
    public java.lang.String getAnoEntrada() {
        return anoEntrada;
    }


    /**
     * Sets the anoEntrada value for this ParametrosRegistroEntradaWS.
     * 
     * @param anoEntrada
     */
    public void setAnoEntrada(java.lang.String anoEntrada) {
        this.anoEntrada = anoEntrada;
    }


    /**
     * Gets the balears value for this ParametrosRegistroEntradaWS.
     * 
     * @return balears
     */
    public java.lang.String getBalears() {
        return balears;
    }


    /**
     * Sets the balears value for this ParametrosRegistroEntradaWS.
     * 
     * @param balears
     */
    public void setBalears(java.lang.String balears) {
        this.balears = balears;
    }


    /**
     * Gets the comentario value for this ParametrosRegistroEntradaWS.
     * 
     * @return comentario
     */
    public java.lang.String getComentario() {
        return comentario;
    }


    /**
     * Sets the comentario value for this ParametrosRegistroEntradaWS.
     * 
     * @param comentario
     */
    public void setComentario(java.lang.String comentario) {
        this.comentario = comentario;
    }


    /**
     * Gets the comentarioNuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @return comentarioNuevo
     */
    public java.lang.String getComentarioNuevo() {
        return comentarioNuevo;
    }


    /**
     * Sets the comentarioNuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @param comentarioNuevo
     */
    public void setComentarioNuevo(java.lang.String comentarioNuevo) {
        this.comentarioNuevo = comentarioNuevo;
    }


    /**
     * Gets the correo value for this ParametrosRegistroEntradaWS.
     * 
     * @return correo
     */
    public java.lang.String getCorreo() {
        return correo;
    }


    /**
     * Sets the correo value for this ParametrosRegistroEntradaWS.
     * 
     * @param correo
     */
    public void setCorreo(java.lang.String correo) {
        this.correo = correo;
    }


    /**
     * Gets the data value for this ParametrosRegistroEntradaWS.
     * 
     * @return data
     */
    public java.lang.String getData() {
        return data;
    }


    /**
     * Sets the data value for this ParametrosRegistroEntradaWS.
     * 
     * @param data
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }


    /**
     * Gets the dataentrada value for this ParametrosRegistroEntradaWS.
     * 
     * @return dataentrada
     */
    public java.lang.String getDataentrada() {
        return dataentrada;
    }


    /**
     * Sets the dataentrada value for this ParametrosRegistroEntradaWS.
     * 
     * @param dataentrada
     */
    public void setDataentrada(java.lang.String dataentrada) {
        this.dataentrada = dataentrada;
    }


    /**
     * Gets the dataVisado value for this ParametrosRegistroEntradaWS.
     * 
     * @return dataVisado
     */
    public java.lang.String getDataVisado() {
        return dataVisado;
    }


    /**
     * Sets the dataVisado value for this ParametrosRegistroEntradaWS.
     * 
     * @param dataVisado
     */
    public void setDataVisado(java.lang.String dataVisado) {
        this.dataVisado = dataVisado;
    }


    /**
     * Gets the descripcionDocumento value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionDocumento
     */
    public java.lang.String getDescripcionDocumento() {
        return descripcionDocumento;
    }


    /**
     * Sets the descripcionDocumento value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionDocumento
     */
    public void setDescripcionDocumento(java.lang.String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }


    /**
     * Gets the descripcionIdiomaDocumento value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionIdiomaDocumento
     */
    public java.lang.String getDescripcionIdiomaDocumento() {
        return descripcionIdiomaDocumento;
    }


    /**
     * Sets the descripcionIdiomaDocumento value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionIdiomaDocumento
     */
    public void setDescripcionIdiomaDocumento(java.lang.String descripcionIdiomaDocumento) {
        this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
    }


    /**
     * Gets the descripcionMunicipi060 value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionMunicipi060
     */
    public java.lang.String getDescripcionMunicipi060() {
        return descripcionMunicipi060;
    }


    /**
     * Sets the descripcionMunicipi060 value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionMunicipi060
     */
    public void setDescripcionMunicipi060(java.lang.String descripcionMunicipi060) {
        this.descripcionMunicipi060 = descripcionMunicipi060;
    }


    /**
     * Gets the descripcionOficina value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionOficina
     */
    public java.lang.String getDescripcionOficina() {
        return descripcionOficina;
    }


    /**
     * Sets the descripcionOficina value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionOficina
     */
    public void setDescripcionOficina(java.lang.String descripcionOficina) {
        this.descripcionOficina = descripcionOficina;
    }


    /**
     * Gets the descripcionOficinaFisica value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionOficinaFisica
     */
    public java.lang.String getDescripcionOficinaFisica() {
        return descripcionOficinaFisica;
    }


    /**
     * Sets the descripcionOficinaFisica value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionOficinaFisica
     */
    public void setDescripcionOficinaFisica(java.lang.String descripcionOficinaFisica) {
        this.descripcionOficinaFisica = descripcionOficinaFisica;
    }


    /**
     * Gets the descripcionOrganismoDestinatario value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionOrganismoDestinatario
     */
    public java.lang.String getDescripcionOrganismoDestinatario() {
        return descripcionOrganismoDestinatario;
    }


    /**
     * Sets the descripcionOrganismoDestinatario value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionOrganismoDestinatario
     */
    public void setDescripcionOrganismoDestinatario(java.lang.String descripcionOrganismoDestinatario) {
        this.descripcionOrganismoDestinatario = descripcionOrganismoDestinatario;
    }


    /**
     * Gets the descripcionRemitente value for this ParametrosRegistroEntradaWS.
     * 
     * @return descripcionRemitente
     */
    public java.lang.String getDescripcionRemitente() {
        return descripcionRemitente;
    }


    /**
     * Sets the descripcionRemitente value for this ParametrosRegistroEntradaWS.
     * 
     * @param descripcionRemitente
     */
    public void setDescripcionRemitente(java.lang.String descripcionRemitente) {
        this.descripcionRemitente = descripcionRemitente;
    }


    /**
     * Gets the destinatari value for this ParametrosRegistroEntradaWS.
     * 
     * @return destinatari
     */
    public java.lang.String getDestinatari() {
        return destinatari;
    }


    /**
     * Sets the destinatari value for this ParametrosRegistroEntradaWS.
     * 
     * @param destinatari
     */
    public void setDestinatari(java.lang.String destinatari) {
        this.destinatari = destinatari;
    }


    /**
     * Gets the disquet value for this ParametrosRegistroEntradaWS.
     * 
     * @return disquet
     */
    public java.lang.String getDisquet() {
        return disquet;
    }


    /**
     * Sets the disquet value for this ParametrosRegistroEntradaWS.
     * 
     * @param disquet
     */
    public void setDisquet(java.lang.String disquet) {
        this.disquet = disquet;
    }


    /**
     * Gets the emailRemitent value for this ParametrosRegistroEntradaWS.
     * 
     * @return emailRemitent
     */
    public java.lang.String getEmailRemitent() {
        return emailRemitent;
    }


    /**
     * Sets the emailRemitent value for this ParametrosRegistroEntradaWS.
     * 
     * @param emailRemitent
     */
    public void setEmailRemitent(java.lang.String emailRemitent) {
        this.emailRemitent = emailRemitent;
    }


    /**
     * Gets the entidad1 value for this ParametrosRegistroEntradaWS.
     * 
     * @return entidad1
     */
    public java.lang.String getEntidad1() {
        return entidad1;
    }


    /**
     * Sets the entidad1 value for this ParametrosRegistroEntradaWS.
     * 
     * @param entidad1
     */
    public void setEntidad1(java.lang.String entidad1) {
        this.entidad1 = entidad1;
    }


    /**
     * Gets the entidad1Grabada value for this ParametrosRegistroEntradaWS.
     * 
     * @return entidad1Grabada
     */
    public java.lang.String getEntidad1Grabada() {
        return entidad1Grabada;
    }


    /**
     * Sets the entidad1Grabada value for this ParametrosRegistroEntradaWS.
     * 
     * @param entidad1Grabada
     */
    public void setEntidad1Grabada(java.lang.String entidad1Grabada) {
        this.entidad1Grabada = entidad1Grabada;
    }


    /**
     * Gets the entidad1Nuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @return entidad1Nuevo
     */
    public java.lang.String getEntidad1Nuevo() {
        return entidad1Nuevo;
    }


    /**
     * Sets the entidad1Nuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @param entidad1Nuevo
     */
    public void setEntidad1Nuevo(java.lang.String entidad1Nuevo) {
        this.entidad1Nuevo = entidad1Nuevo;
    }


    /**
     * Gets the entidad2 value for this ParametrosRegistroEntradaWS.
     * 
     * @return entidad2
     */
    public java.lang.String getEntidad2() {
        return entidad2;
    }


    /**
     * Sets the entidad2 value for this ParametrosRegistroEntradaWS.
     * 
     * @param entidad2
     */
    public void setEntidad2(java.lang.String entidad2) {
        this.entidad2 = entidad2;
    }


    /**
     * Gets the entidad2Nuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @return entidad2Nuevo
     */
    public java.lang.String getEntidad2Nuevo() {
        return entidad2Nuevo;
    }


    /**
     * Sets the entidad2Nuevo value for this ParametrosRegistroEntradaWS.
     * 
     * @param entidad2Nuevo
     */
    public void setEntidad2Nuevo(java.lang.String entidad2Nuevo) {
        this.entidad2Nuevo = entidad2Nuevo;
    }


    /**
     * Gets the entidadCastellano value for this ParametrosRegistroEntradaWS.
     * 
     * @return entidadCastellano
     */
    public java.lang.String getEntidadCastellano() {
        return entidadCastellano;
    }


    /**
     * Sets the entidadCastellano value for this ParametrosRegistroEntradaWS.
     * 
     * @param entidadCastellano
     */
    public void setEntidadCastellano(java.lang.String entidadCastellano) {
        this.entidadCastellano = entidadCastellano;
    }


    /**
     * Gets the fora value for this ParametrosRegistroEntradaWS.
     * 
     * @return fora
     */
    public java.lang.String getFora() {
        return fora;
    }


    /**
     * Sets the fora value for this ParametrosRegistroEntradaWS.
     * 
     * @param fora
     */
    public void setFora(java.lang.String fora) {
        this.fora = fora;
    }


    /**
     * Gets the hora value for this ParametrosRegistroEntradaWS.
     * 
     * @return hora
     */
    public java.lang.String getHora() {
        return hora;
    }


    /**
     * Sets the hora value for this ParametrosRegistroEntradaWS.
     * 
     * @param hora
     */
    public void setHora(java.lang.String hora) {
        this.hora = hora;
    }


    /**
     * Gets the idioex value for this ParametrosRegistroEntradaWS.
     * 
     * @return idioex
     */
    public java.lang.String getIdioex() {
        return idioex;
    }


    /**
     * Sets the idioex value for this ParametrosRegistroEntradaWS.
     * 
     * @param idioex
     */
    public void setIdioex(java.lang.String idioex) {
        this.idioex = idioex;
    }


    /**
     * Gets the idioma value for this ParametrosRegistroEntradaWS.
     * 
     * @return idioma
     */
    public java.lang.String getIdioma() {
        return idioma;
    }


    /**
     * Sets the idioma value for this ParametrosRegistroEntradaWS.
     * 
     * @param idioma
     */
    public void setIdioma(java.lang.String idioma) {
        this.idioma = idioma;
    }


    /**
     * Gets the idiomaExtracto value for this ParametrosRegistroEntradaWS.
     * 
     * @return idiomaExtracto
     */
    public java.lang.String getIdiomaExtracto() {
        return idiomaExtracto;
    }


    /**
     * Sets the idiomaExtracto value for this ParametrosRegistroEntradaWS.
     * 
     * @param idiomaExtracto
     */
    public void setIdiomaExtracto(java.lang.String idiomaExtracto) {
        this.idiomaExtracto = idiomaExtracto;
    }


    /**
     * Gets the leido value for this ParametrosRegistroEntradaWS.
     * 
     * @return leido
     */
    public java.lang.Boolean getLeido() {
        return leido;
    }


    /**
     * Sets the leido value for this ParametrosRegistroEntradaWS.
     * 
     * @param leido
     */
    public void setLeido(java.lang.Boolean leido) {
        this.leido = leido;
    }


    /**
     * Gets the localitzadorsDocs value for this ParametrosRegistroEntradaWS.
     * 
     * @return localitzadorsDocs
     */
    public java.lang.String getLocalitzadorsDocs() {
        return localitzadorsDocs;
    }


    /**
     * Sets the localitzadorsDocs value for this ParametrosRegistroEntradaWS.
     * 
     * @param localitzadorsDocs
     */
    public void setLocalitzadorsDocs(java.lang.String localitzadorsDocs) {
        this.localitzadorsDocs = localitzadorsDocs;
    }


    /**
     * Gets the motivo value for this ParametrosRegistroEntradaWS.
     * 
     * @return motivo
     */
    public java.lang.String getMotivo() {
        return motivo;
    }


    /**
     * Sets the motivo value for this ParametrosRegistroEntradaWS.
     * 
     * @param motivo
     */
    public void setMotivo(java.lang.String motivo) {
        this.motivo = motivo;
    }


    /**
     * Gets the municipi060 value for this ParametrosRegistroEntradaWS.
     * 
     * @return municipi060
     */
    public java.lang.String getMunicipi060() {
        return municipi060;
    }


    /**
     * Sets the municipi060 value for this ParametrosRegistroEntradaWS.
     * 
     * @param municipi060
     */
    public void setMunicipi060(java.lang.String municipi060) {
        this.municipi060 = municipi060;
    }


    /**
     * Gets the numeroDocumentosRegistro060 value for this ParametrosRegistroEntradaWS.
     * 
     * @return numeroDocumentosRegistro060
     */
    public java.lang.String getNumeroDocumentosRegistro060() {
        return numeroDocumentosRegistro060;
    }


    /**
     * Sets the numeroDocumentosRegistro060 value for this ParametrosRegistroEntradaWS.
     * 
     * @param numeroDocumentosRegistro060
     */
    public void setNumeroDocumentosRegistro060(java.lang.String numeroDocumentosRegistro060) {
        this.numeroDocumentosRegistro060 = numeroDocumentosRegistro060;
    }


    /**
     * Gets the numeroEntrada value for this ParametrosRegistroEntradaWS.
     * 
     * @return numeroEntrada
     */
    public java.lang.String getNumeroEntrada() {
        return numeroEntrada;
    }


    /**
     * Sets the numeroEntrada value for this ParametrosRegistroEntradaWS.
     * 
     * @param numeroEntrada
     */
    public void setNumeroEntrada(java.lang.String numeroEntrada) {
        this.numeroEntrada = numeroEntrada;
    }


    /**
     * Gets the oficina value for this ParametrosRegistroEntradaWS.
     * 
     * @return oficina
     */
    public java.lang.String getOficina() {
        return oficina;
    }


    /**
     * Sets the oficina value for this ParametrosRegistroEntradaWS.
     * 
     * @param oficina
     */
    public void setOficina(java.lang.String oficina) {
        this.oficina = oficina;
    }


    /**
     * Gets the oficinafisica value for this ParametrosRegistroEntradaWS.
     * 
     * @return oficinafisica
     */
    public java.lang.String getOficinafisica() {
        return oficinafisica;
    }


    /**
     * Sets the oficinafisica value for this ParametrosRegistroEntradaWS.
     * 
     * @param oficinafisica
     */
    public void setOficinafisica(java.lang.String oficinafisica) {
        this.oficinafisica = oficinafisica;
    }


    /**
     * Gets the origenRegistro value for this ParametrosRegistroEntradaWS.
     * 
     * @return origenRegistro
     */
    public java.lang.String getOrigenRegistro() {
        return origenRegistro;
    }


    /**
     * Sets the origenRegistro value for this ParametrosRegistroEntradaWS.
     * 
     * @param origenRegistro
     */
    public void setOrigenRegistro(java.lang.String origenRegistro) {
        this.origenRegistro = origenRegistro;
    }


    /**
     * Gets the paramRegPubEnt value for this ParametrosRegistroEntradaWS.
     * 
     * @return paramRegPubEnt
     */
    public ParametrosRegistroPublicadoEntradaWS getParamRegPubEnt() {
        return paramRegPubEnt;
    }


    /**
     * Sets the paramRegPubEnt value for this ParametrosRegistroEntradaWS.
     * 
     * @param paramRegPubEnt
     */
    public void setParamRegPubEnt(ParametrosRegistroPublicadoEntradaWS paramRegPubEnt) {
        this.paramRegPubEnt = paramRegPubEnt;
    }


    /**
     * Gets the procedenciaGeografica value for this ParametrosRegistroEntradaWS.
     * 
     * @return procedenciaGeografica
     */
    public java.lang.String getProcedenciaGeografica() {
        return procedenciaGeografica;
    }


    /**
     * Sets the procedenciaGeografica value for this ParametrosRegistroEntradaWS.
     * 
     * @param procedenciaGeografica
     */
    public void setProcedenciaGeografica(java.lang.String procedenciaGeografica) {
        this.procedenciaGeografica = procedenciaGeografica;
    }


    /**
     * Gets the registroActualizado value for this ParametrosRegistroEntradaWS.
     * 
     * @return registroActualizado
     */
    public java.lang.Boolean getRegistroActualizado() {
        return registroActualizado;
    }


    /**
     * Sets the registroActualizado value for this ParametrosRegistroEntradaWS.
     * 
     * @param registroActualizado
     */
    public void setRegistroActualizado(java.lang.Boolean registroActualizado) {
        this.registroActualizado = registroActualizado;
    }


    /**
     * Gets the registroAnulado value for this ParametrosRegistroEntradaWS.
     * 
     * @return registroAnulado
     */
    public java.lang.String getRegistroAnulado() {
        return registroAnulado;
    }


    /**
     * Sets the registroAnulado value for this ParametrosRegistroEntradaWS.
     * 
     * @param registroAnulado
     */
    public void setRegistroAnulado(java.lang.String registroAnulado) {
        this.registroAnulado = registroAnulado;
    }


    /**
     * Gets the registroGrabado value for this ParametrosRegistroEntradaWS.
     * 
     * @return registroGrabado
     */
    public java.lang.Boolean getRegistroGrabado() {
        return registroGrabado;
    }


    /**
     * Sets the registroGrabado value for this ParametrosRegistroEntradaWS.
     * 
     * @param registroGrabado
     */
    public void setRegistroGrabado(java.lang.Boolean registroGrabado) {
        this.registroGrabado = registroGrabado;
    }


    /**
     * Gets the salida1 value for this ParametrosRegistroEntradaWS.
     * 
     * @return salida1
     */
    public java.lang.String getSalida1() {
        return salida1;
    }


    /**
     * Sets the salida1 value for this ParametrosRegistroEntradaWS.
     * 
     * @param salida1
     */
    public void setSalida1(java.lang.String salida1) {
        this.salida1 = salida1;
    }


    /**
     * Gets the salida2 value for this ParametrosRegistroEntradaWS.
     * 
     * @return salida2
     */
    public java.lang.String getSalida2() {
        return salida2;
    }


    /**
     * Sets the salida2 value for this ParametrosRegistroEntradaWS.
     * 
     * @param salida2
     */
    public void setSalida2(java.lang.String salida2) {
        this.salida2 = salida2;
    }


    /**
     * Gets the tipo value for this ParametrosRegistroEntradaWS.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this ParametrosRegistroEntradaWS.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the validado value for this ParametrosRegistroEntradaWS.
     * 
     * @return validado
     */
    public java.lang.Boolean getValidado() {
        return validado;
    }


    /**
     * Sets the validado value for this ParametrosRegistroEntradaWS.
     * 
     * @param validado
     */
    public void setValidado(java.lang.Boolean validado) {
        this.validado = validado;
    }


    /**
     * Gets the errores value for this ParametrosRegistroEntradaWS.
     * 
     * @return errores
     */
    public ListaErroresEntrada getErrores() {
        return errores;
    }


    /**
     * Sets the errores value for this ParametrosRegistroEntradaWS.
     * 
     * @param errores
     */
    public void setErrores(ListaErroresEntrada errores) {
        this.errores = errores;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParametrosRegistroEntradaWS)) return false;
        ParametrosRegistroEntradaWS other = (ParametrosRegistroEntradaWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.usuarioConexion==null && other.getUsuarioConexion()==null) || 
             (this.usuarioConexion!=null &&
              this.usuarioConexion.equals(other.getUsuarioConexion()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
              		  ((this.usuarioRegistro==null && other.getPassword()==null) || 
                      (this.usuarioRegistro!=null &&
                       this.usuarioRegistro.equals(other.getPassword()))) &&
            ((this.actualizacion==null && other.getActualizacion()==null) || 
             (this.actualizacion!=null &&
              this.actualizacion.equals(other.getActualizacion()))) &&
            ((this.altres==null && other.getAltres()==null) || 
             (this.altres!=null &&
              this.altres.equals(other.getAltres()))) &&
            ((this.altresNuevo==null && other.getAltresNuevo()==null) || 
             (this.altresNuevo!=null &&
              this.altresNuevo.equals(other.getAltresNuevo()))) &&
            ((this.anoEntrada==null && other.getAnoEntrada()==null) || 
             (this.anoEntrada!=null &&
              this.anoEntrada.equals(other.getAnoEntrada()))) &&
            ((this.balears==null && other.getBalears()==null) || 
             (this.balears!=null &&
              this.balears.equals(other.getBalears()))) &&
            ((this.comentario==null && other.getComentario()==null) || 
             (this.comentario!=null &&
              this.comentario.equals(other.getComentario()))) &&
            ((this.comentarioNuevo==null && other.getComentarioNuevo()==null) || 
             (this.comentarioNuevo!=null &&
              this.comentarioNuevo.equals(other.getComentarioNuevo()))) &&
            ((this.correo==null && other.getCorreo()==null) || 
             (this.correo!=null &&
              this.correo.equals(other.getCorreo()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            ((this.dataentrada==null && other.getDataentrada()==null) || 
             (this.dataentrada!=null &&
              this.dataentrada.equals(other.getDataentrada()))) &&
            ((this.dataVisado==null && other.getDataVisado()==null) || 
             (this.dataVisado!=null &&
              this.dataVisado.equals(other.getDataVisado()))) &&
            ((this.descripcionDocumento==null && other.getDescripcionDocumento()==null) || 
             (this.descripcionDocumento!=null &&
              this.descripcionDocumento.equals(other.getDescripcionDocumento()))) &&
            ((this.descripcionIdiomaDocumento==null && other.getDescripcionIdiomaDocumento()==null) || 
             (this.descripcionIdiomaDocumento!=null &&
              this.descripcionIdiomaDocumento.equals(other.getDescripcionIdiomaDocumento()))) &&
            ((this.descripcionMunicipi060==null && other.getDescripcionMunicipi060()==null) || 
             (this.descripcionMunicipi060!=null &&
              this.descripcionMunicipi060.equals(other.getDescripcionMunicipi060()))) &&
            ((this.descripcionOficina==null && other.getDescripcionOficina()==null) || 
             (this.descripcionOficina!=null &&
              this.descripcionOficina.equals(other.getDescripcionOficina()))) &&
            ((this.descripcionOficinaFisica==null && other.getDescripcionOficinaFisica()==null) || 
             (this.descripcionOficinaFisica!=null &&
              this.descripcionOficinaFisica.equals(other.getDescripcionOficinaFisica()))) &&
            ((this.descripcionOrganismoDestinatario==null && other.getDescripcionOrganismoDestinatario()==null) || 
             (this.descripcionOrganismoDestinatario!=null &&
              this.descripcionOrganismoDestinatario.equals(other.getDescripcionOrganismoDestinatario()))) &&
            ((this.descripcionRemitente==null && other.getDescripcionRemitente()==null) || 
             (this.descripcionRemitente!=null &&
              this.descripcionRemitente.equals(other.getDescripcionRemitente()))) &&
            ((this.destinatari==null && other.getDestinatari()==null) || 
             (this.destinatari!=null &&
              this.destinatari.equals(other.getDestinatari()))) &&
            ((this.disquet==null && other.getDisquet()==null) || 
             (this.disquet!=null &&
              this.disquet.equals(other.getDisquet()))) &&
            ((this.emailRemitent==null && other.getEmailRemitent()==null) || 
             (this.emailRemitent!=null &&
              this.emailRemitent.equals(other.getEmailRemitent()))) &&
            ((this.entidad1==null && other.getEntidad1()==null) || 
             (this.entidad1!=null &&
              this.entidad1.equals(other.getEntidad1()))) &&
            ((this.entidad1Grabada==null && other.getEntidad1Grabada()==null) || 
             (this.entidad1Grabada!=null &&
              this.entidad1Grabada.equals(other.getEntidad1Grabada()))) &&
            ((this.entidad1Nuevo==null && other.getEntidad1Nuevo()==null) || 
             (this.entidad1Nuevo!=null &&
              this.entidad1Nuevo.equals(other.getEntidad1Nuevo()))) &&
            ((this.entidad2==null && other.getEntidad2()==null) || 
             (this.entidad2!=null &&
              this.entidad2.equals(other.getEntidad2()))) &&
            ((this.entidad2Nuevo==null && other.getEntidad2Nuevo()==null) || 
             (this.entidad2Nuevo!=null &&
              this.entidad2Nuevo.equals(other.getEntidad2Nuevo()))) &&
            ((this.entidadCastellano==null && other.getEntidadCastellano()==null) || 
             (this.entidadCastellano!=null &&
              this.entidadCastellano.equals(other.getEntidadCastellano()))) &&
            ((this.fora==null && other.getFora()==null) || 
             (this.fora!=null &&
              this.fora.equals(other.getFora()))) &&
            ((this.hora==null && other.getHora()==null) || 
             (this.hora!=null &&
              this.hora.equals(other.getHora()))) &&
            ((this.idioex==null && other.getIdioex()==null) || 
             (this.idioex!=null &&
              this.idioex.equals(other.getIdioex()))) &&
            ((this.idioma==null && other.getIdioma()==null) || 
             (this.idioma!=null &&
              this.idioma.equals(other.getIdioma()))) &&
            ((this.idiomaExtracto==null && other.getIdiomaExtracto()==null) || 
             (this.idiomaExtracto!=null &&
              this.idiomaExtracto.equals(other.getIdiomaExtracto()))) &&
            ((this.leido==null && other.getLeido()==null) || 
             (this.leido!=null &&
              this.leido.equals(other.getLeido()))) &&
            ((this.localitzadorsDocs==null && other.getLocalitzadorsDocs()==null) || 
             (this.localitzadorsDocs!=null &&
              this.localitzadorsDocs.equals(other.getLocalitzadorsDocs()))) &&
            ((this.motivo==null && other.getMotivo()==null) || 
             (this.motivo!=null &&
              this.motivo.equals(other.getMotivo()))) &&
            ((this.municipi060==null && other.getMunicipi060()==null) || 
             (this.municipi060!=null &&
              this.municipi060.equals(other.getMunicipi060()))) &&
            ((this.numeroDocumentosRegistro060==null && other.getNumeroDocumentosRegistro060()==null) || 
             (this.numeroDocumentosRegistro060!=null &&
              this.numeroDocumentosRegistro060.equals(other.getNumeroDocumentosRegistro060()))) &&
            ((this.numeroEntrada==null && other.getNumeroEntrada()==null) || 
             (this.numeroEntrada!=null &&
              this.numeroEntrada.equals(other.getNumeroEntrada()))) &&
            ((this.oficina==null && other.getOficina()==null) || 
             (this.oficina!=null &&
              this.oficina.equals(other.getOficina()))) &&
            ((this.oficinafisica==null && other.getOficinafisica()==null) || 
             (this.oficinafisica!=null &&
              this.oficinafisica.equals(other.getOficinafisica()))) &&
            ((this.origenRegistro==null && other.getOrigenRegistro()==null) || 
             (this.origenRegistro!=null &&
              this.origenRegistro.equals(other.getOrigenRegistro()))) &&
            ((this.paramRegPubEnt==null && other.getParamRegPubEnt()==null) || 
             (this.paramRegPubEnt!=null &&
              this.paramRegPubEnt.equals(other.getParamRegPubEnt()))) &&
            ((this.procedenciaGeografica==null && other.getProcedenciaGeografica()==null) || 
             (this.procedenciaGeografica!=null &&
              this.procedenciaGeografica.equals(other.getProcedenciaGeografica()))) &&
            ((this.registroActualizado==null && other.getRegistroActualizado()==null) || 
             (this.registroActualizado!=null &&
              this.registroActualizado.equals(other.getRegistroActualizado()))) &&
            ((this.registroAnulado==null && other.getRegistroAnulado()==null) || 
             (this.registroAnulado!=null &&
              this.registroAnulado.equals(other.getRegistroAnulado()))) &&
            ((this.registroGrabado==null && other.getRegistroGrabado()==null) || 
             (this.registroGrabado!=null &&
              this.registroGrabado.equals(other.getRegistroGrabado()))) &&
            ((this.salida1==null && other.getSalida1()==null) || 
             (this.salida1!=null &&
              this.salida1.equals(other.getSalida1()))) &&
            ((this.salida2==null && other.getSalida2()==null) || 
             (this.salida2!=null &&
              this.salida2.equals(other.getSalida2()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.validado==null && other.getValidado()==null) || 
             (this.validado!=null &&
              this.validado.equals(other.getValidado()))) &&
            ((this.errores==null && other.getErrores()==null) || 
             (this.errores!=null &&
              this.errores.equals(other.getErrores())));
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
        if (getUsuarioConexion() != null) {
            _hashCode += getUsuarioConexion().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getUsuarioRegistro() != null) {
            _hashCode += getUsuarioRegistro().hashCode();
        }
        if (getActualizacion() != null) {
            _hashCode += getActualizacion().hashCode();
        }
        if (getAltres() != null) {
            _hashCode += getAltres().hashCode();
        }
        if (getAltresNuevo() != null) {
            _hashCode += getAltresNuevo().hashCode();
        }
        if (getAnoEntrada() != null) {
            _hashCode += getAnoEntrada().hashCode();
        }
        if (getBalears() != null) {
            _hashCode += getBalears().hashCode();
        }
        if (getComentario() != null) {
            _hashCode += getComentario().hashCode();
        }
        if (getComentarioNuevo() != null) {
            _hashCode += getComentarioNuevo().hashCode();
        }
        if (getCorreo() != null) {
            _hashCode += getCorreo().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        if (getDataentrada() != null) {
            _hashCode += getDataentrada().hashCode();
        }
        if (getDataVisado() != null) {
            _hashCode += getDataVisado().hashCode();
        }
        if (getDescripcionDocumento() != null) {
            _hashCode += getDescripcionDocumento().hashCode();
        }
        if (getDescripcionIdiomaDocumento() != null) {
            _hashCode += getDescripcionIdiomaDocumento().hashCode();
        }
        if (getDescripcionMunicipi060() != null) {
            _hashCode += getDescripcionMunicipi060().hashCode();
        }
        if (getDescripcionOficina() != null) {
            _hashCode += getDescripcionOficina().hashCode();
        }
        if (getDescripcionOficinaFisica() != null) {
            _hashCode += getDescripcionOficinaFisica().hashCode();
        }
        if (getDescripcionOrganismoDestinatario() != null) {
            _hashCode += getDescripcionOrganismoDestinatario().hashCode();
        }
        if (getDescripcionRemitente() != null) {
            _hashCode += getDescripcionRemitente().hashCode();
        }
        if (getDestinatari() != null) {
            _hashCode += getDestinatari().hashCode();
        }
        if (getDisquet() != null) {
            _hashCode += getDisquet().hashCode();
        }
        if (getEmailRemitent() != null) {
            _hashCode += getEmailRemitent().hashCode();
        }
        if (getEntidad1() != null) {
            _hashCode += getEntidad1().hashCode();
        }
        if (getEntidad1Grabada() != null) {
            _hashCode += getEntidad1Grabada().hashCode();
        }
        if (getEntidad1Nuevo() != null) {
            _hashCode += getEntidad1Nuevo().hashCode();
        }
        if (getEntidad2() != null) {
            _hashCode += getEntidad2().hashCode();
        }
        if (getEntidad2Nuevo() != null) {
            _hashCode += getEntidad2Nuevo().hashCode();
        }
        if (getEntidadCastellano() != null) {
            _hashCode += getEntidadCastellano().hashCode();
        }
        if (getFora() != null) {
            _hashCode += getFora().hashCode();
        }
        if (getHora() != null) {
            _hashCode += getHora().hashCode();
        }
        if (getIdioex() != null) {
            _hashCode += getIdioex().hashCode();
        }
        if (getIdioma() != null) {
            _hashCode += getIdioma().hashCode();
        }
        if (getIdiomaExtracto() != null) {
            _hashCode += getIdiomaExtracto().hashCode();
        }
        if (getLeido() != null) {
            _hashCode += getLeido().hashCode();
        }
        if (getLocalitzadorsDocs() != null) {
            _hashCode += getLocalitzadorsDocs().hashCode();
        }
        if (getMotivo() != null) {
            _hashCode += getMotivo().hashCode();
        }
        if (getMunicipi060() != null) {
            _hashCode += getMunicipi060().hashCode();
        }
        if (getNumeroDocumentosRegistro060() != null) {
            _hashCode += getNumeroDocumentosRegistro060().hashCode();
        }
        if (getNumeroEntrada() != null) {
            _hashCode += getNumeroEntrada().hashCode();
        }
        if (getOficina() != null) {
            _hashCode += getOficina().hashCode();
        }
        if (getOficinafisica() != null) {
            _hashCode += getOficinafisica().hashCode();
        }
        if (getOrigenRegistro() != null) {
            _hashCode += getOrigenRegistro().hashCode();
        }
        if (getParamRegPubEnt() != null) {
            _hashCode += getParamRegPubEnt().hashCode();
        }
        if (getProcedenciaGeografica() != null) {
            _hashCode += getProcedenciaGeografica().hashCode();
        }
        if (getRegistroActualizado() != null) {
            _hashCode += getRegistroActualizado().hashCode();
        }
        if (getRegistroAnulado() != null) {
            _hashCode += getRegistroAnulado().hashCode();
        }
        if (getRegistroGrabado() != null) {
            _hashCode += getRegistroGrabado().hashCode();
        }
        if (getSalida1() != null) {
            _hashCode += getSalida1().hashCode();
        }
        if (getSalida2() != null) {
            _hashCode += getSalida2().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getValidado() != null) {
            _hashCode += getValidado().hashCode();
        }
        if (getErrores() != null) {
            _hashCode += getErrores().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParametrosRegistroEntradaWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroEntradaWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "usuario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("password");
        elemField.setXmlName(new javax.xml.namespace.QName("", "password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuarioRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "usuarioRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "actualizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("altres");
        elemField.setXmlName(new javax.xml.namespace.QName("", "altres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("altresNuevo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "altresNuevo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anoEntrada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anoEntrada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balears");
        elemField.setXmlName(new javax.xml.namespace.QName("", "balears"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comentario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comentario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comentarioNuevo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comentarioNuevo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "correo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataentrada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataentrada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataVisado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataVisado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionIdiomaDocumento");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionIdiomaDocumento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionMunicipi060");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionMunicipi060"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionOficina");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionOficina"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionOficinaFisica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionOficinaFisica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionOrganismoDestinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionOrganismoDestinatario"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionRemitente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionRemitente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinatari");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinatari"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("disquet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disquet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("emailRemitent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "emailRemitent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad1Grabada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad1Grabada"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad1Nuevo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad1Nuevo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidad2Nuevo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad2Nuevo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entidadCastellano");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidadCastellano"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hora");
        elemField.setXmlName(new javax.xml.namespace.QName("", "hora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idioex");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idioex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("idiomaExtracto");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idiomaExtracto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("leido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "leido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localitzadorsDocs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "localitzadorsDocs"));
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
        elemField.setFieldName("municipi060");
        elemField.setXmlName(new javax.xml.namespace.QName("", "municipi060"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroDocumentosRegistro060");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroDocumentosRegistro060"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroEntrada");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroEntrada"));
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
        elemField.setFieldName("oficinafisica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "oficinafisica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("origenRegistro");
        elemField.setXmlName(new javax.xml.namespace.QName("", "origenRegistro"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paramRegPubEnt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "paramRegPubEnt"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "ParametrosRegistroPublicadoEntradaWS"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("procedenciaGeografica");
        elemField.setXmlName(new javax.xml.namespace.QName("", "procedenciaGeografica"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registroActualizado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroActualizado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registroAnulado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroAnulado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registroGrabado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroGrabado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("salida1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "salida1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("salida2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "salida2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("validado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "validado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", "listaErroresEntrada"));
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
