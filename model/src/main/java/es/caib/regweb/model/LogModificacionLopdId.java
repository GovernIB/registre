package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class LogModificacionLopdId implements java.io.Serializable {

	private String tipoAcceso;
	private String usuario;
	private int fecha;
	private int hora;
	private String codigoEntradaSalida;
	private int numero;
	private int anyo;
	private int oficina;
	private int fechaModificacion;
	private int horaModificacion;

	public LogModificacionLopdId() {
	}

	public LogModificacionLopdId(String tipoAcceso, String usuario, int fecha,
			int hora, String codigoEntradaSalida, int numero, int anyo,
			int oficina, int fechaModificacion, int horaModificacion) {
		this.tipoAcceso = tipoAcceso;
		this.usuario = usuario;
		this.fecha = fecha;
		this.hora = hora;
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.numero = numero;
		this.anyo = anyo;
		this.oficina = oficina;
		this.fechaModificacion = fechaModificacion;
		this.horaModificacion = horaModificacion;
	}

	@Column(name = "FZVTIPAC", nullable = false, length = 10)
	public String getTipoAcceso() {
		return this.tipoAcceso;
	}

	public void setTipoAcceso(String tipoAcceso) {
		this.tipoAcceso = tipoAcceso;
	}

	@Column(name = "FZVCUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FZVDATAC", nullable = false, precision = 8, scale = 0)
	public int getFecha() {
		return this.fecha;
	}

	public void setFecha(int fecha) {
		this.fecha = fecha;
	}

	@Column(name = "FZVHORAC", nullable = false, precision = 9, scale = 0)
	public int getHora() {
		return this.hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	@Column(name = "FZVCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZVNUMEN", nullable = false, precision = 5, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Column(name = "FZVANOEN", nullable = false, precision = 4, scale = 0)
	public int getAnyo() {
		return this.anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	@Column(name = "FZVCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficina() {
		return this.oficina;
	}

	public void setOficina(int oficina) {
		this.oficina = oficina;
	}

	@Column(name = "FZVFMODI", nullable = false, precision = 8, scale = 0)
	public int getFechaModificacion() {
		return this.fechaModificacion;
	}

	public void setFechaModificacion(int fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	@Column(name = "FZVHMODI", nullable = false, precision = 8, scale = 0)
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
		if (!(other instanceof LogModificacionLopdId))
			return false;
		LogModificacionLopdId castOther = (LogModificacionLopdId) other;

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
				&& (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getNumero() == castOther.getNumero())
				&& (this.getAnyo() == castOther.getAnyo())
				&& (this.getOficina() == castOther.getOficina())
				&& (this.getFechaModificacion() == castOther.getFechaModificacion())
				&& (this.getHoraModificacion() == castOther.getHoraModificacion());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTipoAcceso() == null ? 0 : this.getTipoAcceso().hashCode());
		result = 37 * result
				+ (getUsuario() == null ? 0 : this.getUsuario().hashCode());
		result = 37 * result + this.getFecha();
		result = 37 * result + this.getHora();
        result = 37 * result 
                + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getNumero();
		result = 37 * result + this.getAnyo();
		result = 37 * result + this.getOficina();
		result = 37 * result + this.getFechaModificacion();
		result = 37 * result + this.getHoraModificacion();
		return result;
	}

}
