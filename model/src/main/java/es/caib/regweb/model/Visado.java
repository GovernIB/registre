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
@Table(name = "BZVISAD")
public class Visado implements java.io.Serializable {

	private VisadoId id;
	private String codigoEntidadInicial;
	private int numeroEntidadInicial;
	private String remitenteInicial;
	private String contenidoInicial;
	private String codigoEntidadFinal;
	private int numeroEntidadFinal;
	private String remitenteFinal;
	private String contenidoFinal;
	private int fechaInicial;
	private int fechaFinal;
	private String usuarioVisado;
	private String textoCambio;

	public Visado() {
	}

	public Visado(VisadoId id, String codigoEntidadInicial,
			int numeroEntidadInicial, String remitenteInicial, String contenidoInicial, String codigoEntidadFinal,
			int numeroEntidadFinal, String remitenteFinal, String contenidoFinal, int fechaInicial,
			int fechaFinal, String usuarioVisado, String textoCambio) {
		this.id = id;
		this.codigoEntidadInicial = codigoEntidadInicial;
		this.numeroEntidadInicial = numeroEntidadInicial;
		this.remitenteInicial = remitenteInicial;
		this.contenidoInicial = contenidoInicial;
		this.codigoEntidadFinal = codigoEntidadFinal;
		this.numeroEntidadFinal = numeroEntidadFinal;
		this.remitenteFinal = remitenteFinal;
		this.contenidoFinal = contenidoFinal;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
		this.usuarioVisado = usuarioVisado;
		this.textoCambio = textoCambio;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZKCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "anyo", column = @Column(name = "FZKANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZKNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZKCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "fechaVisado", column = @Column(name = "FZKFVISA", nullable = false, precision = 8, scale = 0)),
			@AttributeOverride(name = "horaVisado", column = @Column(name = "FZKHVISA", nullable = false, precision = 8, scale = 0)) })
	public VisadoId getId() {
		return this.id;
	}

	public void setId(VisadoId id) {
		this.id = id;
	}

	@Column(name = "FZKCENTI", nullable = false, length = 7)
	public String getCodigoEntidadInicial() {
		return this.codigoEntidadInicial;
	}

	public void setCodigoEntidadInicial(String codigoEntidadInicial) {
		this.codigoEntidadInicial = codigoEntidadInicial;
	}

	@Column(name = "FZKNENTI", nullable = false, precision = 3, scale = 0)
	public int getNumeroEntidadInicial() {
		return this.numeroEntidadInicial;
	}

	public void setNumeroEntidadInicial(int numeroEntidadInicial) {
		this.numeroEntidadInicial = numeroEntidadInicial;
	}

	@Column(name = "FZKREMII", nullable = false, length = 30)
	public String getRemitenteInicial() {
		return this.remitenteInicial;
	}

	public void setRemitenteInicial(String remitenteInicial) {
		this.remitenteInicial = remitenteInicial;
	}

	@Column(name = "FZKCONEI", nullable = false, length = 160)
	public String getContenidoInicial() {
		return this.contenidoInicial;
	}

	public void setContenidoInicial(String contenidoInicial) {
		this.contenidoInicial = contenidoInicial;
	}

	@Column(name = "FZKCENTF", nullable = false, length = 7)
	public String getCodigoEntidadFinal() {
		return this.codigoEntidadFinal;
	}

	public void setCodigoEntidadFinal(String codigoEntidadFinal) {
		this.codigoEntidadFinal = codigoEntidadFinal;
	}

	@Column(name = "FZKNENTF", nullable = false, precision = 3, scale = 0)
	public int getNumeroEntidadFinal() {
		return this.numeroEntidadFinal;
	}

	public void setNumeroEntidadFinal(int numeroEntidadFinal) {
		this.numeroEntidadFinal = numeroEntidadFinal;
	}

	@Column(name = "FZKREMIF", nullable = false, length = 30)
	public String getRemitenteFinal() {
		return this.remitenteFinal;
	}

	public void setRemitenteFinal(String remitenteFinal) {
		this.remitenteFinal = remitenteFinal;
	}

	@Column(name = "FZKCONEF", nullable = false, length = 160)
	public String getContenidoFinal() {
		return this.contenidoFinal;
	}

	public void setContenidoFinal(String contenidoFinal) {
		this.contenidoFinal = contenidoFinal;
	}

	@Column(name = "FZKFENTI", nullable = false, precision = 8, scale = 0)
	public int getFechaInicial() {
		return this.fechaInicial;
	}

	public void setFechaInicial(int fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	@Column(name = "FZKFENTF", nullable = false, precision = 8, scale = 0)
	public int getFechaFinal() {
		return this.fechaFinal;
	}

	public void setFechaFinal(int fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	@Column(name = "FZKCUSVI", nullable = false, length = 10)
	public String getUsuarioVisado() {
		return this.usuarioVisado;
	}

	public void setUsuarioVisado(String usuarioVisado) {
		this.usuarioVisado = usuarioVisado;
	}

	@Column(name = "FZKTEXTO", nullable = false, length = 150)
	public String getTextoCambio() {
		return this.textoCambio;
	}

	public void setTextoCambio(String textoCambio) {
		this.textoCambio = textoCambio;
	}

}
