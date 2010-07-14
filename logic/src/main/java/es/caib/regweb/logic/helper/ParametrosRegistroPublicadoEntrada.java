package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.*;
import java.text.*;

public class ParametrosRegistroPublicadoEntrada implements Serializable {
	
	private int anoEntrada;
	private int numero;
	private int oficina;
	private int numeroBOCAIB;
	private int fecha;
	private int pagina;
	private int lineas;
	private String contenido;
	private String observaciones;
	private boolean leido;

    public boolean getLeido() {
		return this.leido;
	}

    public void setLeido(boolean leido) {
		this.leido = leido;
	}

    public int getAnoEntrada() {
		return this.anoEntrada;
	}

    public void setAnoEntrada(int anoEntrada) {
		this.anoEntrada = anoEntrada;
	}

    public int getNumero() {
		return this.numero;
	}

    public void setNumero(int numero) {
		this.numero = numero;
	}

    public int getOficina() {
		return this.oficina;
	}

    public void setOficina(int oficina) {
		this.oficina = oficina;
	}

    public int getNumeroBOCAIB() {
		return this.numeroBOCAIB;
	}

    public void setNumeroBOCAIB(int numeroBOCAIB) {
		this.numeroBOCAIB = numeroBOCAIB;
	}

    public void setFecha(int fecha) {
		this.fecha = fecha;
	}

    public int getFecha() {
		return this.fecha;
	}

    public String getFechaTexto() {
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaBOIB=null;
		String fechaC1=String.valueOf(fecha);
		String data;
		try {
			fechaBOIB=yyyymmdd.parse(fechaC1);
			data=(ddmmyyyy.format(fechaBOIB));
		} catch (Exception e) {
			data=fechaC1;
		}
		return data;
	}

    public int getPagina() {
		return this.pagina;
	}

    public void setPagina(int pagina) {
		this.pagina = pagina;
	}

    public int getLineas() {
		return this.lineas;
	}

    public void setLineas(int lineas) {
		this.lineas = lineas;
	}

    public String getContenido() {
		return this.contenido;
	}

    public void setContenido(String contenido) {
		this.contenido = contenido;
	}

    public String getObservaciones() {
		return this.observaciones;
	}

    public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}