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
@Table(name = "BZOFOR")
@org.hibernate.annotations.Table(appliesTo = "BZOFOR", comment = "OficinaOrganismoPermetEnviarEmail")
public class OficinaOrganismoPermetEnviarEmail implements java.io.Serializable {

	private OficinaOrganismoPermetEnviarEmailId id;

	public OficinaOrganismoPermetEnviarEmail() {
	}

	public OficinaOrganismoPermetEnviarEmail(OficinaOrganismoPermetEnviarEmailId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoOficina", column = @Column(name = "OFO_CODIOFI ", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigoOrganismo", column = @Column(name = "OFO_CODIORG", nullable = false, precision = 4, scale = 0)) })
	public OficinaOrganismoPermetEnviarEmailId getId() {
		return this.id;
	}

	public void setId(OficinaOrganismoPermetEnviarEmailId id) {
		this.id = id;
	}

}
