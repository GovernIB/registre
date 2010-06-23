package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class VisadoId implements java.io.Serializable {

	private String codigoEntradaSalida;
	private int anyo;
	private int numero;
	private int oficina;
	private int fechaVisado;
	private int horaVisado;

	public VisadoId() {
	}

	public VisadoId(String codigoEntradaSalida, int anyo, int numero,
			int oficina, int fechaVisado, int horaVisado) {
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.anyo = anyo;
		this.numero = numero;
		this.oficina = oficina;
		this.fechaVisado = fechaVisado;
		this.horaVisado = horaVisado;
	}

	@Column(name = "FZKCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZKANOEN", nullable = false, precision = 4, scale = 0)
	public int getAnyo() {
		return this.anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	@Column(name = "FZKNUMEN", nullable = false, precision = 5, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Column(name = "FZKCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficina() {
		return this.oficina;
	}

	public void setOficina(int oficina) {
		this.oficina = oficina;
	}

	@Column(name = "FZKFVISA", nullable = false, precision = 8, scale = 0)
	public int getFechaVisado() {
		return this.fechaVisado;
	}

	public void setFechaVisado(int fechaVisado) {
		this.fechaVisado = fechaVisado;
	}

	@Column(name = "FZKHVISA", nullable = false, precision = 8, scale = 0)
	public int getHoraVisado() {
		return this.horaVisado;
	}

	public void setHoraVisado(int horaVisado) {
		this.horaVisado = horaVisado;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VisadoId))
			return false;
		VisadoId castOther = (VisadoId) other;

		return (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getAnyo() == castOther.getAnyo())
				&& (this.getNumero() == castOther.getNumero())
				&& (this.getOficina() == castOther.getOficina())
				&& (this.getFechaVisado() == castOther.getFechaVisado())
				&& (this.getHoraVisado() == castOther.getHoraVisado());
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getAnyo();
		result = 37 * result + this.getNumero();
		result = 37 * result + this.getOficina();
		result = 37 * result + this.getFechaVisado();
		result = 37 * result + this.getHoraVisado();
		return result;
	}

}
