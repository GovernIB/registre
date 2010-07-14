package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.Vector;

public class ParametrosReproUsuario implements Serializable {

	private String repro;
	private String codUsuario;
	private String nomRepro;
	private String tipRepro;
	
	public String getRepro(){
		return this.repro;
	}
	public String getUsuario(){
	    return this.codUsuario;	
	}
	public String getNombre(){
		return this.nomRepro;
	}
	public String getTipRepro(){
		return this.tipRepro;
	}
	public void setRepro(String repro){
		this.repro = repro;
	}
	public void setCodUsuario(String usuario){
		this.codUsuario = usuario;
	}
	public void setNombre(String nombre){
		this.nomRepro = nombre;
	}
	public void setTipRepro(String tipoRepro){
		this.tipRepro = tipoRepro;
	}	
}
