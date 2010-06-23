package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class OficioRemisionId implements java.io.Serializable {

	private int anyoOficio;
	private int oficinaOficio;
	private int numeroOficio;

	public OficioRemisionId() {
	}

	public OficioRemisionId(int anyoOficio, int oficinaOficio, int numeroOficio) {
		this.anyoOficio = anyoOficio;
		this.oficinaOficio = oficinaOficio;
		this.numeroOficio = numeroOficio;
	}

	@Column(name = "REM_OFANY", nullable = false, precision = 4, scale = 0)
	public int getAnyoOficio() {
		return this.anyoOficio;
	}

	public void setAnyoOficio(int anyoOficio) {
		this.anyoOficio = anyoOficio;
	}

	@Column(name = "REM_OFOFI", nullable = false, precision = 2, scale = 0)
	public int getOficinaOficio() {
		return this.oficinaOficio;
	}

	public void setOficinaOficio(int oficinaOficio) {
		this.oficinaOficio = oficinaOficio;
	}

	@Column(name = "REM_OFNUM", nullable = false, precision = 5, scale = 0)
	public int getNumeroOficio() {
		return this.numeroOficio;
	}

	public void setNumeroOficio(int numeroOficio) {
		this.numeroOficio = numeroOficio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OficioRemisionId))
			return false;
		OficioRemisionId castOther = (OficioRemisionId) other;

		return (this.getAnyoOficio() == castOther.getAnyoOficio())
				&& (this.getOficinaOficio() == castOther.getOficinaOficio())
				&& (this.getNumeroOficio() == castOther.getNumeroOficio());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAnyoOficio();
		result = 37 * result + this.getOficinaOficio();
		result = 37 * result + this.getNumeroOficio();
		return result;
	}

}
