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
@Table(name = "BZLIBRO")
public class Libro implements java.io.Serializable {

	private LibroId id;
	private int contadorPaginaEntradaSalida;
	private int ultimaFechaEdicion;

	public Libro() {
	}

	public Libro(LibroId id, int contadorPaginaEntradaSalida, int ultimaFechaEdicion) {
		this.id = id;
		this.contadorPaginaEntradaSalida = contadorPaginaEntradaSalida;
		this.ultimaFechaEdicion = ultimaFechaEdicion;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyoEntradaSalida", column = @Column(name = "FZCAENSA", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZCCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "oficinaEntradaSalida", column = @Column(name = "FZCCAGCO", nullable = false, precision = 2, scale = 0)) })
	public LibroId getId() {
		return this.id;
	}

	public void setId(LibroId id) {
		this.id = id;
	}

	@Column(name = "FZCNUMPA", nullable = false, precision = 5, scale = 0)
	public int getContadorPaginaEntradaSalida() {
		return this.contadorPaginaEntradaSalida;
	}

	public void setContadorPaginaEntradaSalida(int contadorPaginaEntradaSalida) {
		this.contadorPaginaEntradaSalida = contadorPaginaEntradaSalida;
	}

	@Column(name = "FZCFECED", nullable = false, precision = 8, scale = 0)
	public int getUltimaFechaEdicion() {
		return this.ultimaFechaEdicion;
	}

	public void setUltimaFechaEdicion(int ultimaFechaEdicion) {
		this.ultimaFechaEdicion = ultimaFechaEdicion;
	}

}
