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
@Table(name = "BZOFREM")
@org.hibernate.annotations.Table(appliesTo = "BZOFREM", comment = "OficioRemision")
public class OficioRemision implements java.io.Serializable {

	private int anyoEntrada;
	private int anyoSalida;
	private String contenido;
	private String descarteEntrada;	
	private int fechaEntrada;
	private int fechaOficio;
	private int fechaNula;	
	private String motivosDescarteEntrada;
	private String motivosNula;
	private String nula;
	private int numeroEntrada;
	private int numeroSalida;
	private int oficinaEntrada;
	private int oficinaSalida;
	private OficioRemisionId id;
	private String usuarioEntrada;
	private String usuarioNula;

	public OficioRemision() {
	}

	public OficioRemision(OficioRemisionId id) {
		this.id = id;
	}

	public OficioRemision(OficioRemisionId id, int fechaOficio, String contenido,
			int anyoSalida, int oficinaSalida, int numeroSalida,
			String nula, String motivosNula, String usuarioNula,
			int fechaNula, int fechaEntrada, String descarteEntrada,
			String usuarioEntrada, String motivosDescarteEntrada, int anyoEntrada,
			int oficinaEntrada, int numeroEntrada) {
		this.id = id;
		this.fechaOficio = fechaOficio;
		this.contenido = contenido;
		this.anyoSalida = anyoSalida;
		this.oficinaSalida = oficinaSalida;
		this.numeroSalida = numeroSalida;
		this.nula = nula;
		this.motivosNula = motivosNula;
		this.usuarioNula = usuarioNula;
		this.fechaNula = fechaNula;
		this.fechaEntrada = fechaEntrada;
		this.descarteEntrada = descarteEntrada;
		this.usuarioEntrada = usuarioEntrada;
		this.motivosDescarteEntrada = motivosDescarteEntrada;
		this.anyoEntrada = anyoEntrada;
		this.oficinaEntrada = oficinaEntrada;
		this.numeroEntrada = numeroEntrada;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyoOficio", column = @Column(name = "REM_OFANY", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficinaOficio", column = @Column(name = "REM_OFOFI", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "numeroOficio", column = @Column(name = "REM_OFNUM", nullable = false, precision = 5, scale = 0)) })
	public OficioRemisionId getId() {
		return this.id;
	}

	public void setId(OficioRemisionId id) {
		this.id = id;
	}

	@Column(name = "REM_OFFEC", precision = 8, scale = 0)
	public int getFechaOficio() {
		return this.fechaOficio;
	}

	public void setFechaOficio(int fechaOficio) {
		this.fechaOficio = fechaOficio;
	}

	@Column(name = "REM_CONT", length = 1500)
	public String getContenido() {
		return this.contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Column(name = "REM_SALANY", precision = 4, scale = 0)
	public int getAnyoSalida() {
		return this.anyoSalida;
	}

	public void setAnyoSalida(int anyoSalida) {
		this.anyoSalida = anyoSalida;
	}

	@Column(name = "REM_SALOFI", precision = 2, scale = 0)
	public int getOficinaSalida() {
		return this.oficinaSalida;
	}

	public void setOficinaSalida(int oficinaSalida) {
		this.oficinaSalida = oficinaSalida;
	}

	@Column(name = "REM_SALNUM", precision = 5, scale = 0)
	public int getNumeroSalida() {
		return this.numeroSalida;
	}

	public void setNumeroSalida(int numeroSalida) {
		this.numeroSalida = numeroSalida;
	}

	@Column(name = "REM_NULA", length = 1)
	public String getNula() {
		return this.nula;
	}

	public void setNula(String nula) {
		this.nula = nula;
	}

	@Column(name = "REM_NULMTD", length = 150)
	public String getMotivosNula() {
		return this.motivosNula;
	}

	public void setMotivosNula(String motivosNula) {
		this.motivosNula = motivosNula;
	}

	@Column(name = "REM_NULUSU", length = 10)
	public String getUsuarioNula() {
		return this.usuarioNula;
	}

	public void setUsuarioNula(String usuarioNula) {
		this.usuarioNula = usuarioNula;
	}

	@Column(name = "REM_NULFEC", precision = 8, scale = 0)
	public int getFechaNula() {
		return this.fechaNula;
	}

	public void setFechaNula(int fechaNula) {
		this.fechaNula = fechaNula;
	}

	@Column(name = "REM_ENTFEC", precision = 8, scale = 0)
	public int getFechaEntrada() {
		return this.fechaEntrada;
	}

	public void setFechaEntrada(int fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	@Column(name = "REM_ENTDES", length = 1)
	public String getDescarteEntrada() {
		return this.descarteEntrada;
	}

	public void setDescarteEntrada(String descarteEntrada) {
		this.descarteEntrada = descarteEntrada;
	}

	@Column(name = "REM_ENTUSU", length = 10)
	public String getUsuarioEntrada() {
		return this.usuarioEntrada;
	}

	public void setUsuarioEntrada(String usuarioEntrada) {
		this.usuarioEntrada = usuarioEntrada;
	}

	@Column(name = "REM_ENTMTD", length = 150)
	public String getMotivosDescarteEntrada() {
		return this.motivosDescarteEntrada;
	}

	public void setMotivosDescarteEntrada(String motivosDescarteEntrada) {
		this.motivosDescarteEntrada = motivosDescarteEntrada;
	}

	@Column(name = "REM_ENTANY", precision = 4, scale = 0)
	public int getAnyoEntrada() {
		return this.anyoEntrada;
	}

	public void setAnyoEntrada(int anyoEntrada) {
		this.anyoEntrada = anyoEntrada;
	}

	@Column(name = "REM_ENTOFI", precision = 2, scale = 0)
	public int getOficinaEntrada() {
		return this.oficinaEntrada;
	}

	public void setOficinaEntrada(int oficinaEntrada) {
		this.oficinaEntrada = oficinaEntrada;
	}

	@Column(name = "REM_ENTNUM", precision = 5, scale = 0)
	public int getNumeroEntrada() {
		return this.numeroEntrada;
	}

	public void setNumeroEntrada(int numeroEntrada) {
		this.numeroEntrada = numeroEntrada;
	}

}
