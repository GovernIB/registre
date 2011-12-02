package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumns;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;

/**

 */
@Embeddable
public class OficinaOrganismoPermetEnviarEmailId implements java.io.Serializable {

	private int oficina;
	private int organismo;

	public OficinaOrganismoPermetEnviarEmailId() {
	}

	public OficinaOrganismoPermetEnviarEmailId(int oficina, int organismo) {
		this.oficina = oficina;
		this.organismo = organismo;
	}

	@Column(name = "OFO_CODIOFI", nullable = false, precision = 2, scale = 0)
	public int getOficina() {
		return this.oficina;
	}

	public void setOficina(int oficina) {
		this.oficina = oficina;
	}

	@Column(name = "OFO_CODIORG", nullable = false, precision = 4, scale = 0)
	public int getOrganismo() {
		return this.organismo;
	}

	public void setOrganismo(int organismo) {
		this.organismo = organismo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OficinaOrganismoPermetEnviarEmailId))
			return false;
		OficinaOrganismoPermetEnviarEmailId castOther = (OficinaOrganismoPermetEnviarEmailId) other;

		return (this.getOficina() == castOther.getOficina())
				&& (this.getOrganismo() == castOther.getOrganismo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getOficina();
		result = 37 * result + this.getOrganismo();
		return result;
	}

}
