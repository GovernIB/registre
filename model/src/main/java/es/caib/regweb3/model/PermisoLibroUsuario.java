package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created 11/03/14 14:30
 *
 * @author mgonzalez
 * @author anadal (index)
 */

@Entity
@Table(name = "RWE_PERMLIBUSU")
@org.hibernate.annotations.Table(appliesTo = "RWE_PERMLIBUSU", indexes = {
    @Index(name="RWE_PELIUS_LIBRO_FK_I", columnNames = {"LIBRO"}),
    @Index(name="RWE_PELIUS_USUARI_FK_I", columnNames = {"USUARIO"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class PermisoLibroUsuario implements Serializable {

    private Long id;
    private Long permiso;
    private Libro libro;
    private UsuarioEntidad usuario;
    private Boolean activo = false;

    public PermisoLibroUsuario() {
    }

    public PermisoLibroUsuario(Long id, Boolean activo, Long idUsuario) {
        this.id = id;
        this.activo = activo;
        this.usuario = new UsuarioEntidad(idUsuario);
    }

    public PermisoLibroUsuario(Long idUsuario, String identificador, String nombre, String apellido1,
                               String apellido2, String documento, Long tipo, String mail, Long permiso) {
        this.usuario = new UsuarioEntidad(idUsuario,identificador, nombre, apellido1, apellido2, documento, tipo, mail);
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

    @ManyToOne()
    @JoinColumn(name="LIBRO")
    @ForeignKey(name="RWE_PERMLIBUSU_LIBRO_FK")
    public Libro getLibro() {
      return libro;
    }

    public void setLibro(Libro libro) {
      this.libro = libro;
    }

    @ManyToOne()
    @JoinColumn(name="USUARIO")
    @ForeignKey(name="RWE_PERMLIBUSU_USUENT_FK")
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

        PermisoLibroUsuario that = (PermisoLibroUsuario) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }


}
