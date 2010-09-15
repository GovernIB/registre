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
@Table(name = "BZENLPD")
@org.hibernate.annotations.Table(appliesTo = "BZENLPD", comment = "LogEntradaLopd")
public class LogEntradaLopd implements java.io.Serializable {

	private LogEntradaLopdId id;

	public LogEntradaLopd() {
	}

	public LogEntradaLopd(LogEntradaLopdId id) {
		this.id = id;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "tipoAcceso", column = @Column(name = "FZTTIPAC", nullable = false, length = 10)),
			@AttributeOverride(name = "usuario", column = @Column(name = "FZTCUSU", nullable = false, length = 10)),
			@AttributeOverride(name = "fecha", column = @Column(name = "FZTDATAC", nullable = false, precision = 8, scale = 0)),
			@AttributeOverride(name = "hora", column = @Column(name = "FZTHORAC", nullable = false, precision = 9, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "FZTNUMEN", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "anyo", column = @Column(name = "FZTANOEN", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "oficina", column = @Column(name = "FZTCAGCO", nullable = false, precision = 2, scale = 0)) })
	public LogEntradaLopdId getId() {
		return this.id;
	}

	public void setId(LogEntradaLopdId id) {
		this.id = id;
	}

}
