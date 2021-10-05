package org.plugin.geiser.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InteresadoG {

	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private TipoDocumento tipoDocumento;
	private String documento;
	private String email;
	private String telefono;
	private CanalNotificacion canalNotificacion;
	private String pais;
	private String provincia;
	private String municipio;
	private String direccion;
	private String cp;
	private String razonSocial;
	private String direccionElectronica;
	private String observaciones;
	private InteresadoG representante;
	
}
