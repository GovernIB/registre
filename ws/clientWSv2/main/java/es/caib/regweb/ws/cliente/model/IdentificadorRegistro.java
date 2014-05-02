package es.caib.regweb.ws.cliente.model;

public abstract class IdentificadorRegistro {
	public static final String TIPO_REGISTRO_ENTRADA = "1";
	public static final String TIPO_REGISTRO_SALIDA = "2";
	
	private String numOficinaRegistro = null;
	private String numeroRegistro = null;
	private String anyoRegistro = null;
    String tipoRegistro = null;
    
	public IdentificadorRegistro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IdentificadorRegistro(String numOficinaRegistro, String numeroRegistro,
			String anyoRegistro) {
		super();
		this.numOficinaRegistro = numOficinaRegistro;
		this.numeroRegistro = numeroRegistro;
		this.anyoRegistro = anyoRegistro;
	}

	/**
	 * @return the numOficinaRegistro
	 */
	public String getNumOficinaRegistro() {
		return numOficinaRegistro;
	}

	/**
	 * @param numOficinaRegistro the numOficinaRegistro to set
	 */
	public void setNumOficinaRegistro(String numOficinaRegistro) {
		this.numOficinaRegistro = numOficinaRegistro;
	}

	/**
	 * @return the numeroRegistro
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * @return the anyoRegistro
	 */
	public String getAnyoRegistro() {
		return anyoRegistro;
	}

	/**
	 * @param anyoRegistro the anyoRegistro to set
	 */
	public void setAnyoRegistro(String anyoRegistro) {
		this.anyoRegistro = anyoRegistro;
	}


	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}
}
