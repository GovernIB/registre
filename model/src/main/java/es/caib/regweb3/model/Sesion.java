/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 31/10/19
 */
@Table(name = "RWE_SESION", indexes = {
        @Index(name = "RWE_SESION_USUENT_FK_I", columnList = "USUARIO")
})
@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_SESION_SEQ", allocationSize = 1)
public class Sesion implements Serializable {

    private Long id;
    private Long idSesion;
    private Date fecha;
    private UsuarioEntidad usuario;
    private Long estado;
    private Long tipoRegistro;
    private String numeroRegistro;

    public Sesion() {
    }

    public Sesion(Long idSesion, UsuarioEntidad usuario, Long estado) {
        this.fecha = new Date();
        this.idSesion = idSesion;
        this.usuario = usuario;
        this.estado = estado;
    }


    @Column(name = "ID", nullable = false, length = 3)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "IDSESION", nullable = false)
    public Long getIdSesion() {
        return idSesion;
    }

    public void setIdSesion(Long idSesion) {
        this.idSesion = idSesion;
    }

    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "USUARIO", foreignKey = @ForeignKey(name = "RWE_SESION_USUENT_FK"))
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }

    @Column(name = "ESTADO")
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Column(name = "TIPO_REGISTRO")
    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @Column(name = "NUMREG")
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

}
