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
@Table(name = "BCODPOS")
public class CodigoPostal implements java.io.Serializable {

	private CodigoPostalId id;
	private int codigoPostal;

	public CodigoPostal() {
	}

	public CodigoPostal(CodigoPostalId id, int codigoPostal) {
		this.id = id;
		this.codigoPostal = codigoPostal;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "tipoAgrupacionGeografica", column = @Column(name = "F12CTAGG", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigoAgrupacionGeografica", column = @Column(name = "F12CAGGE", nullable = false, precision = 3, scale = 0)) })
	public CodigoPostalId getId() {
		return this.id;
	}

	public void setId(CodigoPostalId id) {
		this.id = id;
	}

	@Column(name = "F12CPOST", nullable = false, precision = 5, scale = 0)
	public int getCodigoPostal() {
		return this.codigoPostal;
	}

	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

}
