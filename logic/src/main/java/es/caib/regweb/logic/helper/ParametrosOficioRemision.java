package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.*;

public class ParametrosOficioRemision implements Serializable {
    
    private String usuario="";

    private boolean error=false;
    private boolean leidos=false;
    private Hashtable errores=new Hashtable();
    private boolean registroGrabado=false;
    private boolean registroActualizado=false;
   
    private String anoOficio=null;
    private String numeroOficio=null;
    private String oficinaOficio=null;
    private String fechaOficio="";
    private String descripcion="";

    private String nulo="";
    private String usuarioNulo="";
    private String motivosNulo="";
    private String fechaNulo="";

    private String anoSalida=null;
    private String numeroSalida=null;
    private String oficinaSalida=null;
    
    private String anoEntrada=null;
    private String numeroEntrada=null;
    private String oficinaEntrada=null;
    private String usuarioEntrada="";
    private String descartadoEntrada="";
    private String motivosDescarteEntrada="";
    private String fechaEntrada="";

    private String[] registros;
    

    public String[] getRegistros() {
		return registros;
	}

    public void setRegistros(String[] registros) {
		this.registros = registros;
	}

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

    public String getFechaEntrada() {
		return fechaEntrada;
	}

    public void setFechaEntrada(String fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

    public String getAnoSalida() {
		return anoSalida;
	}

    public void setAnoSalida(String anoSalida) {
		this.anoSalida = anoSalida;
	}

    public String getNumeroSalida() {
		return numeroSalida;
	}

    public void setNumeroSalida(String numeroSalida) {
		this.numeroSalida = numeroSalida;
	}

    public String getOficinaSalida() {
		return oficinaSalida;
	}

    public void setOficinaSalida(String oficinaSalida) {
		this.oficinaSalida = oficinaSalida;
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

    public String getFechaOficio() {
		return fechaOficio;
	}

    public void setFechaOficio(String fechaOficio) {
		this.fechaOficio = fechaOficio;
	}

    public String getDescripcion() {
		return descripcion;
	}

    public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

    public String getNulo() {
		return nulo;
	}

    public void setNulo(String nulo) {
		this.nulo = nulo;
	}

    public String getUsuarioNulo() {
		return usuarioNulo;
	}

    public void setUsuarioNulo(String usuarioNulo) {
		this.usuarioNulo = usuarioNulo;
	}

    public String getMotivosNulo() {
		return motivosNulo;
	}

    public void setMotivosNulo(String motivosNulo) {
		this.motivosNulo = motivosNulo;
	}

    public String getFechaNulo() {
		return fechaNulo;
	}

    public void setFechaNulo(String fechaNulo) {
		this.fechaNulo = fechaNulo;
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


    public boolean getGrabado() {
        return registroGrabado;
    }
    
    public boolean getActualizado() {
        return registroActualizado;
    }

    public void setGrabado(boolean es_grabado) {
        this.registroGrabado=es_grabado;
    }

    public void setActualizado(boolean es_actualizado) {
        this.registroActualizado=es_actualizado;
    }


}