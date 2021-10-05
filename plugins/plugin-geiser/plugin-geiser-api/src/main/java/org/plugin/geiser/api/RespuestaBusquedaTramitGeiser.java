package org.plugin.geiser.api;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaBusquedaTramitGeiser extends Respuesta {

	private int nuTotalApuntes;
	private List<EstadoTramitacion> estadosTramitacionRegistro;
	
}
