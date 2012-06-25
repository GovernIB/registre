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
@Table(name = "BZHISEMAIL")
@org.hibernate.annotations.Table(appliesTo = "BZHISEMAIL", comment = "Modificacion")
public class HistoricoEmails implements java.io.Serializable {

	private HistoricoEmailsId id;
	private String emailDestinatario;
	private String codigoUsuario;
	private String tipusEmail;
	private String fecha;
	private String hora;

	public HistoricoEmails() {
	}

	public HistoricoEmails(HistoricoEmailsId id,String tipusEmail, String fecha,  String hora, String codigoUsuario, String emailDestinatario) {
		this.id = id;
		this.tipusEmail = tipusEmail;
		this.fecha = fecha;
		this.hora = hora;
		this.codigoUsuario = codigoUsuario;
		this.emailDestinatario = emailDestinatario;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigoEntradaSalida", column = @Column(name = "BHE_TIPUS", nullable = false, length = 1)),
			@AttributeOverride(name = "anyoEntradaSalida", column = @Column(name = "BHE_ANY", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "numeroEntradaSalida", column = @Column(name = "BHE_NUMREG", nullable = false, precision = 5, scale = 0)),
			@AttributeOverride(name = "oficinaEntradaSalida", column = @Column(name = "BHE_CODIOFI", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "numero", column = @Column(name = "BHE_NUM", nullable = false, precision = 3, scale = 0))}
			)
	public HistoricoEmailsId getId() {
		return this.id;
	}

	public void setId(HistoricoEmailsId id) {
		this.id = id;
	}

	@Column(name = "BHE_TIPUSMAIL", nullable = false, length = 1)
	public String getTipusEmail() {
		return this.tipusEmail;
	}

	public void setTipusEmail(String tipusEmail) {
		this.tipusEmail = tipusEmail;
	}

	@Column(name = "BHE_HORA", nullable = false, length = 5)
	public String getHora() {
		return this.hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	@Column(name = "BHE_DATA", nullable = false, length = 10)
	public String getFecha() {
		return this.fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	@Column(name = "BHE_EMAIL", nullable = false, length = 50)
	public String getEmailDestinatario() {
		return emailDestinatario;
	}


	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}

	@Column(name = "BHE_CODUSU", nullable = false, length = 10)
	public String getCodigoUsuario() {
		return codigoUsuario;
	}


	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
}
