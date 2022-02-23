package es.caib.regweb3.model;


import javax.persistence.*;

/**
 * Created 8/04/14 13:31
 *
 * @author mgonzalez
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_MODELO_RECIBO", indexes = {
        @Index(name = "RWE_MODREB_ENTIDA_FK_I", columnList = "ENTIDAD"),
        @Index(name = "RWE_MODREB_ARCHIV_FK_I", columnList = "MODELO")})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class ModeloRecibo extends Traducible {

    private Long id;
    private String nombre;
    private Entidad entidad;
    private Archivo modelo;

    public ModeloRecibo() {
    }

    public ModeloRecibo(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey =@ForeignKey(name = "RWE_MODELRECIBO_ENTIDAD_FK"))
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "MODELO", foreignKey =@ForeignKey(name = "RWE_MODELRECIBO_ARCHIVO_FK"))
    public Archivo getModelo() {
        return modelo;
    }

    public void setModelo(Archivo modelo) {
        this.modelo = modelo;
    }

}
