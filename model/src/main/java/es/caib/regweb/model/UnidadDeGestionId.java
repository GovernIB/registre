package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class UnidadDeGestionId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int codigoUnidad;
	private int codigoOficina;

	public UnidadDeGestionId() {
	}

	public UnidadDeGestionId(int codigoUnidad, int codigoOficina) {
		this.codigoUnidad = codigoUnidad;
		this.codigoOficina = codigoOficina;
	}

	@Column(name = "UNI_CODI", nullable = false, precision = 3, scale = 0)
	public int getCodigoUnidad() {
		return this.codigoUnidad;
	}

	public void setCodigoUnidad(int codigoUnidad) {
		this.codigoUnidad = codigoUnidad;
	}

	@Column(name = "UNI_CODIOFI", nullable = false, precision = 2, scale = 0)
	public int getCodigoOficina() {
		return this.codigoOficina;
	}

	public void setCodigoOficina(int codigoOficina) {
		this.codigoOficina = codigoOficina;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof NumeroCorreoId))
			return false;
		UnidadDeGestionId castOther = (UnidadDeGestionId) other;

		return (this.getCodigoUnidad() == castOther.getCodigoUnidad())
				&& (this.getCodigoOficina() == castOther.getCodigoOficina());
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result + this.getCodigoUnidad();
		result = 37 * result + this.getCodigoOficina();
;
		return result;
	}

}
