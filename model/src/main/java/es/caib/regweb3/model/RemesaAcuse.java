package es.caib.regweb3.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Limit Tecnologies S.L.
 * @author Jamal
 */
@Entity
@Table(name = "RWE_REMESA_ACUSE")
@org.hibernate.annotations.Table(appliesTo = "RWE_REMESA_ACUSE", indexes = {
    @Index(name="RWE_REMESA_ACUSE_METAD_I", columnNames = {"METADATOS"}),
    @Index(name="RWE_REMESA_ACUSE_REFER_I", columnNames = {"REFERENCIA"}),
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class RemesaAcuse implements Serializable {

	private static final long serialVersionUID = 3077948894205536490L;
	
	private Long id;
    private String nombre;
    private String mimeType;
    private String metadatos;
    private byte[] referencia;
    private String csvResguardo;
    
    private Remesa remesa;

    public RemesaAcuse() { }

	public RemesaAcuse(String nombre, String mimeType, String metadatos, byte[] referencia, String csvResguardo,
			Remesa remesa) {
		super();
		this.nombre = nombre;
		this.mimeType = mimeType;
		this.metadatos = metadatos;
		this.referencia = referencia;
		this.csvResguardo = csvResguardo;
		this.remesa = remesa;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NOMBRE")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "MIME_TYPE")
	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	@Column(name = "METADATOS")
	public String getMetadatos() {
		return metadatos;
	}

	public void setMetadatos(String metadatos) {
		this.metadatos = metadatos;
	}

	@Column(name = "REFERENCIA")
	public byte[] getReferencia() {
		return referencia;
	}

	public void setReferencia(byte[] referencia) {
		this.referencia = referencia;
	}

	@Column(name = "CSV_RESGUARDO")
	public String getCsvResguardo() {
		return csvResguardo;
	}

	public void setCsvResguardo(String csvResguardo) {
		this.csvResguardo = csvResguardo;
	}

	@ManyToOne(optional = false)
    @JoinColumn(name = "REMESA")
    @ForeignKey(name = "RWE_REMESA_ACUSE_REMESA_FK")
    @JsonIgnore
    public Remesa getRemesa() {
        return remesa;
    }

    public void setRemesa(Remesa remesa) {
        this.remesa = remesa;
    }

	@Transient
    private Integer pageNumber = 1;

    @Transient
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Transient
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    
}
