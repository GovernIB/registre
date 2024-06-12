package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by Fundació Bit on 05/05/16
 *
 * @author anadal
 * @author earrivi
 */
@Entity
@Table(name = "RWE_PROPIEDADGLOBAL",
        indexes = {
                @Index(name = "RWE_PROPIE_ENTIDA_FK_I", columnList = "ENTIDAD"),
        },
        uniqueConstraints = {
                @UniqueConstraint(name="RWE_PROPIEDADGLOBAL_CLA_ENT_UK", columnNames = {"CLAVE", "ENTIDAD"
                })
        })
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class PropiedadGlobal implements Serializable {

    private Long id;
    private String clave;
    private String valor;
    private String descripcion;
    private Long entidad;
    private Long tipo;

    public PropiedadGlobal() {

    }

    public PropiedadGlobal(Long idEntidad) {
        this.entidad = idEntidad;
    }

    public PropiedadGlobal(String clave, String valor, String descripcion, Long entidad, Long tipo) {
        this.clave = clave;
        this.valor = valor;
        this.descripcion = descripcion;
        this.entidad = entidad;
        this.tipo = tipo;
    }

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "CLAVE", nullable = false, length = 255)
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Column(name = "VALOR", length = 2048)
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Column(name = "DESCRIPCION", length = 255)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "ENTIDAD", length = 50)
    public Long getEntidad() {
        return entidad;
    }

    public void setEntidad(Long entidad) {
        this.entidad = entidad;
    }

    @Column(name = "TIPO", length = 50)
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @Transient
    private Integer pageNumber;

    @Transient
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Transient
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Transient
    public String getValorCorto() {

        String valorCorto = getValor();

        if (valorCorto.length() > 40) {
            valorCorto = getValor().substring(0, 40) + "...";
        }

        return valorCorto;
    }

    @Transient
    public String getClaveCorto() {

        String claveCorto = getClave();

        if (claveCorto.length() > 40) {
            claveCorto = getClave().substring(0, 40) + "...";
        }

        return claveCorto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropiedadGlobal that = (PropiedadGlobal) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}