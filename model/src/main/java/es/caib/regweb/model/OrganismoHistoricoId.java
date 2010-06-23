package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class OrganismoHistoricoId implements java.io.Serializable {

	private int codigo;
	private int fechaAlta;

	public OrganismoHistoricoId() {
	}

	public OrganismoHistoricoId(int codigo, int fechaAlta) {
		this.codigo = codigo;
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "FHXCORGA", nullable = false, precision = 4, scale = 0)
	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	@Column(name = "FHXFALTA", nullable = false, precision = 8, scale = 0)
	public int getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(int fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OrganismoHistoricoId))
			return false;
		OrganismoHistoricoId castOther = (OrganismoHistoricoId) other;

		return (this.getCodigo() == castOther.getCodigo())
				&& (this.getFechaAlta() == castOther.getFechaAlta());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodigo();
		result = 37 * result + this.getFechaAlta();
		return result;
	}

}
