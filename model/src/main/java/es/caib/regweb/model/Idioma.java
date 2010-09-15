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
@Table(name = "BZIDIOM")
@org.hibernate.annotations.Table(appliesTo = "BZIDIOM", comment = "Idioma")
public class Idioma implements java.io.Serializable {

	private String codigo;
	private String nombre;

	public Idioma() {
	}

	public Idioma(String codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	@Id
	@Column(name = "FZMCIDI", nullable = false, length = 1)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "FZMDIDI", nullable = false, length = 15)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
