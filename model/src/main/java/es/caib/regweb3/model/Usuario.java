package es.caib.regweb3.model;

import es.caib.regweb3.utils.RegwebConstantes;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 * Date: 6/02/14
 */
@Entity
@Table(name = "RWE_USUARIO")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "usuario")
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuario implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String nombre;
    @XmlElement
    private String apellido1;
    @XmlElement
    private String apellido2;
    @XmlTransient
    private String documento;
    @XmlTransient
    private String email;
    @XmlTransient
    private String identificador;
    @XmlTransient
    private Long tipoUsuario;
    @XmlTransient
    private Boolean rwe_superadmin = false;
    @XmlTransient
    private Boolean rwe_admin = false;
    @XmlTransient
    private Boolean rwe_usuari = false;
    @XmlTransient
    private Long idioma;

    /**
     *
     */
    public Usuario() {
        super();
    }

    public Usuario(Long id) {
        this.id=id;
    }

    /**
     * @param id
     * @param identificador
     */
    public Usuario(Long id, String identificador) {
        this.id = id;
        this.identificador = identificador;
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

    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "APELLIDO1")
    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    @Column(name = "APELLIDO2")
    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    @Column(name = "DOCUMENTO")
    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Column(name = "EMAIL", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "IDENTIFICADOR", nullable = false, unique = true)
    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    

    @Column(name = "TIPOUSUARIO")    
    @JsonIgnore
    public Long getTipoUsuario() { return tipoUsuario; }
    
    public void setTipoUsuario(Long tipoUsuario) { this.tipoUsuario = tipoUsuario; }




    @Column(name="IDIOMA")
    public Long getIdioma() {
        return idioma;
    }

    public void setIdioma(Long idioma) {
        this.idioma = idioma;
    }

   

    @Column(name = "RWE_SUPERADMIN", nullable = false)
    public Boolean getRwe_superadmin() {
        return rwe_superadmin;
    }

    public void setRwe_superadmin(Boolean rwe_superadmin) {
        this.rwe_superadmin = rwe_superadmin;
    }

    @Column(name = "RWE_ADMIN", nullable = false)
    public Boolean getRwe_admin() {
        return rwe_admin;
    }

    public void setRwe_admin(Boolean rwe_admin) {
        this.rwe_admin = rwe_admin;
    }


    @Column(name = "RWE_USUARI", nullable = false)
    public Boolean getRwe_usuari() {
        return rwe_usuari;
    }

    public void setRwe_usuari(Boolean rwe_usuari) {
        this.rwe_usuari = rwe_usuari;
    }


    @Transient
    public void setRoles(List<Rol> roles){

        if(roles.contains(new Rol(RegwebConstantes.ROL_SUPERADMIN))){
            setRwe_superadmin(true);
        }else{
            setRwe_superadmin(false);
        }

        if(roles.contains(new Rol(RegwebConstantes.ROL_ADMIN))){
            setRwe_admin(true);
        }else{
            setRwe_admin(false);
        }

        if(roles.contains(new Rol(RegwebConstantes.ROL_USUARI))){
            setRwe_usuari(true);
        }else{
            setRwe_usuari(false);
        }

    }

   @Transient
    public String getNombreCompleto(){

        String nombreCompleto = getNombre();
        if(getApellido1() != null){
            nombreCompleto = nombreCompleto + " " + getApellido1();
        }
        if(getApellido2() != null){
            nombreCompleto = nombreCompleto + " " + getApellido2();
        }

        return nombreCompleto;
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

        Usuario usuario = (Usuario) o;

        if (id != null ? !id.equals(usuario.id) : usuario.id != null) return false;

        return true;
    }

}
