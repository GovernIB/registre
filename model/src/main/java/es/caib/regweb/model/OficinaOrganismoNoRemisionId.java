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
public class OficinaOrganismoNoRemisionId implements java.io.Serializable {

	private Oficina oficina;
	private Organismo organismo;

	public OficinaOrganismoNoRemisionId() {
	}

	public OficinaOrganismoNoRemisionId(Oficina oficina, Organismo organismo) {
		this.oficina = oficina;
		this.organismo = organismo;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="FZFCAGCO", nullable = false, referencedColumnName = "FAACAGCO")
	public Oficina getOficina() {
		return this.oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="FZFCORGA", nullable = false, referencedColumnName = "FAXCORGA")
	public Organismo getOrganismo() {
		return this.organismo;
	}

	public void setOrganismo(Organismo organismo) {
		this.organismo = organismo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OficinaOrganismoId))
			return false;
		OficinaOrganismoId castOther = (OficinaOrganismoId) other;

		return (this.getOficina() == castOther.getOficina())
				&& (this.getOrganismo() == castOther.getOrganismo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getOficina().getCodigo();
		result = 37 * result + this.getOrganismo().getCodigo();
		return result;
	}

}
