package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Entity
@Table(name = "RWE_REPRO")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Plantilla implements Serializable {

    private Long id;
    private Long tipoRegistro;
    private String nombre;
    private String repro;
    private UsuarioEntidad usuario;
    private Boolean activo = true;
    private int orden;

    public Plantilla() {
    }

    public Plantilla(Long id, String nombre) {
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

    @Column(name = "TIPOREGISTRO", nullable = false)
    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "REPRO")
    @JsonIgnore
    public String getRepro() {
        return repro;
    }

    public void setRepro(String repro) {
        this.repro = repro;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIOENTIDAD", foreignKey = @ForeignKey(name = "RWE_REPRO_USUARIO_FK"))
    @JsonIgnore
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }

    @Column(name = "ACTIVO", nullable = false)
    @JsonIgnore
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Column(name = "ORDEN", nullable = false)
    @JsonIgnore
    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
