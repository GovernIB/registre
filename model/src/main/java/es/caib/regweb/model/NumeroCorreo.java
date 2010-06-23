package es.caib.regweb.model;



import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**

 */
@Entity
@Table(name = "BZNCORR")
public class NumeroCorreo implements java.io.Serializable {

	private NumeroCorreoId id;
	private String numeroCorreo;

	public NumeroCorreo() {
	}

	public NumeroCorreo(NumeroCorreoId id, String numeroCorreo) {
		this.id = id;
		this.numeroCorreo = numeroCorreo;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZPCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "oficinaEntradaSalida", column = @Column(name = "FZPCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "anyoEntradaSalida", column = @Column(name = "FZPANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numeroEntradaSalida", column = @Column(name = "FZPNUMEN", nullable = false, precision = 5, scale = 0)) })
	public NumeroCorreoId getId() {
		return this.id;
	}

	public void setId(NumeroCorreoId id) {
		this.id = id;
	}

	@Column(name = "FZPNCORR", nullable = false, length = 8)
	public String getNumeroCorreo() {
		return this.numeroCorreo;
	}

	public void setNumeroCorreo(String numeroCorreo) {
		this.numeroCorreo = numeroCorreo;
	}

}
