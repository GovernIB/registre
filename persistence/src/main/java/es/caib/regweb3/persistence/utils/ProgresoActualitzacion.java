package es.caib.regweb3.persistence.utils;

import java.util.ArrayList;
import java.util.List;

public class ProgresoActualitzacion {

	public enum TipoInfo {
		TITOL, WARNING, SUBTITOL, INFO, SUBINFO, TEMPS, SEPARADOR, ERROR
	}

	Integer progreso = 0;
	Integer numRegistrosSIR;
	Integer numRegistrosSIRRecuperados = 0;
	List<ActualitzacionInfo> info = new ArrayList<ProgresoActualitzacion.ActualitzacionInfo>();

	boolean error = false;
	String errorMsg;

	public void addInfo(TipoInfo tipus, String text) {
		info.add(new ActualitzacionInfo(tipus, text));
	}

	public void addSeparador() {
		info.add(new ActualitzacionInfo(TipoInfo.SEPARADOR, ""));
	}

	public void incrementRegistrosRecuperados() {
		this.numRegistrosSIRRecuperados++;
		double auxprogres = (this.numRegistrosSIRRecuperados.doubleValue() / this.numRegistrosSIR.doubleValue()) * 100;
		this.progreso = (int) auxprogres;
	}

	public Integer getProgreso() {
		return progreso;
	}

	public void setProgreso(Integer progreso) {
		this.progreso = progreso;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public class ActualitzacionInfo {
		TipoInfo tipo;
		String texto;
		
		public ActualitzacionInfo() {}
		
		public ActualitzacionInfo(TipoInfo tipo, String texto) {
			this.tipo = tipo;
			this.texto = texto;
		}

		public TipoInfo getTipo() {
			return tipo;
		}
		public void setTipo(TipoInfo tipo) {
			this.tipo = tipo;
		}
		public String getTexto() {
			return texto;
		}
		public void setTexto(String texto) {
			this.texto = texto;
		}
	}

	public Integer getNumRegistrosSIR() {
		return numRegistrosSIR;
	}

	public void setNumRegistrosSIR(Integer numRegistrosSIR) {
		this.numRegistrosSIR = numRegistrosSIR;
	}

	public Integer getNumRegistrosSIRRecuperados() {
		return numRegistrosSIRRecuperados;
	}

	public void setNumRegistrosSIRRecuperados(Integer numRegistrosSIRRecuperados) {
		this.numRegistrosSIRRecuperados = numRegistrosSIRRecuperados;
	}

	public List<ActualitzacionInfo> getInfo() {
		return info;
	}

	public void setInfo(List<ActualitzacionInfo> info) {
		this.info = info;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
