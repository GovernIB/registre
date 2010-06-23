package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class AutorizacionId implements java.io.Serializable {

	private String usuario;
	private String codigoAutorizacion;
	private int codigoOficina;

	public AutorizacionId() {
	}

	public AutorizacionId(String usuario, String codigoAutorizacion, int codigoOficina) {
		this.usuario = usuario;
		this.codigoAutorizacion = codigoAutorizacion;
		this.codigoOficina = codigoOficina;
	}

	@Column(name = "FZHCUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FZHCAUT", nullable = false, length = 2)
	public String getCodigoAutorizacion() {
		return this.codigoAutorizacion;
	}

	public void setCodigoAutorizacion(String codigoAutorizacion) {
		this.codigoAutorizacion = codigoAutorizacion;
	}

	@Column(name = "FZHCAGCO", nullable = false, precision = 2, scale = 0)
	public int getCodigoOficina() {
		return this.codigoOficina;
	}

	public void setCodigoOficina(int codigoOficina) {
		this.codigoOficina = codigoOficina;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AutorizacionId))
			return false;
		AutorizacionId castOther = (AutorizacionId) other;

		return ((this.getUsuario().equals(castOther.getUsuario())) || (this
				.getUsuario() != null
				&& castOther.getUsuario() != null && this.getUsuario().equals(
				castOther.getUsuario())))
				&& ((this.getCodigoAutorizacion().equals(castOther.getCodigoAutorizacion())) || (this
						.getCodigoAutorizacion() != null
						&& castOther.getCodigoAutorizacion() != null && this.getCodigoAutorizacion()
						.equals(castOther.getCodigoAutorizacion())))
				&& (this.getCodigoOficina() == castOther.getCodigoOficina());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37 * result
				+ (getCodigoAutorizacion() == null ? 0 : this.getCodigoAutorizacion().hashCode());
		result = 37 * result + this.getCodigoOficina();
		return result;
	}

}
