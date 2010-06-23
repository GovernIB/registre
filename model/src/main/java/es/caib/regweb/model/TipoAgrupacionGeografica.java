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
@Table(name = "BTIPAGR")
public class TipoAgrupacionGeografica implements java.io.Serializable {

	private TipoAgrupacionGeograficaId id;
	private String nombre;
	private int tipoPadre;

	public TipoAgrupacionGeografica() {
	}

	public TipoAgrupacionGeografica(TipoAgrupacionGeograficaId id, String nombre, int tipoPadre) {
		this.id = id;
		this.nombre = nombre;
		this.tipoPadre = tipoPadre;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "tipo", column = @Column(name = "FLDCTAGG", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "fechaBaja", column = @Column(name = "FLDFBAJA", nullable = false, precision = 6, scale = 0)) })
	public TipoAgrupacionGeograficaId getId() {
		return this.id;
	}

	public void setId(TipoAgrupacionGeograficaId id) {
		this.id = id;
	}

	@Column(name = "FLDDTAGG", nullable = false, length = 30)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "FLDCTAGS", nullable = false, precision = 2, scale = 0)
	public int getTipoPadre() {
		return this.tipoPadre;
	}

	public void setTipoPadre(int tipoPadre) {
		this.tipoPadre = tipoPadre;
	}

}
