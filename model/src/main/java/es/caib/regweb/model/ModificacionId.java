package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class ModificacionId implements java.io.Serializable {

	private String codigoEntradaSalida;
	private int anyoEntradaSalida;
	private int numeroEntradaSalida;
	private int oficinaEntradaSalida;
	private String usuarioModificacion;
	private int fechaModficacion;
	private int horaModificacion;

	public ModificacionId() {
	}

	public ModificacionId(String codigoEntradaSalida, int anyoEntradaSalida, int numeroEntradaSalida,
			int oficinaEntradaSalida, String usuarioModificacion, int fechaModficacion, int horaModificacion) {
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.anyoEntradaSalida = anyoEntradaSalida;
		this.numeroEntradaSalida = numeroEntradaSalida;
		this.oficinaEntradaSalida = oficinaEntradaSalida;
		this.usuarioModificacion = usuarioModificacion;
		this.fechaModficacion = fechaModficacion;
		this.horaModificacion = horaModificacion;
	}

	@Column(name = "FZJCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZJANOEN", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaSalida() {
		return this.anyoEntradaSalida;
	}

	public void setAnyoEntradaSalida(int anyoEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	@Column(name = "FZJNUMEN", nullable = false, precision = 5, scale = 0)
	public int getNumeroEntradaSalida() {
		return this.numeroEntradaSalida;
	}

	public void setNumeroEntradaSalida(int numeroEntradaSalida) {
		this.numeroEntradaSalida = numeroEntradaSalida;
	}

	@Column(name = "FZJCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficinaEntradaSalida() {
		return this.oficinaEntradaSalida;
	}

	public void setOficinaEntradaSalida(int oficinaEntradaSalida) {
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	@Column(name = "FZJCUSMO", nullable = false, length = 10)
	public String getUsuarioModificacion() {
		return this.usuarioModificacion;
	}

	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	@Column(name = "FZJFMODI", nullable = false, precision = 8, scale = 0)
	public int getFechaModficacion() {
		return this.fechaModficacion;
	}

	public void setFechaModficacion(int fechaModficacion) {
		this.fechaModficacion = fechaModficacion;
	}

	@Column(name = "FZJHMODI", nullable = false, precision = 8, scale = 0)
	public int getHoraModificacion() {
		return this.horaModificacion;
	}

	public void setHoraModificacion(int horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModificacionId))
			return false;
		ModificacionId castOther = (ModificacionId) other;

		return (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getAnyoEntradaSalida() == castOther.getAnyoEntradaSalida())
				&& (this.getNumeroEntradaSalida() == castOther.getNumeroEntradaSalida())
				&& (this.getOficinaEntradaSalida() == castOther.getOficinaEntradaSalida())
				&& ((this.getUsuarioModificacion().equals(castOther.getUsuarioModificacion())) || (this
						.getUsuarioModificacion() != null
						&& castOther.getUsuarioModificacion() != null && this
						.getUsuarioModificacion().equals(castOther.getUsuarioModificacion())))
				&& (this.getFechaModficacion() == castOther.getFechaModficacion())
				&& (this.getHoraModificacion() == castOther.getHoraModificacion());
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getAnyoEntradaSalida();
		result = 37 * result + this.getNumeroEntradaSalida();
		result = 37 * result + this.getOficinaEntradaSalida();
		result = 37 * result
				+ (getUsuarioModificacion() == null ? 0 : this.getUsuarioModificacion().hashCode());
		result = 37 * result + this.getFechaModficacion();
		result = 37 * result + this.getHoraModificacion();
		return result;
	}

}
