package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.*;

public class ParametrosRegistroEntrada implements Serializable {
    
    private String dataVisado="";
    private String dataentrada="";
    private String hora="";
    private String oficina="";
    private String oficinafisica="0";
    private String data="";
    private String tipo="";
    private String idioma="";
    private String entidad1="";
    private String entidad1Grabada="";
    private String entidad2="";
    private String altres="";
    private String balears="";
    private String fora="";
    private String salida1="";
    private String salida2="";
    private String destinatari="";
    private String idioex="";
    private String disquet="";
    private String comentario="";
    private String usuario;
    private int fzanume=0;
    private String correo="";
    private String registroAnulado="";
    private boolean actualizacion=false;
    private boolean leidos=false;
    private String motivo="";
    private String entidad1Nuevo="";
    private String entidad2Nuevo="";
    private String altresNuevo="";
    private String comentarioNuevo="";
    private String password="";
    private String municipi060="";
    private String descripcioMunicipi060="";
    private String numeroDocumentosRegistro060="0";
    
    private boolean error=false;
    private boolean validado=false;
    private boolean registroGrabado=false;
    private boolean registroActualizado=false;
    private Hashtable errores=new Hashtable();
    private String entidadCastellano=null;

    private String anoEntrada=null;
    private String numeroEntrada=null;
    private String descripcionOficina=null;
    private String descripcionOficinaFisica=null;
    private String descripcionRemitente=null;
    private String descripcionOrganismoDestinatario=null;
    private String descripcionDocumento=null;
    private String descripcionIdiomaDocumento=null;
    private String procedenciaGeografica=null;
    private String idiomaExtracto=null;
    
    // Variables para la publicacion de un registro
    private String BOIBdata="";
    private int BOIBnumeroBOCAIB=0;
    private int BOIBpagina=0;
    private int BOIBlineas=0;
    private String BOIBtexto="";
    private String BOIBobservaciones="";

	public void setEntidadCastellano(String entidadCastellano) {
		this.entidadCastellano=entidadCastellano;
	}

    public String getEntidadCastellano() {
		return entidadCastellano;
	}

    public void setBOIBdata(String BOIBdata) {
        this.BOIBdata=BOIBdata;
    }

    public void setBOIBnumeroBOCAIB(int BOIBnumeroBOCAIB) {
        this.BOIBnumeroBOCAIB=BOIBnumeroBOCAIB;
    }

    public void setBOIBpagina(int BOIBpagina) {
        this.BOIBpagina=BOIBpagina;
    }

    public void setBOIBlineas(int BOIBlineas) {
        this.BOIBlineas=BOIBlineas;
    }

    public void setBOIBtexto(String BOIBtexto) {
        this.BOIBtexto=BOIBtexto;
    }

    public void setBOIBobservaciones(String BOIBobservaciones) {
        this.BOIBobservaciones=BOIBobservaciones;
    }
    
    public String getBOIBdata() {
        return BOIBdata;
    }

    public int getBOIBnumeroBOCAIB() {
        return BOIBnumeroBOCAIB;
    }

    public int getBOIBpagina() {
        return BOIBpagina;
    }

    public int getBOIBlineas() {
        return BOIBlineas;
    }

    public String getBOIBtexto() {
        return BOIBtexto;
    }

    public String getBOIBobservaciones() {
        return BOIBobservaciones;
    }


      public void setregistroActualizado(boolean es_actualizado) {
		this.registroActualizado=es_actualizado;
	}

    public boolean getregistroActualizado() {
		return registroActualizado;
	}

     public void setregistroGrabado(boolean es_grabado) {
		this.registroGrabado=es_grabado;
	}

    public boolean getregistroGrabado() {
		return registroGrabado;
	}
    
    /* Recogemos campos enviados por registro.jsp */
    public void setAnoEntrada(String anoEntrada) {
        this.anoEntrada=anoEntrada;
    }

    public void setActualizacion(boolean actualizacion) {
        this.actualizacion=actualizacion;
    }
    
    public void setCorreo(String correo) {
        this.correo=correo;
    }
    
    public void setRegistroAnulado(String registroAnulado) {
        this.registroAnulado=registroAnulado;
    }
    
    public void setNumeroEntrada(String numeroEntrada) {
        this.numeroEntrada=numeroEntrada;
    }
    
    public void setDescripcionOficina(String descripcionOficina) {
        this.descripcionOficina=descripcionOficina;
    }
    
    public void setDescripcionOficinaFisica(String descripcionOficinaFisica) {
        this.descripcionOficinaFisica=descripcionOficinaFisica;
    }
    
    public void setDescripcionRemitente(String descripcionRemitente) {
        this.descripcionRemitente=descripcionRemitente;
    }
    
    public void setDescripcionOrganismoDestinatario(String descripcionOrganismoDestinatario) {
        this.descripcionOrganismoDestinatario=descripcionOrganismoDestinatario;
    }
    
    public void setDescripcionDocumento(String descripcionDocumento) {
        this.descripcionDocumento=descripcionDocumento;
    }
    
    public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) {
        this.descripcionIdiomaDocumento=descripcionIdiomaDocumento;
    }
    
    public void setProcedenciaGeografica(String procedenciaGeografica) {
        this.procedenciaGeografica=procedenciaGeografica;
    }
    
    public void setIdiomaExtracto(String idiomaExtracto) {
        this.idiomaExtracto=idiomaExtracto;
    }
    
    public void setdataentrada(String dataentrada) {
        this.dataentrada=dataentrada;
    }
    
    public void setDataVisado(String dataVisado) {
        this.dataVisado=dataVisado;
    }
    
    public void sethora(String hora) {
        this.hora=hora;
    }
    
    public void setoficina(String oficina) {
        this.oficina=oficina;
    }
    
    public void setoficinafisica(String oficinafisica) {
        this.oficinafisica=oficinafisica;
      
    }
    
    public void setdata(String data) {
        this.data=data;
    }
    
    public void settipo(String tipo) {
        this.tipo=tipo.toUpperCase();
    }
    
    public void setidioma(String idioma) {
        this.idioma=idioma;
    }
    
    public void setentidad1(String entidad1) {
        this.entidad1=entidad1.toUpperCase();
    }
    
    public void setentidad2(String entidad2) {
        this.entidad2=entidad2;
    }
    
    public void setaltres(String altres) {
        this.altres=altres;
    }
    
    public void setbalears(String balears) {
        this.balears=balears;
    }
    
    public void setfora(String fora) {
        this.fora=fora;
    }
    
    public void setsalida1(String salida1) {
        this.salida1=salida1;
    }
    
    public void setsalida2(String salida2) {
        this.salida2=salida2;
    }
    
    public void setdestinatari(String destinatari) {
        this.destinatari=destinatari;
    }
    
    public void setidioex(String idioex) {
        this.idioex=idioex;
    }
    
    public void setdisquet(String disquet) {
        this.disquet=disquet;
    }
    
    public void setcomentario(String comentarioEntero) {
        if (comentarioEntero.length()>160) {
            comentarioEntero=comentarioEntero.substring(0, 160);
        }
        this.comentario=comentarioEntero;
    }
    
    public void setMotivo(String motivo) {
        this.motivo=motivo;
    }

    public void setEntidad1Nuevo(String entidad1Nuevo) {
        this.entidad1Nuevo=entidad1Nuevo;
    }

    public void setEntidad2Nuevo(String entidad2Nuevo) {
        this.entidad2Nuevo=entidad2Nuevo;
    }

    public void setAltresNuevo(String altresNuevo) {
        this.altresNuevo=altresNuevo;
    }

    public void setMunicipi060(String municipi060) {
        this.municipi060=municipi060;
    }

    public void setComentarioNuevo(String comentarioNuevoEntero) {
        if (comentarioNuevoEntero.length()>160) {
            comentarioNuevoEntero=comentarioNuevoEntero.substring(0, 160);
        }
        this.comentarioNuevo=comentarioNuevoEntero;
    }

    public void fijaUsuario(String usuario) {
        this.usuario=usuario.toUpperCase();
    }

    public String getUsuario() {
        return usuario;
    }

    public String getPassword() {
        return password;
    }


    public void fijaPasswordUser(String password) {
        this.password=password;
    }

    public boolean getValidado() {
        return validado;
    }

     public void setValidado(boolean es_valido) {
        this.validado=es_valido;
    }
    
    /* Devolvemos la serie de errores */
    
    public Hashtable getErrores() {
        return errores;
    }
    
    /* Devolvemos si el registro se ha grabado bien */
    public boolean getGrabado() {
        return registroGrabado;
    }

    /* Devolvemos si el registro se ha actualizado  bien */
    public boolean getActualizado() {
        return registroActualizado;
    }
    
    /* Devolvemos valores para mostrarlos */
    
    public String getOficina() {
        return oficina;
    }

    public String getOficinafisica() {
        return oficinafisica;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMunicipi060() {
        return municipi060;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getRegistroAnulado() {
        return registroAnulado;
    }

    public String getEntidad1() {
        return entidad1;
    }
    
    public String getEntidad2() {
        return entidad2;
    }

    public String getAltres() {
        return altres;
    }

    public String getBalears() {
        return balears;
    }

    public String getFora() {
        return fora;
    }

    public String getSalida1() {
        return salida1;
    }

    public String getSalida2() {
        return salida2;
    }

    public String getDestinatari() {
        return destinatari;
    }

    public String getIdioex() {
        return idioex;
    }

    public String getDisquet() {
        return disquet;
    }

    public String getComentario() {
        return comentario;
    }

    public String getHora() {
        return hora;
    }

    public String getDataEntrada() {
        return dataentrada;
    }
    
    public String getDataVisado() {
        return dataVisado;
    }
    
    public String getData() {
        return data;
    }

    public String getNumero() {
        return String.valueOf(fzanume);
    }
    
    public String getAnoEntrada() {
        return anoEntrada;
    }

    public String getNumeroEntrada() {
        return numeroEntrada;
    }

    public String getDescripcionOficina() {
        return descripcionOficina;
    }

    public String getDescripcionOficinaFisica() {
        return descripcionOficinaFisica;
    }

    public String getDescripcionRemitente() {
        return descripcionRemitente;
    }

    public String getDescripcionMunicipi060() {
        return descripcioMunicipi060;
    }

    public void setDescripcionMunicipi060(String descripcioMunicipi060) {
        this.descripcioMunicipi060=descripcioMunicipi060;
    }

    public String getDescripcionOrganismoDestinatario() {
        return descripcionOrganismoDestinatario;
    }

    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    public String getDescripcionIdiomaDocumento() {
        return descripcionIdiomaDocumento;
    }

    public String getCorreo() {
        return correo;
    }

    public String getProcedenciaGeografica() {
        return procedenciaGeografica;
    }

    public String getIdiomaExtracto() {
        return idiomaExtracto;
    }

    public boolean getLeido() {
        return leidos;
    }
    
    public void setLeido(boolean leidos) {
        this.leidos=leidos;
    }

    public boolean getActualizacion() {
        return actualizacion;
    }

    public String getEntidad1Nuevo() {
        return entidad1Nuevo;
    }

    public String getEntidad2Nuevo() {
        return entidad2Nuevo;
    }

    public String getAltresNuevo() {
        return altresNuevo;
    }

    public String getComentarioNuevo() {
        return comentarioNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getEntidad1Grabada() {
        return entidad1Grabada;
    }

    public void setEntidad1Grabada(String entidad1Grabada) {
        this.entidad1Grabada=entidad1Grabada;
    }

	public String getNumeroDocumentosRegistro060() {
		return numeroDocumentosRegistro060;
	}

	public void setNumeroDocumentosRegistro060(String numeroDocumentosRegistro060) {
		this.numeroDocumentosRegistro060 = numeroDocumentosRegistro060;
	}
}