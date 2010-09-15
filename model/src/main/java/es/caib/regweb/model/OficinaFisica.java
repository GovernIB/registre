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
@Table(name = "BZOFIFIS")
@org.hibernate.annotations.Table(appliesTo = "BZOFIFIS", comment = "OficinaFisica")
public class OficinaFisica implements java.io.Serializable {

	private OficinaFisicaId id;
	private String nombre;
	private int fechaBaja;

	public OficinaFisica() {
	}

	public OficinaFisica(OficinaFisicaId id, String nombre, int fechaBaja) {
		this.id = id;
		this.nombre = nombre;
		this.fechaBaja = fechaBaja;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoOficina", column = @Column(name = "FZOCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigoOficinaFisica", column = @Column(name = "OFF_CODI", nullable = false, precision = 4, scale = 0)) })
	public OficinaFisicaId getId() {
		return this.id;
	}

	public void setId(OficinaFisicaId id) {
		this.id = id;
	}

	@Column(name = "OFF_NOM", nullable = false, length = 25)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "OFF_FECBAJ", nullable = false, precision = 6, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

}
