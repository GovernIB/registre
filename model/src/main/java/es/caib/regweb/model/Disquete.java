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
@Table(name = "BZDISQU")
public class Disquete implements java.io.Serializable {

	private DisqueteId id;
	private int numero;

	public Disquete() {
	}

	public Disquete(DisqueteId id, int numero) {
		this.id = id;
		this.numero = numero;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZLCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "oficinaEntradaSalida", column = @Column(name = "FZLCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "anyoEntradaSalida", column = @Column(name = "FZLAENSA", nullable = false, precision = 4, scale = 0)) })
	public DisqueteId getId() {
		return this.id;
	}

	public void setId(DisqueteId id) {
		this.id = id;
	}

	@Column(name = "FZLNDIS", nullable = false, precision = 5, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

}
