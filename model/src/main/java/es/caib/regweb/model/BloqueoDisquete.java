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
@Table(name = "BZBLOQU")
public class BloqueoDisquete implements java.io.Serializable {

	private BloqueoDisqueteId id;
	private String usuario;

	public BloqueoDisquete() {
	}

	public BloqueoDisquete(BloqueoDisqueteId id, String usuario) {
		this.id = id;
		this.usuario = usuario;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZNCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "anyoEntradaSalida", column = @Column(name = "FZNAENSA", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficinaEntradaSalida", column = @Column(name = "FZNCAGCO", nullable = false, precision = 2, scale = 0)) })
	public BloqueoDisqueteId getId() {
		return this.id;
	}

	public void setId(BloqueoDisqueteId id) {
		this.id = id;
	}

	@Column(name = "FZNCUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
