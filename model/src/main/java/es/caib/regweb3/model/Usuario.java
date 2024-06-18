package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.caib.regweb3.utils.RegwebConstantes;

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
@SequenceGenerator(name = "generator", sequenceName = "RWE_USUARIO_SEQ", allocationSize = 1)
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
    private Boolean rwe_ws_entrada = false;
    @XmlTransient
    private Boolean rwe_ws_salida = false;
    @XmlTransient
    private Boolean rwe_ws_ciudadano = false;
    @XmlTransient
    private Boolean dib_user_rw = false;

    @XmlTransient
    private Long idioma;

    /**
     *
     */
    public Usuario() {
        super();
    }

    public Usuario(Long id) {
        this.id = id;
    }

    /**
     * @param id
     * @param identificador
     */
    public Usuario(Long id, String identificador) {
        this.id = id;
        this.identificador = identificador;
    }

    public Usuario(String identificador, String nombre, String apellido1,
                   String apellido2, String documento, Long tipo, String mail) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.documento = documento;
        this.tipoUsuario = tipo;
        this.email = mail;
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
    public Long getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Long tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    @Column(name = "IDIOMA")
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

    @Column(name = "RWE_WS_ENTRADA", nullable = false)
    public Boolean getRwe_ws_entrada() {
        return rwe_ws_entrada;
    }

    public void setRwe_ws_entrada(Boolean rwe_ws_entrada) {
        this.rwe_ws_entrada = rwe_ws_entrada;
    }

    @Column(name = "RWE_WS_SALIDA", nullable = false)
    public Boolean getRwe_ws_salida() {
        return rwe_ws_salida;
    }

    public void setRwe_ws_salida(Boolean rwe_ws_salida) {
        this.rwe_ws_salida = rwe_ws_salida;
    }

    @Column(name = "RWE_WS_CIUDADANO", nullable = false)
    public Boolean getRwe_ws_ciudadano() {
        return rwe_ws_ciudadano;
    }

    public void setRwe_ws_ciudadano(Boolean rwe_ws_ciudadano) {
        this.rwe_ws_ciudadano = rwe_ws_ciudadano;
    }

    @Column(name = "DIB_USER_RW", nullable = false)
    public Boolean getDib_user_rw() {
        return dib_user_rw;
    }

    public void setDib_user_rw(Boolean dib_user_rw) {
        this.dib_user_rw = dib_user_rw;
    }

    @Transient
    public void setRoles(List<Rol> roles) {

        if(roles != null){
            setRwe_superadmin(roles.contains(new Rol(RegwebConstantes.RWE_SUPERADMIN)));
            setRwe_admin(roles.contains(new Rol(RegwebConstantes.RWE_ADMIN)));
            setRwe_usuari(roles.contains(new Rol(RegwebConstantes.RWE_USUARI)));
            setRwe_ws_entrada(roles.contains(new Rol(RegwebConstantes.RWE_WS_ENTRADA)));
            setRwe_ws_salida(roles.contains(new Rol(RegwebConstantes.RWE_WS_SALIDA)));
            setRwe_ws_ciudadano(roles.contains(new Rol(RegwebConstantes.RWE_WS_CIUDADANO)));
            setDib_user_rw(roles.contains(new Rol(RegwebConstantes.DIB_USER_RW)));
        }else{
            setRwe_superadmin(false);
            setRwe_admin(false);
            setRwe_usuari(false);
            setRwe_ws_entrada(false);
            setRwe_ws_salida(false);
            setRwe_ws_ciudadano(false);
            setDib_user_rw(false);
        }
    }

    @Transient
    public String getNombreCompleto() {

        String nombreCompleto = getNombre();
        if (getApellido1() != null) {
            nombreCompleto = nombreCompleto + " " + getApellido1();
        }
        if (getApellido2() != null) {
            nombreCompleto = nombreCompleto + " " + getApellido2();
        }

        return nombreCompleto;
    }

    @Transient
    public String getNombreIdentificador() {

        return getNombreCompleto() + " (" + getIdentificador() + ")";
    }

    @Override
    public String toString() {
        if (id != null) {
            return id.toString();
        } else {
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
