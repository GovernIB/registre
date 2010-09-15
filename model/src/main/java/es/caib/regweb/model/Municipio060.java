package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**

 */
@Entity
@Table(name = "BZMUN_060", uniqueConstraints = @UniqueConstraint(columnNames = "MUN_NOM"))
@org.hibernate.annotations.Table(appliesTo = "BZMUN_060", comment = "Municipio060")
public class Municipio060 implements java.io.Serializable {

	private String codigoMunicipio;
	private String nombreMunicipio;
	private int fechaBaja;

	public Municipio060() {
	}

	public Municipio060(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	public Municipio060(String codigoMunicipio, String nombreMunicipio, int fechaBaja) {
		this.codigoMunicipio = codigoMunicipio;
		this.nombreMunicipio = nombreMunicipio;
		this.fechaBaja = fechaBaja;
	}

	@Id
	@Column(name = "MUN_CODI", unique = true, nullable = false, length = 3)
	public String getCodigoMunicipio() {
		return this.codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

	@Column(name = "MUN_NOM", unique = true, length = 30)
	public String getNombreMunicipio() {
		return this.nombreMunicipio;
	}

	public void setNombreMunicipio(String nombreMunicipio) {
		this.nombreMunicipio = nombreMunicipio;
	}

	@Column(name = "MUN_FECBAJ", precision = 8, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

}
