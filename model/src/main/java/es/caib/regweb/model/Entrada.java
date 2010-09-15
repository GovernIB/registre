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
@Table(name = "BZENTRA")
@org.hibernate.annotations.Table(appliesTo = "BZENTRA", comment = "Entrada")
public class Entrada implements java.io.Serializable {

	private EntradaId id;
	private int fechaDocumento;
	private String remitente;
	private String contenido;
	private String tipoDocumento;
	private String editLibroEntrada;
	private String nula;
	private String procedenciaGeografica;
	private int fechaEntrada;
	private int tipoAgrupacionGeografica;
	private int codigoAgrupacionGeografica;
	private int codigoOrganismo;
	private int fechaActualizacion;
	private String codigoEntidad;
	private int numeroEntidad;
	private int horaEntrada;
	private String idioma;
	private String contenidoCatalan;
	private int numeroLocalizador;
	private int anyoLocalizador;
	private int numeroDisquete;
	private int fechaSistema;
	private int horaSistema;
	private String usuario;
	private String codigoIdioma;

	public Entrada() {
	}

	public Entrada(EntradaId id, int fechaDocumento,
			String remitente, String contenido, String tipoDocumento, String editLibroEntrada,
			String nula, String procedenciaGeografica, int fechaEntrada, int tipoAgrupacionGeografica,
			int codigoAgrupacionGeografica, int codigoOrganismo, int fechaActualizacion, String codigoEntidad,
			int numeroEntidad, int horaEntrada, String idioma, String contenidoCatalan,
			int numeroLocalizador, int anyoLocalizador, int numeroDisquete, int fechaSistema, int horaSistema,
			String usuario, String codigoIdioma) {
		this.id = id;
		this.fechaDocumento = fechaDocumento;
		this.remitente = remitente;
		this.contenido = contenido;
		this.tipoDocumento = tipoDocumento;
		this.editLibroEntrada = editLibroEntrada;
		this.nula = nula;
		this.procedenciaGeografica = procedenciaGeografica;
		this.fechaEntrada = fechaEntrada;
		this.tipoAgrupacionGeografica = tipoAgrupacionGeografica;
		this.codigoAgrupacionGeografica = codigoAgrupacionGeografica;
		this.codigoOrganismo = codigoOrganismo;
		this.fechaActualizacion = fechaActualizacion;
		this.codigoEntidad = codigoEntidad;
		this.numeroEntidad = numeroEntidad;
		this.horaEntrada = horaEntrada;
		this.idioma = idioma;
		this.contenidoCatalan = contenidoCatalan;
		this.numeroLocalizador = numeroLocalizador;
		this.anyoLocalizador = anyoLocalizador;
		this.numeroDisquete = numeroDisquete;
		this.fechaSistema = fechaSistema;
		this.horaSistema = horaSistema;
		this.usuario = usuario;
		this.codigoIdioma = codigoIdioma;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "anyo", column = @Column(name = "FZAANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZANUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZACAGCO", nullable = false, precision = 2, scale = 0)) })
	public EntradaId getId() {
		return this.id;
	}

	public void setId(EntradaId id) {
		this.id = id;
	}

	@Column(name = "FZAFDOCU", nullable = false, precision = 8, scale = 0)
	public int getFechaDocumento() {
		return this.fechaDocumento;
	}

	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	@Column(name = "FZAREMIT", nullable = false, length = 30)
	public String getRemitente() {
		return this.remitente;
	}

	public void setRemitente(String remitente) {
		this.remitente = remitente;
	}

	@Column(name = "FZACONEN", nullable = false, length = 160)
	public String getContenido() {
		return this.contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Column(name = "FZACTIPE", nullable = false, length = 2)
	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Column(name = "FZACEDIE", nullable = false, length = 1)
	public String getEditLibroEntrada() {
		return this.editLibroEntrada;
	}

	public void setEditLibroEntrada(String editLibroEntrada) {
		this.editLibroEntrada = editLibroEntrada;
	}

	@Column(name = "FZAENULA", nullable = false, length = 1)
	public String getNula() {
		return this.nula;
	}

	public void setNula(String nula) {
		this.nula = nula;
	}

	@Column(name = "FZAPROCE", nullable = false, length = 25)
	public String getProcedenciaGeografica() {
		return this.procedenciaGeografica;
	}

	public void setProcedenciaGeografica(String procedenciaGeografica) {
		this.procedenciaGeografica = procedenciaGeografica;
	}

	@Column(name = "FZAFENTR", nullable = false, precision = 8, scale = 0)
	public int getFechaEntrada() {
		return this.fechaEntrada;
	}

	public void setFechaEntrada(int fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	@Column(name = "FZACTAGG", nullable = false, precision = 2, scale = 0)
	public int getTipoAgrupacionGeografica() {
		return this.tipoAgrupacionGeografica;
	}

	public void setTipoAgrupacionGeografica(int tipoAgrupacionGeografica) {
		this.tipoAgrupacionGeografica = tipoAgrupacionGeografica;
	}

	@Column(name = "FZACAGGE", nullable = false, precision = 3, scale = 0)
	public int getCodigoAgrupacionGeografica() {
		return this.codigoAgrupacionGeografica;
	}

	public void setCodigoAgrupacionGeografica(int codigoAgrupacionGeografica) {
		this.codigoAgrupacionGeografica = codigoAgrupacionGeografica;
	}

	@Column(name = "FZACORGA", nullable = false, precision = 4, scale = 0)
	public int getCodigoOrganismo() {
		return this.codigoOrganismo;
	}

	public void setCodigoOrganismo(int codigoOrganismo) {
		this.codigoOrganismo = codigoOrganismo;
	}

	@Column(name = "FZAFACTU", nullable = false, precision = 8, scale = 0)
	public int getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(int fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@Column(name = "FZACENTI", nullable = false, length = 7)
	public String getCodigoEntidad() {
		return this.codigoEntidad;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	@Column(name = "FZANENTI", nullable = false, precision = 3, scale = 0)
	public int getNumeroEntidad() {
		return this.numeroEntidad;
	}

	public void setNumeroEntidad(int numeroEntidad) {
		this.numeroEntidad = numeroEntidad;
	}

	@Column(name = "FZAHORA", nullable = false, precision = 4, scale = 0)
	public int getHoraEntrada() {
		return this.horaEntrada;
	}

	public void setHoraEntrada(int horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	@Column(name = "FZACIDIO", nullable = false, precision = 1, scale = 0)
	public String getIdioma() {
		return this.idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	@Column(name = "FZACONE2", nullable = false, length = 160)
	public String getContenidoCatalan() {
		return this.contenidoCatalan;
	}

	public void setContenidoCatalan(String contenidoCatalan) {
		this.contenidoCatalan = contenidoCatalan;
	}

	@Column(name = "FZANLOC", nullable = false, precision = 6, scale = 0)
	public int getNumeroLocalizador() {
		return this.numeroLocalizador;
	}

	public void setNumeroLocalizador(int numeroLocalizador) {
		this.numeroLocalizador = numeroLocalizador;
	}

	@Column(name = "FZAALOC", nullable = false, precision = 4, scale = 0)
	public int getAnyoLocalizador() {
		return this.anyoLocalizador;
	}

	public void setAnyoLocalizador(int anyoLocalizador) {
		this.anyoLocalizador = anyoLocalizador;
	}

	@Column(name = "FZANDIS", nullable = false, precision = 5, scale = 0)
	public int getNumeroDisquete() {
		return this.numeroDisquete;
	}

	public void setNumeroDisquete(int numeroDisquete) {
		this.numeroDisquete = numeroDisquete;
	}

	@Column(name = "FZAFSIS", nullable = false, precision = 8, scale = 0)
	public int getFechaSistema() {
		return this.fechaSistema;
	}

	public void setFechaSistema(int fechaSistema) {
		this.fechaSistema = fechaSistema;
	}

	@Column(name = "FZAHSIS", nullable = false, precision = 8, scale = 0)
	public int getHoraSistema() {
		return this.horaSistema;
	}

	public void setHoraSistema(int horaSistema) {
		this.horaSistema = horaSistema;
	}

	@Column(name = "FZACUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FZACIDI", nullable = false, length = 1)
	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

}
