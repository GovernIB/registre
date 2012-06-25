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
@Table(name = "BZUNIGES")
@org.hibernate.annotations.Table(appliesTo = "BZUNIGES", comment = "Unidad de Gestion")
public class UnidadDeGestion implements java.io.Serializable {

	private UnidadDeGestionId id;
	private String nombre;
	private String actiu;
	private String direccionEmail;

	public UnidadDeGestion() {
	}

	public UnidadDeGestion(UnidadDeGestionId id, String nombre, String actiu, String direccionEmail) {
		this.id = id;
		this.nombre = nombre;
		this.actiu = actiu;
		this.direccionEmail = direccionEmail;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoUnidad", column = @Column(name = "UNI_CODI ", nullable = false, precision = 3, scale = 0)),
			@AttributeOverride(name = "codigoOficina", column = @Column(name = "UNI_CODIOFI", nullable = false, precision = 2, scale = 0)) })
	public UnidadDeGestionId getId() {
		return this.id;
	}

	public void setId(UnidadDeGestionId id) {
		this.id = id;
	}

	@Column(name = "UNI_NOM", nullable = false, length = 20)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Column(name = "UNI_BAJA ", nullable = false, length = 1)
	public String getActiu() {
		return this.actiu;
	}

	public void setActiu(String actiu) {
		this.actiu = actiu;
	}
	@Column(name = "UNI_EMAIL", nullable = false, length = 50)
	public String getDireccionEmail() {
		return this.direccionEmail;
	}

	public void setDireccionEmail(String direccionEmail) {
		this.direccionEmail = direccionEmail;
	}

}
