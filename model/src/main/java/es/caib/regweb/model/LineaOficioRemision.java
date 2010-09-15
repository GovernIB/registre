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
@Table(name = "BZOFRENT")
@org.hibernate.annotations.Table(appliesTo = "BZOFRENT", comment = "LiniaOficioRemision")
public class LineaOficioRemision implements java.io.Serializable {

	private LineaOficioRemisionId id;
	private String descarteEntrada;
	private String usuarioEntrada;
	private String motivosDescarteEntrada;
	private int anyoOficio;
	private int oficinaOficio;
	private int numeroOficio;

	public LineaOficioRemision() {
	}

	public LineaOficioRemision(LineaOficioRemisionId id) {
		this.id = id;
	}

	public LineaOficioRemision(LineaOficioRemisionId id, String descarteEntrada, String usuarioEntrada,
			String motivosDescarteEntrada, int anyoOficio, int oficinaOficio, int numeroOficio) {
		this.id = id;
		this.descarteEntrada = descarteEntrada;
		this.usuarioEntrada = usuarioEntrada;
		this.motivosDescarteEntrada = motivosDescarteEntrada;
		this.anyoOficio = anyoOficio;
		this.oficinaOficio = oficinaOficio;
		this.numeroOficio = numeroOficio;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyoEntradaRegistro", column = @Column(name = "REN_ENTANY", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficinaEntradaRegistro", column = @Column(name = "REN_ENTOFI", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "numeroEntradaRegistro", column = @Column(name = "REN_ENTNUM", nullable = false, precision = 5, scale = 0)) })
	public LineaOficioRemisionId getId() {
		return this.id;
	}

	public void setId(LineaOficioRemisionId id) {
		this.id = id;
	}

	@Column(name = "REN_ENTDES", length = 1)
	public String getDescarteEntrada() {
		return this.descarteEntrada;
	}

	public void setDescarteEntrada(String descarteEntrada) {
		this.descarteEntrada = descarteEntrada;
	}

	@Column(name = "REN_ENTUSU", length = 10)
	public String getUsuarioEntrada() {
		return this.usuarioEntrada;
	}

	public void setUsuarioEntrada(String usuarioEntrada) {
		this.usuarioEntrada = usuarioEntrada;
	}

	@Column(name = "REN_ENTMTD", length = 150)
	public String getMotivosDescarteEntrada() {
		return this.motivosDescarteEntrada;
	}

	public void setMotivosDescarteEntrada(String motivosDescarteEntrada) {
		this.motivosDescarteEntrada = motivosDescarteEntrada;
	}

	@Column(name = "REN_OFANY", precision = 4, scale = 0)
	public int getAnyoOficio() {
		return this.anyoOficio;
	}

	public void setAnyoOficio(int anyoOficio) {
		this.anyoOficio = anyoOficio;
	}

	@Column(name = "REN_OFOFI", precision = 2, scale = 0)
	public int getOficinaOficio() {
		return this.oficinaOficio;
	}

	public void setOficinaOficio(int oficinaOficio) {
		this.oficinaOficio = oficinaOficio;
	}

	@Column(name = "REN_OFNUM", precision = 5, scale = 0)
	public int getNumeroOficio() {
		return this.numeroOficio;
	}

	public void setNumeroOficio(int numeroOficio) {
		this.numeroOficio = numeroOficio;
	}

}
