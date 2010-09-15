package es.caib.regweb.model;



import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

/**

 */
@Entity
@Table(name = "BORGANI")
@org.hibernate.annotations.Table(appliesTo = "BORGANI", comment = "Organismo")
public class Organismo implements java.io.Serializable {

	private int codigo;
	private String nombreCorto;
	private String nombreLargo;
	private String esOrganismoConselleriaAgricultura;
	private int fechaBaja;

	public Organismo() {
	}

	public Organismo(int codigo, String nombreCorto, String nombreLargo,
			String esOrganismoConselleriaAgricultura, int fechaBaja) {
		this.codigo = codigo;
		this.nombreCorto = nombreCorto;
		this.nombreLargo = nombreLargo;
		this.esOrganismoConselleriaAgricultura = esOrganismoConselleriaAgricultura;
		this.fechaBaja = fechaBaja;
	}

	@Id
	@Column(name = "FAXCORGA", nullable = false, precision = 4, scale = 0)
	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	@Column(name = "FAXDORGR", nullable = false, length = 15)
	public String getNombreCorto() {
		return this.nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	@Column(name = "FAXDORGT", nullable = false, length = 40)
	public String getNombreLargo() {
		return this.nombreLargo;
	}

	public void setNombreLargo(String nombreLargo) {
		this.nombreLargo = nombreLargo;
	}

	@Column(name = "FAXCOAGR", nullable = false, length = 1)
	public String getEsOrganismoConselleriaAgricultura() {
		return this.esOrganismoConselleriaAgricultura;
	}

	public void setEsOrganismoConselleriaAgricultura(String esOrganismoConselleriaAgricultura) {
		this.esOrganismoConselleriaAgricultura = esOrganismoConselleriaAgricultura;
	}

	@Column(name = "FAXFBAJA", nullable = false, precision = 6, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

}
