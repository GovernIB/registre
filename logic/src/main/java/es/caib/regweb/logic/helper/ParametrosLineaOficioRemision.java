package es.caib.regweb.logic.helper;

import java.util.Hashtable;

public class ParametrosLineaOficioRemision {
    
    private String usuario="";

    private boolean error=false;
    private boolean leidos=false;
    private Hashtable errores=new Hashtable();

    private boolean registroGrabado=false;
    private boolean registroActualizado=false;
   
    private String anoEntrada=null;
    private String numeroEntrada=null;
    private String oficinaEntrada=null;
    private String usuarioEntrada="";
    private String descartadoEntrada="";
    private String motivosDescarteEntrada="";

    private String anoOficio=null;
    private String numeroOficio=null;
    private String oficinaOficio=null;
    

    
    /* Grabamos registro si las validaciones son correctas */
    
    public String getAnoEntrada() {
		return anoEntrada;
	}


    public void setAnoEntrada(String anoEntrada) {
		this.anoEntrada = anoEntrada;
	}


    public String getNumeroEntrada() {
		return numeroEntrada;
	}


    public void setNumeroEntrada(String numeroEntrada) {
		this.numeroEntrada = numeroEntrada;
	}


    public String getOficinaEntrada() {
		return oficinaEntrada;
	}


    public void setOficinaEntrada(String oficinaEntrada) {
		this.oficinaEntrada = oficinaEntrada;
	}


    public String getUsuarioEntrada() {
		return usuarioEntrada;
	}


    public void setUsuarioEntrada(String usuarioEntrada) {
		this.usuarioEntrada = usuarioEntrada;
	}


    public String getDescartadoEntrada() {
		return descartadoEntrada;
	}


    public void setDescartadoEntrada(String descartadoEntrada) {
		this.descartadoEntrada = descartadoEntrada;
	}


    public String getMotivosDescarteEntrada() {
		return motivosDescarteEntrada;
	}


    public void setMotivosDescarteEntrada(String motivosDescarteEntrada) {
		this.motivosDescarteEntrada = motivosDescarteEntrada;
	}




    public String getAnoOficio() {
		return anoOficio;
	}


    public void setAnoOficio(String anoOficio) {
		this.anoOficio = anoOficio;
	}


    public String getNumeroOficio() {
		return numeroOficio;
	}


    public void setNumeroOficio(String numeroOficio) {
		this.numeroOficio = numeroOficio;
	}


    public String getOficinaOficio() {
		return oficinaOficio;
	}


    public void setOficinaOficio(String oficinaOficio) {
		this.oficinaOficio = oficinaOficio;
	}


    public String getUsuario() {
		return usuario;
	}


    public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


    public Hashtable getErrores() {
		return errores;
	}


    public void setErrores(Hashtable errores) {
		this.errores = errores;
	}


    public boolean isLeidos() {
		return leidos;
	}


    public void setLeidos(boolean leidos) {
		this.leidos = leidos;
	}

    
    /* Devolvemos si el registro se ha grabado bien */
    public boolean getGrabado() {
        return registroGrabado;
    }

    /* Devolvemos si el registro se ha actualizado  bien */
    public boolean getActualizado() {
        return registroActualizado;
    }
    
       

}