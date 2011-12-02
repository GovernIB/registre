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
@Table(name = "BZMODEMAIL")
@org.hibernate.annotations.Table(appliesTo = "BZMODEMAIL", comment = "ModeloEmail")
public class ModeloEmail implements java.io.Serializable {

	private ModeloEmailId id;
	private String titulo;
	private String cuerpo;

	public ModeloEmail() {
	}

	public ModeloEmail(ModeloEmailId id, String titulo, String cuerpo) {
		this.id = id;
		this.titulo = titulo;
		this.cuerpo = cuerpo;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "idioma", column = @Column(name = "BME_IDIOMA ", nullable = false, length = 2)),
			@AttributeOverride(name = "tipo", column = @Column(name = "BME_TIPO ", nullable = false, length = 2))
			})
	public ModeloEmailId getId() {
		return this.id;
	}

	public void setId(ModeloEmailId id) {
		this.id = id;
	}

	@Column(name = "BME_TITULO", nullable = false, length = 100)
	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@Column(name = "BME_CUERPO", nullable = false, length = 1000)
	public String getCuerpo() {
		return this.cuerpo;
	}

	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}

}
