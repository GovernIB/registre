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
@Table(name = "BZAUTOR")
@org.hibernate.annotations.Table(appliesTo = "BZAUTOR", comment = "Autorizacion")
public class Autorizacion implements java.io.Serializable {

	private AutorizacionId id;

	public Autorizacion() {
	}

	public Autorizacion(AutorizacionId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "usuario", column = @Column(name = "FZHCUSU", nullable = false, length = 10)),
			@AttributeOverride(name = "codigoAutorizacion", column = @Column(name = "FZHCAUT", nullable = false, length = 2)),
			@AttributeOverride(name = "codigoOficina", column = @Column(name = "FZHCAGCO", nullable = false, precision = 2, scale = 0)) })
	public AutorizacionId getId() {
		return this.id;
	}

	public void setId(AutorizacionId id) {
		this.id = id;
	}

}
