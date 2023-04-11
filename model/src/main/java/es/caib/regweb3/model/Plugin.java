package es.caib.regweb3.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by Fundació Bit on 05/05/16
 *
 * @author anadal
 * @author earrivi
 */
@Entity
@Table(name = "RWE_PLUGIN", indexes = {@Index(name = "RWE_PLUGI_ENTIDA_FK_I", columnList = "ENTIDAD")})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Plugin implements Serializable {

    private Long id;
    private String nombre;
    private String descripcion;
    private String clase;
    private String propiedadesAdmin;
    private String propiedadesEntidad;
    private Boolean activo = true;
    private Long entidad;
    private Long tipo;

    public Plugin() {

    }

    public Plugin(String nombre, String descripcion, String clase, Boolean activo, Long entidad, Long tipo,String propiedadesAdmin, String propiedadesEntidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.clase = clase;
        this.propiedadesAdmin = propiedadesAdmin;
        this.propiedadesEntidad = propiedadesEntidad;
        this.activo = activo;
        this.entidad = entidad;
        this.tipo = tipo;
    }

    public Plugin(String clase) {
        this.clase = clase;
    }

    public Plugin(Long idEntidad) {
        this.entidad = idEntidad;
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

    @Column(name = "NOMBRE", length = 255, nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "DESCRIPCION", length = 2000, nullable = false)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "CLASE", length = 1000, nullable = false)
    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "PROPIEDADES_ADMIN", length = 2147483647)
    public String getPropiedadesAdmin() {
        return propiedadesAdmin;
    }

    public void setPropiedadesAdmin(String propiedadesAdmin) {
        this.propiedadesAdmin = propiedadesAdmin;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "PROPIEDADES_ENTIDAD", length = 2147483647)
    public String getPropiedadesEntidad() {
        return propiedadesEntidad;
    }

    public void setPropiedadesEntidad(String propiedadesEntidad) {
        this.propiedadesEntidad = propiedadesEntidad;
    }

    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plugin that = (Plugin) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}