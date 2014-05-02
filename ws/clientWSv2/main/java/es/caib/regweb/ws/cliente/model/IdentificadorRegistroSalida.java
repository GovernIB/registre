package es.caib.regweb.ws.cliente.model;

public final class IdentificadorRegistroSalida extends IdentificadorRegistro {

	public IdentificadorRegistroSalida() {
		tipoRegistro = TIPO_REGISTRO_SALIDA;
	}

	public IdentificadorRegistroSalida(String numOficinaRegistro,
			String numeroRegistro, String anyoRegistro) {
		super(numOficinaRegistro, numeroRegistro, anyoRegistro);
		tipoRegistro = TIPO_REGISTRO_SALIDA;
	}

}
