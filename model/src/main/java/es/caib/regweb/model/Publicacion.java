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
@Table(name = "BZPUBLI")
@org.hibernate.annotations.Table(appliesTo = "BZPUBLI", comment = "Publicacion")
public class Publicacion implements java.io.Serializable {

	private PublicacionId id;
	private int numeroBocaib;
	private int fechaPublicacion;
	private int numeroPagina;
	private int numeroLineas;
	private String contenido;
	private String observaciones;

	public Publicacion() {
	}

	public Publicacion(PublicacionId id,
			int numeroBocaib, int fechaPublicacion, int numeroPagina, int numeroLineas,
			String contenido, String observaciones) {
		this.id = id;
		this.numeroBocaib = numeroBocaib;
		this.fechaPublicacion = fechaPublicacion;
		this.numeroPagina = numeroPagina;
		this.numeroLineas = numeroLineas;
		this.contenido = contenido;
		this.observaciones = observaciones;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyo", column = @Column(name = "FZEANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZENUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZECAGCO", nullable = false, precision = 2, scale = 0)) })
	public PublicacionId getId() {
		return this.id;
	}

	public void setId(PublicacionId id) {
		this.id = id;
	}

	@Column(name = "FZENBOCA", nullable = false, precision = 3, scale = 0)
	public int getNumeroBocaib() {
		return this.numeroBocaib;
	}

	public void setNumeroBocaib(int numeroBocaib) {
		this.numeroBocaib = numeroBocaib;
	}

	@Column(name = "FZEFPUBL", nullable = false, precision = 8, scale = 0)
	public int getFechaPublicacion() {
		return this.fechaPublicacion;
	}

	public void setFechaPublicacion(int fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	@Column(name = "FZENPAGI", nullable = false, precision = 5, scale = 0)
	public int getNumeroPagina() {
		return this.numeroPagina;
	}

	public void setNumeroPagina(int numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	@Column(name = "FZENLINE", nullable = false, precision = 6, scale = 0)
	public int getNumeroLineas() {
		return this.numeroLineas;
	}

	public void setNumeroLineas(int numeroLineas) {
		this.numeroLineas = numeroLineas;
	}

	@Column(name = "FZECONEN", nullable = false, length = 160)
	public String getContenido() {
		return this.contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Column(name = "FZEOBSER", nullable = false, length = 50)
	public String getObservaciones() {
		return this.observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
}
