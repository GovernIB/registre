package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;


/**
 *
 *
 * @author earrivi
 */

@Entity
@Table(name = "RWE_PERMORGUSU")
@org.hibernate.annotations.Table(appliesTo = "RWE_PERMORGUSU", indexes = {
    @Index(name="RWE_POU_ORG_FK_I", columnNames = {"ORGANISMO"}),
    @Index(name="RWE_POU_USUARI_FK_I", columnNames = {"USUARIO"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_POU_SEQ", allocationSize = 1)
public class PermisoOrganismoUsuario implements Serializable {

    private Long id;
    private Long permiso;
    private Organismo organismo;
    private UsuarioEntidad usuario;
    private Boolean activo = false;

    public PermisoOrganismoUsuario() {
    }

    public PermisoOrganismoUsuario(Long idUsuario, String identificador, String nombre, String apellido1,
                               String apellido2, String documento, Long tipo, String mail, Long permiso, Long id) {
        this.usuario = new UsuarioEntidad(idUsuario,identificador, nombre, apellido1, apellido2, documento, tipo, mail);
        this.permiso = permiso;
        this.id = id;
    }

    public PermisoOrganismoUsuario(Long id, Boolean activo, Long idUsuario, Long permiso) {
        this.id = id;
        this.activo = activo;
        this.usuario = new UsuarioEntidad(idUsuario);
        this.permiso = permiso;
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

    
    @Column(name="PERMISO")
    public Long getPermiso() {
      return permiso;
    }

    public void setPermiso(Long permiso) {
      this.permiso = permiso;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ORGANISMO")
    @ForeignKey(name="RWE_POU_ORG_FK")
    public Organismo getOrganismo() {
      return organismo;
    }

    public void setOrganismo(Organismo organismo) {
      this.organismo = organismo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USUARIO")
    @ForeignKey(name="RWE_POU_USUENT_FK")
    public UsuarioEntidad getUsuario() {
      return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
      this.usuario = usuario;
    }

    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

        PermisoOrganismoUsuario that = (PermisoOrganismoUsuario) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }


}
