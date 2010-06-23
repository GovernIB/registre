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
@Table(name = "BZREPRO")
public class Repro implements java.io.Serializable {

	private ReproId id;
	private String datos;

	public Repro() {
	}

	public Repro(ReproId id) {
		this.id = id;
	}

	public Repro(ReproId id, String datos) {
		this.id = id;
		this.datos = datos;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "usuario", column = @Column(name = "FZCCUSU", nullable = false, length = 10)),
			@AttributeOverride(name = "nombre", column = @Column(name = "FZCNREP", nullable = false, length = 50)),
			@AttributeOverride(name = "tipo", column = @Column(name = "FZTIREP", nullable = false, length = 10)) })
	public ReproId getId() {
		return this.id;
	}

	public void setId(ReproId id) {
		this.id = id;
	}

	@Column(name = "FZCDREP", length = 65535)
	public String getDatos() {
		return this.datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

}
