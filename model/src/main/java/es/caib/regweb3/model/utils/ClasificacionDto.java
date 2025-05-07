package es.caib.regweb3.model.utils;

import java.util.Date;

/**
 * 
 * @author Limit Tecnolgoies S.L
 *
 */
public class ClasificacionDto {

	private Long registroId;
	
	private DocumentoVisor documento;
	
	private Long codigoSia;
	
	private String retardo;
	
	private Date caducidad;

	public Long getRegistroId() {
		return registroId;
	}

	public void setRegistroId(Long registroId) {
		this.registroId = registroId;
	}

	public DocumentoVisor getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoVisor documento) {
		this.documento = documento;
	}

	public Long getCodigoSia() {
		return codigoSia;
	}

	public void setCodigoSia(Long codigoSia) {
		this.codigoSia = codigoSia;
	}

	public String getRetardo() {
		return retardo;
	}

	public void setRetardo(String retardo) {
		this.retardo = retardo;
	}

	public Date getCaducidad() {
		return caducidad;
	}

	public void setCaducidad(Date caducidad) {
		this.caducidad = caducidad;
	}
	
}
