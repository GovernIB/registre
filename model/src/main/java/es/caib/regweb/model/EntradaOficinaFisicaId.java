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
public class EntradaOficinaFisicaId implements java.io.Serializable {

	private Entrada entrada;

	public EntradaOficinaFisicaId() {
	}

	public EntradaOficinaFisicaId(Entrada entrada) {
		this.entrada = entrada;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumns ({
        @JoinColumn(name="FOEANOEN", nullable = false, referencedColumnName = "FZAANOEN"),
        @JoinColumn(name="FOENUMEN", nullable = false, referencedColumnName = "FZANUMEN"),
        @JoinColumn(name="FOECAGCO", nullable = false, referencedColumnName = "FZACAGCO")
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
		if (!(other instanceof EntradaOficinaFisicaId))
			return false;
		EntradaOficinaFisicaId castOther = (EntradaOficinaFisicaId) other;

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
