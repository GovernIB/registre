package es.caib.regweb.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OficinaFisicaHistoricoId implements java.io.Serializable {

	private int codigoOficina;
	private int codigoOficinaFisica;
	private int fechaAlta;

    public OficinaFisicaHistoricoId() {
	}

	public OficinaFisicaHistoricoId(int codigoOficina, int codigoOficinaFisica, int fechaAlta, int fechaBaja) {
		this.codigoOficina = codigoOficina;
		this.codigoOficinaFisica = codigoOficinaFisica;
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "FZHCAGCO", nullable = false, precision = 2, scale = 0)
	public int getCodigoOficina() {
		return this.codigoOficina;
	}

	public void setCodigoOficina(int codigoOficina) {
		this.codigoOficina = codigoOficina;
	}

	@Column(name = "OFH_CODI", nullable = false, precision = 4, scale = 0)
	public int getCodigoOficinaFisica() {
		return this.codigoOficinaFisica;
	}

	public void setCodigoOficinaFisica(int codigoOficinaFisica) {
		this.codigoOficinaFisica = codigoOficinaFisica;
	}

	@Column(name = "OFH_FECALT", nullable = false, precision = 8, scale = 0)
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
		if (!(other instanceof OficinaFisicaHistoricoId))
			return false;
		OficinaFisicaHistoricoId castOther = (OficinaFisicaHistoricoId) other;

		return (this.getCodigoOficina() == castOther.getCodigoOficina())
				&& (this.getCodigoOficinaFisica() == castOther.getCodigoOficinaFisica())
			    && (this.getFechaAlta() == castOther.getFechaAlta());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodigoOficina();
		result = 37 * result + this.getCodigoOficinaFisica();
		result = 37 * result + this.getFechaAlta();
		return result;
	}

}
