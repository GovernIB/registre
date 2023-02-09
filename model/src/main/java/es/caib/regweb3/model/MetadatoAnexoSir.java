package es.caib.regweb3.model;

import javax.persistence.*;

/**
 * @author mgonzalez
 * @version 1
 * 03/11/2022
 */
@Entity
@Table(name = "RWE_METADATO_ANEXO_SIR")
@SequenceGenerator(name = "generator", sequenceName = "RWE_MTDANSIR_SEQ", allocationSize = 1)
public class MetadatoAnexoSir extends MetadatoSir {

    protected Long id;
    protected AnexoSir anexoSir;


    public MetadatoAnexoSir() {
    }

    public MetadatoAnexoSir(Long tipo, String campo, String valor) {
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


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AnexoSir.class)
    @JoinColumn(name = "ANEXO_SIR", foreignKey = @ForeignKey(name = "RWE_METANEX_ANEXOSIR_FK"))
    public AnexoSir getAnexoSir() {
        return anexoSir;
    }

    public void setAnexoSir(AnexoSir anexoSir) {
        this.anexoSir = anexoSir;
    }
}
