/**
 * ParametrosRegistroSalidaWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.regweb.ws.services.regwebfacade;

public class ParametrosRegistroSalidaWS  implements java.io.Serializable {
    private java.lang.String usuario;

    private java.lang.String password;

    private java.lang.String usuarioConexion;

    private java.lang.String anoSalida;

    private java.lang.Boolean actualizacion;

    private java.lang.Boolean registroSalidaGrabado;

    private java.lang.String entidadCastellano;

    private java.lang.String registroAnulado;

    private java.lang.String numeroSalida;

    private java.lang.String descripcionOficina;

    private java.lang.String descripcionOficinaFisica;

    private java.lang.String descripcionDestinatario;

    private java.lang.String correo;

    private java.lang.String descripcionOrganismoRemitente;

    private java.lang.String descripcionDocumento;

    private java.lang.String descripcionIdiomaDocumento;

    private java.lang.String destinoGeografico;

    private java.lang.String idiomaExtracto;

    private java.lang.String datasalida;

    private java.lang.String hora;

    private java.lang.String oficina;

    private java.lang.String oficinafisica;

    private java.lang.String data;

    private java.lang.String tipo;

    private java.lang.String idioma;

    private java.lang.String entidad1;

    private java.lang.String entidad2;

    private java.lang.String altres;

    private java.lang.String balears;

    private java.lang.String fora;

    private java.lang.String entrada1;

    private java.lang.String entrada2;

    private java.lang.String remitent;

    private java.lang.String idioex;

    private java.lang.String disquet;

    private java.lang.Boolean registroActualizado;

    private java.lang.String comentario;

    private java.lang.String motivo;

    private java.lang.String entidad1Nuevo;

    private java.lang.String entidad2Nuevo;

    private java.lang.String altresNuevo;

    private java.lang.String comentarioNuevo;

    private java.lang.String dataVisado;

    private java.lang.Boolean validado;

    private java.lang.Boolean leido;

    private java.lang.String entidad1Grabada;

    private java.lang.String emailRemitent;

    private java.lang.String localitzadorsDocs;

    private es.caib.regweb.ws.services.regwebfacade.ListaErroresSalida errores;

    public ParametrosRegistroSalidaWS() {
    }

    public ParametrosRegistroSalidaWS(
           java.lang.String usuario,
           java.lang.String password,
           java.lang.String usuarioConexion,
           java.lang.String anoSalida,
           java.lang.Boolean actualizacion,
           java.lang.Boolean registroSalidaGrabado,
           java.lang.String entidadCastellano,
           java.lang.String registroAnulado,
           java.lang.String numeroSalida,
           java.lang.String descripcionOficina,
           java.lang.String descripcionOficinaFisica,
           java.lang.String descripcionDestinatario,
           java.lang.String correo,
           java.lang.String descripcionOrganismoRemitente,
           java.lang.String descripcionDocumento,
           java.lang.String descripcionIdiomaDocumento,
           java.lang.String destinoGeografico,
           java.lang.String idiomaExtracto,
           java.lang.String datasalida,
           java.lang.String hora,
           java.lang.String oficina,
           java.lang.String oficinafisica,
           java.lang.String data,
           java.lang.String tipo,
           java.lang.String idioma,
           java.lang.String entidad1,
           java.lang.String entidad2,
           java.lang.String altres,
           java.lang.String balears,
           java.lang.String fora,
           java.lang.String entrada1,
           java.lang.String entrada2,
           java.lang.String remitent,
           java.lang.String idioex,
           java.lang.String disquet,
           java.lang.Boolean registroActualizado,
           java.lang.String comentario,
           java.lang.String motivo,
           java.lang.String entidad1Nuevo,
           java.lang.String entidad2Nuevo,
           java.lang.String altresNuevo,
           java.lang.String comentarioNuevo,
           java.lang.String dataVisado,
           java.lang.Boolean validado,
           java.lang.Boolean leido,
           java.lang.String entidad1Grabada,
           java.lang.String emailRemitent,
           java.lang.String localitzadorsDocs,
           es.caib.regweb.ws.services.regwebfacade.ListaErroresSalida errores) {
           this.usuario = usuario;
           this.password = password;
           this.usuarioConexion = usuarioConexion;
           this.anoSalida = anoSalida;
           this.actualizacion = actualizacion;
           this.registroSalidaGrabado = registroSalidaGrabado;
           this.entidadCastellano = entidadCastellano;
           this.registroAnulado = registroAnulado;
           this.numeroSalida = numeroSalida;
           this.descripcionOficina = descripcionOficina;
           this.descripcionOficinaFisica = descripcionOficinaFisica;
           this.descripcionDestinatario = descripcionDestinatario;
           this.correo = correo;
           this.descripcionOrganismoRemitente = descripcionOrganismoRemitente;
           this.descripcionDocumento = descripcionDocumento;
           this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
           this.destinoGeografico = destinoGeografico;
           this.idiomaExtracto = idiomaExtracto;
           this.datasalida = datasalida;
           this.hora = hora;
           this.oficina = oficina;
           this.oficinafisica = oficinafisica;
           this.data = data;
           this.tipo = tipo;
           this.idioma = idioma;
           this.entidad1 = entidad1;
           this.entidad2 = entidad2;
           this.altres = altres;
           this.balears = balears;
           this.fora = fora;
           this.entrada1 = entrada1;
           this.entrada2 = entrada2;
           this.remitent = remitent;
           this.idioex = idioex;
           this.disquet = disquet;
           this.registroActualizado = registroActualizado;
           this.comentario = comentario;
           this.motivo = motivo;
           this.entidad1Nuevo = entidad1Nuevo;
           this.entidad2Nuevo = entidad2Nuevo;
           this.altresNuevo = altresNuevo;
           this.comentarioNuevo = comentarioNuevo;
           this.dataVisado = dataVisado;
           this.validado = validado;
           this.leido = leido;
           this.entidad1Grabada = entidad1Grabada;
           this.emailRemitent = emailRemitent;
           this.localitzadorsDocs = localitzadorsDocs;
           this.errores = errores;
    }


    /**
     * Gets the usuario value for this ParametrosRegistroSalidaWS.
     * 
     * @return usuario
     */
    public java.lang.String getUsuario() {
        return usuario;
    }


    /**
     * Sets the usuario value for this ParametrosRegistroSalidaWS.
     * 
     * @param usuario
     */
    public void setUsuario(java.lang.String usuario) {
        this.usuario = usuario;
    }


    /**
     * Gets the password value for this ParametrosRegistroSalidaWS.
     * 
     * @return password
     */
    public java.lang.String getPassword() {
        return password;
    }


    /**
     * Sets the password value for this ParametrosRegistroSalidaWS.
     * 
     * @param password
     */
    public void setPassword(java.lang.String password) {
        this.password = password;
    }


    /**
     * Gets the usuarioConexion value for this ParametrosRegistroSalidaWS.
     * 
     * @return usuarioConexion
     */
    public java.lang.String getUsuarioConexion() {
        return usuarioConexion;
    }


    /**
     * Sets the usuarioConexion value for this ParametrosRegistroSalidaWS.
     * 
     * @param usuarioConexion
     */
    public void setUsuarioConexion(java.lang.String usuarioConexion) {
        this.usuarioConexion = usuarioConexion;
    }


    /**
     * Gets the anoSalida value for this ParametrosRegistroSalidaWS.
     * 
     * @return anoSalida
     */
    public java.lang.String getAnoSalida() {
        return anoSalida;
    }


    /**
     * Sets the anoSalida value for this ParametrosRegistroSalidaWS.
     * 
     * @param anoSalida
     */
    public void setAnoSalida(java.lang.String anoSalida) {
        this.anoSalida = anoSalida;
    }


    /**
     * Gets the actualizacion value for this ParametrosRegistroSalidaWS.
     * 
     * @return actualizacion
     */
    public java.lang.Boolean getActualizacion() {
        return actualizacion;
    }


    /**
     * Sets the actualizacion value for this ParametrosRegistroSalidaWS.
     * 
     * @param actualizacion
     */
    public void setActualizacion(java.lang.Boolean actualizacion) {
        this.actualizacion = actualizacion;
    }


    /**
     * Gets the registroSalidaGrabado value for this ParametrosRegistroSalidaWS.
     * 
     * @return registroSalidaGrabado
     */
    public java.lang.Boolean getRegistroSalidaGrabado() {
        return registroSalidaGrabado;
    }


    /**
     * Sets the registroSalidaGrabado value for this ParametrosRegistroSalidaWS.
     * 
     * @param registroSalidaGrabado
     */
    public void setRegistroSalidaGrabado(java.lang.Boolean registroSalidaGrabado) {
        this.registroSalidaGrabado = registroSalidaGrabado;
    }


    /**
     * Gets the entidadCastellano value for this ParametrosRegistroSalidaWS.
     * 
     * @return entidadCastellano
     */
    public java.lang.String getEntidadCastellano() {
        return entidadCastellano;
    }


    /**
     * Sets the entidadCastellano value for this ParametrosRegistroSalidaWS.
     * 
     * @param entidadCastellano
     */
    public void setEntidadCastellano(java.lang.String entidadCastellano) {
        this.entidadCastellano = entidadCastellano;
    }


    /**
     * Gets the registroAnulado value for this ParametrosRegistroSalidaWS.
     * 
     * @return registroAnulado
     */
    public java.lang.String getRegistroAnulado() {
        return registroAnulado;
    }


    /**
     * Sets the registroAnulado value for this ParametrosRegistroSalidaWS.
     * 
     * @param registroAnulado
     */
    public void setRegistroAnulado(java.lang.String registroAnulado) {
        this.registroAnulado = registroAnulado;
    }


    /**
     * Gets the numeroSalida value for this ParametrosRegistroSalidaWS.
     * 
     * @return numeroSalida
     */
    public java.lang.String getNumeroSalida() {
        return numeroSalida;
    }


    /**
     * Sets the numeroSalida value for this ParametrosRegistroSalidaWS.
     * 
     * @param numeroSalida
     */
    public void setNumeroSalida(java.lang.String numeroSalida) {
        this.numeroSalida = numeroSalida;
    }


    /**
     * Gets the descripcionOficina value for this ParametrosRegistroSalidaWS.
     * 
     * @return descripcionOficina
     */
    public java.lang.String getDescripcionOficina() {
        return descripcionOficina;
    }


    /**
     * Sets the descripcionOficina value for this ParametrosRegistroSalidaWS.
     * 
     * @param descripcionOficina
     */
    public void setDescripcionOficina(java.lang.String descripcionOficina) {
        this.descripcionOficina = descripcionOficina;
    }


    /**
     * Gets the descripcionOficinaFisica value for this ParametrosRegistroSalidaWS.
     * 
     * @return descripcionOficinaFisica
     */
    public java.lang.String getDescripcionOficinaFisica() {
        return descripcionOficinaFisica;
    }


    /**
     * Sets the descripcionOficinaFisica value for this ParametrosRegistroSalidaWS.
     * 
     * @param descripcionOficinaFisica
     */
    public void setDescripcionOficinaFisica(java.lang.String descripcionOficinaFisica) {
        this.descripcionOficinaFisica = descripcionOficinaFisica;
    }


    /**
     * Gets the descripcionDestinatario value for this ParametrosRegistroSalidaWS.
     * 
     * @return descripcionDestinatario
     */
    public java.lang.String getDescripcionDestinatario() {
        return descripcionDestinatario;
    }


    /**
     * Sets the descripcionDestinatario value for this ParametrosRegistroSalidaWS.
     * 
     * @param descripcionDestinatario
     */
    public void setDescripcionDestinatario(java.lang.String descripcionDestinatario) {
        this.descripcionDestinatario = descripcionDestinatario;
    }


    /**
     * Gets the correo value for this ParametrosRegistroSalidaWS.
     * 
     * @return correo
     */
    public java.lang.String getCorreo() {
        return correo;
    }


    /**
     * Sets the correo value for this ParametrosRegistroSalidaWS.
     * 
     * @param correo
     */
    public void setCorreo(java.lang.String correo) {
        this.correo = correo;
    }


    /**
     * Gets the descripcionOrganismoRemitente value for this ParametrosRegistroSalidaWS.
     * 
     * @return descripcionOrganismoRemitente
     */
    public java.lang.String getDescripcionOrganismoRemitente() {
        return descripcionOrganismoRemitente;
    }


    /**
     * Sets the descripcionOrganismoRemitente value for this ParametrosRegistroSalidaWS.
     * 
     * @param descripcionOrganismoRemitente
     */
    public void setDescripcionOrganismoRemitente(java.lang.String descripcionOrganismoRemitente) {
        this.descripcionOrganismoRemitente = descripcionOrganismoRemitente;
    }


    /**
     * Gets the descripcionDocumento value for this ParametrosRegistroSalidaWS.
     * 
     * @return descripcionDocumento
     */
    public java.lang.String getDescripcionDocumento() {
        return descripcionDocumento;
    }


    /**
     * Sets the descripcionDocumento value for this ParametrosRegistroSalidaWS.
     * 
     * @param descripcionDocumento
     */
    public void setDescripcionDocumento(java.lang.String descripcionDocumento) {
        this.descripcionDocumento = descripcionDocumento;
    }


    /**
     * Gets the descripcionIdiomaDocumento value for this ParametrosRegistroSalidaWS.
     * 
     * @return descripcionIdiomaDocumento
     */
    public java.lang.String getDescripcionIdiomaDocumento() {
        return descripcionIdiomaDocumento;
    }


    /**
     * Sets the descripcionIdiomaDocumento value for this ParametrosRegistroSalidaWS.
     * 
     * @param descripcionIdiomaDocumento
     */
    public void setDescripcionIdiomaDocumento(java.lang.String descripcionIdiomaDocumento) {
        this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
    }


    /**
     * Gets the destinoGeografico value for this ParametrosRegistroSalidaWS.
     * 
     * @return destinoGeografico
     */
    public java.lang.String getDestinoGeografico() {
        return destinoGeografico;
    }


    /**
     * Sets the destinoGeografico value for this ParametrosRegistroSalidaWS.
     * 
     * @param destinoGeografico
     */
    public void setDestinoGeografico(java.lang.String destinoGeografico) {
        this.destinoGeografico = destinoGeografico;
    }


    /**
     * Gets the idiomaExtracto value for this ParametrosRegistroSalidaWS.
     * 
     * @return idiomaExtracto
     */
    public java.lang.String getIdiomaExtracto() {
        return idiomaExtracto;
    }


    /**
     * Sets the idiomaExtracto value for this ParametrosRegistroSalidaWS.
     * 
     * @param idiomaExtracto
     */
    public void setIdiomaExtracto(java.lang.String idiomaExtracto) {
        this.idiomaExtracto = idiomaExtracto;
    }


    /**
     * Gets the datasalida value for this ParametrosRegistroSalidaWS.
     * 
     * @return datasalida
     */
    public java.lang.String getDatasalida() {
        return datasalida;
    }


    /**
     * Sets the datasalida value for this ParametrosRegistroSalidaWS.
     * 
     * @param datasalida
     */
    public void setDatasalida(java.lang.String datasalida) {
        this.datasalida = datasalida;
    }


    /**
     * Gets the hora value for this ParametrosRegistroSalidaWS.
     * 
     * @return hora
     */
    public java.lang.String getHora() {
        return hora;
    }


    /**
     * Sets the hora value for this ParametrosRegistroSalidaWS.
     * 
     * @param hora
     */
    public void setHora(java.lang.String hora) {
        this.hora = hora;
    }


    /**
     * Gets the oficina value for this ParametrosRegistroSalidaWS.
     * 
     * @return oficina
     */
    public java.lang.String getOficina() {
        return oficina;
    }


    /**
     * Sets the oficina value for this ParametrosRegistroSalidaWS.
     * 
     * @param oficina
     */
    public void setOficina(java.lang.String oficina) {
        this.oficina = oficina;
    }


    /**
     * Gets the oficinafisica value for this ParametrosRegistroSalidaWS.
     * 
     * @return oficinafisica
     */
    public java.lang.String getOficinafisica() {
        return oficinafisica;
    }


    /**
     * Sets the oficinafisica value for this ParametrosRegistroSalidaWS.
     * 
     * @param oficinafisica
     */
    public void setOficinafisica(java.lang.String oficinafisica) {
        this.oficinafisica = oficinafisica;
    }


    /**
     * Gets the data value for this ParametrosRegistroSalidaWS.
     * 
     * @return data
     */
    public java.lang.String getData() {
        return data;
    }


    /**
     * Sets the data value for this ParametrosRegistroSalidaWS.
     * 
     * @param data
     */
    public void setData(java.lang.String data) {
        this.data = data;
    }


    /**
     * Gets the tipo value for this ParametrosRegistroSalidaWS.
     * 
     * @return tipo
     */
    public java.lang.String getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this ParametrosRegistroSalidaWS.
     * 
     * @param tipo
     */
    public void setTipo(java.lang.String tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the idioma value for this ParametrosRegistroSalidaWS.
     * 
     * @return idioma
     */
    public java.lang.String getIdioma() {
        return idioma;
    }


    /**
     * Sets the idioma value for this ParametrosRegistroSalidaWS.
     * 
     * @param idioma
     */
    public void setIdioma(java.lang.String idioma) {
        this.idioma = idioma;
    }


    /**
     * Gets the entidad1 value for this ParametrosRegistroSalidaWS.
     * 
     * @return entidad1
     */
    public java.lang.String getEntidad1() {
        return entidad1;
    }


    /**
     * Sets the entidad1 value for this ParametrosRegistroSalidaWS.
     * 
     * @param entidad1
     */
    public void setEntidad1(java.lang.String entidad1) {
        this.entidad1 = entidad1;
    }


    /**
     * Gets the entidad2 value for this ParametrosRegistroSalidaWS.
     * 
     * @return entidad2
     */
    public java.lang.String getEntidad2() {
        return entidad2;
    }


    /**
     * Sets the entidad2 value for this ParametrosRegistroSalidaWS.
     * 
     * @param entidad2
     */
    public void setEntidad2(java.lang.String entidad2) {
        this.entidad2 = entidad2;
    }


    /**
     * Gets the altres value for this ParametrosRegistroSalidaWS.
     * 
     * @return altres
     */
    public java.lang.String getAltres() {
        return altres;
    }


    /**
     * Sets the altres value for this ParametrosRegistroSalidaWS.
     * 
     * @param altres
     */
    public void setAltres(java.lang.String altres) {
        this.altres = altres;
    }


    /**
     * Gets the balears value for this ParametrosRegistroSalidaWS.
     * 
     * @return balears
     */
    public java.lang.String getBalears() {
        return balears;
    }


    /**
     * Sets the balears value for this ParametrosRegistroSalidaWS.
     * 
     * @param balears
     */
    public void setBalears(java.lang.String balears) {
        this.balears = balears;
    }


    /**
     * Gets the fora value for this ParametrosRegistroSalidaWS.
     * 
     * @return fora
     */
    public java.lang.String getFora() {
        return fora;
    }


    /**
     * Sets the fora value for this ParametrosRegistroSalidaWS.
     * 
     * @param fora
     */
    public void setFora(java.lang.String fora) {
        this.fora = fora;
    }


    /**
     * Gets the entrada1 value for this ParametrosRegistroSalidaWS.
     * 
     * @return entrada1
     */
    public java.lang.String getEntrada1() {
        return entrada1;
    }


    /**
     * Sets the entrada1 value for this ParametrosRegistroSalidaWS.
     * 
     * @param entrada1
     */
    public void setEntrada1(java.lang.String entrada1) {
        this.entrada1 = entrada1;
    }


    /**
     * Gets the entrada2 value for this ParametrosRegistroSalidaWS.
     * 
     * @return entrada2
     */
    public java.lang.String getEntrada2() {
        return entrada2;
    }


    /**
     * Sets the entrada2 value for this ParametrosRegistroSalidaWS.
     * 
     * @param entrada2
     */
    public void setEntrada2(java.lang.String entrada2) {
        this.entrada2 = entrada2;
    }


    /**
     * Gets the remitent value for this ParametrosRegistroSalidaWS.
     * 
     * @return remitent
     */
    public java.lang.String getRemitent() {
        return remitent;
    }


    /**
     * Sets the remitent value for this ParametrosRegistroSalidaWS.
     * 
     * @param remitent
     */
    public void setRemitent(java.lang.String remitent) {
        this.remitent = remitent;
    }


    /**
     * Gets the idioex value for this ParametrosRegistroSalidaWS.
     * 
     * @return idioex
     */
    public java.lang.String getIdioex() {
        return idioex;
    }


    /**
     * Sets the idioex value for this ParametrosRegistroSalidaWS.
     * 
     * @param idioex
     */
    public void setIdioex(java.lang.String idioex) {
        this.idioex = idioex;
    }


    /**
     * Gets the disquet value for this ParametrosRegistroSalidaWS.
     * 
     * @return disquet
     */
    public java.lang.String getDisquet() {
        return disquet;
    }


    /**
     * Sets the disquet value for this ParametrosRegistroSalidaWS.
     * 
     * @param disquet
     */
    public void setDisquet(java.lang.String disquet) {
        this.disquet = disquet;
    }


    /**
     * Gets the registroActualizado value for this ParametrosRegistroSalidaWS.
     * 
     * @return registroActualizado
     */
    public java.lang.Boolean getRegistroActualizado() {
        return registroActualizado;
    }


    /**
     * Sets the registroActualizado value for this ParametrosRegistroSalidaWS.
     * 
     * @param registroActualizado
     */
    public void setRegistroActualizado(java.lang.Boolean registroActualizado) {
        this.registroActualizado = registroActualizado;
    }


    /**
     * Gets the comentario value for this ParametrosRegistroSalidaWS.
     * 
     * @return comentario
     */
    public java.lang.String getComentario() {
        return comentario;
    }


    /**
     * Sets the comentario value for this ParametrosRegistroSalidaWS.
     * 
     * @param comentario
     */
    public void setComentario(java.lang.String comentario) {
        this.comentario = comentario;
    }


    /**
     * Gets the motivo value for this ParametrosRegistroSalidaWS.
     * 
     * @return motivo
     */
    public java.lang.String getMotivo() {
        return motivo;
    }


    /**
     * Sets the motivo value for this ParametrosRegistroSalidaWS.
     * 
     * @param motivo
     */
    public void setMotivo(java.lang.String motivo) {
        this.motivo = motivo;
    }


    /**
     * Gets the entidad1Nuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @return entidad1Nuevo
     */
    public java.lang.String getEntidad1Nuevo() {
        return entidad1Nuevo;
    }


    /**
     * Sets the entidad1Nuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @param entidad1Nuevo
     */
    public void setEntidad1Nuevo(java.lang.String entidad1Nuevo) {
        this.entidad1Nuevo = entidad1Nuevo;
    }


    /**
     * Gets the entidad2Nuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @return entidad2Nuevo
     */
    public java.lang.String getEntidad2Nuevo() {
        return entidad2Nuevo;
    }


    /**
     * Sets the entidad2Nuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @param entidad2Nuevo
     */
    public void setEntidad2Nuevo(java.lang.String entidad2Nuevo) {
        this.entidad2Nuevo = entidad2Nuevo;
    }


    /**
     * Gets the altresNuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @return altresNuevo
     */
    public java.lang.String getAltresNuevo() {
        return altresNuevo;
    }


    /**
     * Sets the altresNuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @param altresNuevo
     */
    public void setAltresNuevo(java.lang.String altresNuevo) {
        this.altresNuevo = altresNuevo;
    }


    /**
     * Gets the comentarioNuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @return comentarioNuevo
     */
    public java.lang.String getComentarioNuevo() {
        return comentarioNuevo;
    }


    /**
     * Sets the comentarioNuevo value for this ParametrosRegistroSalidaWS.
     * 
     * @param comentarioNuevo
     */
    public void setComentarioNuevo(java.lang.String comentarioNuevo) {
        this.comentarioNuevo = comentarioNuevo;
    }


    /**
     * Gets the dataVisado value for this ParametrosRegistroSalidaWS.
     * 
     * @return dataVisado
     */
    public java.lang.String getDataVisado() {
        return dataVisado;
    }


    /**
     * Sets the dataVisado value for this ParametrosRegistroSalidaWS.
     * 
     * @param dataVisado
     */
    public void setDataVisado(java.lang.String dataVisado) {
        this.dataVisado = dataVisado;
    }


    /**
     * Gets the validado value for this ParametrosRegistroSalidaWS.
     * 
     * @return validado
     */
    public java.lang.Boolean getValidado() {
        return validado;
    }


    /**
     * Sets the validado value for this ParametrosRegistroSalidaWS.
     * 
     * @param validado
     */
    public void setValidado(java.lang.Boolean validado) {
        this.validado = validado;
    }


    /**
     * Gets the leido value for this ParametrosRegistroSalidaWS.
     * 
     * @return leido
     */
    public java.lang.Boolean getLeido() {
        return leido;
    }


    /**
     * Sets the leido value for this ParametrosRegistroSalidaWS.
     * 
     * @param leido
     */
    public void setLeido(java.lang.Boolean leido) {
        this.leido = leido;
    }


    /**
     * Gets the entidad1Grabada value for this ParametrosRegistroSalidaWS.
     * 
     * @return entidad1Grabada
     */
    public java.lang.String getEntidad1Grabada() {
        return entidad1Grabada;
    }


    /**
     * Sets the entidad1Grabada value for this ParametrosRegistroSalidaWS.
     * 
     * @param entidad1Grabada
     */
    public void setEntidad1Grabada(java.lang.String entidad1Grabada) {
        this.entidad1Grabada = entidad1Grabada;
    }


    /**
     * Gets the emailRemitent value for this ParametrosRegistroSalidaWS.
     * 
     * @return emailRemitent
     */
    public java.lang.String getEmailRemitent() {
        return emailRemitent;
    }


    /**
     * Sets the emailRemitent value for this ParametrosRegistroSalidaWS.
     * 
     * @param emailRemitent
     */
    public void setEmailRemitent(java.lang.String emailRemitent) {
        this.emailRemitent = emailRemitent;
    }


    /**
     * Gets the localitzadorsDocs value for this ParametrosRegistroSalidaWS.
     * 
     * @return localitzadorsDocs
     */
    public java.lang.String getLocalitzadorsDocs() {
        return localitzadorsDocs;
    }


    /**
     * Sets the localitzadorsDocs value for this ParametrosRegistroSalidaWS.
     * 
     * @param localitzadorsDocs
     */
    public void setLocalitzadorsDocs(java.lang.String localitzadorsDocs) {
        this.localitzadorsDocs = localitzadorsDocs;
    }


    /**
     * Gets the errores value for this ParametrosRegistroSalidaWS.
     * 
     * @return errores
     */
    public es.caib.regweb.ws.services.regwebfacade.ListaErroresSalida getErrores() {
        return errores;
    }


    /**
     * Sets the errores value for this ParametrosRegistroSalidaWS.
     * 
     * @param errores
     */
    public void setErrores(es.caib.regweb.ws.services.regwebfacade.ListaErroresSalida errores) {
        this.errores = errores;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ParametrosRegistroSalidaWS)) return false;
        ParametrosRegistroSalidaWS other = (ParametrosRegistroSalidaWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.usuario==null && other.getUsuario()==null) || 
             (this.usuario!=null &&
              this.usuario.equals(other.getUsuario()))) &&
            ((this.password==null && other.getPassword()==null) || 
             (this.password!=null &&
              this.password.equals(other.getPassword()))) &&
            ((this.usuarioConexion==null && other.getUsuarioConexion()==null) || 
             (this.usuarioConexion!=null &&
              this.usuarioConexion.equals(other.getUsuarioConexion()))) &&
            ((this.anoSalida==null && other.getAnoSalida()==null) || 
             (this.anoSalida!=null &&
              this.anoSalida.equals(other.getAnoSalida()))) &&
            ((this.actualizacion==null && other.getActualizacion()==null) || 
             (this.actualizacion!=null &&
              this.actualizacion.equals(other.getActualizacion()))) &&
            ((this.registroSalidaGrabado==null && other.getRegistroSalidaGrabado()==null) || 
             (this.registroSalidaGrabado!=null &&
              this.registroSalidaGrabado.equals(other.getRegistroSalidaGrabado()))) &&
            ((this.entidadCastellano==null && other.getEntidadCastellano()==null) || 
             (this.entidadCastellano!=null &&
              this.entidadCastellano.equals(other.getEntidadCastellano()))) &&
            ((this.registroAnulado==null && other.getRegistroAnulado()==null) || 
             (this.registroAnulado!=null &&
              this.registroAnulado.equals(other.getRegistroAnulado()))) &&
            ((this.numeroSalida==null && other.getNumeroSalida()==null) || 
             (this.numeroSalida!=null &&
              this.numeroSalida.equals(other.getNumeroSalida()))) &&
            ((this.descripcionOficina==null && other.getDescripcionOficina()==null) || 
             (this.descripcionOficina!=null &&
              this.descripcionOficina.equals(other.getDescripcionOficina()))) &&
            ((this.descripcionOficinaFisica==null && other.getDescripcionOficinaFisica()==null) || 
             (this.descripcionOficinaFisica!=null &&
              this.descripcionOficinaFisica.equals(other.getDescripcionOficinaFisica()))) &&
            ((this.descripcionDestinatario==null && other.getDescripcionDestinatario()==null) || 
             (this.descripcionDestinatario!=null &&
              this.descripcionDestinatario.equals(other.getDescripcionDestinatario()))) &&
            ((this.correo==null && other.getCorreo()==null) || 
             (this.correo!=null &&
              this.correo.equals(other.getCorreo()))) &&
            ((this.descripcionOrganismoRemitente==null && other.getDescripcionOrganismoRemitente()==null) || 
             (this.descripcionOrganismoRemitente!=null &&
              this.descripcionOrganismoRemitente.equals(other.getDescripcionOrganismoRemitente()))) &&
            ((this.descripcionDocumento==null && other.getDescripcionDocumento()==null) || 
             (this.descripcionDocumento!=null &&
              this.descripcionDocumento.equals(other.getDescripcionDocumento()))) &&
            ((this.descripcionIdiomaDocumento==null && other.getDescripcionIdiomaDocumento()==null) || 
             (this.descripcionIdiomaDocumento!=null &&
              this.descripcionIdiomaDocumento.equals(other.getDescripcionIdiomaDocumento()))) &&
            ((this.destinoGeografico==null && other.getDestinoGeografico()==null) || 
             (this.destinoGeografico!=null &&
              this.destinoGeografico.equals(other.getDestinoGeografico()))) &&
            ((this.idiomaExtracto==null && other.getIdiomaExtracto()==null) || 
             (this.idiomaExtracto!=null &&
              this.idiomaExtracto.equals(other.getIdiomaExtracto()))) &&
            ((this.datasalida==null && other.getDatasalida()==null) || 
             (this.datasalida!=null &&
              this.datasalida.equals(other.getDatasalida()))) &&
            ((this.hora==null && other.getHora()==null) || 
             (this.hora!=null &&
              this.hora.equals(other.getHora()))) &&
            ((this.oficina==null && other.getOficina()==null) || 
             (this.oficina!=null &&
              this.oficina.equals(other.getOficina()))) &&
            ((this.oficinafisica==null && other.getOficinafisica()==null) || 
             (this.oficinafisica!=null &&
              this.oficinafisica.equals(other.getOficinafisica()))) &&
            ((this.data==null && other.getData()==null) || 
             (this.data!=null &&
              this.data.equals(other.getData()))) &&
            ((this.tipo==null && other.getTipo()==null) || 
             (this.tipo!=null &&
              this.tipo.equals(other.getTipo()))) &&
            ((this.idioma==null && other.getIdioma()==null) || 
             (this.idioma!=null &&
              this.idioma.equals(other.getIdioma()))) &&
            ((this.entidad1==null && other.getEntidad1()==null) || 
             (this.entidad1!=null &&
              this.entidad1.equals(other.getEntidad1()))) &&
            ((this.entidad2==null && other.getEntidad2()==null) || 
             (this.entidad2!=null &&
              this.entidad2.equals(other.getEntidad2()))) &&
            ((this.altres==null && other.getAltres()==null) || 
             (this.altres!=null &&
              this.altres.equals(other.getAltres()))) &&
            ((this.balears==null && other.getBalears()==null) || 
             (this.balears!=null &&
              this.balears.equals(other.getBalears()))) &&
            ((this.fora==null && other.getFora()==null) || 
             (this.fora!=null &&
              this.fora.equals(other.getFora()))) &&
            ((this.entrada1==null && other.getEntrada1()==null) || 
             (this.entrada1!=null &&
              this.entrada1.equals(other.getEntrada1()))) &&
            ((this.entrada2==null && other.getEntrada2()==null) || 
             (this.entrada2!=null &&
              this.entrada2.equals(other.getEntrada2()))) &&
            ((this.remitent==null && other.getRemitent()==null) || 
             (this.remitent!=null &&
              this.remitent.equals(other.getRemitent()))) &&
            ((this.idioex==null && other.getIdioex()==null) || 
             (this.idioex!=null &&
              this.idioex.equals(other.getIdioex()))) &&
            ((this.disquet==null && other.getDisquet()==null) || 
             (this.disquet!=null &&
              this.disquet.equals(other.getDisquet()))) &&
            ((this.registroActualizado==null && other.getRegistroActualizado()==null) || 
             (this.registroActualizado!=null &&
              this.registroActualizado.equals(other.getRegistroActualizado()))) &&
            ((this.comentario==null && other.getComentario()==null) || 
             (this.comentario!=null &&
              this.comentario.equals(other.getComentario()))) &&
            ((this.motivo==null && other.getMotivo()==null) || 
             (this.motivo!=null &&
              this.motivo.equals(other.getMotivo()))) &&
            ((this.entidad1Nuevo==null && other.getEntidad1Nuevo()==null) || 
             (this.entidad1Nuevo!=null &&
              this.entidad1Nuevo.equals(other.getEntidad1Nuevo()))) &&
            ((this.entidad2Nuevo==null && other.getEntidad2Nuevo()==null) || 
             (this.entidad2Nuevo!=null &&
              this.entidad2Nuevo.equals(other.getEntidad2Nuevo()))) &&
            ((this.altresNuevo==null && other.getAltresNuevo()==null) || 
             (this.altresNuevo!=null &&
              this.altresNuevo.equals(other.getAltresNuevo()))) &&
            ((this.comentarioNuevo==null && other.getComentarioNuevo()==null) || 
             (this.comentarioNuevo!=null &&
              this.comentarioNuevo.equals(other.getComentarioNuevo()))) &&
            ((this.dataVisado==null && other.getDataVisado()==null) || 
             (this.dataVisado!=null &&
              this.dataVisado.equals(other.getDataVisado()))) &&
            ((this.validado==null && other.getValidado()==null) || 
             (this.validado!=null &&
              this.validado.equals(other.getValidado()))) &&
            ((this.leido==null && other.getLeido()==null) || 
             (this.leido!=null &&
              this.leido.equals(other.getLeido()))) &&
            ((this.entidad1Grabada==null && other.getEntidad1Grabada()==null) || 
             (this.entidad1Grabada!=null &&
              this.entidad1Grabada.equals(other.getEntidad1Grabada()))) &&
            ((this.emailRemitent==null && other.getEmailRemitent()==null) || 
             (this.emailRemitent!=null &&
              this.emailRemitent.equals(other.getEmailRemitent()))) &&
            ((this.localitzadorsDocs==null && other.getLocalitzadorsDocs()==null) || 
             (this.localitzadorsDocs!=null &&
              this.localitzadorsDocs.equals(other.getLocalitzadorsDocs()))) &&
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
        if (getUsuario() != null) {
            _hashCode += getUsuario().hashCode();
        }
        if (getPassword() != null) {
            _hashCode += getPassword().hashCode();
        }
        if (getUsuarioConexion() != null) {
            _hashCode += getUsuarioConexion().hashCode();
        }
        if (getAnoSalida() != null) {
            _hashCode += getAnoSalida().hashCode();
        }
        if (getActualizacion() != null) {
            _hashCode += getActualizacion().hashCode();
        }
        if (getRegistroSalidaGrabado() != null) {
            _hashCode += getRegistroSalidaGrabado().hashCode();
        }
        if (getEntidadCastellano() != null) {
            _hashCode += getEntidadCastellano().hashCode();
        }
        if (getRegistroAnulado() != null) {
            _hashCode += getRegistroAnulado().hashCode();
        }
        if (getNumeroSalida() != null) {
            _hashCode += getNumeroSalida().hashCode();
        }
        if (getDescripcionOficina() != null) {
            _hashCode += getDescripcionOficina().hashCode();
        }
        if (getDescripcionOficinaFisica() != null) {
            _hashCode += getDescripcionOficinaFisica().hashCode();
        }
        if (getDescripcionDestinatario() != null) {
            _hashCode += getDescripcionDestinatario().hashCode();
        }
        if (getCorreo() != null) {
            _hashCode += getCorreo().hashCode();
        }
        if (getDescripcionOrganismoRemitente() != null) {
            _hashCode += getDescripcionOrganismoRemitente().hashCode();
        }
        if (getDescripcionDocumento() != null) {
            _hashCode += getDescripcionDocumento().hashCode();
        }
        if (getDescripcionIdiomaDocumento() != null) {
            _hashCode += getDescripcionIdiomaDocumento().hashCode();
        }
        if (getDestinoGeografico() != null) {
            _hashCode += getDestinoGeografico().hashCode();
        }
        if (getIdiomaExtracto() != null) {
            _hashCode += getIdiomaExtracto().hashCode();
        }
        if (getDatasalida() != null) {
            _hashCode += getDatasalida().hashCode();
        }
        if (getHora() != null) {
            _hashCode += getHora().hashCode();
        }
        if (getOficina() != null) {
            _hashCode += getOficina().hashCode();
        }
        if (getOficinafisica() != null) {
            _hashCode += getOficinafisica().hashCode();
        }
        if (getData() != null) {
            _hashCode += getData().hashCode();
        }
        if (getTipo() != null) {
            _hashCode += getTipo().hashCode();
        }
        if (getIdioma() != null) {
            _hashCode += getIdioma().hashCode();
        }
        if (getEntidad1() != null) {
            _hashCode += getEntidad1().hashCode();
        }
        if (getEntidad2() != null) {
            _hashCode += getEntidad2().hashCode();
        }
        if (getAltres() != null) {
            _hashCode += getAltres().hashCode();
        }
        if (getBalears() != null) {
            _hashCode += getBalears().hashCode();
        }
        if (getFora() != null) {
            _hashCode += getFora().hashCode();
        }
        if (getEntrada1() != null) {
            _hashCode += getEntrada1().hashCode();
        }
        if (getEntrada2() != null) {
            _hashCode += getEntrada2().hashCode();
        }
        if (getRemitent() != null) {
            _hashCode += getRemitent().hashCode();
        }
        if (getIdioex() != null) {
            _hashCode += getIdioex().hashCode();
        }
        if (getDisquet() != null) {
            _hashCode += getDisquet().hashCode();
        }
        if (getRegistroActualizado() != null) {
            _hashCode += getRegistroActualizado().hashCode();
        }
        if (getComentario() != null) {
            _hashCode += getComentario().hashCode();
        }
        if (getMotivo() != null) {
            _hashCode += getMotivo().hashCode();
        }
        if (getEntidad1Nuevo() != null) {
            _hashCode += getEntidad1Nuevo().hashCode();
        }
        if (getEntidad2Nuevo() != null) {
            _hashCode += getEntidad2Nuevo().hashCode();
        }
        if (getAltresNuevo() != null) {
            _hashCode += getAltresNuevo().hashCode();
        }
        if (getComentarioNuevo() != null) {
            _hashCode += getComentarioNuevo().hashCode();
        }
        if (getDataVisado() != null) {
            _hashCode += getDataVisado().hashCode();
        }
        if (getValidado() != null) {
            _hashCode += getValidado().hashCode();
        }
        if (getLeido() != null) {
            _hashCode += getLeido().hashCode();
        }
        if (getEntidad1Grabada() != null) {
            _hashCode += getEntidad1Grabada().hashCode();
        }
        if (getEmailRemitent() != null) {
            _hashCode += getEmailRemitent().hashCode();
        }
        if (getLocalitzadorsDocs() != null) {
            _hashCode += getLocalitzadorsDocs().hashCode();
        }
        if (getErrores() != null) {
            _hashCode += getErrores().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ParametrosRegistroSalidaWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "ParametrosRegistroSalidaWS"));
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
        elemField.setFieldName("usuarioConexion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UsuarioConexion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anoSalida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anoSalida"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("registroSalidaGrabado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroSalidaGrabado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("registroAnulado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "registroAnulado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroSalida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroSalida"));
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
        elemField.setFieldName("descripcionDestinatario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionDestinatario"));
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
        elemField.setFieldName("descripcionOrganismoRemitente");
        elemField.setXmlName(new javax.xml.namespace.QName("", "descripcionOrganismoRemitente"));
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
        elemField.setFieldName("destinoGeografico");
        elemField.setXmlName(new javax.xml.namespace.QName("", "destinoGeografico"));
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
        elemField.setFieldName("datasalida");
        elemField.setXmlName(new javax.xml.namespace.QName("", "datasalida"));
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
        elemField.setFieldName("data");
        elemField.setXmlName(new javax.xml.namespace.QName("", "data"));
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
        elemField.setFieldName("idioma");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idioma"));
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
        elemField.setFieldName("entidad2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("balears");
        elemField.setXmlName(new javax.xml.namespace.QName("", "balears"));
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
        elemField.setFieldName("entrada1");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entrada1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entrada2");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entrada2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("remitent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "remitent"));
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
        elemField.setFieldName("disquet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "disquet"));
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
        elemField.setFieldName("comentario");
        elemField.setXmlName(new javax.xml.namespace.QName("", "comentario"));
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
        elemField.setFieldName("entidad1Nuevo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "entidad1Nuevo"));
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
        elemField.setFieldName("altresNuevo");
        elemField.setXmlName(new javax.xml.namespace.QName("", "altresNuevo"));
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
        elemField.setFieldName("dataVisado");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dataVisado"));
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
        elemField.setFieldName("leido");
        elemField.setXmlName(new javax.xml.namespace.QName("", "leido"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("emailRemitent");
        elemField.setXmlName(new javax.xml.namespace.QName("", "emailRemitent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("errores");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errores"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", "listaErroresSalida"));
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
