package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class TipoAgrupacionGeograficaId implements java.io.Serializable {

	private int tipo;
	private int fechaBaja;

	public TipoAgrupacionGeograficaId() {
	}

	public TipoAgrupacionGeograficaId(int tipo, int fechaBaja) {
		this.tipo = tipo;
		this.fechaBaja = fechaBaja;
	}

	@Column(name = "FLDCTAGG", nullable = false, precision = 2, scale = 0)
	public int getTipo() {
		return this.tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	@Column(name = "FLDFBAJA", nullable = false, precision = 6, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TipoAgrupacionGeograficaId))
			return false;
		TipoAgrupacionGeograficaId castOther = (TipoAgrupacionGeograficaId) other;

		return (this.getTipo() == castOther.getTipo())
				&& (this.getFechaBaja() == castOther.getFechaBaja());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTipo();
		result = 37 * result + this.getFechaBaja();
		return result;
	}

}
