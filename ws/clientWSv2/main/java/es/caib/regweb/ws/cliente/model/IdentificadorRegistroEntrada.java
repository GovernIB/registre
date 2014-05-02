package es.caib.regweb.ws.cliente.model;

public final class IdentificadorRegistroEntrada extends IdentificadorRegistro {

	public IdentificadorRegistroEntrada() {
		tipoRegistro = TIPO_REGISTRO_ENTRADA;
	}

	public IdentificadorRegistroEntrada(String numOficinaRegistro,
			String numeroRegistro, String anyoRegistro) {
		super(numOficinaRegistro, numeroRegistro, anyoRegistro);
		tipoRegistro = TIPO_REGISTRO_ENTRADA;
	}

}
