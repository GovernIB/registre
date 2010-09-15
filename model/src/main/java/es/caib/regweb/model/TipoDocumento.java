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
@Table(name = "BZTDOCU")
@org.hibernate.annotations.Table(appliesTo = "BZTDOCU", comment = "TipoDocumento")
public class TipoDocumento implements java.io.Serializable {

	private TipoDocumentoId id;
	private String nombre;

	public TipoDocumento() {
	}

	public TipoDocumento(TipoDocumentoId id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigo", column = @Column(name = "FZICTIPE", nullable = false, length = 2)),
			@AttributeOverride(name = "fechaBaja", column = @Column(name = "FZIFBAJA", nullable = false, precision = 8, scale = 0)) })
	public TipoDocumentoId getId() {
		return this.id;
	}

	public void setId(TipoDocumentoId id) {
		this.id = id;
	}

	@Column(name = "FZIDTIPE", nullable = false, length = 30)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
