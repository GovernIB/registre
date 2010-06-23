package es.caib.regweb.model;



import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumns;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**

 */
@Entity
@Table(name = "BAGRUGE")
public class AgrupacionGeografica implements java.io.Serializable {

	private AgrupacionGeograficaId id;
	private String nombre;
	private int fechaBaja;
	private AgrupacionGeografica padre;

	public AgrupacionGeografica() {
	}

	public AgrupacionGeografica(AgrupacionGeograficaId id, String nombre,
			int fechaBaja, AgrupacionGeografica padre) {
		this.id = id;
		this.nombre = nombre;
		this.fechaBaja = fechaBaja;
		this.padre = padre;
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "tipo", column = @Column(name = "FABCTAGG", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigo", column = @Column(name = "FABCAGGE", nullable = false, precision = 3, scale = 0)) })
	public AgrupacionGeograficaId getId() {
		return this.id;
	}

	public void setId(AgrupacionGeograficaId id) {
		this.id = id;
	}

	@Column(name = "FABDAGGE", nullable = false, length = 30)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "FABFBAJA", nullable = false, precision = 6, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumns ({
        @JoinColumn(name="FABCTASU", referencedColumnName = "FABCTAGG"),
        @JoinColumn(name="FABCAGSU", referencedColumnName = "FABCAGGE")
    })
	public AgrupacionGeografica getPadre() {
		return this.padre;
	}

	public void setPadre(AgrupacionGeografica padre) {
		this.padre = padre;
	}
}
