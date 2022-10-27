package es.caib.regweb3.model;

import javax.persistence.*;

/**
 * @author mgonzalez
 * @version 1
 * 26/10/2022
 */
@Entity
@Table(name = "RWE_METADATO_REGSAL")
@SequenceGenerator(name = "generator", sequenceName = "RWE_MTDRS_SEQ", allocationSize = 1)
public class MetadatosRegistroSalida extends Metadato {


    protected Long id;
    protected RegistroSalida registroSalida;


    public MetadatosRegistroSalida() {
    }

    public MetadatosRegistroSalida(Long tipo, String campo, String valor) {
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



    @ManyToOne(fetch = FetchType.EAGER, targetEntity = RegistroSalida.class)
    @JoinColumn(name = "REGISTRO_SALIDA", nullable = false, foreignKey =@ForeignKey(name = "RWE_METRSAL_REGSAL_FK"))
    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
    }


}
