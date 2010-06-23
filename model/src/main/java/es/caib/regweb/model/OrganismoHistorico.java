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
@Table(name = "BHORGAN")
public class OrganismoHistorico implements java.io.Serializable {

	private OrganismoHistoricoId id;
	private String nombreCorto;
	private String nombreLargo;
	private int fechaBaja;

	public OrganismoHistorico() {
	}

	public OrganismoHistorico(OrganismoHistoricoId id, String nombreCorto, String nombreLargo, int fechaBaja) {
		this.id = id;
		this.nombreCorto = nombreCorto;
		this.nombreLargo = nombreLargo;
		this.fechaBaja = fechaBaja;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigo", column = @Column(name = "FHXCORGA", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "fechaAlta", column = @Column(name = "FHXFALTA", nullable = false, precision = 8, scale = 0)) })
	public OrganismoHistoricoId getId() {
		return this.id;
	}

	public void setId(OrganismoHistoricoId id) {
		this.id = id;
	}

	@Column(name = "FHXDORGR", nullable = false, length = 15)
	public String getNombreCorto() {
		return this.nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	@Column(name = "FHXDORGT", nullable = false, length = 40)
	public String getNombreLargo() {
		return this.nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	@Column(name = "FHXFBAJA", nullable = false, precision = 8, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

}
