package org.plugin.geiser.api;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApunteRegistro {

	private String nuRegistro;
	private Date fechaRegistro;
	private TipoAsiento tipoAsiento;
	private String resumen;
	private EstadoRegistro estado;
	private DocumentacionFisica documentacionFisica;
	private String oficinaOrigen;
	private String organoOrigen;
	private String oficinaDestino;
	private String organoDestino;
	private List<AnexoG> anexos;
	private List<InteresadoG> interesados;
	
}
