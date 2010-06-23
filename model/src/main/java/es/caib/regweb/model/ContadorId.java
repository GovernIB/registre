package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class ContadorId implements java.io.Serializable {

	private int anyo;
	private String codigoEntradaSalida;
	private int oficina;

	public ContadorId() {
	}

	public ContadorId(int anyo, String codigoEntradaSalida, int oficina) {
		this.anyo = anyo;
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.oficina = oficina;
	}

	@Column(name = "FZDAENSA", nullable = false, precision = 4, scale = 0)
	public int getAnyo() {
		return this.anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	@Column(name = "FZDCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZDCAGCO", nullable = false, precision = 2, scale = 0)
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
		if (!(other instanceof ContadorId))
			return false;
		ContadorId castOther = (ContadorId) other;

		return (this.getAnyo() == castOther.getAnyo())
				&& (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getOficina() == castOther.getOficina());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAnyo();
        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getOficina();
		return result;
	}

}
