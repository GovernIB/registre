package es.caib.regweb.model;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BHAGECO")
public class OficinaHistorico implements java.io.Serializable {

	private OficinaHistoricoId id;
    private String nombre;
	private int fechaBaja;

	public OficinaHistorico() {
	}

    public OficinaHistorico(OficinaHistoricoId id, String nombre, int fechaBaja) {
        this.id = id;
        this.nombre = nombre;
        this.fechaBaja = fechaBaja;
    }
    
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "codigo", column = @Column(name = "FHACAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "fechaAlta", column = @Column(name = "FHAFALTA", nullable = false, precision = 8, scale = 0)) })
	public OficinaHistoricoId getId() {
		return this.id;
	}

	public void setId(OficinaHistoricoId id) {
		this.id = id;
	}

    @Column(name = "FHADAGCO", nullable = false, length = 20)
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "FHAFBAJA", nullable = false, precision = 8, scale = 0)
    public int getFechaBaja() {
        return this.fechaBaja;
    }

    public void setFechaBaja(int fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
    
}
