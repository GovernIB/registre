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
@Table(name = "BZENTRA060")
@org.hibernate.annotations.Table(appliesTo = "BZENTRA060", comment = "Entrada060")
public class Entrada060 implements java.io.Serializable {

	private Entrada060Id id;
	private String codigoMunicipio;

	public Entrada060() {
	}

	public Entrada060(Entrada060Id id, String codigoMunicipio) {
		this.id = id;
		this.codigoMunicipio = codigoMunicipio;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyoEntrada", column = @Column(name = "ENT_ANY", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficinaEntrada", column = @Column(name = "ENT_OFI", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "numeroEntrada", column = @Column(name = "ENT_NUM", nullable = false, precision = 5, scale = 0)) })
	public Entrada060Id getId() {
		return this.id;
	}

	public void setId(Entrada060Id id) {
		this.id = id;
	}

	@Column(name = "ENT_CODIMUN", nullable = false, length = 3)
	public String getCodigoMunicipio() {
		return this.codigoMunicipio;
	}

	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}

}
