package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionRegistroEnvioGeiser extends PeticionRegistroGeiser {

	private TipoEnvio tipoEnvio;
	
}
