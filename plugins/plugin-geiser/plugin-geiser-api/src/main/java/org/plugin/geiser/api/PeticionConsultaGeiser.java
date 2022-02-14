package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionConsultaGeiser extends Peticion {

	private String nuRegistro;
	private boolean incluirContenidoAnexo;
	private boolean incluirContenidoCSV;
}
