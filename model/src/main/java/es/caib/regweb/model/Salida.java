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
@Table(name = "BZSALIDA")
@org.hibernate.annotations.Table(appliesTo = "BZSALIDA", comment = "Salida")
public class Salida implements java.io.Serializable {

	private SalidaId id;
	private int fechaDocumento;
	private String destinatario;
	private String contenido;
	private String tipoDocumento;
	private String editLibroSalidas;
	private String nula;
	private String destinoGeografico;
	private int fechaSalida;
	private int tipoAgrupacionGeografica;
	private int codigoAgrupacionGeografica;
	private int codigoOrganismo;
	private int fechaActualizacion;
	private String codigoEntidad;
	private int numeroEntidad;
	private int hora;
	private String idioma;
	private String contenidoCatalan;
	private int numeroLocalizador;
	private int anyoLocalizador;
	private int numeroDisquete;
	private int fechaSistema;
	private int horaSistema;
	private String usuario;
	private String codigoIdioma;

	public Salida() {
	}

	public Salida(SalidaId id, int fechaDocumento, String destinatario,
			String contenido, String tipoDocumento, String editLibroSalidas, String nula,
			String destinoGeografico, int fechaSalida, int tipoAgrupacionGeografica, int codigoAgrupacionGeografica,
			int codigoOrganismo, int fechaActualizacion, String codigoEntidad, int numeroEntidad,
			int hora, String idioma, String contenidoCatalan, int numeroLocalizador,
			int anyoLocalizador, int numeroDisquete, int fechaSistema, int horaSistema,
			String usuario, String codigoIdioma) {
		this.id = id;
		this.fechaDocumento = fechaDocumento;
		this.destinatario = destinatario;
		this.contenido = contenido;
		this.tipoDocumento = tipoDocumento;
		this.editLibroSalidas = editLibroSalidas;
		this.nula = nula;
		this.destinoGeografico = destinoGeografico;
		this.fechaSalida = fechaSalida;
		this.tipoAgrupacionGeografica = tipoAgrupacionGeografica;
		this.codigoAgrupacionGeografica = codigoAgrupacionGeografica;
		this.codigoOrganismo = codigoOrganismo;
		this.fechaActualizacion = fechaActualizacion;
		this.codigoEntidad = codigoEntidad;
		this.numeroEntidad = numeroEntidad;
		this.hora = hora;
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
			@AttributeOverride(name = "anyo", column = @Column(name = "FZSANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZSNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZSCAGCO", nullable = false, precision = 2, scale = 0)) })
	public SalidaId getId() {
		return this.id;
	}

	public void setId(SalidaId id) {
		this.id = id;
	}

	@Column(name = "FZSFDOCU", nullable = false, precision = 8, scale = 0)
	public int getFechaDocumento() {
		return this.fechaDocumento;
	}

	public void setFechaDocumento(int fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	@Column(name = "FZSREMIT", nullable = false, length = 30)
	public String getDestinatario() {
		return this.destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	@Column(name = "FZSCONEN", nullable = false, length = 160)
	public String getContenido() {
		return this.contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	@Column(name = "FZSCTIPE", nullable = false, length = 2)
	public String getTipoDocumento() {
		return this.tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Column(name = "FZSCEDIE", nullable = false, length = 1)
	public String getEditLibroSalidas() {
		return this.editLibroSalidas;
	}

	public void setEditLibroSalidas(String editLibroSalidas) {
		this.editLibroSalidas = editLibroSalidas;
	}

	@Column(name = "FZSENULA", nullable = false, length = 1)
	public String getNula() {
		return this.nula;
	}

	public void setNula(String nula) {
		this.nula = nula;
	}

	@Column(name = "FZSPROCE", nullable = false, length = 25)
	public String getDestinoGeografico() {
		return this.destinoGeografico;
	}

	public void setDestinoGeografico(String destinoGeografico) {
		this.destinoGeografico = destinoGeografico;
	}

	@Column(name = "FZSFENTR", nullable = false, precision = 8, scale = 0)
	public int getFechaSalida() {
		return this.fechaSalida;
	}

	public void setFechaSalida(int fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	@Column(name = "FZSCTAGG", nullable = false, precision = 2, scale = 0)
	public int getTipoAgrupacionGeografica() {
		return this.tipoAgrupacionGeografica;
	}

	public void setTipoAgrupacionGeografica(int tipoAgrupacionGeografica) {
		this.tipoAgrupacionGeografica = tipoAgrupacionGeografica;
	}

	@Column(name = "FZSCAGGE", nullable = false, precision = 3, scale = 0)
	public int getCodigoAgrupacionGeografica() {
		return this.codigoAgrupacionGeografica;
	}

	public void setCodigoAgrupacionGeografica(int codigoAgrupacionGeografica) {
		this.codigoAgrupacionGeografica = codigoAgrupacionGeografica;
	}

	@Column(name = "FZSCORGA", nullable = false, precision = 4, scale = 0)
	public int getCodigoOrganismo() {
		return this.codigoOrganismo;
	}

	public void setCodigoOrganismo(int codigoOrganismo) {
		this.codigoOrganismo = codigoOrganismo;
	}

	@Column(name = "FZSFACTU", nullable = false, precision = 8, scale = 0)
	public int getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(int fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	@Column(name = "FZSCENTI", nullable = false, length = 7)
	public String getCodigoEntidad() {
		return this.codigoEntidad;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	@Column(name = "FZSNENTI", nullable = false, precision = 3, scale = 0)
	public int getNumeroEntidad() {
		return this.numeroEntidad;
	}

	public void setNumeroEntidad(int numeroEntidad) {
		this.numeroEntidad = numeroEntidad;
	}

	@Column(name = "FZSHORA", nullable = false, precision = 4, scale = 0)
	public int getHora() {
		return this.hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	@Column(name = "FZSCIDIO", nullable = false, length = 1)
	public String isIdioma() {
		return this.idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	@Column(name = "FZSCONE2", nullable = false, length = 160)
	public String getContenidoCatalan() {
		return this.contenidoCatalan;
	}

	public void setContenidoCatalan(String contenidoCatalan) {
		this.contenidoCatalan = contenidoCatalan;
	}

	@Column(name = "FZSNLOC", nullable = false, precision = 6, scale = 0)
	public int getNumeroLocalizador() {
		return this.numeroLocalizador;
	}

	public void setNumeroLocalizador(int numeroLocalizador) {
		this.numeroLocalizador = numeroLocalizador;
	}

	@Column(name = "FZSALOC", nullable = false, precision = 4, scale = 0)
	public int getAnyoLocalizador() {
		return this.anyoLocalizador;
	}

	public void setAnyoLocalizador(int anyoLocalizador) {
		this.anyoLocalizador = anyoLocalizador;
	}

	@Column(name = "FZSNDIS", nullable = false, precision = 5, scale = 0)
	public int getNumeroDisquete() {
		return this.numeroDisquete;
	}

	public void setNumeroDisquete(int numeroDisquete) {
		this.numeroDisquete = numeroDisquete;
	}

	@Column(name = "FZSFSIS", nullable = false, precision = 8, scale = 0)
	public int getFechaSistema() {
		return this.fechaSistema;
	}

	public void setFechaSistema(int fechaSistema) {
		this.fechaSistema = fechaSistema;
	}

	@Column(name = "FZSHSIS", nullable = false, precision = 8, scale = 0)
	public int getHoraSistema() {
		return this.horaSistema;
	}

	public void setHoraSistema(int horaSistema) {
		this.horaSistema = horaSistema;
	}

	@Column(name = "FZSCUSU", nullable = false, length = 10)
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "FZSCIDI", nullable = false, length = 1)
	public String getCodigoIdioma() {
		return this.codigoIdioma;
	}

	public void setCodigoIdioma(String codigoIdioma) {
		this.codigoIdioma = codigoIdioma;
	}

}
