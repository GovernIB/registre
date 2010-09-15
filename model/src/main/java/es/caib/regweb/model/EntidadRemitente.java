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
@Table(name = "BZENTID")
@org.hibernate.annotations.Table(appliesTo = "BZENTID", comment = "EntidadRemitente")
public class EntidadRemitente implements java.io.Serializable {

	private EntidadRemitenteId id;
	private String codigoCatalan;
	private String nombre;
	private String nombreCatalan;

	public EntidadRemitente() {
	}

	public EntidadRemitente(EntidadRemitenteId id, String codigoCatalan, String nombre, String nombreCatalan) {
		this.id = id;
		this.codigoCatalan = codigoCatalan;
		this.nombre = nombre;
		this.nombreCatalan = nombreCatalan;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigo", column = @Column(name = "FZGCENTI", nullable = false, length = 7)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZGNENTI", nullable = false, precision = 3, scale = 0)),
			@AttributeOverride(name = "fechaBaja", column = @Column(name = "FZGFBAJA", nullable = false, precision = 8, scale = 0)) })
	public EntidadRemitenteId getId() {
		return this.id;
	}

	public void setId(EntidadRemitenteId id) {
		this.id = id;
	}

	@Column(name = "FZGCENT2", nullable = false, length = 7)
	public String getCodigoCatalan() {
		return this.codigoCatalan;
	}

	public void setCodigoCatalan(String codigoCatalan) {
		this.codigoCatalan = codigoCatalan;
	}

	@Column(name = "FZGDENTI", nullable = false, length = 30)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "FZGDENT2", nullable = false, length = 30)
	public String getNombreCatalan() {
		return this.nombreCatalan;
	}

	public void setNombreCatalan(String nombreCatalan) {
		this.nombreCatalan = nombreCatalan;
	}

}
