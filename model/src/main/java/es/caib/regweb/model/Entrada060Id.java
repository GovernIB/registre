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
public class Entrada060Id implements java.io.Serializable {

	private Entrada entrada;

	public Entrada060Id() {
	}

	public Entrada060Id(Entrada entrada) {
		this.entrada = entrada;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumns ({
        @JoinColumn(name="ENT_ANY", referencedColumnName = "FZAANOEN"),
        @JoinColumn(name="ENT_NUM", referencedColumnName = "FZANUMEN"),
        @JoinColumn(name="ENT_OFI", referencedColumnName = "FZACAGCO")
    })
	public Entrada getEntrada() {
		return this.entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof Entrada060Id))
			return false;
		Entrada060Id castOther = (Entrada060Id) other;

		return (this.getEntrada() == castOther.getEntrada());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getEntrada().getId().getAnyo();
		result = 37 * result + this.getEntrada().getId().getNumero();
		result = 37 * result + this.getEntrada().getId().getOficina();
		return result;
	}

}
