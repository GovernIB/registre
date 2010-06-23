package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class TipoDocumentoId implements java.io.Serializable {

	private String codigo;
	private int fechaBaja;

	public TipoDocumentoId() {
	}

	public TipoDocumentoId(String codigo, int fechaBaja) {
		this.codigo = codigo;
		this.fechaBaja = fechaBaja;
	}

	@Column(name = "FZICTIPE", nullable = false, length = 2)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "FZIFBAJA", nullable = false, precision = 8, scale = 0)
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
		if (!(other instanceof TipoDocumentoId))
			return false;
		TipoDocumentoId castOther = (TipoDocumentoId) other;

		return ((this.getCodigo().equals(castOther.getCodigo())) || (this
				.getCodigo() != null
				&& castOther.getCodigo() != null && this.getCodigo()
				.equals(castOther.getCodigo())))
				&& (this.getFechaBaja() == castOther.getFechaBaja());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodigo() == null ? 0 : this.getCodigo().hashCode());
		result = 37 * result + this.getFechaBaja();
		return result;
	}

}
