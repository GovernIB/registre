package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class ReproId implements java.io.Serializable {

	private String usuario;
	private String nombre;
	private String tipo;

	public ReproId() {
	}

	public ReproId(String usuario, String nombre, String tipo) {
		this.usuario = usuario;
		this.nombre = nombre;
		this.tipo = tipo;
	}

	@Column(name = "FZCCUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FZCNREP", nullable = false, length = 50)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "FZTIREP", nullable = false, length = 10)
	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ReproId))
			return false;
		ReproId castOther = (ReproId) other;

		return ((this.getUsuario().equals(castOther.getUsuario())) || (this
				.getUsuario() != null
				&& castOther.getUsuario() != null && this.getUsuario().equals(
				castOther.getUsuario())))
				&& ((this.getNombre().equals(castOther.getNombre())) || (this
						.getNombre() != null
						&& castOther.getNombre() != null && this.getNombre()
						.equals(castOther.getNombre())))
				&& ((this.getTipo().equals(castOther.getTipo())) || (this
						.getTipo() != null
						&& castOther.getTipo() != null && this.getTipo()
						.equals(castOther.getTipo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37 * result
				+ (getNombre() == null ? 0 : this.getNombre().hashCode());
		result = 37 * result
				+ (getTipo() == null ? 0 : this.getTipo().hashCode());
		return result;
	}

}
