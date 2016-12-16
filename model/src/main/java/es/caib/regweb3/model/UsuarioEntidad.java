package es.caib.regweb3.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_USUARIO_ENTIDAD")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "usuarioEntidad")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsuarioEntidad implements Serializable{

    @XmlAttribute
    private Long id;
    @XmlElement
    private Usuario usuario;
    @XmlTransient
    private Entidad entidad;
    @XmlTransient
    private Boolean activo = true;
    @XmlTransient
    private Oficina ultimaOficina;

    public UsuarioEntidad() {
    }

    public UsuarioEntidad(Long id) {
        this.id = id;
    }

    public UsuarioEntidad(Long id, Usuario usuario, Long idEntidad) {
        this.id = id;
        this.usuario = usuario;
        this.entidad = new Entidad(idEntidad);
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

    @ManyToOne()
    @JoinColumn(name = "USUARIO")
    @ForeignKey(name = "RWE_USUENT_USUARIO_FK")
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @ManyToOne()
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_USUENT_ENTIDAD_FK")
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @ManyToOne()
    @JoinColumn(name = "ULTIMAOFICINA")
    @ForeignKey(name = "RWE_USUENT_OFICINA_FK")
    @JsonIgnore
    public Oficina getUltimaOficina() {
        return ultimaOficina;
    }

    public void setUltimaOficina(Oficina ultimaOficina) {
        this.ultimaOficina = ultimaOficina;
    }

    @Transient
    public String getNombreCompleto(){
        return getUsuario().getNombreCompleto();
    }

    @Override
    public String toString() {
        if(id != null){
            return id.toString();
        }else{
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioEntidad usuarioEntidad = (UsuarioEntidad) o;

        if (id != null ? !id.equals(usuarioEntidad.id) : usuarioEntidad.id != null) return false;

        return true;
    }
}
