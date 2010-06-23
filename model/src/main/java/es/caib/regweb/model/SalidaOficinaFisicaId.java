package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumns;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;

/**

 */
@Embeddable
public class SalidaOficinaFisicaId implements java.io.Serializable {

    private Salida salida;

	public SalidaOficinaFisicaId() {
	}

	public SalidaOficinaFisicaId(Salida salida) {
		this.salida = salida;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumns ({
        @JoinColumn(name="FOSANOEN", referencedColumnName = "FZSANOEN"),
        @JoinColumn(name="FOSNUMEN", referencedColumnName = "FZSNUMEN"),
        @JoinColumn(name="FOSCAGCO", referencedColumnName = "FZSCAGCO")
    })
	public Salida getSalida() {
		return this.salida;
	}

	public void setSalida(Salida salida) {
		this.salida = salida;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SalidaOficinaFisicaId))
			return false;
		SalidaOficinaFisicaId castOther = (SalidaOficinaFisicaId) other;

		return (this.getSalida() == castOther.getSalida());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSalida().getId().getAnyo();
		result = 37 * result + this.getSalida().getId().getNumero();
		result = 37 * result + this.getSalida().getId().getOficina();
		return result;
	}

}
