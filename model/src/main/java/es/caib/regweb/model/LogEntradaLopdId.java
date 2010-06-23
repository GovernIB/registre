package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class LogEntradaLopdId implements java.io.Serializable {

	private String tipoAcceso;
	private String usuario;
	private int fecha;
	private int hora;
	private int numero;
	private int anyo;
	private int oficina;

	public LogEntradaLopdId() {
	}

	public LogEntradaLopdId(String tipoAcceso, String usuario, int fecha,
			int hora, int numero, int anyo, int oficina) {
		this.tipoAcceso = tipoAcceso;
		this.usuario = usuario;
		this.fecha = fecha;
		this.hora = hora;
		this.numero = numero;
		this.anyo = anyo;
		this.oficina = oficina;
	}

	@Column(name = "FZTTIPAC", nullable = false, length = 10)
	public String getTipoAcceso() {
		return this.tipoAcceso;
	}

	public void setTipoAcceso(String tipoAcceso) {
		this.tipoAcceso = tipoAcceso;
	}

	@Column(name = "FZTCUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FZTDATAC", nullable = false, precision = 8, scale = 0)
	public int getFecha() {
		return this.fecha;
	}

	public void setFecha(int fecha) {
		this.fecha = fecha;
	}

	@Column(name = "FZTHORAC", nullable = false, precision = 9, scale = 0)
	public int getHora() {
		return this.hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	@Column(name = "FZTNUMEN", nullable = false, precision = 5, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Column(name = "FZTANOEN", nullable = false, precision = 4, scale = 0)
	public int getAnyo() {
		return this.anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	@Column(name = "FZTCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficina() {
		return this.oficina;
	}

	public void setOficina(int oficina) {
		this.oficina = oficina;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LogEntradaLopdId))
			return false;
		LogEntradaLopdId castOther = (LogEntradaLopdId) other;

		return ((this.getTipoAcceso().equals(castOther.getTipoAcceso())) || (this
				.getTipoAcceso() != null
				&& castOther.getTipoAcceso() != null && this.getTipoAcceso()
				.equals(castOther.getTipoAcceso())))
				&& ((this.getUsuario().equals(castOther.getUsuario())) || (this
						.getUsuario() != null
						&& castOther.getUsuario() != null && this.getUsuario()
						.equals(castOther.getUsuario())))
				&& (this.getFecha() == castOther.getFecha())
				&& (this.getHora() == castOther.getHora())
				&& (this.getNumero() == castOther.getNumero())
				&& (this.getAnyo() == castOther.getAnyo())
				&& (this.getOficina() == castOther.getOficina());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTipoAcceso() == null ? 0 : this.getTipoAcceso().hashCode());
		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37 * result + this.getFecha();
		result = 37 * result + this.getHora();
		result = 37 * result + this.getNumero();
		result = 37 * result + this.getAnyo();
		result = 37 * result + this.getOficina();
		return result;
	}

}
