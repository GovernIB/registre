package es.caib.regweb3.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created 14/10/14 9:06
 *
 * @author mgonzalez
 */
@Entity
@Table(name = "RWE_PENDIENTE")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Pendiente implements Serializable {

    private Long id;
    private Entidad entidad;
    private Long idOrganismo;
    private Boolean procesado;
    private String estado;
    private Date fecha;

    public Pendiente() {
    }

    public Pendiente(Entidad entidad, Long idOrganismo, Boolean procesado, String estado) {
        this.entidad = entidad;
        this.idOrganismo = idOrganismo;
        this.procesado = procesado;
        this.estado = estado;
        this.fecha = new Date();
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_PENDIE_ENTIDAD_FK"))
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "IDORGANISMO")
    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    @Column(name = "PROCESADO")
    public Boolean getProcesado() {
        return procesado;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    @Column(name = "ESTADO")
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
