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
@Table(name = "BZSALOFF")
public class SalidaOficinaFisica implements java.io.Serializable {

	private SalidaOficinaFisicaId id;
	private int oficinaFisica;

	public SalidaOficinaFisica() {
	}

	public SalidaOficinaFisica(SalidaOficinaFisicaId id, int oficinaFisica) {
		this.id = id;
		this.oficinaFisica = oficinaFisica;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyo", column = @Column(name = "FOSANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FOSNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FOSCAGCO", nullable = false, precision = 2, scale = 0)) })
	public SalidaOficinaFisicaId getId() {
		return this.id;
	}

	public void setId(SalidaOficinaFisicaId id) {
		this.id = id;
	}

	@Column(name = "OFS_CODI", nullable = false, precision = 4, scale = 0)
	public int getOficinaFisica() {
		return this.oficinaFisica;
	}

	public void setOficinaFisica(int oficinaFisica) {
		this.oficinaFisica = oficinaFisica;
	}

}
