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
public class OficinaFisicaId implements java.io.Serializable {

	private Oficina oficina;
	private int codigoOficinaFisica;

	public OficinaFisicaId() {
	}

	public OficinaFisicaId(Oficina oficina, int codigoOficinaFisica) {
		this.oficina = oficina;
		this.codigoOficinaFisica = codigoOficinaFisica;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="FZOCAGCO", referencedColumnName = "FAACAGCO")
	public Oficina getOficina() {
		return this.oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	@Column(name = "OFF_CODI", nullable = false, precision = 4, scale = 0)
	public int getCodigoOficinaFisica() {
		return this.codigoOficinaFisica;
	}

	public void setCodigoOficinaFisica(int codigoOficinaFisica) {
		this.codigoOficinaFisica = codigoOficinaFisica;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OficinaFisicaId))
			return false;
		OficinaFisicaId castOther = (OficinaFisicaId) other;

		return (this.getOficina().getCodigo() == castOther.getOficina().getCodigo())
				&& (this.getCodigoOficinaFisica() == castOther.getCodigoOficinaFisica());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getOficina().getCodigo();
		result = 37 * result + this.getCodigoOficinaFisica();
		return result;
	}

}
