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
@Table(name = "BZMODIF")
public class Modificacion implements java.io.Serializable {

	private ModificacionId id;
	private String remitente;
	private String codigoEntidad;
	private int numeroEntidad;
	private String indicadorVisadoRemitente;
	private String contenido;
	private String indicadorVisadoExtracto;
	private int fechaVisado;
	private int horaVisado;
	private String usuarioVisado;
	private String textoCambio;

	public Modificacion() {
	}

	public Modificacion(ModificacionId id,
			String remitente, String codigoEntidad, int numeroEntidad, String indicadorVisadoRemitente,
			String contenido, String indicadorVisadoExtracto, int fechaVisado, int horaVisado,
			String usuarioVisado, String textoCambio) {
		this.id = id;
		this.remitente = remitente;
		this.codigoEntidad = codigoEntidad;
		this.numeroEntidad = numeroEntidad;
		this.indicadorVisadoRemitente = indicadorVisadoRemitente;
		this.contenido = contenido;
		this.indicadorVisadoExtracto = indicadorVisadoExtracto;
		this.fechaVisado = fechaVisado;
		this.horaVisado = horaVisado;
		this.usuarioVisado = usuarioVisado;
		this.textoCambio = textoCambio;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZJCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "anyoEntradaSalida", column = @Column(name = "FZJANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numeroEntradaSalida", column = @Column(name = "FZJNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficinaEntradaSalida", column = @Column(name = "FZJCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "usuarioModificacion", column = @Column(name = "FZJCUSMO", nullable = false, length = 10)),
			@AttributeOverride(name = "fechaModficacion", column = @Column(name = "FZJFMODI", nullable = false, precision = 8, scale = 0)),
			@AttributeOverride(name = "horaModificacion", column = @Column(name = "FZJHMODI", nullable = false, precision = 8, scale = 0)) })
	public ModificacionId getId() {
		return this.id;
	}

	public void setId(ModificacionId id) {
		this.id = id;
	}

	@Column(name = "FZJREMIT", nullable = false, length = 30)
	public String getRemitente() {
		return this.remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	@Column(name = "FZJCENTI", nullable = false, length = 7)
	public String getCodigoEntidad() {
		return this.codigoEntidad;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	@Column(name = "FZJNENTI", nullable = false, precision = 3, scale = 0)
	public int getNumeroEntidad() {
		return this.numeroEntidad;
	}

	public void setNumeroEntidad(int numeroEntidad) {
		this.numeroEntidad = numeroEntidad;
	}

	@Column(name = "FZJIREMI", nullable = false, length = 1)
	public String getIndicadorVisadoRemitente() {
		return this.indicadorVisadoRemitente;
	}

	public void setIndicadorVisadoRemitente(String indicadorVisadoRemitente) {
		this.indicadorVisadoRemitente = indicadorVisadoRemitente;
	}

	@Column(name = "FZJCONEN", nullable = false, length = 160)
	public String getContenido() {
		return this.contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Column(name = "FZJIEXTR", nullable = false, length = 1)
	public String getIndicadorVisadoExtracto() {
		return this.indicadorVisadoExtracto;
	}

	public void setIndicadorVisadoExtracto(String indicadorVisadoExtracto) {
		this.indicadorVisadoExtracto = indicadorVisadoExtracto;
	}

	@Column(name = "FZJFVISA", nullable = false, precision = 8, scale = 0)
	public int getFechaVisado() {
		return this.fechaVisado;
	}

	public void setFechaVisado(int fechaVisado) {
		this.fechaVisado = fechaVisado;
	}

	@Column(name = "FZJHVISA", nullable = false, precision = 8, scale = 0)
	public int getHoraVisado() {
		return this.horaVisado;
	}

	public void setHoraVisado(int horaVisado) {
		this.horaVisado = horaVisado;
	}

	@Column(name = "FZJCUSVI", nullable = false, length = 10)
	public String getUsuarioVisado() {
		return this.usuarioVisado;
	}

	public void setUsuarioVisado(String usuarioVisado) {
		this.usuarioVisado = usuarioVisado;
	}

	@Column(name = "FZJTEXTO", nullable = false, length = 150)
	public String getTextoCambio() {
		return this.textoCambio;
	}

	public void setTextoCambio(String textoCambio) {
		this.textoCambio = textoCambio;
	}

}
