package es.caib.regweb3.model;

import javax.persistence.*;

/**
 * @author mgonzalez
 * @version 1
 * 26/10/2022
 */

@Entity
@Table(name = "RWE_METADATO_REGENT")
@SequenceGenerator(name = "generator", sequenceName = "RWE_MTDRE_SEQ", allocationSize = 1)
public class MetadatosRegistroEntrada extends Metadato {


    protected Long id;
    protected RegistroEntrada registroEntrada;


    public MetadatosRegistroEntrada() {
    }

    public MetadatosRegistroEntrada(Long tipo, String campo, String valor) {
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

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = RegistroEntrada.class)
    @JoinColumn(name = "REGISTRO_ENTRADA", nullable = false, foreignKey =@ForeignKey(name = "RWE_METAREN_REGENT_FK"))
    public RegistroEntrada getRegistroEntrada() {
        return registroEntrada;
    }

    public void setRegistroEntrada(RegistroEntrada registroEntrada) {
        this.registroEntrada = registroEntrada;
    }


}
