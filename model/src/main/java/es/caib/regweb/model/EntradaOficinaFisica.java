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
@Table(name = "BZENTOFF")
public class EntradaOficinaFisica implements java.io.Serializable {

	private EntradaOficinaFisicaId id;
	private int oficinaFisica;

	public EntradaOficinaFisica() {
	}

	public EntradaOficinaFisica(EntradaOficinaFisicaId id, int oficinaFisica) {
		this.id = id;
		this.oficinaFisica = oficinaFisica;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyo", column = @Column(name = "FOEANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FOENUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FOECAGCO", nullable = false, precision = 2, scale = 0)) })
	public EntradaOficinaFisicaId getId() {
		return this.id;
	}

	public void setId(EntradaOficinaFisicaId id) {
		this.id = id;
	}

	@Column(name = "OFE_CODI", nullable = false, precision = 4, scale = 0)
	public int getOficinaFisica() {
		return this.oficinaFisica;
	}

	public void setOficinaFisica(int oficinaFisica) {
		this.oficinaFisica = oficinaFisica;
	}

}
