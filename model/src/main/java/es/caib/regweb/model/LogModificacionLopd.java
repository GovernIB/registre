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
@Table(name = "BZMOLPD")
public class LogModificacionLopd implements java.io.Serializable {

	private LogModificacionLopdId id;

	public LogModificacionLopd() {
	}

	public LogModificacionLopd(LogModificacionLopdId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "tipoAcceso", column = @Column(name = "FZVTIPAC", nullable = false, length = 10)),
			@AttributeOverride(name = "usuario", column = @Column(name = "FZVCUSU", nullable = false, length = 10)),
			@AttributeOverride(name = "fecha", column = @Column(name = "FZVDATAC", nullable = false, precision = 8, scale = 0)),
			@AttributeOverride(name = "hora", column = @Column(name = "FZVHORAC", nullable = false, precision = 9, scale = 0)),
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "FZVCENSA", nullable = false, length = 1)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZVNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "anyo", column = @Column(name = "FZVANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZVCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "fechaModificacion", column = @Column(name = "FZVFMODI", nullable = false, precision = 8, scale = 0)),
			@AttributeOverride(name = "horaModificacion", column = @Column(name = "FZVHMODI", nullable = false, precision = 8, scale = 0)) })
	public LogModificacionLopdId getId() {
		return this.id;
	}

	public void setId(LogModificacionLopdId id) {
		this.id = id;
	}

}
