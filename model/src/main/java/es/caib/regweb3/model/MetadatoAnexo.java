package es.caib.regweb3.model;

import javax.persistence.*;

/**
 * @author mgonzalez
 * @version 1
 * 26/10/2022
 */
@Entity
@Table(name = "RWE_METADATO_ANEXO")
@SequenceGenerator(name = "generator", sequenceName = "RWE_MTDAN_SEQ", allocationSize = 1)
public class MetadatoAnexo extends Metadato {

    protected Long id;
    protected Anexo anexo;


    public MetadatoAnexo() {
    }

    public MetadatoAnexo(Long tipo, String campo, String valor) {
        super(tipo, campo, valor);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @Column(name = "ID", unique = true)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Anexo.class)
    @JoinColumn(name = "ANEXO", foreignKey = @ForeignKey(name = "RWE_METANEX_ANEXO_FK"))
    public Anexo getAnexo() {
        return anexo;
    }

    public void setAnexo(Anexo anexo) {
        this.anexo = anexo;
    }



}
