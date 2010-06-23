package es.caib.regweb.logic.helper;

import java.util.*;

public class ParametrosRegistroModificado {
	
    private int anoEntrada;
    private int anoSalida;
	private String entidad1;
	private String entidad1Catalan;
	private int entidad2;
	private String extracto;
	private String usuarioModificacion;
	private String usuarioVisado;
	private String indVisExtracto;
	private String indVisRemitente;
	private int numeroRegistro;
	private String remitente;
	private String motivo;
	private String fechaModificacion;
	private String horaModificacion;
	private boolean leido=false;
	private boolean hayVisadoRemitente=false;
	private boolean hayVisadoExtracto=false;
	private String idiomaExtracto="";
	private int fechaDocumento=0;
	private String tipoDocumento="";
	private int fechaRegistro=0;
	private int fzacagge=0;
	private int destinatario=0;
	private String idioma="";
	private String fora="";
	private String comentario="";
	private String altres="";
	private String entidad1Old="";
	private int entidad2Old=0;
	private String password="";
	
	private int oficina;
	
	/* Set's para la creacion del objeto RegistroModificadoEntrada */

    public void setFora(String fora) {
		this.fora=fora;
	}
    public String getFora() {
		return fora;
	}

    public void setIdioma(String idioma) {
		this.idioma=idioma;
	}
    public String getIdioma() {
		return idioma;
	}

    public void setIdiomaExtracto(String idiomaExtracto) {
		this.idiomaExtracto=idiomaExtracto;
	}
    public String getIdiomaExtracto() {
		return idiomaExtracto;
	}


    public void setDestinatario(int destinatario) {
		this.destinatario=destinatario;
	}
    public int getDestinatario() {
		return destinatario;
	}


    public void setAnoEntrada(int anoEntrada) {
		this.anoEntrada=anoEntrada;
	}

    public void setAnoSalida(int anoSalida) {
		this.anoSalida=anoSalida;
	}

    public void setOficina(int oficina) {
		this.oficina = oficina;
	}

    public void fijaPasswordUser(String password) {
		this.password = password;
	}

    public void setEntidad1(String entidad1) {
		this.entidad1=entidad1;
	}

    public void setEntidad1Old(String entidad1Old) {
		this.entidad1Old=entidad1Old;
	}
    public String getEntidad1Old() {
		return entidad1Old;
	}

    public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento=tipoDocumento;
	}
    public String getTipoDocumento() {
		return tipoDocumento;
	}

    public void setComentario(String comentario) {
		this.comentario=comentario;
	}
    public String getComentario() {
		return comentario;
	}

    public void setExtracto(String extracto) {
		this.extracto=extracto;
	}

    public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion=usuarioModificacion;
	}
    public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

    public void setUsuarioVisado(String usuarioVisado) {
		this.usuarioVisado=usuarioVisado;
	}

    public String getUsuarioVisado() {
		return usuarioVisado;
	}

    public void setIndVisExtracto(String indVisExtracto) {
		this.indVisExtracto=indVisExtracto;
	}

    public void setIndVisRemitente(String indVisRemitente) {
		this.indVisRemitente=indVisRemitente;
	}

    public void setEntidad2(int entidad2) {
		this.entidad2=entidad2;
	}

    public void setEntidad2Old(int entidad2Old) {
		this.entidad2Old=entidad2Old;
	}
    public int getEntidad2Old() {
		return entidad2Old;
	}

    public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento=fechaDocumento;
	}
    public int getFechaDocumento() {
		return fechaDocumento;
	}

    public void setFechaRegistro(int fechaRegistro) {
		this.fechaRegistro=fechaRegistro;
	}
    public int getFechaRegistro() {
		return fechaRegistro;
	}

    public void setFzacagge(int fzacagge) {
		this.fzacagge=fzacagge;
	}
    public int getFzacagge() {
		return fzacagge;
	}

    public void setNumeroRegistro(int numeroRegistro) {
		this.numeroRegistro=numeroRegistro;
	}

    public void setRemitente(String remitente) {
		this.remitente=remitente;
	}

    public void setMotivo(String motivo) {
		this.motivo=motivo;
	}

    public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion=fechaModificacion;
	}

    public String getFechaModificacion() {
		return fechaModificacion;
	}

    public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion=horaModificacion;
	}

    public String getHoraModificacion() {
		return horaModificacion;
	}

    public void setAltres(String altres) {
		this.altres=altres;
	}

    public String getAltres() {
		return altres;
	}

    public void setVisarRemitente(boolean hayVisadoRemitente) {
		this.hayVisadoRemitente=hayVisadoRemitente;
	}

    public void setVisarExtracto(boolean hayVisadoExtracto) {
		this.hayVisadoExtracto=hayVisadoExtracto;
	}

    public boolean isHayVisadoRemitente() {
		return hayVisadoRemitente;
	}

    public boolean isHayVisadoExtracto() {
		return hayVisadoExtracto;
	}

    public void setLeido(boolean leido) {
		this.leido=leido;
	}

    public boolean getLeido() {
		return leido;
	}

    public int getNumeroRegistro() {
		return numeroRegistro;
	}

    public int getAnoEntrada() {
		return anoEntrada;
	}

    public int getAnoSalida() {
		return anoSalida;
	}

    public int getOficina() {
		return oficina;
	}

    public String getMotivo() {
		return motivo;
	}

    public String getEntidad1Catalan() {
		return entidad1Catalan;
	}
    public void setEntidad1Catalan(String entidad1Catalan) {
		this.entidad1Catalan=entidad1Catalan;
	}

    public String getEntidad1() {
		return entidad1;
	}

    public int getEntidad2() {
		return entidad2;
	}

    public String getRemitente() {
		return remitente;
	}

    public String getExtracto() {
		return extracto;
	}

}