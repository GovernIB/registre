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
@Table(name = "BZCONES")
public class Contador implements java.io.Serializable {

	private ContadorId id;
	private int numero;

	public Contador() {
	}

	public Contador(ContadorId id, int numero) {
		this.id = id;
		this.numero = numero;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyo", column = @Column(name = "FZDAENSA", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZDCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZDCAGCO", nullable = false, precision = 2, scale = 0)) })
	public ContadorId getId() {
		return this.id;
	}

	public void setId(ContadorId id) {
		this.id = id;
	}

	@Column(name = "FZDNUMER", nullable = false, precision = 5, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
