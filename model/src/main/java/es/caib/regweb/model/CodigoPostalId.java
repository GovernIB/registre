package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumns;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;

/**

 */
@Embeddable
public class CodigoPostalId implements java.io.Serializable {

	private AgrupacionGeografica agrupacionGeografica;

	public CodigoPostalId() {
	}

	public CodigoPostalId(AgrupacionGeografica agrupacionGeografica) {
		this.agrupacionGeografica = agrupacionGeografica;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumns ({
        @JoinColumn(name="F12CTAGG", nullable = false, referencedColumnName = "FABCTAGG"),
        @JoinColumn(name="F12CAGGE", nullable = false, referencedColumnName = "FABCAGGE")
    })
	public AgrupacionGeografica getAgrupacionGeografica() {
		return this.agrupacionGeografica;
	}

	public void setAgrupacionGeografica(AgrupacionGeografica agrupacionGeografica) {
		this.agrupacionGeografica = agrupacionGeografica;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CodigoPostalId))
			return false;
		CodigoPostalId castOther = (CodigoPostalId) other;

		return (this.getAgrupacionGeografica() == castOther.getAgrupacionGeografica());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int)this.getAgrupacionGeografica().getId().getTipo();
		result = 37 * result + (int)this.getAgrupacionGeografica().getId().getCodigo();
		return result;
	}

}
