package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class LineaOficioRemisionId implements java.io.Serializable {

	private int anyoEntradaRegistro;
	private int oficinaEntradaRegistro;
	private int numeroEntradaRegistro;

	public LineaOficioRemisionId() {
	}

	public LineaOficioRemisionId(int anyoEntradaRegistro, int oficinaEntradaRegistro, int numeroEntradaRegistro) {
		this.anyoEntradaRegistro = anyoEntradaRegistro;
		this.oficinaEntradaRegistro = oficinaEntradaRegistro;
		this.numeroEntradaRegistro = numeroEntradaRegistro;
	}

	@Column(name = "REN_ENTANY", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaRegistro() {
		return this.anyoEntradaRegistro;
	}

	public void setAnyoEntradaRegistro(int anyoEntradaRegistro) {
		this.anyoEntradaRegistro = anyoEntradaRegistro;
	}

	@Column(name = "REN_ENTOFI", nullable = false, precision = 2, scale = 0)
	public int getOficinaEntradaRegistro() {
		return this.oficinaEntradaRegistro;
	}

	public void setOficinaEntradaRegistro(int oficinaEntradaRegistro) {
		this.oficinaEntradaRegistro = oficinaEntradaRegistro;
	}

	@Column(name = "REN_ENTNUM", nullable = false, precision = 5, scale = 0)
	public int getNumeroEntradaRegistro() {
		return this.numeroEntradaRegistro;
	}

	public void setNumeroEntradaRegistro(int numeroEntradaRegistro) {
		this.numeroEntradaRegistro = numeroEntradaRegistro;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LineaOficioRemisionId))
			return false;
		LineaOficioRemisionId castOther = (LineaOficioRemisionId) other;

		return (this.getAnyoEntradaRegistro() == castOther.getAnyoEntradaRegistro())
				&& (this.getOficinaEntradaRegistro() == castOther.getOficinaEntradaRegistro())
				&& (this.getNumeroEntradaRegistro() == castOther.getNumeroEntradaRegistro());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAnyoEntradaRegistro();
		result = 37 * result + this.getOficinaEntradaRegistro();
		result = 37 * result + this.getNumeroEntradaRegistro();
		return result;
	}

}
