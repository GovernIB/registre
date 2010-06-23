package es.caib.regweb.logic.helper;

import java.util.*;

public class ParametrosRegistroSalida {
	
    private String dataVisado="";
	private String datasalida="";
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
	private String entrada1="";
	private String entrada2="";
	private String remitent="";
	private String idioex="";
	private String disquet="";
	private String comentario="";
	private String usuario;
	private int fzsnume=0;
	private String correo="";
	private String registroAnulado="";
	private boolean actualizacion=false;
	private boolean registroActualizado=false;
	private boolean leidos=false;
	private String motivo="";
	private String entidad1Nuevo="";
	private String entidad2Nuevo="";
	private String altresNuevo="";
	private String comentarioNuevo="";
	private String password="";
		
	private boolean error=false;
	private boolean validado=false;
	private boolean registroSalidaGrabado=false;
	private Hashtable errores=new Hashtable();
	private String entidadCastellano=null;
	
	private String anoSalida=null;
	private String numeroSalida=null;
	private String descripcionOficina=null;
	private String descripcionOficinaFisica=null;
	private String descripcionDestinatario=null;
	private String descripcionOrganismoRemitente=null;
	private String descripcionDocumento=null;
	private String descripcionIdiomaDocumento=null;
	private String destinoGeografico=null;
	private String idiomaExtracto=null;


    public void setAnoSalida(String anoSalida) {
		this.anoSalida=anoSalida;
	}

    public void setActualizacion(boolean actualizacion) {
		this.actualizacion=actualizacion;
	}

    public boolean getActualizacion() {
		return actualizacion;
	}


    public void fijaPasswordUser(String password) {
		this.password=password;
	}

    public String getPassword() {
		return password;
	}
	
	public void setEntidadCastellano(String entidadCastellano) {
		this.entidadCastellano=entidadCastellano;
	}

    public String getEntidadCastellano() {
		return entidadCastellano;
	}
    
    public void setRegistroAnulado(String registroAnulado) {
		this.registroAnulado=registroAnulado;
	}
	
    public void setNumeroSalida(String numeroSalida) {
		this.numeroSalida=numeroSalida;
	}
	
    public void setDescripcionOficina(String descripcionOficina) {
		this.descripcionOficina=descripcionOficina;
	}
	
    public void setDescripcionOficinaFisica(String descripcionOficinaFisica) {
		this.descripcionOficinaFisica=descripcionOficinaFisica;
	}
	
    public void setDescripcionDestinatario(String descripcionDestinatario) {
		this.descripcionDestinatario=descripcionDestinatario;
	}

    public void setCorreo(String correo) {
		this.correo=correo;
	}

    public void setDescripcionOrganismoRemitente(String descripcionOrganismoRemitente) {
		this.descripcionOrganismoRemitente=descripcionOrganismoRemitente;
	}

    public void setDescripcionDocumento(String descripcionDocumento) {
		this.descripcionDocumento=descripcionDocumento;
	}

    public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) {
		this.descripcionIdiomaDocumento=descripcionIdiomaDocumento;
	}

    public void setDestinoGeografico(String destinoGeografico) {
		this.destinoGeografico=destinoGeografico;
	}

    public void setIdiomaExtracto(String idiomaExtracto) {
		this.idiomaExtracto=idiomaExtracto;
	}
	
    public void setdatasalida(String datasalida) {
		this.datasalida=datasalida;
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
		this.entidad1=entidad1.toUpperCase().trim();
	}

    public void setentidad2(String entidad2) {
		this.entidad2=entidad2.trim();
	}

    public void setaltres(String altres) {
		this.altres=altres.trim();
	}

    public void setbalears(String balears) {
		this.balears=balears.trim();
	}

    public void setfora(String fora) {
		this.fora=fora.trim();
	}

    public void setentrada1(String entrada1) {
		this.entrada1=entrada1.trim();
	}

    public void setentrada2(String entrada2) {
		this.entrada2=entrada2.trim();
	}

    public void setremitent(String remitent) {
		this.remitent=remitent.trim();
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
	
    public void setDataVisado(String dataVisado) {
		this.dataVisado=dataVisado;
	}
	
    public boolean getValidado() {
		return validado;
	}
	
    public boolean getError() {
		return error;
	}
	
    public Hashtable getErrores() {
		return errores;
	}
	
    public boolean getGrabado() {
		return registroSalidaGrabado;
	}
	
    public String getOficina() {
		return oficina;
	}

    public String getOficinafisica() {
		return oficinafisica;
	}

    public String getCorreo() {
		return correo;
	}

    public String getTipo() {
		return tipo;
	}

    public String getRegistroAnulado() {
		return registroAnulado;
	}

    public String getIdioma() {
		return idioma;
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

    public String getEntrada1() {
		return entrada1;
	}

    public String getEntrada2() {
		return entrada2;
	}

    public String getRemitent() {
		return remitent;
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

    public String getDataSalida() {
		return datasalida;
	}

    public String getData() {
		return data;
	}

    public String getNumero() {
		return String.valueOf(fzsnume);
	}
	
    public int getFzsnume() {
		return fzsnume;
	}
	
    public String getAnoSalida() {
		return anoSalida;
	}

    public String getNumeroSalida() {
		return numeroSalida;
	}

    public String getDescripcionOficina() {
		return descripcionOficina;
	}

    public String getDescripcionOficinaFisica() {
		return descripcionOficinaFisica;
	}
	
    public String getDescripcionDestinatario() {
		return descripcionDestinatario;
	}
	
    public String getDescripcionOrganismoRemitente() {
		return descripcionOrganismoRemitente;
	}
	
    public String getDescripcionDocumento() {
		return descripcionDocumento;
	}
	
    public String getDescripcionIdiomaDocumento() {
		return descripcionIdiomaDocumento;
	}
	
    public String getDestinoGeografico() {
		return destinoGeografico;
	}

    public String getIdiomaExtracto() {
		return idiomaExtracto;
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

    public boolean getLeido() {
		return leidos;
	}

    public void setLeido(boolean leidos) {
        this.leidos=leidos;
    }

    public String getDataVisado() {
		return dataVisado;
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

    public boolean getActualizado() {
		return registroActualizado;
	}
	
	

}