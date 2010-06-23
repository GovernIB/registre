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
@Table(name = "BZSALPD")
public class LogSalidaLopd implements java.io.Serializable {

	private LogSalidaLopdId id;

	public LogSalidaLopd() {
	}

	public LogSalidaLopd(LogSalidaLopdId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "tipoAcceso", column = @Column(name = "FZUTIPAC", nullable = false, length = 10)),
			@AttributeOverride(name = "usuario", column = @Column(name = "FZUCUSU", nullable = false, length = 10)),
			@AttributeOverride(name = "fecha", column = @Column(name = "FZUDATAC", nullable = false, precision = 8, scale = 0)),
			@AttributeOverride(name = "hora", column = @Column(name = "FZUHORAC", nullable = false, precision = 9, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZUNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "anyo", column = @Column(name = "FZUANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZUCAGCO", nullable = false, precision = 2, scale = 0)) })
	public LogSalidaLopdId getId() {
		return this.id;
	}

	public void setId(LogSalidaLopdId id) {
		this.id = id;
	}

}
