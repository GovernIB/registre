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
@Table(name = "RWE_REMESA_ANEXO")
@org.hibernate.annotations.Table(appliesTo = "RWE_REMESA_ANEXO", indexes = {
    @Index(name="RWE_REMESA_ANEXO_ENLAC_I", columnNames = {"enlace"}),
    @Index(name="RWE_REMESA_ANEXO_REFER_I", columnNames = {"REFERENCIA"}),
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class RemesaAnexo implements Serializable {

	private static final long serialVersionUID = 3077948894205536490L;
	
	private Long id;
    private String enlace;
    private byte[] referencia;
    
    private Remesa remesa;

    public RemesaAnexo() { }

	public RemesaAnexo(String enlace, byte[] referencia,
			Remesa remesa) {
		super();
		this.enlace = enlace;
		this.referencia = referencia;
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

    @Column(name = "ENLACE")
	public String getEnlace() {
		return enlace;
	}

	public void setEnlace(String enlace) {
		this.enlace = enlace;
	}

	@Column(name = "REFERENCIA")
	public byte[] getReferencia() {
		return referencia;
	}

	public void setReferencia(byte[] referencia) {
		this.referencia = referencia;
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
