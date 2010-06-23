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
@Table(name = "BZOFIRE")
public class OficinaOrganismoNoRemision implements java.io.Serializable {

	private OficinaOrganismoNoRemisionId id;

	public OficinaOrganismoNoRemision() {
	}

	public OficinaOrganismoNoRemision(OficinaOrganismoNoRemisionId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoOficina", column = @Column(name = "FZFCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigoOrganismo", column = @Column(name = "FZFCORGA", nullable = false, precision = 4, scale = 0)) })
	public OficinaOrganismoNoRemisionId getId() {
		return this.id;
	}

	public void setId(OficinaOrganismoNoRemisionId id) {
		this.id = id;
	}

}
