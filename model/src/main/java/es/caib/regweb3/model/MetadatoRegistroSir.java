package es.caib.regweb3.model;

import javax.persistence.*;

/**
 * @author mgonzalez
 * @version 1
 * 03/11/2022
 */
@Entity
@Table(name = "RWE_METADATO_REGSIR")
@SequenceGenerator(name = "generator", sequenceName = "RWE_MTDRESIR_SEQ", allocationSize = 1)
public class MetadatoRegistroSir extends MetadatoSir {
    protected Long id;
    protected RegistroSir registroSir;


    public MetadatoRegistroSir() {
    }

    public MetadatoRegistroSir(Long tipo, String campo, String valor) {
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

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = RegistroSir.class)
    @JoinColumn(name = "REGISTRO_SIR", foreignKey = @ForeignKey(name = "RWE_METARSIR_REGSIR_FK"))
    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
    }
}
