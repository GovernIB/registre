package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PeticionBusquedaTramitGeiser extends PeticionBusquedaGeiser {

	private TipoDocumento tipoDocumentoInteresadoRepre;
	private String documentoInteresadoRepre;
	private boolean incluirEnviosSir;
	
}
