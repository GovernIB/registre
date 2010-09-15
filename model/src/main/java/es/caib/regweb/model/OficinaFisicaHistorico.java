package es.caib.regweb.model;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BHOFIFIS")
@org.hibernate.annotations.Table(appliesTo = "BHOFIFIS", comment = "OficinaFisicaHistorico")
public class OficinaFisicaHistorico implements java.io.Serializable {

	private OficinaFisicaHistoricoId id;
    private String nombre;
	private int fechaBaja;

	public OficinaFisicaHistorico() {
	}

	public OficinaFisicaHistorico(OficinaFisicaHistoricoId id, String nombre, int fechaBaja) {
		this.id = id;
		this.nombre = nombre;
		this.fechaBaja = fechaBaja;
	}

	@EmbeddedId
	@AttributeOverrides( {
		    @AttributeOverride(name = "codigoOficina", column = @Column(name = "FZHCAGCO", nullable = false, precision = 2, scale = 0)),
			@AttributeOverride(name = "codigoOficinaFisica", column = @Column(name = "OFH_CODI", nullable = false, precision = 4, scale = 0)),
			@AttributeOverride(name = "fechaAlta", column = @Column(name = "OFH_FECALT", nullable = false, precision = 8, scale = 0)) })
	public OficinaFisicaHistoricoId getId() {
		return this.id;
	}

	public void setId(OficinaFisicaHistoricoId id) {
		this.id = id;
	}

    @Column(name = "OFH_NOM", nullable = false, length = 20)
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "OFH_FECBAJ", nullable = false, precision = 8, scale = 0)
    public int getFechaBaja() {
        return this.fechaBaja;
    }

    public void setFechaBaja(int fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
    
}
