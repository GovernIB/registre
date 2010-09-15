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
@Table(name = "BZOFIOR")
@org.hibernate.annotations.Table(appliesTo = "BZOFIOR", comment = "OficinaOrganismo")
public class OficinaOrganismo implements java.io.Serializable {

	private OficinaOrganismoId id;

	public OficinaOrganismo() {
	}

	public OficinaOrganismo(OficinaOrganismoId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoOficina", column = @Column(name = "FZFCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigoOrganismo", column = @Column(name = "FZFCORGA", nullable = false, precision = 4, scale = 0)) })
	public OficinaOrganismoId getId() {
		return this.id;
	}

	public void setId(OficinaOrganismoId id) {
		this.id = id;
	}

}
